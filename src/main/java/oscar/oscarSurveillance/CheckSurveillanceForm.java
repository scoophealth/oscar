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
 * CheckSurveillanceForm.java
 *
 * Created on September 10, 2004, 11:37 AM
 */

package oscar.oscarSurveillance;


import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.oscarehr.util.MiscUtils;

/**
 *
 * @author  Jay Gallagher
 */
public class CheckSurveillanceForm extends ActionForm {
   private static Logger log = MiscUtils.getLogger();
   
   String proceed =null;
   String demographicNo = null;
   
   /** Creates a new instance of CheckSurveillanceForm */
   public CheckSurveillanceForm() {
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
    * Getter for property demographicNo.
    * @return Value of property demographicNo.
    */
   public java.lang.String getDemographicNo() {
      log.debug("CheckSurveillanceForm.getDemographicNo called :"+demographicNo);
      return demographicNo;
   }
   
   /**
    * Setter for property demographicNo.
    * @param demographicNo New value of property demographicNo.
    */
   public void setDemographicNo(java.lang.String demographicNo) {
      log.debug("CheckSurveillanceForm.setDemographicNo called :"+demographicNo);
      this.demographicNo = demographicNo;
   }
   
}
