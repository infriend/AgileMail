//import * as AxiosLogger from 'axios-logger'
import axios from 'axios'
const defaultUrl = 'http://localhost:3001'
const loginPost = (address,passwd) => {
    axios({
        method:'POST',
        url : 'http://localhost:3001/user',
        data :{
            emailaddress: address,
	        password: passwd
        }
    }).then(response => {
        console.log(response)
    })
}
const emailPost = (fromaddr,toaddr,subjectStr,contentStr) => {
    axios({
        method:'POST',
        url : 'http://localhost:3001/inbox',
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
const draftPost = (fromaddr,toaddr,subjectStr,contentStr) => {
    axios({
        method:'POST',
        url : 'http://localhost:3001/draft',
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
        url : 'http://localhost:3001/deleted',
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
    var newurl = 'http://localhost:3001/inbox/'+mail.id
    axios({
        method:'DELETE',
        url :newurl
    }).then(response=>{
        console.log(response)
    })
}
const deleteAddrListPost = (useraddr,addr) => {
    var newurl = 'http://localhost:3001/addData/'+addr.id
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
        url : 'http://localhost:3001/deleted',
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
    var newurl = 'http://localhost:3001/draft/'+mail.id
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
        url : 'http://localhost:3001/deleted',
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
    var newurl = 'http://localhost:3001/alreadySent/'+mail.id
    axios({
        method:'DELETE',
        url :newurl
    }).then(response=>{
        console.log(response)
    })
}
const deletedTotoallyListPost = (useraddr,mail) => {
    var newurl = 'http://localhost:3001/deleted/'+mail.id
    axios({
        method:'DELETE',
        url :newurl
    }).then(response=>{
        console.log(response)
    })
}
const getAddrBook = (useraddr,addrdata,setAddrData) => {
    var data
     axios({
        method:'GET',
        url : 'http://localhost:3001/addData',
    }).then(response=>{
        console.log(response)
        data = response.data
        setAddrData(data)
    })
    return data
}
const getInboxList = (useraddr,boxData,setBoxData) => {
    var data
    axios({
        method:'GET',
        url : 'http://localhost:3001/inbox',
    }).then(response=>{
        //console.log(response)
        data = response.data
        setBoxData(data)
        //console.log("inbox:")
        //console.log(data)
    })
}
const getDraftList = (useraddr,boxData,setBoxData) => {
    var data
    axios({
        method:'GET',
        url : 'http://localhost:3001/draft',
    }).then(response=>{
        data = response.data
        setBoxData(data)
    })
}
const getSentList = (useraddr,boxData,setBoxData) => {
    var data
    axios({
        method:'GET',
        url : 'http://localhost:3001/alreadySent',
    }).then(response=>{
        data = response.data
        setBoxData(data)
    })
}
const getDeleteList = (useraddr,boxData,setBoxData) => {
    var data
    axios({
        method:'GET',
        url : 'http://localhost:3001/deleted',
    }).then(response=>{
        data = response.data
        setBoxData(data)
    })
}
const getDetailOfMail = (useraddr,id,emailAddress,subject,setDetailData) => {
    var data
    var newurl = 'http://localhost:3001/detail/'+id
    //console.log(newurl)
    axios({
        method:'GET',
        url : newurl,
    }).then(response=>{
        data = response.data            
        //console.log("api:")
       // console.log(data)
        setDetailData(data)
    })

}
export default {loginPost,
                emailPost,
                draftPost,
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
                getDetailOfMail}
