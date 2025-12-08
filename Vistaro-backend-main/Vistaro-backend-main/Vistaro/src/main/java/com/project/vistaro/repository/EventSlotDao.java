package com.project.vistaro.repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import com.project.vistaro.model.EventSlot;

public interface EventSlotDao {

    EventSlot add(EventSlot slot);
    int update(EventSlot slot);
    int deleteById(Integer slotId);
    Optional<EventSlot> findById(Integer slotId);
    List<EventSlot> findByEventId(Integer eventId);
    List<EventSlot> findByCity(String city);
    List<EventSlot> findAll();
    int countOverlappingSlots(Integer venueId, Integer slotId, Timestamp start, Timestamp end);

}

