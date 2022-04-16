import React ,{useMemo}from 'react';
import { Col, Layout, Row,Table, Button,Toast} from '@douyinfe/semi-ui';
import { useNavigate, useSearchParams } from 'react-router-dom';
import api from '../../api/api'
const AlreadySend = ({useraddr,setUseraddr,boxData,setBoxData,detailData,setDetailData,folderList,setFolderList}) => {
    const navigate = useNavigate()
    useraddr = JSON.parse(localStorage.getItem("userdata"))
    var all,bidcurr
    const [params] = useSearchParams()
    folderList = JSON.parse(localStorage.getItem("folderList"))
    const findName = (list,bid) =>{
        let resname
        for(let obj in list){
            console.log(list[obj])
            if(list[obj].children.length !== 0){
                console.log("children")
                resname = findName(list[obj].children,bid)
                if(resname !== undefined)
                    return resname
            }
            if(list[obj].folderId === bid)
                return list[obj].name
        }
        return resname
    }
    const findInf = (list,bid) => {
        let ans
        for(let i in list){
            if(list[i].children.length !== 0){
                ans = findInf(list[i].children,bid)
                if(ans !== undefined)
                    return ans
            }
            if(list[i].folderId === bid)
                return list[i].message
        }
        return ans
    }
    const boxname = findName(folderList,params.get('bid'))
    if(boxData == undefined){
        all = 0
        bidcurr = params.get('bid')
        api.getMailList(bidcurr,useraddr,setBoxData)
    }
    else {
        all = findInf(folderList,params.get('bid'))
    }
    const mailOnclick= async(id) =>{
        var url = '/main/readmail?id='+params.get('bid')+"_"+id
        const sth = await api.getMailDetail(params.get('bid'),useraddr,id,setDetailData)
        navigate(url)
    }
    const columns = [
        {
            title: '收信人',
            dataIndex: 'replyTo',
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
            onFilter: (value, record) => record.to.includes(value)

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
            title: '更新日期',
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
            title: '发送邮箱',
            dataIndex: 'fromEmailAccount',
            render: (text, record, index) => {
                var id = record.uid
                return(
                    <div onClick={()=>mailOnclick(id)}>
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
        if(selectedobj.length === undefined){
            Toast.error('删除列表为空！')
        }else{
            let maillist = selectedobj.map(target => {
            return target.uid
            })
            if(maillist.length === 0){
            Toast.error('删除列表为空！')
            }else{
                console.log(maillist)
                api.putMailIntoTrash(params.get('bid'),useraddr,maillist)
                Toast.success('删除成功')
                navigate('/main/alreadySent?bid='+params.get('bid'))
                //navigate('/main/')
            }
        }       
    }
    const pagination = useMemo(() => ({
        pageSize: 7
    }), []);
    return(
        <><div>
            <h4>{boxname}，一共{all}封</h4>
        </div>
            <div
                style={{
                    borderRadius: '10px',
                    border: '1px solid var(--semi-color-border)',
                    height: 'auto',
                    padding: '16px',
                }}
            >
                <Table columns={columns} dataSource={data} rowSelection={rowSelection} pagination={pagination} rowKey="uid" />
                <Button type='primary' theme='solid' style={{ width: 100, marginTop: 12, marginRight: 30,marginLeft:30 }}
                onClick={deleteOnclick}>删除记录</Button>
                <Button style={{marginTop: 12,width:100}}>转发</Button>
            </div></>
    )
}
export default AlreadySend