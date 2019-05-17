package com.itberries.technopark.itberries.services.impl;

import com.itberries.technopark.itberries.dao.IEventDao;
import com.itberries.technopark.itberries.models.Event;
import com.itberries.technopark.itberries.services.IEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventServiceImpl implements IEventService {

    IEventDao iEventDao;

    @Autowired
    public EventServiceImpl(IEventDao iEventDao) {
        this.iEventDao = iEventDao;
    }

    @Override
    public List<Event> findAllEvents() {
        return iEventDao.getAllEvents();
    }
}
