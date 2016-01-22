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

<%-- Used in registering users
REQUREMENTS: (2 PARAMS)
1.  pathtophr - example: ../../phr/ (tail slash requrement) (as of July 22, 2009 - not needed)
2.  forwardto - where to go after user is authenticated
--%><%--
The taglib directive below imports the JSTL library. If you uncomment it,
you must also add the JSTL library to the project. The Add Library... action
on Libraries node in Projects view can be used to add the JSTL 1.1 library.
--%>
<%--
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%> 
--%>
<%@page import="org.oscarehr.phr.util.MyOscarUtils"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic" %>

<%@ page import="oscar.oscarProvider.data.ProviderData"%>

<%
String providerName = request.getSession().getAttribute("userfirstname") + " " + 
        request.getSession().getAttribute("userlastname");
String providerNo = (String) request.getSession().getAttribute("user");
ProviderData providerData = new ProviderData();
providerData.setProviderNo(providerNo);
String providerPhrId = providerData.getMyOscarId();
//this particular <logic:present statement does not search parameters in request scope for some reason
pageContext.setAttribute("phrUserLoginErrorMsg", request.getAttribute("phrUserLoginErrorMsg"));
pageContext.setAttribute("phrTechLoginErrorMsg", request.getAttribute("phrTechLoginErrorMsg"));

%>

   <style type="text/css">
        .myoscarLoginElementNoAuth {
            border: 0;
            padding-left: 3px;
            padding-right: 3px;
            background-color: #f3e9e9;
            font-size: 12px;
        }
        
        .myoscarLoginElementAuth {
            border: 0;
            padding-left: 3px;
            padding-right: 3px;
            background-color: #d9ecd8;
            font-size: 12px;
        }
        .moreInfoBoxoverBody{
            border: 1px solid #9fbbe8;
            padding: 1px;
            padding-left: 3px;
            padding-right: 3px;
            border-top: 0px;
            font-size: 10px;
            background-color: white;
        }
        .moreInfoBoxoverHeader{
            border: 1px solid #9fbbe8;
            background-color: #e8ecf3;
            padding: 2px;
            padding-left: 3px;
            padding-right: 3px;
            border-bottom: 0px;
            font-size: 10px;
            color: red;
        }
   </style>
   <%
		if (MyOscarUtils.isMyOscarEnabled((String) session.getAttribute("user")))
		{
			%>
				<div class="myoscarLoginElementAuth">
				    Status: <b>Logged in as <%=providerName%></b><br/>
				    <form action="<%=request.getContextPath()%>/phr/Logout.do" name="phrLogout" method="POST">
				        <input type="hidden" name="forwardto" value="<%=(String) request.getAttribute("forwardto")%>">
				        <center><a href="javascript:;" onclick="document.forms['phrLogout'].submit();">Logout</a></center>
				    </form>
				</div>
			<%
		}
		else
		{
			%>
		        <div class="myoscarLoginElementNoAuth">
		            <form action="<%=request.getContextPath()%>/phr/Login.do" name="phrLogin" method="POST">
		                <%request.setAttribute("phrUserLoginErrorMsg", request.getParameter("phrUserLoginErrorMsg"));
		                request.setAttribute("phrTeAttributechLoginErrorMsg", request.getParameter("phrTechLoginErrorMsg"));%>
		                <logic:present name="phrUserLoginErrorMsg">
		                    <div class="phrLoginErrorMsg" style="color: red;"><bean:write name="phrUserLoginErrorMsg"/> 
		                    <logic:present name="phrTechLoginErrorMsg">
		                    <a href="javascript:;" title="fade=[on] requireclick=[off] cssheader=[moreInfoBoxoverHeader] cssbody=[moreInfoBoxoverBody] singleclickstop=[on] header=[MyOSCAR Server Response:] body=[<bean:write name="phrTechLoginErrorMsg"/> </br>]">More Info</a>
		                    </logic:present>
		                    </div> 
		                </logic:present>
		                <logic:notPresent name="phrUserLoginErrorMsg">
		                    Login Required.  
		                </logic:notPresent>
		                Status: <b>Not logged in</b><br/>
		                <%=providerName%> password: <input type="password" id="phrPassword" name="phrPassword" style="font-size: 8px; width: 40px;"> <a href="javascript: document.forms['phrLogin'].submit()">Login</a>
		                <script type="text/javascript" language="JavaScript">document.getElementById('phrPassword').focus()</script>
		                <input type="hidden" name="forwardto" value="<%=request.getParameter("forwardto")%>">
		            </form>
		        </div>
		        <script type="text/javascript" src="<%=request.getContextPath()%>/share/javascript/boxover.js"></script>
			<%
		}
	%>
        