import React, { useState, useEffect, useCallback } from 'react';
import { useParams } from 'react-router-dom';
import axios from 'axios';
const url = 'http://localhost:8080';

function CalculateLoan() {
	const { token } = useParams();
	const [loanAmount, setLoanAmount] = useState('');
	const [interestRate, setInterestRate] = useState('');
	const [loanTerms, setLoanTerms] = useState('');
	const [startDate, setStartDate] = useState('');
	const [incomes, setIncomes] = useState([]);
	const [expenses, setExpenses] = useState([]);
	const [monthlyIncome, setMonthlyIncome] = useState();
	const [monthlyExpense, setMonthlyExpense] = useState();
	const [deposit, setDeposit] = useState();
	const mortgage = 0;
	const [resultString, setResultString] = useState('');
	
	const taxBrackets = [
	    { minIncome: 0, maxIncome: 18200, taxRate: 0, baseTax: 0 },
	    { minIncome: 18201, maxIncome: 45000, taxRate: 0.19, baseTax: 0 },
	    { minIncome: 45001, maxIncome: 120000, taxRate: 0.325, baseTax: 5092 },
	    { minIncome: 120001, maxIncome: 180000, taxRate: 0.37, baseTax: 29467 },
	    { minIncome: 180001, maxIncome: Infinity, taxRate: 0.45, baseTax: 51667 },
	];
	
	const calculateAfterTaxIncome = (grossIncome) => {
	    let tax = 0;
	
	    for (let i = 0; i < taxBrackets.length; i++) {
	      const { minIncome, maxIncome, taxRate, baseTax } = taxBrackets[i];
	      if (grossIncome >= minIncome) {
	        if (grossIncome <= maxIncome || !maxIncome) {
	          tax += baseTax + (grossIncome - minIncome + 1) * taxRate;
	          break;
	        } else {
	          tax += (maxIncome - minIncome + 1) * taxRate;
	        }
	      }
	    }
	
	    const afterTaxIncome = grossIncome - tax;
	    return afterTaxIncome;
    };
    const calculateMonthlyIncome = useCallback(() => {
		let ret = 0;
		incomes.forEach(i => {
			if (i.frequency === 'daily') {
				ret += i.amount*365;
			}
			else if (i.frequency === 'weekly') {
				ret += i.amount * 52;
			}
			else if (i.frequency === 'monthly') {
				ret += i.amount * 12;
			}
			else {	// Yearly
				ret += i.amount;
			}
		})
		const afterTaxIncome = calculateAfterTaxIncome(ret);
		let monthlyAfterTaxIncome = afterTaxIncome / 12;
		monthlyAfterTaxIncome = Math.round(monthlyAfterTaxIncome);
		setMonthlyIncome(monthlyAfterTaxIncome);
	}, [incomes]);
		
	const calculateMonthlyExpense = useCallback(() => {
		let ret = 0;
		expenses.forEach(e => {
			if (e.frequency === 'daily') {
				ret += e.amount*365;
			}
			else if (e.frequency === 'weekly') {
				ret += e.amount * 52;
			}
			else if (e.frequency === 'monthly') {
				ret += e.amount * 12;
			}
			else if (e.frequency === 'yearly') {	// Yearly
				ret += e.amount;
			}
		})
		ret = ret / 12
		ret = Math.round(ret);
		setMonthlyExpense(ret);
	}, [expenses]);
	
	useEffect(() => {
	  async function getUserIncomesHistory() {
	    try {
	      const response = await axios.get(url + `/getIncomesInfobyToken/${token}`);
	      setIncomes(response.data);
	      calculateMonthlyIncome();
	    } catch (error) {
	      alert(`Error: ${error}`);
	      throw new Error(error);
	    }
	  }
	  async function getUserExpensesHistory() {
	    try {
	      const response = await axios.get(url + `/getExpensesByToken/${token}`);
	      setExpenses(response.data);
	      calculateMonthlyExpense();
	    } catch (error) {
	      alert(`Error: ${error}`);
	      throw new Error(error);
	    }
	  }
	  async function getBalance () {
			try {
				const response = await axios.get(url+`/getBalance/${token}`);
				setDeposit(response.data);
			} catch(error) {
				alert(`Error: ${error}`);
				throw new Error(error);
			}
		}
	  getBalance();
	  getUserIncomesHistory();
	  getUserExpensesHistory();
	}, [token, calculateMonthlyIncome, calculateMonthlyExpense]);
	
	function calculateMortgagePayments() {
	  // calculate the number of full months between the start date and today
	  const start = new Date(startDate);
	  const today = new Date();
	  const monthsElapsed = (today.getFullYear() - start.getFullYear()) * 12 + (today.getMonth() - start.getMonth());
	
	  // calculate the monthly payment amount based on the mortgage amount and the number of payments remaining
	  const remainingPayments = 360 - monthsElapsed; // assume a 30-year mortgage with 360 total payments
	  const monthlyPayment = mortgage / remainingPayments;
	
	  return monthlyPayment.toFixed(2); // round the monthly payment to 2 decimal places
	}
	
	const calculateLoan = (event) => {
		event.preventDefault();
		const monthlyInterestRate = parseFloat(interestRate / 1200);
		const numPayments = parseFloat(loanTerms * 12);
		const mortgagePayment = parseFloat(calculateMortgagePayments());
		const totalMonthlyPayment = parseFloat(loanAmount * monthlyInterestRate) / (1 - Math.pow(1 + monthlyInterestRate, -numPayments)) + mortgagePayment;
		const steps = 
		`Your current deposit: ${deposit.amount}\n
Your expected monthly income: ${monthlyIncome}\n
Your expected monthly expense: ${monthlyExpense}\n
Your target amount: ${loanAmount}\n
		\n
Loan calculation steps:\n
- Convert annual interest rate to monthly rate by dividing by 1200\n
- Convert loan terms from years to months by multiplying by 12\n
- Calculate mortgage payments based on remaining payments and total mortgage amount\n
- Calculate monthly payment for loan using standard formula\n
- Add mortgage payments to get total monthly payment\n
- Round the monthly payment to 2 decimal places\n
	    `;
	    const s = `Your total monthly repayment is $${totalMonthlyPayment.toFixed(2)}.\n\n${steps}`;
		alert(s);
	};
	
	if (!monthlyIncome) {
			return(
				<div>
				<h1>This is Calculate page</h1>
				<p>You have not setup your incomes yet</p>
				</div>
			)
	}
	return (
			<div>
				<h1>Calculate Loan</h1>
				<form onSubmit={calculateLoan}>
				  <div class="mb-3">
				    <label for="loanAmount" class="form-label">Loan Amount</label>
				    <input type="text" class="form-control" id="loanAmount" onChange={(event) => setLoanAmount(event.target.value)} />
				  </div>
				  <div class="mb-3">
				    <label for="interestRate" class="form-label">Interest Rate</label>
				    <input type="text" class="form-control" id="interestRate" onChange={(event) => setInterestRate(event.target.value)} />
				  </div>
				  <div class="mb-3">
				    <label for="loanTerms" class="form-label">Loan Terms (in Year)</label>
				    <input type="text" class="form-control" id="loanTerms" onChange={(event) => setLoanTerms(event.target.value)} />
				  </div>
				  <div class="mb-3">
				    <label for="startDate" class="form-label">Start Date</label>
				    <input type="date" class="form-control" id="startDate" onChange={(event) => setStartDate(event.target.value)}/>
				  </div>
				  <button type="submit" class="btn btn-primary">Submit</button>
				</form>
			</div>
		)
}
export default CalculateLoan;