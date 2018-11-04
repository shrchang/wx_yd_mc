<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<div class="pageContent">
<form method="post" action="<%=basePath %>system/roles/insertPermissions/${roleId}?navTabId=roleNavTab&callbackType=closeCurrent" class="pageForm required-validate" onsubmit="return validateCallback(this,navTabAjaxDone);">
    <div class="pageFormContent" layoutH="57" >
        	权限列表
        <ul class="ztree" id="permissionsTree">
		</ul>
        <input name="permissionIds" type="hidden" id="permissionIds"/>
        <input name="roleId" id="roleId" type="hidden" value="${roleId}"/>
    </div>

    <div class="formBar">
        <ul>
            <li><div class="buttonActive"><div class="buttonContent"><button type="submit">保存</button></div></div></li>
            <li><div class="button"><div class="buttonContent"><button type="button" class="close">关闭</button></div></div></li>
        </ul>
    </div>
</form>
</div>

<script type="text/javascript">

	var setting = {
        data: {
            simpleData: {
                enable: true
            }
        },
        check: {
            enable: true
        },
        callback: {
            onCheck: onCheck
        }
    };
	var zNodes = ${treeData};
    $.fn.zTree.init($("#permissionsTree"), setting, zNodes);
    function onCheck(){
    	var zTree = $.fn.zTree.getZTreeObj("permissionsTree");
        var checkPerms = zTree.getCheckedNodes(true);
        var permIds = '';
        for(var i=0;i<checkPerms.length;i++){
        	permIds += checkPerms[i].id+',';
        }
        $("#permissionIds").val(permIds.substring(0,permIds.length-1));
    }
</script>