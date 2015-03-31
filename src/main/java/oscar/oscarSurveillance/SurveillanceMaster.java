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
 * SurveillanceMaster.java
 *
 * Created on September 10, 2004, 3:02 PM
 */

package oscar.oscarSurveillance;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Hashtable;

import org.apache.commons.digester.Digester;
import org.apache.log4j.Logger;
import org.oscarehr.util.MiscUtils;
import org.xml.sax.SAXException;

import oscar.OscarProperties;

/**
 * Manages Surveys currently loaded
 * @author Jay Gallagher
 */
public   class SurveillanceMaster {
    private static Logger log = MiscUtils.getLogger();

   static SurveillanceMaster surveillanceMaster = new SurveillanceMaster();
   static boolean loaded = false;
   static ArrayList<Survey> surveyList = null;
   static Hashtable surveyTable = null;

   /**
    * Return boolean value whether the initSurvey method has been called to configure the SurveillanceMaster
    * @return true if initSurvey has been called false if it hasn't
    */
   public static boolean isLoaded(){
      boolean isLoaded = true;
      if (surveyList == null){
         isLoaded = false;
      }
      return isLoaded;
   }

   /**
    * used to get an Instance of SurveillanceMaster
    * @return Instance of SurveillanceMaster
    */
   public static SurveillanceMaster getInstance() {
      if (!isLoaded()){
         initSurvey();
      }
      return surveillanceMaster;
   }

   /**
    * Used to get the Survey Class for a SurveyId
    * @param surveyId id of the survey
    * @return survey with id of surveyId
    */
   public Survey getSurveyById(String surveyId){
      return (Survey) surveyTable.get(surveyId);
   }

   /** Creates a new instance of SurveillanceMaster */
   protected SurveillanceMaster() {
      log.debug("SurveillanceMaster Initialized");

   }

   /**
    * Get all survey classes currently loaded
    * @return Collection of Survey Classes
    */
   public ArrayList<Survey> getCurrentSurveys(){
      return surveyList;
   }

   private void clearSurveys(){
      surveyList = null;
      surveyTable = null;
      surveyList = new ArrayList<Survey>();
      surveyTable = new Hashtable();
   }

   /**
    * Add a survey to the list of currently run surveys
    * @param s Survey
    */
   public void addSurvey(Survey s){
      log.debug("addSurvey(Survey s) gets called");
      if (surveyList == null){
         clearSurveys();
      }
      surveyTable.put(s.getSurveyId(),s);
      surveyList.add(s);
   }

   /**
    * Find the number of surveys currently loaded
    * @return number of surveys loaded
    */
   public static int numSurveys(){
      int retval = -1;
      if (surveyList != null){
          retval = surveyList.size();
      }
      return retval;
   }

   public static void displaySurveys(){
      if(surveyList != null){
      for (int i = 0 ; i < surveyList.size();i++){
         Survey s = surveyList.get(i);
         log.debug("title "+ s.getSurveyTitle()+ " randomness "+s.getRandomness()+" period "+s.getPeriod());
         log.debug("survey Question: "+s.getSurveyQuestion());
         s.displayProvidersInSurvey();
         s.displayAnswers2();
      }
      }
   }

   /**
    * Find if any surveys are available
    * @return true if there are surveys loaded false if none
    */
   public static boolean surveysEmpty(){
      boolean surveysempty = true;
      if(surveyList != null && surveyList.size() != 0){
         surveysempty = false;
      }
      return surveysempty;
   }

   /**
    * Load surveys from the survey config file.  Name of the file is define in the oscar.properties file.
    * the Key is: surveillance_config_file
    *
    * XML format ex
          <PRE>

         <surveillance-config>
            <survey surveyTitle="Flu Survey 2004 / 2005 survey" randomness="10" period="1" surveyId="flu04">
                <surveyQuestion>Does the patient have sore throat, arthralgia, myalgia, or prostration which could be due to influenza virus.</surveyQuestion>
                <provider>999998</provider>
                <provider>999999</provider>
                <answer status="D" value="">Ask Later</answer>
                <answer status="A" value="Y">Yes</answer>
                <answer status="A" value="N">No</answer>
                <answer status="A" value="R">Don't Ask Again</answer>
             </survey>
          </surveillance-config>

    *     </PRE>
    * More than one survey element can be defined
    *
    * surveyTitle.....Title of the survey, This will display at the top of the page.
    * randomness......The random sample to be selected ie 10 = 1 in 10
    * period..........The number of days not include this patient in the survey once they have been asked.
    * surveyId........The Id of the survey, must be unique and a limit of 5 characters
    * surveyQuestion..Question of the survey,  This will display under the title of the survey on the page.
    * provider........One element for each provider paticipating in the survey. Provider will not be include in the survey if they are not defined.
    * answer..........Possible answer of the survey. Each element with display on the page as a button
    *   status.......Status of the question when answered. A = answered , D = deffered, When a question is deffered it will be asked again without effecting the random sample.
    *   value........Short form of the question answer.  Used to run queries on results.
    */
   public static void initSurvey(){
      Digester digester = new Digester();
      digester.push(surveillanceMaster); // Push controller servlet onto the stack
      digester.setValidating(false);

      digester.addObjectCreate("surveillance-config/survey",Survey.class);
      digester.addSetProperties("surveillance-config/survey");
      digester.addBeanPropertySetter("surveillance-config/survey/surveyQuestion","surveyQuestion");
      digester.addBeanPropertySetter("surveillance-config/survey/patientCriteria","patientCriteria");
      digester.addBeanPropertySetter("surveillance-config/survey/exportQuery","exportQuery");
      digester.addBeanPropertySetter("surveillance-config/survey/exportString","exportString");
      digester.addCallMethod("surveillance-config/survey/provider","addProvider",0);
      digester.addCallMethod("surveillance-config/survey/answer","addAnswer",3);
      digester.addCallParam("surveillance-config/survey/answer",0);
      digester.addCallParam("surveillance-config/survey/answer",1,"value");
      digester.addCallParam("surveillance-config/survey/answer",2,"status");
      digester.addSetNext("surveillance-config/survey","addSurvey");



      String filename = OscarProperties.getInstance().getProperty("surveillance_config_file");
      if (filename != null){
         FileInputStream input = null;
         try{
            input = new FileInputStream(filename) ;
         }catch(Exception eio){
            if (input == null){
               log.error("OSCAR SURVEILLANCE ERROR:  could not find file :"+filename,eio);
            }
            surveyList = new ArrayList<Survey>();
            surveyTable = new Hashtable();
         }

         try {
            digester.parse(input);
            input.close();
         }
         catch (SAXException e) { log.error("filename :"+filename,e); }
         catch(Exception eio2){ log.error("filename :"+filename,eio2); }


         if(surveyList == null){
            surveyList  = new ArrayList<Survey>();
            surveyTable = new Hashtable();
            log.error("OSCAR SURVEILLANCE ERROR: could not load from file "+filename);
         }
      }else{
         log.debug("OSCAR SURVEILLANCE -- module not initialized");
         surveyList  = new ArrayList<Survey>();
         surveyTable = new Hashtable();
      }
      log.debug(numSurveys());
      displaySurveys();

   }
}
