import React ,{useMemo}from 'react';
import { Col, Layout, Row,Table, Button,Toast} from '@douyinfe/semi-ui';
import Icon, { IconMore} from '@douyinfe/semi-icons'
import { useNavigate } from 'react-router-dom';
const DraftBox = () => {
    const navigate = useNavigate()
    var all = 60
    const columns = [
        {
            title: '收信人',
            dataIndex: 'toName',
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
                    <div onClick={() => navigate('/main/readmail')}>
                        {text}
                    </div>
                );
            }
        },
        {
            title: '',
            dataIndex: 'operate',
            render: () => {
                return <IconMore />;
            }
        },
    ];
    const data = [
        {
            key: '1',
            mainTitle: 'Semi Design 设计稿.fig',
            toName: '姜鹏志',
            updateTime: '2020-02-02 05:13',

        },
        {
            key: '2',
            mainTitle: 'Semi Design 分享演示文稿',
            toName: '郝宣',
            updateTime: '2020-01-17 05:31',
        },
        {
            key: '3',
            mainTitle: '设计文档',
            toName: 'Zoey Edwards',
            updateTime: '2020-01-26 11:01',
        },
        {
            key: '4',
            mainTitle: 'Semi Pro 设计稿.fig',
            toName: '姜鹏志',
            updateTime: '2020-02-02 05:13',

        },
        {
            key: '5',
            mainTitle: 'Semi Pro 分享演示文稿',
            toName: '郝宣',
            updateTime: '2020-01-17 05:31',
        },
        {
            key: '6',
            mainTitle: 'Semi Pro 设计文档',
            toName: 'Zoey Edwards',
            updateTime: '2020-01-26 11:01',
        },
    ];
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
            <h4>草稿箱，一共{all}封</h4>
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
                onClick={() => Toast.success('删除成功')}>删除草稿</Button>
                <Button style={{marginTop: 12,width:100}}>转发</Button>
            </div></>
    )
}
export default DraftBox