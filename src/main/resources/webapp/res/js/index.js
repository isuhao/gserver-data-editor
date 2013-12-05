$(function() {
	$('#packageTree').tree({
	    url:'getTree',
	    onClick: function(node){
	    	if (!node.state)
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
            		if (node.state !== undefined) {
            			window.location.href="./"+node.id+"/downloadFolder";
            		} else {
            			window.location.href="./"+node.id+"/download";
            		}
            	}
            });
        }
	});
	String.prototype.startWith=function(s){
		  if(s==null||s==""||this.length==0||s.length>this.length)
		   return false;
		  if(this.substr(0,s.length)==s)
		     return true;
		  else
		     return false;
		  return true;
		 };
		
	$('#tt').tabs({
		onSelect : function(table) {
			var currTab = $('#tt').tabs('getTab', table);
			$('#tt').tabs('update', {
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

function open1(table) {
	if ($('#tt').tabs('exists', table)) {
		$('#tt').tabs('select', table);
	} else {
		$('#tt').tabs('add', {
			title : table,
			closable : true,
		});
	}
}