package org.oscarehr.common.hl7.v2.oscar_to_oscar;

import java.io.FileOutputStream;

import org.apache.log4j.Logger;
import org.oscarehr.util.MiscUtils;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.AbstractMessage;
import ca.uhn.hl7v2.parser.EncodingNotSupportedException;
import ca.uhn.hl7v2.parser.PipeParser;
import ca.uhn.hl7v2.validation.impl.NoValidation;

public final class OscarToOscarUtils {
	private static final Logger logger = MiscUtils.getLogger();

	public static final String UPLOAD_MESSAGE_TYPE = "OSCAR_TO_OSCAR_HL7_V2";
	public static final PipeParser pipeParser = initialisePipeParser();

	private static PipeParser initialisePipeParser() {
		PipeParser pipeParser = new PipeParser();
		pipeParser.setValidationContext(new NoValidation());
		return (pipeParser);
	}

	public static AbstractMessage pipeParserParse(String hl7Message) throws EncodingNotSupportedException, HL7Exception {
		// convert \n to \r as per hl7 spec section 2.8
		hl7Message = hl7Message.replaceAll("\n", "\r");
		// the above will have converted \r\n to \r\r so fix that too
		hl7Message = hl7Message.replaceAll("\r\r", "\r");

		AbstractMessage message = (AbstractMessage) pipeParser.parse(hl7Message);
		return (message);
	}

	public static void dumpMessageToDebugger(AbstractMessage message) {
		try {
			String result = pipeParser.encode(message);
			result = result.replace("\r", "\r\n");
			result = result.replace("\r\r", "\r");
			logger.error(result);
		} catch (HL7Exception e) {
			logger.error("Unexpected error.", e);
		}
	}

	public static void dumpMessageToFile(AbstractMessage message) {
		try {
			String result = pipeParser.encode(message);
			result = result.replace("\r", "\r\n");
			result = result.replace("\r\r", "\r");

			FileOutputStream fos = new FileOutputStream("/tmp/temp.hl7");
			fos.write(result.getBytes());
			fos.flush();
			fos.close();
		} catch (Exception e) {
			logger.error("Unexpected error.", e);
		}
	}
}
