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
<%@ page import="oscar.oscarEncounter.data.EctRourkeRecord" %>
<html:html locale="true">
<% response.setHeader("Cache-Control","no-cache");%>
<head>
<title>Printing Rourke Baby Record</title>
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
    EctRourkeRecord rec = new EctRourkeRecord();
    java.util.Properties props = rec.getRourkeRecord(demoNo, formId);
%>

<body bgproperties="fixed"  bgcolor="#FFFFFF" onLoad="javascript:window.focus()" topmargin="0" leftmargin="0" rightmargin="0">
<html:form action="Rourke">

<input type="hidden" name="demographic_no" value="<%= props.getProperty("demographic_no", "0") %>" />
<input type="hidden" name="ID" value="<%= props.getProperty("ID", "0") %>"/>
<input type="hidden" name="provider_no" value=<%=request.getParameter("provNo")%> />
<input type="hidden" name="formCreated" value="<%= props.getProperty("formCreated", "") %>" />
<input type="hidden" name="formEdited" value="<%= props.getProperty("formEdited", "") %>" />

<table border="0" cellspacing="0" cellpadding="0" width="100%" >
    <tr bgcolor="#A9A9A9">
        <th align=CENTER class="titleweb"  ><font face="Helvetica" color="#FFFFFF">
            Rourke Baby Record: EVIDENCE BASED INFANT/CHILD HEALTH MAINTENANCE GUIDE I</font></th>
    </tr>
</table>
<table width="100%" border="0" cellspacing="1" cellpadding="2">
    <tr valign="top">
        <td width="15%" nowrap>Birth remarks<br>
            <textarea readonly="true" name="birthRemarks" style="width:100%" rows="2" cols="15"><%= props.getProperty("birthRemarks", "") %></textarea>
        </td>
        <td width="20%" nowrap>Risk Factors/Family History<br>
            <textarea readonly="true" name="riskFactors" style="width:100%" rows="2" cols="15"><%= props.getProperty("riskFactors", "") %></textarea>
        </td>
        <td width="65%" nowrap>
            <p>
                NAME<input type="text" readonly="true" name="pName" maxlength="60" size="30" value="<%= props.getProperty("pName", "") %>">
                Birth Date (d/m/yr)<input type="text" readonly="true" name="birthDate" size="10" maxlength="10" value="<%= props.getProperty("birthDate", "") %>" >
                M<%= checkMarks(props.getProperty("male", "")) %>
                &nbsp; F<%= checkMarks(props.getProperty("female", "")) %>
            </p>
            <p>
                Length:<input type="text" readonly="true" name="length" size="6" maxlength="6" value="<%= props.getProperty("length", "") %>">
                cm. Head Circ:<input type="text" readonly="true" name="headCirc" size="6" maxlength="6" value="<%= props.getProperty("headCirc", "") %>">
                cm. Birth Wt.<input type="text" readonly="true" name="birthWeight" size="6" maxlength="7" value="<%= props.getProperty("birthWeight", "") %>">
                gms. Discharge Wt.<input type="text" readonly="true" name="dischargeWeight" size="6" maxlength="7"  value="<%= props.getProperty("dischargeWeight", "") %>">gms.
            </p>
        </td>
    </tr>
</table>
<table width="100%" border="1" cellspacing="0" cellpadding="0">
    <tr align="center">
        <td width="10%" bgcolor="#EEEEEE"> <b>DATE / AGE</b> </td>
        <td  width="22.5%" colspan="3" bgcolor="#EEEEEE">within 1 week</td>
        <td  width="22.5%" colspan="3" bgcolor="#EEEEEE">2 weeks (optional)</td>
        <td  width="22.5%" colspan="3" bgcolor="#EEEEEE">1 month (optional)</td>
        <td width="22.5%" colspan="3" bgcolor="#EEEEEE">2 months</td>
    </tr>
    <tr align="center">
        <td bgcolor="#EEEEEE"> <b>GROWTH</b> </td>
        <td>Ht.<br><input type="text" readonly="true" name="ht1w" size="4" maxlength="5" value="<%= props.getProperty("ht1w", "") %>"></td>
        <td>Wt.<br><input type="text" readonly="true" name="wt1w" size="4" maxlength="5" value="<%= props.getProperty("wt1w", "") %>"></td>
        <td>Hd. Circ av. 35cm<br><input type="text" readonly="true" name="hc1w" size="4" maxlength="5" value="<%= props.getProperty("hc1w", "") %>"></td>
        <td>Ht. <br><input type="text" readonly="true" name="ht2w" size="4" maxlength="5" value="<%= props.getProperty("ht2w", "") %>"></td>
        <td>Wt. <br><input type="text" readonly="true" name="wt2w" size="4" maxlength="5" value="<%= props.getProperty("wt2w", "") %>"></td>
        <td>Hd. Circ <br><input type="text" readonly="true" name="hc2w" size="4" maxlength="5" value="<%= props.getProperty("hc2w", "") %>"></td>
        <td>Ht. <br><input type="text" readonly="true" name="ht1m" size="4" maxlength="5" value="<%= props.getProperty("ht1m", "") %>"></td>
        <td>Wt. <br><input type="text" readonly="true" name="wt1m" size="4" maxlength="5" value="<%= props.getProperty("wt1m", "") %>"></td>
        <td>Hd. Circ <br><input type="text" readonly="true" name="hc1m" size="4" maxlength="5" value="<%= props.getProperty("hc1m", "") %>"></td>
        <td>Ht. <br><input type="text" readonly="true" name="ht2m" size="4" maxlength="5" value="<%= props.getProperty("ht2m", "") %>"></td>
        <td>Wt. <br><input type="text" readonly="true" name="wt2m" size="4" maxlength="5" value="<%= props.getProperty("wt2m", "") %>"></td>
        <td width="7%">Hd. Circ <br><input type="text" readonly="true" name="hc2m" size="4" maxlength="5" value="<%= props.getProperty("hc2m", "") %>"></td>
    </tr>
    <tr align="center">
        <td bgcolor="#EEEEEE"> <b>PARENTAL CONCERNS</b> </td>
        <td colspan="3">
            <textarea readonly="true" name="pConcern1w" style="width:100%" cols="10" rows="2"><%= props.getProperty("pConcern1w", "") %></textarea>
        </td>
        <td colspan="3">
            <textarea readonly="true" name="pConcern2w" style="width:100%" cols="10" rows="2"><%= props.getProperty("pConcern2w", "") %></textarea>
        </td>
        <td colspan="3">
            <textarea readonly="true" name="pConcern1m" style="width:100%" cols="10" rows="2"><%= props.getProperty("pConcern1m", "") %></textarea>
        </td>
        <td colspan="3">
            <textarea readonly="true" name="pConcern2m" style="width:100%" cols="10" rows="2"><%= props.getProperty("pConcern2m", "") %></textarea>
        </td>
    </tr>
    <tr>
        <td bgcolor="#EEEEEE">
            <div align="center"><b>NUTRITION</b>:</div>
        </td>
        <td colspan="3" valign="top">
            <%= checkMarks(props.getProperty("breastFeeding1w", "")) %>
            <b>Breast feeding* Vit.D 10ug=400IU/day*</b><br>
            <%= checkMarks(props.getProperty("formulaFeeding1w", "")) %>
            <i>Formula Feeding</i> (Fe fortified) <br>[150ml = 5oz/kg/day]<br>
            <%= checkMarks(props.getProperty("stoolUrine1w", "")) %>
            Stool pattern &amp; urine output
        </td>
        <td colspan="3" valign="top">
            <%= checkMarks(props.getProperty("breastFeeding2w", "")) %>
            <b>Breast feeding* Vit.D 10ug=400IU/day*</b><br>
            <%= checkMarks(props.getProperty("formulaFeeding2w", "")) %>
            <i>Formula Feeding</i> (Fe fortified) <br>[150ml = 5oz/kg/day]<br>
            <%= checkMarks(props.getProperty("stoolUrine2w", "")) %>
            Stool pattern &amp; urine output
        </td>
        <td colspan="3" valign="top">
            <%= checkMarks(props.getProperty("breastFeeding1m", "")) %>
            <b>Breast feeding* Vit.D 10ug=400IU/day*</b><br>
            <%= checkMarks(props.getProperty("formulaFeeding1m", "")) %>
            <i>Formula Feeding</i> (Fe fortified) <br>
            <%= checkMarks(props.getProperty("stoolUrine1m", "")) %>
            Stool pattern &amp; urine output </td>
        <td colspan="3" valign="top">
            <%= checkMarks(props.getProperty("breastFeeding2m", "")) %>
            <b>Breast feeding* Vit.D 10ug=400IU/day*</b><br>
            <%= checkMarks(props.getProperty("formulaFeeding2m", "")) %>
            <i>Formula Feeding</i> (Fe fortified)
        </td>
    </tr>
    <tr>
        <td bgcolor="#EEEEEE">
            <p align="center"><b>EDUCATION &amp; ADVICE</b></p>
            <p align="center"><b>Safety</b></p>
            <p align="center"><b>Behaviour</b></p>
            <p align="center"><b>Family</b></p>
            <p align="center"><b>Other</b></p>
        </td>
        <td colspan="3" valign="top">
            <p>
                <%= checkMarks(props.getProperty("carSeat1w", "")) %>
                <b>Car seat (infant)*</b><br>
                <%= checkMarks(props.getProperty("cribSafety1w", "")) %>
                Crib safety
            </p>
            <p>
                <%= checkMarks(props.getProperty("sleeping1w", "")) %>
                Sleeping/crying<br>
                <%= checkMarks(props.getProperty("sooth1w", "")) %>
                Soothability/ responsiveness <br>
                <%= checkMarks(props.getProperty("bonding1w", "")) %>
                Parenting/bonding <br>
                <%= checkMarks(props.getProperty("fatigue1w", "")) %>
                Fatigue/depression <br>
                Family conflict/stress<br>
                <%= checkMarks(props.getProperty("siblings1w", "")) %>
                Siblings <br>
                <%= checkMarks(props.getProperty("homeVisit1w", "")) %>
                <b> Assess home visit need*</b> <br>
                <%= checkMarks(props.getProperty("sleepPos1w", "")) %>
                <b>Sleep position* </b><br>
                <%= checkMarks(props.getProperty("temp1w", "")) %>
                <i>Temperature control &amp; overdressing</i><br>
                <%= checkMarks(props.getProperty("smoke1w", "")) %>
                <b>Second hand smoke* </b><br>
            </p>
        </td>
        <td colspan="3" valign="top">
            <p>
                <%= checkMarks(props.getProperty("carSeat2w", "")) %>
                <b>Car seat (infant)*</b><br>
                <%= checkMarks(props.getProperty("cribSafety2w", "")) %>
                Crib safety
            </p>
            <p>
                <%= checkMarks(props.getProperty("sleeping2w", "")) %>
                Sleeping/crying<br>
                <%= checkMarks(props.getProperty("sooth2w", "")) %>
                Soothability/ responsiveness <br>
                <%= checkMarks(props.getProperty("bonding2w", "")) %>
                Parenting/bonding <br>
                <%= checkMarks(props.getProperty("fatigue2w", "")) %>
                Fatigue/depression <br>
                Family conflict/stress<br>
                <%= checkMarks(props.getProperty("siblings2w", "")) %>
                Siblings <br>
                <%= checkMarks(props.getProperty("homeVisit2w", "")) %>
                <b> Assess home visit need*</b> <br>
                <%= checkMarks(props.getProperty("sleepPos2w", "")) %>
                <b>Sleep position* </b><br>
                <%= checkMarks(props.getProperty("temp2w", "")) %>
                <i>Temperature control &amp; overdressing</i><br>
                <%= checkMarks(props.getProperty("smoke2w", "")) %>
                <b>Second hand smoke* </b><br>
            </p>
        </td>
        <td colspan="3" valign="top">
            <p>
                <%= checkMarks(props.getProperty("carbonMonoxide1m", "")) %>
                Carbon monoxide/<b><br>
                </b><i>Smoke detectors*</i><br>
                <%= checkMarks(props.getProperty("sleepwear1m", "")) %>
                <i> Non-inflam. sleepwear</i> <br>
                <%= checkMarks(props.getProperty("hotWater1m", "")) %>
                <i>Hot water &lt; 54 &deg;C*</i><br>
                <%= checkMarks(props.getProperty("toys1m", "")) %>
                Choking/safe toys*<br>
                <%= checkMarks(props.getProperty("crying1m", "")) %>
                Sleep/crying<br>
                <%= checkMarks(props.getProperty("sooth1m", "")) %>
                Soothability/ responsiveness <br>
                <%= checkMarks(props.getProperty("interaction1m", "")) %>
                Parent/child interaction <br>
                <%= checkMarks(props.getProperty("supports1m", "")) %>
                Assess supports
            </p>
        </td>
        <td colspan="3" valign="top">
            <p>
                <%= checkMarks(props.getProperty("falls2m", "")) %>
                <i>Falls*</i><br>
                <%= checkMarks(props.getProperty("toys2m", "")) %>
                Choking/safe toys*</p>
            <p>
                <%= checkMarks(props.getProperty("crying2m", "")) %>
                Sleep/crying<br>
                <%= checkMarks(props.getProperty("sooth2m", "")) %>
                Soothability/ responsiveness <br>
                <%= checkMarks(props.getProperty("interaction2m", "")) %>
                Parent/child interaction <br>
                <%= checkMarks(props.getProperty("stress2m", "")) %>
                Depression/family stress </p>
            <p>&nbsp;</p>
            <p>&nbsp;</p>
            <p>
                <%= checkMarks(props.getProperty("fever2m", "")) %>
                Fever control
            </p>
        </td>
    </tr>
    <tr>
        <td bgcolor="#EEEEEE">
            <div align="center"><b>DEVELOPMENT</b><br>
                (Inquiry &amp; observation of milestones)<br>
                Tasks are set after the time of normal milestone acquisition.<br>
                Absence of any item suggests the need for further assessment of development<br>
            </div>
        </td>
        <td colspan="3">&nbsp;</td>
        <td colspan="3">&nbsp;</td>
        <td colspan="3" valign="top">
            <%= checkMarks(props.getProperty("focusGaze1m", "")) %>
            Focuses gaze<br>
            <%= checkMarks(props.getProperty("startles1m", "")) %>
            Startles to loud or sudden noise<br>
            <%= checkMarks(props.getProperty("sucks1m", "")) %>
            Sucks well on nipple<br>
            <%= checkMarks(props.getProperty("noParentsConcerns1m", "")) %>
            No parent concerns
        </td>
        <td colspan="3" valign="top">
            <%= checkMarks(props.getProperty("followMoves2m", "")) %>
            Follows movement with eyes<br>
            <%= checkMarks(props.getProperty("sounds2m", "")) %>
            Has a variety of sounds &amp; cries<br>
            <%= checkMarks(props.getProperty("headUp2m", "")) %>
            Holds head up when held at adult&#146;s shoulder<br>
            <%= checkMarks(props.getProperty("cuddled2m", "")) %>
            Enjoys being touched &amp; cuddled<br>
            <%= checkMarks(props.getProperty("noParentConcerns2m", "")) %>
            No parent concerns
        </td>
    </tr>
    <tr>
        <td bgcolor="#EEEEEE">
            <div align="center"><b>PHYSICAL EXAMINATION</b><br>
                Evidence based screening for specific conditions is highlighted, but an
                appropriate age-specific focused physical examination is recommended at
                each visit
            </div>
        </td>
        <td colspan="3" valign="top">
            <p>
                <%= checkMarks(props.getProperty("skin1w", "")) %>
                <i>Skin (jaundice, dry)</i><br>
                <%= checkMarks(props.getProperty("fontanelles1w", "")) %>
                Fontanelles<br>
                <%= checkMarks(props.getProperty("eyes1w", "")) %>
                <i>Eyes (red reflex)</i><br>
                <%= checkMarks(props.getProperty("ears1w", "")) %>
                <i>Ears (drums)</i><br>
                <%= checkMarks(props.getProperty("heartLungs1w", "")) %>
                Heart/Lungs<br>
                <%= checkMarks(props.getProperty("umbilicus1w", "")) %>
                Umbilicus<br>
                <%= checkMarks(props.getProperty("femoralPulses1w", "")) %>
                Femoral pulses <br>
                <%= checkMarks(props.getProperty("hips1w", "")) %>
                <b>Hips</b> <br>
                <%= checkMarks(props.getProperty("testicles1w", "")) %>
                Testicles<br>
                <%= checkMarks(props.getProperty("maleUrinary1w", "")) %>
                Male urinary stream/foreskin care
            </p>
            <p>&nbsp; </p>
        </td>
        <td colspan="3" valign="top">
            <p>
                <%= checkMarks(props.getProperty("skin2w", "")) %>
                <i>Skin (jaundice, dry)</i><br>
                <%= checkMarks(props.getProperty("fontanelles2w", "")) %>
                Fontanelles<br>
                <%= checkMarks(props.getProperty("eyes2w", "")) %>
                <i>Eyes (red reflex)</i><br>
                <%= checkMarks(props.getProperty("ears2w", "")) %>
                <i>Ears (drums)</i><br>
                <%= checkMarks(props.getProperty("heartLungs2w", "")) %>
                Heart/Lungs<br>
                <%= checkMarks(props.getProperty("umbilicus2w", "")) %>
                Umbilicus<br>
                <%= checkMarks(props.getProperty("femoralPulses2w", "")) %>
                Femoral pulses <br>
                <%= checkMarks(props.getProperty("hips2w", "")) %>
                <b>Hips</b> <br>
                <%= checkMarks(props.getProperty("testicles2w", "")) %>
                Testicles<br>
                <%= checkMarks(props.getProperty("maleUrinary2w", "")) %>
                Male urinary stream/foreskin care
            </p>
            <p>&nbsp; </p>
        </td>
        <td colspan="3" valign="top">
            <%= checkMarks(props.getProperty("fontanelles1m", "")) %>
            Fontanelles<br>
            <%= checkMarks(props.getProperty("eyes1m", "")) %>
            <i>Eyes (red reflex)<br>
            <%= checkMarks(props.getProperty("cover1m", "")) %>
            </i><b> Cover/uncover test &amp; inquiry*</b><br>
            <%= checkMarks(props.getProperty("hearing1m", "")) %>
            <b> Hearing inquiry</b><br>
            <%= checkMarks(props.getProperty("heart1m", "")) %>
            Heart<br>
            <%= checkMarks(props.getProperty("hips1m", "")) %>
            <b>Hips </b>
        </td>
        <td colspan="3" valign="top">
            <%= checkMarks(props.getProperty("fontanelles2m", "")) %>
            Fontanelles<br>
            <%= checkMarks(props.getProperty("eyes2m", "")) %>
            <i>Eyes (red reflex)<br>
            <%= checkMarks(props.getProperty("cover2m", "")) %>
            </i><b> Cover/uncover test &amp; inquiry*</b><br>
            <%= checkMarks(props.getProperty("hearing2m", "")) %>
            <b> Hearing inquiry</b><br>
            <%= checkMarks(props.getProperty("heart2m", "")) %>
            Heart<br>
            <%= checkMarks(props.getProperty("hips2m", "")) %>
            <b>Hips </b>
        </td>
    </tr>
    <tr>
        <td bgcolor="#EEEEEE">
            <div align="center"><b>PROBLEMS &amp; PLANS</b></div>
        </td>
        <td colspan="3">
            <%= checkMarks(props.getProperty("pkuThyroid1w", "")) %>
            <b> PKU, Thyroid</b><br>
            <%= checkMarks(props.getProperty("hemoScreen1w", "")) %>
            <b>Hemoglobinopathy Screen (if at risk)*</b>
        </td>
        <td colspan="3">&nbsp;</td>
        <td colspan="3">&nbsp;</td>
        <td colspan="3">&nbsp;</td>
    </tr>
    <tr>
        <td bgcolor="#EEEEEE">
            <div align="center"><b>IMMUNIZATION</b><br>
                Guidelines may vary by province
            </div>
        </td>
        <td colspan="3" valign="top">If HBsAg-positive parent or sibling:<br>
            <%= checkMarks(props.getProperty("hepB1w", "")) %>
            <b> Hep. B vaccine* </b></td>
        <td colspan="3">&nbsp;</td>
        <td colspan="3" valign="top">Give information:<br>
            <%= checkMarks(props.getProperty("immunization1m", "")) %>
            Immunization<br>
            <%= checkMarks(props.getProperty("acetaminophen1m", "")) %>
            Acetaminophen<br>
            If HBsAg-positive parent or sibling:<br>
            <%= checkMarks(props.getProperty("hepB1m", "")) %>
            <b>Hep. B vaccine* </b>
        </td>
        <td colspan="3">
            <p>
                <%= checkMarks(props.getProperty("acetaminophen2m", "")) %>
                Acetaminophen
            </p>
            <p>
                <%= checkMarks(props.getProperty("hib2m", "")) %>
                <b>HIB</b><br>
                <%= checkMarks(props.getProperty("polio2m", "")) %>
                <b> aPDT polio </b>
            </p>
        </td>
    </tr>
    <tr>
        <td bgcolor="#EEEEEE">
            <div align="center"><b>Signature</b></div>
        </td>
        <td colspan="3">&nbsp;
            <input type="hidden" name="signature1w" value="<%= props.getProperty("signature1w", "") %>" />
        </td>
        <td colspan="3">&nbsp;
            <input type="hidden" name="signature2w" value="<%= props.getProperty("signature2w", "") %>" />
        </td>
        <td colspan="3">&nbsp;
            <input type="hidden" name="signature1m" value="<%= props.getProperty("signature1m", "") %>" />
        </td>
        <td colspan="3">&nbsp;
            <input type="hidden" name="signature2m" value="<%= props.getProperty("signature2m", "") %>" />
        </td>
    </tr>
</table>
<table>
    <tr>
        <td class="hidePrint">
            <a href="formRourke.jsp?demographic_no=<%=demoNo%>&formId=<%=formId%>">Cancel</a>&nbsp;<a href=# onClick="window.print()">Print</a>
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