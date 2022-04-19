import React ,{useMemo,useRef,useEffect,useState}from 'react';
import { Rating,Table, Button,Toast,Popconfirm,Typography,Cascader } from '@douyinfe/semi-ui';
import api from '../../api/api'
import { useNavigate,useSearchParams } from 'react-router-dom';
import { IconMailStroked1,IconInbox } from '@douyinfe/semi-icons';
const Inbox = ({useraddr,setUseraddr,boxData,setBoxData,detailData,setDetailData,folderList,setFolderList}) => {
    const navigate = useNavigate()
    const { Text } = Typography;
    console.log(useraddr)
    //useraddr = localStorage.getItem("currmail")
    //console.log(useraddr)
    const [params] = useSearchParams()
    var all = 0,notRead = 0,recent = 0
    var bidcurr
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
            if(list[i].folderId === bid){
                return {
                    all : list[i].total,
                    notread : list[i].unread,
                    recent: list[i].recent
                }
            }
                
        }
        return ans
    }
    const boxname = findName(folderList,params.get('bid'))
    if(boxData == undefined){
        bidcurr = params.get('bid')
        api.getMailList(bidcurr,useraddr,setBoxData)
    }
    else {
        
        let ans = findInf(folderList,params.get('bid'))
        all = ans.all
        recent = ans.recent
        notRead = ans.notread
        console.log(ans)
        console.log(all)

    }
    
    const mailOnclick = async(id) => {
        var url = '/main/readmail?id='+params.get('bid')+"_"+id
        const sth = await api.getMailDetail(params.get('bid'),useraddr,id,setDetailData)
        navigate(url)
    }
    const starOnchange = (val,id) => {
        //console.log("当前flag:"+ val)
        let flag = val?true:false
        api.flagMail(params.get('bid'),flag,useraddr,id)
    } 
    const readOnclick = (seen,id) =>{
        bidcurr = params.get('bid')
        api.setReadStatusMail(params.get('bid'),!seen,useraddr,id)
        api.getMailList(bidcurr,useraddr,setBoxData)
        data = boxData
    }
    const columns = [
        {
            dataIndex: 'seen',
            width:'auto',
            render: (text,record,index) => {
                var id = record.uid
                if(text){
                    return(
                        <IconInbox size="large" style={{color:'rgba(var(--semi-grey-5), 1)'}}
                        onClick={()=>{readOnclick(record.seen,id)}}/>
                    )
                }else{
                    return(
                        <IconMailStroked1 size="large" style={{color:'rgba(var(--semi-amber-5), 1)'}}
                        onClick={()=>{readOnclick(record.seen,id)}}/>
                    )
                }

            }

        },
        {
            title: '发信人',
            dataIndex: 'from',
            width: 'auto',
            render: (text, record, index) => {
                var id = record.uid
                //console.log(record.from[0])
                //console.log(record.from[0].includes('@nju.edu.cn>'))
                if(record.seen){//false表示最近还没看过
                    return (
                        <div onClick={()=>mailOnclick(id)} >
                            <Text>{text[0]}</Text>
                        </div>
                    );
                }else{
                    return (

                        <div onClick={()=>mailOnclick(id)} >
                            <Text strong>{text[0]}</Text>
                        </div>
                    );

                }

            },
            filters: [
                {
                    text: 'NJUmail',
                    value: '@nju.edu.cn>',
                },
                {
                    text: 'foxmail',
                    value: '@foxmail.com',
                },
                {
                    text: '163',
                    value: '@163.com',
                },
                {
                    text: 'qqmail',
                    value: '@qq.com',
                },
                {
                    text: 'NJU_Smail',
                    value: '@smail.nju.edu.cn>',
                },
                
            ],
            onFilter: (value, record) => record.from[0].includes(value)

        },
        {
            title: '主题',
            dataIndex: 'subject',
            width:450,
            render: (text, record, index) => {
                var id = record.uid
                if(record.seen){
                    return (
                        <div onClick={()=>mailOnclick(id)}>
                            <Text>{text}</Text>
                        </div>
                    );
                }else{
                    return (
                        <div onClick={()=>mailOnclick(id)}>
                            <Text strong>{text}</Text>
                        </div>
                    );
                }
            }
        },
        {
            title: '收信日期',
            dataIndex: 'datetime',
            render: (text, record, index) => {
                var id = record.uid
                if(record.seen){
                    return (
                        <div onClick={()=>mailOnclick(id)}>
                            <Text>{text}</Text>
                        </div>
                    );
                }else{
                    return (
                        <div onClick={()=>mailOnclick(id)}>
                            <Text strong>{text}</Text>
                        </div>
                    );
                }
            }
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
    var data = boxData
    console.log(data)
    //console.log(folderList)
    var selectedobj = {}
    //const [selectedobj,setSelectedObj] =useState()
    const rowSelection = {
        onSelect: (record, selected) => {
            //console.log(`select row: ${selected}`, record);
        },
        onSelectAll: (selected, selectedRows) => {
            //console.log(`select all rows: ${selected}`, selectedRows);
        },
        onChange: (selectedRowKeys, selectedRows) => {
            //console.log(`selectedRowKeys: ${selectedRowKeys}`, 'selectedRows: ', selectedRows);
            selectedobj = selectedRows
            //setSelectedObj(selectedRows)
            //console.log(selectedobj)
        },
    };
    const pagination = useMemo(() => ({
        pageSize: 7
    }), []);
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
                navigate('/main/inbox?bid='+params.get('bid'))
                //navigate('/main/')
            }
        }       
    }
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
        console.log(value)
        setVal(value)
    }
    const onconfirm = (obj) => {
        console.log(obj)
        let maillist = obj.map(target => {
            return target.uid
            })
       // console.log(val[val.length-1])
        //console.log(maillist)
        api.moveMail(params.get('bid'),val[val.length-1],maillist,useraddr)
    }
    return(
        <><div>
            <h4>{boxname}，一共{all}封，新邮件{recent}封，未读{notRead}封</h4>
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
                <Popconfirm
                
                    title="移动邮件至："
                    content={        <Cascader
                        style={{ width: 300 }}
                        treeData={treeData}
                        placeholder="请选择移动邮件的位置"
                        value={val}
                        onChange={e => onChange(e)}
                    />}
                    onConfirm={()=>onconfirm(selectedobj)}
                    //onCancel={() => Toast.warning('取消删除！')}
                >
                    <Button style={{marginTop: 12,width:100}}>移动邮件</Button>
                </Popconfirm>
                
            </div></>
    )
}
export default Inbox