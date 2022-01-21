import React, { useRef, useState } from 'react'
import {history  } from "react-router-dom"
import { Button, Card ,Row, Col, Form,Avatar,Space,Typography} from '@douyinfe/semi-ui'
import Register from './Register'
const Login = () => {
    const { Meta } = Card;
    const { Text } = Typography;
    const { Title } = Typography
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
            }
            footerLine={ true }
            footerStyle={{ display: 'flex', justifyContent: 'center' }}
            footer={
                    <Button block theme='solid' onClick={() => {window.location.href="/main"}}>L O G I N</Button>
            }>
              <Form 
                
                style={{ lineHeight: 2}}>
                <Row>
                    <Col >
                        <Form.InputGroup  label={{ text: (<span>用户名</span>), required: true }} labelPosition='top'>
                            <Form.Input field='用户名'  placeholder='请输入用户名' style={{width:300}} showClear/>
                            <Form.Input field='mail ' placeholder='@Agilemail.com' style={{width:170}} disabled/>
                        </Form.InputGroup>
                    
                    </Col>
                </Row>
                <Row>
                 <Col>
                        <Form>
                            <Form.Input field='密码*' mode="password"  placeholder='请输入密码' style={{width:470}} showClear/>
                         </Form> 
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