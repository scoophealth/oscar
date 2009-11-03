/*
 *
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. * 
 * This software is published under the GPL GNU General Public License.
 * This program is free software; you can redistribute it and/or 
 * modify it under the terms of the GNU General Public License 
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version. *
 * This program is distributed in the hope that it will be useful, 
 * but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
 * GNU General Public License for more details. * * You should have received a copy of the GNU General Public License 
 * along with this program; if not, write to the Free Software 
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. * 
 * 
 * <OSCAR TEAM> 
 * 
 * This software was written for the 
 * Department of Family Medicine 
 * McMaster Unviersity 
 * Hamilton 
 * Ontario, Canada 
 */

package oscar.oscarRx.data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.LinkedList;

import oscar.oscarDB.DBHandler;

public class RxPatientData {
   
   /* Patient Search */
   
   public Patient[] PatientSearch(String surname, String firstName) {
      
      Patient[] arr = {};     
      ArrayList lst = new ArrayList();      
      try {         
         DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);         
         ResultSet rs;         
         Patient p;         
         rs = db.GetSQL(
         "SELECT demographic_no, last_name, first_name, sex, year_of_birth, "         
         + "month_of_birth, date_of_birth, address, city, postal, phone "         
         + "FROM demographic WHERE last_name LIKE '"         
         + surname + "%' AND first_name LIKE '" + firstName + "%'");
         
         while (rs.next()) {            
            p = new Patient(rs.getInt("demographic_no"), db.getString(rs,"last_name"),            
            db.getString(rs,"first_name"), db.getString(rs,"sex"),            
            calcDate(db.getString(rs,"year_of_birth"),
            db.getString(rs,"month_of_birth"),            
            db.getString(rs,"date_of_birth")),            
            db.getString(rs,"address"), db.getString(rs,"city"),
            db.getString(rs,"postal"),            
            db.getString(rs,"phone"), db.getString(rs,"hin"));            
            lst.add(p);            
         }         
         rs.close();         
         arr = (Patient[]) lst.toArray(arr);         
      }
      catch (SQLException e) {         
         System.out.println(e.getMessage());         
      }      
      return arr;
      
   }
   
   /* Patient Information */
   
   public Patient getPatient(int demographicNo) throws java.sql.SQLException {      
      DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);      
      ResultSet rs;      
      Patient p = null;      
      try {         
         rs = db.GetSQL(
         "SELECT demographic_no, last_name, first_name, sex, year_of_birth, "         
         + "month_of_birth, date_of_birth, address, city, postal, phone,hin "         
         + "FROM demographic WHERE demographic_no = " + demographicNo);
         
         if (rs.next()) {            
            p = new Patient(rs.getInt("demographic_no"), db.getString(rs,"last_name"),            
            db.getString(rs,"first_name"), db.getString(rs,"sex"),            
            calcDate(db.getString(rs,"year_of_birth"),
            db.getString(rs,"month_of_birth"),            
            db.getString(rs,"date_of_birth")),            
            db.getString(rs,"address"), db.getString(rs,"city"),
            db.getString(rs,"postal"),            
            db.getString(rs,"phone"), db.getString(rs,"hin"));            
            System.out.println(db.getString(rs,"first_name"));
         }         
         rs.close();         
      }
      catch (SQLException e) {         
         System.out.println(e.getMessage());         
      }
      
      return p;      
   }
   
   
   public Patient getPatient(String demographicNo) throws java.sql.SQLException {      
      DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);      
      ResultSet rs;      
      Patient p = null;      
      try {         
         rs = db.GetSQL(
         "SELECT demographic_no, last_name, first_name, sex, year_of_birth, "         
         + "month_of_birth, date_of_birth, address, city, postal, phone,hin "         
         + "FROM demographic WHERE demographic_no = " + demographicNo);
         
         if (rs.next()) {            
            p = new Patient(rs.getInt("demographic_no"), db.getString(rs,"last_name"),            
            db.getString(rs,"first_name"), db.getString(rs,"sex"),            
            calcDate(db.getString(rs,"year_of_birth"),
            db.getString(rs,"month_of_birth"),            
            db.getString(rs,"date_of_birth")),            
            db.getString(rs,"address"), db.getString(rs,"city"),
            db.getString(rs,"postal"),            
            db.getString(rs,"phone"), db.getString(rs,"hin"));            
         }         
         rs.close();         
      }
      catch (SQLException e) {         
         System.out.println(e.getMessage());         
      }
      
      return p;      
   }
   private java.util.Date calcDate(String year, String month, String day) {      
      int iYear = Integer.parseInt(year);
      int iMonth = Integer.parseInt(month) - 1;      
      int iDay = Integer.parseInt(day);
      
      GregorianCalendar ret = new GregorianCalendar(iYear, iMonth, iDay);     
      return ret.getTime();      
   }
   
   private int calcAge(java.util.Date DOB) {      
      GregorianCalendar now = new GregorianCalendar();     
      int curYear = now.get(Calendar.YEAR);      
      int curMonth = (now.get(Calendar.MONTH) + 1);      
      int curDay = now.get(Calendar.DAY_OF_MONTH);
      
      Calendar cal = new GregorianCalendar();      
      cal.setTime(DOB);      
      int iYear = cal.get(Calendar.YEAR);      
      int iMonth = (cal.get(Calendar.MONTH) + 1);      
      int iDay = cal.get(Calendar.DAY_OF_MONTH);      
      int age = 0;
     
      if (curMonth > iMonth || (curMonth == iMonth && curDay >= iDay)) {         
         age = curYear - iYear;         
      }else {         
         age = curYear - iYear - 1;         
      }
      
      return age;      
   }
   
   public class Patient {      
      int demographicNo;      
      String surname;      
      String firstName;      
      String sex;      
      java.util.Date DOB;      
      String address;      
      String city;      
      String postal;      
      String phone;
      String hin;
      
      public Patient(int demographicNo, String surname, String firstName,String sex, java.util.Date DOB,
                     String address, String city, String postal, String phone,String hin) {
         
         this.demographicNo = demographicNo;         
         this.surname = surname;         
         this.firstName = firstName;         
         this.sex = sex;         
         this.DOB = DOB;         
         this.address = address;         
         this.city = city;         
         this.postal = postal;        
         this.phone = phone;
         this.hin = hin;         
      }
      
      public int getDemographicNo() {         
         return this.demographicNo;         
      }
      
      public String getSurname() {               
         return this.surname;         
      }
      
      public String getFirstName() {           
         return this.firstName;         
      }
            
      public String getSex() {         
         return this.sex;         
      }
            
      public String getHin() {
         return this.hin;
      }
      
      public java.util.Date getDOB() {         
         return this.DOB;         
      }
      
      public int getAge() {         
         return calcAge(this.getDOB());         
      }
      
      public String getAddress() {         
         return this.address;        
      }
      
      public String getCity() {         
         return this.city;         
      }
      
      public String getPostal() {         
         return this.postal;         
      }
      
      public String getPhone() {         
         return this.phone;         
      }
      
      public Allergy getAllergy(int id) {
           Allergy allergy = null;
           try {            
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);            
            ResultSet rs;                        
            
            rs = db.GetSQL("SELECT * FROM allergies WHERE allergyid  = " + String.valueOf(id));
            
            if(rs.next()) {               
               allergy = new Allergy(rs.getInt("allergyid"), rs.getDate("entry_date"),               
               db.getString(rs,"DESCRIPTION"),
               rs.getInt("HICL_SEQNO"), rs.getInt("HIC_SEQNO"),               
               rs.getInt("AGCSP"), rs.getInt("AGCCS"),
               rs.getInt("TYPECODE"));
                              
               allergy.getAllergy().setReaction(db.getString(rs,"reaction"));
	       allergy.getAllergy().setStartDate(rs.getDate("start_date"));
               allergy.getAllergy().setAgeOfOnset(db.getString(rs,"age_of_onset"));
               allergy.getAllergy().setSeverityOfReaction(db.getString(rs,"severity_of_reaction"));
               allergy.getAllergy().setOnSetOfReaction(db.getString(rs,"onset_of_reaction"));
               allergy.getAllergy().setRegionalIdentifier(db.getString(rs,"regional_identifier"));
                              
            }            
            rs.close();            
            
         }
         catch (SQLException e) {            
            System.out.println(e.getMessage());            
         }  
           
         return allergy;
      }
      
      public Allergy[] getAllergies() {         
         Allergy[] arr = {};         
         LinkedList lst = new LinkedList();         
         try {            
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);            
            ResultSet rs;            
            Allergy allergy;
            
            rs = db.GetSQL("SELECT * FROM allergies WHERE demographic_no = '" + getDemographicNo() + "' and archived = '0' ORDER BY DESCRIPTION");
            
            while (rs.next()) {               
               allergy = new Allergy(rs.getInt("allergyid"), rs.getDate("entry_date"),               
               db.getString(rs,"DESCRIPTION"),
               rs.getInt("HICL_SEQNO"), rs.getInt("HIC_SEQNO"),               
               rs.getInt("AGCSP"), rs.getInt("AGCCS"),
               rs.getInt("TYPECODE"));
                              
               allergy.getAllergy().setReaction(db.getString(rs,"reaction"));
	       allergy.getAllergy().setStartDate(rs.getDate("start_date"));
               allergy.getAllergy().setAgeOfOnset(db.getString(rs,"age_of_onset"));
               allergy.getAllergy().setSeverityOfReaction(db.getString(rs,"severity_of_reaction"));
               allergy.getAllergy().setOnSetOfReaction(db.getString(rs,"onset_of_reaction"));
               allergy.getAllergy().setRegionalIdentifier(db.getString(rs,"regional_identifier"));
               
               lst.add(allergy);               
            }            
            rs.close();            
            arr = (Allergy[]) lst.toArray(arr);            
         }
         catch (SQLException e) {            
            System.out.println(e.getMessage());            
         }
         
         return arr;         
      }
      
      public Allergy addAllergy(java.util.Date entryDate,RxAllergyData.Allergy allergyCode) {         
         Allergy allergy = null;
         try {            
            allergy = new Allergy(0, entryDate, allergyCode);            
            allergy.Save();                               
         }catch (SQLException e) {            
            System.out.println(e.getMessage());                               
         }  
         return allergy;     
      }
      
      public boolean deleteAllergy(int allergyId) {         
         boolean b = false;
         try {            
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);            
            String sql = "update allergies set archived = '1'  WHERE allergyid = '"+allergyId+"'";            
            b = db.RunSQL(sql);                                  
         }catch (SQLException e) {            
            System.out.println(e.getMessage());            
            b = false;            
         }
         return b;
      }
      
      public Disease[] getDiseases() {         
         Disease[] arr = {};         
         LinkedList lst = new LinkedList();         
         try {            
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);            
            ResultSet rs;            
            Disease d;            
            rs = db.GetSQL("SELECT * FROM diseases WHERE demographic_no = '" + getDemographicNo()+"'");            
            while (rs.next()) {               
               d = new Disease(rs.getInt("diseaseid"), db.getString(rs,"ICD9_E"),rs.getDate("entry_date"));               
               lst.add(d);               
            }            
            rs.close();            
            arr = (Disease[]) lst.toArray(arr);            
         }catch (SQLException e) {            
            System.out.println(e.getMessage());            
         }         
         return arr;         
      }
      
      public Disease addDisease(String ICD9, java.util.Date entryDate) throws SQLException {         
         Disease disease = new Disease(0, ICD9, entryDate);         
         disease.Save();         
         return disease;         
      }
      
      //TODO should not delete
      public boolean deleteDisease(int diseaseId) throws SQLException {         
         DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);         
         String sql = "DELETE FROM diseases WHERE diseaseid = " + diseaseId;         
         boolean b = db.RunSQL(sql);         
         return b;         
      }
      
      public RxPrescriptionData.Prescription[] getPrescribedDrugsUnique() {         
         return new RxPrescriptionData().getUniquePrescriptionsByPatient(this.getDemographicNo());         
      }
      
      public RxPrescriptionData.Prescription[] getPrescribedDrugs() {         
         return new RxPrescriptionData().getPrescriptionsByPatient(this.getDemographicNo());         
      }
      
      public RxPrescriptionData.Prescription[] getPrescribedDrugScripts() {
        return new RxPrescriptionData().getPrescriptionScriptsByPatient(this.getDemographicNo());
      }         
      
      public class Allergy {         
         int allergyId;         
         java.util.Date entryDate;         
         RxAllergyData.Allergy allergy;
         
         public Allergy(int allergyId, java.util.Date entryDate,RxAllergyData.Allergy allergy) {            
            this.allergyId = allergyId;            
            this.entryDate = entryDate;            
            this.allergy = allergy;            
         }
         
         public Allergy(java.util.Date entryDate, RxAllergyData.Allergy allergy) {            
            this.allergyId = 0;            
            this.entryDate = entryDate;            
            this.allergy = allergy;            
         }
         
         public Allergy(int allergyId, java.util.Date entryDate,String DESCRIPTION,int HICL_SEQNO, int HIC_SEQNO, int AGCSP, int AGCCS,int TYPECODE) {            
            this.allergyId = allergyId;            
            this.entryDate = entryDate;            
            this.allergy = new RxAllergyData().getAllergy(DESCRIPTION, HICL_SEQNO,HIC_SEQNO, AGCSP, AGCCS, TYPECODE);            
         }
         
         public int getAllergyId() {            
            return this.allergyId;            
         }
         
         public java.util.Date getEntryDate() {            
            return this.entryDate;            
         }
         
         public void setEntryDate(java.util.Date RHS) {            
            this.entryDate = RHS;            
         }
         
         public RxAllergyData.Allergy getAllergy() {            
            return this.allergy;            
         }
         
         public boolean Save() throws SQLException {            
            boolean b = false;            
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);            
            b = this.Save(db);            
            return b;            
         }
         
         public boolean Save(DBHandler db) throws SQLException {            
            boolean b;            
            String sql;            
            if (this.getAllergyId() == 0) {               
               sql = "INSERT INTO allergies (demographic_no, entry_date, "               
               + "DESCRIPTION, HICL_SEQNO, HIC_SEQNO, AGCSP, AGCCS, TYPECODE,reaction,drugref_id,start_date,age_of_onset,severity_of_reaction,onset_of_reaction,regional_identifier) "               
               + "VALUES (" + Patient.this.getDemographicNo() + ", '"               
               + oscar.oscarRx.util.RxUtil.DateToString(this.getEntryDate()) + "', '"               
               + this.allergy.getDESCRIPTION() + "', "               
               + this.allergy.getHICL_SEQNO() + ", "               
               + this.allergy.getHIC_SEQNO() + ", "               
               + this.allergy.getAGCSP() + ", "               
               + this.allergy.getAGCCS() + ", "               
               + this.allergy.getTYPECODE() + ", '"               
               + this.allergy.getReaction() + "', '"               
               + this.allergy.getPickID() + "', '" 
	       + oscar.oscarRx.util.RxUtil.DateToString(this.allergy.getStartDate()) + "', '"
               + this.allergy.getAgeOfOnset() + "', '"
               + this.allergy.getSeverityOfReaction() + "', '"
               + this.allergy.getOnSetOfReaction()+"','"
               + this.allergy.getRegionalIdentifier()+"')";
               
               b = db.RunSQL(sql);
               
               sql = "SELECT Max(allergyid) FROM allergies";               
               ResultSet rs = db.GetSQL(sql);
               
               if (rs.next()) {                  
                  this.allergyId = rs.getInt(1);                  
               }
               
               rs.close();               
            } else {
               
               sql = "UPDATE allergies SET entry_date = '" + oscar.oscarRx.util.RxUtil.DateToString(this.getEntryDate()) + "', "
               + "DESCRIPTION = '" + allergy.getDESCRIPTION() + "', "               
               + "HICL_SEQNO = " + allergy.getHICL_SEQNO() + ", "               
               + "HIC_SEQNO = " + allergy.getHIC_SEQNO() + ", "               
               + "AGCSP = " + allergy.getAGCSP() + ", "
               + "AGCCS = " + allergy.getAGCCS() + ", "
               + "TYPECODE = " + allergy.getTYPECODE() + ", "
               + "reaction = '" + allergy.getReaction() + "', "               
               + "drugref_id = '" + allergy.getPickID() + "' " 
	       + "start_date = '" + oscar.oscarRx.util.RxUtil.DateToString(allergy.getStartDate()) + "', "
               + "age_of_onset ='"+allergy.getAgeOfOnset() + "',"
               + "severity_of_reaction = '"+allergy.getSeverityOfReaction() + "',"
               + "onset_of_reaction = '"+allergy.getOnSetOfReaction() + "' "
               + "WHERE allergyid = " + this.getAllergyId();
               
               b = db.RunSQL(sql);               
            }
            
            return b;            
         }
         
      }
      
      public class Disease {         
         int diseaseId;         
         String ICD9;         
         java.util.Date entryDate;
         
         public Disease(int diseaseId, String ICD9, java.util.Date entryDate) {            
            this.diseaseId = diseaseId;            
            this.ICD9 = ICD9;            
            this.entryDate = entryDate;            
         }
         
         public int getDiseaseId() {            
            return this.diseaseId;            
         }
         
         public String getICD9() {            
            return this.ICD9;            
         }
         
         public void setICD9(String RHS) {            
            this.ICD9 = RHS;            
         }
         
         public RxCodesData.Disease getDisease() {            
            return new RxCodesData().getDisease(this.getICD9());            
         }
         
         public java.util.Date getEntryDate() {            
            return this.entryDate;            
         }
         
         public void setEntryDate(java.util.Date RHS) {            
            this.entryDate = RHS;            
         }
         
         public boolean Save() throws SQLException {            
            boolean b = false;            
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);            
            b = this.Save(db);            
            return b;            
         }
         
         public boolean Save(DBHandler db) throws SQLException {            
            boolean b;            
            String sql;            
            if (this.getDiseaseId() == 0) {               
               sql = "INSERT INTO diseases (demographic_no, ICD9, entry_date) "               
               + "VALUES (" + Patient.this.getDemographicNo() + ", '"               
               + this.getICD9() + "', '" + this.getEntryDate() + "')";               
               b = db.RunSQL(sql);               
               
               sql = "SELECT Max(diseaseid) FROM diseases";               
               ResultSet rs = db.GetSQL(sql);
               
               if (rs.next()) {                  
                  this.diseaseId = rs.getInt(1);                  
               }
               
               rs.close();               
            } else {               
               sql = "UPDATE diseases SET ICD9 = '" + this.getICD9() + "', "               
               + "entry_date = '" + this.getEntryDate().toString() + "' "               
               + "WHERE diseaseid = " + this.getDiseaseId();               
               b = db.RunSQL(sql);               
            }
            
            return b;            
         }
         
      }
      
   }
   
}
