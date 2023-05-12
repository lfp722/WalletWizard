import React, { useState } from 'react';
import { Link } from 'react-router-dom';
import '../styles/LoginStyle.css';
import axios from 'axios';
const url = 'http://localhost:8080';

function Login() {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');

  const handleSubmit = async (event) => {
    event.preventDefault();
    try {
		const credential = {
			email: email,
			password: password
		}
		const config = {
			headers: {
                'Content-Type': 'application/json',
                'Access-Control-Allow-Origin': '*',
                'Access-Control-Allow-Methods': 'GET, POST, PUT, DELETE, OPTIONS',
                'Access-Control-Allow-Headers': 'Content-Type, Authorization, X-Requested-With'
            }
		};
		const response = await axios.post(url + '/userLogin', credential, config);
		if (response.status === 200) {
		  window.location.href = `/mainpage/${response.data.token}`;
		} else if (response.status === 401) {
		  // Error: handle the error response here
		  alert('Invalid email or password');
		} else {
		  // Other errors: handle them here
		  alert(`An error occurred: ${response.statusText}`);
		}
		} catch(error) {
			alert(`User does not exist or credential not correct`);
		}
  };

  return (
    <div className="container">
      <h1>Login</h1>
      <form onSubmit={handleSubmit}>
        <div className="form-group">
          <label className="form-label" htmlFor="email">
            Email
          </label>
          <input
            className="form-control"
            type="email"
            id="email"
            name="email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            required
          />
        </div>
        <div className="form-group">
          <label className="form-label" htmlFor="password">
            Password
          </label>
          <input
            className="form-control"
            type="password"
            id="password"
            name="password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            required
          />
        </div>
        <button className="btn" type="submit">
          Login
        </button>
      </form>
      <p>
        Don't have an account? <Link to="/register">Register</Link>
      </p>
    </div>
  );
}

export default Login;
