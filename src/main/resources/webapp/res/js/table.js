$(function() {
	$('#dg').edatagrid({
		url : 'loaddata',
		saveUrl : 'save',
		updateUrl : 'update',
		destroyUrl : 'delete',
		onError: function(index, data){
			if (data.errorMsg) {
				$.messager.alert('失败', data.errorMsg, 'error');
			} else {
				$.messager.alert('失败', "服务器返回了错误的结果，请刷新数据！", 'error');
			}
		}
	});
});
/**
 * 执行搜索
 * 
 * @param value 关键词
 * @param name 搜索条件（为'all'时表示不过滤）
 */
function doSearch(value, name) {
	// @
	var $edg = $('#dg');
	var $searchBox = $('#tableSearchBox');
	var _all = 'all';
	//
	var param = {};
	if (name === _all) {
		$searchBox.searchbox('setValue', '');
	} else {
		param[String(name)] = value;
	}
	$edg.edatagrid('load', param);
}

/**
 * 锁表函数
 * 
 * @param obj DOM按钮
 */
function lockTable(obj) {
	var postUrl = "./lockTable";
	$.post(postUrl, {}, function(data) {
		if (data==='OK') {
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
/**
 * 解表函数
 * 
 * @param obj DOM按钮
 */
function unLockTable(obj) {
	var postUrl = "./unLockTable";
	$.post(postUrl, {}, function(data) {
		if (data==='OK') {
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
/**
 * 导出txt函数
 */
function exportTxt() {
	window.location.href="./download";
}