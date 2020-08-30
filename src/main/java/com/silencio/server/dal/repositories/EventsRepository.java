package com.silencio.server.dal.repositories;

import com.silencio.server.models.pojos.Event;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface EventsRepository extends MongoRepository<Event, String> {
    List<Event> findEventsByTimestampGreaterThan(long timestamp);
}
