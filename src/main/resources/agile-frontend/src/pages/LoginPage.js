import React,{useState}from 'react'
import {Layout} from '@douyinfe/semi-ui'
import Login from '../components/Login'
import '../css/loginpage.css'
const LoginPage = ({useraddr,setUseraddr}) => {
    const { Content } = Layout;
    useraddr = useraddr
    return(
    <Layout className="wrap"
        style={{
            backgroundImage:"url("+ require("../images/backgroundImage.jpg")+")",backgroundSize:"cover"}   
        } 
    >  
        <Content>
            <br /><br /><br /><br /><br />
            <Login className="mainCard" useraddr={useraddr} setUseraddr={setUseraddr}/>
        </Content>
    </Layout>
    )
}
export default LoginPage