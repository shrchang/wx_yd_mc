<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>商户管理平台</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	<style type="text/css">
	
		
	html, body, div, span, applet, object, iframe,
	h1, h2, h3, h4, h5, h6, p, blockquote, pre,
	a, abbr, acronym, address, big, cite, code,
	del, dfn, em, font, img, ins, kbd, q, s, samp,
	small, strike, strong, sub, sup, tt, var,
	dl, dt, dd, ol, ul, li,
	fieldset, form, label, legend,
	table, caption, tbody, tfoot, thead, tr, th, td { padding:0; margin:0; font-size:12px; line-height:100%; font-family:Arial, sans-serif;}
	
	ul, ol { list-style:none;}
	img { border:0;}
	
	body { margin:0; text-align:center; background:#FFF url(<%=basePath %>system/assets/themes/default/images/login_bg.png) repeat-x top;}
	
	#login { display:block; width:950px; margin:0 auto; text-align:left;}
	#login_header { display:block; padding-top:40px; height:80px;}
	.login_logo { float:left; margin-top:10px;}
	.login_info { float:left; margin-left:10px; line-height:80px; font-size:18px; color:#0088cc; font-weight: bold;}
	.login_headerContent { float:right; display:block; width:280px; height:80px; padding:0 40px; background:url(<%=basePath %>system/assets/themes/default/images/login_header_bg.png) no-repeat top right;}
	.navList { display:block; overflow:hidden; height:20px; padding-left:28px;}
	.navList ul { float:left; display:block; overflow:hidden;}
	.navList li { float:left; display:block; margin-left:-1px; padding:0 10px; background:url(<%=basePath %>system/assets/themes/default/images/login_list.png) no-repeat 0 5px;}
	.navList a { display:block; white-space:nowrap; line-height:21px; color:#000; text-decoration:none;}
	.navList a:hover { text-decoration:underline;}
	
	#login_content { display:block; position:relative;}
	.login_title { display:block; padding:25px 0 0 38px;}
	.loginForm { display:block; width:255px; padding:70px 20px 0 20px; position:absolute; right:40px;}
	.loginForm p { margin:10px 0;}
	.loginForm label { float:left; width:70px; padding:0 0 0 10px; line-height:25px; color:#4c4c4c; font-size:14px;}
	.loginForm input { padding:3px 2px; border-style:solid; border-width:1px; border-color:#80a5c4;}
	.loginForm .login_input {}
	.loginForm .code { float:left; margin-right:5px;}
	.login_bar { padding-left:80px;padding-top: 10px}
	.login_bar .sub { display:block; width:75px; height:30px; border:none; background:url(<%=basePath %>system/assets/themes/default/images/login_sub.png) no-repeat; cursor:pointer;}
	
	.login_banner { display:block; height:270px;}
	.login_main { display:block; height:200px; padding-right:40px; background:url(<%=basePath %>system/assets/themes/default/images/login_content_bg.png) no-repeat top;}
	
	.helpList { float:right; width:200px;}
	.helpList li { display:block; padding-left:10px; background:url(<%=basePath %>system/assets/themes/default/images/login_list.png) no-repeat 0 -40px;}
	.helpList a { line-height:21px; color:#333; text-decoration:none; }
	.helpList a:hover { text-decoration:underline;}
	
	.login_inner { display:block; width:560px; padding:30px 20px 0 20px;}
	.login_inner p { margin:10px 0; line-height:150%; font-size:14px; color:#666;}
	
	#login_footer { clear:both; display:block; margin-bottom:20px; padding:10px; border-top:solid 1px #e2e5e8; color:#666; text-align:center;}
	
	</style>
  </head>
  
  <body>
  	<div id="login">
		<div id="login_header">
			<h1 class="login_logo">
				<a><img src="<%=basePath %>system/assets/themes/default/images/login_logo.gif" /></a>
			</h1>
			<div class="login_headerContent">
				<h2 class="login_title"><img src="<%=basePath %>system/assets/themes/default/images/login_title.png" /></h2>
			</div>
		</div>
		<div id="login_content">
			<div class="loginForm">
				<form action="<%=basePath %>system/users/login" method="post">
					<p>
						<label>用户名：</label>
						<input type="text" name="userId" size="20" class="login_input" />
					</p>
					<p>
						<label>密码：</label>
						<input type="password" name="userPassword" size="20" class="login_input" />
					</p>
					<div class="login_bar">
						<input class="sub" type="submit" value=" " />
					</div>
					<div style="margin-top: 10px;text-align: center;color: red">${error }</div>
				</form>
			</div>
			<div class="login_banner"><img src="<%=basePath %>system/assets/themes/default/images/login_banner.jpg" /></div>
			<div class="login_main">
				<ul class="helpList">
					<li><a >忘记密码？</a></li>
				</ul>
				<div class="login_inner">
					<p>快速接单、订单管理，会员管理；</p>
					<p>制定资金的转换规则；</p>
					<p>您还可以快速发布推广活动信息。</p>
					<p>......</p>
				</div>
			</div>
		</div>
		<div id="login_footer">
			商户信息管理平台
		</div>
	</div>
  </body>
</html>
