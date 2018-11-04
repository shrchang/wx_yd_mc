<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<div class="pageContent">
<form method="post" action="<%=basePath %>system/coupons/update/${coupon.id}?navTabId=couponNavTab&callbackType=closeCurrent" class="pageForm required-validate" onsubmit="return validateCallback(this,navTabAjaxDone);">
    <div class="pageFormContent" layoutH="57">
    <fieldset>
        <legend>修改优惠信息</legend>
        <dl>
            <dt>优惠名称: </dt>
            <dd><input class="required" type="text" name="couponName" value="${coupon.couponName}"/></dd>
        </dl>
        
        <dl>
            <dt>所属商户：</dt>
            <dd>
                <select name="busId" class="combox required" onchange="busOnchange(this)" ref="refShopIdByCouponAdd" refUrl="<%=basePath %>system/shops/findByBus/{value}">
	                <option value="">请选择商户</option>
	                <c:forEach var="item" items="${bus}">
	                    <option value="${item.id }" ${item.id==coupon.busId?"selected":""}>${item.busName }</option>
	                </c:forEach>
	            </select>
	            <input id="busName" name="busName" type="hidden" value="${coupon.busName}"/>
            </dd>
        </dl>
        
        <dl>
            <dt>所属分店：</dt>
            <dd>
                <select id="refShopIdByCouponAdd" name="shopId" class="combox required" onchange="shopOnchange(this)">
                    <option value="">请选择分店</option>
                    <c:forEach var="item" items="${shops}">
                        <option value="${item.id }" ${item.id==coupon.shopId?"selected":""}>${item.shopName }</option>
                    </c:forEach>
                </select>
                <input id="shopName" name="shopName" type="hidden" value="${coupon.shopName}"/>
            </dd>
        </dl>
        
        <dl>
            <dt>优惠类型：</dt>
            <dd>
                <select name="couponType" class="combox required" onchange="couponTypeOnChange(this.value)">
                    <option value="DISCOUNT" ${coupon.couponType=="DISCOUNT"?"selected":""}>普通折扣</option>
                    <option value="PERIOD" ${coupon.couponType=="PERIOD"?"selected":""}>提前预定折扣</option>
                    <option value="CASH" ${coupon.couponType=="CASH"?"selected":""}>现金优惠</option>
                </select>
            </dd>
        </dl>
        
        <dl id="discountTxt">
            <dt>折扣：</dt>
            <dd><input class="number" id="discount" name="discount" min="0" max="0.9" value="${coupon.discount}"/></dd>
        </dl>
        
        <dl id="startDateTxt">
            <dt>开始时间：</dt>
            <dd><input class="date" id="startDate" name="startDate" readonly="readonly" value="${coupon.startDate}"/></dd>
        </dl>
        
        <dl id="endDateTxt">
            <dt>结束时间：</dt>
            <dd><input class="date" id="endDate" name="endDate" readonly="readonly" value="${coupon.endDate}"/></dd>
        </dl>
        
        <dl id="freeMoneyTxt" style="display: none">
            <dt>优惠现金：</dt>
            <dd><input class="number" id="freeMoney" name="freeMoney" value="${coupon.freeMoney}"/></dd>
        </dl>
        
        <dl id="beforeDayTxt" style="display: none">
            <dt>提前预定天数: </dt>
            <dd><input class="digits" type="text" id="beforeDay" name="beforeDay" value="${coupon.beforeDay}"/>
            </dd>
        </dl>
        
        <dl class="nowrap">
            <dt>描述：</dt>
            <dd><textarea cols="60" rows="2" name="couponDesc">${coupon.couponDesc}</textarea></dd>
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
couponTypeOnChange("${coupon.couponType}")
function busOnchange(obj){
	var busName=$(obj).find("option:selected").text();
	$("#busName").val(busName);
}
function shopOnchange(obj){
    var busName=$(obj).find("option:selected").text();
    $("#shopName").val(busName);
}

function couponTypeOnChange(couponType){
	if(couponType=="DISCOUNT"){
		$("#freeMoneyTxt").hide();
		$("#beforeDayTxt").hide();
		//$("#freeMoney").val("");
        //$("#beforeDay").val("");
		$("#discountTxt").show();
		$("#startDateTxt").show();
		$("#endDateTxt").show();
	}else if(couponType=="CASH"){
		$("#freeMoneyTxt").show();
        $("#startDateTxt").show();
        $("#endDateTxt").show();
        $("#beforeDayTxt").hide();
        $("#discountTxt").hide();
        //$("#beforeDay").val("");
        //$("#discount").val("");
	}else if(couponType=="PERIOD"){
        $("#beforeDayTxt").show();
        $("#discountTxt").show();
		$("#freeMoneyTxt").hide();
        $("#startDateTxt").hide();
        $("#endDateTxt").hide();
        //$("#freeMoneyTxt").val("");
        //$("#startDateTxt").val("");
        //$("#endDateTxt").val("");
	}
}
</script>
</div>

