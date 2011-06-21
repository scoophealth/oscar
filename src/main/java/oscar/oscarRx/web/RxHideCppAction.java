package oscar.oscarRx.web;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.oscarehr.common.dao.DrugDao;
import org.oscarehr.common.model.Drug;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

public class RxHideCppAction extends DispatchAction {

	private static final Logger logger = MiscUtils.getLogger();

	private DrugDao drugDao = (DrugDao)SpringUtils.getBean("drugDao");
	
    public ActionForward update(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        String prescriptId = request.getParameter("prescriptId");
        String value= request.getParameter("value");
        Drug drug = drugDao.find(Integer.valueOf(prescriptId));
        if(drug != null) {
        	drug.setHideFromCpp(Boolean.valueOf(value));
        }
        drugDao.merge(drug);
        try {
        	response.getWriter().println("ok");
        }catch(IOException e) {
        	logger.error("error",e);
        }
        return null;
    }
}
