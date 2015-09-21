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

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
      String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
      boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_eChart" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../../../securityError.jsp?type=_eChart");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>

<%
        String co = (String) request.getAttribute("cols");
        String ro = (String) request.getAttribute("rows");
        int cols = 0;
        int rows = 0;
    try{
        cols = Integer.parseInt(co);
        rows = Integer.parseInt(ro);
        rows++;
        cols++;

    }catch(Exception e){
    	MiscUtils.getLogger().error("there was a boo-boo co="+co+" ro="+ro, e);
    }

    String setName = ((String) request.getAttribute("name"));
%>


<%@page import="org.oscarehr.util.MiscUtils"%><html:html locale="true">


<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title><bean:message
	key="oscarEncounter.immunization.config.createImmunizationSetConfig.title" />
</title>
<html:base />

</head>
<script language="javascript">
function BackToOscar()
{
       window.close();
}

function switchColor(val){
    var value = val
    var td = val.parentNode;
     td.bgcolor = "#ffffff";
}
</script>
<link rel="stylesheet" type="text/css" media="all" href="../share/css/extractedFromPages.css"  />

<link rel="stylesheet" type="text/css" href="../styles.css">
<body topmargin="0" leftmargin="0" vlink="#0000FF"
	onload="window.focus();">
<html:errors />
<table border="0" cellpadding="0" cellspacing="0"
	style="border-collapse: collapse" bordercolor="#111111" width="100%"
	id="AutoNumber1" height="100%">
	<tr>
		<td width="100%"
			style="padding-left: 3; padding-right: 3; padding-top: 2; padding-bottom: 2"
			height="0%" colspan="2">
		<p class="HelpAboutLogout"><span class="FakeLink"><a
			href="Help.htm"><bean:message key="global.help" /></a></span> | <span
			class="FakeLink"><a href="About.htm"><bean:message
			key="global.about" /></a></span> | <span class="FakeLink"> <a
			href="Disclaimer.htm"><bean:message key="global.disclaimer" /></a></span></p>
		</td>
	</tr>
	<tr>
		<td width="10%" height="37" bgcolor="#000000">&nbsp;</td>
		<td width="100%" bgcolor="#000000"
			style="border-left: 2px solid #A9A9A9; padding-left: 5" height="0%">
		<p class="ScreenTitle"><bean:message
			key="oscarEncounter.immunization.config.createImmunizationSetConfig.msgCreateNew" /></p>
		</td>
	</tr>
	<tr>
		<td></td>
		<td width="100%" style="border-left: 2px solid #A9A9A9;" height="100%"
			valign="top">
		<table cellpadding="0" cellspacing="2"
			style="border-collapse: collapse" bordercolor="#111111" width="100%"
			height="100%">

			<!----Start new rows here-->
			<tr>
				<td>
				<div class="DivContentTitle"><br>
				<bean:message
					key="oscarEncounter.immunization.config.createImmunizationSetConfig.msgStep4" />
				<br>
				<bean:message
					key="oscarEncounter.immunization.config.createImmunizationSetConfig.msgStep5" />
				<br>
				<bean:message
					key="oscarEncounter.immunization.config.createImmunizationSetConfig.msgStep6" />
				</div>
				</td>
			</tr>
			<tr>
				<td><bean:message
					key="oscarEncounter.immunization.config.createImmunizationSetConfig.msgSetName" />:
				<%=setName%></td>
			</tr>
			<tr>
				<td><html:form
					action="/oscarEncounter/immunization/config/CreateImmunizationSetConfig">

					<html:hidden property="name" value="<%=setName%>" />
					<table border=1>
						<%for (int i = 0; i < rows; i++ ){ %>
						<tr>
							<%  if ( i == 0 ) {
                                            for (int j=0; j < cols; j++){
                                                if (j==0){%>
							<th class=head>&nbsp;</th>
							<%}else{%>
							<th class=head><textarea name="heading<%=i+"D"+j%>" rows="2"></textarea>
							</th>
							<%}%>
							<%    }

                                      }else{

                                            for (int j=0; j < cols; j++){
                                                if (j==0){%>
							<td class=head><textarea name="immunization<%=i+"D"+j%>"
								rows="2"></textarea></td>

							<%}else{%>
							<td class=grey valign=middle align=center><input
								type="checkbox" name="yearAge<%=i+"D"+j%>"
								onclick="switchColor(this)"></td>
							<%}%>
							<%}
                                        }
                                    %>

						</tr>
						<%}%>
						<tr>
							<td colspan=<%=cols%>><input type="submit"
								value="<bean:message key="oscarEncounter.immunization.config.createImmunizationSetConfig.btnRender"/>" />
							</td>
						</tr>
					</table>
				</html:form></td>

			</tr>



			<!----End new rows here-->

			<tr height="100%">
				<td></td>
			</tr>
		</table>
		</td>
	</tr>

	<tr>
		<td height="0%"
			style="border-bottom: 2px solid #A9A9A9; border-top: 2px solid #A9A9A9;"></td>
		<td height="0%"
			style="border-bottom: 2px solid #A9A9A9; border-top: 2px solid #A9A9A9;"></td>
	</tr>
	<tr>
		<td width="100%" height="0%" colspan="2">&nbsp;</td>
	</tr>
	<tr>
		<td width="100%" height="0%" style="padding: 5" bgcolor="#DCDCDC"
			colspan="2"></td>
	</tr>
</table>
</body>
</html:html>
