<%--

    Copyright (c) 2008-2012 Indivica Inc.

    This software is made available under the terms of the
    GNU General Public License, Version 2, 1991 (GPLv2).
    License details are available via "indivica.ca/gplv2"
    and "gnu.org/licenses/gpl-2.0.html".

--%>
<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="oscar.oscarMDS.data.ProviderData, java.util.ArrayList"%>

<link rel="stylesheet" type="text/css" href="encounterStyles.css">
<link rel="stylesheet" type="text/css" media="all" href="../share/css/extractedFromPages.css"  />
<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title><bean:message key="oscarMDS.selectProvider.title" /></title>
</head>

<script language='JavaScript'>

function doStuff() {
    
  var   allSelected = "";
    if ( document.providerSelectForm.selectedProviders.selectedIndex == -1 ) {
        alert ("Please select at least one provider");
    } else {
        for (i=0; i < document.providerSelectForm.selectedProviders.options.length; i++) {
            if (document.providerSelectForm.selectedProviders.options[i].selected) {
                if (allSelected != "") {
                    allSelected = allSelected + ",";
                }
                allSelected = allSelected + document.providerSelectForm.selectedProviders.options[i].value;
            }
        }
        
        self.opener.document.getElementsByName("selectedProviders")[0].value = allSelected;
        self.opener.ForwardSelectedRows();
        setTimeout("self.close();", 1000);
    }
}

</script>

<body>
<form name="providerSelectForm" method="post" action="AssignLab.do">
<center>
<p><font size="-1"><bean:message
	key="oscarMDS.selectProvider.msgSelectProvider" />:</font></p>
<select name="selectedProviders" size="10" multiple>
	<% ArrayList providers = ProviderData.getProviderList();
                       for (int i=0; i < providers.size(); i++) { %>
	<option value="<%= (String) ((ArrayList) providers.get(i)).get(0) %>"
		<%= ( ((String) ((ArrayList) providers.get(i)).get(0)).equals(request.getParameter("providerNo")) ? " selected" : "" ) %>><%= (String) ((ArrayList) providers.get(i)).get(1) %>
	<%= (String) ((ArrayList) providers.get(i)).get(2) %></option>
	<% } %>
</select>
<p><input type="button" class="button"
	value="<bean:message key="oscarMDS.selectProvider.btnOk"/>"
	onclick="doStuff()"> <input type="button" class="button"
	value="<bean:message key="oscarMDS.selectProvider.btnCancel"/>"
	onclick="window.close()"></p>
</center>
</form>
</body>
</html>