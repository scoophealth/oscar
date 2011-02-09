/*
 * WorkFlow.java
 *
 * Created on November 7, 2006, 10:14 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package oscar.oscarWorkflow;

import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

/**
 * Generic WorkFlow Description.  
 * 
 
 * @author jay
 */
public interface WorkFlow {
    public ArrayList getActiveWorkFlowList(String demographicNo);
    public ArrayList getActiveWorkFlowList();
    public String getState(String state);  
    public List getStates();
    public WorkFlowInfo executeRules(Hashtable hashtable);
    public WorkFlowInfo executeRules(WorkFlowDS wfDS,Hashtable hashtable);
    public String getLink(String demographic,String workFlowId);
    int addToWorkFlow(String providerNo, String demographicNo, Date endDate);
    public WorkFlowDS getWorkFlowDS();
    
}
