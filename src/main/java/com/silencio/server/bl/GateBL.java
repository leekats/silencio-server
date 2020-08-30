package com.silencio.server.bl;

import com.silencio.server.dal.FSLogger;
import com.silencio.server.dal.GateDAL;
import com.silencio.server.dal.PersonsDAL;
import com.silencio.server.models.pojos.Detection;
import com.silencio.server.models.pojos.Event;
import com.silencio.server.models.pojos.Person;
import org.bson.types.ObjectId;
import org.reactivestreams.Processor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.UnicastProcessor;

import java.util.List;
import java.util.Optional;

import static com.silencio.server.models.Consts.EVENT_GET_UPDATE;
import static com.silencio.server.models.enums.Indication.RED;
import static java.util.Objects.nonNull;

@Service
public class GateBL {
    private final GateDAL dal;
    private final PersonsDAL persons;
    private final FSLogger logger;

    private final Processor<Event, Event> updateUnicastProcessor = UnicastProcessor.create();
    private final Flux<Event> updates = Flux.from(updateUnicastProcessor)
            .replay(0)
            .autoConnect();

    @Autowired
    public GateBL(GateDAL dal, PersonsDAL persons, FSLogger logger) {
        this.dal = dal;
        this.persons = persons;
        this.logger = logger;
    }

    public Flux<Event> getUpdates() {
        return updates
                .doOnError(logger::error)
                .checkpoint(EVENT_GET_UPDATE);
    }

    public void newDetection(Detection detection) {
        final Event event = Event.builder()
                ._id(ObjectId.get())
                .suspects(detection.getSuspects())
                .timestamp(detection.getTimestamp())
                .imageUrl(detection.getImageUrl())
                .cameraId("default-camera")
                .indication(RED)
                .build();

        if (nonNull(detection.getSuspects()) && detection.getSuspects().size() > 0) {
            final String image = detection.getSuspects().get(0).getFace().getExternalImageId();
            final Optional<Person> maybePerson = Optional.ofNullable(persons.getPerson(image.substring(0, image.lastIndexOf("."))));

            maybePerson.ifPresent(person -> {
                event.setPersonId(person.getPersonId());
                event.setIndication(persons.getPersonsIndication(person.getPermits()));
            });
        }

        dal.insertUpdate(event);
        updateUnicastProcessor.onNext(event);
    }

    public List<Event> getLatestEvents() {
        final long thirtyMinutesAgo = System.currentTimeMillis() - 30 * 60 * 1000;
        return dal.getEventsAfterTimeStamp(thirtyMinutesAgo);
    }
}