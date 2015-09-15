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
<%
  int oox=0, ooy=0;
  if(request.getParameter("oox")!=null) oox = Integer.parseInt(request.getParameter("oox"));
  if(request.getParameter("ooy")!=null) ooy = Integer.parseInt(request.getParameter("ooy"));
%>
<%@ page
	import="java.util.*, java.net.*, oscar.util.*, oscar.form.graphic.*"
	errorPage="errorpage.jsp"%>
<%@page import="org.oscarehr.util.LoggedInInfo"%>
<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>ANTENATAL RECORD 2</title>
<link rel="stylesheet" href="antenatalRecordPrint.css">
<script language="JavaScript">
<!--

var ox = <%=oox%>;
var oy = <%=ooy%>;
function ff(x,y,w,h,name) { //need escape to name for ' and "
  x = eval(ox+x);
  y = eval(oy+y);
  document.writeln('<div ID="bdiv1" STYLE="position:absolute; visibility:visible; z-index:2; left:'+x+'px; top:'+y+'px; width:'+w+'px; height:'+h+'px;"> ');
  document.writeln(name);
  document.writeln('</div>');
}
//-->
</SCRIPT>
</head>

<%
    Properties props = new Properties();
    StringBuffer temp = new StringBuffer("");
	for (Enumeration e = request.getParameterNames() ; e.hasMoreElements() ;) {
	    temp = new StringBuffer(e.nextElement().toString());
		props.setProperty(temp.toString(), request.getParameter(temp.toString()));
    }
%>

<body onLoad="setfocus()" topmargin="0" leftmargin="1" rightmargin="1"
	bgcolor="navy">
<img src="graphics/formAR2.gif">

<script language="JavaScript">

ff(750,10,50,20,'<span class="title"><a href="javascript: window.print();">Print</a></span>' );
ff(750,40,50,20,'<span class="title"><a href=# onClick="window.close()">Cancel</a></span>' );
ff(192,0,300,20,'<span class="title">Antenatal Record 2</span>' );
ff(9,65,100,20,'<span class="tdname">Name</span>' );
ff(8,87,100,20,'<span class="tdname">Address</span>' );
ff(8,111,200,20,'<span class="tdname">Birth attendants</span>' );
ff(191,111,200,20,'<span class="tdname">Newborn care</span>' );

ff(50,75,500,20,"<%= UtilMisc.JSEscape(props.getProperty("c_pName", "")) %>" );
ff(50,97,500,20,"<%= UtilMisc.JSEscape(props.getProperty("c_address", "")) %>" );
ff(12,122,200,20,"<%= UtilMisc.JSEscape(props.getProperty("c_ba", "")) %>");
ff(195,122,250,20,"<%= UtilMisc.JSEscape(props.getProperty("c_nc", "")) %>" );

//ff(215,138,600,20,'<span class="tdname"><b>Summary of Risk Factors, Allergies, and Medications</b></span>' );
ff(146,155,200,20,'<span class="tdname">Risk Factors</span>' );
ff(418,155,200,20,'<span class="tdname">Allergies</span>' );
ff(596,155,200,20,'<span class="tdname">Medications</span>' );

//ff(6,228,200,20,'<span class="tdname1"><b>Final EDB</b> (yyyy/mm/dd)</span>' );
ff(496,228,100,20,'<span class="tdname">Hb</span>' );
ff(555,228,100,20,'<span class="tdname">MCV</span>' );
ff(607,228,100,20,'<span class="tdname">MSS</span>' );

ff(16,245,200,20,"<%= props.getProperty("c_finalEDB", "") %>" );
ff(140,245,200,20,"<%= UtilMisc.JSEscape(props.getProperty("c_gravida", "")) %>" );
ff(220,245,200,20,"<%= UtilMisc.JSEscape(props.getProperty("c_term", "")) %>" );
ff(300,245,200,20,"<%= UtilMisc.JSEscape(props.getProperty("c_prem", "")) %>" );
ff(380,245,200,20,"<%= UtilMisc.JSEscape(props.getProperty("ar2_etss", "")) %>" );
ff(450,245,200,20,"<%= UtilMisc.JSEscape(props.getProperty("c_living", "")) %>" );
ff(493,245,200,20,"<%= UtilMisc.JSEscape(props.getProperty("ar2_hb", "")) %>" );
ff(550,245,200,20,"<%= UtilMisc.JSEscape(props.getProperty("ar2_mcv", "")) %>" );
ff(605,245,200,20,"<%= UtilMisc.JSEscape(props.getProperty("ar2_mss", "")) %>" );

ff(6,266,200,20,'<span class="tdname">Pre-preg. wt.</span>' );
ff(104,268,200,20,'<span class="tdname">Rubella</span>' );
ff(104,278,200,20,'<span class="tdname">immune</span>' );
ff(190,268,200,20,'<span class="tdname">HBs Ag</span>' );
ff(275,268,200,20,'<span class="tdname">VDRL</span>' );
ff(347,268,200,20,'<span class="tdname">Blood</span>' );
ff(347,278,200,20,'<span class="tdname">group</span>' );
ff(420,268,200,20,'<span class="tdname">Rh type</span>' );
ff(495,268,200,20,'<span class="tdname">Antibodies</span>' );
ff(642,268,200,20,'<span class="tdname">Rh IG</span>' );
ff(642,278,200,20,'<span class="tdname">Given</span>' );

ff(16,278,200,20,"<%= UtilMisc.JSEscape(props.getProperty("c_ppWt", "")) %>" );
ff(145,275,200,20,"<%= UtilMisc.JSEscape(props.getProperty("ar2_rubella", "")) %>" );
ff(230,275,200,20,"<%= UtilMisc.JSEscape(props.getProperty("ar2_hbs", "")) %>" );
ff(310,275,200,20,"<%= UtilMisc.JSEscape(props.getProperty("ar2_vdrl", "")) %>" );
ff(385,275,200,20,"<%= UtilMisc.JSEscape(props.getProperty("ar2_bloodGroup", "")) %>" );
ff(460,275,200,20,"<%= UtilMisc.JSEscape(props.getProperty("ar2_rh", "")) %>" );
ff(550,275,200,20,"<%= UtilMisc.JSEscape(props.getProperty("ar2_antibodies", "")) %>" );
ff(685,275,200,20,"<%= UtilMisc.JSEscape(props.getProperty("ar2_rhIG", "")) %>" );

//ff(305,296,300,20,'<span class="tdname"><b>Subsequent Visits</b></span>' );
ff(16,320,200,20,'<span class="tdname">Date</span>' );
ff(55,316,200,20,'<span class="tdname">G-age</span>' );
ff(65,327,200,20,'<span class="tdname">wk.</span>' );
ff(98,316,200,20,'<span class="tdname">S-F</span>' );
ff(102,327,200,20,'<span class="tdname">Ht.</span>' );
ff(138,316,200,20,'<span class="tdname">Wt.</span>' );
ff(135,327,200,20,'<span class="tdname">(lb/K)</span>' );
ff(178,316,200,20,'<span class="tdname">Presn</span>' );
ff(179,327,200,20,'<span class="tdname">Posn</span>' );
ff(228,320,200,20,'<span class="tdname">FHR/FM</span>' );
ff(285,314,200,20,'<span class="tdname">Urine</span>' );
ff(278,325,200,20,'<span class="tdname">Pr</span>' );
ff(305,325,200,20,'<span class="tdname">GI</span>' );
ff(340,320,200,20,'<span class="tdname">B.P.</span>' );
ff(500,320,200,20,'<span class="tdname">Comments</span>' );
ff(688,316,100,20,'<span class="tdname">Cig./</span>' );
ff(691,327,100,20,'<span class="tdname">day</span>' );

//if(<%--= lv.equalsIgnoreCase("pg2")--%>) {
    ff(3,345,200,20,'<span class="smalltdname"><%= props.getProperty("pg2_date1", "") %></span>' );
    ff(53,345,200,20,"<%= props.getProperty("pg2_gest1", "") %>" );
    ff(90,345,200,20,"<%= props.getProperty("pg2_ht1", "") %>" );
    ff(132,345,200,20,"<%= props.getProperty("pg2_wt1", "") %>" );
    ff(175,345,200,20,"<%= props.getProperty("pg2_presn1", "") %>" );
    ff(220,345,200,20,"<%= props.getProperty("pg2_FHR1", "") %>" );
    ff(278,345,200,20,"<%= props.getProperty("pg2_urinePr1", "") %>" );
    ff(302,345,200,20,"<%= props.getProperty("pg2_urineGl1", "") %>" );
    ff(328,345,200,20,"<%= props.getProperty("pg2_BP1", "") %>" );
    ff(375,345,310,20,"<%= UtilMisc.JSEscape(props.getProperty("pg2_comments1", "")) %>" );
    ff(692,345,100,20,"<%= props.getProperty("pg2_cig1", "") %>" );

    ff(3,363,200,20,'<span class="smalltdname"><%= props.getProperty("pg2_date2", "") %></span>' );
    ff(53,363,200,20,"<%= props.getProperty("pg2_gest2", "") %>" );
    ff(90,363,200,20,"<%= props.getProperty("pg2_ht2", "") %>" );
    ff(132,363,200,20,"<%= props.getProperty("pg2_wt2", "") %>" );
    ff(175,363,200,20,"<%= props.getProperty("pg2_presn2", "") %>" );
    ff(220,363,200,20,"<%= props.getProperty("pg2_FHR2", "") %>" );
    ff(278,363,200,20,"<%= props.getProperty("pg2_urinePr2", "") %>" );
    ff(302,363,200,20,"<%= props.getProperty("pg2_urineGl2", "") %>" );
    ff(328,363,200,20,"<%= props.getProperty("pg2_BP2", "") %>" );
    ff(375,363,310,20,"<%= UtilMisc.JSEscape(props.getProperty("pg2_comments2", "")) %>" );
    ff(692,363,100,20,"<%= props.getProperty("pg2_cig2", "") %>" );

    ff(3,381,200,20,'<span class="smalltdname"><%= props.getProperty("pg2_date3", "") %></span>' );
    ff(53,381,200,20,"<%= props.getProperty("pg2_gest3", "") %>" );
    ff(90,381,200,20,"<%= props.getProperty("pg2_ht3", "") %>" );
    ff(132,381,200,20,"<%= props.getProperty("pg2_wt3", "") %>" );
    ff(175,381,200,20,"<%= props.getProperty("pg2_presn3", "") %>" );
    ff(220,381,200,20,"<%= props.getProperty("pg2_FHR3", "") %>" );
    ff(278,381,200,20,"<%= props.getProperty("pg2_urinePr3", "") %>" );
    ff(302,381,200,20,"<%= props.getProperty("pg2_urineGl3", "") %>" );
    ff(328,381,200,20,"<%= props.getProperty("pg2_BP3", "") %>" );
    ff(375,381,310,20,"<%= UtilMisc.JSEscape(props.getProperty("pg2_comments3", "")) %>" );
    ff(692,381,100,20,"<%= props.getProperty("pg2_cig3", "") %>" );

    ff(3,399,200,20,'<span class="smalltdname"><%= props.getProperty("pg2_date4", "") %></span>' );
    ff(53,399,200,20,"<%= props.getProperty("pg2_gest4", "") %>" );
    ff(90,399,200,20,"<%= props.getProperty("pg2_ht4", "") %>" );
    ff(132,399,200,20,"<%= props.getProperty("pg2_wt4", "") %>" );
    ff(175,399,200,20,"<%= props.getProperty("pg2_presn4", "") %>" );
    ff(220,399,200,20,"<%= props.getProperty("pg2_FHR4", "") %>" );
    ff(278,399,200,20,"<%= props.getProperty("pg2_urinePr4", "") %>" );
    ff(302,399,200,20,"<%= props.getProperty("pg2_urineGl4", "") %>" );
    ff(328,399,200,20,"<%= props.getProperty("pg2_BP4", "") %>" );
    ff(375,399,310,20,"<%= UtilMisc.JSEscape(props.getProperty("pg2_comments4", "")) %>" );
    ff(692,399,100,20,"<%= props.getProperty("pg2_cig4", "") %>" );

    ff(3,417,200,20,'<span class="smalltdname"><%= props.getProperty("pg2_date5", "") %></span>' );
    ff(53,417,200,20,"<%= props.getProperty("pg2_gest5", "") %>" );
    ff(90,417,200,20,"<%= props.getProperty("pg2_ht5", "") %>" );
    ff(132,417,200,20,"<%= props.getProperty("pg2_wt5", "") %>" );
    ff(175,417,200,20,"<%= props.getProperty("pg2_presn5", "") %>" );
    ff(220,417,200,20,"<%= props.getProperty("pg2_FHR5", "") %>" );
    ff(278,417,200,20,"<%= props.getProperty("pg2_urinePr5", "") %>" );
    ff(302,417,200,20,"<%= props.getProperty("pg2_urineGl5", "") %>" );
    ff(328,417,200,20,"<%= props.getProperty("pg2_BP5", "") %>" );
    ff(375,417,310,20,"<%= UtilMisc.JSEscape(props.getProperty("pg2_comments5", "")) %>" );
    ff(692,417,100,20,"<%= props.getProperty("pg2_cig5", "") %>" );

    ff(3,435,200,20,'<span class="smalltdname"><%= props.getProperty("pg2_date6", "") %></span>' );
    ff(53,435,200,20,"<%= props.getProperty("pg2_gest6", "") %>" );
    ff(90,435,200,20,"<%= props.getProperty("pg2_ht6", "") %>" );
    ff(132,435,200,20,"<%= props.getProperty("pg2_wt6", "") %>" );
    ff(175,435,200,20,"<%= props.getProperty("pg2_presn6", "") %>" );
    ff(220,435,200,20,"<%= props.getProperty("pg2_FHR6", "") %>" );
    ff(278,435,200,20,"<%= props.getProperty("pg2_urinePr6", "") %>" );
    ff(302,435,200,20,"<%= props.getProperty("pg2_urineGl6", "") %>" );
    ff(328,435,200,20,"<%= props.getProperty("pg2_BP6", "") %>" );
    ff(375,435,310,20,"<%= UtilMisc.JSEscape(props.getProperty("pg2_comments6", "")) %>" );
    ff(692,435,100,20,"<%= props.getProperty("pg2_cig6", "") %>" );

    ff(3,453,200,20,'<span class="smalltdname"><%= props.getProperty("pg2_date7", "") %></span>' );
    ff(53,453,200,20,"<%= props.getProperty("pg2_gest7", "") %>" );
    ff(90,453,200,20,"<%= props.getProperty("pg2_ht7", "") %>" );
    ff(132,453,200,20,"<%= props.getProperty("pg2_wt7", "") %>" );
    ff(175,453,200,20,"<%= props.getProperty("pg2_presn7", "") %>" );
    ff(220,453,200,20,"<%= props.getProperty("pg2_FHR7", "") %>" );
    ff(278,453,200,20,"<%= props.getProperty("pg2_urinePr7", "") %>" );
    ff(302,453,200,20,"<%= props.getProperty("pg2_urineGl7", "") %>" );
    ff(328,453,200,20,"<%= props.getProperty("pg2_BP7", "") %>" );
    ff(375,453,310,20,"<%= UtilMisc.JSEscape(props.getProperty("pg2_comments7", "")) %>" );
    ff(692,453,100,20,"<%= props.getProperty("pg2_cig7", "") %>" );

    ff(3,473,200,20,'<span class="smalltdname"><%= props.getProperty("pg2_date8", "") %></span>' );
    ff(53,473,200,20,"<%= props.getProperty("pg2_gest8", "") %>" );
    ff(90,473,200,20,"<%= props.getProperty("pg2_ht8", "") %>" );
    ff(132,473,200,20,"<%= props.getProperty("pg2_wt8", "") %>" );
    ff(175,473,200,20,"<%= props.getProperty("pg2_presn8", "") %>" );
    ff(220,473,200,20,"<%= props.getProperty("pg2_FHR8", "") %>" );
    ff(278,473,200,20,"<%= props.getProperty("pg2_urinePr8", "") %>" );
    ff(302,473,200,20,"<%= props.getProperty("pg2_urineGl8", "") %>" );
    ff(328,473,200,20,"<%= props.getProperty("pg2_BP8", "") %>" );
    ff(375,473,310,20,"<%= UtilMisc.JSEscape(props.getProperty("pg2_comments8", "")) %>" );
    ff(692,473,100,20,"<%= props.getProperty("pg2_cig8", "") %>" );

    ff(3,491,200,20,'<span class="smalltdname"><%= props.getProperty("pg2_date9", "") %></span>' );
    ff(53,491,200,20,"<%= props.getProperty("pg2_gest9", "") %>" );
    ff(90,491,200,20,"<%= props.getProperty("pg2_ht9", "") %>" );
    ff(132,491,200,20,"<%= props.getProperty("pg2_wt9", "") %>" );
    ff(175,491,200,20,"<%= props.getProperty("pg2_presn9", "") %>" );
    ff(220,491,200,20,"<%= props.getProperty("pg2_FHR9", "") %>" );
    ff(278,491,200,20,"<%= props.getProperty("pg2_urinePr9", "") %>" );
    ff(302,491,200,20,"<%= props.getProperty("pg2_urineGl9", "") %>" );
    ff(328,491,200,20,"<%= props.getProperty("pg2_BP9", "") %>" );
    ff(375,491,310,20,"<%= UtilMisc.JSEscape(props.getProperty("pg2_comments9", "")) %>" );
    ff(692,491,100,20,"<%= props.getProperty("pg2_cig9", "") %>" );

    ff(3,509,200,20,'<span class="smalltdname"><%= props.getProperty("pg2_date10", "") %></span>' );
    ff(53,509,200,20,"<%= props.getProperty("pg2_gest10", "") %>" );
    ff(90,509,200,20,"<%= props.getProperty("pg2_ht10", "") %>" );
    ff(132,509,200,20,"<%= props.getProperty("pg2_wt10", "") %>" );
    ff(175,509,200,20,"<%= props.getProperty("pg2_presn10", "") %>" );
    ff(220,509,200,20,"<%= props.getProperty("pg2_FHR10", "") %>" );
    ff(278,509,200,20,"<%= props.getProperty("pg2_urinePr10", "") %>" );
    ff(302,509,200,20,"<%= props.getProperty("pg2_urineGl10", "") %>" );
    ff(328,509,200,20,"<%= props.getProperty("pg2_BP10", "") %>" );
    ff(375,509,310,20,"<%= UtilMisc.JSEscape(props.getProperty("pg2_comments10", "")) %>" );
    ff(692,509,100,20,"<%= props.getProperty("pg2_cig10", "") %>" );

    ff(3,528,200,20,'<span class="smalltdname"><%= props.getProperty("pg2_date11", "") %></span>' );
    ff(53,528,200,20,"<%= props.getProperty("pg2_gest11", "") %>" );
    ff(90,528,200,20,"<%= props.getProperty("pg2_ht11", "") %>" );
    ff(132,528,200,20,"<%= props.getProperty("pg2_wt11", "") %>" );
    ff(175,528,200,20,"<%= props.getProperty("pg2_presn11", "") %>" );
    ff(220,528,200,20,"<%= props.getProperty("pg2_FHR11", "") %>" );
    ff(278,528,200,20,"<%= props.getProperty("pg2_urinePr11", "") %>" );
    ff(302,528,200,20,"<%= props.getProperty("pg2_urineGl11", "") %>" );
    ff(328,528,200,20,"<%= props.getProperty("pg2_BP11", "") %>" );
    ff(375,528,310,20,"<%= UtilMisc.JSEscape(props.getProperty("pg2_comments11", "")) %>" );
    ff(692,528,100,20,"<%= props.getProperty("pg2_cig11", "") %>" );

    ff(3,547,200,20,'<span class="smalltdname"><%= props.getProperty("pg2_date12", "") %></span>' );
    ff(53,547,200,20,"<%= props.getProperty("pg2_gest12", "") %>" );
    ff(90,547,200,20,"<%= props.getProperty("pg2_ht12", "") %>" );
    ff(132,547,200,20,"<%= props.getProperty("pg2_wt12", "") %>" );
    ff(175,547,200,20,"<%= props.getProperty("pg2_presn12", "") %>" );
    ff(220,547,200,20,"<%= props.getProperty("pg2_FHR12", "") %>" );
    ff(278,547,200,20,"<%= props.getProperty("pg2_urinePr12", "") %>" );
    ff(302,547,200,20,"<%= props.getProperty("pg2_urineGl12", "") %>" );
    ff(328,547,200,20,"<%= props.getProperty("pg2_BP12", "") %>" );
    ff(375,547,310,20,"<%= UtilMisc.JSEscape(props.getProperty("pg2_comments12", "")) %>" );
    ff(692,547,100,20,"<%= props.getProperty("pg2_cig12", "") %>" );

    ff(3,566,200,20,'<span class="smalltdname"><%= props.getProperty("pg2_date13", "") %></span>' );
    ff(53,566,200,20,"<%= props.getProperty("pg2_gest13", "") %>" );
    ff(90,566,200,20,"<%= props.getProperty("pg2_ht13", "") %>" );
    ff(132,566,200,20,"<%= props.getProperty("pg2_wt13", "") %>" );
    ff(175,566,200,20,"<%= props.getProperty("pg2_presn13", "") %>" );
    ff(220,566,200,20,"<%= props.getProperty("pg2_FHR13", "") %>" );
    ff(278,566,200,20,"<%= props.getProperty("pg2_urinePr13", "") %>" );
    ff(302,566,200,20,"<%= props.getProperty("pg2_urineGl13", "") %>" );
    ff(328,566,200,20,"<%= props.getProperty("pg2_BP13", "") %>" );
    ff(375,566,310,20,"<%= UtilMisc.JSEscape(props.getProperty("pg2_comments13", "")) %>" );
    ff(692,566,100,20,"<%= props.getProperty("pg2_cig13", "") %>" );

    ff(3,585,200,20,'<span class="smalltdname"><%= props.getProperty("pg2_date14", "") %></span>' );
    ff(53,585,200,20,"<%= props.getProperty("pg2_gest14", "") %>" );
    ff(90,585,200,20,"<%= props.getProperty("pg2_ht14", "") %>" );
    ff(132,585,200,20,"<%= props.getProperty("pg2_wt14", "") %>" );
    ff(175,585,200,20,"<%= props.getProperty("pg2_presn14", "") %>" );
    ff(220,585,200,20,"<%= props.getProperty("pg2_FHR14", "") %>" );
    ff(278,585,200,20,"<%= props.getProperty("pg2_urinePr14", "") %>" );
    ff(302,585,200,20,"<%= props.getProperty("pg2_urineGl14", "") %>" );
    ff(328,585,200,20,"<%= props.getProperty("pg2_BP14", "") %>" );
    ff(375,585,310,20,"<%= UtilMisc.JSEscape(props.getProperty("pg2_comments14", "")) %>" );
    ff(692,585,100,20,"<%= props.getProperty("pg2_cig14", "") %>" );

    ff(3,603,200,20,'<span class="smalltdname"><%= props.getProperty("pg2_date15", "") %></span>' );
    ff(53,603,200,20,"<%= props.getProperty("pg2_gest15", "") %>" );
    ff(90,603,200,20,"<%= props.getProperty("pg2_ht15", "") %>" );
    ff(132,603,200,20,"<%= props.getProperty("pg2_wt15", "") %>" );
    ff(175,603,200,20,"<%= props.getProperty("pg2_presn15", "") %>" );
    ff(220,603,200,20,"<%= props.getProperty("pg2_FHR15", "") %>" );
    ff(278,603,200,20,"<%= props.getProperty("pg2_urinePr15", "") %>" );
    ff(302,603,200,20,"<%= props.getProperty("pg2_urineGl15", "") %>" );
    ff(328,603,200,20,"<%= props.getProperty("pg2_BP15", "") %>" );
    ff(375,603,310,20,"<%= UtilMisc.JSEscape(props.getProperty("pg2_comments15", "")) %>" );
    ff(692,603,100,20,"<%= props.getProperty("pg2_cig15", "") %>" );

    ff(3,621,200,20,'<span class="smalltdname"><%= props.getProperty("pg2_date16", "") %></span>' );
    ff(53,621,200,20,"<%= props.getProperty("pg2_gest16", "") %>" );
    ff(90,621,200,20,"<%= props.getProperty("pg2_ht16", "") %>" );
    ff(132,621,200,20,"<%= props.getProperty("pg2_wt16", "") %>" );
    ff(175,621,200,20,"<%= props.getProperty("pg2_presn16", "") %>" );
    ff(220,621,200,20,"<%= props.getProperty("pg2_FHR16", "") %>" );
    ff(278,621,200,20,"<%= props.getProperty("pg2_urinePr16", "") %>" );
    ff(302,621,200,20,"<%= props.getProperty("pg2_urineGl16", "") %>" );
    ff(328,621,200,20,"<%= props.getProperty("pg2_BP16", "") %>" );
    ff(375,621,310,20,"<%= UtilMisc.JSEscape(props.getProperty("pg2_comments16", "")) %>" );
    ff(692,621,100,20,"<%= props.getProperty("pg2_cig16", "") %>" );

    ff(3,639,200,20,'<span class="smalltdname"><%= props.getProperty("pg2_date17", "") %></span>' );
    ff(53,639,200,20,"<%= props.getProperty("pg2_gest17", "") %>" );
    ff(90,639,200,20,"<%= props.getProperty("pg2_ht17", "") %>" );
    ff(132,639,200,20,"<%= props.getProperty("pg2_wt17", "") %>" );
    ff(175,639,200,20,"<%= props.getProperty("pg2_presn17", "") %>" );
    ff(220,639,200,20,"<%= props.getProperty("pg2_FHR17", "") %>" );
    ff(278,639,200,20,"<%= props.getProperty("pg2_urinePr17", "") %>" );
    ff(302,639,200,20,"<%= props.getProperty("pg2_urineGl17", "") %>" );
    ff(328,639,200,20,"<%= props.getProperty("pg2_BP17", "") %>" );
    ff(375,639,310,20,"<%= UtilMisc.JSEscape(props.getProperty("pg2_comments17", "")) %>" );
    ff(692,639,100,20,"<%= props.getProperty("pg2_cig17", "") %>" );

    ff(6,928,300,20,'<span class="tdname">Signature of attendant</span>' );
    ff(120,938,300,20,"<%= props.getProperty("pg2_signature", "") %>" );
    ff(465,928,300,20,'<span class="tdname">Date (yyyy/mm/dd)</span>' );
    ff(560,938,300,20,"<%= props.getProperty("pg2_formDate", "") %>" );
//} else {
    ff(3,345,200,20,'<span class="smalltdname"><%= props.getProperty("pg3_date18", "") %></span>' );
    ff(53,345,200,20,"<%= props.getProperty("pg3_gest18", "") %>" );
    ff(90,345,200,20,"<%= props.getProperty("pg3_ht18", "") %>" );
    ff(132,345,200,20,"<%= props.getProperty("pg3_wt18", "") %>" );
    ff(175,345,200,20,"<%= props.getProperty("pg3_presn18", "") %>" );
    ff(220,345,200,20,"<%= props.getProperty("pg3_FHR18", "") %>" );
    ff(278,345,200,20,"<%= props.getProperty("pg3_urinePr18", "") %>" );
    ff(302,345,200,20,"<%= props.getProperty("pg3_urineGl18", "") %>" );
    ff(328,345,200,20,"<%= props.getProperty("pg3_BP18", "") %>" );
    ff(375,345,310,20,"<%= UtilMisc.JSEscape(props.getProperty("pg3_comments18", "")) %>" );
    ff(692,345,100,20,"<%= props.getProperty("pg3_cig18", "") %>" );

    ff(3,363,200,20,'<span class="smalltdname"><%= props.getProperty("pg3_date19", "") %></span>' );
    ff(53,363,200,20,"<%= props.getProperty("pg3_gest19", "") %>" );
    ff(90,363,200,20,"<%= props.getProperty("pg3_ht19", "") %>" );
    ff(132,363,200,20,"<%= props.getProperty("pg3_wt19", "") %>" );
    ff(175,363,200,20,"<%= props.getProperty("pg3_presn19", "") %>" );
    ff(220,363,200,20,"<%= props.getProperty("pg3_FHR19", "") %>" );
    ff(278,363,200,20,"<%= props.getProperty("pg3_urinePr19", "") %>" );
    ff(302,363,200,20,"<%= props.getProperty("pg3_urineGl19", "") %>" );
    ff(328,363,200,20,"<%= props.getProperty("pg3_BP19", "") %>" );
    ff(375,363,310,20,"<%= UtilMisc.JSEscape(props.getProperty("pg3_comments19", "")) %>" );
    ff(692,363,100,20,"<%= props.getProperty("pg3_cig19", "") %>" );

    ff(3,381,200,20,'<span class="smalltdname"><%= props.getProperty("pg3_date20", "") %></span>' );
    ff(53,381,200,20,"<%= props.getProperty("pg3_gest20", "") %>" );
    ff(90,381,200,20,"<%= props.getProperty("pg3_ht20", "") %>" );
    ff(132,381,200,20,"<%= props.getProperty("pg3_wt20", "") %>" );
    ff(175,381,200,20,"<%= props.getProperty("pg3_presn20", "") %>" );
    ff(220,381,200,20,"<%= props.getProperty("pg3_FHR20", "") %>" );
    ff(278,381,200,20,"<%= props.getProperty("pg3_urinePr20", "") %>" );
    ff(302,381,200,20,"<%= props.getProperty("pg3_urineGl20", "") %>" );
    ff(328,381,200,20,"<%= props.getProperty("pg3_BP20", "") %>" );
    ff(375,381,310,20,"<%= UtilMisc.JSEscape(props.getProperty("pg3_comments20", "")) %>" );
    ff(692,381,100,20,"<%= props.getProperty("pg3_cig20", "") %>" );

    ff(3,399,200,20,'<span class="smalltdname"><%= props.getProperty("pg3_date21", "") %></span>' );
    ff(53,399,200,20,"<%= props.getProperty("pg3_gest21", "") %>" );
    ff(90,399,200,20,"<%= props.getProperty("pg3_ht21", "") %>" );
    ff(132,399,200,20,"<%= props.getProperty("pg3_wt21", "") %>" );
    ff(175,399,200,20,"<%= props.getProperty("pg3_presn21", "") %>" );
    ff(220,399,200,20,"<%= props.getProperty("pg3_FHR21", "") %>" );
    ff(278,399,200,20,"<%= props.getProperty("pg3_urinePr21", "") %>" );
    ff(302,399,200,20,"<%= props.getProperty("pg3_urineGl21", "") %>" );
    ff(328,399,200,20,"<%= props.getProperty("pg3_BP21", "") %>" );
    ff(375,399,310,20,"<%= UtilMisc.JSEscape(props.getProperty("pg3_comments21", "")) %>" );
    ff(692,399,100,20,"<%= props.getProperty("pg3_cig21", "") %>" );

    ff(3,417,200,20,'<span class="smalltdname"><%= props.getProperty("pg3_date22", "") %></span>' );
    ff(53,417,200,20,"<%= props.getProperty("pg3_gest22", "") %>" );
    ff(90,417,200,20,"<%= props.getProperty("pg3_ht22", "") %>" );
    ff(132,417,200,20,"<%= props.getProperty("pg3_wt22", "") %>" );
    ff(175,417,200,20,"<%= props.getProperty("pg3_presn22", "") %>" );
    ff(220,417,200,20,"<%= props.getProperty("pg3_FHR22", "") %>" );
    ff(278,417,200,20,"<%= props.getProperty("pg3_urinePr22", "") %>" );
    ff(302,417,200,20,"<%= props.getProperty("pg3_urineGl22", "") %>" );
    ff(328,417,200,20,"<%= props.getProperty("pg3_BP22", "") %>" );
    ff(375,417,310,20,"<%= UtilMisc.JSEscape(props.getProperty("pg3_comments22", "")) %>" );
    ff(692,417,100,20,"<%= props.getProperty("pg3_cig22", "") %>" );

    ff(3,435,200,20,'<span class="smalltdname"><%= props.getProperty("pg3_date23", "") %></span>' );
    ff(53,435,200,20,"<%= props.getProperty("pg3_gest23", "") %>" );
    ff(90,435,200,20,"<%= props.getProperty("pg3_ht23", "") %>" );
    ff(132,435,200,20,"<%= props.getProperty("pg3_wt23", "") %>" );
    ff(175,435,200,20,"<%= props.getProperty("pg3_presn23", "") %>" );
    ff(220,435,200,20,"<%= props.getProperty("pg3_FHR23", "") %>" );
    ff(278,435,200,20,"<%= props.getProperty("pg3_urinePr23", "") %>" );
    ff(302,435,200,20,"<%= props.getProperty("pg3_urineGl23", "") %>" );
    ff(328,435,200,20,"<%= props.getProperty("pg3_BP23", "") %>" );
    ff(375,435,310,20,"<%= UtilMisc.JSEscape(props.getProperty("pg3_comments23", "")) %>" );
    ff(692,435,100,20,"<%= props.getProperty("pg3_cig23", "") %>" );

    ff(3,453,200,20,'<span class="smalltdname"><%= props.getProperty("pg3_date24", "") %></span>' );
    ff(53,453,200,20,"<%= props.getProperty("pg3_gest24", "") %>" );
    ff(90,453,200,20,"<%= props.getProperty("pg3_ht24", "") %>" );
    ff(132,453,200,20,"<%= props.getProperty("pg3_wt24", "") %>" );
    ff(175,453,200,20,"<%= props.getProperty("pg3_presn24", "") %>" );
    ff(220,453,200,20,"<%= props.getProperty("pg3_FHR24", "") %>" );
    ff(278,453,200,20,"<%= props.getProperty("pg3_urinePr24", "") %>" );
    ff(302,453,200,20,"<%= props.getProperty("pg3_urineGl24", "") %>" );
    ff(328,453,200,20,"<%= props.getProperty("pg3_BP24", "") %>" );
    ff(375,453,310,20,"<%= UtilMisc.JSEscape(props.getProperty("pg3_comments24", "")) %>" );
    ff(692,453,100,20,"<%= props.getProperty("pg3_cig24", "") %>" );

    ff(3,473,200,20,'<span class="smalltdname"><%= props.getProperty("pg3_date25", "") %></span>' );
    ff(53,473,200,20,"<%= props.getProperty("pg3_gest25", "") %>" );
    ff(90,473,200,20,"<%= props.getProperty("pg3_ht25", "") %>" );
    ff(132,473,200,20,"<%= props.getProperty("pg3_wt25", "") %>" );
    ff(175,473,200,20,"<%= props.getProperty("pg3_presn25", "") %>" );
    ff(220,473,200,20,"<%= props.getProperty("pg3_FHR25", "") %>" );
    ff(278,473,200,20,"<%= props.getProperty("pg3_urinePr25", "") %>" );
    ff(302,473,200,20,"<%= props.getProperty("pg3_urineGl25", "") %>" );
    ff(328,473,200,20,"<%= props.getProperty("pg3_BP25", "") %>" );
    ff(375,473,310,20,"<%= UtilMisc.JSEscape(props.getProperty("pg3_comments25", "")) %>" );
    ff(692,473,100,20,"<%= props.getProperty("pg3_cig25", "") %>" );

    ff(3,491,200,20,'<span class="smalltdname"><%= props.getProperty("pg3_date26", "") %></span>' );
    ff(53,491,200,20,"<%= props.getProperty("pg3_gest26", "") %>" );
    ff(90,491,200,20,"<%= props.getProperty("pg3_ht26", "") %>" );
    ff(132,491,200,20,"<%= props.getProperty("pg3_wt26", "") %>" );
    ff(175,491,200,20,"<%= props.getProperty("pg3_presn26", "") %>" );
    ff(220,491,200,20,"<%= props.getProperty("pg3_FHR26", "") %>" );
    ff(278,491,200,20,"<%= props.getProperty("pg3_urinePr26", "") %>" );
    ff(302,491,200,20,"<%= props.getProperty("pg3_urineGl26", "") %>" );
    ff(328,491,200,20,"<%= props.getProperty("pg3_BP26", "") %>" );
    //ff(375,491,310,20,"<%= props.getProperty("pg3_sv9co", "") %>" );
    ff(375,491,310,20,"<%= UtilMisc.JSEscape(props.getProperty("pg3_comments26", "")) %>" );
    ff(692,491,100,20,"<%= props.getProperty("pg3_cig26", "") %>" );

    ff(3,509,200,20,'<span class="smalltdname"><%= props.getProperty("pg3_date27", "") %></span>' );
    ff(53,509,200,20,"<%= props.getProperty("pg3_gest27", "") %>" );
    ff(90,509,200,20,"<%= props.getProperty("pg3_ht27", "") %>" );
    ff(132,509,200,20,"<%= props.getProperty("pg3_wt27", "") %>" );
    ff(175,509,200,20,"<%= props.getProperty("pg3_presn27", "") %>" );
    ff(220,509,200,20,"<%= props.getProperty("pg3_FHR27", "") %>" );
    ff(278,509,200,20,"<%= props.getProperty("pg3_urinePr27", "") %>" );
    ff(302,509,200,20,"<%= props.getProperty("pg3_urineGl27", "") %>" );
    ff(328,509,200,20,"<%= props.getProperty("pg3_BP27", "") %>" );
    ff(375,509,310,20,"<%= UtilMisc.JSEscape(props.getProperty("pg3_comments27", "")) %>" );
    ff(692,509,100,20,"<%= props.getProperty("pg3_cig27", "") %>" );

    ff(3,528,200,20,'<span class="smalltdname"><%= props.getProperty("pg3_date28", "") %></span>' );
    ff(53,528,200,20,"<%= props.getProperty("pg3_gest28", "") %>" );
    ff(90,528,200,20,"<%= props.getProperty("pg3_ht28", "") %>" );
    ff(132,528,200,20,"<%= props.getProperty("pg3_wt28", "") %>" );
    ff(175,528,200,20,"<%= props.getProperty("pg3_presn28", "") %>" );
    ff(220,528,200,20,"<%= props.getProperty("pg3_FHR28", "") %>" );
    ff(278,528,200,20,"<%= props.getProperty("pg3_urinePr28", "") %>" );
    ff(302,528,200,20,"<%= props.getProperty("pg3_urineGl28", "") %>" );
    ff(328,528,200,20,"<%= props.getProperty("pg3_BP28", "") %>" );
    ff(375,528,310,20,"<%= UtilMisc.JSEscape(props.getProperty("pg3_comments28", "")) %>" );
    ff(692,528,100,20,"<%= props.getProperty("pg3_cig28", "") %>" );

    ff(3,547,200,20,'<span class="smalltdname"><%= props.getProperty("pg3_date29", "") %></span>' );
    ff(53,547,200,20,"<%= props.getProperty("pg3_gest29", "") %>" );
    ff(90,547,200,20,"<%= props.getProperty("pg3_ht29", "") %>" );
    ff(132,547,200,20,"<%= props.getProperty("pg3_wt29", "") %>" );
    ff(175,547,200,20,"<%= props.getProperty("pg3_presn29", "") %>" );
    ff(220,547,200,20,"<%= props.getProperty("pg3_FHR29", "") %>" );
    ff(278,547,200,20,"<%= props.getProperty("pg3_urinePr29", "") %>" );
    ff(302,547,200,20,"<%= props.getProperty("pg3_urineGl29", "") %>" );
    ff(328,547,200,20,"<%= props.getProperty("pg3_BP29", "") %>" );
    ff(375,547,310,20,"<%= UtilMisc.JSEscape(props.getProperty("pg3_comments29", "")) %>" );
    ff(692,547,100,20,"<%= props.getProperty("pg3_cig29", "") %>" );

    ff(3,566,200,20,'<span class="smalltdname"><%= props.getProperty("pg3_date30", "") %></span>' );
    ff(53,566,200,20,"<%= props.getProperty("pg3_gest30", "") %>" );
    ff(90,566,200,20,"<%= props.getProperty("pg3_ht30", "") %>" );
    ff(132,566,200,20,"<%= props.getProperty("pg3_wt30", "") %>" );
    ff(175,566,200,20,"<%= props.getProperty("pg3_presn30", "") %>" );
    ff(220,566,200,20,"<%= props.getProperty("pg3_FHR30", "") %>" );
    ff(278,566,200,20,"<%= props.getProperty("pg3_urinePr30", "") %>" );
    ff(302,566,200,20,"<%= props.getProperty("pg3_urineGl30", "") %>" );
    ff(328,566,200,20,"<%= props.getProperty("pg3_BP30", "") %>" );
    ff(375,566,310,20,"<%= UtilMisc.JSEscape(props.getProperty("pg3_comments30", "")) %>" );
    ff(692,566,100,20,"<%= props.getProperty("pg3_cig30", "") %>" );

    ff(3,585,200,20,'<span class="smalltdname"><%= props.getProperty("pg3_date31", "") %></span>' );
    ff(53,585,200,20,"<%= props.getProperty("pg3_gest31", "") %>" );
    ff(90,585,200,20,"<%= props.getProperty("pg3_ht31", "") %>" );
    ff(132,585,200,20,"<%= props.getProperty("pg3_wt31", "") %>" );
    ff(175,585,200,20,"<%= props.getProperty("pg3_presn31", "") %>" );
    ff(220,585,200,20,"<%= props.getProperty("pg3_FHR31", "") %>" );
    ff(278,585,200,20,"<%= props.getProperty("pg3_urinePr31", "") %>" );
    ff(302,585,200,20,"<%= props.getProperty("pg3_urineGl31", "") %>" );
    ff(328,585,200,20,"<%= props.getProperty("pg3_BP31", "") %>" );
    ff(375,585,310,20,"<%= UtilMisc.JSEscape(props.getProperty("pg3_comments31", "")) %>" );
    ff(692,585,100,20,"<%= props.getProperty("pg3_cig31", "") %>" );

    ff(3,603,200,20,'<span class="smalltdname"><%= props.getProperty("pg3_date32", "") %></span>' );
    ff(53,603,200,20,"<%= props.getProperty("pg3_gest32", "") %>" );
    ff(90,603,200,20,"<%= props.getProperty("pg3_ht32", "") %>" );
    ff(132,603,200,20,"<%= props.getProperty("pg3_wt32", "") %>" );
    ff(175,603,200,20,"<%= props.getProperty("pg3_presn32", "") %>" );
    ff(220,603,200,20,"<%= props.getProperty("pg3_FHR32", "") %>" );
    ff(278,603,200,20,"<%= props.getProperty("pg3_urinePr32", "") %>" );
    ff(302,603,200,20,"<%= props.getProperty("pg3_urineGl32", "") %>" );
    ff(328,603,200,20,"<%= props.getProperty("pg3_BP32", "") %>" );
    ff(375,603,310,20,"<%= UtilMisc.JSEscape(props.getProperty("pg3_comments32", "")) %>" );
    ff(692,603,100,20,"<%= props.getProperty("pg3_cig32", "") %>" );

    ff(3,621,200,20,'<span class="smalltdname"><%= props.getProperty("pg3_date33", "") %></span>' );
    ff(53,621,200,20,"<%= props.getProperty("pg3_gest33", "") %>" );
    ff(90,621,200,20,"<%= props.getProperty("pg3_ht33", "") %>" );
    ff(132,621,200,20,"<%= props.getProperty("pg3_wt33", "") %>" );
    ff(175,621,200,20,"<%= props.getProperty("pg3_presn33", "") %>" );
    ff(220,621,200,20,"<%= props.getProperty("pg3_FHR33", "") %>" );
    ff(278,621,200,20,"<%= props.getProperty("pg3_urinePr33", "") %>" );
    ff(302,621,200,20,"<%= props.getProperty("pg3_urineGl33", "") %>" );
    ff(328,621,200,20,"<%= props.getProperty("pg3_BP33", "") %>" );
    ff(375,621,310,20,"<%= UtilMisc.JSEscape(props.getProperty("pg3_comments33", "")) %>" );
    ff(692,621,100,20,"<%= props.getProperty("pg3_cig33", "") %>" );

    ff(3,639,200,20,'<span class="smalltdname"><%= props.getProperty("pg3_date34", "") %></span>' );
    ff(53,639,200,20,"<%= props.getProperty("pg3_gest34", "") %>" );
    ff(90,639,200,20,"<%= props.getProperty("pg3_ht34", "") %>" );
    ff(132,639,200,20,"<%= props.getProperty("pg3_wt34", "") %>" );
    ff(175,639,200,20,"<%= props.getProperty("pg3_presn34", "") %>" );
    ff(220,639,200,20,"<%= props.getProperty("pg3_FHR34", "") %>" );
    ff(278,639,200,20,"<%= props.getProperty("pg3_urinePr34", "") %>" );
    ff(302,639,200,20,"<%= props.getProperty("pg3_urineGl34", "") %>" );
    ff(328,639,200,20,"<%= props.getProperty("pg3_BP34", "") %>" );
    ff(375,639,310,20,"<%= UtilMisc.JSEscape(props.getProperty("pg3_comments34", "")) %>" );
    ff(692,639,100,20,"<%= props.getProperty("pg3_cig34", "") %>" );

    ff(6,928,300,20,'<span class="tdname">Signature of attendant</span>' );
    ff(120,938,300,20,"<%= props.getProperty("pg3_signature", "") %>" );
    ff(465,928,300,20,'<span class="tdname">Date (yyyy/mm/dd)</span>' );
    ff(560,938,300,20,"<%= props.getProperty("pg3_formDate", "") %>" );
//}

ff(240,665,100,20,'<span class="tdname">Date</span>' );
ff(298,665,100,20,'<span class="tdname">GA</span>' );
ff(410,665,100,20,'<span class="tdname">Result</span>' );

ff(227,683,100,20,'<%= props.getProperty("ar2_uDate1", "") %>' );
ff(285,683,100,20,'<%= props.getProperty("ar2_uGA1", "") %>' );
ff(340,683,300,20,"<%= UtilMisc.JSEscape(props.getProperty("ar2_uResults1", "")) %>" );
ff(227,702,100,20,"<%= props.getProperty("ar2_uDate2", "") %>" );
ff(285,702,100,20,"<%= props.getProperty("ar2_uGA2", "") %>" );
ff(340,702,300,20,"<%= UtilMisc.JSEscape(props.getProperty("ar2_uResults2", "")) %>" );
ff(227,721,100,20,"<%= props.getProperty("ar2_uDate3", "") %>" );
ff(285,721,100,20,"<%= props.getProperty("ar2_uGA3", "") %>" );
ff(340,721,300,20,"<%= UtilMisc.JSEscape(props.getProperty("ar2_uResults3", "")) %>" );
ff(227,738,100,20,"<%= props.getProperty("ar2_uDate4", "") %>" );
ff(285,738,100,20,"<%= props.getProperty("ar2_uGA4", "") %>" );
ff(340,738,300,20,"<%= UtilMisc.JSEscape(props.getProperty("ar2_uResults4", "")) %>" );

ff(530,653,200,20,'<span class="tdname"><b>Referral Plan</b></span>' );
//ff(531,652,200,20,'<span class="tdname"><b>Referral Plan</b></span>' );
ff(517,665,50,20,"<%= props.getProperty("ar2_obstetrician", "").equals("")?"":"X"%>" );
ff(530,664,200,20,'<span class="tdname">Obstetrician</span>' );
ff(517,677,50,20,"<%= props.getProperty("ar2_pediatrician", "").equals("")?"":"X"%>" );
ff(530,676,200,20,'<span class="tdname">Pediatrician</span>' );
ff(517,689,50,20,"<%= props.getProperty("ar2_anesthesiologist", "").equals("")?"":"X"%>" );
ff(530,688,200,20,'<span class="tdname">Anesthesiologist</span>' );
ff(517,701,50,20,"<%= props.getProperty("ar2_socialWorker", "").equals("")?"":"X"%>" );
ff(530,700,200,20,'<span class="tdname">Social worker</span>' );
ff(517,714,50,20,"<%= props.getProperty("ar2_dietician", "").equals("")?"":"X"%>" );
ff(530,712,200,20,'<span class="tdname">Dietitian</span>' );
ff(517,725,50,20,"<%= props.getProperty("ar2_otherAR2", "").equals("")?"":"X"%>" );
ff(530,724,200,20,'<span class="tdname">Other</span>' );
ff(517,737,300,20,"<%= UtilMisc.JSEscape(props.getProperty("ar2_otherBox", "")) %>" );

ff(617,653,200,20,'<span class="tdname"><b>Discussion Topics</b></span>' );
ff(612,665,50,20,"<%= props.getProperty("ar2_drugUse", "").equals("")?"":"X"%>" );
ff(624,664,200,20,'<span class="tdname">Drug use</span>' );
ff(612,677,50,20,"<%= props.getProperty("ar2_smoking", "").equals("")?"":"X"%>" );
ff(624,676,200,20,'<span class="tdname">Smoking</span>' );
ff(612,689,50,20,"<%= props.getProperty("ar2_alcohol", "").equals("")?"":"X"%>" );
ff(624,688,200,20,'<span class="tdname">Alcohol</span>' );
ff(612,701,50,20,"<%= props.getProperty("ar2_exercise", "").equals("")?"":"X"%>" );
ff(624,700,200,20,'<span class="tdname">Exercise</span>' );
ff(612,713,50,20,"<%= props.getProperty("ar2_workPlan", "").equals("")?"":"X"%>" );
ff(624,712,200,20,'<span class="tdname">Work plan</span>' );
ff(612,725,50,20,"<%= props.getProperty("ar2_intercourse", "").equals("")?"":"X"%>" );
ff(624,724,200,20,'<span class="tdname">Intercourse</span>' );
ff(612,737,50,20,"<%= props.getProperty("ar2_dental", "").equals("")?"":"X"%>" );
ff(624,736,200,20,'<span class="tdname">Dental care</span>' );
ff(612,749,50,20,"<%= props.getProperty("ar2_travel", "").equals("")?"":"X"%>" );
ff(624,748,200,20,'<span class="tdname">Travel</span>' );

ff(612,761,50,20,"<%= props.getProperty("ar2_prenatal", "").equals("")?"":"X"%>" );
ff(624,760,200,20,'<span class="tdname">Prenatal classes</span>' );
ff(612,773,50,20,"<%= props.getProperty("ar2_breast", "").equals("")?"":"X"%>" );
ff(624,772,200,20,'<span class="tdname">Breast feeding</span>' );
ff(612,785,50,20,"<%= props.getProperty("ar2_birth", "").equals("")?"":"X"%>" );
ff(624,784,200,20,'<span class="tdname">Birth plan</span>' );
ff(612,797,50,20,"<%= props.getProperty("ar2_preterm", "").equals("")?"":"X"%>" );
ff(624,796,200,20,'<span class="tdname">Preterm labour</span>' );
ff(612,809,50,20,"<%= props.getProperty("ar2_prom", "").equals("")?"":"X"%>" );
ff(624,808,200,20,'<span class="tdname">PROM</span>' );
ff(612,821,50,20,"<%= props.getProperty("ar2_fetal", "").equals("")?"":"X"%>" );
ff(624,820,200,20,'<span class="tdname">Fetal movement</span>' );
ff(612,833,50,20,"<%= props.getProperty("ar2_admission", "").equals("")?"":"X"%>" );
ff(624,832,200,20,'<span class="tdname">Admission timing</span>' );
ff(612,845,50,20,"<%= props.getProperty("ar2_labour", "").equals("")?"":"X"%>" );
ff(624,844,200,20,'<span class="tdname">Labour support</span>' );
ff(612,857,50,20,"<%= props.getProperty("ar2_pain", "").equals("")?"":"X"%>" );
ff(624,856,200,20,'<span class="tdname">Pain management</span>' );
ff(612,869,50,20,"<%= props.getProperty("ar2_depression", "").equals("")?"":"X"%>" );
ff(624,868,200,20,'<span class="tdname">Depression</span>' );
ff(612,881,50,20,"<%= props.getProperty("ar2_circumcision", "").equals("")?"":"X"%>" );
ff(624,880,200,20,'<span class="tdname">Circumcision</span>' );
ff(612,893,50,20,"<%= props.getProperty("ar2_car", "").equals("")?"":"X"%>" );
ff(624,892,200,20,'<span class="tdname">Car safety</span>' );
ff(612,905,50,20,"<%= props.getProperty("ar2_contraception", "").equals("")?"":"X"%>" );
ff(624,904,200,20,'<span class="tdname">Contraception</span>' );
ff(612,917,50,20,"<%= props.getProperty("ar2_onCall", "").equals("")?"":"X"%>" );
ff(624,916,200,20,'<span class="tdname">On call</span>' );

ff(232,765,300,20,'<span class="tdname">1. Pap</span>' );
ff(339,765,200,20,"<%= UtilMisc.JSEscape(props.getProperty("ar2_pap", "")) %>" );
ff(232,780,300,20,'<span class="tdname">2. GC/Chlamydia</span>' );
ff(339,780,200,20,"<%= UtilMisc.JSEscape(props.getProperty("ar2_chlamydia", "")) %>" );
ff(232,792,300,20,'<span class="tdname">3. HIV</span>' );
ff(339,792,200,20,"<%= UtilMisc.JSEscape(props.getProperty("ar2_hiv", "")) %>" );
ff(232,804,300,20,'<span class="tdname">4. B. vaginosis</span>' );
ff(339,804,200,20,"<%= UtilMisc.JSEscape(props.getProperty("ar2_vaginosis", "")) %>" );
ff(232,816,300,20,'<span class="tdname">5. Group B strep</span>' );
ff(339,816,200,20,"<%= UtilMisc.JSEscape(props.getProperty("ar2_strep", "")) %>" );
ff(232,828,300,20,'<span class="tdname">6. Urine culture</span>' );
ff(339,828,200,20,"<%= UtilMisc.JSEscape(props.getProperty("ar2_urineCulture", "")) %>" );
ff(232,840,300,20,'<span class="tdname">7. Sickle dex</span>' );
ff(339,840,200,20,"<%= UtilMisc.JSEscape(props.getProperty("ar2_sickleDex", "")) %>" );
ff(232,852,300,20,'<span class="tdname">8. Hb electro</span>' );
ff(339,852,200,20,"<%= UtilMisc.JSEscape(props.getProperty("ar2_electro", "")) %>" );
ff(232,864,300,20,'<span class="tdname">9. Amnio/CVS</span>' );
ff(339,864,200,20,"<%= UtilMisc.JSEscape(props.getProperty("ar2_amnio", "")) %>" );
ff(232,876,300,20,'<span class="tdname">10. Glucose screen</span>' );
ff(339,876,200,20,"<%= UtilMisc.JSEscape(props.getProperty("ar2_glucose", "")) %>" );
ff(232,888,300,20,'<span class="tdname">11. Other</span>' );
ff(339,888,200,20,"<%= UtilMisc.JSEscape(props.getProperty("ar2_otherAR2Name", "")) %>" );
ff(235,900,200,20,"<%= UtilMisc.JSEscape(props.getProperty("ar2_otherResult", "")) %>" );

ff(405,900,300,50,"<%= UtilMisc.JSEscape(props.getProperty("ar2_psych", "")) %>" );


ff(15,956,200,20,'<span class="smalltdname">0375-64 (99/08)</span>' );
ff(190,956,350,20,"<span class=\"smalltdname\">Canary - Mother's chart - forward to hospital  Pink - Attendant's copy  White - Infant's chart</span>" );
ff(665,956,200,20,'<span class="smalltdname">7530-4654</span>' );
</script>

<div ID="bdiv1"
	STYLE="position:absolute; visibility:visible; z-index:2; left:<%=10+oox%>px; top:<%=ooy+172%>px; width:330px; height:60px;">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td><%= props.getProperty("c_riskFactors", "").replaceAll("\\n","<br>")%>
		</td>
	</tr>
</table>
</div>
<div ID="bdiv1"
	STYLE="position:absolute; visibility:visible; z-index:2; left:<%=347+oox%>px; top:<%=ooy+172%>px; width:180px; height:50px;">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td><%= props.getProperty("c_allergies", "") %></td>
	</tr>
</table>
</div>
<div ID="bdiv1"
	STYLE="position:absolute; visibility:visible; z-index:2; left:<%=530+oox%>px; top:<%=ooy+172%>px; width:180px; height:50px;">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td><%= props.getProperty("c_meds", "") %></td>
	</tr>
</table>
</div>
<div ID="bdiv1"
	STYLE="position:absolute; visibility:visible; z-index:2; left:<%=410+oox%>px; top:<%=ooy+765%>px; width:210px; height:80px;">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td><%= props.getProperty("ar2_commentsAR2", "") %></td>
	</tr>
</table>
</div>

<%
String fedb = props.getProperty("c_finalEDB", "");
String urlparam = "";

int width = 200, height = 235 ;
int x = 0, y = 0;
int ox = 21, oy = 254;
float dx = 0f, dy = 0f;
int ipos = 0;

if (!fedb.equals("") ) {
	FrmGraphicAR arG = new FrmGraphicAR();
    for (int i = 1; i < 18; i++) {
        if (!props.getProperty(("pg2_date" + i), "").equals("") ) {
			dx = arG.getWeekByEDB(fedb, props.getProperty(("pg2_date" + i)));
			if (arG.getWeekInt() < 19 || arG.getWeekInt() > 40 || arG.getHt(props.getProperty(("pg2_ht" + i), "")).equals("") || !props.getProperty(("pg2_ht" + i), "").matches("\\d*\\.?\\d+") || arG.getHt(props.getProperty(("pg2_ht" + i), "")).equals("-")) continue;
       		dy = Float.parseFloat(arG.getHt(props.getProperty("pg2_ht" + i)));
	        x = (int) ((ox + (dx -19) * width / (11.5 * 2)) -2) ;
	        y = (int) ((oy - (dy - 11.818) * height / (5.636 * 5)) -1) ;
            urlparam += "&x" + (i-1) + "=" + x + "|" + y;
		}
	}
    for (int i = 18; i < 35; i++) {
        if (!props.getProperty(("pg3_date" + i), "").equals("") ) {
			dx = arG.getWeekByEDB(fedb, props.getProperty(("pg3_date" + i)));
			if (arG.getWeekInt() < 19 || arG.getWeekInt() > 40 || arG.getHt(props.getProperty(("pg3_ht" + i), "")).equals("") || !props.getProperty(("pg3_ht" + i), "").matches("\\d*\\.?\\d+") || arG.getHt(props.getProperty(("pg3_ht" + i), "")).equals("-")) continue;
       		dy = Float.parseFloat(arG.getHt(props.getProperty("pg3_ht" + i)));
	        x = (int) ((ox + (dx -19) * width / (11.5 * 2)) -2) ;
	        y = (int) ((oy - (dy - 11.818) * height / (5.636 * 5)) -1) ;
            urlparam += "&x" + (i-1) + "=" + x + "|" + y;
		}
	}
%>
<div ID="graphic"
	STYLE="position:absolute; visibility:visible; z-index:2; left:<%=3+oox%>px; top:<%=653+ooy%>px; width:221px; height:276px;">
<embed type="image/svg+xml"
	src="formar2svg.jsp?bgimage=<%=URLEncoder.encode("../images/formar2_99_08gra.gif")%>&bgimagewidth=222&bgimageheight=276<%=urlparam%>"
	width="221" height="276" wmode="transparent" /></div>
<%
}
%>
</body>
</html>
