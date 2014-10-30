package com.sohu.tw.mysqldump.conf;

import com.sohu.tw.mysqldump.util.Util;
import org.apache.commons.digester.annotations.rules.BeanPropertySetter;
import org.apache.commons.digester.annotations.rules.ObjectCreate;
import org.apache.commons.digester.annotations.rules.SetNext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ObjectCreate(pattern = "configuration/table-maps/table-map/source-table")
public class SourceTable {
	private final static Logger logger = LoggerFactory
			.getLogger(SourceTable.class);
	@BeanPropertySetter(pattern = "configuration/table-maps/table-map/source-table/schema")
	private String schema;
	@BeanPropertySetter(pattern = "configuration/table-maps/table-map/source-table/table-name")
	private String tableName;
	@BeanPropertySetter(pattern = "configuration/table-maps/table-map/source-table/host")
	private String host;
	@BeanPropertySetter(pattern = "configuration/table-maps/table-map/source-table/port")
	private int port = 3306;
	@BeanPropertySetter(pattern = "configuration/table-maps/table-map/source-table/user")
	private String user;
	@BeanPropertySetter(pattern = "configuration/table-maps/table-map/source-table/password")
	private String pwd;
	@BeanPropertySetter(pattern = "configuration/table-maps/table-map/source-table/charset")
	private String charset = "GBK";
	@BeanPropertySetter(pattern = "configuration/table-maps/table-map/source-table/query-field")
	private String queryField = "";
	private final List<ColumnInterface> columns = new ArrayList<ColumnInterface>();

	private Map<String, ColumnMeta> meta = null;

	public SourceTable() {
		this(null, null);
	}

	public SourceTable(String schema, String table) {
		this.schema = schema;
		this.tableName = table;
	}

	@SetNext
	public void addColumn(Column column) {
		this.columns.add(column);
	}

	@SetNext
	public void addColumn(GeneratedColumn column) {
		this.columns.add(column);
	}

	public void addColumn(ColumnInterface c) {
		this.columns.add(c);
	}

	public List<ColumnInterface> getColumns() {
		return this.columns;
	}

	/**
	 * @return the schema
	 */
	public String getSchema() {
		return this.schema;
	}

	/**
	 * @param schema
	 *            the schema to set
	 */
	public void setSchema(String schema) {
		this.schema = schema;
	}

	public String getHost() {
		return this.host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return this.port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getUser() {
		return this.user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPwd() {
		return this.pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getCharset() {
		return this.charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

	public String getQueryField() {
		return queryField;
	}

	public void setQueryField(String queryField) {
		this.queryField = queryField;
	}

	public synchronized Map<String, ColumnMeta> getMeta() {
		if (this.meta == null) {
			this.meta = SourceTable.this.getColumnMetas(this.schema,
					this.tableName);
		}
		return this.meta;
	}

	public String getTableName() {
		return this.tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((this.schema == null) ? 0 : this.schema.hashCode());
		result = prime * result
				+ ((this.tableName == null) ? 0 : this.tableName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (this.getClass() != obj.getClass())
			return false;
		final SourceTable other = (SourceTable) obj;
		if (this.schema == null) {
			if (other.schema != null)
				return false;
		} else if (!this.schema.equals(other.schema))
			return false;
		if (this.tableName == null) {
			if (other.tableName != null)
				return false;
		} else if (!this.tableName.equals(other.tableName))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "SourceTable [schema=" + this.schema + ", tableName="
				+ this.tableName + "]";
	}
    public static void main(String[] args) {
      SourceTable sourceTable = new SourceTable();
//      sourceTable.host = "10.10.58.68";
//      sourceTable.port = 3307;
//      sourceTable.user = "videoread";
//      sourceTable.pwd = "2sd5j4fcg0h";
//      sourceTable.getColumnMetas("videodb", "videoinfo");
        sourceTable.host = "10.11.152.153";
        sourceTable.port = 3308;
        sourceTable.user = "mvrs_api";
        sourceTable.pwd = "KjA3C6254b";
        sourceTable.getColumnMetas("mvrs", "video");
    }
	Map<String, ColumnMeta> getColumnMetas(String schema, String table) {
		Connection con = null;
		final Map<String, ColumnMeta> result = new HashMap<String, ColumnMeta>();
		try {
			con = DriverManager.getConnection("jdbc:mysql://" + this.getHost()
					+ ":" + this.getPort() + Util.PATH_SEPERATER + schema + "?"
					+ "user=" + this.getUser() + "&password=" + this.getPwd());

			final ResultSet rs = con.getMetaData().getColumns("", schema,
					table, "%");
			try {
				while (rs.next()) {
					final ColumnMeta meta = new ColumnMeta();
					meta.setTableCat(rs.getString("TABLE_CAT"));
					meta.setTableSchema(rs.getString("TABLE_SCHEM"));
					meta.setTablenName(rs.getString("TABLE_NAME"));
					meta.setColumnName(rs.getString("COLUMN_NAME"));
					meta.setDataType(rs.getInt("DATA_TYPE"));
					meta.setTypeName(rs.getString("TYPE_NAME"));
					meta.setColumnSize(rs.getInt("COLUMN_SIZE"));
					meta.setDescimalDigits(rs.getInt("DECIMAL_DIGITS"));
					meta.setRadix(rs.getInt("NUM_PREC_RADIX"));
					meta.setNullAble(rs.getInt("NULLABLE"));
					meta.setRemarks(rs.getString("REMARKS"));
					meta.setColumnDef(rs.getString("COLUMN_DEF"));
					meta.setCharOctetLen(rs.getInt("CHAR_OCTET_LENGTH"));
					meta.setOrginalPos(rs.getInt("ORDINAL_POSITION"));
					meta.setIsNullable(rs.getString("IS_NULLABLE"));
					meta.setSourceDataType(rs.getShort("SOURCE_DATA_TYPE"));
					meta.setIsAutoIncrement(rs.getString("IS_AUTOINCREMENT"));
					result.put(meta.getColumnName(), meta);
				}
			} finally {
				rs.close();
			}

		} catch (final SQLException e) {
			SourceTable.logger.error(e.getMessage(), e);
			System.exit(1);
		} finally {
			try {
				con.close();
			} catch (final SQLException e) {
				SourceTable.logger.error(e.getMessage(), e);
				System.exit(1);
			}
		}
		return result;
	}

	public boolean hasIndex() {
		return new File(Configuration.instance().getIndexDir()
				+ Util.PATH_SEPERATER + this.tableName + ".idx").exists();

	}

	public boolean hasIndexAndDel() {
		File file = new File(Configuration.instance().getIndexDir()
				+ Util.PATH_SEPERATER + this.tableName + ".idx");
		if (file.exists()) {
			file.delete();
			return true;
		} else {
			return false;
		}
	}

	public void saveTableIndex(long index) {
		hasIndexAndDel();
		OutputStreamWriter out = null;
		try {
			out = new OutputStreamWriter(new FileOutputStream(new File(
					Configuration.instance().getIndexDir()
							+ Util.PATH_SEPERATER + this.tableName + ".idx")));
			out.write(String.valueOf(index));
		} catch (final IOException e) {
			SourceTable.logger.error(
					"IO error ocoured when saving  the index file for the tailer:"
							+ this.tableName, e);
		} finally {
			try {
				if (out != null) {
					out.close();
				}
			} catch (final IOException e) {
				SourceTable.logger.error(
						"Failed to close the IO to save the index for the tailer:"
								+ this.tableName, e);
			}
		}
	}

	public String getTableMaxValueFromIndex() {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(new File(Configuration
					.instance().getIndexDir()
					+ Util.PATH_SEPERATER + this.tableName + ".idx")));
			final String index = reader.readLine();
			return index;
		} catch (final Exception e) {
			SourceTable.logger.info(e.getMessage());
		} finally {
			try {
				if (reader != null) {
					reader.close();
				}

			} catch (final IOException e) {
				SourceTable.logger.info(e.getMessage());
			}
		}
		return null;
	}
}
