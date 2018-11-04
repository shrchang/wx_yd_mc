<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<div class="pageContent">
<form method="post" action="<%=basePath %>system/faqs/update/${faq.id}?navTabId=faqNavTab&callbackType=closeCurrent" class="pageForm required-validate" onsubmit="return validateCallback(this,navTabAjaxDone);">
    <div class="pageFormContent" layoutH="57">
    <fieldset>
        <legend>修改自动回复信息</legend>
        
        <dl>
            <dt>所属商户：</dt>
            <dd>
                <select id="busId" name="busId" class="combox required" onchange="busOnchange(this)" ref="parentId" refUrl="<%=basePath %>system/faqs/findByBus/{value}">
	                <option value="">请选择关联商户</option>
	                <c:forEach var="item" items="${bus}">
	                    <option value="${item.id }" ${item.id==faq.busId?"selected":""}>${item.busName }</option>
	                </c:forEach>
	            </select>
	             <input id="busName" name="busName" type="hidden" value="${faq.busName}"/>
            </dd>
        </dl>
        
        <dl>
            <dt>父级分类：</dt>
            <dd>
                <select id="parentId" name="parentId" class="combox" onchange="parentIdOnchange(this.value)">
                    <option value="0">请选择父级分类</option>
                    <c:forEach var="item" items="${parents}">
                        <option value="${item.id }" ${item.id==faq.parentId?"selected":""}>${item.faqType }</option>
                    </c:forEach>
                </select>
            </dd>
        </dl>
        <dl>
            <dt>问题编号: </dt>
            <dd><input class="digits" type="text" id="faqNumber" name="faqNumber" value="${faq.faqNumber}"></dd>
        </dl>
        <dl id="faqTypeTxt">
            <dt>问题分类: </dt>
            <dd><input type="text" id="faqType" name="faqType" value="${faq.faqType}"></dd>
        </dl>
        <dl id="faqTitleTxt" style="display: none">
            <dt>问题标题: </dt>
            <dd><input type="text" id="faqTitle" name="faqTitle" value="${faq.faqTitle}"></dd>
        </dl>
        <dl id="faqContextTxt" style="display: none"  class="nowrap">
            <dt>问题内容: </dt>
            <dd><textarea cols="80" rows="2" id="faqContext" name="faqContext">${faq.faqContext}</textarea></dd>
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
parentIdOnchange("${faq.parentId}");
function parentIdOnchange(val){
	if(val=='0'){
		$("#faqTypeTxt").show();
		$("#faqTitleTxt").hide();
		$("#faqContextTxt").hide();
	}else{
		$("#faqTypeTxt").hide();
        $("#faqTitleTxt").show();
        $("#faqContextTxt").show();
	}
}
</script>
</div>

