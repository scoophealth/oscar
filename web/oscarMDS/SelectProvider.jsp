<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="oscar.oscarMDS.data.ProviderData, java.util.ArrayList" %>

<link rel="stylesheet" type="text/css" href="encounterStyles.css">
<style type="text/css">
<!--
select       { width: 300px; }
.button      { width: 100px; }
-->
</style>

<html>
<head>
<title>
Select Provider
</title>
</head>

<script language='JavaScript'>

function doStuff() {
    allSelected = "";
    if ( this.providerSelectForm.selectedProviders.selectedIndex == -1 ) {
        alert ("Please select at least one provider");
    } else {
        for (i=0; i < this.providerSelectForm.selectedProviders.options.length; i++) {
            if (this.providerSelectForm.selectedProviders.options[i].selected) {
                if (allSelected != "") {
                    allSelected = allSelected + ",";
                }
                allSelected = allSelected + this.providerSelectForm.selectedProviders.options[i].value;
            }
        }
        opener.reassignForm.selectedProviders.value = allSelected;
        opener.reassignForm.submit();
        window.close();
    }
}

</script>

<body>
    <form name="providerSelectForm" method="post" action="AssignLab.do">
        <center>
        <p><font size="-1">Select the provider(s)<br>who will receive the selected labs:</font></p>        
            <select name="selectedProviders" size="10" multiple>
                <% ArrayList providers = ProviderData.getProviderList();
                       for (int i=0; i < providers.size(); i++) { %>
                           <option value="<%= (String) ((ArrayList) providers.get(i)).get(0) %>"<%= ( ((String) ((ArrayList) providers.get(i)).get(0)).equals(request.getParameter("providerNo")) ? " selected" : "" ) %>><%= (String) ((ArrayList) providers.get(i)).get(1) %> <%= (String) ((ArrayList) providers.get(i)).get(2) %></option>
                <% } %>
            </select>
            <p>
                <input type="button" class="button" value="Ok" onclick="doStuff()">
                <input type="button" class="button" value="Cancel" onclick="window.close()">
            </p>
        </center>
    </form>
</body>
</html>