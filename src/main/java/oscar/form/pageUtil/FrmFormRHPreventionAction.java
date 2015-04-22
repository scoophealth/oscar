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


package oscar.form.pageUtil;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.oscarehr.managers.SecurityInfoManager;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.form.FrmRecord;
import oscar.form.FrmRecordFactory;
import oscar.log.LogAction;
import oscar.log.LogConst;
import oscar.oscarWorkflow.WorkFlow;
import oscar.oscarWorkflow.WorkFlowFactory;
import oscar.oscarWorkflow.WorkFlowState;
import oscar.util.UtilDateUtilities;

/*
 
 CREATE TABLE `formRhImmuneGlobulin` (
  `ID` int(10) NOT NULL auto_increment,
  `demographic_no` int(10) NOT NULL default '0',
  `provider_no` int(10) default NULL,
  `formCreated` date default NULL,
  `formEdited` timestamp(14) NOT NULL,
  `workflowId` int(10),
 
  `state`               char(1),
  `dateOfReferral`      datetime,
  `edd`                 datetime,
  `motherSurname`       varchar(30), 
  `motherFirstname`     varchar(30), 
  `dob`                 date,
  `motherHIN`           varchar(20),
  `motherVC`            varchar(30),
  `motherAddress`       varchar(60),
  `motherCity`          varchar(60),
  `motherABO`           char(3),
  `motherRHtype`        char(4),
  `hospitalForDelivery` varchar(255), 
  `refPhySurname`       varchar(30), 
  `refPhyFirstname`     varchar(30), 
  `refPhyAddress`       varchar(60), 
  `refPhyPhone`         varchar(20),
  `refPhyFax`           varchar(20),
                     
  
  PRIMARY KEY  (`ID`)
) TYPE=MyISAM
 
 
 
 */



 /* 
 * @author jay
 */
public class FrmFormRHPreventionAction extends Action{
	
	private SecurityInfoManager securityInfoManager = SpringUtils.getBean(SecurityInfoManager.class);
    
    public ActionForward execute(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response){    
        MiscUtils.getLogger().debug("FrmFormRHPrevention Action");
        
        if(!securityInfoManager.hasPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), "_form", "w", null)) {
			throw new SecurityException("missing required security object (_form)");
		}
        
        // <action path="/form/AddRHWorkFlow" scope="request" name="FrmForm" type="oscar.form.pageUtil.FrmFormAddRHWorkFlowAction">
        String demographicNo = request.getParameter("demographic_no");
        if (demographicNo == null){
            demographicNo = (String) request.getAttribute("demographic_no");
        }
        String ip = request.getRemoteAddr();
        String providerNo = (String) request.getSession().getAttribute("user");
        String workflowId = request.getParameter("workflowId");
        String state = request.getParameter("state");
        
        MiscUtils.getLogger().debug("FrmFormRHPreventionAction demographic "+demographicNo);
        ActionForward af = mapping.findForward("success");
        
        int workId = -1;
        
        String workflowType = "RH";
        WorkFlowFactory flowFactory = new WorkFlowFactory();
        WorkFlow flow = flowFactory.getWorkFlow(workflowType);
        ArrayList currentWorkFlows = flow.getActiveWorkFlowList(demographicNo);

        String dateToParse = request.getParameter("edd");
        MiscUtils.getLogger().debug("New workflow for "+demographicNo+" EDD "+dateToParse);
        Date endDate = UtilDateUtilities.StringToDate(dateToParse);   

        //Currently open work flows ?
        if(currentWorkFlows != null && currentWorkFlows.size() > 0){
            MiscUtils.getLogger().debug("size of current workflows "+currentWorkFlows.size());
            request.setAttribute("currentWorkFlow",currentWorkFlows.get(0));
             Hashtable h = (Hashtable) currentWorkFlows.get(0);
             String currentId = (String) h.get("ID");
             if (workflowId != null ){
            //LOG CHANGE NOW
                MiscUtils.getLogger().debug("Changing workflow for  "+demographicNo+ " to "+state);
                
                WorkFlowState wfs = new WorkFlowState();
                
                wfs.updateWorkFlowState( workflowId, state,endDate );
                LogAction.addLog((String) request.getSession().getAttribute("user"), LogConst.UPDATE, "WF_"+workflowType, state, ip);
            }
        } else{  
            //if none are found open, offer to create a new one  (could be existing but closed)
            request.setAttribute("newWorkFlowNeeded","true");   
          
            workId = flow.addToWorkFlow(providerNo, demographicNo, endDate);        
            LogAction.addLog((String) request.getSession().getAttribute("user"), LogConst.UPDATE, "WF_"+workflowType, state, ip);
        
        }
        
   
        //SAVE RECORD   
            FrmRecord rec = null;
            String where = ""; 
            int newID = 0;

            FrmRecordFactory recorder = new FrmRecordFactory();
            try{
                rec = recorder.factory(request.getParameter("form_class"));
                Properties props = new Properties();
                for (Enumeration varEnum = request.getParameterNames(); varEnum.hasMoreElements();) {
                    String name = (String) varEnum.nextElement();                    
                    props.setProperty(name, request.getParameter(name));                    
                }
                if (!props.containsKey("workflowId")){
                    props.setProperty("workflowId",""+workId);
                }

                props.setProperty("provider_no", providerNo);
                newID = rec.saveFormRecord(props);
                LogAction.addLog((String) request.getSession().getAttribute("user"), LogConst.ADD, request.getParameter("form_class"), "" + newID, ip);
            }catch(Exception factEx){
            	MiscUtils.getLogger().error("Error", factEx);
            }

            request.setAttribute("demographic_no",demographicNo); 
            where = af.getPath();
            try {
                where = rec.createActionURL(where, "save", demographicNo, "" + newID);
            } catch (SQLException ex) {MiscUtils.getLogger().error("Error", ex);
            }
        
            af = new ActionForward(where);
       
        
        return af ;
    }
    /** Creates a new instance of FrmFormRHPreventionAction */
    public FrmFormRHPreventionAction() {
    }
    
}
