package com.itberries.technopark.itberries.services.impl;

import com.itberries.technopark.itberries.dao.IEventDao;
import com.itberries.technopark.itberries.models.Event;
import com.itberries.technopark.itberries.models.EventDetailDTO;
import com.itberries.technopark.itberries.models.EventShortDTO;
import com.itberries.technopark.itberries.services.IEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EventServiceImpl implements IEventService {

    private IEventDao iEventDao;

    @Autowired
    public EventServiceImpl(IEventDao iEventDao) {
        this.iEventDao = iEventDao;
    }

    @Override
    public List<EventShortDTO> findAllEvents() {
        List<Event> allEvents = iEventDao.getAllEvents();
        List<EventShortDTO> events = new ArrayList<>();
        for (Event ev : allEvents) {
            events.add(new EventShortDTO(ev.getTitle(), ev.getDate(), ev.getCity(), ev.getPreviewUrl(), ev.getId()));
        }
        return events;
    }


    @Override
    public EventDetailDTO getEventById(Long id) {
        Event eventById = iEventDao.getEventById(id);

        return new EventDetailDTO(eventById.getTitle(),
                eventById.getDate(),
                eventById.getCity(),
                eventById.getImageUrl(),
                eventById.getDescription(),
                eventById.getDetailsUrl());
    }
}
