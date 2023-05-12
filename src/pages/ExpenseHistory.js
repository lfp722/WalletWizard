import React, { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import axios from 'axios';
import '../styles/ExpenseHistoryStyle.css';
const url = 'http://localhost:8080';

function ExpenseHistory() {
  const { token } = useParams();
  const [expenses, setExpenses] = useState([]);

  useEffect(() => {
    async function getUserExpensesHistory() {
      try {
        const response = await axios.get(url + `/getExpensesByToken/${token}`);
        setExpenses(response.data);
      } catch (error) {
        alert(`Error: ${error}`);
        throw new Error(error);
      }
    }
    getUserExpensesHistory();
  }, [token]);

  // Define a compare function that compares the dates of two expenses
  function compareExpensesByDate(a, b) {
    return new Date(a.date) - new Date(b.date);
  }
  
  async function deleteExpense(id) {
  try {
    const response = await fetch(url + `/deleteExpense/${id}`, {
      method: 'DELETE'
    });
    if (response.ok) {
      // Success, return true or some other value to indicate success
      return true;
    } else {
      // Failure, throw an error
      throw new Error('Failed to delete expense');
    }
  } catch (error) {
    // Handle errors
    console.error(error);
    throw error;
  }
}

function handleDeleteExpense(id) {
  // Delete the expense from the server
  deleteExpense(id)
    .then(() => {
      // Remove the expense from the UI
      setExpenses(expenses.filter(expense => expense.expenseId !== id));
    })
    .catch(error => alert(error));
}


  // Sort the expenses by date in ascending order
  expenses.sort(compareExpensesByDate).reverse();

  return (
    <div className="timeline-container">
      <h1>Expense History</h1>
      <div className="timeline">
        {expenses.map((expense) => (
          <div className="timeline-item" key={expense.expenseId}>
            <div className="timeline-dot"></div>
            <div className="timeline-content">
              <h2>Amount: {expense.amount}</h2>
              <p>Frequency: {expense.frequency}</p>
              <p>Notes: {expense.notes}</p>
              <p>Date: {expense.date}</p>
              <button onClick={() => handleDeleteExpense(expense.expenseId)}>Delete</button>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
}

export default ExpenseHistory;
