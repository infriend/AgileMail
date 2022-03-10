import React ,{useMemo,useState}from 'react';
import { Col, Layout, Row,Table, Button,Toast,Modal} from '@douyinfe/semi-ui';
import api from '../../api/api'
const AddressBook= ({useraddr,setUseraddr,addrData,setAddrData}) => {
    useraddr = JSON.parse(localStorage.getItem("userdata"))
    var all
    if(addrData == undefined) {
        all = 0
        api.getAddrBook(useraddr,addrData,setAddrData)
        }
    else all = addrData.length

    const columns = [
        {
            title: '姓名',
            dataIndex: 'name',
            width: 'auto',

        },
        {
            title: '邮箱',
            dataIndex: 'emailAddress',
            width:500,
        },
        {
            title: '来自邮箱',
            dataIndex: 'fromEmailAccount',
            width:'auto',
            filters: [
                {
                    text: 'GMail',
                    value: '@gmail.com',
                },
                {
                    text: '163mail',
                    value: '@163.com',
                },
            ],
            onFilter: (value, record) => record.fromEmailAccount.includes(value)
        },
    ];
    const data = addrData.contactList
    console.log(data)
    var selectedobj = {}
    const rowSelection = {
        onSelect: (record, selected) => {
            console.log(`select row: ${selected}`, record);
        },
        onSelectAll: (selected, selectedRows) => {
            console.log(`select all rows: ${selected}`, selectedRows);
        },
        onChange: (selectedRowKeys, selectedRows) => {
            console.log(`selectedRowKeys: ${selectedRowKeys}`, 'selectedRows: ', selectedRows);
            selectedobj = selectedRows
        },
    };
    const deletedOnclick = () =>{
        var issuccess = true//test
        if(issuccess){
            console.log(selectedobj);
            for(var i = 0; i < selectedobj.length; i++){
                api.deleteAddrListPost(useraddr,selectedobj[i])
            }
            Toast.success('删除成功')
            api.getAddrBook(useraddr,addrData,setAddrData)
        }
            
        else
            Toast.error('删除失败')
    }
    const pagination = useMemo(() => ({
        pageSize: 7
    }), []);
    return(
        <><div>
            <h4>通讯录，一共{all}位联系人</h4>
        </div>
            <div
                style={{
                    borderRadius: '10px',
                    border: '1px solid var(--semi-color-border)',
                    height: 'auto',
                    padding: '16px',
                }}
            >
                <Table columns={columns} dataSource={data} rowSelection={rowSelection} pagination={pagination} rowKey="id"/>
                <Button type='primary' theme='solid' style={{ width: 100, marginTop: 12, marginRight: 30,marginLeft:30 }}
                onClick={deletedOnclick}>删除联系人</Button>
                <Button style={{marginTop: 12,width:100}}>添加联系人</Button>
            </div></>
    )
}
export default AddressBook