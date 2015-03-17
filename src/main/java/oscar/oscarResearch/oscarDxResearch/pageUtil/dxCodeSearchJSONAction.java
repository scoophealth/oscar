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
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.oscarehr.common.dao.Icd9Dao;
import org.oscarehr.common.model.Icd9;
import org.oscarehr.util.JsonUtil;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

public class dxCodeSearchJSONAction extends DispatchAction {

	private static Logger logger = MiscUtils.getLogger(); 
	
	public ActionForward searchICD9(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response) {

		String keyword = request.getParameter("keyword");
		Icd9Dao dao = SpringUtils.getBean(Icd9Dao.class);		
		List<Icd9> icd9List =  dao.getIcd9(keyword);
		
		try {
	        jsonify(icd9List, response);
        } catch (IOException e) {
        	logger.error("JSON Error", e);
        }

		return null;
	}


	private static void jsonify(final List<?> classList, 
	    		final HttpServletResponse response) throws IOException {

		response.setContentType("text/x-json");
		
		String jsonstring = JsonUtil.pojoCollectionToJson(classList);
		PrintWriter pout = response.getWriter();
		pout.write(jsonstring);
	
		pout.flush();
		pout.close();
		         
    }
	 
		
}
