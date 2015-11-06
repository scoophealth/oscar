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


package oscar.oscarRx.pageUtil;

import java.io.IOException;
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.oscarehr.managers.SecurityInfoManager;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.SpringUtils;

import oscar.oscarRx.data.RxDrugData;



public final class RxDrugInfoAction extends Action {
	private SecurityInfoManager securityInfoManager = SpringUtils.getBean(SecurityInfoManager.class);


    public ActionForward execute(ActionMapping mapping,
				 ActionForm form,
				 HttpServletRequest request,
				 HttpServletResponse response)
	throws IOException, ServletException {

		if (!securityInfoManager.hasPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), "_rx", "r", null)) {
			throw new RuntimeException("missing required security object (_rx)");
		}
            
            
            String GN = null;
            String BN = null;

            if(request.getParameter("GN") != null){
                if(! request.getParameter("GN").equals("null")){
                    GN = request.getParameter("GN");                    
                    response.sendRedirect("http://resource.oscarmcmaster.org/oscarResource/OSCAR_search/OSCAR_search_results?title="+URLEncoder.encode(GN,"UTF-8"));
                }
            }

            if(request.getParameter("BN") != null){
                if(! request.getParameter("BN").equals("null")){
                    BN = request.getParameter("BN");
                    RxDrugData drugData = new RxDrugData();
                    String genName = null;
                    try{
                     genName = drugData.getGenericName(BN);                       
                    }catch(Exception e){
                     genName = BN   ;
                    }
                    response.sendRedirect("http://resource.oscarmcmaster.org/oscarResource/OSCAR_search/OSCAR_search_results?title="+URLEncoder.encode(genName,"UTF-8"));
                }
            }
            
            if(GN == null && BN == null){
                //Need to return to just the search page on oscarResource
            }
                                    
            return null;
    }
}
