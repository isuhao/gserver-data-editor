<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<link rel="stylesheet" type="text/css" href="../res/easyui/themes/default/easyui.css">
		<link rel="stylesheet" type="text/css" href="../res/easyui/themes/icon.css">
		<link rel="stylesheet" type="text/css" href="../res/css/demo.css">
		<script type="text/javascript" src="../res/easyui/jquery-1.8.0.min.js"></script>
		<script type="text/javascript" src="../res/easyui/jquery.easyui.min.js"></script>
		<script type="text/javascript" src="../res/js/table_popup.js"></script>
	</head>
	<body>
		<table id="pdg" title="" class="easyui-datagrid" style="width:700px;height:308px"
		url="../${tablename}/loaddata" toolbar="#poptoolbar" pagination="true"
		rownumbers="true" fitColumns="true" singleSelect="true">
			<thead>
				<tr>
					<c:forEach var="column" items="${columns}">
						<th field="${column.name}" width="50">${column.comment}</th>
					</c:forEach>
				</tr>
			</thead>
		</table>
		<div id="poptoolbar">
			<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-ok" plain="true" onclick="setVal()">设为此行关联值</a>
			<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-mini-edit" plain="true" onclick="setDefault()">设为缺省值</a>
			<a href="javascript:void(0)" class="easyui-linkbutton" iconCls="icon-help" plain="true" onclick="display('1')">录入统计</a>
		</div>
		<div style="visibility: hidden">
			<input type="hidden" id="pdg_field" value="${relatedfield}"/>
			<input type="hidden" id="dg_default" value="${defaultliteral}"/>
		</div>
	</body>
</html>
