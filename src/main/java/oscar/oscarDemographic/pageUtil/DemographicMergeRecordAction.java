/*
 * DemographicMergeRecordAction.java
 *
 * Created on September 11, 2007, 3:52 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package oscar.oscarDemographic.pageUtil;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import oscar.oscarDemographic.data.DemographicMerged;

/**
 *
 * @author wrighd
 */
public class DemographicMergeRecordAction  extends Action {
    
    Logger logger = Logger.getLogger(DemographicMergeRecordAction.class);
    
    public DemographicMergeRecordAction() {
        
    }
    public ActionForward execute(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) {
        
        if (request.getParameterValues("records")==null) {
            return mapping.findForward("failure");
        }
        String outcome = "success";
        ArrayList records = new ArrayList(Arrays.asList(request.getParameterValues("records")));
        String head = request.getParameter("head");
        String action = request.getParameter("mergeAction");
        String provider_no = request.getParameter("provider_no");
        DemographicMerged dmDAO = new DemographicMerged();
        
        if (action.equals("merge") && head != null && records.size() > 1 && records.contains(head)){
            
            for (int i=0; i < records.size(); i++){
                if (!((String) records.get(i)).equals(head))
                    try{
                        dmDAO.Merge((String) records.get(i), head);
                    }catch(SQLException e){
                        logger.error("Could not merged records: "+records.get(i)+","+head, e);
                        outcome = "failure";
                    }
            }
            
        }else if(action.equals("unmerge") && records.size() > 0){
            outcome = "successUnMerge";
            for (int i=0; i < records.size(); i++){
                String demographic_no = (String) records.get(i);
                try{
                    dmDAO.UnMerge(demographic_no, provider_no);
                }catch(SQLException e){
                    logger.error("Could not unmerge the record: "+records.get(i), e);
                    outcome = "failureUnMerge";
                }
            }
            
        }else{
            outcome = "failure";
        }
        request.setAttribute("mergeoutcome",outcome);
        
        if (request.getParameter("caisiSearch") != null && request.getParameter("caisiSearch").equalsIgnoreCase("yes")){
            outcome = "caisiSearch";
        }
        
        return mapping.findForward(outcome);
    }
}
