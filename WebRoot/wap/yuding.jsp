<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE html>
<html>

<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <title>确认预订</title>
    <meta name="description" content="">
    <meta name="keywords" content="">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no">
    <link rel="stylesheet" type="text/css" href="<%=basePath %>wap/assets/css/animate.css">
    <link rel="stylesheet" type="text/css" href="<%=basePath %>wap/assets/css/main.css">
    <link href="<%=basePath %>wap/assets/css/weui.min.css" rel="stylesheet">
	<link href="<%=basePath %>wap/assets/css/jquery-weui.css" rel="stylesheet">
</head>

<body class="animated bounceInRight yd-body" youdao="bind">
    <!--<header class="yd-header">确定预订</header>-->
    <div class="weui_panel weui_panel_access">
    <div class="content">
        <form action="<%=basePath%>protal/orders/createOrder" method="post" id="createOrderForm">
            <input type="hidden" id="roomIds" name="roomIds" value="${roomIds}" />
            <input type="hidden" id="bookingDates" name="bookingDates" value="${bookingDates}" />
            <input type="hidden" id="timeRanges" name="timeRanges" value="${timeRanges}" />
            <input type="hidden" id="beforeRoomPrice" name="beforeRoomPrice" value="${roomPrice}" />
            <input type="hidden" id="roomPrice" name="roomPrice" value="${roomPrice}" />
            <input type="hidden" id="orderMoney" name="orderMoney" value="${bookingPrice}" />
            <input type="hidden" id="couponActivityName" name="couponActivityName" value="${bookingPrice}" />
            <section class="section1">
                <div class="changdi">
                    <p>场地信息</p>
                </div>
            </section>
            <!-- section1-ed -->
            <c:forEach items="${orderItems}" var="item" varStatus="status">
                <c:choose>
                    <c:when test="${status.count==1}">
                        <section class="section2">
                    </c:when>
                    <c:otherwise>
                        <section class="section2" style="clear:left;padding-top: 10px;">
                    </c:otherwise>
                </c:choose>
                <div class="step1">
                    <h1><span>${item.shopName}-${item.roomName}</span></h1>
                    <p>预定时间 : <span><fmt:formatDate value="${item.reserveDate}" pattern="yyyy-MM-dd"/> &nbsp; ${item.reserveTime}</span>
                    </p>
                    <p>推荐人数 : <span>${item.personRange}</span>&nbsp;&nbsp;定金：<span>￥${item.bookingPrice}</span><span class="num-style">￥${item.roomPrice}</span>
                    </p>
                </div>
                </section>
            </c:forEach>
            <!-- section3-ed -->
            <section class="section1">
                <div class="changdi">
                    <p>预订信息</p>
                </div>
            </section>
            <!-- section4-ed -->
            <section class="section5">
                <div class="p">
                    <p>
                        <label for="tjmobile">推荐人卡号</label>
                        <input type="tel" id="recommend" name="recommend" placeholder="请输入推荐人卡号" class="yd-input fn-rt">
                    </p>
                </div>
                <div class="p">
                    <p>
                        <label for="ydmobile">预订手机号</label>
                        <input type="tel" placeholder="请输入预订手机号" id="phoneNumber" name="phoneNumber" class="yd-input fn-rt">
                    </p>
                </div>
                <div class="p">
                    <p>
                        <label for="">价格</label><a href="javascritp:void(0);" style="text-decoration:line-through;float:right;">￥${roomPrice}</a>
                    </p>
                </div>
                <div class="p">
                    <p>
                        <label for="">优惠活动</label>
                        <select name="couponActivityId" id="couponActivityId" class="select yd-input" onchange="couponOnchange(this)">
                            <option value="" discount="">请选择优惠活动</option>
                            <c:forEach var="coupon" items="${coupons}">
                                <option value="${coupon.id}" discount="${coupon.discount}">${coupon.couponName}</option>
                            </c:forEach>
                        </select>
                    </p>
                </div>
                <div class="p">
                    <p>
                        <label for="">优惠价</label><span class="fudong red fn-rt" id="discountPrice">￥${roomPrice}</span>
                    </p>
                </div>
                <div class="p">
                    <p>
                        <label for="">定金</label><span class="fudong red fn-rt" id="bookingPriceSpan">￥${bookingPrice}</span>
                    </p>
                </div>
                <!-- 
		        <div class="p">
		        <p style="color:#ff0000;font-size: 14px;">信息说明信息说明信息说明信息说明...</p>
		        </div>
		        -->
            </section>
            <!-- section5-ed -->
            <br>
            <footer class="yd-footer" style="height:50px"> <a href="" class="subbtn quxiao">取消订单</a>
                <a href="javascript:void(0);" onclick="submitOrder()" class="subbtn tijiao">提交订单</a>
            </footer>
    </div>
    </div>
    </form>
    <script type='text/javascript' src="<%=basePath %>wap/assets/js/jquery.min.js" charset='utf-8'></script>
    <script type="text/javascript" src="<%=basePath %>wap/assets/js/jquery-weui.js"></script>
    <script type="text/javascript">
        function submitOrder() {
            var phoneNumber = $('#phoneNumber').val();
            if (phoneNumber == '') {
                $.alert("请输入预定手机号");
                return;
            }
            if (!/^(13[0-9]|14[0-9]|15[0-9]|18[0-9])\d{8}$/i.test(phoneNumber)) {
                $.alert("请输入正确的手机号");
                return;
            }
            $("#createOrderForm").submit();
        }

        function couponOnchange(obj) {
            var discount = $(obj).find("option:selected").attr("discount");
            var couponName = $(obj).find("option:selected").text();
            $('#couponActivityName').val(couponName);
            console.log(discount);
            if (discount != '') {
                $('#roomPrice').val(Math.round(${roomPrice} * discount));
                $('#discountPrice').text('￥' + Math.round(${roomPrice} * discount));
                $('#bookingPriceSpan').text('￥' + Math.round(${bookingPrice} * discount));
                $('#orderMoney').val(Math.round(${bookingPrice} * discount));
            } else {
                $('#roomPrice').val(${roomPrice});
                $('#discountPrice').text('￥' + ${roomPrice});
                $('#bookingPriceSpan').text('￥' + ${bookingPrice});
                $('#orderMoney').val(${bookingPrice});
            }
        }
    </script>
</body>

</html>