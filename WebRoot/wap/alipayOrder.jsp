<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
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

		<title>支付宝支付</title>
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
			<!-- 
			<header class="bar bar-nav">
			  <button class="button button-link button-nav pull-left">
			    <span class="icon icon-left"></span>
			   我的订单
			  </button>
			  <h1 class="title">支付宝信息</h1>
			</header>
			 -->
			 		
			<div class="content">
				
				<div class="content-padded">
		        <p>请将定金付款至下面支付宝账号，在下面输入框中填写<font style="font-weight: bold;color: red">付款流水号</font>，等待管理员确认。同时可以前往<font style="font-weight: bold;color: red">我的订单</font>输入流水号信息，<font style="font-weight: bold;color: red" id="outTimeHtml">${timeOut }分钟</font>之后将取消未付款的订单。</p>
		    	</div>
		    	
		    	
				  <div class="list-block">
				    <ul>
				      <li class="item-content">
				        <div class="item-inner">
				          <div class="item-title">支付宝账号</div>
				          <div class="item-after">${alipayAccount }</div>
				        </div>
				      </li>
				      <li class="item-content">
				        <div class="item-inner">
				          <div class="item-title">支付宝名称</div>
				          <div class="item-after">${alipayName }</div>
				        </div>
				      </li>
				    </ul>
				  </div>
		    	
		    	<div class="list-block">
		    		<ul>
		    			<li class="item-content">
		    				<div class="item-inner">
								<div class="item-title label">
									流水号
								</div>
								<div class="item-input">
									<input type="text" name="swiftNumber" id="swiftNumber" placeholder="请输入付款流水号">
								</div>
							</div>
		    			</li>
		    		</ul>
		    	</div>
		    	
		    	<div class="content-block">
			    	<div class="row">
			      		<div class="col-100"><a href="#" onclick="submitSwiftNumber()" class="button button-big button-fill button-success">提交流水号</a></div>
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
		 	function submitSwiftNumber(){
		 		var swiftNumber= $('#swiftNumber').val();
		 		if(swiftNumber==null || swiftNumber == ''){
		 			$.alert('请输入支付流水号','提示信息');
		 			return;
		 		}
		 		$.ajax({
				     type: 'PUT',
				     url: '<%=basePath %>protal/orders/${order.id}/otherPayOrder?swiftNumber='+encodeURI(encodeURI(swiftNumber)),
				     dataType: 'json',
				     success: function(data){
				         if(data.state=='OK'){
	                        $.alert('操作成功，等待管理员确认订单！',"提示信息",function(){
		                        window.location.href='<%=basePath %>protal/orders/${systemCode}';
	                        });
	                    }else{
	                        $.alert('操作失败：'+data.errmsg,"提示信息");
	                    }
				     },
				     error: function(xhr, type){
				         $.alert('操作失败!',"提示信息")
				     }
				});
		 	}
		 </script>
	</body>
</html>
