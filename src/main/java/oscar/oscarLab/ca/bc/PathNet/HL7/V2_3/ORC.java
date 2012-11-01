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
import org.oscarehr.billing.CA.BC.dao.Hl7OrcDao;
import org.oscarehr.billing.CA.BC.model.Hl7Orc;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.oscarLab.ca.bc.PathNet.HL7.Node;
/*
 * @author Jesse Bank
 * For The Oscar McMaster Project
 * Developed By Andromedia
 * www.andromedia.ca
 */
public class ORC extends oscar.oscarLab.ca.bc.PathNet.HL7.Node {
    private static Logger logger=MiscUtils.getLogger(); 
    private static Hl7OrcDao dao = SpringUtils.getBean(Hl7OrcDao.class);
    
    public Node Parse(String line) {
      if(line.startsWith("ORC")) {
         return super.Parse(line, 0, 1);
      }
      logger.error("Error During Parsing, Unknown Line - oscar.PathNet.HL7.V2_3.ORC - Message: " + line);
      return null;
   }
   
   //Inserts record into hl7_orc table

   
   protected String getInsertSql(int parent) {return null;}

   public int ToDatabase(int parent)throws SQLException {
	   
	   Hl7Orc h = new Hl7Orc();
	   h.setOrderControl(get("order_control",""));
	   h.setPlacerOrderNumber1(get("placer_order_number1",""));
	   h.setFillerOrderNumber(get("filler_order_number",""));
	   h.setPlacerOrderNumber2(get("placer_order_number2",""));
	   h.setOrderStatus(get("order_status",""));
	   h.setResponseFlag(get("response_flag",""));
	   h.setQuantityTiming(get("quantity_timing",""));
	   h.setParent(get("parent",""));
	   h.setDatetimeOfTransaction(this.convertTSToDate(get("date_time_of_transaction","")));
	   h.setEnteredBy(get("entered_by",""));
	   h.setVerifiedBy(get("verified_by",""));
	   h.setOrderingProvider(get("ordering_provider",""));
	   h.setEntererLocation(get("enterer_location",""));
	   h.setCallbackPhoneNumber(get("callback_phone_number",""));
	   h.setOrderEffectiveDateTime(this.convertTSToDate(get("order_effective_date_time","")));
	   h.setOrderControlCodeReason(get("order_control_code_reason",""));
	   h.setEnteringOrganization(get("entering_organization",""));
	   h.setEnteringDevice(get("entering_device",""));
	   h.setActionBy(get("action_by",""));
	   
	   dao.persist(h);
	   
	   return 0;
	
   }
   
   
   protected String[] getProperties() {
      return new String[]{
         "order_control",
         "placer_order_number1",
         "filler_order_number",
         "placer_order_number2",
         "order_status",
         "response_flag",
         "quantity_timing",
         "parent",
         "date_time_of_transaction",
         "entered_by",
         "verified_by",
         "ordering_provider",
         "enterer_location",
         "callback_phone_number",
         "order_effective_date_time",
         "order_control_code_reason",
         "entering_organization",
         "entering_device",
         "action_by"
      };
   }
}
