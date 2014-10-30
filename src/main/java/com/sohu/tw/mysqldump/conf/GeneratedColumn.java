package com.sohu.tw.mysqldump.conf;

import org.apache.commons.digester.annotations.rules.BeanPropertySetter;
import org.apache.commons.digester.annotations.rules.ObjectCreate;

@ObjectCreate(pattern = "configuration/table-maps/table-map/source-table/columns/generated-column")
public class GeneratedColumn implements ColumnInterface {
	@BeanPropertySetter(pattern = "configuration/table-maps/table-map/source-table/columns/generated-column/fromColumns")
	private String fromColumns;
	@BeanPropertySetter(pattern = "configuration/table-maps/table-map/source-table/columns/generated-column/function")
	private String function;

	public String getFromColumns() {
		return this.fromColumns;
	}

	public void setFromColumns(String fromColumns) {
		this.fromColumns = fromColumns;
	}

	public String getFunction() {
		return this.function;
	}

	public void setFunction(String function) {
		this.function = function;
	}

	@BeanPropertySetter(pattern = "configuration/table-maps/table-map/source-table/columns/generated-column/type")
	private String type;

	@Override
	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String getName() {
		return "";
	}

	@Override
	public String toString() {
		return "GeneratedColumn [fromColumns=" + this.fromColumns
				+ ", function=" + this.function + ", type=" + this.type + "]";
	}

}
