/**
 * PrintLabsAction.java
 *
 * Created on November 27, 2007, 9:42 AM
 * Author: Adam Balanga
 */

package oscar.oscarLab.ca.all.pageUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.oscarehr.util.MiscUtils;

import oscar.oscarLab.ca.all.Hl7textResultsData;
import oscar.oscarLab.ca.all.parsers.Factory;
import oscar.oscarLab.ca.all.parsers.OLISHL7Handler;

/**
 *
 * @author wrighd
 */
public class PrintOLISLabsAction extends Action{
    
    Logger logger = Logger.getLogger(PrintLabsAction.class);
    
    /** Creates a new instance of PrintLabsAction */
    public PrintOLISLabsAction() {
    }
    
    public ActionForward execute(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response){
        
    	try {
	    	String segmentID = request.getParameter("segmentID");
	    	int obr = Integer.valueOf(request.getParameter("obr"));
	    	int obx = Integer.valueOf(request.getParameter("obx"));
	    	
	    	if ("true".equals(request.getParameter("showLatest"))) {
	    		String multiLabId = Hl7textResultsData.getMatchingLabs(segmentID);
	    		segmentID = multiLabId.split(",")[multiLabId.split(",").length - 1];
	    	}
		    	
	    	OLISHL7Handler handler = (OLISHL7Handler) Factory.getHandler(segmentID);

	    	handler.processEncapsulatedData(request, response, obr, obx);
    	}
    	catch (Exception e) {
    		MiscUtils.getLogger().error("error",e);
    	}
    	return null;
    }
}
