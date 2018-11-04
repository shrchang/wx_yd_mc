<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<div class="pageContent">
<form method="post" action="<%=basePath %>system/cards/recharge/${card.id}?navTabId=cardNavTab&callbackType=closeCurrent" class="pageForm required-validate" onsubmit="return validateCallback(this,dialogAjaxDone);">
    <div class="pageFormContent" layoutH="57">
    <fieldset>
        <legend>会员卡充值</legend>
        <dl>
            <dt>用户标识: </dt>
            <dd><input class="required" type="text" id="userId" name="userId" value="${card.userId}" readonly="readonly"></dd>
        </dl>
        <dl>
            <dt>用户名称：</dt>
            <dd><input class="required" type="text" id="userName" name="userName" value="${card.userName}" readonly="readonly"></dd>
        </dl>
        <dl>
            <dt>会员卡号：</dt>
            <dd><input class="required" type="text" id="cardNumber" name="cardNumber" value="${card.cardNumber}" readonly="readonly"></dd>
        </dl>
        <dl>
            <dt>充值金额：</dt>
            <dd>
                <select id="recharge" name="recharge" class="combox" onchange="rechargeChange(this.value)">
	                <option value="-1">请选择充值金额</option>
	                <c:forEach var="item" items="${items}">
	                    <option value="${item.totalMoney }">${item.payInfo }</option>
	                </c:forEach>
	                <option value="0">其他</option>
	            </select>
            </dd>
        </dl>
        <dl id="otherDiv" style="display: none">
            <dt>其他金额：</dt>
            <dd><input class="required digits" type="text" id="otherMoney" name="otherMoney"></dd>
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
    function rechargeChange(val){
    	if(val==0){
    		$("#otherDiv").show();
    		$("#otherMoney").val("");
    	}else{
    		$("#otherDiv").hide();
    		$("#otherMoney").val(0);
    	}
    }
</script>
</div>

