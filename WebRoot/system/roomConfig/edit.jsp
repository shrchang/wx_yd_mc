<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<div class="pageContent">
<form method="post" action="<%=basePath %>system/roomConfigs/update/${rc.id}?navTabId=roomConfigNavTab&callbackType=closeCurrent" class="pageForm required-validate" onsubmit="return validateCallback(this,navTabAjaxDone);">
    <div class="pageFormContent" layoutH="57">
    <fieldset>
        <legend>修改场地配置信息</legend>
        <dl>
            <dt>场地名称: </dt>
            <dd><input class="required" readonly="readonly" type="text" id="roomName" name="roomName" value="${rc.roomName}"></dd>
        </dl>
        
        <dl>
            <dt>星期: </dt>
            <dd><input class="required" readonly="readonly" type="text" id="week" name="week" value="${rc.week}"></dd>
        </dl>
        
        <dl>
            <dt>时间段: </dt>
            <dd><input class="required" type="text" id="timeRange" name="timeRange" value="${rc.timeRange}"></dd>
        </dl>
        
        <dl>
            <dt>价格： </dt>
            <dd><input class="required" type="text" id="roomPrice" name="roomPrice" value="${rc.roomPrice}"></dd>
        </dl>
        
        <dl>
            <dt>预定金额比率:  </dt>
            <dd><input class="required" type="text" id="bookingPriceRate" name="bookingPriceRate" value="${rc.bookingPriceRate}"></dd>
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

