import axios from "axios";

const API = "/api/v1/user-event";

// USER: create a new draft
export const createUserEventDraft = (data) =>
	axios.post(`${API}`, data);

// USER: get drafts by user
export const getDraftsByUser = (userId) =>
	axios.get(`${API}/user/${userId}`);

// ADMIN: list by status (PENDING / APPROVED / REJECTED)
export const getDraftsByStatus = (status) =>
	axios.get(`${API}/status/${status}`);

// ADMIN/USER: delete draft
export const deleteDraft = (draftId) =>
	axios.delete(`${API}/${draftId}`);

// ADMIN: approve draft (server will create Event + Slot + Details)
export const approveDraft = (draftId, body) =>
	axios.post(`${API}/${draftId}/approve`, body); // { adminComment }

// ADMIN: reject draft
export const rejectDraft = (draftId, body) =>
	axios.post(`${API}/${draftId}/reject`, body); // { adminComment }
