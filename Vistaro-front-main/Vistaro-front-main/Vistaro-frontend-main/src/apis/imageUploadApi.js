import axios from "axios";

export const uploadImage = (file) => {
	const formData = new FormData();
	formData.append("file", file);

	return axios.post("http://localhost:9090/api/v1/upload/image", formData, {
		headers: { "Content-Type": "multipart/form-data" }
	});
};
