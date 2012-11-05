/**
 * Copyright (c) 2001-2002. Andromedia. All Rights Reserved.
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
 * This software was written for
 * Andromedia, to be provided as
 * part of the OSCAR McMaster
 * EMR System
 */


package oscar.oscarLab.ca.bc.PathNet.HL7.V2_3;

import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.oscarehr.billing.CA.BC.dao.Hl7MshDao;
import org.oscarehr.billing.CA.BC.model.Hl7Msh;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.oscarLab.ca.bc.PathNet.HL7.Node;

/* @author Jesse Bank
 * For The Oscar McMaster Project
 * Developed By Andromedia
 * www.andromedia.ca
 */
public class MSH extends oscar.oscarLab.ca.bc.PathNet.HL7.Node {
	
   private Logger _logger = MiscUtils.getLogger();
   private Hl7MshDao dao = SpringUtils.getBean(Hl7MshDao.class);

   public Node Parse(String line) {
      return super.Parse(line, 1, 1);
   }
   
   
   //This inserts a record into the hl7_msh returns a 0 if it worked and 1 if it failed  
   public int ToDatabase(int parent) throws SQLException {
	   Hl7Msh h = new Hl7Msh();
	   h.setMessageId(parent);
	   h.setSeperator(get("seperator", ""));
	   h.setEncoding(get("encoding_characters", ""));
	   h.setSendingApp(get("sending_application", ""));
	   h.setSendingFacility(get("sending_facility", ""));
	   h.setReceivingApp(get("receiving_application", ""));
	   h.setReceivingFacility(get("receiving_facility", ""));
	   h.setDateTime(convertTSToDate(get("date_time_of_message", "")));
	   h.setSecurity(get("security", ""));
	   h.setMessageType(get("message_type", ""));
	   h.setControlId(get("message_control_id", ""));
	   h.setProcessingId(get("processing_id", ""));
	   h.setVersionId(get("version_id", ""));
	   h.setSequenceNumber(get("sequence_number", ""));
	   h.setContinuationPointer(get("continuation_pointer", ""));
	   h.setAcceptAckType(get("accept_acknowledgment_type", ""));
	   h.setApplicationAckType(get("application_acknowledge_type", ""));
	   h.setCountryCode(get("country_code", ""));
	   h.setCharacterSet(get("character_set", ""));
	   h.setLanguage(get("principal_language_of_message", ""));
	   dao.persist(h);
	   
      return 0;      
   }
   
   protected String getInsertSql(int parent) {return null;}
   

   protected String[] getProperties() {
      return new String[] {
         "seperator",
         "encoding_characters",
         "sending_application",
         "sending_facility",
         "receiving_application",
         "receiving_facility",
         "date_time_of_message",
         "security",
         "message_type",
         "message_control_id",
         "processing_id",
         "version_id",
         "sequence_number",
         "continuation_pointer",
         "accept_acknowledgment_type",
         "application_acknowledge_type",
         "country_code",
         "character_set",
         "principal_language_of_message"
      };
   }
}
