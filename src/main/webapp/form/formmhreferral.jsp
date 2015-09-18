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

<%@ page import="oscar.form.*, java.util.*"%>
<%@ page import="java.io.FileInputStream"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@page import="org.oscarehr.util.LoggedInInfo"%>
<% java.util.Properties oscarVariables = oscar.OscarProperties.getInstance(); %>

<html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>Mental Health Referral</title>
<html:base />
<link rel="stylesheet" type="text/css" media="screen"
	href="mhStyles.css">
<link rel="stylesheet" type="text/css" media="print" href="print.css">
<script type="text/javascript" language="Javascript">

<%
	String formClass = "MentalHealth";
	String formLink = "formmhreferral.jsp";

    int demoNo = Integer.parseInt(request.getParameter("demographic_no"));
    int formId = Integer.parseInt(request.getParameter("formId"));
	int provNo = Integer.parseInt((String) session.getAttribute("user"));
	FrmRecord rec = (new FrmRecordFactory()).factory(formClass);
    java.util.Properties props = rec.getFormRecord(LoggedInInfo.getLoggedInInfoFromSession(request),demoNo, formId);
    if ( formId ==0 ){
		props = ((FrmMentalHealthRecord) rec).getFormCustRecord(props, provNo);
    }
    oscar.oscarEncounter.util.EctFileUtil list = new oscar.oscarEncounter.util.EctFileUtil();
    props.setProperty("c_lastVisited", "referral");

    String projecthome = oscarVariables.getProperty("project_home");
    String path = "form/dataFiles";

	if (props.getProperty("demo_roster_status")!=null && !props.getProperty("demo_roster_status").equalsIgnoreCase("RO")) 
		out.println("alert(\"Only rostered patients can be referred to Social Worker.\\n(Roster Status: \\\"" + props.getProperty("demo_roster_status") + "\\\")\");");
%>
	
	function popupFixedPage(vheight,vwidth,varpage) { 
		var page = "" + varpage;
		windowprop = "height="+vheight+",width="+vwidth+",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes,screenX=10,screenY=0,top=0,left=0";
		var popup=window.open(page, "planner", windowprop);
	}

	var mainAction = "";
	var mainTarget = "";
	
	 function getMainAction(){
	    mainAction = document.forms[0].action;	  
	    mainTarget = docuemtn.forms[0].target;	  
	 }
	
    function onPrint() {
        document.forms[0].submit.value="print"; //printReferral
        var ret = numvalidate();
        //if(ret==true) {
            //ret = confirm("Do you wish to save this form and view the print preview?");
            popupFixedPage(650,850,'../provider/notice.htm');
            document.forms[0].action = "formmhreferralprint.jsp";
            document.forms[0].target="planner";
            document.forms[0].submit();
            document.forms[0].target="apptProviderSearch";
        //}
        return false;
    }
    function onSave() {
        document.forms[0].submit.value="save";
        document.forms[0].action = mainAction;
        document.forms[0].target = mainTarget;
        var ret = confirm("Are you sure you want to save this form?");
        
        return ret;
    }
    function onSaveExit() {
        document.forms[0].submit.value="exit";
        document.forms[0].action = mainAction;
        document.forms[0].target = mainTarget;
        var ret = confirm("Are you sure you wish to save and close this window?");
        
        return ret;
    }
    
    function insert(fromName, num) {
        switch(fromName) {
        case "r_rps":
            if(document.forms[0].r_rps1.value=="") {
                document.forms[0].r_rps1.value = num;
            }else if(document.forms[0].r_rps2.value=="") {
                document.forms[0].r_rps2.value = num;
            }else if(document.forms[0].r_rps3.value=="") {
                document.forms[0].r_rps3.value = num;
            }
            break;
        case "r_rpi":
            if(document.forms[0].r_rpi1.value=="") {
                document.forms[0].r_rpi1.value = num;
            }else if(document.forms[0].r_rpi2.value=="") {
                document.forms[0].r_rpi2.value = num;
            }else if(document.forms[0].r_rpi3.value=="") {
                document.forms[0].r_rpi3.value = num;
            }
            break;
        case "r_rmpi":
            if(document.forms[0].r_rmpi1.value=="") {
                document.forms[0].r_rmpi1.value = num;
            }else if(document.forms[0].r_rmpi2.value=="") {
                document.forms[0].r_rmpi2.value = num;
            }else if(document.forms[0].r_rmpi3.value=="") {
                document.forms[0].r_rmpi3.value = num;
            }
            break;
        case "r_ir":
            if(document.forms[0].r_ir1.value=="") {
                document.forms[0].r_ir1.value = num;
            }else if(document.forms[0].r_ir2.value=="") {
                document.forms[0].r_ir2.value = num;
            }else if(document.forms[0].r_ir3.value=="") {
                document.forms[0].r_ir3.value = num;
            }
            break;
        case "r_arm":
            if(document.forms[0].r_arm1.value=="") {
                document.forms[0].r_arm1.value = num;
            }else if(document.forms[0].r_arm2.value=="") {
                document.forms[0].r_arm2.value = num;
            }else if(document.forms[0].r_arm3.value=="") {
                document.forms[0].r_arm3.value = num;
            }
            break;
        default:
            break;
        }
    }

     function numvalidate() {

            if( document.forms[0].r_rps1.value != "" && isNaN(document.forms[0].r_rps1.value) ) {
                bringthToAttentionth(document.forms[0].r_rps1); 
                return false;
            }
            if(document.forms[0].r_rps2.value != "" && isNaN(document.forms[0].r_rps2.value)) {
                bringthToAttentionth(document.forms[0].r_rps2); 
                return false;
            }
            if(isNaN(document.forms[0].r_rps3.value)) {
                bringthToAttentionth(document.forms[0].r_rps3);
                return false;
            }
            if(isNaN(document.forms[0].r_rpi1.value)) {
                bringthToAttentionth(document.forms[0].r_rpi1); 
                return false;
            }
            if(isNaN(document.forms[0].r_rpi2.value)) {
                 bringthToAttentionth(document.forms[0].r_rpi2); 
                 return false;
            }
            if(isNaN(document.forms[0].r_rpi3.value)) {
                 bringthToAttentionth(document.forms[0].r_rpi3); 
                 return false;
            }
            if(isNaN(document.forms[0].r_rmpi1.value)) {
                 bringthToAttentionth(document.forms[0].r_rmpi1); 
                 return false;
            }
            if(isNaN(document.forms[0].r_rmpi2.value)) {
                 bringthToAttentionth(document.forms[0].r_rmpi2); 
                 return false;
            }
            if(isNaN(document.forms[0].r_rmpi3.value)) {
                 bringthToAttentionth(document.forms[0].r_rmpi3); 
                 return false;
            }
            if(isNaN(document.forms[0].r_ir1.value)) {
                 bringthToAttentionth(document.forms[0].r_ir1); 
                 return false;
            }
            if(isNaN(document.forms[0].r_ir2.value)) {
                 bringthToAttentionth(document.forms[0].r_ir2); 
                 return false;
            }
            if(isNaN(document.forms[0].r_ir3.value)) {
                 bringthToAttentionth(document.forms[0].r_ir3); 
                 return false;
            }

            if(isNaN(document.forms[0].r_arm1.value)) {
                 bringthToAttentionth(document.forms[0].r_arm1); 
                 return false;
            }
            if(isNaN(document.forms[0].r_arm2.value)) {
                 bringthToAttentionth(document.forms[0].r_arm2); 
                 return false;
            }
            if(isNaN(document.forms[0].r_arm3.value)) {
                 bringthToAttentionth(document.forms[0].r_arm3); 
                 return false;
            }
        
    }
    function bringthToAttentionth(docItem){
       docItem.focus();
       alert("This value must be numeric.");
    } 

    function  myFunction(){
        var pagey = 0; 
        return false;
    }
</script>
<link rel="stylesheet" type="text/css" media="all" href="../share/css/extractedFromPages.css"  />
</head>

<body bgproperties="fixed" topmargin="0" leftmargin="0" rightmargin="0"
	onload="getMainAction()">
<html:form action="/form/formname" onsubmit="return numvalidate()">

	<input type="hidden" name="demographic_no"
		value="<%= props.getProperty("demographic_no", "0") %>" />
	<input type="hidden" name="ID"
		value="<%= props.getProperty("ID", "0") %>" />
	<input type="hidden" name="provider_no"
		value=<%=request.getParameter("provNo")%> />
	<input type="hidden" name="formCreated"
		value="<%= props.getProperty("formCreated", "") %>" />
	<input type="hidden" name="form_class" value="<%=formClass%>" />
	<input type="hidden" name="form_link" value="<%=formLink%>" />
	<input type="hidden" name="formId" value="<%=formId%>" />
	<input type="hidden" name="c_lastVisited"
		value=<%=props.getProperty("c_lastVisited", "Referral")%> />
	<input type="hidden" name="submit" value="exit" />

	<table class="Head" class="hidePrint">
		<tr>
			<td align="left"><input type="submit" value="Save"
				onclick="javascript:return onSave();" /> <input type="submit"
				value="Save and Exit" onclick="javascript:return onSaveExit();" /> <input
				type="submit" value="Exit" onclick="javascript:return onExit();" />
			<input type="submit" value="Print"
				onclick="javascript:return onPrint();" /></td>
			<td align="right">Referral &nbsp;|&nbsp; <a
				href="formmhassessment.jsp?demographic_no=<%=demoNo%>&formId=<%=formId%>&provNo=<%=provNo%>">Assessment</a>
			&nbsp;|&nbsp; <a
				href="formmhoutcome.jsp?demographic_no=<%=demoNo%>&formId=<%=formId%>&provNo=<%=provNo%>">Outcome</a>
			</td>
		</tr>
	</table>

	<table cellpadding="1" cellspacing="0" class="mainTable"
		bgcolor="#E0EEE0">
		<tr>
			<th align="center"><big>MENTAL HEALTH REFERRAL</big><br>
			<br>
			</th>
		</tr>
		<tr>
			<td colspan="4">
			<table border="0" cellpadding="2" cellspacing="0" width="100%">
				<tr>
					<td width="50%" rowspan="5">&nbsp;</td>
					<td>Name:</td>
					<td align="right"><input type="text" name="c_pName" size="40"
						value="<%= props.getProperty("c_pName", "") %>" readonly="true" /></td>
				</tr>
				<tr>
					<td>Sex:</td>
					<td align="right"><input type="text" name="c_sex" size="40"
						value="<%= props.getProperty("c_sex", "") %>" readonly="true" /></td>
				</tr>
				</tr>
				<td>Address:</td>
				<td align="right"><input type="text" name="c_address" size="40"
					value="<%= props.getProperty("c_address", "") %>" readonly="true" /></td>
				</tr>
				<tr>
					<td>Home Phone:</td>
					<td align="right"><input type="text" name="c_homePhone"
						size="40" value="<%= props.getProperty("c_homePhone", "") %>"
						readonly="true" /></td>
				</tr>
				<tr>
					<td>Birth Date <small>(yyyy/mm/dd)</small>:</td>
					<td align="right"><input type="text" name="c_birthDate"
						size="40" value="<%= props.getProperty("c_birthDate", "") %>"
						readonly="true" /></td>
				</tr>
			</table>
			</td>
		</tr>
		<tr>
			<td>
			<table class="TableWithBorder" cellpadding="2" cellspacing="0"
				width="100%">
				<tr>
					<td>Referral Date<small>(yyyy/mm/dd)</small>:</td>
					<td><input type="text" name="c_referralDate" size="40"
						value="<%= props.getProperty("c_referralDate", "") %>" /></td>
					<td>Referred By:</td>
					<td align="right"><input type="text" name="c_referredBy"
						size="40" value="<%= props.getProperty("c_referredBy", "") %>" /></td>
				</tr>
			</table>
			</td>
		</tr>
		<tr>
			<td colspan="4">
			<table border="1" cellpadding="2" cellspacing="0" width="100%">
				<tr>
					<td class="mhList" valign="top">Psychiatric Symptoms: <br>
					<br>
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 1. <input type="text" name="r_rps1"
						value="<%= props.getProperty("r_rps1", "") %>" size="2" /> &nbsp;
					2. <input type="text" name="r_rps2"
						value="<%= props.getProperty("r_rps2", "") %>" size="2" /> &nbsp;
					3. <input type="text" name="r_rps3"
						value="<%= props.getProperty("r_rps3", "") %>" size="2" /> <br>
					<br>
					<% String[] rps = list.loadData("mhReferral/PsychiatricSymptoms.txt", projecthome, path );
                            for (int i=0; i<rps.length; i++)
                            {
                                if(rps[i]!=null)
                                {
                        %> <a
						href="javascript: function myFunction() {return false; }"
						class="mhlink" onclick="javascript:insert('r_rps', <%=i+1%>);"><%=i+1%>.
					<%= rps[i] %></a><br>
					<%
                                }
                            }
                        %> &nbsp;<input type="text" name="r_rpsOther"
						value="<%= props.getProperty("r_rpsOther", "") %>" /></td>
					<td class="mhList" valign="top">Psychosocial Issues:<br>
					<br>
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 1. <input type="text" name="r_rpi1"
						value="<%= props.getProperty("r_rpi1", "") %>" size="2" /> &nbsp;
					2. <input type="text" name="r_rpi2"
						value="<%= props.getProperty("r_rpi2", "") %>" size="2" /> &nbsp;
					3. <input type="text" name="r_rpi3"
						value="<%= props.getProperty("r_rpi3", "") %>" size="2" /> <br>
					<br>
					<% String[] rpi = list.loadData("mhReferral/PsychosocialIssues.txt", projecthome, path );
                            for (int i=0; i<rpi.length; i++)
                            {
                                if(rpi[i]!=null)
                                {
                        %> <a
						href="javascript: function myFunction() {return false; }"
						class="mhlink" onclick="javascript:insert('r_rpi', <%=i+1%>);"><%=i+1%>.
					<%= rpi[i] %></a><br>
					<%
                                }
                            }
                        %> &nbsp;<input type="text" name="r_rpiOther"
						value="<%= props.getProperty("r_rpiOther", "") %>" /></td>
					<td class="mhList" valign="top">Med/Phy Issues:<br>
					<br>
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 1. <input type="text" name="r_rmpi1"
						value="<%= props.getProperty("r_rmpi1", "") %>" size="2" />
					&nbsp; 2. <input type="text" name="r_rmpi2"
						value="<%= props.getProperty("r_rmpi2", "") %>" size="2" /> &nbsp;
					3. <input type="text" name="r_rmpi3"
						value="<%= props.getProperty("r_rmpi3", "") %>" size="2" /> <br>
					<br>
					<% String[] rmpi = list.loadData("mhReferral/MedPhyIssues.txt", projecthome, path );
                            for (int i=0; i<rmpi.length; i++)
                            {
                                if(rmpi[i]!=null)
                                {
                        %> <a
						href="javascript: function myFunction() {return false; }"
						class="mhlink" onclick="javascript:insert('r_rmpi', <%=i+1%>);"><%=i+1%>.
					<%= rmpi[i] %></a><br>
					<%
                                }
                            }
                        %> &nbsp;<input type="text" name="r_rmpiOther"
						value="<%= props.getProperty("r_rmpiOther", "") %>" /></td>
				</tr>
				<tr>
					<td class="mhSelect"><br>
					</td>
					<td class="mhSelect"><br>
					</td>
					<td class="mhSelect"><br>
					</td>
				</tr>
				<tr>
					<td class="mhList" valign="top">Interventions Requested:<br>
					<br>
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 1. <input type="text" name="r_ir1"
						value="<%= props.getProperty("r_ir1", "") %>" size="2" /> &nbsp;
					2. <input type="text" name="r_ir2"
						value="<%= props.getProperty("r_ir2", "") %>" size="2" /> &nbsp;
					3. <input type="text" name="r_ir3"
						value="<%= props.getProperty("r_ir3", "") %>" size="2" /> <br>
					<br>
					<% String[] ir = list.loadData("mhReferral/InterventionsRequested.txt", projecthome, path );
                            for (int i=0; i<ir.length; i++)
                            {
                                if(ir[i]!=null)
                                {
                        %> <a
						href="javascript: function myFunction() {return false; }"
						class="mhlink" onclick="javascript:insert('r_ir', <%=i+1%>);"><%=i+1%>.
					<%= ir[i] %></a><br>
					<%
                                }
                            }
                        %> &nbsp;<input type="text" name="r_irOther"
						value="<%= props.getProperty("r_irOther", "") %>" /></td>
					<td class="mhList" valign="top">Advice Regarding Management:<br>
					<br>
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 1. <input type="text" name="r_arm1"
						value="<%= props.getProperty("r_arm1", "") %>" size="2" /> &nbsp;
					2. <input type="text" name="r_arm2"
						value="<%= props.getProperty("r_arm2", "") %>" size="2" /> &nbsp;
					3. <input type="text" name="r_arm3"
						value="<%= props.getProperty("r_arm3", "") %>" size="2" /> <br>
					<br>
					<% String[] arm = list.loadData("mhReferral/AdviceRegardingManagement.txt", projecthome, path );
                            for (int i=0; i<arm.length; i++)
                            {
                                if(arm[i]!=null)
                                {
                        %> <a
						href="javascript: function myFunction() {return false; }"
						class="mhlink" onclick="javascript:insert('r_arm', <%=i+1%>);"><%=i+1%>.
					<%= arm[i] %></a><br>
					<%
                                }
                            }
                        %> &nbsp;<input type="text" name="r_armOther"
						value="<%= props.getProperty("r_armOther", "") %>" /></td>
					<td class="mhList" rowspan="2" valign="top">Comments:<br>
					<textarea class="mhRefTextarea" name="r_refComments"><%= props.getProperty("r_refComments", "")%></textarea>
					</td>
				</tr>
				<tr>
					<td class="mhSelect"><br>
					</td>
					<td class="mhSelect"><br>
					</td>
				</tr>
			</table>
			</td>
		</tr>
	</table>

	<table class="Head" class="hidePrint">
		<tr>
			<td align="left"><input type="submit" value="Save"
				onclick="javascript:return onSave();" /> <input type="submit"
				value="Save and Exit"
				onclick="javascript:return confirm('Are you sure you wish to save and close this window?');" />
			<input type="submit" value="Exit"
				onclick="javascript:return onExit();" /> <input type="submit"
				value="Print" onclick="javascript:return onPrint();" /></td>
			<td align="right">Referral &nbsp;|&nbsp; <a
				href="formmhassessment.jsp?demographic_no=<%=demoNo%>&formId=<%=formId%>&provNo=<%=provNo%>">Assessment</a>
			&nbsp;|&nbsp; <a
				href="formmhoutcome.jsp?demographic_no=<%=demoNo%>&formId=<%=formId%>&provNo=<%=provNo%>">Outcome</a>
			</td>
		</tr>
	</table>
</html:form>
</body>
</html:html>
