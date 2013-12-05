package com.gserver.data.editor.util;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.DateConverter;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.gserver.data.editor.TableEntity;
import com.gserver.data.editor.annotation.ArrayData;
import com.gserver.data.editor.annotation.Comment;
import com.gserver.data.editor.dto.TableTitle;

public class EntityUtils {
	public static final String PACKAGE = "com.gserver.data.editor.entity";

	public static final String SAPCE = "\t";
	public static final String ENTER = "\r\n";
	/**
	 * 供{@link #getMappedClass}方法使用。
	 */
	private static ConcurrentMap<String, Class<TableEntity>> nameToClass = Maps.newConcurrentMap();

	/**
	 * 通过表名找对应的{@code TableEntity}实现类。
	 * 
	 * @param tableName
	 *            表名
	 * @return {@code TableEntity}实现类，或{@code null}。
	 */
	public static Class<TableEntity> getMappedClass(String tableName) {
		Class<TableEntity> clazz = nameToClass.get(tableName);
		if (clazz != null)
			return clazz;

		List<Class<TableEntity>> classes = Lists.newArrayList();
		try {
			classes.addAll(ClassUtils.getSubClasses(PACKAGE, TableEntity.class));
			for (Class<TableEntity> claz : classes) {
				if (claz.isInterface()) {
					continue;
				}
				Table tableAnnotation = claz.getAnnotation(Table.class);
				if (tableAnnotation.name().equals(tableName)) {
					nameToClass.putIfAbsent(tableName, claz);
					return claz;
				}
			}
		} catch (IOException e) {
			// This IOException should never been thrown.
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// This ClassNotFoundException should never been thrown either!
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 供{@link #getTableEntity}方法使用。
	 */
	static {
		DateConverter d = new DateConverter();
		String[] datePattern = { "yyyy-mm-dd", "yyyy/mm/dd", "yyyy.mm.dd" };
		d.setPatterns(datePattern);
		ConvertUtils.register(d, java.util.Date.class);
	}

	/**
	 * 根据表名和属性参数{@code Map}生成一个{@code TableEntity}实现类。
	 * <p>
	 * <em>警告</em>：如果传入参数{@code Map}因类型不匹配，不能被正确解析到{@code TableEntity}
	 * 实现类同名字段时，此方法不会给这些字段赋值，而<em>不抛出异常</em>。
	 * 
	 * @param table
	 *            表实例
	 * @param paramMap
	 *            参数映射。
	 * @return 已赋值（但可能不完全）的{@code tablename}对应{@code TableEntity}实现类，或{@code
	 *         null}。
	 */
	public static TableEntity getTableEntity(TableEntity entity, Map<String, String[]> paramMap) {
		if (entity != null) {
			try {
				BeanUtils.populate(entity, paramMap); // TODO 这里没有查出错误的数据输入
				return entity;
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * 根据表名和字段名得到这一列数据缺省值。
	 * <p>
	 * 主要是以POJO字段类型分类，也会参考字段上的注解。
	 * 
	 * @param table
	 *            表名
	 * @param fieldName
	 *            POJO字段名
	 * @return 数据字段缺省值。
	 */
	public static String getDefaultLiteral(String table, String fieldName) {
		Class<TableEntity> entityClass = getMappedClass(table);
		Field field;
		try {
			field = entityClass.getDeclaredField(fieldName);
		} catch (SecurityException e) {
			e.printStackTrace();
			return "";
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
			return "";
		}
		Class<?> fieldType = field.getType();
		if (fieldType == boolean.class || fieldType == Boolean.class) {
			return "false";
		} else if (fieldType == char.class || fieldType == Character.class) {
			return "";
		} else if (fieldType == byte.class || fieldType == short.class || fieldType == int.class || fieldType == long.class || fieldType == float.class || fieldType == double.class
				|| Number.class.isAssignableFrom(fieldType)) {
			return "-1";
		} else if (Date.class.isAssignableFrom(fieldType)) {
			return "-1";
		} else if (fieldType.isArray() || Collection.class.isAssignableFrom(fieldType)) {
			return "";
		} else if (fieldType.isEnum()) {
			return "";
		} else {
			return "";
		}
	}

	/**
	 * 给{@code TableTitleDto}的@{@code editorType}字段赋参数。
	 * 
	 * @param dto
	 *            被赋值对象
	 * @param field
	 *            字段类
	 * @param editorOptionsForeign
	 *            查出的关联表配置参数
	 */
	public static void setEditorType(TableTitle dto, Field field, String[] editorOptionsForeign) {
		Class<?> fieldClass = field.getType();
		// NoEditor
		if (field.isAnnotationPresent(Id.class) && field.isAnnotationPresent(GeneratedValue.class) && field.getAnnotation(GeneratedValue.class).strategy() == GenerationType.AUTO) {
			dto.setEditorType(EditorType.NoEditor);
			return;
		}
		// Array
		if (fieldClass == String.class && field.isAnnotationPresent(ArrayData.class)) {
			dto.setEditorType(EditorType.Array);
			return;
		}
		// Foreign
		if (editorOptionsForeign != null) {
			dto.setEditorType(EditorType.Foreign);
			dto.setEditorOptions(editorOptionsForeign);
			return;
		}
		// J_String
		if (fieldClass == String.class) {
			dto.setEditorType(EditorType.J_String);
			return;
		}
		// J_Byte
		if (fieldClass == Byte.class || fieldClass == byte.class) {
			dto.setEditorType(EditorType.J_Byte);
			return;
		}
		// J_Integer
		if (fieldClass == Integer.class || fieldClass == int.class) {
			dto.setEditorType(EditorType.J_Integer);
			return;
		}
		// J_Short
		if (fieldClass == Short.class || fieldClass == short.class) {
			dto.setEditorType(EditorType.J_Short);
			return;
		}
		// J_Long
		if (fieldClass == Long.class || fieldClass == long.class) {
			dto.setEditorType(EditorType.J_Long);
			return;
		}
		// J_Float
		if (fieldClass == Float.class || fieldClass == float.class) {
			dto.setEditorType(EditorType.J_Float);
			return;
		}
		// J_Double
		if (fieldClass == Double.class || fieldClass == double.class) {
			dto.setEditorType(EditorType.J_Double);
			return;
		}
		// 默认最通用的J_String
		dto.setEditorType(EditorType.J_String);
	}

	public static StringBuffer spellTitle(Class<TableEntity> t, List<Field> declaredFields, Map<Field, Long> arrayFields) {
		// 拼字段名
		StringBuffer sb = new StringBuffer();
		sb.append("/");
		for (Field field : declaredFields) {
			Comment annotation = field.getAnnotation(Comment.class);
			sb.append(annotation.desc());
			Long max = arrayFields.get(field);
			if (max == null) {
				sb.append(SAPCE);
			} else {
				for (int i = 0; i < max; i++) {
					sb.append(SAPCE);
				}
			}
		}
		sb.append(ENTER);
		sb.append(";");
		for (Field field : declaredFields) {
			sb.append(field.getName());
			Long max = arrayFields.get(field);
			if (max == null) {
				sb.append(SAPCE);
			} else {
				for (int i = 0; i < max; i++) {
					sb.append(SAPCE);
				}
			}

		}
		sb.append(ENTER);

		return sb;
	}

	public static StringBuffer spellContent(Class<TableEntity> t, List<Field> declaredFields, Map<Field, Long> arrayFields, List<TableEntity> data) {

		StringBuffer sb = new StringBuffer();
		for (TableEntity tableEntity : data) {
			for (Field field : declaredFields) {
				Object fieldValue = ReflectionUtils.getFieldValue(tableEntity, field.getName());
				Long max = arrayFields.get(field);
				if (max == null) {
					sb.append(fieldValue).append(SAPCE);
				} else {
					String[] v = String.valueOf(fieldValue).split(",");
					for (String string : v) {
						sb.append(string).append(SAPCE);
					}
					if (v.length < max) {
						for (int i = v.length + 1; i < max; i++) {
							sb.append(SAPCE);
						}
					}
				}
			}
			sb.append(ENTER);
		}
		return sb;
	}

	/**
	 * 得到数据库所有的表名称。
	 * 
	 * @return 数据库所有的表名称。
	 */
	public static List<String[]> getTableNames() {
		List<String[]> list = Lists.newArrayList();
		try {
			List<Class<TableEntity>> classes = Lists.newArrayList();
			classes.addAll(ClassUtils.getSubClasses(PACKAGE, TableEntity.class));
			for (Class<TableEntity> class1 : classes) {
				if (class1.isInterface()) {
					continue;
				}
				Comment title = class1.getAnnotation(Comment.class);
				Table table = class1.getAnnotation(Table.class);
				String[] transfer = new String[] { table.name(), title != null ? title.desc() : table.name() };
				list.add(transfer);
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 得到{@code TableEntity}实现类对应的表格简单标题。
	 * <p>
	 * 返回值只设置了字段名{@link TableTitle#name}和中文描述{@link TableTitle#comment}
	 * ，其他信息未赋值。
	 * 
	 * @param entityClass
	 *            {@code TableEntity}实现类型
	 * @return 此类所有字段对应的简单字段标题列表。
	 */
	public static List<TableTitle> getSimpleTitles(Class<TableEntity> entityClass) {
		List<TableTitle> titles = Lists.newArrayList();
		Field[] declaredFields = entityClass.getDeclaredFields();
		for (Field field : declaredFields) {
			String propName = field.getName();
			String comment = null;
			String search = "";
			Comment annotation = field.getAnnotation(Comment.class);
			if (annotation != null) {
				comment = annotation.desc();
				search = annotation.search();
			} else {
				comment = propName;
			}
			TableTitle dto = new TableTitle(propName, comment, search, null, null); // 暂时不绑定editor和option，option在上一层中绑定。
			titles.add(dto);
		}
		return titles;
	}

	/**
	 * 根据要找的数组型字段名，数组列数，被匹配关键字在数组中的下标位置，和被查的值，生成一个{@code SQL}正则表达式{@code WHERE}
	 * 条件。
	 * 
	 * @param fieldNameInSQL
	 *            数据库中存的字段名
	 * @param idxSize
	 *            数组列数
	 * @param index
	 *            被匹配关键字在数组中的下标
	 * @param thatValue
	 *            被查的值
	 * @return {@code SQL}正则表达式{@code WHERE}条件。
	 */
	public static String makeContainRestriction(String fieldNameInSQL, int idxSize, int index, String thatValue) {
		// 生成regexs正则条件
		String wildcard = "[^,]*";
		StringBuilder groupFormat = new StringBuilder();
		int i = idxSize;
		groupFormat.append(wildcard);
		while (--i > 0) {
			groupFormat.append(',').append(wildcard);
		}
		String unvaluedGroup = groupFormat.toString();
		groupFormat.setLength(0);
		groupFormat.append(index == i++ ? thatValue : wildcard);
		while (i < idxSize) {
			groupFormat.append(',').append(index == i++ ? thatValue : wildcard);
		}
		String valuedGroup = groupFormat.toString();
		StringBuilder sb = new StringBuilder();
		String one = sb.append('^').append(valuedGroup).append('$').toString();
		sb.setLength(0);
		String beginning = sb.append('^').append(valuedGroup).append("(,").append(unvaluedGroup).append(")+$").toString();
		sb.setLength(0);
		String end = sb.append("^(").append(unvaluedGroup).append(",)+").append(valuedGroup).append('$').toString();
		sb.setLength(0);
		String middle = sb.append("^(").append(unvaluedGroup).append(",)+(").append(valuedGroup).append(")+(,").append(unvaluedGroup).append(")*$").toString();
		String[] regexs = new String[] { one, beginning, end, middle };
		// 根据regexs参数碎片，拼成WHERE条件
		sb.setLength(0);
		sb.append('`').append(fieldNameInSQL).append("` REGEXP \"").append(regexs[0]).append("\" OR `").append(fieldNameInSQL).append("` REGEXP \"").append(regexs[1]).append("\" OR `").append(
				fieldNameInSQL).append("` REGEXP \"").append(regexs[2]).append("\" OR `").append(fieldNameInSQL).append("` REGEXP \"").append(regexs[3]).append('\"');
		return sb.toString();
	}

}
