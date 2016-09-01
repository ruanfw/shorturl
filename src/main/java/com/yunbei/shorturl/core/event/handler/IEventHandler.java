package com.yunbei.shorturl.core.event.handler;

import java.util.List;

import com.yunbei.shorturl.core.event.Event;
import com.yunbei.shorturl.core.event.EventType;

public interface IEventHandler {

    public void deal(Event event);

    public List<EventType> getHandleEventTypes();

}
