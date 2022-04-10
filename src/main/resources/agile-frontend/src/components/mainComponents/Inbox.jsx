import React ,{useMemo,useRef,useEffect}from 'react';
import { Col, Layout, Row,Table, Button,Toast,Dropdown} from '@douyinfe/semi-ui';
import api from '../../api/api'
import { useNavigate,useSearchParams } from 'react-router-dom';
import { useState } from 'react';
const Inbox = ({useraddr,setUseraddr,boxData,setBoxData,detailData,setDetailData,folderList,setFolderList}) => {
    const navigate = useNavigate()
    useraddr = JSON.parse(localStorage.getItem("userdata"))
    const [mailData,setMailData] = useState()
    var all = 0,notRead = 0
    var bidcurr
    const [params] = useSearchParams()
    if(boxData == undefined){
        bidcurr = params.get('bid')
        api.getMailList(bidcurr,useraddr,setBoxData)
    }
    else {
        all = boxData.length
    }
    folderList = JSON.parse(localStorage.getItem("folderList"))
    const mailOnclick = (id) => {
        var url = '/main/readmail?id='+params.get('bid')+"_"+id
        navigate(url)
    }
    const columns = [
        {
            title: '发信人',
            dataIndex: 'from',
            width: 'auto',
            render: (text, record, index) => {
                var id = record.uid
                return (
                    <div onClick={()=>mailOnclick(id)}>
                        {text[0]}
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
            onFilter: (value, record) => record.from.includes(value)

        },
        {
            title: '主题',
            dataIndex: 'subject',
            width:450,
            render: (text, record, index) => {
                var id = record.uid
                return (
                    <div onClick={()=>mailOnclick(id)}>
                        {text}
                    </div>
                );
            }
        },
        {
            title: '收信日期',
            dataIndex: 'datetime',
            render: (text, record, index) => {
                var id = record.uid
                return (
                    <div onClick={()=>mailOnclick(id)}>
                        {text}
                    </div>
                );
            }
        },
        {
            title: '收信邮箱',
            dataIndex: 'fromEmailAccount',
            width: 'auto',
            render: (text, record, index) => {
                var id = record.uid
                return(
                    <div onClick={()=>mailOnclick(id)}>
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
            onFilter: (value, record) => record.fromEmailAccount.includes(value),
            
        },
    ];
    const data = boxData
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
            //console.log(selectedobj)
        },
    };
    const pagination = useMemo(() => ({
        pageSize: 7
    }), []);
    const deleteOnclick = ()=> {
        var issuccess = true//test
        let newdata = boxData
        if(issuccess){
            
            for(var i = 0; i < selectedobj.length; i++){
                //api.deletedInboxListPost(useraddr,selectedobj[i])
            }
            Toast.success('删除成功')
            //api.getInboxList(useraddr,boxData,setBoxData)
            navigate('/main/inbox')
        }
        else
            Toast.error('删除失败')
    }
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
                <Table columns={columns} dataSource={data} rowSelection={rowSelection} pagination={pagination} rowKey="uid"/>
                <Button type='primary' theme='solid' style={{ width: 100, marginTop: 12, marginRight: 30,marginLeft:30 }}
                onClick={deleteOnclick}>删除邮件</Button>
                <Button style={{marginTop: 12,width:100}}>转发</Button>
            </div></>
    )
}
export default Inbox