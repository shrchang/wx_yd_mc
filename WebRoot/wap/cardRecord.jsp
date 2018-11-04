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

		<title>会员卡变更记录</title>
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
				<div class="buttons-tab">
					<a href="#allRecord" class="button tab-link active">全部</a>
		            <a href="#rechargeRecord" class="button tab-link">充值</a>
		            <a href="#consumeRecord" class="button tab-link">消费</a>
		            <a href="#otherRecord" class="button tab-link">其他</a>
				</div>
				<div class="content-block">
					<div class="tabs">
						<div id="allRecord" class="tab active">
							<div class="content-block" style="margin: -1rem;">
							    <c:forEach var="record" items="${records}" varStatus="status">
									<div class="card" >
								    	<div class="card-header">
								    	   <c:choose>
					                            <c:when test='${record.changeType=="RECHARGE"}'>充值</c:when>
					                            <c:when test='${record.changeType=="CONSUME"}'>消费</c:when>
					                            <c:when test='${record.changeType=="MONTH_GRANT"}'>月结赠送</c:when>
					                            <c:otherwise>其他</c:otherwise>
					                        </c:choose>
								    	</div>
								    	<div class="card-content">
								      		<div class="card-content-inner">
												<c:choose>
	                                                <c:when test='${record.changeType=="RECHARGE"}'><p>充值金额￥：+${record.changeMoney }</p></c:when>
	                                                <c:when test='${record.changeType=="CONSUME"}'><p>消费金额￥：-${record.changeMoney }</p></c:when>
	                                                <c:when test='${record.changeType=="MONTH_GRANT"}'><p>月结赠送￥：+${record.changeMoney }</p></c:when>
	                                                <c:otherwise><p>其他￥：${record.changeMoney }</p></c:otherwise>
	                                            </c:choose>
												<p>剩余金额￥：${record.remainingSum }</p>
											</div>
								    	</div>
								    	<div class="card-footer">
									      	变更时间：<fmt:formatDate value="${record.changeDate }" pattern="yyyy-MM-dd HH:mm:ss"/>
									    </div>
								  	</div>
							    </c:forEach>
		      				</div>
						</div>
						<div id="rechargeRecord" class="tab">
						  <div class="content-block" style="margin: -1rem;">
						      <c:forEach var="record" items="${records}" varStatus="status">
						          <c:if test='${record.changeType=="RECHARGE"}'>
						              <div class="card">
	                                       <div class="card-header">充值</div>
	                                       <div class="card-content">
	                                           <div class="card-content-inner">
	                                               <p>充值金额￥：+${record.changeMoney }</p>
	                                               <p>剩余金额￥：${record.remainingSum }</p>
	                                           </div>
	                                       </div>
	                                       <div class="card-footer">
	                                                                                                                    变更时间：<fmt:formatDate value="${record.changeDate }" pattern="yyyy-MM-dd HH:mm:ss"/>
	                                       </div>
	                                   </div>
						          </c:if>
	                            </c:forEach>
                            </div>
						</div>
						<div id="consumeRecord" class="tab">
							<div class="content-block" style="margin: -1rem;">
								<c:forEach var="record" items="${records}" varStatus="status">
                                  <c:if test='${record.changeType=="CONSUME"}'>
                                      <div class="card">
                                           <div class="card-header">消费</div>
                                           <div class="card-content">
                                               <div class="card-content-inner">
                                                   <p>消费金额￥：-${record.changeMoney }</p>
                                                   <p>剩余金额￥：${record.remainingSum }</p>
                                               </div>
                                           </div>
                                           <div class="card-footer">
                                                                                                                        变更时间：<fmt:formatDate value="${record.changeDate }" pattern="yyyy-MM-dd HH:mm:ss"/>
                                           </div>
                                       </div>
                                  </c:if>
                                </c:forEach>
							</div>
						</div>
						<div id="otherRecord" class="tab">
							<div class="content-block" style="margin: -1rem;">
								<c:forEach var="record" items="${records}" varStatus="status">
                                  <c:if test='${record.changeType=="MONTH_GRANT"}'>
                                      <div class="card">
                                           <div class="card-header">月结赠送</div>
                                           <div class="card-content">
                                               <div class="card-content-inner">
                                                   <p>月结赠送￥：+${record.changeMoney }</p>
                                                   <p>剩余金额￥：${record.remainingSum }</p>
                                               </div>
                                           </div>
                                           <div class="card-footer">
                                                                                                                        变更时间：<fmt:formatDate value="${record.changeDate }" pattern="yyyy-MM-dd HH:mm:ss"/>
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
	</body>
</html>
