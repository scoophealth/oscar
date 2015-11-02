<%--

    Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
    This software is published under the GPL GNU General Public License.
    This program is free software; you can redistribute it and/or
    modify it under the terms of the GNU General Public License
    as published by the Free Software Foundation; either version 2
    of the License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program; if not, write to the Free Software
    Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.

    This software was written for the
    Department of Family Medicine
    McMaster University
    Hamilton
    Ontario, Canada

--%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
      String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
      boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_report,_admin.reporting" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../securityError.jsp?type=_report&type=_admin.reporting");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>

<%@page import="org.oscarehr.util.MiscUtils"%><%@page import="org.apache.poi.hssf.usermodel.HSSFRow,org.apache.poi.hssf.usermodel.HSSFSheet,org.apache.poi.hssf.usermodel.HSSFWorkbook,com.Ostermiller.util.CSVParser"%><%

    String csv = (String) session.getAttribute("clinicalReportCSV");
    String action = request.getParameter("getCSV");
        if (action != null) {
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment; filename=\"oscarReport.csv\"");
            try {
                response.getWriter().write(csv);
            } catch (Exception ioe) {
            	MiscUtils.getLogger().error("Error", ioe);
            }
        }
        action = request.getParameter("getXLS");
        if (action != null) {
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment; filename=\"oscarReport.xls\"");
            String[][] data = CSVParser.parse(csv);
            HSSFWorkbook wb = new HSSFWorkbook();
            HSSFSheet sheet = wb.createSheet("OSCAR_Report");
            for (int x=0; x<data.length; x++) {
                HSSFRow row = sheet.createRow((short)x);
                for (int y=0; y<data[x].length; y++) {
                    try{
                       double d = Double.parseDouble(data[x][y]);
                        row.createCell((short)y).setCellValue(d);
                    }catch(Exception e){
                       row.createCell((short)y).setCellValue(data[x][y]);
                    }
                }
            }
            try {    
                wb.write(response.getOutputStream());
		return;
            } catch(Exception e) {
            	MiscUtils.getLogger().error("Error", e);
            }
            
        }
       

%>
