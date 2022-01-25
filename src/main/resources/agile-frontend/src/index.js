import React from 'react';
import ReactDOM from 'react-dom';
import './index.css';
import App from './App';
import Register from './components/Register';
import LoginPage from './pages/LoginPage';
import MainPage from './pages/MainPage';
import {
  BrowserRouter,
  Routes, Route,
} from "react-router-dom"
import HelpCenter from './components/HelpCenter';
ReactDOM.render(
  <BrowserRouter>
    <Routes>
      <Route path='/' element = {<App/>} />
      <Route path='/login' element = {<LoginPage/>} />
      <Route path='/main/*' element = {<MainPage/>} />
      <Route path='/register' element = {<Register />} />
      <Route path='/helpcenter' element= {<HelpCenter />} />
    </Routes>
 </BrowserRouter>,
  document.getElementById('root')
);

//reportWebVitals();

/*
  <React.StrictMode>
    <App />
  </React.StrictMode>,
  */
 /*
   <BrowserRouter>
   <Routes>
      <Route path='/' element = {<App/>} />
      <Route path='/register' element = {<Register />} />
   </Routes>
  </BrowserRouter>,
 */
/*
<Route path='/writemail' element={<ContentWrite/>} />
*/