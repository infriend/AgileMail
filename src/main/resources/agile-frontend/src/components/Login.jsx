import React, { useRef, useState } from 'react'
import { useNavigate } from 'react-router-dom';
import api from '../api/api'
import { Button, Card ,Row, Col,
    Notification, Form,Avatar,Select,Typography} from '@douyinfe/semi-ui'
const Login = ({useraddr,setUseraddr}) => {
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
    let opts3 = {
        duration: 3,
        position: 'topRight',
        content: '请选择邮箱类型',
        title: '登录失败',
    };
    const navigate = useNavigate()
    let success = true;//测试用
    const loginSubmmit = (values) => {
        console.log(values)
        if(values.mail==undefined){
            Notification.info({ ...opts3, position: 'top' })  
        }
        else if(values.username== undefined||values.passwd==undefined){
            Notification.info({ ...opts2, position: 'top' })  
            return; 
        }else{
            var address = values.username+values.mail
            console.log(address)
            //api.loginPost(address,values.passwd)
            success = true;
            if(success){
                setUseraddr({name:values.username,addr:values.mail})
                console.log(useraddr.name+" & "+useraddr.addr)
                navigate('/main/')
            }
             else   
                Notification.info({ ...opts, position: 'top' }) 

        }
    }
    const { Meta } = Card;
    const { Text,Title } = Typography;
    return(
        <div>
            <Row >
            <Col span={8} offset={8}>
            <Card style={{ maxWidth: 600 }}
            title={
                <Meta 
                    title={<Title heading={4} style={{margin: '8px 0'}}>Agile-Mail Login</Title>}
                    avatar={
                        <Avatar 
                            alt='Card meta img'
                            size="default"
                            src='https://lf3-static.bytednsdoc.com/obj/eden-cn/ptlz_zlp/ljhwZthlaukjlkulzlp/card-meta-avatar-docs-demo.jpg'
                        />
                    }
                />
            }
            headerExtraContent={
                <Text link={{ href: '/register' }}>
                    注册
                </Text>
            }>
              <Form 
                style={{ lineHeight: 1}}
                onSubmit={loginSubmmit}>
                <Row>
                    <Col >
                        <Form.InputGroup  label={{ text: (<span>用户名</span>), required: true }} labelPosition='top'>
                            <Form.Input  field='username'  placeholder='请输入用户名' style={{width:300}} showClear/>
                            <Form.Select field='mail'  defaultValue='@gmail.com'style={{width:170}}>
                                <Select.Option value='@gmail.com'>@gmail.com</Select.Option>
                                <Select.Option value='@163.com'>@163.com</Select.Option>
                                <Select.Option value='@qq.com'>@qq.com</Select.Option>
                            </Form.Select>
                        </Form.InputGroup>
                    
                    </Col>
                </Row>
                <Row>
                    <Col>
                        <Form.Input field= 'passwd' label={{text:('密码'),required: true}} mode="password"  placeholder='请输入密码' style={{width:470} } showClear/>
                         <br /><br />
                         <Button block theme='solid'htmlType='submit' >L O G I N</Button>
                    </Col>
                </Row>
               </Form> 
        </Card>
                </Col>
            </Row>
        </div>

    )
}
export default Login