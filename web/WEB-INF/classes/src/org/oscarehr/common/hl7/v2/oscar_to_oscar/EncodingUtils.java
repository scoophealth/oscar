package org.oscarehr.common.hl7.v2.oscar_to_oscar;

import java.io.UnsupportedEncodingException;

import org.apache.commons.codec.binary.Base64;

public final class EncodingUtils {
	public static final String ENCODING="UTF-8";
	public static final Base64 base64=new Base64();
	
	public static String encodeBase64String(byte[] b) throws UnsupportedEncodingException {
		return (new String(EncodingUtils.base64.encode(b), EncodingUtils.ENCODING));
	}
}
