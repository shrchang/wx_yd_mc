<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<div class="pageContent sortDrag" selector="h1">
    <div class="panel">
        <h1>订单信息-${order.status }</h1>
        <div class="pageFormContent">
            <fieldset>
                <dl>
		            <dt>订单编号：</dt>
		            <dd>${order.orderNumber }</dd>
		        </dl>
		        <dl>
                    <dt>所属商户：</dt>
                    <dd>${order.busName }</dd>
                </dl>
                <dl>
                    <dt>所属分店：</dt>
                    <dd>${order.shopName }</dd>
                </dl>
                <dl>
                    <dt>场地价格：</dt>
                    <dd>${order.beforeRoomPrice }</dd>
                </dl>
                <dl>
                    <dt>折扣价格：</dt>
                    <dd>${order.roomPrice }</dd>
                </dl>
                <dl>
                    <dt>其他消费：</dt>
                    <dd>${order.otherPrice }</dd>
                </dl>
                <dl>
                    <dt>定金：</dt>
                    <dd>${order.orderMoney }</dd>
                </dl>
                <dl>
                    <dt>用户名：</dt>
                    <dd>${order.userName }</dd>
                </dl>
                <dl>
                    <dt>手机号：</dt>
                    <dd>${order.phoneNumber }</dd>
                </dl>
                <dl>
                    <dt>推荐人卡号：</dt>
                    <dd>${order.recommend }</dd>
                </dl>
                <dl>
                    <dt>订单日期：</dt>
                    <dd><fmt:formatDate value="${order.orderDate }" pattern="yyyy-MM-dd HH:mm:ss"/></dd>
                </dl>
                <dl>
                    <dt>备注：</dt>
                    <dd>${order.orderDesc }</dd>
                </dl>
            </fieldset>
        </div>
    </div>
    
    <div class="panel">
        <h1>预定信息</h1>
        <div>
            <table class="list" width="98%">
                <thead>
                    <tr>
                        <th>预定分店</th>
                        <th>预定场地</th>
                        <th>预定日期</th>
                        <th>预定时间</th>
                        <th>场地价格</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="roomItem" items="${roomItems}">
                        <tr>
                            <td align="center">${roomItem.shopName }</td>
	                        <td align="center">${roomItem.roomName }</td>
	                        <td align="center"><fmt:formatDate value="${roomItem.reserveDate }" pattern="yyyy-MM-dd"/></td>
	                        <td align="center">${roomItem.reserveTime }</td>
	                        <td align="center">${roomItem.roomPrice }</td>
	                    </tr>
                    </c:forEach>
                    
                </tbody>
            </table>
        </div>
    </div>
    
    <div class="panel" defH="80">
        <h1>支付信息</h1>
        <div>
            <table class="list" width="98%">
                <thead>
                    <tr>
                        <th>支付类型</th>
                        <th>支付方式</th>
                        <th>支付金额</th>
                        <th>代付用户名</th>
                        <th>代付会员卡</th>
                        <th>付款流水号</th>
                        <th>支付状态</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="payItem" items="${payItems}">
	                    <tr>
	                        <td align="center">${payItem.payType=="booking_money"?"支付定金":"支付全款" }</td>
	                        <td align="center">
	                           <c:choose>
	                               <c:when test='${payItem.payMode=="alipay"}'>支付宝</c:when>
	                               <c:when test='${payItem.payMode=="tenpay"}'>微信支付</c:when>
	                               <c:when test='${payItem.payMode=="card"}'>会员卡</c:when>
	                               <c:when test='${payItem.payMode=="replace"}'>会员卡代付</c:when>
	                           </c:choose>
	                        </td>
	                        <td align="center">${payItem.payMoney }</td>
	                        <td align="center">${payItem.payForAnotherName }</td>
	                        <td align="center">${payItem.userCardNumber }</td>
	                        <td align="center">${payItem.swiftNumber }</td>
	                        <td align="center">
	                           <c:choose>
                                   <c:when test='${payItem.status=="paid"}'>已支付</c:when>
                                   <c:when test='${payItem.status=="unpaid"}'>未支付</c:when>
                                   <c:when test='${payItem.status=="affirm"}'>待确认</c:when>
                               </c:choose>
	                        </td>
	                    </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
</div>
