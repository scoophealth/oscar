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


/**
 * PrintLabsAction.java
 *
 * Created on November 27, 2007, 9:42 AM
 * Author: Adam Balanga
 */

package oscar.oscarLab.ca.all.pageUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.oscarehr.util.MiscUtils;

import oscar.oscarLab.ca.all.Hl7textResultsData;
import oscar.oscarLab.ca.all.parsers.Factory;
import oscar.oscarLab.ca.all.parsers.OLISHL7Handler;

/**
 *
 * @author wrighd
 */
public class PrintOLISLabsAction extends Action{
    
    Logger logger = Logger.getLogger(PrintLabsAction.class);
    
    /** Creates a new instance of PrintLabsAction */
    public PrintOLISLabsAction() {
    }
    
    public ActionForward execute(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response){
        
    	try {
	    	String segmentID = request.getParameter("segmentID");
	    	int obr = Integer.valueOf(request.getParameter("obr"));
	    	int obx = Integer.valueOf(request.getParameter("obx"));
	    	
	    	if ("true".equals(request.getParameter("showLatest"))) {
	    		String multiLabId = Hl7textResultsData.getMatchingLabs(segmentID);
	    		segmentID = multiLabId.split(",")[multiLabId.split(",").length - 1];
	    	}
		    	
	    	OLISHL7Handler handler = (OLISHL7Handler) Factory.getHandler(segmentID);

	    	handler.processEncapsulatedData(request, response, obr, obx);
    	}
    	catch (Exception e) {
    		MiscUtils.getLogger().error("error",e);
    	}
    	return null;
    }
}
