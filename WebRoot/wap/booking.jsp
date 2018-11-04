<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%> 
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>在线预订</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=0, minimum-scale=1.0, maximum-scale=1.0">
  	<meta name="apple-mobile-web-app-capable" content="yes">
  	<meta name="apple-mobile-web-app-status-bar-style" content="black">
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	<script type="text/javascript" src="<%=basePath %>wap/assets/js/jquery.min.js"></script>
	<script type="text/javascript" src="<%=basePath %>wap/assets/js/tableHeadFixer.js"></script>
	<script type="text/javascript" src="<%=basePath %>wap/assets/js/jquery-weui.js"></script>
	<link href="<%=basePath %>wap/assets/css/weui.min.css" rel="stylesheet">
	<link href="<%=basePath %>wap/assets/css/jquery-weui.css" rel="stylesheet">
	<link href="<%=basePath %>wap/assets/bootstrap-3.3.2/css/bootstrap.css" rel="stylesheet">
	<script type="text/javascript">
		$(document).ready(function() {
			$(".table").tableHeadFixer({"left" : 1}); 
			$('#tableDiv').css("height",$(window).height()-125);
			//$.alert("自定义的消息内容","提示信息");
			$(".weui_navbar_item").click(function() {
				var shopId = $(this).attr("id");
				//$(".weui_bar_item_on").removeClass("weui_bar_item_on");
				//$(this).addClass("weui_bar_item_on");
				window.location.href = '<%=basePath%>protal/init/<%=session.getAttribute("systemCode")%>?shopId='+shopId;
			});
			$(":radio").click(function(){
				window.location.href = '<%=basePath%>protal/init/<%=session.getAttribute("systemCode")%>?shopId='+$(this).val();
			});
			
			$(":button").click(function(){
				var status = $(this).attr("status");
				if(status!='OK'){
					return;
				}
				var price = Number($(this).attr("price"));
				var roomName = $(this).attr("roomName");
				var timeRange = $(this).attr("timeRange");
				var bookingDate = $(this).attr("bookingDate");
				var shopName = $(this).attr("shopName");
				var roomId = $(this).attr("roomId");
				var configId = $(this).attr("configId");
				var rangeId = $(this).attr("rangeId");
				
				
				var totalPrice = Number($("#totalPrice").text());
				
				var append = true;
				$("#bookingRoom p").each(function(){  
				    var tempConfigId = $(this).attr("configId"); 
				    if(configId==tempConfigId){
				    	$(this).remove();
				    	append = false;
				    	totalPrice = totalPrice-price;
				    	return;
				    }
				});
				var list = $("#bookingRoom p");
				if(list.length>=4){
					$.alert("一次最多预定四个场地");
					return;
				}
				if(append){
					$(this).addClass("btn-success");
					$('#tableDiv').css("height",$('#tableDiv').height()-28);
					$('#footerInfo').css("height",$('#footerInfo').height()+28);
					$("#bookingRoom").append("<p class='weui_media_desc' style='margin-top: 1px;' timeRange='"+rangeId+"' roomId='"+roomId+"' bookingDate='"+bookingDate+"' configId='"+configId+"'>"+shopName+" "+roomName+" "+bookingDate+"("+timeRange+") ￥"+price+"</p>");
					totalPrice = totalPrice+price;
				}else{
					$(this).removeClass("btn-success");
					$('#tableDiv').css("height",$('#tableDiv').height()+28);
					$('#footerInfo').css("height",$('#footerInfo').height()-28);
				}
				 $("#totalPrice").text(totalPrice);
			});
			
			$("#sbtBooking").click(function(event){
		    	var bookingDates = "";
		    	var roomIds = "";
		    	var timeranges = "";
		    	$("#bookingRoom p").each(function(){  
				    bookingDates += $(this).attr('bookingDate')+"|";  
				    roomIds += $(this).attr('roomId')+"|";  
				    timeranges += $(this).attr('timeRange')+"|";  
				});
		    	if(roomIds==""){
		    		alert("请选择需要预定的场次");
		    		return;
		    	}
		    	$('#roomIds').val(roomIds);
		    	$('#bookingDates').val(bookingDates);
		    	$('#timeRanges').val(timeranges);
		    	$("#bookingForm").submit();
		    });
			
		});
		
		$(window).resize(function() {
			$('#tableDiv').css("height",$(window).height()-125);
		});
	</script>
  </head>
  
  <body>
  <div class="weui_tab">
	  <div class="weui_navbar">
	   	<c:forEach var="shop" items="${shops}" varStatus="contextIndex">
			<a class='weui_navbar_item ${shop.id==shopId?"weui_bar_item_on":"" }' id="${shop.id}"> <input type="radio"  ${shop.id==shopId?"checked":"" } name="shopRadio" value="${shop.id}"/>${shop.shopName} </a>
		</c:forEach>
	  </div>
	  <div class="weui_tab_bd">
			<div style='height: 500px;' id="tableDiv">
				<table  class="table table-bordered">
					<thead>
						<tr class="info">
							<th style="text-align: center;vertical-align: middle;font-size: 13px">日期/场地</th>
							<c:forEach var="room" items="${rooms}">
								<th style="text-align: center;vertical-align: middle;">${room.roomName}</th>
							</c:forEach>
						</tr>
					</thead>
					<tbody>
					
							<c:forEach  var="views" items="${dates}">
								<tr>
									<td class="info" style="min-width:80px;text-align: center;vertical-align: middle;font-size: 13px">
										<c:if test='${views.week==0 or views.week==6}'>
											<font color="blue"><fmt:formatDate value="${views.bookingDate }" pattern="MM-dd"/><br/>(${views.weekStr})</font>
										</c:if>
										<c:if test='${views.week>0 and views.week<6}'>
											<fmt:formatDate value="${views.bookingDate }" pattern="MM-dd"/><br/>(${views.weekStr})
										</c:if>
									</td>
									<c:forEach var="roomInfos" items="${views.roomInfo}">
											<c:if test="${fn:length(roomInfos.roomConfigs)==2}">
											     <td style="min-width:220px;text-align: center;vertical-align: middle;">
											</c:if>
											<c:if test="${fn:length(roomInfos.roomConfigs)==1}">
                                                 <td style="min-width:110px;text-align: center;vertical-align: middle;">
                                            </c:if>
                                            <c:if test="${fn:length(roomInfos.roomConfigs)>2}">
                                                <td style="min-width:330px;text-align: center;vertical-align: middle;">
                                            </c:if>
											<c:forEach var="configs" items="${roomInfos.roomConfigs}">
												<c:choose>
													<c:when test='${configs.status=="SOLD"}'>
														<button class="btn btn-sm btn-danger opacity" style="border-radius:20px;margin: 2px" type="button">${configs.timeRange}</button>
													</c:when>
													<c:when test='${configs.status=="DISABLE"}'>
														<button class="btn btn-sm btn-warning opacity" style="border-radius:20px;margin: 2px" type="button">${configs.timeRange}</button>
													</c:when>
													<c:otherwise>
														<button class="btn btn-sm btn-default" style="border-radius:20px;margin-top: 2px;border: 1px solid #007aff;" type="button" shopName="${views.shopName}" configId="${configs.configId}" rangeId="${configs.rangeId}" roomId="${roomInfos.roomId}" roomName="${roomInfos.roomName}" price="${configs.price}" bookingDate="<fmt:formatDate value="${views.bookingDate }" pattern="yyyy-MM-dd"/>" timeRange="${configs.timeRange}" status="${configs.status}">${configs.timeRange}</button>
													</c:otherwise>
												</c:choose>
											</c:forEach>
										</td>
									</c:forEach>
								</tr>
							</c:forEach>
					</tbody>
				</table>
			</div>
			<div>
			
			</div>
			
		</div>
		<div class="weui_tabbar" id="footerInfo" style="height: 100px">
			<div style="width: 100%;margin-top: 10px;text-align: center;">
				<button class="btn btn-default btn-sm" style="width: 22%;border-radius:20px;border: 1px solid #007aff;" type="button">可选</button>
				<button class="btn btn-success btn-sm" style="width: 22%;border-radius:20px" type="button">选中</button>
				<button class="btn btn-danger btn-sm" style="width: 22%;border-radius:20px" type="button">已售</button>
				<button class="btn btn-warning btn-sm" style="width: 22%;border-radius:20px" type="button">不可订</button>
				<hr style="margin: 5px;height:1px;border:none;border-top:1px dashed #0066CC;" />
				<div style="width:100%;font-size: 0.9em;text-align: left;margin-top: 5px;margin-left: 10px" id="bookingRoom">
				</div>
				<hr style="margin: 5px;height:1px;border:none;border-top:1px dashed #0066CC;" />
				<div style="width: 100%;display:inline-block;margin-bottom: 0px">
					<div style="text-align: left;float:left;margin: 7px">
						<font size="4rem">预定金额￥：<span style="color:red" id="totalPrice">0</span></font>
					</div>
				
					<div style="text-align:right;float:right;">
						<button class="btn btn-primary" type="button" id="sbtBooking" style="margin-right: 20px">立即预定</button>
					</div>
				</div>
				<form action="<%=basePath %>protal/orders/bookingPage" method="post" id="bookingForm">
		            <input type="hidden" id="roomIds" name="roomIds"/>
		            <input type="hidden" id="bookingDates" name="bookingDates">
		            <input type="hidden" id="timeRanges" name="timeRanges"/>
		        </form>
			</div>
		</div>
	</div>
  </body>
</html>
