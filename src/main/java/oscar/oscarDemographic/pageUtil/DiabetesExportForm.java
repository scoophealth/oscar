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


package oscar.oscarDemographic.pageUtil;

import org.apache.struts.action.ActionForm;

import oscar.util.UtilDateUtilities;

/**
 *
 * @author Ronnie Cheng
 */
public class DiabetesExportForm extends ActionForm {
         
    String patientSet = null;
    String startDate = "1900-01-01";
    String endDate = UtilDateUtilities.getToday("yyyy-MM-dd");
    
    public DiabetesExportForm() {
    }

    /**
    * Getter for properties.
    * @return Value of properties.
    */
    public String getPatientSet() {
        return patientSet;
    }
    public String getstartDate() {
        return startDate;
    }
    public String getendDate() {
        return endDate;
    }

    /**
    * Setter for properties
    * @param patientSet New value of properties.
    */
    public void setPatientSet(String patientSet) {
        this.patientSet = patientSet;
    }
    public void setStartDate(String startDate) {
        if (UtilDateUtilities.StringToDate(startDate, "yyyy-MM-dd")!=null) {
            this.startDate = startDate;
        }
    }
    public void setEndDate(String endDate) {
        if (UtilDateUtilities.StringToDate(endDate, "yyyy-MM-dd")!=null) {
            this.endDate = endDate;
        }
    }
}
