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

import org.apache.log4j.Logger;
import org.oscarehr.billing.CA.BC.dao.Hl7ObrDao;
import org.oscarehr.billing.CA.BC.model.Hl7Obr;
import org.oscarehr.util.SpringUtils;

import oscar.oscarLab.ca.bc.PathNet.HL7.Node;
/*
 * @author Jesse Bank
 * For The Oscar McMaster Project
 * Developed By Andromedia
 * www.andromedia.ca
 */
public class OBR extends oscar.oscarLab.ca.bc.PathNet.HL7.Node {
   Logger _logger = Logger.getLogger(this.getClass());
   private static Hl7ObrDao dao = SpringUtils.getBean(Hl7ObrDao.class);
 
   private ArrayList<OBX> obxs;
   private ArrayList<NTE>note;

   public OBR() {
      super();
      this.obxs = new ArrayList<OBX>();
      this.note = new ArrayList<NTE>();
   }
   //If line starts OBR the regular parse method is called
   //If line starts with OBX a new OBX object is created and added to the obxs ArrayList, the obx parse method is called
   //IF line starts with NTE a new NTE object is created and addeds to the note ArrayList, the nte parse method is called
   public Node Parse(String line) {
      _logger.debug("line: "+line);
      if(line.startsWith("OBR")) {
         return super.Parse(line, 0, 1);
      } else if(line.startsWith("OBX")) {
         OBX obx = new OBX();
         this.obxs.add(obx);
         return obx.Parse(line);
      } else if(line.startsWith("NTE")) {
         NTE nte = new NTE();
         this.note.add(nte);
         return nte.Parse(line);
      }
      _logger.error("Error During Parsing, Unknown Line - oscar.PathNet.HL7.V2_3.OBR - Message: " + line);
      return null;
   }
   public String getNote() {
      String notes = "";
      int size = note.size();
      for(int i = 0; i < size; ++i ) {
         notes += (note.get(i)).get("comment", "");
      }
      return notes;
   }
 
   protected String getInsertSql(int parent) {return null;}
   
   protected String getUpdateSql(String id) { return null;}

   //////
   public int ToDatabase(int parent)throws SQLException {
      _logger.debug("result_status :"+this.get("result_status","") );
  
      Hl7Obr h = new Hl7Obr();
      h.setPidId(parent);
      h.setSetId(get("set_id","set_id"));
      h.setPlacerOrderNumber(get("placer_order_number",""));
      h.setFillerOrderNumber(get("filler_order_number",""));
      h.setUniversalServiceId(get("universal_service_id",""));
      h.setPriority(get("priority",""));
      h.setRequestedDateTime(convertTSToDate(get("requested_date_time","")));
      h.setOberservationDateTime(convertTSToDate(get("observation_date_time","")));
      h.setObservationEndDateTime(convertTSToDate(get("observation_end_date_time","")));
      h.setCollectionVolume(get("collection_volume",""));
      h.setCollectorIdentifier(get("collector_identifier",""));
      h.setSpecimenActionCode(get("specimen_action_code",""));
      h.setDangerCode(get("danger_code",""));
      h.setRelevantClinicalInfo(get("relevant_clinical_info",""));
      h.setSpecimenReceivedDateTime(convertTSToDate(get("specimen_received_date_time","")));
      h.setSpecimenSource(get("specimen_source",""));
      h.setOrderingProvider(get("ordering_provider",""));
      h.setOrderCallbackPhoneNumber(get("order_callback_phone_number",""));
      h.setPlacersField1(get("placers_field1",""));
      h.setPlacersField2(get("palcers_field2",""));
      h.setFillerField1(get("filler_field1",""));
      h.setFillerField2(get("filler_field2",""));
      h.setResultsReportStatusChange(convertTSToDate(get("results_report_status_change","")));
      h.setChargeToPractice(get("charge_to_practice",""));
      h.setDiagnosticServiceSectId(get("diagnostic_service_sect_id",""));
      h.setResultStatus(get("result_status",""));
      h.setParentResult(get("parent_result",""));
      h.setQuantityTiming(get("quantity_timing",""));
      h.setResultCopiesTo(get("result_copies_to",""));
      h.setParentNumber(get("parent_number",""));
      h.setTransporationMode(get("transportation_mode",""));
      h.setReasonForStudy(get("reason_for_study",""));
      h.setPrincipalResultInterpreter(get("principal_result_interpreter",""));
      h.setAssistantResultInterpreter(get("assistant_result_interpreter",""));
      h.setTechnician(get("technician",""));
      h.setTranscriptionist(get("transcriptionist",""));
      h.setScheduledDateTime(convertTSToDate(get("scheduled_date_time","")));
      h.setNumberOfSampleContainers(get("number_of_sample_containers",""));
      h.setTransportLogisticsOfCollectedSample(get("transport_logistics_of_collected_sample",""));
      h.setCollectorComment(get("collector_comment",""));
      h.setTransportArrangementResponsibility(get("transport_arrangement_responsibility",""));
      h.setTransportArranged(get("transport_arranged",""));
      h.setEscortRequired(get("escort_required",""));
      h.setPlannedPatientTransportComment(get("planned_patient_transport_comment",""));
      h.setNote(getNote());
      
      dao.persist(h);
      
      int lastInsert = h.getId();
      _logger.debug("Index of insert:"+lastInsert);
      int size = this.obxs.size();
      _logger.debug("OBX size:"+size);
      for(int i = 0; i < size; ++i) {
         (obxs.get(i)).ToDatabase(lastInsert);
      }
      return 0;
   }
   
   protected String[] getProperties() {
      return new String[] {
         "set_id",
         "placer_order_number",
         "filler_order_number",
         "universal_service_id",
         "priority",
         "requested_date_time",
         "observation_date_time",
         "observation_end_date_time",
         "collection_volume",
         "collector_identifier",
         "specimen_action_code",
         "danger_code",
         "relevant_clinical_info",
         "specimen_received_date_time",
         "specimen_source",
         "ordering_provider",
         "order_callback_phone_number",
         "placers_field1",
         "palcers_field2",
         "filler_field1",
         "filler_field2",
         "results_report_status_change",
         "charge_to_practice",
         "diagnostic_service_sect_id",
         "result_status",
         "parent_result",
         "quantity_timing",
         "result_copies_to",
         "parent_number",
         "transportation_mode",
         "reason_for_study",
         "principal_result_interpreter",
         "assistant_result_interpreter",
         "technician",
         "transcriptionist",
         "scheduled_date_time",
         "number_of_sample_containers",
         "transport_logistics_of_collected_sample",
         "collector_comment",
         "transport_arrangement_responsibility",
         "transport_arranged",
         "escort_required",
         "planned_patient_transport_comment" };
   }
}
