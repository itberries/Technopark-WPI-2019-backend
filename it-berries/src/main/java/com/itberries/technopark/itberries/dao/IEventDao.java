package com.itberries.technopark.itberries.dao;

import com.itberries.technopark.itberries.models.Event;

import java.util.List;

public interface IEventDao {

    List<Event> getAllEvents();

    Event getEventById(Long id);
}
