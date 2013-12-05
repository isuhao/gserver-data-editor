<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<table id="dg" title="数据表格" style="width:1000px;height:364px" toolbar="#toolbar" pagination="true" idField="id" rownumbers="true" fitColumns="true" singleSelect="true">
	<thead>
		<tr>
			<th field="${columns[0].name}" width="50">${columns[0].comment}</th>
			<th data-options="field:'${columns[1].name}',width:50,
                        editor:{
                            type:'combobox',
                            options:{
                                valueField:'0',
                                textField:'1',
                                url:'../getTables',
                                required : true,
                                onSelect : function(titlePair) {
                                	generateRaletedOptions(titlePair[0], '${columns[2].name}');
                                	generateRaletedOptions(titlePair[0], '${columns[3].name}');
                                }
                            }
                        }">${columns[1].comment}</th>
			<th data-options="field:'${columns[2].name}',width:50,  editor:{ type:'combobox', options:{ valueField:'name', textField:'comment', required : true} }">${columns[2].comment}</th>
			<th data-options="field:'${columns[3].name}',width:50,  editor:{ type:'combobox', options:{ valueField:'name', textField:'comment', required : true} }">${columns[3].comment}</th>
			<th field="${columns[4].name}" width="50" editor="{type:'text',options:{required:false}}">${columns[4].comment}</th>
			<th field="${columns[5].name}" width="50" editor="{type:'tableWindow', options:{divId:'popTableWin', table:'${tablename}', field:'${columns[5].name}', openTable:'${columns[5].editorOptions[0]}', openTableField:'${columns[5].editorOptions[1]}'}}">${columns[5].comment}</th>
			<th field="${columns[6].name}" width="50" align:'center' editor="{type:'validatebox',options:{validType:'J_Integer'}}">${columns[6].comment}</th>
			<th field="${columns[7].name}" width="50" editor="{type:'text',options:{required:false}}">${columns[7].comment}</th>
		</tr>
	</thead>
</table>