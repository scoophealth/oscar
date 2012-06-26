<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title><bean:message key="procedimento.title" /></title>
<link rel="stylesheet" href="../../web_fat.css">
<script src="../../oscar.js"></script>
<script>
  function selecionar(param1, param2) {
    try {
      opener.document.forms[0].elements['coProc'].value=param1;
      opener.document.forms[0].elements['dsProc'].value=param2;
      parent.close();
    } catch (e) {
    }   
  }

  function setfocus() {
     document.forms[0].codigoProc.focus();
  }
</script>

</head>

<html:form action="/popup/procedimento/findProc.do" method="post"
	focus="codigoProc">

	<input type="hidden" name="formCoProc" value="coProc">
	<input type="hidden" name="formDsProc" value="dsProc">

	<body background="../../images/gray_bg.jpg" bgproperties="fixed"
		onLoad="setfocus()" onUnload="" topmargin="0" leftmargin="0"
		rightmargin="0">
	<center>
	<table cellspacing="0" cellpadding="2" width="100%" border="0">
		<tr>
			<th><bean:message key="procedimento.index.description" /></th>
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
			<p><bean:message key="procedimento.description" /></p>
			</td>
		</tr>
		<tr class="body">
			<td><html:errors />
			<table cellpadding="3" cellspacing="3" width="100%">
				<tr>
					<td><span class="formLabel"><bean:message
						key="procedimento.co_procedimento" /></span></td>
					<td><span class="formLabel"><bean:message
						key="procedimento.ds_procedimento" /></span></td>
				</tr>
				<tr>
					<td><html:text property="codigoProc" size="15" maxlength="10" /></td>
					<td><html:text property="descProc" size="25" maxlength="50" /></td>
				</tr>
			</table>
			</td>
		</tr>
	</table>
	<br>
	<table cellpadding="3" cellspacing="3" border="0" width="100%">
		<tr>
			<td align="center"><html:submit>
				<bean:message key="procedimento.btnFindProc" />
			</html:submit></td>
		</tr>
	</table>
	<br>
	<table cellspacing="0" cellpadding="2" width="90%" border="0">
		<tr>
			<th colspan="3">
			<p><bean:message key="procedimento_list.description" /></p>
			</td>
		</tr>
		<tr>
			<th class="tableLabel">&nbsp;</th>
			<th class="tableLabel"><bean:message
				key="procedimento.co_procedimento" /></th>
			<th class="tableLabel"><bean:message
				key="procedimento.ds_procedimento" /></th>
		</tr>
		<logic:present name="procedimentoForm" property="procedimentos">
			<bean:write name="pagerHeader" scope="request" filter="false" /><%! String key;%>
			<logic:iterate id="procedimento" name="procedimentoForm"
				property="procedimentos"
				type="oscar.billing.cad.model.CadProcedimentos" offset="offset"
				length="length" indexId="idx">
				<tr>
					<td align="center"><a href="#" class="formLink"
						onclick="javascript:selecionar('<bean:write name="procedimento" property="coProcedimento"/>','<bean:write name="procedimento" property="dsProcedimento"/>');"><bean:message
						key="popups.popup_procedimento.btnSelect" /></a></td>
					<td><bean:write name="procedimento" property="coProcedimento" />
					</td>
					<td><bean:write name="procedimento" property="dsProcedimento" />
					</td>
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
