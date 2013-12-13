/**
 * arrayeditor
 * 
 * 数组编辑器
 * 
 * Dependencies:
 *   datagrid
 *   messager
 *   tableWindow
 */
(function($) {
	$.fn.arrayeditor = function(options, param) {
		if ( typeof options === 'string') {
			var method = $.fn.arrayeditor.methods[options];
			if (method) {
				return method(this, param);
			}
		}
		options = options || {};
		return this.each(function() {
			var state = $.data(this, 'arrayeditor');
			if (state) {
				$.extend(state.options, options);
			} else {
				$.data(this, 'arrayeditor', {
					options : $.extend({}, $.fn.arrayeditor.defaults, options)
				});
			}
			buildEditor(this);
		});
	};

	$.fn.arrayeditor.methods = {
		options : function(jq) {
			var opts = $.data(jq[0], 'arrayeditor').options;
			return opts;
		},
	};

	function buildEditor(target) {
		var opts = $.data(target, 'arrayeditor').options;
		opts.earliestValue = opts.inputJq.val();
		var arrayRule = opts.arrayRule;
		for (var keyField in arrayRule) {
			for (var keyPossibleValue in arrayRule[keyField]) {
				var titlesArray = arrayRule[keyField][keyPossibleValue];
				for (var i = 0; i < titlesArray.length; ++i) {
					var titleRule = titlesArray[i];
					if (titleRule && titleRule.constraint_id && titleRule.constraint_id > 0) {
						$.ajax({
							url : opts.constraintUrl,
							type : 'post',
							async : false,
							data : {
								id : titleRule.constraint_id
							},
							dataType : 'json',
							success : function(data) {
								if (data.length > 0) {
									var cons = data[0];
									titleRule["toTableName"] = cons.bname;
									titleRule["toPos"] = cons.bpos;
								}
							}
						});
					}
				}
			}
		}

		var $dialogDiv = $('#' + opts.divId).empty().append($('<table id="' + opts.dialogTableId + '"></table>'));

		(function createTable(oldValue) {
			var colGroups = function() {
				var colGroups = [];
				var cols = [];
				var arrayRule = opts.arrayRule;
				$.each(arrayRule, function(field, i) {
					var keyValue = function(field) {
						var clickIndex = opts.parentEditIndex;
						var $parentDg = opts.parentDg;
						var value = '';
						var editor;
						try {
							editor = $parentDg.datagrid('getEditor', {
								index : clickIndex,
								field : field
							});
						} catch (e) {// TypeError
							editor = undefined;
						}
						if (!editor) {
							var row = $parentDg.datagrid('getRows')[clickIndex];
							value = row[field];
						} else {
							value = editor.actions.getValue(editor.target);
						}
						if (value === undefined)
							value = '';
						return value;
					}(field);
					if (arrayRule[field] !== undefined) {
						var colInfo = arrayRule[field][String(keyValue)];
						if (colInfo) {
							for (var i = 0; i < colInfo.length; ++i) {
								var editor;
								if (colInfo[i].toTableName) {
									editor = {
										type : 'tableDialog',
										options : {
											divId : 'popTableWin',
											table : colInfo[i].toTableName,
											field : colInfo[i].toPos,
											openTable : colInfo[i].toTableName,
											openTableField : colInfo[i].toPos
										}
									};
								} else {
									editor = {
										type : 'text',
									};
								}
								var oneCol = {
									field : 'idx_' + colInfo[i]["idx"],
									title : colInfo[i]["desc"],
									width : 80,
									editor : editor
								};
								cols.push(oneCol);
							}
						}
					}
				});
				if (cols.length === 0) {// 说明前面的控制字段填写错误，可以出个警告
					$.messager.show({
						title : '未找到数组列标题',
						msg : "无法生成数组元素标题，未配置此数组，或者您输入了错误的控制字段值！",
						timeout : 5000,
						showType : 'slide'
					});
					var oneCol = {
						field : 'idx_1',
						title : '未找到配置',
						width : 80,
						editor : {
							type : 'text',
						}
					};
					cols.push(oneCol);
				}
				colGroups.push(cols);
				return colGroups;
			}();
			var data = function(oldValue, arrayRule) {
				var datArray = [];
				if (oldValue !== '') {
					var titleLength = colGroups[0].length;
					// 不可能为0，前面至少放了一列
					var splitarray = oldValue.split(',');
					if (splitarray.length % titleLength) {
						$.messager.show({
							title : '原输入和数组列数不匹配',
							msg : "原输入数组长度，不是列的整数倍，表格后面补了" + (titleLength - splitarray.length % titleLength) + "个空单元格！",
							timeout : 5000,
							showType : 'slide'
						});
					}
					var datium;
					for (var i = 0; i < splitarray.length; ++i) {
						if (i % titleLength === 0) {
							datium = {};
							datArray.push(datium);
						}
						datium["idx_" + ((i % titleLength) + 1)] = splitarray[i];
					}
				}
				return datArray;
			}(oldValue, arrayRule);

			var $dialogTable = $('#' + opts.dialogTableId);
			$dialogTable.datagrid({
				columns : colGroups,
				singleSelect : true,
				data : data,
				rownumbers : true,
				fitColumns : false,

				onClickCell : function(index, field, value) {
					var lastIndex = $(this).datagrid('getRowIndex', $(this).datagrid('getSelected'));
					if (lastIndex !== undefined && lastIndex >= 0) {
						$(this).datagrid('endEdit', lastIndex);
					}
					$(this).datagrid('selectRow', index).datagrid('beginEdit', index);
					var ed = $(this).datagrid('getEditor', {
						index : index,
						field : field
					});
					$(ed.target).focus();
				},
				toolbar : [{
					text : '增加行',
					iconCls : 'icon-add',
					handler : function() {
						var lastIndex = $dialogTable.datagrid('getRowIndex', $dialogTable.datagrid('getSelected'));
						if (lastIndex !== undefined && lastIndex >= 0) {
							$dialogTable.datagrid('endEdit', lastIndex);
						}
						var editIndex = $dialogTable.datagrid('getRows').length;
						$dialogTable.datagrid('insertRow', {
							index : editIndex,
							row : {}
						});
						$dialogTable.datagrid('selectRow', editIndex).datagrid('beginEdit', editIndex);
						var editors = $dialogTable.datagrid('getEditors', editIndex);
						if (editors.length) {
							editors[0].target.focus();
						}
					}
				}, {
					text : '删除行',
					iconCls : 'icon-remove',
					handler : function() {
						var editIndex = $dialogTable.datagrid('getRowIndex', $dialogTable.datagrid('getSelected'));
						if (editIndex === -1) {
							return;
						}
						$dialogTable.datagrid('cancelEdit', editIndex).datagrid('deleteRow', editIndex);
					}
				}, {
					text : '还原',
					iconCls : 'icon-undo',
					handler : function() {
						// 还原表格内容的同时，还要讲父层那个输入框的内容还原
						$dialogTable.datagrid('rejectChanges');
						opts.inputJq.val(opts.earliestValue);
					}
				}, '-', {
					text : '预览结果',
					iconCls : 'icon-help',
					handler : function() {
						var finalStrValue = _generateValue();
						$.messager.alert('预览结果', finalStrValue);
					}
				}, {
					text : '确定',
					iconCls : 'icon-ok',
					handler : function() {
						var finalStrValue = _generateValue();
						opts.inputJq.val(finalStrValue);
					}
				}]
			});

			/**
			 *根据当前的表格内容转成字符串
			 */
			function _generateValue() {
				var editIndex = $dialogTable.datagrid('getRowIndex', $dialogTable.datagrid('getSelected'));
				if (Number(editIndex) > -1) {
					$dialogTable.datagrid('endEdit', editIndex);
				}
				var rows = $dialogTable.datagrid('getRows');
				var strRows = $.map(rows, function(row) {
					var arr = [];
					for (var idx in row) {
						arr.push(row[idx]);
					}
					return arr.join(',');
				});
				var finalStr = strRows.join(',');
				return finalStr;
			}

		}(opts.inputJq.val()));

		$dialogDiv.dialog({
			title : '数组编辑器',
			width : 650,
			height : 200,
			padding : 10,
			closed : false,
			cache : false,
			resizable : true,
			modal : true
		});
	}


	$.fn.arrayeditor.defaults = $.extend({}, {
		/**
		 *请求约束关系表的url 
		 */
		constraintUrl : '../_tt_constraint/find',
		/**
		 * 转为弹出层元素的选择器
		 */
		popDialogJq : undefined
	});

})(jQuery);
