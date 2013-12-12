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