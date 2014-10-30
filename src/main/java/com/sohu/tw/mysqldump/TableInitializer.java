package com.sohu.tw.mysqldump;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hdfs.DFSClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sohu.tw.mysqldump.conf.SourceTable;
import com.sohu.tw.mysqldump.conf.TableMapper;
import com.sohu.tw.mysqldump.util.Util;

public class TableInitializer {
	private final static Logger logger = LoggerFactory
			.getLogger(TableInitializer.class);

	private final TableMapper mapper;
	private final int columnNumber;

	public TableInitializer(TableMapper mapper) throws IOException {
		if (mapper == null) {
			throw new IllegalArgumentException();
		}
		this.mapper = mapper;
		this.columnNumber = mapper.getSourceTable().getColumns().size();

	}

	public void initialize() throws IOException {
		final SourceTable st = this.mapper.getSourceTable();
		String queryField = st.getQueryField();
		String path = this.mapper.getTablePath() + Util.PATH_SEPERATER
				+ st.getTableName();
		final DFSClient client = new DFSClient(new Configuration());
		try {
			TableInitializer.logger.info("start to initialize table:"
					+ this.mapper.getTableName());
			if ("".equals(queryField) || !st.hasIndex()) {
				if (client.exists(this.mapper.getTablePath())) {
					client.delete(this.mapper.getTablePath(), true);
				}
			} else {
				path = this.mapper.getTablePath() + Util.PATH_SEPERATER
						+ System.currentTimeMillis();
			}
		} finally {
			client.close();
		}
		final DFSRowWriter writer = DFSRowWriterFactory.create(path,
				this.mapper, this.columnNumber);
		final SourceTableDumper dumper = new SourceTableDumper(st, writer);
		try {
			TableInitializer.logger.info("start to dump the source table:"
					+ st.toString());
			dumper.dump();
			TableInitializer.logger.info("end to dump the source table:"
					+ st.toString());
		} finally {
			writer.close();
		}
		TableInitializer.logger.info("end to initialize table:"
				+ this.mapper.getTableName());

	}
}
