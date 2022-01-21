import './App.css';
import MainPage from './pages/MainPage'
import LoginPage from './pages/LoginPage'
import Register from './components/Register';
import React from 'react';
import {
  BrowserRouter as Router,
  Routes, Route, Link
} from "react-router-dom"

const App = () =>{
  var isSuccess = false
  return(
    <LoginPage isSuccess={isSuccess}/>
  )
}

export default App;
//<LoginPage isSuccess={isSuccess}/>