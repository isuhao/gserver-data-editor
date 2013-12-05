function generateRaletedOptions(openTable, relatedField) {
	var relatedEditor = $('#dg').edatagrid('getEditor', {
		index : $('#dg').edatagrid('getClickIndex'),
		field : relatedField
	});
	var url = '../' + openTable + '/columns';
	
	relatedEditor.target.combobox('reload', url);
}
