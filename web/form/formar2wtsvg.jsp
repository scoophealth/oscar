<?xml version="1.0" encoding="ISO-8859-1" standalone="no" ?>
<%@ page contentType="image/svg-xml" %>
<!DOCTYPE svg PUBLIC "-//W3C//DTD SVG 1.0//EN" 
  "http://www.w3.org/TR/2001/REC-SVG-20010904/DTD/svg10.dtd">
<svg width="600" height="600" xmlns="http://www.w3.org/2000/svg" >
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
    
    
     <rect id="square" x="0" y="0" width="5" height="5" fill="black" stroke-width="0" stroke="black" opacity="1"/>
     <ellipse id="circle" cx="3" cy="3" rx="3" ry="3" stroke-width="1" stroke="black"/>
     <g id="cross" x="0" y="0" width="4" height="4" >
         <line x1="0" y1="0" x2="4" y2="4" stroke-width="1" stroke="black"/>
         <line x1="0" y1="4" x2="4" y2="0" stroke-width="1" stroke="black"/>
     </g>
  </defs>

<%
	if (request.getParameter("bgimage") != null) {
%>
  <image id="img" x="0" y="0" width="<%=request.getParameter("bgimagewidth")%>" height="<%=request.getParameter("bgimageheight")%>" xlink:href="<%=request.getParameter("bgimage")%>" opacity="1"  onmouseout="showDot(evt)" onmouseover="showDot(evt)"/>"
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
			y = (int) Integer.parseInt(request.getParameter("x" + i).substring(ipos+1)) ;//System.out.println(dx + " : " +dy);
%>
	<use xlink:href="#square" transform="translate(<%=x + ", " + y%>)" />
<%
		}
	}
%>
</svg>
