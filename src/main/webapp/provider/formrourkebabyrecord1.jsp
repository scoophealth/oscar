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
<title>Rourke Baby Record</title>
<script language="JavaScript">
<!--		
function setfocus() {
  this.focus();
  //document.serviceform.xml_ff_socialsupport.focus();
}
//-->
</SCRIPT>
<link rel="stylesheet" type="text/css" media="all" href="../share/css/extractedFromPages.css"  />
<link rel="stylesheet" href="../css/receptionistapptstyle.css"
	type="text/css">
</head>

<body onLoad="setfocus()" bgproperties="fixed" bgcolor="#c4e9f6"
	onLoad="setfocus()" topmargin="0" leftmargin="0" rightmargin="0">
<form name="serviceform" action="providercontrol.jsp" method="POST">
<%
  //if bNewForm is false (0), then it should be able to display xml data.
  boolean bNew = true, bNewList = true; 
  String content="";
  if( request.getParameter("bNewForm")!=null && request.getParameter("bNewForm").compareTo("0")==0 ) bNew = false;

  if(!bNew ) { //not new form
    bNewList = false;
    Form f = formDao.find(Integer.parseInt(request.getParameter("form_no")));
    if(f != null) {
      content = f.getContent();
%> <xml id="xml_list"><encounter> <%=content%> </encounter></xml> <%
    }
  } else {
    Form f =  formDao.search_form_no(Integer.parseInt(request.getParameter("demographic_no")), "Old Rourke");
    
    if (f != null) {
      bNew = false;
      content = f.getContent();
%> <xml id="xml_list"><encounter> <%=content%> </encounter></xml> <%
    }          
  }
%>
<table border="0" cellspacing="0" cellpadding="0" width="100%">
	<tr bgcolor="#486ebd">
		<th align=CENTER class="titleweb"><font face="Helvetica"
			color="#FFFFFF">Rourke Baby Record: EVIDENCE BASED
		INFANT/CHILD HEALTH MAINTENANCE GUIDE I</font></th>
	</tr>
</table>
<table width="100%" border="0" cellspacing="1" cellpadding="2"
	<%=bNew?"":"datasrc='#xml_list'"%>>
	<tr valign="top">
		<td width="15%" nowrap>Birth remarks<br>
		<textarea name="xml_br" style="width: 100%" rows="2" cols="15"
			<%=bNew?"":"datafld='xml_br'"%>></textarea></td>
		<td width="20%" nowrap>Risk Factors/Family History<br>
		<textarea name="xml_rffh" style="width: 100%" rows="2" cols="15"
			<%=bNew?"":"datafld='xml_rffh'"%>></textarea></td>
		<td width="65%" nowrap>
		<p>NAME <input type="text" name="xml_name" maxlength="60"
			size="30" <%=bNew?"":"datafld='xml_name'"%>> Birth Date
		(d/m/yr) <input type="text" name="xml_bd" size="10" maxlength="10"
			<%=bNew?"":"datafld='xml_bd'"%>> M <input type="checkbox"
			name="xml_m" value="checked" <%=bNew?"":"datafld='xml_m'"%>>
		&nbsp; F <input type="checkbox" name="xml_f" value="checked"
			<%=bNew?"":"datafld='xml_f'"%>></p>
		<p>Length: <input type="text" name="xml_l" size="6" maxlength="6"
			<%=bNew?"":"datafld='xml_l'"%>> cm. Head Circ: <input
			type="text" name="xml_hc" size="6" maxlength="6"
			<%=bNew?"":"datafld='xml_hc'"%>> cm. Birth Wt. <input
			type="text" name="xml_bw" size="6" maxlength="7"
			<%=bNew?"":"datafld='xml_bw'"%>> gms. Discharge Wt. <input
			type="text" name="xml_dw" size="6" maxlength="7"
			<%=bNew?"":"datafld='xml_dw'"%>> gms.</p>
		</td>
	</tr>
</table>
<table width="100%" border="1" cellspacing="0" cellpadding="0"
	<%=bNew?"":"datasrc='#xml_list'"%>>
	<tr align="center">
		<td width="10%" bgcolor="#CCFFFF"><b>DATE / AGE</b></td>
		<td width="22.5%" colspan="3" bgcolor="#CCFFCC">within 1 week</td>
		<td width="22.5%" colspan="3" bgcolor="#CCFF99">2 weeks
		(optional)</td>
		<td width="22.5%" colspan="3" bgcolor="#CCFF00">1 month
		(optional)</td>
		<td width="22.5%" colspan="3" bgcolor="#66FF00">2 months</td>
	</tr>
	<tr align="center">
		<td bgcolor="#CCFFFF"><b>GROWTH</b></td>
		<td>Ht.<br>
		<input type="text" name="xml_gh1w" size="4" maxlength="5"
			<%=bNew?"":"datafld='xml_gh1w'"%>></td>
		<td>Wt.<br>
		<input type="text" name="xml_gw1w" size="4" maxlength="5"
			<%=bNew?"":"datafld='xml_gw1w'"%>></td>
		<td>Hd. Circ av. 35cm<br>
		<input type="text" name="xml_ghc1w" size="4" maxlength="5"
			<%=bNew?"":"datafld='xml_ghc1w'"%>></td>
		<td>Ht. <br>
		<input type="text" name="xml_gh2w" size="4" maxlength="5"
			<%=bNew?"":"datafld='xml_gh2w'"%>></td>
		<td>Wt. <br>
		<input type="text" name="xml_gw2w" size="4" maxlength="5"
			<%=bNew?"":"datafld='xml_gw2w'"%>></td>
		<td>Hd. Circ <br>
		<input type="text" name="xml_ghc2w" size="4" maxlength="5"
			<%=bNew?"":"datafld='xml_ghc2w'"%>></td>
		<td>Ht. <br>
		<input type="text" name="xml_gh1m" size="4" maxlength="5"
			<%=bNew?"":"datafld='xml_gh1m'"%>></td>
		<td>Wt. <br>
		<input type="text" name="xml_gw1m" size="4" maxlength="5"
			<%=bNew?"":"datafld='xml_gw1m'"%>></td>
		<td>Hd. Circ <br>
		<input type="text" name="xml_ghc1m" size="4" maxlength="5"
			<%=bNew?"":"datafld='xml_ghc1m'"%>></td>
		<td>Ht. <br>
		<input type="text" name="xml_gh2m" size="4" maxlength="5"
			<%=bNew?"":"datafld='xml_gh2m'"%>></td>
		<td>Wt. <br>
		<input type="text" name="xml_gw2m" size="4" maxlength="5"
			<%=bNew?"":"datafld='xml_gw2m'"%>></td>
		<td width="7%">Hd. Circ <br>
		<input type="text" name="xml_ghc2m" size="4" maxlength="5"
			<%=bNew?"":"datafld='xml_ghc2m'"%>></td>
	</tr>
	<tr align="center">
		<td bgcolor="#CCFFFF"><b>PARENTAL CONCERNS</b></td>
		<td colspan="3"><textarea name="xml_pc1w" style="width: 100%"
			cols="10" rows="2" <%=bNew?"":"datafld='xml_pc1w'"%>></textarea></td>
		<td colspan="3"><textarea name="xml_pc2w" style="width: 100%"
			cols="10" rows="2" <%=bNew?"":"datafld='xml_pc2w'"%>></textarea></td>
		<td colspan="3"><textarea name="xml_pc1m" style="width: 100%"
			cols="10" rows="2" <%=bNew?"":"datafld='xml_pc1m'"%>></textarea></td>
		<td colspan="3"><textarea name="xml_pc2m" style="width: 100%"
			cols="10" rows="2" <%=bNew?"":"datafld='xml_pc2m'"%>></textarea></td>
	</tr>
	<tr>
		<td bgcolor="#CCFFFF">
		<div align="center"><b>NUTRITION</b>:</div>
		</td>
		<td colspan="3" valign="top"><input type="radio" name="xml_nbf1w"
			value="checked" <%=bNew?"":"datafld='xml_nbf1w'"%>> <b>Breast
		feeding* Vit.D 10ug=400IU/day*</b><br>
		<input type="radio" name="xml_nff1w" value="checked"
			<%=bNew?"":"datafld='xml_nff1w'"%>> <i>Formula Feeding</i>
		(Fe fortified) <br>
		[150ml = 5oz/kg/day]<br>
		<input type="radio" name="xml_nsp1w" value="checked"
			<%=bNew?"":"datafld='xml_nsp1w'"%>> Stool pattern &amp; urine
		output</td>
		<td colspan="3" valign="top"><input type="radio" name="xml_nbf2w"
			value="checked" <%=bNew?"":"datafld='xml_nbf2w'"%>> <b>Breast
		feeding* Vit.D 10ug=400IU/day*</b><br>
		<input type="radio" name="xml_nff2w" value="checked"
			<%=bNew?"":"datafld='xml_nff2w'"%>> <i>Formula Feeding</i>
		(Fe fortified) <br>
		[150ml = 5oz/kg/day]<br>
		<input type="radio" name="xml_nsp2w" value="checked"
			<%=bNew?"":"datafld='xml_nsp2w'"%>> Stool pattern &amp; urine
		output</td>
		<td colspan="3" valign="top"><input type="radio" name="xml_nbf1m"
			value="checked" <%=bNew?"":"datafld='xml_nbf1m'"%>> <b>Breast
		feeding* Vit.D 10ug=400IU/day*</b><br>
		<input type="radio" name="xml_nff1m" value="checked"
			<%=bNew?"":"datafld='xml_nff1m'"%>> <i>Formula Feeding</i>
		(Fe fortified) <br>
		<input type="radio" name="xml_nsp1m" value="checked"
			<%=bNew?"":"datafld='xml_nsp1m'"%>> Stool pattern &amp; urine
		output</td>
		<td colspan="3" valign="top"><input type="radio" name="xml_nbf2m"
			value="checked" <%=bNew?"":"datafld='xml_nbf2m'"%>> <b>Breast
		feeding* Vit.D 10ug=400IU/day*</b><br>
		<input type="radio" name="xml_nff2m" value="checked"
			<%=bNew?"":"datafld='xml_nff2m'"%>> <i>Formula Feeding</i>
		(Fe fortified)</td>
	</tr>
	<tr>
		<td bgcolor="#CCFFFF">
		<p align="center"><b>EDUCATION &amp; ADVICE</b></p>
		<p align="center"><b>Safety</b></p>
		<p align="center"><b>Behaviour</b></p>
		<p align="center"><b>Family</b></p>
		<p align="center"><b>Other</b></p>
		</td>
		<td colspan="3" valign="top">
		<p><input type="radio" name="xml_eacsi1w" value="checked"
			<%=bNew?"":"datafld='xml_eacsi1w'"%>> <b>Car seat
		(infant)*</b><br>
		<input type="radio" name="xml_eacs1w" value="checked"
			<%=bNew?"":"datafld='xml_eacs1w'"%>> Crib safety</p>
		<p><input type="radio" name="xml_easc1w" value="checked"
			<%=bNew?"":"datafld='xml_easc1w'"%>> Sleeping/crying<br>
		<input type="radio" name="xml_easr1w" value="checked"
			<%=bNew?"":"datafld='xml_easr1w'"%>>
		Soothability/responsiveness <br>
		<input type="radio" name="xml_eapb1w" value="checked"
			<%=bNew?"":"datafld='xml_eapb1w'"%>> Parenting/bonding <br>
		<input type="radio" name="xml_eafdfcs1w" value="checked"
			<%=bNew?"":"datafld='xml_eafdfcs1w'"%>> Fatigue/depression <br>
		Family conflict/stress<br>
		<input type="radio" name="xml_eas1w" value="checked"
			<%=bNew?"":"datafld='xml_eas1w'"%>> Siblings <br>
		<input type="radio" name="xml_eaahvn1w" value="checked"
			<%=bNew?"":"datafld='xml_eaahvn1w'"%>> <b> Assess home
		visit need*</b> <br>
		<input type="radio" name="xml_easp1w" value="checked"
			<%=bNew?"":"datafld='xml_easp1w'"%>> <b>Sleep position* </b><br>
		<input type="radio" name="xml_eatco1w" value="checked"
			<%=bNew?"":"datafld='xml_eatco1w'"%>> <i>Temperature
		control &amp; overdressing</i><br>
		<input type="radio" name="xml_eashs1w" value="checked"
			<%=bNew?"":"datafld='xml_eashs1w'"%>> <b>Second hand
		smoke* </b><br>
		</p>
		</td>
		<td colspan="3" valign="top">
		<p><input type="radio" name="xml_eacsi2w" value="checked"
			<%=bNew?"":"datafld='xml_eacsi2w'"%>> <b>Car seat
		(infant)*</b><br>
		<input type="radio" name="xml_eacs2w" value="checked"
			<%=bNew?"":"datafld='xml_eacs2w'"%>> Crib safety</p>
		<p><input type="radio" name="xml_easc2w" value="checked"
			<%=bNew?"":"datafld='xml_easc2w'"%>> Sleeping/crying<br>
		<input type="radio" name="xml_easr2w" value="checked"
			<%=bNew?"":"datafld='xml_easr2w'"%>>
		Soothability/responsiveness <br>
		<input type="radio" name="xml_eapb2w" value="checked"
			<%=bNew?"":"datafld='xml_eapb2w'"%>> Parenting/bonding <br>
		<input type="radio" name="xml_eafdfcs2w" value="checked"
			<%=bNew?"":"datafld='xml_eafdfcs2w'"%>> Fatigue/depression <br>
		Family conflict/stress<br>
		<input type="radio" name="xml_eas2w" value="checked"
			<%=bNew?"":"datafld='xml_eas2w'"%>> Siblings <br>
		<input type="radio" name="xml_eaahvn2w" value="checked"
			<%=bNew?"":"datafld='xml_eaahvn2w'"%>> <b> Assess home
		visit need*</b> <br>
		<input type="radio" name="xml_easp2w" value="checked"
			<%=bNew?"":"datafld='xml_easp2w'"%>> <b>Sleep position* </b><br>
		<input type="radio" name="xml_eatco2w" value="checked"
			<%=bNew?"":"datafld='xml_eatco2w'"%>> <i>Temperature
		control &amp; overdressing</i><br>
		<input type="radio" name="xml_eashs2w" value="checked"
			<%=bNew?"":"datafld='xml_eashs2w'"%>> <b>Second hand
		smoke* </b><br>
		</p>
		</td>
		<td colspan="3" valign="top">
		<p><input type="radio" name="xml_eacmsd1m" value="checked"
			<%=bNew?"":"datafld='xml_eacmsd1m'"%>> Carbon monoxide/<b><br>
		</b><i>Smoke detectors*</i><br>
		<input type="radio" name="xml_eanis1m" value="checked"
			<%=bNew?"":"datafld='xml_eanis1m'"%>> <i> Non-inflam.
		sleepwear</i> <br>
		<input type="radio" name="xml_eahw1m" value="checked"
			<%=bNew?"":"datafld='xml_eahw1m'"%>> <i>Hot water &lt; 54
		&deg;C*</i><br>
		<input type="radio" name="xml_eacst1m" value="checked"
			<%=bNew?"":"datafld='xml_eacst1m'"%>> Choking/safe toys*<br>
		<input type="radio" name="xml_easc1m" value="checked"
			<%=bNew?"":"datafld='xml_easc1m'"%>> Sleep/crying<br>
		<input type="radio" name="xml_easr1m" value="checked"
			<%=bNew?"":"datafld='xml_easr1m'"%>>
		Soothability/responsiveness <br>
		<input type="radio" name="xml_eapci1m" value="checked"
			<%=bNew?"":"datafld='xml_eapci1m'"%>> Parent/child
		interaction <br>
		<input type="radio" name="xml_eaas1m" value="checked"
			<%=bNew?"":"datafld='xml_eaas1m'"%>> Assess supports</p>
		</td>
		<td colspan="3" valign="top">
		<p><input type="radio" name="xml_eaf2m" value="checked"
			<%=bNew?"":"datafld='xml_eaf2m'"%>> <i>Falls*</i><br>
		<input type="radio" name="xml_eacst2m" value="checked"
			<%=bNew?"":"datafld='xml_eacst2m'"%>> Choking/safe toys*</p>
		<p><input type="radio" name="xml_easc2m" value="checked"
			<%=bNew?"":"datafld='xml_easc2m'"%>> Sleep/crying<br>
		<input type="radio" name="xml_easr2m" value="checked"
			<%=bNew?"":"datafld='xml_easr2m'"%>>
		Soothability/responsiveness <br>
		<input type="radio" name="xml_eapci2m" value="checked"
			<%=bNew?"":"datafld='xml_eapci2m'"%>> Parent/child
		interaction <br>
		<input type="radio" name="xml_eadfs2m" value="checked"
			<%=bNew?"":"datafld='xml_eadfs2m'"%>> Depression/family
		stress</p>
		<p>&nbsp;</p>
		<p>&nbsp;</p>
		<p><input type="radio" name="xml_eafc2m" value="checked"
			<%=bNew?"":"datafld='xml_eafc2m'"%>> Fever control</p>
		</td>
	</tr>
	<tr>
		<td bgcolor="#CCFFFF">
		<div align="center"><b>DEVELOPMENT</b><br>
		(Inquiry &amp; observation of milestones)<br>
		Tasks are set after the time of normal milestone acquisition.<br>
		Absence of any item suggests the need for further assessment of
		development<br>
		</div>
		</td>
		<td colspan="3">&nbsp;</td>
		<td colspan="3">&nbsp;</td>
		<td colspan="3" valign="top"><input type="radio" name="xml_dfg1m"
			value="checked" <%=bNew?"":"datafld='xml_dfg1m'"%>> Focuses
		gaze<br>
		<input type="radio" name="xml_dstlsn1m" value="checked"
			<%=bNew?"":"datafld='xml_dstlsn1m'"%>> Startles to loud or
		sudden noise<br>
		<input type="radio" name="xml_dswon1m" value="checked"
			<%=bNew?"":"datafld='xml_dswon1m'"%>> Sucks well on nipple<br>
		<input type="radio" name="xml_dnpc1m" value="checked"
			<%=bNew?"":"datafld='xml_dnpc1m'"%>> No parent concerns</td>
		<td colspan="3" valign="top"><input type="radio"
			name="xml_dfmwe2m" value="checked"
			<%=bNew?"":"datafld='xml_dfmwe2m'"%>> Follows movement with
		eyes<br>
		<input type="radio" name="xml_dhasc2m" value="checked"
			<%=bNew?"":"datafld='xml_dhasc2m'"%>> Has a variety of sounds
		&amp; cries<br>
		<input type="radio" name="xml_dhhuw2m" value="checked"
			<%=bNew?"":"datafld='xml_dhhuw2m'"%>> Holds head up when held
		at adult&#146;s shoulder<br>
		<input type="radio" name="xml_debtc2m" value="checked"
			<%=bNew?"":"datafld='xml_debtc2m'"%>> Enjoys being touched
		&amp; cuddled<br>
		<input type="radio" name="xml_dnpc2m" value="checked"
			<%=bNew?"":"datafld='xml_dnpc2m'"%>> No parent concerns</td>
	</tr>
	<tr>
		<td bgcolor="#CCFFFF">
		<div align="center"><b>PHYSICAL EXAMINATION</b><br>
		Evidence based screening for specific conditions is highlighted, but
		an appropriate age-specific focused physical examination is
		recommended at each visit</div>
		</td>
		<td colspan="3" valign="top">
		<p><input type="radio" name="xml_pes1w" value="checked"
			<%=bNew?"":"datafld='xml_pes1w'"%>> <i>Skin (jaundice,
		dry)</i><br>
		<input type="radio" name="xml_pef1w" value="checked"
			<%=bNew?"":"datafld='xml_pef1w'"%>> Fontanelles<br>
		<input type="radio" name="xml_peerr1w" value="checked"
			<%=bNew?"":"datafld='xml_peerr1w'"%>> <i>Eyes (red
		reflex)</i><br>
		<input type="radio" name="xml_ped1w" value="checked"
			<%=bNew?"":"datafld='xml_ped1w'"%>> <i>Ears (drums)</i><br>
		<input type="radio" name="xml_pehl1w" value="checked"
			<%=bNew?"":"datafld='xml_pehl1w'"%>> Heart/Lungs<br>
		<input type="radio" name="xml_peu1w" value="checked"
			<%=bNew?"":"datafld='xml_peu1w'"%>> Umbilicus<br>
		<input type="radio" name="xml_pefp1w" value="checked"
			<%=bNew?"":"datafld='xml_pefp1w'"%>> Femoral pulses <br>
		<input type="radio" name="xml_peh1w" value="checked"
			<%=bNew?"":"datafld='xml_peh1w'"%>> <b>Hips</b> <br>
		<input type="radio" name="xml_pet1w" value="checked"
			<%=bNew?"":"datafld='xml_pet1w'"%>> Testicles<br>
		<input type="radio" name="xml_pemus1w" value="checked"
			<%=bNew?"":"datafld='xml_pemus1w'"%>> Male urinary
		stream/foreskin care</p>
		<p>&nbsp;</p>
		</td>
		<td colspan="3" valign="top"><input type="radio" name="xml_pes2w"
			value="checked" <%=bNew?"":"datafld='xml_pes2w'"%>> <i>
		Skin (jaundice, dry)</i><br>
		<input type="radio" name="xml_pef2w" value="checked"
			<%=bNew?"":"datafld='xml_pef2w'"%>> Fontanelles<br>
		<input type="radio" name="xml_peerr2w" value="checked"
			<%=bNew?"":"datafld='xml_peerr2w'"%>> <i>Eyes (red
		reflex)<br>
		<input type="radio" name="xml_ped2w" value="checked"
			<%=bNew?"":"datafld='xml_ped2w'"%>> Ears (drums)</i><br>
		<input type="radio" name="xml_pehl2w" value="checked"
			<%=bNew?"":"datafld='xml_pehl2w'"%>> Heart/Lungs<br>
		<input type="radio" name="xml_peu2w" value="checked"
			<%=bNew?"":"datafld='xml_peu2w'"%>> Umbilicus<br>
		<input type="radio" name="xml_pefp2w" value="checked"
			<%=bNew?"":"datafld='xml_pefp2w'"%>> Femoral pulses <br>
		<input type="radio" name="xml_peh2w" value="checked"
			<%=bNew?"":"datafld='xml_peh2w'"%>> <b>Hips </b><br>
		<input type="radio" name="xml_pet2w" value="checked"
			<%=bNew?"":"datafld='xml_pet2w'"%>> Testicles<br>
		<input type="radio" name="xml_pemus2w" value="checked"
			<%=bNew?"":"datafld='xml_pemus2w'"%>> Male urinary
		stream/foreskin care</td>
		<td colspan="3" valign="top"><input type="radio" name="xml_pef1m"
			value="checked" <%=bNew?"":"datafld='xml_pef1m'"%>>
		Fontanelles<br>
		<input type="radio" name="xml_peerr1m" value="checked"
			<%=bNew?"":"datafld='xml_peerr1m'"%>> <i>Eyes (red
		reflex)<br>
		<input type="radio" name="xml_pecuti1m" value="checked"
			<%=bNew?"":"datafld='xml_pecuti1m'"%>> </i><b> Cover/uncover
		test &amp; inquiry*</b><br>
		<input type="radio" name="xml_pehi1m" value="checked"
			<%=bNew?"":"datafld='xml_pehi1m'"%>> <b> Hearing inquiry</b><br>
		<input type="radio" name="xml_peht1m" value="checked"
			<%=bNew?"":"datafld='xml_peht1m'"%>> Heart<br>
		<input type="radio" name="xml_peh1m" value="checked"
			<%=bNew?"":"datafld='xml_peh1m'"%>> <b>Hips </b></td>
		<td colspan="3" valign="top"><input type="radio" name="xml_pef2m"
			value="checked" <%=bNew?"":"datafld='xml_pef2m'"%>>
		Fontanelles<br>
		<input type="radio" name="xml_peerr2m" value="checked"
			<%=bNew?"":"datafld='xml_peerr2m'"%>> <i>Eyes (red
		reflex)<br>
		<input type="radio" name="xml_pecuti2m" value="checked"
			<%=bNew?"":"datafld='xml_pecuti2m'"%>> </i><b> Cover/uncover
		test &amp; inquiry*</b><br>
		<input type="radio" name="xml_pehi2m" value="checked"
			<%=bNew?"":"datafld='xml_pehi2m'"%>> <b> Hearing inquiry</b><br>
		<input type="radio" name="xml_peht2m" value="checked"
			<%=bNew?"":"datafld='xml_peht2m'"%>> Heart<br>
		<input type="radio" name="xml_peh2m" value="checked"
			<%=bNew?"":"datafld='xml_peh2m'"%>> <b>Hips </b><i> </i></td>
	</tr>
	<tr>
		<td bgcolor="#CCFFFF">
		<div align="center"><b>PROBLEMS &amp; PLANS</b></div>
		</td>
		<td colspan="3"><input type="radio" name="xml_pppt1w"
			value="checked" <%=bNew?"":"datafld='xml_pppt1w'"%>> <b>
		PKU, Thyroid</b><br>
		<input type="radio" name="xml_pphs1w" value="checked"
			<%=bNew?"":"datafld='xml_pphs1w'"%>> <b>Hemoglobinopathy
		Screen (if at risk)*</b></td>
		<td colspan="3">&nbsp;</td>
		<td colspan="3">&nbsp;</td>
		<td colspan="3">&nbsp;</td>
	</tr>
	<tr>
		<td bgcolor="#CCFFFF">
		<div align="center"><b>IMMUNIZATION</b><br>
		Guidelines may vary by province</div>
		</td>
		<td colspan="3" valign="top">If HBsAg-positive parent or sibling:<br>
		<input type="radio" name="xml_ihb1w" value="checked"
			<%=bNew?"":"datafld='xml_ihb1w'"%>> <b> Hep. B vaccine* </b></td>
		<td colspan="3">&nbsp;</td>
		<td colspan="3" valign="top">Give information:<br>
		<input type="radio" name="xml_ii1m" value="checked"
			<%=bNew?"":"datafld='xml_ii1m'"%>> Immunization<br>
		<input type="radio" name="xml_ia1m" value="checked"
			<%=bNew?"":"datafld='xml_ia1m'"%>> Acetaminophen<br>
		If HBsAg-positive parent or sibling:<br>
		<input type="radio" name="xml_ihb1m" value="checked"
			<%=bNew?"":"datafld='xml_ihb1m'"%>> <b>Hep. B vaccine* </b></td>
		<td colspan="3">
		<p><input type="radio" name="xml_ia2m" value="checked"
			<%=bNew?"":"datafld='xml_ia2m'"%>> Acetaminophen</p>
		<p><input type="radio" name="xml_ihib2m" value="checked"
			<%=bNew?"":"datafld='xml_ihib2m'"%>> <b>HIB</b><br>
		<input type="radio" name="xml_iap2m" value="checked"
			<%=bNew?"":"datafld='xml_iap2m'"%>> <b> aPDT polio </b></p>
		</td>
	</tr>
	<tr>
		<td bgcolor="#CCFFFF">
		<div align="center"><b>Signature</b></div>
		</td>
		<td colspan="3">&nbsp;</td>
		<td colspan="3">&nbsp;</td>
		<td colspan="3">&nbsp;</td>
		<td colspan="3">&nbsp;</td>
	</tr>
	<tr bgcolor="#486ebd">
		<td align="center" colspan="13">
		<%
  GregorianCalendar now=new GregorianCalendar();
%> <input type="hidden" name="xml_subject"
			value="Form:Rourke Baby Record"> <input type="hidden"
			name="demographic_no"
			value="<%=request.getParameter("demographic_no")%>"> <input
			type="hidden" name="form_date"
			value='<%=now.get(Calendar.YEAR)+"-"+(now.get(Calendar.MONTH)+1)+"-"+now.get(Calendar.DAY_OF_MONTH)%>'>
		<input type="hidden" name="form_time"
			value='<%=now.get(Calendar.HOUR_OF_DAY)+":"+now.get(Calendar.MINUTE)+":"+now.get(Calendar.SECOND)%>'>
		<input type="hidden" name="user_no" value='<%=user_no%>'> <input
			type="hidden" name="form_name" value='rourkebabyrecord1'> <input
			type="hidden" name="dboperation" value="save_form"> <input
			type="hidden" name="displaymode" value="saveform"> <%
  if(bNewList) {
%> <input type='submit' name='subbutton' value=' Save '> <%
  }
%> <input type="button" name="Button" value=" Cancel "
			onClick="window.close();"></td>
	</tr>
</table>

<p><font size="-2">Grade of evidence: (A) <b>Bold type -
Good evidence</b>: (B)<i> Italic - Fair evidence</i> (C) Plain - Consensus
with no definitive evidence (*)<i> see Infant/Child Health
Maintenance: Selected Guidelines on reverse of Guide I</i><br>
** Disclaimer: Given the constantly evolving nature of evidence and
changing recommendations, the <i>Rourke Baby Record</i>: EB is meant to
be used as a guide only.</font></p>
<p align="right"><font size="-2">Part #MC0046</font></p>
</form>
</body>
</html>
