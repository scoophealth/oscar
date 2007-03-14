/**
 *  Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
 *  This software is published under the GPL GNU General Public License.
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU General Public License
 *  as published by the Free Software Foundation; either version 2
 *  of the License, or (at your option) any later version. *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU General Public License for more details. * * You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. *
 * 
 *  Jason Gallagher
 * 
 *  This software was written for the
 *  Department of Family Medicine
 *  McMaster University
 *  Hamilton
 *  Ontario, Canada   Creates a new instance of LabResultData
 *
 * LabResultData.java
 *
 * Created on April 21, 2005, 4:31 PM
 */

package oscar.oscarLab.ca.on;

import java.util.Date;
import java.util.Comparator;
import oscar.oscarLab.ca.bc.PathNet.PathnetResultsData;
import oscar.oscarLab.ca.on.CML.CMLLabTest;
import oscar.util.UtilDateUtilities;


/**
 *
 * @author Jay Gallagher
 */
public class LabResultData implements Comparable{
   
   public static String CML = "CML";
   public static String MDS = "MDS";
   public static String EXCELLERIS = "BCP"; //EXCELLERIS
   public String segmentID;
   public String labPatientId;
   public String acknowledgedStatus;

   public String healthNumber;
   public String patientName;
   public String sex;
   public String resultStatus;
   public String dateTime;
   private Date dateTimeObr;
   public String priority;
   public String requestingClient;
   public String discipline;
   public String reportStatus;   
   public boolean abn = false;
   public String labType; // ie CML or MDS
   public boolean finalRes = true;
   public boolean isMatchedToPatient = true;
   
   public LabResultData() {
   }
   
   public LabResultData(String labT) {
      if (CML.equals(labT)){
         labType = CML;
      }else if (MDS.equals(labT)){
         labType = MDS;
      }else if (EXCELLERIS.equals(labT)){
          labType = EXCELLERIS;
      }
      
   }
   
   
   public boolean isAbnormal(){ 
       if (EXCELLERIS.equals(this.labType)){
          PathnetResultsData prd = new PathnetResultsData();
          if (prd.findPathnetAdnormalResults(this.segmentID) > 0){
               this.abn= true;
          }
       }else if(CML.equals(this.labType)){
          CMLLabTest cml = new CMLLabTest(); 
          if (cml.findCMLAdnormalResults(this.segmentID) > 0){
               this.abn= true;
          }
       }
       
       return abn ; 
       
       
   }
   
   
   public boolean isFinal(){ return finalRes ;}
   
   public boolean isMDS(){
      boolean ret = false;
      if (MDS.equals(labType)){ ret = true; }
      return ret;
   }
   
   public boolean isCML(){
      boolean ret = false;
      if (CML.equals(labType)){ ret = true; }
      return ret;
   }   
   
   public String getDiscipline(){
       if (CML.equals(this.labType)){
         CMLLabTest cml = new CMLLabTest();  
         this.discipline = cml.getDiscipline(this.segmentID);
       }else if (EXCELLERIS.equals(this.labType)){
          PathnetResultsData prd = new PathnetResultsData();
          this.discipline = prd.findPathnetDisipline(this.segmentID);
       }
       
       return this.discipline;
   }
   
   public String getPatientName(){
       return this.patientName;
   }
   
   public String getHealthNumber(){
       return this.healthNumber;
   }
 
   public String getSex(){
       return this.sex;
   }
   
   public boolean isMatchedToPatient(){
//       if (EXCELLERIS.equals(this.labType)){
//          PathnetResultsData prd = new PathnetResultsData();
//          this.isMatchedToPatient = prd.isLabLinkedWithPatient(this.segmentID);
//       }
       CommonLabResultData commonLabResultData = new CommonLabResultData();
       this.isMatchedToPatient = commonLabResultData.isLabLinkedWithPatient(this.segmentID,this.labType);
       return this.isMatchedToPatient;
   }
    
   
   public String getDateTime(){  
      if (EXCELLERIS.equals(this.labType)){
          PathnetResultsData prd = new PathnetResultsData();
          this.dateTime = prd.findPathnetObservationDate(this.segmentID);
      }
      return this.dateTime;
   }
   
   
   
   public String getReportStatus(){
      if (EXCELLERIS.equals(this.labType)){
          PathnetResultsData prd = new PathnetResultsData();
          this.reportStatus = prd.findPathnetStatus(this.segmentID);
      }
      return this.reportStatus;
   }
            
   public String getPriority(){
       return this.priority;
   }
   
   
     
   public String getRequestingClient(){
       if (EXCELLERIS.equals(this.labType)){
          PathnetResultsData prd = new PathnetResultsData();
          this.requestingClient = prd.findPathnetOrderingProvider(this.segmentID);
      }
       return this.requestingClient;
   }

   public Date getDateObj(){
       if (EXCELLERIS.equals(this.labType)){    
          this.dateTimeObr = UtilDateUtilities.getDateFromString(this.getDateTime(), "yyyy-MM-dd HH:mm:ss");
       }
     
      return this.dateTimeObr;
   }
   
   public void setDateObj(Date d){
       this.dateTimeObr = d;
   }
   
    public int compareTo(Object object) {
        int ret = 0;
        if (this.getDateObj() != null){
            if (this.dateTimeObr.after( ((LabResultData) object).getDateObj() )){
                ret = -1;
            }else if(this.dateTimeObr.before( ((LabResultData) object).getDateObj() )){
                ret = 1;
            }
        }
        return ret;
    }
                
    public CompareId getComparatorId() {
        return new CompareId();
    }
    
    
    public class CompareId implements Comparator {
        
        public int compare( Object o1, Object o2 ) {
            LabResultData lab1 = (LabResultData)o1;
            LabResultData lab2 = (LabResultData)o2;
            
            int labPatientId1 = Integer.parseInt(lab1.labPatientId);
            int labPatientId2 = Integer.parseInt(lab2.labPatientId);
            
            if( labPatientId1 < labPatientId2 )
                return -1;
            else if( labPatientId1 > labPatientId2 )
                return 1;
            else
                return 0;
        }
    }
   
   
}




