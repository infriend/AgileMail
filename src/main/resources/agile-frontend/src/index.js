import React from 'react';
import ReactDOM from 'react-dom';
import './index.css';
import App from './App';
import axios from 'axios'
import Register from './pages/Register';
import LoginPage from './pages/LoginPage';
import MainPage from './pages/MainPage';
import {
  BrowserRouter,
  Routes, Route,
} from "react-router-dom"
import HelpCenter from './pages/HelpCenter';

ReactDOM.render(
  <App />,
  document.getElementById('root')
);