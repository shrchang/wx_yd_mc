<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://shiro.apache.org/tags" prefix="shiro" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<form id="pagerForm" rel="pagerForm" method="post" action="<%=basePath %>system/cards/list">
    <input type="hidden" name="pageNum" value="1" />
    <input type="hidden" name="numPerPage" value="${pageInfo.numPerPage}" />
    <input type="hidden" name="busId" value="${pageInfo.busId}"/>
    <input type="hidden" name="searchKey" value="${pageInfo.searchKey}"/>
    <input type="hidden" name="orderField" value="${pageInfo.orderField}" />
    <input type="hidden" name="orderDirection" value="${pageInfo.orderDirection}" />
</form>


<div class="pageHeader">
    <form onsubmit="return navTabSearch(this);" rel="pagerForm" action="<%=basePath %>system/cards/list" method="post">
    <div class="searchBar">
        <table class="searchContent">
            <tr>
                <shiro:hasRole name="admin">
	                <td>所属商户：</td>
	                <td>
	                     <select id="busId" name="busId" class="combox">
			                <option value="-1">请选择商户</option>
			                <c:forEach var="item" items="${bus}">
			                    <option value="${item.id }" ${item.id==pageInfo.busId?"selected":"" }>${item.busName }</option>
			                </c:forEach>
			            </select>
	                </td>
                </shiro:hasRole>
                <td>微信昵称：</td>
                <td>
                     <input type="text" name="searchKey" value="${pageInfo.searchKey}"/>
                </td>
                <td>
                    <div class="button"><div class="buttonContent"><button type="submit">查询</button></div></div>
                </td>
            </tr>
        </table>
    </div>
    </form>
</div>
<div class="pageContent">
    <div class="panelBar">
        <ul class="toolBar">
            <li><a class="add" href="<%=basePath %>system/cards/affire/{targetId}" target="dialog"><span id="affireLink">充值</span></a></li>
            <li class="line">line</li>
            <li><a class="icon" href="<%=basePath %>system/cards/records/{targetId}" target="navTab" ><span>变更记录</span></a></li>
            <li class="line">line</li>
            <shiro:hasRole name="admin">
	            <li><a class="delete" href="<%=basePath %>system/cards/delete/{targetId}" target="ajaxTodo" title="确定要删除吗?"><span>删除</span></a></li>
	            <li class="line">line</li>
            </shiro:hasRole>
        </ul>
    </div>
    <table class="table" onselrow="doseltablerow_menulist" getselcol="6" width="100%" layoutH="115">
        <thead>
            <tr>
                <th>所属商户</th>
                <th>会员卡号</th>
                <th>微信昵称</th>
                <th orderField="recharge" <c:if test='${pageInfo.orderField == "recharge" }'> class="${pageInfo.orderDirection}"  </c:if>>充值金额</th>
                <th orderField="totalAmount" <c:if test='${pageInfo.orderField == "totalAmount" }'> class="${pageInfo.orderDirection}"  </c:if>>总金额</th>
                <th orderField="remainingSum" <c:if test='${pageInfo.orderField == "remainingSum" }'> class="${pageInfo.orderDirection}"  </c:if>>余额</th>
                <th orderField="status" <c:if test='${pageInfo.orderField == "status" }'> class="${pageInfo.orderDirection}"  </c:if>>状态</th>
                <th orderField="createTime" <c:if test='${pageInfo.orderField == "createTime" }'> class="${pageInfo.orderDirection}"  </c:if>>开卡时间</th>
                <th orderField="lastRechargeTime"  <c:if test='${pageInfo.orderField == "lastRechargeTime" }'> class="${pageInfo.orderDirection}"  </c:if>>最后充值时间</th>
                <th orderField="lastPayTime"  <c:if test='${pageInfo.orderField == "lastPayTime" }'> class="${pageInfo.orderDirection}"  </c:if>>最后消费时间</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="item" items="${list.rows}">
                <tr target="targetId" rel="${item.id }">
                    <td>${item.busName}</td>
                    <td>${item.cardNumber }</td>
                    <td>${item.userName}</td>
                    <td>${item.recharge}</td>
                    <td>${item.totalAmount}</td>
                    <td>${item.remainingSum}</td>
                    <td>${item.status=="OK"?"正常":"待审核"}</td>
                    <td><fmt:formatDate value="${item.createTime }" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                    <td><fmt:formatDate value="${item.lastRechargeTime }" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                    <td><fmt:formatDate value="${item.lastPayTime }" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
    <div class="panelBar">
        <div class="pages">
        <!--  -->
            <span>每页显示</span>
            <select class="combox" name="numPerPage" targetType="navTab" onchange="navTabPageBreak({numPerPage:this.value})">
                <option value="10" ${pageInfo.numPerPage==10?"selected":""}>10</option>
                <option value="20" ${pageInfo.numPerPage==20?"selected":""}>20</option>
                <option value="50" ${pageInfo.numPerPage==50?"selected":""}>50</option>
                <option value="100" ${pageInfo.numPerPage==100?"selected":""}>100</option>
            </select>
            <span>条，共${list.total}条记录</span>
        </div>
        
        <div class="pagination" targetType="navTab" totalCount="${list.total}" numPerPage="${pageInfo.numPerPage}" pageNumShown="5" currentPage="${pageInfo.pageNum}"></div>

    </div>
    <script type="text/javascript">
    function doseltablerow_menulist(colText){
    	if(colText=='正常'){
    		$('#affireLink').text("充值");
    	}else{
    		$('#affireLink').text("审核");
    	}
    }
    </script>
</div>
