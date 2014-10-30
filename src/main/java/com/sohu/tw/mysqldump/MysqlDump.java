package com.sohu.tw.mysqldump;

import java.io.IOException;
import java.util.List;

import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sohu.tw.mysqldump.conf.TableMapper;

public class MysqlDump {
	private final static Logger logger = LoggerFactory
			.getLogger(MysqlDump.class);

	public static void main(String[] args) throws IOException,
			InterruptedException, ParseException {
		logger.info(System.getProperty("java.library.path"));
		MysqlDump.initialize();
	}

	private static void initialize() throws IOException {
		final List<TableMapper> mapper = com.sohu.tw.mysqldump.conf.Configuration
				.instance().getTableMappers();
		for (final TableMapper m : mapper) {
			new TableInitializer(m).initialize();
		}
	}

	public void stop() {

	}
}
