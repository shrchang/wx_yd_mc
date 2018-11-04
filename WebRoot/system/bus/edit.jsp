<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<div class="pageContent">
<form method="post" action="<%=basePath %>system/bus/update/${bus.id}?navTabId=busNavTab&callbackType=closeCurrent" class="pageForm required-validate" onsubmit="return validateCallback(this,navTabAjaxDone);">
    <div class="pageFormContent" layoutH="57">
    	<fieldset>
    	<legend>修改商户</legend>
        <dl>
        	<dt>商户名称</dt>
        	<dd><input class="required" id="busName" name="busName" value="${bus.busName}"></dd>
        </dl>
        
        <dl>
        	<dt>法人</dt>
        	<dd><input class="required" id="busPerson" name="busPerson"  value="${bus.busPerson}"></dd>
        </dl>
        
        <dl>
        	<dt>身份证</dt>
        	<dd><input class="required" id="identityCard" name="identityCard" value="${bus.identityCard}"></dd>
        </dl>
        
        <dl>
        	<dt>最大分店数</dt>
        	<dd><input class="required digits" id="maxShop" name="maxShop" value="${bus.maxShop}"></dd>
        </dl>
        
         <dl>
        	<dt>最大场地数</dt>
        	<dd><input class="required digits" id="maxRoom" name="maxRoom" value="${bus.maxRoom}"></dd>
        </dl>
        
        <dl>
        	<dt>联系电话</dt>
        	<dd><input class="required digits" id="phoneNumber" name="phoneNumber" value="${bus.phoneNumber}"></dd>
        </dl>
        
        <dl>
        	<dt>对公账号</dt>
        	<dd><input class="required digits" id="publicAccount" name="publicAccount" value="${bus.publicAccount}"></dd>
        </dl>
        
        <dl>
        	<dt>开户行</dt>
        	<dd><input class=required id="bankType" name="bankType" value="${bus.bankType}"></dd>
        </dl>
        
        <dl>
        	<dt>省份</dt>
        	<dd><select class="required" id="busProvince" name="busProvince"></select></dd>
        </dl>
        
        <dl>
        	<dt>城市</dt>
        	<dd><select class="required" id="busCity" name="busCity"></select></dd>
        </dl>
        <dl>
        	<dt>区/县</dt>
        	<dd><select class="required" id="busCounty" name="busCounty"></select></dd>
        </dl>
        <dl class="nowrap">
        	<dt>详细地址</dt>
        	<dd><textarea id="busAddress" name="busAddress" class="required" cols="80" rows="2">${bus.busAddress}</textarea></dd>
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
</div>
<script type="text/javascript">
var mypcas=new PCAS("busProvince","busCity","busCounty");
mypcas.SetValue("${bus.busProvince}","${bus.busCity}","${bus.busCounty}");
</script>