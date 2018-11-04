<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<div class="pageContent">
<form method="post" action="<%=basePath %>system/shoplinks/update/${linkConfig.id}?navTabId=shoplinksNavTab&callbackType=closeCurrent" class="pageForm required-validate" onsubmit="return validateCallback(this,navTabAjaxDone);">
    <div class="pageFormContent" layoutH="57">
    <fieldset>
        <legend>修改分店链接信息</legend>
        <dl>
            <dt>链接名称: </dt>
            <dd><input class="required" type="text" id="linkName" name="linkName" value="${linkConfig.linkName}"></dd>
        </dl>
        
        
        <dl class="nowrap">
            <dt>URL：</dt>
            <dd><textarea cols="80" rows="2" id="linkPath" name="linkPath">${linkConfig.linkPath}</textarea></dd>
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

