package com.project.vistaro.service;

import java.util.List;

import com.project.vistaro.model.UserEventDraft;

public interface UserEventDraftService {

    Integer createDraft(UserEventDraft draft);

    UserEventDraft getDraft(Integer draftId);

    List<UserEventDraft> getDraftsByUser(Integer userId);

    List<UserEventDraft> getDraftsByStatus(String status); // PENDING / APPROVED / REJECTED

    void updateDraft(UserEventDraft draft);

    void deleteDraft(Integer draftId);

    void approveDraft(Integer draftId, String adminComment);

    void rejectDraft(Integer draftId, String adminComment);
}
