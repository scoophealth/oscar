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
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="oscar.form.*"%>
<%@page import="org.oscarehr.util.LoggedInInfo"%>
<html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title><bean:message key="oscarEncounter.formAlpha.title" /></title>
<link rel="stylesheet" type="text/css" href="alphaStyle.css">
<link rel="stylesheet" type="text/css" media="print" href="print.css">
<html:base />

<%
    String formClass = "Alpha";
    String formLink = "formalpha.jsp";

    int demoNo = Integer.parseInt(request.getParameter("demographic_no"));
    int formId = Integer.parseInt(request.getParameter("formId"));
    int provNo = Integer.parseInt((String) session.getAttribute("user"));
    FrmRecord rec = (new FrmRecordFactory()).factory(formClass);
    java.util.Properties props = rec.getFormRecord(LoggedInInfo.getLoggedInInfoFromSession(request),demoNo, formId);
%>

<script type="text/javascript" language="Javascript">
        function onPrint() {
            window.print();
        }
        function onSave() {
            document.forms[0].submit.value="save";
            var ret = confirm("<bean:message key="oscarEncounter.formAlpha.msgWannaSave"/>");            
            return ret;
        }
        
        function onSaveExit() {
            document.forms[0].submit.value="exit";
            var ret = confirm("<bean:message key="oscarEncounter.formAlpha.msgWannaSaveClose"/>");            
            return ret;
        }
    </script>
</head>


<body topmargin="0" leftmargin="0" rightmargin="0">
<html:form action="/form/formname">

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
	<input type="hidden" name="pName"
		value="<%= props.getProperty("pName", "") %>" />
	<input type="hidden" name="provNo"
		value="<%= request.getParameter("provNo") %>" />
	<input type="hidden" name="submit" value="exit" />

	<table class="Head" class="hidePrint">
		<tr>
			<td align="left"><input type="submit"
				value="<bean:message key="oscarEncounter.formAlpha.btnSave"/>"
				onclick="javascript:return onSave();" /> <input type="submit"
				value="<bean:message key="oscarEncounter.formAlpha.btnSaveExit"/>"
				onclick="javascript:return onSaveExit();" /> <input type="submit"
				value="<bean:message key="oscarEncounter.formAlpha.btnExit"/>"
				onclick="javascript:return onExit();" /> <input type="button"
				value="<bean:message key="oscarEncounter.formAlpha.btnPrint"/>"
				onclick="javascript:return onPrint();" /></td>
		</tr>
	</table>

	<table border="0" cellspacing="0" cellpadding="0" width="100%">
		<tr bgcolor="#486ebd">
			<th align='CENTER'><font size="-1"
				face="Arial, Helvetica, sans-serif" color="#FFFFFF"><bean:message
				key="oscarEncounter.formAlpha.msgAlpha" /></font></th>
		</tr>
	</table>
	<table width="100%" border="0" bgcolor="ivory">
		<tr bgcolor="#99FF99">
			<td><b><bean:message
				key="oscarEncounter.formAlpha.msgFamilyFactors" /></b></td>
			<td align="right"></td>
		</tr>
		<tr>
			<td width="50%"><b><bean:message
				key="oscarEncounter.formAlpha.formSocialSupport" /></b>
			<li><font size="-1"><bean:message
				key="oscarEncounter.formAlpha.msgFamilyFeel" /></font></li>
			<li><font size="-1"><bean:message
				key="oscarEncounter.formAlpha.msgFormWhoHelp" /></font></li>
			</td>
			<td><textarea name="socialSupport" style="width: 100%" cols="40"
				rows="3"><%= props.getProperty("socialSupport", "") %></textarea></td>
		</tr>
		<tr>
			<td><b><bean:message
				key="oscarEncounter.formAlpha.formRecentStressfulEvents" /></b>
			<li><font size="-1"><bean:message
				key="oscarEncounter.formAlpha.msgLifeChanges" /></font></li>
			<li><font size="-1"><bean:message
				key="oscarEncounter.formAlpha.msgPlanningChanges" /></font></li>
			</td>
			<td><textarea name="lifeEvents" style="width: 100%" cols="40"
				rows="3"><%= props.getProperty("lifeEvents", "") %></textarea></td>
		</tr>
		<tr>
			<td><b><bean:message
				key="oscarEncounter.formAlpha.formCoupleRelationship" /></b>
			<li><font size="-1"><bean:message
				key="oscarEncounter.formAlpha.msgRelationship" /></font></li>
			<li><font size="-1"><bean:message
				key="oscarEncounter.formAlpha.msgRelationshipAfterBirth" /></font><br>
			</li>
			</td>
			<td><textarea name="coupleRelationship" style="width: 100%"
				cols="40" rows="3"><%= props.getProperty("coupleRelationship", "") %></textarea>
			</td>
		</tr>
		<tr bgcolor="#99FF99">
			<td colspan="2"><b><bean:message
				key="oscarEncounter.formAlpha.msgMaternalFactors" /></b></td>
		</tr>
		<tr>
			<td valign="top"><b><bean:message
				key="oscarEncounter.formAlpha.formPrenatal" /></b>
			<li><font size="-1"><bean:message
				key="oscarEncounter.formAlpha.msgPrenatalVisit" /></font></li>
			</td>
			<td><textarea name="prenatalCare" style="width: 100%" cols="40"
				rows="2"><%= props.getProperty("prenatalCare", "") %></textarea></td>
		</tr>
		<tr>
			<td valign="top"><b><bean:message
				key="oscarEncounter.formAlpha.formPrenatalEducation" /></b>
			<li><font size="-1"><bean:message
				key="oscarEncounter.formAlpha.msgPrenatalPlans" /> </font></li>
			</td>
			<td><textarea name="prenatalEducation" style="width: 100%"
				cols="40" rows="2"><%= props.getProperty("prenatalEducation", "") %></textarea>
			</td>
		</tr>
		<tr>
			<td valign="top"><b><bean:message
				key="oscarEncounter.formAlpha.formFeelingsTowardpregnancy" /> </b>
			<li><font size="-1"><bean:message
				key="oscarEncounter.formAlpha.msgFellPregnant" /></font></li>
			<li><font size="-1"><bean:message
				key="oscarEncounter.formAlpha.msgFellAboutpregnant" /></font></li>
			</td>
			<td><textarea name="feelingsRePregnancy" style="width: 100%"
				cols="40" rows="2"><%= props.getProperty("feelingsRePregnancy", "") %></textarea>
			</td>
		</tr>
		<tr>
			<td valign="top"><b><bean:message
				key="oscarEncounter.formAlpha.formRelationshipWithParents" /></b>
			<li><font size="-1"><bean:message
				key="oscarEncounter.formAlpha.msgRelationshipWithParents" /></font></li>
			<li><font size="-1"><bean:message
				key="oscarEncounter.formAlpha.msgLovedByParents" /></font></li>
			</td>
			<td><textarea name="relationshipParents" style="width: 100%"
				cols="40" rows="2"><%= props.getProperty("relationshipParents", "") %></textarea>
			</td>
		</tr>
		<tr>
			<td valign="top"><b><bean:message
				key="oscarEncounter.formAlpha.formSelfEsteem" /></b>
			<li><font size="-1"><bean:message
				key="oscarEncounter.formAlpha.msgSelfEsteemConcerns" /></font></li>
			</td>
			<td><textarea name="selfEsteem" style="width: 100%" cols="40"
				rows="2"><%= props.getProperty("selfEsteem", "") %></textarea></td>
		</tr>
		<tr>
			<td valign="top"><b><bean:message
				key="oscarEncounter.formAlpha.formEmotionalProblems" /></b>
			<li><font size="-1"><bean:message
				key="oscarEncounter.formAlpha.msgEmotioanlProblems" /></font></li>
			<li><font size="-1"><bean:message
				key="oscarEncounter.formAlpha.msgPsychiatrist" /></font></li>
			</td>
			<td><textarea name="psychHistory" style="width: 100%" cols="40"
				rows="2"><%= props.getProperty("psychHistory", "") %></textarea></td>
		</tr>
		<tr>
			<td valign="top"><b><bean:message
				key="oscarEncounter.formAlpha.formDepression" /></b>
			<li><font size="-1"><bean:message
				key="oscarEncounter.formAlpha.msgMood" /> </font></li>
			</td>
			<td><textarea name="depression" style="width: 100%" cols="40"
				rows="2"><%= props.getProperty("depression", "") %></textarea></td>
		</tr>
		<tr bgcolor="#99FF99">
			<td valign="top" colspan="2"><b><bean:message
				key="oscarEncounter.formAlpha.msgSubstanceUse" /></b></td>
		</tr>
		<tr>
			<td valign="top"><b><bean:message
				key="oscarEncounter.formAlpha.formAlcohol" /></b>
			<li><font size="-1"><bean:message
				key="oscarEncounter.formAlpha.msgDrinksPerWeek" /></font></li>
			<li><font size="-1"><bean:message
				key="oscarEncounter.formAlpha.msgTimesDrinkMore" /></font></li>
			<li><font size="-1"><bean:message
				key="oscarEncounter.formAlpha.msgDrugsUse" /></font></li>
			<li><font size="-1"><bean:message
				key="oscarEncounter.formAlpha.msgPartnerAlcoholAndDrugs" /></font></li>
			<li><font size="-1"><bean:message
				key="oscarEncounter.formAlpha.msgCAGE" /></font></li>
			</td>
			<td><textarea name="alcoholDrugAbuse" style="width: 100%"
				cols="40" rows="5"><%= props.getProperty("alcoholDrugAbuse", "") %></textarea>
			</td>
		</tr>
		<tr bgcolor="#99FF99">
			<td valign="top" colspan="2"><b><bean:message
				key="oscarEncounter.formAlpha.msgFamilyViolence" /></b></td>
		</tr>
		<tr>
			<td valign="top"><b><bean:message
				key="oscarEncounter.formAlpha.msgAbuse" /></b>
			<li><font size="-1"><bean:message
				key="oscarEncounter.formAlpha.msgRelationship" /></font></li>
			<li><font size="-1"><bean:message
				key="oscarEncounter.formAlpha.msgFatherViolence" /></font></li>
			<li><font size="-1"><bean:message
				key="oscarEncounter.formAlpha.msgParentsViolence" /></font></li>
			<li><font size="-1"><bean:message
				key="oscarEncounter.formAlpha.msgAbusedAsAChild" /></font></li>
			</td>
			<td><textarea name="abuse" style="width: 100%" cols="40"
				rows="5"><%= props.getProperty("abuse", "") %></textarea></td>
		</tr>
		<tr>
			<td valign="top"><b><bean:message
				key="oscarEncounter.formAlpha.formWomanAbuse" /></b><br>
			<li><font size="-1"><bean:message
				key="oscarEncounter.formAlpha.msgSolveArguments" /> </font></li>
			<li><font size="-1"><bean:message
				key="oscarEncounter.formAlpha.msgFeelFrightened" /> </font></li>
			<li><font size="-1"><bean:message
				key="oscarEncounter.formAlpha.msgBeenHit" /></font></li>
			<li><font size="-1"><bean:message
				key="oscarEncounter.formAlpha.msgHumiliated" /> </font></li>
			<li><font size="-1"><bean:message
				key="oscarEncounter.formAlpha.msgForcedSex" /> </font></li>
			</td>
			<td><textarea name="womanAbuse" style="width: 100%" cols="40"
				rows="5"><%= props.getProperty("womanAbuse", "") %></textarea></td>
		</tr>
		<tr style="page-break-before: always;">
			<td valign="top"><b><bean:message
				key="oscarEncounter.formAlpha.formPreviousChildAbuse" /></b>
			<li><font size="-1"><bean:message
				key="oscarEncounter.formAlpha.msgDistantChild" /> </font></li>
			<li><font size="-1"><bean:message
				key="oscarEncounter.formAlpha.msgChildProtectionAgency" /> </font></li>
			</td>
			<td><textarea name="childAbuse" style="width: 100%" cols="40"
				rows="2"><%= props.getProperty("childAbuse", "") %></textarea></td>
		</tr>
		<tr>
			<td valign="top"><b><bean:message
				key="oscarEncounter.formAlpha.formChildDiscipline" /> </b>
			<li><font size="-1"><bean:message
				key="oscarEncounter.formAlpha.msgDiscilinedMother" /></font></li>
			<li><font size="-1"><bean:message
				key="oscarEncounter.formAlpha.msgHowWillDiscipline" /></font></li>
			<li><font size="-1"><bean:message
				key="oscarEncounter.formAlpha.msgMisbehave" /></font></li>
			</td>
			<td><textarea name="childDiscipline" style="width: 100%"
				cols="40" rows="3"><%= props.getProperty("childDiscipline", "") %></textarea>
			</td>
		</tr>
	</table>
	<table width="100%" border="0" bgcolor="ivory">
		<tr bgcolor="#99ff99">
			<td align="center" colspan="6"><b><bean:message
				key="oscarEncounter.formAlpha.msgFollowUp" /></b></td>
		</tr>
		<tr>
			<td><input type="checkbox" name="provCounselling"
				<%= props.getProperty("provCounselling", "") %>></td>
			<td><bean:message key="oscarEncounter.formAlpha.formCounselling" /></td>
			<td><input type="checkbox" name="homecare"
				<%= props.getProperty("homecare", "") %>></td>
			<td><bean:message key="oscarEncounter.formAlpha.formHomecare" /></td>
			<td><input type="checkbox" name="assaultedWomen"
				<%= props.getProperty("assaultedWomen", "") %>></td>
			<td><bean:message
				key="oscarEncounter.formAlpha.formAssaultedWomen" /></td>
		</tr>
		<tr>
			<td><input type="checkbox" name="addAppts"
				<%= props.getProperty("addAppts", "") %>></td>
			<td><bean:message key="oscarEncounter.formAlpha.form" /></td>
			<td><input type="checkbox" name="parentingClasses"
				<%= props.getProperty("parentingClasses", "") %>></td>
			<td><bean:message
				key="oscarEncounter.formAlpha.formParentingClasses" /></td>
			<td><input type="checkbox" name="legalAdvice"
				<%= props.getProperty("legalAdvice", "") %>></td>
			<td><bean:message key="oscarEncounter.formAlpha.formLegalAdvise" /></td>
		</tr>
		<tr>
			<td><input type="checkbox" name="postpartumAppts"
				<%= props.getProperty("postpartumAppts", "") %>></td>
			<td><bean:message
				key="oscarEncounter.formAlpha.formPospartumAppointments" /></td>
			<td><input type="checkbox" name="addictPrograms"
				<%= props.getProperty("addictPrograms", "") %>></td>
			<td><bean:message
				key="oscarEncounter.formAlpha.formAddictionTreatment" /></td>
			<td><input type="checkbox" name="cas"
				<%= props.getProperty("cas", "") %>></td>
			<td><bean:message key="oscarEncounter.formAlpha.formChildrenAid" /></td>
		</tr>
		<tr>
			<td><input type="checkbox" name="babyVisits"
				<%= props.getProperty("babyVisits", "") %>></td>
			<td><bean:message key="oscarEncounter.formAlpha.formBabyVisits" />
			</td>
			<td><input type="checkbox" name="quitSmoking"
				<%= props.getProperty("quitSmoking", "") %>></td>
			<td><bean:message
				key="oscarEncounter.formAlpha.formSmokingCessation" /></td>
			<td><input type="checkbox" name="other1"
				<%= props.getProperty("other1", "") %>></td>
			<td><bean:message key="oscarEncounter.formAlpha.formOther" />:<input
				type="text" name="other1Name"
				value="<%= props.getProperty("other1Name", "")%>"></td>
		</tr>
		<tr>
			<td><input type="checkbox" name="publicHealth"
				<%= props.getProperty("publicHealth", "") %>></td>
			<td><bean:message
				key="oscarEncounter.formAlpha.formPublicHealth" /></td>
			<td><input type="checkbox" name="socialWorker"
				<%= props.getProperty("socialWorker", "") %>></td>
			<td><bean:message
				key="oscarEncounter.formAlpha.formSocialWorker" /></td>
			<td><input type="checkbox" name="other2"
				<%= props.getProperty("other2", "") %>></td>
			<td><bean:message key="oscarEncounter.formAlpha.formOther" />:<input
				type="text" name="other2Name"
				value="<%= props.getProperty("other2Name", "") %>"></td>
		</tr>
		<tr>
			<td><input type="checkbox" name="prenatalEdu"
				<%= props.getProperty("prenatalEdu", "") %>></td>
			<td><bean:message
				key="oscarEncounter.formAlpha.formPrenatalEducation" /></td>
			<td><input type="checkbox" name="psych"
				<%= props.getProperty("psych", "") %>></td>
			<td><bean:message
				key="oscarEncounter.formAlpha.formPsychologist" /></td>
			<td><input type="checkbox" name="other3"
				<%= props.getProperty("other3", "") %>></td>
			<td>Other:<input type="text" name="other3Name"
				value="<%= props.getProperty("other3Name", "") %>"></td>
		</tr>
		<tr>
			<td><input type="checkbox" name="nutritionist"
				<%= props.getProperty("nutritionist", "") %>></td>
			<td><bean:message
				key="oscarEncounter.formAlpha.formNutrucionist" /></td>
			<td><input type="checkbox" name="therapist"
				<%= props.getProperty("therapist", "") %>></td>
			<td><bean:message
				key="oscarEncounter.formAlpha.formFamilyTherapist" /></td>
			<td><input type="checkbox" name="other4"
				<%= props.getProperty("other4", "") %>></td>
			<td><bean:message key="oscarEncounter.formAlpha.formOther" />:<input
				type="text" name="other4Name"
				value="<%= props.getProperty("other4Name", "") %>"></td>
		</tr>
		<tr>
			<td><input type="checkbox" name="resources"
				<%= props.getProperty("resources", "") %>></td>
			<td><bean:message
				key="oscarEncounter.formAlpha.formMothersGroup" /></td>
			<td>&nbsp;</td>
			<td>&nbsp;</td>
			<td>&nbsp;</td>
			<td>&nbsp;</td>
		</tr>
	</table>
	<table width="100%" border="0" bgcolor="ivory">
		<tr>
			<td><b><bean:message
				key="oscarEncounter.formAlpha.formComments" /></b>:<br>
			<textarea name="comments" style="width: 100%" cols="80"><%= props.getProperty("comments", "") %></textarea>
			</td>

		</tr>
	</table>

	<table class="Head" class="hidePrint">
		<tr>
			<td align="left"><input type="submit"
				value="<bean:message key="oscarEncounter.formAlpha.btnSave"/>"
				onclick="javascript:return onSave();" /> <input type="submit"
				value="<bean:message key="oscarEncounter.formAlpha.btnSaveExit"/>"
				onclick="javascript:return onSaveExit();" /> <input type="submit"
				value="<bean:message key="oscarEncounter.formAlpha.btnExit"/>"
				onclick="javascript:return onExit();" /> <input type="button"
				value="<bean:message key="oscarEncounter.formAlpha.btnPrint"/>"
				onclick="javascript:return onPrint();" /></td>
		</tr>
	</table>

</html:form>
</body>
</html:html>
