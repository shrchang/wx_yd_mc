<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://shiro.apache.org/tags" prefix="shiro" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<form id="pagerForm" rel="pagerForm" method="post" action="<%=basePath %>system/menus/list">
    <input type="hidden" name="pageNum" value="1" />
    <input type="hidden" name="numPerPage" value="${pageInfo.numPerPage}" />
    <input type="hidden" name="busId" value="${pageInfo.busId}"/>
    <input type="hidden" name="searchKey" value="${pageInfo.searchKey}"/>
</form>


<div class="pageHeader">
    <form onsubmit="return navTabSearch(this);" rel="pagerForm" action="<%=basePath %>system/menus/list" method="post">
    <div class="searchBar">
        <table class="searchContent">
            <tr>
                <shiro:hasRole name="admin">
	                <td>所属商户：</td>
	                <td>
	                     <select id="busId" name="busId" class="combox">
			                <option value="-1">请选择关联商户</option>
			                <c:forEach var="item" items="${bus}">
			                    <option value="${item.id }" ${item.id==pageInfo.busId?"selected":"" }>${item.busName }</option>
			                </c:forEach>
			            </select>
	                </td>
                </shiro:hasRole>
                <td>菜单名称：<input type="text" name="searchKey" value="${pageInfo.searchKey}"/></td>
                <td>
                    <div class="button"><div class="buttonContent"><button type="submit">查询</button></div></div>
                </td>
            </tr>
        </table>
    </div>
    </form>
</div>
<div class="pageContent">
    <div class="panelBar">
        <ul class="toolBar">
            <li><a class="add" href="<%=basePath %>system/menus/syncMenus" target="ajaxTodo" title="将微信中的菜单同步到本地，同时本地已有数据将被覆盖，确认同步？"><span>同步微信菜单</span></a></li>
            <li class="line">line</li>
            <li><a class="add" href="<%=basePath %>system/menus/createWechatMenu" target="ajaxTodo" title="确认将本地菜单数据应用到微信中吗？"><span>应用菜单到微信</span></a></li>
            <li class="line">line</li>
            <li><a class="add" href="<%=basePath %>system/menus/add" target="navTab"><span>添加</span></a></li>
            <li class="line">line</li>
            <li><a class="edit" href="<%=basePath %>system/menus/edit/{targetId}" target="navTab" ><span>修改</span></a></li>
            <li class="line">line</li>
            <li><a class="delete" href="<%=basePath %>system/menus/delete/{targetId}" target="ajaxTodo" title="确定要删除吗?"><span>删除</span></a></li>
            <li class="line">line</li>
        </ul>
    </div>
    <table class="table" nowrapTD="false" width="100%" layoutH="115">
        <thead>
            <tr>
                <th width="60">所属商户</th>
                <th width="30">ID</th>
                <th width="30">PID</th>
                <th width="30">KEY</th>
                <th width="60">名称</th>
                <th width="30">类型</th>
                <th width="30">顺序</th>
                <th width="30">URL</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="item" items="${list.rows}">
                <tr target="targetId" rel="${item.menuId }">
                    <td>${item.busName }</td>
                    <td>${item.menuId}</td>
                    <td>${item.parentId}</td>
                    <td>${item.key }</td>
                    <td>${item.name}</td>
                    <td>
                        <c:choose>
                            <c:when test='${item.type=="view"}'>
                                                                                    链接
                            </c:when>
                            <c:when test='${item.type=="click"}'>
                                                                                    按钮
                            </c:when>
                            <c:when test='${empty item.type}'>
                                                                                     组合
                            </c:when>
                        </c:choose>
                    </td>
                    <td>${item.orderNumber}</td>
                    <td>${item.url}</td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
    <div class="panelBar">
        <div class="pages">
        <!--  -->
            <span>每页显示</span>
            <select class="combox" name="numPerPage" targetType="navTab" onchange="navTabPageBreak({numPerPage:this.value})">
                <option value="10" ${pageInfo.numPerPage==10?"selected":""}>10</option>
                <option value="20" ${pageInfo.numPerPage==20?"selected":""}>20</option>
                <option value="50" ${pageInfo.numPerPage==50?"selected":""}>50</option>
                <option value="100" ${pageInfo.numPerPage==100?"selected":""}>100</option>
            </select>
            <span>条，共${list.total}条记录</span>
        </div>
        
        <div class="pagination" targetType="navTab" totalCount="${list.total}" numPerPage="${pageInfo.numPerPage}" pageNumShown="5" currentPage="${pageInfo.pageNum}"></div>

    </div>
</div>
