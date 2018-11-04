<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<base href="<%=basePath%>">

		<title>我的订单</title>
		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<meta name="viewport" content="initial-scale=1, maximum-scale=1">
		<meta name="apple-mobile-web-app-capable" content="yes">
		<meta name="apple-mobile-web-app-status-bar-style" content="black">
		<link rel="stylesheet" href="//g.alicdn.com/msui/sm/0.5.8/css/sm.min.css">
		<!-- 
		<link rel="stylesheet" href="<%=basePath%>wap/assets/css/light7.min.css">
		<link rel="stylesheet" href="<%=basePath%>wap/assets/css/light7-extend.min.css">
		 -->
	</head>

	<body>
		<div class="page" id="mainPage">
			<div class="content">
				<div class="buttons-tab">
					<a href="#allOrders" class="button tab-link active">全部</a>
		            <a href="#unpaidOrders" class="button tab-link">待付款</a>
		            <a href="#finishOrders" class="button tab-link">已完成</a>
		            <a href="#cancelOrders" class="button tab-link">已取消</a>
				</div>
				<div class="content-block">
					<div class="tabs">
						<div id="allOrders" class="tab active">
							<div class="content-block" style="margin: -1rem">
							
							    <c:forEach var="orders" items="${orders}">
									<div class="card">
								    	<div class="card-header">${orders.shopName } <c:if test='${sessionScope.WC_USER.openid!=orders.userToken}'>[代付]</c:if>
									    	<c:choose>
									    	   <c:when test='${orders.status=="UNPAID"}'>
									    	       <a href="#" class="link" onclick="paidOrder('${orders.id}')" style="margin-right: 1px">待付定金</a>
									    	   </c:when>
									    	   <c:when test='${orders.status=="AFFIRM"}'>
	                                               <a href="#" class="link" onclick="paidOrder('${orders.id}')" style="margin-right: 1px">待确认</a>
	                                           </c:when>
	                                           <c:when test='${orders.status=="PAID"}'>
	                                               <a href="#" class="link" onclick="paidOrder('${orders.id}')" style="margin-right: 1px">待付全款</a>
	                                           </c:when>
	                                           <c:when test='${orders.status=="FINISH"}'>
	                                               <label style="margin-right: 1px">已完成</label>
	                                           </c:when>
	                                           <c:when test='${orders.status=="CANCELLED"}'>
	                                               <label style="margin-right: 1px">已取消</label>
	                                           </c:when>
									    	</c:choose>
								    	</div>
								    	<div class="card-content">
								      		<div class="card-content-inner">
								      		    <c:forEach var="roomItem" items="${orders.orderItems}">
								      		        <p>${roomItem.roomName }【<fmt:formatDate value="${roomItem.reserveDate }" pattern="yyyy-MM-dd (E)"/> ${roomItem.reserveTime }】</p>
								      		    </c:forEach>
												
												<p class="color-gray">场地价格：￥${orders.roomPrice } 【实际消费：￥${orders.roomPrice+orders.otherPrice }】</p>
												<p class="color-gray">已付定金：￥${orders.status=="UNPAID"?"0":orders.orderMoney }</p>
												<p class="color-gray">待付金额：<font color="#CC3333">￥${orders.status=="UNPAID"?orders.roomPrice:orders.roomPrice-orders.orderMoney }</font></p>
											</div>
								    	</div>
								    	<div class="card-footer">
									      <fmt:formatDate value="${orders.orderDate }" pattern="yyyy-MM-dd HH:mm:ss"/>
									      <c:if test='${orders.status=="UNPAID" or orders.status=="AFFIRM"}'>
										      <a href="#" onclick="cancelOrder('${orders.id}')" class="link">取消订单</a>
									      </c:if>
									    </div>
								  	</div>
							    </c:forEach>
							  	
		      				</div>
						</div>
						<div id="unpaidOrders" class="tab">
					       <div class="content-block" style="margin: -1rem">
                               
                               <c:forEach var="orders" items="${orders}">
                                    <c:if test='${orders.status=="PAID" or orders.status=="UNPAID" or orders.status=="AFFIRM"}'>
	                                    <div class="card">
	                                        <div class="card-header">${orders.shopName } <c:if test='${sessionScope.WC_USER.openid!=orders.userToken}'>[代付]</c:if>
	                                            <c:choose>
	                                               <c:when test='${orders.status=="UNPAID"}'>
	                                                   <a href="#" class="link" onclick="paidOrder('${orders.id}')" style="margin-right: 1px">待付定金</a>
	                                               </c:when>
	                                               <c:when test='${orders.status=="AFFIRM"}'>
	                                                   <a href="#" class="link" onclick="paidOrder('${orders.id}')" style="margin-right: 1px">待确认</a>
	                                               </c:when>
	                                               <c:when test='${orders.status=="PAID"}'>
	                                                   <a href="#" class="link" onclick="paidOrder('${orders.id}')" style="margin-right: 1px">待付全款</a>
	                                               </c:when>
	                                            </c:choose>
	                                        </div>
	                                        <div class="card-content">
	                                            <div class="card-content-inner">
	                                                <c:forEach var="roomItem" items="${orders.orderItems}">
	                                                    <p>${roomItem.roomName }【<fmt:formatDate value="${roomItem.reserveDate }" pattern="yyyy-MM-dd (E)"/> ${roomItem.reserveTime }】</p>
	                                                </c:forEach>
	                                                
	                                                <p class="color-gray">场地价格：￥${orders.roomPrice } 【实际消费：￥${orders.roomPrice+orders.otherPrice }】</p>
	                                                <p class="color-gray">已付定金：￥${orders.status=="UNPAID"?"0":orders.orderMoney }</p>
													<p class="color-gray">待付金额：<font color="#CC3333">￥${orders.status=="UNPAID"?orders.roomPrice:orders.roomPrice-orders.orderMoney }</font></p>
	                                            </div>
	                                        </div>
	                                        <div class="card-footer">
	                                          <fmt:formatDate value="${orders.orderDate }" pattern="yyyy-MM-dd HH:mm:ss"/>
	                                          <c:if test='${orders.status=="UNPAID" or orders.status=="AFFIRM"}'>
	                                              <a href="#" onclick="cancelOrder('${orders.id}')" class="link">取消订单</a>
	                                          </c:if>
	                                        </div>
	                                    </div>
                                    </c:if>
                                </c:forEach>
                               
                            </div>
						</div>
						<div id="finishOrders" class="tab">
							<div class="content-block" style="margin: -1rem">
								
                                <c:forEach var="orders" items="${orders}">
                                    <c:if test='${orders.status=="FINISH"}'>
                                        <div class="card">
                                            <div class="card-header">${orders.shopName } <c:if test='${sessionScope.WC_USER.openid!=orders.userToken}'>[代付]</c:if>
                                                <label style="margin-right: 1px">已完成</label>
                                            </div>
                                            <div class="card-content">
                                                <div class="card-content-inner">
                                                    <c:forEach var="roomItem" items="${orders.orderItems}">
                                                        <p>${roomItem.roomName }【<fmt:formatDate value="${roomItem.reserveDate }" pattern="yyyy-MM-dd (E)"/> ${roomItem.reserveTime }】</p>
                                                    </c:forEach>
                                                    
                                                    <p class="color-gray">场地价格：￥${orders.roomPrice } 【实际消费：￥${orders.roomPrice+orders.otherPrice }】</p>
                                                    <p class="color-gray">已付定金：￥${orders.orderMoney }</p>
													<p class="color-gray">待付金额：<font color="#CC3333">￥${orders.roomPrice-orders.orderMoney }</font></p>
                                                </div>
                                            </div>
                                            <div class="card-footer">
                                              <fmt:formatDate value="${orders.orderDate }" pattern="yyyy-MM-dd HH:mm:ss"/>
                                            </div>
                                        </div>
                                    </c:if>
                                </c:forEach>
                    
							</div>
						</div>
						<div id="cancelOrders" class="tab">
							<div class="content-block" style="margin: -1rem">
								
                                <c:forEach var="orders" items="${orders}">
                                    <c:if test='${orders.status=="CANCELLED"}'>
                                        <div class="card">
                                            <div class="card-header">${orders.shopName } <c:if test='${sessionScope.WC_USER.openid!=orders.userToken}'>[代付]</c:if>
                                                <label style="margin-right: 1px">已取消</label>
                                            </div>
                                            <div class="card-content">
                                                <div class="card-content-inner">
                                                    <c:forEach var="roomItem" items="${orders.orderItems}">
                                                        <p>${roomItem.roomName }【<fmt:formatDate value="${roomItem.reserveDate }" pattern="yyyy-MM-dd (E)"/> ${roomItem.reserveTime }】</p>
                                                    </c:forEach>
                                                    
                                                    <p class="color-gray">场地价格：￥${orders.roomPrice } 【实际消费：￥${orders.roomPrice+orders.otherPrice }】</p>
                                                    <p class="color-gray">已付定金：￥0</p>
													<p class="color-gray">待付金额：<font color="#CC3333">￥${orders.roomPrice}</font></p>
                                                </div>
                                            </div>
                                            <div class="card-footer">
                                              <fmt:formatDate value="${orders.orderDate }" pattern="yyyy-MM-dd HH:mm:ss"/>
                                            </div>
                                        </div>
                                    </c:if>
                                </c:forEach>
                                
							</div>
						</div>
					</div>
				</div>




			</div>
		</div>
		<script type='text/javascript' src='<%=basePath%>wap/assets/js/zepto.min.js' charset='utf-8'></script>
		<script type='text/javascript' src='//g.alicdn.com/msui/sm/0.5.8/js/sm.min.js' charset='utf-8'></script>
		<!-- 
		<script type='text/javascript' src='<%=basePath%>wap/assets/js/light7.min.js' charset='utf-8'> </script>
		<script type='text/javascript' src='<%=basePath%>wap/assets/js/light7-extend.min.js' charset='utf-8'></script>
		 -->
		<script type="text/javascript">
			function cancelOrder(orderId){
			    $.confirm('确认取消订单？','提示信息',function(){
			        $.showPreloader();
			        $.ajax({
			            url:'<%=basePath%>protal/orders/'+orderId+'/cancelOrder',
			            type:'PUT',
			            dataType: 'json',
			            success:function(data){
			                $.hidePreloader();
			                $.alert('取消订单成功！','操作结果',function(){
			                    location.reload();
			                });
			            },
			            error:function(xhr,status){
			                $.hidePreloader();
			                $.alert('操作失败，错误码：'+status,'操作结果');
			            }
			        });
			    });
			}
			
			function paidOrder(orderId){
			     window.location.href = '<%=basePath %>protal/orders/'+orderId+'/payOrderPage';
			}
			
		</script>
	</body>
</html>
