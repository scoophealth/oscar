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
import org.oscarehr.billing.CA.BC.dao.Hl7PidDao;
import org.oscarehr.billing.CA.BC.model.Hl7Pid;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.oscarLab.ca.bc.PathNet.HL7.Node;
/*
 * @author Jesse Bank
 * For The Oscar McMaster Project
 * Developed By Andromedia
 * www.andromedia.ca
 */
public class PID extends oscar.oscarLab.ca.bc.PathNet.HL7.Node {
    private static Logger logger=MiscUtils.getLogger();
    private static Hl7PidDao dao = SpringUtils.getBean(Hl7PidDao.class);
    private ArrayList<PIDContainer> containers;
    private ArrayList<Node> note;


   public PID() {
      this.containers = new ArrayList<PIDContainer>();
      this.note = new ArrayList<Node>();
   }


   //This checks what the line starts with and then acts accordingly
   //IF line starts PID it calls the normal parse method.
   //IF the line starts with ORC it creates a new instance of PIDContainer and adds it too the containers ArrayList.
   //   It also calls the PIDcontainers parse method
   //If the line starts with OBR it checks to see if the Containers ArrayList is empty or if the last element already has an OBR record attached to it.
   // if the containers is empty or the last record attached has an OBR attached it creates an new PIDContainer and calls on the PIDContainer to parse that line.
   // else it just calls Parse on the last element in the containers ArrayList
   //If the line starts with NTE it creates a new NTE object, calls the NTE parse method, and adds it to the ArrayList
   //IF the line equals anything else the it just calls parse  method on the last element in the ArrayList
   public Node Parse(String line) {
      if(line.startsWith("PID")) {
         return super.Parse(line, 0, 1);
      }
      if(line.startsWith("ORC")) {
         PIDContainer container = new PIDContainer();
         this.containers.add(container);
         return container.Parse(line);
      } else if(line.startsWith("OBR")) {
         if(this.containers.isEmpty() || (this.containers.get(this.containers.size() -1)).HasOBR()) {
            PIDContainer container = new PIDContainer();
            this.containers.add(container);
            return container.Parse(line);
         } else {
            return (this.containers.get(this.containers.size() -1)).Parse(line);
         }
      } else if(line.startsWith("NTE")) {
         NTE nte = new NTE();
         this.note.add(nte.Parse(line));
      } else {
         return (this.containers.get(this.containers.size() -1)).Parse(line);
      }
      logger.error("Error During Parsing, Unknown Line - oscar.PathNet.HL7.V2_3.PID - Message: " + line);
      return null;
   }

   public String getNote() {
      String notes = "";
      int size = note.size();
      for(int i = 0; i < size; ++i ) {
         notes += ((NTE)note.get(i)).get("comment", "");
      }
      return notes;
   }
 

   protected String getInsertSql(int parent) {return null; }

   //This inserts a record into the hl7_pid table with a key to the hl7.message_id field
   //Then gets the last insert Id from the hl7_pid table
   //Then for each PIDContainer in containers ArrayList calls the PIDContainer.ToDatabase
   public int ToDatabase(int parent) throws SQLException {
	   
	   Hl7Pid h = new Hl7Pid();
	   h.setSetId(get("set_id",""));
	   h.setExternalId(get("external_id",""));
	   h.setInternalId(get("internal_id",""));
	   h.setAlternateId(get("alternate_id",""));
	   h.setPatientName(get("patient_name",""));
	   h.setMotherMaidenName(get("mother_maiden_name",""));
	   h.setDateOfBirth(this.convertTSToDate(get("date_of_birth","")));
	   h.setSex(get("sex",""));
	   h.setPatientAlias(get("patient_alias",""));
	   h.setRace(get("race",""));
	   h.setPatientAddress(get("patient_address",""));
	   h.setCountryCode(get("country_code",""));
	   h.setHomeNumber(get("home_number",""));
	   h.setWorkNumber(get("work_number",""));
	   h.setLanguage(get("language",""));
	   h.setMaritalStatus(get("marital_status",""));
	   h.setReligion(get("religion",""));
	   h.setPatientAccountNumber(get("patient_account_number",""));
	   h.setSsnNumber(get("ssn_number",""));
	   h.setDriverLicense(get("driver_license",""));
	   h.setMotherIdentifier(get("mother_identifier",""));
	   h.setEthnicGroup(get("ethnic_group",""));
	   h.setBirthPlace(get("birth_place",""));
	   h.setMultipleBirthIndicator(get("multiple_birth_indicator",""));
	   h.setBirthOrder(get("birth_order",""));
	   h.setCitizenship(get("citizenship",""));
	   h.setVeteranMilitaryStatus(get("veteran_military_status",""));
	   h.setNationality(get("nationality",""));
	   h.setPatientDeathDateTime(convertTSToDate(get("patient_death_date_time","")));
	   h.setPatientDeathIndicator(get("patient_death_indicator",""));
	   h.setNote(getNote());
	   
	   dao.persist(h);
	   
      int lastInsert = h.getId();
      int size = this.containers.size();
      for(int i = 0; i < size; ++i) {
         (this.containers.get(i)).ToDatabase(lastInsert);
      }
      return lastInsert;
   }
   
   protected String[] getProperties() {
      return new String[]{
         "set_id",
         "external_id",
         "internal_id",
         "alternate_id",
         "patient_name",
         "mother_maiden_name",
         "date_of_birth",
         "sex",
         "patient_alias",
         "race",
         "patient_address",
         "country_code",
         "home_number",
         "work_number",
         "language",
         "marital_status",
         "religion",
         "patient_account_number",
         "ssn_number",
         "driver_license",
         "mother_identifier",
         "ethnic_group",
         "birth_place",
         "multiple_birth_indicator",
         "birth_order",
         "citizenship",
         "veteran_military_status",
         "nationality",
         "patient_death_date_time",
         "patient_death_indicator"
      };
   }
}
