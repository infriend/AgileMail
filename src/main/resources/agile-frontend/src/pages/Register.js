import React, { useRef, useState } from 'react'
import {Layout} from '@douyinfe/semi-ui'
import Register from "../components/Register"
import '../css/loginpage.css'
const RegisterPage = ({useraddr,setUseraddr}) =>{
    if (useraddr === undefined){
        useraddr = localStorage.getItem("userdata")
    }
    const { Content } = Layout;
    useraddr = useraddr
    return(
        <Layout className="wrap"
                style={{
                    backgroundImage:"url("+ require("../images/backgroundImage2.jpg")+")",backgroundSize:"cover"}
                }
        >
            <Content >
                <Register className="middle" useraddr={useraddr} setUseraddr={setUseraddr}/>
            </Content>
        </Layout>
    )
}
export default RegisterPage
