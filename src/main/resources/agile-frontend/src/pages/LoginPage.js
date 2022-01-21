import React from 'react'
import {Layout} from '@douyinfe/semi-ui'
import Login from '../components/Login'
import '../css/loginpage.css'
const LoginPage = (isSuccess) => {
    const { Header,Content,Footer } = Layout;
    return(
    <Layout className="wrap"
    style={{
        backgroundImage:"url("+ require("../images/backgroundImage.jpg")+")",backgroundSize:"cover"}   
    } 
    >  
        <Content>
            <br /><br /><br /><br /><br />
            <Login className="mainCard"/>
        </Content>
    </Layout>
    )
}
export default LoginPage