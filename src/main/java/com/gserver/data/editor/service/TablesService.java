package com.gserver.data.editor.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.gserver.data.editor.TableEntity;
import com.gserver.data.editor.dto.TableTitle;
import com.gserver.data.editor.dto.Tree;
import com.gserver.data.editor.util.BaseQEntity;

public interface TablesService {

	public <I extends Serializable> TableEntity getDataById(Class<TableEntity> t, I id);

	public List<TableEntity> findTableListByIds(Class<TableEntity> t, Long[] ids);

	public BaseQEntity<TableEntity> getPage(Class<TableEntity> t, BaseQEntity<TableEntity> b, Map<String, Object> searchParams);

	public <I extends Serializable> void deleteDataById(Class<TableEntity> t, I id);

	/**
	 * 新增一行数据。
	 * 
	 * @param t
	 *            数据对象
	 */
	public void insertData(TableEntity t);

	/**
	 * 更新一行已有数据。
	 * 
	 * @param t
	 *            数据对象
	 */
	public void updateData(TableEntity t);

	/**
	 * 给{@code TableTitleDto}的@{@code editorType}和{@code editorOptions}字段赋参数。
	 * 
	 * @param dto
	 *            被赋值对象
	 * @param beanClass
	 *            对象的类
	 * @param table
	 *            表名
	 * @param fieldName
	 *            字段名
	 * @throws SecurityException
	 * @throws NoSuchFieldException
	 */
	public void setEditor(TableTitle dto, Class<TableEntity> beanClass, String table, String fieldName);

	/**
	 * 通过关联信息，判断{@code tableB}表的值对象{@code t}是否可以删除。
	 * 
	 * @param tableB
	 *            表名
	 * @param t
	 *            表记录值对象
	 * @return {@code true}表示可删，{@code false}表示不可删。
	 */
	public boolean isDeletable(String tableB, TableEntity t);

	/**
	 * 得到下载表的内容。
	 * 
	 * @param t
	 *            数据表类
	 * @return 下载表的内容。
	 */
	public String downloadContent(Class<TableEntity> t);

	/**
	 * 根据包名得到包内数据表的树状结构。
	 * <p>
	 * 如果{@code packageName}为{@code null}，得到一个虚拟的根节点。
	 * 
	 * @param packageName
	 *            包名
	 * @return 包名得到包内数据表的树状结构。
	 */
	public List<Tree> getTreeLayer(String packageName);

	/**
	 * 根据表名和一个字段的名字，去数组配置表查找这个字段已配的可能值。
	 * 
	 * @param tableName
	 *            表名
	 * @param field
	 *            字段名
	 * @return 数组配置表中配的，这个表这个字段的可能值集合。
	 */
	public List<String> getKeyOptions(String tableName, String field);

}
