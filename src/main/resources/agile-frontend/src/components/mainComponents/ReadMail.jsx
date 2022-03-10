import React from 'react';
import { Col, Form, Row, Button,Typography,Card} from '@douyinfe/semi-ui';
import api from '../../api/api'
import { useSearchParams } from 'react-router-dom';
const Readmail = ({useraddr,setUseraddr,boxData,setBoxData,detailData,setDetailData,boxType,setBoxType}) =>{
    const { Section, Input } = Form;
    const {Text,Paragraph} = Typography
    useraddr = JSON.parse(localStorage.getItem("userdata"))
    const [params] = useSearchParams()
    if(boxData === undefined){
        api.getInboxList(useraddr,boxData,setBoxData)
    }
    if(detailData===undefined){
        if(boxData === undefined){
            api.getInboxList(useraddr,boxData,setBoxData)//get，此时boxdata还未刷新
        }else{
            var id = params.get('id')-1//通过
            if(id < 0)
                id = 0
            const to = boxData[0].to
            const subject = boxData[0].subject
            api.getDetailOfMail(useraddr,id+1,to,subject,setDetailData)
        }

    }
    var data =  {
            
    }
    if(detailData !== undefined)
        data = detailData
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
                            <Section text={data.subject}>
                                <Row gutter={16}>
                                    <Col span={8}>
                                        <Text strong>发件人：</Text>
                                        <Text style={{color:'rgba(var(--semi-green-6), 1)'}} strong>{data.from}</Text>
                                        <Text> {`<`}{data.from}{`>`}</Text>
                                    </Col>
                                </Row>
                                <Row gutter={16}>
                                    <Col span={8}>
                                    <Text strong>时间：</Text>
                                    <Text>{data.datetime}</Text>
                                    </Col>
                                </Row>
                                <Row gutter={16}>
                                    <Col span={8}>
                                        <Text strong>收件人：</Text>
                                        <Text style={{color:'rgba(var(--semi-purple-7), 1)'}} strong>{data.to}</Text>
                                        <Text> {`<`}{data.to}{`>`}</Text>
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
                                        style={{ padding: '6px 24px',marginRight: 25,color:'rgba(var(--semi-light-green-5), 1)' }}
                                        onClick={()=>console.log(boxData[0])}>
                                        回复
                                </Button>
                                <Button type="danger"  
                                        style={{ padding: '6px 24px',marginRight: 25 }}
                                        onClick={()=>console.log(params.get('id'))}>
                                        删除
                                </Button>
                                <Button type="primary"  
                                        style={{ padding: '6px 24px' }}
                                        onClick={()=>console.log(detailData)}>
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