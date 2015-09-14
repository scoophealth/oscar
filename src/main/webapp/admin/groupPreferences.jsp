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
 
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>

<%@ page import="java.util.List"%>
<%@ page import="org.oscarehr.util.SpringUtils" %>
<%@ page import="org.oscarehr.common.model.MyGroup, org.oscarehr.common.dao.MyGroupDao"%>
<%@ page import="org.oscarehr.common.model.MyGroupPrimaryKey" %>
<%@ page import="org.oscarehr.common.model.CtlBillingService, org.oscarehr.common.dao.CtlBillingServiceDao"%>

<%
    String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
    boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_admin,_admin.userAdmin" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../securityError.jsp?type=_admin&type=_admin.userAdmin");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>

<%
    MyGroupDao myGroupDao = (MyGroupDao) SpringUtils.getBean("myGroupDao");
    CtlBillingServiceDao ctlBillingServiceDao = (CtlBillingServiceDao) SpringUtils.getBean("ctlBillingServiceDao");
    String currentForm = "";
%>
<html:html locale="true">
    <head>
            <script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
            <script type="text/javascript" src="<%= request.getContextPath() %>/js/jquery-1.7.1.min.js"></script>          
            <script type="text/javascript">
		 function changeBillingForm() {
                        document.forms["groupPreference"].method.value="setDefaultBillingForm";
                        document.forms["groupPreference"].submit();
                }

                function savePreference() {
                        document.forms["groupPreference"].method.value="save";
                        document.forms["groupPreference"].submit();
                }
            </script>
            <title><bean:message key="admin.grouppref.title" /></title>
    </head>


    <body topmargin="0" leftmargin="0" rightmargin="0">

        <html:form method="post" action="/admin/GroupPreference">
            
            <input type="hidden" id="method" name="method"/>
            
            <!--Header-->
            <table border=0 cellspacing=0 cellpadding=0 width="100%">
		<tr bgcolor="#486ebd">
                    <th align=CENTER NOWRAP>
                        <font face="Helvetica" color="#FFFFFF">
                                <bean:message key="admin.grouppref.title" />
                        </font>
                    </th>
                </tr>
            </table>
            
            <!--Group/Billing Form Content-->
            <div style="text-align: center">
                <table border="0" cellpadding="0" cellspacing="0" width="80%" style="margin-left:auto; margin-right:auto; text-align:left">
                    <tr>
                        <td width="100%">

                            <table BORDER="0" CELLPADDING="0" CELLSPACING="1" WIDTH="100%" BGCOLOR="#C0C0C0">
                                <tr BGCOLOR="#CCFFFF">
                                    <td ALIGN="center" style="font-weight:bold; font-family:sans-serif"><bean:message key="admin.grouppref.selectBillingForm" /></td>
                                    <td ALIGN="center">					
                                        <select id="chosenForm" name="chosenForm" onChange="changeBillingForm()">
                                                    <option value=""></option>
                                                    <%
                                                        List<Object[]> forms = ctlBillingServiceDao.getUniqueServiceTypes();
                                                        for(Object[] form:forms) {
                                                            String serviceType = (String)form[0];
                                                            String selected="";
                                                            if(serviceType.equals(request.getParameter("chosenForm"))) {
                                                                    currentForm = serviceType; 
                                                                    selected=" selected=\"selected\" ";
                                                            }
                                                    %>
                                                    <option value="<%=serviceType%>" <%=selected%>><%=(String)form[1]%></option>
                                                    <%  } %>						
                                            </select>
                                    </td>
                                </tr>

                                <%	
                                    List<String> groups = myGroupDao.getGroups();

                                    int i=0;
                                    for(String group:groups) {

                                        String selected="";
                                        String defaultBillForm = myGroupDao.getDefaultBillingForm(group);
                                        if(defaultBillForm != null && defaultBillForm.equals(currentForm)) {
                                                selected=" checked=\"checked\" ";
                                        }

                                        i++;
                                %>
                                <tr BGCOLOR="<%=i%2==0?"ivory":"white"%>">
                                    <td>&nbsp; <%=group%></td>
                                    <td ALIGN="center">
                                            <input type="checkbox" name="data" <%=selected%> value="<%=group%>"> 					
                                </tr>
                                <%  } %>	
                            </table>
                        </td>
                    </tr>
                </table>
            </div>
            <!--footer-->
            <table width="100%" BGCOLOR="#486ebd">
                <tr>
                    <td align="center">
			<input type="button" name="Submit" value="<bean:message key='admin.adminnewgroup.btnSubmit'/>" onClick="savePreference()"/>
			<input TYPE="reset" VALUE="<bean:message key='global.btnClose'/>" onClick="window.close();">
                    </td>
                </tr>
            </table>

        </html:form>
    </body>
</html:html>
    

