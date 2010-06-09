package org.oscarehr.common.hl7.v2.oscar_to_oscar;

import java.io.UnsupportedEncodingException;

import org.apache.commons.codec.binary.Base64;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.AbstractMessage;
import ca.uhn.hl7v2.parser.EncodingNotSupportedException;
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
	
	public static AbstractMessage pipeParserParse(String hl7Message) throws EncodingNotSupportedException, HL7Exception
	{
		// convert \n to \r as per hl7 spec section 2.8
		hl7Message=hl7Message.replaceAll("\n", "\r");
		// the above will have converted \r\n to \r\r so fix that too
		hl7Message=hl7Message.replaceAll("\r\r", "\r");
		
		AbstractMessage message=(AbstractMessage) OscarToOscarUtils.pipeParser.parse(hl7Message);
		return(message);
	}
}
