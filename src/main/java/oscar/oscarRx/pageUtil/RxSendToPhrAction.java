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
 * RxSendToPhr.java
 *
 * Created on June 12, 2007, 2:47 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package oscar.oscarRx.pageUtil;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.managers.DemographicManager;
import org.oscarehr.managers.SecurityInfoManager;
import org.oscarehr.myoscar.client.ws_manager.AccountManager;
import org.oscarehr.myoscar.commons.MedicalDataType;
import org.oscarehr.myoscar.utils.MyOscarLoggedInInfo;
import org.oscarehr.phr.service.PHRService;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.oscarEncounter.data.EctProviderData;
import oscar.oscarRx.data.RxPatientData;
import oscar.oscarRx.data.RxPrescriptionData.Prescription;

/**
 * @author apavel
 */
public class RxSendToPhrAction extends Action {
	private SecurityInfoManager securityInfoManager = SpringUtils.getBean(SecurityInfoManager.class);

	PHRService phrService = null;

	/** Creates a new instance of RxSendToPhr */
	public RxSendToPhrAction() {
	}

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);
		if (!securityInfoManager.hasPrivilege(loggedInInfo, "_rx", "r", null)) {
			throw new RuntimeException("missing required security object (_rx)");
		}
		if (!securityInfoManager.hasPrivilege(loggedInInfo, "_demographic", "r", null)) {
			throw new RuntimeException("missing required security object (_demographic)");
		}
		
		// boolean errors = false;
		String errorMsg = null;

		oscar.oscarRx.pageUtil.RxSessionBean bean = (oscar.oscarRx.pageUtil.RxSessionBean) request.getSession().getAttribute("RxSessionBean");
		// authenticate
		EctProviderData.Provider prov = new EctProviderData().getProvider(bean.getProviderNo());

		try {
			MyOscarLoggedInInfo myOscarLoggedInInfo=MyOscarLoggedInInfo.getLoggedInInfo(request.getSession());
			DemographicManager demographicManager = SpringUtils.getBean(DemographicManager.class);
			Demographic demographic = demographicManager.getDemographic(loggedInInfo, bean.getDemographicNo());
			Long myOscarUserId = AccountManager.getUserId(myOscarLoggedInInfo, demographic.getMyOscarUserName());

			RxPatientData.Patient patient = null;

			patient = RxPatientData.getPatient(loggedInInfo, demographic.getDemographicNo());

			oscar.oscarRx.data.RxPrescriptionData.Prescription[] prescribedDrugs;
			prescribedDrugs = patient.getPrescribedDrugs();

			MiscUtils.getLogger().debug("prescribed drugs length" + prescribedDrugs.length);
			for (int idx = 0; idx < prescribedDrugs.length; ++idx) {
				Prescription drug = prescribedDrugs[idx];
				if (drug.isCurrent() == true && !drug.isArchived()) {
					try {
						// if updating removed because drugs are never edited, only represcribed
						/*
						 * if (phrService.isIndivoRegistered(PHRConstants.DOCTYPE_MEDICATION(), drug.getDrugId()+"")) { //if updating MiscUtils.getLogger().debug("running update"); String phrDrugIndex =
						 * phrService.getPhrIndex(PHRConstants.DOCTYPE_MEDICATION(), drug.getDrugId()+""); phrService.sendUpdateMedication(prov, demoNo, patientMyOscarId, drug, phrDrugIndex); //drug.setIndivoIdx(newIndex); } else { //if adding
						 */

						// only add new drugs, no updating old drugs because they cannot be edited

						if (!phrService.isIndivoRegistered(MedicalDataType.MEDICATION.name(), drug.getDrugId() + "")) {

							phrService.sendAddMedication(prov, demographic.getDemographicNo(), myOscarUserId, drug);
						}
						// throw new Exception("Error: Cannot marshal the document");
					} catch (Exception e) {
						MiscUtils.getLogger().error("Error", e);
						errorMsg = e.getMessage();
						// errors = true;
						request.setAttribute("error_msg", errorMsg);
						return mapping.findForward("finished");
					}
				}
			}
		} catch (Exception e) {
			MiscUtils.getLogger().error("Error", e);
		}

		request.setAttribute("error_msg", errorMsg);
		return mapping.findForward("finished");
	}

	public void setPhrService(PHRService pServ) {
		this.phrService = pServ;
	}

}
