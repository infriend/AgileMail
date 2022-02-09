import React ,{useMemo}from 'react';
import { Col, Layout, Row,Table, Button,Toast} from '@douyinfe/semi-ui';
import { useNavigate } from 'react-router-dom';
import api from '../../api/api'
const AlreadySend = ({useraddr,setUseraddr,boxData,setBoxData}) => {
    const navigate = useNavigate()
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
            }

        },
        {
            title: '主题',
            dataIndex: 'subject',
            width:500,
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
                
            }
        },
    ];
    const data = boxData
    const rowSelection = {
        onSelect: (record, selected) => {
            console.log(`select row: ${selected}`, record);
        },
        onSelectAll: (selected, selectedRows) => {
            console.log(`select all rows: ${selected}`, selectedRows);
        },
        onChange: (selectedRowKeys, selectedRows) => {
            console.log(`selectedRowKeys: ${selectedRowKeys}`, 'selectedRows: ', selectedRows);
        },
    };

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
                onClick={() => Toast.success('删除成功')}>删除记录</Button>
                <Button style={{marginTop: 12,width:100}}>转发</Button>
            </div></>
    )
}
export default AlreadySend