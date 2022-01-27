import React from 'react';
import { Col, Form, Row, Button, Upload,Toast} from '@douyinfe/semi-ui';
import Icon, { IconUpload} from '@douyinfe/semi-icons'
const ContentWrite = () => {
    var username = "AgileUser123"
    return(
        <><div>
            <h4>晚上好，{username}</h4>
        </div>
            <div
                style={{
                    borderRadius: '10px',
                    border: '1px solid var(--semi-color-border)',
                    height: 'auto',
                    padding: '16px',
                }}
            >
                <Row>
                    <Col>
                        <Form 
                        labelPosition='left'
                        labelWidth = '70px'>
                            <Form.Input field='to' label='收件人' style={{ width: '100%' }} ></Form.Input>
                            <Form.Input field='title' label='主题' style={{ width: '100%' }}></Form.Input>
                            <Form.Upload action='../images'>
                                <Button icon={<IconUpload />} theme="light">
                                    添加附件
                                </Button>
            </Form.Upload>
                            <Form.TextArea autosize rows={12}></Form.TextArea>
                            <Button type='primary' theme='solid' 
                            style={{ width: 100, marginTop: 12, marginRight: 30,marginLeft:70 }}
                            onClick={() => Toast.success('发送成功')}>发送邮件</Button>
                            <Button style={{marginTop: 12,width:100}}
                            onClick={() => Toast.success('保存成功')}>存草稿</Button>
                        </Form>
                    </Col>
                </Row>
            </div></>
    )
}
export default ContentWrite