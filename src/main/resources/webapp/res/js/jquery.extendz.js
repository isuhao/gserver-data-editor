/**
 * validatebox 扩展验证规则
 * 
 * Dependencies:
 * validatebox
 */
(function($) {
	$.extend($.fn.validatebox.defaults.rules, {
		/**
		 * Java byte validation
		 */
		J_Byte : {
			validator : function(value, param) {
				var rawStr = String(value);
				if (rawStr.indexOf('.') === -1) {
					var parseNumber = Number(value);
					if (parseNumber === parseNumber && parseNumber >= -128 && parseNumber <= 127)
						return true;
				}
				return false;
			},
			message : "Please input a byte."
		},
		/**
		 * Java short validation
		 */
		J_Short : {
			validator : function(value, param) {
				var rawStr = String(value);
				if (rawStr.indexOf('.') === -1) {
					var parseNumber = Number(value);
					if (parseNumber === parseNumber && parseNumber >= -32768 && parseNumber <= 32767) {
						return true;
					}
				}
				return false;
			},
			message : "Please input a short."
		},
		/**
		 * Java int validation
		 */
		J_Integer : {
			validator : function(value, param) {
				var rawStr = String(value);
				if (rawStr.indexOf('.') === -1) {
					var parseNumber = Number(value);
					if (parseNumber === parseNumber && parseNumber >= -2147483648 && parseNumber <= 2147483647) {
						return true;
					}
				}
				return false;
			},
			message : "Please input an int."
		},
		/**
		 * Java long validation
		 */
		J_Long : {
			validator : function(value, param) {
				var rawStr = String(value);
				if (rawStr.indexOf('.') === -1) {
					var parseNumber = Number(value);
					if (parseNumber === parseNumber && parseNumber >= -9223372036854774808 && parseNumber <= 9223372036854774807) {
						return true;
					}
				}
				return false;
			},
			message : "Please input a long."
		},
		/**
		 * Java float validation
		 */
		J_Float : {
			validator : function(value, param) {
				var parseNumber = Number(value);
				if (parseNumber === parseNumber && parseNumber >= -3.40292347E+38 && parseNumber <= 3.40292347E+38) {
					return true;
				}
				return false;
			},
			message : "Please input a float."
		},
		/**
		 * Java double validation
		 */
		J_Double : {
			validator : function(value, param) {
				var parseNumber = Number(value);
				if (parseNumber === parseNumber) {
					return true;
				}
				return false;
			},
			message : "Please input a double."
		}
	});
})(jQuery);

/**
 * arrays
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

	function buildEditor(target) {
		var opts = $.data(target, 'arrayeditor').options;
		opts.earliestValue = opts.inputTarget.val();
		var arrayRule = opts.arrayRule;
		for (var keyField in arrayRule) if (arrayRule.hasOwnProperty(keyField)) {
			var oneArrayRule = arrayRule[keyField];
			for (var keyPossibleValue in oneArrayRule) if (oneArrayRule.hasOwnProperty(keyPossibleValue)) {
				var titlesArray = arrayRule[keyField][keyPossibleValue];
				for (var i = titlesArray.length; i--; ) {
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

		var $dialogTable = $('<table>');
		var $dialogDiv = $(target).empty().append($dialogTable);

		(function createTable(oldValue) {
			var colGroups = function() {
				var colGroups = [];
				var cols = [];
				var arrayRule = opts.arrayRule;
				$.each(arrayRule, function(field, i) {
					var keyValue = function(field) {
						var $parentDg = opts.parentDg;
						var	parentEditIndex = $parentDg.edatagrid('options').editIndex;
						var	editor = $parentDg.datagrid('getEditor', {
								index : parentEditIndex,
								field : field
							});
						if (!editor) { // 这种情况因为：控制列是不可编辑的例如NoEditor，或者控制列的editor尚未初始化，从原始数据中取值。
							var row = $parentDg.datagrid('getRows')[parentEditIndex];
							return row[field];
						} else { // 如果能取到editor，必须从editor取用户输入的新值
							return editor.actions.getValue(editor.target);
						}
					}(field);
					if (arrayRule[field] !== undefined) {
						var colInfo = arrayRule[field][String(keyValue)];
						if (colInfo) {
							for (var i = colInfo.length; i--; ) {
								var editor;
								if (colInfo[i].toTableName) {
									editor = {
										type : 'tableDialog',
										options : {
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
								cols.unshift(oneCol);
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
			var data = (function(oldValue, arrayRule) {
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
							// init new row
							datium = {};
							for (var j = titleLength; j--;) {
								datium["idx_" + (j + 1)] = '';
							}
							datArray.push(datium);
						}
						datium["idx_" + ((i % titleLength) + 1)] = splitarray[i];
					}
				}
				return datArray;
			})(oldValue, arrayRule);
			$dialogTable.datagrid({
				columns : colGroups,
				singleSelect : true,
				data : data,
				rownumbers : true,
				fitColumns : false,
				fit : true,
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
						opts.inputTarget.val(opts.earliestValue);
						$.messager.show({
							title : '设置成功',
							msg : '数组值还原成功，录入值：' + opts.earliestValue,
							timeout : 2500,
							showType : 'slide'
						});
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
						opts.inputTarget.val(finalStrValue);
						$.messager.show({
							title : '设置成功',
							msg : '数组值设置成功，录入值：' + finalStrValue,
							timeout : 2500,
							showType : 'slide'
						});
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
					var arrKey = [],
						arrVal = [];
					for (var idx in row) if (row.hasOwnProperty(idx)) {
						arrKey.push(idx);
					}
					arrKey.sort();
					for (var i = arrKey.length; i--; ) {
						arrVal.unshift(row[arrKey[i]]);
					}
					return arrVal.join(',');
				});
				var finalStr = strRows.join(',');
				return finalStr;
			}

		}(opts.inputTarget.val()));

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
		constraintUrl : '../_tt_constraint/find'
	});

})(jQuery);

/**
 * edatagrid editor扩展
 * 
 * Dependencies:
 * edatagrid
 * combobox
 * validType
 */
(function($) {
	$.extend($.fn.datagrid.defaults.editors, {
		/**
		 * validateCombobox
		 * combobox结合对表字段进行java类型验证的edatagrid编辑器类型，数据内容必填（required）。
		 * 
		 * 当edatagrid的某个field绑定editor为validateCombobox，会向url地址发起请求，得到形如： 
		 * 	{options: ['data_1', 'data_2', 'data_3'], dataType: 'String'}
		 * 的数据。options为combobox的data选项数据，dataType为做验证用的java数据类型。
		 * 
		 * options参数：
		 * 	table：当前编辑的表名，
		 * 	field：对应的字段名，
		 * 	url：数据选项请求地址。
		 * 
		 * 依赖： validsplus
		 * 
		 * 用法：
		 * <c:if test='${column.editorType=="Key"}'>
		 * <th field="${column.name}" width="50" editor="{type:'validateCombobox', options:{table:'${tablename}', field:'${column.name}', url:'../validateCombobox'}}">${column.comment}</th>
		 * </c:if>
		 * 
		 */
		validateCombobox: {
			init: function(container, options){
				var combodata;
				var validType = undefined;
				var required = undefined;
				$.ajax({
					url : options.url,
					type : 'post',
					async : false,
					data : {
						tableName : options.table,
						field : options.field
					},
					dataType : 'json',
					success : function(data) {
						combodata = data.options;
						switch (data.dataType) {
						case "byte":
						case "Byte":
							validType = "J_Byte";
							required = "required";
							break;
						case "short":
						case "Short":
							validType = "J_Short";
							required = "required";
							break;
						case "int":
						case "Integer":
							validType = "J_Integer";
							required = "required";
							break;
						case "long":
						case "Long":
							validType = "J_Long";
							required = "required";
							break;
						case "float":
						case "Float":
							validType = "J_Float";
							required = "required";
							break;
						case "double":
						case "Double":
							validType = "J_Double";
							required = "required";
							break;
						default:
							validType = undefined;
							break;
						}
					}
				});
				var $combobox = $('<input>');
				if (validType) {
					$combobox.attr('validType', validType);
				}
				if (required) {
					$combobox.attr('required', required);
				}
	        	$combobox.appendTo(container);
	            return $combobox.combobox({
						data : combodata,
						valueField : "0",
						textField : "0",
						formatter: function(row){
	            			return row;
	            		}
					});
	        },
	        destroy: function(target){
	            $(target).combobox('destroy');
	        },
	        getValue: function(target){
	            return $(target).combobox('getValue');
	        },
	        setValue: function(target, value){
	        	$(target).combobox('setValue', value);
	        },
	        resize: function(target, width){
	        	$(target).combobox('resize',width);
	        }
	    },
	    /**
	     * tableDialog
	     * 弹出表关联界面编辑器，每个页面单例。
	     * 
	     * options参数：
	     * 	table：当前编辑的表名，
		 * 	field：对应的字段名，
		 * 	openTable：弹出表的表名，
		 * 	openTableField：弹出表的关联字段名。
		 * 
		 * 依赖：
		 * table_popup.jsp
	     */
		tableDialog : {
			init : function(container, options) {
				var field = options.field;
				// 先放一个text，点击这个text的时候，触发事件，生成弹出框。弹出层是模态窗口，保证关窗口前不会再次触发text的click事件。
				var input = $('<input type="text" class="datagrid-editable-input" readonly="true">').click(function() {
					//只能有一个$window
					var $window = $.fn.datagrid.defaults.editors.tableDialog.static_window;
					if ($window) {
						$window.remove();
					}
					//同页面，tableDialog只能弹出一个，公用原型中的static_targetInput
					$.fn.datagrid.defaults.editors.tableDialog.static_targetInput = $(this);
					$window = $('<div>').appendTo($(this).parent()).window({
						width : 718,
						height : 344,
						minimizable : false,
						maximizable : false,
						collapsible : false,
						modal : true,
						title : field,
						href : '../' + options.openTable + '/popopen?relatedfield=' + options.openTableField + '&table=' + options.table + '&field=' + field // 弹出表请求url
					});
					$.fn.datagrid.defaults.editors.tableDialog.static_window = $window;
				}).appendTo(container);
				return input;
			},
			destroy : function(target) {
				$(target).remove();
			},
			getValue : function(target) {
				return $(target).val();
			},
			setValue : function(target, value) {
				$(target).val(value);
			},
			resize : function(target, width) {
				$(target)._outerWidth(width);
			}
		},
		 /**
	     * arrayDialog
	     * 数组编辑器，每个页面单例。
	     * 
	     * options参数：
	     * 	table：当前编辑的表名，
		 * 	field：对应的字段名，
		 * 	containerDg：父层datagrid的selector，
		 * 	arrayRule：数组控制规则，形如，
		 * 		{"type":{
		 * 			"1":[
		 * 				{"code":1,"constraint_id":1,"desc":"1st","idx":1,"keyField":"type","keyValue":"1","tableName":"talent","targetField":"value"},
		 * 				{"code":2,"constraint_id":-1,"desc":"2nd","idx":2,"keyField":"type","keyValue":"1","tableName":"talent","targetField":"value"}
		 * 			]
		 * 		}}
		 * 
		 * 依赖：
		 * jquery.edatagrid.js
		 * jquery.arrays.js
		 * tableDialog
		 * table_popup.jsp
	     */
		arrayDialog : {
			init : function(container, options) {
				var field = options.field;
				var input = $('<input type="text" class="datagrid-editable-input" readonly="true">').click(function() {
					var $window = $.fn.datagrid.defaults.editors.arrayDialog.static_window;
					if ($window) {
						$window.remove();
					}
					$window = $('<div>').appendTo($(this).parent()).arrayeditor({
						inputTarget : $(this),
						arrayRule : options.arrayRule,
						table : options.table,
						// 以下二行是为了得到数组控制字段的值。因为此editor生成时不知道控制字段的值是什么，
						// 只能传datagrid，利用当前的editIndex，实时生成弹出时再去动态查询。
						field : field,
						parentDg : $(options.containerDg)
					});
					$.fn.datagrid.defaults.editors.arrayDialog.static_window = $window;
				}).appendTo(container);
				return input;
			},
			destroy : function(target) {
				$(target).remove();
			},
			getValue : function(target) {
				return $(target).val();
			},
			setValue : function(target, value) {
				$(target).val(value);
			},
			resize : function(target, width) {
				$(target)._outerWidth(width);
			}
		}
	});
})(jQuery);