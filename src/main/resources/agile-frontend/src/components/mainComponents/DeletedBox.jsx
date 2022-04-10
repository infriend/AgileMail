import React ,{useMemo}from 'react';
import { Col, Layout, Row,Table, Popconfirm, Button, Toast} from '@douyinfe/semi-ui';
import { useNavigate,useSearchParams } from 'react-router-dom';
import api from '../../api/api'
const DeletedBox = ({useraddr,setUseraddr,boxData,setBoxData,detailData,setDetailData,folderList,setFolderList}) => {
    const navigate = useNavigate()
    useraddr = JSON.parse(localStorage.getItem("userdata"))
    var all = 0,notRead = 0
    var bidcurr
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
    const boxname = findName(folderList,params.get('bid'))
    if(boxData == undefined){
        bidcurr = params.get('bid')
        api.getMailList(bidcurr,useraddr,setBoxData)
    }
    else {
        all = boxData.length
    }
    const mailOnclick = async(id) => {
        var url = '/main/readmail?id='+params.get('bid')+"_"+id
        const sth = await api.getMailDetail(params.get('bid'),useraddr,id,setDetailData)
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
            width:500,
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
            title: '收件邮箱',
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

    const pagination = useMemo(() => ({
        pageSize: 7
    }), []);
    const onconfirm = () => {
        var issuccess = true//test
        if(issuccess){
            console.log(selectedobj);
            for(var i = 0; i < selectedobj.length; i++){
                //api.deletedTotoallyListPost(useraddr,selectedobj[i])
            }
            Toast.success('彻底删除')
            //api.getDeleteList(useraddr,boxData,setBoxData)
            navigate('/main/deleted')
        }
            
        else
            Toast.error('删除失败')
    }
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
                <Table columns={columns} dataSource={data} rowSelection={rowSelection} pagination={pagination} rowKey="uid"/>
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