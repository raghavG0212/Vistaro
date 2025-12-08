package com.project.vistaro.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;

import com.project.vistaro.exception.BusinessRuleException;
import com.project.vistaro.exception.ResourceNotFoundException;
import com.project.vistaro.model.Event;
import com.project.vistaro.model.EventCategory;
import com.project.vistaro.model.EventFormat;
import com.project.vistaro.model.EventSlot;
import com.project.vistaro.model.GeneralEventDetails;
import com.project.vistaro.model.UserEventDraft;
import com.project.vistaro.repository.UserEventDraftDao;

@Service
public class UserEventDraftServiceImpl implements UserEventDraftService {

    private final UserEventDraftDao dao;
    private final EventService eventService;
    private final GeneralEventDetailsService generalEventDetailsService;
    private final EventSlotService eventSlotService;

    public UserEventDraftServiceImpl(
            UserEventDraftDao dao,
            EventService eventService,
            GeneralEventDetailsService generalEventDetailsService,
            EventSlotService eventSlotService
    ) {
        this.dao = dao;
        this.eventService = eventService;
        this.generalEventDetailsService = generalEventDetailsService;
        this.eventSlotService = eventSlotService;
    }

    @Override
    public Integer createDraft(UserEventDraft draft) {
        if (draft.getUserId() == null) {
            throw new BusinessRuleException("userId is required for creating a draft");
        }
        if (draft.getTitle() == null || draft.getTitle().isBlank()) {
            throw new BusinessRuleException("Title is required");
        }
        if (draft.getDescription() == null || draft.getDescription().isBlank()) {
            throw new BusinessRuleException("Description is required");
        }
        if (draft.getEventStart() == null || draft.getEventEnd() == null) {
            throw new BusinessRuleException("Event start/end time are required");
        }
        if (draft.getVenueId() == null) {
            throw new BusinessRuleException("Venue is required");
        }

        if (draft.getBasePrice() == null) {
            draft.setBasePrice(BigDecimal.ZERO);
        }

        if (draft.getApprovalStatus() == null) {
            draft.setApprovalStatus("PENDING");
        }

        return dao.save(draft);
    }

    @Override
    public UserEventDraft getDraft(Integer draftId) {
        return dao.findById(draftId)
                .orElseThrow(() -> new ResourceNotFoundException("Draft not found: " + draftId));
    }

    @Override
    public List<UserEventDraft> getDraftsByUser(Integer userId) {
        return dao.findByUserId(userId);
    }

    @Override
    public List<UserEventDraft> getDraftsByStatus(String status) {
        return dao.findAllByStatus(status);
    }

    @Override
    public void updateDraft(UserEventDraft draft) {
        dao.update(draft);
    }

    @Override
    public void deleteDraft(Integer draftId) {
        UserEventDraft d = getDraft(draftId);
        if ("APPROVED".equalsIgnoreCase(d.getApprovalStatus())) {
            throw new BusinessRuleException("Approved drafts cannot be deleted.");
        }
        dao.delete(draftId);
    }

    @Override
    public void approveDraft(Integer draftId, String adminComment) {
        UserEventDraft draft = getDraft(draftId);

        if ("APPROVED".equalsIgnoreCase(draft.getApprovalStatus())) {
            throw new BusinessRuleException("Draft is already approved.");
        }
        if ("REJECTED".equalsIgnoreCase(draft.getApprovalStatus())) {
            throw new BusinessRuleException("Rejected drafts cannot be approved.");
        }

        // 1. Create Event
        Event event = new Event();
        event.setTitle(draft.getTitle());
        event.setDescription(draft.getDescription());
        event.setCategory(EventCategory.EVENT);
        event.setSubCategory(draft.getSubCategory());
        event.setBannerUrl(draft.getBannerUrl());
        event.setThumbnailUrl(draft.getThumbnailUrl());
        event.setStartTime(draft.getEventStart());
        event.setEndTime(draft.getEventEnd());

        Event createdEvent = eventService.create(event);

        // 2. Create GeneralEventDetails
        GeneralEventDetails details = new GeneralEventDetails();
        details.setEventId(createdEvent.getEventId());
        details.setArtist(draft.getArtist());
        details.setHost(draft.getHost());
        details.setGenre(draft.getGenre());
        details.setStartTime(draft.getEventStart());
        details.setEndTime(draft.getEventEnd());
        details.setAdditionalInfo("User submitted event listing.");

        generalEventDetailsService.addEventDetails(details);

        // 3. Create EventSlot (auto generates seats)
        EventSlot slot = new EventSlot();
        slot.setEventId(createdEvent.getEventId());
        slot.setVenueId(draft.getVenueId());
        slot.setStartTime(
                draft.getSlotStart() != null ? draft.getSlotStart() : draft.getEventStart()
        );
        slot.setEndTime(
                draft.getSlotEnd() != null ? draft.getSlotEnd() : draft.getEventEnd()
        );
        slot.setFormat(EventFormat.NA); // EVENTs default to NA format
        slot.setLanguage(null);
        slot.setBasePrice(draft.getBasePrice());

        EventSlot createdSlot = eventSlotService.addEventSlot(slot);

        // 4. Update draft
        draft.setApprovalStatus("APPROVED");
        draft.setAdminComment(adminComment);
        draft.setEventId(createdEvent.getEventId());
        draft.setSlotId(createdSlot.getSlotId());

        dao.update(draft);
    }

    @Override
    public void rejectDraft(Integer draftId, String adminComment) {
        UserEventDraft draft = getDraft(draftId);

        if ("APPROVED".equalsIgnoreCase(draft.getApprovalStatus())) {
            throw new BusinessRuleException("Approved drafts cannot be rejected.");
        }

        if (adminComment == null || adminComment.trim().isEmpty()) {
            throw new BusinessRuleException("Admin comment is required for rejection.");
        }

        draft.setApprovalStatus("REJECTED");
        draft.setAdminComment(adminComment.trim());

        dao.update(draft);
    }
}
