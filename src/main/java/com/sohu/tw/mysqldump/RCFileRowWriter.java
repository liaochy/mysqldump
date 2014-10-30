package com.sohu.tw.mysqldump;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hive.ql.io.RCFile;
import org.apache.hadoop.hive.serde2.columnar.BytesRefArrayWritable;
import org.apache.hadoop.hive.serde2.columnar.BytesRefWritable;
import org.apache.hadoop.io.compress.CompressionCodec;
import org.apache.hadoop.util.ReflectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RCFileRowWriter implements DFSRowWriter {

	private final static Logger logger = LoggerFactory
			.getLogger(RCFileRowWriter.class);
	private final RCFile.Writer writer;
	private final Configuration conf;
	private final FileSystem fs;

	public RCFileRowWriter(boolean isCompressed, Path path, int columnNumber)
			throws IOException {
		if (path == null) {
			throw new IllegalArgumentException("The path must not be null");
		}
		this.conf = new Configuration();
		this.conf.setInt(RCFile.COLUMN_NUMBER_CONF_STR, columnNumber);
		this.fs = FileSystem.get(this.conf);
		final Path hdfsPath = path.makeQualified(this.fs);
		if (isCompressed) {
			this.writer = new RCFile.Writer(FileSystem.get(this.conf),
					this.conf, hdfsPath, null, this.getCompressionCodec());
		} else {
			this.writer = new RCFile.Writer(FileSystem.get(this.conf),
					this.conf, hdfsPath);
		}
	}

	public RCFileRowWriter(boolean isCompressed, String path, int columnNumber)
			throws IOException {
		this(isCompressed, new Path(path), columnNumber);
	}

	@Override
	public void write(String[] values) throws IOException {
		if (values == null) {
			throw new IllegalArgumentException(
					"The values must not be null value");
		}
		final BytesRefArrayWritable braw = new BytesRefArrayWritable(
				values.length);
		for (int i = 0; i < values.length; i++) {
			final String v = values[i];
			final BytesRefWritable brw = new BytesRefWritable();
			final byte[] data = v.getBytes();
			brw.set(data, 0, data.length);
			braw.set(i, brw);
		}
		this.writer.append(braw);

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

	@Override
	public void close() {
		if (this.writer != null) {
			try {
				this.writer.close();
			} catch (final IOException e) {
				RCFileRowWriter.logger.error(e.getMessage(), e);
			}
		}
	}

}
