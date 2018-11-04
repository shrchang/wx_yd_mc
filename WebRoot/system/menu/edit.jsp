<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<div class="pageContent">
<form method="post" action="<%=basePath %>system/menus/update/${menu.menuId}?navTabId=menuNavTab&callbackType=closeCurrent" class="pageForm required-validate" onsubmit="return validateCallback(this,navTabAjaxDone);">
    <div class="pageFormContent" layoutH="57">
    <fieldset>
        <legend>添加微信菜单信息</legend>
        <dl>
            <dt>菜单名称：</dt>
            <dd><input class="required" id="name" name="name" value="${menu.name}"></dd>
        </dl>
        <dl>
            <dt>菜单类型：</dt>
            <dd>
                <select id="type" name="type" class="combox" onchange="typeOnchange(this.value)">
                    <option value="">组合</option>
                    <option value="view" ${menu.type=="view"?"selected":""}>链接</option>
                    <option value="click" ${menu.type=="click"?"selected":""}>按钮</option>
                </select>
            </dd>
        </dl>
        <dl id="parenIdDiv">
            <dt>父级菜单：</dt>
            <dd>
                <select id="parentId" name="parentId" disabled="disabled">
                    <option value="-1">请选择父级菜单</option>
                    <c:forEach var="item" items="${parents}">
                    	<option value="${item.menuId}" ${item.menuId==menu.parentId?"selected":""}>${item.name}</option>
                    </c:forEach>
                </select>
            </dd>
        </dl>
        <dl id="menuUrlDiv" style="display: none;">
            <dt>菜单链接(素材)：</dt>
            <dd>
                <select id="url" name="url" class="combox" onchange="urlOnchange(this.value)">
                    <option value="-1">请选择素材信息</option>
                    <c:forEach var="item" items="${materials}">
                    	<option value="${item.url}" mediaId="${item.mediaId}" ${item.mediaId==menu.materialMediaId?"selected":""}>${item.title}</option>
                    </c:forEach>
                    <option value="-2" ${menu.materialMediaId==""?"selected":""}>自定义链接</option>
                </select>
                <input id="materialMediaId" name="materialMediaId" type="hidden" value="${menu.materialMediaId}">
                <input type="hidden" id="key" name="key" value="${menu.key}"/>
            </dd>
        </dl>
        <dl class="nowrap" id="customMenuUrlDiv" style='display: ${menu.materialMediaId==""?"block;":"none;"}'>
            <dt>自定义链接值: </dt>
            <dd>
            	<textarea id="customMenuUrl" name="customMenuUrl" cols="80" rows="2">${menu.url}</textarea>
            </dd>
        </dl>
        
        <dl>
            <dt>排序: </dt>
            <dd><input class="digits" type="text" id="orderNumber" name="orderNumber" value="${menu.orderNumber}"></dd>
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
typeOnchange("${menu.type}");
function typeOnchange(menuType){
	if(menuType=='view'){
   		$('#menuUrlDiv').show();
   		$('#parentId').attr("disabled",false); 
   	}else if(menuType=='click'){
		$('#menuUrlDiv').hide();
		$('#customMenuUrlDiv').hide();
		$('#parentId').attr("disabled",false); 
   	}else{
   		$('#menuUrlDiv').hide();
   		$('#customMenuUrlDiv').hide();
   		$('#parentId').val('-1');
   		$('#parentId').attr("disabled",true); 
   	}
}

function urlOnchange(val){
	if(val=='-2'){
		$("#customMenuUrlDiv").show();
	}else{
		$("#customMenuUrlDiv").hide();
	}
	
	var mediaId = $('#url').find('option:selected').attr('mediaId');
    $('#materialMediaId').val(mediaId);
}
</script>
</div>

