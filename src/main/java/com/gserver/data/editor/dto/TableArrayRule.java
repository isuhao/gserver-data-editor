package com.gserver.data.editor.dto;


public class TableArrayRule {

	private String tableName;// 表名
	private String keyField;// 源字段
	private String keyValue;// 源值
	private String targetField;// 目标字段
	private Long keyValueNum;// 目标值个数

	public TableArrayRule(String tableName, String keyField, String keyValue, String targetField, Long keyValueNum) {
		this.tableName = tableName;
		this.keyField = keyField;
		this.keyValue = keyValue;
		this.targetField = targetField;
		this.keyValueNum = keyValueNum;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getKeyField() {
		return keyField;
	}

	public void setKeyField(String keyField) {
		this.keyField = keyField;
	}

	public String getTargetField() {
		return targetField;
	}

	public void setTargetField(String targetField) {
		this.targetField = targetField;
	}

	public String getKeyValue() {
		return keyValue;
	}

	public void setKeyValue(String keyValue) {
		this.keyValue = keyValue;
	}

	public Long getKeyValueNum() {
		return keyValueNum;
	}

	public void setKeyValueNum(Long keyValueNum) {
		this.keyValueNum = keyValueNum;
	}
}
