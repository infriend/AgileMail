import React from 'react';
import { Col, Layout, Row,Skeleton,Breadcrumb} from '@douyinfe/semi-ui';
import InitialMain from './mainComponents/InnitialMain';
import ContentWrite from './mainComponents/ContentWrite';
import Inbox from './mainComponents/Inbox'
import AddressBook from './mainComponents/AddressBook';
import DraftBox from './mainComponents/DraftBox';
import AlreadySend from './mainComponents/AlreadySend';
import DeletedBox from './mainComponents/DeletedBox';
import ReadMail from './mainComponents/ReadMail';
import { Routes, Route,Outlet } from "react-router-dom"
const ContentMain = ({useraddr,setUseraddr}) => {
    var username = "AgileUser123"
    return(
        <>
        <Routes>
            <Route index element={<InitialMain useraddr = {useraddr} setUseraddr = {setUseraddr}/>} />
            <Route path='/writemail' element={<ContentWrite useraddr = {useraddr} setUseraddr = {setUseraddr}/>} />
            <Route path='/inbox' element={<Inbox useraddr = {useraddr} setUseraddr = {setUseraddr}/>} />
            <Route path='/addressbook' element={<AddressBook useraddr = {useraddr} setUseraddr = {setUseraddr}/>} />
            <Route path='/draft' element={<DraftBox useraddr = {useraddr} setUseraddr = {setUseraddr}/>} />
            <Route path='/alreadysent' element={<AlreadySend useraddr = {useraddr} setUseraddr = {setUseraddr}/>} />
            <Route path='/deleted' element={<DeletedBox useraddr = {useraddr} setUseraddr = {setUseraddr} />} />
            <Route path='/readmail' element={<ReadMail useraddr = {useraddr} setUseraddr = {setUseraddr}/>} />
        </Routes>

        <Outlet />
        </>
    )
}
export default ContentMain