$(function() {
	$('#dg').edatagrid({
		url : 'loaddata',
		saveUrl : 'save',
		updateUrl : 'update',
		destroyUrl : 'delete'
	});
});

function doSearch(value, name) {
	if (name == 'all') {
		$('#tableSearchBox').searchbox('setValue', '');
		$('#dg').edatagrid('load', {});
	} else {
		var param = {};
		param[String(name)] = value;
		$('#dg').edatagrid('load', param);
	}
}

function lockTable(obj) {
	$.post("./lockTable", {}, function(data) {
		if (data=='OK') {
			$(obj).removeAttr('onclick').unbind('click').bind('click', function() {
				unLockTable(obj);
			});
			$(obj).linkbutton({
				text: '解锁此表'
			});
			$.messager.show({
				title : '锁定成功',
				msg : '锁定表之后别人不能编辑',
				timeout : 2500,
				showType : 'slide'
			});
		}
	});
}

function unLockTable(obj) {
	$.post("./unLockTable", {}, function(data) {
		if (data=='OK') {
			$(obj).removeAttr('onclick').unbind('click').bind('click', function() {
				lockTable(obj);
			});
			$(obj).linkbutton({
				text: '锁定此表'
			});
			$.messager.show({
				title : '解锁成功',
				msg : '解锁之后别人可以编辑',
				timeout : 2500,
				showType : 'slide'
			});
		}
	});
}

function exportTxt() {
	window.location.href="./download";
}