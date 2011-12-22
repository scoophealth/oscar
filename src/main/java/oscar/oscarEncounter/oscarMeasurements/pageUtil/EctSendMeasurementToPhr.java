/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package oscar.oscarEncounter.oscarMeasurements.pageUtil;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBException;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.indivo.IndivoException;
import org.oscarehr.phr.PHRAuthentication;
import org.oscarehr.phr.model.PHRMeasurement;
import org.oscarehr.phr.service.PHRService;
import org.oscarehr.phr.util.MyOscarUtils;
import org.oscarehr.util.MiscUtils;

import oscar.oscarDemographic.data.DemographicData;
import oscar.oscarEncounter.data.EctProviderData;
import oscar.oscarEncounter.oscarMeasurements.bean.EctMeasurementsDataBean;
import oscar.oscarEncounter.oscarMeasurements.bean.EctMeasurementsDataBeanHandler;

/**
 *
 * @author apavel
 */
public class EctSendMeasurementToPhr extends Action {
    private Logger logger=MiscUtils.getLogger();
	
    PHRService phrService = null;
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, 
            HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, JAXBException, IndivoException {

        String errorMsg = null;
        
        try {
	        String demographicNo = request.getParameter("demographicNo");
	        String providerNo = (String) request.getSession().getAttribute("user");
	        String[] measurementTypeList = request.getParameterValues("measurementTypeList");
	        
	        EctProviderData.Provider provider = new EctProviderData().getProvider(providerNo);
	        DemographicData demoData = new DemographicData();
	        PHRAuthentication auth = (PHRAuthentication) request.getSession().getAttribute(PHRAuthentication.SESSION_PHR_AUTH);
	        Long myOscarUserId = MyOscarUtils.getMyOscarUserId(auth, demoData.getDemographic(demographicNo).getMyOscarUserName());

	        
	        EctMeasurementsDataBeanHandler hd = new EctMeasurementsDataBeanHandler(demographicNo);
	        for (String measurementType: measurementTypeList) {
	            List<EctMeasurementsDataBean> measurements =  hd.getMeasurementObjectByType(measurementType, demographicNo);
	            for (EctMeasurementsDataBean measurement: measurements) {
	                if (!phrService.isIndivoRegistered(measurementType, measurement.getId()+"")) {
	                      PHRMeasurement phrMeasurement = new PHRMeasurement(provider, demographicNo, myOscarUserId, measurementType, measurement);
	                      phrService.sendAddDocument(phrMeasurement, measurement.getId() + "");
	                }
	                
	            }
	        }
        } catch (Exception e) {
        	errorMsg=e.getMessage();
        	logger.error("Error", e);
        }
        
        request.setAttribute("error_msg", errorMsg);
        return mapping.findForward("finished");
    }
    
    public void setPhrService(PHRService pServ){
        this.phrService = pServ;
    }    
}
