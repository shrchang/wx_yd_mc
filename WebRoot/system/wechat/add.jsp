<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<div class="pageContent">
<form method="post" action="<%=basePath %>system/wechatConfigs/insert?navTabId=wechatConfigNavTab&callbackType=closeCurrent" class="pageForm required-validate" onsubmit="return validateCallback(this,navTabAjaxDone);">
    <div class="pageFormContent" layoutH="57">
    <fieldset>
        <legend>添加微信配置信息</legend>
        
        <dl>
            <dt>所属商户：</dt>
            <dd>
                <select id="busId" name="busId" class="combox required" onchange="busOnchange(this)">
	                <option value="">请选择关联商户</option>
	                <c:forEach var="item" items="${bus}">
	                    <option value="${item.id }">${item.busName }</option>
	                </c:forEach>
	            </select>
	             <input id="busName" name="busName" type="hidden"/>
            </dd>
        </dl>
        <dl>
            <dt>接口凭证: </dt>
            <dd><input class="required" type="text" id="appId" name="appId"></dd>
        </dl>
        <dl>
            <dt>令牌: </dt>
            <dd><input class="required" type="text" id="token" name="token"></dd>
        </dl>
        <dl>
            <dt>凭证秘钥: </dt>
            <dd><input class="required" type="text" id="secret" name="secret"></dd>
        </dl>
        <dl>
            <dt>商户号: </dt>
            <dd><input class="required" type="text" id="machId" name="machId"></dd>
        </dl>
         <dl>
            <dt>支付密钥: </dt>
            <dd><input class="required" type="text" id="privateKey" name="privateKey"></dd>
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

