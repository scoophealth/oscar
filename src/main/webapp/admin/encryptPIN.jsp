<jsp:useBean id="apptMainBean" class="oscar.AppointmentMainBean"
	scope="session" />
<%
   apptMainBean.encryptPIN();
%>
<script type="text/javascript">
window.opener.close();
self.close();
</script>
