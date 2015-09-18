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

<!DOCTYPE html>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>

<%@ page import="oscar.form.FrmRecord"%>
<%@ page import="oscar.form.FrmRecordFactory"%>
<%@ page import="org.oscarehr.util.LocaleUtils"%>
<%@ page import="org.oscarehr.common.dao.DemographicDao"%>
<%@ page import="org.oscarehr.common.model.Demographic"%>
<%@ page import="org.oscarehr.util.SpringUtils"%>
<%@page import="org.oscarehr.util.LoggedInInfo" %>

<%
    String formClass = "Policy";
    String formLink = "formPolicy.jsp";   
    
    int formId = Integer.parseInt(request.getParameter("formId"));
    int demoNo = Integer.parseInt(request.getParameter("demographic_no"));
    
    FrmRecord rec = (new FrmRecordFactory()).factory(formClass);
    java.util.Properties props = rec.getFormRecord(LoggedInInfo.getLoggedInInfoFromSession(request),demoNo, formId);  
    
    DemographicDao demoDao = (DemographicDao) SpringUtils.getBean("demographicDao");
    Demographic demo = demoDao.getDemographic(request.getParameter("demographic_no"));
    String demoName = demo.getFormattedName();
    
%>

<html:html locale="true">
    <head>
        <script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>    
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>No Show and Cancellation Policy</title>
        <link rel="stylesheet" type="text/css" href="westernuStyle.css">                        
        <script src="../share/javascript/prototype.js" type="text/javascript"></script>
    </head>
    
    <script type="text/javascript" language="Javascript">

    function onExit() {
            window.close();
        return(false);
    }    

    function onPrintPDF() {
        document.forms[0].submit.value="printall";                    
        document.forms[0].action = "../form/formname.do?__title=Student+Health+Services+Policy&__cfgfile=PolicyFormPrint&__cfgfile=PolicyFormPrint&__template=PolicyForm-<%=props.getProperty("formVersion","")%>"; 
        document.forms[0].target="_blank";       
        return true;
    }
    
    </script>     
    
    <body bgproperties="fixed" topmargin="0" leftmargin="0" rightmargin="0" onload="window.resizeTo(768,768)" bgcolor="#eeeeee">
        <html:form action="/form/formname">   
            <input type="hidden" name="form_class" value="<%=formClass%>" />
            <input type="hidden" name="form_link" value="<%=formLink%>" />
            <input type="hidden" name="formId" value="<%=formId%>" />
            <input type="hidden" name="demographic_no" value="<%= props.getProperty("demographic_no", "0") %>" />
            <input type="hidden" name="submit" value="exit"/>          
            
            <h1>No Show and Cancellation Policy</h1>
                        
            <!--Patient Information--> 
            <table border="0" cellspacing="0" cellpadding="0" width="740px">                  
		<tr>
		<td class="borderGrayBottomRight">
                    <font class="subHeading">Patient Name : </font>
                    <input type="text" name="patientName" value="<%=demo.getFormattedName() %>" /> 
		</td>
                </tr>              
		<tr>
		<td class="borderGrayBottomRight">
                    <font class="subHeading">Chart Number : </font>
                    <input type="text" name="chartno" value="<%=demo.getChartNo() %>" />
		</td>
                </tr>  
		<tr>
		<td class="borderGrayBottomRight">
                    <font class="subHeading">Health Card Number : </font>
                    <input type="text" name="hin" value="<%=demo.getHin() %>" />
		</td>
                </tr>                
                <tr>
                    <td class="borderGrayBottomRight">
                    <font class="subHeading">Demographic Number: </font>
                    <input type="text" name="demographic_no" value="<%=props.getProperty("demographic_no","")%>"/>
                </td> 
                </tr>               
            </table>            

            
            <!--Policy Information-->    
            <hr/> 
            
            <table border="0" cellspacing="0" cellpadding="0" width="740px">            
                <tr>
                     <td>Policy Form Version Signed: </font>
                     <input type="text" name="formVersion" value="<%=props.getProperty("formVersion","")%>" />
                     </td>  
                </tr>    
                <tr>
                     <td>Date Policy Form Signed: </font>
                     <input type="text" name="formCreated" value="<%=props.getProperty("formCreated","")%>" />
                     </td>  
                </tr>                
            </table>

            <!--Print --> 
	    <table>
		<tr>
		     <td nowrap="true">			
                     <input type="submit" value="Print Policy v.<%=props.getProperty("formVersion","")%>" onClick="javascript:return onPrintPDF();" />
                     <input id="exitbut" type="submit" value="Exit" onclick="javascript: return onExit();" />
		     </td>
		</tr>
	    </table>
               
        </html:form>
    </body>   
</html:html>
