package com.itberries.technopark.itberries.services;

import com.itberries.technopark.itberries.models.Event;

import java.util.List;

public interface IEventService {
    List<Event> findAllEvents();
}
