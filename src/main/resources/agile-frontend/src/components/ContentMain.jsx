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
import AssociateMail from './mainComponents/AssociateMail';
const ContentMain = ({useraddr,setUseraddr,addrData,setAddrData,boxData,setBoxData,
    detailData,setDetailData,folderList,setFolderList}) => {
    if (useraddr === undefined){
        useraddr = localStorage.getItem("userdata")
        }
    if (folderList === undefined){
        folderList = localStorage.getItem("folderList")
    }
    return(
        <>
        <Routes>
            <Route index element={<InitialMain useraddr = {useraddr} setUseraddr = {setUseraddr}/>} />
            <Route path='/writemail' element={<ContentWrite useraddr = {useraddr} setUseraddr = {setUseraddr}/>} />
            <Route path='/inbox' element={<Inbox useraddr = {useraddr} setUseraddr = {setUseraddr}
                                                        boxData={boxData} setBoxData={setBoxData}
                                                        detailData={detailData} setDetailData={setDetailData}
                                                        folderList={folderList} setFolderList = {setFolderList}
                                                         />} />
            <Route path='/addressbook' element={<AddressBook useraddr = {useraddr} setUseraddr = {setUseraddr} 
                                                        addrData={addrData} setAddrData={setAddrData}/>} />
            <Route path='/associateMail' element={<AssociateMail useraddr = {useraddr} setUseraddr = {setUseraddr} 
                                                        addrData={addrData} setAddrData={setAddrData}/>} />                                                        
            <Route path='/draft' element={<DraftBox useraddr = {useraddr} setUseraddr = {setUseraddr}
                                                        boxData={boxData} setBoxData={setBoxData}
                                                        folderList={folderList} setFolderList = {setFolderList}
                                                        />} />
            <Route path='/alreadysent' element={<AlreadySend useraddr = {useraddr} setUseraddr = {setUseraddr}
                                                        boxData={boxData} setBoxData={setBoxData}
                                                        folderList={folderList} setFolderList = {setFolderList}
                                                        />} />
            <Route path='/deleted' element={<DeletedBox useraddr = {useraddr} setUseraddr = {setUseraddr}
                                                        boxData={boxData} setBoxData={setBoxData}
                                                        folderList={folderList} setFolderList = {setFolderList}
                                                         />} />
            <Route path='/readmail' element={<ReadMail useraddr = {useraddr} setUseraddr = {setUseraddr}
                                                        boxData={boxData} setBoxData={setBoxData}
                                                        detailData={detailData} setDetailData={setDetailData}
                                                        folderList={folderList} setFolderList = {setFolderList}
                                                        />} />
        </Routes>

        <Outlet />
        </>
    )
}
export default ContentMain