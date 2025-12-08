package com.project.vistaro.service;

import java.util.List;

import com.project.vistaro.model.EventSlot;

public interface EventSlotService {

    EventSlot addEventSlot(EventSlot slot);
    EventSlot updateEventSlot(Integer slotId, EventSlot slot);
    void deleteEventSlot(Integer slotId);
    EventSlot getById(Integer slotId);
    List<EventSlot> getByEventId(Integer eventId);
    List<EventSlot> getByCity(String city);
    List<EventSlot> getAllSlots();
}
