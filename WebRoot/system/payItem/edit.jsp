<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<div class="pageContent">
<form method="post" action="<%=basePath %>system/payItems/update/${payItem.id}?navTabId=payItemNavTab&callbackType=closeCurrent" class="pageForm required-validate" onsubmit="return validateCallback(this,dialogAjaxDone);">
    <div class="pageFormContent" layoutH="57">
    <fieldset>
        <legend>修改充值信息</legend>
        <dl>
            <dt>充值标题: </dt>
            <dd><input class="required" type="text" id="payInfo" name="payInfo" value="${payItem.payInfo}"></dd>
        </dl>
        
        <dl>
            <dt>所属商户：</dt>
            <dd>
                <select id="busId" name="busId" class="combox" onchange="busOnchange(this)">
	                <option value="-1">请选择关联商户</option>
	                <c:forEach var="item" items="${bus}">
	                    <option value="${item.id }" ${item.id==payItem.busId?"selected":""}>${item.busName }</option>
	                </c:forEach>
	            </select>
	            <input id="busName" name="busName" type="hidden" value="${payItem.busName}"/>
            </dd>
        </dl>
        
        
        <dl>
            <dt>充值金额：</dt>
            <dd><input class="required digits" type="text" id="payMoney" name="payMoney" value="${payItem.payMoney}"></dd>
        </dl>
        
        <dl>
            <dt>总金额（包含赠送）：</dt>
            <dd><input class="required digits" type="text" id="totalMoney" name="totalMoney" value="${payItem.totalMoney}"></dd>
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

