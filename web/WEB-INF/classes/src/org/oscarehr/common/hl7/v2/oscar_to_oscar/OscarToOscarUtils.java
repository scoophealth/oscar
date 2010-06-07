package org.oscarehr.common.hl7.v2.oscar_to_oscar;

import java.io.UnsupportedEncodingException;

import org.apache.commons.codec.binary.Base64;

import ca.uhn.hl7v2.parser.PipeParser;
import ca.uhn.hl7v2.validation.impl.NoValidation;

public final class OscarToOscarUtils {
	public static final String SERVICE_NAME = "OSCAR_TO_OSCAR_HL7_V2";
	public static final String ENCODING="UTF-8";
	public static final Base64 base64=new Base64();
	public static final PipeParser pipeParser=initialisePipeParser();
	
	public enum CategoryType
	{
		REFERRAL
	}
	
	private static PipeParser initialisePipeParser()
	{
		PipeParser pipeParser=new PipeParser();
		pipeParser.setValidationContext(new NoValidation());
		return(pipeParser);
	}
	
	public static String encodeBase64String(byte[] b) throws UnsupportedEncodingException {
		return (new String(OscarToOscarUtils.base64.encode(b), OscarToOscarUtils.ENCODING));
	}
}
