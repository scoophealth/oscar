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


package oscar.oscarReport.pageUtil;

import org.apache.struts.action.ActionForm;

public final class RptRehabStudyForm extends ActionForm {

    String formName;    
    String startDate;
    String endDate;

    public String getFormName(){
       return formName;
    }

    public void setFormName(String formName){
       this.formName = formName;
    }
    
    public String getStartDate(){
        return startDate;
        
    }
    
    public void setStartDate(String startDate){
        this.startDate = startDate;
    }
    
    public String getEndDate(){
        return endDate;
        
    }
    
    public void setEndDate(String endDate){
        this.endDate = endDate;
    }
    
}
