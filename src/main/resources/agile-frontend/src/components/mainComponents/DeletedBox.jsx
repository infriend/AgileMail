import React ,{useMemo,useState}from 'react';
import { Rating,Table, Popconfirm, Button, Toast,Cascader} from '@douyinfe/semi-ui';
import { useNavigate,useSearchParams } from 'react-router-dom';
import { IconInbox } from '@douyinfe/semi-icons';
import api from '../../api/api'
const DeletedBox = ({useraddr,setUseraddr,boxData,setBoxData,detailData,setDetailData,folderList,setFolderList}) => {
    const navigate = useNavigate()
    //useraddr = JSON.parse(localStorage.getItem("userdata"))
    var all = 0
    var bidcurr
    const [params] = useSearchParams()
    folderList = JSON.parse(localStorage.getItem("folderList"))
    const findName = (list,bid) =>{
        let resname
        for(let obj in list){
            //console.log(list[obj])
            if(list[obj].children.length !== 0){
                //console.log("children")
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
        bidcurr = params.get('bid')
        api.getMailList(bidcurr,useraddr,setBoxData)
    }
    else {
        all = findInf(folderList,params.get('bid'))
    }
    const mailOnclick = async(id) => {
        var url = '/main/readmail?id='+params.get('bid')+"_"+id
        const sth = await api.getMailDetail(params.get('bid'),useraddr,id,setDetailData)
        navigate(url)
    }
    const starOnchange = (val,id) => {
        let flag = val?true:false
        api.flagMail(params.get('bid'),flag,useraddr,id)
    } 
    
    const columns = [
        {
            dataIndex: 'seen',
            width:'auto',
            render: (text,record,index) => {
                return(
                    <IconInbox size="large" style={{color:'rgba(var(--semi-grey-5), 1)'}}
                    />
                )
            }
        },
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
        {
            dataIndex: 'flagged',
            width: 'auto',
            render:(text,record,index) => {
                let id = record.uid
                let flag = text?1:0
                return(
                    <Rating allowClear={true} defaultValue={flag} count={1}
                    onChange={(val)=>{starOnchange(val,id)}}/>
                )
            }
        }
    ];
    const data = boxData
    //const [selectedobj,setSelectedObj] =useState()
    var selectedobj = {}
    const rowSelection = {
        onSelect: (record, selected) => {
        },
        onSelectAll: (selected, selectedRows) => {
        },
        onChange: (selectedRowKeys, selectedRows) => {
            //setSelectedObj(selectedRows)
            selectedobj = selectedRows
        },
    };
        const turnIntoTree = (target) => {
        if (target.children.length < 1){//不跟有子节点
            if(target.folderId === params.get('bid')){//bid相同直接禁用
                return{
                    label : target.name,
                    value : target.folderId,
                    disabled: true,
                }
            }else{
                return{
                    label : target.name,
                    value : target.folderId
                }
            }

        }else{
            return{
                label : target.name,
                value : target.folderId,
                children : target.children.map(turnIntoTree)
            }
        }
    }
    const treeData = (folderList.map(turnIntoTree))
    const [val,setVal] = useState()
    const onChange = (value) => {
        //console.log(value)
        setVal(value)
    }
    const onconfirm2 = (obj) => {
        let maillist = obj.map(target => {
            return target.uid
            })
       // console.log(val[val.length-1])
        //console.log(maillist)
        api.moveMail(params.get('bid'),val[val.length-1],maillist,useraddr)
    }
    const pagination = useMemo(() => ({
        pageSize: 7
    }), []);
    const onconfirm = () => {
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
                api.deleteMail(params.get('bid'),useraddr,maillist)
                Toast.success('删除成功')
                navigate('/main/deleted?='+params.get('bid'))
                //navigate('/main/')
            }
        } 
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
                <Popconfirm
                
                title="移动邮件至："
                content={        <Cascader
                    style={{ width: 300 }}
                    treeData={treeData}
                    placeholder="请选择移动邮件的位置"
                    value={val}
                    onChange={e => onChange(e)}
                />}
                onConfirm={()=>onconfirm2(selectedobj)}
                //onCancel={() => Toast.warning('取消删除！')}
            >
                <Button style={{marginTop: 12,width:100}}>移动邮件</Button>
            </Popconfirm>
            </div></>
    )
}
export default DeletedBox