package com.gserver.data.editor.entity;

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
@Comment(desc = "任务表")
@Table(name = "mission")
public class Mission implements TableEntity , Serializable{
	
	private static final long serialVersionUID = -6605646337536152059L;

	@Id
	@Comment(search = "search_EQ_code", desc = "任务id")
	@Column(name = "code", nullable = false)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long code;

	@Comment(search = "search_LIKE_name", desc = "名称")
	@Column(name = "name", nullable = false)
	private String name;

	@Comment(search = "search_EQ_level", desc = "任务等级")
	@Column(name = "level", nullable = false)
	private int level;

	@Comment(search = "search_EQ_level", desc = "可见等级")
	@Column(name = "previewlvl", nullable = false)
	private int previewlvl;

	@Comment(search = "search_LIKE_conditionz", desc = "接收条件")
	@Column(name = "conditionz", nullable = false)
	private String condition;

	@Comment(search = "search_EQ_type", desc = "任务类型")
	@Column(name = "type", nullable = false)
	private int type;

	@Comment(search = "search_EQ_ctype", desc = "子类型")
	@Column(name = "ctype", nullable = false)
	private int ctype;

	@Comment(search = "search_EQ_repeatable", desc = "是否可重复领取")
	@Column(name = "repeatable", nullable = false)
	private boolean repeatable;

	@Comment(search = "search_EQ_timeType", desc = "开始类型")
	@Column(name = "timeType", nullable = false)
	private int timeType;

	@Comment(search = "search_EQ_startTime", desc = "开始时间")
	@Column(name = "startTime", nullable = false)
	private long startTime;

	@Comment(search = "search_EQ_time", desc = "时间")
	@Column(name = "time", nullable = false)
	private long time;

	@Comment(search = "search_EQ_pre", desc = "前置任务")
	@Column(name = "pre", nullable = false)
	private long pre;

	@Comment(search = "search_EQ_next", desc = "后续任务")
	@Column(name = "next", nullable = false)
	private long next;

	@Comment(search = "search_EQ_talk", desc = "任务对话")
	@Column(name = "talk", nullable = false)
	private String talk;

	public Mission() {
		super();
	}

	public Mission(Long code, String name, int level, int previewlvl, String condition, int type, int ctype,
			boolean repeatable, int timeType, long startTime, long time, long pre, long next, String talk) {
		super();
		this.code = code;
		this.name = name;
		this.level = level;
		this.previewlvl = previewlvl;
		this.condition = condition;
		this.type = type;
		this.ctype = ctype;
		this.repeatable = repeatable;
		this.timeType = timeType;
		this.startTime = startTime;
		this.time = time;
		this.pre = pre;
		this.next = next;
		this.talk = talk;
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

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getPreviewlvl() {
		return previewlvl;
	}

	public void setPreviewlvl(int previewlvl) {
		this.previewlvl = previewlvl;
	}

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getCtype() {
		return ctype;
	}

	public void setCtype(int ctype) {
		this.ctype = ctype;
	}

	public boolean isRepeatable() {
		return repeatable;
	}

	public void setRepeatable(boolean repeatable) {
		this.repeatable = repeatable;
	}

	public int getTimeType() {
		return timeType;
	}

	public void setTimeType(int timeType) {
		this.timeType = timeType;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public long getPre() {
		return pre;
	}

	public void setPre(long pre) {
		this.pre = pre;
	}

	public long getNext() {
		return next;
	}

	public void setNext(long next) {
		this.next = next;
	}

	public String getTalk() {
		return talk;
	}

	public void setTalk(String talk) {
		this.talk = talk;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((code == null) ? 0 : code.hashCode());
		result = prime * result + ((condition == null) ? 0 : condition.hashCode());
		result = prime * result + ctype;
		result = prime * result + level;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + (int) (next ^ (next >>> 32));
		result = prime * result + (int) (pre ^ (pre >>> 32));
		result = prime * result + previewlvl;
		result = prime * result + (repeatable ? 1231 : 1237);
		result = prime * result + (int) (startTime ^ (startTime >>> 32));
		result = prime * result + ((talk == null) ? 0 : talk.hashCode());
		result = prime * result + (int) (time ^ (time >>> 32));
		result = prime * result + timeType;
		result = prime * result + type;
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
		Mission other = (Mission) obj;
		if (code == null) {
			if (other.code != null)
				return false;
		} else if (!code.equals(other.code))
			return false;
		if (condition == null) {
			if (other.condition != null)
				return false;
		} else if (!condition.equals(other.condition))
			return false;
		if (ctype != other.ctype)
			return false;
		if (level != other.level)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (next != other.next)
			return false;
		if (pre != other.pre)
			return false;
		if (previewlvl != other.previewlvl)
			return false;
		if (repeatable != other.repeatable)
			return false;
		if (startTime != other.startTime)
			return false;
		if (talk == null) {
			if (other.talk != null)
				return false;
		} else if (!talk.equals(other.talk))
			return false;
		if (time != other.time)
			return false;
		if (timeType != other.timeType)
			return false;
		if (type != other.type)
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Mission [code=");
		builder.append(code);
		builder.append(", name=");
		builder.append(name);
		builder.append(", level=");
		builder.append(level);
		builder.append(", previewlvl=");
		builder.append(previewlvl);
		builder.append(", condition=");
		builder.append(condition);
		builder.append(", type=");
		builder.append(type);
		builder.append(", ctype=");
		builder.append(ctype);
		builder.append(", repeatable=");
		builder.append(repeatable);
		builder.append(", timeType=");
		builder.append(timeType);
		builder.append(", startTime=");
		builder.append(startTime);
		builder.append(", time=");
		builder.append(time);
		builder.append(", pre=");
		builder.append(pre);
		builder.append(", next=");
		builder.append(next);
		builder.append(", talk=");
		builder.append(talk);
		builder.append("]");
		return builder.toString();
	}

}
