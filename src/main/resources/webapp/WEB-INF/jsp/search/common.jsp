<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<input id="tableSearchBox" class="easyui-searchbox" data-options="prompt:'输入关键字',menu:'#mm',searcher:doSearch" style="width:300px"></input>
<div id="mm" style="width:120px">
	<div name='all' data-options="selected:true,iconCls:'icon-ok'">
		全部
	</div>
	<c:forEach var="column" items="${columns}">
		<c:if test="${column.search!=''}">
			<div name="${column.search}" data-options="">${column.comment}</div>
		</c:if>
	</c:forEach>
</div>