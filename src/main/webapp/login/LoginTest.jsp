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

<%--     
    Author     : rjonasz
--%>
<%@page import="java.util.List, java.util.Map, java.util.Date, java.util.Calendar, java.util.GregorianCalendar, java.io.*,java.text.SimpleDateFormat" %>
<%@page import="org.oscarehr.util.SpringUtils,org.oscarehr.common.dao.PropertyDao,org.oscarehr.common.model.Property" %>
<%@page import="org.oscarehr.common.service.AcceptableUseAgreementManager" %>
<%@page import="org.oscarehr.util.SpringUtils,org.oscarehr.PMmodule.dao.ProviderDao,org.oscarehr.common.model.Provider" %>
<%
	ProviderDao providerDao = SpringUtils.getBean(ProviderDao.class);
%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

        <%
           /*
           -If the user has an agreement proceed with login
           -If they don't read the file out
           -If reading fails proceed with login
           
           */
            Provider provider = providerDao.getProvider((String)session.getAttribute("user"));
        	Date signdate = null;
        	if(provider != null) {
        		signdate = provider.getSignedConfidentiality();
        	}
           

            Date cutoff = AcceptableUseAgreementManager.getAgreementCutoffDate();
            //out.write("cutoff "+cutoff+" "+signdate.after(cutoff)+" signdate "+signdate);

            if( !AcceptableUseAgreementManager.hasAUA() || (signdate != null && signdate.after(cutoff)) ) {  
                String proceedURL = (String)request.getSession().getAttribute("proceedURL");
                proceedURL = request.getContextPath() + proceedURL;
                request.getSession().setAttribute("proceedURL", null);                
                response.sendRedirect(proceedURL);
            }
          %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
            <title><bean:message key="provider.login.title.confidentiality"/></title>
    </head>
    <body>
        <div style="text-align:center;">
            <h3><bean:message key="provider.login.title.confidentiality"/></h3>
        <div style="margin-left:auto; margin-right:auto; text-align:left; width:70%; padding:5px; border:2px groove black;"><%=AcceptableUseAgreementManager.getAUAText()%></div>
        <br>
        <form method="post" action="<c:out value="${pageContext.request.contextPath}/login/recordLogin.do"/>">
            <input type="hidden" name="submit" id="submit">
            <input type="submit" onclick="document.forms[0].submit.value='accept';return true;" value="<bean:message key="provider.login.btn.agree"/>">
            <input type="submit" onclick="document.forms[0].submit.value='refuse';return true;" value="<bean:message key="provider.login.btn.refuse"/>">
        </form>
        </div>
    </body>
</html>