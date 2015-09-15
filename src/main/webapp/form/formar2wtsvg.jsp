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

<?xml version="1.0" encoding="ISO-8859-1" standalone="no" ?>
<%@ page contentType="image/svg-xml"%>
<!DOCTYPE svg PUBLIC "-//W3C//DTD SVG 1.0//EN" 
  "http://www.w3.org/TR/2001/REC-SVG-20010904/DTD/svg10.dtd">
<svg width="600" height="600" xmlns="http://www.w3.org/2000/svg">
<desc>Scripting the onclick event</desc>
<defs>
<script type="text/ecmascript"> 
    <![CDATA[
      function showDot(evt) {
        var SVGDoc = evt.getTarget().getOwnerDocument();
        var rect = SVGDoc.getElementById("square");
        var style = rect.getAttribute("fill");
        if (style == 'black')
          rect.setAttribute("fill", "red");
        else 
          rect.setAttribute("fill", "black");
      }
    ]]> 
    </script>


<rect id="square" x="0" y="0" width="5" height="5" fill="black"
	stroke-width="0" stroke="black" opacity="1" />
<ellipse id="circle" cx="3" cy="3" rx="3" ry="3" stroke-width="1"
	stroke="black" />
<g id="cross" x="0" y="0" width="4" height="4">
<line x1="0" y1="0" x2="4" y2="4" stroke-width="1" stroke="black" />
<line x1="0" y1="4" x2="4" y2="0" stroke-width="1" stroke="black" />
</g>
</defs>

<%
	if (request.getParameter("bgimage") != null) {
%>
<image id="img" x="0" y="0"
	width="<%=request.getParameter("bgimagewidth")%>"
	height="<%=request.getParameter("bgimageheight")%>"
	xlink:href="<%=request.getParameter("bgimage")%>" opacity="1"
	onmouseout="showDot(evt)" onmouseover="showDot(evt)" />
"
<% 
	} 
%>

<%
	int x = 0, y = 0;
	int ipos = 0;

	for (int i =0; i < 34; i++) {
		if (request.getParameter("x" + i) != null) {
			ipos = request.getParameter("x" + i).indexOf("|");
			x = (int) Integer.parseInt(request.getParameter("x" + i).substring(0, ipos)) ;
			y = (int) Integer.parseInt(request.getParameter("x" + i).substring(ipos+1)) ;
%>
<use xlink:href="#square" transform="translate(<%=x + ", " + y%>)" />
<%
		}
	}
%>
</svg>
