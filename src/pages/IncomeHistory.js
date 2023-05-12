import React, { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import axios from 'axios';
import '../styles/ExpenseHistoryStyle.css';
const url = 'http://localhost:8080';

function IncomeHistory() {
  const { token } = useParams();
  const [incomes, setIncomes] = useState([]);

  useEffect(() => {
    async function getUserIncomesHistory() {
      try {
        const response = await axios.get(url + `/getIncomesInfobyToken/${token}`);
        setIncomes(response.data);
      } catch (error) {
        alert(`Error: ${error}`);
        throw new Error(error);
      }
    }
    getUserIncomesHistory();
  }, [token]);
  
  async function deleteIncome(id) {
  try {
    const response = await fetch(url + `/deleteIncome/${id}`, {
      method: 'DELETE'
    });
    if (response.ok) {
      // Success, return true or some other value to indicate success
      return true;
    } else {
      // Failure, throw an error
      throw new Error('Failed to delete Income');
    }
  } catch (error) {
    // Handle errors
    console.error(error);
    throw error;
  }
}

function handleDeleteExpense(id) {
  // Delete the expense from the server
  deleteIncome(id)
    .then(() => {
      // Remove the expense from the UI
      setIncomes(incomes.filter(income => income.incomeId !== id));
    })
    .catch(error => alert(error));
}

  return (
    <div className="timeline-container">
      <h1>Income History</h1>
      <div className="timeline">
        {incomes.map((income) => (
          <div className="timeline-item" key={income.incomeId}>
            <div className="timeline-dot"></div>
            <div className="timeline-content">
              <h2>Amount: {income.amount}</h2>
              <p>Frequency: {income.frequency}</p>
              <p>Source: {income.source}</p>
              <button onClick={() => handleDeleteExpense(income.incomeId)}>Delete</button>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
}

export default IncomeHistory;
