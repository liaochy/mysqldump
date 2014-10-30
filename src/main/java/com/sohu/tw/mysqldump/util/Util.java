package com.sohu.tw.mysqldump.util;

import java.io.File;
import java.sql.Types;
import java.text.SimpleDateFormat;

import org.apache.commons.lang.StringUtils;

public class Util {
	public final static String FIELDS_TERMINATED = "\t";
	public final static String LINE_TERMINATED = "\n";
	public final static String PATH_SEPERATER = "/";
	public final static SimpleDateFormat yyyyMMddHH_sdf = new SimpleDateFormat(
			"yyyyMMddHH");
	public final static SimpleDateFormat yyyyMMddHHmmss_sdf = new SimpleDateFormat(
			"yyyyMMddHHmmss");
	public final static SimpleDateFormat std_sdf = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");

	public static boolean isNumericSQLType(int sqlType) {
		return Types.BIT == sqlType || Types.BIGINT == sqlType
				|| Types.DECIMAL == sqlType || Types.DOUBLE == sqlType
				|| Types.FLOAT == sqlType || Types.INTEGER == sqlType
				|| Types.NUMERIC == sqlType || Types.REAL == sqlType
				|| Types.SMALLINT == sqlType || Types.TINYINT == sqlType;
	}

	public static void delFolder(String folderPath) {
		try {
			Util.delAllFile(folderPath);
			String filePath = folderPath;
			filePath = filePath.toString();
			final File myFilePath = new File(filePath);
			myFilePath.delete();
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	public static boolean delAllFile(String path) {
		boolean flag = false;
		final File file = new File(path);
		if (!file.exists()) {
			return flag;
		}
		if (!file.isDirectory()) {
			return flag;
		}
		final String[] tempList = file.list();
		File temp = null;
		for (int i = 0; i < tempList.length; i++) {
			if (path.endsWith(File.separator)) {
				temp = new File(path + tempList[i]);
			} else {
				temp = new File(path + File.separator + tempList[i]);
			}
			if (temp.isFile()) {
				temp.delete();
			}
			if (temp.isDirectory()) {
				Util.delAllFile(path + Util.PATH_SEPERATER + tempList[i]);
				Util.delFolder(path + Util.PATH_SEPERATER + tempList[i]);
				flag = true;
			}
		}
		return flag;
	}

	public static String[] toStringArray(Object[] objs) {
		if (objs == null) {
			return null;
		}
		if (objs.length == 0) {
			return new String[] {};
		}
		String[] result = new String[objs.length];
		for (int i = 0; i < result.length; i++) {
			if (objs[i] != null) {
				result[i] = objs[i].toString();
			}

		}
		return result;
	}
	
	public static String removeQuote(String value) {
		if (value.length() > 1 && value.startsWith("'") && value.endsWith("'")) {
			return StringUtils.strip(value, "'");
		}
		return value;
	}

}
