import React, { useState } from 'react';
import '../styles/UpdateIncomeStyle.css';
import { useParams } from 'react-router-dom';
import axios from 'axios';
const url = 'http://localhost:8080';

function UpdateExpense() {
	const [amount, setAmount] = useState('');
	const [notes, setNotes] = useState('');
	const [date, setDate] = useState('');
	const [frequency, setFrequency] = useState('One-time');
	const { token } = useParams();
		
	const handleSubmit = async (event) => {
		event.preventDefault();
		if (amount === '') {
			alert('Amount is Empty');
			return;
		}
		if (notes === '') {
			alert('Notes is Empty');
			return;
		}
		try {
			const i = {
				amount: amount,
				notes: notes,
				frequency: frequency,
				date: date
			}
			const config = {
				headers: {
	                'Content-Type': 'application/json',
	                'Access-Control-Allow-Origin': '*',
	                'Access-Control-Allow-Methods': 'GET, POST, PUT, DELETE, OPTIONS',
	                'Access-Control-Allow-Headers': 'Content-Type, Authorization, X-Requested-With'
	            }
			};
			const response = await axios.post(url + '/addExpense/' + token, i, config);
			if (response.status === 200) {
				alert('Successfully updated your income!');
				setAmount('');
				setNotes('');
				setFrequency('One-time');
				setDate('');
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
	
	  const handleNotesChange = (event) => {
	    setNotes(event.target.value);
	  };
	
	  const handleFrequencyChange = (event) => {
	    setFrequency(event.target.value);
	  };
	  
	  const handleDateChange = (event) => {
	  setDate(event.target.value);
	};
	return (
		<div>
      <h1>Update Your Expense</h1>
      <form onSubmit={handleSubmit}>
        <label>
          Amount:
          <input type="number" value={amount} onChange={handleAmountChange} />
        </label>
        <br />
        <label>
          Notes:
          <input type="text" value={notes} onChange={handleNotesChange} />
        </label>
        <br />
        <label>
          Frequency:
          <select value={frequency} onChange={handleFrequencyChange}>
            <option value="daily">Daily</option>
            <option value="weekly">Weekly</option>
            <option value="monthly">Monthly</option>
            <option value="yearly">Yearly</option>
            <option value="One-time">One-time</option>
          </select>
        </label>
        <br />
        <label>
	    Date:
	    <input type="date" value={date} onChange={handleDateChange} />
	  </label>
	  <br />
        <button type="submit">Submit</button>
      </form>
    </div>
	);
}
export default UpdateExpense;