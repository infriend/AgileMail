import React, {useState} from 'react';
import { Col, Layout, Row } from '@douyinfe/semi-ui';
import {Nav } from '@douyinfe/semi-ui';
import Icon, { IconMailStroked1, IconUserGroup, IconSetting,IconEdit} from '@douyinfe/semi-icons'
import { useNavigate } from 'react-router-dom';
import api from '../api/api'
const SiderMain = ({useraddr,setUseraddr,addrData,setAddrData,boxData,setBoxData,folderList, setFolderList}) => {
    const navigate = useNavigate()
    useraddr = JSON.parse(localStorage.getItem("userdata"))
    var itemlist = [{ itemKey: 'writemail', text: '写信', icon: <IconEdit size="large" />},
    { itemKey: 'addressbook', text: '通讯录', icon: <IconUserGroup size="large" /> }]
    const turnIntoItem = (target) =>{
        if (target.children.length < 1){
            return{
                itemKey : target.category+"_"+target.folderId,
                text : target.name,
                icon: <IconMailStroked1 size="large" />,
                //folderid : target.folderId,
            }
        }else{
            return{
                text :target.name,
                itemKey : target.category+target.folderId,
                //folderid : target.folderId,
                icon: <IconSetting />,
                items : target.children.map(turnIntoItem)
            }
        }
    }
    if(folderList === undefined){
        if (localStorage.getItem("folderList") != null){
            folderList = JSON.parse(localStorage.getItem("folderList"))
            //console.log(1)
        }else
            api.getFolderList(useraddr,setFolderList)
    }else{
        //console.log(1)
        console.log(folderList)
        itemlist.push(...folderList.map(turnIntoItem))
        let temp = itemlist[1]
        itemlist[1] = itemlist[2]
        itemlist[2] = temp
        //console.log(2)
        console.log(itemlist)
    }

    //console.log(folderList)
    const siderOnSelect = (data) =>{
        console.log(data)
        let tempaddr = ''
        if(data.itemKey.indexOf('addressbook') != -1){
            tempaddr = data.itemKey
            //api.getAddrBook(useraddr,addrData,setAddrData)
        }else if(data.itemKey === 'writemail'){
            tempaddr = 'writemail'
        }
        else if(data.itemKey.indexOf('INBOX') != -1){
            let arr = data.itemKey.split("_")
            tempaddr = 'inbox?bid='+ arr[1]
            api.getMailList(arr[1],useraddr,setBoxData)
        }else if(data.itemKey.indexOf('DRAFT') != -1){
            let arr = data.itemKey.split("_")
            tempaddr = 'draft?bid='+arr[1]
            api.getMailList(arr[1],useraddr,setBoxData)
        }else if(data.itemKey === 'SENT'){
            tempaddr = 'alreadySent'
            //api.getSentList(useraddr,boxData,setBoxData)
        }else if(data.itemKey === 'TRASH'){
            tempaddr = 'deleted'
            //api.getDeleteList(useraddr,boxData,setBoxData)
        }else if(data.itemKey === 'OTHER'){
            tempaddr = 'inbox'
        }
        navigate('/main/'+tempaddr)
    }
    return(
        <Nav
        style={{ maxWidth: 220, height: '100%' }}
        defaultSelectedKeys={['Home']}
        items={/*[
            { itemKey: 'writemail', text: '写信', icon: <IconEdit size="large" />},
            { itemKey: 'inbox', text: '收信', icon: <IconMailStroked1 size="large" /> },
            { itemKey: 'addressbook', text: '通讯录', icon: <IconUserGroup size="large" /> },
            {
                text: '邮箱管理',
                icon: <IconSetting />,
                itemKey: 'management',
                items: [{itemKey:'draft',text: '草稿箱'}, {itemKey:'alreadySent',text: '已发送'},{itemKey:'deleted',text: '已删除'}],
            },   
        ]*/itemlist}
        onSelect={siderOnSelect}
        onClick={data => {console.log(data.itemKey)}}
        footer={{
            collapseButton: true,
        }}
    />
    )
}
export default SiderMain