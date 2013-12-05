package com.gserver.data.editor.dto;

import java.util.Arrays;

import com.gserver.data.editor.util.EditorType;

/**
 * 表标题行信息传输对象，包括客户端建立{@code datagrid}的所有必须知道的参数。
 * 
 */
public class TableTitle {
	/**
	 * 列对应的POJO字段名
	 */
	private String name;
	/**
	 * 列对应的中文列名解释
	 */
	private String comment;
	/**
	 * 注解的查找语句
	 */
	private String search;
	/**
	 * {@code datagrid}编辑器类型
	 */
	private EditorType editorType;
	/**
	 * {@code datagrid}编辑器参数，此字段因{@link #editorType}的不同而不同。
	 */
	private String[] editorOptions = {};

	public TableTitle() {
		super();
	}

	public TableTitle(String name, String comment, String search, EditorType editorType,
			String[] editorOptions) {
		this.name = name;
		this.search = search;
		this.comment = comment;
		this.editorType = editorType;
		this.editorOptions = editorOptions;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getSearch() {
		return search;
	}

	public void setSearch(String search) {
		this.search = search;
	}

	public EditorType getEditorType() {
		return editorType;
	}

	public void setEditorType(EditorType editorType) {
		this.editorType = editorType;
	}

	public String[] getEditorOptions() {
		return editorOptions;
	}

	public void setEditorOptions(String[] editorOptions) {
		this.editorOptions = editorOptions;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("TableTitleDto [comment=");
		builder.append(comment);
		builder.append(", editorOptions=");
		builder.append(Arrays.toString(editorOptions));
		builder.append(", editorType=");
		builder.append(editorType);
		builder.append(", name=");
		builder.append(name);
		builder.append(", search=");
		builder.append(search);
		builder.append("]");
		return builder.toString();
	}

}
