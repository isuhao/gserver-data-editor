package com.gserver.data.editor.entity.conf;

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
@Comment(desc = "test:类型测试表")
@Table(name = "_tt_alltypes")
public class TestEntity implements TableEntity {
	@Id
	@Comment(search = "search_EQ_code",desc = "id")
	@Column(name = "code")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long code;

	@Comment(search = "search_EQ_jbyte",desc = "jbyte")
	@Column(name = "jbyte", nullable = true)
	private byte jbyte;

	@Comment(search = "search_EQ_jshort",desc = "jshort")
	@Column(name = "jshort", nullable = true)
	private short jshort;

	@Comment(search = "search_EQ_jint",desc = "jint")
	@Column(name = "jint", nullable = true)
	private int jint;

	@Comment(search = "search_EQ_jlong",desc = "jlong")
	@Column(name = "jlong", nullable = true)
	private long jlong;

	@Comment(search = "search_EQ_jfloat",desc = "jfloat")
	@Column(name = "jfloat", nullable = true)
	private float jfloat;

	@Comment(search = "search_EQ_jdouble",desc = "jdouble")
	@Column(name = "jdouble", nullable = true)
	private double jdouble;
	
	@Comment(search = "search_LIKE_jstring",desc = "jstring")
	@Column(name = "jstring", nullable = true)
	private String jstring;
	
	@Comment(search = "search_LIKE_jstringarray",desc = "jstringarray")
	@Column(name = "jstringarray", nullable = true)
	@ArrayData
	private String jstringarray;

	public TestEntity() {
		super();
	}

	public TestEntity(Long code, byte jbyte, short jshort, int jint, long jlong, float jfloat, double jdouble, String jstring, String jstringarray) {
		this.code = code;
		this.jbyte = jbyte;
		this.jshort = jshort;
		this.jint = jint;
		this.jlong = jlong;
		this.jfloat = jfloat;
		this.jdouble = jdouble;
		this.jstring = jstring;
		this.jstringarray = jstringarray;
	}

	public Long getCode() {
		return code;
	}

	public void setCode(Long code) {
		this.code = code;
	}

	public byte getJbyte() {
		return jbyte;
	}

	public void setJbyte(byte jbyte) {
		this.jbyte = jbyte;
	}

	public short getJshort() {
		return jshort;
	}

	public void setJshort(short jshort) {
		this.jshort = jshort;
	}

	public int getJint() {
		return jint;
	}

	public void setJint(int jint) {
		this.jint = jint;
	}

	public long getJlong() {
		return jlong;
	}

	public void setJlong(long jlong) {
		this.jlong = jlong;
	}

	public float getJfloat() {
		return jfloat;
	}

	public void setJfloat(float jfloat) {
		this.jfloat = jfloat;
	}

	public double getJdouble() {
		return jdouble;
	}

	public void setJdouble(double jdouble) {
		this.jdouble = jdouble;
	}

	public String getJstring() {
		return jstring;
	}

	public void setJstring(String jstring) {
		this.jstring = jstring;
	}

	public String getJstringarray() {
		return jstringarray;
	}

	public void setJstringarray(String jstringarray) {
		this.jstringarray = jstringarray;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((code == null) ? 0 : code.hashCode());
		result = prime * result + jbyte;
		long temp;
		temp = Double.doubleToLongBits(jdouble);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + Float.floatToIntBits(jfloat);
		result = prime * result + jint;
		result = prime * result + (int) (jlong ^ (jlong >>> 32));
		result = prime * result + jshort;
		result = prime * result + ((jstring == null) ? 0 : jstring.hashCode());
		result = prime * result + ((jstringarray == null) ? 0 : jstringarray.hashCode());
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
		TestEntity other = (TestEntity) obj;
		if (code == null) {
			if (other.code != null)
				return false;
		} else if (!code.equals(other.code))
			return false;
		if (jbyte != other.jbyte)
			return false;
		if (Double.doubleToLongBits(jdouble) != Double.doubleToLongBits(other.jdouble))
			return false;
		if (Float.floatToIntBits(jfloat) != Float.floatToIntBits(other.jfloat))
			return false;
		if (jint != other.jint)
			return false;
		if (jlong != other.jlong)
			return false;
		if (jshort != other.jshort)
			return false;
		if (jstring == null) {
			if (other.jstring != null)
				return false;
		} else if (!jstring.equals(other.jstring))
			return false;
		if (jstringarray == null) {
			if (other.jstringarray != null)
				return false;
		} else if (!jstringarray.equals(other.jstringarray))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("TestEntity [code=");
		builder.append(code);
		builder.append(", jbyte=");
		builder.append(jbyte);
		builder.append(", jdouble=");
		builder.append(jdouble);
		builder.append(", jfloat=");
		builder.append(jfloat);
		builder.append(", jint=");
		builder.append(jint);
		builder.append(", jlong=");
		builder.append(jlong);
		builder.append(", jshort=");
		builder.append(jshort);
		builder.append(", jstring=");
		builder.append(jstring);
		builder.append(", jstringarray=");
		builder.append(jstringarray);
		builder.append("]");
		return builder.toString();
	}

}
