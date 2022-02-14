import React, {useState} from 'react';
import { Col, Layout, Row } from '@douyinfe/semi-ui';
import {Nav } from '@douyinfe/semi-ui';
import Icon, { IconMailStroked1, IconUserGroup, IconSetting,IconEdit} from '@douyinfe/semi-icons'
import { useNavigate } from 'react-router-dom';
import api from '../api/api'
const SiderMain = ({useraddr,setUseraddr,addrData,setAddrData,boxData,setBoxData}) => {
    const navigate = useNavigate()
    useraddr = JSON.parse(localStorage.getItem("userdata"))
    const siderOnSelect = (data) =>{
        if(data.itemKey == 'addressbook'){
            api.getAddrBook(useraddr,addrData,setAddrData)
        }else if(data.itemKey == 'inbox'){
            api.getInboxList(useraddr,boxData,setBoxData)
            //console.log("siderinbox:"+boxData[0].subject)
        }else if(data.itemKey == 'draft'){
            api.getDraftList(useraddr,boxData,setBoxData)
        }else if(data.itemKey == 'alreadySent'){
            api.getSentList(useraddr,boxData,setBoxData)
        }else if(data.itemKey == 'deleted'){
            api.getDeleteList(useraddr,boxData,setBoxData)
        }
        navigate('/main/'+data.itemKey)
    }
    return(
        <Nav
        style={{ maxWidth: 220, height: '100%' }}
        defaultSelectedKeys={['Home']}
        items={[
            { itemKey: 'writemail', text: '写信', icon: <IconEdit size="large" />},
            { itemKey: 'inbox', text: '收信', icon: <IconMailStroked1 size="large" /> },
            { itemKey: 'addressbook', text: '通讯录', icon: <IconUserGroup size="large" /> },
            {
                text: '邮箱管理',
                icon: <IconSetting />,
                itemKey: 'management',
                items: [{itemKey:'draft',text: '草稿箱'}, {itemKey:'alreadySent',text: '已发送'},{itemKey:'deleted',text: '已删除'}],
            },   
        ]}
        onSelect={siderOnSelect}
        onClick={data => {console.log(data.itemKey)}}
        footer={{
            collapseButton: true,
        }}
    />
    )
}
export default SiderMain