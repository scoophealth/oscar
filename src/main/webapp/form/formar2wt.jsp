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
%>
<%@ page
	import="java.util.*, java.sql.*, java.net.*, oscar.util.*, oscar.form.graphic.*"
	errorPage="errorpage.jsp"%>
<%@ page import="oscar.form.*"%>
<%@page import="org.oscarehr.util.LoggedInInfo"%>
<HTML>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<TITLE>New Document</TITLE>
</HEAD>

<%
    Properties props = new Properties();
    StringBuffer temp = new StringBuffer("");
	for (Enumeration e = request.getParameterNames() ; e.hasMoreElements() ;) {
	    temp = new StringBuffer(e.nextElement().toString());
		props.setProperty(temp.toString(), request.getParameter(temp.toString()));
    }
%>

<BODY>
<center>
<h3>Weight Gain</h3>
</center>
<%
String fedb = props.getProperty("c_finalEDB", "");
String cppw = props.getProperty("c_ppWt", "");
String urlparam = "";

int width = 373, height = 366 ;
int x = 0, y = 0;
int ox = 235, oy = 412;
float dx = 0f, dy = 0f;
int ipos = 0;
float ppw = 0f; //Pre-preg. wt

if (!fedb.equals("") && !cppw.equals("")) {
	FrmGraphicAR arG = new FrmGraphicAR();
    for (int i = 1; i < 18; i++) {
        if (!props.getProperty(("pg2_date" + i), "").equals("") ) {
			dx = arG.getWeekByEDB(fedb, props.getProperty(("pg2_date" + i)));
			if (arG.getWeekInt() < 8 || arG.getWeekInt() > 40 || arG.getHt(props.getProperty(("pg2_wt" + i), "")).equals("")) continue;
       		dy = Float.parseFloat(arG.getWt(props.getProperty("pg2_wt" + i)));
			ppw = Float.parseFloat(arG.getWt(cppw));
			dy = dy - ppw;

			if (dy<0 || dy>28) continue;
	        x = (int) ((ox + (dx -8) * width / (9 * 4)) -2) ;
	        y = (int) ((oy - (dy - 0) * height / (6 * 2)) -1) ;
            urlparam += "&x" + (i-1) + "=" + x + "|" + y;
		}
	}
    for (int i = 18; i < 35; i++) {
        if (!props.getProperty(("pg3_date" + i), "").equals("") ) {
			dx = arG.getWeekByEDB(fedb, props.getProperty(("pg3_date" + i)));
			if (arG.getWeekInt() < 19 || arG.getWeekInt() > 40 || arG.getHt(props.getProperty(("pg3_wt" + i), "")).equals("")) continue;
       		dy = Float.parseFloat(arG.getWt(props.getProperty("pg3_wt" + i)));
			ppw = Float.parseFloat(arG.getWt(cppw));
			dy = dy - ppw;

			if (dy<0 || dy>28) continue;
	        x = (int) ((ox + (dx -8) * width / (9 * 4)) -2) ;
	        y = (int) ((oy - (dy - 0) * height / (6 * 2)) -1) ;
            urlparam += "&x" + (i-1) + "=" + x + "|" + y;
		}
	}
%>
<div ID="graphic"
	STYLE="position:absolute; visibility:visible; z-index:2; left:<%=100+oox%>px; top:<%=50+ooy%>px; width:600px; height:600px;">
<embed type="image/svg+xml"
	src="formar2wtsvg.jsp?bgimage=<%=URLEncoder.encode("graphics/pregnancyweightgain.jpg")%>&bgimagewidth=580&bgimageheight=498<%=urlparam%>"
	width="600" height="600" wmode="transparent" /></div>
<%
}
%>
</BODY>
</HTML>
