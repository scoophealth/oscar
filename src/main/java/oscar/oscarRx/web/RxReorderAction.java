package oscar.oscarRx.web;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.oscarehr.casemgmt.service.CaseManagementManager;
import org.oscarehr.common.dao.DrugDao;
import org.oscarehr.common.model.Drug;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

public class RxReorderAction extends DispatchAction {

	private static final Logger logger = MiscUtils.getLogger();

	private DrugDao drugDao = (DrugDao)SpringUtils.getBean("drugDao");
	
    public ActionForward update(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
    	String demographicNo = request.getParameter("demographicNo");
        int drugId = Integer.parseInt(request.getParameter("drugId"));
        int swapDrugId = Integer.parseInt(request.getParameter("swapDrugId"));
        String direction = request.getParameter("direction");
        
        CaseManagementManager caseManagementManager = (CaseManagementManager) SpringUtils.getBean("caseManagementManager");        
        List<Drug> drugs = caseManagementManager.getPrescriptions(demographicNo, true);
        DrugDao drugDao = (DrugDao)SpringUtils.getBean("drugDao");
        
        Drug myDrug = null;
        Drug swapDrug = null;

        for(Drug drug:drugs) {
        	if(drug.getId().intValue() == drugId) {
        		myDrug = drug;
        	}
        	if(drug.getId().intValue() == swapDrugId) {
        		swapDrug = drug;
        	}
        }
        
        if(myDrug == null || swapDrug == null) {
        	MiscUtils.getLogger().warn("Couldn't find the drugs to swap!");        	
        } else {
        	int myPosition = myDrug.getPosition();
        	int swapPosition = swapDrug.getPosition();
        	myDrug.setPosition(swapPosition);
        	swapDrug.setPosition(myPosition);
        	drugDao.merge(myDrug);
        	drugDao.merge(swapDrug);
        }

        
        try {
        	response.getWriter().println("ok");
        }catch(IOException e) {
        	logger.error("error",e);
        }
        return null;
    }
}
