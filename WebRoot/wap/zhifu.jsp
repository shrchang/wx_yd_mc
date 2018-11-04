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
<html><head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
  <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no">
  <title>订单支付</title>
  <meta name="description" content="">
  <meta name="keywords" content="">
  <link rel="stylesheet" type="text/css" href="<%=basePath %>wap/assets/css/animate.css">
  <link rel="stylesheet" type="text/css" href="<%=basePath %>wap/assets/css/main.css">
  <script src="<%=basePath %>wap/assets/js/jquery.min.js"></script>
  <script type="text/javascript" src="<%=basePath %>wap/assets/js/jquery-weui.js"></script>
  <link href="<%=basePath %>wap/assets/css/weui.min.css" rel="stylesheet">
  <link href="<%=basePath %>wap/assets/css/jquery-weui.css" rel="stylesheet">
</head>

<body class="animated bounceInRight yd-body" youdao="bind">
    <div class="page">
    <input type="hidden" id="orderId" name="orderId" value="${order.id}">
  <header class="zf-header"><span style="font-weight: 600;font-size: 14px;" id="endTime"></span></header>
  <!-- header-ed -->
  <section class="section6 ">
    <div class="p">
      <p>预定信息<span class="fudong bbb">${roomNames}</span></p>
    </div>
    <div class="p">
      <p>订单编号<span class="fudong bbb">${order.orderNumber}</span></p>
    </div>
    <div class="p">
      <p>订单金额<span class="fudong bbb">￥${order.roomPrice}</span></p>
    </div>
    <div class="p" style="border-bottom: 1px solid #eaeaea;">
      <p>定金<span class="fudong red">￥ ${order.orderMoney}
      </span></p>
    </div>
    <div class="p" style="border-bottom: 1px solid #eaeaea;">
      <p>待付金额<span class="fudong red">￥
        <c:choose>
	        <c:when test='${order.status=="PAID"}'>
	            ${order.roomPrice+order.otherPrice-order.orderMoney}
	        </c:when>
	        <c:when test='${order.status=="UNPAID"}'>
                ${order.roomPrice+order.otherPrice}
            </c:when>
	        <c:otherwise>${order.roomPrice}</c:otherwise>
	      </c:choose>
      </span></p>
    </div>
  </section>
  <section class="section6" style="margin-top: 20px;" id="pay">
    <div class="p zf">
      <p><span class="pay-icon weixin"></span>微信支付<span class="circle current" data-pay="weixin"></span></p>
    </div>
    <!-- 
    <div class="p zf">
      <p><span class="pay-icon zhifubao"></span>支付宝支付<span class="circle" data-pay="alipay"></span></p>
    </div>
     -->
    <div class="p zf">
      <p><span class="pay-icon vip1"></span>会员卡支付<span class="circle" data-pay="vip"></span></p>
    </div>
    <div class="p zf" style="border-bottom: 1px solid #eaeaea;">
      <p><span class="pay-icon vip2"></span>会员卡代付<span class="circle" data-pay="supervip"></span></p>
    </div>
    <div class="p" style="display: none;" id="userCardNumberDiv">
        <p>
           <label for="ydmobile">代付会员卡</label>
           <input type="tel" id="userCardNumber" name="userCardNumber" placeholder="请输入代付会员卡" style="float: right;height: 22px;margin-top: 12px">
       </p>
    </div>
  </section>
  <div><a href="javascript:;" class="queren">确认支付</a></div>
  
  </div>
  <script type="text/javascript">
    function getRTime(){ 
	   var EndTime= new Date(${outTime}); //截止时间 
	   var NowTime = new Date(); 
	   var t =EndTime.getTime() - NowTime.getTime(); 
	   if(t<0){
		  document.getElementById("endTime").innerHTML = "已超时"; 
		  return;
	   }
	   var d=Math.floor(t/1000/60/60/24); 
	   var h=Math.floor(t/1000/60/60%24); 
	   var m=Math.floor(t/1000/60%60); 
	   var s=Math.floor(t/1000%60); 
	   document.getElementById("endTime").innerHTML = "支付剩余时间:"+m+"分"+s+"秒"; 
    } 
    var status = '${order.status}'
    if(status=='UNPAID'){
    	$('#payTitle').show();
	    setInterval(getRTime,1000); 
    }else if(status=='AFFIRM'){
        document.getElementById("endTime").innerHTML = "订单状态:待确认"; 
    }else if(status=='PAID'){
        document.getElementById("endTime").innerHTML = "订单状态:待付全款"; 
    }else if(status=='FINISH'){
        document.getElementById("endTime").innerHTML = "订单状态:已完成"; 
    }else if(status=='CANCELLED'){
        document.getElementById("endTime").innerHTML = "订单状态:已取消"; 
    }
    
    $(".queren").click(function(){
    	var currentPay = $(".zf").siblings().find('.current').eq(0).data("pay");
    	if(currentPay=="vip"){
    		cardPayOrder($('#orderId').val());
    	}
    	if(currentPay=="alipay"){
            window.location.href = '<%=basePath %>protal/orders/'+$('#orderId').val()+'/otherPayPage';
        }
    	if(currentPay=="weixin"){
            weixinPayOrder($('#orderId').val());
        }
    	if(currentPay=="supervip"){
            otherCardPayOrder($('#orderId').val());
        }
    });
    
    function otherCardPayOrder(orderId){
    	var cardNumber = $('#userCardNumber').val();
    	if(cardNumber==''){
    		$.alert("请输入代付会员卡号！");
    		return;
    	}
        $.ajax({
            url:'<%=basePath%>protal/orders/'+orderId+'/otherCardPayOrder?cardNumber='+encodeURI(encodeURI(cardNumber)),
            type:'PUT',
            dataType: 'json',
            success:function(data){
                if(data.state=='OK'){
                    $.alert('代付订单已经申请，请等待该会员确认并付款！',function(){
	                    location.href='<%=basePath%>protal/orders/<%=session.getAttribute("systemCode")%>';
                    });
                } else if(data.state=='NOT_FOUND_CARD'){
                    $.alert('未查询到会员信息，请确认是否已经成为会员。');
                } else if(data.state=='NOT_AVAILABLE_CARD'){
                    $.alert('会员卡等待审核中，请联系客服进行充值，多充多送。');
                } else if(data.state=='NOT_ENOUGH_CARD'){
                    $.alert('会员卡余额不足，请充值！');
                }
            },
            error:function(xhr){
                $.alert('操作失败');
            }
        });
    }
    
    //TODO 改为post方式，参数不带到url中，防止微信支付页面未注册
    function weixinPayOrder(orderId){
    	$.ajax({
            url:'<%=basePath%>protal/orders/'+orderId+'/tenPayOrder',
            type:'GET',
            dataType: 'json',
            success:function(data){
                if(data.errorMsg=='OK'){
                    callpay(data);
                } else{
                    $.alert(data.errorMsg);
                }
            },
            error:function(xhr){
                $.alert('操作失败');
            }
        });
    }
    function callpay(data) {
	    WeixinJSBridge.invoke('getBrandWCPayRequest', {
	        "appId" : data.appId,
	        "timeStamp" : data.timeStamp,
	        "nonceStr" : data.nonceStr,
	        "package" : data.packageStr,
	        "signType" : "MD5",
	        "paySign" : data.sign
	    }, function(res) {
	        WeixinJSBridge.log(res.err_msg);
	        if (res.err_msg == "get_brand_wcpay_request:ok") {
	            $.alert("微信支付成功!",function(){
	            	location.href='<%=basePath%>protal/orders/<%=session.getAttribute("systemCode")%>';
	            });
	        } else if (res.err_msg == "get_brand_wcpay_request:cancel") {
	            $.alert("用户取消支付！");
	        } else {
	            $.alert("支付失败！");
	        }
	    })
	}
    function cardPayOrder(orderId){
        $.ajax({
            url:'<%=basePath%>protal/orders/'+orderId+'/cardPayOrder',
            type:'PUT',
            dataType: 'json',
            success:function(data){
                if(data.state=='OK'){
                    $.alert('支付订单成功！',function(){
	                    location.href='<%=basePath%>protal/orders/<%=session.getAttribute("systemCode")%>';
                    });
                } else if(data.state=='NOT_FOUND_CARD'){
                    $.alert('未查询到会员信息，请确认是否已经成为会员。');
                } else if(data.state=='NOT_AVAILABLE_CARD'){
                    $.alert('会员卡等待审核中，请联系客服进行充值，多充多送。');
                } else if(data.state=='NOT_ENOUGH_CARD'){
                    $.alert('会员卡余额不足，请充值！');
                }
            },
            error:function(xhr){
                $.alert('操作失败');
            }
        });
    }
    
    //选择支付方式
    var pay = $("#pay .zf");
    var checek = $("#pay .zf").find('p');
    var radio = checek.find('.circle');
    var current = $(".zf").siblings().find('.current').length;
    checek.click(function() {
        $(this).find('.circle').addClass('current')
        $(this).parents('.zf').siblings().find('.circle').removeClass('current');
        var type = $(this).find('.circle').data("pay");
        if(type=="supervip"){
	        $("#userCardNumberDiv").show();
        }else{
        	$("#userCardNumberDiv").hide();
        }
    })
	  
  </script>
</body></html>