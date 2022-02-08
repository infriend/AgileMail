import React ,{useMemo}from 'react';
import { Col, Layout, Row,Table, Button,Typography,Toast} from '@douyinfe/semi-ui';
import testdata from '../../data/testdata.json'
import { useNavigate } from 'react-router-dom';
const Inbox = ({useraddr,setUseraddr}) => {
    const navigate = useNavigate()
    var all = 60
    var notRead = 5
    const {Text,Paragraph} = Typography
    const columns = [
        {
            title: '发信人',
            dataIndex: 'fromName',
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
            dataIndex: 'mainTitle',
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
            dataIndex: 'updateTime',
            render: (text, record, index) => {
                return (
                    <div onClick={() => navigate('/main/readmail') }>
                        {text}
                    </div>
                );
            }
        },
        {
            title: '邮箱类型',
            dataIndex: 'mailType',
            render: (text, record, index) => {
                return(
                    <div onClick={() => navigate('/main/readmail') }>
                {text}
            </div>
                );
            },
            filters: [
                {
                    text: 'GMail',
                    value: 'GMail',
                },
                {
                    text: '163mail',
                    value: '163mail',
                },
            ],
            onFilter: (value,text, record) => {record.mailType.includes(value)},
            
        },
    ];
    const data = testdata.inbox_Data
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
            <h4>收件箱，一共{all}封，未读{notRead}封</h4>
        </div>
            <div
                style={{
                    borderRadius: '10px',
                    border: '1px solid var(--semi-color-border)',
                    height: 'auto',
                    padding: '16px',
                }}
            >
                <Table columns={columns} dataSource={data} rowSelection={rowSelection} pagination={pagination} />
                <Button type='primary' theme='solid' style={{ width: 100, marginTop: 12, marginRight: 30,marginLeft:30 }}
                onClick={() => Toast.success('删除成功')}>删除邮件</Button>
                <Button style={{marginTop: 12,width:100}}>转发</Button>
            </div></>
    )
}
export default Inbox