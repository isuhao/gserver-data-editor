/**
 * edatagrid - jQuery EasyUI
 *
 * Licensed under the GPL:
 *   http://www.gnu.org/licenses/gpl.txt
 *
 * Copyright 2011 stworthy [ stworthy@gmail.com ]
 *
 * Dependencies:
 *   datagrid
 *   messager
 *
 */
(function($) {
	function buildGrid(target) {
		var opts = $.data(target, 'edatagrid').options;
		$(target).datagrid($.extend({}, opts, {
			onDblClickCell : function(index, field) {
				opts.clickIndex = index;
				if (opts.editing) {
					$(this).edatagrid('editRow', index);
					focusEditor(field);
				}
			},
			onClickCell : function(index, field) {
				opts.clickIndex = index;
				if (opts.editing && opts.editIndex >= 0) {
					$(this).edatagrid('editRow', index);
					focusEditor(field);
				}
			},
			onAfterEdit : function(index, row) {
				opts.editIndex = undefined;
				var url = row.isNewRecord ? opts.saveUrl : opts.updateUrl;
				if (url) {
					$.ajax({
						type : 'POST',
						url : url,
						data : row,
						success : function(data) {
							if (data.success) {
								data.isNewRecord = null;
								$(target).datagrid('updateRow', {
									index : index,
									row : data
								});
								if (opts.tree) {
									var idValue = row[opts.idField || 'id'];
									var t = $(opts.tree);
									var node = t.tree('find', idValue);
									if (node) {
										node.text = row[opts.treeTextField];
										t.tree('update', node);
									} else {
										var pnode = t.tree('find', row[opts.treeParentField]);
										t.tree('append', {
											parent : ( pnode ? pnode.target : null),
											data : [{
												id : idValue,
												text : row[opts.treeTextField]
											}]
										});
									}
								}
							} else {
								var msg=data.msg;
								if(msg==null){									
									$.messager.alert('保存失败', "失败的结果，请刷新！", 'error');
								}else{
									$.messager.alert('保存失败', msg, 'error');
								}
								// TODO 或许以后可以做个回滚，不过现在暂时不会
							}
							opts.onSave.call(target, index, row);
						},
						error : function(data) {
							$.messager.alert('保存失败',"错误的请求，请刷新！" , 'error');
							// TODO 或许以后可以做个回滚，不过现在暂时不会
						},
						dataType : 'json'
					});
				} else {
					opts.onSave.call(target, index, row);
				}
				if (opts.onAfterEdit)
					opts.onAfterEdit.call(target, index, row);
			},
			onCancelEdit : function(index, row) {
				opts.editIndex = undefined;
				if (row.isNewRecord) {
					$(this).datagrid('deleteRow', index);
				}
				if (opts.onCancelEdit) {
					opts.onCancelEdit.call(target, index, row);
				}
			},
			onBeforeLoad : function(param) {
				if (opts.onBeforeLoad.call(target, param) == false) {
					return false;
				}
				$(this).datagrid('rejectChanges');
				if (opts.tree) {
					var node = $(opts.tree).tree('getSelected');
					param[opts.treeParentField] = node ? node.id : undefined;
				}
			}
		}));

		function focusEditor(field) {
			var editor = $(target).datagrid('getEditor', {
				index : opts.editIndex,
				field : field
			});
			if (editor) {
				editor.target.focus();
			} else {
				var editors = $(target).datagrid('getEditors', opts.editIndex);
				if (editors.length) {
					editors[0].target.focus();
				}
			}
		}

		if (opts.tree) {
			$(opts.tree).tree({
				url : opts.treeUrl,
				onClick : function(node) {
					$(target).datagrid('load');
				},
				onDrop : function(dest, source, point) {
					var targetId = $(this).tree('getNode', dest).id;
					$.ajax({
						url : opts.treeDndUrl,
						type : 'post',
						data : {
							id : source.id,
							targetId : targetId,
							point : point
						},
						dataType : 'json',
						success : function() {
							$(target).datagrid('load');
						}
					});
				}
			});
		}
	}


	$.fn.edatagrid = function(options, param) {
		if ( typeof options == 'string') {
			var method = $.fn.edatagrid.methods[options];
			if (method) {
				return method(this, param);
			} else {
				return this.datagrid(options, param);
			}
		}

		options = options || {};
		return this.each(function() {
			var state = $.data(this, 'edatagrid');
			if (state) {
				$.extend(state.options, options);
			} else {
				$.data(this, 'edatagrid', {
					options : $.extend({}, $.fn.edatagrid.defaults, $.fn.edatagrid.parseOptions(this), options)
				});
			}
			buildGrid(this);
		});
	};

	$.fn.edatagrid.parseOptions = function(target) {
		return $.extend({}, $.fn.datagrid.parseOptions(target), {
		});
	};

	$.fn.edatagrid.methods = {
		options : function(jq) {
			var opts = $.data(jq[0], 'edatagrid').options;
			return opts;
		},
		enableEditing : function(jq) {
			return jq.each(function() {
				var opts = $.data(this, 'edatagrid').options;
				opts.editing = true;
			});
		},
		disableEditing : function(jq) {
			return jq.each(function() {
				var opts = $.data(this, 'edatagrid').options;
				opts.editing = false;
			});
		},
		editRow : function(jq, index) {
			return jq.each(function() {
				var dg = $(this);
				var opts = $.data(this, 'edatagrid').options;
				var editIndex = opts.editIndex;
				if (editIndex != index) {
					if (dg.datagrid('validateRow', editIndex)) {
						if (editIndex >= 0) {
							if (opts.onBeforeSave.call(this, editIndex) == false) {
								setTimeout(function() {
									dg.datagrid('selectRow', editIndex);
								}, 0);
								return;
							}
						}
						dg.datagrid('endEdit', editIndex);
						dg.datagrid('beginEdit', index);
						opts.editIndex = index;

						var rows = dg.datagrid('getRows');
						opts.onEdit.call(this, index, rows[index]);
					} else {
						setTimeout(function() {
							dg.datagrid('selectRow', editIndex);
						}, 0);
					}
				}
			});
		},
		addRow : function(jq, index) {
			return jq.each(function() {
				var dg = $(this);
				var opts = $.data(this, 'edatagrid').options;
				if (opts.editIndex >= 0) {
					if (!dg.datagrid('validateRow', opts.editIndex)) {
						dg.datagrid('selectRow', opts.editIndex);
						return;
					}
					if (opts.onBeforeSave.call(this, opts.editIndex) == false) {
						setTimeout(function() {
							dg.datagrid('selectRow', opts.editIndex);
						}, 0);
						return;
					}
					dg.datagrid('endEdit', opts.editIndex);
				}
				var rows = dg.datagrid('getRows');

				function _add(index, row) {
					if (index == undefined) {
						dg.datagrid('appendRow', row);
						opts.editIndex = rows.length - 1;
					} else {
						dg.datagrid('insertRow', {
							index : index,
							row : row
						});
						opts.editIndex = index;
					}
				}

				if ( typeof index == 'object') {
					_add(index.index, $.extend(index.row, {
						isNewRecord : true
					}));
				} else {
					_add(index, {
						isNewRecord : true
					});
				}
				opts.clickIndex = opts.editIndex;

				//				if (index == undefined){
				//					dg.datagrid('appendRow', {isNewRecord:true});
				//					opts.editIndex = rows.length - 1;
				//				} else {
				//					dg.datagrid('insertRow', {
				//						index: index,
				//						row: {isNewRecord:true}
				//					});
				//					opts.editIndex = index;
				//				}

				dg.datagrid('beginEdit', opts.editIndex);
				dg.datagrid('selectRow', opts.editIndex);

				if (opts.tree) {
					var node = $(opts.tree).tree('getSelected');
					rows[opts.editIndex][opts.treeParentField] = ( node ? node.id : 0);
				}

				opts.onAdd.call(this, opts.editIndex, rows[opts.editIndex]);
			});
		},
		saveRow : function(jq) {
			return jq.each(function() {
				var dg = $(this);
				var opts = $.data(this, 'edatagrid').options;
				if (opts.onBeforeSave.call(this, opts.editIndex) == false) {
					setTimeout(function() {
						dg.datagrid('selectRow', opts.editIndex);
					}, 0);
					return;
				}
				$(this).datagrid('endEdit', opts.editIndex);
			});
		},
		getClickIndex : function(jq) {
			// 还不太明白为啥别的方法都用的jq.each
			var opts = $.data(jq[0], 'edatagrid').options;
			return opts.clickIndex;
		},
		cancelRow : function(jq) {
			return jq.each(function() {
				var index = $(this).edatagrid('options').editIndex;
				$(this).datagrid('cancelEdit', index);
			});
		},
		destroyRow : function(jq) {
			return jq.each(function() {
				var dg = $(this);
				var opts = $.data(this, 'edatagrid').options;
				var row = dg.datagrid('getSelected');
				if (!row) {
					$.messager.show({
						title : opts.destroyMsg.norecord.title,
						msg : opts.destroyMsg.norecord.msg
					});
					return;
				}
				$.messager.confirm(opts.destroyMsg.confirm.title, opts.destroyMsg.confirm.msg, function(r) {
					if (r) {
						var index = dg.datagrid('getRowIndex', row);
						if (row.isNewRecord) {
							dg.datagrid('cancelEdit', index);
						} else {
							if (opts.destroyUrl) {
								//var idValue = row[opts.idField || 'code'];
								//$.post(opts.destroyUrl, {code:idValue}, function(){
								$.post(opts.destroyUrl, row, function(data) {
									if (data.success) {
										if (opts.tree) {
											dg.datagrid('reload');
											var t = $(opts.tree);
											var node = t.tree('find', idValue);
											if (node) {
												t.tree('remove', node.target);
											}
										} else {
											dg.datagrid('cancelEdit', index);
											dg.datagrid('deleteRow', index);
										}
										opts.onDestroy.call(dg[0], index, row);
									} else {
										var msg=data.msg;
										if(msg==null){									
											$.messager.alert('删除失败', '存在关联，不能删除！', 'error');
										}else{
											$.messager.alert('删除失败', msg, 'error');
										}
									}

								},"json");
							} else {
								dg.datagrid('cancelEdit', index);
								dg.datagrid('deleteRow', index);
								opts.onDestroy.call(dg[0], index, row);
							}
						}
					}
				});
			});
		}
	};

	$.fn.edatagrid.defaults = $.extend({}, $.fn.datagrid.defaults, {
		editing : true,
		editIndex : -1,
		clickIndex : -1,
		destroyMsg : {
			norecord : {
				title : 'Warning',
				msg : 'No record is selected.'
			},
			confirm : {
				title : 'Confirm',
				msg : 'Are you sure you want to delete?'
			}
		},
		//		destroyConfirmTitle: 'Confirm',
		//		destroyConfirmMsg: 'Are you sure you want to delete?',

		url : null, // return the datagrid data
		saveUrl : null, // return the added row
		updateUrl : null, // return the updated row
		destroyUrl : null, // return {success:true}
		popupWin : null,

		tree : null, // the tree selector
		treeUrl : null, // return tree data
		treeDndUrl : null, // to process the drag and drop operation, return {success:true}
		treeTextField : 'name',
		treeParentField : 'parentId',

		onAdd : function(index, row) {
		},
		onEdit : function(index, row) {
		},
		onBeforeSave : function(index) {
		},
		onSave : function(index, row) {
		},
		onDestroy : function(index, row) {
		}
	});

	$.extend($.fn.validatebox.defaults.rules, {
		J_Byte : {
			validator : function(value, param) {
				var rawStr = String(value);
				if (rawStr.indexOf('.') == -1) {
					var parseNumber = Number(value);
					if (parseNumber === parseNumber && parseNumber >= -128 && parseNumber <= 127)
						return true;
				}
				return false;
			},
			message : "Please input a byte."
		},
		J_Short : {
			validator : function(value, param) {
				var rawStr = String(value);
				if (rawStr.indexOf('.') == -1) {
					var parseNumber = Number(value);
					if (parseNumber === parseNumber && parseNumber >= -32768 && parseNumber <= 32767) {
						return true;
					}
				}
				return false;
			},
			message : "Please input a short."
		},
		J_Integer : {
			validator : function(value, param) {
				var rawStr = String(value);
				if (rawStr.indexOf('.') == -1) {
					var parseNumber = Number(value);
					if (parseNumber === parseNumber && parseNumber >= -2147483648 && parseNumber <= 2147483647) {
						return true;
					}
				}
				return false;
			},
			message : "Please input an int."
		},
		J_Long : {
			validator : function(value, param) {
				var rawStr = String(value);
				if (rawStr.indexOf('.') == -1) {
					var parseNumber = Number(value);
					if (parseNumber === parseNumber && parseNumber >= -9223372036854774808 && parseNumber <= 9223372036854774807) {
						return true;
					}
				}
				return false;
			},
			message : "Please input a long."
		},
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

	$.extend($.fn.datagrid.defaults.editors, {
		tableWindow : {
			init : function(container, options) {
				if ($.fn.datagrid.defaults.editors.tableWindow.static_counter === undefined) {
					$.fn.datagrid.defaults.editors.tableWindow.static_counter = 0;
				}
				var counter = $.fn.datagrid.defaults.editors.tableWindow.static_counter++;
				var popup = options.divId;
				var table = options.table;
				var field = options.field;
				var openTable = options.openTable;
				var openTableField = options.openTableField;
				var inputId = 'tableWinInput_' + field + '_' + counter;
				var input = $('<input id="' + inputId + '" type="text" class="datagrid-editable-input" readonly="true">').click(function() {
					$('#popWinInputId').val($(this).attr('id'));
					var url = '../' + openTable + '/popopen?relatedfield=' + openTableField + '&table=' + table + '&field=' + field;
					$('#'+popup).window({
						width : 718,
						height : 344,
						minimizable : false,
						collapsible : false,
						modal : true,
						title : field,
						href : url
					});
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

	$.extend($.fn.datagrid.defaults.editors, {
		arrayDialog : {
			init : function(container, options) {
				var arrayRule = options.arrayRule;
				var table = options.table;
				var field = options.field;
				var inputId = 'arrayDialogInput_' + field;
				var input = $('<input id="' + inputId + '" type="text" class="datagrid-editable-input" readonly="true">').click(function() {
					$('#' + options.divId).arrayeditor({
						divId : options.divId,
						inputJq : $('#' + inputId),
						arrayRule : arrayRule,
						table : table,
						field : field,
						parentDg : $('#dg'),
						dialogTableId : options.dialogTableId,
						parentEditIndex : $('#dg').edatagrid('getClickIndex')
					});
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
