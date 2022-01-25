import React from 'react';
import { Col, Layout, Row,Skeleton,Breadcrumb} from '@douyinfe/semi-ui';
const InitialMain = () => {
    var username = "AgileUser123"
    return(
        <><div>
            <h4>早上好，{username}</h4>
        </div>
            <div
                style={{
                    borderRadius: '10px',
                    border: '1px solid var(--semi-color-border)',
                    height: '376px',
                    padding: '32px',
                }}
            >
                <Skeleton placeholder={<Skeleton.Paragraph rows={2} />} loading={true}>
                    <p>Hi, Bytedance dance dance.</p>
                    <p>Hi, Bytedance dance dance.</p>
                </Skeleton>
            </div></>
    )
}
export default InitialMain