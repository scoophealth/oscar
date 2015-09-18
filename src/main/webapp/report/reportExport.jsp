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

<%@page contentType="application/octet-stream"%><%@page
	import="oscar.oscarDemographic.data.*,java.util.*,oscar.oscarPrevention.*,oscar.oscarProvider.data.*,oscar.util.*,oscar.oscarReport.ClinicalReports.*"%>
<%
     
   
     ArrayList arrList = (ArrayList) session.getAttribute("ClinicalReports"); 
     if (arrList != null){
        String id = request.getParameter("id");
        
        if (id != null){
        ReportEvaluator re = (ReportEvaluator) arrList.get(Integer.parseInt(id));
        
        String filename = re.getName().replaceAll("/","---").replaceAll(" ","_"); // "report.txt";
        response.addHeader("Content-Disposition", "attachment;filename=" + filename);
        
        out.write(re.getCSV());
        }else{
            response.addHeader("Content-Disposition", "attachment;filename=report.txt" );   
            for (int i = 0; i < arrList.size();i++){
               ReportEvaluator re = (ReportEvaluator) arrList.get(i);     
               String filename = re.getName().replaceAll("/","---").replaceAll(" ","_"); // "report.txt";
                 
               out.write("'"+filename+"',"+re.getCSV()+'\n');
            }
        }
     }else{
        response.addHeader("Content-Disposition", "attachment;filename=error.txt" ); 
        out.write("ERROR:No report found.");
     }
     
%>
