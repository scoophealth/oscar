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

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.oscarehr.common.dao.forms.FormsDao;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.oscarDB.DBHandler;

public class RptRehabStudyAction extends Action {

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		RptRehabStudyForm frm = (RptRehabStudyForm) form;

		if (request.getSession().getAttribute("user") == null) {
			response.sendRedirect("../logout.htm");
		}

		String formName = frm.getFormName();
		String startDate = frm.getStartDate();
		String endDate = frm.getEndDate();
		StringBuilder results = new StringBuilder("<table>");
		FormsDao dao = SpringUtils.getBean(FormsDao.class);
		
		try {
            String sql = "select * from " + formName + " limit 1"; 
            ResultSet rs = null;
            try {
	            rs = DBHandler.GetSQL(sql);
	            if(rs.next()) {
	                results.append(getHeadingStructure(rs));
	            }
            } finally {
            	if (rs != null) {
            		rs.close();
            	}
            }

			sql = "select max(formEdited) as formEdited, demographic_no from " + formName + " where formEdited > '" + startDate + "' and formEdited < '" + endDate + "' group by demographic_no";
			for(Object[] o : dao.runNativeQuery(sql)) {
				String formEdited = String.valueOf(o[0]);
				String demographic_no = String.valueOf(o[1]);
				
				String sqlDemo = "SELECT * FROM " + formName + " where demographic_no='" + demographic_no + "' AND formEdited='" + formEdited + "'";
				List<Object[]> fs = dao.runNativeQuery(sqlDemo);
				for(Object[] f : fs) {        
					results.append(getStructure(f));
				}
			}
		} catch (Exception e) {
			MiscUtils.getLogger().error("Error", e);
		}

		results.append("</table>");
		request.setAttribute("results", results.toString());
		request.setAttribute("formName", formName);

		return mapping.findForward("success");
	}

	public String getHeadingStructure(ResultSet rs) throws Exception {
		// assuming  multiple rows in rs
		StringBuilder sb = new StringBuilder();

		ResultSetMetaData rsmd = rs.getMetaData();
		int columns = rsmd.getColumnCount();
		String[] columnNames = new String[columns];
		for (int i = 0; i < columns; i++) { // for each column in result set
			columnNames[i] = rsmd.getColumnName(i + 1);
			// put names in array
			// use i+1 or else you're going to get an exception
			// insert headings for table
			sb.append("<th class='headerColor'>");
			sb.append(columnNames[i]);
			sb.append("</th>");
		}
		return sb.toString();
	}

	public String getStructure(Object[] os) throws Exception {
		if (os == null) {
			return "";
		}
		
		// assuming  multiple rows in rs
		StringBuilder sb = new StringBuilder();
		int columns = os.length;
		for(Object o : os) {
			sb.append("<tr>");
			
			for (int j = 0; j < columns; j++) {
				sb.append("<td>");
				sb.append(o);
				sb.append("</td>");
			}
			sb.append("</tr>");
		}
		return sb.toString();
	}

}
