package com.gserver.data.editor.util;

import java.util.List;

public class BaseQEntity<T> {

	protected long total = 0;

	protected int currentPage = 1;

	protected int totalPages = 1;

	protected int perPageRows = 15;

	protected boolean showPages = true;

	protected boolean isOrder = false;

	protected String orderby = "";

	protected String asc = "asc";

	protected List<T> rows;

	public List<T> getRows() {
		return rows;
	}

	public void setRows(List<T> rows) {
		this.rows = rows;
	}

	public boolean isOrder() {
		return isOrder;
	}

	public static <T> BaseQEntity<T> newInstance() {
		return new BaseQEntity<T>();
	}

	private BaseQEntity() {
	}

	public BaseQEntity<T> setOrder(boolean isOrder) {
		this.isOrder = isOrder;
		return this;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public BaseQEntity<T> setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
		return this;
	}

	public int getPerPageRows() {
		return perPageRows;
	}

	public BaseQEntity<T> setPerPageRows(int perPageRows) {
		this.perPageRows = perPageRows;
		return this;
	}

	public boolean isShowPages() {
		return showPages;
	}

	public BaseQEntity<T> setShowPages(boolean showPages) {
		this.showPages = showPages;
		return this;
	}

	public int getTotalPages() {
		return totalPages;
	}

	public BaseQEntity<T> setTotalPages(int totalPages) {
		this.totalPages = totalPages;
		return this;
	}

	

	public long getTotal() {
		return total;
	}

	public BaseQEntity<T> setTotal(long total) {
		this.total = total;
		this.totalPages = (int) Math.ceil((this.total - 1) / this.perPageRows) + 1;
		if (this.currentPage > this.totalPages) {
			this.currentPage = 1;
		}
		return this;
	}

	public String getAsc() {
		return asc;
	}

	public BaseQEntity<T> setAsc(String asc) {
		this.asc = asc;
		return this;
	}

	public String getOrderby() {
		return orderby;
	}

	public BaseQEntity<T> setOrderby(String orderby) {
		this.orderby = orderby;
		return this;
	}
}
