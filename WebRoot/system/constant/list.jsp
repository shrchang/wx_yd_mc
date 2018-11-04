<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://shiro.apache.org/tags" prefix="shiro" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<form id="pagerForm" rel="pagerForm" method="post" action="<%=basePath %>system/constant/list">
    <input type="hidden" name="pageNum" value="1" />
    <input type="hidden" name="numPerPage" value="${pageInfo.numPerPage}" />
    <input type="hidden" name="busId" value="${pageInfo.busId}"/>
    <input type="hidden" name="searchKey" value="${pageInfo.searchKey}"/>
</form>


<div class="pageHeader">
    <form onsubmit="return navTabSearch(this);" rel="pagerForm" action="<%=basePath %>system/constant/list" method="post">
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
                
                <td>
                        分店名称：<input type="text" name="searchKey" value="${pageInfo.searchKey}"/>
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
            <li><a class="add" href="<%=basePath %>system/constant/add" target="navTab"><span>添加</span></a></li>
            <li class="line">line</li>
            <li><a class="edit" href="<%=basePath %>system/constant/edit/{targetId}" target="navTab" ><span>修改</span></a></li>
            <li class="line">line</li>
            <li><a class="delete" href="<%=basePath %>system/constant/delete/{targetId}" target="ajaxTodo" title="确定要删除吗?"><span>删除</span></a></li>
            <li class="line">line</li>
        </ul>
    </div>
    <table class="table" width="100%" layoutH="115">
        <thead>
            <tr>
                <th>商户名</th>
                <th>KEY</th>
                <th>分组</th>
                <th>名称</th>
                <th>值</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="item" items="${list.rows}">
                <tr target="targetId" rel="${item.id }">
                    <td>${item.busName}</td>
                    <td>${item.constantId}</td>
                    <td>${item.constantGroup}</td>
                    <td>${item.constantName}</td>
                    <td>${item.constantValue}</td>
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
