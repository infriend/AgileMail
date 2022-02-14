import React from 'react';
import { useNavigate } from 'react-router-dom';
import { Button,Avatar,Nav } from '@douyinfe/semi-ui';
import Icon, { IconUndo, IconSemiLogo, IconHelpCircle} from '@douyinfe/semi-icons'
const HeaderMain = ({useraddr,setUseraddr}) => {
    var username = useraddr.name
    var addr = useraddr.addr
    const navigate = useNavigate()
    return(
        <div>
        <Nav mode="horizontal" defaultSelectedKeys={['Home']}>
            <Nav.Header>
                <IconSemiLogo style={{ width: '96px', height: '36px', fontSize: 36 }} />
            </Nav.Header>
            <span
                style={{
                    color: 'var(--semi-color-text-2)',
                }}
            >
                <span
                    style={{
                        marginRight: '30px',
                        color: 'var(--semi-color-text-0)',
                        fontWeight: '700',
                    }}
                    onClick={()=>navigate('/main')}
                >
                    邮箱首页
                </span>
                <span style={{ marginRight: '24px' }}>{username}@{addr}</span>
               
            </span>
            <Nav.Footer>
                <Button
                    theme="borderless"
                    icon={<IconHelpCircle size="large" />}
                    style={{
                        color: 'var(--semi-color-text-2)',
                        marginRight: '12px',
                    }}
                    onClick={()=>navigate('/helpcenter')}
                />
                <Button
                    theme="borderless"
                    icon={<IconUndo size="large" />}
                    style={{
                        color: 'var(--semi-color-text-2)',
                        marginRight: '12px',
                    }}
                    onClick={()=>navigate('/')}
                />
            </Nav.Footer>
        </Nav>
    </div>
    )
}
export default HeaderMain