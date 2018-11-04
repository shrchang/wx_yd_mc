<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
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

		<title>会员卡充值</title>
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
		<div class="page">
			<div class="content">
			    <form action="<%=basePath %>protal/card/${card.id}/submitRecharge" method="post" onsubmit="return submitRecharge(this)">
		    	<div class="content-block-title">会员卡信息</div>
		    	<div class="list-block">
		    		<ul>
		    			<li class="item-content">
		    				<div class="item-inner">
								<div class="item-title label">
									昵称
								</div>
								<div class="item-input">
									<input type="text" id="serName" name="serName" value="${card.userName}"  readonly="readonly"/>
								</div>
							</div>
		    			</li>
		    			<li class="item-content">
		    				<div class="item-inner">
								<div class="item-title label">
									卡号
								</div>
								<div class="item-input">
									<input type="text" id="cardNumber" name="cardNumber" value="${card.cardNumber}" readonly="readonly"/>
								</div>
							</div>
		    			</li>
		    			<li class="item-content">
                            <div class="item-inner">
                                <div class="item-title label">
                                                                                        余额
                                </div>
                                <div class="item-input">
                                    <input type="text" id="remainingSum" name="remainingSum" style="color: red" value="${card.remainingSum}" readonly="readonly"/>
                                </div>
                            </div>
                        </li>
		    		</ul>
		    	</div>
		    	
		    	<div class="content-block-title">充值金额</div>
		    	<div class="list-block media-list">
			    	<ul>
			    	    <c:forEach var="item" items="${rechargeItems}">
			    	        <li>
		                        <label class="label-checkbox item-content">
		                          <input type="radio" name="rechargeItem" value="${item.id}">
		                          <div class="item-media"><i class="icon icon-form-checkbox"></i></div>
		                          <div class="item-inner">
		                            <div class="item-title-row">
		                              <div class="item-title">${item.payInfo}</div>
		                            </div>
		                          </div>
		                        </label>
		                      </li>
			    	    </c:forEach>
					</ul>
		    	</div>
		    	
		    	<div class="content-block">
				      <p><a href="#" class="button button-big button-fill button-success" onclick="submitRecharge()">充值</a></p>
				</div>
			

                </form>
			</div>
		</div>
		<script type='text/javascript' src='<%=basePath%>wap/assets/js/zepto.min.js' charset='utf-8'></script>
		<script type='text/javascript' src='//g.alicdn.com/msui/sm/0.5.8/js/sm.min.js' charset='utf-8'></script>
		<!-- 
		<script type='text/javascript' src='<%=basePath%>wap/assets/js/light7.min.js' charset='utf-8'> </script>
		<script type='text/javascript' src='<%=basePath%>wap/assets/js/light7-extend.min.js' charset='utf-8'></script>
		 -->
		 <script type="text/javascript">
		 function submitRecharge(){
			 var payItem = $('input[name="rechargeItem"]:checked').val();//from.rechargeItem.value;
			 if(payItem=="" || typeof(payItem)=="undefined"){
				 $.alert("请选择充值金额","温馨提示");
				 return false;
			 }
			 $.ajax({
			     type: 'POST',
			     url: '<%=basePath %>protal/card/${card.id}/submitRecharge',
			     data: {rechargeItem:payItem},
			     dataType: 'json',
			     success: function(payData){
			         if(payData.errorMsg=='OK'){
                        callpay(payData.appId,payData.timeStamp,payData.nonceStr,payData.packageStr,payData.sign);
                    }else{
                        $.alert('充值失败：'+payData.errorMsg,"提示信息");
                    }
			     },
			     error: function(xhr, type){
			         $.alert('充值失败!',"提示信息")
			     }
			});
		 }
		 
		 function callpay(appId,timeStamp,nonceStr,packageValue,paySign) {
                WeixinJSBridge.invoke('getBrandWCPayRequest', {
                    "appId" : appId,
                    "timeStamp" : timeStamp,
                    "nonceStr" : nonceStr,
                    "package" : packageValue,
                    "signType" : "MD5",
                    "paySign" : paySign
                }, function(res) {
                    WeixinJSBridge.log(res.err_msg);
                    if (res.err_msg == "get_brand_wcpay_request:ok") {
                        $.alert("会员卡充值成功!","提示信息");
                    } else if (res.err_msg == "get_brand_wcpay_request:cancel") {
                        $.alert("用户取消支付！","提示信息");
                    } else {
                        $.alert("支付失败！","提示信息");
                    }
                })
            }
		 </script>
	</body>
</html>
