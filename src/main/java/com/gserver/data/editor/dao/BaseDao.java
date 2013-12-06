package com.gserver.data.editor.dao;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.gserver.data.editor.TableEntity;
import com.gserver.data.editor.util.BaseQEntity;


public interface BaseDao {

	/**
	 * 根据Id获得对象。
	 * 
	 * @param <T>
	 * @param <I>
	 * @param clz
	 * @param id
	 * @return
	 */
	public <T, I extends Serializable> T get(Class<T> clz, I id);

	/**
	 * 保存新增的数据行
	 */
	public <T> void save(T t);

	/**
	 * 更新已有的数据行。
	 */
	public <T> void update(T t);

	/**
	 * 根据Id删除记录。
	 * 
	 * @param <T>
	 * @param entityClass
	 * @param entityid
	 */
	public <T> void delete(Class<T> entityClass, Object entityid);

	/**
	 * 根据Id数组删除记录。
	 * 
	 * @param <T>
	 * @param entityClass
	 * @param entityids
	 */
	public <T> void delete(Class<T> entityClass, Object[] entityids);

	public <T> List<T> findAll(Class<T> clazz, Specification<T> specification);

	public <T> long getCount(Class<T> entityClass, Specification<T> specification);

	public BaseQEntity<TableEntity> getPage(Class<TableEntity> t, BaseQEntity<TableEntity> b, Specification<TableEntity> specification);
}
