<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://shiro.apache.org/tags" prefix="shiro"%>
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
	<link href="<%=basePath %>system/assets/themes/default/style.css" rel="stylesheet" type="text/css" media="screen"/>
	<link href="<%=basePath %>system/assets/themes/css/core.css" rel="stylesheet" type="text/css" media="screen"/>
	<link href="<%=basePath %>system/assets/themes/css/print.css" rel="stylesheet" type="text/css" media="print"/>
	<link href="<%=basePath %>system/assets/uploadify/css/uploadify.css" rel="stylesheet" type="text/css" media="screen"/>
	<link href="<%=basePath %>system/assets/zTreeStyle/zTreeStyle.css" rel="stylesheet" type="text/css" media="screen"/>
	<!--[if IE]>
	<link href="<%=basePath %>system/assets/themes/css/ieHack.css" rel="stylesheet" type="text/css" media="screen"/>
	<![endif]-->
	
	<!--[if lt IE 9]><script src="<%=basePath %>system/assets/js/speedup.js" type="text/javascript"></script><script src="<%=basePath %>system/assets/js/jquery-1.11.3.min.js" type="text/javascript"></script><![endif]-->
	<!--[if gte IE 9]><!--><script src="<%=basePath %>system/assets/js/jquery-2.1.4.min.js" type="text/javascript"></script><!--<![endif]-->
	
	<script src="<%=basePath %>system/assets/js/jquery.cookie.js" type="text/javascript"></script>
	<script src="<%=basePath %>system/assets/js/jquery.validate.js" type="text/javascript"></script>
	<script src="<%=basePath %>system/assets/js/jquery.bgiframe.js" type="text/javascript"></script>
	<script src="<%=basePath %>system/assets/xheditor/xheditor-1.2.2.min.js" type="text/javascript"></script>
	<script src="<%=basePath %>system/assets/xheditor/xheditor_lang/zh-cn.js" type="text/javascript"></script>
	<script src="<%=basePath %>system/assets/uploadify/scripts/jquery.uploadify.js" type="text/javascript"></script>
	
	
	<script src="<%=basePath %>system/assets/js/dwz.core.js" type="text/javascript"></script>
	<script src="<%=basePath %>system/assets/js/dwz.util.date.js" type="text/javascript"></script>
	<script src="<%=basePath %>system/assets/js/dwz.validate.method.js" type="text/javascript"></script>
	<script src="<%=basePath %>system/assets/js/dwz.barDrag.js" type="text/javascript"></script>
	<script src="<%=basePath %>system/assets/js/dwz.drag.js" type="text/javascript"></script>
	<script src="<%=basePath %>system/assets/js/dwz.tree.js" type="text/javascript"></script>
	<script src="<%=basePath %>system/assets/js/dwz.accordion.js" type="text/javascript"></script>
	<script src="<%=basePath %>system/assets/js/dwz.ui.js" type="text/javascript"></script>
	<script src="<%=basePath %>system/assets/js/dwz.theme.js" type="text/javascript"></script>
	<script src="<%=basePath %>system/assets/js/dwz.switchEnv.js" type="text/javascript"></script>
	<script src="<%=basePath %>system/assets/js/dwz.alertMsg.js" type="text/javascript"></script>
	<script src="<%=basePath %>system/assets/js/dwz.combox.js" type="text/javascript"></script>
	<script src="<%=basePath %>system/assets/js/dwz.contextmenu.js" type="text/javascript"></script>
	<script src="<%=basePath %>system/assets/js/dwz.navTab.js" type="text/javascript"></script>
	<script src="<%=basePath %>system/assets/js/dwz.tab.js" type="text/javascript"></script>
	<script src="<%=basePath %>system/assets/js/dwz.resize.js" type="text/javascript"></script>
	<script src="<%=basePath %>system/assets/js/dwz.dialog.js" type="text/javascript"></script>
	<script src="<%=basePath %>system/assets/js/dwz.dialogDrag.js" type="text/javascript"></script>
	<script src="<%=basePath %>system/assets/js/dwz.sortDrag.js" type="text/javascript"></script>
	<script src="<%=basePath %>system/assets/js/dwz.cssTable.js" type="text/javascript"></script>
	<script src="<%=basePath %>system/assets/js/dwz.stable.js" type="text/javascript"></script>
	<script src="<%=basePath %>system/assets/js/dwz.taskBar.js" type="text/javascript"></script>
	<script src="<%=basePath %>system/assets/js/dwz.ajax.js" type="text/javascript"></script>
	<script src="<%=basePath %>system/assets/js/dwz.pagination.js" type="text/javascript"></script>
	<script src="<%=basePath %>system/assets/js/dwz.database.js" type="text/javascript"></script>
	<script src="<%=basePath %>system/assets/js/dwz.datepicker.js" type="text/javascript"></script>
	<script src="<%=basePath %>system/assets/js/dwz.effects.js" type="text/javascript"></script>
	<script src="<%=basePath %>system/assets/js/dwz.panel.js" type="text/javascript"></script>
	<script src="<%=basePath %>system/assets/js/dwz.checkbox.js" type="text/javascript"></script>
	<script src="<%=basePath %>system/assets/js/dwz.history.js" type="text/javascript"></script>
	<script src="<%=basePath %>system/assets/js/dwz.print.js" type="text/javascript"></script>
	<script src="<%=basePath %>system/assets/js/dwz.regional.zh.js" type="text/javascript"></script>
	
	<script src="<%=basePath %>system/assets/js/jquery.ztree.all-3.5.min.js" type="text/javascript"></script>
	<script src="<%=basePath %>system/assets/js/PCASClass.js"></script>
	<script type="text/javascript">
	$(function(){
		DWZ.init("<%=basePath %>system/assets/dwz.frag.xml", {
			loginUrl:"<%=basePath %>system/login.jsp", loginTitle:"登录",	// 弹出登录对话框
			loginUrl:"<%=basePath %>system/login.jsp",	// 跳到登录页面
			statusCode:{ok:200, error:300, timeout:301}, //【可选】
			pageInfo:{pageNum:"pageNum", numPerPage:"numPerPage", orderField:"orderField", orderDirection:"orderDirection"}, //【可选】
			keys: {statusCode:"statusCode", message:"message"}, //【可选】
			ui:{hideMode:'offsets'}, //【可选】hideMode:navTab组件切换的隐藏方式，支持的值有’display’，’offsets’负数偏移位置的值，默认值为’display’
			debug:true,	// 调试模式 【true|false】
			callback:function(){
				initEnv();
				$("#themeList").theme({themeBase:"<%=basePath %>system/assets/themes"}); // themeBase 相对于index页面的主题base路径
			}
		});
		
	});
	function logout(){
		alertMsg.confirm("确认退出系统吗？", {
			okCall: function(){
				var url = "<%=basePath %>system/users/logout";
				var link = document.getElementById("logoutLink");  
				link.href = url;
				link.setAttribute("onclick",'');
				link.click("return false");
			}
	});
	}
	</script>
  </head>
  
 
<body scroll="no">
	<div id="layout">
		<div id="header">
			<div class="headerNav">
				<a class="logo" >标志</a>
				<ul class="nav">
					<li><a><shiro:principal /></a></li>
					<li><a href="<%=basePath %>system/user/updatePwd.jsp"" target="dialog">修改密码 </a></li>
					<li><a id="logoutLink" onclick="logout()" href="javascript:void(0)">退出</a></li>
				</ul>
				<ul class="themeList" id="themeList">
					<li theme="default"><div class="selected">蓝色</div></li>
					<li theme="green"><div>绿色</div></li>
					<li theme="purple"><div>紫色</div></li>
					<li theme="silver"><div>银色</div></li>
					<li theme="azure"><div>天蓝</div></li>
				</ul>
			</div>

			<!-- navMenu -->
			
		</div>

		<div id="leftside">
			<div id="sidebar_s">
				<div class="collapse">
					<div class="toggleCollapse"><div></div></div>
				</div>
			</div>
			<div id="sidebar">
				<div class="toggleCollapse"><h2>菜单导航</h2><div>收缩</div></div>

				<div class="accordion" fillSpace="sidebar">
					<div class="accordionHeader">
					</div>
					<div class="accordionContent">
						<ul class="tree treeFolder">
							<li icon="<%=basePath%>system/assets/treeIcon/home.png"><a>商户管理</a>
								<ul>
									<li icon="<%=basePath%>system/assets/treeIcon/order.png"><a href="<%=basePath %>system/orders/list" target="navTab" rel="orderNavTab">订单管理</a></li>
									<shiro:hasRole name="admin">
										<li icon="<%=basePath%>system/assets/treeIcon/bus.png"><a href="<%=basePath %>system/bus/list" target="navTab" rel="busNavTab">商户信息</a></li>
									</shiro:hasRole>
									<shiro:hasAnyRoles name="admin,business">
										<li icon="<%=basePath%>system/assets/treeIcon/card.png"><a href="<%=basePath %>system/cards/list" target="navTab" rel="cardNavTab">会员卡管理</a></li>
										<li icon="<%=basePath%>system/assets/treeIcon/payItem.png"><a href="<%=basePath %>system/payItems/list" target="navTab" rel="payItemNavTab">充值管理</a></li>
									</shiro:hasAnyRoles>
								</ul>
							</li>
							<shiro:hasAnyRoles name="admin,business">
							<li icon="<%=basePath%>system/assets/treeIcon/shop.png"><a>店铺管理</a>
								<ul>
									<li icon="<%=basePath%>system/assets/treeIcon/branch.png"><a href="<%=basePath %>system/shops/list" target="navTab" rel="shopNavTab">分店管理</a></li>
									<li icon="<%=basePath%>system/assets/treeIcon/shopLinks.png"><a href="<%=basePath %>system/shoplinks/list" target="navTab" rel="shoplinksNavTab">分店链接配置</a></li>
									<li icon="<%=basePath%>system/assets/treeIcon/room.png"><a href="<%=basePath %>system/rooms/list" target="navTab" rel="roomNavTab">场地管理</a></li>
									<li icon="<%=basePath%>system/assets/treeIcon/roomConfig.png"><a href="<%=basePath %>system/roomConfigs/list" target="navTab" rel="roomConfigNavTab">场地配置</a></li>
									<li icon="<%=basePath%>system/assets/treeIcon/coupon.png"><a href="<%=basePath %>system/coupons/list" target="navTab" rel="couponNavTab">优惠活动</a></li>
									<li icon="<%=basePath%>system/assets/treeIcon/setmeal.png"><a href="<%=basePath %>system/setmeal/list" target="navTab" rel="setmealNavTab">增值套餐</a></li>
								</ul>
							</li>
							</shiro:hasAnyRoles>		
							<shiro:hasAnyRoles name="admin,business">
							<li icon="<%=basePath%>system/assets/treeIcon/wechat.png"><a>微信配置</a>
								<ul>
								    <shiro:hasRole name="admin">
									   <li icon="<%=basePath%>system/assets/treeIcon/wechatConfig.png"><a href="<%=basePath %>system/wechatConfigs/list" target="navTab" rel="wechatConfigNavTab">微信配置</a></li>
									</shiro:hasRole>
									<li icon="<%=basePath%>system/assets/treeIcon/menu.png"><a href="<%=basePath %>system/menus/list" target="navTab" rel="menuNavTab">菜单管理</a></li>
									<li icon="<%=basePath%>system/assets/treeIcon/faq.png"><a href="<%=basePath %>system/faqs/list" target="navTab" rel="faqNavTab">自动回复</a></li>
								</ul>
							</li>
							</shiro:hasAnyRoles>
							<shiro:hasAnyRoles name="admin,business">
							<li icon="<%=basePath%>system/assets/treeIcon/system.png"><a>系统管理</a>
								<ul>
									<li icon="<%=basePath%>system/assets/treeIcon/sysConfig.png"><a href="<%=basePath %>system/constant/list" target="navTab" rel="sysCfgNavTab">系统配置</a></li>
						            <shiro:hasRole name="admin">
									<li icon="<%=basePath%>system/assets/treeIcon/user.png"><a href="<%=basePath %>system/users/list" target="navTab" rel="userNavTab">用户管理</a></li>
									<li icon="<%=basePath%>system/assets/treeIcon/role.png"><a href="<%=basePath %>system/roles/list" target="navTab" rel="roleNavTab">角色管理</a></li>
									<li icon="<%=basePath%>system/assets/treeIcon/permission.png"><a href="<%=basePath %>system/permissions/list" target="navTab" rel="persiNavTab">权限管理</a></li>
						            </shiro:hasRole>
								</ul>
							</li>
						    </shiro:hasAnyRoles>
						</ul>
					</div>
				</div>
			</div>
		</div>
		<div id="container">
			<div id="navTab" class="tabsPage">
				<div class="tabsPageHeader">
					<div class="tabsPageHeaderContent"><!-- 显示左右控制时添加 class="tabsPageHeaderMargin" -->
						<ul class="navTab-tab">
							<li tabid="main" class="main"><a href="javascript:;"><span><span class="home_icon">首页</span></span></a></li>
						</ul>
					</div>
					<div class="tabsLeft">left</div><!-- 禁用只需要添加一个样式 class="tabsLeft tabsLeftDisabled" -->
					<div class="tabsRight">right</div><!-- 禁用只需要添加一个样式 class="tabsRight tabsRightDisabled" -->
					<div class="tabsMore">more</div>
				</div>
				<ul class="tabsMoreList">
					<li><a href="javascript:;">首页</a></li>
				</ul>
				<div class="navTab-panel tabsPageContent layoutBox">
					<div class="page unitBox">
						<div class="accountInfo">
                            <div class="alertInfo">
                                <p><span>系统时间</span></p>
                                <p id="systemTime"></p>
                            </div>
                            <div class="right" style="text-align: center;">
                               <p style="color:red"> </p>
                            </div>
                            
                            <p><span>欢迎使用在线预订平台！</span></p>
                        </div>
						<div class="pageFormContent" layoutH="80" style="margin-right:230px">
						  <p><span>欢迎使用在线预订平台！</span></p>
						</div>
					</div>
					
				</div>
			</div>
		</div>

	</div>

	<div id="footer">商户管理平台</div>
    <script type="text/javascript">
        var t = null;
	    t = setTimeout(time,1000);//开始执行
	    function time()
	    {
	       clearTimeout(t);//清除定时器
	       dt = new Date();
	       var h=dt.getHours();
	       var m=dt.getMinutes();
	       var s=dt.getSeconds();
	       document.getElementById("systemTime").innerHTML =  dt.getFullYear()+"-"+(dt.getMonth()+1)+"-"+dt.getDate()+" "+h+":"+m+":"+s;
	       t = setTimeout(time,1000); //设定定时器，循环执行             
	    } 
    </script>

</body>
</html>
