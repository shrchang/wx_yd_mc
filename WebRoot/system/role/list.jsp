<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<form id="pagerForm" rel="pagerForm" method="post" action="<%=basePath %>system/roles/list">
    <input type="hidden" name="pageNum" value="1" />
    <input type="hidden" name="numPerPage" value="${pageInfo.numPerPage}" />
    <input type="hidden" name="roleCode" value="${pageInfo.roleCode}"/>
    <input type="hidden" name="roleDesc" value="${pageInfo.roleDesc}"/>
</form>


<div class="pageHeader">
    <form onsubmit="return navTabSearch(this);" rel="pagerForm" action="<%=basePath %>system/roles/list" method="post">
    <div class="searchBar">
        <table class="searchContent">
            <tr>
                <td>
                    角色编码：<input type="text" name="roleCode" value="${pageInfo.roleCode}"/>
                </td>
                
                <td>
                    角色描述：<input type="text" name="roleDesc" value="${pageInfo.roleDesc}"/>
                </td>
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
            <li><a class="add" href="<%=basePath %>system/role/add.jsp" target="dialog"><span>添加</span></a></li>
            <li class="line">line</li>
            <li><a class="icon" href="<%=basePath %>system/roles/setPermissions/{targetId}" target="navTab" ><span>设置权限</span></a></li>
            <li class="line">line</li>
            <li><a class="delete" href="<%=basePath %>system/roles/delete/{targetId}" target="ajaxTodo" title="确定要删除吗?"><span>删除</span></a></li>
            <li class="line">line</li>
        </ul>
    </div>
    <table class="table" width="100%" layoutH="115">
        <thead>
            <tr>
                <th>序号</th>
                <th>权限编码</th>
                <th>权限描述</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="item" items="${list.rows}">
                <tr target="targetId" rel="${item.id }">
                    <td>${item.id}</td>
                    <td>${item.roleCode}</td>
                    <td>${item.roleDesc}</td>
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
