// Sidebar
import Welcome from './Welcome';
import CalculateLoan from './CalculateLoan';
import UpdateIncome from './UpdateIncome';
import UpdateExpense from './UpdateExpense';
import ExpenseHistory from './ExpenseHistory';
import Balance from './Balance';
import IncomeHistory from './IncomeHistory';
import FinancialGoal from './FinancialGoal';
import AboutUs from './AboutUs';
import LogoutButton from '../components/Logout';
import "../styles/MainPageStyle.css"
import React, { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import axios from 'axios';
const url = 'http://localhost:8080';




function SidebarMenu({ setActiveMenuItem }) {
  const { token } = useParams();
  return (
    <div className="sidebar">
      <ul className="menu-items">
        <li onClick={() => setActiveMenuItem(`/calculateLoan/${token}`)}>Calculate Loan</li>
        <li onClick={() => setActiveMenuItem(`/updateIncome/${token}`)}>Update Income</li>
        <li onClick={() => setActiveMenuItem(`/updateExpense/${token}`)}>Update Expense</li>
        <li onClick={() => setActiveMenuItem(`/ExpenseHistory/${token}`)}>Expense History</li>
        <li onClick={() => setActiveMenuItem(`/IncomeHistory/${token}`)}>Income History</li>
        <li onClick={() => setActiveMenuItem(`/FinancialGoal/${token}`)}>Financial Goal</li>
        <li onClick={() => setActiveMenuItem(`/Balance/${token}`)}>Balance</li>
        <li onClick={() => setActiveMenuItem(`/aboutUs`)}>About Us</li>
      </ul>
    </div>
  );
}

function MainContent({ activeMenuItem }) {
	const { token } = useParams();
	//Just append options here
  switch (activeMenuItem) {
    case `/calculateLoan/${token}`:
      return <CalculateLoan />;
    case `/updateIncome/${token}`:
      return <UpdateIncome />
  	case `/updateExpense/${token}`:
	  return <UpdateExpense />
    case `/ExpenseHistory/${token}`:
		return <ExpenseHistory />
	case `/Balance/${token}`:
		return <Balance />
	case `/IncomeHistory/${token}`:
		return <IncomeHistory />
	case `/FinancialGoal/${token}`:
		return <FinancialGoal />
	case `/aboutUs`:
		return <AboutUs />
    default:
      return <Welcome />;
  }
}


function MainPage() {
  const { token } = useParams();
  const [user, setUser] = useState(null);
  const [activeMenuItem, setActiveMenuItem] = useState('calculate-loan');
  
  // User info
  useEffect(() => {
    async function fetchUser() {
      try {
        const response = await axios.get(url+`/getUserInfobyToken/${token}`);
        setUser(response.data);
      } catch (error) {
        alert(error);
      }
    }
    fetchUser();
  }, [token]);
  
  // Loading until user info retrieved
  if (!user) {
    return <div>Loading...</div>;
  }

  return (
    <div className="app-container">
      <header className="app-header"><div className="header">Hello {user.firstname}!</div><LogoutButton /></header>
      <div className="app-content">
        <div className="app-sidebar">
          <SidebarMenu setActiveMenuItem={setActiveMenuItem} token={token} />
        </div>
        <div className="app-main-content">
          <MainContent activeMenuItem={activeMenuItem} />
        </div>
      </div>
    </div>
  );
}


export default MainPage;
