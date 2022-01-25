import React ,{useMemo}from 'react';
import { Col, Layout, Row,Table, Button} from '@douyinfe/semi-ui';
import Icon, { IconMore} from '@douyinfe/semi-icons'
const Inbox = () => {
    var all = 60
    var notRead = 5

    const columns = [
        {
            title: '发信人',
            dataIndex: 'fromName',
            width: 'auto',

        },
        {
            title: '主题',
            dataIndex: 'mainTitle',
            width:500,
        },
        {
            title: '更新日期',
            dataIndex: 'updateTime',
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
            fromName: '姜鹏志',
            updateTime: '2020-02-02 05:13',

        },
        {
            key: '2',
            mainTitle: 'Semi Design 分享演示文稿',
            fromName: '郝宣',
            updateTime: '2020-01-17 05:31',
        },
        {
            key: '3',
            mainTitle: '设计文档',
            fromName: 'Zoey Edwards',
            updateTime: '2020-01-26 11:01',
        },
        {
            key: '4',
            mainTitle: 'Semi Pro 设计稿.fig',
            fromName: '姜鹏志',
            updateTime: '2020-02-02 05:13',

        },
        {
            key: '5',
            mainTitle: 'Semi Pro 分享演示文稿',
            fromName: '郝宣',
            updateTime: '2020-01-17 05:31',
        },
        {
            key: '6',
            mainTitle: 'Semi Pro 设计文档',
            fromName: 'Zoey Edwards',
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
                <Button type='primary' theme='solid' style={{ width: 100, marginTop: 12, marginRight: 30,marginLeft:30 }}>删除邮件</Button>
                <Button style={{marginTop: 12,width:100}}>转发</Button>
            </div></>
    )
}
export default Inbox