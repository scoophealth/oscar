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


/*
 * Survey.java
 *
 * Created on September 10, 2004, 3:04 PM
 */

package oscar.oscarSurveillance;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Random;

import org.apache.log4j.Logger;
import org.oscarehr.util.MiscUtils;

import oscar.oscarDB.DBHandler;

/**
 *
 * @author  Jay Gallagher
 */
public class Survey {
   private static Logger log = MiscUtils.getLogger();
   
   static long seed = 0;
   
   String surveyTitle;
   String surveyQuestion;
   String surveyId;
   String patientCriteria = null;
   String exportString = null;
   String exportQuery = null;
   int randomness;
   int period;// num days
   ArrayList providersParticipating = null;
   private ArrayList answers = null;
   private Hashtable answerTable = null;
   String surveyStatus = "";
   
   public static String DEFFERED = "D";
   public static String NOTSELECTED = "P";
   public static String ANSWERED = "A";
   public static String DOESNOTMEETCRITERIA = "C";
   Random random = null;
   
   /** Creates a new instance of Survey */
   public Survey() {
      initRandom();
   }
   
   private boolean hasPatientCriteria(){
      boolean hasPatientCriteria = true;
      if ( patientCriteria == null){
         hasPatientCriteria = false;
      }
      return hasPatientCriteria;
   }
   
   private void initRandom(){
      while (seed == System.currentTimeMillis());
      seed = System.currentTimeMillis();
      log.debug("setting seed "+seed );
      random =  new Random(seed);      
   }
   
   /**
    * Patient Demographic is used to find out if this patient has been deffered from this Survey
    */
   boolean isDeffered(String status){
      boolean isdeffered = false;
      if (status != null && status.equals("D")){
         isdeffered = true;
      }    
      log.debug("Patient deffered: "+isdeffered +" status was "+status);
      return isdeffered; // true;  //TODO
   }
   
   boolean isPatientSeenInPeriod(String status){
      boolean patientseen = false;
      if (status != null && ( status.equals("P") || status.equals("A") || status.equals("C")) ){
         patientseen = true;
      }
      log.debug("Was patient seen in this period: "+patientseen+ " status was "+status);
      return patientseen ;//false;  //TODO    
   }
   
   boolean doesPatientMeetCriteria(String demographic_no){
      boolean doespatientmeetcriteria = true;
      if (hasPatientCriteria()){
          log.debug("Survey "+surveyTitle+" has patientCriteria");
          doespatientmeetcriteria = isDemographicSelected(demographic_no,patientCriteria);
      }
      log.debug("Does patient Meet Criteria "+doespatientmeetcriteria);
      return doespatientmeetcriteria; //TODO
   }
   
   public boolean isDemographicSelected(String demographicNo,String queryString){
       boolean isdemographicSelected = true;
       int num = 0;
       try {
            
            ResultSet rs;            
            String sql = "SELECT count(demographic_no) as demoCount FROM demographic WHERE demographic_no = '" + demographicNo +"' and "+queryString;            
            if ( queryString != null && queryString.trim().startsWith("FROM") ){
               sql = "SELECT count(demographic.demographic_no) as demoCount "+queryString+" and demographic.demographic_no = '" + demographicNo +"'";
            }
            rs = DBHandler.GetSQL(sql);            
            if (rs.next()) {
               num = rs.getInt("demoCount");
            }
            log.debug(" num of demos that match criteria "+num);
            if (num == 0){
               isdemographicSelected = false;
            }            
            rs.close();
            
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
       return isdemographicSelected;
    }
   
   boolean isPatientSelected(String demograpic_no){
      return true; //TODO
   }
   
   boolean isPatientRandomlySelected(){
      boolean isPatientSelect = false;            
      int randomValue = random.nextInt(randomness);
      if (randomValue == 0){
         isPatientSelect = true;
      }          
      log.debug("Is Patient Randomly Selected :"+isPatientSelect);
      return  isPatientSelect; //true; //TODO
   }
   
   boolean isInSurvey(String demographic_no,String provider_no){
      boolean isinsurvey = false;    
      String sStatus = null;
      if(isProviderInSurvey(provider_no)){      
         // Get all records for this period 
         sStatus = getSurveyStatusForPeriod(demographic_no);
         
         
         if(isDeffered(sStatus)){  //in this period
              isinsurvey = true;
         }else{
            if (!isPatientSeenInPeriod(sStatus)){            
               if (doesPatientMeetCriteria(demographic_no)){
                  if(isPatientRandomlySelected()){
                     isinsurvey = true;
                     addPatientToPeriod(demographic_no,provider_no,Survey.DEFFERED);   
                  }else{
                     addPatientToPeriod(demographic_no,provider_no,Survey.NOTSELECTED);                  
                  }
               }         
            }
         }               
      }
      log.debug("Provider : "+provider_no + " Demo : "+demographic_no+"  in survey :"+isinsurvey);
      return isinsurvey;
   }
   
   private void getRecordsForPeriod(String demographic_no){
      try{
         
         String sql = //"select * from surveyData where to_days(survey_date) < to_days('"+endDate+"'))
         "select * from surveyData where demographic_no = '"+demographic_no+"' and (to_days(now()) - to_days(survey_date) < "+period+") ";
         
         ResultSet rs = DBHandler.GetSQL(sql);
         if(rs.next()){
            surveyStatus = oscar.Misc.getString(rs, "status");            
         }            
         rs.close();
         
      }catch(Exception e){
         MiscUtils.getLogger().error("Error", e);
      }
   }
   
   
   private String getSurveyStatusForPeriod(String demographic_no){
      String sStatus = null;
      try{
         
         String sql = //"select * from surveyData where to_days(survey_date) < to_days('"+endDate+"'))
         "select * from surveyData where surveyId = '"+surveyId+"' and demographic_no = '"+demographic_no+"' and (to_days(now()) - to_days(survey_date) < "+period+") ";
         
         ResultSet rs = DBHandler.GetSQL(sql);
         if(rs.next()){
            sStatus = oscar.Misc.getString(rs, "status");            
         }            
         rs.close();
         
      }catch(Exception e){
         MiscUtils.getLogger().error("Error", e);
      }
      return sStatus;
   }

   
   private String addPatientToPeriod(String demographic_no,String provider_no, String status){
      return addPatientToPeriod(demographic_no, provider_no, status,"");
   }
   
   private String addPatientToPeriod(String demographic_no,String provider_no, String status,String answer){
      //This will beused to set patient to been seen in this period.
      String insertId = "";
      try{
         
         String sql = 
         "insert into surveyData ( surveyId, demographic_no,provider_no,status,answer,survey_date) values "
            +"("
            +"'"+surveyId+"',"
            +"'"+demographic_no+"',"
            +"'"+provider_no+"',"
            +"'"+status+"',"
            +"'"+answer+"',"
            +"now())";
         
         DBHandler.RunSQL(sql);
         ResultSet rs = DBHandler.GetSQL("SELECT LAST_INSERT_ID()");
         if (rs.next()){
            insertId = oscar.Misc.getString(rs, 1);
         }         
      }catch(Exception e){
         MiscUtils.getLogger().error("Error", e);
      }
      return insertId;
   }
   
   
   
   public static String YES = "Y";
   public static String NO = "N";   
   public static String DONTASKAGAIN = "R";
    
   
   
   
   private String getSurveyIdForDemographic(String demographic_no){
      String surveyDataId = null;
      try{
         
         String sql = //"select * from surveyData where to_days(survey_date) < to_days('"+endDate+"'))
         "select * from surveyData where surveyId = '"+surveyId+"' and demographic_no = '"+demographic_no+"' and (to_days(now()) - to_days(survey_date) < "+period+") ";
         
         ResultSet rs = DBHandler.GetSQL(sql);
         if(rs.next()){
            surveyDataId = oscar.Misc.getString(rs, "surveyDataId");            
         }            
         rs.close();
         
      }catch(Exception e){
         MiscUtils.getLogger().error("Error", e);
      }
      return surveyDataId;
   }
   public void processAnswer(String provider_no,String demographic_no,String answer){
      String surveyDataId = getSurveyIdForDemographic(demographic_no);           
      Answer a = getAnswerByString(answer);
      if (surveyDataId == null){
         addPatientToPeriod(demographic_no,provider_no, a.answerStatus,a.answerValue);
      }else{   
         setAnswer(surveyDataId,answer);
      }
      
   }
   
   public void setAnswer(String surveyDataId,String answer){
      Answer a = getAnswerByString(answer);
      log.debug("Answer a :"+a.answerString +" answer "+answer);
      try{
         
         String sql = "update surveyData set "
                     +" status = '"+a.answerStatus+"',"
                     +" answer = '"+a.answerValue+"'"
                     +" where surveyDataId = "
                     +"'"+surveyDataId+"'";
         DBHandler.RunSQL(sql);         
      }catch(Exception e){
         MiscUtils.getLogger().error("Error", e);
      }      
   }
   
   
   
   public boolean hasExport(){
      boolean export = false;
      if (exportString != null && exportQuery != null){
         export = true;
      }
      return export;
   }
   
   
   
   //GETTERS AND SETTERS
   
   /**
    * Getter for property surveyTitle.
    * @return Value of property surveyTitle.
    */
   public java.lang.String getSurveyTitle() {
      return surveyTitle;
   }
   
   /**
    * Setter for property surveyTitle.
    * @param surveyTitle New value of property surveyTitle.
    */
   public void setSurveyTitle(java.lang.String surveyTitle) {
      this.surveyTitle = surveyTitle;
   }
   
   /**
    * Getter for property surveyQuestion.
    * @return Value of property surveyQuestion.
    */
   public java.lang.String getSurveyQuestion() {
      return surveyQuestion;
   }
   
   /**
    * Setter for property surveyQuestion.
    * @param surveyQuestion New value of property surveyQuestion.
    */
   public void setSurveyQuestion(java.lang.String surveyQuestion) {
      this.surveyQuestion = surveyQuestion;
   }
   
   /**
    * Getter for property surveyId.
    * @return Value of property surveyId.
    */
   public java.lang.String getSurveyId() {
      return surveyId;
   }
   
   /**
    * Setter for property surveyId.
    * @param surveyId New value of property surveyId.
    */
   public void setSurveyId(java.lang.String surveyId) {
      this.surveyId = surveyId;
   }
   
   public void addProvider(String p){
     if (providersParticipating == null){
        providersParticipating = new ArrayList();
     }
     if (!providersParticipating.contains(p)){
        providersParticipating.add(p);          
     }
   }
   
   public boolean isProviderInSurvey(String p){
      boolean isproviderinsurvey = false;
      if ( providersParticipating != null && providersParticipating.contains(p)){
        isproviderinsurvey = true;
      }
      log.debug("provider : "+p+" in survey :" +isproviderinsurvey);
      return isproviderinsurvey;
   }
   
   
   public void displayProvidersInSurvey(){
      if ( providersParticipating != null){
      for (int i = 0; i < providersParticipating.size(); i++){
         String pro = (String) providersParticipating.get(i);
         log.debug(pro);
      }
      }
   }
   
   
   /**
    * Getter for property period.
    * @return Value of property period.
    */
   public int getPeriod() {
      return period;
   }
   
   /**
    * Setter for property period.
    * @param period New value of property period.
    */
   public void setPeriod(int period) {
      this.period = period;
   }
   
   /**
    * Getter for property randomness.
    * @return Value of property randomness.
    */
   public int getRandomness() {
      return randomness;
   }
   
   /**
    * Setter for property randomness.
    * @param randomness New value of property randomness.
    */
   public void setRandomness(int randomness) {
      this.randomness = randomness;
   }
   
   public void addAnswer(String answerString,String answerValue, String answerStatus){
      log.debug("adding answer ");
      if (answers == null || answerTable == null){
         clearAnswers();
      }
      Answer a = new Answer(answerString,answerValue,answerStatus);
      answers.add(a);
      answerTable.put(answerString, a);
      
   }
   public void clearAnswers(){
      answers = null;
      answerTable = null;      
      answers = new ArrayList();
      answerTable = new Hashtable();
   }

   public Answer getAnswerByString(String answerString){
      Answer retval = null;
      if (answerTable != null){
         retval = (Answer)  answerTable.get(answerString);         
      }
      return retval;
   }
   
   public String getAnswerStringById(String answerID){
      String answerString = "";
      for( int i = 0 ; i < answers.size(); i++){
         Answer a = (Answer) answers.get(i);
         if( answerID.equals(a.answerValue) ){
            answerString = a.answerString;
         }
      }
      return answerString;
   }
   
   public Enumeration getAnswers(){
      return answerTable.keys();
   } 
   
   public ArrayList getAnswersList(){
      return answers;
   }
   
   public int numAnswers(){
      int numanswer = 0;
      if( answers != null){
         numanswer = answers.size();
      }
      return numanswer;
   }
   
   public String getAnswerString(int i){
      String ansStr = null;
      if(i >= 0 && i < answers.size()){
         Answer a = (Answer) answers.get(i);
         ansStr = a.answerString;
      }
      return ansStr;
   }
   
   public void displayAnswers(){
      for ( int i = 0 ; i < answers.size(); i++){                  
         Answer a =  (Answer) answers.get(i);
         if (a != null){
         log.debug(" answer "+a.answerString +" value "+ a.answerValue +" status "+a.answerStatus);
         }else{
            log.debug(" NO answers ");
         }
      }
   }
   
   public void displayAnswers2(){
      Enumeration e = getAnswers();
      while(e.hasMoreElements()){
         String s = (String) e.nextElement();
         Answer a =  getAnswerByString(s);
         if (a != null){
         log.debug("2 answer "+a.answerString +" value "+ a.answerValue +" status "+a.answerStatus);
         }else{
            log.debug(" NO answers ");
         }
      }
   }
   
   
   
   
   
   public ArrayList getStatusCount(String surveyId){
      ArrayList list = new ArrayList();
      try{
         
         String sql = "select status , count(status) as countstatus from surveyData where surveyId = '"+surveyId+"' group by status";
         ResultSet rs = DBHandler.GetSQL(sql);  
         
         while(rs.next()){
            String[] s =  {oscar.Misc.getString(rs, "status"),oscar.Misc.getString(rs, "countstatus")};
            list.add(s);
         }         
      }catch(Exception e){
         MiscUtils.getLogger().error("Error", e);
      }      
      return list;
   }
   
   public ArrayList getAnswerCount(String surveyId){
      ArrayList list = new ArrayList();
      try{
         
         String sql = "select answer , count(answer) as countanswer from surveyData where surveyId = '"+surveyId+"' and status = 'A'  group by answer;";
         
         ResultSet rs = DBHandler.GetSQL(sql);  
         
         while(rs.next()){
            String[] s =  {oscar.Misc.getString(rs, "answer"),oscar.Misc.getString(rs, "countanswer")};
            list.add(s);
         }         
      }catch(Exception e){
         MiscUtils.getLogger().error("Error", e);
      }      
      return list;
   }
   
   /**
    * Getter for property patientCriteria.
    * @return Value of property patientCriteria.
    */
   public java.lang.String getPatientCriteria() {
      return patientCriteria;
   }   
   
   /**
    * Setter for property patientCriteria.
    * @param patientCriteria New value of property patientCriteria.
    */
   public void setPatientCriteria(java.lang.String patientCriteria) {
      this.patientCriteria = patientCriteria;
   }   
   
   /**
    * Getter for property exportString.
    * @return Value of property exportString.
    */
   public java.lang.String getExportString() {
      return exportString;
   }
   
   /**
    * Setter for property exportString.
    * @param exportString New value of property exportString.
    */
   public void setExportString(java.lang.String exportString) {
      this.exportString = exportString;
   }
   
   /**
    * Getter for property exportQuery.
    * @return Value of property exportQuery.
    */
   public java.lang.String getExportQuery() {
      return exportQuery;
   }
   
   /**
    * Setter for property exportQuery.
    * @param exportQuery New value of property exportQuery.
    */
   public void setExportQuery(java.lang.String exportQuery) {
      this.exportQuery = exportQuery;
   }
   
   class Answer {
      public String answerString = "";
      public String answerValue = "";      
      public String answerStatus ="";
      
      public Answer(String answerString,String answerValue, String answerStatus){
         this.answerString = answerString;
         this.answerValue  = answerValue;
         this.answerStatus = answerStatus;
      
      }
   }
   
}
