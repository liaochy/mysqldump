package com.sohu.tw.mysqldump;

import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.compress.CompressionCodec;
import org.apache.hadoop.util.ReflectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sohu.tw.mysqldump.util.Util;

public class TextFileRowWriter implements DFSRowWriter {
	private final static Logger logger = LoggerFactory
			.getLogger(TextFileRowWriter.class);
	private final Configuration conf;
	private final FileSystem fs;
	private final OutputStream out;

	public TextFileRowWriter(boolean isCompressed, String path)
			throws IOException {
		if (StringUtils.isBlank(path)) {
			throw new IllegalArgumentException("The path must not be blank");
		}
		this.conf = new Configuration();
		this.fs = FileSystem.get(this.conf);
		final Path hdfsPath = new Path(path).makeQualified(this.fs);
		this.out = this.getCompressionCodec().createOutputStream(
				this.fs.create(hdfsPath, true));
	}

	@Override
	public void write(String[] values) throws IOException {
		if (values == null || values.length <= 0) {
			throw new IllegalArgumentException(
					"The values must not be null value");
		}
		final StringBuffer buffer = new StringBuffer();
		buffer.append(values[0]);
		if (values.length > 1) {
			for (int i = 1; i < values.length; i++) {
				buffer.append(Util.FIELDS_TERMINATED);
				buffer.append(values[i]);
			}
		}
		buffer.append(Util.LINE_TERMINATED);
		this.out.write(buffer.toString().getBytes());
	}

	@Override
	public void close() throws IOException {
		if (this.out != null) {
			try {
				this.out.close();
			} catch (final IOException e) {
				TextFileRowWriter.logger.error(e.getMessage(), e);
			}
		}
	}

	private CompressionCodec getCompressionCodec() {
		try {
			final Class<?> codec = Class.forName(this.conf
					.get(DFSRowWriter.CODEC_KEY));
			return (CompressionCodec) ReflectionUtils.newInstance(codec,
					this.conf);
		} catch (final ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

}
