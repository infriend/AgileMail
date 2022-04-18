import React, { useRef, useState } from 'react'
import { Col, Layout, Row } from '@douyinfe/semi-ui';
import '../css/mainpage.css'
import HeaderMain from '../components/HeaderMain';
import SidererMain from '../components/SiderMain';
import ContentMain from '../components/ContentMain';
import api from '../api/api'
const MainPage = ({useraddr,setUseraddr}) =>{
    const { Header, Footer, Sider, Content } = Layout;
    const [boxData,setBoxData] = useState();
    const [addrData, setAddrData] = useState();
    const [folderList, setFolderList] = useState();
    const [assoData,setAssoData] = useState();
    if(folderList === undefined){
        api.getFolderList(useraddr,setFolderList)

    }else{
        //console.log(folderList)
    }
    if(assoData === undefined){
        api.getAssociatedAddrList(setAssoData)
    }else{
        let data = assoData[0].emailAddress
        let list = data.split("@")
        const t = {name:list[0],addr:list[1]}
        localStorage.setItem("currmail",data)
        console.log(JSON.parse(localStorage.getItem("associatedList")))
    }
    const [detailData,setDetailData] = useState();
    //console.log("main:"+useraddr.name)
    useraddr = JSON.parse(localStorage.getItem("userdata"))
    return (
        <Layout className="mainPage" style={{ border: '1px solid var(--semi-color-border)',height: '100%',weight:'100%',position:'absolute'}}>
            <Header style={{ backgroundColor: 'var(--semi-color-bg-1)' }}>
                <HeaderMain useraddr = {useraddr} setUseraddr = {setUseraddr}assoData={assoData} setAssoData ={setAssoData} />
            </Header>
            <Layout >
                <Sider style={{ backgroundColor: 'var(--semi-color-bg-1)' }}>
                    <SidererMain useraddr={useraddr} setUseraddr={setUseraddr} addrData={addrData} setAddrData={setAddrData}
                    boxData={boxData} setBoxData={setBoxData} 
                    folderList={folderList} setFolderList = {setFolderList}
                    assoData={assoData} setAssoData ={setAssoData}/>    
                </Sider>
                <Content
                    style={{
                    padding: '24px',
                    backgroundColor: 'var(--semi-color-bg-0)',
                }}>
                    <ContentMain useraddr = {useraddr} setUseraddr = {setUseraddr} addrData={addrData} setAddrData={setAddrData}
                    boxData={boxData} setBoxData={setBoxData} detailData={detailData} setDetailData={setDetailData}
                    folderList={folderList} setFolderList = {setFolderList}
                    assoData={assoData} setAssoData ={setAssoData}
                    />
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