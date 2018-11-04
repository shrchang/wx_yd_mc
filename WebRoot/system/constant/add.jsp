<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<div class="pageContent">
<form method="post" action="<%=basePath %>system/constant/insert?navTabId=sysCfgNavTab&callbackType=closeCurrent" class="pageForm required-validate" onsubmit="return validateCallback(this,navTabAjaxDone);">
    <div class="pageFormContent" layoutH="57">
    <fieldset>
        <legend>添加系统配置</legend>
        <dl>
            <dt>KEY: </dt>
            <dd><input type="text" id="constantId" name="constantId"></dd>
        </dl>
        <dl>
            <dt>所属商户：</dt>
            <dd>
                <select id="busId" name="busId" class="combox" onchange="busOnchange(this)">
	                <option value="-1">请选择关联商户</option>
	                <c:forEach var="item" items="${bus}">
	                    <option value="${item.id }">${item.busName }</option>
	                </c:forEach>
	            </select>
	            <input id="busName" name="busName" type="hidden"/>
            </dd>
        </dl>
        
        <dl>
            <dt>配置分组: </dt>
            <dd><input type="text" id="constantGroup" name="constantGroup"></dd>
        </dl>
       
        <dl>
            <dt>名称: </dt>
            <dd><input type="text" id="constantName" name="constantName"></dd>
        </dl>
        
        <dl class="nowrap">
            <dt>值: </dt>
            <dd>
            	<textarea id="constantValue" name="constantValue" class="required" cols="80" rows="2"></textarea>
            </dd>
        </dl>
    </fieldset>
    
        
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

