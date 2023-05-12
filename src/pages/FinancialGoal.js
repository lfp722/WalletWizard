import React, { useState, useEffect, useMemo } from 'react';
import { useParams } from 'react-router-dom';
import axios from 'axios';
import '../styles/FinancialGoalStyle.css'
const url = 'http://localhost:8080';

function FinancialGoal () {
	const { token } = useParams();
	const [balances, setBalances] = useState();
	const [goals, setGoals] = useState([]);
	const [title, setTitle] = useState('');
	const [targetAmount, setTargetAmount] = useState(0);
	const [dueDate, setDueDate] = useState('');
	
	useEffect(() => {
		async function getBalance () {
			try {
				const response = await axios.get(url+`/getBalance/${token}`);
				setBalances(response.data);
			} catch(error) {
				alert(`Error: ${error}`);
				throw new Error(error);
			}
		}
	    async function getUserFinancialGoals() {
		try {
	        const response = await axios.get(url + `/getFinancialGoalsbyToken/${token}`);
	        setGoals(response.data);
	      } catch (error) {
	        alert(`Error: ${error}`);
	        throw new Error(error);
	      }
		}
		getBalance();
		getUserFinancialGoals();
	});
	
	const createGoal = async (e) => {
	    e.preventDefault();
	
	    const newGoal = {
	      title: title,
	      targetAmount: targetAmount,
	      dueDate: dueDate
	    };
	    const config = {
			headers: {
	            'Content-Type': 'application/json',
	            'Access-Control-Allow-Origin': '*',
	            'Access-Control-Allow-Methods': 'GET, POST, PUT, DELETE, OPTIONS',
	            'Access-Control-Allow-Headers': 'Content-Type, Authorization, X-Requested-With'
	        }
		};
	    try {
	      await axios.post(url + `/addFinancialGoal/${token}`, newGoal, config);
	      // do something with the response, e.g. update state or navigate to a different page
	    } catch (error) {
	      alert(`Error: ${error}`);
	      throw new Error(error);
	    }
	
	    // reset form fields
	    setTitle('');
	    setTargetAmount(0);
	    setDueDate('');
	};
	
	const calculateProgress = useMemo(() => {
	  return (targetAmount, currentAmount) => {
	    const progress = (currentAmount / targetAmount) * 100;
	    return progress > 100 ? 100 : progress;
	  };
	}, []);
	
	return (
	<div>
      <h1>Create New Financial Goal</h1>
      <form onSubmit={createGoal}>
        <div>
          <label htmlFor="title">Title:</label>
          <input
            type="text"
            id="title"
            value={title}
            onChange={(e) => setTitle(e.target.value)}
          />
        </div>
        <div>
          <label htmlFor="targetAmount">Target Amount:</label>
          <input
            type="number"
            id="targetAmount"
            value={targetAmount}
            onChange={(e) => setTargetAmount(e.target.value)}
          />
        </div>
        <div>
          <label htmlFor="dueDate">Due Date:</label>
          <input
            type="date"
            id="dueDate"
            value={dueDate}
            onChange={(e) => setDueDate(e.target.value)}
          />
        </div>
        <button type="submit">Create Goal</button>
      </form>
      <div className="timeline-container">
      <div className="timeline">
          {goals.map((goal) => {
            const progress = calculateProgress(goal.targetAmount, balances.amount);
            return (
              <div className="timeline-item" key={goal.financialGoalId}>
                <div className="timeline-dot"></div>
                <div className="timeline-content">
                  <h2>Amount: {goal.targetAmount}</h2>
                  <p>Title: {goal.title}</p>
                  <p>Due Date: {goal.dueDate}</p>
                  <p>Progress: </p>
                  <div className="progress-bar">
                    <div
                      className="progress-bar-fill"
                      style={{ width: `${progress}%` }}
                    ></div>
                  </div>
                </div>
              </div>
            );
          })}
        </div>
    </div>
    </div>
	);
}

export default FinancialGoal;
