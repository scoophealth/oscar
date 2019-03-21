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
<%@page import="org.oscarehr.common.model.OscarLog"%>
<%@page import="org.oscarehr.util.LoggedInInfo"%>
<%@page import="oscar.log.LogAction"%>
<%@page import="java.util.Enumeration"%>
<%@page import="org.oscarehr.common.model.Demographic"%>
<%@page import="org.oscarehr.common.dao.DemographicDao"%>
<%@page import="org.oscarehr.common.model.Allergy"%>
<%@page import="java.util.List"%>
<%@page import="org.oscarehr.util.SpringUtils"%>
<%@page import="org.oscarehr.common.dao.AllergyDao"%>
<%@page contentType="text/html"%>
<%@ include file="/casemgmt/taglibs.jsp"%>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
    String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
	boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_admin" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../securityError.jsp?type=_admin");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>


<%
String curUser_no = (String) session.getAttribute("user");

DemographicDao demographicDao = SpringUtils.getBean(DemographicDao.class);
AllergyDao allergyDao = SpringUtils.getBean(AllergyDao.class);

List<Allergy> allergies = allergyDao.findAllCustomAllergiesWithNullNonDrugFlag(0,500);

if("migrate".equals(request.getParameter("action"))) {
	Enumeration e =  request.getParameterNames();
	while(e.hasMoreElements()) {
		String key = (String)e.nextElement();
		if(key.startsWith("nonDrug_")) {
			String allergyId = key.substring(8);
			String value = request.getParameter(key);
			
			Allergy allergy = allergyDao.find(Integer.parseInt(allergyId));
			if(allergy != null) {
				allergy.setNonDrug(Boolean.valueOf(value));
				allergyDao.merge(allergy);
				
				OscarLog log = new OscarLog();
				log.setAction("Allergy");
				log.setContentId(allergy.getId().toString());
				log.setContent("Update NonDrug flag");
				log.setData(value);
				log.setDemographicId(allergy.getDemographicNo());
				log.setIp(request.getRemoteAddr());
				log.setProviderNo(LoggedInInfo.getLoggedInInfoFromSession(request).getLoggedInProviderNo());
				LogAction.addLogSynchronous(log);
			}
		}
	}
}
%>

<html:html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<script type="text/javascript" src="<%= request.getContextPath() %>/share/jquery/jquery-1.4.2.js"></script>
<script type="text/javascript" src="../share/javascript/Oscar.js"></script>
<link rel="stylesheet" type="text/css" href="../oscarEncounter/encounterStyles.css">

<script type="text/JavaScript">
function popupPage(vheight,vwidth,varpage) { //open a new popup window
  var page = "" + varpage;
  windowprops = "height="+vheight+",width="+vwidth+",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes,screenX=0,screenY=0,top=0,left=0";//360,680
  var popup=window.open(page, "groupno", windowprops);
  if (popup != null) {
    if (popup.opener == null) {
      popup.opener = self;
    }
    popup.focus();
  }
}
</script>
<html:base />

<title>Bulk fix custom allergies</title>


</head>


<body>
        <table class="MainTable" id="scrollNumber1" name="encounterTable" style="margin: 0px;width:80%">
            <tr class="topbar">
                <td class="MainTableTopRowLeftColumn" width="60px">Migrate Allergies</td>
                <td class="MainTableTopRowRightColumn">
                    <table class="TopStatusBar">
                        <tr>
                            <td>Set non-drug flag for Custom Allergies</td>
                            <td style="text-align: right;"  > 
                                    <a href="javascript: popupStart(300, 400, 'About.jsp')">About</a> |
                                    <a href="javascript: popupStart(300, 400, 'License.jsp')">License</a>
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
         </table>
         <br/>
         
         <%if(request.getParameter("action") == null) { %>
       
        <form action="migrateCustomAllergyNonDrug.jsp">
          
        <table  cellspacing="0" style="margin: 0px;width:50%" border="1" >
        	<thead>
        		<tr>
        			
        			<th>Patient</th>
        			<th>Allergy</th>
        			<th>Non-Drug</th>
        		</tr>
        	</thead>
        	<tbody>
        		<%for(Allergy a:allergies) { 
        			Demographic demographic =  demographicDao.getDemographicById(a.getDemographicNo());
        		%>
        		<tr>
        			
	            	<td><a href="javascript:void()" onClick="popupPage(800,1000,'<%=request.getContextPath()%>/demographic/demographiccontrol.jsp?demographic_no=<%=demographic.getDemographicNo()%>&displaymode=edit&dboperation=search_detail')"><%=demographic.getFormattedName()%><br/><%=demographic.getFormattedDob()%><br/></a></td>
	            	<td><a href="javascript:void()" onClick="popupPage(600,1000,'<%=request.getContextPath()%>/oscarRx/ShowAllergies2.jsp?demographicNo=<%=demographic.getDemographicNo()%>')"><%=a.getDescription() %></a></td>
	            	<td>
	            		<input type="radio" name="nonDrug_<%=a.getId()%>" value="false">Drug &nbsp;&nbsp;
	            		<input type="radio" name="nonDrug_<%=a.getId()%>" value="true">Non-Drug
	            	</td>
            	</tr>
            	<% } %>
        	</tbody>
        </table>
        <br/>
        
        
       
        	<input type="submit" value="Save All Changes"/>
        	<input type="hidden" name="action" value="migrate"/>
        </form>
        
        <% } else { 
        	boolean result =true;
        	if(result) {
        		%><h4>Changes were successful</h4><%
        	} else {
        		%><h4>An Error Occurred</h4><%
        	}
        } %>
         
</body>

</html:html>
