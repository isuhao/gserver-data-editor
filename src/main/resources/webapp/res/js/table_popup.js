/**
 *将编辑器输入框设为默认值
 */
function setDefault() {
	var defaultVal = getDefaultVal();
	assign(defaultVal);
	$.messager.show({
			title : '关联成功',
			msg : '关联为缺省值成功。已关联：1行（录入值：' + defaultVal + '），',
			timeout : 2500,
			showType : 'slide'
		});
}

/**
 *将选定值追加到编辑器输入框
 */
function appendVal(maxLength) {
	var newVal = getSelectedVal();
	var oldValue = getInputJq().val();

	if (oldValue && oldValue != getDefaultVal()) {
		var valueArray = oldValue.split(',');
		if (maxLength > 0 && valueArray.length >= maxLength) {
			$.messager.alert('关联失败', '元素上限：' + maxLength + '，已关联：' + valueArray.length + '行（录入值：' + oldValue + '）。', 'error');
			return;
		} else {
			valueArray.push(newVal);
			assign(valueArray.join(','));
			$.messager.show({
				title : '关联成功',
				msg : '增加关联成功。已关联：' + valueArray.length + '行（录入值：' + getInputJq().val() + '），还可关联' + (maxLength == 0 ? '无限' : '' + (maxLength - valueArray.length)) + '行',
				timeout : 2500,
				showType : 'slide'
			});
		}
	} else {
		assign(newVal);
		$.messager.show({
			title : '关联成功',
			msg : '增加关联成功。已关联：1行（录入值：' + newVal + '），还可关联' + (maxLength == 0 ? '无限' : maxLength - 1) + '行',
			timeout : 2500,
			showType : 'slide'
		});
	}

}

/**
 * 显示输入框内容 
 */
function display(maxLength) {
	var relatedLength = 0;
	var val = getInputJq().val();
	var arr = val.split(',');
	$.messager.alert('录入统计', '录入值：' + val + ' (已关联' + arr.length + '行，还可关联' + (maxLength == 0 ? '无限' : '' + (maxLength - arr.length)) + '行）');
}

/**
 *将选定值赋给编辑器输入框
 */
function setVal() {
	var newVal = getSelectedVal();
	assign(newVal);
	$.messager.show({
			title : '关联成功',
			msg : '设置关联成功。已关联：1行（录入值：' + newVal + '），',
			timeout : 2500,
			showType : 'slide'
		});
}

/**
 *得到选定行的关联列的值
 */
function getSelectedVal() {
	var $pdg = $('#pdg');
	var pdg_selectedrow = $pdg.datagrid("getSelected");
	var pdg_field = $('#pdg_field').val();
	var pdg_target = pdg_selectedrow[pdg_field];
	return pdg_target;
}

/**
 *获得默认值
 */
function getDefaultVal() {
	return $('#dg_default').val();
}

/**
 *给关联的input元素写值
 */
function assign(newValue) {
	getInputJq().val(newValue);
}

/**
 *得到与此编辑器相关联的那个input元素
 */
function getInputJq() {
	var inputJqId = '#' + $.fn.datagrid.defaults.editors.tableDialog.static_targetInputId;
	return $(inputJqId);
}
