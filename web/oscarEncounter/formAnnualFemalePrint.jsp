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
<%@ page import="oscar.oscarEncounter.data.EctAnnualRecord" %>
<html:html locale="true">
<% response.setHeader("Cache-Control","no-cache");%>
<head>
<title>Annual Female Health Review</title>
<link rel="stylesheet" type="text/css" href="annualStyle.css">
<link rel="stylesheet" type="text/css" media="print" href="print.css">
<html:base/>
</head>

<%
    int demoNo = Integer.parseInt(request.getParameter("demographic_no"));
    int formId = Integer.parseInt(request.getParameter("formId"));
    int provNo = Integer.parseInt(request.getParameter("provNo"));
    EctAnnualRecord rec = new EctAnnualRecord();
    java.util.Properties props = rec.getAnnualRecord(demoNo, formId);
%>

<script type="text/javascript" language="Javascript">
    function onPrint() {
        window.print();
        return true;
    }
    function onClose() {
        window.location="formAnnual.jsp?demographic_no=<%=demoNo%>&formId=<%=formId%>&provNo=<%=provNo%>";
        return true;
    }
</script>

<BODY class="printAnnual" topmargin="0" leftmargin="0" rightmargin="0">

<input type="hidden" name="demographic_no" value="<%= props.getProperty("demographic_no", "0") %>" />
<input type="hidden" name="ID" value="<%= props.getProperty("ID", "0") %>"/>
<input type="hidden" name="provider_no" value=<%=request.getParameter("provNo")%> />
<input type="hidden" name="formCreated" value="<%= props.getProperty("formCreated", "") %>" />
<input type="hidden" name="formEdited" value="<%= props.getProperty("formEdited", "") %>" />
<input type="hidden" name="provNo" value="<%= request.getParameter("provNo") %>" />

<table class="Header">
    <tr>
        <td align="left">
            <input type="button" value="Print" onclick="javascript:return onPrint();" />
            <input type="button" value="Close" onclick="javascript:return onClose();"/>
        </td>
    </tr>
</table>

<table cellspacing="3" cellpadding="0" width="100%">
    <tr>
        <td>
            <big><i><b>ANNUAL FEMALE HEALTH REVIEW</b></i></big>
        </td>
        <td>
            <b>Name:</b> <%= props.getProperty("pName", "") %>
        </td>
        <td>
            <b>Age:</b> <%= props.getProperty("age", "") %>
        </td>
        <td>
            <b>Date</b><small>(yyyy/mm/dd)</small>: <%=props.getProperty("formDate", "") %>
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
                    <td valign="top" width="30%" style="height:480px;"><%= props.getProperty("currentConcerns", "") %></td>
                </tr>
                <tr>
                    <td align="center">
                        See chart for continuation<br>
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
                    <td align="right"><%= props.getProperty("head", "") %></td>
                </tr>
                <tr>
                    <td><%= checkMarks(props.getProperty("respN", "")) %></td>
                    <td><%= checkMarks(props.getProperty("respAbN", "")) %></td>
                    <td>Resp:</td>
                    <td align="right"><%= props.getProperty("resp", "") %></td>
                </tr>
                <tr>
                    <td><%= checkMarks(props.getProperty("cardioN", "")) %></td>
                    <td><%= checkMarks(props.getProperty("cardioAbN", "")) %></td>
                    <td>Cardio:</td>
                    <td align="right"><%= props.getProperty("cardio", "") %></td>
                </tr>
                <tr>
                    <td><%= checkMarks(props.getProperty("giN", "")) %></td>
                    <td><%= checkMarks(props.getProperty("giAbN", "")) %></td>
                    <td>G.I.:</td>
                    <td align="right"><%= props.getProperty("gi", "") %></td>
                </tr>
                <tr>
                    <td><%= checkMarks(props.getProperty("guN", "")) %></td>
                    <td><%= checkMarks(props.getProperty("guAbN", "")) %></td>
                    <td>G.U.:</td>
                    <td align="right"><%= props.getProperty("gu", "") %></td>
                </tr>
                <tr>
                    <td><%= checkMarks(props.getProperty("skinN", "")) %></td>
                    <td><%= checkMarks(props.getProperty("skinAbN", "")) %></td>
                    <td>Skin:</td>
                    <td colspan="3" align="right"><%= props.getProperty("skin", "") %></td>
                </tr>
                <tr>
                    <td><%= checkMarks(props.getProperty("mskN", "")) %></td>
                    <td><%= checkMarks(props.getProperty("mskAbN", "")) %></td>
                    <td>MSK:</td>
                    <td colspan="3" align="right"><%= props.getProperty("msk", "") %></td>
                </tr>
                <tr>
                    <td><%= checkMarks(props.getProperty("endocrinN", "")) %></td>
                    <td><%= checkMarks(props.getProperty("endocrinAbN", "")) %></td>
                    <td>Endocrin:</td>
                    <td colspan="3" align="right"><%= props.getProperty("endocrin", "") %></td>
                </tr>
                <tr>
                    <td valign="top"><%= checkMarks(props.getProperty("otherN", "")) %></td>
                    <td valign="top"><%= checkMarks(props.getProperty("otherAbN", "")) %></td>
                    <td valign="top">OTHER:</td>
                    <td colspan="3" align="right"><%= props.getProperty("other", "") %></td>
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
                    <td nowrap="true">GTPAL Revisions?&nbsp;
                        <%= checkMarks(props.getProperty("noGtpalRevisions", "")) %>
                        NO
                        <%= checkMarks(props.getProperty("yesGtpalRevisions", "")) %>
                        YES<br>
                        <%= checkMarks(props.getProperty("frontSheet", "")) %>
                        Front Sheet Updated
                    </td>
                </tr>
                <tr>
                    <td>&nbsp;</td>
                    <td>&nbsp;</td>
                    <td>
                        LMP<small>(yyyy/mm/dd)</small>: <%= props.getProperty("lmp", "") %><br>
                        Menopause: <%= props.getProperty("menopause", "") %>
                        /yrs.
                    </td>
                </tr>
                <tr>
                    <td><%= checkMarks(props.getProperty("papSmearsN", "")) %></td>
                    <td><%= checkMarks(props.getProperty("papSmearsAbN", "")) %></td>
                    <td nowrap="true">Previous Pap. Smears: <%= props.getProperty("papSmears", "") %></td>
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
                    <td colspan="4">REVIEW:</td>
                </tr>
                <tr>
                    <td>&nbsp;</td>
                    <td><%= checkMarks(props.getProperty("drugs", "")) %></td>
                    <td>Drugs</td>
                    <td>&nbsp;</td>
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
        <td class="DashedBorder" width="50%">
            <table>
                <tr>
                    <td colspan="3" nowrap="true"><b>LIFESTYLE REVIEW:</b></td>
                    <td><b><i><small>("Any concerns with ...?")</small></i></b></td>
                </tr>
                <tr>
                    <td>No</td>
                    <td colspan="2">Yes</td>
                    <td>&nbsp;</td>
                </tr>
                <tr>
                    <td><%= checkMarks(props.getProperty("smokingNo", "")) %></td>
                    <td><%= checkMarks(props.getProperty("smokingYes", "")) %></td>
                    <td>Smoking:</td>
                    <td align="right"><%= props.getProperty("smoking", "") %></td>
                </tr>
                <tr>
                    <td><%= checkMarks(props.getProperty("alcoholNo", "")) %></td>
                    <td><%= checkMarks(props.getProperty("alcoholYes", "")) %></td>
                    <td>Alcohol:</td>
                    <td align="right"><%= props.getProperty("alcohol", "") %></td>
                </tr>
                <tr>
                    <td><%= checkMarks(props.getProperty("otcNo", "")) %></td>
                    <td><%= checkMarks(props.getProperty("otcYes", "")) %></td>
                    <td>OTC/Illicit Drugs:</td>
                    <td align="right"><%= props.getProperty("otc", "") %></td>
                </tr>
                <tr>
                    <td><%= checkMarks(props.getProperty("exerciseNo", "")) %></td>
                    <td><%= checkMarks(props.getProperty("exerciseYes", "")) %></td>
                    <td>Exercise/Sports</td>
                    <td align="right"><%= props.getProperty("exercise", "") %></td>
                </tr>
                <tr>
                    <td><%= checkMarks(props.getProperty("nutritionNo", "")) %></td>
                    <td><%= checkMarks(props.getProperty("nutritionYes", "")) %></td>
                    <td>Nutrition:</td>
                    <td align="right"><%= props.getProperty("nutrition", "") %></td>
                </tr>
                <tr>
                    <td><%= checkMarks(props.getProperty("dentalNo", "")) %></td>
                    <td><%= checkMarks(props.getProperty("dentalYes", "")) %></td>
                    <td>Dental Hygiene:</td>
                    <td align="right"><%= props.getProperty("dental", "") %></td>
                </tr>
                <tr>
                    <td valign="top"><%= checkMarks(props.getProperty("relationshipNo", "")) %></td>
                    <td valign="top"><%= checkMarks(props.getProperty("relationshipYes", "")) %></td>
                    <td valign="top">Relationship Issues:</td>
                    <td align="right"><%= props.getProperty("relationship", "") %></td>
                </tr>
                <tr>
                    <td><%= checkMarks(props.getProperty("sexualityNo", "")) %></td>
                    <td><%= checkMarks(props.getProperty("sexualityYes", "")) %></td>
                    <td nowrap="true">Sexuality Risks (STD/HIV):</td>
                    <td align="right"><%= props.getProperty("sexuality", "") %></td>
                </tr>
                <tr>
                    <td><%= checkMarks(props.getProperty("occupationalNo", "")) %></td>
                    <td><%= checkMarks(props.getProperty("occupationalYes", "")) %></td>
                    <td nowrap="true">Occupational Risks:</td>
                    <td align="right"><%= props.getProperty("occupational", "") %></td>
                </tr>
                <tr>
                    <td><%= checkMarks(props.getProperty("drivingNo", "")) %></td>
                    <td><%= checkMarks(props.getProperty("drivingYes", "")) %></td>
                    <td nowrap="true">Driving Safety:</td>
                    <td align="right"><%= props.getProperty("driving", "") %></td>
                </tr>
                <tr>
                    <td><%= checkMarks(props.getProperty("travelNo", "")) %></td>
                    <td><%= checkMarks(props.getProperty("travelYes", "")) %></td>
                    <td nowrap="true">Foreign Travel (in last yr.):</td>
                    <td align="right"><%= props.getProperty("travel", "") %></td>
                </tr>
                <tr>
                    <td valign="top"><%= checkMarks(props.getProperty("otherNo", "")) %></td>
                    <td valign="top"><%= checkMarks(props.getProperty("otherYes", "")) %></td>
                    <td nowrap="true" valign="top">Other:</td>
                    <td rowspan="3" align="right"><%= props.getProperty("otherLifestyle", "") %></td>
                </tr>
            </table>
        </td>
        <td width="100%" valign="top" class="DashedBorder">
            <table width="100%">
                <tr>
                    <td width="50%" colspan="2"><b>SCREENING REVIEW:</b></td>
                </tr>
                <tr>
                    <td>&nbsp;</td>
                </tr>
                <tr>
                    <td><%= checkMarks(props.getProperty("mammogram", "")) %></td>
                    <td>Mammogram</td>
                </tr>
                <tr>
                    <td><%= checkMarks(props.getProperty("breast", "")) %></td>
                    <td>Breast-Self Exam</td>
                </tr>
                <tr>
                    <td><%= checkMarks(props.getProperty("pap", "")) %></td>
                    <td>Pap Smear</td>
                </tr>
                <tr>
                    <td><%= checkMarks(props.getProperty("femaleImmunization", "")) %></td>
                    <td>Immunization</td>
                </tr>
                <tr>
                    <td><%= checkMarks(props.getProperty("precontraceptive", "")) %></td>
                    <td nowrap="true">Precontraceptive Councelling (Rubella, Folate)</td>
                </tr>
                <tr>
                    <td><%= checkMarks(props.getProperty("femaleCardiac", "")) %></td>
                    <td>Cardiac Risk Factors</td>
                </tr>
                <tr>
                    <td><%= checkMarks(props.getProperty("osteoporosis", "")) %></td>
                    <td>Osteoporosis Risk</td>
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
        <td colspan="9"><b>PHYSICAL EXAM:</b></td>
    </tr>
    <tr>
        <td><b>VITALS:  </b></td>
        <td>
            B.P: <%= props.getProperty("bprTop", "") %>/
            <%= props.getProperty("bprBottom", "") %> (R)
        </td>
        <td align="right">Pulse: </td>
        <td><%= props.getProperty("pulse", "") %> /min</td>
        <td align="right">Height:</td>
        <td><%= props.getProperty("height", "") %> cm.</td>
        <td align="right">Weight: <%= props.getProperty("weight", "") %> Kg.</td>
    <tr>
        <td>&nbsp;</td>
        <td>
            B.P: <%= props.getProperty("bplTop", "") %>/
            <%= props.getProperty("bplBottom", "") %> (L)
        </td>
        <td align="right">Rhythm:</td>
        <td><%= props.getProperty("rhythm", "") %></td>
        <td align="right">Urine Dipstick:</td>
        <td><%= props.getProperty("urine", "") %></td>
    </tr>
</table>
<table style="page-break-before:always;" width="100%">
    <tr>
        <td rowspan="3">
            <table width="100%" class="DashedBorder">
                <tr>
                    <td><b> PHYSICAL SIGNS: </b></td>
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
                    <td><b>ASSESSMENT</b></td>
                </tr>
                <tr>
                    <td align="center" class="assessmentPlan"><%= props.getProperty("assessment", "") %></td>
                </tr>
            </table>
        </td>
        <td>
            <table width="100%">
                <tr>
                    <td align="center"><b>PLAN</b></td>
                </tr>
                <tr>
                    <td align="center" class="assessmentPlan"><%= props.getProperty("plan", "") %></td>
                </tr>
            </table>
        </td>
    </tr>
    <tr>
        <td colspan="2" align="right">
            Signature: <%= props.getProperty("signature", "") %>
        </td>
    </tr>
</table>
</td>
</tr>
</table>

<table class="Header">
    <tr>
        <td align="left">
            <input type="button" value="Print" onclick="javascript:return onPrint();" />
            <input type="button" value="Close" onclick="javascript:return onClose();"/>
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