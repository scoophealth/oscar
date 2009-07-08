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
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.indivo.IndivoException;
import org.oscarehr.phr.PHRConstants;
import org.oscarehr.phr.model.PHRMeasurement;
import org.oscarehr.phr.service.PHRService;
import oscar.oscarDemographic.data.DemographicData;
import oscar.oscarEncounter.data.EctProviderData;
import oscar.oscarEncounter.oscarMeasurements.bean.EctMeasurementsDataBean;
import oscar.oscarEncounter.oscarMeasurements.bean.EctMeasurementsDataBeanHandler;

/**
 *
 * @author apavel
 */
public class EctSendMeasurementToPhr extends Action {
    
    PHRService phrService = null;
    PHRConstants phrConstants = null;
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, 
            HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, JAXBException, IndivoException {
        //System.out.println("CALLED................." + request.getParameterValues("measurementTypeList"));
        String errorMsg = null;
        String demographicNo = request.getParameter("demographicNo");
        String providerNo = (String) request.getSession().getAttribute("user");
        String[] measurementTypeList = request.getParameterValues("measurementTypeList");
        
        EctProviderData.Provider provider = new EctProviderData().getProvider(providerNo);
        DemographicData demoData = new DemographicData();
        String patientMyOscarId = demoData.getDemographic(demographicNo).getPin();
        
        EctMeasurementsDataBeanHandler hd = new EctMeasurementsDataBeanHandler(demographicNo);
        for (String measurementType: measurementTypeList) {
            List<EctMeasurementsDataBean> measurements =  hd.getMeasurementObjectByType(measurementType, demographicNo);
            for (EctMeasurementsDataBean measurement: measurements) {
                if (!phrService.isIndivoRegistered(phrConstants.DOCTYPE_MEASUREMENT(), measurement.getId()+"")) {
                      PHRMeasurement phrMeasurement = new PHRMeasurement(provider, demographicNo, patientMyOscarId, measurement);
                      phrService.sendAddDocument(phrMeasurement, measurement.getId() + "");
                }
                
            }
        }
        request.setAttribute("error_msg", errorMsg);
        return mapping.findForward("finished");
    }
    
    public void setPhrService(PHRService pServ){
        this.phrService = pServ;
    }
    
    public void setPhrConstants(PHRConstants pConst) {
        this.phrConstants = pConst;
    }
}
