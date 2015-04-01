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


package oscar.oscarBilling.ca.bc.pageUtil;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.oscarehr.managers.DemographicManager;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import oscar.OscarProperties;
import oscar.oscarBilling.ca.bc.MSP.TeleplanFileWriter;
import oscar.oscarBilling.ca.bc.MSP.TeleplanSubmission;
import oscar.oscarBilling.ca.bc.data.BillingmasterDAO;
import oscar.oscarProvider.data.ProviderData;

/**
 * Action Simulates a MSP teleplan file but doesn't commit any of the data. 
 * 
 * @author jay
 */
public class SimulateTeleplanFileAction extends Action{

    /**
     * Creates a new instance of GenerateTeleplanFileAction
     */
    public SimulateTeleplanFileAction() {
    }

    public ActionForward execute(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws Exception{

    	String dataCenterId = OscarProperties.getInstance().getProperty("dataCenterId");

        String provider = request.getParameter("provider");
        String providerBillingNo = request.getParameter("provider");
        if(provider != null && provider.equals("all")){
            providerBillingNo = "%";
        }
        @SuppressWarnings("deprecation")
        ProviderData pd = new ProviderData();
     
        @SuppressWarnings("deprecation")
        List<String> list = pd.getProviderListWithInsuranceNo(providerBillingNo);

        @SuppressWarnings("deprecation")
        ProviderData[] pdArr = new ProviderData[list.size()];

        for (int i=0;i < list.size(); i++){
            String provNo = list.get(i);
            pdArr[i] = new ProviderData(provNo);
        }
        //This needs to be replaced for sim
        boolean testRun = true;
        //To prevent multiple submissions being generated at the same time
        synchronized (this) {
            try {
                WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getSession().getServletContext());
                BillingmasterDAO billingmasterDAO = (BillingmasterDAO) ctx.getBean("BillingmasterDAO");
                DemographicManager demographicManager =  ctx.getBean(DemographicManager.class);
             
                TeleplanFileWriter teleplanWr = new TeleplanFileWriter();
                teleplanWr.setBillingmasterDAO(billingmasterDAO);
                teleplanWr.setDemographicManager(demographicManager);
                TeleplanSubmission submission = teleplanWr.getSubmission(LoggedInInfo.getLoggedInInfoFromSession(request), testRun, pdArr, dataCenterId);

                //response.getWriter().print(submission.getHtmlFile());
                request.setAttribute("TeleplanHtmlFile", submission.getHtmlFile());
                
            }catch(Exception e){
                MiscUtils.getLogger().debug("Error: Teleplan Html File", e);
            }
        }
        return mapping.findForward("success");
        //return null;
    }
}
