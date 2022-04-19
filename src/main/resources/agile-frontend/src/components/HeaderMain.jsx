import React from 'react';
import { useNavigate } from 'react-router-dom';
import { Button,Select,Nav } from '@douyinfe/semi-ui';
import Icon, { IconUndo, IconSemiLogo, IconHelpCircle} from '@douyinfe/semi-icons'
import api from '../api/api';
const HeaderMain = ({useraddr,setUseraddr,folderList,setFolderList}) => {
    useraddr = JSON.parse(localStorage.getItem("userdata"))
    //assoData = localStorage.getItem("currmail")
    //console.log(assoData)
    const turnIntoList = (target) => {
        return{
            value:target.emailAddress,
            label:target.emailAddress,
        }
    }
    const templist =JSON.parse(localStorage.getItem("associatedList"))
    var assolist
    if(templist !== undefined && templist !== null && templist.length > 0){
        assolist = templist.map(turnIntoList)
    }else{
        assolist = [{value:'',label:''}]
    }
    var username = useraddr.name
    const navigate = useNavigate()
    const selectOnchange = (value) => {
        console.log(value)
        localStorage.setItem("currmail",value)
        api.getFolderList(value,setFolderList)
        navigate('/main/')
    }
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
                <span style={{ marginRight: '24px' }}>{username}，当前邮箱为：
                <Select placeholder='请选择当前邮箱' style={{ width: 180 }}
                optionList={assolist} defaultValue={localStorage.getItem("currmail")} onChange={selectOnchange}/>
                </span>
               
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