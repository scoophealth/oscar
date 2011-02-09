<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page import="java.util.*, oscar.oscarBilling.ca.on.pageUtil.*"%>

<%//
			if (session.getAttribute("user") == null)
				response.sendRedirect("../../../../../logout.jsp");

			BillingSpecPrep obj = new BillingSpecPrep();
			Vector vec = obj.getBillingClaimObj(request);
			boolean billSaved = obj.addABillingRecord(vec);

			if(billSaved) {
                String prevention = request.getParameter("goPrev");
				if ( prevention != null && !prevention.equalsIgnoreCase("") ) {
					
				response.sendRedirect("../../../../../oscarPrevention/AddPreventionData.jsp?prevention=" + prevention + "&demographic_no="
								+ request.getParameter("functionid"));
				}
			}

%>


<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<script LANGUAGE="JavaScript">
    <!--
    function start(){
      this.focus();
    }
    //-->
</script>
</head>
<body onload="start()">
<center>
<table border="0" cellspacing="0" cellpadding="0" width="90%">
	<tr bgcolor="#486ebd">
		<th>ADD A BILLING RECORD</th>
	</tr>
</table>
<%if (billSaved) {

			%>
<h1>Successful Addition of a billing Record.</h1>
<script LANGUAGE="JavaScript">             
              self.close();
              self.opener.refresh();              
        </script> <%} else {%>
<h1>Sorry, addition has failed.</h1>
<%}%>
<p></p>
<hr width="90%"></hr>
<form><input type="button" value="Close this window"
	onClick="window.close()"></form>
</center>
</body>
</html>
