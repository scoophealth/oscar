<%--


    Copyright (c) 2005-2012. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved.
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

    This software was written for
    Centre for Research on Inner City Health, St. Michael's Hospital,
    Toronto, Ontario, Canada

--%>
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

<%@page import="org.oscarehr.util.MiscUtils"%>
<%@page import="org.oscarehr.common.dao.ProviderPreferenceDao"%>
<%@page import="org.oscarehr.util.SpringUtils"%>
<%@page import="org.oscarehr.common.model.ProviderPreference"%><html>
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
	String userNo = (String) session.getAttribute("user");
    ArrayList<String> newDocArr = (ArrayList<String>)request.getSession().getServletContext().getAttribute("CaseMgmtUsers");    
    
    ProviderPreferenceDao providerPreferenceDao=(ProviderPreferenceDao)SpringUtils.getBean("providerPreferenceDao");
    ProviderPreference providerPreference=providerPreferenceDao.find(userNo);
    
	

	String newCME = null;
	if(providerPreference!=null){
		newCME = providerPreference.getDefaultNewOscarCme();			
	}
	
	if(newDocArr.isEmpty()) {
		if("enabled".equals(newCME)){
			newDocArr.add(userNo);
			request.getSession().getServletContext().setAttribute("CaseMgmtUsers",newDocArr);
		}
    }
    

    if( userNo != null && newDocArr != null ) {
        
        if( request.getMethod().equalsIgnoreCase("get") ) {
%>
<h3>Assign New Casemanagement Screen to:</h3>

<form method="post" action="newCaseManagementEnable.jsp" id="sbForm">
<%            
				for(Object[] o : securityManager.findProviders(loggedInInfo)) {
					Security s = (Security) o[0];
					Provider p = (Provider) o[1];
                    String provNo = p.getProviderNo();
                    if(!userNo.equals(provNo)) 
                    	continue;
                    if( newDocArr.contains("all") || newDocArr.contains(provNo)) {
%> <input type="checkbox" name="encTesters" value="<%=provNo%>" checked><%=p.getLastName()%>,
<%=p.getFirstName()%><br>
<%
                    }
                    else {
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
            String encTesters = request.getParameter("encTesters");

            if( encTesters == null ) {                
                
                if( newDocArr.contains("all") ) {
                    newDocArr.clear();
                    for(Object[] o : securityManager.findProviders(loggedInInfo)) {
    					Security s = (Security) o[0];
    					Provider p = (Provider) o[1];
                    
                        String provNo = p.getProviderNo();
                        if( userNo.equals(provNo) ) 
                            continue;
                            
                        newDocArr.add(provNo);
                   }
                }
                else {
                    newDocArr.remove(userNo);
                }
                
            }
            else
                newDocArr.add(encTesters);
            
            Collections.sort(newDocArr);
        
            request.getSession().getServletContext().setAttribute("CaseMgmtUsers", newDocArr);

            if (providerPreference==null)
            {
            	// insert a default preference
				providerPreference=new ProviderPreference();
            	providerPreference.setProviderNo(userNo);
            	providerPreferenceDao.persist(providerPreference);
            } 
            	
			if(encTesters!=null && !encTesters.isEmpty()) {
				providerPreference.setDefaultNewOscarCme("enabled");
			} else {
				providerPreference.setDefaultNewOscarCme("disabled");
			}
			providerPreferenceDao.merge(providerPreference);
			
%>
<h3>Casemanagement Users Update Complete!</h3>

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
