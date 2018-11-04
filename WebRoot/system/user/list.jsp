<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<form id="pagerForm" rel="pagerForm" method="post" action="<%=basePath %>system/users/list">
    <input type="hidden" name="pageNum" value="1" />
    <input type="hidden" name="numPerPage" value="${pageInfo.numPerPage}" />
    <input type="hidden" name="userName" value="${pageInfo.userName}"/>
    <input type="hidden" name="userId" value="${pageInfo.userId}"/>
</form>


<div class="pageHeader">
    <form onsubmit="return navTabSearch(this);" rel="pagerForm" action="<%=basePath %>system/users/list" method="post">
    <div class="searchBar">
        <table class="searchContent">
            <tr>
                <td>
                    登陆ID：<input type="text" name="userId" value="${pageInfo.userId}"/>
                </td>
                
                <td>
                    用户名称：<input type="text" name="userName" value="${pageInfo.userName}"/>
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
            <li><a class="add" href="<%=basePath %>system/users/add" target="dialog" title="添加用户" height="350"><span>添加</span></a></li>
            <li class="line">line</li>
            <li><a class="edit" href="<%=basePath %>system/users/edit/{userId}" target="dialog"><span>修改</span></a></li>
            <li class="line">line</li>
            <li><a class="icon" href="<%=basePath %>system/users/resetPwd/{userId}" target="ajaxTodo" title="确定要重置密码为8个8吗？"><span>重置密码</span></a></li>
            <li class="line">line</li>
            <li><a class="delete" href="<%=basePath %>system/users/delete/{userId}" target="ajaxTodo" title="确定要删除吗?"><span>删除</span></a></li>
            <li class="line">line</li>
        </ul>
    </div>
    <table class="table" width="100%" layoutH="115">
        <thead>
            <tr>
                <th>ID</th>
                <th>登陆账号</th>
                <th>用户名称</th>
                <th>关联商户</th>
                <th>手机号</th>
                <th>邮箱</th>
                <th>创建时间</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="item" items="${userList.rows}" varStatus="s">
                <tr target="userId" rel="${item.id }">
                    <td>${item.id}</td>
                    <td>${item.userId}</td>
                    <td>${item.userName}</td>
                    <td>${item.busName}</td>
                    <td>${item.phoneNumber}</td>
                    <td>${item.email}</td>
                    <td><fmt:formatDate value="${item.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
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
            <span>条，共${userList.total}条记录</span>
        </div>
        
        <div class="pagination" targetType="navTab" totalCount="${userList.total}" numPerPage="${pageInfo.numPerPage}" pageNumShown="5" currentPage="${pageInfo.pageNum}"></div>

    </div>
</div>
