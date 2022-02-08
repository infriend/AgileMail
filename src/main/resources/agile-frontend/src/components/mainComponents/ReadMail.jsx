import React from 'react';
import { Col, Form, Row, Button,Typography,Card} from '@douyinfe/semi-ui';
import Icon, { IconUpload} from '@douyinfe/semi-icons'
const Readmail = ({useraddr,setUseraddr}) =>{
    const { Section, Input } = Form;
    const {Text,Paragraph} = Typography
    const data =  {
            title:"邮件标题test1",
            fromName : 'user1',
            address : 'uqwewqewq@agilemail.com',
            time:'2022-01-27 05:31',
            toName:'agileuser123',
            toAddress:'AgileUser123@agilemail.com',
            content:"React 是一个用于构建用户界面的 JAVASCRIPT 库。React 主要用于构建 UI，很多人认为 React 是 MVC 中的 V（视图）。React 起源于 Facebook 的内部项目，用来架设 Instagram 的网站，并于 2013 年 5 月开源。React 拥有较高的性能，代码逻辑非常简单，越来越多的人已开始关注和使用它React 是一个用于构建用户界面的 JAVASCRIPT 库。React 主要用于构建 UI，很多人认为 React 是 MVC 中的 V（视图）。React 起源于 Facebook 的内部项目，用来架设 Instagram 的网站，并于 2013 年 5 月开源。React 拥有较高的性能，代码逻辑非常简单，越来越多的人已开始关注和使用它React 是一个用于构建用户界面的 JAVASCRIPT 库。React 主要用于构建 UI，很多人认为 React 是 MVC 中的 V（视图）。React 起源于 Facebook 的内部项目，用来架设 Instagram 的网站，并于 2013 年 5 月开源。React 拥有较高的性能，代码逻辑非常简单，越来越多的人已开始关注和使用它React 是一个用于构建用户界面的 JAVASCRIPT 库。React 主要用于构建 UI，很多人认为 React 是 MVC 中的 V（视图）。React 起源于 Facebook 的内部项目，用来架设 Instagram 的网站，并于 2013 年 5 月开源。React 拥有较高的性能，代码逻辑非常简单，越来越多的人已开始关注和使用它React 是一个用于构建用户界面的 JAVASCRIPT 库。React 主要用于构建 UI，很多人认为 React 是 MVC 中的 V（视图）。React 起源于 Facebook 的内部项目，用来架设 Instagram 的网站，并于 2013 年 5 月开源。React 拥有较高的性能，代码逻辑非常简单，越来越多的人已开始关注和使用它React 是一个用于构建用户界面的 JAVASCRIPT 库。React 主要用于构建 UI，很多人认为 React 是 MVC 中的 V（视图）。React 起源于 Facebook 的内部项目，用来架设 Instagram 的网站，并于 2013 年 5 月开源。React 拥有较高的性能，代码逻辑非常简单，越来越多的人已开始关注和使用它"
    }

    console.log(data.title)
    return(
        <>
            <div
                style={{
                    borderRadius: '10px',
                    border: '1px solid var(--semi-color-border)',
                    height: 'auto',
                    padding: '30px',
                }}
            >
                <Row>
                    <Col>
                        <Form 
                        labelPosition='top'
                        labelWidth = 'auto'>
                            <Section text={data.title}>
                                <Row gutter={16}>
                                    <Col span={8}>
                                        <Text strong>发件人：</Text>
                                        <Text style={{color:'rgba(var(--semi-green-6), 1)'}} strong>{data.fromName}</Text>
                                        <Text> {`<`}{data.address}{`>`}</Text>
                                    </Col>
                                </Row>
                                <Row gutter={16}>
                                    <Col span={8}>
                                    <Text strong>时间：</Text>
                                    <Text>{data.time}</Text>
                                    </Col>
                                </Row>
                                <Row gutter={16}>
                                    <Col span={8}>
                                        <Text strong>收件人：</Text>
                                        <Text style={{color:'rgba(var(--semi-purple-7), 1)'}} strong>{data.toName}</Text>
                                        <Text> {`<`}{data.toAddress}{`>`}</Text>
                                    </Col>
                                </Row>
                                
                            </Section>
                            <br />
                            <div>
                                <Card>
                                    <Paragraph>
                                        {data.content}
                                    </Paragraph>
                                </Card>
                            </div>
                            <div>
                                <br />
                                <Button type="primary"
                                        style={{ padding: '6px 24px',marginRight: 25,color:'rgba(var(--semi-light-green-5), 1)' }}>
                                        回复
                                </Button>
                                <Button type="danger"  
                                        style={{ padding: '6px 24px',marginRight: 25 }}>
                                        删除
                                </Button>
                                <Button type="primary"  
                                        style={{ padding: '6px 24px' }}>
                                        转发
                                </Button>
                            </div>
                        </Form>
                    </Col>
                </Row>
            </div></>
    )
}
export default Readmail