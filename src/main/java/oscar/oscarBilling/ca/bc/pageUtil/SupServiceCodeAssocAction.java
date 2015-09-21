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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;
import org.oscarehr.util.SpringUtils;

import oscar.oscarBilling.ca.bc.data.SupServiceCodeAssocDAO;

public class SupServiceCodeAssocAction
    extends Action {
  public ActionForward execute(ActionMapping actionMapping,
                               ActionForm actionForm,
                               HttpServletRequest servletRequest,
                               HttpServletResponse servletResponse) {
    SupServiceCodeAssocActionForm frm = (
        SupServiceCodeAssocActionForm) actionForm;

    SupServiceCodeAssocDAO dao = SpringUtils.getBean(SupServiceCodeAssocDAO.class);
    ActionForward fwd = actionMapping.findForward("success");
    if (!SupServiceCodeAssocActionForm.MODE_VIEW.equals(frm.getActionMode())) {
      ActionMessages errors = frm.validate(actionMapping, servletRequest);
      if (!errors.isEmpty()) {
        this.saveErrors(servletRequest,errors);
        fwd = actionMapping.getInputForward();
      }
      else {
        if (SupServiceCodeAssocActionForm.MODE_DELETE.equals(frm.getActionMode())) {
          dao.deleteServiceCodeAssociation(frm.getId());
        }
        else if (SupServiceCodeAssocActionForm.MODE_EDIT.equals(frm.getActionMode())) {
          dao.saveOrUpdateServiceCodeAssociation(frm.getPrimaryCode(),
                                                 frm.getSecondaryCode());
        }
      }
    }

    servletRequest.setAttribute("list", dao.getServiceCodeAssociactions());
    return fwd;
  }
}
