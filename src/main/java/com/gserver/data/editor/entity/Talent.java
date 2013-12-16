package com.gserver.data.editor.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.gserver.data.editor.TableEntity;
import com.gserver.data.editor.annotation.ArrayData;
import com.gserver.data.editor.annotation.Comment;

@Entity
@Comment(desc = "天赋表")
@Table(name = "talent")
public class Talent implements TableEntity , Serializable{

	private static final long serialVersionUID = 6930966618111300478L;

	@Id
	@Comment(search = "search_EQ_code", desc = "id")
	@Column(name = "code")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long code;

	@Comment(search = "search_LIKE_name", desc = "名称")
	@Column(name = "name", nullable = false)
	private String name;

	@Comment(search = "search_EQ_pos", desc = "层数")
	@Column(name = "pos", nullable = false)
	private int pos;

	@Comment(search = "search_EQ_type", desc = "类型")
	@Column(name = "type", nullable = false)
	private int type;

	@Comment(search = "search_EQ_Csize", desc = "图片大小")
	@Column(name = "Csize", nullable = false)
	private int csize;

	@Comment(search = "search_EQ_Cimage", desc = "图片编号")
	@Column(name = "Cimage", nullable = false)
	private int cimage;

	@Comment(search = "search_LIKE_lost", desc = "激活消耗")
	@Column(name = "lost", nullable = false)
	@ArrayData
	private String lost;

	@Comment(search = "search_LIKE_value", desc = "参数")
	@Column(name = "value", nullable = false)
	@ArrayData
	private String value;

	@Comment(search = "search_EQ_level", desc = "等级")
	@Column(name = "level", nullable = false)
	private String level;

	@Comment(search = "search_EQ_pre", desc = "前置节点")
	@Column(name = "pre", nullable = false)
	private String pre;

	public Talent() {
		super();
	}

	public Talent(Long code, String name, int pos, int type, int csize, int cimage, String lost, String value,
			String level, String pre) {
		super();
		this.code = code;
		this.name = name;
		this.pos = pos;
		this.type = type;
		this.csize = csize;
		this.cimage = cimage;
		this.lost = lost;
		this.value = value;
		this.level = level;
		this.pre = pre;
	}

	public Long getCode() {
		return code;
	}

	public void setCode(Long code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getPos() {
		return pos;
	}

	public void setPos(int pos) {
		this.pos = pos;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getCsize() {
		return csize;
	}

	public void setCsize(int csize) {
		this.csize = csize;
	}

	public int getCimage() {
		return cimage;
	}

	public void setCimage(int cimage) {
		this.cimage = cimage;
	}

	public String getLost() {
		return lost;
	}

	public void setLost(String lost) {
		this.lost = lost;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getPre() {
		return pre;
	}

	public void setPre(String pre) {
		this.pre = pre;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + cimage;
		result = prime * result + ((code == null) ? 0 : code.hashCode());
		result = prime * result + csize;
		result = prime * result + ((level == null) ? 0 : level.hashCode());
		result = prime * result + ((lost == null) ? 0 : lost.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + pos;
		result = prime * result + ((pre == null) ? 0 : pre.hashCode());
		result = prime * result + type;
		result = prime * result + ((value == null) ? 0 : value.hashCode());
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
		Talent other = (Talent) obj;
		if (cimage != other.cimage)
			return false;
		if (code == null) {
			if (other.code != null)
				return false;
		} else if (!code.equals(other.code))
			return false;
		if (csize != other.csize)
			return false;
		if (level == null) {
			if (other.level != null)
				return false;
		} else if (!level.equals(other.level))
			return false;
		if (lost == null) {
			if (other.lost != null)
				return false;
		} else if (!lost.equals(other.lost))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (pos != other.pos)
			return false;
		if (pre == null) {
			if (other.pre != null)
				return false;
		} else if (!pre.equals(other.pre))
			return false;
		if (type != other.type)
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Talent [code=");
		builder.append(code);
		builder.append(", name=");
		builder.append(name);
		builder.append(", pos=");
		builder.append(pos);
		builder.append(", type=");
		builder.append(type);
		builder.append(", csize=");
		builder.append(csize);
		builder.append(", cimage=");
		builder.append(cimage);
		builder.append(", lost=");
		builder.append(lost);
		builder.append(", value=");
		builder.append(value);
		builder.append(", level=");
		builder.append(level);
		builder.append(", pre=");
		builder.append(pre);
		builder.append("]");
		return builder.toString();
	}

}
