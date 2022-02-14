//import * as AxiosLogger from 'axios-logger'
import axios from 'axios'
const baseUrl = 'http://localhost:8081'
//const baseUrl = '172.19.240.244:8081'
const setAuthToken = token => {
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
      if (localStorage.getItem("token") != null) {
        config.headers["token"] = localStorage.getItem("token");
      }
  
      return config;
    },
    err => Promise.reject(err)
  );
  
const loginPost = (address,domain,passwd) => {
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
        localStorage.setItem("loginToken",token)
        console.log(localStorage)
        setAuthToken(token)
    })
}
const emailPost = (fromaddr,toaddr,subjectStr,contentStr) => {
    axios({
        method:'POST',
        url : `${baseUrl}/email`,
        data : {
            from:fromaddr,
            to:toaddr,
            subject:subjectStr,
            content:contentStr
        }
    }).then(response=>{
        console.log(response)
    })
}
const draftPut = (fromaddr,toaddr,subjectStr,contentStr) => {
    axios({
        method:'PUT',
        url : `${baseUrl}/draft`,
        data : {
            from:fromaddr,
            to:toaddr,
            subject:subjectStr,
            content:contentStr
        }
    }).then(response=>{
        console.log(response)
    })
}
const deletedInboxListPost = (useraddr,mail) => {
    axios({
        method:'POST',
        url : `${baseUrl}/garbage`,
        data: {
            from:mail.from,
            subject:mail.subject,
            to:mail.fromEmailAccount,
        }
    }).then(response=>{
        console.log(response)
    })
    var newurl = `${baseUrl}/inbox`+mail.id
    axios({
        method:'DELETE',
        url :newurl
    }).then(response=>{
        console.log(response)
    })
}
const deleteAddrListPost = (useraddr,addr) => {
    var newurl = `${baseUrl}/addData/`+addr.id
    axios({
        method:'DELETE',
        url :newurl
    }).then(response=>{
        console.log(response)
    })
}
const deletedDraftListPost = (useraddr,mail) => {
    axios({
        method:'POST',
        url : `${baseUrl}/deleted`,
        data: {
            from:mail.from,
            subject:mail.subject,
            datetime:'2020-01-26 11:01',
            fromEmailAccount:mail.fromEmailAccount,
            state:'0'

        }
    }).then(response=>{
        console.log(response)
    })
    var newurl = `${baseUrl}/draft/`+mail.id
    axios({
        method:'DELETE',
        url :newurl
    }).then(response=>{
        console.log(response)
    })
}
const deletedSentListPost = (useraddr,mail) => {
    axios({
        method:'POST',
        url : `${baseUrl}/deleted`,
        data: {
            from:mail.from,
            subject:mail.subject,
            datetime:'2020-01-26 11:01',
            fromEmailAccount:mail.fromEmailAccount,
            state:'0'

        }
    }).then(response=>{
        console.log(response)
    })
    var newurl = `${baseUrl}/alreadySent/${mail.id}`
    axios({
        method:'DELETE',
        url :newurl
    }).then(response=>{
        console.log(response)
    })
}
const deletedTotoallyListPost = (useraddr,mail) => {
    var newurl = `${baseUrl}/deleted/${mail.id}`
    axios({
        method:'DELETE',
        url :newurl,
        params : {
            from: mail.from,
            to: mail.fromEmailAccount,
            subject: mail.subject
        }
    }).then(response=>{
        console.log(response)
    })
}
const getAddrBook = (useraddr,addrdata,setAddrData) => {
    var data
     axios({
        method:'GET',
        url : `${baseUrl}/addData`,
        params : {
            emailAddress : useraddr.name+"@"+useraddr.addr
        }
    }).then(response=>{
        data = response.data
        setAddrData(data)
    })
    return data
}
const getInboxList = (useraddr,boxData,setBoxData) => {
    var data
    axios({
        method:'GET',
        url : `${baseUrl}/inbox/list/`,
        params : {
            emailAddress : useraddr.name+"@"+useraddr.addr
        }
    }).then(response=>{
        data = response.data
        setBoxData(data)
        localStorage.setItem("listinf",JSON.stringify(data))
    })
}
const getDraftList = (useraddr,boxData,setBoxData) => {
    var data
    axios({
        method:'GET',
        url : `${baseUrl}/draft/list`,
        params:{
            emailAddress : useraddr.name+"@"+useraddr.addr
        }
    }).then(response=>{
        data = response.data
        setBoxData(data)
        localStorage.setItem("listinf",data)
    })
}
const getSentList = (useraddr,boxData,setBoxData) => {
    var data
    axios({
        method:'GET',
        url : `${baseUrl}/alreadySent`,
    }).then(response=>{
        data = response.data
        setBoxData(data)
        localStorage.setItem("listinf",data)
    })
}
const getDeleteList = (useraddr,boxData,setBoxData) => {
    var data
    axios({
        method:'GET',
        url : `${baseUrl}/garbage/list`,
        params : {
            emailAddress: useraddr.name+"@"+useraddr.addr
        }
    }).then(response=>{
        data = response.data
        setBoxData(data)
        localStorage.setItem("listinf",data)
    })
}
const getDetailOfMail = (useraddr,id,emailAddress,subject,setDetailData) => {
    var data
    var newurl = `${baseUrl}/detail/${id}`
    axios({
        method:'GET',
        url : newurl,
        params : {
            emailAddress: emailAddress,
            subject : subject
        }
    }).then(response=>{
        data = response.data            
        setDetailData(data)
    })

}
const getDetailOfDraft = (useraddr,id,emailAddress,subject,setDetailData) => {
    var data
    var newurl = `${baseUrl}/draft/detail`
    axios({
        method:'GET',
        url : newurl,
        params : {
            emailAddress: emailAddress,
            subject : subject
        }
    }).then(response=>{
        data = response.data            
        setDetailData(data)
    })

}
export default {loginPost,
                emailPost,
                draftPut,
                deletedInboxListPost,
                deleteAddrListPost,
                deletedDraftListPost,
                deletedSentListPost,
                deletedTotoallyListPost,
                getAddrBook,
                getInboxList,
                getDraftList,
                getSentList,
                getDeleteList,
                getDetailOfMail,
                getDetailOfDraft,
                setAuthToken}
