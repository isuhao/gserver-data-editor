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
		arrayDialog : {
			init : function(container, options) {
				var field = options.field;
				var input = $('<input type="text" class="datagrid-editable-input" readonly="true">').click(function() {
					$('#' + options.divId).arrayeditor({
						divId : options.divId,
						inputTarget : $(this),
						arrayRule : options.arrayRule,
						table : options.table,
						field : field,
						parentDg : $('#dg'),
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