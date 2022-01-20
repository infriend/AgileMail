import { Route, Switch, Redirect} from 'react-router-dom'
import './App.css';
import MainPage from './pages/MainPage'
import LoginPage from './pages/LoginPage'
import React from 'react';

const App = () =>{
  var isSuccess = false
  return(
    <div>
      <LoginPage isSuccess={isSuccess}/>
    </div>
  )
}

export default App;
