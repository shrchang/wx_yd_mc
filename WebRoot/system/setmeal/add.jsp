<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<div class="pageContent">
<form method="post" id="addRoomForm" action="<%=basePath %>system/setmeal/insert?navTabId=setmealNavTab&callbackType=closeCurrent" onsubmit="return validateCallback(this,navTabAjaxDone);" class="pageForm required-validate">
    <div class="pageFormContent" layoutH="57">
    <fieldset>
        <legend>添加增值套餐信息</legend>
        <dl>
            <dt>套餐名称: </dt>
            <dd><input class="required" type="text" id="mealName" name="mealName"></dd>
        </dl>
        
        <dl>
            <dt>所属商户：</dt>
            <dd>
                <select name="busId" id="busId" class="combox" onchange="busOnchange(this)" ref="refShopId" refUrl="<%=basePath %>system/shops/findByBus/{value}">
	                <option value="">请选择商户</option>
	                <c:forEach var="item" items="${bus}">
	                    <option value="${item.id }">${item.busName }</option>
	                </c:forEach>
	            </select>
	            <input id="busName" name="busName" type="hidden"/>
            </dd>
        </dl>
        
        <dl>
            <dt>所属分店：</dt>
            <dd>
                <select id="refShopId" name="shopId" class="combox" onchange="shopOnchange(this)">
                    <option value="">请选择分店</option>
                </select>
                <input id="shopName" name="shopName" type="hidden"/>
            </dd>
        </dl>
        
        <dl>
            <dt>套餐价格: </dt>
            <dd><input class="required number" type="text" id="mealPrice" name="mealPrice"></dd>
        </dl>
        
        <dl class="nowrap">
            <dt>描述：</dt>
            <dd><textarea cols="80" rows="2" id="mealDesc" name="mealDesc"></textarea></dd>
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
function shopOnchange(obj){
    var busName=$(obj).find("option:selected").text();
    $("#shopName").val(busName);
}
</script>
</div>

