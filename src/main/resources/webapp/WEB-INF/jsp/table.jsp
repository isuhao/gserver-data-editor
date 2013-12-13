<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link rel="stylesheet" type="text/css" href="../res/easyui/themes/default/easyui.css">
		<link rel="stylesheet" type="text/css" href="../res/easyui/themes/icon.css">
		<link rel="stylesheet" type="text/css" href="../res/css/demo.css">
		<script type="text/javascript" src="../res/easyui/jquery-1.8.0.min.js"></script>
		<script type="text/javascript" src="../res/easyui/jquery.easyui.min.js"></script>
		<script type="text/javascript" src="../res/easyui/jquery.edatagrid.js"></script>
		<script type="text/javascript" src="../res/js/jquery.validsplus.js"></script>
		<script type="text/javascript" src="../res/js/jquery.editorsplus.js"></script>
		<script type="text/javascript" src="../res/js/table.js"></script>
		<script type="text/javascript" src="../res/js/table_popup.js"></script>
		<script type="text/javascript" src="../res/js/arrays.js"></script>
		<script type="text/javascript" src="../res/js/related_combobox.js"></script>
	</head>
	<body>
		<c:choose>
			<c:when test='${tablename=="_tt_constraint"}'>
				<%@ include file="table_content/_tt_constraint.jsp" %>
			</c:when>
			<c:when test='${tablename=="_tt_arrayrule"}'>
				<%@ include file="table_content/_tt_arrayrule.jsp" %>
			</c:when>
			<c:otherwise>
				<%@ include file="table_content/common.jsp" %>
			</c:otherwise>
		</c:choose>

		<div id="toolbar">
			<table class="toobar-format">
				<thead></thead>
				<tbody>
				<tr>
					<td>
						<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="javascript:$('#dg').edatagrid('addRow')">新增行</a>
						<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="javascript:$('#dg').edatagrid('destroyRow')">删除行</a>
						<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-save" plain="true" onclick="javascript:$('#dg').edatagrid('saveRow')">保存</a>
						<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-undo" plain="true" onclick="javascript:$('#dg').edatagrid('cancelRow')">取消</a>
					</td>
					<td class="right-align">
						<%@ include file="search/common.jsp" %>
					</td>
				</tr>
				</tbody>
			</table>
		</div>
		<div>
			<table style="width:1000px">
				<thead></thead>
				<tbody>
					<tr>
						<td class="right-align">
							<c:choose>
								<c:when test="${lockTableIP==pageContext.request.remoteAddr}">
									<a href="#" class="easyui-linkbutton" onclick="unLockTable(this)">解锁此表</a>
								</c:when>
								<c:when test="${lockTableIP!=null && lockTableIP!=pageContext.request.remoteAddr}">
									<a href="#" class="easyui-linkbutton" data-options="disabled:true">此表已被${lockTableIP}锁定</a>
								</c:when>
								<c:otherwise>
									<a href="#" class="easyui-linkbutton" onclick="lockTable(this)">锁定此表</a>
								</c:otherwise>
							</c:choose>
							<c:if test="${!fn:startsWith(tablename, '_tt_')}">
								<a href="#" class="easyui-linkbutton" onclick="exportTxt()">导出</a>
							</c:if>
						</td>
					</tr>
				</tbody>
			</table>
		</div>
		<div class="secret-infomation">
			<div id="popTableWin"></div>
			<div id="arrayDlg"></div>
		</div>
	</body>
</html>