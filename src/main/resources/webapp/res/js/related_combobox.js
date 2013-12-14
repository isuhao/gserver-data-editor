/**
 * 关联combobox的option选项生成
 * 
 * @param openTable 编辑的表名
 * @param reloadField 需要重载入的字段名
 */
function reloadOptions(openTable, reloadField) {
	var url = '../' + openTable + '/columns';
	var $edg = $('#dg');
	var targetEditor = $edg.edatagrid('getEditor', {
		index : $edg.edatagrid('getClickIndex'),
		field : reloadField
	});
	targetEditor.target.combobox('reload', url);
}
