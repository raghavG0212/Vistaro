package com.project.vistaro.repository;

import java.util.List;
import java.util.Optional;

import com.project.vistaro.model.UserEventDraft;

public interface UserEventDraftDao {

    Integer save(UserEventDraft draft);

    Optional<UserEventDraft> findById(Integer draftId);

    List<UserEventDraft> findByUserId(Integer userId);

    List<UserEventDraft> findAllByStatus(String status); // PENDING / APPROVED / REJECTED

    void update(UserEventDraft draft);

    void delete(Integer draftId);
}
