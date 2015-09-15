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
    String roleName2$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
    boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName2$%>" objectName="_form" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../securityError.jsp?type=_form");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>

<%@ page import="oscar.util.*, oscar.form.*, oscar.form.data.*,java.util.*, oscar.oscarProvider.data.*, oscar.oscarPrevention.*"%>

<% 
                String id = request.getParameter("id");
                String date = request.getParameter("date");
                
                if (id != null) { 
                ArrayList<Map<String,Object>> alist = PreventionData.getPreventionDataFromExt("workflowId", id);       
                
                for (int k = 0; k < alist.size(); k++){
               	Map<String,Object> hdata = alist.get(k);
                Map<String,String> hextended = PreventionData.getPreventionKeyValues(""+hdata.get("id"));
                String refused = (String) hdata.get("refused");
                %>
<fieldset><legend> Injection # <%=k+1%> &nbsp;
&nbsp; &nbsp; Date: <%=hdata.get("preventionDate")%> &nbsp; &nbsp;
&nbsp; Weeks: <%=UtilDateUtilities.calculateGestationAge( (Date) hdata.get("prevention_date_asDate") , UtilDateUtilities.StringToDate (date))%>
</legend> <%if ( refused.equals("1")){ %> Refused <a
	onclick="deleteInjection('<%=hdata.get("id")%>')"
	href="javascript: function myFunction() {return false; }"
	style="color: blue;"> Delete </a> <%}else{%> Given By: <%=ProviderData.getProviderName((String)hdata.get("provider_no"))%>
Location: <%=hextended.get("location")  %> Lot #: <%=hextended.get("lot")  %>
Product #: <%=hextended.get("product")  %> Dosage: <%=hextended.get("dosage")  %>
<a onclick="deleteInjection('<%=hdata.get("id")%>')"
	href="javascript: function myFunction() {return false; }"
	style="color: blue;"> Delete </a> </br>
Reason: <%=hextended.get("reason")  %> <%}%>
</fieldset>
<%  }
                 }%>
