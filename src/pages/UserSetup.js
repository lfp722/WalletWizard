import React, { useState } from 'react';
import { useParams } from 'react-router-dom';
import axios from 'axios';
import '../styles/UserSetupStyle.css';
const url = 'http://localhost:8080';

function UserSetup() {
	const [amount, setAmount] = useState('');
	const { token } = useParams();
	const handleSubmit = async (event) => {
		event.preventDefault();
		try {
			const b = {
				amount: amount
			}
			const config = {
	            headers: {
	                'Content-Type': 'application/json',
	                'Access-Control-Allow-Origin': '*',
	                'Access-Control-Allow-Methods': 'GET, POST, PUT, DELETE, OPTIONS',
	                'Access-Control-Allow-Headers': 'Content-Type, Authorization, X-Requested-With'
	            }
	        };
	        const response = await axios.post(url + `/addBalance/${token}`, b, config);
	        if (response.status === 200) {
				alert(`Setup Successful!\nYou will now be directed to the main page.`);
				window.location.href = `/mainpage/${token}`;
			}
		} catch (error) {
			alert(`Error: ${error}`);
			throw new Error(error);
		}
	}
	
	return (
		<div>
			<h1>Finish Setting Up Your Account</h1>
			<div className="container">
				<form onSubmit={handleSubmit}>
					<div className="form-group">
			          <label htmlFor="amount">Starting Balance:</label>
			          <input type="number" className="form-control" id="amount" value={amount} onChange={(e) => setAmount(e.target.value)} />
			        </div>
			        <button type="submit" className="btn btn-primary mt-3">Finish Setup</button>
				</form>
			</div>
		</div>
	)
}

export default UserSetup;
