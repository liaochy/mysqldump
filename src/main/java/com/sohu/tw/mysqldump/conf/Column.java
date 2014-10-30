package com.sohu.tw.mysqldump.conf;

import org.apache.commons.digester.annotations.rules.BeanPropertySetter;
import org.apache.commons.digester.annotations.rules.ObjectCreate;

@ObjectCreate(pattern = "configuration/table-maps/table-map/source-table/columns/column")
public class Column implements ColumnInterface {
	@BeanPropertySetter(pattern = "configuration/table-maps/table-map/source-table/columns/column/name")
	private String name;
	@BeanPropertySetter(pattern = "configuration/table-maps/table-map/source-table/columns/column/type")
	private String type;

	@Override
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
