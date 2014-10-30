package com.sohu.tw.mysqldump.util;

import java.util.Date;

import org.apache.commons.lang.StringUtils;

public class ParseMethods {

	public static String getTimeKey(String[] time) {
		if (time.length < 1) {
			throw new IllegalArgumentException();
		}
		if (StringUtils.isNumeric(time[0])) {
			return Util.yyyyMMddHH_sdf.format(new Date(
					Long.parseLong(time[0]) * 1000));
		} else {
			try {
				return Util.yyyyMMddHH_sdf.format(Util.std_sdf.parse(time[0]));
			} catch (final Exception e) {
				return Util.yyyyMMddHH_sdf.format(new Date(0L));
			}
		}
	}

	public static String combine(String[] values) {
		if (values.length < 1) {
			throw new IllegalArgumentException();
		}
		StringBuffer buffer = new StringBuffer();
		buffer.append(values[0]);
		for (int i = 1; i < values.length; i++) {
			buffer.append('-');
			buffer.append(values[i]);
		}
		return buffer.toString();
	}

	public static String convertEmail(String values[]) {
		if (values == null || values.length == 0) {
			return null;
		}
		String value = values[0];
		if (value == null) {
			return value;
		}
		if (value.indexOf('@') == -1) {
			return value.concat("@chinaren.com");

		} else if (value.indexOf("@sms") > 0) {
			return value.concat(".sohu.com");

		} else if (value.indexOf("@sogou") > 0) {
			return value.concat(".com");

		} else if (value.indexOf("@sohu") > 0) {
			return value.concat(".com");

		} else if (value.indexOf("@vip") > 0) {
			return value.concat(".sohu.com");

		} else if (value.indexOf("@game") > 0) {
			return value.concat(".sohu.com");

		} else if (value.indexOf("@17173") > 0) {
			return value.concat(".com");

		} else if (value.indexOf("@focus") > 0) {
			return value.concat(".cn");

		} else if (value.indexOf("@sol") > 0) {
			return value.concat(".sohu.com");
		}
		return value;
	}
}
