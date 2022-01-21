import React from 'react';
import { Col, Layout, Row } from '@douyinfe/semi-ui';
import {Nav } from '@douyinfe/semi-ui';
import Icon, { IconMailStroked1, IconUserGroup, IconSetting,IconEdit} from '@douyinfe/semi-icons'
const SiderMain = () => {
    return(
        <Nav
        style={{ maxWidth: 220, height: '100%' }}
        defaultSelectedKeys={['Home']}
        items={[
            { itemKey: 'Home', text: '写信', icon: <IconEdit size="large" /> },
            { itemKey: 'Histogram', text: '收信', icon: <IconMailStroked1 size="large" /> },
            { itemKey: 'Live', text: '通讯录', icon: <IconUserGroup size="large" /> },
            {
                text: '邮箱管理',
                icon: <IconSetting />,
                itemKey: 'job',
                items: ['草稿箱', '已发送','已删除'],
            },   
        ]}
        footer={{
            collapseButton: true,
        }}
    />
    )
}
export default SiderMain