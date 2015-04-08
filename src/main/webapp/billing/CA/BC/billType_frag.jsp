<%@page import="java.sql.*" errorPage=""%>
<%@page
	import="java.math.*, java.util.*, java.sql.*, oscar.*, java.net.*,oscar.oscarBilling.ca.bc.MSP.*,oscar.oscarBilling.ca.bc.data.*"%>
<%@taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<!--
  -
  This jsp fragment displays the Bill Type drop down which is used by the MSP and WCB "corrections" screens.
  TODO: Localize Strings
-->
<%
  BillingFormData billForm = new BillingFormData();
  Properties statusTypeProps = billForm.getStatusProperties(billForm.getStatusTypes(null));
  String[] codes = (String[]) request.getAttribute("codes");
  String BillType = request.getParameter("BillType");
  List statusTypes = billForm.getStatusTypes(codes);
  request.setAttribute("statusTypes", statusTypes);
%>
<style>
label {
	font-weight: bold;
}
</style>
<script type="text/javascript">
    function callToggleWCB(){
        if (toggleWCB){
            toggleWCB();
        }
    }
</script>
<table>
	<tr>
		<td nowrap="nowrap"><label for="billtype">Billing Type: </label>
		</td>
		<td>
		<div id="billtype"><%=statusTypeProps.getProperty(BillType)%></div>
		</td>
	</tr>
	<tr>
		<td><label for="status">Change Type:</label></td>
		<td><html:select styleId="status" property="status"
			onchange="javascript:document.forms[0].xml_status.value = this.value;callToggleWCB();">
			<html:options collection="statusTypes" property="billingstatus"
				labelProperty="displayNameExt" />
		</html:select></td>
	</tr>
</table>
<input type="hidden" name="xml_status" value="<%=BillType%>">
<br />
