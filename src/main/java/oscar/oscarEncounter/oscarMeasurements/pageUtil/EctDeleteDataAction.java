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

package oscar.oscarEncounter.oscarMeasurements.pageUtil;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.oscarehr.common.dao.MeasurementDao;
import org.oscarehr.common.dao.MeasurementsDeletedDao;
import org.oscarehr.common.model.Measurement;
import org.oscarehr.common.model.MeasurementsDeleted;
import org.oscarehr.managers.SecurityInfoManager;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.util.ConversionUtils;
import oscar.util.ParameterActionForward;

public class EctDeleteDataAction extends Action {

	private SecurityInfoManager securityInfoManager = SpringUtils.getBean(SecurityInfoManager.class);
	
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		if(!securityInfoManager.hasPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), "_measurement", "d", null)) {
			throw new SecurityException("missing required security object (_measurement)");
		}
		
		EctDeleteDataForm frm = (EctDeleteDataForm) form;
		request.getSession().setAttribute("EctDeleteDataForm", frm);
		String[] deleteCheckbox = frm.getDeleteCheckbox();

		MeasurementsDeletedDao measurementsDeletedDao = (MeasurementsDeletedDao) SpringUtils.getBean("measurementsDeletedDao");
		MeasurementDao measurementDao = SpringUtils.getBean(MeasurementDao.class);
		if (deleteCheckbox != null) {

			MeasurementDao dao = SpringUtils.getBean(MeasurementDao.class);
			for (int i = 0; i < deleteCheckbox.length; i++) {
				MiscUtils.getLogger().debug(deleteCheckbox[i]);

				Measurement m = dao.find(ConversionUtils.fromIntString(deleteCheckbox[i]));
				if (m != null) {
					measurementsDeletedDao.persist(new MeasurementsDeleted(m));
					measurementDao.remove(Integer.parseInt(deleteCheckbox[i]));
				}
			}
		}

		if (frm.getType() != null) {
			ParameterActionForward forward = new ParameterActionForward(mapping.findForward("success"));
			forward.addParameter("type", frm.getType());
			return forward;
		}
		return mapping.findForward("success");
	}

}
