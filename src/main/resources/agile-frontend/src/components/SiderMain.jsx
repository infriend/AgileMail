import React, {useState} from 'react';
import { Col, Layout, Row } from '@douyinfe/semi-ui';
import {Nav } from '@douyinfe/semi-ui';
import Icon, { IconMailStroked1, IconUserGroup, IconSetting,IconEdit} from '@douyinfe/semi-icons'
import { useNavigate } from 'react-router-dom';
const SiderMain = ({useraddr,setUseraddr}) => {
    const navigate = useNavigate()
    
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
        onSelect={data => navigate('/main/'+data.itemKey)}
        onClick={data => {console.log(data.itemKey)}}
        footer={{
            collapseButton: true,
        }}
    />
    )
}
export default SiderMain