import React from 'react';
import { Col, Layout, Row,Button,Empty} from '@douyinfe/semi-ui';
import { IllustrationSuccess, IllustrationSuccessDark } from '@douyinfe/semi-illustrations';
import { useNavigate } from 'react-router-dom';
const InitialMain = ({useraddr,setUseraddr}) => {
    useraddr = JSON.parse(localStorage.getItem("userdata"))
    var username = useraddr.name
    const navigate = useNavigate()
    var d = new Date()
    const time = () =>{
        var time = d.getHours()
        if(time>=6 && time < 12)
            return "早上好"
        else if (time >=12 && time < 14)
            return "中午好"
        else if (time >= 14 && time < 18)
            return "下午好"
        else if (time >= 18 && time < 23)
            return "晚上好"
        else
            return "夜深了"
    }
    return(
        <><div>
            <h4>{time()}，{username}</h4>
        </div>
            <div
                style={{
                    borderRadius: '10px',
                    border: '1px solid var(--semi-color-border)',
                    height: '410px',
                    padding: '32px',
                }}
            >
                <Empty
                    
                    image={<IllustrationSuccess style={{width: 300, height: 300}} />}
                    layout="horizontal"
                    style={{ width: 800,marginLeft:200,marginTop:50,position: 'horizontal'}}
                >
                    <div>
                        <br /><br /><br />
                        <h2>欢迎使用AgileMail邮件系统</h2>
                        <Button type="primary" theme="solid" 
                        style={{ padding: '6px 24px',marginRight: 50 }}
                        onClick={() => navigate('/main/writemail')}>
                                写邮件
                        </Button>
                        <Button type="primary"  
                        style={{ padding: '6px 24px' }}
                        onClick={() => navigate('/main/inbox?bid=1')}>
                                收件箱
                        </Button>
                    </div>
                    
                </Empty>
            </div></>
    )
}
export default InitialMain