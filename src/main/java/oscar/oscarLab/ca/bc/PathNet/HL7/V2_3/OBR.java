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

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import oscar.oscarDB.DBHandler;
import oscar.oscarLab.ca.bc.PathNet.HL7.Node;
/*
 * @author Jesse Bank
 * For The Oscar McMaster Project
 * Developed By Andromedia
 * www.andromedia.ca
 */
public class OBR extends oscar.oscarLab.ca.bc.PathNet.HL7.Node {
   Logger _logger = Logger.getLogger(this.getClass());
   private static final String select = "SELECT hl7_obr.obr_id FROM hl7_obr WHERE hl7_obr.filler_order_number='@filler_no'";

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
   //This method is passed in the hl7_pid.pid_id foreign key
   //The first thing it does is check the result_status of this object, which could be
   //F = complete
   //  Looks for a record with the same filler_order_number field.
   //   If it can find one it updates the old record
   //   If it can't find one it inserts a new record
   //  Then for each object in the obxs ArrayList calls OBX.ToDatabase
   //I  = pending
   //  Inserts into hl7_obr. gets return id
   //  Then for each object in the obxs ArrayList calls OBX.ToDatabase
   //C = corrected
   //  Updated record with the filler_order_number
   //  Then for each object in the obxs ArrayList calls OBX.ToDatabase but with the setUpdate(true) method called
   //X = deleted (currently not in use)
   //  Nothing is done for this currently
   public int ToDatabaseOLD(DBHandler db, int parent)throws SQLException {
      _logger.debug("result_status :"+this.get("result_status","") );
      if(this.get("result_status", "").equalsIgnoreCase("A") || this.get("result_status", "").equalsIgnoreCase("F")) {
                                                                           _logger.debug("getting Last Updated filler #:"+this.get("filler_order_number", ""));
         int index = getLastUpdated(db, this.get("filler_order_number", ""));
                                                                           _logger.debug("getting index of Updated filler #:"+this.get("filler_order_number", "")+" "+index);
         if(index != 0) {
                                                                           _logger.debug("Running Update: "+this.getUpdateSql(this.get("filler_order_number", "")));
            DBHandler.RunSQL(this.getUpdateSql(this.get("filler_order_number", "")));
         } else {
                                                                           _logger.debug("Running Insert: "+this.getInsertSql(parent));
            DBHandler.RunSQL(this.getInsertSql(parent));
            index = super.getLastInsertedId();
                                                                           _logger.debug("Index of insert:"+index);
         }
         int size = this.obxs.size();
                                                                           _logger.debug("OBX size:"+size);
         for(int i = 0; i < size; ++i) {
            (obxs.get(i)).ToDatabase(index);
         }
      } else if(this.get("result_status", "").equalsIgnoreCase("I")) {
                                                                           _logger.debug("Running Insert when stat = I:"+this.getInsertSql(parent));
         DBHandler.RunSQL(this.getInsertSql(parent));
         int lastInsert = super.getLastInsertedId();
                                                                           _logger.debug("Index of insert:"+lastInsert);
         int size = this.obxs.size();
                                                                           _logger.debug("OBX size:"+size);
         for(int i = 0; i < size; ++i) {
            (obxs.get(i)).ToDatabase(lastInsert);
         }
      } else if(this.get("result_status", "").equalsIgnoreCase("C")) {
                                                                           _logger.debug("Running Update when stat = C :"+this.getUpdateSql(this.get("filler_order_number", "")));
         DBHandler.RunSQL(this.getUpdateSql(this.get("filler_order_number", "")));
         int lastUpdate = this.getLastUpdated(db, this.get("filler_order_number", ""));
                                                                           _logger.debug("Update id of lastUpdate:"+lastUpdate);
         int size = this.obxs.size();
                                                                           _logger.debug("OBX size:"+size);
         for(int i = 0; i < size; ++i) {
            (obxs.get(i)).setUpdate(true);
            (obxs.get(i)).ToDatabase(lastUpdate);
         }
      }
      return 0;
   }

   //////
   public int ToDatabase(int parent)throws SQLException {
      _logger.debug("result_status :"+this.get("result_status","") );
      _logger.debug("Running Insert when "+this.getInsertSql(parent));

      DBHandler.RunSQL(this.getInsertSql(parent));
      int lastInsert = super.getLastInsertedId();
      _logger.debug("Index of insert:"+lastInsert);
      int size = this.obxs.size();
      _logger.debug("OBX size:"+size);
      for(int i = 0; i < size; ++i) {
         (obxs.get(i)).ToDatabase(lastInsert);
      }
      return 0;
   }
   //////


   protected int getLastUpdated(DBHandler db, String id)throws SQLException {
      ResultSet result = DBHandler.GetSQL("SELECT obr_id FROM hl7_obr WHERE filler_order_number='" + id +"'");
      int parent = 0;
      if(result.next()) {
         parent = result.getInt(1);
      }
      return parent;
   }

   protected String getInsertSql(int parent) {
      String fields = "INSERT INTO hl7_obr ( pid_id";
      String values = "VALUES ('" + String.valueOf(parent) + "'";
      String[] properties = this.getProperties();
      for(int i = 0; i < properties.length; ++i) {
         fields += ", " + properties[i];
         values += ", '" + this.get(properties[i], "") + "'";
      }
      fields += ", note";
      values += ", '" + getNote() + "'";
      return fields + ") " + values + ");";
   }

   protected String getUpdateSql(String id) {
      String sql = "UPDATE hl7_obr SET" ;
      String[] properties = this.getProperties();
      for(int i = 0; i < properties.length; ++i) {
         sql += " " + properties[i] + "='" + get(properties[i], "") + "',";
      }
      sql += "note='" + getNote() + "' WHERE filler_order_number='" + id + "'";
      return sql;
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
