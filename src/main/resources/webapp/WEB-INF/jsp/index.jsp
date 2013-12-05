<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>数据表编辑器</title>
<link rel="stylesheet" type="text/css" href="./res/easyui/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="./res/css/main.css">
<script type="text/javascript" src="./res/easyui/jquery-1.8.0.min.js"></script>
<script type="text/javascript" src="./res/easyui/jquery.easyui.min.js"></script>
<script type="text/javascript" src="./res/js/index.js"></script>
<link rel="stylesheet" type="text/css" href="./res/css/prettify.css">
<script type="text/javascript" src="./res/js/prettify.js"></script>
</head>
<body class="easyui-layout" style="overflow-y: hidden" scroll="no">
	<div region="north" border="false"
		style="background: #666; text-align: center">
		<div id="header-inner">
			<table cellpadding="0" cellspacing="0" style="width: 100%;">
				<tr>
					<td rowspan="2" style="width: 20px;"></td>
					<td style="height: 52px;">
						<div style="color: #fff; font-size: 22px; font-weight: bold;">
							<a href="./"
								style="color: #fff; font-size: 22px; font-weight: bold; text-decoration: none">策划数据表编辑器</a>
						</div>
					</td>
				</tr>
			</table>
		</div>
	</div>

	<div region="west" border="false" split="true" title="&nbsp;"
		style="width: 200px; padding: 5px;">
        <ul id="packageTree" ></ul>
	</div>
	<div region="center" border="false">
		<div id="tt" class="easyui-tabs" fit="true" border="false" plain="true">
			<div id="guideDiv" title="welcome" href="./res/guide.html">
			</div>
		</div>
	</div>
	<div id="treeMenu" class="easyui-menu" style="width:120px;">
        <div id="exportInMenu">导出</div>
    </div>
</body>
</html>


