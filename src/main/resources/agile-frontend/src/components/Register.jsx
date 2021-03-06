import React, { useState } from 'react'
import { useNavigate } from 'react-router-dom';
import api from '../api/api'
import { Button, Card ,Row, Col,
    Notification, Form,Avatar,Select,Typography} from '@douyinfe/semi-ui'
const Register = ({useraddr,setUseraddr}) => {
    const [code,setCode] = useState()
    let opts = {
        duration: 3,
        position: 'topRight',
        content: '用户名重复',
        title: '注册失败',
    };
    let opts2 = {
        duration: 3,
        position: 'topRight',
        content: '用户名或密码为空',
        title: '注册失败',
    };
    let opts3 = {
        duration: 1,
        position: 'topRight',
        content: '请登录用户名',
        title: '注册成功',
    };
    const navigate = useNavigate()
    let success = true;//测试用
    var t3 = window.innerWidth
    const t1 = t3 *0.18
    const t2 = t3 * 0.12
    console.log(t3)
    const registerSubmmit = (values) => {
        console.log(values)
        if(values.username=== undefined||values.passwd===undefined){
            Notification.info({ ...opts2, position: 'top' })  
            return; 
        }else{
            api.registerPost(values.username,values.passwd,setCode)
            if(code === '1' ) 
                success = true 
            else 
                success = false
            success = true;//for test only
            if(success){
                Notification.info({ ...opts3, position: 'top' }) 
                navigate('/')
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
                                title={<Title heading={4} style={{ margin: '8px 0' }}>Agile-Mail Register</Title>}
                                avatar={<Avatar
                                    alt='Card meta img'
                                    size="default"
                                    src='https://lf3-static.bytednsdoc.com/obj/eden-cn/ptlz_zlp/ljhwZthlaukjlkulzlp/card-meta-avatar-docs-demo.jpg' />} />}
                            headerExtraContent={<Text link={{ href: '/' }}>
                                返回
                            </Text>}>
                            <Form
                                style={{ lineHeight: 1 }}
                                onSubmit={registerSubmmit}>
                                <Row >
                                    <Col>
                                    <div>
                                        <Form.InputGroup label={{ text: (<span>用户名</span>), required: true }} labelPosition='top'>
                                            <Form.Input field='username' placeholder='请输入用户名' style={{ width: 470 }} showClear />
                                        </Form.InputGroup>
                                       </div> 
                                    </Col>
                                </Row>
                                <Row>
                                    <Col>
                                        <Form.Input field='passwd' label={{ text: ('密码'), required: true }} mode="password" placeholder='请输入密码' style={{ width: 470 }} showClear />
                                        <br /><br />
                                        <Button block theme='solid' htmlType='submit'>Confirm</Button>
                                    </Col>
                                </Row>
                            </Form>
                        </Card>
                    </Col>
                </Row>
            </div></>

    )
}
export default Register