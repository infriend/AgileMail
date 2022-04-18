import {React,useCallback} from 'react';
import { Col, Form, Row, Button,Typography,Card} from '@douyinfe/semi-ui';
import api from '../../api/api'
import { useSearchParams} from 'react-router-dom';
const Readmail = ({useraddr,setUseraddr,boxData,setBoxData,detailData,setDetailData,folderList,setFolderList}) =>{
    const { Section, Input } = Form;
    const {Text,Paragraph} = Typography
    //useraddr = JSON.parse(localStorage.getItem("userdata"))
    folderList = JSON.parse(localStorage.getItem("folderList"))
    const [params] = useSearchParams()
    var data = localStorage.getItem("mailDetail")
    console.log("data"+data)
    console.log(detailData)
    if(data.length === 0 || detailData == undefined){
        console.log("gengxin")
        console.log(data)
        var idlist = params.get('id').split("_")
        api.getMailDetail(idlist[0],useraddr,idlist[1],setDetailData)
    }else{
        console.log("bugengxin")
        console.log(data)
        console.log(detailData)
        if (detailData != undefined)
            data = detailData
    }
    //var idlist = params.get('id').split("_")
    //api.getMailDetail(idlist[0],useraddr,idlist[1],setDetailData)    
    if(detailData !== undefined){
        if(data !== detailData){
            data = detailData
        }        
    }
    console.log(data)
    const FromText = () =>{
        let fromlist = []
        if(data !== undefined){
            let list = data.from
            if(list !== undefined){//防止因为异步调用而报错undefined
                fromlist = list.map((item)=>{
                    return <><Text><Text style={{ color: 'rgba(var(--semi-green-6), 1)' }} strong>{item}</Text> {`<`}{item}{`>`}</Text></>
                })
            }
        }
        return (fromlist)
    }
    const ReceiveText = () => {
        let tolist = []
        if(data !== undefined){
            tolist.push(<><Text style={{ color: 'rgba(var(--semi-purple-7), 1)' }} strong>{data.fromEmailAccount}</Text><Text> {`<`}{data.fromEmailAccount}{`>`}</Text></>)
            let list = data.recipients
            if(list !== undefined){//防止因为异步调用而报错undefined
                if(list !== null){
                    tolist.push(list.map((item)=>{
                        <><Text style={{ color: 'rgba(var(--semi-purple-7), 1)' }} strong>{item}</Text><Text> {`<`}{item}{`>`}</Text></>
                    }))
                }
            }
            
        }
        return (tolist)
    }
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
                                    <Col>
                                        <Text strong>发件人：</Text>
                                        <FromText />

                                    </Col>
                                </Row>
                                <Row gutter={16}>
                                    <Col span={8}>
                                    <Text strong>时间：</Text>
                                    <Text>{data.datetime}</Text>
                                    </Col>
                                </Row>
                                <Row gutter={16}>
                                    <Col>
                                        <Text strong>收件人：</Text>
                                        <ReceiveText/>
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