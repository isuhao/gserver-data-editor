$(function() {
	$('#dg').edatagrid({
		url : 'loaddata',
		saveUrl : 'save',
		updateUrl : 'update',
		destroyUrl : 'delete',
		onError: function(index, data){
			if (data.errorMsg) {
				if (typeof data.errorMsg === "string") {
					$.messager.alert('失败', data.errorMsg, 'error');
				} else if (typeof data.errorMsg === "object") {
					for (var a in data.errorMsg) if (data.errorMsg.hasOwnProperty(a)) {
						function _getWarningTarget(field) {
							var	index = $('#dg').edatagrid('options').editIndex;
							var	editor = $('#dg').datagrid('getEditor', {
									index : index,
									field : field
								});
							var target = null;
							if (editor) {
								target = editor.target;
								if (target.hasClass('datagrid-editable-input')) {
									//target = $(target[0]);
								} else if (target.hasClass('combobox-f combo-f')) {
									target = $($(target).next().children('.combo-text')[0]);
								}
							}
							return target;
						}
						var warningTarget = _getWarningTarget(a);
						var toolMessage = data.errorMsg[a];
						if (warningTarget) {
							warningTarget.validatebox({
			                	prompt: toolMessage,
				                tipOptions: {    // the options to create tooltip
				                    showEvent: 'mouseenter',
				                    hideEvent: 'mouseleave',
				                    showDelay: 0,
				                    hideDelay: 0,
				                    zIndex: '',
				                    onShow: function(){
				                    	var $t = $(this);
				                    	var _initCss = function () {
				                    		$t.tooltip('tip').css({
				                    			color: '#000',
				                    			borderColor: '#CC9933',
				                    			backgroundColor: '#FFFFCC'
				                    		});
				                    		_initCss = undefined;
				                    	}
				                    	if (_initCss) _initCss();
				                    	var validateboxOpts = $t.validatebox('options');
				                    	if (validateboxOpts.prompt) {
				                    		$t.tooltip('update', validateboxOpts.prompt);
				                    	} else {
				                    		if (validateboxOpts.required && !('' + $t.val())) {
				                    			$t.tooltip('update', validateboxOpts.missingMessage);
				                    		} else {
				                    			if (!$t.hasClass('validatebox-invalid')) {
				                    				$t.tooltip('tip').hide();
				                    			} else {
				                    				$t.tooltip('update', validateboxOpts.invalidMessage || validateboxOpts.rules[validateboxOpts.validType].message);
				                    			}
				                    		}
				                    	}
				                    }
				                }
				            });
							warningTarget.addClass('validatebox-invalid');
							var bindFunction = function(){
		                    	delete $(this).validatebox('options').tipOptions.prompt;
		                    	delete $(this).validatebox('options').prompt;
								$(this).removeClass('validatebox-invalid');
								$(this).unbind('click', bindFunction);
							};
							warningTarget.bind('click', bindFunction);
						}
					}
				}
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