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


package oscar.oscarPrevention.pageUtil;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.apache.struts.action.ActionForm;

/**
 *
 * @author Jay Gallagher
 */
public class PreventionReportForm extends ActionForm{
   
   
   public PreventionReportForm() {
   }
   
   /**
    * Getter for property setName.
    * @return Value of property setName.
    */
   public java.lang.String getSetName() {
      return setName;
   }
   
   /**
    * Setter for property setName.
    * @param setName New value of property setName.
    */
   public void setSetName(java.lang.String setName) {
      this.setName = setName;
   }
   
   /**
    * Getter for property prevention.
    * @return Value of property prevention.
    */
   public java.lang.String getPrevention() {
      return prevention;
   }
   
   /**
    * Setter for property prevention.
    * @param prevention New value of property prevention.
    */
   public void setPrevention(java.lang.String prevention) {
      this.prevention = prevention;
   }
   
   /**
    * Getter for property patientSet.
    * @return Value of property patientSet.
    */
   public java.lang.String getPatientSet() {
      return patientSet;
   }
   
   /**
    * Setter for property patientSet.
    * @param patientSet New value of property patientSet.
    */
   public void setPatientSet(java.lang.String patientSet) {
      this.patientSet = patientSet;
   }
   
   /**
    * Getter for property asofDate.
    * @return Value of property asofDate.
    */
   public java.lang.String getAsofDate() {
      if (asofDate == null){
         Calendar today = Calendar.getInstance();          
         Format formatter = new SimpleDateFormat("yyyy-MM-dd");
         asofDate = formatter.format(today.getTime());             
      }
      return asofDate;
   }   
   
   /**
    * Setter for property asofDate.
    * @param asofDate New value of property asofDate.
    */
   public void setAsofDate(java.lang.String asofDate) {
      this.asofDate = asofDate;
   }
   
   
   String asofDate = null;
   String setName = null;
   String prevention  = null;
   String patientSet = null;
}
