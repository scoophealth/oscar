/**
 *
 * Copyright (c) 2005-2012. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved.
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
 * This software was written for
 * Centre for Research on Inner City Health, St. Michael's Hospital,
 * Toronto, Ontario, Canada
 */


package org.oscarehr.provider.web;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.oscarehr.common.dao.ViewDao;
import org.oscarehr.common.model.View;

/**
 *
 * @author rjonasz
 */
public class ProviderViewAction extends DispatchAction {

    private ViewDao userViewDAO;

    /** Creates a new instance of ProviderViewAction */
    public ProviderViewAction() {
    }

    public void setUserViewDAO(ViewDao viewDao) {
        this.userViewDAO = viewDao;
    }

     public ActionForward unspecified(ActionMapping actionmapping,
                               ActionForm actionform,
                               HttpServletRequest request,
                               HttpServletResponse response) {

        return null;
    }

      public ActionForward save(ActionMapping actionmapping,
                               ActionForm actionform,
                               HttpServletRequest request,
                               HttpServletResponse response) {

         String view_name = request.getParameter("view_name");
         String role = (String)request.getSession().getAttribute("userrole");
         Map<String,View> map = this.userViewDAO.getView(view_name,role);

         String [] names = request.getParameterValues("name");
         String [] values = request.getParameterValues("value");
         View v;

         //first we delete any current view
         Set<String> keys = map.keySet();
         String key;
         for( Iterator<String> iter = keys.iterator(); iter.hasNext();) {
             key = iter.next();
             v = map.get(key);
             this.userViewDAO.delete(v);
         }

         //now we save new view
         for( int idx = 0; idx < names.length; ++idx ) {
             v = new View();
             v.setName(names[idx]);
             v.setRole(role);
             v.setValue(values[idx]);
             v.setView_name(view_name);
             this.userViewDAO.saveView(v);
         }

         return null;
     }

}
