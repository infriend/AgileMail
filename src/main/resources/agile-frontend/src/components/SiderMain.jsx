import React, {useState} from 'react';
import { Col, Layout, Row } from '@douyinfe/semi-ui';
import {Nav } from '@douyinfe/semi-ui';
import Icon, { IconMailStroked1, IconUserGroup, IconSetting,IconEdit,IconPaperclip} from '@douyinfe/semi-icons'
import { useNavigate } from 'react-router-dom';
import api from '../api/api'
const SiderMain = ({useraddr,setUseraddr,addrData,setAddrData,boxData,setBoxData,folderList, setFolderList}) => {
    const navigate = useNavigate()
    //useraddr = JSON.parse(localStorage.getItem("userdata"))
    console.log(useraddr)
    var itemlist = [{ itemKey: 'writemail', text: '写信', icon: <IconEdit size="large" />},
    { itemKey: 'addressbook', text: '通讯录', icon: <IconUserGroup size="large" /> }]
    const turnIntoItem = (target) =>{
        if (target.children.length < 1){
            return{
                itemKey : target.category+"_"+target.folderId,
                text : target.name,
                icon: <IconMailStroked1 size="large" />,
            }
        }else{
            return{
                text :target.name,
                itemKey : target.category+"_"+target.folderId,
                icon: <IconSetting />,
                items : target.children.map(turnIntoItem)
            }
        }
    }
    if(folderList === undefined|| folderList === null ||folderList.length === null){
        let fl = localStorage.getItem("folderList")
        console.log(fl)
        if (fl != null){
            folderList = JSON.parse(localStorage.getItem("folderList"))
        }else{
            console.log(useraddr)
            api.getFolderList(useraddr,setFolderList)
        }
        folderList = JSON.parse(localStorage.getItem("folderList"))
            
    }else{
        console.log(folderList.length)
        if(folderList.length !== undefined && folderList !== null){
            //console.log(folderList)
            itemlist.push(...folderList.map(turnIntoItem))
            if(folderList.length > 0){
                let temp = itemlist[1]
                itemlist[1] = itemlist[2]
                itemlist[2] = temp
            }
        }
        itemlist.push({itemKey: 'associateMail', text: '设置关联邮箱', icon: <IconPaperclip size="large" />})
        //console.log(itemlist)
    }
    if(itemlist.length < 3){
        itemlist.push({itemKey: 'associateMail', text: '设置关联邮箱', icon: <IconPaperclip size="large" />})
    }
    console.log(folderList)
    const siderOnSelect = (data) =>{
        console.log(data)
        let tempaddr = ''
        if(data.itemKey.indexOf('addressbook') != -1){//api还没弄好
            tempaddr = data.itemKey
        }else if(data.itemKey === 'writemail'){//api还没弄好
            tempaddr = 'writemail'
        }else if(data.itemKey === 'associateMail'){
            tempaddr = 'associateMail'
        }
        else if(data.itemKey.indexOf('INBOX') != -1){
            let arr = data.itemKey.split("_")
            tempaddr = 'inbox?bid='+ arr[1]
            api.getMailList(arr[1],useraddr,setBoxData)
        }else if(data.itemKey.indexOf('DRAFT') != -1){
            let arr = data.itemKey.split("_")
            tempaddr = 'draft?bid='+arr[1]
            api.getMailList(arr[1],useraddr,setBoxData)
        }else if(data.itemKey.indexOf('SENT') != -1){
            let arr = data.itemKey.split("_")
            tempaddr = 'alreadySent?bid='+arr[1]
            api.getMailList(arr[1],useraddr,setBoxData)
        }else if(data.itemKey.indexOf('TRASH') != -1){
            let arr = data.itemKey.split("_")
            tempaddr = 'deleted?bid='+arr[1]
            api.getMailList(arr[1],useraddr,setBoxData)
        }else if(data.itemKey.indexOf('OTHER') != -1){
            let arr = data.itemKey.split("_")
            tempaddr = 'inbox?bid='+arr[1]
            api.getMailList(arr[1],useraddr,setBoxData)
        }
        //localStorage.setItem("boxName",data.text)
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