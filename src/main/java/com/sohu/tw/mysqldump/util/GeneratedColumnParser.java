package com.sohu.tw.mysqldump.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

import com.sohu.tw.mysqldump.conf.ColumnMeta;
import com.sohu.tw.mysqldump.conf.GeneratedColumn;

public class GeneratedColumnParser {
	private static final String columnSeperator = ",";
	private String[] record;
	private Map<String, ColumnMeta> columnMap;

	public GeneratedColumnParser(String[] record,
			Map<String, ColumnMeta> columnMap) {
		this.record = record;
		this.columnMap = columnMap;

	}

	public String parse(GeneratedColumn c) {
		String[] columns = c.getFromColumns().split(columnSeperator);
		String[] values = new String[columns.length];
		for (int i = 0; i < columns.length; i++) {
			int valueIndex = columnMap.get(columns[i]).getOrginalPos() - 1;
			values[i] = Util.removeQuote(record[valueIndex]);
		}
		try {
			Method m = ParseMethods.class.getMethod(c.getFunction(),
					String[].class);
			return (String) m.invoke(null, new Object[] { values });
		} catch (SecurityException e) {
			throw new RuntimeException(e.getCause());
		} catch (NoSuchMethodException e) {
			throw new RuntimeException(e.getCause());
		} catch (IllegalArgumentException e) {
			throw new RuntimeException(e.getMessage(), e.getCause());
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e.getCause());
		} catch (InvocationTargetException e) {
			throw new RuntimeException(e.getCause());
		}
	}

}
