import React ,{useMemo}from 'react';
import { Col, Layout, Row,Table, Popconfirm, Button, Toast} from '@douyinfe/semi-ui';
import { useNavigate } from 'react-router-dom';
import api from '../../api/api'
const DeletedBox = ({useraddr,setUseraddr,boxData,setBoxData}) => {
    var all
    if(boxData == undefined){
        all = 0
        api.getDeleteList(useraddr,boxData,setBoxData)
    }
    else all = boxData.length
    const navigate = useNavigate()
    const columns = [
        {
            title: '发信人',
            dataIndex: 'from',
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
            title: '收件邮箱',
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
    const onconfirm = () => {
        Toast.success('确认删除！')
    }
    return(
        <><div>
            <h4>已删除，一共{all}封</h4>
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
                <Popconfirm
                    title="确定是否要彻底删除？"
                    content="此修改将不可逆"
                    onConfirm={onconfirm}
                    onCancel={() => Toast.warning('取消删除！')}
                >
                <Button type='primary' theme='solid' style={{ width: 100, marginTop: 12, marginRight: 30,marginLeft:30 }}>彻底删除</Button>
                </Popconfirm>
                <Button style={{marginTop: 12,width:100}}>转发</Button>
            </div></>
    )
}
export default DeletedBox