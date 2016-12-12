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
<security:oscarSec roleName="<%=roleName$%>" objectName="_dxresearch" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../../securityError.jsp?type=_dxresearch");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>

<%@ page
	import="java.math.*, java.util.*, java.sql.*, oscar.*, java.net.*, oscar.oscarResearch.oscarDxResearch.bean.*"%>
<ul
	<%
   String demoNO = request.getParameter("demographicNo");
   String maxlen = request.getParameter("maxlen");
   int len = -1;
   
   try{
      len = Integer.parseInt(maxlen);         
   }catch(Exception e){}   
      
   dxResearchBeanHandler dxResearchBeanHand = new dxResearchBeanHandler(demoNO);
   Vector patientDx = dxResearchBeanHand.getDxResearchBeanVector();
   
   ArrayList<dxResearchBean> patientDxA = new ArrayList<dxResearchBean>();
   for (int i=0; i<patientDx.size(); i++) {
      dxResearchBean code = (dxResearchBean)patientDx.get(i);
      //sort the list by descriptions
   	  int j=0;
   	  for (j=0; j<patientDxA.size(); j++) {
   		  if (patientDxA.get(j).getDescription().compareToIgnoreCase(code.getDescription())>=0) {
   			  patientDxA.add(j, code); break;
   		  }
   	  }
   	  if (j==patientDxA.size()) patientDxA.add(code);
   }
   for (int i=0; i<patientDxA.size(); i++) {
      String desc = patientDxA.get(i).getDescription();
      if (len != -1){
         desc = org.apache.commons.lang.StringUtils.abbreviate(desc,len) ;
      }
   %>>
	<li>- <%=desc%></li<%
   }%>
>
</ul>
