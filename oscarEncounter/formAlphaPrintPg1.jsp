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

<%@ page import="java.util.*, java.sql.*, oscar.*"  %>
<%@ page import="oscar.oscarEncounter.data.EctAlphaRecord" %>

<HTML>
<HEAD>
<TITLE> ALPHA print pg1</TITLE>
<link rel="stylesheet" href="alphaStyle.css" >

<style media="print">
.hidePrint
{
    display: none;
}
</style>

</HEAD>


<%
    int demoNo = Integer.parseInt(request.getParameter("demoNo"));
    int formId = Integer.parseInt(request.getParameter("formId"));
    EctAlphaRecord rec = new EctAlphaRecord();
    java.util.Properties props = rec.getAlphaRecord(demoNo, formId);
%>


<BODY BGCOLOR="#FFFFFF">

<TABLE BORDER CELLSPACING=1 BORDERCOLOR="#000000" CELLPADDING=4>
<TR><TD VALIGN="TOP"HEIGHT=16 colspan="3">
<table border="0" width="100%"><tr><th ALIGN="CENTER">
<FONT FACE="Arial, Helvetica"><B>ANTENATAL PSYCHOSOCIAL HEALTH ASSESSMENT (ALPHA)</B></FONT></TH>
<td align="right" class="hidePrint">
    <a href="formAlphaPrintPg2.jsp?demoNo=<%=demoNo%>&formId=<%=formId%>"> next page </a> | <a href=# onClick="window.print();">PRINT</a> | <a href="formAlpha.jsp?demographic_no=<%=demoNo%>&formId=<%=formId%>">Cancel</a>
</td></tr></table>
</td></TR>
<TR>
    <TD WIDTH="92%" VALIGN="TOP" ALIGN="JUSTIFY" colspan="2"> <font face="Arial, Helvetica" size="-2">Antenatal
      psychosocial problems may be associated with unfavourable postpartum outcomes.
      The questions on this form are suggested ways of enquiring about psychosocial
      health.<BR>
      Issues of <B>high</B> concern to the woman, her family or the caregiver
      usually indicate a need for additional supports or services. When <B>some</B>
      concerns are identified, follow-up and/or referral should be considered.
      Additional information can be obtained from the ALPHA Guide.*<BR>
      <I>Please consider the sensitivity of this information before sharing it
      with other caregivers.</I></font></TD>
<TD WIDTH="8%" VALIGN="TOP">
<FONT FACE="Arial, Helvetica" Size="-1">Addressograph</FONT></TD>
</TR>

  <TR bgcolor="#eeeeee">
    <TD WIDTH="50%" VALIGN="TOP" ALIGN="CENTER" bgcolor="#eeeeee"> <font face="Arial, Helvetica" size="-1"><B>ANTENATAL
      FACTORS</B></font></TD>
    <TD  WIDTH="50%" VALIGN="TOP" ALIGN="CENTER" colspan="2"> <font face="Arial, Helvetica" size="-1"><B>COMMENTS/PLAN</B></font></TD>
  </TR>
  <TR bgcolor="#f9f9f9">
    <TD VALIGN="TOP" colspan="3"> <FONT FACE="Arial, Helvetica" Size="-1">FAMILY
      FACTORS</FONT></TD>
  </TR>
  <TR>
    <TD WIDTH="50%" VALIGN="TOP"> <FONT FACE="Arial, Helvetica" Size="-1"><B>Social
      support</B></FONT> (<B><I>CA, WA,</I></B> <FONT SIZE="-1">PD</FONT>)<BR>
      <FONT SIZE="-1" FACE="Arial, Helvetica">•How does your partner/family feel
      about your pregnancy?</FONT><BR>
      <FONT SIZE="-1" FACE="Arial, Helvetica">•Who will be helping you when you
      go home with your baby?</FONT> </TD>
    <TD VALIGN="TOP" WIDTH="50%" colspan="2"> <%= props.getProperty("socialSupport", "") %>&nbsp;</TD>
  </TR>
  <TR>
    <TD WIDTH="50%" VALIGN="TOP"> <B><FONT Size="-1" FACE="Arial, Helvetica">Recent
      stressful life events</FONT></B> (<B><I>CA, WA, PD,</I></B> <FONT SIZE="-1">PI</FONT>)<BR>
      <FONT SIZE="-1" FACE="Arial, Helvetica">•What life changes have you experiences
      this year?</FONT><BR>
      <FONT SIZE="-1" FACE="Arial, Helvetica">•What changes are you planning during
      this pregnancy?</FONT></TD>
    <TD VALIGN="TOP" colspan="2"> <%= props.getProperty("lifeEvents", "") %>&nbsp;</TD>
  </TR>
  <TR>
    <TD WIDTH="50%" VALIGN="TOP"> <B><FONT SIZE="-1" FACE="Arial, Helvetica">Couple's
      relationship</FONT></B> (<B><I>CD, PD,</I></B> <FONT SIZE="-1">WA, CA</FONT>)<BR>
      <FONT SIZE="-1" FACE="Arial, Helvetica">•How would you describe your relationship
      with your partner?</FONT><BR>
      <FONT SIZE="-1" FACE="Arial, Helvetica">•What do you think your relationship
      will be like after the birth?</FONT></TD>
    <TD VALIGN="TOP" colspan="2"> <%= props.getProperty("coupleRelationship", "") %>&nbsp;</TD>
  </TR>
  <TR bgcolor="#f9f9f9">
    <TD VALIGN="TOP"> <FONT FACE="Arial, Helvetica" Size="-1">MATERNAL
      FACTORS</FONT></TD>
  </TR>
  <TR>
    <TD WIDTH="50%" VALIGN="TOP"> <FONT FACE="Arial, Helvetica" Size="-1"><B>Prenatal
      care (late onset)</B></FONT> (<B><I>WA</I></B>)<BR>
      <FONT SIZE="-1" FACE="Arial, Helvetica">•First prenatal visit in third trimester?
      (check records)</FONT></TD>
    <TD VALIGN="TOP" colspan="2"> <%= props.getProperty("prenatalCare", "") %>&nbsp;</TD>
  </TR>
  <TR>
    <TD WIDTH="50%" VALIGN="TOP"> <B><FONT FACE="Arial, Helvetica" Size="-1">Prenatal
      education (refusal or quit)</FONT></B> (<B><I>CA</I></B>)<BR>
      <FONT SIZE="-1" FACE="Arial, Helvetica">•What are your plans for prenatal
      classes?</FONT></TD>
    <TD VALIGN="TOP" colspan="2"> <%= props.getProperty("prenatalEducation", "") %>&nbsp;</TD>
  </TR>
  <TR>
    <TD WIDTH="50%" VALIGN="TOP"> <B><FONT FACE="Arial, Helvetica" Size="-1">Feelings
      toward pregnancy after 20 weeks</FONT></B> (<B><I>CA, WA</I></B>)<BR>
      <FONT SIZE="-1" FACE="Arial, Helvetica">•How did you feel when you just
      found out you were pregnant?</FONT><BR>
      <FONT SIZE="-1" FACE="Arial, Helvetica">•How do you feel about it now?</FONT></TD>
    <TD VALIGN="TOP" colspan="2"> <%= props.getProperty("feelingsRePregnancy", "") %>&nbsp;</TD>
  </TR>
  <TR>
    <TD WIDTH="50%" VALIGN="TOP"> <B><FONT FACE="Arial, Helvetica" Size="-1">Relationship
      with parents in childhood</FONT></B> (<B><I>CA</I></B>)<BR>
      <FONT SIZE="-1" FACE="Arial, Helvetica">•How did you get along with your
      parents?</FONT><BR>
      <FONT SIZE="-1" FACE="Arial, Helvetica">•Did you feel loved by your parents?</FONT></TD>
    <TD VALIGN="TOP" colspan="2"> <%= props.getProperty("relationshipParents", "") %>&nbsp;</TD>
  </TR>
  <TR>
    <TD WIDTH="50%" VALIGN="TOP"> <B><FONT FACE="Arial, Helvetica" Size="-1">Self-esteem</FONT></B>
      (<B><I>CA,</I></B> <FONT SIZE="-1">WA</FONT>)<BR>
      <FONT SIZE="-1" FACE="Arial, Helvetica">•What concerns do you have about
      becoming/being a mother?</FONT></TD>
    <TD VALIGN="TOP" colspan="2"> <%= props.getProperty("selfEsteem", "") %>&nbsp;</TD>
  </TR>
  <TR>
    <TD WIDTH="50%" VALIGN="TOP"> <B><FONT FACE="Arial, Helvetica" Size="-1">History
      of psychiatric/emotional problems</FONT></B> (<B><I>CA, WA,</I></B> <FONT SIZE="-1">PD</FONT>)<BR>
      <FONT SIZE="-1" FACE="Arial, Helvetica">•Have you ever had emotional problems?</FONT><BR>
      •<FONT SIZE="-1" FACE="Arial, Helvetica">Have you ever seen a psychiatrist
      or therapist?</FONT></TD>
    <TD VALIGN="TOP" colspan="2"> <%= props.getProperty("psychHistory", "") %>&nbsp;</TD>
  </TR>
  <TR>
    <TD WIDTH="50%" VALIGN="TOP"> <B><FONT FACE="Arial, Helvetica" Size="-1">Depression
      in this pregnancy</FONT></B> (<B><I>PD</I></B>)<BR>
      <FONT SIZE="-1" FACE="Arial, Helvetica">•How has your mood been during this
      pregnancy?</FONT></TD>
    <TD VALIGN="TOP" colspan="2"> <%= props.getProperty("depression", "") %>&nbsp;</TD>
  </TR>
  <TR>
    <TD VALIGN="TOP" ALIGN="CENTER" COLSPAN=2><font face="Arial, Helvetica" size="-1"><b>ASSOCIATED
      POSTPARTUM OUTCOMES</b><br>
      The antenatal factors in the left column have been shown to be associated
      with the postpartum outcomes listed below. <b><i>Bold, italics</i></b> indicates
      <b><i>good</i></b> evidence of association. Regular text indicates fair
      evidence of association. <b>CA</b>&nbsp;&#150;&nbsp;Child Abuse <b>CD</b>&nbsp;&#150;&nbsp;Couple
      Dysfunction <b>PI</b>&nbsp;&#150;&nbsp;Physical Illness <b>PD</b>&nbsp;&#150;&nbsp;Postpartum
      Depression <b>WA</b>&nbsp;&#150;&nbsp;Woman Abuse</font></TD>
  </TR>
</TABLE>

</BODY>
</HTML>

