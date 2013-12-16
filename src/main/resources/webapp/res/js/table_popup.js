/**
 * 给目标输入框写一个新值。
 * 
 * @param newVal 新值
 */
function setVal(newVal) {
	getInputJq().val(newVal);
	$.messager.show({
			title : '关联成功',
			msg : '关联成功，录入值：' + newVal,
			timeout : 2500,
			showType : 'slide'
		});
}

/**
 * 将目标输入框的值，置为弹出表某所选某字段的值，若没有选行则不做处理。
 * 
 * @param chooseField 需要选值的字段
 */
function relateRow(chooseField) {
	var newVal = undefined,
		$pdg = $('#pdg'),
		pdg_selectedrow = $pdg.datagrid("getSelected");
	if (pdg_selectedrow !== null) {
		newVal = pdg_selectedrow[chooseField];
		getInputJq().val(newVal);
		$.messager.show({
			title : '关联成功',
			msg : '关联成功，录入值：' + newVal,
			timeout : 2500,
			showType : 'slide'
		});
	}
}

/**
 * 显示输入框内容。
 */
function display() {
	$.messager.alert('录入统计', '录入值：' + getInputJq().val());
}

/**
 *得到与此编辑器相关联的那个input元素，这个元素来自编辑器类型tableDialog的属性中。
 */
function getInputJq() {
	return $.fn.datagrid.defaults.editors.tableDialog.static_targetInput;
}