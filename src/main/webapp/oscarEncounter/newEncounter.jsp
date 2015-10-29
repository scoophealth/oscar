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

<%@page import="org.oscarehr.util.SpringUtils"%>
<%@page import="org.oscarehr.common.model.Provider"%>
<%@page import="org.oscarehr.common.model.Security"%>
<%@ page import="org.oscarehr.util.LoggedInInfo" %>
<%@ page import="org.oscarehr.managers.SecurityManager" %>
<%
	LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);
	org.oscarehr.managers.SecurityManager securityManager = SpringUtils.getBean(org.oscarehr.managers.SecurityManager.class);
%>
<%@page
	import="java.util.Collections, java.util.Arrays, java.util.ArrayList"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01//EN"
   "http://www.w3.org/TR/html4/strict.dtd">

<%@page import="org.oscarehr.util.MiscUtils"%><html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>New Encounter Access</title>
<script type="text/javascript" LANGUAGE="JavaScript">

        function checkAll(formId){
	   var f = document.getElementById(formId);
           var val = f.checkA.checked;
	   for (i =0; i < f.encTesters.length; i++){
	   	f.encTesters[i].checked = val;
	   }
	}
        </script>

</head>
<body>

<%  
    ArrayList newDocArr = (ArrayList) request.getSession().getServletContext().getAttribute("newDocArr");
    Boolean useNewEchart = (Boolean) request.getSession().getServletContext().getAttribute("useNewEchart");
    
    String userNo = (String) request.getSession().getAttribute("user"); 
    if( userNo != null && (newDocArr != null || (useNewEchart != null && useNewEchart.equals(Boolean.TRUE))) ) {
        
        if( newDocArr == null ) newDocArr = new ArrayList();
        
        if( request.getMethod().equalsIgnoreCase("get") ) {
%>
<h3>Assign New Encounter to:</h3>

<form method="post" action="newEncounter.jsp" id="sbForm"><input
	type="checkbox" name="checkAll2" onclick="checkAll('sbForm')"
	id="checkA" /> Check All<br>
<%            
				for(Object[] o : securityManager.findProviders(loggedInInfo)) {
					Security s = (Security) o[0];
					Provider p = (Provider) o[1];
					
                    String provNo = p.getProviderNo();
                    if( newDocArr.contains(provNo)) {
%> <input type="checkbox" name="encTesters" value="<%=provNo%>" checked><%=p.getLastName()%>,
<%=p.getFirstName()%><br>
<%
                    } else {
%> <input type="checkbox" name="encTesters" value="<%=provNo%>"><%=p.getLastName()%>,
<%=p.getFirstName()%><br>
<%
                    }                
                }
%> <input type="submit" value="Update">
</form>
<%
        }
        else {
            String[] encTesters = request.getParameterValues("encTesters");

            if( encTesters == null ) {
                newDocArr.clear();
            } else {
                newDocArr = new ArrayList(Arrays.asList(encTesters));
            }
            
            Collections.sort(newDocArr);
        
            request.getSession().getServletContext().setAttribute("newDocArr", newDocArr);
        
%>
<h3>New Testers Update Complete!</h3>

<%
        }

    }
    else {
%>
<p>You have to be logged in to use this form</p>

<%
    }
%>

</body>
</html>
