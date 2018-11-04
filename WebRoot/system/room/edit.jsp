<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<div class="pageContent">
<form method="post" action="<%=basePath %>system/rooms/update/${room.id}?navTabId=roomNavTab&callbackType=closeCurrent" class="pageForm required-validate" onsubmit="return validateCallback(this,navTabAjaxDone);">
    <div class="pageFormContent" layoutH="57">
    <fieldset>
        <legend>修改场地信息</legend>
        <dl>
            <dt>场地名称: </dt>
            <dd><input class="required" type="text" id="roomName" name="roomName" value="${room.roomName}"></dd>
        </dl>
        
        <dl>
            <dt>所属商户：</dt>
            <dd>
                <select name="busId" class="combox" onchange="busOnchange(this)" ref="refShopIdByEdit" refUrl="<%=basePath %>system/shops/findByBus/{value}">
	                <option value="">请选择商户</option>
	                <c:forEach var="item" items="${bus}">
	                    <option value="${item.id }" ${item.id==room.busId?"selected":""}>${item.busName }</option>
	                </c:forEach>
	            </select>
	            <input id="busName" name="busName" type="hidden" value="${room.busName}"/>
            </dd>
        </dl>
        
        <dl>
            <dt>所属分店：</dt>
            <dd>
                <select id="refShopIdByEdit" name="shopId" class="combox" onchange="shopOnchange(this)">
                    <option value="">请选择分店</option>
                    <c:forEach var="item" items="${shops}">
	                    <option value="${item.id }" ${item.id==room.shopId?"selected":""}>${item.shopName }</option>
	                </c:forEach>
                </select>
                <input id="shopName" name="shopName" type="hidden" value="${room.shopName}"/>
            </dd>
        </dl>
        
        <dl>
            <dt>最小人数: </dt>
            <dd><input class="required digits" type="text" id="minPerson" name="minPerson" value="${room.minPerson}"></dd>
        </dl>
        
         <dl>
            <dt>最大人数: </dt>
            <dd><input class="required digits" type="text" id="maxPerson" name="maxPerson" value="${room.maxPerson}"></dd>
        </dl>
        
        <dl class="nowrap">
            <dt>描述：</dt>
            <dd><textarea cols="80" rows="2" id="roomDesc" name="roomDesc">${room.roomDesc}</textarea></dd>
        </dl>
        
        <dl class="nowrap">
        	<dt>场地图片：</dt>
        	<dd style="width:300px;">
        		<ul id="pictureUl" class="img-ul-list">
        			<c:forEach var="item" items="${room.pictures}">
        				<li class="toolBar" path="${item.pictureSrc}" fileName="${item.pictureRealName}" oldName="${item.pictureName}" realPath="${item.pictureRealPath}">
        					<a href="${item.pictureSrc}" target="_blank">${item.pictureName}</a>
        					<span onclick="delPicture(this,${item.id})">删除</span>
        				</li>
        			</c:forEach>
        		</ul>
        	</dd>
        	<input type="button" value="上传图片" onclick="upload(${room.id});"/>
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
<form action="<%=basePath %>system/rooms/upload" id="fileForm" style="display: none;">
   <input type="file" accept="image/png,image/jpeg,image/gif" value="上传" id="fileUpload" name="file" onchange="uploadFile(this)"/>
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

function upload(id){
	$("#fileUpload").click();
	$("#fileUpload").attr("roomId",id);
}

var basePath= '<%=basePath %>';

function uploadFile(input){
	var $this = $(input);
	var roomId = $("#fileUpload").attr("roomId");
	var form = new FormData();
	form.append("file",$("#fileUpload")[0].files[0]);
	form.append("roomId",roomId);
	$.ajax({
		type: 'POST',
		url:basePath+'system/rooms/upload',
		data:form,
		processData:false,
		contentType:false,
		cache: false,
		success: function(res){
			console.log("上传后的数据："+ JSON.stringify(res));
			$("#fileUpload").val("");
			if(res.retCode == '200'){
				$("#pictureUl").append("<li class='toolBar' path="+res.path+" fileName="+res.fileName+" oldName="+res.oldName+" realPath="+res.realPath+
						"><a href="+res.path+" target='_blank'>"+res.oldName+"</a><span onclick='delPicture(this,"+res.id+")'>删除</span></li>");
			}else{
				alertMsg.error(res.retMsg);
			}
		},
		error:DWZ.ajaxError
	});
}

function delPicture(li,id){
	
	$.ajax({
		type: 'POST',
		url:basePath+'system/rooms/picture/del',
		data:{id:id},
		dataType:"json",
		cache: false,
		success: function(res){
			console.log(JSON.stringify(res));
			if(res.code == '200'){
				alertMsg.correct(res.msg);
				$(li).parent().remove();
			}else{
				alertMsg.error(res.msg);
			}
		},
		error:DWZ.ajaxError
	});
}


</script>
</div>

