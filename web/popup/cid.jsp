<%@page contentType="text/html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<!--  
/*
 * 
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
 * This software is published under the GPL GNU General Public License. 
 * This program is free software; you can redistribute it and/or 
 * modify it under the terms of the GNU General Public License 
 * as published by the Free Software Foundation; either version 2 
 * of the License, or (at your option) any later version. * 
 * This program is distributed in the hope that it will be useful, 
 * but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
 * GNU General Public License for more details. * * You should have received a copy of the GNU General Public License 
 * along with this program; if not, write to the Free Software 
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. * 
 * 
 * <OSCAR TEAM>
 * 
 * This software was written for the 
 * Department of Family Medicine 
 * McMaster University 
 * Hamilton 
 * Ontario, Canada 
 */
-->
<html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<meta http-equiv="Cache-Control" content="no-cache" />
<title><bean:message key="cid.title" /></title>
<link rel="stylesheet" href="../../web_fat.css">
<script src="../../oscar.js"></script>
<script>
  function selecionar(param1, param2) {
    try {
      opener.document.forms[0].elements['coCid'].value=param1;
      opener.document.forms[0].elements['dsCid'].value=param2;
      parent.close();
    } catch (e) {
    }   
  }

  function setfocus() {
     document.forms[0].codigo.focus();
  }
</script>

</head>

<html:form action="/popup/cid/findCid.do" method="post" focus="codigo">

	<input type="hidden" name="formCoCid" value="coCid">
	<input type="hidden" name="formDsCid" value="dsCid">

	<body background="../../images/gray_bg.jpg" bgproperties="fixed"
		onLoad="setfocus()" onUnload="" topmargin="0" leftmargin="0"
		rightmargin="0">
	<center>
	<table cellspacing="0" cellpadding="2" width="100%" border="0">
		<tr>
			<th><bean:message key="cid.index.description" /></th>
		</tr>
	</table>
	<table border="0" cellspacing="0" cellpadding="0" width="90%">
		<tr>
			<td align="right"><a href="#" onclick="window.close()"><bean:message
				key="global.btnClose" /></a></td>
		</tr>
	</table>
	<table cellspacing="0" cellpadding="2" width="90%" border="0">
		<tr>
			<th colspan="2">
			<p><bean:message key="cid.description" /></p>
			</td>
		</tr>
		<tr class="body">
			<td><html:errors />
			<table cellpadding="3" cellspacing="3" width="100%">
				<tr>
					<td><span class="formLabel"><bean:message
						key="cid.co_cid" /></span></td>
					<td><span class="formLabel"><bean:message
						key="cid.ds_cid" /></span></td>
				</tr>
				<tr>
					<td><html:text property="codigo" size="15" maxlength="10" /></td>
					<td><html:text property="desc" size="25" maxlength="50" /></td>
				</tr>
			</table>
			</td>
		</tr>
	</table>
	<br>
	<table cellpadding="3" cellspacing="3" border="0" width="100%">
		<tr>
			<td align="center"><html:submit>
				<bean:message key="cid.btnFindCid" />
			</html:submit></td>
		</tr>
	</table>
	<br>
	<table cellspacing="0" cellpadding="2" width="90%" border="0">
		<tr>
			<th colspan="3">
			<p><bean:message key="cid_list.description" /></p>
			</td>
		</tr>
		<tr>
			<th class="tableLabel">&nbsp;</th>
			<th class="tableLabel"><bean:message key="cid.co_cid" /></th>
			<th class="tableLabel"><bean:message key="cid.ds_cid" /></th>
		</tr>
		<logic:present name="cidForm" property="cid">
			<bean:write name="pagerHeader" scope="request" filter="false" /><%! String key;%>
			<logic:iterate id="cid" name="cidForm" property="cid"
				type="oscar.billing.cad.model.CadCid" offset="offset"
				length="length" indexId="idx">
				<tr>
					<td align="center"><a href="#" class="formLink"
						onclick="javascript:selecionar('<bean:write name="cid" property="coCid"/>','<bean:write name="cid" property="dsCid"/>');"><bean:message
						key="popups.popup_cid.btnSelect" /></a></td>
					<td><bean:write name="cid" property="coCid" /></td>
					<td><bean:write name="cid" property="dsCid" /></td>
				</tr>
			</logic:iterate>
		</logic:present>
	</table>
	<table border="0" cellspacing="0" cellpadding="0" width="90%">
		<tr>
			<td align="right"><a href="#" onclick="window.close()"><bean:message
				key="global.btnClose" /></a></td>
		</tr>
	</table>
	</center>


	</body>
</html:form>
</html:html>