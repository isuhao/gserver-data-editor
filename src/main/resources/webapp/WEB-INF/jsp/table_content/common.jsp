<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<table id="dg" title="数据表格" style="width:1000px;height:364px" toolbar="#toolbar" pagination="true" idField="id" rownumbers="true" fitColumns="true" singleSelect="true">
	<thead>
		<tr>
			<c:forEach var="column" items="${columns}">
				<c:choose>
					<c:when test='${column.editorType=="NoEditor"}'>
						<th field="${column.name}" width="50">${column.comment}</th>
					</c:when>
					<c:when test='${column.editorType=="J_String"}'>
						<th field="${column.name}" width="50" editor="{type:'text',options:{required:false}}">${column.comment}</th>
					</c:when>
					<c:when test='${column.editorType=="J_Byte"}'>
						<th field="${column.name}" width="50" align:'center' editor="{type:'validatebox',options:{validType:'J_Byte', required:true}}">${column.comment}</th>
					</c:when>
					<c:when test='${column.editorType=="J_Short"}'>
						<th field="${column.name}" width="50" align:'center' editor="{type:'validatebox',options:{validType:'J_Short', required:true}}">${column.comment}</th>
					</c:when>
					<c:when test='${column.editorType=="J_Integer"}'>
						<th field="${column.name}" width="50" align:'center' editor="{type:'validatebox',options:{validType:'J_Integer', required:true}}">${column.comment}</th>
					</c:when>
					<c:when test='${column.editorType=="J_Long"}'>
						<th field="${column.name}" width="50" align:'center' editor="{type:'validatebox',options:{validType:'J_Long', required:true}}">${column.comment}</th>
					</c:when>
					<c:when test='${column.editorType=="J_Float"}'>
						<th field="${column.name}" width="50" align:'center' editor="{type:'validatebox',options:{validType:'J_Float', required:true}}">${column.comment}</th>
					</c:when>
					<c:when test='${column.editorType=="J_Double"}'>
						<th field="${column.name}" width="50" align:'center' editor="{type:'validatebox',options:{validType:'J_Double', required:true}}">${column.comment}</th>
					</c:when>
					<c:when test='${column.editorType=="Array"}'>
						<th field="${column.name}" width="50" editor='{type:"arrayDialog", options:{table:"${tablename}", field:"${column.name}", arrayRule:${column.editorOptions[0]}, containerDg:"#dg" }}'>${column.comment}</th>
					</c:when>
					<c:when test='${column.editorType=="Foreign"}'>
						<th field="${column.name}" width="50" editor="{type:'tableDialog', options:{table:'${tablename}', field:'${column.name}', openTable:'${column.editorOptions[0]}', openTableField:'${column.editorOptions[1]}'}}">${column.comment}</th>
					</c:when>
					<c:when test='${column.editorType=="Key"}'>
						<th field="${column.name}" width="50" editor="{type:'validateCombobox', options:{table:'${tablename}', field:'${column.name}', url:'../validateCombobox'}}">${column.comment}</th>
					</c:when>
					<c:otherwise>
						<th field="${column.name}" width="50" editor="{type:'text',options:{required:false}}">${column.comment}</th>
					</c:otherwise>
				</c:choose>
			</c:forEach>
		</tr>
	</thead>
</table>