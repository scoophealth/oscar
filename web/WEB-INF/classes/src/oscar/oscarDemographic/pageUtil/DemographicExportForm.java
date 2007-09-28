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
 * Ontario, Canada   Creates a new instance of DemographicExportForm
 *
 *
 * DemographicExportForm.java
 *
 * Created on June 29, 2005, 11:49 AM
 */

package oscar.oscarDemographic.pageUtil;

import org.apache.struts.action.*;

/**
 *
 * @author Jay Gallagher
 */
public class DemographicExportForm extends ActionForm {
         
   String patientSet = null;
   String mediaType = null;
   String noOfMedia = null;
   
   public DemographicExportForm() {
   }
   
   /**
    * Getter for properties.
    * @return Value of properties.
    */
   public java.lang.String getPatientSet() {
      return patientSet;
   }
   public java.lang.String getMediaType() {
       return mediaType;
   }
   public java.lang.String getNoOfMedia() {
       return noOfMedia;
   }
   
   /**
    * Setter for properties
    * @param patientSet New value of properties.
    */
   public void setPatientSet(java.lang.String patientSet) {
      this.patientSet = patientSet;
   }
   public void setMediaType(java.lang.String mediaType) {
       this.mediaType = mediaType;
   }
   public void setNoOfMedia(java.lang.String noOfMedia) {
       if (Integer.parseInt(noOfMedia)>0) this.noOfMedia = noOfMedia;
       else this.noOfMedia = "1";
   }
}
