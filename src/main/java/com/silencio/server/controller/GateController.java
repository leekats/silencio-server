package com.silencio.server.controller;

import com.silencio.server.bl.GateBL;
import com.silencio.server.dal.FSLogger;
import com.silencio.server.models.pojos.Detection;
import com.silencio.server.models.pojos.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.util.List;

import static com.silencio.server.models.Consts.CONTROLLER;
import static com.silencio.server.models.Consts.DETECTION;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/gate")
@RestController
public class GateController {
    private final GateBL bl;
    private final FSLogger logger;

    @Autowired
    public GateController(GateBL bl, FSLogger logger) {
        this.bl = bl;
        this.logger = logger;
    }

    @PostMapping(path ="/new")
    public void newDetection(@RequestBody Detection event) {
        bl.newDetection(event);
    }

    @GetMapping(path = "/updates", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<Event>> getUpdates() {
        System.out.println("Client Connected");
        return bl.getUpdates()
                .map(event -> ServerSentEvent.builder(event)
                        .data(event)
                        .event(DETECTION)
                        .id(event.get_id().toHexString())
                        .build())
                .doOnError(logger::error)
                .checkpoint(CONTROLLER)
                .cache();
    }

    @GetMapping(path = "/events")
    public List<Event> getLatestEvents() {
        return bl.getLatestEvents();
    }
}