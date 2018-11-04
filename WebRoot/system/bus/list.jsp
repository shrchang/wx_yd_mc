<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<form id="pagerForm" rel="pagerForm" method="post" action="<%=basePath %>system/bus/list">
    <input type="hidden" name="pageNum" value="1" />
    <input type="hidden" name="numPerPage" value="${pageInfo.numPerPage}" />
    <input type="hidden" name="searchKey" value="${pageInfo.searchKey}"/>
    <input type="hidden" name="orderField" value="${pageInfo.orderField}" />
    <input type="hidden" name="orderDirection" value="${pageInfo.orderDirection}" />
</form>


<div class="pageHeader">
    <form onsubmit="return navTabSearch(this);" rel="pagerForm" action="<%=basePath %>system/bus/list" method="post">
    <div class="searchBar">
        <table class="searchContent">
            <tr>
                <td>
                    商户名：<input type="text" name="searchKey" value="${pageInfo.searchKey}"/>
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
            <li><a class="add" href="<%=basePath %>system/bus/add.jsp" target="navTab" title="添加商户"><span>添加</span></a></li>
            <li class="line">line</li>
            <li><a class="edit" href="<%=basePath %>system/bus/edit/{targetId}" target="navTab" title="修改商户"><span>修改</span></a></li>
            <li class="line">line</li>
            <li><a class="delete" href="<%=basePath %>system/bus/delete/{targetId}" target="ajaxTodo" title="确定要删除吗?"><span>删除</span></a></li>
            <li class="line">line</li>
        </ul>
    </div>
    <table class="table" width="100%" layoutH="115">
        <thead>
            <tr>
                <th>商户名</th>
                <th>法人</th>
                <th>身份证</th>
                <th>电话</th>
                <th>最大分店数</th>
                <th>最大场地数</th>
                <th>对公账号</th>
                <th>开户行</th>
                <th>省份</th>
                <th>城市</th>
                <th>区/县</th>
                <th>详细地址</th>
                <th orderField="createTime"  <c:if test='${pageInfo.orderField == "createTime" }'> class="${pageInfo.orderDirection}"  </c:if>>入驻时间</th>
                <th orderField="lastUpdateTime"  <c:if test='${pageInfo.orderField == "lastUpdateTime" }'> class="${pageInfo.orderDirection}"  </c:if>>最后变更时间</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="item" items="${list.rows}">
                <tr target="targetId" rel="${item.id }">
                    <td>${item.busName}</td>
                    <td>${item.busPerson}</td>
                    <td>${item.identityCard}</td>
                    <td>${item.phoneNumber}</td>
                    <td>${item.maxShop}</td>
                    <td>${item.maxRoom}</td>
                    <td>${item.publicAccount}</td>
                    <td>${item.bankType}</td>
                    <td>${item.busProvince}</td>
                    <td>${item.busCity}</td>
                    <td>${item.busCounty}</td>
                    <td>${item.busAddress}</td>
                    <td><fmt:formatDate value="${item.createTime }" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                    <td><fmt:formatDate value="${item.lastUpdateTime }" pattern="yyyy-MM-dd HH:mm:ss"/></td>
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
