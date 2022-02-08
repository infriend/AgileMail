import React, { useRef, useState } from 'react'
import { Col, Layout, Row } from '@douyinfe/semi-ui';
import '../css/mainpage.css'
import HeaderMain from '../components/HeaderMain';
import SidererMain from '../components/SiderMain';
import ContentMain from '../components/ContentMain';

const MainPage = ({useraddr,setUseraddr}) =>{
    const { Header, Footer, Sider, Content } = Layout;
    console.log("main:"+useraddr.name)
    return (
        <Layout className="mainPage" style={{ border: '1px solid var(--semi-color-border)',height: '100%',weight:'100%',position:'absolute'}}>
            <Header style={{ backgroundColor: 'var(--semi-color-bg-1)' }}>
                <HeaderMain useraddr = {useraddr} setUseraddr = {setUseraddr}/>
            </Header>
            <Layout >
                <Sider style={{ backgroundColor: 'var(--semi-color-bg-1)' }}>
                    <SidererMain />    
                </Sider>
                <Content
                    style={{
                    padding: '24px',
                    backgroundColor: 'var(--semi-color-bg-0)',
                }}>
                    <ContentMain useraddr = {useraddr} setUseraddr = {setUseraddr}/>
                </Content>
            </Layout>
            <Footer style={{
                    display: 'flex',
                    justifyContent: 'space-between',
                    padding: '20px',
                    color: 'var(--semi-color-text-2)',
                    backgroundColor: 'rgba(var(--semi-grey-0), 1)',
                }}>                <span
                style={{
                    display: 'flex',
                    alignItems: 'center',
                }}
            >
                
                <span>Copyright © 2022 AgileTeam. All Rights Reserved. </span>
            </span></Footer>
        </Layout>
    );
}
export default MainPage