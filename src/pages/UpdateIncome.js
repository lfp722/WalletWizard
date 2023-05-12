import React, { useState } from 'react';
import '../styles/UpdateIncomeStyle.css';
import { useParams } from 'react-router-dom';
import axios from 'axios';
const url = 'http://localhost:8080';

function UpdateIncome() {
	const [amount, setAmount] = useState('');
	const [source, setSource] = useState('');
	const [frequency, setFrequency] = useState('daily');
	const { token } = useParams();
	
	const handleSubmit = async (event) => {
		event.preventDefault();
		if (amount === '') {
			alert('Amount is Empty');
			return;
		}
		if (source === '') {
			alert('Source is Empty');
			return;
		}
		try {
			const i = {
				amount: amount,
				source: source,
				frequency: frequency
			}
			const config = {
				headers: {
	                'Content-Type': 'application/json',
	                'Access-Control-Allow-Origin': '*',
	                'Access-Control-Allow-Methods': 'GET, POST, PUT, DELETE, OPTIONS',
	                'Access-Control-Allow-Headers': 'Content-Type, Authorization, X-Requested-With'
	            }
			};
			const response = await axios.post(url + '/addIncome/' + token, i, config);
			if (response.status === 200) {
				alert('Successfully updated your income!');
				setAmount('');
				setSource('');
				setFrequency('daily');
			} else {
				alert(`Not successful: ${response.status}`);
			}
		} catch (error) {
			alert(`Error orrcured: ${error}`);
		}
	};
	
	const handleAmountChange = (event) => {
	    setAmount(event.target.value);
	};
	
	  const handleSourceChange = (event) => {
	    setSource(event.target.value);
	  };
	
	  const handleFrequencyChange = (event) => {
	    setFrequency(event.target.value);
	  };
	return (
		<div>
      <h1>Update Your Income</h1>
      <form onSubmit={handleSubmit}>
        <label>
          Amount:
          <input type="number" value={amount} onChange={handleAmountChange} />
        </label>
        <br />
        <label>
          Source:
          <input type="text" value={source} onChange={handleSourceChange} />
        </label>
        <br />
        <label>
          Frequency:
          <select value={frequency} onChange={handleFrequencyChange}>
            <option value="daily">Daily</option>
            <option value="weekly">Weekly</option>
            <option value="monthly">Monthly</option>
            <option value="yearly">Yearly</option>
          </select>
        </label>
        <br />
        <button type="submit">Submit</button>
      </form>
    </div>
	);
}
export default UpdateIncome;