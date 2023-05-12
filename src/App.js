import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Login from './pages/Login';
import Register from './pages/Register';
import Home from './pages/Home';
import MainPage from './pages/MainPage';
import UserSetup from './pages/UserSetup';
import Error from './pages/Error';

function App() {
  return (
    <Router>
      <Routes>
      	<Route exact path="/" element={<Home />} />
        <Route exact path="/login" element={<Login />} />
        <Route exact path="/register" element={<Register />} />
        <Route exact path="/mainpage/:token" element={<MainPage />} />
        <Route exact path="/userSetup/:token" element={<UserSetup />} />
        <Route path="*" element={<Error />} />
      </Routes>
    </Router>
  );
}

export default App;
