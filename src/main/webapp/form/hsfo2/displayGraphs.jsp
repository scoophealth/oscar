<%--

    Copyright (C) 2007  Heart & Stroke Foundation
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

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">


<html:html>
<head>
  <script type="text/javascript" src="<%=request.getContextPath()%>/js/global.js"></script>
  <script type="text/javascript" src="<%=request.getContextPath()%>/js/HSFO2.js"></script>
  
  <title>HSFO2 Graphs</title>
 
  <link rel="stylesheet" type="text/css" media="all" href="../share/css/extractedFromPages.css" />
  <link rel="stylesheet" href="../form/hsfo2/hsfo2.css">
</head>

<body onLoad="initialize()">

<!-- graph table -->
<table style="width: 766px; border: medium solid;">
  <tr>
    <td><img src="<%=request.getAttribute("graphFile.Systolic Blood Pressure")%>" alt="<%=request.getAttribute("graphFile.Systolic Blood Pressure")%>"/></td>
  </tr>
  <tr>
    <td><img src="<%=request.getAttribute("graphFile.Diastolic Blood Pressure")%>" alt="<%=request.getAttribute("graphFile.Diastolic Blood Pressure")%>"/> </td>
  </tr>
  <tr>
    <td><img src="<%=request.getAttribute("graphFile.BMI")%>" alt="<%=request.getAttribute("graphFile.BMI")%>"/> </td>
  </tr>  
  <tr>
    <td><img src="<%=request.getAttribute("graphFile.Waist")%>" alt="<%=request.getAttribute("graphFile.Waist")%>"/></td>
  </tr>
  <tr>
    <td><img src="<%=request.getAttribute("graphFile.LDL")%>" alt="<%=request.getAttribute("graphFile.LDL")%>"/></td>
  </tr>  
  <tr>
    <td><img src="<%=request.getAttribute("graphFile.TC_HDL")%>" alt="<%=request.getAttribute("graphFile.TC_HDL")%>"/></td>
  </tr>
  <tr>
    <td><img src="<%=request.getAttribute("graphFile.Importance")%>" alt="<%=request.getAttribute("graphFile.Importance")%>"/></td>
  </tr>
  <tr>
    <td><img src="<%=request.getAttribute("graphFile.Confidence")%>" alt="<%=request.getAttribute("graphFile.Confidence")%>"/> </td>
  </tr>
</table>
<!-- end of graph table -->

</body>
</html:html>
