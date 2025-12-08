package com.project.vistaro.service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.vistaro.exception.ResourceNotFoundException;
import com.project.vistaro.model.EventSlot;
import com.project.vistaro.model.Seat;
import com.project.vistaro.model.Venue;
import com.project.vistaro.repository.EventSlotDao;
import com.project.vistaro.repository.SeatDao;
import com.project.vistaro.repository.VenueDao;

@Service
public class EventSlotServiceImpl implements EventSlotService {

    private final EventSlotDao eventSlotDao;
    private final VenueDao venueDao;
    private final SeatDao seatDao;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public EventSlotServiceImpl(EventSlotDao eventSlotDao, VenueDao venueDao, SeatDao seatDao) {
        this.eventSlotDao = eventSlotDao;
        this.venueDao = venueDao;
        this.seatDao = seatDao;
    }

    @Override
    public EventSlot addEventSlot(EventSlot slot) {
        EventSlot created = eventSlotDao.add(slot);
//        int overlapCount = eventSlotDao.countOverlappingSlots(
//                slot.getVenueId(),
//                null,                   // during create, slotId is null
//                Timestamp.valueOf(slot.getStartTime()),
//                Timestamp.valueOf(slot.getEndTime())
//        );
//
//
//        if (overlapCount > 0) {
//            throw new RuntimeException("This venue is already occupied during the selected time range.");
//        }
        
        generateSeatsForSlot(created);
        return created;
    }

    private void generateSeatsForSlot(EventSlot slot) {
        Venue venue = venueDao.findById(slot.getVenueId())
                .orElseThrow(() -> new ResourceNotFoundException("Venue not found with id: " + slot.getVenueId()));

        String layoutJson = venue.getSeatLayoutJson();
        if (layoutJson == null || layoutJson.isBlank()) return;

        try {
            Map<String, Object> layout = objectMapper.readValue(layoutJson, Map.class);
            Object rowsObj = layout.get("rows");
            if (!(rowsObj instanceof List)) return;

            List<?> rows = (List<?>) rowsObj;

            for (Object rowObj : rows) {
                if (!(rowObj instanceof Map)) continue;

                Map<?, ?> rowMap = (Map<?, ?>) rowObj;

                String label = String.valueOf(rowMap.get("label"));
                Object countObj = rowMap.get("count");
                Object typeObj = rowMap.get("type");
                Object priceObj = rowMap.get("price");

                if (label == null || countObj == null || typeObj == null || priceObj == null) continue;

                int count = Integer.parseInt(countObj.toString());
                String seatType = typeObj.toString();
                BigDecimal priceDec = new BigDecimal(priceObj.toString());
                BigDecimal price = new BigDecimal(priceDec.toString());

                for (int i = 1; i <= count; i++) {
                    String seatNum = label + i;

                    Seat seat = new Seat();
                    seat.setSlotId(slot.getSlotId());
                    seat.setRowLabel(label);
                    seat.setSeatNumber(seatNum);
                    seat.setSeatType(seatType);
                    seat.setPrice(price);
                    seat.setIsBooked(false);
                    seat.setIsLocked(false);
                    seat.setLockedUntil(null);

                    seatDao.save(seat);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Invalid seat_layout_json for venue id: " + venue.getVenueId(), e);
        }
    }

    @Override
    public EventSlot updateEventSlot(Integer slotId, EventSlot slot) {
        EventSlot existing = eventSlotDao.findById(slotId)
                .orElseThrow(() -> new ResourceNotFoundException("EventSlot not found with id: " + slotId));

        slot.setSlotId(existing.getSlotId());
        eventSlotDao.update(slot);
        return slot;
    }

    @Override
    public void deleteEventSlot(Integer slotId) {
        eventSlotDao.findById(slotId)
                .orElseThrow(() -> new ResourceNotFoundException("EventSlot not found with id: " + slotId));
        eventSlotDao.deleteById(slotId);
    }

    @Override
    public EventSlot getById(Integer slotId) {
        return eventSlotDao.findById(slotId)
                .orElseThrow(() -> new ResourceNotFoundException("EventSlot not found with id: " + slotId));
    }

    @Override
    public List<EventSlot> getByEventId(Integer eventId) {
        return eventSlotDao.findByEventId(eventId);
    }

    @Override
    public List<EventSlot> getByCity(String city) {
        return eventSlotDao.findByCity(city);
    }

    @Override
    public List<EventSlot> getAllSlots() {
        return eventSlotDao.findAll();
    }
}
