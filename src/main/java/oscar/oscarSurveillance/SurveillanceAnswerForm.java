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
 * SurveillanceAnswerForm.java
 *
 * Created on September 10, 2004, 8:08 PM
 */

package oscar.oscarSurveillance;

import org.apache.struts.action.ActionForm;

/**
 *
 * @author  Jay Gallagher
 */
public class SurveillanceAnswerForm extends ActionForm{
   
   String answer = null;
   
   String proceed = null;
   String demographicNo = null;
   String surveyId = null;
   String currentSurveyNum = null;
   
   /** Creates a new instance of SurveillanceAnswerForm */
   public SurveillanceAnswerForm() {
   }
   
   /**
    * Getter for property answer.
    * @return Value of property answer.
    */
   public java.lang.String getAnswer() {
      return answer;
   }
   
   /**
    * Setter for property answer.
    * @param answer New value of property answer.
    */
   public void setAnswer(java.lang.String answer) {
      this.answer = answer;
   }
   
   /**
    * Getter for property proceed.
    * @return Value of property proceed.
    */
   public java.lang.String getProceed() {
      return proceed;
   }
   
   /**
    * Setter for property proceed.
    * @param proceed New value of property proceed.
    */
   public void setProceed(java.lang.String proceed) {
      this.proceed = proceed;
   }
   
   /**
    * Getter for property demographic_no.
    * @return Value of property demographic_no.
    */
   public java.lang.String getDemographicNo() {
      return demographicNo;
   }
   
   /**
    * Setter for property demographic_no.
    * @param demographicNo New value of property demographic_no.
    */
   public void setDemographicNo(java.lang.String demographicNo) {
      this.demographicNo = demographicNo;
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
   
   /**
    * Getter for property currentSurveyNum.
    * @return Value of property currentSurveyNum.
    */
   public java.lang.String getCurrentSurveyNum() {
      return currentSurveyNum;
   }
   
   /**
    * Setter for property currentSurveyNum.
    * @param currentSurveyNum New value of property currentSurveyNum.
    */
   public void setCurrentSurveyNum(java.lang.String currentSurveyNum) {
      this.currentSurveyNum = currentSurveyNum;
   }
   
}
