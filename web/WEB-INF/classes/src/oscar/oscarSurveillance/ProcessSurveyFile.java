/**
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
 * Jason Gallagher
 *
 * This software was written for the
 * Department of Family Medicine
 * McMaster University
 * Hamilton
 * Ontario, Canada   
 *
 *
 * ProcessSurveyFile.java
 *
 * Created on October 4, 2004, 9:57 AM
 */

package oscar.oscarSurveillance;

import java.io.*;
import java.sql.*;
import oscar.*;
import oscar.oscarDB.*;

/**
 *
 * @author  Jay Gallagher
 */
public class ProcessSurveyFile {
   
   
   
   private int maxProcessed(String surveyId){
      int maxprocessed = 0 ;
      try{
         DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
         String sql = "select max(processed) as maxprocessed from surveyData  where surveyId = '"+surveyId+"'  ";
         
         ResultSet rs = db.GetSQL(sql);
         
         if(rs.next()){
            maxprocessed = rs.getInt("maxprocessed");            
         }            
         rs.close();
         db.CloseConn();
         
      }catch(Exception e){
         e.printStackTrace();
      }
      return maxprocessed;
   }
   
   private void setProcessed(String surveyDataId, int processedId){
      try{
         DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
         String sql = "update surveyData set processed = '"+processedId+"' where surveyDataId = '"+surveyDataId+"'  ";         
         db.RunSQL(sql);                           
         db.CloseConn();         
      }catch(Exception e){
         e.printStackTrace();
      }
   }
   
   private void writeSurveyFile(ResultSet rs,String surveyId) throws SQLException{
      int processedId = maxProcessed(surveyId);
      processedId++;
      String fileDir = OscarProperties.getInstance().getProperty("surveillance_directory");
      String filename = surveyId+Integer.toString(processedId)+".txt";
      
      String qChar = "\"";
      //open file
      try {
        BufferedWriter out = new BufferedWriter(new FileWriter(fileDir+filename));
        
        while(rs.next()){
           String surveyDataId = rs.getString("surveyDataId");
           String dateSeen = rs.getString("survey_date");
           String patientAnswer = rs.getString("answer");
           String yearOfBirth = rs.getString("year_of_birth");
           String fsa = rs.getString("postal");
           if (fsa.length() > 3){
              fsa = fsa.substring(0,3);
           }
           
           out.write(qChar+dateSeen+qChar+"\t"+qChar+patientAnswer+qChar+"\t"+qChar+yearOfBirth+qChar+"\t"+qChar+fsa+qChar+"\n");
           setProcessed(surveyDataId,processedId);
        }
        
        out.close();
      } catch (IOException e) {
         e.printStackTrace();
      }
      //close file
      
   }
   
   
   public synchronized String processSurveyFile(String surveyId){
      String sStatus = null;
      int numRecordsToProcess = 0;
      try{
         DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
         String processCount = "select count(surveyDataId) as recordsForProcessing from surveyData  where surveyId = '"+surveyId+"' and processed is null and status = 'A'";
         
         ResultSet rs = db.GetSQL(processCount);
         
         if (rs.next()){
            numRecordsToProcess = rs.getInt("recordsForProcessing");
            
            if (numRecordsToProcess > 0){                  
               String sql = //"select * from surveyData where to_days(survey_date) < to_days('"+endDate+"'))
               "select s.surveyDataId, s.survey_date, s.answer, d.year_of_birth,d.postal from surveyData s, demographic d where s.surveyId = '"+surveyId+"' and s.demographic_no = d.demographic_no  and processed is null and status = 'A'";         
               rs = db.GetSQL(sql);         
               writeSurveyFile(rs,surveyId);
            }
         }
         rs.close();
         db.CloseConn();
         
      }catch(Exception e){
         e.printStackTrace();
      }
      return sStatus;
   }

   
   /** Creates a new instance of ILISurveyFile */
   public ProcessSurveyFile() {
   }
   
   
   
}
