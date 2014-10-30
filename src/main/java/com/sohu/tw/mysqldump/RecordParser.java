package com.sohu.tw.mysqldump;

import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class RecordParser {

	public static final Logger LOG = LoggerFactory
			.getLogger(RecordParser.class);

	private static final char field = ',';
	private static final char record = '\n';
	private static final char enclose = '\'';
	private static final char escape = '\\';
	private static final char NULL_CHAR = '\000';

	private enum ParseState {
		FIELD_START, ENCLOSED_FIELD, UNENCLOSED_FIELD, ENCLOSED_ESCAPE, ENCLOSED_EXPECT_DELIMITER, UNENCLOSED_ESCAPE
	}

	/**
	 * An error thrown when parsing fails.
	 */
	public static class ParseError extends Exception {

		private static final long serialVersionUID = 1L;

		public ParseError() {
			super("ParseError");
		}

		public ParseError(final String msg) {
			super(msg);
		}

		public ParseError(final String msg, final Throwable cause) {
			super(msg, cause);
		}

		public ParseError(final Throwable cause) {
			super(cause);
		}
	}

	private final ArrayList<String> outputs;

	public RecordParser() {
		this.outputs = new ArrayList<String>();
	}

	public List<String> parseRecord(CharSequence input) throws ParseError {
		if (null == input) {
			throw new ParseError("null input string");
		}

		return this.parseRecord(CharBuffer.wrap(input));
	}

	public List<String> parseRecord(CharBuffer input) throws ParseError {
		if (null == input) {
			throw new ParseError("null input string");
		}
		char curChar = RecordParser.NULL_CHAR;
		ParseState state = ParseState.FIELD_START;
		final int len = input.length();
		StringBuilder sb = null;

		this.outputs.clear();

		final char enclosingChar = RecordParser.enclose;
		final char fieldDelim = RecordParser.field;
		final char recordDelim = RecordParser.record;
		final char escapeChar = RecordParser.escape;
		for (int pos = 0; pos < len; pos++) {
			curChar = input.get();
			switch (state) {
			case FIELD_START:
				if (null != sb) {
					this.outputs.add(sb.toString());
				}

				sb = new StringBuilder();
				if (enclosingChar == curChar) {
					state = ParseState.ENCLOSED_FIELD;
				} else if (escapeChar == curChar) {
					state = ParseState.UNENCLOSED_ESCAPE;
				} else if (fieldDelim == curChar) {
					continue;
				} else if (recordDelim == curChar) {
					pos = len;
				} else {
					state = ParseState.UNENCLOSED_FIELD;
					sb.append(curChar);
				}

				break;

			case ENCLOSED_FIELD:
				if (escapeChar == curChar) {
					state = ParseState.ENCLOSED_ESCAPE;
				} else if (enclosingChar == curChar) {
					state = ParseState.ENCLOSED_EXPECT_DELIMITER;
				} else {
					sb.append(curChar);
				}

				break;

			case UNENCLOSED_FIELD:
				if (escapeChar == curChar) {
					state = ParseState.UNENCLOSED_ESCAPE;
				} else if (fieldDelim == curChar) {
					state = ParseState.FIELD_START;
				} else if (recordDelim == curChar) {
					pos = len;
				} else {
					sb.append(curChar);
				}

				break;

			case ENCLOSED_ESCAPE:
				sb.append(curChar);
				state = ParseState.ENCLOSED_FIELD;
				break;

			case ENCLOSED_EXPECT_DELIMITER:
				if (fieldDelim == curChar) {
					state = ParseState.FIELD_START;
				} else if (recordDelim == curChar) {
					pos = len;
				} else {
					throw new ParseError("Expected delimiter at position "
							+ pos);
				}
				break;

			case UNENCLOSED_ESCAPE:
				sb.append(curChar);
				state = ParseState.UNENCLOSED_FIELD;
				break;

			default:
				throw new ParseError("Unexpected parser state: " + state);
			}
		}

		if (state == ParseState.FIELD_START && curChar == fieldDelim) {
			if (null != sb) {
				this.outputs.add(sb.toString());
				sb = new StringBuilder();
			}
		}

		if (null != sb) {
			this.outputs.add(sb.toString());
		}

		return this.outputs;
	}
}
