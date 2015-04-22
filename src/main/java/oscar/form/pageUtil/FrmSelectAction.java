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


package oscar.form.pageUtil;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.oscarehr.common.dao.EncounterFormDao;
import org.oscarehr.common.model.EncounterForm;
import org.oscarehr.managers.SecurityInfoManager;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

public class FrmSelectAction extends Action {

	private static Logger logger = MiscUtils.getLogger();
	private SecurityInfoManager securityInfoManager = SpringUtils.getBean(SecurityInfoManager.class);
	
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		FrmSelectForm frm = (FrmSelectForm) form;
		request.getSession().setAttribute("FrmSelectForm", frm);

		if(!securityInfoManager.hasPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), "_form", "w", null)) {
			throw new SecurityException("missing required security object (_form)");
		}
		
		String fwd=frm.getForward();
		if (fwd != null) {
			if (fwd.compareTo("add") == 0) {
				logger.debug("the add button is pressed");

				String[] selectedAddTypes = frm.getSelectedAddTypes();
				if (selectedAddTypes != null) {
					for (int i = 0; i < selectedAddTypes.length; i++) {
						addForm(selectedAddTypes[i]);
					}
				}
			} else if (fwd.compareTo("delete") == 0) {
				logger.debug("the delete button is pressed");
				String[] selectedDeleteTypes = frm.getSelectedDeleteTypes();
				if (selectedDeleteTypes != null) {
					for (int i = 0; i < selectedDeleteTypes.length; i++) {
						deleteForm(selectedDeleteTypes[i]);
					}
				}
			} else if (fwd.compareTo("up") == 0) {
				logger.debug("The Move UP button is pressed!");
				String[] selectedMoveUpTypes = frm.getSelectedDeleteTypes();
				if (selectedMoveUpTypes != null) {
					for (int i = 0; i < selectedMoveUpTypes.length; i++) {
						moveUpOne(selectedMoveUpTypes[i]);
					}
				}
			}

			else if (fwd.compareTo("down") == 0) {
				logger.debug("The Move DOWN button is pressed!");
				String[] selectedMoveDownTypes = frm.getSelectedDeleteTypes();
				if (selectedMoveDownTypes != null) {
					for (int i = selectedMoveDownTypes.length - 1; i >= 0; i--) {
						moveDown(selectedMoveDownTypes[i]);
					}
				}
			}
		}

		return mapping.findForward("success");
	}

	private void addForm(String formName) {
		EncounterFormDao encounterFormDao = (EncounterFormDao) SpringUtils.getBean("encounterFormDao");
		List<EncounterForm> encounterForms = encounterFormDao.findAll();

		int maxCurrentDisplayCount = 0;
		// find the largest current display number
		for (EncounterForm encounterForm : encounterForms) {
			maxCurrentDisplayCount = Math.max(maxCurrentDisplayCount, encounterForm.getDisplayOrder());
		}
		// ++ to get the next number
		maxCurrentDisplayCount++;

		for (EncounterForm encounterForm : encounterForms) {
			if (encounterForm.getFormName().equals(formName)) {
				encounterForm.setDisplayOrder(maxCurrentDisplayCount);
				encounterFormDao.merge(encounterForm);
				return;
			}
		}
	}

	private void deleteForm(String formName) {
		EncounterFormDao encounterFormDao = (EncounterFormDao) SpringUtils.getBean("encounterFormDao");
		List<EncounterForm> encounterForms = encounterFormDao.findAll();

		// go through the forms list and set this form display to 0
		// and any other form with a greater number should be decremented by 1
		List<EncounterForm> tempForms = encounterFormDao.findByFormName(formName);
		for (EncounterForm tempForm : tempForms) {
			int oldDisplayOrder = tempForm.getDisplayOrder();
			for (EncounterForm encounterForm : encounterForms) {
				if (encounterForm.getDisplayOrder() > oldDisplayOrder) {
					encounterForm.setDisplayOrder(encounterForm.getDisplayOrder() - 1);
					encounterFormDao.merge(encounterForm);
				} else if (encounterForm.getDisplayOrder() == oldDisplayOrder) {
					encounterForm.setDisplayOrder(0);
					encounterFormDao.merge(encounterForm);
				}
			}
		}
	}

	private void moveUpOne(String formName) {
		EncounterFormDao encounterFormDao = (EncounterFormDao) SpringUtils.getBean("encounterFormDao");
		List<EncounterForm> encounterForms = encounterFormDao.findAll();

		int oldPosition = -1;
		for (EncounterForm encounterForm : encounterForms) {
			if (encounterForm.getFormName().equals(formName)) {
				oldPosition = encounterForm.getDisplayOrder();
			}
		}

		// ignore if can't find (-1), ignore if hidden (0), and ignore if already at top (1)
		if (oldPosition > 1) {
			for (EncounterForm encounterForm : encounterForms) {
				if (encounterForm.getDisplayOrder() == oldPosition - 1) {
					encounterForm.setDisplayOrder(oldPosition);
					encounterFormDao.merge(encounterForm);
				} else if (encounterForm.getDisplayOrder() == oldPosition) {
					encounterForm.setDisplayOrder(oldPosition - 1);
					encounterFormDao.merge(encounterForm);
				}
			}
		}
	}

	private void moveDown(String formName) {
		EncounterFormDao encounterFormDao = (EncounterFormDao) SpringUtils.getBean("encounterFormDao");
		List<EncounterForm> encounterForms = encounterFormDao.findAll();

		int oldPosition = -1;
		for (EncounterForm encounterForm : encounterForms) {
			if (encounterForm.getFormName().equals(formName)) {
				oldPosition = encounterForm.getDisplayOrder();
			}
		}

		// ignore if can't find (-1), ignore if hidden (0)
		if (oldPosition > 0) {
			boolean isLast = true;
			for (EncounterForm encounterForm : encounterForms) {
				if (encounterForm.getDisplayOrder() > oldPosition) {
					isLast = false;
				}
			}

			// if already last item ignore
			if (!isLast) {
				for (EncounterForm encounterForm : encounterForms) {
					if (encounterForm.getDisplayOrder() == oldPosition + 1) {
						encounterForm.setDisplayOrder(oldPosition);
						encounterFormDao.merge(encounterForm);
					} else if (encounterForm.getDisplayOrder() == oldPosition) {
						encounterForm.setDisplayOrder(oldPosition + 1);
						encounterFormDao.merge(encounterForm);
					}
				}
			}
		}
	}
}
