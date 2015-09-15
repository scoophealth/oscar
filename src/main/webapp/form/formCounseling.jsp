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
<%@ page import="org.oscarehr.common.dao.ClinicDAO"%>
<%@ page import="org.oscarehr.common.model.Clinic"%>
<%@ page import="org.oscarehr.PMmodule.dao.ProviderDao"%>
<%@ page import="org.oscarehr.util.SpringUtils"%>
<%@page import="org.oscarehr.util.LoggedInInfo"%>

<%
    String formClass = "Counseling";
    String formLink = "formCounseling.jsp";
    String projectHome = request.getContextPath().substring(1);
    
    int formId = Integer.parseInt(request.getParameter("formId"));
    int demoNo = Integer.parseInt(request.getParameter("demographic_no"));
    
    FrmRecord rec = (new FrmRecordFactory()).factory(formClass);
    java.util.Properties props = rec.getFormRecord(LoggedInInfo.getLoggedInInfoFromSession(request),demoNo, formId);  
    
    DemographicDao demoDao = (DemographicDao) SpringUtils.getBean("demographicDao");
    Demographic demo = demoDao.getDemographic(request.getParameter("demographic_no"));
    String demoName = demo.getFormattedName();   

    ProviderDao providerDao = (ProviderDao) SpringUtils.getBean("providerDao");
    String providerNo = props.getProperty("provider_no","");
    String providerName = "";
    if (providerNo != null && !providerNo.isEmpty() && !providerNo.equals("999998")){
        providerName = providerDao.getProviderName(providerNo);
    }
    else {
        providerName = LocaleUtils.getMessage(request.getLocale(),"oscarEncounter.formCounseling.notValidated");    
    }
    props.setProperty("doc_name",providerName);    
%>


<html:html locale="true">
<% response.setHeader("Cache-Control","no-cache");%>
                                                       
<style type="text/css" media="print">
 .DoNotPrint {
	 display: none;
 }
</style>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<title>Counseling</title>
</head>
<body bgproperties="fixed" topmargin="0" leftmargin="0" rightmargin="0" onload="window.resizeTo(768,768)" bgcolor="#eeeeee">
        <html:form action="/form/formname">
        <input type="hidden" name="demographic_no" value="<%= props.getProperty("demographic_no", "0") %>" />
	<div class="DoNotPrint">
	<p> This date field must formatted as seen (DD/MM/YYYY), you may change the date but do not change formatting. </p>
	</div>
	<input type="text" name="formCreated" value="<%=props.getProperty("formCreated","")%>" />
        <input type="hidden" name="form_class" value="<%=formClass%>" />
        <input type="hidden" name="form_link" value="<%=formLink%>" />
        <input type="hidden" name="formId" value="<%=formId%>" />
        <input type="hidden" name="demographic_no" value="<%= props.getProperty("demographic_no", "0") %>" /> 
        <input type="hidden" name="doc_name" value="<%=props.getProperty("doc_name", "")%>" />        
        <input type="hidden" name="cl_name" value="<%= props.getProperty("clinicName", "") %>" />
        <input type="hidden" name="cl_address1" value="<%=props.getProperty("clinicAddress","")%>" />
        <input type="hidden" name="cl_address2" value="<%=props.getProperty("clinicCity","")%>" />
        <input type="hidden" name="cl_phone" value="<%=props.getProperty("clinicPhone","")%>" />
        <input type="hidden" name="cl_fax" value="<%=props.getProperty("clinicFax","")%>" />
        <input type="hidden" name="submit" value="exit"/>

        <div style="font-size: 24px; font-family: arial, helvetica, sans-serif;">
            <center>
                <b>Counselling Note</b>
            </center>
        </div>
        <div style="font-size: 19px; font-family: arial, helvetica, sans-serif;">
            <center>
                <b><i><%=props.getProperty("clinicName","")%></i></b>
            </center>
        </div>
        <font face="Arial, Helvetica, sans-serif">
        <TABLE WIDTH="100%" align="center" cellpadding="0" cellspacing="0"  style="font-size: 13px;">
            <TR>
                <TD><%=props.getProperty("clinicAddress","")%></TD>
                <TD ALIGN="right">Phone: <%=props.getProperty("clinicPhone","")%></TD>
            </TR>
            
            <TR>
                <TD><%=props.getProperty("clinicCity","")%></TD>
                <TD ALIGN="right">Fax: <%=props.getProperty("clinicFax","")%></TD>
            </TR>
            
        </TABLE>
        <center>
            <TABLE WIDTH="100%" cols="2" style="font-size: 13px;">
                <TD>
                <TD>
                    <TABLE WIDTH="100%" align="center" style="border: 1px solid;" >
                        <TR>
                            <TD align="left" width="40%">Patient:</TD>
                            <TD align="left">
                                
                                    <INPUT NAME="p_name" style="border: none; font-size: 13px; text-decoration: underline; width: 100%;" TYPE="text" value="<%=demo.getFormattedName() %>">
                                    </INPUT>
                                
                            </TD>
                        </TR>
                        <TR>
                            <TD align="left">Address:</TD>
                            <TD align="left">
                                
                                    <INPUT NAME="p_address1" style="border: none; font-size: 13px; text-decoration: underline; width: 100%;" TYPE="text" value="<%=demo.getAddress() %>, <%=demo.getCity() %>">
                                    </INPUT>
                                
                            </TD>
                        </TR>
                        <TR>
                            <TD align="left">&nbsp;</TD>
                            <TD align="left">
                                
                                    <INPUT NAME="p_address2" style="border: none; font-size: 13px; text-decoration: underline; width: 100%;" TYPE="text" value="<%=demo.getProvince() %>, <%=demo.getPostal() %>">
                                    </INPUT>			
                                
                            </TD>
                        </TR>
                        <TR>
                            <TD align="left">Phone:</TD>
                            <TD align="left">
                                <INPUT NAME="p_phone" style="border: none; font-size: 13px; text-decoration: underline; width: 100%;" TYPE="text" value="<%=demo.getPhone() %>">
                                   </INPUT>			
                                
                            </TD>
                        </TR>
                        <TR>
                            <TD align="left">Birthdate:</TD>
                            <TD align="left">
                                <INPUT NAME="p_birthdate" style="border: none; font-size: 13px; text-decoration: underline; width: 100%;" TYPE="text" value="<%=demo.getFormattedDob() %>">
                                </INPUT>                           
                            </TD>
                        </TR>
                        <TR>			
                            <TD align="left">Health Card No:</TD>
                            <TD align="left">
                                <INPUT NAME="p_healthcard" style="border: none; font-size: 13px; text-decoration: underline; width: 100%;" TYPE="text" value="<%=demo.getHin() %>">
                                </INPUT>
                            </TD>
                        </TR>
                    </TABLE>
                </TD>
            </TABLE>
        </center>
        <br>


<div id="textareaDiv" style="position: relative;width:800px">
    <textarea id="comments" name="comments" cols="100" rows="60" style="overflow:hidden; font-family:courier new; font-size:12px;"><%=props.getProperty("comments", "")%></textarea>
</div>

        <div id="buttons">
            <input id="savebut" type="submit" value="Save" onclick="javascript: return onSave();" />
            <input id="saveexitbut" type="submit" value="Save and Exit" onclick="javascript: return onSaveExit();" />
            <input id="exitbut" type="submit" value="Exit" onclick="javascript: return onExit();" />
            <input id="printPDF" type="submit" value="Print PDF" onclick="javascript: return onPrintPDF();"/>
        </div>
</body>
   
<script type="text/javascript">
    function reset() {        
        document.forms[0].target = "";
        document.forms[0].action = "/<%=projectHome%>/form/formname.do" ;
    }

    function onSave() {
        document.forms[0].submit.value="save";
        reset();        
        ret = confirm("Are you sure you want to save this form?");
        return ret;
    }

    function onSaveExit() {
        document.forms[0].submit.value="exit";
        reset();  
        ret = confirm("Are you sure you wish to save and close this window?");
        return ret;
    }

    function onExit() {
        if(confirm("Are you sure you wish to exit without saving your changes?")==true)
        {
            window.close();
        }
        return(false);
    }
        
    function onPrintPDF() {
        temp=document.forms[0].action;
        document.forms[0].submit.value="printall";
        document.forms[0].action = "../form/formname.do?__title=form+Counseling&__cfgfile=CounselingNotePrint&__template=CounselingNoteForm";
        document.forms[0].target="_blank";
        return true;
    }

</script>
</html:form>
</html:html>
