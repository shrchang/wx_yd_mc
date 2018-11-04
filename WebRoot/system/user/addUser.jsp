<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<div class="pageContent">
<form method="post" action="<%=basePath %>system/users/insert?navTabId=userNavTab&callbackType=closeCurrent" class="pageForm required-validate" onsubmit="return validateCallback(this,dialogAjaxDone);">
    <div class="pageFormContent" layoutH="57">
        <p>
            <label>登录名: </label>
            <input type="text" name="userId" class="required alphanumeric" maxlength="16"/>
        </p>
        <p>
            <label>用户名: </label>
            <input type="text" name="userName" class="required" maxlength="16"/>
        </p>
        <p>
            <label>关联商户：</label>
            <select id="busId" name="busId" class="combox" onchange="busOnchange(this)">
                <option value="-1">请选择关联商户</option>
	            <c:forEach var="item" items="${bus}">
	                <option value="${item.id }">${item.busName }</option>
	            </c:forEach>
            </select>
            <input id="busName" name="busName" type="hidden"/>
        </p>
        <p>
            <label>用户角色: </label>
            <c:forEach var="item" items="${roles}">
	            <input type="checkbox" name="userRoles" value="${item.id }"  class="required"/>${item.roleDesc }
            </c:forEach>
        </p>
        <p>
            <label>手机号: </label>
            <input type="text" name="phoneNumber" class="required digits" maxlength="11" minlength="11"/>
        </p>
        <p>
            <label>邮箱: </label>
            <input type="text" name="email" class="required email" maxlength="32"/>
        </p>
        <p>
            <label>密码: </label>
            <input type="password" name="userPassword" id="userPassword" class="required" maxlength="32"  minlength="8"/>
        </p>
        <p>
            <label>确认密码: </label>
            <input type="password" name="confirmUserPassword" class="required" maxlength="32" equalTo="#userPassword" minlength="8"/>
        </p>
    </div>

    <div class="formBar">
        <ul>
            <li><div class="buttonActive"><div class="buttonContent"><button type="submit">保存</button></div></div></li>
            <li><div class="button"><div class="buttonContent"><button type="button" class="close">关闭</button></div></div></li>
        </ul>
    </div>
</form>
<script type="text/javascript">
function busOnchange(obj){
	var busName=$(obj).find("option:selected").text();
	$("#busName").val(busName);
}
</script>
</div>

