import React, { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import axios from 'axios';
import '../styles/ExpenseHistoryStyle.css';
const url = 'http://localhost:8080';

function Balance() {
	const { token } = useParams();
	const [oldBalance, setOldBalance] = useState('');
	const [newBalance, setNewBalance] = useState('');
	
	useEffect(() => {
		async function getBalance () {
			try {
				const response = await axios.get(url+`/getBalance/${token}`);
				setOldBalance(response.data.amount);
			} catch(error) {
				alert(`Error: ${error}`);
				throw new Error(error);
			}
		}
		getBalance();
	}, [token]);
	
	const handleSubmit = async (event) => {
		event.preventDefault();
		try {
			const newB = {
				amount: newBalance
			}
			const config = {
				headers: {
	                'Content-Type': 'application/json',
	                'Access-Control-Allow-Origin': '*',
	                'Access-Control-Allow-Methods': 'GET, POST, PUT, DELETE, OPTIONS',
	                'Access-Control-Allow-Headers': 'Content-Type, Authorization, X-Requested-With'
	            }
			};
			const response = await axios.post(url +`/setBalance/${token}`, newB, config);
			if (response.status === 200) {
				setOldBalance(newBalance);
				setNewBalance('');
				alert(`Balance Updated Successfully`);
			}
		} catch (error) {
			alert(`Error: ${error}`);
			throw new Error(error);
		}
	}
	return (
    <div className="container mt-5">
      <h1 className="text-center mb-4">Balance</h1>
      <p className="text-center">Current Balance: {oldBalance}</p>
      <form onSubmit={handleSubmit}>
        <div className="form-group">
          <label htmlFor="amount">Balance:</label>
          <input type="number" className="form-control" id="amount" value={newBalance} onChange={(e) => setNewBalance(e.target.value)} />
        </div>
        <button type="submit" className="btn btn-primary mt-3">Update Balance</button>
      </form>
    </div>
  );
}

export default Balance;
