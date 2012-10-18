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

import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

import org.oscarehr.common.dao.WorkFlowDao;
import org.oscarehr.util.SpringUtils;

/**
 *
 * @author jay
 *
 *
 */
public class WorkFlowState {
    public final static String RHWORKFLOW = "RH";
    public final static String INIT_STATE = "1";
    
    private WorkFlowDao dao = SpringUtils.getBean(WorkFlowDao.class);
    

    public WorkFlowState() {
    }
    
    //TODO: need to add which provider added it  OR i could just logg it as well
    public int addToWorkFlow(String workflowType,String providerNo, String demographicNo, Date endDate,String current_state){
        org.oscarehr.common.model.WorkFlow wf = new  org.oscarehr.common.model.WorkFlow();
        wf.setWorkflowType(workflowType);
        wf.setProviderNo(providerNo);
        wf.setDemographicNo(demographicNo);
        wf.setCompletionDate(endDate);
        wf.setCurrentState(current_state);
        wf.setCreateDateTime(new Date());
        dao.persist(wf);
        
        return wf.getId();
    }
    
    public void updateWorkFlowState(String workflowId,String state ){
    	org.oscarehr.common.model.WorkFlow wf = dao.find(Integer.parseInt(workflowId));
    	if(wf != null) {
    		wf.setCurrentState(state);
    		dao.merge(wf);
    	}
    }
    
    public void updateWorkFlowState(String workflowId,String state, Date date ){
    	org.oscarehr.common.model.WorkFlow wf = dao.find(Integer.parseInt(workflowId));
    	if(wf != null) {
    		wf.setCurrentState(state);
    		wf.setCompletionDate(date);
    		dao.merge(wf);
    	}
    }
    
    
    public ArrayList getWorkFlowList(String workflowType){
    	ArrayList list = new ArrayList();
        
    	List<org.oscarehr.common.model.WorkFlow> ws = dao.findByWorkflowType(workflowType);
    	for(org.oscarehr.common.model.WorkFlow w:ws) {
    		Hashtable h = new Hashtable();
            h.put("ID",w.getId().toString());
            h.put("workflow_type", w.getWorkflowType());
            h.put("create_date_time", w.getCreateDateTime());
            h.put("demographic_no", w.getDemographicNo());
            if (w.getCompletionDate() != null){
               h.put("completion_date", w.getCompletionDate());
            }
            h.put("current_state", w.getCurrentState());
            list.add(h);
    	}

        return list;
    }
    
    public ArrayList getActiveWorkFlowList(String workflowType){
        ArrayList list = new ArrayList();
              
    	List<org.oscarehr.common.model.WorkFlow> ws = dao.findActiveByWorkflowType(workflowType);
    	for(org.oscarehr.common.model.WorkFlow w:ws) {
    		Hashtable h = new Hashtable();
            h.put("ID",w.getId().toString());
            h.put("workflow_type", w.getWorkflowType());
            h.put("create_date_time", w.getCreateDateTime());
            h.put("demographic_no", w.getDemographicNo());
            if (w.getCompletionDate() != null){
               h.put("completion_date", w.getCompletionDate());
            }
            h.put("current_state", w.getCurrentState());
            list.add(h);
    	}
        
        return list;
    }
    
    public ArrayList getActiveWorkFlowList(String workflowType, String demographicNo){
        ArrayList list = new ArrayList();
    	List<org.oscarehr.common.model.WorkFlow> ws = dao.findActiveByWorkflowTypeAndDemographicNo(workflowType,demographicNo);
    	for(org.oscarehr.common.model.WorkFlow w:ws) {
    		Hashtable h = new Hashtable();
            h.put("ID",w.getId().toString());
            h.put("workflow_type", w.getWorkflowType());
            h.put("create_date_time", w.getCreateDateTime());
            h.put("demographic_no", w.getDemographicNo());
            if (w.getCompletionDate() != null){
               h.put("completion_date", w.getCompletionDate());
            }
            h.put("current_state", w.getCurrentState());
            list.add(h);
    	}
        return list;
    }
    
    
    public static String rhState(Object s){
         Hashtable h = new Hashtable();
         h.put("1","No Appt made");
         h.put("2","Appt Booked");
         h.put("3","Injection 28");
         h.put("4","Missed Appt");
         h.put("C","Closed");
         String ss = (String) s;
         return (String) h.get(ss);
    }
}
