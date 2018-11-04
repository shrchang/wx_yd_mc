<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://shiro.apache.org/tags" prefix="shiro" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<div class="pageContent">
<form method="post" action="<%=basePath %>system/roomConfigs/insert?navTabId=roomConfigNavTab&callbackType=closeCurrent" class="pageForm required-validate" onsubmit="return validateCallback(this,navTabAjaxDone);">
    <div class="pageFormContent" layoutH="57">
    <fieldset>
        <legend>添加场地配置信息</legend>
        
        <shiro:hasRole name="admin">
        <dl>
            <dt>所属商户：</dt>
            <dd>
                <select id="busId" name="busId" class="combox" onchange="busOnchange(this)" ref="cbx_shopId" refUrl="<%=basePath %>system/shops/findByBus/{value}">
                    <option value="-1">请选择商户</option>
                    <c:forEach var="item" items="${bus}">
                        <option value="${item.id }" ${item.id==pageInfo.busId?"selected":"" }>${item.busName }</option>
                    </c:forEach>
                </select>
                <input id="busName" name="busName" type="hidden"/>
            </dd>
        </dl>
        </shiro:hasRole>
        <dl>
            <dt>所属分店：</dt>
            <dd>
                <select id="cbx_shopId" name="shopId" class="combox" onchange="shopOnchange(this)" ref="cbx_roomId" refUrl="<%=basePath %>system/rooms/findByShop/{value}">
                    <option value="-1">请选择分店</option>
                    <c:forEach var="item" items="${shops}">
                         <option value="${item.id }" ${item.id==pageInfo.shopId?"selected":"" }>${item.shopName }</option>
                    </c:forEach>
                </select>
                <input id="shopName" name="shopName" type="hidden"/>
            </dd>
        </dl>
        
        <dl>
            <dt>所属场地：</dt>
            <dd>
                <select id="cbx_roomId" name="roomId" class="combox" onchange="roomOnchange(this)">
                    <option value="-1">请选择场地</option>
                    <c:forEach var="item" items="${rooms}">
                         <option value="${item.id }" ${item.id==pageInfo.roomId?"selected":"" }>${item.roomName }</option>
                    </c:forEach>
                </select>
                <input id="roomName" name="roomName" type="hidden"/>
            </dd>
        </dl>
        
        <dl class="nowrap">
            <dt>时间范围：</dt>
            <dd>
            <textarea cols="80" rows="2" class="required" id="timeRange" name="timeRange"></textarea><span class="info">(多个时间段以'|'分隔，如00:00-12:00|12:30-21:00)</span>
            </dd>
        </dl>
        
        <dl class="nowrap">
            <dt>星期：</dt>
            <dd>
            <input type="checkbox" name="weeks" value="1"  class="required"/>星期一
            <input type="checkbox" name="weeks" value="2"  class="required"/>星期二
            <input type="checkbox" name="weeks" value="3"  class="required"/>星期三
            <input type="checkbox" name="weeks" value="4"  class="required"/>星期四
            <input type="checkbox" name="weeks" value="5"  class="required"/>星期五
            <input type="checkbox" name="weeks" value="6"  class="required"/>星期六
            <input type="checkbox" name="weeks" value="0"  class="required"/>星期日
            </dd>
        </dl>
        
         <dl>
            <dt>价格： </dt>
            <dd><input type="text" id="roomPrice" name="roomPrice" class="required"></dd>
        </dl>
        
         <dl>
            <dt>预定金额比率：</dt>
            <dd><input class="required number" type="text" id="bookingPriceRate" name="bookingPriceRate" min="0.1" max="0.9" alt="0.5"></dd>
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
function roomOnchange(obj){
    var roomName=$(obj).find("option:selected").text();
    $("#roomName").val(roomName);
}
</script>
</div>

