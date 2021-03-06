//import * as AxiosLogger from 'axios-logger'
import axios from 'axios'
//const baseUrl = 'http://localhost:8081'
//const baseUrl = '172.19.240.244:3000'
const baseUrl = 'http://172.19.240.244:8081'
// const baseUrl = 'http://localhost:3001'//json server
const setAuthToken = (token) => {
    if (token) {
      // headers 每个请求都需要用到的
      axios.defaults.headers.common["Authorization"] = token;
    } else {
      delete axios.defaults.headers.common["Authorization"];
    }
  }
/* 请求拦截 */
axios.interceptors.request.use(
    config => {
      if (localStorage.getItem("loginToken") != null) {
        config.headers["Authorization"] = localStorage.getItem("loginToken");
      }
  
      return config;
    },
    err => Promise.reject(err)
  );
  //---------注册登录----------
const loginPost = (username,domain,passwd,setCode) => {//登录邮箱 
    //let t = 'QWZ3ASASD'
    axios({
        method:'POST',
        url : `${baseUrl}/login`,
        data :{
            username: username,
	        password: passwd,
            
        }
    }).then(response => {
        const token = response.data.data
        const code = response.data.code
        setCode(code)
        localStorage.setItem("loginToken",token)
        setAuthToken(token)
    })
}
const registerPost = (username,passwd,setCode) => {//注册
    //let t = 'QWZ3ASASD'
    axios({
        method:'POST',
        url : `${baseUrl}/register`,
        data :{
            username: username,
            password: passwd
        }
    }).then(response => {
        const msg = response.data.data
        const code = response.data.code
        setCode(code)
        console.log(msg)
        //setAuthToken(t)
    })
}
//-------关联邮箱接口-----------
const getAssociatedAddrList = (setAssoData) => { //获取用户关联的邮箱列表
    axios({
        method: 'GET',
        url:`${baseUrl}/account/email/list`
        // url:`${baseUrl}/assoaccount`
    }).then(response=>{
        const data = response.data.data
        //console.log(response)
        //setAssoData(data)
        if(data.length > 0){
            let addr = data[0].emailAddress
            setAssoData(addr)
        }
        localStorage.setItem("associatedList",JSON.stringify(data))//在localstroage中存入总的list

    })
}
const associateNewAddr = (mailaddr,maildomain,mailpasswd,setCode) => {//关联新邮箱
    axios({
        method: 'POST',
        url:`${baseUrl}/account/email`,
        data :{
            emailAddress: mailaddr,
            password: mailpasswd, 
            domain: maildomain
        }
    }).then(response=>{//没有body
        //const code = response.data.code
        //setCode(code)
    })
}
const deleteAssociatedAddr = (mailaddr) => {
    axios({
        method: 'DELETE',
        url:`${baseUrl}/account/email`,
        data:{
            emailAddress: mailaddr
        }
    }).then(response=>{//没有body
    })
}
const switchMail = (mailaddr) => {
    console.log("switch")
    axios({
        method:'POST',
        url:`${baseUrl}/account/email/switch`,
        data:{
            emailAddress: mailaddr
        }
    }).then()
}
//-----读取文件夹与邮件接口-----
const getFolderList = (useraddr,setFolderList) => {//获取folder信息
    console.log(useraddr)
    axios({
        method:'GET',
        url: `${baseUrl}/folder`,
        params:{
            emailAddress : useraddr
        }

    }).then( response => {
        const inf = response.data.data//inf是一个数组
        console.log(inf)
        setFolderList(inf)
        localStorage.setItem("folderList",JSON.stringify(inf))
    })

}
const getMailList = (folderid,useraddr,setBoxData) => {//查看文件夹邮件列表
    axios({
        method:'GET',
        url:`${baseUrl}/${folderid}/list`,
        // url:`${baseUrl}/list`,
        params:{
            // folderId:folderid,
            emailAddress : useraddr
        }
    }).then(response => {
        //console.log("change")
        const data = response.data.data

        //console.log(data)
        setBoxData(data)
    })
}
const getMailDetail = async(folderid, useraddr, messageuid, setDetailData) => {//查看邮件
    await axios({
        method: 'GET',
        url: `${baseUrl}/${folderid}/message/${messageuid}`,
       // url: `${baseUrl}/testDetail`,
        params: {
            emailAddress: useraddr,
        }
    }).then(response => {
        const data = response.data.data;
        console.log(data);   
        localStorage.setItem("mailDetail", JSON.stringify(data))
        console.log(localStorage.getItem("mailDetail"))
        //console.log("fromapi2");  
        //setDetailData(data[0]);     
    });
}
const getAttachment = () => {//获取附件

}
//------修改文件夹与邮件接口-------
const moveMail = (folderid,toFolderId, messageidList,useraddr) =>{//移动邮件
    axios({
        method: 'PUT',
        url:`${baseUrl}/${folderid}/messages/folder/${toFolderId}`,
        data:{
            folderId: folderid,
            toFolderId: toFolderId,
            msgIds: messageidList,
            emailAddress: useraddr
        }
    }).then()

}
const putMailIntoTrash = (folderid,useraddr,messageidList) => {//删除邮件
    axios({
        method: 'PUT',
        url:`${baseUrl}/${folderid}/messages/trash`,
        data:{
            folderId : folderid,
        	emailAddress: useraddr,
	        msgIds: messageidList
        }
    }).then(response => {
        console.log(response.data)
        //更新folder信息
        let tempfolderInf = JSON.parse(localStorage.getItem("folderList"))
        for(let i in tempfolderInf){
            if(tempfolderInf[i].folderId === response.data.folderId){
                tempfolderInf[i] = response.data
                localStorage.setItem("folderList",JSON.stringify(tempfolderInf))
                break
            }
        }
    })

}
const deleteMail = (folderid,useraddr,messageidList) => {//彻底删除邮件
    axios({
        method: 'DELETE',
        url: `${baseUrl}/${folderid}/messages`,
        data:{
            folderId : folderid,
        	emailAddress: useraddr,
	        msgIds: messageidList
        }
    }).then(response => {
        let tempfolderInf = JSON.parse(localStorage.getItem("folderList"))
        for(let i in tempfolderInf){
            if(tempfolderInf[i].folderId === response.data.folderId){
                tempfolderInf[i] = response.data
                localStorage.setItem("folderList",JSON.stringify(tempfolderInf))
                break
            }
        }

    })
}
const setReadStatusMail = (folderid,seen,useraddr,messageidList) => {//设为已读/未读
    let list = []
    list.push(messageidList)
    axios({
        method: 'PUT',
        url:`${baseUrl}/${folderid}/messages/seen/${seen}`,
        data:{
            folderId : folderid,
            seen: seen,
        	emailAddress: useraddr,
	        msgIds: list
            }
    }).then()
}
const flagMail = (folderid,flagged,useraddr,messageidList) => {//设为已标记/取消标记
    let list = []
    list.push(messageidList)
    axios({
        method: 'PUT',
        url:`${baseUrl}/${folderid}/messages/flagged/${flagged}`,
        data:{
            folderId : folderid,
            flagged: flagged,
        	emailAddress: useraddr,
	        msgIds: list
        }
    }).then()

}
//----------发信接口----------------
const sendMail = (note) => {
    console.log(note)
    axios({
        method: 'POST',
        url:`${baseUrl}/email`,
        data:{
            from: note.from, //发送邮箱
	        toUser: note.to,//所有邮件地址请用,隔开，摆在同一个字符串中
	        ccUser: note.cc, //抄送
	        bccUser: note.bcc, //秘密抄送
	        subject: note.subject, //主题
	        content: note.content, //主要内容
	        //attachment: [string],//string数组
	        //html: 0/1//如果做了html邮件，可以添加这个字段，没做就当它不存在
        }
    }).then(response=>{
        const data = response
        console.log(response)
    })
}
const sendDraft = (note) => {
    axios({
        method: 'POST',
        url:`${baseUrl}/email`,
        data:{
            from: note.from, //发送邮箱
	        toUser: note.to,//所有邮件地址请用,隔开，摆在同一个字符串中
	        ccUser: note.cc, //抄送
	        bccUser: note.bcc, //秘密抄送
	        subject: note.subject, //主题
	        content: note.content, //主要内容
	        //attachment: [string],//string数组
	        //html: 0/1//如果做了html邮件，可以添加这个字段，没做就当它不存在
        }
    }).then(response=>{
        const data = response.data
    })
}
//---------通讯录-------
const getContact = (setAddrData) => {//临时接口，获取联系人名单
    axios({
        method: 'GET',
        url:`${baseUrl}/contact`,
        data:{

        }
    }).then(response=>{
        const data = response.data.data
        //console.log(response.data)
        //console.log(response.data.data)
        setAddrData(data)
        localStorage.setItem("addrData",JSON.stringify(data))

    })
}
const addContact = (contactMail,name) => {//添加用户联系人
    axios({
        method:'POST',
        url:`${baseUrl}/contact`,
        data:{
            contactEmail:contactMail,
            name:name
        }
    }).then()

}
const deleteContact = (uid) => {
    axios({
        method:'DELETE',
        url:`${baseUrl}/contact/${uid}`,
        data:{
            contactUid:uid
        }
    }).then()
}
export default {
                setAuthToken,
                loginPost,
                registerPost,

                getAssociatedAddrList,
                associateNewAddr,
                deleteAssociatedAddr,
                switchMail,

                getFolderList,
                getMailList,
                getAttachment,
                getMailDetail,

                moveMail, 
                putMailIntoTrash,
                deleteMail,
                flagMail,
                setReadStatusMail,

                sendMail,
                sendDraft,
                getContact,
                addContact,
                deleteContact
                }
