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
        //console.log(1)
    })
}
export default {loginPost}
