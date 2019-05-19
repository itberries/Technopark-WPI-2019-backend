package com.itberries.technopark.itberries.responses.models;

import com.itberries.technopark.itberries.models.Event;
import com.itberries.technopark.itberries.models.EventShortDTO;

import java.util.List;

public class EventShortResponse {
    List<EventShortDTO> events;

    public EventShortResponse(List<EventShortDTO> events) {
        this.events = events;
    }

    public List<EventShortDTO> getEvents() {
        return events;
    }

    public void setEvents(List<EventShortDTO> events) {
        this.events = events;
    }
}
