import React,{useState}from 'react'
import {Layout} from '@douyinfe/semi-ui'
import Login from '../components/Login'
import '../css/loginpage.css'
const LoginPage = ({useraddr,setUseraddr,assoData,setAssoData}) => {
    if (useraddr === undefined){
        useraddr = localStorage.getItem("userdata")
    }
    const { Content } = Layout;
    useraddr = useraddr
    return(
    <Layout className="wrap"
        style={{
            backgroundImage:"url("+ require("../images/backgroundImage.jpg")+")",backgroundSize:"cover"}   
        } 
    >  
        <Content >
            <Login className="middle" useraddr={useraddr} setUseraddr={setUseraddr}/>
        </Content>
    </Layout>
    )
}
export default LoginPage