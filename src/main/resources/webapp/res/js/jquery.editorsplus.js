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
		 * 依赖： jquery.validsplus.js
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