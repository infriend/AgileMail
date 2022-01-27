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
const ContentMain = () => {
    var username = "AgileUser123"
    return(
        <>
        <Routes>
            <Route index element={<InitialMain/>} />
            <Route path='/writemail' element={<ContentWrite/>} />
            <Route path='/inbox' element={<Inbox/>} />
            <Route path='/addressbook' element={<AddressBook/>} />
            <Route path='/draft' element={<DraftBox/>} />
            <Route path='/alreadysent' element={<AlreadySend/>} />
            <Route path='/deleted' element={<DeletedBox />} />
            <Route path='/readmail' element={<ReadMail />} />
        </Routes>

        <Outlet />
        </>
    )
}
export default ContentMain