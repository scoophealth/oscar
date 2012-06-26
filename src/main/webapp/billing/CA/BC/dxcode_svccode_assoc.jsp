<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/rewrite-tag.tld" prefix="rewrite"%>
<%@page import="oscar.oscarDemographic.data.*"%>
<%@page
	import="java.text.*, java.util.*, oscar.oscarBilling.ca.bc.data.*,oscar.oscarBilling.ca.bc.pageUtil.*"%>
<html:html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title><bean:message
	key="oscar.billing.CA.BC.billingBC.dxcode_svccode_assoc.title" /></title>
<html:base />
<link rel="stylesheet" type="text/css" media="all" href="../share/css/extractedFromPages.css"  />
<script language="JavaScript">
<!--
var remote=null;

function rs(n,u,w,h,x) {
  args="width="+w+",height="+h+",resizable=yes,scrollbars=yes,status=0,top=60,left=30";
  remote=window.open(u,n,args);
  if (remote != null) {
    if (remote.opener == null){
	  remote.opener = self;

	 }
  }
  if (x == 1) { return remote; }
}


var awnd=null;
function ScriptAttach() {


  t0 = escape(document.forms[0].xml_diagnostic_detail1.value);
  t1 = escape(document.forms[0].xml_diagnostic_detail2.value);
  t2 = escape(document.forms[0].xml_diagnostic_detail3.value);
  awnd=rs('att','billingDigNewSearch.jsp?name='+t0 + '&name1=' + t1 + '&name2=' + t2 + '&search=',600,600,1);
  awnd.focus();



}



function OtherScriptAttach() {
  t0 = escape(document.forms[0].xml_other1.value);
  awnd=rs('att','<rewrite:reWrite jspPage="billingCodeNewSearch.jsp"/>?name='+t0 + '&name1=&name2=&search=',820,660,1);
  awnd.focus();
}

//-->
</SCRIPT>
<script language="JavaScript">
</script>
<link rel="stylesheet"
	href="../../../oscarBilling/CA/billing/billing.css" type="text/css">
</head>
<%
String mode = request.getAttribute("mode")!=null?(String)request.getAttribute("mode"):"";
%>
<body bgcolor="#FFFFFF" text="#000000" rightmargin="0" leftmargin="0"
	topmargin="10" marginwidth="0" marginheight="0">
<h2><html:errors /></h2>
<html:form action="/billing/CA/BC/saveAssocAction"
	target="oscar.oscarBilling.ca.bc.pageUtil.BillingCreateBillingForm">
	<html:hidden property="mode" />
	<table width="75%" border="1" align="center" cellpadding="3"
		cellspacing="3" bgcolor="EEEEFF">
		<tr bgcolor="#000000">
			<td width="90%" height="40" align="left">
			<p><font face="Verdana, Arial, Helvetica, sans-serif"
				color="#FFFFFF"> <b> <font
				face="Arial, Helvetica, sans-serif" size="2"><bean:message
				key="oscar.billing.CA.BC.billingBC.dxcode_svccode_assoc.title" /></font> </b> </font>
			</p>
			</td>
		</tr>
		<tr bgcolor="CCCCFF">
			<td><strong><bean:message
				key="oscar.billing.CA.BC.billingBC.dxcode_svccode_assoc.step1" /></strong></td>
		</tr>
		<tr>
			<td width="78%"><font
				face="Verdana, Arial, Helvetica, sans-serif" size="1"> <%
           boolean state= mode.equals("edit")?true:false;
            %> <html:text property="xml_other1" size="40"
				readonly="<%=state%>" /> <a href="javascript:OtherScriptAttach()">
			<img src="../../../images/search_code.jpg" border="0"> </a> </font></td>
		</tr>
		<tr bgcolor="CCCCFF">
			<td><strong><bean:message
				key="oscar.billing.CA.BC.billingBC.dxcode_svccode_assoc.step2" /></strong></td>
		</tr>
		<tr>
			<td><font face="Verdana, Arial, Helvetica, sans-serif" size="1">
			<bean:message
				key="oscar.billing.CA.BC.billingBC.dxcode_svccode_assoc.dxcode" /> <html:text
				property="xml_diagnostic_detail1" size="25" /> </font> <font
				face="Verdana, Arial, Helvetica, sans-serif" size="1"> <a
				href="javascript:ScriptAttach()"> <img
				src="../../../images/search_dx_code.jpg" border="0"> </a> </font> <font
				face="Verdana, Arial, Helvetica, sans-serif" size="1">&nbsp;</font>
			</td>
		</tr>
		<!--This is really quite redundant but its code copied from bcBilling.jsp
      using the same search screens, therefore the hidden fields need to be here so that the javascript doesn't break -->
		<html:hidden property="xml_diagnostic_detail2" />
		<html:hidden property="xml_diagnostic_detail3" />
		<!--Technically don't need three but just in case
      <tr>
        <td>
          <font face="Verdana, Arial, Helvetica, sans-serif" size="1">            DX Code #2:
            <html:text property="xml_diagnostic_detail2" size="25"/>
          </font>
        </td>
      </tr>
      <tr>
        <td>
          <font face="Verdana, Arial, Helvetica, sans-serif" size="1">            DX Code #3:
            <html:text property="xml_diagnostic_detail3" size="25"/>
          </font>
        </td>
      </tr>
      <tr>
        -->
		<td></td>
		</tr>
		<tr bgcolor="CCCCFF">
			<td align="center"><html:submit value="Save" property="Submit" />
			<html:button value="Cancel" property="Button"
				onclick="window.close();" /></td>
		</tr>
	</table>
</html:form>
</body>
</html:html>
