<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<div class="pageContent">
<form method="post" id="addRoomForm" action="<%=basePath %>system/rooms/insert?navTabId=roomNavTab&callbackType=closeCurrent" class="pageForm required-validate">
    <div class="pageFormContent" layoutH="57">
    <fieldset>
        <legend>添加场地信息</legend>
        <dl>
            <dt>场地名称: </dt>
            <dd><input class="required" type="text" id="roomName" name="roomName"></dd>
        </dl>
        
        <dl>
            <dt>所属商户：</dt>
            <dd>
                <select name="busId" id="busId" class="combox" onchange="busOnchange(this)" ref="refShopId" refUrl="<%=basePath %>system/shops/findByBus/{value}">
	                <option value="">请选择商户</option>
	                <c:forEach var="item" items="${bus}">
	                    <option value="${item.id }">${item.busName }</option>
	                </c:forEach>
	            </select>
	            <input id="busName" name="busName" type="hidden"/>
            </dd>
        </dl>
        
        <dl>
            <dt>所属分店：</dt>
            <dd>
                <select id="refShopId" name="shopId" class="combox" onchange="shopOnchange(this)">
                    <option value="">请选择分店</option>
                </select>
                <input id="shopName" name="shopName" type="hidden"/>
            </dd>
        </dl>
        
        <dl>
            <dt>最小人数: </dt>
            <dd><input class="required digits" type="text" id="minPerson" name="minPerson"></dd>
        </dl>
        
         <dl>
            <dt>最大人数: </dt>
            <dd><input class="required digits" type="text" id="maxPerson" name="maxPerson"></dd>
        </dl>
        
        <dl>
            <dt>时间段一: </dt>
            <dd><input class="required" type="text" id="timeOne" name="timeOne" alt="00:00-12:00"></dd>
        </dl>
        
         <dl>
            <dt>时间段二: </dt>
            <dd><input type="text" id="timeTwo" name="timeTwo" alt="12:00-18:00"></dd>
        </dl>
        
        <dl>
            <dt>时间段三: </dt>
            <dd><input type="text" id="timeThree" name="timeThree" alt="18:00-24:00"></dd>
        </dl>
        
         <dl>
            <dt>预定金额比率: </dt>
            <dd><input class="required number" type="text" id="bookingPriceRate" name="bookingPriceRate" min="0.1" max="0.9"></dd>
        </dl>
        
        
        <dl class="nowrap">
            <dt>描述：</dt>
            <dd><textarea cols="80" rows="2" id="roomDesc" name="roomDesc"></textarea></dd>
        </dl>
        <dl class="nowrap">
        	<dt>场地图片：</dt>
        	<dd style="width:300px;">
        		<ul id="pictureUl" class="img-ul-list">
        		</ul>
        	</dd>
        	<input type="button" value="上传图片" onclick="upload();"/>
        </dl>
        
    </fieldset>
    
        
    </div>

    <div class="formBar">
        <ul>
            <li><div class="buttonActive"><div class="buttonContent"><button type="button" onclick="addRoom()">保存</button></div></div></li>
            <li><div class="button"><div class="buttonContent"><button type="button" class="close">关闭</button></div></div></li>
        </ul>
    </div>
    
</form>
<form action="<%=basePath %>system/rooms/upload" id="fileForm" style="display: none;">
   <input type="file" accept="image/png,image/jpeg,image/gif" value="上传" id="fileUpload" name="file" onchange="uploadFile()"/>
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

function upload(){
	$("#fileUpload").click();
}
var basePath= '<%=basePath %>';
/**
 * 上传触发的东西
 */
function uploadFile(){
	var form = new FormData();
	form.append("file",$("#fileUpload")[0].files[0]);
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
				$("#pictureUl").append("<li class='toolBar' path="+res.path+" fileName="+res.fileName+" oldName="+res.oldName+" realPath="+res.realPath+"><a href="+res.path+" target='_blank'>"+res.oldName+"</a><span onclick='delPicture(this)'>删除</span></li>");
				getImgStr();
			}else{
				alertMsg.error(res.retMsg);
			}
		},
		error:DWZ.ajaxError
	});
}

var imgArr = [];
/**
 * 获取图片的内容数据
 */
function getImgStr(){
	var liList = $("#pictureUl li");
	imgArr = [];
	for(var i=0;i<liList.length;i++){
		var obj = {};
		var $li = $(liList[i]);
		obj.pictureRealPath = $li.attr("realPath");
		obj.pictureSrc = $li.attr("path");
		obj.pictureName = $li.attr("oldName");
		obj.pictureRealName = $li.attr("fileName");
		obj.pictureDesc="上传图片";
		imgArr.push(obj);
	}
}

/**
 * 新增场地
 */
function addRoom(){
// 	return validateCallback(form,navTabAjaxDone);
	var $form = $("#addRoomForm");

	if (!$form.valid()) {
		return;
	}
	
	var dataObj = {};
	dataObj.roomName = $("#roomName").val();
	dataObj.busId=$("#busId").val();
	dataObj.busName=$("#busName").val();
	dataObj.shopId=$("#refShopId").val();
	dataObj.shopName=$("#shopName").val();
	dataObj.minPerson=$("#minPerson").val();
	dataObj.maxPerson=$("#maxPerson").val();
	dataObj.timeOne=$("#timeOne").val();
	dataObj.timeTwo=$("#timeTwo").val();
	dataObj.timeThree=$("#timeThree").val();
	dataObj.bookingPriceRate=$("#bookingPriceRate").val();
	dataObj.roomDesc=$("#roomDesc").val();
	dataObj.pictures=imgArr;
	
	var _submitFn = function(){
		$.ajax({
			type:'POST',
			url:basePath+'system/rooms/insert?navTabId=roomNavTab&callbackType=closeCurrent',
			data:JSON.stringify(dataObj),
			traditional:true,
			dataType:"json",
			contentType:"application/json",
			cache: false,
			success: navTabAjaxDone,
			error: DWZ.ajaxError
		});
	}
	_submitFn();
}

function delPicture(li){
	$(li).parent().remove();
	getImgStr();
}
</script>
</div>

