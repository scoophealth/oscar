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


package oscar.oscarReport.pageUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.oscarehr.util.MiscUtils;

import oscar.oscarReport.data.RptSearchData;

/**
 *
 * @author jay
 */
public class RptDemographQueryFavouriteDeleteAction extends Action {


    public ActionForward execute(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) {
        
        String queriesToDelete[] = request.getParameterValues("queryFavourite");
        
        if (queriesToDelete != null){
            RptSearchData searchData = new RptSearchData();
            for (int i=0; i < queriesToDelete.length; i++){
               searchData.deleteQueryFavourite(queriesToDelete[i]);
               MiscUtils.getLogger().debug("to Delete "+queriesToDelete[i]);    
            }
        }
        
        return mapping.findForward("success");
    }
    
    /** Creates a new instance of RptDemographQueryFavouriteDeleteAction */
    public RptDemographQueryFavouriteDeleteAction() {
    }
    
}
