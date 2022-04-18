import React ,{useMemo,useState}from 'react';
import { Popconfirm,Table, Button,Toast,Modal, Form} from '@douyinfe/semi-ui';
import api from '../../api/api'
import { useNavigate,useSearchParams } from 'react-router-dom';
const AddressBook= ({useraddr,setUseraddr,addrData,setAddrData}) => {
    const navigate = useNavigate()
    const [params] = useSearchParams()
    useraddr = JSON.parse(localStorage.getItem("userdata"))
    var all
    if(addrData === undefined) {
        all = 0
        //api.getAddrBook(useraddr,addrData,setAddrData)
        api.getContact(setAddrData)
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
    const data = addrData
    //console.log(addrData)
    const [selectedobj,setSelectedObj] =useState()
    const rowSelection = {
        onSelect: (record, selected) => {
            //console.log(`select row: ${selected}`, record);
        },
        onSelectAll: (selected, selectedRows) => {
            //console.log(`select all rows: ${selected}`, selectedRows);
        },
        onChange: (selectedRowKeys, selectedRows) => {
            //console.log(`selectedRowKeys: ${selectedRowKeys}`, 'selectedRows: ', selectedRows);
            setSelectedObj(selectedRows)
        },
    };
    const onconfirm = () => {
        if(selectedobj.length === undefined){
            Toast.error('删除列表为空！')
        }else{
            let contactlist = selectedobj.map(target => {
            return target.uid
            })
            if(contactlist.length === 0){
            Toast.error('删除列表为空！')
            }else{
                //console.log(contactlist)
                //此处应有删除联系人的api
                //api.
                Toast.success('删除成功')
                navigate('/main/addressbook')
            }
        } 
    }
    const submitContact = (value) => {
        console.log("value")
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
                <Table columns={columns} dataSource={data} rowSelection={rowSelection} pagination={pagination} rowKey="uid"/>
                <Popconfirm
                    title="确定是否要彻底删除联系人？"
                    content="此修改将不可逆"
                    onConfirm={onconfirm}
                    onCancel={() => Toast.warning('取消删除！')}
                >
                <Button type='primary' theme='solid' style={{ width: 100, marginTop: 12, marginRight: 30,marginLeft:30 }}>删除联系人</Button>
                </Popconfirm>
                <Popconfirm
                
                title="添加联系人："
                content={ <Form onSubmit={submitContact}>
                    <Form.Input label={{ text: (<span>姓名</span>), required: true }}field='contactName' showClear />
                    <Form.Input label={{ text: (<span>邮箱</span>), required: true }}field='contactAddr' showClear />
                </Form>}
                onConfirm={submitContact}
                //onCancel={() => Toast.warning('取消删除！')}
            >
                <Button style={{marginTop: 12,width:100}}>添加联系人</Button>
            </Popconfirm>
            </div></>
    )
}
export default AddressBook