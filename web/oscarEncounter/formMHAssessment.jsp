<!--  
/*
 * 
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
 * This software is published under the GPL GNU General Public License. 
 * This program is free software; you can redistribute it and/or 
 * modify it under the terms of the GNU General Public License 
 * as published by the Free Software Foundation; either version 2 
 * of the License, or (at your option) any later version. * 
 * This program is distributed in the hope that it will be useful, 
 * but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
 * GNU General Public License for more details. * * You should have received a copy of the GNU General Public License 
 * along with this program; if not, write to the Free Software 
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. * 
 * 
 * <OSCAR TEAM>
 * 
 * This software was written for the 
 * Department of Family Medicine 
 * McMaster Unviersity 
 * Hamilton 
 * Ontario, Canada 
 */
-->

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="oscar.oscarEncounter.data.EctMentalHealthRecord, java.util.*" %>
<%@ page import="java.io.FileInputStream" %>
<jsp:useBean id="oscarVariables" class="java.util.Properties" scope="session" />
<html:html locale="true">
<% response.setHeader("Cache-Control","no-cache");%>
<head>
<title>Mental Health Assessment and Intervention Plan</title>
<html:base/>
<link rel="stylesheet" type="text/css" media="screen" href="mhStyles.css" >
<link rel="stylesheet" type="text/css" media="print" href="print.css">

<%
    int demoNo = Integer.parseInt(request.getParameter("demographic_no"));
    int formId = Integer.parseInt(request.getParameter("formId"));
    int provNo = Integer.parseInt(request.getParameter("provNo"));
    	String projecthome = oscarVariables.getProperty("project_home");
   EctMentalHealthRecord rec = new EctMentalHealthRecord();
    java.util.Properties props = rec.getMentalHealthRecord(demoNo, formId, provNo);
    oscar.oscarEncounter.util.EctFileUtil list = new oscar.oscarEncounter.util.EctFileUtil();
    props.setProperty("c_lastVisited", "Assessment");
    props.setProperty("a_formDate", oscar.util.UtilDateUtilities.DateToString(oscar.util.UtilDateUtilities.Today(), "yyyy/MM/dd"));
%>


<script type="text/javascript" language="Javascript">
    function onPrint() {
        document.forms[0].submit.value="printAssessment";
        var ret = confirm("Do you wish to save this form and view the print preview?");
        return ret;
    }
    function onSave() {
        document.forms[0].submit.value="save";
        return(confirm("Are you sure you want to save this form?"));
    }
    function onExit() {
        if(confirm("Are you sure you wish to exit without saving your changes?")==true)
        {
            window.close();
        }
        return(false);
    }
    function insert(fromName, num) {
        switch(fromName) {
        case "a_aps":
            if(document.forms[0].a_aps1.value=="") {
                document.forms[0].a_aps1.value = num;
            }else if(document.forms[0].a_aps2.value=="") {
                document.forms[0].a_aps2.value = num;
            }else if(document.forms[0].a_aps3.value=="") {
                document.forms[0].a_aps3.value = num;
            }
            break;
        case "a_api":
            if(document.forms[0].a_api1.value=="") {
                document.forms[0].a_api1.value = num;
            }else if(document.forms[0].a_api2.value=="") {
                document.forms[0].a_api2.value = num;
            }else if(document.forms[0].a_api3.value=="") {
                document.forms[0].a_api3.value = num;
            }
            break;
        case "a_ampi":
            if(document.forms[0].a_ampi1.value=="") {
                document.forms[0].a_ampi1.value = num;
            }else if(document.forms[0].a_ampi2.value=="") {
                document.forms[0].a_ampi2.value = num;
            }else if(document.forms[0].a_ampi3.value=="") {
                document.forms[0].a_ampi3.value = num;
            }
            break;
        case "a_tp":
            if(document.forms[0].a_tp1.value=="") {
                document.forms[0].a_tp1.value = num;
            }else if(document.forms[0].a_tp2.value=="") {
                document.forms[0].a_tp2.value = num;
            }else if(document.forms[0].a_tp3.value=="") {
                document.forms[0].a_tp3.value = num;
            }
            break;
        default:
            break;
        }
    }
</script>
</head>

<body bgproperties="fixed" topmargin="0" leftmargin="0" rightmargin="0">
<html:form action="/oscarEncounter/MentalHealth">

<input type="hidden" name="demographic_no" value="<%= props.getProperty("demographic_no", "0") %>" />
<input type="hidden" name="ID" value="<%= props.getProperty("ID", "0") %>"/>
<input type="hidden" name="provider_no" value=<%=request.getParameter("provNo")%> />
<input type="hidden" name="formCreated" value="<%= props.getProperty("formCreated", "") %>" />
<input type="hidden" name="formEdited" value="<%= props.getProperty("formEdited", "") %>" />
<input type="hidden" name="formId" value="<%=formId%>" />
<input type="hidden" name="c_lastVisited" value=<%=props.getProperty("c_lastVisited", "Referral")%> />
<input type="hidden" name="submit" value="exit"/>

<table class="Head" class="hidePrint">
    <tr>
        <td align="left">
            <input type="submit" value="Save" onclick="javascript:return onSave();" />
            <input type="submit" value="Save and Exit" onclick="javascript:return onSaveExit();"/>
            <input type="submit" value="Exit" onclick="javascript:return onExit();"/>
            <input type="submit" value="Print" onclick="javascript:return onPrint();"/>
        </td>
        <td align="right">
            <a href="formMHReferral.jsp?demographic_no=<%=demoNo%>&formId=<%=formId%>&provNo=<%=provNo%>">Referral</a>
            &nbsp;|&nbsp;
            Assessment
            &nbsp;|&nbsp;
            <a href="formMHOutcome.jsp?demographic_no=<%=demoNo%>&formId=<%=formId%>&provNo=<%=provNo%>">Outcome</a>
        </td>
    </tr>
</table>

<table cellpadding="1" cellspacing="0" class="mainTable" bgcolor="#F0F8FF">
    <tr>
        <th align="center">
            <big>MENTAL HEALTH ASSESSMENT and INTERVENTION PLAN</big><br><br>
        </th>
    </tr>
    <tr>
        <td colspan="4">
            <table border="0" cellpadding="2" cellspacing="0" width="100%">
                <tr>
                    <td width="50%" rowspan="5">&nbsp;</td>
                    <td>Name:</td>
                    <td align="right"><input type="text" name="c_pName" size="40" value="<%= props.getProperty("c_pName", "") %>" readonly="true"/></td>
                </tr>
                <tr>
                    <td>Sex:</td>
                    <td align="right"><input type="text" name="c_sex" size="40" value="<%= props.getProperty("c_sex", "") %>" readonly="true"/></td>
                </tr>
                </tr>
                    <td>Address:</td>
                    <td align="right"><input type="text" name="c_address" size="40" value="<%= props.getProperty("c_address", "") %>" readonly="true"/></td>
                </tr>
                <tr>
                    <td>Home Phone:</td>
                    <td align="right"><input type="text" name="c_homePhone" size="40" value="<%= props.getProperty("c_homePhone", "") %>" readonly="true"/></td>
                </tr>
                <tr>
                    <td>Birth Date <small>(yyyy/mm/dd)</small>: </td>
                    <td align="right"><input type="text" name="c_birthDate" size="40" value="<%= props.getProperty("c_birthDate", "") %>" readonly="true"/></td>
                </tr>
            </table>
        </td>
    </tr>
    <tr>
        <td>
            <table class="TableWithBorder" cellpadding="2" cellspacing="0" width="100%">
                <tr>
                    <td>Referral Date<small>(yyyy/mm/dd)</small>: </td>
                    <td><input type="text" name="c_referralDate" size="40" value="<%= props.getProperty("c_referralDate", "") %>" readonly="true" /></td>
                    <td>Referred By: </td>
                    <td align="right"><input type="text" name="c_referredBy" size="40" value="<%= props.getProperty("c_referredBy", "") %>" readonly="true" /></td>
                </tr>
                <tr>
                    <td>Assessment Date<small>(yyyy/mm/dd)</small>: </td>
                    <td><input type="text" name="a_formDate" size="40" value="<%= props.getProperty("a_formDate", "") %>" readonly="true"/></td>
                    <td>HSO Specialist: </td>
                    <td align="right"><input type="text" name="a_specialist" size="40" value="<%= props.getProperty("a_specialist", "") %>" /></td>
                </tr>
            </table>
        </td>
    </tr>
    <tr>
        <td colspan="4">
            <table border="1" cellpadding="2" cellspacing="0" width="100%">
                <tr>
                    <td class="mhList" valign="top">
                        Psychiatric Symptoms:<br>
                        <% String[] aps = list.loadData("mhAssessment/PsychiatricSymptoms.txt", projecthome );
                            for (int i=0; i<aps.length; i++)
                            {
                                if(aps[i]!=null)
                                {
                        %>
                                    <a onclick="javascript:insert('a_aps', <%=i+1%>);"><%=i+1%>. <%= aps[i] %></a><br>
                        <%
                                }
                            }
                        %>
                        &nbsp;<input type="text" name="a_apsOther" value="<%= props.getProperty("a_apsOther", "") %>" />
                    </td>
                    <td class="mhList" valign="top">
                        Psychosocial Issues:<br>
                        <% String[] api = list.loadData("mhAssessment/PsychosocialIssues.txt", projecthome );
                            for (int i=0; i<api.length; i++)
                            {
                                if(api[i]!=null)
                                {
                        %>
                                    <a onclick="javascript:insert('a_api', <%=i+1%>);"><%=i+1%>. <%= api[i] %></a><br>
                        <%
                                }
                            }
                        %>
                        &nbsp;<input type="text" name="a_apiOther" value="<%= props.getProperty("a_apiOther", "") %>" />
                    </td>
                    <td class="mhList" valign="top">
                        Med/Phy Issues:<br>
                        <% String[] ampi = list.loadData("mhAssessment/MedPhyIssues.txt", projecthome );
                            for (int i=0; i<ampi.length; i++)
                            {
                                if(ampi[i]!=null)
                                {
                        %>
                                    <a onclick="javascript:insert('a_ampi', <%=i+1%>);"><%=i+1%>. <%= ampi[i] %></a><br>
                        <%
                                }
                            }
                        %>
                        &nbsp;<input type="text" name="a_ampiOther" value="<%= props.getProperty("a_ampiOther", "") %>" />
                    </td>
                </tr>
                <tr>
                    <td class="mhSelect">
                        Psychiatric Symptoms:<br>
                        1. <input type="text" name="a_aps1" value="<%= props.getProperty("a_aps1", "") %>" /><br>
                        2. <input type="text" name="a_aps2" value="<%= props.getProperty("a_aps2", "") %>" /><br>
                        3. <input type="text" name="a_aps3" value="<%= props.getProperty("a_aps3", "") %>" />
                    </td>
                    <td class="mhSelect">
                        Psychosocial Issues:<br>
                        1. <input type="text" name="a_api1" value="<%= props.getProperty("a_api1", "") %>" /><br>
                        2. <input type="text" name="a_api2" value="<%= props.getProperty("a_api2", "") %>" /><br>
                        3. <input type="text" name="a_api3" value="<%= props.getProperty("a_api3", "") %>" />
                    </td>
                    <td class="mhSelect">
                        Med/Phy Issues:<br>
                        1. <input type="text" name="a_ampi1" value="<%= props.getProperty("a_ampi1", "") %>" /><br>
                        2. <input type="text" name="a_ampi2" value="<%= props.getProperty("a_ampi2", "") %>" /><br>
                        3. <input type="text" name="a_ampi3" value="<%= props.getProperty("a_ampi3", "") %>" />
                    </td>
                </tr>
                <tr>
                    <td class="mhList" valign="top">
                        Treatment Plan:<br>
                        <% String[] tp = list.loadData("mhAssessment/TreatmentPlan.txt", projecthome );
                            for (int i=0; i<tp.length; i++)
                            {
                                if(tp[i]!=null)
                                {
                        %>
                                    <a onclick="javascript:insert('a_tp', <%=i+1%>);"><%=i+1%>. <%= tp[i] %></a><br>
                        <%
                                }
                            }
                        %>
                        &nbsp;<input type="text" name="a_tpOther" value="<%= props.getProperty("a_tpOther", "") %>" />
                    </td>
                    <td colspan="2" rowspan="2" class="mhList" valign="top">
                        Assessment Comments:<br>
                        <textarea class="mhAssTextarea" name="a_assComments"><%= props.getProperty("a_assComments", "") %></textarea>
                    </td>
                </tr>
                <tr>
                    <td class="mhSelect">
                        Treatment Plan:<br>
                        1. <input type="text" name="a_tp1" value="<%= props.getProperty("a_tp1", "") %>" /><br>
                        2. <input type="text" name="a_tp2" value="<%= props.getProperty("a_tp2", "") %>" /><br>
                        3. <input type="text" name="a_tp3" value="<%= props.getProperty("a_tp3", "") %>" />
                    </td>
                </tr>
            </table>
        </td>
    </tr>
</table>
<table class="Head" class="hidePrint">
    <tr>
        <td align="left">
            <input type="submit" value="Save" onclick="javascript:return onSave();" />
            <input type="submit" value="Save and Exit" onclick="javascript:return onSaveExit();"/>
            <input type="submit" value="Exit" onclick="javascript:return onExit();"/>
            <input type="submit" value="Print" onclick="javascript:return onPrint();"/>
        </td>
        <td align="right">
            <a href="formMHReferral.jsp?demographic_no=<%=demoNo%>&formId=<%=formId%>&provNo=<%=provNo%>">Referral</a>
            &nbsp;|&nbsp;
            Assessment
            &nbsp;|&nbsp;
            <a href="formMHOutcome.jsp?demographic_no=<%=demoNo%>&formId=<%=formId%>&provNo=<%=provNo%>">Outcome</a>
        </td>
    </tr>
</table>

</html:form>
</body>
</html:html>