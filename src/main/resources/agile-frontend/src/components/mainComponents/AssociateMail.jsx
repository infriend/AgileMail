import React ,{useMemo,useState}from 'react';
import { Popconfirm,Table, Button,Toast,Modal, Form} from '@douyinfe/semi-ui';
import api from '../../api/api'
import { useNavigate,useSearchParams } from 'react-router-dom';
const AssociateMail= ({useraddr,setUseraddr}) => {
    const navigate = useNavigate()
    const [params] = useSearchParams()
    //useraddr = JSON.parse(localStorage.getItem("userdata"))
    var all
    /*if(assoData === undefined) {
        all = 0
        //api.getAddrBook(useraddr,addrData,setAddrData)
        //api.getContact(setAddrData)
        }
    //else all = addrData.length*/

    const columns = [
        {
            title: '邮箱',
            dataIndex: 'emailAddress',
            width:'auto',
        },
        {
            title: '域名',
            dataIndex: 'domain',
            width: 'auto',

        }
    ];
    var t = JSON.parse(localStorage.getItem("associatedList"))
    var data
    if(t != undefined && t != null && t.length !=undefined){
        data = t
    }else{
        data = []
    }
    
    if(data !== undefined){
        all = data.length
    }
    //console.log(data)
    const [selectedobj,setSelectedObj] =useState()
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
    const submitContact = () => {
        console.log("value")
        let domain = addContactMail.addMailAddr.split("@")[1]
        console.log(domain)
        api.associateNewAddr(addContactMail.addMailAddr,domain,addContactMail.addMailPasswd)
        console.log(addContactMail);
    }
    const pagination = useMemo(() => ({
        pageSize: 7
    }), []);
    return(
        <><div>
            <h4>关联邮箱，一共{all}个关联邮箱</h4>
        </div>
            <div
                style={{
                    borderRadius: '10px',
                    border: '1px solid var(--semi-color-border)',
                    height: 'auto',
                    padding: '16px',
                }}
            >
                <Table columns={columns} dataSource={data} rowSelection={rowSelection} pagination={pagination} rowKey="emailAddress"/>
                <Popconfirm
                    title="确定是否要彻底删除关联邮箱？"
                    content="此修改将不可逆"
                    onConfirm={onconfirm}
                    onCancel={() => Toast.warning('取消删除！')}
                >
                <Button type='primary' theme='solid' style={{ width: 100, marginTop: 12, marginRight: 30,marginLeft:30 }}>删除关联邮箱</Button>
                </Popconfirm>
                <Popconfirm
                
                title="添加关联邮箱："
                content={ <Form onValueChange={values=>{setAddContactMail(values)}}>
                    <Form.Input label={{ text: (<span>邮箱地址</span>), required: true }}field='addMailAddr' showClear />
                    <Form.Input label={{ text: (<span>密码</span>), required: true }}field='addMailPasswd' showClear />
                </Form>}
                onConfirm={submitContact}
                //onCancel={() => Toast.warning('取消删除！')}
            >
                <Button style={{marginTop: 12,width:100}}>添加邮箱</Button>
            </Popconfirm>
            </div></>
    )
}
export default AssociateMail