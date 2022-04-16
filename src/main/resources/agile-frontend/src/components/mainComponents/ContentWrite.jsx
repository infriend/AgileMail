import React, {useState} from 'react';
import { Col, Form, Row, Button, Upload,Toast, useFormState,Collapsible} from '@douyinfe/semi-ui';
import api from '../../api/api'
import Icon, { IconUpload} from '@douyinfe/semi-icons'
const ContentWrite = ({useraddr,setUseraddr}) => {
    var username = useraddr.name
    const [submitstate, setsubmitstate] = useState();
    useraddr = JSON.parse(localStorage.getItem("userdata"))
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
    const mailSubmmit = (values) =>{
        const tempNote ={}
        tempNote.from = useraddr.name+'@'+useraddr.addr
        tempNote.to = values.to
        tempNote.subject = values.title
        tempNote.content = values.content
        //console.log(values)
        console.log(tempNote)
        var issuccess = true;//test
        if (submitstate == "send"){
            //api.emailPost(tempNote.from,tempNote.to,tempNote.subject,tempNote.content)
            if(issuccess)
                Toast.success('发送成功')
            else
                Toast.error('发送失败')
        }else if(submitstate == "draft"){
            //api.draftPut(tempNote.from,tempNote.to,tempNote.subject,tempNote.content)
            if(issuccess)
                Toast.success('保存成功')
            else
                Toast.error('发送失败')
        }
    }
    const sendOnclick = () => {
        setsubmitstate("send")
    }
    const draftOnclick = () => {
        setsubmitstate("draft")
    }
    return(
        <><div>
            <h4>{time()}，{username}</h4>
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
                        labelWidth = '70px'
                        onSubmit={mailSubmmit}>
                            <Form.Input field='to' label='收件人' style={{ width: '100%' }} ></Form.Input>
                            <Form.Input field='title' label='主题' style={{ width: '100%' }}></Form.Input>
                            <Form.Upload action='../images'>
                                <Button icon={<IconUpload />} theme="light">
                                    添加附件
                                </Button>
            </Form.Upload>
                            <Form.TextArea field = 'content' autosize rows={12}></Form.TextArea>
                            <Button type='primary' theme='solid' htmlType='submit'
                            style={{ width: 100, marginTop: 12, marginRight: 30,marginLeft:70 }}
                            onClick={sendOnclick}>发送邮件</Button>
                            <Button style={{marginTop: 12,width:100}} htmlType='submit'
                            onClick={draftOnclick}>存草稿</Button>
                        </Form>
                    </Col>
                </Row>
            </div></>
    )
}
export default ContentWrite