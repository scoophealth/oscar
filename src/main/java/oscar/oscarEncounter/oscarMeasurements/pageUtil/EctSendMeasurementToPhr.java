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
import org.oscarehr.myoscar.utils.MyOscarLoggedInInfo;
import org.oscarehr.phr.model.PHRMeasurement;
import org.oscarehr.phr.service.PHRService;
import org.oscarehr.phr.util.MyOscarUtils;
import org.oscarehr.util.MiscUtils;

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
	        Integer demographicNo = Integer.parseInt(request.getParameter("demographicNo"));
	        String providerNo = (String) request.getSession().getAttribute("user");
	        String[] measurementTypeList = request.getParameterValues("measurementTypeList");
	        
	        EctProviderData.Provider provider = new EctProviderData().getProvider(providerNo);
	        
			MyOscarLoggedInInfo myOscarLoggedInInfo=MyOscarLoggedInInfo.getLoggedInInfo(request.getSession());
	        Long myOscarUserId = MyOscarUtils.getMyOscarUserIdFromOscarDemographicId(myOscarLoggedInInfo, demographicNo);
	        
	        EctMeasurementsDataBeanHandler hd = new EctMeasurementsDataBeanHandler(demographicNo);
	        for (String measurementType: measurementTypeList) {
	            List<EctMeasurementsDataBean> measurements =  EctMeasurementsDataBeanHandler.getMeasurementObjectByType(measurementType, demographicNo);
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
