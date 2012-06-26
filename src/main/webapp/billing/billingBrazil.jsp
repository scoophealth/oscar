<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<html>
<html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>Faturamento</title>
<link rel="stylesheet" href="../../../web_fat.css">
<script src="../../../oscar.js"></script>
<script>
  var xsize = 650;
  var ysize = 400;	

  function popupProcedimento() {
      popup('/oscar/popup/procedimento/findProc.do?formCoProc=coProc&formDsProc=dsProc&coProc=' + document.forms[0].coProc.value + '&dsProc=' + document.forms[0].dsProc.value + '',xsize,ysize);
  }

  function popupCid() {
      popup('/oscar/popup/cid/findCid.do?formCoCid=coCid&formDsCid=dsCid&coCid=' + document.forms[0].coCid.value + '&dsCid=' + document.forms[0].dsCid.value + '',xsize,ysize);
  }

</script>


</head>

<body background="../../../images/gray_bg.jpg" bgproperties="fixed"
	onLoad="setfocus()" topmargin="0" leftmargin="0" rightmargin="0">
<html:form method="post"
	action="/oscar/billing/procedimentoRealizado/add.do">
	<table cellspacing="0" cellpadding="2" width="100%" border="0"
		align="center">
		<tr>
			<th>Faturamento</th>
		</tr>
	</table>
	<p>&nbsp;</p>
	<table cellspacing="0" cellpadding="2" width="95%" border="1"
		align="center">
		<tr class="body">
			<td width="10%"><span class="formLabel">Data da consulta</span></td>
			<td width="20%"><span class="formLabel"> <bean:write
				name="procedimentoRealizadoForm"
				property="appointment.appointmentDate" /> </span></td>
			<td width="10%"><span class="formLabel">Paciente</span></td>
			<td width="20%"><span class="formLabel"> <bean:write
				name="procedimentoRealizadoForm"
				property="appointment.demographic.name" /> </span></td>
		</tr>
		<tr class="body">
			<td width="10%"><span class="formLabel">M&eacute;dico</span></td>
			<td width="20%"><span class="formLabel"> <bean:write
				name="procedimentoRealizadoForm"
				property="appointment.provider.name" /> </span></td>
			<td width="10%"><span class="formLabel">Motivo da
			consulta</span></td>
			<td width="20%"><span class="formLabel"> <bean:write
				name="procedimentoRealizadoForm" property="appointment.reason" /> </span></td>
		</tr>
	</table>
	<p>&nbsp;</p>
	<table cellspacing="0" cellpadding="2" width="95%" border="1"
		align="center">
		<tr class="body">
			<td width="10%"><span class="formLabel">Tipo de
			Atendimento</span></td>
			<td width="20%"><span class="formLabel"> <html:select
				property="tpAtendimento">
				<html:options collection="TPATENDIMENTO"
					property="coTipoatendimento" labelProperty="dsTipoatendimento" />
			</html:select></td>
			</span>
		</tr>
	</table>
	<p>&nbsp;</p>
	<table cellspacing="0" cellpadding="2" width="95%" border="1"
		align="center">
		<tr class="body">
			<td width="10%"><span class="formLabel">Formul&aacute;rio
			de Faturamento</span></td>
			<td width="90%"><html:select property="formulario.coFormulario">
				<html:options name="procedimentoRealizadoForm"
					collection="FORMULARIOS" property="coFormulario"
					labelProperty="dsFormulario" />
			</html:select>&nbsp; <html:submit onclick="set('formulario');"> Pesquisar </html:submit>
			</td>
		</tr>
	</table>
	<p>&nbsp;</p>
	<table cellspacing="0" cellpadding="2" width="95%" border="1"
		align="center">
		<tr>
			<th>Procedimentos encontrados no formul&aacute;rio <b><bean:write
				name="procedimentoRealizadoForm" property="formulario.dsFormulario" /></b></th>
		</tr>
	</table>
	<br>
	<table cellspacing="2" cellpadding="2" width="95%" border="1"
		align="center">
		<tr>
			<th class="tableLabel" width="10%" align="center">Marcar</th>
			<th class="tableLabel" width="90%" align="left">Descri&ccedil;&atilde;o</th>
		</tr>
		<logic:present name="procedimentoRealizadoForm"
			property="procedimentosForm">
			<logic:iterate id="procedimento" name="procedimentoRealizadoForm"
				property="procedimentosForm">
				<tr class="body">
					<td>
					<div align="center"><html:multibox
						name="procedimentoRealizadoForm" property="procedimentosChecked">
						<bean:write name="procedimento"
							property="cadProcedimentos.coProcedimento" />
					</html:multibox></div>
					</td>
					<td><span class="formLabel"><bean:write
						name="procedimento" property="cadProcedimentos.coProcedimento" />&nbsp;
					<bean:write name="procedimento"
						property="cadProcedimentos.dsProcedimento" /></span></td>
				</tr>
			</logic:iterate>
		</logic:present>
	</table>
	<br>
	<table cellspacing="0" cellpadding="2" width="95%" border="0"
		align="center">
		<tr>
			<td align="left"><html:submit onclick="set('procedimento');"> Inserir procedimentos selecionados </html:submit></td>
		</tr>
	</table>
	<br>
	<table cellspacing="0" cellpadding="2" width="95%" border="1"
		align="center">
		<tr class="body">
			<td width="20%"><a href="#" class="formLink"
				onclick="popupProcedimento()">Pesquisar</a> &nbsp; <span
				class="formLabel">Procedimento</span></td>
			<td width="80%"><html:text
				property="cadProcedimentos.coProcedimento" size="10" maxlength="8"
				styleId="coProc" /> &nbsp; <html:text
				property="cadProcedimentos.dsProcedimento" size="50" maxlength="100"
				styleId="dsProc" /> &nbsp; <html:submit onclick="set('add_proc');"> Inserir </html:submit>
			</td>
		</tr>
	</table>
	<br>
	<table cellspacing="0" cellpadding="2" width="95%" border="1"
		align="center">
		<tr>
			<th>Rela&ccedil;&atilde;o dos Procedimentos Realizados no
			Paciente</th>
		</tr>
	</table>
	<br>
	<table cellspacing="0" cellpadding="2" width="95%" border="1"
		align="center">
		<tr>
			<th class="tableLabel" width="80%" align="left">Descri&ccedil;&atilde;o</th>
			<th class="tableLabel" width="20%" align="center">A&ccedil;&atilde;o</th>
		</tr>
		<logic:iterate id="procedimentoRealizado"
			name="procedimentoRealizadoForm"
			property="appointment.procedimentoRealizado">
			<tr class="body">
				<td><span class="formLabel"><bean:write
					name="procedimentoRealizado"
					property="cadProcedimentos.coProcedimento" />&nbsp; <bean:write
					name="procedimentoRealizado"
					property="cadProcedimentos.dsProcedimento" /></span></td>
				<td><html:link
					page="/oscar/billing/procedimentoRealizado/deleteProc.do"
					paramName="procedimentoRealizado" paramId="coProc"
					paramProperty="cadProcedimentos.coProcedimento"
					styleClass="formLink">
	            Remover
	          </html:link></td>
			</tr>
		</logic:iterate>
	</table>
	<br>
	<table cellspacing="0" cellpadding="2" width="95%" border="1"
		align="center">
		<tr>
			<th>Diagn&oacute;stico</th>
		</tr>
	</table>
	<br>
	<table cellspacing="0" cellpadding="2" width="95%" border="1"
		align="center">
		<tr class="body">
			<td width="20%"><a href="#" class="formLink"
				onclick="popupCid()">Pesquisar</a> &nbsp; <span class="formLabel">C&oacute;digo
			CID-10*</span></td>
			<td width="80%"><html:text property="coCid" size="10"
				maxlength="4" styleId="coCid" /> &nbsp; <html:text property="dsCid"
				size="50" maxlength="134" styleId="dsCid" /> <html:submit
				onclick="set('diagnostico');"> Inserir diagn&oacute;stico </html:submit>
			</td>
		</tr>
	</table>
	<br>
	<table cellspacing="0" cellpadding="2" width="95%" border="1"
		align="center">
		<tr>
			<th>Rela&ccedil;&atilde;o dos Diagn&oacute;sticos do Paciente</th>
		</tr>
	</table>
	<br>
	<table cellspacing="0" cellpadding="2" width="95%" border="1"
		align="center">
		<tr>
			<th class="tableLabel" width="80%" align="left">Descri&ccedil;&atilde;o</th>
			<th class="tableLabel" width="20%" align="center">A&ccedil;&atilde;o</th>
		</tr>
		<logic:iterate id="diagnostico" name="procedimentoRealizadoForm"
			property="appointment.diagnostico">
			<tr class="body">
				<td><span class="formLabel"> <bean:write
					name="diagnostico" property="cadCid.coCid" />&nbsp; <bean:write
					name="diagnostico" property="cadCid.dsCid" /> </span></td>
				<td><html:link
					page="/oscar/billing/procedimentoRealizado/deleteDiag.do"
					paramName="diagnostico" paramId="coCid"
					paramProperty="cadCid.coCid" styleClass="formLink">
	            Remover
	          </html:link></td>
			</tr>
		</logic:iterate>
	</table>
	<p align="center"><html:submit onclick="set('gravar');"> Gravar </html:submit>
	</p>

	<html:hidden property="dispatch" value="error" />
	<SCRIPT>function set(target) {document.forms[0].dispatch.value=target;}</SCRIPT>

</html:form>

<table border="0" cellspacing="0" cellpadding="0" width="100%"
	align="center">
	<tr>
		<td><a href="#" onclick="window.close();"> <img
			src="../../../images/leftarrow.gif" border="0" width="25" height="20"
			align="absmiddle"><bean:message key="global.btnBack" /></a></td>
		<td align="right"><a href="#" onclick="window.close();"><bean:message
			key="global.btnLogout" /><img src="../../../images/rightarrow.gif"
			border="0" width="25" height="20" align="absmiddle"></a></td>
	</tr>
</table>

</body>

</html:html>
</html>
