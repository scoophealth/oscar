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
package org.oscarehr.common.web;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import org.apache.commons.lang.WordUtils;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.LabelValueBean;
import org.oscarehr.common.dao.AbstractCodeSystemDao;
import org.oscarehr.common.model.AbstractCodeSystemModel;
import org.oscarehr.util.SpringUtils;
/**
 * @author marc
 *
 */
public class CodeSearchService extends Action {

	@Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		List<LabelValueBean> results = new ArrayList<LabelValueBean>();
		String codingSystem = request.getParameter("codingSystem");

		AbstractCodeSystemDao dao = (AbstractCodeSystemDao)SpringUtils.getBean(WordUtils.uncapitalize(codingSystem) + "Dao");

		if(request.getParameter("term") != null && request.getParameter("term").length() > 0) {
			List<AbstractCodeSystemModel> r = dao.searchCode(request.getParameter("term"));
			for(AbstractCodeSystemModel result:r) {
				results.add(new LabelValueBean(result.getDescription(),result.getCode()));
			}
		}
		response.setContentType("text/x-json");
        JSONArray jsonArray = JSONArray.fromObject( results );
        jsonArray.write(response.getWriter());

	    return null;
    }


}
