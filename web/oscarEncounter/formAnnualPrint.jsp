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

<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="oscar.oscarEncounter.data.EctAnnualRecord" %>
<html:html locale="true">
<% response.setHeader("Cache-Control","no-cache");%>
<head>
<title><bean:message key="oscarEncounter.formAnnualPrint.title"/></title>
<link rel="stylesheet" type="text/css" href="annualStyle.css">
<html:base/>

<style media="print">
.hidePrint
{
    display: none;
}
</style>
</head>

<%
    int demoNo = Integer.parseInt(request.getParameter("demographic_no"));
    int formId = Integer.parseInt(request.getParameter("formId"));
    EctAnnualRecord rec = new EctAnnualRecord();
    java.util.Properties props = rec.getAnnualRecord(demoNo, formId);
    String color="#FFFFFF";
%>



<BODY bgproperties="fixed" bgcolor="<%=color%>" onLoad="javascript:window.focus()" topmargin="0" leftmargin="0" rightmargin="0">
<html:form action="/oscarEncounter/Annual">


<input type="hidden" name="demographic_no" value="<%= props.getProperty("demographic_no", "0") %>" />
<input type="hidden" name="ID" value="<%= props.getProperty("ID", "0") %>"/>
<input type="hidden" name="provider_no" value=<%=request.getParameter("provNo")%> />
<input type="hidden" name="formCreated" value="<%= props.getProperty("formCreated", "") %>" />
<input type="hidden" name="formEdited" value="<%= props.getProperty("formEdited", "") %>" />

<table >
<tr>
<td>
<table cellspacing="3" cellpadding="0" width="100%">
    <tr>
        <td>
            <big><i><b><bean:message key="oscarEncounter.formAnnualPrint.msgAnnualHealthReview"/></b></i></big>
        </td>
        <td>
            <b><bean:message key="oscarEncounter.formAnnualPrint.msgName"/>:</b> <input type="text" class="Input" name="pName" readonly="true" size="30" value="<%= props.getProperty("pName", "") %>" />
        </td>
        <td>
            <b><bean:message key="oscarEncounter.formAnnualPrint.msgAge"/>:</b> <input type="text" class="Input" readonly="true" name="age" size="11" value="<%= props.getProperty("age", "") %>" readonly="true" />
        </td>
        <td>
            <b><bean:message key="oscarEncounter.formAnnualPrint.msgDate"/></b><small>(yyyy/mm/dd)</small>: <input type="text" readonly="true" class="Input" name="formDate" size="11" value="<%=props.getProperty("formDate", "") %>" />
        </td>
    </tr>
</table>
<table width="100%" >
    <tr>
        <td rowspan="4">
            <table class="DashedBorder" width="100%">
                <tr>
                    <td><b><bean:message key="oscarEncounter.formAnnualPrint.msgCurrentConcerns"/>:</b></td>
                </tr>
                <tr>
                    <td><textarea style="height:480px; width:400px;" name="currentConcerns" readonly="true"><%= props.getProperty("currentConcerns", "") %></textarea></td>
                </tr>
                <tr>
                    <td align="center">
                        <bean:message key="oscarEncounter.formAnnualPrint.msgSeeChart"/> &nbsp;&nbsp;&nbsp;
                        <%= checkMarks(props.getProperty("currentConcernsNo", "")) %>
                        &nbsp;<bean:message key="oscarEncounter.formAnnualPrint.msgNo"/>&nbsp;&nbsp;&nbsp;
                        <%= checkMarks(props.getProperty("currentConcernsYes", "")) %>
                        &nbsp;<bean:message key="oscarEncounter.formAnnualPrint.msgYes"/>
                    </td>
                </tr>
            </table>
        </td>
        <td>
            <table width="100%">
                <tr>
                    <td colspan="3"><b><bean:message key="oscarEncounter.formAnnualPrint.msgSystemsReview"/>:</b></td>
                    <td>&nbsp;</td>
                </tr>
                <tr>
                    <td><b><bean:message key="oscarEncounter.formAnnualPrint.msgN"/></b></td>
                    <td colspan="2"><b><bean:message key="oscarEncounter.formAnnualPrint.msgAbN"/></b></td>
                </tr>
                <tr>
                    <td><%= checkMarks(props.getProperty("headN", "")) %></td>
                    <td><%= checkMarks(props.getProperty("headAbN", "")) %></td>
                    <td align="left" nowrap="true"><bean:message key="oscarEncounter.formAnnualPrint.msgHeadNeck"/>:</td>
                    <td align="right"><input type="text" readonly="true" name="head" class="SystemsReview" value="<%= props.getProperty("head", "") %>" /></td>
                </tr>
                <tr>
                    <td><%= checkMarks(props.getProperty("respN", "")) %></td>
                    <td><%= checkMarks(props.getProperty("respAbN", "")) %></td>
                    <td><bean:message key="oscarEncounter.formAnnualPrint.msgResp"/>:</td>
                    <td align="right"><input type="text" readonly="true" name="resp" class="SystemsReview" value="<%= props.getProperty("resp", "") %>" /></td>
                </tr>
                <tr>
                    <td><%= checkMarks(props.getProperty("cardioN", "")) %></td>
                    <td><%= checkMarks(props.getProperty("cardioAbN", "")) %></td>
                    <td><bean:message key="oscarEncounter.formAnnualPrint.msgCardio"/>:</td>
                    <td align="right"><input type="text" readonly="true" name="cardio" class="SystemsReview" value="<%= props.getProperty("cardio", "") %>" /></td>
                </tr>
                <tr>
                    <td><%= checkMarks(props.getProperty("giN", "")) %></td>
                    <td><%= checkMarks(props.getProperty("giAbN", "")) %></td>
                    <td><bean:message key="oscarEncounter.formAnnualPrint.msgGI"/>:</td>
                    <td align="right"><input type="text" readonly="true" name="gi" class="SystemsReview" value="<%= props.getProperty("gi", "") %>" /></td>
                </tr>
                <tr>
                    <td><%= checkMarks(props.getProperty("guN", "")) %></td>
                    <td><%= checkMarks(props.getProperty("guAbN", "")) %></td>
                    <td><bean:message key="oscarEncounter.formAnnualPrint.msgGU"/>:</td>
                    <td align="right"><input type="text" readonly="true" name="gu" class="SystemsReview" value="<%= props.getProperty("gu", "") %>" /></td>
                </tr>
            </table>
        </td>
    </tr>
    <tr>
        <td>
            <table width="100%" class="DashedBorder">
                <tr>
                    <td>&nbsp;</td>
                    <td>&nbsp;</td>
                    <td>(<bean:message key="oscarEncounter.formAnnualPrint.msgWomen"/>)</td>
                </tr>
                <tr>
                    <td>&nbsp;</td>
                    <td>&nbsp;</td>
                    <td nowrap="true"><bean:message key="oscarEncounter.formAnnualPrint.msgGTPAL"/>&nbsp;
                        <%= checkMarks(props.getProperty("noGtpalRevisions", "")) %>
                        <bean:message key="oscarEncounter.formAnnualPrint.msgNo"/>
                        <%= checkMarks(props.getProperty("yesGtpalRevisions", "")) %>
                        <bean:message key="oscarEncounter.formAnnualPrint.msgYes"/>
                        <%= checkMarks(props.getProperty("frontSheet", "")) %>
                        <bean:message key="oscarEncounter.formAnnualPrint.msgFrontSheet"/>
                    </td>
                </tr>
                <tr>
                    <td>&nbsp;</td>
                    <td>&nbsp;</td>
                    <td>
                        <bean:message key="oscarEncounter.formAnnualPrint.msgLMP"/><small>(yyyy/mm/dd)</small>: <input type="text" readonly="true" name="lmp" value="<%= props.getProperty("lmp", "") %>" size="11" />
                        &nbsp;&nbsp;&nbsp;
                        <bean:message key="oscarEncounter.formAnnualPrint.msgMenopause"/>: <input type="text" readonly="true" name="menopause" size="3" value="<%= props.getProperty("menopause", "") %>" />
                        /<bean:message key="oscarEncounter.formAnnualPrint.msgMenopauseUnit"/>
                    </td>
                </tr>
                <tr>
                    <td><%= checkMarks(props.getProperty("papSmearsN", "")) %></td>
                    <td><%= checkMarks(props.getProperty("papSmearsAbN", "")) %></td>
                    <td nowrap="true"><bean:message key="oscarEncounter.formAnnualPrint.msgPreviousPapSmears"/>: <input type="text" readonly="true" name="papSmears" style="width:285px;" value="<%= props.getProperty("papSmears", "") %>" /></td>
                </tr>
            </table>
        </td>
    </tr>
    <tr>
        <td>
            <table width="100%">
                <tr>
                    <td><%= checkMarks(props.getProperty("skinN", "")) %></td>
                    <td><%= checkMarks(props.getProperty("skinAbN", "")) %></td>
                    <td><bean:message key="oscarEncounter.formAnnualPrint.msgSkin"/>:<font color="<%=color%>"> <bean:message key="oscarEncounter.formAnnualPrint.msgneck"/></font></td>
                    <td colspan="3" align="right"><input type="text" readonly="true" name="skin" class="SystemsReview" value="<%= props.getProperty("skin", "") %>" /></td>
                </tr>
                <tr>
                    <td><%= checkMarks(props.getProperty("mskN", "")) %></td>
                    <td><%= checkMarks(props.getProperty("mskAbN", "")) %></td>
                    <td><bean:message key="oscarEncounter.formAnnualPrint.msgMSK"/>:</td>
                    <td colspan="3" align="right"><input type="text" readonly="true" name="msk" class="SystemsReview" value="<%= props.getProperty("msk", "") %>" /></td>
                </tr>
                <tr>
                    <td><%= checkMarks(props.getProperty("endocrinN", "")) %></td>
                    <td><%= checkMarks(props.getProperty("endocrinAbN", "")) %></td>
                    <td><bean:message key="oscarEncounter.formAnnualPrint.msgEndocrin"/>:</td>
                    <td colspan="3" align="right"><input type="text" readonly="true" name="endocrin" class="SystemsReview" value="<%= props.getProperty("endocrin", "") %>" /></td>
                </tr>
                <tr>
                    <td valign="top"><%= checkMarks(props.getProperty("otherN", "")) %></td>
                    <td valign="top"><%= checkMarks(props.getProperty("otherAbN", "")) %></td>
                    <td valign="top"><bean:message key="oscarEncounter.formAnnualPrint.msgOther"/>:</td>
                    <td colspan="3" align="right"><textarea name="other" class="SystemsReview" style="height:50px;" readonly="true"><%= props.getProperty("other", "") %></textarea></td>
                </tr>
                <tr>
                    <td>&nbsp;</td>
                    <td>&nbsp;</td>
                    <td colspan="4"><bean:message key="oscarEncounter.formAnnualPrint.msgReview"/>:</td>
                </tr>
                <tr>
                    <td>&nbsp;</td>
                    <td><%= checkMarks(props.getProperty("drugs", "")) %></td>
                    <td><bean:message key="oscarEncounter.formAnnualPrint.msgDrugs"/></td>
                    <td><font color="<%=color%>">Drug Reactions</font></td>
                    <td align="right"><%= checkMarks(props.getProperty("medSheet", "")) %></td>
                    <td><bean:message key="oscarEncounter.formAnnualPrint.msgMedSheet"/></td>
                </tr>
                <tr>
                    <td>&nbsp;</td>
                    <td><%= checkMarks(props.getProperty("allergies", "")) %></td>
                    <td colspan="2" nowrap="true">Allergies & Drug Reactions</td>
                    <td align="right"><%= checkMarks(props.getProperty("frontSheet1", "")) %></td>
                    <td><bean:message key="oscarEncounter.formAnnualPrint.msgFrontSheetUpdated"/></td>
                </tr>
                <tr>
                    <td>&nbsp;</td>
                    <td><%= checkMarks(props.getProperty("familyHistory", "")) %></td>
                    <td colspan="2">Family History</td>
                    <td align="right"><%= checkMarks(props.getProperty("frontSheet2", "")) %></td>
                    <td><bean:message key="oscarEncounter.formAnnualPrint.msgFrontSheetUpdated"/></td>
                </tr>
            </table>
        </td>
    </tr>
</table>
<table width="100%">
    <tr>
        <td>
            <table width="100%" class="DashedBorder">
                <tr>
                    <td colspan="3" nowrap="true"><b><bean:message key="oscarEncounter.formAnnualPrint.msgLifestyleReview"/>:</b></td>
                    <td><b><i><small>("Any concerns with ...?")</small></i></b></td>
                </tr>
                <tr>
                    <td><bean:message key="oscarEncounter.formAnnualPrint.msgNo"/></td>
                    <td colspan="2"><bean:message key="oscarEncounter.formAnnualPrint.msgYes"/></td>
                    <td>&nbsp;</td>
                    <td><bean:message key="oscarEncounter.formAnnualPrint.msgNo"/></td>
                    <td colspan="2"><bean:message key="oscarEncounter.formAnnualPrint.msgYes"/></td>
                    <td>&nbsp;</td>
                </tr>
                <tr>
                    <td><%= checkMarks(props.getProperty("smokingNo", "")) %></td>
                    <td><%= checkMarks(props.getProperty("smokingYes", "")) %></td>
                    <td><bean:message key="oscarEncounter.formAnnualPrint.msgSmoking"/>:</td>
                    <td align="right"><input type="text" readonly="true" name="smoking" class="LifestyleReview" value="<%= props.getProperty("smoking", "") %>" /></td>
                    <td><%= checkMarks(props.getProperty("sexualityNo", "")) %></td>
                    <td><%= checkMarks(props.getProperty("sexualityYes", "")) %></td>
                    <td nowrap="true"><bean:message key="oscarEncounter.formAnnualPrint.msgSexualityRisks"/>:</td>
                    <td align="right"><input type="text" readonly="true" name="sexuality" class="LifestyleReview" value="<%= props.getProperty("sexuality", "") %>" /></td>
                </tr>
                <tr>
                    <td><%= checkMarks(props.getProperty("alcoholNo", "")) %></td>
                    <td><%= checkMarks(props.getProperty("alcoholYes", "")) %></td>
                    <td><bean:message key="oscarEncounter.formAnnualPrint.msgAlcohol"/>:</td>
                    <td align="right"><input type="text" readonly="true" name="alcohol" class="LifestyleReview" value="<%= props.getProperty("alcohol", "") %>" /></td>
                    <td><%= checkMarks(props.getProperty("occupationalNo", "")) %></td>
                    <td><%= checkMarks(props.getProperty("occupationalYes", "")) %></td>
                    <td nowrap="true"><bean:message key="oscarEncounter.formAnnualPrint.msgOccupationalRisks"/>:</td>
                    <td align="right"><input type="text" readonly="true" name="occupational" class="LifestyleReview" value="<%= props.getProperty("occupational", "") %>" /></td>
                </tr>
                <tr>
                    <td><%= checkMarks(props.getProperty("otcNo", "")) %></td>
                    <td><%= checkMarks(props.getProperty("otcYes", "")) %></td>
                    <td><bean:message key="oscarEncounter.formAnnualPrint.msgIllicitDrugs"/>:</td>
                    <td align="right"><input type="text" readonly="true" name="otc" class="LifestyleReview" value="<%= props.getProperty("otc", "") %>" /></td>
                    <td><%= checkMarks(props.getProperty("drivingNo", "")) %></td>
                    <td><%= checkMarks(props.getProperty("drivingYes", "")) %></td>
                    <td nowrap="true"><bean:message key="oscarEncounter.formAnnualPrint.msgDrivingSafety"/>:</td>
                    <td align="right"><input type="text" readonly="true" name="driving" class="LifestyleReview" value="<%= props.getProperty("driving", "") %>" /></td>
                </tr>
                <tr>
                    <td><%= checkMarks(props.getProperty("exerciseNo", "")) %></td>
                    <td><%= checkMarks(props.getProperty("exerciseYes", "")) %></td>
                    <td><bean:message key="oscarEncounter.formAnnualPrint.msgExercise"/></td>
                    <td align="right"><input type="text" readonly="true" name="exercise" class="LifestyleReview" value="<%= props.getProperty("exercise", "") %>" /></td>
                    <td><%= checkMarks(props.getProperty("travelNo", "")) %></td>
                    <td><%= checkMarks(props.getProperty("travelYes", "")) %></td>
                    <td nowrap="true"><bean:message key="oscarEncounter.formAnnualPrint.msgForeignTravel"/>:</td>
                    <td align="right"><input type="text" readonly="true" name="travel" class="LifestyleReview" value="<%= props.getProperty("travel", "") %>" /></td>
                </tr>
                <tr>
                    <td><%= checkMarks(props.getProperty("nutritionNo", "")) %></td>
                    <td><%= checkMarks(props.getProperty("nutritionYes", "")) %></td>
                    <td><bean:message key="oscarEncounter.formAnnualPrint.msgNutrition"/>:</td>
                    <td align="right"><input type="text" readonly="true" name="nutrition" class="LifestyleReview" value="<%= props.getProperty("nutrition", "") %>" /></td>
                    <td><%= checkMarks(props.getProperty("otherNo", "")) %></td>
                    <td><%= checkMarks(props.getProperty("otherYes", "")) %></td>
                    <td nowrap="true"><bean:message key="oscarEncounter.formAnnualPrint.msgOther"/>:</td>
                    <td rowspan="3" align="right"><textarea name="otherLifestyle" class="LifestyleReview" rows="6" readonly="true"><%= props.getProperty("otherLifestyle", "") %></textarea></td>
                </tr>
                <tr>
                    <td><%= checkMarks(props.getProperty("dentalNo", "")) %></td>
                    <td><%= checkMarks(props.getProperty("dentalYes", "")) %></td>
                    <td><bean:message key="oscarEncounter.formAnnualPrint.msgDentalHygiene"/>:</td>
                    <td align="right"><input type="text" readonly="true" name="dental" class="LifestyleReview" value="<%= props.getProperty("dental", "") %>" /></td>
                </tr>
                <tr>
                    <td valign="top"><%= checkMarks(props.getProperty("relationshipNo", "")) %></td>
                    <td valign="top"><%= checkMarks(props.getProperty("relationshipYes", "")) %></td>
                    <td valign="top"><bean:message key="oscarEncounter.formAnnualPrint.msgRelationshipIssues"/>:</td>
                    <td align="right"><textarea name="relationship" class="LifestyleReview" rows="2" readonly="true"><%= props.getProperty("relationship", "") %></textarea></td>
                </tr>
            </table>
        </td>
    </tr>
</table>
<table width="100%">
    <tr>
        <td>
            <table width="100%" class="DashedBorder">
                <tr>
                    <td width="50%" colspan="3"><b><bean:message key="oscarEncounter.formAnnualPrint.msgScreeningReview"/>:</b></td>
                    <td width="50%" colspan="3"><b><bean:message key="oscarEncounter.formAnnualPrint.msgScreeningReview"/>:</b></td>
                </tr>
                <tr>
                    <td>&nbsp;&nbsp;&nbsp;</td>
                    <td colspan="2"><b><bean:message key="oscarEncounter.formAnnualPrint.msgWomen"/>:</b></td>
                    <td>&nbsp;&nbsp;</td>
                    <td colspan="2"><b><bean:message key="oscarEncounter.formAnnualPrint.msgMen"/>:</b></td>
                </tr>
                <tr>
                    <td>&nbsp;</td>
                    <td><%= checkMarks(props.getProperty("mammogram", "")) %></td>
                    <td><bean:message key="oscarEncounter.formAnnualPrint.msgMammogram"/></td>
                    <td>&nbsp;</td>
                    <td><%= checkMarks(props.getProperty("rectal", "")) %></td>
                    <td><bean:message key="oscarEncounter.formAnnualPrint.msgRectalExam"/>
                </tr>
                <tr>
                    <td>&nbsp;</td>
                    <td><%= checkMarks(props.getProperty("breast", "")) %></td>
                    <td><bean:message key="oscarEncounter.formAnnualPrint.msgBreastSelf"/></td>
                    <td>&nbsp;</td>
                    <td><%= checkMarks(props.getProperty("maleCardiac", "")) %></td>
                    <td><bean:message key="oscarEncounter.formAnnualPrint.msgCardiacRisk"/>
                </tr>
                <tr>
                    <td>&nbsp;</td>
                    <td><%= checkMarks(props.getProperty("pap", "")) %></td>
                    <td><bean:message key="oscarEncounter.formAnnualPrint.msgPapSmear"/></td>
                    <td>&nbsp;</td>
                    <td><%= checkMarks(props.getProperty("maleImmunization", "")) %></td>
                    <td><bean:message key="oscarEncounter.formAnnualPrint.msgImmunization"/>
                </tr>
                <tr>
                    <td>&nbsp;</td>
                    <td><%= checkMarks(props.getProperty("femaleImmunization", "")) %></td>
                    <td><bean:message key="oscarEncounter.formAnnualPrint.msgImmunization"/></td>
                    <td>&nbsp;</td>
                    <td><%= checkMarks(props.getProperty("maleOther1c", "")) %></td>
                    <td><input type="text" readonly="true" name="maleOther1" class="ScreeningReview" value="<%= props.getProperty("maleOther1", "") %>" /></td>
                </tr>
                <tr>
                    <td>&nbsp;</td>
                    <td><%= checkMarks(props.getProperty("precontraceptive", "")) %></td>
                    <td nowrap="true"><bean:message key="oscarEncounter.formAnnualPrint.msgPrecontraceptive"/></td>
                    <td>&nbsp;</td>
                    <td><%= checkMarks(props.getProperty("maleOther2c", "")) %></td>
                    <td><input type="text" readonly="true" name="maleOther2" class="ScreeningReview" value="<%= props.getProperty("maleOther2", "") %>" /></td>
                </tr>
                <tr>
                    <td>&nbsp;</td>
                    <td><%= checkMarks(props.getProperty("femaleCardiac", "")) %></td>
                    <td><bean:message key="oscarEncounter.formAnnualPrint.msgCardiacRisk"/></td>
                </tr>
                <tr>
                    <td>&nbsp;</td>
                    <td><%= checkMarks(props.getProperty("osteoporosis", "")) %></td>
                    <td><bean:message key="oscarEncounter.formAnnualPrint.msgOsteoporosis"/></td>
                </tr>
                <tr>
                    <td>&nbsp;</td>
                    <td><%= checkMarks(props.getProperty("femaleOther1c", "")) %></td>
                    <td><input type="text" readonly="true" name="femaleOther1" class="ScreeningReview" value="<%= props.getProperty("femaleOther1", "") %>" /></td>
                </tr>
                <tr>
                    <td>&nbsp;</td>
                    <td><%= checkMarks(props.getProperty("femaleOther2c", "")) %></td>
                    <td><input type="text" readonly="true" name="femaleOther2" class="ScreeningReview" value="<%= props.getProperty("femaleOther2", "") %>" /></td>
                </tr>
            </table>
        </td>
    </tr>
</table>
<table width="100%">
    <tr>
        <td colspan="9"><b><bean:message key="oscarEncounter.formAnnualPrint.msgPhysicalExam"/>:</b></td>
    </tr>
    <tr>
        <td><b><bean:message key="oscarEncounter.formAnnualPrint.msgVitals"/>:  </b></td>
        <td>
            <bean:message key="oscarEncounter.formAnnualPrint.msgBP"/>: <input type="text" readonly="true" name="bprTop" size="5" value="<%= props.getProperty("bprTop", "") %>" />/
            <input type="text" readonly="true" name="bprBottom" size="5" value="<%= props.getProperty("bprBottom", "") %>" /> <bean:message key="oscarEncounter.formAnnualPrint.msgR"/>
        </td>
        <td align="right"><bean:message key="oscarEncounter.formAnnualPrint.msgPulse"/>: </td>
        <td><input type="text" readonly="true" name="pulse" size="10" value="<%= props.getProperty("pulse", "") %>" /> /<bean:message key="oscarEncounter.formAnnualPrint.msgPulseUnit"/></td>
        <td align="right"><bean:message key="oscarEncounter.formAnnualPrint.msgHeight"/>:</td>
        <td><input type="text" readonly="true" name="height" size="10" value="<%= props.getProperty("height", "") %>" /> <bean:message key="oscarEncounter.formAnnualPrint.msgHeightUnit"/></td>
        <td align="right"><bean:message key="oscarEncounter.formAnnualPrint.msgWeight"/>: <input type="text" readonly="true" name="weight" size="10" value="<%= props.getProperty("weight", "") %>" /> <bean:message key="oscarEncounter.formAnnualPrint.msgWeightUnit"/></td>
    <tr>
        <td>&nbsp;</td>
        <td>
            <bean:message key="oscarEncounter.formAnnualPrint.msgBP"/>: <input type="text" readonly="true" name="bplTop" size="5" value="<%= props.getProperty("bplTop", "") %>" />/
            <input type="text" readonly="true" name="bplBottom" size="5" value="<%= props.getProperty("bplBottom", "") %>" /> <bean:message key="oscarEncounter.formAnnualPrint.msgL"/>
        </td>
        <td align="right"><bean:message key="oscarEncounter.formAnnualPrint.msgRhythm"/>:</td>
        <td><input type="text" readonly="true" name="rhythm" size="10" value="<%= props.getProperty("rhythm", "") %>" /></td>
        <td align="right"><bean:message key="oscarEncounter.formAnnualPrint.msgUrinedipstick"/>:</td>
        <td><input type="text" readonly="true" name="urine" size="20" value="<%= props.getProperty("urine", "") %>" /></td>
    </tr>
</table>
<table>
    <tr>
        <td rowspan="3">
            <table width="100%" class="DashedBorder">
                <tr>
                    <td><b> <bean:message key="oscarEncounter.formAnnualPrint.msgPhysicalSigns"/>: </b></td>
                </tr>
                <tr>
                    <td><textarea name="physicalSigns" class="PhysicalSigns" readonly="true"><%= props.getProperty("physicalSigns", "") %></textarea></td>
                </tr>
            </table>
        </td>
        <td valign="top"><bean:message key="oscarEncounter.formAnnualPrint.msgIconOfBreasts"/></td>
    </tr>
    <tr>
        <td><bean:message key="oscarEncounter.formAnnualPrint.msgSomeBlankSpace"/></td>
    </tr>
    <tr>
        <td valign="bottom"><bean:message key="oscarEncounter.formAnnualPrint.msgIconOfAbdomen"/></td>
    </tr>
</table>
<table width="100%" class="TableWithBorder">
    <tr>
        <td>
            <table width="100%">
                <tr>
                    <td><b><bean:message key="oscarEncounter.formAnnualPrint.msgAssessment"/></b></td>
                </tr>
                <tr>
                    <td align="center"><textarea name="assessment" class="AssessmentPlan" readonly="true"><%= props.getProperty("assessment", "") %></textarea></td>
                </tr>
            </table>
        </td>
        <td>
            <table width="100%">
                <tr>
                    <td align="center"><b><bean:message key="oscarEncounter.formAnnualPrint.msgPlan"/></b></td>
                </tr>
                <tr>
                    <td align="center"><textarea name="plan" class="AssessmentPlan" readonly="true"><%= props.getProperty("plan", "") %></textarea></td>
                </tr>
            </table>
        </td>
    </tr>
    <tr>
        <td colspan="2" align="right">
            <bean:message key="oscarEncounter.formAnnualPrint.msgSignature"/>: <input type="text" readonly="true" name="signature" size="30" value="<%= props.getProperty("signature", "") %>" />
        </td>
    </tr>
</table>
</td>
</tr>
<tr>
    <td class="hidePrint">
        <a href="formAnnual.jsp?demographic_no=<%=demoNo%>&formId=<%=formId%>"><bean:message key="oscarEncounter.formAnnualPrint.btnCancel"/></a>&nbsp;<a href=# onClick="window.print()"><bean:message key="oscarEncounter.formAnnualPrint.btnPrint"/></a>
    </td>
</tr>
</table>


</html:form>
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