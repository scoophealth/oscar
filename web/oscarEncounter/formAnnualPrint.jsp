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
<title>Annual Health Review</title>
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
            <big><i><b>ANNUAL HEALTH REVIEW</b></i></big>
        </td>
        <td>
            <b>Name:</b> <input type="text" class="Input" name="pName" readonly="true" size="30" value="<%= props.getProperty("pName", "") %>" />
        </td>
        <td>
            <b>Age:</b> <input type="text" class="Input" readonly="true" name="age" size="11" value="<%= props.getProperty("age", "") %>" readonly="true" />
        </td>
        <td>
            <b>Date</b><small>(yyyy/mm/dd)</small>: <input type="text" readonly="true" class="Input" name="formDate" size="11" value="<%=props.getProperty("formDate", "") %>" />
        </td>
    </tr>
</table>
<table width="100%" >
    <tr>
        <td rowspan="4">
            <table class="DashedBorder" width="100%">
                <tr>
                    <td><b>CURRENT CONCERNS:</b></td>
                </tr>
                <tr>
                    <td><textarea style="height:480px; width:400px;" name="currentConcerns" readonly="true"><%= props.getProperty("currentConcerns", "") %></textarea></td>
                </tr>
                <tr>
                    <td align="center">
                        See chart for continuation &nbsp;&nbsp;&nbsp;
                        <%= checkMarks(props.getProperty("currentConcernsNo", "")) %>
                        &nbsp;No&nbsp;&nbsp;&nbsp;
                        <%= checkMarks(props.getProperty("currentConcernsYes", "")) %>
                        &nbsp;Yes
                    </td>
                </tr>
            </table>
        </td>
        <td>
            <table width="100%">
                <tr>
                    <td colspan="3"><b>SYSTEMS REVIEW:</b></td>
                    <td>&nbsp;</td>
                </tr>
                <tr>
                    <td><b>N</b></td>
                    <td colspan="2"><b>AbN</b></td>
                </tr>
                <tr>
                    <td><%= checkMarks(props.getProperty("headN", "")) %></td>
                    <td><%= checkMarks(props.getProperty("headAbN", "")) %></td>
                    <td align="left" nowrap="true">Head & Neck:</td>
                    <td align="right"><input type="text" readonly="true" name="head" class="SystemsReview" value="<%= props.getProperty("head", "") %>" /></td>
                </tr>
                <tr>
                    <td><%= checkMarks(props.getProperty("respN", "")) %></td>
                    <td><%= checkMarks(props.getProperty("respAbN", "")) %></td>
                    <td>Resp:</td>
                    <td align="right"><input type="text" readonly="true" name="resp" class="SystemsReview" value="<%= props.getProperty("resp", "") %>" /></td>
                </tr>
                <tr>
                    <td><%= checkMarks(props.getProperty("cardioN", "")) %></td>
                    <td><%= checkMarks(props.getProperty("cardioAbN", "")) %></td>
                    <td>Cardio:</td>
                    <td align="right"><input type="text" readonly="true" name="cardio" class="SystemsReview" value="<%= props.getProperty("cardio", "") %>" /></td>
                </tr>
                <tr>
                    <td><%= checkMarks(props.getProperty("giN", "")) %></td>
                    <td><%= checkMarks(props.getProperty("giAbN", "")) %></td>
                    <td>G.I.:</td>
                    <td align="right"><input type="text" readonly="true" name="gi" class="SystemsReview" value="<%= props.getProperty("gi", "") %>" /></td>
                </tr>
                <tr>
                    <td><%= checkMarks(props.getProperty("guN", "")) %></td>
                    <td><%= checkMarks(props.getProperty("guAbN", "")) %></td>
                    <td>G.U.:</td>
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
                    <td>(Women)</td>
                </tr>
                <tr>
                    <td>&nbsp;</td>
                    <td>&nbsp;</td>
                    <td nowrap="true">GTPAL Revisions?&nbsp;
                        <%= checkMarks(props.getProperty("noGtpalRevisions", "")) %>
                        NO
                        <%= checkMarks(props.getProperty("yesGtpalRevisions", "")) %>
                        YES
                        <%= checkMarks(props.getProperty("frontSheet", "")) %>
                        Front Sheet Updated
                    </td>
                </tr>
                <tr>
                    <td>&nbsp;</td>
                    <td>&nbsp;</td>
                    <td>
                        LMP<small>(yyyy/mm/dd)</small>: <input type="text" readonly="true" name="lmp" value="<%= props.getProperty("lmp", "") %>" size="11" />
                        &nbsp;&nbsp;&nbsp;
                        Menopause: <input type="text" readonly="true" name="menopause" size="3" value="<%= props.getProperty("menopause", "") %>" />
                        /yrs.
                    </td>
                </tr>
                <tr>
                    <td><%= checkMarks(props.getProperty("papSmearsN", "")) %></td>
                    <td><%= checkMarks(props.getProperty("papSmearsAbN", "")) %></td>
                    <td nowrap="true">Previous Pap. Smears: <input type="text" readonly="true" name="papSmears" style="width:285px;" value="<%= props.getProperty("papSmears", "") %>" /></td>
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
                    <td>Skin:<font color="<%=color%>"> & Neck</font></td>
                    <td colspan="3" align="right"><input type="text" readonly="true" name="skin" class="SystemsReview" value="<%= props.getProperty("skin", "") %>" /></td>
                </tr>
                <tr>
                    <td><%= checkMarks(props.getProperty("mskN", "")) %></td>
                    <td><%= checkMarks(props.getProperty("mskAbN", "")) %></td>
                    <td>MSK:</td>
                    <td colspan="3" align="right"><input type="text" readonly="true" name="msk" class="SystemsReview" value="<%= props.getProperty("msk", "") %>" /></td>
                </tr>
                <tr>
                    <td><%= checkMarks(props.getProperty("endocrinN", "")) %></td>
                    <td><%= checkMarks(props.getProperty("endocrinAbN", "")) %></td>
                    <td>Endocrin:</td>
                    <td colspan="3" align="right"><input type="text" readonly="true" name="endocrin" class="SystemsReview" value="<%= props.getProperty("endocrin", "") %>" /></td>
                </tr>
                <tr>
                    <td valign="top"><%= checkMarks(props.getProperty("otherN", "")) %></td>
                    <td valign="top"><%= checkMarks(props.getProperty("otherAbN", "")) %></td>
                    <td valign="top">OTHER:</td>
                    <td colspan="3" align="right"><textarea name="other" class="SystemsReview" style="height:50px;" readonly="true"><%= props.getProperty("other", "") %></textarea></td>
                </tr>
                <tr>
                    <td>&nbsp;</td>
                    <td>&nbsp;</td>
                    <td colspan="4">REVIEW:</td>
                </tr>
                <tr>
                    <td>&nbsp;</td>
                    <td><%= checkMarks(props.getProperty("drugs", "")) %></td>
                    <td>Drugs</td>
                    <td><font color="<%=color%>">Drug Reactions</font></td>
                    <td align="right"><%= checkMarks(props.getProperty("medSheet", "")) %></td>
                    <td>Med. Sheet Updated</td>
                </tr>
                <tr>
                    <td>&nbsp;</td>
                    <td><%= checkMarks(props.getProperty("allergies", "")) %></td>
                    <td colspan="2" nowrap="true">Allergies & Drug Reactions</td>
                    <td align="right"><%= checkMarks(props.getProperty("frontSheet1", "")) %></td>
                    <td>Front Sheet Updated</td>
                </tr>
                <tr>
                    <td>&nbsp;</td>
                    <td><%= checkMarks(props.getProperty("familyHistory", "")) %></td>
                    <td colspan="2">Family History</td>
                    <td align="right"><%= checkMarks(props.getProperty("frontSheet2", "")) %></td>
                    <td>Front Sheet Updated</td>
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
                    <td colspan="3" nowrap="true"><b>LIFESTYLE REVIEW:</b></td>
                    <td><b><i><small>("Any concerns with ...?")</small></i></b></td>
                </tr>
                <tr>
                    <td>No</td>
                    <td colspan="2">Yes</td>
                    <td>&nbsp;</td>
                    <td>No</td>
                    <td colspan="2">Yes</td>
                    <td>&nbsp;</td>
                </tr>
                <tr>
                    <td><%= checkMarks(props.getProperty("smokingNo", "")) %></td>
                    <td><%= checkMarks(props.getProperty("smokingYes", "")) %></td>
                    <td>Smoking:</td>
                    <td align="right"><input type="text" readonly="true" name="smoking" class="LifestyleReview" value="<%= props.getProperty("smoking", "") %>" /></td>
                    <td><%= checkMarks(props.getProperty("sexualityNo", "")) %></td>
                    <td><%= checkMarks(props.getProperty("sexualityYes", "")) %></td>
                    <td nowrap="true">Sexuality Risks (STD/HIV):</td>
                    <td align="right"><input type="text" readonly="true" name="sexuality" class="LifestyleReview" value="<%= props.getProperty("sexuality", "") %>" /></td>
                </tr>
                <tr>
                    <td><%= checkMarks(props.getProperty("alcoholNo", "")) %></td>
                    <td><%= checkMarks(props.getProperty("alcoholYes", "")) %></td>
                    <td>Alcohol:</td>
                    <td align="right"><input type="text" readonly="true" name="alcohol" class="LifestyleReview" value="<%= props.getProperty("alcohol", "") %>" /></td>
                    <td><%= checkMarks(props.getProperty("occupationalNo", "")) %></td>
                    <td><%= checkMarks(props.getProperty("occupationalYes", "")) %></td>
                    <td nowrap="true">Occupational Risks:</td>
                    <td align="right"><input type="text" readonly="true" name="occupational" class="LifestyleReview" value="<%= props.getProperty("occupational", "") %>" /></td>
                </tr>
                <tr>
                    <td><%= checkMarks(props.getProperty("otcNo", "")) %></td>
                    <td><%= checkMarks(props.getProperty("otcYes", "")) %></td>
                    <td>OTC/Illicit Drugs:</td>
                    <td align="right"><input type="text" readonly="true" name="otc" class="LifestyleReview" value="<%= props.getProperty("otc", "") %>" /></td>
                    <td><%= checkMarks(props.getProperty("drivingNo", "")) %></td>
                    <td><%= checkMarks(props.getProperty("drivingYes", "")) %></td>
                    <td nowrap="true">Driving Safety:</td>
                    <td align="right"><input type="text" readonly="true" name="driving" class="LifestyleReview" value="<%= props.getProperty("driving", "") %>" /></td>
                </tr>
                <tr>
                    <td><%= checkMarks(props.getProperty("exerciseNo", "")) %></td>
                    <td><%= checkMarks(props.getProperty("exerciseYes", "")) %></td>
                    <td>Exercise/Sports</td>
                    <td align="right"><input type="text" readonly="true" name="exercise" class="LifestyleReview" value="<%= props.getProperty("exercise", "") %>" /></td>
                    <td><%= checkMarks(props.getProperty("travelNo", "")) %></td>
                    <td><%= checkMarks(props.getProperty("travelYes", "")) %></td>
                    <td nowrap="true">Foreign Travel (in last yr.):</td>
                    <td align="right"><input type="text" readonly="true" name="travel" class="LifestyleReview" value="<%= props.getProperty("travel", "") %>" /></td>
                </tr>
                <tr>
                    <td><%= checkMarks(props.getProperty("nutritionNo", "")) %></td>
                    <td><%= checkMarks(props.getProperty("nutritionYes", "")) %></td>
                    <td>Nutrition:</td>
                    <td align="right"><input type="text" readonly="true" name="nutrition" class="LifestyleReview" value="<%= props.getProperty("nutrition", "") %>" /></td>
                    <td><%= checkMarks(props.getProperty("otherNo", "")) %></td>
                    <td><%= checkMarks(props.getProperty("otherYes", "")) %></td>
                    <td nowrap="true">Other:</td>
                    <td rowspan="3" align="right"><textarea name="otherLifestyle" class="LifestyleReview" rows="6" readonly="true"><%= props.getProperty("otherLifestyle", "") %></textarea></td>
                </tr>
                <tr>
                    <td><%= checkMarks(props.getProperty("dentalNo", "")) %></td>
                    <td><%= checkMarks(props.getProperty("dentalYes", "")) %></td>
                    <td>Dental Hygiene:</td>
                    <td align="right"><input type="text" readonly="true" name="dental" class="LifestyleReview" value="<%= props.getProperty("dental", "") %>" /></td>
                </tr>
                <tr>
                    <td valign="top"><%= checkMarks(props.getProperty("relationshipNo", "")) %></td>
                    <td valign="top"><%= checkMarks(props.getProperty("relationshipYes", "")) %></td>
                    <td valign="top">Relationship Issues:</td>
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
                    <td width="50%" colspan="3"><b>SCREENING REVIEW:</b></td>
                    <td width="50%" colspan="3"><b>SCREENING REVIEW:</b></td>
                </tr>
                <tr>
                    <td>&nbsp;&nbsp;&nbsp;</td>
                    <td colspan="2"><b>Women:</b></td>
                    <td>&nbsp;&nbsp;</td>
                    <td colspan="2"><b>Men:</b></td>
                </tr>
                <tr>
                    <td>&nbsp;</td>
                    <td><%= checkMarks(props.getProperty("mammogram", "")) %></td>
                    <td>Mammogram</td>
                    <td>&nbsp;</td>
                    <td><%= checkMarks(props.getProperty("rectal", "")) %></td>
                    <td>Rectal Exam
                </tr>
                <tr>
                    <td>&nbsp;</td>
                    <td><%= checkMarks(props.getProperty("breast", "")) %></td>
                    <td>Breast-Self Exam</td>
                    <td>&nbsp;</td>
                    <td><%= checkMarks(props.getProperty("maleCardiac", "")) %></td>
                    <td>Cardiac Risk Factors
                </tr>
                <tr>
                    <td>&nbsp;</td>
                    <td><%= checkMarks(props.getProperty("pap", "")) %></td>
                    <td>Pap Smear</td>
                    <td>&nbsp;</td>
                    <td><%= checkMarks(props.getProperty("maleImmunization", "")) %></td>
                    <td>Immunization
                </tr>
                <tr>
                    <td>&nbsp;</td>
                    <td><%= checkMarks(props.getProperty("femaleImmunization", "")) %></td>
                    <td>Immunization</td>
                    <td>&nbsp;</td>
                    <td><%= checkMarks(props.getProperty("maleOther1c", "")) %></td>
                    <td><input type="text" readonly="true" name="maleOther1" class="ScreeningReview" value="<%= props.getProperty("maleOther1", "") %>" /></td>
                </tr>
                <tr>
                    <td>&nbsp;</td>
                    <td><%= checkMarks(props.getProperty("precontraceptive", "")) %></td>
                    <td nowrap="true">Precontraceptive Councelling (Rubella, Folate)</td>
                    <td>&nbsp;</td>
                    <td><%= checkMarks(props.getProperty("maleOther2c", "")) %></td>
                    <td><input type="text" readonly="true" name="maleOther2" class="ScreeningReview" value="<%= props.getProperty("maleOther2", "") %>" /></td>
                </tr>
                <tr>
                    <td>&nbsp;</td>
                    <td><%= checkMarks(props.getProperty("femaleCardiac", "")) %></td>
                    <td>Cardiac Risk Factors</td>
                </tr>
                <tr>
                    <td>&nbsp;</td>
                    <td><%= checkMarks(props.getProperty("osteoporosis", "")) %></td>
                    <td>Osteoporosis Risk</td>
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
        <td colspan="9"><b>PHYSICAL EXAM:</b></td>
    </tr>
    <tr>
        <td><b>VITALS:  </b></td>
        <td>
            B.P: <input type="text" readonly="true" name="bprTop" size="5" value="<%= props.getProperty("bprTop", "") %>" />/
            <input type="text" readonly="true" name="bprBottom" size="5" value="<%= props.getProperty("bprBottom", "") %>" /> (R)
        </td>
        <td align="right">Pulse: </td>
        <td><input type="text" readonly="true" name="pulse" size="10" value="<%= props.getProperty("pulse", "") %>" /> /min</td>
        <td align="right">Height:</td>
        <td><input type="text" readonly="true" name="height" size="10" value="<%= props.getProperty("height", "") %>" /> cm.</td>
        <td align="right">Weight: <input type="text" readonly="true" name="weight" size="10" value="<%= props.getProperty("weight", "") %>" /> Kg.</td>
    <tr>
        <td>&nbsp;</td>
        <td>
            B.P: <input type="text" readonly="true" name="bplTop" size="5" value="<%= props.getProperty("bplTop", "") %>" />/
            <input type="text" readonly="true" name="bplBottom" size="5" value="<%= props.getProperty("bplBottom", "") %>" /> (L)
        </td>
        <td align="right">Rhythm:</td>
        <td><input type="text" readonly="true" name="rhythm" size="10" value="<%= props.getProperty("rhythm", "") %>" /></td>
        <td align="right">Urine Dipstick:</td>
        <td><input type="text" readonly="true" name="urine" size="20" value="<%= props.getProperty("urine", "") %>" /></td>
    </tr>
</table>
<table>
    <tr>
        <td rowspan="3">
            <table width="100%" class="DashedBorder">
                <tr>
                    <td><b> PHYSICAL SIGNS: </b></td>
                </tr>
                <tr>
                    <td><textarea name="physicalSigns" class="PhysicalSigns" readonly="true"><%= props.getProperty("physicalSigns", "") %></textarea></td>
                </tr>
            </table>
        </td>
        <td valign="top">icon of breasts</td>
    </tr>
    <tr>
        <td>some blank space</td>
    </tr>
    <tr>
        <td valign="bottom">icon of abdomen</td>
    </tr>
</table>
<table width="100%" class="TableWithBorder">
    <tr>
        <td>
            <table width="100%">
                <tr>
                    <td><b>ASSESSMENT</b></td>
                </tr>
                <tr>
                    <td align="center"><textarea name="assessment" class="AssessmentPlan" readonly="true"><%= props.getProperty("assessment", "") %></textarea></td>
                </tr>
            </table>
        </td>
        <td>
            <table width="100%">
                <tr>
                    <td align="center"><b>PLAN</b></td>
                </tr>
                <tr>
                    <td align="center"><textarea name="plan" class="AssessmentPlan" readonly="true"><%= props.getProperty("plan", "") %></textarea></td>
                </tr>
            </table>
        </td>
    </tr>
    <tr>
        <td colspan="2" align="right">
            Signature: <input type="text" readonly="true" name="signature" size="30" value="<%= props.getProperty("signature", "") %>" />
        </td>
    </tr>
</table>
</td>
</tr>
<tr>
    <td class="hidePrint">
        <a href="formAnnual.jsp?demographic_no=<%=demoNo%>&formId=<%=formId%>">Cancel</a>&nbsp;<a href=# onClick="window.print()">Print</a>
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