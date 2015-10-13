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
<security:oscarSec roleName="<%=roleName$%>" objectName="_demographic" rights="w" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect(request.getContextPath() + "/securityError.jsp?type=_demographic");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>

<%@page import="org.oscarehr.util.LoggedInInfo"%>
<%@ page import="oscar.oscarReport.data.DemographicSets, oscar.oscarDemographic.data.DemographicData" %>
<%@ page import="java.util.ArrayList" %>
<%@ include file="/casemgmt/taglibs.jsp" %>

<%
	LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <title>JSP Page</title>
        <style type="text/css">
            a:hover {
                font-weight: bold;
            }

            div.demographicSection{
               //width:49%;
               width:100%;
               margin-top: 2px;
               margin-left:3px;
               border-top: 1px solid #ccccff;
               border-bottom: 1px solid #ccccff;
               border-left: 1px solid #ccccff;
               border-right: 1px solid #ccccff;
               float: left;
            }

            div.demographicSection h3 {
               background-color: #ccccff;
               font-size: 8pt;
               font-variant:small-caps;
               font:bold;
               margin-top:0px;
               padding-top:0px;
               margin-bottom:0px;
               padding-bottom:0px;
            }

            div.demographicSection ul{

                   list-style:none;
                   list-style-type:circle;
                   list-style-position:inside;
                   padding-left:1px;
                   margin-left:1px;
                   margin-top:0px;
                   padding-top:1px;
                   margin-bottom:0px;
                   padding-bottom:0px;
                   background-color: #EEEEFF;
            }


            div.demographicSection li {
            padding-right: 15px;
            white-space: nowrap;
            }
        </style>
    </head>
    <body>
        <div class="demographicSection">
            <%
            String demoNo = request.getParameter("demographic_no");
            DemographicSets demoSets = new DemographicSets();
            java.util.List<String> arrCurDemoSets = demoSets.getDemographicSets(demoNo);
            ArrayList arrDemo = new ArrayList();
            arrDemo.add(demoNo);
            pageContext.setAttribute("curSets",arrCurDemoSets);
            DemographicData demoData = new DemographicData();
            String setName = request.getParameter("setName");
            if( setName != null && setName.trim().length() > 0 ) {
            if( !arrCurDemoSets.contains(setName) ) {
            demoSets.addDemographicSet(setName,arrDemo);
            arrCurDemoSets.add(setName);
            %>
            <p style="font-size:small; font-variant:small-caps"><bean:message key="demographic.demographiccohort.saved" /> <%=demoData.getDemographic(loggedInInfo, demoNo).getFirstName() + " " + demoData.getDemographic(loggedInInfo, demoNo).getLastName()%> <bean:message key="demographic.demographiccohort.to" /> <%=setName%></p>
            <%
            }
            }
            java.util.List<String> arrDemoSets = demoSets.getDemographicSets();
            pageContext.setAttribute("arrDemoSets", arrDemoSets);
            %>  
            <h3><bean:message key="demographic.demographiccohort.currentpatientset" /></h3>
            <ul>
                <logic:iterate id="set" name="curSets">
                    <li><c:out value="${set}"/></li>
                </logic:iterate>
            </ul>
            <h3><bean:message key="demographic.demographiccohort.addtopatientset" /></h3>
            <ul>
                <logic:iterate id="set" name="arrDemoSets">
                    <li><a href="demographicCohort.jsp?demographic_no=<%=demoNo%>&setName=<c:out value="${set}"/>"><c:out value="${set}"/></a></li>
                </logic:iterate>
            </ul>
            <br>
            <form method="get" action="demographicCohort.jsp">
                <input type="hidden" name="demographic_no" value="<%=demoNo%>">
                <h3><bean:message key="demographic.demographiccohort.newpatientset" /></h3>
                <input type="text" name="setName">&nbsp;<input type="submit" value="<bean:message key="demographic.demographiccohort.save" />">
            </form>
        </div>
    </body>
</html>
