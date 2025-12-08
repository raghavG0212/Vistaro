package com.project.vistaro.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.project.vistaro.dto.AdminDecisionDto;
import com.project.vistaro.dto.UserEventDraftDto;
import com.project.vistaro.model.UserEventDraft;
import com.project.vistaro.service.UserEventDraftService;

@RestController
@RequestMapping("/api/v1/user-event")
@CrossOrigin
public class UserEventDraftController {

    private final UserEventDraftService service;

    public UserEventDraftController(UserEventDraftService service) {
        this.service = service;
    }

    // ---------------- Helpers ----------------

    private UserEventDraft dtoToEntity(UserEventDraftDto dto) {
        UserEventDraft d = new UserEventDraft();

        d.setDraftId(dto.draftId);
        d.setUserId(dto.userId);

        d.setTitle(dto.title);
        d.setDescription(dto.description);
        d.setSubCategory(dto.subCategory);
        d.setBannerUrl(dto.bannerUrl);
        d.setThumbnailUrl(dto.thumbnailUrl);

        d.setArtist(dto.artist);
        d.setHost(dto.host);
        d.setGenre(dto.genre);

        if (dto.eventStart != null) {
            d.setEventStart(LocalDateTime.parse(dto.eventStart));
        }
        if (dto.eventEnd != null) {
            d.setEventEnd(LocalDateTime.parse(dto.eventEnd));
        }

        d.setVenueId(dto.venueId);

        if (dto.slotStart != null) {
            d.setSlotStart(LocalDateTime.parse(dto.slotStart));
        }
        if (dto.slotEnd != null) {
            d.setSlotEnd(LocalDateTime.parse(dto.slotEnd));
        }

        d.setBasePrice(dto.basePrice);
        d.setEventId(dto.eventId);
        d.setSlotId(dto.slotId);

        d.setApprovalStatus(dto.approvalStatus);
        d.setAdminComment(dto.adminComment);

        // createdAt is DB-managed on insert; we don't set it from DTO

        return d;
    }

    private UserEventDraftDto entityToDto(UserEventDraft d) {
        UserEventDraftDto dto = new UserEventDraftDto();

        dto.draftId = d.getDraftId();
        dto.userId = d.getUserId();

        dto.title = d.getTitle();
        dto.description = d.getDescription();
        dto.subCategory = d.getSubCategory();
        dto.bannerUrl = d.getBannerUrl();
        dto.thumbnailUrl = d.getThumbnailUrl();

        dto.artist = d.getArtist();
        dto.host = d.getHost();
        dto.genre = d.getGenre();

        dto.eventStart = d.getEventStart() != null ? d.getEventStart().toString() : null;
        dto.eventEnd = d.getEventEnd() != null ? d.getEventEnd().toString() : null;

        dto.venueId = d.getVenueId();

        dto.slotStart = d.getSlotStart() != null ? d.getSlotStart().toString() : null;
        dto.slotEnd = d.getSlotEnd() != null ? d.getSlotEnd().toString() : null;

        dto.basePrice = d.getBasePrice();
        dto.eventId = d.getEventId();
        dto.slotId = d.getSlotId();

        dto.approvalStatus = d.getApprovalStatus();
        dto.adminComment = d.getAdminComment();

        dto.createdAt = d.getCreatedAt() != null ? d.getCreatedAt().toString() : null;

        return dto;
    }

    // ---------------- USER: Create Draft ----------------

    @PostMapping
    public ResponseEntity<UserEventDraftDto> createDraft(@RequestBody UserEventDraftDto dto) {
        UserEventDraft draft = dtoToEntity(dto);
        Integer id = service.createDraft(draft);
        draft.setDraftId(id);
        return ResponseEntity.ok(entityToDto(draft));
    }

    // ---------------- USER: Get Drafts by User ----------------

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<UserEventDraftDto>> getByUser(@PathVariable Integer userId) {
        return ResponseEntity.ok(
                service.getDraftsByUser(userId).stream()
                        .map(this::entityToDto)
                        .collect(Collectors.toList())
        );
    }

    // ---------------- ADMIN: Get Drafts by Status ----------------
    //   status path variable: PENDING / APPROVED / REJECTED

    @GetMapping("/status/{status}")
    public ResponseEntity<List<UserEventDraftDto>> getByStatus(@PathVariable String status) {
        return ResponseEntity.ok(
                service.getDraftsByStatus(status).stream()
                        .map(this::entityToDto)
                        .collect(Collectors.toList())
        );
    }

    // ---------------- APPROVE draft (creates Event + Details + Slot) ----------------

    @PostMapping("/{draftId}/approve")
    public ResponseEntity<Void> approve(
            @PathVariable Integer draftId,
            @RequestBody(required = false) AdminDecisionDto body
    ) {
        String comment = body != null ? body.getAdminComment() : null;
        service.approveDraft(draftId, comment);
        return ResponseEntity.ok().build();
    }

    // ---------------- REJECT draft ----------------

    @PostMapping("/{draftId}/reject")
    public ResponseEntity<Void> reject(
            @PathVariable Integer draftId,
            @RequestBody AdminDecisionDto body
    ) {
        String comment = body != null ? body.getAdminComment() : null;
        service.rejectDraft(draftId, comment);
        return ResponseEntity.ok().build();
    }

    // ---------------- Delete draft ----------------

    @DeleteMapping("/{draftId}")
    public ResponseEntity<Void> delete(@PathVariable Integer draftId) {
        service.deleteDraft(draftId);
        return ResponseEntity.ok().build();
    }
}
