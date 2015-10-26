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
<security:oscarSec roleName="<%=roleName$%>" objectName="_form" rights="w" reverse="<%=true%>">
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
%>
<%@ page import="java.util.*, java.sql.*, oscar.*" errorPage="errorpage.jsp"%>

<%@page import="org.oscarehr.util.SpringUtils" %>
<%@page import="org.oscarehr.common.dao.DemographicAccessoryDao" %>
<%@page import="org.oscarehr.common.model.DemographicAccessory" %>
<%@page import="org.oscarehr.common.dao.FormDao" %>
<%@page import="org.oscarehr.common.model.Form" %>
<%@page import="org.oscarehr.common.dao.DemographicDao" %>
<%@page import="org.oscarehr.common.model.Demographic" %>
<%
	DemographicAccessoryDao demographicAccessoryDao = (DemographicAccessoryDao)SpringUtils.getBean("demographicAccessoryDao");
	FormDao formDao = SpringUtils.getBean(FormDao.class);
	DemographicDao demographicDao = SpringUtils.getBean(DemographicDao.class);
%>

<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>FORM ALPHA</title>
<link rel="stylesheet" href="antenatalrecord1.css">
<script language="JavaScript">
<!--		
var saveTemp=0;
function setfocus() {
  this.focus();
  document.serviceform.xml_ff_socialsupport.focus();
  //document.serviceform.xml_ff_socialsupport.select();
}
function onExit() {
  if(confirm("Are you sure to exit WITHOUT saving the form?")) window.close();
}
function onPrint() {
  saveTemp=2;
}
function onSaveClose() { //save & close
  saveTemp=3;
}
function onSubmitForm() {
  if(saveTemp==0) {
    document.serviceform.target="apptProvider";
    document.serviceform.cmd.value="Save & Exit";
    document.serviceform.submit();
  }
  if(saveTemp==1) {
    popupPage(10,10,'providercontrol.jsp');
    document.serviceform.target="printlocation";
    document.serviceform.cmd.value="Save";
    document.serviceform.submit();
    document.serviceform.target="apptProvider";
    saveTemp=0;
  }
  if(saveTemp==2) {
    document.serviceform.cmd.value="Print Preview";
    document.serviceform.submit();
  }
  if(saveTemp==3) {
    document.serviceform.cmd.value="Save";
    document.serviceform.submit();
  }
  return false;
}
//-->
</SCRIPT>
<%
  //if bNewForm is false (0), then it should be able to display xml data.
  boolean bNew = true;
  if( request.getParameter("bNewForm")!=null && request.getParameter("bNewForm").compareTo("0")==0 ) 
    bNew = false;

  if( !bNew ) {
    String content="";
    Form f = formDao.find(Integer.parseInt(request.getParameter("form_no")));
    if(f != null) {
        content = f.getContent();
%>
<xml id="xml_list">
<encounter>
<%=content%>
</encounter>
</xml>
<%
    }     
  }
%>
</head>
<body onLoad="setfocus()" topmargin="0" leftmargin="0" rightmargin="0">
<form name="serviceform" action="providercontrol.jsp" method="POST"
	onSubmit="return (onSubmitForm());">

<table border="0" cellspacing="0" cellpadding="0" width="100%">
	<tr bgcolor="#486ebd">
		<th width="15%" nowrap><%=bNew?"<input type='submit' name='saveexit' value='Save to Enc.& Exit' onClick='onSaveExit()'> ":""%></th>
		<th align='CENTER'><font size="-1"
			face="Arial, Helvetica, sans-serif" color="#FFFFFF">ANTENATAL
		PSYCHOSOCIAL HEALTH ASSESSMENT (ALPHA)</font></th>
		<th width="25%" nowrap align='right'><%=bNew?"<input type='button' name='Button' value=' Exit ' onClick='onExit();'>":"<input type='button' name='Button' value=' Exit ' onClick='window.close();'>" %>
		<input type="submit" name="print" value="Print Preview"
			onClick='onPrint()'> <a href=#
			onClick="popupPage(200,300,'formarprintsetting.jsp');return false;">
		. </a> <input type="hidden" name="oox" value="0"> <input
			type="hidden" name="ooy" value="0"> <input type="hidden"
			name="cmd" value=""></th>
	</tr>
</table>


<table width="100%" border="0" bgcolor="ivory"
	<%=bNew?"":"datasrc='#xml_list'"%>>
	<tr bgcolor="#99FF99">
		<td><b>FAMILY FACTORS</b></td>
		<td align="right"><%--=bNew?"<input type='submit' name='subbutton' value=' Save '>":""--%>
		</td>
	</tr>
	<tr>
		<td width="50%"><b>Social support (CA, WA, PD) </b>

		<li><font size="-1">How does your partner/family feel
		about your pregnancy?</font></li>
		<li><font size="-1">Who will be helping you when you go
		home with your baby?</font></li>
		</td>
		<td><textarea name="xml_ff_socialsupport" style="width: 100%"
			cols="40" rows="3" <%=bNew?"":"datafld='xml_ff_socialsupport'"%>></textarea>
		</td>
	</tr>
	<tr>
		<td><b>Recent stressful life events (CA, WA, PD, PI)</b>

		<li><font size="-1">What life changes have you experienced
		this year?</font></li>
		<li><font size="-1">What changes are you planning during
		this pregnancy?</font></li>
		</td>
		<td><textarea name="xml_ff_recentstressfullifeevents"
			style="width: 100%" cols="40" rows="3"
			<%=bNew?"":"datafld='xml_ff_recentstressfullifeevents'"%>></textarea>
		</td>
	</tr>
	<tr>
		<td><b>Couple's relationship (CD, PD, WA, CA)</b>

		<li><font size="-1">How would you describe your
		relationship with your partner?</font></li>
		<li><font size="-1">What do you think your relationship
		will be like after the birth?</font><br>
		</li>
		</td>
		<td><textarea name="xml_ff_couplerelationship"
			style="width: 100%" cols="40" rows="3"
			<%=bNew?"":"datafld='xml_ff_couplerelationship'"%>></textarea></td>
	</tr>
	<tr bgcolor="#99FF99">
		<td colspan="2"><b>MATERNAL FACTORS</b></td>
	</tr>
	<tr>
		<td valign="top"><b>Prenatal care (late onset) (WA)</b>

		<li><font size="-1">First prenatal visit in third
		trimester? (check records)</font></li>
		</td>
		<td><textarea name="xml_mf_prenatalcare" style="width: 100%"
			cols="40" rows="2" <%=bNew?"":"datafld='xml_mf_prenatalcare'"%>></textarea>
		</td>
	</tr>
	<tr>
		<td valign="top"><b>Prenatal education (refusal or quit) (CA)</b>

		<li><font size="-1">What are your plans for prenatal
		classes? </font></li>
		</td>
		<td><textarea name="xml_mf_prenataleducation" style="width: 100%"
			cols="40" rows="2" <%=bNew?"":"datafld='xml_mf_prenataleducation'"%>></textarea>
		</td>
	</tr>
	<tr>
		<td valign="top"><b>Feelings toward pregnancy after 20 weeks
		(CA, WA) </b>

		<li><font size="-1">How did you feel when you just found
		out you were pregnant?</font></li>
		<li><font size="-1">How do you feel about it now?</font></li>
		</td>
		<td><textarea name="xml_mf_feelingstopregnancy20"
			style="width: 100%" cols="40" rows="2"
			<%=bNew?"":"datafld='xml_mf_feelingstopregnancy20'"%>></textarea></td>
	</tr>
	<tr>
		<td valign="top"><b>Relationship with parents in childhood
		(CA)</b>

		<li><font size="-1">How did you get along with your
		parents?</font></li>
		<li><font size="-1">Did you feel loved by your parents?</font></li>
		</td>
		<td><textarea name="xml_mf_relationshipwithparents"
			style="width: 100%" cols="40" rows="2"
			<%=bNew?"":"datafld='xml_mf_relationshipwithparents'"%>></textarea></td>
	</tr>
	<tr>
		<td valign="top"><b>Self esteem (CA, WA)</b>

		<li><font size="-1">What concerns do you have about
		becoming/being a mother?</font></li>
		</td>
		<td><textarea name="xml_mf_selfesteem" style="width: 100%"
			cols="40" rows="2" <%=bNew?"":"datafld='xml_mf_selfesteem'"%>></textarea>
		</td>
	</tr>
	<tr>
		<td valign="top"><b>History of psychiatric/emotional problems
		(CA, WA, PD)</b>

		<li><font size="-1">Have you ever had emotional problems?</font></li>
		<li><font size="-1">Have you ever seen a psychiatrist or
		therapist?</font></li>
		</td>
		<td><textarea name="xml_mf_historypsychiatricemaotional"
			style="width: 100%" cols="40" rows="2"
			<%=bNew?"":"datafld='xml_mf_historypsychiatricemaotional'"%>></textarea>
		</td>
	</tr>
	<tr>
		<td valign="top"><b>Depression in this pregnancy (PD)</b>

		<li><font size="-1">How has your mood been during this
		pregnancy? </font></li>
		</td>
		<td><textarea name="xml_mf_depression" style="width: 100%"
			cols="40" rows="2" <%=bNew?"":"datafld='xml_mf_depression'"%>></textarea>
		</td>
	</tr>
	<tr bgcolor="#99FF99">
		<td valign="top" colspan="2"><b>SUBSTANCE USE</b></td>
	</tr>
	<tr>
		<td valign="top"><b>Alcohol/drug abuse (WA, CA)</b>

		<li><font size="-1">How many drinks of alcohol do you have
		per week?</font></li>
		<li><font size="-1">Are there times when you drink more
		than that?</font></li>
		<li><font size="-1">Do you or your partner use
		recreational drugs?</font></li>
		<li><font size="-1">Do you or your partner have a problem
		with alcohol or drugs?</font></li>
		<li><font size="-1">Consider CAGE (<b>C</b>ut down, <b>A</b>nnoyed,
		<b>G</b>uilty, <b>E</b>ye opener)</font></li>
		</td>
		<td><textarea name="xml_su_alcoholdrugabuse" style="width: 100%"
			cols="40" rows="5" <%=bNew?"":"datafld='xml_su_alcoholdrugabuse'"%>></textarea>
		</td>
	</tr>
	<tr bgcolor="#99FF99">
		<td valign="top" colspan="2"><b>FAMILY VIOLENCE</b></td>
	</tr>
	<tr>
		<td valign="top"><b>Woman or partner experienced or witnessed
		abuse <br>
		(physical, emotional, sexual) (CA, WA)</b>

		<li><font size="-1">What was your parents' relationship
		like?</font></li>
		<li><font size="-1">Did your father ever scare or hurt
		your mother?</font></li>
		<li><font size="-1">Did your parents ever scare or hurt
		you?</font></li>
		<li><font size="-1">Were you ever sexually abused as a
		child?</font></li>
		</td>
		<td><textarea name="xml_fv_experiencedwitnessedabuse"
			style="width: 100%" cols="40" rows="5"
			<%=bNew?"":"datafld='xml_fv_experiencedwitnessedabuse'"%>></textarea>
		</td>
	</tr>
	<tr>
		<td valign="top"><b>Current or past woman abuse (WA, CA, PD)</b><br>

		<li><font size="-1">How do you and your partner solve
		arguments? </font></li>
		<li><font size="-1">Do you ever feel frightened by what
		your partner says or does? </font></li>
		<li><font size="-1">Have you ever been hit/pushed/slapped
		by a partner? </font></li>
		<li><font size="-1">Has your partner ever humiliated you
		or psychologically abused you in other ways? </font></li>
		<li><font size="-1">Have you ever been forced to have sex
		against your will? </font></li>
		</td>
		<td><textarea name="xml_fv_currentpastabuse" style="width: 100%"
			cols="40" rows="5" <%=bNew?"":"datafld='xml_fv_currentpastabuse'"%>></textarea>
		</td>
	</tr>
	<tr>
		<td valign="top"><b>Previous child abuse by woman or partner
		(CA)</b>

		<li><font size="-1">Do you/your partner have children not
		living with you? If so, why? </font></li>
		<li><font size="-1">Have you ever had involvement with a
		child protection agency<br>
		(ie Children's Aid Society)? </font></li>
		</td>
		<td><textarea name="xml_fv_previouschildabuse"
			style="width: 100%" cols="40" rows="2"
			<%=bNew?"":"datafld='xml_fv_previouschildabuse'"%>></textarea></td>
	</tr>
	<tr>
		<td valign="top"><b>Child discipline (CA) </b>

		<li><font size="-1">How were you disciplined as a child?</font></li>
		<li><font size="-1">How do you think you will discipline
		your child?</font></li>
		<li><font size="-1">How do you deal with your kids at home
		when they misbehave?</font></li>

		</td>
		<td><textarea name="xml_fv_childdiscipline" style="width: 100%"
			cols="40" rows="3" <%=bNew?"":"datafld='xml_fv_childdiscipline'"%>></textarea>
		</td>
	</tr>
</table>
<table width="100%" border="0" bgcolor="ivory"
	<%=bNew?"":"datasrc='#xml_list'"%>>
	<tr bgcolor="#99ff99">
		<td align="center" colspan="6"><b>FOLLOW-UP PLAN</b></td>
	</tr>
	<tr>
		<td><input type="checkbox" name="xml_fu_scbp" value="checked"
			<%=bNew?"":"datafld='xml_fu_scbp'"%>></td>
		<td>Supportive counselling by provider</td>
		<td><input type="checkbox" name="xml_fu_homecare" value="checked"
			<%=bNew?"":"datafld='xml_fu_homecare'"%>></td>
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
<table width="100%" border="0" bgcolor="ivory"
	<%=bNew?"":"datasrc='#xml_list'"%>>
	<tr>
		<td><b>COMMENTS</b>:<br>
		<textarea name="xml_comments" style="width: 100%" cols="80"
			<%=bNew?"":"datafld='xml_comments'"%>></textarea></td>
	</tr>
	<tr bgcolor="#486ebd">
		<td align="center">
		<%
  GregorianCalendar now=new GregorianCalendar();
%> <input type="hidden" name="xml_subject" value="form:ALPHA"> <input
			type="hidden" name="demographic_no"
			value="<%=request.getParameter("demographic_no")%>"> <input
			type="hidden" name="form_date"
			value='<%=now.get(Calendar.YEAR)+"-"+(now.get(Calendar.MONTH)+1)+"-"+now.get(Calendar.DAY_OF_MONTH)%>'>
		<input type="hidden" name="form_time"
			value='<%=now.get(Calendar.HOUR_OF_DAY)+":"+now.get(Calendar.MINUTE)+":"+now.get(Calendar.SECOND)%>'>
		<input type="hidden" name="user_no" value='<%=user_no%>'> <input
			type="hidden" name="form_name" value='ALPHA'> <input
			type="hidden" name="dboperation" value="save_form"> <input
			type="hidden" name="displaymode" value="saveform"> <%=bNew?"<input type='submit' name='subbutton' value=' Save '>":""%>
		<input type="button" name="Button" value=" Cancel "
			onClick="window.close();"></td>
	</tr>
</table>
</form>
</body>
</html>
