<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://shiro.apache.org/tags" prefix="shiro" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<form id="pagerForm" rel="pagerForm" method="post" action="<%=basePath %>system/cards/records/${cardId}">
    <input type="hidden" name="pageNum" value="1" />
    <input type="hidden" name="numPerPage" value="${pageInfo.numPerPage}" />
    <input type="hidden" name="searchKey" value="${pageInfo.searchKey}"/>
</form>


<div class="pageHeader">
    <form onsubmit="return navTabSearch(this);" rel="pagerForm" action="<%=basePath %>system/cards/records/${cardId}" method="post">
    <div class="searchBar">
        <table class="searchContent">
            <tr>
                <td>变更类型：</td>
                <td>
                    <select name="searchKey" class="combox">
                        <option value="">请选择变更类型</option>
                        <option value="RECHARGE" ${pageInfo.searchKey=="RECHARGE"?"selected":""}>充值</option>
                        <option value="CONSUME" ${pageInfo.searchKey=="CONSUME"?"selected":""}>消费</option>
                        <option value="MONTH_GRANT" ${pageInfo.searchKey=="MONTH_GRANT"?"selected":""}>月结赠送</option>
                    </select>
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
    <table class="table" width="100%" layoutH="85">
        <thead>
            <tr>
                <th>变更类型</th>
                <th>变更趴点</th>
                <th>变更后剩余趴点</th>
                <th>变更场地</th>
                <th>变更时间</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="item" items="${list.rows}">
                <tr target="targetId" rel="${item.id }">
                    <td>
                        <c:choose>
                            <c:when test='${item.changeType=="RECHARGE"}'>充值</c:when>
                            <c:when test='${item.changeType=="CONSUME"}'>消费</c:when>
                            <c:when test='${item.changeType=="MONTH_GRANT"}'>月结赠送</c:when>
                        </c:choose>
                    </td>
                    <td>${item.changeMoney }</td>
                    <td>${item.remainingSum}</td>
                    <td>${item.roomName}</td>
                    <td><fmt:formatDate value="${item.changeDate }" pattern="yyyy-MM-dd HH:mm:ss"/></td>
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
</div>
