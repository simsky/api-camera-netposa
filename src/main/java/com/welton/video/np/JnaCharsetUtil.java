package com.welton.video.np;

import java.nio.charset.Charset;

public class JnaCharsetUtil {
	static Charset CS_JNA  = Charset.defaultCharset();
	static {
		String prop = System.getProperty("jna.encoding");
		if(prop != null) {
			CS_JNA = Charset.forName(prop);
		}
	}

	public static String cvtString(byte[] buf, int offset, int length) {
		return new String(buf, offset, length, CS_JNA);
	}
}
