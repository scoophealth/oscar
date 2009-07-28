v<%
  	if (session.getAttribute("user") == null){
		response.sendRedirect("../logout.jsp");
	}
%>

<%@ page
	import="oscar.form.*, java.util.*,oscar.oscarBilling.ca.bc.pageUtil.*,oscar.oscarDB.*,oscar.oscarBilling.ca.bc.MSP.*"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/rewrite-tag.tld" prefix="rewrite"%>

<html:html locale="true">
<% response.setHeader("Cache-Control","no-cache");%>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<script type="text/javascript" src="../../../share/javascript/Oscar.js"></script>
<html:base />
<title>OSCAR BC Billing - WCB</title>
<script language="JavaScript">
  function billingFormActive(){
     oscarLog("billingFormActive")
      if(window.opener && window.opener.replaceWCB){
          oscarLog("Calling on replaceWCB");
        window.opener.replaceWCB('<%=request.getAttribute("WCBFormId")%>');  
      }
      oscarLog("billingFormActiveEnd");
      window.close();
  }  
</script>
</head>
<body onLoad="billingFormActive()" bgproperties="fixed" topmargin="0" leftmargin="0" rightmargin="0">
    <input type="button" value="Close" onclick="window.close();"/>
</body>
</html:html>


<%!

String checked(String val,String str,boolean dfault){
    String retval = "";
    if(str == null || str.equals("null")){
        str = "";
    }
    if (str.equals("")  && dfault){
        retval = "CHECKED";
    }else if (str != null && str.equalsIgnoreCase(val) ){
        retval = "CHECKED";
    }
    return retval;
}

boolean isEmpty(ArrayList a){
   boolean isEmpty = false;
   if ( a.size() == 0 ) isEmpty = true;
   return isEmpty;
}
%>