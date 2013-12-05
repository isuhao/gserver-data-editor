package com.gserver.data.editor.dao;

import java.lang.reflect.Field;
import java.util.List;

import org.hibernate.criterion.Criterion;

import com.gserver.data.editor.TableEntity;

/**
 * 
 */
public interface TablesDao extends BaseDao {

	public <T> List<T> getAll(Class<T> clazz, Criterion... criterion);

	public int count(Class<TableEntity> clazz, Criterion... criterion);

	public long count(Class<TableEntity> clazz, Field field);
}
