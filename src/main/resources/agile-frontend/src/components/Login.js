import React, { useRef, useState } from 'react'
import { Button, Card ,Row, Col, Form,Avatar,Space,Typography} from '@douyinfe/semi-ui'
const Login = () => {
    const { Meta } = Card;
    const { Text } = Typography;
    const { Title } = Typography
    return(
        <div>
            <Row >
            <Col span={8} offset={8}>
            <Card
            style={{ maxWidth: 500 }}
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
                <Text link>
                    注册
                </Text>
            }
            footerLine={ true }
            footerStyle={{ display: 'flex', justifyContent: 'center' }}
            footer={
                    <Button block theme='solid'>L O G I N</Button>
            }
        >
                <Form layout='horizontal'>
                    <Space spacing='loose'>
                    <Form.Input field='用户名*'  placeholder='请输入用户名' style={{width:200}} showClear/>
                    <Form.Input field='mail ' placeholder='@Agilemail.com' style={{width:217}} disabled/>
                    </Space>
                </Form>

                <Form>
                    <Form.Input field='密码*' mode="password"  placeholder='请输入密码'  showClear/>
                </Form>  
        </Card>
                </Col>
            </Row>
        </div>
    )
}
export default Login