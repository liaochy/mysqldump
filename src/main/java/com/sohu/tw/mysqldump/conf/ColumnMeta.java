package com.sohu.tw.mysqldump.conf;

public class ColumnMeta {

	// # TABLE_CAT String => table catalog (may be null)
	private String tableCat;
	// # TABLE_SCHEM String => table schema (may be null)
	private String tableSchema;
	// # TABLE_NAME String => table name
	private String tablenName;
	// # COLUMN_NAME String => column name
	private String columnName;
	// # DATA_TYPE int => SQL type from java.sql.Types
	private int dataType;
	// # TYPE_NAME String => Data source dependent type name, for a UDT the type
	// name is fully qualified
	private String typeName;
	// # COLUMN_SIZE int => column size.
	private int columnSize;
	// # DECIMAL_DIGITS int => the number of fractional digits. Null is returned
	// for data types where DECIMAL_DIGITS is not applicable.
	private int descimalDigits;
	// # NUM_PREC_RADIX int => Radix (typically either 10 or 2)
	private int radix;
	// # NULLABLE int => is NULL allowed.
	private int nullAble;
	// # REMARKS String => comment describing column (may be null)
	private String remarks;
	// # COLUMN_DEF String => default value for the column, which should be
	// interpreted as a string when the value is enclosed in single quotes (may
	// be null)
	private String columnDef;
	// # CHAR_OCTET_LENGTH int => for char types the maximum number of bytes in
	// the column
	private int charOctetLen;
	// # ORDINAL_POSITION int => index of column in table (starting at 1)
	private int orginalPos;
	// # IS_NULLABLE String => ISO rules are used to determine the nullability
	// for a column.
	private String isNullable;
	// # SOURCE_DATA_TYPE short => source type of a distinct type or
	// user-generated Ref type, SQL type from java.sql.Types (null if DATA_TYPE
	// isn't DISTINCT or user-generated REF)
	private short sourceDataType;
	// # IS_AUTOINCREMENT String => Indicates whether this column is auto
	// incremented
	private String isAutoIncrement;

	public String getTableCat() {
		return this.tableCat;
	}

	public void setTableCat(String tableCat) {
		this.tableCat = tableCat;
	}

	public String getTableSchema() {
		return this.tableSchema;
	}

	public void setTableSchema(String tableSchema) {
		this.tableSchema = tableSchema;
	}

	public String getTablenName() {
		return this.tablenName;
	}

	public void setTablenName(String tablenName) {
		this.tablenName = tablenName;
	}

	public int getDataType() {
		return this.dataType;
	}

	public void setDataType(int dataType) {
		this.dataType = dataType;
	}

	public String getTypeName() {
		return this.typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public int getColumnSize() {
		return this.columnSize;
	}

	public void setColumnSize(int columnSize) {
		this.columnSize = columnSize;
	}

	public int getDescimalDigits() {
		return this.descimalDigits;
	}

	public void setDescimalDigits(int descimalDigits) {
		this.descimalDigits = descimalDigits;
	}

	public int getRadix() {
		return this.radix;
	}

	public void setRadix(int radix) {
		this.radix = radix;
	}

	public int getNullAble() {
		return this.nullAble;
	}

	public void setNullAble(int nullAble) {
		this.nullAble = nullAble;
	}

	public String getRemarks() {
		return this.remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getColumnDef() {
		return this.columnDef;
	}

	public void setColumnDef(String columnDef) {
		this.columnDef = columnDef;
	}

	public int getCharOctetLen() {
		return this.charOctetLen;
	}

	public void setCharOctetLen(int charOctetLen) {
		this.charOctetLen = charOctetLen;
	}

	public int getOrginalPos() {
		return this.orginalPos;
	}

	public void setOrginalPos(int orginalPos) {
		this.orginalPos = orginalPos;
	}

	public String getIsNullable() {
		return this.isNullable;
	}

	public void setIsNullable(String isNullable) {
		this.isNullable = isNullable;
	}

	public short getSourceDataType() {
		return this.sourceDataType;
	}

	public void setSourceDataType(short sourceDataType) {
		this.sourceDataType = sourceDataType;
	}

	public String getIsAutoIncrement() {
		return this.isAutoIncrement;
	}

	public void setIsAutoIncrement(String isAutoIncrement) {
		this.isAutoIncrement = isAutoIncrement;
	}

	public String getColumnName() {
		return this.columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

}
