<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<div class="pageContent">
<form method="post" action="<%=basePath %>system/shops/insert?navTabId=shopNavTab&callbackType=closeCurrent" class="pageForm required-validate" onsubmit="return validateCallback(this,navTabAjaxDone);">
    <div class="pageFormContent" layoutH="57">
    <fieldset>
        <legend>添加分店信息</legend>
        <dl>
            <dt>分店名称: </dt>
            <dd><input class="required" type="text" id="shopName" name="shopName"></dd>
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
            <dt>省份：</dt>
            <dd><select class="required" id="shopProvince" name="shopProvince"></select></dd>
        </dl>
        
        <dl>
            <dt>城市：</dt>
            <dd><select class="required" id="shopCity" name="shopCity"></select></dd>
        </dl>
        
        <dl>
            <dt>区/县：</dt>
            <dd><select class="required" id="shopCounty" name="shopCounty"></select></dd>
        </dl>
        
        <dl class="nowrap">
            <dt>详细地址：</dt>
            <dd><textarea class="required" cols="80" rows="2" id="shopAddress" name="shopAddress" placeholder="详细地址"></textarea></dd>
        </dl>
        
        <dl class="nowrap">
            <dt>描述：</dt>
            <dd><textarea cols="80" rows="2" id="shopDesc" name="shopDesc"></textarea></dd>
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
var mypcas=new PCAS("shopProvince,请选择省份","shopCity,请选择城市","shopCounty,请选择区/县")
</script>
</div>

