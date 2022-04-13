//import * as AxiosLogger from 'axios-logger'
import axios from 'axios'
//const baseUrl = 'http://localhost:8081'
//const baseUrl = '172.19.240.244:8081'
const baseUrl = 'http://localhost:3001'//json server
const setAuthToken = token => {
    if (token) {
      // headers 每个请求都需要用到的
      axios.defaults.headers.common["Authorization"] = token;
    } else {
      delete axios.defaults.headers.common["Authorization"];
    }
  }
/* 请求拦截 */
/*axios.interceptors.request.use(
    config => {
      if (localStorage.getItem("loginToken") != null) {
        config.headers["token"] = localStorage.getItem("token");
      }
  
      return config;
    },
    err => Promise.reject(err)
  );*/
  
const loginPost = (address,domain,passwd,setCode) => {//登录邮箱
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
        //setCode('1')
        localStorage.setItem("loginToken",token)
        console.log(localStorage)
        setAuthToken(token)
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
const getMailList = (folderid,useraddr,setBoxData) => {
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
const getMailDetail = async(folderid, useraddr, messageuid, setDetailData) => {
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
const putMailIntoTrash = (folderid,useraddr,messageidList) => {
    axios({
        method: 'PUT',
        url:`${folderid}/messages/trash`,
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
const deleteMail = (folderid,useraddr,messageidList) => {
    axios({
        method: 'DELETE',
        url: `${folderid}/messages`,
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

export default {
                loginPost,
                getFolderList,
                getMailList,
                setAuthToken,
                getMailDetail,
                putMailIntoTrash,
                deleteMail
                }
