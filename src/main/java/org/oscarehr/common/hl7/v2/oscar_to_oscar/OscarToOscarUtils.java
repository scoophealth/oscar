/**
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
 * This software is published under the GPL GNU General Public License.
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version. 
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 *
 * This software was written for the
 * Department of Family Medicine
 * McMaster University
 * Hamilton
 * Ontario, Canada
 */


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
		try
		{
			try {
				AbstractMessage message = (AbstractMessage) pipeParser.parse(hl7Message);
		        return (message);
	        } 
			catch (EncodingNotSupportedException e)
			{
				// make a feeble attempt at fixing line feed characters
				// sometimes this doesn't work because HAPI sprinkles 0A or 0D's around fields within a segment sometimes for no good reason at all, which causes this to break up a segment into multiple lines and barf.
				
				// convert \n to \r as per hl7 spec section 2.8
		        hl7Message = hl7Message.replaceAll("\n", "\r");
		        // the above will have converted \r\n to \r\r so fix that too
		        hl7Message = hl7Message.replaceAll("\r\r", "\r");
	
		        AbstractMessage message = (AbstractMessage) pipeParser.parse(hl7Message);
		        return (message);
			}
		}
		catch (HL7Exception e) {
		
        	logger.error("Unable to parse message : "+hl7Message);
        	throw(e);
        }
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
