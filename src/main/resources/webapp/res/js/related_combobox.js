/**
 * 关联combobox的option选项生成
 * 
 * @param openTable
 * @param relatedField
 */
function reloadOptions(openTable, reloadField) {
	var url = '../' + openTable + '/columns';
	var relatedEditor = $('#dg').edatagrid('getEditor', {
		index : $('#dg').edatagrid('getClickIndex'),
		field : reloadField
	});
	relatedEditor.target.combobox('reload', url);
}
