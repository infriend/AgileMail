import React ,{useMemo,useState}from 'react';
import { Popconfirm,Table, Button,Toast,Modal, Form} from '@douyinfe/semi-ui';
import api from '../../api/api'
import { useNavigate,useSearchParams } from 'react-router-dom';
const AddressBook= ({useraddr,setUseraddr,addrData,setAddrData}) => {
    const navigate = useNavigate()
    const [params] = useSearchParams()
    //useraddr = JSON.parse(localStorage.getItem("userdata"))
    var all
    if(addrData === undefined) {
        if(JSON.parse(localStorage.getItem("addrData"))!== null)
            all = JSON.parse(localStorage.getItem("addrData")).length
        //api.getAddrBook(useraddr,addrData,setAddrData)
        //api.getContact(setAddrData)
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
            dataIndex: 'contactEmail',
            width:500,
        },
    ];
    var data
    if(addrData === undefined){
        data = JSON.parse(localStorage.getItem("addrData"))
    }else{
        data = addrData
    }
    console.log(data)
    //const [selectedobj,setSelectedObj] =useState()
    var selectedobj = {}
    const [addContactMail, setAddContactMail] = useState()
    const rowSelection = {
        onSelect: (record, selected) => {
            //console.log(`select row: ${selected}`, record);
        },
        onSelectAll: (selected, selectedRows) => {
            //console.log(`select all rows: ${selected}`, selectedRows);
        },
        onChange: (selectedRowKeys, selectedRows) => {
            //console.log(`selectedRowKeys: ${selectedRowKeys}`, 'selectedRows: ', selectedRows);
            //setSelectedObj(selectedRows)
            selectedobj = selectedRows
        },
    };
    const onconfirm = () => {
        if(selectedobj.length === undefined){
            Toast.error('删除列表为空！')
        }else{
            console.log(selectedobj)
            let contactlist = selectedobj.map(target => {
            return target.id
            })
            if(contactlist.length === 0){
            Toast.error('删除列表为空！')
            }else{
                console.log(contactlist)
                //此处应有删除联系人的api
                api.deleteContact(contactlist[0])
                Toast.success('删除成功')
                navigate('/main/addressbook')
            }
        } 
    }
    const submitContact = () => {
        //console.log(addContactMail)
        //console.log(useraddr)
        api.addContact(addContactMail.contactAddr,addContactMail.contactName)
        console.log(addContactMail);
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
                content={ <Form onValueChange={values=>{setAddContactMail(values)}}>
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