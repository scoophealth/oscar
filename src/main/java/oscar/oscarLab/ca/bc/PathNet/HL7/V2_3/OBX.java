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
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.oscarehr.billing.CA.BC.dao.Hl7ObxDao;
import org.oscarehr.billing.CA.BC.model.Hl7Obx;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.oscarLab.ca.bc.PathNet.HL7.Node;
/*
 * @author Jesse Bank
 * For The Oscar McMaster Project
 * Developed By Andromedia
 * www.andromedia.ca
 */
public class OBX extends oscar.oscarLab.ca.bc.PathNet.HL7.Node {
    private static Logger logger=MiscUtils.getLogger();
    private static Hl7ObxDao dao = SpringUtils.getBean(Hl7ObxDao.class);
    private ArrayList<NTE> note;
    private boolean update = false;

   public OBX() {
      note = new ArrayList<NTE>();
   }
   //If line starts with OBX then its parsed in the normal way
   //IF line starts with NTE then a new NTE object is created and added to the note ArrayList, parse method is called
   public Node Parse(String line) {
      if(line.startsWith("OBX")) {
         return super.Parse(line, 0, 1);
      } else if(line.startsWith("NTE")) {
         NTE nte = new NTE();
         this.note.add(nte);
         return nte.Parse(line);
      }
      logger.error("Error During Parsing, Unknown Line - oscar.PathNet.HL7.V2_3.OBX - Message: " + line);
      return null;
   }

   public int ToDatabase(int parent)throws SQLException {
	   List<Hl7Obx> results = new ArrayList<Hl7Obx>();
	   if(update) {
		   results = dao.findByObrId(parent);
	   } else {
		   results.add(new Hl7Obx());
	   }
	   
	   for(Hl7Obx h:results) {
		   h.setSetId(get("set_id",""));
		   h.setValueType(get("value_type",""));
		   h.setObservationIdentifier(get("observation_identifier",""));
		   h.setObservationSubId(get("observation_sub_id",""));
		   h.setObservationResults(get("observation_results",""));
		   h.setUnits(get("units",""));
		   h.setReferenceRange(get("reference_range",""));
		   h.setAbnormalFlags(get("abnormal_flags",""));
		   h.setProbability(get("probability",""));
		   h.setNatureOfAbnormalTest(get("nature_of_abnormal_test",""));
		   h.setObservationResultStatus(get("observation_result_status",""));
		   h.setDateLastNormalValue(this.convertTSToDate(get("date_last_normal_value","")));
		   h.setUserDefinedAccessChecks(get("user_defined_access_checks",""));
		   h.setObservationDateTime(convertTSToDate(get("observation_date_time","")));
		   h.setProducerId(get("producer_id",""));
		   h.setResponsibleObserver(get("responsible_observer",""));
		   h.setObservationMethod(get("observation_method",""));
		   h.setNote(getNote());
		   
		   
		   if(update) {
			   dao.merge(h);
		   } else {
			   dao.persist(h);
		   }
	   }
	   
	  return 0;
   }


   public void setUpdate(boolean update) {
      this.update = update;
   }

   public String getNote() {
      String notes = "";
      int size = note.size();
      for(int i = 0; i < size; ++i ) {
         notes += (note.get(i)).get("comment", "");
      }
      return notes;
   }

   protected String getUpdateSql(int parent) {return null;}

   protected String getInsertSql(int parent) {return null;}

   protected String[] getProperties() {
      return new String[] {
         "set_id",
         "value_type",
         "observation_identifier",
         "observation_sub_id",
         "observation_results",
         "units",
         "reference_range",
         "abnormal_flags",
         "probability",
         "nature_of_abnormal_test",
         "observation_result_status",
         "date_last_normal_value",
         "user_defined_access_checks",
         "observation_date_time",
         "producer_id",
         "responsible_observer",
         "observation_method" };
   }
}
