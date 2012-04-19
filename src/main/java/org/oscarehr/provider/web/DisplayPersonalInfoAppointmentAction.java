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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

/**
 * Toggles variable for displaying reason on appointment page
 * @author rjonasz
 */
public class DisplayPersonalInfoAppointmentAction extends DispatchAction {
    
    /** Creates a new instance of DisplayPersonalInfoAppointmentAction */
    public DisplayPersonalInfoAppointmentAction() {
    }
    
     public ActionForward toggle(ActionMapping actionmapping,
                               ActionForm actionform,
                               HttpServletRequest request,
                               HttpServletResponse response) {
            
        Boolean showPersonal = (Boolean)request.getSession().getAttribute("showPersonal");
            
        if( showPersonal == null ) {
            showPersonal = true;        
        }
        else {
            showPersonal = !showPersonal;
        }
        
        request.getSession().setAttribute("showPersonal",showPersonal);
        
        return null;
     }
     
}
