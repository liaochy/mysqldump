package com.sohu.tw.mysqldump;

import java.io.IOException;

import com.sohu.tw.mysqldump.conf.TableMapper;

public class DFSRowWriterFactory {

	public static DFSRowWriter create(String path, TableMapper mapper,int columnNumber)
			throws IOException {
		String fileType = mapper.getFileType();
		if (fileType.equals("rcf")) {
			return new RCFileRowWriter(mapper.isCompressed(), path,
					columnNumber);
		} else if (fileType.equalsIgnoreCase("text")) {
			return new TextFileRowWriter(mapper.isCompressed(), path);
		} else if (fileType.equalsIgnoreCase("seq")) {
			throw new UnsupportedOperationException(
					"unsupport the seq dfs row writer:" + fileType);
		} else {
			throw new UnsupportedOperationException("Unknow dfs row writer:"
					+ fileType);
		}
	}

}
