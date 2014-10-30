package com.sohu.tw.mysqldump;

import java.io.IOException;

public interface DFSRowWriter {
	final String CODEC_KEY = "mapred.output.compression.codec";

	public void write(String[] values) throws IOException;

	public void close() throws IOException;

}
