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
<%@page import="org.oscarehr.common.model.Drug"%>
<%@page import="org.oscarehr.util.SpringUtils"%>
<%@page import="org.oscarehr.common.dao.DrugDao"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/oscar-tag.tld" prefix="oscar"%>

<%@page import="org.apache.commons.lang.StringEscapeUtils"%>

<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
    String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
    boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_rx" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../securityError.jsp?type=_rx");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>

<html:html locale="true">
<head>

<%-- <script type="text/javascript" src="<%= request.getContextPath() %>/share/javascript/prototype.js"></script> 
<script type="text/javascript" src="<%= request.getContextPath() %>/share/javascript/Oscar.js"></script>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script> --%>

<script type="text/javascript" src="${ oscar_context_path }/js/jquery-1.7.1.min.js" ></script>
<script type="text/javascript" src="${ oscar_context_path }/js/jquery-ui-1.8.18.custom.min.js" ></script>
<script type="text/javascript" >var ctx = '${ oscar_context_path }';</script>
<title>Drug Reason</title>
<html:base />

<%
String id = request.getParameter("id");
DrugDao drugDao = SpringUtils.getBean(DrugDao.class);
Drug drug = drugDao.find(Integer.parseInt(id));
String drugForm = drug.getDrugForm();


if("update".equals(request.getParameter("action"))) {
	drug.setDrugForm(request.getParameter("drugForm"));
	drugDao.merge(drug);
}
%>
 
<link rel="stylesheet" type="text/css" href="styles.css">
<script type="text/javascript">
<%
if("update".equals(request.getParameter("action"))) {
%>
	window.opener.refresh();
	window.close();
<% } %>
</script>	
</head>

<body topmargin="0" leftmargin="0" vlink="#0000FF">

<form action="">

<p>Current form is: <b><%=drugForm%></b></p>
<br/> 
<b>Select new drug form :</b>

<select name="drugForm">
<option value="Aerosol metered-dose">Aerosol metered-dose</option>
<option value="Aerosol with propellants">Aerosol with propellants</option>
<option value="Bar chewable">Bar chewable</option>
<option value="Bead">Bead</option>
<option value="Biscuit">Biscuit</option>
<option value="Bolus">Bolus</option>
<option value="Capsule">Capsule</option>
<option value="Cartridge">Cartridge</option>
<option value="Capsule delayed release">Capsule delayed release</option>
<option value="Cement">Cement</option>
<option value="Cord">Cord</option>
<option value="Cream">Cream</option>
<option value="Douche">Douche</option>
<option value="Drops">Drops</option>
<option value="Dressing">Dressing</option>
<option value="Capsule enteric-coated">Capsule enteric-coated</option>
<option value="Tablet enteric coated">Tablet enteric coated</option>
<option value="Elixir">Elixir</option>
<option value="Emulsion">Emulsion</option>
<option value="Enema">Enema</option>
<option value="Tablet effervescent">Tablet effervescent</option>
<option value="Floss">Floss</option>
<option value="Gas">Gas</option>
<option value="Granule for suspension delayed release">Granule for suspension delayed release</option>
<option value="Granule effervescent">Granule effervescent</option>
<option value="Gel">Gel</option>
<option value="Gel controlled release">Gel controlled release</option>
<option value="Globules">Globules</option>
<option value="Granules">Granules</option>
<option value="Graft">Graft</option>
<option value="Gum chewable">Gum chewable</option>
<option value="Implant">Implant</option>
<option value="Insert">Insert</option>
<option value="Insert extended release">Insert extended release</option>
<option value="Jam">Jam</option>
<option value="Kit">Kit</option>
<option value="Leaf">Leaf</option>
<option value="Liposome">Liposome</option>
<option value="Liquid">Liquid</option>
<option value="Lotion">Lotion</option>
<option value="Lozenge">Lozenge</option>
<option value="Mouthwash gargle">Mouthwash gargle</option>
<option value="Ointment">Ointment</option>
<option value="Ovules">Ovules</option>
<option value="Pad">Pad</option>
<option value="Patch">Patch</option>
<option value="Powder enteric coated">Powder enteric coated</option>
<option value="Powder effervescent">Powder effervescent</option>
<option value="Pencil">Pencil</option>
<option value="Piece chewable">Piece chewable</option>
<option value="Plaster">Plaster</option>
<option value="Metered dose pump">Metered dose pump</option>
<option value="Drug premix">Drug premix</option>
<option value="Pellet (oral)">Pellet (oral)</option>
<option value="Powder">Powder</option>
<option value="Powder sustained release">Powder sustained release</option>
<option value="Powder for suspension sustained release">Powder for suspension sustained release</option>
<option value="Paste">Paste</option>
<option value="Powder for suspension">Powder for suspension</option>
<option value="Powder for solution">Powder for solution</option>
<option value="Ring slow release">Ring slow release</option>
<option value="Suspension delayed release">Suspension delayed release</option>
<option value="Shampoo">Shampoo</option>
<option value="Spray metered dose">Spray metered dose</option>
<option value="Solution">Solution</option>
<option value="Soap bar">Soap bar</option>
<option value="Sprinkle capsule">Sprinkle capsule</option>
<option value="Sponge">Sponge</option>
<option value="Spray">Spray</option>
<option value="Suppository sustained release">Suppository sustained release</option>
<option value="Sustained release capsule">Sustained release capsule</option>
<option value="Disc sustained release">Disc sustained release</option>
<option value="Syrup sustained release">Syrup sustained release</option>
<option value="Sustained release tablet">Sustained release tablet</option>
<option value="Stick">Stick</option>
<option value="Strip">Strip</option>
<option value="Suppository">Suppository</option>
<option value="Suspension">Suspension</option>
<option value="Suture">Suture</option>
<option value="Swab">Swab</option>
<option value="Syrup">Syrup</option>
<option value="Tablet">Tablet</option>
<option value="Tablet chewable">Tablet chewable</option>
<option value="Tablet dispersible">Tablet dispersible</option>
<option value="Tablet delayed release">Tablet delayed release</option>
<option value="Tablet rapid dissolve">Tablet rapid dissolve</option>
<option value="Tablet sublingual">Tablet sublingual</option>
<option value="Tape">Tape</option>
<option value="Teat dilator">Teat dilator</option>
<option value="Tea herbal">Tea herbal</option>
<option value="Toothpaste">Toothpaste</option>
<option value="Unassigned">Unassigned</option>
<option value="Wafer">Wafer</option>
<option value="Wipe">Wipe</option>

</select>
<input type="hidden" name="id" value="<%=id%>"/>
<input type="hidden" name="action" value="update"/>
<input type="submit"/>
</form>
</body>

</html:html>
