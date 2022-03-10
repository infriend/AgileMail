import React ,{useMemo}from 'react';
import { Col, Layout, Row,Table, Button,Toast} from '@douyinfe/semi-ui';
import { useNavigate } from 'react-router-dom';
import api from '../../api/api'
const AlreadySend = ({useraddr,setUseraddr,boxData,setBoxData}) => {
    const navigate = useNavigate()
    useraddr = JSON.parse(localStorage.getItem("userdata"))
    var all = 0
    if(boxData == undefined){
        all = 0
        api.getSentList(useraddr,boxData,setBoxData)
    }
    else all = boxData.length
    const columns = [
        {
            title: '收信人',
            dataIndex: 'to',
            width: 'auto',
            render: (text, record, index) => {
                return (
                    <div onClick={() => navigate('/main/readmail')}>
                        {text}
                    </div>
                );
            },
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
            onFilter: (value, record) => record.to.includes(value)

        },
        {
            title: '主题',
            dataIndex: 'subject',
            width:450,
            render: (text, record, index) => {
                return (
                    <div onClick={() => navigate('/main/readmail')}>
                        {text}
                    </div>
                );
            }
        },
        {
            title: '更新日期',
            dataIndex: 'datetime',
            render: (text, record, index) => {
                return (
                    <div onClick={() => navigate('/main/readmail')}>
                        {text}
                    </div>
                );
            }
        },
        {
            title: '发送邮箱',
            dataIndex: 'fromEmailAccount',
            render: (text, record, index) => {
                return(
                    <div onClick={() => navigate('/main/readmail') }>
                            {text}
                    </div>
                )
                
            },
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
    const data = boxData
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
    const deleteOnclick = ()=> {
        var issuccess = true//test
        //let newdata = boxData
        if(issuccess){
            console.log(selectedobj);
            for(var i = 0; i < selectedobj.length; i++){
                api.deletedSentListPost(useraddr,selectedobj[i])
                //newdata.splice(i)
            }
            //setBoxData(newdata)
            Toast.success('删除成功')
            api.getSentList(useraddr,boxData,setBoxData)
            navigate('/main/alreadySent')
        }
            
        else
            Toast.error('删除失败')
    }
    const pagination = useMemo(() => ({
        pageSize: 7
    }), []);
    return(
        <><div>
            <h4>已发送，一共{all}封</h4>
        </div>
            <div
                style={{
                    borderRadius: '10px',
                    border: '1px solid var(--semi-color-border)',
                    height: 'auto',
                    padding: '16px',
                }}
            >
                <Table columns={columns} dataSource={data} rowSelection={rowSelection} pagination={pagination} rowKey="id" />
                <Button type='primary' theme='solid' style={{ width: 100, marginTop: 12, marginRight: 30,marginLeft:30 }}
                onClick={deleteOnclick}>删除记录</Button>
                <Button style={{marginTop: 12,width:100}}>转发</Button>
            </div></>
    )
}
export default AlreadySend