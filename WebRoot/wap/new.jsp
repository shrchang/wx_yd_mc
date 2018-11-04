<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html>
<html>
    
    <head>
        <meta charset="utf-8">
        <title>在线预订</title>
        <meta name="viewport" content="width=device-width, minimum-scale=1, maximum-scale=1, user-scalable=no">
        <meta content="telephone=no" name="format-detection">
        <script src="<%=basePath %>wap/assets/js/zeptoWithFx.min.js"></script>
        <script src="<%=basePath %>wap/assets/js/mobileResponsive.js"></script>
        <script type="text/javascript">
            var utmSource = '';
        </script>
    </head>
    
    <body data-design-width="750">
        <link href="<%=basePath %>wap/assets/css/book.css" type="text/css" rel="stylesheet">
        <script src="<%=basePath %>wap/assets/js/touchScroll.js"></script>
        <script>
            var minHour = 1; //最小起订时间
            
        </script>
        
        <body data-design-width="750">
            <div id="loading" data-lock="1" class="loading">
                <div class="loading-icon">
                </div>
            </div>
            <div class="main hide" data-due="">
                <div class="header">
                    <div class="nav">
                    	<ul>
                    		<c:forEach var="shop" items="${shops}" varStatus="shopIndex">
                    			<li ${shopId==shop.id?"class='active'":"" } id="${shop.id}" style="width: ${100/fn:length(shops)}%">
                    			    <c:choose>
                    			         <c:when test="${shopId eq shop.id}">
		                    				 <a style="color:#FFFFFF" href="<%=basePath%>protal/init/v2/${systemCode}?shopId=${shop.id}">${shop.shopName}</a>
                    			         </c:when>
                    			         <c:otherwise>
		                    			     <a style="color:#000000" href="<%=basePath%>protal/init/v2/${systemCode}?shopId=${shop.id}">${shop.shopName}</a>
                    			         </c:otherwise>
                    			    </c:choose>
                    			</li>
                    		</c:forEach>
	                    </ul>
                    </div>
                    <div class="shopLink">
                    	<ul>
                    		<c:forEach var="shopLink" items="${shopLinks}">
		                    	<li><a href="${shopLink.linkPath}">${shopLink.linkName}</a></li>
                    		</c:forEach>
	                    </ul>
                    </div>
                </div>
                <div class="book-date">
                    <div class="data-change">
                        <img src="<%=basePath %>wap/assets/images/arrow.png" alt="">
                    </div>
                    <div class="date-wrap">
                        <ul>
                        	<c:forEach var="weekDate" items="${weekDates}">
                        		<a href='<%=basePath%>protal/init/v2/${systemCode}?shopId=${shopId}&time=${weekDate.time}' data-t="${weekDate.time}">
                                <li ${currentDate==weekDate.time?"class='active'":""} ${currentDate}>
                                    <p>${weekDate.weekStr}</p>
                                    <p><fmt:formatDate value="${weekDate.date}" pattern="MM月dd日"/></p>
                                </li>
                            </a>
                        	</c:forEach>
                        </ul>
                    </div>
                </div>
                <div class="book-container">
                    <div class="book-area">
                        <ul>
                        	<c:forEach var="room" items="${rooms}">
                        		<li>${room.roomName}</li>
                        	</c:forEach>
                        </ul>
                    </div>
                    
                    <div id="wrapper">
                        <div id="scroller">
                            <div class="book-list">
                            	<c:forEach var="roomInfos" items="${roomInfos}">
	                                <ul>
	                                	<c:forEach var="configs" items="${roomInfos.roomConfigs}">
	                                		<c:choose>
												<c:when test='${configs.status=="SOLD"}'>
													<li goodsid="${configs.rangeId}" price="${configs.price}" course_content="${roomInfos.roomName},${configs.timeRange},${roomInfos.roomId},${currentDate}" class="sold">
												</c:when>
												<c:when test='${configs.status=="DISABLE"}'>
													<li goodsid="${configs.rangeId}" price="${configs.price}" course_content="${roomInfos.roomName},${configs.timeRange},${roomInfos.roomId},${currentDate}" class="disable">
												</c:when>
												<c:otherwise>
													<li goodsid="${configs.rangeId}" price="${configs.price}" course_content="${roomInfos.roomName},${configs.timeRange},${roomInfos.roomId},${currentDate}" class="available">
												</c:otherwise>
											</c:choose>
		                                        <em>
		                                            ${configs.timeRange}<br/>￥${configs.price}
		                                        </em>
		                                    </li>
	                                    </c:forEach>
	                                </ul>
                            	</c:forEach>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="book-order">
                    <ul class="book-tip">
		                <li>
		                    <div></div>
		                    <div>可预订</div>
		                </li>
		                <li>
		                    <div></div>
		                    <div>已选择</div>
		                </li>
		                <li>
		                    <div></div>
		                    <div>已售完</div>
		                </li>
		                <li>
		                    <div></div>
		                    <div>不可订</div>
		                </li>
		            </ul>
                    <ul class="book-orderinfo hide">
                    </ul>
                    <div class="book-submit J_submit">
                        <div>请选择场地</div>
                    </div>
                </div>
            </div>
            <div class="book-headTip">
                Tips： 本场预订以两小时为单位，请尽量保持场次连续。
            </div>
            <div class="book-noPaySprite hide">
            </div>
            <div class="toast hide">
                <div class="toast-alert">
                    <div class="toast-msg">
                    </div>
                </div>
            </div>
            <div class="hide">
	            <form action="<%=basePath %>protal/orders/bookingPage" method="post" id="bookingForm">
	                <input type="hidden" id="roomIds" name="roomIds"/>
	                <input type="hidden" id="bookingDates" name="bookingDates">
	                <input type="hidden" id="timeRanges" name="timeRanges"/>
	            </form>
            </div>
            <script src="<%=basePath %>wap/assets/js/courtBook.js">
            </script>
            <script src="<%=basePath %>wap/assets/js/book.js">
            </script>
        </body>

</html>