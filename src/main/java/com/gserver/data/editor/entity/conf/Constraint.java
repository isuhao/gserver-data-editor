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
@Comment(desc = "conf:关联")
@Table(name = "_tt_constraint")
public class Constraint implements TableEntity, Serializable{

	private static final long serialVersionUID = -8563972652881167365L;

	@Id
	@Comment(search = "search_EQ_code",desc = "Id")
	@Column(name = "code")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long code;

	@Comment(search = "search_EQ_aname",desc = "目标表名称")
	@Column(name = "aname", nullable = false)
	private String aname;

	@Comment(search = "search_EQ_apos",desc = "目标表列名")
	@Column(name = "apos", nullable = false)
	private String apos;

	@Comment(search = "search_EQ_bname",desc = "源表名称")
	@Column(name = "bname", nullable = false)
	private String bname;

	@Comment(search = "search_EQ_bpos",desc = "源表列名")
	@Column(name = "bpos", nullable = false)
	private String bpos;

	public Constraint() {
		super();
	}

	public Constraint(Long code, String aname, String apos, String bname, String bpos) {
		this.code = code;
		this.aname = aname;
		this.apos = apos;
		this.bname = bname;
		this.bpos = bpos;
	}

	public Long getCode() {
		return code;
	}

	public void setCode(Long code) {
		this.code = code;
	}

	public String getAname() {
		return aname;
	}

	public void setAname(String aname) {
		this.aname = aname;
	}

	public String getApos() {
		return apos;
	}

	public void setApos(String apos) {
		this.apos = apos;
	}

	public String getBname() {
		return bname;
	}

	public void setBname(String bname) {
		this.bname = bname;
	}

	public String getBpos() {
		return bpos;
	}

	public void setBpos(String bpos) {
		this.bpos = bpos;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((aname == null) ? 0 : aname.hashCode());
		result = prime * result + ((apos == null) ? 0 : apos.hashCode());
		result = prime * result + ((bname == null) ? 0 : bname.hashCode());
		result = prime * result + ((bpos == null) ? 0 : bpos.hashCode());
		result = prime * result + ((code == null) ? 0 : code.hashCode());
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
		Constraint other = (Constraint) obj;
		if (aname == null) {
			if (other.aname != null)
				return false;
		} else if (!aname.equals(other.aname))
			return false;
		if (apos == null) {
			if (other.apos != null)
				return false;
		} else if (!apos.equals(other.apos))
			return false;
		if (bname == null) {
			if (other.bname != null)
				return false;
		} else if (!bname.equals(other.bname))
			return false;
		if (bpos == null) {
			if (other.bpos != null)
				return false;
		} else if (!bpos.equals(other.bpos))
			return false;
		if (code == null) {
			if (other.code != null)
				return false;
		} else if (!code.equals(other.code))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Constraint [aname=");
		builder.append(aname);
		builder.append(", apos=");
		builder.append(apos);
		builder.append(", bname=");
		builder.append(bname);
		builder.append(", bpos=");
		builder.append(bpos);
		builder.append(", code=");
		builder.append(code);
		builder.append("]");
		return builder.toString();
	}

}
