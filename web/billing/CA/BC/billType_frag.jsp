
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
<%@page contentType="text/html; charset=iso-8859-1" language="java"
	import="java.sql.*" errorPage=""%>
<%@page
	import="java.math.*, java.util.*, java.sql.*, oscar.*, java.net.*,oscar.oscarBilling.ca.bc.MSP.*,oscar.oscarBilling.ca.bc.data.*;"%>
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
