import React from 'react';
import '../styles/ErrorStyle.css';

function Error () {
	const goBackToMainPage = () => {
		window.location.href = '/';
	}
	return (
		<div>
			<h1>Oops!</h1>	
			<p>404 - PAGE NOT FOUND</p>
			<p>The page you are looking for might have been removed<br/>
			had its name changed or is temporarily unavailable.</p>
			<button onClick={goBackToMainPage}>Go Back To the Main Page</button>
		</div>
	);
}

export default Error;