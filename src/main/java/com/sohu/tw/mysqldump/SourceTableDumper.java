package com.sohu.tw.mysqldump;

import com.sohu.tw.mysqldump.RecordParser.ParseError;
import com.sohu.tw.mysqldump.conf.*;
import com.sohu.tw.mysqldump.util.GeneratedColumnParser;
import com.sohu.tw.mysqldump.util.Util;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;
import org.apache.commons.exec.PumpStreamHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.CharBuffer;
import java.sql.Types;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class SourceTableDumper {
    private final static Logger logger = LoggerFactory
            .getLogger(SourceTableDumper.class);
    private final SourceTable st;
    private final DFSRowWriter writer;
    private long index;

    public SourceTableDumper(SourceTable table, DFSRowWriter writer) {
        if (table == null) {
            throw new IllegalArgumentException(
                    "The table must not be null value");
        }
        if (writer == null) {
            throw new IllegalArgumentException(
                    "The writer must not be null value");
        }
        this.st = table;
        this.writer = writer;
        if (!"".equals(st.getQueryField())) {
            if (st.hasIndex()) {
                index = Long.valueOf(st.getTableMaxValueFromIndex());
            } else {
                index = 0;
                st.saveTableIndex(index);
            }
        }
    }

    public void dump() {
        try {
            final DefaultExecutor executor = new DefaultExecutor();
            final PipedOutputStream pos = new PipedOutputStream();
            final PipedInputStream pin = new PipedInputStream(pos);
            executor.setStreamHandler(new PumpStreamHandler(pos));
            final RowProcesser proc = new RowProcesser(pin);
            proc.start();
            SourceTableDumper.logger.info(this.getCmd().toString());
            executor.execute(this.getCmd());
            proc.join();
            st.saveTableIndex(index);
        } catch (final ExecuteException e) {
            throw new RuntimeException(e.getMessage(), e.getCause());
        } catch (final IOException e) {
            throw new RuntimeException(e.getMessage(), e.getCause());
        } catch (final InterruptedException e) {
            throw new RuntimeException(e.getMessage(), e.getCause());
        }
    }

    private CommandLine getCmd() {
        final CommandLine cli = new CommandLine(Configuration.instance()
                .getMysqlHome()
                + "/bin/mysqldump");
        cli.addArgument("--host=" + this.st.getHost());
        cli.addArgument("--user=" + this.st.getUser());
        cli.addArgument("--password=" + this.st.getPwd());
        cli.addArgument("--port=" + this.st.getPort());
        cli.addArgument("--skip-opt");
        cli.addArgument("--compact");
        cli.addArgument("--no-create-db");
        cli.addArgument("--no-create-info");
        cli.addArgument("--skip-tz-utc");
        cli.addArgument("--quick");
        cli.addArgument("--single-transaction");
        cli.addArgument("--set-gtid-purged=OFF");
        cli.addArgument(this.st.getSchema(), false);
        cli.addArgument(this.st.getTableName(), false);
        if (!"".equals(st.getQueryField())) {
            String id = st.getQueryField();
            StringBuffer str = new StringBuffer("--where=");
            str.append(id);
            str.append(">=");
            str.append(st.getTableMaxValueFromIndex());
            cli.addArgument(str.toString());
        }
        return cli;

    }

    private String[] cleanRecord(String[] record) {
        final List<ColumnInterface> colums = this.st.getColumns();
        final Map<String, ColumnMeta> columnMap = this.st.getMeta();

        if (colums.isEmpty()) {
            return record;

        } else {
            final GeneratedColumnParser gcp = new GeneratedColumnParser(record,
                    columnMap);
            final List<String> array = new ArrayList<String>();
            for (final ColumnInterface c : colums) {
                final String name = c.getName();
                final ColumnMeta meta = columnMap.get(name);
                if (c instanceof GeneratedColumn) {
                    array.add(gcp.parse((GeneratedColumn) c));
                } else if (meta.getDataType() == Types.TIMESTAMP) {
                    try {
                        array.add(Util.yyyyMMddHHmmss_sdf.format(Util.std_sdf
                                .parse(record[meta.getOrginalPos() - 1])));
                    } catch (final ParseException e) {
                        array.add(Util.yyyyMMddHHmmss_sdf.format(new Date(0L)));
//						SourceTableDumper.logger.error(e.getMessage(), e);
                    }
                } else if (Util.isNumericSQLType(meta.getDataType())) {
                    array.add(record[meta.getOrginalPos() - 1].split("\\s")[0]);
                } else {
                    array.add(record[meta.getOrginalPos() - 1]);
                }

            }
            if (!"".equals(st.getQueryField()) && array.size() > 0) {
                long max = Long.valueOf(array.get(0));
                if (max == index) {
                    return new String[]{};
                }
                if (max > index) {
                    index = max;
                }
            }
            return array.toArray(new String[]{});
        }
    }

    /**
     * @author sunhengxin
     */
    private class RowProcesser extends Thread {

        private final InputStream in;
        private final RecordParser parser = new RecordParser();

        public RowProcesser(InputStream in) {
            if (in == null) {
                throw new NullPointerException();
            }
            this.setDaemon(true);
            this.in = in;
        }

        public void run() {
            BufferedReader bu = null;
            try {
                bu = new BufferedReader(new InputStreamReader(this.in, "UTF-8"));
                int preambleLen = -1;
                while (true) {
                    final String line = bu.readLine();
                    if (line == null) {
                        break;
                    }
                    final String recordStartMark = "VALUES (";
                    preambleLen = line.indexOf(recordStartMark);

                    if (preambleLen == -1) {
                        continue;
                    }
                    final CharBuffer charbuf = CharBuffer.wrap(line,
                            preambleLen + recordStartMark.length(), line.length() - 2);
                    try {
                        final List<String> values = this.parser
                                .parseRecord(charbuf);
                        String[] record = SourceTableDumper.this
                                .cleanRecord(values.toArray(new String[]{}));
                        if (record.length > 0) {
                            SourceTableDumper.this.writer.write(record);
                        }
                    } catch (final ParseError e) {
                        SourceTableDumper.logger.error(e.getMessage() + ":"
                                + line, e);
                    }
                }

            } catch (final IOException e) {
                if (e.getMessage().equals("Write end dead")) {
                } else {
                    SourceTableDumper.logger.error(e.getMessage(), e);
                }
            } finally {
                try {
                    if (bu != null) {
                        bu.close();
                    }
                } catch (final IOException e) {
                    SourceTableDumper.logger.error(e.getMessage(), e);
                }
            }
        }
    }
}
