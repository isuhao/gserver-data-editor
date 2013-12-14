package com.gserver.data.editor.service.impl;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Table;

import org.apache.commons.beanutils.PropertyUtils;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.SimpleExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.gserver.data.editor.TableEntity;
import com.gserver.data.editor.annotation.ArrayData;
import com.gserver.data.editor.annotation.Comment;
import com.gserver.data.editor.dao.TablesDao;
import com.gserver.data.editor.dto.TableTitle;
import com.gserver.data.editor.dto.Tree;
import com.gserver.data.editor.entity.conf.ArrayRule;
import com.gserver.data.editor.entity.conf.Constraint;
import com.gserver.data.editor.service.TablesService;
import com.gserver.data.editor.util.BaseQEntity;
import com.gserver.data.editor.util.ClassUtils;
import com.gserver.data.editor.util.DynamicSpecifications;
import com.gserver.data.editor.util.EditorType;
import com.gserver.data.editor.util.EntityUtils;
import com.gserver.data.editor.util.ReflectionUtils;
import com.gserver.data.editor.util.SearchFilter;

@Service("tablesService")
public class TablesServiceImpl implements TablesService {

	@Autowired
	TablesDao tablesDao;

	public <I extends Serializable> TableEntity getDataById(Class<TableEntity> t, I id) {
		return tablesDao.get(t, id);
	}

	public List<TableEntity> findTableListByIds(Class<TableEntity> t, Long[] ids) {
		SimpleExpression[] list = new SimpleExpression[ids.length];
		for (int i = 0; i < ids.length; i++) {
			list[i] = Restrictions.eq("code", Long.valueOf(ids[i]));
		}
		List<TableEntity> all = tablesDao.getAll(t, Restrictions.or(list));
		return all;
	}

	public void insertData(TableEntity t) {
		tablesDao.save(t);
	}

	public void updateData(TableEntity t) {
		tablesDao.update(t);
	}

	public <I extends Serializable> void deleteDataById(Class<TableEntity> t, I id) {
		tablesDao.delete(t, id);
	}

	public void setEditor(TableTitle dto, Class<TableEntity> beanClass, String table, String fieldName) {
		Field field = ReflectionUtils.getDeclaredField(beanClass, fieldName);
		// 得到数据表table中column列的外表关联参数。
		List<Constraint> constraints = tablesDao.getAll(Constraint.class, Restrictions.eq("aname", table), Restrictions.eq("apos", fieldName));
		String[] editorOptionsForeign = null;
		if (constraints != null && constraints.size() > 0) {
			// 数组表示参数，数组下标{@code [0]}对应的表B名称，下标{@code [1]}表示此关联在表B中的列
			Constraint cons = constraints.get(0);
			editorOptionsForeign = new String[] { cons.getBname(), cons.getBpos() };
		}
		List<ArrayRule> arrayRules_key = tablesDao.getAll(ArrayRule.class, Restrictions.eq("tableName", table), Restrictions.eq("keyField", fieldName));
		EntityUtils.setEditorType(dto, field, editorOptionsForeign, arrayRules_key);
		if (dto.getEditorType() == EditorType.Array) {
			List<ArrayRule> arrayRules_target = tablesDao.getAll(ArrayRule.class, Restrictions.eq("tableName", table), Restrictions.eq("targetField", fieldName));
			// Map<控制列名, Map<可能的控制值, List<ArrayRule>>>
			Map<String, Map<String, List<ArrayRule>>> map = Maps.newHashMap();
			for (ArrayRule arrayRule : arrayRules_target) {
				Map<String, List<ArrayRule>> bySameKeyField = map.get(arrayRule.getKeyField());
				if (bySameKeyField == null) {
					bySameKeyField = Maps.newHashMap();
					map.put(arrayRule.getKeyField(), bySameKeyField);
				}
				List<ArrayRule> bySameKeyValue = bySameKeyField.get(arrayRule.getKeyValue());
				if (bySameKeyValue == null) {
					bySameKeyValue = Lists.newArrayList();
					bySameKeyField.put(arrayRule.getKeyValue(), bySameKeyValue);
				}
				bySameKeyValue.add(arrayRule);
			}
			String json = JSON.toJSONString(map);
			String[] editorOptions = new String[] { json };
			dto.setEditorOptions(editorOptions);
		}
	}

	public boolean isDeletable(String tableB, TableEntity t) {
		// 去Constraint表取，所有bname列和参数tableB完全匹配的所有记录（不只是数量）。若数量大于0，说明有这样的配置（但这一行记录不一定被关联），此时得知aname，apos，bpos。
		List<Constraint> findByTb = tablesDao.getAll(Constraint.class, Restrictions.eq("bname", tableB));
		if (findByTb.size() > 0) {
			for (Constraint con : findByTb) {
				String tableA = con.getAname();
				String posA = con.getApos();
				String posB = con.getBpos();
				Object posBValue = null;
				try {
					posBValue = PropertyUtils.getProperty(t, posB);
				} catch (Exception e) {
					e.printStackTrace();
					// 异常说明t没有posB名字的字段，这可能是因为这条con的posB没有配正确，对判断关联没影响。
					continue;
				}
				Class<TableEntity> clazzA = EntityUtils.getMappedClass(tableA);
				Field posAField = ReflectionUtils.getDeclaredField(clazzA, posA);
				// 如果aname.apos不是数组，去aname表中查，如果aname.apos字段的值等于
				// t.bpos字段值的记录多于1条，说明被关联，不能删除
				if (!posAField.isAnnotationPresent(ArrayData.class)) {
					int count = tablesDao.count(clazzA, Restrictions.eq(posA, posBValue));
					if (count > 0) {
						return false;
					}
				}
				// 如果aname.apos是数组，去aname表中查，逐条分析是否存在aname.apos数组包含t.bpos值（并且二者是关联配置的），如果有，立即返回不能删除。
				else {
					// 根据con.getCode()和ArrayRule表中constraint_id相对应的那条，我们才能知道要查什么样的关键词。
					List<ArrayRule> keyWordsShouldBe = tablesDao.getAll(ArrayRule.class, Restrictions.eq("tableName", tableA), Restrictions.eq("constraint_id", con.getCode()));
					// 从keyWordsShouldBe任取一个，得到查询关键词。
					for (ArrayRule queryKeyWords : keyWordsShouldBe) {
						List<ArrayRule> rulesWeNeed = tablesDao.getAll(ArrayRule.class, Restrictions.eq("tableName", tableA), Restrictions.eq("keyField", queryKeyWords.getKeyField()), Restrictions
								.eq("keyValue", queryKeyWords.getKeyValue()), Restrictions.eq("targetField", posA));
						Collections.sort(rulesWeNeed, new Comparator<ArrayRule>() {
							// 根据数组下标排序rulesWeNeed。
							public int compare(ArrayRule o1, ArrayRule o2) {
								return (o1.getIdx() > o2.getIdx()) ? 1 : (o1.getIdx() == o2.getIdx()) ? 0 : -1;
							}
						});
						for (int i = 0; i < rulesWeNeed.size(); ++i) {
							if (con.getCode().equals(rulesWeNeed.get(i).getConstraint_id())) {
								// 数组若干个格中的哪一个下标
								int index = i;
								// 去aname找aname.apos的格式匹配
								Field targetField = ReflectionUtils.getDeclaredField(clazzA, con.getApos());
								String sqlRestriction = EntityUtils.makeContainRestriction(targetField.getAnnotation(Column.class).name(), rulesWeNeed.size(), index, String.valueOf(posBValue));
								Class<?> keyFieldType = ReflectionUtils.getDeclaredField(clazzA, queryKeyWords.getKeyField()).getType();
								int count = tablesDao.count(clazzA, Restrictions.eq(queryKeyWords.getKeyField(), ReflectionUtils.convertStringToObject(queryKeyWords.getKeyValue(), keyFieldType)),
										Restrictions.sqlRestriction(sqlRestriction));
								if (count > 0) {
									return false;
								}
							}
						}
					}
				}
			}
		}
		return true;
	}

	public BaseQEntity<TableEntity> getPage(Class<TableEntity> t, BaseQEntity<TableEntity> b, Map<String, Object> searchParams) {
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		Specification<TableEntity> specification = DynamicSpecifications.bySearchFilter(filters.values(), t);
		b = tablesDao.getPage(t, b, specification);
		return b;
	}

	public String downloadContent(Class<TableEntity> t) {
		List<Field> declaredFields = ReflectionUtils.getDeclaredFields(t);
		// 获得所有数组的field
		Map<Field, Long> arrayFields = Maps.newHashMap();
		for (Field field : declaredFields) {
			if (field.isAnnotationPresent(ArrayData.class)) {
				long fieldMax = tablesDao.count(t, field);
				arrayFields.put(field, fieldMax);
			}
		}
		StringBuffer spellTitle = EntityUtils.spellTitle(t, declaredFields, arrayFields);
		List<TableEntity> data = tablesDao.getAll(t);
		StringBuffer spellContent = EntityUtils.spellContent(t, declaredFields, arrayFields, data);

		return spellTitle.append(spellContent).toString();
	}

	public List<Tree> getTreeLayer(String packageName) {
		List<Tree> list = Lists.newArrayList();
		if (packageName == null) {
			String packageDesc = ClassUtils.findPackageInfo(EntityUtils.PACKAGE);
			Tree tree = new Tree(EntityUtils.PACKAGE, packageDesc, "closed");
			list.add(tree);
			return list;
		}
		Set<String> names;
		try {
			names = ClassUtils.getDirectChildrenNames(packageName);
			for (String f : names) {
				if (f.endsWith(".class")) { // Class
					String className = f.substring(0, f.indexOf(".class"));
					Class<?> c = Thread.currentThread().getContextClassLoader().loadClass(className);
					if (!c.isInterface() && TableEntity.class.isAssignableFrom(c)) {
						String tableName = c.getAnnotation(Table.class).name();
						String tableDesc = c.isAnnotationPresent(Comment.class) ? c.getAnnotation(Comment.class).desc() : c.getSimpleName();
						list.add(new Tree(tableName, tableDesc, "open"));
					}
				} else { // Package
					String packageDesc = ClassUtils.findPackageInfo(f);
					list.add(new Tree(f, packageDesc, "closed"));
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return list;
	}

	public List<String> getKeyOptions(String tableName, String field) {
		List<String> list = tablesDao.getAll(ArrayRule.class, Projections.property("keyValue"),Restrictions.eq("tableName", tableName), Restrictions.eq("keyField", field));
		return list;
	}
}
