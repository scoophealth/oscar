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


package oscar.oscarWorkflow;

import java.util.Date;
import java.util.Hashtable;

import org.oscarehr.util.MiscUtils;

import oscar.util.UtilDateUtilities;

/**
 *
 * @author jay
 */
public class WorkFlowInfo {
    
    private String ID = null;
    private String workflowType = null;
    private Date createDateTime = null;
    private String demographicNo = null;
    private Date completionDate = null;
    private String currentState = null;
    private String colour = null;
     
    /** Creates a new instance of WorkFlowInfo */
    public WorkFlowInfo() {
    }
    
    public WorkFlowInfo(Hashtable h){ 
       MiscUtils.getLogger().debug("loading data...");
       this.setID((String)            h.get("ID"));
       this.setWorkflowType((String)  h.get("workflow_type"));        
       this.setCreateDateTime((Date)  h.get("create_date_time") );
       this.setDemographicNo((String) h.get("demographic_no") );
       this.setCompletionDate((Date)  h.get("completion_date"));
       this.setCurrentState((String)  h.get("current_state"));
       MiscUtils.getLogger().debug("data loaded...");
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getWorkflowType() {
        return workflowType;
    }

    public void setWorkflowType(String workflowType) {
        this.workflowType = workflowType;
    }

    public Date getCreateDateTime() {
        return createDateTime;
    }

    public void setCreateDateTime(Date createDateTime) {
        this.createDateTime = createDateTime;
    }

    public String getDemographicNo() {
        return demographicNo;
    }

    public void setDemographicNo(String demographicNo) {
        this.demographicNo = demographicNo;
    }

    public Date getCompletionDate() {
        return completionDate;
    }

    public void setCompletionDate(Date completionDate) {
        this.completionDate = completionDate;
    }

    public String getCurrentState() {
        return currentState;
    }

    public void setCurrentState(String currentState) {
        this.currentState = currentState;
    }

    public String getColour() {
        return colour;
    }

    public void setColour(String colour) {
        this.colour = colour;
    }
    
    public boolean isCurrentState(String state){
        boolean is = false;
        if (state != null && currentState != null){
            if (state.equals(currentState)){
                is = true;
            }
        }
        return is;
    }

    public int getGestationAge(){
        //TODO: WHAT HAPPENS WITH NO EDD???
        int ret = -1;
        MiscUtils.getLogger().debug("GEST "+this.completionDate);
        if(this.completionDate != null){
           ret = UtilDateUtilities.calculateGestationAge( new Date(), this.completionDate);
        }
        return ret;
    }
   
    
}
