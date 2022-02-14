import React, { useRef, useState } from 'react'
const Register = () =>{
    return(
        <div class="rg_layout">
        <div class = "rg_left">
            <p>新用户注册</p>
            <p>USER REGISTER</p>
        </div>
        <div class = "rg_center">
            <div class="rg_form">
                {/* <!-- 定义表单 form--> */}
                <form action="#" method="post">
                    <table >
                        <tr>
                            <td class="td_left"><label for="usename"></label> 用户名</td>
                            <td class="td_right"><input type="text" name="username" id="username" placeholder="请输入用户名"></input></td>
                        </tr>
                        <tr>
                            <td class="td_left"><label for="password"></label> 密码</td>
                            <td class="td_right"><input type="password" name="password" id="password" placeholder="请输入密码"></input></td>
                        </tr>
                        <tr>
                            <td class="td_left"><label for="email"></label> email</td>
                            <td class="td_right"><input type="email" name="email" id="email" placeholder="请输入email"></input></td>
                        </tr>
                        <tr>
                            <td class="td_left"><label for="name"></label> 姓名</td>
                            <td class="td_right"><input type="text" name="name" id="name" placeholder="请输入姓名"></input></td>
                        </tr>
                        <tr>
                            <td class="td_left"><label for="tel"></label> 手机号</td>
                            <td class="td_right"><input type="text" name="tel" id="tel" placeholder="请输入手机号"></input></td>
                        </tr>
                        <tr>
                            <td class="td_left"><label>性别</label></td>
                            <td class="td_right"><input type="radio" name="gender" value="male">男</input>
                                <input type="radio" name="gender" value="famale">女</input>
                            </td>
                        </tr>
                        <tr>
                            <td class="td_left"><label for="birthday">出生日期</label></td>
                            <td class="td_right"><input type="data" name="birthday" id="birthday" placeholder="年/月/日"></input></td>
                        </tr>
                        <tr>
                            <td class="td_left"><label for="checkcode">验证码</label></td>
                            <td class="td_right"><input type="text" name="checkcode" id="checkcode" placeholder="请输入验证码"></input>
                                <img id ="img_check" src="src\images\backgroundImage.jpg" ></img>
                            </td>
                        </tr>
                        <tr>
                            <td colspan="2" align="center"><input type="submit" id = "btn_sub" value="注册"></input></td>
                        </tr>
                    </table>
                </form>
            </div>
        </div>
        <div class = "rg_right">
            <p>已有账号?&nbsp;  <a href="#">&nbsp;立即登录&nbsp;</a></p>
        </div>
    </div>
    )
}
export default Register
