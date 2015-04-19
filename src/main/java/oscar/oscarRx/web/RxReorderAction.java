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
import org.oscarehr.managers.SecurityInfoManager;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

public class RxReorderAction extends DispatchAction {

	private static final Logger logger = MiscUtils.getLogger();
	private SecurityInfoManager securityInfoManager = SpringUtils.getBean(SecurityInfoManager.class);

	public ActionForward update(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		if (!securityInfoManager.hasPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), "_rx", "u", null)) {
			throw new RuntimeException("missing required security object (_rx)");
		}
		
		String demographicNo = request.getParameter("demographicNo");
		int drugId = Integer.parseInt(request.getParameter("drugId"));
		int swapDrugId = Integer.parseInt(request.getParameter("swapDrugId"));

		CaseManagementManager caseManagementManager = (CaseManagementManager) SpringUtils.getBean("caseManagementManager");
		List<Drug> drugs = caseManagementManager.getPrescriptions(demographicNo, true);
		DrugDao drugDao = (DrugDao) SpringUtils.getBean("drugDao");

		Drug myDrug = null;
		Drug swapDrug = null;

		for (Drug drug : drugs) {
			if (drug.getId().intValue() == drugId) {
				myDrug = drug;
			}
			if (drug.getId().intValue() == swapDrugId) {
				swapDrug = drug;
			}
		}

		if (myDrug == null || swapDrug == null) {
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
		} catch (IOException e) {
			logger.error("error", e);
		}
		return null;
	}
}
