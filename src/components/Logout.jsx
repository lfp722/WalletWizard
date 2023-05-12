import React from 'react';

export const Logout = () => {
	window.location.href = '/';
}

export default function LogoutButton () {
	return <button className="logout-button" onClick={() => Logout()}>Logout</button>;
}