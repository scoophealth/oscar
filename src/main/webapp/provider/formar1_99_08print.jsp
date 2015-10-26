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
<%@ page import="java.util.*, java.sql.*, oscar.*"
	errorPage="errorpage.jsp"%>

<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>ANTENATAL RECORD</title>
<link rel="stylesheet" href="antenatalrecord.css">
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
	background="../images/formar1_99_08.gif">

<div ID="bdiv1"
	STYLE="position:absolute; visibility:visible; z-index:2; left:<%=50+oox%>px; top:<%=96+ooy%>px; width:400px; height:20px;">
<%=request.getParameter("xml_name")%></div>

<script language="JavaScript">
ff(50,120,500,20,"<%=request.getParameter("xml_address")%>" );
ff(25,148,100,20,"<%=request.getParameter("xml_dob")%>");
ff(120,148,50,20,"<%=request.getParameter("xml_age")%>" );
ff(152,145,20,20,"<%=request.getParameter("xml_msm")==null?"":"X"%>" );
ff(180,145,20,20,"<%=request.getParameter("xml_mscl")==null?"":"X"%>" );
ff(210,145,20,20,"<%=request.getParameter("xml_mss")==null?"":"X"%>" );
ff(240,148,200,20,"<%=request.getParameter("xml_el")%>" );

ff(5,170,140,20,"<%=request.getParameter("xml_occp")%>" );
ff(113,170,140,20,"<%=request.getParameter("xml_lang")%>" );
ff(213,170,140,20,"<%=request.getParameter("xml_hp")%>" );
ff(310,170,140,20,"<%=request.getParameter("xml_wp")%>" );
ff(400,170,200,20,"<%=request.getParameter("xml_nop")%>" );
ff(555,170,50,20,"<%=request.getParameter("xml_page")%>" );
ff(580,170,140,20,"<%=request.getParameter("xml_poccp")%>" );

ff(12,197,20,20,"<%=request.getParameter("xml_baobs")==null?"":"X"%>" );
ff(54,197,20,20,"<%=request.getParameter("xml_bafp")==null?"":"X"%>" );
ff(89,197,20,20,"<%=request.getParameter("xml_bam")==null?"":"X"%>" );
ff(10,218,200,20,"<%=request.getParameter("xml_ba")%>" );
ff(150,197,200,100,"<%=request.getParameter("xml_fphy")%>" );
ff(322,197,20,20,"<%=request.getParameter("xml_ncp")==null?"":"X"%>" );
ff(363,197,20,20,"<%=request.getParameter("xml_ncfp")==null?"":"X"%>" );
ff(398,197,20,20,"<%=request.getParameter("xml_ncm")==null?"":"X"%>" );
ff(320,218,200,20,"<%=request.getParameter("xml_nc")%>" );

ff(12,238,20,20,"<%=request.getParameter("xml_vbac")==null?"":"X"%>" );
ff(12,256,20,20,"<%=request.getParameter("xml_rcs")==null?"":"X"%>" );

ff(121,293,300,20,"<%=request.getParameter("xml_lmp")%>" );
ff(270,295,100,20,"<%=request.getParameter("xml_c")%>" );
ff(340,298,20,20,"<%=request.getParameter("xml_r")==null?"":"X"%>" );
ff(453,295,20,20,"<%=request.getParameter("xml_edb")%>" );
ff(550,320,20,20,"<%=request.getParameter("xml_fedb")%>" );

ff(12,328,20,20,"<%=request.getParameter("xml_iud")==null?"":"X"%>" );
ff(53,328,20,20,"<%=request.getParameter("xml_ht")==null?"":"X"%>" );
ff(132,325,200,20,"<%=request.getParameter("xml_htt")%>" );
ff(242,328,20,20,"<%=request.getParameter("xml_oc")==null?"":"X"%>" );
ff(286,325,200,20,"<%=request.getParameter("xml_ot")%>" );
ff(440,320,200,20,"<%=request.getParameter("xml_lu")%>" );

ff(12,367,100,20,"<%=request.getParameter("xml_gra")%>" );
ff(70,367,100,20,"<%=request.getParameter("xml_term")%>" );
ff(93,367,100,20,"<%=request.getParameter("xml_prem")%>" );
ff(130,367,20,20,"<%=request.getParameter("xml_ecc")==null?"":"X"%>" );
ff(185,365,100,20,"<%=request.getParameter("xml_ect")%>" );
ff(242,367,20,20,"<%=request.getParameter("xml_tec")==null?"":"X"%>" );
ff(310,365,100,20,"<%=request.getParameter("xml_tet")%>" );
ff(365,367,20,20,"<%=request.getParameter("xml_spc")==null?"":"X"%>" );
ff(440,365,100,20,"<%=request.getParameter("xml_spt")%>" );
ff(502,367,20,20,"<%=request.getParameter("xml_stc")==null?"":"X"%>" );
ff(555,365,100,20,"<%=request.getParameter("xml_stt")%>" );
ff(602,367,100,20,"<%=request.getParameter("xml_liv")%>" );
ff(650,372,200,20,"<%=request.getParameter("xml_mul")%>" );

ff(12,442,20,20,"1" );
ff(38,442,100,20,"<%=request.getParameter("xml_oh1ye")%>" );
ff(78,442,20,20,"<%=request.getParameter("xml_oh1se")%>" );
ff(100,442,100,20,"<%=request.getParameter("xml_oh1ge")%>" );
ff(148,442,100,20,"<%=request.getParameter("xml_oh1bi")%>" );
ff(200,442,100,20,"<%=request.getParameter("xml_oh1le")%>" );
ff(245,442,200,20,"<%=request.getParameter("xml_oh1pl")%>" );
ff(335,442,20,20,"<%=request.getParameter("xml_oh1sv")==null?"":"X"%>" );
ff(351,442,20,20,"<%=request.getParameter("xml_oh1cs")==null?"":"X"%>" );
ff(367,442,20,20,"<%=request.getParameter("xml_oh1as")==null?"":"X"%>" );
ff(392,442,340,20,"<%=request.getParameter("xml_oh1co")%>" );

ff(12,460,20,20,"2" );
ff(38,460,100,20,"<%=request.getParameter("xml_oh2ye")%>" );
ff(78,460,20,20,"<%=request.getParameter("xml_oh2se")%>" );
ff(100,460,100,20,"<%=request.getParameter("xml_oh2ge")%>" );
ff(148,460,100,20,"<%=request.getParameter("xml_oh2bi")%>" );
ff(200,460,100,20,"<%=request.getParameter("xml_oh2le")%>" );
ff(245,460,200,20,"<%=request.getParameter("xml_oh2pl")%>" );
ff(335,460,20,20,"<%=request.getParameter("xml_oh2sv")==null?"":"X"%>" );
ff(351,460,20,20,"<%=request.getParameter("xml_oh2cs")==null?"":"X"%>" );
ff(367,460,20,20,"<%=request.getParameter("xml_oh2as")==null?"":"X"%>" );
ff(392,460,340,20,"<%=request.getParameter("xml_oh2co")%>" );

ff(12,478,20,20,"3" );
ff(38,478,100,20,"<%=request.getParameter("xml_oh3ye")%>" );
ff(78,478,20,20,"<%=request.getParameter("xml_oh3se")%>" );
ff(100,478,100,20,"<%=request.getParameter("xml_oh3ge")%>" );
ff(148,478,100,20,"<%=request.getParameter("xml_oh3bi")%>" );
ff(200,478,100,20,"<%=request.getParameter("xml_oh3le")%>" );
ff(245,478,200,20,"<%=request.getParameter("xml_oh3pl")%>" );
ff(335,478,20,20,"<%=request.getParameter("xml_oh3sv")==null?"":"X"%>" );
ff(351,478,20,20,"<%=request.getParameter("xml_oh3cs")==null?"":"X"%>" );
ff(367,478,20,20,"<%=request.getParameter("xml_oh3as")==null?"":"X"%>" );
ff(392,478,340,20,"<%=request.getParameter("xml_oh3co")%>" );

ff(12,496,20,20,"4" );
ff(38,496,100,20,"<%=request.getParameter("xml_oh4ye")%>" );
ff(78,496,20,20,"<%=request.getParameter("xml_oh4se")%>" );
ff(100,496,100,20,"<%=request.getParameter("xml_oh4ge")%>" );
ff(148,496,100,20,"<%=request.getParameter("xml_oh4bi")%>" );
ff(200,496,100,20,"<%=request.getParameter("xml_oh4le")%>" );
ff(245,496,200,20,"<%=request.getParameter("xml_oh4pl")%>" );
ff(335,496,20,20,"<%=request.getParameter("xml_oh4sv")==null?"":"X"%>" );
ff(351,496,20,20,"<%=request.getParameter("xml_oh4cs")==null?"":"X"%>" );
ff(367,496,20,20,"<%=request.getParameter("xml_oh4as")==null?"":"X"%>" );
ff(392,496,340,20,"<%=request.getParameter("xml_oh4co")%>" );

ff(12,514,20,20,"5" );
ff(38,514,100,20,"<%=request.getParameter("xml_oh5ye")%>" );
ff(78,514,20,20,"<%=request.getParameter("xml_oh5se")%>" );
ff(100,514,100,20,"<%=request.getParameter("xml_oh5ge")%>" );
ff(148,514,100,20,"<%=request.getParameter("xml_oh5bi")%>" );
ff(200,514,100,20,"<%=request.getParameter("xml_oh5le")%>" );
ff(245,514,200,20,"<%=request.getParameter("xml_oh5pl")%>" );
ff(335,514,20,20,"<%=request.getParameter("xml_oh5sv")==null?"":"X"%>" );
ff(351,514,20,20,"<%=request.getParameter("xml_oh5cs")==null?"":"X"%>" );
ff(367,514,20,20,"<%=request.getParameter("xml_oh5as")==null?"":"X"%>" );
ff(392,514,340,20,"<%=request.getParameter("xml_oh5co")%>" );

ff(12,532,20,20,"6" );
ff(38,532,100,20,"<%=request.getParameter("xml_oh6ye")%>" );
ff(78,532,20,20,"<%=request.getParameter("xml_oh6se")%>" );
ff(100,532,100,20,"<%=request.getParameter("xml_oh6ge")%>" );
ff(148,532,100,20,"<%=request.getParameter("xml_oh6bi")%>" );
ff(200,532,100,20,"<%=request.getParameter("xml_oh6le")%>" );
ff(245,532,200,20,"<%=request.getParameter("xml_oh6pl")%>" );
ff(335,532,20,20,"<%=request.getParameter("xml_oh6sv")==null?"":"X"%>" );
ff(351,532,20,20,"<%=request.getParameter("xml_oh6cs")==null?"":"X"%>" );
ff(367,532,20,20,"<%=request.getParameter("xml_oh6as")==null?"":"X"%>" );
ff(392,532,340,20,"<%=request.getParameter("xml_oh6co")%>" );

ff(101,592,20,20,"<%=request.getParameter("xml_cp1b")==null?"":"X"%>" );
ff(101,604,20,20,"<%=request.getParameter("xml_cp2v")==null?"":"X"%>" );
ff(65,625,20,20,"<%=request.getParameter("xml_cp3c")%>" );
ff(101,628,20,20,"<%=request.getParameter("xml_cp3s")==null?"":"X"%>" );
ff(101,640,20,20,"<%=request.getParameter("xml_cp4d")==null?"":"X"%>" );
ff(68,661,20,20,"<%=request.getParameter("xml_cp5d")%>" );
ff(101,664,20,20,"<%=request.getParameter("xml_cp5a")==null?"":"X"%>" );
ff(101,676,20,20,"<%=request.getParameter("xml_cp6i")==null?"":"X"%>" );
ff(101,688,20,20,"<%=request.getParameter("xml_cp7r")==null?"":"X"%>" );
ff(101,700,20,20,"<%=request.getParameter("xml_cp8o")==null?"":"X"%>" );

ff(101,747,20,20,"<%=request.getParameter("xml_nafa")==null?"":"X"%>" );
ff(101,759,20,20,"<%=request.getParameter("xml_namp")==null?"":"X"%>" );
ff(101,778,20,20,"<%=request.getParameter("xml_nadb")==null?"":"X"%>" );
ff(101,790,20,20,"<%=request.getParameter("xml_nadr")==null?"":"X"%>" );
ff(101,802,20,20,"<%=request.getParameter("xml_nadref")==null?"":"X"%>" );

ff(226,593,20,20,"<%=request.getParameter("xml_m9hy")==null?"":"X"%>" );
ff(245,593,20,20,"<%=request.getParameter("xml_m9hn")==null?"":"X"%>" );
ff(226,605,20,20,"<%=request.getParameter("xml_m10ey")==null?"":"X"%>" );
ff(245,605,20,20,"<%=request.getParameter("xml_m10en")==null?"":"X"%>" );
ff(226,617,20,20,"<%=request.getParameter("xml_m11hy")==null?"":"X"%>" );
ff(245,617,20,20,"<%=request.getParameter("xml_m11hn")==null?"":"X"%>" );
ff(226,629,20,20,"<%=request.getParameter("xml_m12ry")==null?"":"X"%>" );
ff(245,629,20,20,"<%=request.getParameter("xml_m12rn")==null?"":"X"%>" );
ff(226,641,20,20,"<%=request.getParameter("xml_m13ry")==null?"":"X"%>" );
ff(245,641,20,20,"<%=request.getParameter("xml_m13rn")==null?"":"X"%>" );
ff(226,653,20,20,"<%=request.getParameter("xml_m14ly")==null?"":"X"%>" );
ff(245,653,20,20,"<%=request.getParameter("xml_m14ln")==null?"":"X"%>" );
ff(226,665,20,20,"<%=request.getParameter("xml_m15ny")==null?"":"X"%>" );
ff(245,665,20,20,"<%=request.getParameter("xml_m15nn")==null?"":"X"%>" );
ff(226,677,20,20,"<%=request.getParameter("xml_m16ay")==null?"":"X"%>" );
ff(245,677,20,20,"<%=request.getParameter("xml_m16an")==null?"":"X"%>" );
ff(226,689,20,20,"<%=request.getParameter("xml_m17by")==null?"":"X"%>" );
ff(245,689,20,20,"<%=request.getParameter("xml_m17bn")==null?"":"X"%>" );
ff(226,701,20,20,"<%=request.getParameter("xml_m18gy")==null?"":"X"%>" );
ff(245,701,20,20,"<%=request.getParameter("xml_m18gn")==null?"":"X"%>" );
ff(226,713,20,20,"<%=request.getParameter("xml_m19hy")==null?"":"X"%>" );
ff(245,713,20,20,"<%=request.getParameter("xml_m19hn")==null?"":"X"%>" );
ff(226,725,20,20,"<%=request.getParameter("xml_m20sy")==null?"":"X"%>" );
ff(245,725,20,20,"<%=request.getParameter("xml_m20sn")==null?"":"X"%>" );
ff(226,737,20,20,"<%=request.getParameter("xml_m21ay")==null?"":"X"%>" );
ff(245,737,20,20,"<%=request.getParameter("xml_m21an")==null?"":"X"%>" );
ff(226,749,20,20,"<%=request.getParameter("xml_m22hy")==null?"":"X"%>" );
ff(245,749,20,20,"<%=request.getParameter("xml_m22hn")==null?"":"X"%>" );
ff(226,761,20,20,"<%=request.getParameter("xml_m23vy")==null?"":"X"%>" );
ff(245,761,20,20,"<%=request.getParameter("xml_m23vn")==null?"":"X"%>" );
ff(226,773,20,20,"<%=request.getParameter("xml_m24py")==null?"":"X"%>" );
ff(245,773,20,20,"<%=request.getParameter("xml_m24pn")==null?"":"X"%>" );
ff(226,785,20,20,"<%=request.getParameter("xml_m25oy")==null?"":"X"%>" );
ff(245,785,20,20,"<%=request.getParameter("xml_m25on")==null?"":"X"%>" );
ff(136,800,200,20,"<%=request.getParameter("xml_m25")%>" );

ff(399,593,20,20,"<%=request.getParameter("xml_g26ay")==null?"":"X"%>" );
ff(418,593,20,20,"<%=request.getParameter("xml_g26an")==null?"":"X"%>" );
ff(399,605,20,20,"<%=request.getParameter("xml_g27ay")==null?"":"X"%>" );
ff(418,605,20,20,"<%=request.getParameter("xml_g27an")==null?"":"X"%>" );
ff(399,633,20,20,"<%=request.getParameter("xml_g28ky")==null?"":"X"%>" );
ff(418,633,20,20,"<%=request.getParameter("xml_g28kn")==null?"":"X"%>" );
ff(399,652,20,20,"<%=request.getParameter("xml_g29py")==null?"":"X"%>" );
ff(418,652,20,20,"<%=request.getParameter("xml_g29pn")==null?"":"X"%>" );

ff(399,672,20,20,"<%=request.getParameter("xml_fh30ny")==null?"":"X"%>" );
ff(418,672,20,20,"<%=request.getParameter("xml_fh30nn")==null?"":"X"%>" );
ff(399,684,20,20,"<%=request.getParameter("xml_fh31dy")==null?"":"X"%>" );
ff(418,684,20,20,"<%=request.getParameter("xml_fh31dn")==null?"":"X"%>" );
ff(399,696,20,20,"<%=request.getParameter("xml_fh32cy")==null?"":"X"%>" );
ff(418,696,20,20,"<%=request.getParameter("xml_fh32cn")==null?"":"X"%>" );
ff(399,719,20,20,"<%=request.getParameter("xml_fh33cy")==null?"":"X"%>" );
ff(418,719,20,20,"<%=request.getParameter("xml_fh33cn")==null?"":"X"%>" );
ff(399,732,20,20,"<%=request.getParameter("xml_fh34cy")==null?"":"X"%>" );
ff(418,732,20,20,"<%=request.getParameter("xml_fh34cn")==null?"":"X"%>" );
ff(399,751,20,20,"<%=request.getParameter("xml_fh35gy")==null?"":"X"%>" );
ff(418,751,20,20,"<%=request.getParameter("xml_fh35gn")==null?"":"X"%>" );
ff(399,779,20,20,"<%=request.getParameter("xml_fh36fy")==null?"":"X"%>" );
ff(418,779,20,20,"<%=request.getParameter("xml_fh36fn")==null?"":"X"%>" );
ff(399,796,20,20,"<%=request.getParameter("xml_fh37oy")==null?"":"X"%>" );
ff(418,796,20,20,"<%=request.getParameter("xml_fh37on")==null?"":"X"%>" );
ff(399,808,20,20,"<%=request.getParameter("xml_fh37ay")==null?"":"X"%>" );
ff(418,808,20,20,"<%=request.getParameter("xml_fh37an")==null?"":"X"%>" );

ff(569,593,20,20,"<%=request.getParameter("xml_idt38s")==null?"":"X"%>" );
ff(569,605,20,20,"<%=request.getParameter("xml_idt39h")==null?"":"X"%>" );
ff(569,617,20,20,"<%=request.getParameter("xml_idt40v")==null?"":"X"%>" );
ff(569,629,20,20,"<%=request.getParameter("xml_idt41t")==null?"":"X"%>" );
ff(500,637,200,20,"<%=request.getParameter("xml_idt42o")%>" );
ff(569,641,20,20,"<%=request.getParameter("xml_idt42t")==null?"":"X"%>" );

ff(569,676,20,20,"<%=request.getParameter("xml_pdt43s")==null?"":"X"%>" );
ff(569,688,20,20,"<%=request.getParameter("xml_pdt44c")==null?"":"X"%>" );
ff(569,700,20,20,"<%=request.getParameter("xml_pdt45e")==null?"":"X"%>" );
ff(569,712,20,20,"<%=request.getParameter("xml_pdt46s")==null?"":"X"%>" );
ff(569,724,20,20,"<%=request.getParameter("xml_pdt47f")==null?"":"X"%>" );
ff(569,736,20,20,"<%=request.getParameter("xml_pdt48p")==null?"":"X"%>" );

ff(600,585,20,20,"<%=request.getParameter("xml_peh")%>" );
ff(670,585,20,20,"<%=request.getParameter("xml_pew")%>" );
ff(640,605,20,20,"<%=request.getParameter("xml_pep")%>" );
ff(610,628,20,20,"<%=request.getParameter("xml_pebp")%>" );

ff(690,662,20,20,"<%=request.getParameter("xml_cinhe")==null?"":"X"%>" );
ff(690,674,20,20,"<%=request.getParameter("xml_cinth")==null?"":"X"%>" );
ff(690,686,20,20,"<%=request.getParameter("xml_cinch")==null?"":"X"%>" );
ff(690,698,20,20,"<%=request.getParameter("xml_cinbr")==null?"":"X"%>" );
ff(690,710,20,20,"<%=request.getParameter("xml_cinca")==null?"":"X"%>" );
ff(690,722,20,20,"<%=request.getParameter("xml_cinab")==null?"":"X"%>" );
ff(690,734,20,20,"<%=request.getParameter("xml_cinva")==null?"":"X"%>" );
ff(690,746,20,20,"<%=request.getParameter("xml_cinne")==null?"":"X"%>" );
ff(690,758,20,20,"<%=request.getParameter("xml_cinpe")==null?"":"X"%>" );
ff(690,770,20,20,"<%=request.getParameter("xml_cinex")==null?"":"X"%>" );
ff(690,782,20,20,"<%=request.getParameter("xml_cince")==null?"":"X"%>" );
ff(618,790,20,20,"<%=request.getParameter("xml_cinun")%>" );
ff(690,794,20,20,"<%=request.getParameter("xml_cinut")==null?"":"X"%>" );
ff(690,806,20,20,"<%=request.getParameter("xml_cinad")==null?"":"X"%>" );

ff(20,955,200,20,"<%=request.getParameter("xml_soa")%>" );
ff(500,955,100,20,"<%=request.getParameter("xml_date")%>" );

</script>
<div ID="bdiv1"
	STYLE="position:absolute; visibility:visible; z-index:2; left:<%=480+oox%>px; top:<%=ooy+197%>px; width:230px; height:30px;">
<pre><%=request.getParameter("xml_ebmf")%></pre></div>

<div ID="bdiv1"
	STYLE="position:absolute; visibility:visible; z-index:2; left:<%=140+oox%>px; top:<%=ooy+240%>px; width:280px; height:50px;">
<pre><%=request.getParameter("xml_Alert_demographicaccessory")%></pre></div>
<div ID="bdiv1"
	STYLE="position:absolute; visibility:visible; z-index:2; left:<%=460+oox%>px; top:<%=ooy+240%>px; width:280px; height:50px;">
<pre><%=request.getParameter("xml_Medication_demographicaccessory")%></pre>
</div>

<div ID="bdiv1"
	STYLE="position:absolute; visibility:visible; z-index:2; left:<%=440+oox%>px; top:<%=ooy+770%>px; width:280px; height:60px;">
<pre><%=request.getParameter("xml_rfi")%></pre></div>

<div ID="bdiv1"
	STYLE="position:absolute; visibility:visible; z-index:2; left:<%=12+oox%>px; top:<%=ooy+850%>px; width:300px; height:60px;">
<pre><%=request.getParameter("xml_comments")%></pre></div>

</body>
</html>
