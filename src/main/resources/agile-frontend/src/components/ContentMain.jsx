import React from 'react';
import { Col, Layout, Row,Skeleton,Breadcrumb} from '@douyinfe/semi-ui';
import InitialMain from './InnitialMain';
import ContentWrite from './ContentWrite';
import Inbox from './Inbox'
import AddressBook from './AddressBook';
import DraftBox from './DraftBox';
import AlreadySend from './AlreadySend';
import DeletedBox from './DeletedBox';
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
        </Routes>

        <Outlet />
        </>
    )
}
export default ContentMain