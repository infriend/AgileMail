//import * as AxiosLogger from 'axios-logger'
import axios from 'axios'
//const baseUrl = 'http://localhost:8081'
//const baseUrl = '172.19.240.244:8081'
const baseUrl = 'http://localhost:3001'//json server
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
  
const loginPost = (address,domain,passwd,setCode) => {//登录邮箱 
    let t = 'QWZ3ASASD'
    axios({
        method:'POST',
        url : `${baseUrl}/login`,
        data :{
            emailAddress: address,
	        password: passwd,
            domain: domain
        }
    }).then(response => {
        const {token} = response.data
        const code = response.code
        setCode(code)
        console.log("log in")
        //setCode('1')
        //localStorage.setItem("loginToken",token)
        localStorage.setItem("loginToken",t)
        console.log(localStorage)
        //setAuthToken(token)
        setAuthToken(t)
    })
}
const getFolderList = (useraddr,setFolderList) => {//获取folder信息
    axios({
        method:'GET',
        url: `${baseUrl}/folder`,
        data:{
            emailAddress : useraddr.name+"@"+useraddr.addr
        }

    }).then(response => {
        const inf = response.data//inf是一个数组
        setFolderList(inf)
        localStorage.setItem("folderList",JSON.stringify(inf))
    })

}
const getMailList = (folderid,useraddr,setBoxData) => {//查看文件夹邮件列表
    axios({
        method:'GET',
        //url:`${baseUrl}/${folderid}/list`,
        url:`${baseUrl}/list`,
        data:{
            folderId:folderid,
            emailAddress : useraddr.name+"@"+useraddr.addr
        }
    }).then(response => {
        const data = response.data
        setBoxData(data)
    })
}
const getMailDetail = async(folderid, useraddr, messageuid, setDetailData) => {//查看邮件
    await axios({
        method: 'GET',
        //url: `${baseUrl}/${folderId}/message/${messageuid}`,
        url: `${baseUrl}/testDetail`,
        data: {
            folderId: folderid,
            emailAddress: useraddr.name + "@" + useraddr.addr,
            messageUid: messageuid
        }
    }).then(response => {
        const data = response.data;
        console.log("fromapi");   
        localStorage.setItem("mailDetail", JSON.stringify(data[0]))
        console.log(localStorage.getItem("mailDetail"))
        console.log("fromapi2");  
        //setDetailData(data[0]);     
    });
}
const putMailIntoTrash = (folderid,useraddr,messageidList) => {//删除邮件
    axios({
        method: 'PUT',
        url:`${baseUrl}/${folderid}/messages/trash`,
        data:{
            folderId : folderid,
        	emailAddress: useraddr.name + "@" + useraddr.addr,
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
        	emailAddress: useraddr.name + "@" + useraddr.addr,
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
const setReadStatusMail = (folderid,seen,useraddr,messageidList) => {
    axios({
        method: 'PUT',
        url:`${baseUrl}/${folderid}/messages/seen/${seen}`,
        data:{
            folderId : folderid,
            seen: seen,
        	emailAddress: useraddr.name + "@" + useraddr.addr,
	        msgIds: messageidList
            }
    }).then()
}
const flagMail = (folderid,flagged,useraddr,messageidList) => {//设为已标记/取消标记
    axios({
        method: 'PUT',
        url:`${baseUrl}/${folderid}/messages/flagged/${flagged}`,
        data:{
            folderId : folderid,
            flagged: flagged,
        	emailAddress: useraddr.name + "@" + useraddr.addr,
	        msgIds: messageidList
        }
    }).then()

}
const sendMail = () => {
    axios({
        method: 'POST',
        url:`${baseUrl}/email`,
        data:{
            /*from: string, //发送邮箱
	        toUser: string,//所有邮件地址请用,隔开，摆在同一个字符串中
	        ccUser: string, //抄送
	        bccUser: string, //秘密抄送
	        subject: string, //主题
	        content: string, //主要内容
	        attachment: [string],//string数组
	        html: 0/1//如果做了html邮件，可以添加这个字段，没做就当它不存在*/
        }
    }).then(response=>{

    })

}
export default {
                loginPost,
                getFolderList,
                getMailList,
                setAuthToken,
                getMailDetail,
                putMailIntoTrash,
                deleteMail,
                flagMail,
                setReadStatusMail,
                sendMail
                }
