<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://shiro.apache.org/tags" prefix="shiro" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<form id="pagerForm" rel="pagerForm" method="post" action="<%=basePath %>system/orders/list">
    <input type="hidden" name="pageNum" value="1" />
    <input type="hidden" name="numPerPage" value="${pageInfo.numPerPage}" />
    <input type="hidden" name="busId" value="${pageInfo.busId}"/>
    <input type="hidden" name="shopId" value="${pageInfo.shopId}"/>
    <input type="hidden" name="searchKey" value="${pageInfo.searchKey}"/>
    <input type="hidden" name="orderField" value="${pageInfo.orderField}" />
    <input type="hidden" name="orderDirection" value="${pageInfo.orderDirection}" />
</form>


<div class="pageHeader">
    <form onsubmit="return navTabSearch(this);" rel="pagerForm" action="<%=basePath %>system/orders/list" method="post">
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
                <td>预定场地：</td>
                <td>
                     <select id="roomId" name="roomId" class="combox">
                        <option value="-1">请选择场地</option>
                        <c:forEach var="item" items="${rooms}">
                             <option value="${item.id}" ${item.id==pageInfo.roomId?"selected":"" }>${item.roomName }</option>
                        </c:forEach>
                    </select>
                </td>
                <td>
                                            预定日期：<input type="text" name="bookingDates" value="${pageInfo.bookingDates}" class="date textInput readonly valid" readonly="true">
                </td>
                <td>
                                       用户名称：<input type="text" name="searchKey" value="${pageInfo.searchKey}"/>
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
            <li><a class="delete" href="<%=basePath %>system/orders/delete/{targetId}" target="ajaxTodo" title="确定要删除吗?"><span>删除</span></a></li>
            <li class="line">line</li>
        </ul>
    </div>
    <table class="table" width="100%" layoutH="115">
        <thead>
            <tr>
                <th>订单编号</th>
                <th orderField="busName" <c:if test='${pageInfo.orderField == "busName" }'> class="${pageInfo.orderDirection}"  </c:if>>所属商户</th>
                <th>所属分店</th>
                <th>预定场地</th>
                <th>预定日期</th>
                <th orderField="beforeRoomPrice" <c:if test='${pageInfo.orderField == "beforeRoomPrice" }'> class="${pageInfo.orderDirection}"  </c:if>>场地价格</th>
                <th orderField="roomPrice" <c:if test='${pageInfo.orderField == "roomPrice" }'> class="${pageInfo.orderDirection}"  </c:if>>折扣价格</th>
                <th orderField="otherPrice" <c:if test='${pageInfo.orderField == "otherPrice" }'> class="${pageInfo.orderDirection}"  </c:if>>其他消费</th>
                <th orderField="orderMoney" <c:if test='${pageInfo.orderField == "orderMoney" }'> class="${pageInfo.orderDirection}"  </c:if>>定金</th>
                <th orderField="userName" <c:if test='${pageInfo.orderField == "userName" }'> class="${pageInfo.orderDirection}"  </c:if>>用户名</th>
                <th>手机号</th>
                <th>推荐人卡号</th>
                <th orderField="orderDate" <c:if test='${pageInfo.orderField == "orderDate" }'> class="${pageInfo.orderDirection}"  </c:if>>订单日期</th>
                <th orderField="status" <c:if test='${pageInfo.orderField == "status" }'> class="${pageInfo.orderDirection}"  </c:if>>状态</th>
                <th>操作</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="item" items="${list.rows}">
                <tr target="targetId" rel="${item.id }">
                    <td align="center"><a href="<%=basePath %>system/orders/${item.id}" target="navTab" style="color: blue;" title="订单详情">${item.orderNumber}</a></td>
                    <td align="center">${item.busName}</td>
                    <td align="center">${item.shopName}</td>
                    <td align="center">${item.roomNames}</td>
                    <td align="center">${item.bookingDates}</td>
                    <td align="center">${item.beforeRoomPrice}</td>
                    <td align="center">${item.roomPrice}</td>
                    <td align="center">${item.otherPrice}</td>
                    <td align="center">${item.orderMoney}</td>
                    <td align="center">${item.userName}</td>
                    <td align="center">${item.phoneNumber}</td>
                    <td align="center">${item.recommend}</td>
                    <td align="center"><fmt:formatDate value="${item.orderDate }" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                    <td align="center">
                        <c:choose>
                            <c:when test='${item.status=="UNPAID"}'>待支付</c:when>
                            <c:when test='${item.status=="AFFIRM"}'>待确认</c:when>
                            <c:when test='${item.status=="PAID"}'>已付定金</c:when>
                            <c:when test='${item.status=="FINISH"}'>已完成</c:when>
                            <c:when test='${item.status=="CANCELLED"}'>已取消</c:when>
                            <c:otherwise>未知</c:otherwise>
                        </c:choose>
                    </td>
                    <td>
                        <c:choose>
                            <c:when test='${item.status=="UNPAID" || item.status=="AFFIRM"}'>
                                <a title="是否已经确认收款？" target="ajaxTodo" href="<%=basePath %>system/orders/${item.id}/affirmOrder" style="color: blue;">确认收款</a>
                                <a title="确认取消订单？" target="ajaxTodo" href="<%=basePath %>system/orders/${item.id}/cancelOrder" style="color: blue;">取消</a>
                            </c:when>
                            <c:when test='${item.status=="PAID"}'>
                                <a title="修改订单金额" target="dialog" href="<%=basePath %>system/orders/${item.id}/editPrice" style="color: blue;" height="350">修改订单金额</a>
                                <a title="确认已经收到场地待付尾款【${item.roomPrice-item.orderMoney }】，其他消费金额【${item.otherPrice==null?"0":item.otherPrice }】，总待付金额【${item.roomPrice-item.orderMoney+item.otherPrice }】？" 
                                target="ajaxTodo" href="<%=basePath %>system/orders/${item.id}/paidOrder" style="color: blue;">确认尾款</a>
                            </c:when>
                            <c:otherwise>
                                                                                       无可用操作
                            </c:otherwise>
                        </c:choose>
	                </td>
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
