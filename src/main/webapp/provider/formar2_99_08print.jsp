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
  int oox=0, ooy=0;
  if(request.getParameter("oox")!=null) oox = Integer.parseInt(request.getParameter("oox"));
  if(request.getParameter("ooy")!=null) ooy = Integer.parseInt(request.getParameter("ooy"));
%>
<%@ page
	import="java.util.*, java.sql.*, java.net.*, oscar.*, oscar.util.UtilMisc, oscar.form.graphic.*"
	errorPage="errorpage.jsp"%>

<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>ANTENATAL RECORD</title>
<link rel="stylesheet" href="antenatalrecordprint.css">
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
<body onLoad="setfocus()" topmargin="0" leftmargin="1" rightmargin="1"
	bgcolor="navy">
<img src="../images/formar2_99_08.gif">
<div ID="bdiv1"
	STYLE="position:absolute; visibility:visible; z-index:2; left:<%=50+oox%>px; top:<%=76+ooy%>px; width:400px; height:20px;">
<%=Misc.JSEscape(request.getParameter("xml_name"))%></div>

<script language="JavaScript">
ff(750,10,1,10,'<span class="title"><a href=# onClick="window.print()">Print</a></span>' );
ff(750,40,1,10,'<span class="title"><a href=# onClick="history.go(-1);return false;">Back</a></span>' );
ff(192,0,300,20,'<span class="title">Antenatal Record 2</span>' );
ff(9,65,100,20,'<span class="tdname">Name</span>' );
ff(8,87,100,20,'<span class="tdname">Address</span>' );
ff(8,111,200,20,'<span class="tdname">Birth attendants</span>' );
ff(191,111,200,20,'<span class="tdname">Newborn care</span>' );

ff(50,97,500,20,"<%=Misc.JSEscape(request.getParameter("xml_address"))%>" );
ff(12,122,200,20,"<%=Misc.JSEscape(request.getParameter("xml_ba"))%>");
ff(195,122,250,20,"<%=Misc.JSEscape(request.getParameter("xml_nc"))%>" );

//ff(215,138,600,20,'<span class="tdname"><b>Summary of Risk Factors, Allergies, and Medications</b></span>' );
ff(146,155,200,20,'<span class="tdname">Risk Factors</span>' );
ff(418,155,200,20,'<span class="tdname">Allergies</span>' );
ff(596,155,200,20,'<span class="tdname">Medications</span>' );

//ff(6,228,200,20,'<span class="tdname1"><b>Final EDB</b> (yyyy/mm/dd)</span>' );
ff(496,228,100,20,'<span class="tdname">Hb</span>' );
ff(555,228,100,20,'<span class="tdname">MCV</span>' );
ff(607,228,100,20,'<span class="tdname">MSS</span>' );

ff(16,245,200,20,"<%=Misc.JSEscape(request.getParameter("xml_fedb"))%>" );
ff(140,245,200,20,"<%=Misc.JSEscape(request.getParameter("xml_gra"))%>" );
ff(220,245,200,20,"<%=Misc.JSEscape(request.getParameter("xml_term"))%>" );
ff(300,245,200,20,"<%=Misc.JSEscape(request.getParameter("xml_prem"))%>" );
ff(380,245,200,20,"<%=Misc.JSEscape(request.getParameter("xml_etss"))%>" );
ff(450,245,200,20,"<%=Misc.JSEscape(request.getParameter("xml_liv"))%>" );
ff(493,245,200,20,"<%=Misc.JSEscape(request.getParameter("xml_hb"))%>" );
ff(550,245,200,20,"<%=Misc.JSEscape(request.getParameter("xml_mcv"))%>" );
ff(605,245,200,20,"<%=Misc.JSEscape(request.getParameter("xml_mss"))%>" );

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

ff(16,278,200,20,"<%=Misc.JSEscape(request.getParameter("xml_ppw"))%>" );
ff(145,275,200,20,"<%=Misc.JSEscape(request.getParameter("xml_rub"))%>" );
ff(230,275,200,20,"<%=Misc.JSEscape(request.getParameter("xml_hbs"))%>" );
ff(310,275,200,20,"<%=Misc.JSEscape(request.getParameter("xml_vdr"))%>" );
ff(385,275,200,20,"<%=Misc.JSEscape(request.getParameter("xml_blo"))%>" );
ff(460,275,200,20,"<%=Misc.JSEscape(request.getParameter("xml_rhp"))%>" );
ff(550,275,200,20,"<%=Misc.JSEscape(request.getParameter("xml_anti"))%>" );
ff(685,275,200,20,"<%=Misc.JSEscape(request.getParameter("xml_rhg"))%>" );

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

ff(3,345,200,20,'<span class="smalltdname"><%=Misc.JSEscape(request.getParameter("xml_sv1da"))%></span>' );
ff(53,345,200,20,"<%=Misc.JSEscape(request.getParameter("xml_sv1ga"))%>" );
ff(90,345,200,20,"<%=Misc.JSEscape(request.getParameter("xml_sv1sf"))%>" );
ff(132,345,200,20,"<%=Misc.JSEscape(request.getParameter("xml_sv1wt"))%>" );
ff(175,345,200,20,"<%=Misc.JSEscape(request.getParameter("xml_sv1pr"))%>" );
ff(220,345,200,20,"<%=Misc.JSEscape(request.getParameter("xml_sv1fh"))%>" );
ff(278,345,200,20,"<%=Misc.JSEscape(request.getParameter("xml_sv1upr"))%>" );
ff(302,345,200,20,"<%=Misc.JSEscape(request.getParameter("xml_sv1ugl"))%>" );
ff(328,345,200,20,"<%=Misc.JSEscape(request.getParameter("xml_sv1bp"))%>" );
ff(692,345,100,20,"<%=Misc.JSEscape(request.getParameter("xml_sv1ra"))%>" );

ff(3,363,200,20,'<span class="smalltdname"><%=Misc.JSEscape(request.getParameter("xml_sv2da"))%></span>' );
ff(53,363,200,20,"<%=Misc.JSEscape(request.getParameter("xml_sv2ga"))%>" );
ff(90,363,200,20,"<%=Misc.JSEscape(request.getParameter("xml_sv2sf"))%>" );
ff(132,363,200,20,"<%=Misc.JSEscape(request.getParameter("xml_sv2wt"))%>" );
ff(175,363,200,20,"<%=Misc.JSEscape(request.getParameter("xml_sv2pr"))%>" );
ff(220,363,200,20,"<%=Misc.JSEscape(request.getParameter("xml_sv2fh"))%>" );
ff(278,363,200,20,"<%=Misc.JSEscape(request.getParameter("xml_sv2upr"))%>" );
ff(302,363,200,20,"<%=Misc.JSEscape(request.getParameter("xml_sv2ugl"))%>" );
ff(328,363,200,20,"<%=Misc.JSEscape(request.getParameter("xml_sv2bp"))%>" );
//ff(375,363,310,20,"<%=Misc.JSEscape(request.getParameter("xml_sv2co"))%>" );
ff(692,363,100,20,"<%=Misc.JSEscape(request.getParameter("xml_sv2ra"))%>" );

ff(3,381,200,20,'<span class="smalltdname"><%=Misc.JSEscape(request.getParameter("xml_sv3da"))%></span>' );
ff(53,381,200,20,"<%=Misc.JSEscape(request.getParameter("xml_sv3ga"))%>" );
ff(90,381,200,20,"<%=Misc.JSEscape(request.getParameter("xml_sv3sf"))%>" );
ff(132,381,200,20,"<%=Misc.JSEscape(request.getParameter("xml_sv3wt"))%>" );
ff(175,381,200,20,"<%=Misc.JSEscape(request.getParameter("xml_sv3pr"))%>" );
ff(220,381,200,20,"<%=Misc.JSEscape(request.getParameter("xml_sv3fh"))%>" );
ff(278,381,200,20,"<%=Misc.JSEscape(request.getParameter("xml_sv3upr"))%>" );
ff(302,381,200,20,"<%=Misc.JSEscape(request.getParameter("xml_sv3ugl"))%>" );
ff(328,381,200,20,"<%=Misc.JSEscape(request.getParameter("xml_sv3bp"))%>" );
//ff(375,381,310,20,"<%=Misc.JSEscape(request.getParameter("xml_sv3co"))%>" );
ff(692,381,100,20,"<%=Misc.JSEscape(request.getParameter("xml_sv3ra"))%>" );

ff(3,399,200,20,'<span class="smalltdname"><%=Misc.JSEscape(request.getParameter("xml_sv4da"))%></span>' );
ff(53,399,200,20,"<%=Misc.JSEscape(request.getParameter("xml_sv4ga"))%>" );
ff(90,399,200,20,"<%=Misc.JSEscape(request.getParameter("xml_sv4sf"))%>" );
ff(132,399,200,20,"<%=Misc.JSEscape(request.getParameter("xml_sv4wt"))%>" );
ff(175,399,200,20,"<%=Misc.JSEscape(request.getParameter("xml_sv4pr"))%>" );
ff(220,399,200,20,"<%=Misc.JSEscape(request.getParameter("xml_sv4fh"))%>" );
ff(278,399,200,20,"<%=Misc.JSEscape(request.getParameter("xml_sv4upr"))%>" );
ff(302,399,200,20,"<%=Misc.JSEscape(request.getParameter("xml_sv4ugl"))%>" );
ff(328,399,200,20,"<%=Misc.JSEscape(request.getParameter("xml_sv4bp"))%>" );
//ff(375,399,310,20,"<%=Misc.JSEscape(request.getParameter("xml_sv4co"))%>" );
ff(692,399,100,20,"<%=Misc.JSEscape(request.getParameter("xml_sv4ra"))%>" );

ff(3,417,200,20,'<span class="smalltdname"><%=Misc.JSEscape(request.getParameter("xml_sv5da"))%></span>' );
ff(53,417,200,20,"<%=Misc.JSEscape(request.getParameter("xml_sv5ga"))%>" );
ff(90,417,200,20,"<%=Misc.JSEscape(request.getParameter("xml_sv5sf"))%>" );
ff(132,417,200,20,"<%=Misc.JSEscape(request.getParameter("xml_sv5wt"))%>" );
ff(175,417,200,20,"<%=Misc.JSEscape(request.getParameter("xml_sv5pr"))%>" );
ff(220,417,200,20,"<%=Misc.JSEscape(request.getParameter("xml_sv5fh"))%>" );
ff(278,417,200,20,"<%=Misc.JSEscape(request.getParameter("xml_sv5upr"))%>" );
ff(302,417,200,20,"<%=Misc.JSEscape(request.getParameter("xml_sv5ugl"))%>" );
ff(328,417,200,20,"<%=Misc.JSEscape(request.getParameter("xml_sv5bp"))%>" );
//ff(375,417,310,20,"<%=Misc.JSEscape(request.getParameter("xml_sv5co"))%>" );
ff(692,417,100,20,"<%=Misc.JSEscape(request.getParameter("xml_sv5ra"))%>" );

ff(3,435,200,20,'<span class="smalltdname"><%=Misc.JSEscape(request.getParameter("xml_sv6da"))%></span>' );
ff(53,435,200,20,"<%=Misc.JSEscape(request.getParameter("xml_sv6ga"))%>" );
ff(90,435,200,20,"<%=Misc.JSEscape(request.getParameter("xml_sv6sf"))%>" );
ff(132,435,200,20,"<%=Misc.JSEscape(request.getParameter("xml_sv6wt"))%>" );
ff(175,435,200,20,"<%=Misc.JSEscape(request.getParameter("xml_sv6pr"))%>" );
ff(220,435,200,20,"<%=Misc.JSEscape(request.getParameter("xml_sv6fh"))%>" );
ff(278,435,200,20,"<%=Misc.JSEscape(request.getParameter("xml_sv6upr"))%>" );
ff(302,435,200,20,"<%=Misc.JSEscape(request.getParameter("xml_sv6ugl"))%>" );
ff(328,435,200,20,"<%=Misc.JSEscape(request.getParameter("xml_sv6bp"))%>" );
//ff(375,435,310,20,"<%=Misc.JSEscape(request.getParameter("xml_sv6co"))%>" );
ff(692,435,100,20,"<%=Misc.JSEscape(request.getParameter("xml_sv6ra"))%>" );

ff(3,453,200,20,'<span class="smalltdname"><%=Misc.JSEscape(request.getParameter("xml_sv7da"))%></span>' );
ff(53,453,200,20,"<%=Misc.JSEscape(request.getParameter("xml_sv7ga"))%>" );
ff(90,453,200,20,"<%=Misc.JSEscape(request.getParameter("xml_sv7sf"))%>" );
ff(132,453,200,20,"<%=Misc.JSEscape(request.getParameter("xml_sv7wt"))%>" );
ff(175,453,200,20,"<%=Misc.JSEscape(request.getParameter("xml_sv7pr"))%>" );
ff(220,453,200,20,"<%=Misc.JSEscape(request.getParameter("xml_sv7fh"))%>" );
ff(278,453,200,20,"<%=Misc.JSEscape(request.getParameter("xml_sv7upr"))%>" );
ff(302,453,200,20,"<%=Misc.JSEscape(request.getParameter("xml_sv7ugl"))%>" );
ff(328,453,200,20,"<%=Misc.JSEscape(request.getParameter("xml_sv7bp"))%>" );
//ff(375,453,310,20,"<%=Misc.JSEscape(request.getParameter("xml_sv7co"))%>" );
ff(692,453,100,20,"<%=Misc.JSEscape(request.getParameter("xml_sv7ra"))%>" );

ff(3,473,200,20,'<span class="smalltdname"><%=Misc.JSEscape(request.getParameter("xml_sv8da"))%></span>' );
ff(53,473,200,20,"<%=Misc.JSEscape(request.getParameter("xml_sv8ga"))%>" );
ff(90,473,200,20,"<%=Misc.JSEscape(request.getParameter("xml_sv8sf"))%>" );
ff(132,473,200,20,"<%=Misc.JSEscape(request.getParameter("xml_sv8wt"))%>" );
ff(175,473,200,20,"<%=Misc.JSEscape(request.getParameter("xml_sv8pr"))%>" );
ff(220,473,200,20,"<%=Misc.JSEscape(request.getParameter("xml_sv8fh"))%>" );
ff(278,473,200,20,"<%=Misc.JSEscape(request.getParameter("xml_sv8upr"))%>" );
ff(302,473,200,20,"<%=Misc.JSEscape(request.getParameter("xml_sv8ugl"))%>" );
ff(328,473,200,20,"<%=Misc.JSEscape(request.getParameter("xml_sv8bp"))%>" );
//ff(375,473,310,20,"<%=Misc.JSEscape(request.getParameter("xml_sv8co"))%>" );
ff(692,473,100,20,"<%=Misc.JSEscape(request.getParameter("xml_sv8ra"))%>" );

ff(3,491,200,20,'<span class="smalltdname"><%=Misc.JSEscape(request.getParameter("xml_sv9da"))%></span>' );
ff(53,491,200,20,"<%=Misc.JSEscape(request.getParameter("xml_sv9ga"))%>" );
ff(90,491,200,20,"<%=Misc.JSEscape(request.getParameter("xml_sv9sf"))%>" );
ff(132,491,200,20,"<%=Misc.JSEscape(request.getParameter("xml_sv9wt"))%>" );
ff(175,491,200,20,"<%=Misc.JSEscape(request.getParameter("xml_sv9pr"))%>" );
ff(220,491,200,20,"<%=Misc.JSEscape(request.getParameter("xml_sv9fh"))%>" );
ff(278,491,200,20,"<%=Misc.JSEscape(request.getParameter("xml_sv9upr"))%>" );
ff(302,491,200,20,"<%=Misc.JSEscape(request.getParameter("xml_sv9ugl"))%>" );
ff(328,491,200,20,"<%=Misc.JSEscape(request.getParameter("xml_sv9bp"))%>" );
//ff(375,491,310,20,"<%=Misc.JSEscape(request.getParameter("xml_sv9co"))%>" );
ff(692,491,100,20,"<%=Misc.JSEscape(request.getParameter("xml_sv9ra"))%>" );

ff(3,509,200,20,'<span class="smalltdname"><%=Misc.JSEscape(request.getParameter("xml_sv10da"))%></span>' );
ff(53,509,200,20,"<%=Misc.JSEscape(request.getParameter("xml_sv10ga"))%>" );
ff(90,509,200,20,"<%=Misc.JSEscape(request.getParameter("xml_sv10sf"))%>" );
ff(132,509,200,20,"<%=Misc.JSEscape(request.getParameter("xml_sv10wt"))%>" );
ff(175,509,200,20,"<%=Misc.JSEscape(request.getParameter("xml_sv10pr"))%>" );
ff(220,509,200,20,"<%=Misc.JSEscape(request.getParameter("xml_sv10fh"))%>" );
ff(278,509,200,20,"<%=Misc.JSEscape(request.getParameter("xml_sv10upr"))%>" );
ff(302,509,200,20,"<%=Misc.JSEscape(request.getParameter("xml_sv10ugl"))%>" );
ff(328,509,200,20,"<%=Misc.JSEscape(request.getParameter("xml_sv10bp"))%>" );
//ff(375,509,310,20,"<%=Misc.JSEscape(request.getParameter("xml_sv10co"))%>" );
ff(692,509,100,20,"<%=Misc.JSEscape(request.getParameter("xml_sv10ra"))%>" );

ff(3,528,200,20,'<span class="smalltdname"><%=Misc.JSEscape(request.getParameter("xml_sv11da"))%></span>' );
ff(53,528,200,20,"<%=Misc.JSEscape(request.getParameter("xml_sv11ga"))%>" );
ff(90,528,200,20,"<%=Misc.JSEscape(request.getParameter("xml_sv11sf"))%>" );
ff(132,528,200,20,"<%=Misc.JSEscape(request.getParameter("xml_sv11wt"))%>" );
ff(175,528,200,20,"<%=Misc.JSEscape(request.getParameter("xml_sv11pr"))%>" );
ff(220,528,200,20,"<%=Misc.JSEscape(request.getParameter("xml_sv11fh"))%>" );
ff(278,528,200,20,"<%=Misc.JSEscape(request.getParameter("xml_sv11upr"))%>" );
ff(302,528,200,20,"<%=Misc.JSEscape(request.getParameter("xml_sv11ugl"))%>" );
ff(328,528,200,20,"<%=Misc.JSEscape(request.getParameter("xml_sv11bp"))%>" );
//ff(375,528,310,20,"<%=Misc.JSEscape(request.getParameter("xml_sv11co"))%>" );
ff(692,528,100,20,"<%=Misc.JSEscape(request.getParameter("xml_sv11ra"))%>" );

ff(3,547,200,20,'<span class="smalltdname"><%=Misc.JSEscape(request.getParameter("xml_sv12da"))%></span>' );
ff(53,547,200,20,"<%=Misc.JSEscape(request.getParameter("xml_sv12ga"))%>" );
ff(90,547,200,20,"<%=Misc.JSEscape(request.getParameter("xml_sv12sf"))%>" );
ff(132,547,200,20,"<%=Misc.JSEscape(request.getParameter("xml_sv12wt"))%>" );
ff(175,547,200,20,"<%=Misc.JSEscape(request.getParameter("xml_sv12pr"))%>" );
ff(220,547,200,20,"<%=Misc.JSEscape(request.getParameter("xml_sv12fh"))%>" );
ff(278,547,200,20,"<%=Misc.JSEscape(request.getParameter("xml_sv12upr"))%>" );
ff(302,547,200,20,"<%=Misc.JSEscape(request.getParameter("xml_sv12ugl"))%>" );
ff(328,547,200,20,"<%=Misc.JSEscape(request.getParameter("xml_sv12bp"))%>" );
//ff(375,547,310,20,"<%=Misc.JSEscape(request.getParameter("xml_sv12co"))%>" );
ff(692,547,100,20,"<%=Misc.JSEscape(request.getParameter("xml_sv12ra"))%>" );

ff(3,566,200,20,'<span class="smalltdname"><%=Misc.JSEscape(request.getParameter("xml_sv13da"))%></span>' );
ff(53,566,200,20,"<%=Misc.JSEscape(request.getParameter("xml_sv13ga"))%>" );
ff(90,566,200,20,"<%=Misc.JSEscape(request.getParameter("xml_sv13sf"))%>" );
ff(132,566,200,20,"<%=Misc.JSEscape(request.getParameter("xml_sv13wt"))%>" );
ff(175,566,200,20,"<%=Misc.JSEscape(request.getParameter("xml_sv13pr"))%>" );
ff(220,566,200,20,"<%=Misc.JSEscape(request.getParameter("xml_sv13fh"))%>" );
ff(278,566,200,20,"<%=Misc.JSEscape(request.getParameter("xml_sv13upr"))%>" );
ff(302,566,200,20,"<%=Misc.JSEscape(request.getParameter("xml_sv13ugl"))%>" );
ff(328,566,200,20,"<%=Misc.JSEscape(request.getParameter("xml_sv13bp"))%>" );
//ff(375,566,310,20,"<%=Misc.JSEscape(request.getParameter("xml_sv13co"))%>" );
ff(692,566,100,20,"<%=Misc.JSEscape(request.getParameter("xml_sv13ra"))%>" );

ff(3,585,200,20,'<span class="smalltdname"><%=Misc.JSEscape(request.getParameter("xml_sv14da"))%></span>' );
ff(53,585,200,20,"<%=Misc.JSEscape(request.getParameter("xml_sv14ga"))%>" );
ff(90,585,200,20,"<%=Misc.JSEscape(request.getParameter("xml_sv14sf"))%>" );
ff(132,585,200,20,"<%=Misc.JSEscape(request.getParameter("xml_sv14wt"))%>" );
ff(175,585,200,20,"<%=Misc.JSEscape(request.getParameter("xml_sv14pr"))%>" );
ff(220,585,200,20,"<%=Misc.JSEscape(request.getParameter("xml_sv14fh"))%>" );
ff(278,585,200,20,"<%=Misc.JSEscape(request.getParameter("xml_sv14upr"))%>" );
ff(302,585,200,20,"<%=Misc.JSEscape(request.getParameter("xml_sv14ugl"))%>" );
ff(328,585,200,20,"<%=Misc.JSEscape(request.getParameter("xml_sv14bp"))%>" );
//ff(375,585,310,20,"<%=Misc.JSEscape(request.getParameter("xml_sv14co"))%>" );
ff(692,585,100,20,"<%=Misc.JSEscape(request.getParameter("xml_sv14ra"))%>" );

ff(3,603,200,20,'<span class="smalltdname"><%=Misc.JSEscape(request.getParameter("xml_sv15da"))%></span>' );
ff(53,603,200,20,"<%=Misc.JSEscape(request.getParameter("xml_sv15ga"))%>" );
ff(90,603,200,20,"<%=Misc.JSEscape(request.getParameter("xml_sv15sf"))%>" );
ff(132,603,200,20,"<%=Misc.JSEscape(request.getParameter("xml_sv15wt"))%>" );
ff(175,603,200,20,"<%=Misc.JSEscape(request.getParameter("xml_sv15pr"))%>" );
ff(220,603,200,20,"<%=Misc.JSEscape(request.getParameter("xml_sv15fh"))%>" );
ff(278,603,200,20,"<%=Misc.JSEscape(request.getParameter("xml_sv15upr"))%>" );
ff(302,603,200,20,"<%=Misc.JSEscape(request.getParameter("xml_sv15ugl"))%>" );
ff(328,603,200,20,"<%=Misc.JSEscape(request.getParameter("xml_sv15bp"))%>" );
//ff(375,603,310,20,"<%=Misc.JSEscape(request.getParameter("xml_sv15co"))%>" );
ff(692,603,100,20,"<%=Misc.JSEscape(request.getParameter("xml_sv15ra"))%>" );

ff(3,621,200,20,'<span class="smalltdname"><%=Misc.JSEscape(request.getParameter("xml_sv16da"))%></span>' );
ff(53,621,200,20,"<%=Misc.JSEscape(request.getParameter("xml_sv16ga"))%>" );
ff(90,621,200,20,"<%=Misc.JSEscape(request.getParameter("xml_sv16sf"))%>" );
ff(132,621,200,20,"<%=Misc.JSEscape(request.getParameter("xml_sv16wt"))%>" );
ff(175,621,200,20,"<%=Misc.JSEscape(request.getParameter("xml_sv16pr"))%>" );
ff(220,621,200,20,"<%=Misc.JSEscape(request.getParameter("xml_sv16fh"))%>" );
ff(278,621,200,20,"<%=Misc.JSEscape(request.getParameter("xml_sv16upr"))%>" );
ff(302,621,200,20,"<%=Misc.JSEscape(request.getParameter("xml_sv16ugl"))%>" );
ff(328,621,200,20,"<%=Misc.JSEscape(request.getParameter("xml_sv16bp"))%>" );
//ff(375,621,310,20,"<%=Misc.JSEscape(request.getParameter("xml_sv16co"))%>" );
ff(692,621,100,20,"<%=Misc.JSEscape(request.getParameter("xml_sv16ra"))%>" );

ff(3,639,200,20,'<span class="smalltdname"><%=Misc.JSEscape(request.getParameter("xml_sv17da"))%></span>' );
ff(53,639,200,20,"<%=Misc.JSEscape(request.getParameter("xml_sv17ga"))%>" );
ff(90,639,200,20,"<%=Misc.JSEscape(request.getParameter("xml_sv17sf"))%>" );
ff(132,639,200,20,"<%=Misc.JSEscape(request.getParameter("xml_sv17wt"))%>" );
ff(175,639,200,20,"<%=Misc.JSEscape(request.getParameter("xml_sv17pr"))%>" );
ff(220,639,200,20,"<%=Misc.JSEscape(request.getParameter("xml_sv17fh"))%>" );
ff(278,639,200,20,"<%=Misc.JSEscape(request.getParameter("xml_sv17upr"))%>" );
ff(302,639,200,20,"<%=Misc.JSEscape(request.getParameter("xml_sv17ugl"))%>" );
ff(328,639,200,20,"<%=Misc.JSEscape(request.getParameter("xml_sv17bp"))%>" );
//ff(375,639,310,20,"<%=Misc.JSEscape(request.getParameter("xml_sv17co"))%>" );
ff(692,639,100,20,"<%=Misc.JSEscape(request.getParameter("xml_sv17ra"))%>" );

ff(240,665,100,20,'<span class="tdname">Date</span>' );
ff(298,665,100,20,'<span class="tdname">GA</span>' );
ff(410,665,100,20,'<span class="tdname">Result</span>' );

ff(227,683,100,20,"<%=Misc.JSEscape(request.getParameter("xml_uda1"))%>" );
ff(285,683,100,20,"<%=Misc.JSEscape(request.getParameter("xml_uga1"))%>" );
ff(340,683,300,20,"<%=Misc.JSEscape(request.getParameter("xml_ure1"))%>" );
ff(227,702,100,20,"<%=Misc.JSEscape(request.getParameter("xml_uda2"))%>" );
ff(285,702,100,20,"<%=Misc.JSEscape(request.getParameter("xml_uga2"))%>" );
ff(340,702,300,20,"<%=Misc.JSEscape(request.getParameter("xml_ure2"))%>" );
ff(227,721,100,20,"<%=Misc.JSEscape(request.getParameter("xml_uda3"))%>" );
ff(285,721,100,20,"<%=Misc.JSEscape(request.getParameter("xml_uga3"))%>" );
ff(340,721,300,20,"<%=Misc.JSEscape(request.getParameter("xml_ure3"))%>" );
ff(227,738,100,20,"<%=Misc.JSEscape(request.getParameter("xml_uda4"))%>" );
ff(285,738,100,20,"<%=Misc.JSEscape(request.getParameter("xml_uga4"))%>" );
ff(340,738,300,20,"<%=Misc.JSEscape(request.getParameter("xml_ure4"))%>" );

ff(530,653,200,20,'<span class="tdname"><b>Referral Plan</b></span>' );
//ff(531,652,200,20,'<span class="tdname"><b>Referral Plan</b></span>' );
ff(517,665,50,20,"<%=Misc.JSEscape(request.getParameter("xml_rpob")==null?"":"X")%>" );
ff(530,664,200,20,'<span class="tdname">Obstetrician</span>' );
ff(517,677,50,20,"<%=Misc.JSEscape(request.getParameter("xml_rppp")==null?"":"X")%>" );
ff(530,676,200,20,'<span class="tdname">Pediatrician</span>' );
ff(517,689,50,20,"<%=Misc.JSEscape(request.getParameter("xml_rpan")==null?"":"X")%>" );
ff(530,688,200,20,'<span class="tdname">Anesthesiologist</span>' );
ff(517,701,50,20,"<%=Misc.JSEscape(request.getParameter("xml_rpsw")==null?"":"X")%>" );
ff(530,700,200,20,'<span class="tdname">Social worker</span>' );
ff(517,714,50,20,"<%=Misc.JSEscape(request.getParameter("xml_rpdi")==null?"":"X")%>" );
ff(530,712,200,20,'<span class="tdname">Dietitian</span>' );
ff(517,725,50,20,"<%=Misc.JSEscape(request.getParameter("xml_rpotc")==null?"":"X")%>" );
ff(530,724,200,20,'<span class="tdname">Other</span>' );
ff(517,737,300,20,"<%=Misc.JSEscape(request.getParameter("xml_rpott"))%>" );

ff(617,653,200,20,'<span class="tdname"><b>Discussion Topics</b></span>' );
ff(612,665,50,20,"<%=Misc.JSEscape(request.getParameter("xml_dtdu")==null?"":"X")%>" );
ff(624,664,200,20,'<span class="tdname">Drug use</span>' );
ff(612,677,50,20,"<%=Misc.JSEscape(request.getParameter("xml_dtsm")==null?"":"X")%>" );
ff(624,676,200,20,'<span class="tdname">Smoking</span>' );
ff(612,689,50,20,"<%=Misc.JSEscape(request.getParameter("xml_dtal")==null?"":"X")%>" );
ff(624,688,200,20,'<span class="tdname">Alcohol</span>' );
ff(612,701,50,20,"<%=Misc.JSEscape(request.getParameter("xml_dtex")==null?"":"X")%>" );
ff(624,700,200,20,'<span class="tdname">Exercise</span>' );
ff(612,713,50,20,"<%=Misc.JSEscape(request.getParameter("xml_dtwp")==null?"":"X")%>" );
ff(624,712,200,20,'<span class="tdname">Work plan</span>' );
ff(612,725,50,20,"<%=Misc.JSEscape(request.getParameter("xml_dtin")==null?"":"X")%>" );
ff(624,724,200,20,'<span class="tdname">Intercourse</span>' );
ff(612,737,50,20,"<%=Misc.JSEscape(request.getParameter("xml_dtdc")==null?"":"X")%>" );
ff(624,736,200,20,'<span class="tdname">Dental care</span>' );
ff(612,749,50,20,"<%=Misc.JSEscape(request.getParameter("xml_dttr")==null?"":"X")%>" );
ff(624,748,200,20,'<span class="tdname">Travel</span>' );

ff(612,761,50,20,"<%=Misc.JSEscape(request.getParameter("xml_dtpc")==null?"":"X")%>" );
ff(624,760,200,20,'<span class="tdname">Prenatal classes</span>' );
ff(612,773,50,20,"<%=Misc.JSEscape(request.getParameter("xml_dtbf")==null?"":"X")%>" );
ff(624,772,200,20,'<span class="tdname">Breast feeding</span>' );
ff(612,785,50,20,"<%=Misc.JSEscape(request.getParameter("xml_dtbp")==null?"":"X")%>" );
ff(624,784,200,20,'<span class="tdname">Birth plan</span>' );
ff(612,797,50,20,"<%=Misc.JSEscape(request.getParameter("xml_dtpl")==null?"":"X")%>" );
ff(624,796,200,20,'<span class="tdname">Preterm labour</span>' );
ff(612,809,50,20,"<%=Misc.JSEscape(request.getParameter("xml_dtpr")==null?"":"X")%>" );
ff(624,808,200,20,'<span class="tdname">PROM</span>' );
ff(612,821,50,20,"<%=Misc.JSEscape(request.getParameter("xml_dtfm")==null?"":"X")%>" );
ff(624,820,200,20,'<span class="tdname">Fetal movement</span>' );
ff(612,833,50,20,"<%=Misc.JSEscape(request.getParameter("xml_dtat")==null?"":"X")%>" );
ff(624,832,200,20,'<span class="tdname">Admission timing</span>' );
ff(612,845,50,20,"<%=Misc.JSEscape(request.getParameter("xml_dtls")==null?"":"X")%>" );
ff(624,844,200,20,'<span class="tdname">Labour support</span>' );
ff(612,857,50,20,"<%=Misc.JSEscape(request.getParameter("xml_dtpm")==null?"":"X")%>" );
ff(624,856,200,20,'<span class="tdname">Pain management</span>' );
ff(612,869,50,20,"<%=Misc.JSEscape(request.getParameter("xml_dtde")==null?"":"X")%>" );
ff(624,868,200,20,'<span class="tdname">Depression</span>' );
ff(612,881,50,20,"<%=Misc.JSEscape(request.getParameter("xml_dtci")==null?"":"X")%>" );
ff(624,880,200,20,'<span class="tdname">Circumcision</span>' );
ff(612,893,50,20,"<%=Misc.JSEscape(request.getParameter("xml_dtcs")==null?"":"X")%>" );
ff(624,892,200,20,'<span class="tdname">Car safety</span>' );
ff(612,905,50,20,"<%=Misc.JSEscape(request.getParameter("xml_dtco")==null?"":"X")%>" );
ff(624,904,200,20,'<span class="tdname">Contraception</span>' );
ff(612,917,50,20,"<%=Misc.JSEscape(request.getParameter("xml_dtoc")==null?"":"X")%>" );
ff(624,916,200,20,'<span class="tdname">On call</span>' );

ff(232,765,300,20,'<span class="tdname">1. Pap</span>' );
ff(339,765,200,20,"<%=Misc.JSEscape(request.getParameter("xml_re1"))%>" );
ff(232,780,300,20,'<span class="tdname">2. GC/Chlamydia</span>' );
ff(339,780,200,20,"<%=Misc.JSEscape(request.getParameter("xml_re2"))%>" );
ff(232,792,300,20,'<span class="tdname">3. HIV</span>' );
ff(339,792,200,20,"<%=Misc.JSEscape(request.getParameter("xml_re3"))%>" );
ff(232,804,300,20,'<span class="tdname">4. B. vaginosis</span>' );
ff(339,804,200,20,"<%=Misc.JSEscape(request.getParameter("xml_re4"))%>" );
ff(232,816,300,20,'<span class="tdname">5. Group B strep</span>' );
ff(339,816,200,20,"<%=Misc.JSEscape(request.getParameter("xml_re5"))%>" );
ff(232,828,300,20,'<span class="tdname">6. Urine culture</span>' );
ff(339,828,200,20,"<%=Misc.JSEscape(request.getParameter("xml_re6"))%>" );
ff(232,840,300,20,'<span class="tdname">7. Sickle dex</span>' );
ff(339,840,200,20,"<%=Misc.JSEscape(request.getParameter("xml_re7"))%>" );
ff(232,852,300,20,'<span class="tdname">8. Hb electro</span>' );
ff(339,852,200,20,"<%=Misc.JSEscape(request.getParameter("xml_re8"))%>" );
ff(232,864,300,20,'<span class="tdname">9. Amnio/CVS</span>' );
ff(339,864,200,20,"<%=Misc.JSEscape(request.getParameter("xml_re9"))%>" );
ff(232,876,300,20,'<span class="tdname">10. Glucose screen</span>' );
ff(339,876,200,20,"<%=Misc.JSEscape(request.getParameter("xml_re10"))%>" );
ff(232,888,300,20,'<span class="tdname">11. Other</span>' );
ff(339,888,200,20,"<%=Misc.JSEscape(request.getParameter("xml_re11"))%>" );
ff(235,900,200,20,"<%=Misc.JSEscape(request.getParameter("xml_st11"))%>" );

ff(405,900,300,50,"<%=Misc.JSEscape(request.getParameter("xml_cpi"))%>" );

ff(6,928,300,20,'<span class="tdname">Signature of attendant</span>' );
ff(120,938,300,20,"<%=Misc.JSEscape(request.getParameter("xml_soa"))%>" );
ff(465,928,300,20,'<span class="tdname">Date (yyyy/mm/dd)</span>' );
ff(560,938,300,20,"<%=Misc.JSEscape(request.getParameter("xml_date"))%>" );

ff(15,956,200,20,'<span class="smalltdname">0375-64 (99/08)</span>' );
ff(190,956,350,20,"<span class=\"smalltdname\">Canary - Mother's chart - forward to hospital  Pink - Attendant's copy  White - Infant's chart</span>" );
ff(665,956,200,20,'<span class="smalltdname">7530-4654</span>' );
</script>

<div ID="bdiv1"
	STYLE="position:absolute; visibility:visible; z-index:2; left:<%=10+oox%>px; top:<%=ooy+172%>px; width:330px; height:60px;">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td><%=UtilMisc.htmlJsEscape(request.getParameter("xml_rfi"))%></td>
	</tr>
</table>
</div>
<div ID="bdiv1"
	STYLE="position:absolute; visibility:visible; z-index:2; left:<%=347+oox%>px; top:<%=ooy+172%>px; width:180px; height:50px;">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td><%=request.getParameter("xml_Alert_demographicaccessory")%></td>
	</tr>
</table>
</div>
<div ID="bdiv1"
	STYLE="position:absolute; visibility:visible; z-index:2; left:<%=530+oox%>px; top:<%=ooy+172%>px; width:180px; height:50px;">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td><%=request.getParameter("xml_Medication_demographicaccessory")%>
		</td>
	</tr>
</table>
</div>
<div ID="bdiv1"
	STYLE="position:absolute; visibility:visible; z-index:2; left:<%=410+oox%>px; top:<%=ooy+765%>px; width:210px; height:80px;">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td><%=request.getParameter("xml_com")%></td>
	</tr>
</table>
</div>

<div ID="bdiv1"
	STYLE="position:absolute; visibility:visible; z-index:2; left:<%=374+oox%>px; top:<%=ooy+345%>px; width:310px; height:20px;">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td><span class="tdname1"><%=request.getParameter("xml_sv1co")%></span>
		</td>
	</tr>
</table>
</div>

<div ID="bdiv1"
	STYLE="position:absolute; visibility:visible; z-index:2; left:<%=374+oox%>px; top:<%=ooy+363%>px; width:310px; height:20px;">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td><span class="tdname1"><%=request.getParameter("xml_sv2co")%></span>
		</td>
	</tr>
</table>
</div>

<div ID="bdiv1"
	STYLE="position:absolute; visibility:visible; z-index:2; left:<%=374+oox%>px; top:<%=ooy+381%>px; width:310px; height:20px;">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td><span class="tdname1"><%=request.getParameter("xml_sv3co")%></span>
		</td>
	</tr>
</table>
</div>

<div ID="bdiv1"
	STYLE="position:absolute; visibility:visible; z-index:2; left:<%=374+oox%>px; top:<%=ooy+399%>px; width:310px; height:20px;">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td><span class="tdname1"><%=request.getParameter("xml_sv4co")%></span>
		</td>
	</tr>
</table>
</div>

<div ID="bdiv1"
	STYLE="position:absolute; visibility:visible; z-index:2; left:<%=374+oox%>px; top:<%=ooy+417%>px; width:310px; height:20px;">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td><span class="tdname1"><%=request.getParameter("xml_sv5co")%></span>
		</td>
	</tr>
</table>
</div>

<div ID="bdiv1"
	STYLE="position:absolute; visibility:visible; z-index:2; left:<%=374+oox%>px; top:<%=ooy+435%>px; width:310px; height:20px;">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td><span class="tdname1"><%=request.getParameter("xml_sv6co")%></span>
		</td>
	</tr>
</table>
</div>

<div ID="bdiv1"
	STYLE="position:absolute; visibility:visible; z-index:2; left:<%=374+oox%>px; top:<%=ooy+453%>px; width:310px; height:20px;">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td><span class="tdname1"><%=request.getParameter("xml_sv7co")%></span>
		</td>
	</tr>
</table>
</div>

<div ID="bdiv1"
	STYLE="position:absolute; visibility:visible; z-index:2; left:<%=374+oox%>px; top:<%=ooy+473%>px; width:310px; height:20px;">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td><span class="tdname1"><%=request.getParameter("xml_sv8co")%></span>
		</td>
	</tr>
</table>
</div>

<div ID="bdiv1"
	STYLE="position:absolute; visibility:visible; z-index:2; left:<%=374+oox%>px; top:<%=ooy+491%>px; width:310px; height:20px;">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td><span class="tdname1"><%=request.getParameter("xml_sv9co")%></span>
		</td>
	</tr>
</table>
</div>

<div ID="bdiv1"
	STYLE="position:absolute; visibility:visible; z-index:2; left:<%=374+oox%>px; top:<%=ooy+509%>px; width:310px; height:20px;">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td><span class="tdname1"><%=request.getParameter("xml_sv10co")%></span>
		</td>
	</tr>
</table>
</div>

<div ID="bdiv1"
	STYLE="position:absolute; visibility:visible; z-index:2; left:<%=374+oox%>px; top:<%=ooy+528%>px; width:310px; height:20px;">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td><span class="tdname1"><%=request.getParameter("xml_sv11co")%></span>
		</td>
	</tr>
</table>
</div>

<div ID="bdiv1"
	STYLE="position:absolute; visibility:visible; z-index:2; left:<%=374+oox%>px; top:<%=ooy+547%>px; width:310px; height:20px;">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td><span class="tdname1"><%=request.getParameter("xml_sv12co")%></span>
		</td>
	</tr>
</table>
</div>

<div ID="bdiv1"
	STYLE="position:absolute; visibility:visible; z-index:2; left:<%=374+oox%>px; top:<%=ooy+566%>px; width:310px; height:20px;">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td><span class="tdname1"><%=request.getParameter("xml_sv13co")%></span>
		</td>
	</tr>
</table>
</div>

<div ID="bdiv1"
	STYLE="position:absolute; visibility:visible; z-index:2; left:<%=374+oox%>px; top:<%=ooy+585%>px; width:310px; height:20px;">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td><span class="tdname1"><%=request.getParameter("xml_sv14co")%></span>
		</td>
	</tr>
</table>
</div>

<div ID="bdiv1"
	STYLE="position:absolute; visibility:visible; z-index:2; left:<%=374+oox%>px; top:<%=ooy+603%>px; width:310px; height:20px;">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td><span class="tdname1"><%=request.getParameter("xml_sv15co")%></span>
		</td>
	</tr>
</table>
</div>

<div ID="bdiv1"
	STYLE="position:absolute; visibility:visible; z-index:2; left:<%=374+oox%>px; top:<%=ooy+621%>px; width:310px; height:20px;">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td><span class="tdname1"><%=request.getParameter("xml_sv16co")%></span>
		</td>
	</tr>
</table>
</div>

<div ID="bdiv1"
	STYLE="position:absolute; visibility:visible; z-index:2; left:<%=374+oox%>px; top:<%=ooy+639%>px; width:310px; height:20px;">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td><span class="tdname1"><%=request.getParameter("xml_sv17co")%></span>
		</td>
	</tr>
</table>
</div>

<%
String fedb = request.getParameter("xml_fedb");
String urlparam = "";

int width = 200, height = 235 ;
int x = 0, y = 0;
int ox = 21, oy = 254;
float dx = 0f, dy = 0f;
int ipos = 0;

if (fedb != null && fedb.length() == 10 ) {
	FrmGraphicAR arG = new FrmGraphicAR();
    for (int i = 1; i < 18; i++) {
        if (request.getParameter("xml_sv" + i + "da") != null && request.getParameter("xml_sv" + i + "da").length() == 10 ) {
			dx = arG.getWeekByEDB(fedb, request.getParameter("xml_sv" + i + "da"));
			if (arG.getWeekInt() < 19 || arG.getWeekInt() > 40 || request.getParameter("xml_sv" + i + "sf") == null  || (request.getParameter("xml_sv" + i + "sf")).equals("-") || arG.getHt(request.getParameter("xml_sv" + i + "sf")).equals("")) continue;
       		dy = Float.parseFloat(arG.getHt(request.getParameter("xml_sv" + i + "sf")));
	        x = (int) ((ox + (dx -19) * width / (11.5 * 2)) -2) ;
	        y = (int) ((oy - (dy - 11.818) * height / (5.636 * 5)) -1) ;
            urlparam += "&x" + (i-1) + "=" + x + "|" + y;
		}
	}
%>
<div ID="graphic"
	STYLE="position:absolute; visibility:visible; z-index:2; left:<%=3+oox%>px; top:<%=653+ooy%>px; width:501px; height:280px;">
<embed type="image/svg+xml"
	src="../form/formar2svg.jsp?bgimage=<%=URLEncoder.encode("../images/formar2_99_08gra.gif")%>&bgimagewidth=222&bgimageheight=276<%=urlparam%>"
	width="221" height="276" wmode="transparent" /></div>
<%
}
%>

</body>
</html>
