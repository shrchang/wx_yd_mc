<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://shiro.apache.org/tags" prefix="shiro" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<form id="pagerForm" rel="pagerForm" method="post" action="<%=basePath %>system/roomConfigs/list">
    <input type="hidden" name="pageNum" value="1" />
    <input type="hidden" name="numPerPage" value="${pageInfo.numPerPage}" />
    <input type="hidden" name="busId" value="${pageInfo.busId}"/>
    <input type="hidden" name="shopId" value="${pageInfo.shopId}"/>
    <input type="hidden" name="roomId" value="${pageInfo.roomId}"/>
    <input type="hidden" name="week" value="${pageInfo.roomId}"/>
</form>


<div class="pageHeader">
    <form onsubmit="return navTabSearch(this);" rel="pagerForm" action="<%=basePath %>system/roomConfigs/list" method="post">
    <div class="searchBar">
        <table class="searchContent">
            <tr>
                <shiro:hasRole name="admin">
	                <td>所属商户：</td>
	                <td>
	                     <select id="busId" name="busId" class="combox" ref="shopId" refUrl="<%=basePath %>system/shops/findByBus/{value}">
			                <option value="-1">请选择商户</option>
			                <c:forEach var="item" items="${bus}">
			                    <option value="${item.id }" ${item.id==pageInfo.busId?"selected":"" }>${item.busName }</option>
			                </c:forEach>
			            </select>
	                </td>
                </shiro:hasRole>
                <td>所属分店：</td>
                <td>
                     <select id="shopId" name="shopId" class="combox" ref="roomId" refUrl="<%=basePath %>system/rooms/findByShop/{value}">
                        <option value="-1">请选择分店</option>
                        <c:forEach var="item" items="${shops}">
                             <option value="${item.id }" ${item.id==pageInfo.shopId?"selected":"" }>${item.shopName }</option>
                        </c:forEach>
                    </select>
                </td>
                <td>所属场地：</td>
                <td>
                     <select id="roomId" name="roomId" class="combox">
                        <option value="-1">请选择场地</option>
                        <c:forEach var="item" items="${rooms}">
                             <option value="${item.id }" ${item.id==pageInfo.roomId?"selected":"" }>${item.roomName }</option>
                        </c:forEach>
                    </select>
                </td>
                <td>星期：</td>
                <td>
                     <select id="week" name="week" class="combox">
                        <option value="">请选择星期</option>
                        <option value="1" ${week==1?"selected":"" }>星期一</option>
                        <option value="2" ${week==2?"selected":"" }>星期二</option>
                        <option value="3" ${week==3?"selected":"" }>星期三</option>
                        <option value="4" ${week==4?"selected":"" }>星期四</option>
                        <option value="5" ${week==5?"selected":"" }>星期五</option>
                        <option value="6" ${week==6?"selected":"" }>星期六</option>
                        <option value="0" ${week==0?"selected":"" }>星期日</option>
                    </select>
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
            <li><a class="add" href="<%=basePath %>system/roomConfigs/add" target="navTab" ><span>添加</span></a></li>
            <li class="line">line</li>
            <li><a class="edit" href="<%=basePath %>system/roomConfigs/edit/{targetId}" target="navTab" ><span>修改</span></a></li>
            <li class="line">line</li>
            <li><a class="delete" href="<%=basePath %>system/roomConfigs/delete/{targetId}" target="ajaxTodo" title="确定要删除吗?"><span>删除</span></a></li>
        </ul>
    </div>
    <table class="table" width="100%" layoutH="115">
        <thead>
            <tr>
                <th>分店名称</th>
                <th>场地名称</th>
                <th>星期</th>
                <th>时间段</th>
                <th>价格</th>
                <th>定金比率</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="item" items="${list.rows}">
                <tr target="targetId" rel="${item.id }">
                    <td>${item.shopName}</td>
                    <td>${item.roomName}</td>
                    <td>
                        <c:choose>
                            <c:when test="${item.week==1}">
                                                                                    星期一
                            </c:when>
                            <c:when test="${item.week==2}">
                                                                                    星期二
                            </c:when>
                            <c:when test="${item.week==3}">
                                                                                    星期三
                            </c:when>
                            <c:when test="${item.week==4}">
                                                                                    星期四
                            </c:when>
                            <c:when test="${item.week==5}">
                                                                                    星期五
                            </c:when>
                            <c:when test="${item.week==6}">
                                                                                    星期六
                            </c:when>
                            <c:when test="${item.week==0}">
                                                                                    星期日
                            </c:when>
                        </c:choose>
                    </td>
                    <td>${item.timeRange}</td>
                    <td>${item.roomPrice}</td>
                    <td><fmt:formatNumber value="${item.bookingPriceRate }" type="percent"/></td>
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
