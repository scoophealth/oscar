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
    String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
    boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_form" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect(request.getContextPath() + "/securityError.jsp?type=_form");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>

<%
  String user_no = (String) session.getAttribute("user");

  //if bNewForm is false (0), then it should be able to display xml data.
  boolean bNew = true;
  if( request.getParameter("bNewForm")!=null && request.getParameter("bNewForm").compareTo("0")==0 ) 
    bNew = false;
%>
<%@ page import="java.util.*, java.sql.*, oscar.*"
	errorPage="errorpage.jsp"%>

<HTML>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<TITLE>ALPHA print</TITLE>
</HEAD>
<BODY BGCOLOR="#FFFFFF">

<TABLE BORDER CELLSPACING=1 BORDERCOLOR="#000000" CELLPADDING=4
	width="100%">
	<TR>
		<TD VALIGN="TOP" HEIGHT=16 colspan="2">
		<table border="0" width="100%">
			<tr>
				<td ALIGN="CENTER"><FONT FACE="Arial, Helvetica"><B>ANTENATAL
				PSYCHOSOCIAL HEALTH ASSESSMENT (ALPHA)</B></FONT></TD>
				<td align="right"><a href="formALPHAprint.jsp"> prev page </a>
				| <a href=# onClick="window.print();">PRINT</a></td>
			</tr>
		</table>
		</td>
	</TR>
	<TR>
		<TD WIDTH="50%" VALIGN="TOP" ALIGN="CENTER" bgcolor="#eeeeee"><B><FONT
			FACE="Arial" SIZE="-1">ANTENATAL FACTORS</FONT></B></TD>
		<TD WIDTH="50%" VALIGN="TOP" ALIGN="CENTER" bgcolor="#eeeeee"><FONT
			FACE="Arial" SIZE="-1"> <B>COMMENTS/PLAN</B></FONT></TD>
	</TR>
	<TR>
		<TD colspan="2" VALIGN="TOP" bgcolor="#f9f9f9"><FONT FACE="Arial"
			SIZE="-1"> SUBSTANCE USE</FONT><FONT FACE="Arial" SIZE="-1">&nbsp;</FONT></TD>
	</TR>
	<TR>
		<TD WIDTH="50%" VALIGN="TOP"><FONT FACE="Arial" SIZE="-1">
		<B>Alcohol/drug abuse (<I>WA, </I>CA)</B><BR>
		�How many drinks of alcohol do you have per week?<BR>
		�Are there times when you drink more than that?<BR>
		�Do you or your partner use recreational drugs?<BR>
		�Do you or your partner have a problem with alcohol or drugs?<BR>
		�Consider CAGE (<B>C</B>ut down, <B>A</B>nnoyed, <B>G</B>uilty, <B>E</B>ye
		opener)</FONT></TD>
		<TD WIDTH="50%" VALIGN="TOP"><FONT FACE="Arial" SIZE="-1">&nbsp;</FONT></TD>
	</TR>
	<TR>
		<TD colspan="2" VALIGN="TOP" bgcolor="#f9f9f9"><FONT FACE="Arial"
			SIZE="-1"> FAMILY VIOLENCE</FONT><FONT FACE="Arial" SIZE="-1">&nbsp;</FONT></TD>
	</TR>
	<TR>
		<TD WIDTH="50%" VALIGN="TOP"><FONT FACE="Arial" SIZE="-1">
		<B>Woman or partner experienced or witnessed abuse
		(physical,&nbsp;emotional, sexual) (<I>CA, </I>WA)</B><BR>
		�What was your parents' relationship like?<BR>
		�Did your father ever scare or hurt your mother?<BR>
		�Did your parents ever scare or hurt you?<BR>
		�Were you ever sexually abused as a child?</FONT></TD>
		<TD WIDTH="50%" VALIGN="TOP"><FONT FACE="Arial" SIZE="-1">&nbsp;</FONT></TD>
	</TR>
	<TR>
		<TD WIDTH="50%" VALIGN="TOP"><FONT FACE="Arial" SIZE="-1">
		<B>Current or past woman abuse (<I>WA</I>, CA, PD)</B><BR>
		�How do you and your partner solve arguments?<BR>
		�Do you ever feel frightened by what your partner says or does?<BR>
		�Have you ever been hit/pushed/slapped by a partner?<BR>
		�Has your partner ever humiliated you or psychologically abused you in
		other ways?<BR>
		�Have you ever been forced to have sex against your will?</FONT></TD>
		<TD WIDTH="50%" VALIGN="TOP"><FONT FACE="Arial" SIZE="-1">&nbsp;</FONT></TD>
	</TR>
	<TR>
		<TD WIDTH="50%" VALIGN="TOP"><FONT FACE="Arial" SIZE="-1">
		<B>Previous child abuse by woman or partner (<I>CA</I>)</B><BR>
		�Do you/your partner have children not living with you? If so, why?<BR>
		�Have you ever had involvement with a child protection agency (i.e.,
		Children's Aid Society)?</FONT></TD>
		<TD WIDTH="50%" VALIGN="TOP"><FONT FACE="Arial" SIZE="-1">&nbsp;</FONT></TD>
	</TR>
	<TR>
		<TD WIDTH="50%" VALIGN="TOP"><FONT FACE="Arial" SIZE="-1">
		<B>Child discipline (<I>CA</I>)</B><BR>
		�How were you disciplined as a child?<BR>
		�How do you think you will discipline your child?<BR>
		�How do you deal with your kids at home when they misbehave?</FONT></TD>
		<TD WIDTH="50%" VALIGN="TOP"><FONT FACE="Arial" SIZE="-1">&nbsp;</FONT></TD>
	</TR>
	<TR>
		<TD COLSPAN="2" bgcolor="#f9f9f9"><B><FONT FACE="Arial"
			SIZE="-2"> <font size="-1">FOLLOW-UP PLAN:</font></FONT></B></TD>
	</TR>
	<TR>
		<TD colspan="2" VALIGN="TOP"><FONT FACE="Arial" SIZE="-2">
		<table border=0>
			<tr>
				<td><input type="checkbox" name="xml_fu_scbp" value="checked"
					<%=bNew?"":"datafld='xml_fu_scbp'"%>></td>
				<td>Supportive counselling by provider</td>
				<td><input type="checkbox" name="xml_fu_homecare"
					value="checked" <%=bNew?"":"datafld='xml_fu_homecare'"%>></td>
				<td>Homecare</td>
				<td><input type="checkbox" name="xml_fu_awhsc" value="checked"
					<%=bNew?"":"datafld='xml_fu_awhsc'"%>></td>
				<td>Assaulted women's helpline / shelter / counseling</td>
			</tr>
			<tr>
				<td><input type="checkbox" name="xml_fu_apra" value="checked"
					<%=bNew?"":"datafld='xml_fu_apra'"%>></td>
				<td>Additional prenatal appointments</td>
				<td><input type="checkbox" name="xml_fu_pcpsg" value="checked"
					<%=bNew?"":"datafld='xml_fu_pcpsg'"%>></td>
				<td>Parenting classes / parents' support group</td>
				<td><input type="checkbox" name="xml_fu_la" value="checked"
					<%=bNew?"":"datafld='xml_fu_la'"%>></td>
				<td>Legal advice</td>
			</tr>
			<tr>
				<td><input type="checkbox" name="xml_fu_apoa" value="checked"
					<%=bNew?"":"datafld='xml_fu_apoa'"%>></td>
				<td>Additional postpartum appointments</td>
				<td><input type="checkbox" name="xml_fu_atp" value="checked"
					<%=bNew?"":"datafld='xml_fu_atp'"%>></td>
				<td>Addiction treatment programs</td>
				<td><input type="checkbox" name="xml_fu_cas" value="checked"
					<%=bNew?"":"datafld='xml_fu_cas'"%>></td>
				<td>Children's Aid Society</td>
			</tr>
			<tr>
				<td><input type="checkbox" name="xml_fu_awbv" value="checked"
					<%=bNew?"":"datafld='xml_fu_awbv'"%>></td>
				<td>Additional well baby visits</td>
				<td><input type="checkbox" name="xml_fu_scr" value="checked"
					<%=bNew?"":"datafld='xml_fu_scr'"%>></td>
				<td>Smoking cessation resources</td>
				<td><input type="checkbox" name="xml_fu_other1" value="checked"
					<%=bNew?"":"datafld='xml_fu_other1'"%>></td>
				<td>Other: <input type="text" name="xml_fu_other11"
					<%=bNew?"":"datafld='xml_fu_other1'"%>></td>
			</tr>
			<tr>
				<td><input type="checkbox" name="xml_fu_phr" value="checked"
					<%=bNew?"":"datafld='xml_fu_phr'"%>></td>
				<td>Public Health referral</td>
				<td><input type="checkbox" name="xml_fu_sw" value="checked"
					<%=bNew?"":"datafld='xml_fu_sw'"%>></td>
				<td>Social Worker</td>
				<td><input type="checkbox" name="xml_fu_other2" value="checked"
					<%=bNew?"":"datafld='xml_fu_other2'"%>></td>
				<td>Other: <input type="text" name="xml_fu_other21"
					<%=bNew?"":"datafld='xml_fu_other21'"%>></td>
			</tr>
			<tr>
				<td><input type="checkbox" name="xml_fu_pes" value="checked"
					<%=bNew?"":"datafld='xml_fu_pes'"%>></td>
				<td>Prenatal education services</td>
				<td><input type="checkbox" name="xml_fu_pp" value="checked"
					<%=bNew?"":"datafld='xml_fu_pp'"%>></td>
				<td>Psychologist / Psychiatrist</td>
				<td><input type="checkbox" name="xml_fu_other3" value="checked"
					<%=bNew?"":"datafld='xml_fu_other3'"%>></td>
				<td>Other: <input type="text" name="xml_fu_other31"
					<%=bNew?"":"datafld='xml_fu_other31'"%>></td>
			</tr>
			<tr>
				<td><input type="checkbox" name="xml_fu_nutritionist"
					value="checked" <%=bNew?"":"datafld='xml_fu_nutritionist'"%>>
				</td>
				<td>Nutritionist</td>
				<td><input type="checkbox" name="xml_fu_pmft" value="checked"
					<%=bNew?"":"datafld='xml_fu_pmft'"%>></td>
				<td>Psychotherapist / marital / family therapist</td>
				<td><input type="checkbox" name="xml_fu_other4" value="checked"
					<%=bNew?"":"datafld='xml_fu_other4'"%>></td>
				<td>Other: <input type="text" name="xml_fu_other41"
					<%=bNew?"":"datafld='xml_fu_other41'"%>></td>
			</tr>
			<tr>
				<td><input type="checkbox" name="xml_fu_crmg" value="checked"
					<%=bNew?"":"datafld='xml_fu_crmg'"%>></td>
				<td>Community resources / mothers' group</td>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
			</tr>
		</table>

		</FONT></TD>
	</TR>
	<TR>
		<TD VALIGN="TOP" COLSPAN=2>
		<p><FONT FACE="Arial" SIZE="-1"> <B>COMMENTS:<BR>
		</B></FONT></p>
		<p>&nbsp;</p>
		<BR>
		</td>
	</tr>
	<tr>
		<td>Date
		Completed&nbsp;:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		</td>
		<td>Signature :</td>
	</tr>
</table>
<table border="0">
	<tr>
		<td><FONT FACE="Arial" SIZE="-1">
		<P><FONT SIZE="-2">Copyright &copy; ALPHA Project 1993
		Version: May 1998<BR>
		*The ALPHA Guide is available through the Department of Family and
		Community Medicine, University of Toronto.</FONT>
		</FONT></TD>
	</TR>
</TABLE>
<P>
</BODY>
</HTML>
