package com.sohu.tw.mysqldump.conf;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.digester.Digester;
import org.apache.commons.digester.annotations.DigesterLoader;
import org.apache.commons.digester.annotations.DigesterLoaderBuilder;
import org.apache.commons.digester.annotations.rules.BeanPropertySetter;
import org.apache.commons.digester.annotations.rules.ObjectCreate;
import org.apache.commons.digester.annotations.rules.SetNext;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Configuration 是一个单例类，由于 digester lib需要反射它的构造函数，所以没有设置为private的构造函数。
 * 所以请不要使用构造函数而是使用 instance方法来构造对象。
 * 
 * @author sunhengxin
 * 
 */
@ObjectCreate(pattern = "configuration")
final public class Configuration {
	private final static Logger logger = LoggerFactory
			.getLogger(Configuration.class);

	private static URL CONF_URL;

	private static Configuration configuration;
	static {
		ClassLoader cL = Thread.currentThread().getContextClassLoader();
		if (cL == null) {
			cL = Configuration.class.getClassLoader();

		}
		if (cL.getResource("conf.xml") != null) {
			Configuration.CONF_URL = cL.getResource("conf.xml");
		} else {
			Configuration.logger.error("Can't find the conf.xml file;vm exits");
			System.exit(1);
		}
	}
	private final List<TableMapper> tableMappers = new ArrayList<TableMapper>();

	@BeanPropertySetter(pattern = "configuration/mysql-home")
	private String mysqlHome;
	private String indexDir;

	private final String home;

	public String getMysqlHome() {
		return this.mysqlHome;
	}

	public void setMysqlHome(String mysqlHome) {
		this.mysqlHome = mysqlHome;
	}


	public String getIndexDir() {
		return this.indexDir;
	}

	public void setIndexDir(String dir) {
		this.indexDir = dir;
	}


	@SetNext
	public void addTableMapper(TableMapper tailer) {
		this.tableMappers.add(tailer);
	}

	public List<TableMapper> getTableMappers() {
		return this.tableMappers;
	}

	public void validate() {
		if (!this.isValidDir(this.getIndexDir())) {
			Configuration.logger.error("The index dir is not correct;vm exits:"
					+ this.getIndexDir());
			System.exit(1);
		}
	}

	private boolean isValidDir(String s) {
		if (StringUtils.isBlank(s)) {
			return false;
		}
		return new File(s).isDirectory();
	}


	public static synchronized Configuration instance() {
		if (Configuration.configuration == null) {
			final DigesterLoader digesterLoader = new DigesterLoaderBuilder()
					.useDefaultAnnotationRuleProviderFactory()
					.useDefaultDigesterLoaderHandlerFactory();
			final Digester digester = digesterLoader
					.createDigester(Configuration.class);
			try {
				Configuration.configuration = (Configuration) digester
						.parse(Configuration.CONF_URL.openStream());
				Configuration.configuration.validate();
			} catch (final Exception e) {
				Configuration.logger.error(
						"The index dir is not correct;vm exits", e);
				System.exit(1);
			}

		}
		return Configuration.configuration;
	}

	public Configuration() {
		this.home = System.getenv("MYSQLDUMP_HOME");
		if (StringUtils.isBlank(this.home)) {
			throw new RuntimeException("Can't found the BEEKEEPER_HOME in env");
		} else {
			this.indexDir = this.home + "/index";
		}
		Configuration.logger.info("My home :" + this.home);
	}
	public String getHome() {
		return home;
	}

	public static void main(String arg[]) {

		Configuration c = Configuration.instance();
		System.out.println(c.getTableMappers().get(0).getTableName());
	}

}
