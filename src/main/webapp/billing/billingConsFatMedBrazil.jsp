<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<html>
<html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>Faturamento</title>
<link rel="stylesheet" href="../../../web_fat.css">
</head>

<body background="../../../images/gray_bg.jpg" bgproperties="fixed"
	onLoad="setfocus()" topmargin="0" leftmargin="0" rightmargin="0">
<html:form method="post"
	action="/oscar/billing/consultaFaturamentoMedico/search.do">
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
			<td width="10%"><span class="formLabel">Situa&ccedil;&atilde;o</span></td>
			<td width="90%"><html:radio property="tipoPesquisa"
				value="<%= oscar.billing.model.Appointment.PENDENTE %>" /><span
				class="formLabel">Pendente</span> &nbsp; <html:radio
				property="tipoPesquisa"
				value="<%= oscar.billing.model.Appointment.AGENDADO %>" /><span
				class="formLabel">Faturado</span> &nbsp; <html:radio
				property="tipoPesquisa"
				value="<%= oscar.billing.model.Appointment.FATURADO %>" /><span
				class="formLabel">BPA</span> &nbsp; <html:radio
				property="tipoPesquisa"
				value="<%= oscar.billing.model.Appointment.TODOS %>" /><span
				class="formLabel">Todos</span> &nbsp;</td>
		</tr>
		<tr class="body">
			<td width="10%"><span class="formLabel">M&eacute;dico</span></td>
			<td width="90%"><html:select property="medico.providerNo">
				<html:options collection="PROVIDERS" property="providerNo"
					labelProperty="name" />
			</html:select>&nbsp;</td>
		</tr>
	</table>
	<br>
	<table cellspacing="0" cellpadding="2" width="95%" border="0"
		align="center">
		<tr>
			<td align="left"><html:submit> Pesquisar </html:submit></td>
		</tr>
	</table>
	<br>
	<br>
	<table cellspacing="0" cellpadding="2" width="95%" border="1"
		align="center">
		<tr>
			<th>Rela&ccedil;&atilde;o de Faturamentos <b><bean:write
				name="consultaFaturamentoMedicoForm" property="descTitle" /></b></th>
		</tr>
	</table>
	<br>
	<table cellspacing="2" cellpadding="2" width="95%" border="1"
		align="center">
		<tr>
			<th class="tableLabel" width="10%" align="center">Data da
			consulta</th>
			<th class="tableLabel" width="10%" align="center">Nr. da
			consulta</th>
			<th class="tableLabel" width="40%" align="left">M&eacute;dico</th>
			<th class="tableLabel" width="40%" align="left">Paciente</th>
		</tr>
		<logic:present name="consultaFaturamentoMedicoForm"
			property="consultas">
			<logic:iterate id="consulta" name="consultaFaturamentoMedicoForm"
				property="consultas" type="oscar.billing.model.Appointment">
				<tr class="body">
					<td>
					<div align="center"><span class="formLabel"> <bean:write
						name="consulta" property="appointmentDate" /> </span></div>
					</td>
					<td>
					<div align="center"><span class="formLabel"> <a
						href="/oscar/oscar/billing/procedimentoRealizado/init.do?appId=<%= consulta.getAppointmentNo() %>"
						title="Preencher fatura"> <bean:write name="consulta"
						property="appointmentNo" /> </a> </span></div>
					</td>
					<td>
					<div align="left"><span class="formLabel"> <bean:write
						name="consulta" property="provider.name" /> </span></div>
					</td>
					<td>
					<div align="left"><span class="formLabel"> <bean:write
						name="consulta" property="demographic.name" /> </span></div>
					</td>
				</tr>
			</logic:iterate>
		</logic:present>
	</table>
</html:form>
</body>

</html:html>
</html>
