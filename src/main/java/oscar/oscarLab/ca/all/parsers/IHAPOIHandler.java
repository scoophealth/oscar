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
package oscar.oscarLab.ca.all.parsers;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.model.v23.segment.MSH;
// import ca.uhn.hl7v2.model.v23.message.ORU_R01;
import ca.uhn.hl7v2.parser.Parser;
import ca.uhn.hl7v2.parser.PipeParser;
import ca.uhn.hl7v2.util.Terser;
import ca.uhn.hl7v2.validation.impl.NoValidation;

public class IHAPOIHandler extends MEDITECHHandler implements MessageHandler  {

	public IHAPOIHandler() {
		// default
	}
	
	@Override
	public void init(String hl7Body) throws HL7Exception {
		Parser parser = new PipeParser();
		parser.setValidationContext(new NoValidation());
		Message message = parser.parse(hl7Body.replaceAll( "\n", "\r\n" ).replace("\\.Zt\\", "\t"));
		
		if( message instanceof ca.uhn.hl7v2.model.v23.message.ORU_R01 ) {
			msg = ( ca.uhn.hl7v2.model.v23.message.ORU_R01 ) message;
		} else {			
			// IHA sends their hospital reports with a message event trigger of O01 
			// this needs to be changed in order for HAPI to determine a proper message structure
			MSH messageHeader = (MSH) message.get("MSH");
			messageHeader.getMessageType().getCm_msg1_MessageType().setValue("ORU");
			messageHeader.getMessageType().getCm_msg2_TriggerEvent().setValue("R01");		
			init( message.encode() );
		}
		if( msg != null ) {
			setTerser( new Terser( msg ) );
		}
	}
}
