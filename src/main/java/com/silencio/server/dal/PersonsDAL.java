package com.silencio.server.dal;

import com.silencio.server.dal.repositories.PersonsRepository;
import com.silencio.server.models.enums.Indication;
import com.silencio.server.models.enums.State;
import com.silencio.server.models.pojos.Permit;
import com.silencio.server.models.pojos.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static com.silencio.server.models.enums.Indication.RED;
import static com.silencio.server.models.enums.State.APPROVED;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Service
public class PersonsDAL {
    private final PersonsRepository db;

    @Autowired
    public PersonsDAL(PersonsRepository db) {
        this.db = db;
    }

    public Person getPerson(String personId) {
        return db.findPersonByPersonId(personId);
    }

    public void addPerson(Person person) {
        db.save(person);
    }

    public void deletePerson(String personId) {
        db.deletePersonByPersonId(personId);
    }

    public boolean addPermit(Permit permit) {
        final Optional<Person> maybePerson = Optional.ofNullable(db.findPersonByPersonId(permit.getPersonId()));
        return maybePerson.map(person -> {
            if (isNull(person.getPermits())) {
                person.setPermits(Collections.singletonList(permit));
            } else {
                person.getPermits().add(permit);
            }
            db.save(person);
            return true;
        }).orElse(false);
    }

    public boolean deletePermit(String personId, String id) {
        final Optional<Person> maybePerson = Optional.ofNullable(db.findPersonByPersonId(personId));
        return maybePerson.map(p -> {
            p.setPermits(p.getPermits().stream()
                    .filter(permit -> !permit.getId().equals(id))
                    .collect(Collectors.toList()));
            db.save(p);
            return true;
        }).orElse(false);
    }

    public List<Permit> getAllPermits() {
        return db.findAll().stream()
                .map(Person::getPermits)
                .filter(Objects::nonNull)
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

    public boolean savePermit(String personId, String permitId, State state, String accepterId) {
        final Optional<Person> maybePerson = Optional.ofNullable(db.findPersonByPersonId(personId));
        return maybePerson
                .filter(p -> nonNull(p.getPermits()))
                .map(person -> {
                    person.getPermits().forEach(permit -> {
                        if (permit.getId().equals(permitId)) {
                            permit.setAccepterId(accepterId);
                            permit.setState(state);
                            db.save(person);
                        }
                    });
                    return true;

                }).orElse(false);
    }

    public Indication getPersonsIndication(List<Permit> permits) {
        if (isNull(permits)) {
            return RED;
        }

        return permits.stream()
                .filter(permit -> permit.getEndAccess() > System.currentTimeMillis())
                .filter(permit -> permit.getState().equals(APPROVED))
                .map(Permit::getIndication)
                .max(Comparator.comparing(Indication::getValue))
                .orElse(RED);
    }

    public List<Person> getAllPersons() {
        return db.findAll();
    }
}