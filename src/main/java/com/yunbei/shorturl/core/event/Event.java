package com.yunbei.shorturl.core.event;

import java.util.Map;

import com.yunbei.shorturl.core.cache.annotation.Hash;

@Hash(key = "shorturl:event")
public class Event {

    private EventType eventType;

    private int entityId;

    private int entityType;

    private Long gmtCreated;

    private Map<String, Object> params;

    public Event(EventType eventType) {
        this.eventType = eventType;
        this.gmtCreated = System.currentTimeMillis();
    }

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    public int getEntityId() {
        return entityId;
    }

    public void setEntityId(int entityId) {
        this.entityId = entityId;
    }

    public int getEntityType() {
        return entityType;
    }

    public void setEntityType(int entityType) {
        this.entityType = entityType;
    }

    public Long getGmtCreated() {
        return gmtCreated;
    }

    public void setGmtCreated(Long gmtCreated) {
        this.gmtCreated = gmtCreated;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public Event setParams(Map<String, Object> params) {
        this.params = params;
        return this;
    }

    @Override
    public String toString() {
        return "Event [eventType=" + eventType + ", entityId=" + entityId + ", entityType=" + entityType
                + ", gmtCreated=" + gmtCreated + ", params=" + params + "]";
    }

}
