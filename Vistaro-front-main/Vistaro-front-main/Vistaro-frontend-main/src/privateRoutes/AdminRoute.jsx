import React from 'react'
import { useSelector } from 'react-redux';
import Loader from '../components/Loader';
import { Navigate, Outlet } from 'react-router-dom';

export default function AdminRoute() {
	const user = useSelector((s) => s.user);
	const isAdmin = useSelector((s) => s.user.role) === "ADMIN"
	if (!user) {
		return <Loader />
	}

	return user && !isAdmin ? <Outlet /> : <Navigate to="/login" />;
}
