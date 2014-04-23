package com.gserver.data.editor.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.util.List;

import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;

public class FileToEntityUtils {

	public static <T> List<T> resolve(Class<T> clazz, InputStream in) {
		List<T> returnList = Lists.newArrayList();
		try {
			// 第0行是注释，第1行是属性名，剩下行都是值
			List<List<String>> rows = readRows(clazz, in);
			List<String> titleRow = rows.get(1);
			List<String> attributeNames = getAttributeNames(titleRow);
			for (int i = 2; i < rows.size(); ++i) {
				List<String> valueRow = rows.get(i);
				T obj = clazz.newInstance();
				if (fillFields(obj, attributeNames, valueRow)) {
					returnList.add(obj);
				}
			}
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		}

		return returnList;
	}

	private static <T> List<List<String>> readRows(Class<T> clazz, InputStream input) {
		List<List<String>> rows = Lists.newArrayList();
		try {

			BufferedReader in = new BufferedReader(new InputStreamReader(input, "UTF-8"));
			String line;
			while ((line = in.readLine()) != null) {
				String[] values = line.split("\t"); // 注意，split会忽略末尾的\t
				List<String> row = Lists.newArrayList(values);
				rows.add(row);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return rows;
	}

	private static List<String> getAttributeNames(List<String> row) {
		List<String> ts = Lists.newArrayList();

		ts.add(row.get(0).substring(1)); // 去掉title行的起始标记
		for (int i = 1; i < row.size(); ++i) {
			ts.add(row.get(i).trim());
		}
		return ts;
	}

	private static <T> boolean fillFields(T obj, List<String> attributeNames, List<String> row) {
		String fieldName = null;
		for (int iterator = 0, range_begin = -1, range_end = -1; iterator < row.size(); ++iterator) {
			String attrName = null;
			if (iterator < attributeNames.size()) {
				attrName = attributeNames.get(iterator);
			}
			if (!Strings.isNullOrEmpty(attrName)) {
				fieldName = attrName;
				range_begin = iterator;
				range_end = iterator + 1;
			} else {// 数组后续空格
				range_end = iterator + 1;
			}
			if (iterator < attributeNames.size() - 1 && !Strings.isNullOrEmpty(attributeNames.get(iterator + 1)) || iterator == row.size() - 1) {
				fillOneField(obj, fieldName, row, range_begin, range_end);
			}
		}
		return true;
	}

	private static <T> void fillOneField(T obj, String attrName, List<String> row, int range_begin, int range_end) {
		try {
			Field field = ReflectionUtils.getDeclaredField(obj, attrName);
			if (field != null) {
				Class<?> type = field.getType();
				field.setAccessible(true);
				if (type.equals(int.class) || type.equals(Integer.class)) {
					field.set(obj, Integer.parseInt(row.get(range_begin).trim()));
				} else if (type.equals(double.class) || type.equals(Double.class)) {
					field.set(obj, Double.valueOf(row.get(range_begin).trim()));
				} else if (type.equals(boolean.class) || type.equals(Boolean.class)) {
					field.set(obj, Boolean.valueOf(row.get(range_begin).trim()));
				} else if (type.equals(short.class) || type.equals(Short.class)) {
					field.set(obj, Short.valueOf(row.get(range_begin).trim()));
				} else if (type.equals(byte.class) || type.equals(Byte.class)) {
					field.set(obj, Byte.valueOf(row.get(range_begin).trim()));
				} else if (type.equals(float.class) || type.equals(Float.class)) {
					field.set(obj, Float.valueOf(row.get(range_begin).trim()));
				} else if (type.equals(long.class) || type.equals(Long.class)) {
					field.set(obj, Long.valueOf(row.get(range_begin).trim()));
				} else {
					field.set(obj, Joiner.on(",").join(row.subList(range_begin, range_end)).replace('^', '\n').replace('~', ' '));
				}
			}
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e2) {
			e2.printStackTrace();
		}
	}

}
