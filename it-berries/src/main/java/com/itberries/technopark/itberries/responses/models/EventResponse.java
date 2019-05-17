package com.itberries.technopark.itberries.responses.models;

import com.itberries.technopark.itberries.models.Event;

import java.util.List;

public class EventResponse {
    List<Event> events;

    public EventResponse(List<Event> events) {
        this.events = events;
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }
}
