<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<div class="pageContent">
<form method="post" action="<%=basePath %>system/users/update?navTabId=userNavTab&callbackType=closeCurrent" class="pageForm required-validate" onsubmit="return validateCallback(this,dialogAjaxDone);">
    <input type="hidden" name="id" value="${user.id}"/>
    <div class="pageFormContent" layoutH="57">
        <p>
            <label>登录名: </label>
            <input type="text" name="userId" value="${user.userId}" readonly="readonly" class="required" maxlength="20"/>
        </p>
        <p>
            <label>用户名: </label>
            <input type="text" name="userName" value="${user.userName}" class="required" maxlength="100"/>
        </p>
        <p>
            <label>关联商户：</label>
            <select id="busId" name="busId" class="combox" onchange="busOnchange(this)">
                <option value="-1">请选择关联商户</option>
                <c:forEach var="item" items="${bus}">
                    <option value="${item.id }" ${user.busId==item.id?"selected":"" }>${item.busName }</option>
                </c:forEach>
            </select>
            <input id="busName" name="busName" type="hidden"/>
        </p>
        <p>
            <label>用户角色: </label>
            <c:forEach var="item" items="${roles}">
	            <input type="checkbox" name="userRoles" value="${item.id }"  class="required" ${item.checked?"checked":"" }/>${item.roleName }
            </c:forEach>
        </p>
        <p>
            <label>手机号: </label>
            <input type="text" name="phoneNumber" value="${user.phoneNumber}" class="required" maxlength="100"/>
        </p>
        <p>
            <label>邮箱: </label>
            <input type="text" name="email" value="${user.email}" class="required" maxlength="100"/>
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