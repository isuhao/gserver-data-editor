package com.gserver.data.editor.entity.conf;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.gserver.data.editor.TableEntity;
import com.gserver.data.editor.annotation.Comment;

@Entity
@Comment(desc = "conf:数组限定")
@Table(name = "_tt_arrayrule")
@SuppressWarnings("serial")
public class ArrayRule implements TableEntity , Serializable{
	@Id
	@Comment(search = "search_EQ_code", desc = "Id")
	@Column(name = "code")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long code;

	@Comment(search = "search_EQ_tableName", desc = "表名称")
	@Column(name = "tableName", nullable = false)
	private String tableName;

	@Comment(search = "search_EQ_keyField", desc = "控制列")
	@Column(name = "keyField", nullable = true)
	private String keyField;

	@Comment(search = "search_EQ_targetField", desc = "被控制列")
	@Column(name = "targetField", nullable = false)
	private String targetField;

	@Comment(search = "search_EQ_keyValue", desc = "控制列可能值")
	@Column(name = "keyValue", nullable = false)
	private String keyValue;

	@Comment(search = "search_EQ_constraint_id", desc = "关联约束记录id")
	@Column(name = "constraint_id", nullable = true)
	private Long constraint_id;

	@Comment(search = "search_EQ_idx", desc = "元素下标顺序")
	@Column(name = "idx", nullable = false)
	private int idx;

	@Comment(search = "search_LIKE_description", desc = "元素描述")
	@Column(name = "description", nullable = true)
	private String desc;

	public ArrayRule() {
		super();
	}

	public ArrayRule(Long code, String tableName, String keyField, String targetField, String keyValue, Long constraintId, int idx, String desc) {
		this.code = code;
		this.tableName = tableName;
		this.keyField = keyField;
		this.targetField = targetField;
		this.keyValue = keyValue;
		this.constraint_id = constraintId;
		this.idx = idx;
		this.desc = desc;
	}

	public Long getCode() {
		return code;
	}

	public void setCode(Long code) {
		this.code = code;
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

	public Long getConstraint_id() {
		return constraint_id;
	}

	public void setConstraint_id(Long constraintId) {
		constraint_id = constraintId;
	}

	public int getIdx() {
		return idx;
	}

	public void setIdx(int idx) {
		this.idx = idx;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((code == null) ? 0 : code.hashCode());
		result = prime * result + ((constraint_id == null) ? 0 : constraint_id.hashCode());
		result = prime * result + ((desc == null) ? 0 : desc.hashCode());
		result = prime * result + idx;
		result = prime * result + ((keyField == null) ? 0 : keyField.hashCode());
		result = prime * result + ((keyValue == null) ? 0 : keyValue.hashCode());
		result = prime * result + ((tableName == null) ? 0 : tableName.hashCode());
		result = prime * result + ((targetField == null) ? 0 : targetField.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ArrayRule other = (ArrayRule) obj;
		if (code == null) {
			if (other.code != null)
				return false;
		} else if (!code.equals(other.code))
			return false;
		if (constraint_id == null) {
			if (other.constraint_id != null)
				return false;
		} else if (!constraint_id.equals(other.constraint_id))
			return false;
		if (desc == null) {
			if (other.desc != null)
				return false;
		} else if (!desc.equals(other.desc))
			return false;
		if (idx != other.idx)
			return false;
		if (keyField == null) {
			if (other.keyField != null)
				return false;
		} else if (!keyField.equals(other.keyField))
			return false;
		if (keyValue == null) {
			if (other.keyValue != null)
				return false;
		} else if (!keyValue.equals(other.keyValue))
			return false;
		if (tableName == null) {
			if (other.tableName != null)
				return false;
		} else if (!tableName.equals(other.tableName))
			return false;
		if (targetField == null) {
			if (other.targetField != null)
				return false;
		} else if (!targetField.equals(other.targetField))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ArrayRule [code=");
		builder.append(code);
		builder.append(", constraint_id=");
		builder.append(constraint_id);
		builder.append(", desc=");
		builder.append(desc);
		builder.append(", idx=");
		builder.append(idx);
		builder.append(", keyField=");
		builder.append(keyField);
		builder.append(", keyValue=");
		builder.append(keyValue);
		builder.append(", tableName=");
		builder.append(tableName);
		builder.append(", targetField=");
		builder.append(targetField);
		builder.append("]");
		return builder.toString();
	}

}
