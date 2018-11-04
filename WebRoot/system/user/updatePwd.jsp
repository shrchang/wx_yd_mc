<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.booking.wechat.persistence.bean.user.SysUser" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
SysUser user = (SysUser)session.getAttribute("sysUser");
%>
<div class="pageContent">
<form method="post" action="<%=basePath %>system/users/updatePwd?navTabId=userNavTab&callbackType=closeCurrent" class="pageForm required-validate" onsubmit="return validateCallback(this,dialogAjaxDone);">
    <div class="pageFormContent" layoutH="57">
        <p>
            <label>登录名: </label>
            <input type="text" name="userId" value="<%=user.getUserId() %>" readonly="readonly" class="required" maxlength="20"/>
        </p>
        <p>
            <label>用户名: </label>
            <input type="text" name="userName" value="<%=user.getUserName() %>" class="required" maxlength="100"/>
        </p>
        <p>
            <label>原密码: </label>
            <input type="password" name="oldPassw0rd" class="required alphanumeric" maxlength="32"/>
        </p>
        <p>
            <label>新密码: </label>
            <input type="password" id="newUserPassword" name="newUserPassword" class="required alphanumeric" maxlength="32"/>
        </p>
        <p>
            <label>确认密码: </label>
            <input type="password" name="confirmUserPassword" equalTo="#newUserPassword" class="required alphanumeric" maxlength="32"/>
        </p>
    </div>

    <div class="formBar">
        <ul>
            <li><div class="buttonActive"><div class="buttonContent"><button type="submit">保存</button></div></div></li>
            <li><div class="button"><div class="buttonContent"><button type="button" class="close">关闭</button></div></div></li>
        </ul>
    </div>
</form>
</div>