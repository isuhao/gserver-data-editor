package com.gserver.data.editor.dto;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Tree implements Serializable{

	private String id;
	private String text;
	private String state;

	public Tree() {
		super();
	}

	public Tree(String id, String text, String state) {
		this.id = id;
		this.text = text;
		this.state = state;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Tree [id=");
		builder.append(id);
		builder.append(", state=");
		builder.append(state);
		builder.append(", text=");
		builder.append(text);
		builder.append("]");
		return builder.toString();
	}

}
