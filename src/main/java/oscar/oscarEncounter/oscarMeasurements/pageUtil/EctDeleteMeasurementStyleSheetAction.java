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
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.oscarehr.common.dao.MeasurementCSSLocationDao;
import org.oscarehr.common.dao.MeasurementGroupStyleDao;
import org.oscarehr.common.model.MeasurementCSSLocation;
import org.oscarehr.common.model.MeasurementGroupStyle;
import org.oscarehr.managers.SecurityInfoManager;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.SpringUtils;

import oscar.util.ConversionUtils;

public class EctDeleteMeasurementStyleSheetAction extends Action {

	private SecurityInfoManager securityInfoManager = SpringUtils.getBean(SecurityInfoManager.class);
	
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		EctDeleteMeasurementStyleSheetForm frm = (EctDeleteMeasurementStyleSheetForm) form;
		
		if( securityInfoManager.hasPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), "_admin", "w", null) || securityInfoManager.hasPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), "_admin.measurements", "w", null) )  {
		
		request.getSession().setAttribute("EctDeleteMeasurementStyleSheetForm", frm);
		String[] deleteCheckbox = frm.getDeleteCheckbox();

		MeasurementGroupStyleDao dao = SpringUtils.getBean(MeasurementGroupStyleDao.class);
		MeasurementCSSLocationDao lDao = SpringUtils.getBean(MeasurementCSSLocationDao.class);

		if (deleteCheckbox != null) {
			for (int i = 0; i < deleteCheckbox.length; i++) {
				List<MeasurementGroupStyle> styles = dao.findByCssId(ConversionUtils.fromIntString(deleteCheckbox[i]));

				for (MeasurementGroupStyle style : styles) {
					MeasurementCSSLocation location = lDao.find(ConversionUtils.fromIntString(deleteCheckbox[i]));
					if (location != null) {
						ActionMessages errors = new ActionMessages();
						errors.add(deleteCheckbox[i],

						new ActionMessage("error.oscarEncounter.Measurements.cannotDeleteStyleSheet", location.getLocation()));
						saveErrors(request, errors);
						return (new ActionForward(mapping.getInput()));
					}
					
					dao.remove(style);
				}
			}
		}

		return mapping.findForward("success");
		
		}else{
			throw new SecurityException("Access Denied!"); //missing required security object (_admin)
		}
	}

}
