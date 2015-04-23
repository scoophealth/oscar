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

package oscar.oscarResearch.oscarDxResearch.pageUtil;

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
import org.oscarehr.common.dao.AbstractCodeSystemDao;
import org.oscarehr.common.dao.QuickListDao;
import org.oscarehr.common.model.AbstractCodeSystemModel;
import org.oscarehr.common.model.QuickList;
import org.oscarehr.managers.SecurityInfoManager;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.SpringUtils;

public class dxResearchUpdateQuickListAction extends Action {
	private static SecurityInfoManager securityInfoManager = SpringUtils.getBean(SecurityInfoManager.class);

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (!securityInfoManager.hasPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), "_dxresearch", "w", null)) {
			throw new RuntimeException("missing required security object (_dxresearch)");
		}
		
		dxResearchUpdateQuickListForm frm = (dxResearchUpdateQuickListForm) form;
		String quickListName = frm.getQuickListName();
		String forward = frm.getForward();
		String codingSystem = frm.getSelectedCodingSystem();
		String curUser = (String) request.getSession().getAttribute("user");
		dxResearchLoadQuickListItemsForm qLItemsFrm = (dxResearchLoadQuickListItemsForm) request.getSession().getAttribute("dxResearchLoadQuickListItemsFrm");
		qLItemsFrm.setQuickListName(quickListName);
		boolean valid = true;

		if (forward.equals("add")) {
			valid = doAdd(request, frm, quickListName, codingSystem, curUser);
		} else if (forward.equals("remove")) {
			doRemove(frm, quickListName);
		}

		if (!valid) {
			return (new ActionForward(mapping.getInput()));
		}

		return mapping.findForward("success");
	}

	private void doRemove(dxResearchUpdateQuickListForm frm, String quickListName) {
		String[] removedItems = frm.getQuickListItems();
		String[] itemValues;
		if (removedItems != null) {
			for (int i = 0; i < removedItems.length; i++) {
				itemValues = removedItems[i].split(",");

				QuickListDao quickListDao = SpringUtils.getBean(QuickListDao.class);
				List<QuickList> quickLists = quickListDao.findByNameResearchCodeAndCodingSystem(quickListName, itemValues[1], itemValues[0]);
				for (QuickList q : quickLists) {
					quickListDao.remove(q.getId());
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	private boolean doAdd(HttpServletRequest request, dxResearchUpdateQuickListForm frm, String quickListName, String codingSystem, String curUser) {
		boolean valid = true;
		String[] xml_research = frm.getXmlResearch();
		ActionMessages errors = new ActionMessages();
		AbstractCodeSystemDao<AbstractCodeSystemModel<?>> dao = (AbstractCodeSystemDao<AbstractCodeSystemModel<?>>) SpringUtils.getBean(AbstractCodeSystemDao.getDaoName(AbstractCodeSystemDao.codingSystem.valueOf(codingSystem)));

		for (int i = 0; i < xml_research.length; i++) {
			if (xml_research[i] == null || xml_research[i].equals("")) {
				continue;
			}

			//need to validate the dxresearch code before write to the database
			AbstractCodeSystemModel<?> codingSystemEntity = dao.findByCodingSystem(xml_research[i]);
			boolean isCodingSystemEntitySet = codingSystemEntity != null;
			if (!isCodingSystemEntitySet) {
				valid = false;
				errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.codeNotFound", xml_research[i], codingSystem));
				saveErrors(request, errors);
			} else {
				QuickListDao quickListDao = SpringUtils.getBean(QuickListDao.class);
				List<QuickList> quickLists = quickListDao.findByNameResearchCodeAndCodingSystem(quickListName, xml_research[i], codingSystem);
				if (!quickLists.isEmpty()) {
					continue;
				}

				QuickList ql = new QuickList();
				ql.setQuickListName(quickListName);
				ql.setDxResearchCode(xml_research[i]);
				ql.setCreatedByProvider(curUser);
				ql.setCodingSystem(codingSystem);

				quickListDao.persist(ql);

			}

		}
		return valid;
	}

}
