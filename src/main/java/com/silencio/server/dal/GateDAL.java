package com.silencio.server.dal;

import com.silencio.server.dal.repositories.EventsRepository;
import com.silencio.server.models.pojos.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GateDAL {
    private EventsRepository db;

    @Autowired
    public GateDAL(EventsRepository db) {
        this.db = db;
    }

    public void insertUpdate(Event event) {
        db.save(event);
    }

    public List<Event> getEventsAfterTimeStamp(long timestamp) {
        return db.findEventsByTimestampGreaterThan(timestamp);
    }
}