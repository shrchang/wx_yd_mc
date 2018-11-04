<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<div class="pageContent">
<form method="post" action="<%=basePath %>system/orders/${order.id}/updateOrderPrice?navTabId=orderNavTab&callbackType=closeCurrent" class="pageForm required-validate" onsubmit="return validateCallback(this,dialogAjaxDone);">
    <div class="pageFormContent" layoutH="57">
        <p>
            <label>场地价格: </label>
            <input type="text" id="roomPrice" name="roomPrice" value="${order.roomPrice }" readonly="readonly"/>
        </p>
        <p>
            <label>预付金额: </label>
            <input type="text" name="orderMoney" value="${order.orderMoney }" readonly="readonly"/>
        </p>
        <p>
            <label>待付金额: </label>
            <input type="text" name="orderEndMoney" id="orderEndMoney" value="${order.roomPrice-order.orderMoney }" readonly="readonly"/>
        </p>
        <p>
            <label>其他消费: </label>
            <input type="text" name="otherPrice" class="required number" maxlength="16" value="${order.otherPrice}"/>
        </p>
        <p>
            <label>现金优惠: </label>
            <c:forEach var="item" items="${coupons}">
                <input type="checkbox" ${item.checked=="true"?"checked":""} name="coupon" value="${item.id }" freeMoney="${item.freeMoney}" onchange="orderCouponChange(this)"/>${item.couponName }
            </c:forEach>
        </p>
        <p class="nowrap">
            <label>备注: </label>
            <textarea cols="80" rows="2" id="orderDesc" name="orderDesc">${order.orderDesc}</textarea>
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
    function orderCouponChange(ckb){
    	var freeMoney = $(ckb).attr("freeMoney");
    	var orderEndMoney = parseFloat($('#orderEndMoney').val());
    	var roomPrice = parseFloat($('#roomPrice').val());
    	if(ckb.checked){
    		if(orderEndMoney>freeMoney){
	    		$('#orderEndMoney').val(orderEndMoney-freeMoney);
    		}
    		if(roomPrice>freeMoney){
	    		$('#roomPrice').val(roomPrice-freeMoney);
    		}
    	}
    	else{
    		$('#orderEndMoney').val(parseFloat(orderEndMoney)+parseFloat(freeMoney));
    		$('#roomPrice').val(parseFloat(roomPrice)+parseFloat(freeMoney));
    	}
    }
</script>
</div>