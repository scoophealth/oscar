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
<%@ page import="oscar.oscarEncounter.data.EctAlphaRecord" %>
<html:html locale="true">
<% response.setHeader("Cache-Control","no-cache");%>
<head>
<title>Antenatal Psychosocial Health Assessment (ALPHA)</title>
<link rel="stylesheet" type="text/css" href="alphaStyle.css">
<link rel="stylesheet" type="text/css" media="print" href="print.css">
<html:base/>

<%
    int demoNo = Integer.parseInt(request.getParameter("demographic_no"));
    int formId = Integer.parseInt(request.getParameter("formId"));
    int provNo = Integer.parseInt(request.getParameter("provNo"));
    EctAlphaRecord rec = new EctAlphaRecord();
    java.util.Properties props = rec.getAlphaRecord(demoNo, formId);
%>

    <script type="text/javascript" language="Javascript">
        function onPrint() {
            window.print();
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
        function onSaveExit() {
            document.forms[0].submit.value="exit";
            ret = confirm("Are you sure you wish to save and close this window?");
            return ret;
        }
    </script>
</head>


<body background="../images/gray_bg.jpg" bgproperties="fixed" topmargin="0" leftmargin="0" rightmargin="0">
<html:form action="/oscarEncounter/Alpha">

<input type="hidden" name="demographic_no" value="<%= props.getProperty("demographic_no", "0") %>" />
<input type="hidden" name="ID" value="<%= props.getProperty("ID", "0") %>"/>
<input type="hidden" name="provider_no" value=<%=request.getParameter("provNo")%> />
<input type="hidden" name="formCreated" value="<%= props.getProperty("formCreated", "") %>" />
<input type="hidden" name="formEdited" value="<%= props.getProperty("formEdited", "") %>" />
<input type="hidden" name="pName" value="<%= props.getProperty("pName", "") %>" />
<input type="hidden" name="provNo" value="<%= request.getParameter("provNo") %>" />
<input type="hidden" name="submit" value="exit"/>

<table class="Head" class="hidePrint">
    <tr>
        <td align="left">
            <input type="submit" value="Save" onclick="javascript:return onSave();" />
            <input type="submit" value="Save and Exit" onclick="javascript:return onSaveExit();"/>
            <input type="submit" value="Exit" onclick="javascript:return onExit();"/>
            <input type="button" value="Print" onclick="javascript:return onPrint();"/>
        </td>
    </tr>
</table>

<table border="0" cellspacing="0" cellpadding="0" width="100%" >
    <tr bgcolor="#486ebd">
        <th align='CENTER'><font size="-1" face="Arial, Helvetica, sans-serif" color="#FFFFFF">ANTENATAL PSYCHOSOCIAL HEALTH ASSESSMENT (ALPHA)</font></th>
    </tr>
</table>
<table width="100%" border="0" bgcolor="ivory">
    <tr bgcolor="#99FF99">
        <td><b>FAMILY FACTORS</b></td>
        <td align="right"></td>
    </tr>
    <tr>
        <td width="50%"><b>Social support (CA, WA, PD)</b>
            <li><font size="-1">How does your partner/family feel about your pregnancy?</font></li>
            <li><font size="-1">Who will be helping you when you go home with your baby?</font></li>
        </td>
        <td>
            <textarea name="socialSupport" style="width:100%" cols="40" rows="3"><%= props.getProperty("socialSupport", "") %></textarea>
        </td>
    </tr>
    <tr>
        <td><b>Recent stressful life events (CA, WA, PD, PI)</b>
            <li><font size="-1">What life changes have you experienced this year?</font></li>
            <li><font size="-1">What changes are you planning during this pregnancy?</font></li>
        </td>
        <td>
            <textarea name="lifeEvents" style="width:100%" cols="40" rows="3"><%= props.getProperty("lifeEvents", "") %></textarea>
        </td>
    </tr>
    <tr>
        <td><b>Couple's relationship (CD, PD, WA, CA)</b>
            <li><font size="-1">How would you describe your relationship with your partner?</font></li>
            <li><font size="-1">What do you think your relationship will be like after the birth?</font><br></li>
        </td>
        <td>
            <textarea name="coupleRelationship" style="width:100%" cols="40" rows="3"><%= props.getProperty("coupleRelationship", "") %></textarea>
        </td>
    </tr>
    <tr bgcolor="#99FF99">
        <td colspan="2"><b>MATERNAL FACTORS</b></td>
    </tr>
    <tr>
        <td valign="top"><b>Prenatal care (late onset) (WA)</b>
            <li><font size="-1">First prenatal visit in third trimester? (check records)</font></li>
        </td>
        <td>
            <textarea name="prenatalCare" style="width:100%" cols="40" rows="2"><%= props.getProperty("prenatalCare", "") %></textarea>
        </td>
    </tr>
    <tr>
        <td valign="top"><b>Prenatal education (refusal or quit) (CA)</b>
            <li><font size="-1">What are your plans for prenatal classes? </font></li>
        </td>
        <td>
            <textarea name="prenatalEducation" style="width:100%" cols="40" rows="2"><%= props.getProperty("prenatalEducation", "") %></textarea>
        </td>
    </tr>
    <tr>
        <td valign="top"> <b>Feelings toward pregnancy after 20 weeks (CA, WA) </b>
            <li><font size="-1">How did you feel when you just found out you were pregnant?</font></li>
            <li><font size="-1">How do you feel about it now?</font></li>
        </td>
        <td>
            <textarea name="feelingsRePregnancy" style="width:100%" cols="40" rows="2"><%= props.getProperty("feelingsRePregnancy", "") %></textarea>
        </td>
    </tr>
    <tr>
        <td valign="top"> <b>Relationship with parents in childhood (CA)</b>
            <li><font size="-1">How did you get along with your parents?</font></li>
            <li><font size="-1">Did you feel loved by your parents?</font></li>
        </td>
        <td>
            <textarea name="relationshipParents" style="width:100%" cols="40" rows="2"><%= props.getProperty("relationshipParents", "") %></textarea>
        </td>
    </tr>
    <tr>
        <td valign="top"> <b>Self esteem (CA, WA)</b>
            <li><font size="-1">What concerns do you have about becoming/being a mother?</font></li>
        </td>
        <td>
            <textarea name="selfEsteem" style="width:100%" cols="40" rows="2"><%= props.getProperty("selfEsteem", "") %></textarea>
        </td>
    </tr>
    <tr>
        <td   valign="top"> <b>History of psychiatric/emotional problems (CA, WA, PD)</b>
            <li> <font size="-1">Have you ever had emotional problems?</font></li>
            <li><font size="-1">Have you ever seen a psychiatrist or therapist?</font></li>
        </td>
        <td>
            <textarea name="psychHistory" style="width:100%" cols="40" rows="2"><%= props.getProperty("psychHistory", "") %></textarea>
        </td>
    </tr>
    <tr>
        <td   valign="top"> <b>Depression in this pregnancy (PD)</b>
            <li><font size="-1">How has your mood been during this pregnancy? </font></li>
        </td>
        <td>
            <textarea name="depression" style="width:100%" cols="40" rows="2"><%= props.getProperty("depression", "") %></textarea>
        </td>
    </tr>
    <tr bgcolor="#99FF99">
        <td valign="top" colspan="2"><b>SUBSTANCE USE</b></td>
    </tr>
    <tr>
        <td valign="top"> <b>Alcohol/drug abuse (WA, CA)</b>
            <li><font size="-1">How many drinks of alcohol do you have per week?</font></li>
            <li><font size="-1">Are there times when you drink more than that?</font></li>
            <li><font size="-1">Do you or your partner use recreational drugs?</font></li>
            <li><font size="-1">Do you or your partner have a problem with alcohol or drugs?</font></li>
            <li><font size="-1">Consider CAGE (<b>C</b>ut down, <b>A</b>nnoyed, <b>G</b>uilty, <b>E</b>ye opener)</font></li>
        </td>
        <td>
            <textarea name="alcoholDrugAbuse" style="width:100%" cols="40" rows="5"><%= props.getProperty("alcoholDrugAbuse", "") %></textarea>
        </td>
    </tr>
    <tr bgcolor="#99FF99">
        <td valign="top" colspan="2"><b>FAMILY VIOLENCE</b></td>
    </tr>
    <tr>
        <td valign="top"> <b>Woman or partner experienced or witnessed abuse <br>
              (physical, emotional, sexual) (CA, WA)</b>
            <li><font size="-1">What was your parents' relationship like?</font></li>
            <li><font size="-1">Did your father ever scare or hurt your mother?</font></li>
            <li><font size="-1">Did your parents ever scare or hurt you?</font></li>
            <li><font size="-1">Were you ever sexually abused as a child?</font></li>
        </td>
        <td>
            <textarea name="abuse" style="width:100%" cols="40" rows="5"><%= props.getProperty("abuse", "") %></textarea>
        </td>
    </tr>
    <tr>
        <td valign="top"> <b>Current or past woman abuse (WA, CA, PD)</b><br>
            <li><font size="-1">How do you and your partner solve arguments? </font></li>
            <li><font size="-1">Do you ever feel frightened by what your partner says or does? </font></li>
            <li><font size="-1">Have you ever been hit/pushed/slapped by a partner?</font></li>
            <li><font size="-1">Has your partner ever humiliated you or psychologically abused you in other ways? </font></li>
            <li><font size="-1">Have you ever been forced to have sex against your will? </font></li>
        </td>
        <td>
            <textarea name="womanAbuse" style="width:100%" cols="40" rows="5"><%= props.getProperty("womanAbuse", "") %></textarea>
        </td>
    </tr>
    <tr style="page-break-before:always;">
        <td valign="top"> <b>Previous child abuse by woman or partner (CA)</b>
            <li><font size="-1">Do you/your partner have children not living with you? If so, why? </font></li>
            <li><font size="-1">Have you ever had involvement with a child protection agency<br>
                (ie Children's Aid Society)? </font></li>
        </td>
        <td>
            <textarea name="childAbuse" style="width:100%" cols="40" rows="2"><%= props.getProperty("childAbuse", "") %></textarea>
        </td>
    </tr>
    <tr>
        <td valign="top"> <b>Child discipline (CA) </b>
            <li><font size="-1">How were you disciplined as a child?</font></li>
            <li><font size="-1">How do you think you will discipline your child?</font></li>
            <li><font size="-1">How do you deal with your kids at home when they misbehave?</font></li>
        </td>
        <td>
            <textarea name="childDiscipline" style="width:100%" cols="40" rows="3"><%= props.getProperty("childDiscipline", "") %></textarea>
        </td>
    </tr>
</table>
<table width="100%" border="0" bgcolor="ivory">
    <tr bgcolor="#99ff99">
        <td align="center" colspan="6"><b>FOLLOW-UP PLAN</b></td>
    </tr>
    <tr>
        <td><input type="checkbox" name="provCounselling" <%= props.getProperty("provCounselling", "") %>></td>
        <td> Supportive counselling by provider</td>
        <td><input type="checkbox" name="homecare" <%= props.getProperty("homecare", "") %>></td>
        <td>Homecare</td>
        <td><input type="checkbox" name="assaultedWomen" <%= props.getProperty("assaultedWomen", "") %>></td>
        <td>Assaulted women's helpline / shelter / counselling</td>
    </tr>
    <tr>
        <td><input type="checkbox" name="addAppts" <%= props.getProperty("addAppts", "") %>></td>
        <td>Additional prenatal appointments </td>
        <td><input type="checkbox" name="parentingClasses" <%= props.getProperty("parentingClasses", "") %>></td>
        <td>Parenting classes / parents' support group</td>
        <td><input type="checkbox" name="legalAdvice" <%= props.getProperty("legalAdvice", "") %>></td>
        <td>Legal advice</td>
    </tr>
    <tr>
        <td><input type="checkbox" name="postpartumAppts" <%= props.getProperty("postpartumAppts", "") %>></td>
        <td>Additional postpartum appointments</td>
        <td><input type="checkbox" name="addictPrograms" <%= props.getProperty("addictPrograms", "") %>></td>
        <td>Addiction treatment programs</td>
        <td><input type="checkbox" name="cas" <%= props.getProperty("cas", "") %>></td>
        <td>Children's Aid Society</td>
    </tr>
    <tr>
        <td><input type="checkbox" name="babyVisits" <%= props.getProperty("babyVisits", "") %>></td>
        <td>Additional well baby visits </td>
        <td><input type="checkbox" name="quitSmoking" <%= props.getProperty("quitSmoking", "") %>></td>
        <td>Smoking cessation resources</td>
        <td><input type="checkbox" name="other1" <%= props.getProperty("other1", "") %>></td>
        <td>Other:<input type="text" name="other1Name" value="<%= props.getProperty("other1Name", "")%>"></td>
    </tr>
    <tr>
        <td><input type="checkbox" name="publicHealth" <%= props.getProperty("publicHealth", "") %>></td>
        <td>Public Health referral </td>
        <td><input type="checkbox" name="socialWorker" <%= props.getProperty("socialWorker", "") %>></td>
        <td>Social Worker</td>
        <td><input type="checkbox" name="other2" <%= props.getProperty("other2", "") %>></td>
        <td>Other:<input type="text" name="other2Name" value="<%= props.getProperty("other2Name", "") %>"></td>
    </tr>
    <tr>
        <td><input type="checkbox" name="prenatalEdu" <%= props.getProperty("prenatalEdu", "") %>></td>
        <td>Prenatal education services </td>
        <td><input type="checkbox" name="psych" <%= props.getProperty("psych", "") %>></td>
        <td>Psychologist / Psychiatrist</td>
        <td><input type="checkbox" name="other3" <%= props.getProperty("other3", "") %>></td>
        <td>Other:<input type="text" name="other3Name" value="<%= props.getProperty("other3Name", "") %>"></td>
    </tr>
    <tr>
        <td><input type="checkbox" name="nutritionist" <%= props.getProperty("nutritionist", "") %>></td>
        <td>Nutritionist </td>
        <td><input type="checkbox" name="therapist" <%= props.getProperty("therapist", "") %>></td>
        <td>Psychotherapist / marital / family therapist</td>
        <td><input type="checkbox" name="other4" <%= props.getProperty("other4", "") %>></td>
        <td>Other:<input type="text" name="other4Name" value="<%= props.getProperty("other4Name", "") %>"></td>
    </tr>
    <tr>
        <td><input type="checkbox" name="resources" <%= props.getProperty("resources", "") %>></td>
        <td>Community resources / mothers' group </td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
    </tr>
</table>
<table width="100%" border="0" bgcolor="ivory">
	<tr>
        <td><b>COMMENTS</b>:<br>
            <textarea name="comments" style="width:100%" cols="80"><%= props.getProperty("comments", "") %></textarea>
        </td>

    </tr>
</table>

<table class="Head" class="hidePrint">
    <tr>
        <td align="left">
            <input type="submit" value="Save" onclick="javascript:return onSave();" />
            <input type="submit" value="Save and Exit" onclick="javascript:return onSaveExit();"/>
            <input type="submit" value="Exit" onclick="javascript:return onExit();"/>
            <input type="button" value="Print" onclick="javascript:return onPrint();"/>
        </td>
    </tr>
</table>

</html:form>
</body>
</html:html>