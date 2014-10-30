package com.sohu.tw.mysqldump.conf;

import org.apache.commons.digester.annotations.rules.BeanPropertySetter;
import org.apache.commons.digester.annotations.rules.ObjectCreate;
import org.apache.commons.digester.annotations.rules.SetNext;

@ObjectCreate(pattern = "configuration/table-maps/table-map")
public class TableMapper {
	@BeanPropertySetter(pattern = "configuration/table-maps/table-map/name")
	private String tableName;
	@BeanPropertySetter(pattern = "configuration/table-maps/table-map/path")
	private String tablePath;
	@BeanPropertySetter(pattern = "configuration/table-maps/table-map/file-type")
	private String fileType = "text";
	@BeanPropertySetter(pattern = "configuration/table-maps/table-map/compressed")
	private boolean compressed;
	private  SourceTable sourceTable;


	public SourceTable getSourceTable() {
		return sourceTable;
	}
	@SetNext
	public void setSourceTable(SourceTable sourceTable) {
		this.sourceTable = sourceTable;
	}

	/**
	 * @return the tableMapper
	 */
	public String getTableName() {
		return this.tableName;
	}

	/**
	 * @param tableMapper
	 *            the tableMapper to set
	 */
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getTablePath() {
		return this.tablePath;
	}

	public void setTablePath(String tablePath) {
		this.tablePath = tablePath;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public boolean isCompressed() {
		return compressed;
	}

	public void setCompressed(boolean compressed) {
		this.compressed = compressed;
	}

}
