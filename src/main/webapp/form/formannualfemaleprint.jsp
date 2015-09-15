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

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ page import="oscar.form.*"%>
<%@page import="org.oscarehr.util.LoggedInInfo"%>

<html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title><bean:message
	key="oscarEncounter.formFemaleAnnualPrint.title" /></title>

<link rel="stylesheet" type="text/css" href="annualStyle.css">
<link rel="stylesheet" type="text/css" media="print" href="print.css">
<html:base />
</head>

<%
    String formClass = "Annual";

    int demoNo = Integer.parseInt(request.getParameter("demographic_no"));
    int formId = Integer.parseInt(request.getParameter("formId"));
    int provNo = Integer.parseInt((String) session.getAttribute("user"));
    FrmRecord rec = (new FrmRecordFactory()).factory(formClass);

    java.util.Properties props = rec.getFormRecord(LoggedInInfo.getLoggedInInfoFromSession(request),demoNo, formId);
%>

<script type="text/javascript" language="Javascript">
    function onPrint() {
        window.print();
        return true;
    }
    function onClose() {
        //window.location="formAnnual.jsp?demographic_no=<%=demoNo%>&formId=<%=formId%>&provNo=<%=provNo%>";
        //return true;
        window.close();
    }
</script>

<BODY class="printAnnual" topmargin="0" leftmargin="0" rightmargin="0">

<input type="hidden" name="demographic_no"
	value="<%= props.getProperty("demographic_no", "0") %>" />
<input type="hidden" name="ID"
	value="<%= props.getProperty("ID", "0") %>" />
<input type="hidden" name="provider_no"
	value=<%=request.getParameter("provNo")%> />
<input type="hidden" name="formCreated"
	value="<%= props.getProperty("formCreated", "") %>" />
<input type="hidden" name="formEdited"
	value="<%= props.getProperty("formEdited", "") %>" />
<input type="hidden" name="provNo"
	value="<%= request.getParameter("provNo") %>" />

<table class="Header">
	<tr>
		<td align="left"><input type="button" value="Print"
			onclick="javascript:return onPrint();" /> <input type="button"
			value="Close" onclick="javascript:return onClose();" /></td>
	</tr>
</table>

<table cellspacing="3" cellpadding="0" width="100%">
	<tr>
		<td><big><i><b><bean:message
			key="oscarEncounter.formFemaleAnnualPrint.msgAnnualFemaleHealthReview" /></b></i></big>
		</td>
		<td><b><bean:message
			key="oscarEncounter.formFemaleAnnualPrint.msgName" />:</b> <%= props.getProperty("pName", "") %>
		</td>
		<td><b><bean:message
			key="oscarEncounter.formFemaleAnnualPrint.msgAge" />:</b> <%= props.getProperty("age", "") %>
		</td>
		<td><b><bean:message
			key="oscarEncounter.formFemaleAnnualPrint.msgDate" /></b><small>(yyyy/mm/dd)</small>:
		<%=props.getProperty("formDate", "") %></td>
	</tr>
</table>
<table width="100%">
	<tr>
		<td rowspan="4" width="55%">
		<table class="DashedBorder" width="100%">
			<tr>
				<td><b><bean:message
					key="oscarEncounter.formFemaleAnnualPrint.msgCurrentConcerns" />:</b></td>
			</tr>
			<tr>
				<td valign="top" width="30%" style="height: 480px;"><%= props.getProperty("currentConcerns", "") %></td>
			</tr>
			<tr>
				<td align="center"><bean:message
					key="oscarEncounter.formFemaleAnnualPrint.msgSeeChart" /><br>
				<%= checkMarks(props.getProperty("currentConcernsNo", "")) %> &nbsp;<bean:message
					key="oscarEncounter.formFemaleAnnualPrint.msgNo" />&nbsp;&nbsp;&nbsp;
				<%= checkMarks(props.getProperty("currentConcernsYes", "")) %>
				&nbsp;<bean:message
					key="oscarEncounter.formFemaleAnnualPrint.msgyes" /></td>
			</tr>
		</table>
		</td>
		<td>
		<table>
			<tr>
				<td colspan="3"><b><bean:message
					key="oscarEncounter.formFemaleAnnualPrint.msgSystemReview" />:</b></td>
				<td>&nbsp;</td>
			</tr>
			<tr>
				<td><b><bean:message
					key="oscarEncounter.formFemaleAnnualPrint.msgN" /></b></td>
				<td colspan="2"><b><bean:message
					key="oscarEncounter.formFemaleAnnualPrint.msgAbN" /></b></td>
			</tr>
			<tr>
				<td><%= checkMarks(props.getProperty("headN", "")) %></td>
				<td><%= checkMarks(props.getProperty("headAbN", "")) %></td>
				<td align="left" nowrap="true"><bean:message
					key="oscarEncounter.formFemaleAnnualPrint.msgHeadNeck" />:</td>
				<td align="left"><%= props.getProperty("head", "") %></td>
			</tr>
			<tr>
				<td><%= checkMarks(props.getProperty("respN", "")) %></td>
				<td><%= checkMarks(props.getProperty("respAbN", "")) %></td>
				<td><bean:message
					key="oscarEncounter.formFemaleAnnualPrint.msgResp" />:</td>
				<td align="left"><%= props.getProperty("resp", "") %></td>
			</tr>
			<tr>
				<td><%= checkMarks(props.getProperty("cardioN", "")) %></td>
				<td><%= checkMarks(props.getProperty("cardioAbN", "")) %></td>
				<td><bean:message
					key="oscarEncounter.formFemaleAnnualPrint.msgCardio" />:</td>
				<td align="left"><%= props.getProperty("cardio", "") %></td>
			</tr>
			<tr>
				<td><%= checkMarks(props.getProperty("giN", "")) %></td>
				<td><%= checkMarks(props.getProperty("giAbN", "")) %></td>
				<td><bean:message
					key="oscarEncounter.formFemaleAnnualPrint.msgGI" />:</td>
				<td align="left"><%= props.getProperty("gi", "") %></td>
			</tr>
			<tr>
				<td><%= checkMarks(props.getProperty("guN", "")) %></td>
				<td><%= checkMarks(props.getProperty("guAbN", "")) %></td>
				<td><bean:message
					key="oscarEncounter.formFemaleAnnualPrint.msgGU" />:</td>
				<td align="left"><%= props.getProperty("gu", "") %></td>
			</tr>
			<tr>
				<td><%= checkMarks(props.getProperty("skinN", "")) %></td>
				<td><%= checkMarks(props.getProperty("skinAbN", "")) %></td>
				<td><bean:message
					key="oscarEncounter.formFemaleAnnualPrint.msgSkin" />:</td>
				<td align="left"><%= props.getProperty("skin", "") %></td>
			</tr>
			<tr>
				<td><%= checkMarks(props.getProperty("mskN", "")) %></td>
				<td><%= checkMarks(props.getProperty("mskAbN", "")) %></td>
				<td><bean:message
					key="oscarEncounter.formFemaleAnnualPrint.MSK" />:</td>
				<td align="left"><%= props.getProperty("msk", "") %></td>
			</tr>
			<tr>
				<td><%= checkMarks(props.getProperty("endocrinN", "")) %></td>
				<td><%= checkMarks(props.getProperty("endocrinAbN", "")) %></td>
				<td><bean:message
					key="oscarEncounter.formFemaleAnnualPrint.msgEndocrin" />:</td>
				<td align="left"><%= props.getProperty("endocrin", "") %></td>
			</tr>
			<tr>
				<td valign="top"><%= checkMarks(props.getProperty("otherN", "")) %></td>
				<td valign="top"><%= checkMarks(props.getProperty("otherAbN", "")) %></td>
				<td valign="top"><bean:message
					key="oscarEncounter.formFemaleAnnualPrint.msgOther" />:</td>
				<td align="left"><%= props.getProperty("other", "") %></td>
			</tr>

		</table>
		</td>
	</tr>
	<tr>
		<td>
		<table width="100%">
			<tr>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
				<td nowrap="true"><bean:message
					key="oscarEncounter.formFemaleAnnualPrint.msgGTPAL" />&nbsp; <%= checkMarks(props.getProperty("noGtpalRevisions", "")) %>
				<bean:message key="oscarEncounter.formFemaleAnnualPrint.msgNo" /> <%= checkMarks(props.getProperty("yesGtpalRevisions", "")) %>
				<bean:message key="oscarEncounter.formFemaleAnnualPrint.msgyes" /><br>
				<%= checkMarks(props.getProperty("frontSheet", "")) %> <bean:message
					key="oscarEncounter.formFemaleAnnualPrint.msgFrontSheet" /></td>
			</tr>
			<tr>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
				<td><bean:message
					key="oscarEncounter.formFemaleAnnualPrint.msgLMP" /><small>(yyyy/mm/dd)</small>:
				<%= props.getProperty("lmp", "") %><br>
				<bean:message
					key="oscarEncounter.formFemaleAnnualPrint.msgMenopause" />: <%= props.getProperty("menopause", "") %>
				/<bean:message
					key="oscarEncounter.formFemaleAnnualPrint.msgMenopauseUnit" /></td>
			</tr>
			<tr>
				<td><%= checkMarks(props.getProperty("papSmearsN", "")) %></td>
				<td><%= checkMarks(props.getProperty("papSmearsAbN", "")) %></td>
				<td nowrap="true"><bean:message
					key="oscarEncounter.formFemaleAnnualPrint.msgPreviousPaoSmears" />:
				<%= props.getProperty("papSmears", "") %></td>
			</tr>
		</table>
		</td>
	</tr>
	<tr>
		<td>
		<table>
			<tr>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
				<td colspan="4"><bean:message
					key="oscarEncounter.formFemaleAnnualPrint.msgReview" />:</td>
			</tr>
			<tr>
				<td>&nbsp;</td>
				<td><%= checkMarks(props.getProperty("drugs", "")) %></td>
				<td><bean:message
					key="oscarEncounter.formFemaleAnnualPrint.msgDrugs" /></td>
				<td>&nbsp;</td>
				<td align="right"><%= checkMarks(props.getProperty("medSheet", "")) %></td>
				<td><bean:message
					key="oscarEncounter.formFemaleAnnualPrint.msgMedSheet" /></td>
			</tr>
			<tr>
				<td>&nbsp;</td>
				<td><%= checkMarks(props.getProperty("allergies", "")) %></td>
				<td colspan="2" nowrap="true"><bean:message
					key="oscarEncounter.formFemaleAnnualPrint.msgAllergies" /></td>
				<td align="right"><%= checkMarks(props.getProperty("frontSheet1", "")) %></td>
				<td><bean:message
					key="oscarEncounter.formFemaleAnnualPrint.msgFrontSheet" /></td>
			</tr>
			<tr>
				<td>&nbsp;</td>
				<td><%= checkMarks(props.getProperty("familyHistory", "")) %></td>
				<td colspan="2"><bean:message
					key="oscarEncounter.formFemaleAnnualPrint.msgFamilyHist" /></td>
				<td align="right"><%= checkMarks(props.getProperty("frontSheet2", "")) %></td>
				<td><bean:message
					key="oscarEncounter.formFemaleAnnualPrint.msgFrontSheet" /></td>
			</tr>
		</table>
		</td>
	</tr>
</table>
<table width="100%">
	<tr>
		<td class="DashedBorder" width="50%">
		<table>
			<tr>
				<td colspan="3" nowrap="true"><b><bean:message
					key="oscarEncounter.formFemaleAnnualPrint.msgLifestyle" />:</b></td>
				<td><b><i><small>("Any concerns with ...?")</small></i></b></td>
			</tr>
			<tr>
				<td><bean:message
					key="oscarEncounter.formFemaleAnnualPrint.msgNo" /></td>
				<td colspan="2"><bean:message
					key="oscarEncounter.formFemaleAnnualPrint.msgyes" /></td>
				<td>&nbsp;</td>
			</tr>
			<tr>
				<td><%= checkMarks(props.getProperty("smokingNo", "")) %></td>
				<td><%= checkMarks(props.getProperty("smokingYes", "")) %></td>
				<td><bean:message
					key="oscarEncounter.formFemaleAnnualPrint.msgSmoking" />:</td>
				<td align="right"><%= props.getProperty("smoking", "") %></td>
			</tr>
			<tr>
				<td><%= checkMarks(props.getProperty("alcoholNo", "")) %></td>
				<td><%= checkMarks(props.getProperty("alcoholYes", "")) %></td>
				<td><bean:message
					key="oscarEncounter.formFemaleAnnualPrint.msgAlcohol" />:</td>
				<td align="right"><%= props.getProperty("alcohol", "") %></td>
			</tr>
			<tr>
				<td><%= checkMarks(props.getProperty("otcNo", "")) %></td>
				<td><%= checkMarks(props.getProperty("otcYes", "")) %></td>
				<td><bean:message
					key="oscarEncounter.formFemaleAnnualPrint.msgIllicitDrugs" />:</td>
				<td align="right"><%= props.getProperty("otc", "") %></td>
			</tr>
			<tr>
				<td><%= checkMarks(props.getProperty("exerciseNo", "")) %></td>
				<td><%= checkMarks(props.getProperty("exerciseYes", "")) %></td>
				<td><bean:message
					key="oscarEncounter.formFemaleAnnualPrint.msgExercise" /></td>
				<td align="right"><%= props.getProperty("exercise", "") %></td>
			</tr>
			<tr>
				<td><%= checkMarks(props.getProperty("nutritionNo", "")) %></td>
				<td><%= checkMarks(props.getProperty("nutritionYes", "")) %></td>
				<td><bean:message
					key="oscarEncounter.formFemaleAnnualPrint.msgNutrition" />:</td>
				<td align="right"><%= props.getProperty("nutrition", "") %></td>
			</tr>
			<tr>
				<td><%= checkMarks(props.getProperty("dentalNo", "")) %></td>
				<td><%= checkMarks(props.getProperty("dentalYes", "")) %></td>
				<td><bean:message
					key="oscarEncounter.formFemaleAnnualPrint.msgDentalHygiene" />:</td>
				<td align="right"><%= props.getProperty("dental", "") %></td>
			</tr>
			<tr>
				<td valign="top"><%= checkMarks(props.getProperty("relationshipNo", "")) %></td>
				<td valign="top"><%= checkMarks(props.getProperty("relationshipYes", "")) %></td>
				<td valign="top"><bean:message
					key="oscarEncounter.formFemaleAnnualPrint.msgRelationshipIssues" />:</td>
				<td align="right"><%= props.getProperty("relationship", "") %></td>
			</tr>
			<tr>
				<td><%= checkMarks(props.getProperty("sexualityNo", "")) %></td>
				<td><%= checkMarks(props.getProperty("sexualityYes", "")) %></td>
				<td nowrap="true"><bean:message
					key="oscarEncounter.formFemaleAnnualPrint.msgSexualityRisks" />:</td>
				<td align="right"><%= props.getProperty("sexuality", "") %></td>
			</tr>
			<tr>
				<td><%= checkMarks(props.getProperty("occupationalNo", "")) %></td>
				<td><%= checkMarks(props.getProperty("occupationalYes", "")) %></td>
				<td nowrap="true"><bean:message
					key="oscarEncounter.formFemaleAnnualPrint.msgOccupationalRisks" />:</td>
				<td align="right"><%= props.getProperty("occupational", "") %></td>
			</tr>
			<tr>
				<td><%= checkMarks(props.getProperty("drivingNo", "")) %></td>
				<td><%= checkMarks(props.getProperty("drivingYes", "")) %></td>
				<td nowrap="true"><bean:message
					key="oscarEncounter.formFemaleAnnualPrint.msgDrivingSafety" />:</td>
				<td align="right"><%= props.getProperty("driving", "") %></td>
			</tr>
			<tr>
				<td><%= checkMarks(props.getProperty("travelNo", "")) %></td>
				<td><%= checkMarks(props.getProperty("travelYes", "")) %></td>
				<td nowrap="true"><bean:message
					key="oscarEncounter.formFemaleAnnualPrint.msgForeignTravel" />:</td>
				<td align="right"><%= props.getProperty("travel", "") %></td>
			</tr>
			<tr>
				<td valign="top"><%= checkMarks(props.getProperty("otherNo", "")) %></td>
				<td valign="top"><%= checkMarks(props.getProperty("otherYes", "")) %></td>
				<td nowrap="true" valign="top"><bean:message
					key="oscarEncounter.formFemaleAnnualPrint.msgOther" />:</td>
				<td rowspan="3" align="right"><%= props.getProperty("otherLifestyle", "") %></td>
			</tr>
		</table>
		</td>
		<td width="100%" valign="top" class="DashedBorder">
		<table width="100%">
			<tr>
				<td width="50%" colspan="2"><b><bean:message
					key="oscarEncounter.formFemaleAnnualPrint.msgScreeningReview" />:</b></td>
			</tr>
			<tr>
				<td>&nbsp;</td>
			</tr>
			<tr>
				<td><%= checkMarks(props.getProperty("mammogram", "")) %></td>
				<td><bean:message
					key="oscarEncounter.formFemaleAnnualPrint.msgMammogram" /></td>
			</tr>
			<tr>
				<td><%= checkMarks(props.getProperty("breast", "")) %></td>
				<td><bean:message
					key="oscarEncounter.formFemaleAnnualPrint.msgBreastSelfTest" /></td>
			</tr>
			<tr>
				<td><%= checkMarks(props.getProperty("pap", "")) %></td>
				<td><bean:message
					key="oscarEncounter.formFemaleAnnualPrint.msgPapSmear" /></td>
			</tr>
			<tr>
				<td><%= checkMarks(props.getProperty("femaleImmunization", "")) %></td>
				<td><bean:message
					key="oscarEncounter.formFemaleAnnualPrint.msgImmunization" /></td>
			</tr>
			<tr>
				<td><%= checkMarks(props.getProperty("precontraceptive", "")) %></td>
				<td nowrap="true"><bean:message
					key="oscarEncounter.formFemaleAnnualPrint.msgprecontraceptive" /></td>
			</tr>
			<tr>
				<td><%= checkMarks(props.getProperty("femaleCardiac", "")) %></td>
				<td><bean:message
					key="oscarEncounter.formFemaleAnnualPrint.msgCardiacRisk" /></td>
			</tr>
			<tr>
				<td><%= checkMarks(props.getProperty("osteoporosis", "")) %></td>
				<td><bean:message
					key="oscarEncounter.formFemaleAnnualPrint.msgOsteoporosis" /></td>
			</tr>
			<tr>
				<td><%= checkMarks(props.getProperty("femaleOther1c", "")) %></td>
				<td><%= props.getProperty("femaleOther1", "") %></td>
			</tr>
			<tr>
				<td><%= checkMarks(props.getProperty("femaleOther2c", "")) %></td>
				<td><%= props.getProperty("femaleOther2", "") %></td>
			</tr>
		</table>
		</td>
	</tr>
</table>
<table width="100%" class="tableWithBorder">
	<tr>
		<td colspan="9"><b><bean:message
			key="oscarEncounter.formFemaleAnnualPrint.msgPhysicalExam" />:</b></td>
	</tr>
	<tr>
		<td><b><bean:message
			key="oscarEncounter.formFemaleAnnualPrint.msgVitals" />: </b></td>
		<td><bean:message
			key="oscarEncounter.formFemaleAnnualPrint.msgBP" />: <%= props.getProperty("bprTop", "") %>/
		<%= props.getProperty("bprBottom", "") %> <bean:message
			key="oscarEncounter.formFemaleAnnualPrint.msgR" /></td>
		<td align="right"><bean:message
			key="oscarEncounter.formFemaleAnnualPrint.msgPulse" />:</td>
		<td><%= props.getProperty("pulse", "") %> <bean:message
			key="oscarEncounter.formFemaleAnnualPrint.msgPulseUnit" /></td>
		<td align="right"><bean:message
			key="oscarEncounter.formFemaleAnnualPrint.msgHeight" />:</td>
		<td><%= props.getProperty("height", "") %> <bean:message
			key="oscarEncounter.formFemaleAnnualPrint.msgHeightUnit" /></td>
		<td align="right"><bean:message
			key="oscarEncounter.formFemaleAnnualPrint.msgWeight" />: <%= props.getProperty("weight", "") %>
		<bean:message key="oscarEncounter.formFemaleAnnualPrint.msgWeightUnit" /></td>
	<tr>
		<td>&nbsp;</td>
		<td><bean:message
			key="oscarEncounter.formFemaleAnnualPrint.msgBP" />: <%= props.getProperty("bplTop", "") %>/
		<%= props.getProperty("bplBottom", "") %> <bean:message
			key="oscarEncounter.formFemaleAnnualPrint.msgL" /></td>
		<td align="right"><bean:message
			key="oscarEncounter.formFemaleAnnualPrint.msgRhythm" />:</td>
		<td><%= props.getProperty("rhythm", "") %></td>
		<td align="right"><bean:message
			key="oscarEncounter.formFemaleAnnualPrint.msgUrineDipstick" />:</td>
		<td><%= props.getProperty("urine", "") %></td>
	</tr>
</table>
<table style="page-break-before: always;" width="100%">
	<tr>
		<td rowspan="3">
		<table width="100%" class="DashedBorder">
			<tr>
				<td><b> <bean:message
					key="oscarEncounter.formFemaleAnnualPrint.msgPhysicalSigns" />: </b></td>
			</tr>
			<tr>
				<td class="physicalSigns"><%= props.getProperty("physicalSigns", "") %></td>
			</tr>
		</table>
		</td>
	</tr>
</table>
<table width="100%" class="TableWithBorder">
	<tr>
		<td>
		<table width="100%">
			<tr>
				<td><b><bean:message
					key="oscarEncounter.formFemaleAnnualPrint.msgAssessment" /></b></td>
			</tr>
			<tr>
				<td align="center" class="assessmentPlan"><%= props.getProperty("assessment", "") %></td>
			</tr>
		</table>
		</td>
		<td>
		<table width="100%">
			<tr>
				<td align="center"><b><bean:message
					key="oscarEncounter.formFemaleAnnualPrint.msgPlan" /></b></td>
			</tr>
			<tr>
				<td align="center" class="assessmentPlan"><%= props.getProperty("plan", "") %></td>
			</tr>
		</table>
		</td>
	</tr>
	<tr>
		<td colspan="2" align="right"><bean:message
			key="oscarEncounter.formFemaleAnnualPrint.msgSignature" />: <%= props.getProperty("signature", "") %>
		</td>
	</tr>
</table>


<table class="Header">
	<tr>
		<td align="left"><input type="button" value="Print"
			onclick="javascript:return onPrint();" /> <input type="button"
			value="Close" onclick="javascript:return onClose();" /></td>
	</tr>
</table>


</body>
</html:html>

<%! String checkMarks(String val)
    {
        String ret="<img src='graphics/notChecked.gif'>";
        if(val.equalsIgnoreCase("checked='checked'"))
        {
            ret="<img src='graphics/checkmark.gif'>";
        }
        return ret;
    }
%>
