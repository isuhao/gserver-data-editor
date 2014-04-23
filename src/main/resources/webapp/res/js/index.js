$(function() {
	$('#packageTree').tree({
	    url:'getTree',
	    onClick: function(node){
		if ($(this).tree('isLeaf', node.target))
	    		open1(node.id);
		},
		onContextMenu: function(e,node){
            e.preventDefault();
            $(this).tree('select',node.target);
            if (node.id.startWith('_tt_') || node.id === 'com.gserver.data.editor.entity.conf') {
            	return;
            }
            $('#treeMenu').menu('show',{
                left: e.pageX,
                top: e.pageY,
                onClick: function(item){
            	if(item.id=="importMenu"){
            		uploadFile();
            	}else if(item.id=="exportMenu"){
	            		if (node.state !== undefined) {
	            			window.location.href="./"+node.id+"/downloadFolder";
	            		} else {
	            			window.location.href="./"+node.id+"/download";
	            		}
            		}
            	}
            });
        }
	});
	
	function uploadFile() {
		var $a = $('<input type="file" name="file">');
		var $form = $('<form>').append($a);
		$a.bind('change', function(){
			$mask = $("<div class=\"datagrid-mask\"></div>")
				.css({display:"block",width:"100%",height:$(window).height()})
				.appendTo("body");
			$pending = $("<div class=\"datagrid-mask-msg\"></div>")
				.html("Processing, please wait ...")
				.css({display:"block",left:($(document.body).outerWidth(true) - 190) / 2,top:($(window).height() - 45) / 2})
				.appendTo("body");
			var formdata = new FormData($form[0]);
			$.ajax({
			    type : 'POST',
			    url : './upload',
			    data : formdata,
			    processData : false,
			    contentType : false,
			    success : function(r) {
			        if (r.successed) {
			        	$.messager.show({
			    			title : '上传成功',
			    			msg : r.msg,
			    			timeout : 5000,
			    			showType : 'slide'
			    		});
			        } else {
			        	$.messager.show({
			    			title : '错误',
			    			msg : r.msg,
			    			timeout : 5000,
			    			showType : 'slide'
			    		});
			        }
			        $mask.remove();
			        $pending.remove();
			    },
			    error : function(r) {
			    	alert('jQuery Error');
			        $mask.remove();
			        $pending.remove();
			    }
			});
			$form.remove();
		});
		$a.trigger('click');
	}
	
	String.prototype.startWith=function(s){
		if(s===undefined||s===null||s===""||this.length===0||s.length>this.length)
			return false;
		if(this.substr(0,s.length)===s)
			return true;
		else
			return false;
	};
		
	$('#tt').tabs({
		onSelect : function(table) {
			var self = $(this);
			var currTab = self.tabs('getTab', table);
			self.tabs('update', {
				tab : currTab,
				options : {
					content : createFrame(table + '/open')
				}
			});

			function createFrame(url) {
				var s = '<iframe scrolling="auto" frameborder="0"  src="' + url
						+ '" style="width:100%;height:100%;"></iframe>';
				return s;
			}
		}
	});
});
/**
 * 打开一个表
 * 
 * @param table 表的数据库名
 */
function open1(table) {
	var $tt = $('#tt');
	if ($tt.tabs('exists', table)) {
		$tt.tabs('select', table);
	} else {
		$tt.tabs('add', {
			title : table,
			closable : true,
		});
	}
}