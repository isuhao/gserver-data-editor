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
		validateCombobox: {
			init: function(container, options){
				var combodata;
				var validType = undefined;
				var required = undefined;
				$.ajax({
					url : "../validateCombobox",
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
		tableDialog : {
			init : function(container, options) {
				if ($.fn.datagrid.defaults.editors.tableDialog.static_counter === undefined) {
					$.fn.datagrid.defaults.editors.tableDialog.static_counter = 0;
				}
				var counter = $.fn.datagrid.defaults.editors.tableDialog.static_counter++;
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
		},
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