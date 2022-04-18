import React, { useState } from 'react'
import { useNavigate } from 'react-router-dom';
import api from '../api/api'
import { Button, Card ,Row, Col,
    Notification, Form,Avatar,Select,Typography} from '@douyinfe/semi-ui'
const Login = ({useraddr,setUseraddr}) => {
    const [code,setCode] = useState()
    let opts = {
        duration: 3,
        position: 'topRight',
        content: '用户名或密码错误',
        title: '登录失败',
    };
    let opts2 = {
        duration: 3,
        position: 'topRight',
        content: '用户名或密码为空',
        title: '登录失败',
    };
    const navigate = useNavigate()
    let success = true;//测试用
    var t3 = window.innerWidth
    const t1 = t3 *0.18
    const t2 = t3 * 0.12
    console.log(t3)
    const loginSubmmit = async (values) => {
        console.log(values)
        /*if(values.mail===undefined){
            Notification.info({ ...opts3, position: 'top' })  
        }
        else */if(values.username=== undefined||values.passwd===undefined){
            Notification.info({ ...opts2, position: 'top' })  
            return; 
        }else{
            //api.loginPost(values.username,values.mail,values.passwd,setCode)
            api.loginPost(values.username,"qweqwe",values.passwd,setCode)
            if(code === '1' ) 
                success = true 
            else 
                success = false
            success = true;//for test only
            if(success){
               const userdata = {name:values.username,addr:"qweqwe"}
                localStorage.setItem("userdata",JSON.stringify(userdata))
                setUseraddr(userdata)
                navigate('/main/')
            }
             else   
                Notification.info({ ...opts, position: 'top' }) 

        }
    }
    const { Meta } = Card;
    const { Text,Title } = Typography;
    return(
        <><div                
        style={{
            height: '21%',
        }}>
            
        </div><div>
                <Row align='middle' type='flex'>
                    <Col span={8} offset={8}>
                        <Card style={{ maxWidth: '330%' }} 
                            title={<Meta
                                title={<Title heading={4} style={{ margin: '8px 0' }}>Agile-Mail Login</Title>}
                                avatar={<Avatar
                                    alt='Card meta img'
                                    size="default"
                                    src='https://lf3-static.bytednsdoc.com/obj/eden-cn/ptlz_zlp/ljhwZthlaukjlkulzlp/card-meta-avatar-docs-demo.jpg' />} />}
                            headerExtraContent={<Text link={{ href: '/register' }}>
                                注册
                            </Text>}>
                            <Form
                                style={{ lineHeight: 1 }}
                                onSubmit={loginSubmmit}>
                                <Row >
                                    <Col>
                                    <div>
                                        <Form.InputGroup label={{ text: (<span>用户名</span>), required: true }} labelPosition='top'>
                                            <Form.Input field='username' placeholder='请输入用户名' style={{ width: t3 }} showClear />
                                            {/*<Form.Select field='mail' defaultValue='@gmail.com' style={{ width: t2 }}>
                                                <Select.Option value='gmail.com'>@gmail.com</Select.Option>
                                                <Select.Option value='163.com'>@163.com</Select.Option>
                                                <Select.Option value='qq.com'>@qq.com</Select.Option>
                                </Form.Select>
                                <Form.Input field='mail' disabled initValue={'@agile.com'} style={{ width: t2 }} /> */} 
                                        </Form.InputGroup>
                                       </div> 
                                    </Col>
                                </Row>
                                <Row>
                                    <Col>
                                        <Form.Input field='passwd' label={{ text: ('密码'), required: true }} mode="password" placeholder='请输入密码' style={{ width: 470 }} showClear />
                                        <br /><br />
                                        <Button block theme='solid' htmlType='submit'>L O G I N</Button>
                                    </Col>
                                </Row>
                            </Form>
                        </Card>
                    </Col>
                </Row>
            </div></>

    )
}
export default Login