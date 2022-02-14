import './App.css';
import MainPage from './pages/MainPage'
import LoginPage from './pages/LoginPage'
import Register from './pages/Register';
import HelpCenter from './pages/HelpCenter';
import React,{useState} from 'react';
import {
  BrowserRouter,
  Routes, Route, Link
} from "react-router-dom"
const App = () =>{
  const [useraddr,setUseraddr] = useState({
    name:'',addr:''})
  //console.log("app:"+useraddr.name+" "+useraddr.addr)
  return(
    <><>
      <BrowserRouter>
        <Routes>
          <Route path='/' element = {<LoginPage useraddr={useraddr} setUseraddr={setUseraddr}/>} />
          <Route path='/main/*' element = {<MainPage useraddr={useraddr} setUseraddr={setUseraddr}/>} />
          <Route path='/register' element = {<Register useraddr={useraddr} setUseraddr={setUseraddr}/>} />
          <Route path='/helpcenter' element= {<HelpCenter />} />
        </Routes>
      </BrowserRouter>
    </></>

  )
}

export default App;
//<LoginPage isSuccess={isSuccess}/>