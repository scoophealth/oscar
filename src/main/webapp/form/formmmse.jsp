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

<%@ page import="oscar.form.*"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@page import="org.oscarehr.util.LoggedInInfo"%>

<html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>Mini Mental State Exam</title>
<html:base />
<link rel="stylesheet" type="text/css" href="mmseStyle.css" />
<link rel="stylesheet" type="text/css" media="print" href="print.css" />
</head>

<%
	String formClass = "MMSE";
	String formLink = "formmmse.jsp";

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
        var ret = confirm("Are you sure you want to save this form?");
        
        return ret;
    }
    
    function onSaveExit() {
        document.forms[0].submit.value="exit";
        var ret = confirm("Are you sure you wish to save and close this window?");
        
        return ret;
    }


    function popup(link) {
    windowprops = "height=700, width=960,location=no,"
    + "scrollbars=yes, menubars=no, toolbars=no, resizable=no, top=0, left=0 titlebar=yes";
    window.open(link, "_blank", windowprops);
}
</script>


<body bgproperties="fixed" topmargin="0" leftmargin="0" rightmargin="0">
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
	<input type="hidden" name="provNo"
		value="<%= request.getParameter("provNo") %>" />
	<input type="hidden" name="submit" value="exit" />

	<table class="Head" class="hidePrint">
		<tr>
			<td nowrap="true"><input type="submit" value="Save"
				onclick="javascript:return onSave();" /> <input type="submit"
				value="Save and Exit" onclick="javascript:return onSaveExit();" /> <input
				type="submit" value="Exit" onclick="javascript:return onExit();" />
			<input type="button" value="Print"
				onclick="javascript:return onPrint();" /></td>
		</tr>
	</table>

	<!-- class="TableWithBorder" -->
	<table width="100%">
		<tr>
			<td class="title" colspan="2" align="center">MINI - MENTAL STATE
			EXAM</td>
		</tr>
		<tr>
			<td colspan="2" align="center">
			<table width="80%">
				<tr>
					<td>Name: <input type="hidden" name="pName"
						value="<%=props.getProperty("pName", "")%>" /> <%=props.getProperty("pName", "")%>
					</td>
					<td>Age: <input type="hidden" name="age"
						value="<%=props.getProperty("age", "")%>" /> <%=props.getProperty("age", "")%>
					</td>
					<td>Sex: <input type="hidden" name="sex"
						value="<%=props.getProperty("sex", "")%>" /> <%=props.getProperty("sex", "")%>
					</td>
					<td>Date: <input type="hidden" size="10" name="formDate"
						value="<%=props.getProperty("formDate", "")%>" /> <%=props.getProperty("formDate", "")%>
					</td>
				</tr>
			</table>
			</td>
		</tr>
		<tr>
			<td align="center"><b>Diagnosis:</b></td>
			<td align="center"><b>Medications:</b></td>
		</tr>
		<tr>
			<td align="center"><textarea name="diagnosis"
				style="height: 75px; width: 95%"><%=props.getProperty("diagnosis", "")%></textarea>
			</td>
			<td align="center"><textarea name="meds"
				style="height: 75px; width: 95%"><%=props.getProperty("meds", "")%></textarea>
			</td>
		</tr>
		<tr>
			<td width="100%" colspan="2">
			<table border="2" cellpadding="5">
				<tr>
					<td width="50%">
					<table border="0">
						<tr>
							<td class="score">SCORE</td>
							<td class="max">MAX</td>
							<td class="title" width="100%">ORIENTATION</td>
						</tr>
						<tr>
							<td class="score"><input type="text" size="1" name="o_date"
								value="<%=props.getProperty("o_date", "")%>" /></td>
							<td class="score">5</td>
							<td>Name: year, season, month, date, day<br>
							<i>("What is the year...?")</i></td>
						</tr>
						<tr>
							<td class="score"><input type="text" size="1" name="o_place"
								value="<%=props.getProperty("o_place", "")%>" /></td>
							<td class="score">5</td>
							<td>Name: country, province, city, hospital, floor<br>
							<i>("Where are we? What country...?")</i></td>
						</tr>
						<tr>
							<td class="score">&nbsp;</td>
							<td class="score">&nbsp;</td>
							<td class="title">REGISTRATION:</td>
						</tr>
						<tr>
							<td class="score"><input type="text" size="1"
								name="r_objects" value="<%=props.getProperty("r_objects", "")%>" /></td>
							<td class="score">3</td>
							<td>Name 3 objects (1 sec. for each).<br>
							Ask patient to repeat all 3.<br>
							Score 1 pt. for each right answer at first try.<br>
							(Repeat up to 6 times until all are learned)</td>
						</tr>
						<tr>
							<td class="score">&nbsp;</td>
							<td class="score">&nbsp;</td>
							<td class="title">ATTENTION & CALCULATION:</td>
						</tr>
						<tr>
							<td class="score"><input type="text" size="1"
								name="a_serial" value="<%=props.getProperty("a_serial", "")%>" /></td>
							</td>
							<td class="score">5</td>
							<td>Serial 7's: 100 - 93 - 86 - 79 - 72 - 65<br>
							Score 1 pt. for each right answer. Stop at 5.<br>
							OR<br>
							Spell "WORLD" backwards.<br>
							Score 1 pt. for each right answer, until first error.</td>
						</tr>
						<tr>
							<td class="score">&nbsp;</td>
							<td class="score">&nbsp;</td>
							<td class="title">RECALL:</td>
						</tr>
						<tr>
							<td class="score"><input type="text" size="1" name="re_name"
								value="<%=props.getProperty("re_name", "")%>" /></td>
							<td class="score">3</td>
							<td>Name the 3 objects learned above.<br>
							Score 1 pt. for each right answer.</td>
						</tr>
						<tr>
							<td class="score">&nbsp;</td>
							<td class="score">&nbsp;</td>
							<td class="title">LANGUAGE TESTS:</td>
						</tr>
						<tr>
							<td class="score"><input type="text" size="1" name="l_name"
								value="<%=props.getProperty("l_name", "")%>" /></td>
							<td class="score">2</td>
							<td>Name: PENCIL, WATCH</td>
						</tr>
						<tr>
							<td class="score"><input type="text" size="1"
								name="l_repeat" value="<%=props.getProperty("l_repeat", "")%>" /></td>
							<td class="score">1</td>
							<td>Repeat: <i>"NO IFS AND OR BUTS"</i></td>
						</tr>
						<tr>
							<td class="score"><input type="text" size="1"
								name="l_follow" value="<%=props.getProperty("l_follow", "")%>" /></td>
							<td class="score">3</td>
							<td>Follow a 3 stage command:<br>
							<i>"Take the paper in your right hand;<br>
							fold it in half; and put it on the floor.</i></td>
						</tr>
						<tr>
							<td class="score"><input type="text" size="1" name="l_read"
								value="<%=props.getProperty("l_read", "")%>" /></td>
							<td class="score">1</td>
							<td>Read & obey: <i>"CLOSE YOUR EYES"</i></td>
						</tr>
						<tr>
							<td class="score"><input type="text" size="1" name="l_write"
								value="<%=props.getProperty("l_write", "")%>" /></td>
							<td class="score">1</td>
							<td>Write a sentence.</td>
						</tr>
						<tr>
							<td class="score"><input type="text" size="1" name="l_copy"
								value="<%=props.getProperty("l_copy", "")%>" /></td>
							<td class="score">1</td>
							<td>Copy the design.</td>
						</tr>
						<tr>
							<td class="score"><input type="text" size="1" name="total"
								value="<%=props.getProperty("total", "")%>" /></td>
							<td class="score"><b>30</b></td>
							<td><b>TOTAL</b></td>
						</tr>
						<tr>
							<td class="score">&nbsp;</td>
							<td class="score">&nbsp;</td>
							<td class="title">LEVEL OF CONSCIOUSNESS:<br>
							<table width="100%">
								<tr>
									<td width="25%">Alert</td>
									<td width="25%">Drowsy</td>
									<td width="25%">Stupor</td>
									<td width="25%">Coma</td>
								</tr>
								<tr>
									<td><input type="checkbox" name="lc_alert"
										<%=props.getProperty("lc_alert", "")%> /></td>
									<td><input type="checkbox" name="lc_drowsy"
										<%=props.getProperty("lc_drowsy", "")%> /></td>
									<td><input type="checkbox" name="lc_stupor"
										<%=props.getProperty("lc_stupor", "")%> /></td>
									<td><input type="checkbox" name="lc_coma"
										<%=props.getProperty("lc_coma", "")%> /></td>
								</tr>
							</table>
							</td>
						</tr>
						<tr>
							<td class="score" colspan="2"><u><i>Interpretation:</i></u>
							</td>
							<td>
							<table>
								<tr>
									<td><input type="checkbox" name="i_dementia"
										<%=props.getProperty("i_dementia", "")%> /></td>
									<td width="25%">0 - 14</td>
									<td>Dementia</td>
								</tr>
								<tr>
									<td><input type="checkbox" name="i_depression"
										<%=props.getProperty("i_depression", "")%> /></td>
									<td>15 - 23</td>
									<td>Depression/cognitive loss</td>
								</tr>
								<td><input type="checkbox" name="i_normal"
									<%=props.getProperty("i_normal", "")%> /></td>
								<td>24 - 30</td>
								<td>Normal</td>
								</tr>
							</table>
							</td>
						</tr>
					</table>
					</td>
					<td valign="top">
					<table class="description">
						<tr>
							<td align="center"><b>INSTRUCTIONS<br>
							FOR ADMINISTRATION OF<br>
							THE MINI - MENTAL STATE</b></td>
						</tr>
						<tr>
							<td><a>Orientation:</a><br>
							1. Ask for the date, then ask specifically for omitted parts
							(season, etc)<br>
							2. Ask in turn, <i>"Can you tell me the name of this hospital
							etc?"</i></td>
						</tr>
						<tr>
							<td><a>Registration:</a><br>
							Ask the patient if you may test his/her memory. The first
							repetition determines the score. Repeat up to 6 times until all
							are learned. If after 6 trials they aren't learned, recall cannot
							be tested.</td>
						</tr>
						<tr>
							<td><a>Attention & Calculation:</a><br>
							Begin with 100 and count backwards by 7. Stop after 5
							subtractions. Or spell "WORLD" backwards. Score is the number of
							letters in correct order.</td>
						</tr>
						<tr>
							<td><a>Recall:</a><br>
							Ask for the previous three words committed to memory.</td>
						</tr>
						<tr>
							<td><a>Language:</a><br>
							<i>Naming:</i> Show a watch and pencil - name them.<br>
							<i>Repetition:</i> Repeat the sentence after you - one trial.</td>
						</tr>
						<tr>
							<td><a>3-Stage command</a><br>
							1 point for each part right.</td>
						</tr>
						<tr>
							<td><a>Reading:</a><br>
							Letters must be large enough. Read it and then do it. 1 point
							only if he/she closes eyes.</td>
						</tr>
						<tr>
							<td><a>Writing:</a><br>
							Spontaneous. It must have a subject and a verb and be sensible.
							Correct grammar and punctuation are not necessary.</td>
						</tr>
						<tr>
							<td><a>Copying:</a><br>
							Intersecting pentagons with 1" sides are copied. All ten angles
							must be present and 2 must intersect to score 1 point. Ignore any
							tremor or notation.</td>
						</tr>
						<tr>
							<td><br>
							<br>
							(J. Psychiat. Res., 1975; 12:189-198)</td>
						</tr>
						<tr>
							<td align="center"><img style="width: 40%; height: 126px;"
								src="graphics/MMSEpentagons.bmp" /></td>
						</tr>
					</table>
					</td>
				</tr>
			</table>
			</td>
		</tr>
	</table>

	<table class="Head" class="hidePrint">
		<tr>
			<td nowrap="true"><input type="submit" value="Save"
				onclick="javascript:return onSave();" /> <input type="submit"
				value="Save and Exit" onclick="javascript:return onSaveExit();" /> <input
				type="submit" value="Exit" onclick="javascript:return onExit();" />
			<input type="button" value="Print"
				onclick="javascript:return onPrint();" /></td>
		</tr>
	</table>

</html:form>
</body>
</html:html>
