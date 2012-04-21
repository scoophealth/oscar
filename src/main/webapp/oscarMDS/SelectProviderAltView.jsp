<%--

    Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
    This software is published under the GPL GNU General Public License.
    This program is free software; you can redistribute it and/or
    modify it under the terms of the GNU General Public License
    as published by the Free Software Foundation; either version 2
    of the License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program; if not, write to the Free Software
    Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.

    This software was written for the
    Department of Family Medicine
    McMaster University
    Hamilton
    Ontario, Canada

--%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="oscar.oscarMDS.data.ProviderData, java.util.ArrayList"%>

<link rel="stylesheet" type="text/css" href="encounterStyles.css">
<link rel="stylesheet" type="text/css" media="all" href="../share/css/extractedFromPages.css"  />
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<script type="text/javascript" src="<c:out value="${ctx}/phr/phr.js"/>"></script>
        <script type="text/javascript" src="<c:out value="${ctx}/share/javascript/prototype.js"/>"></script>
        <script type="text/javascript" src="<c:out value="${ctx}/share/javascript/screen.js"/>"></script>
        <script type="text/javascript" src="<c:out value="${ctx}/share/javascript/rx.js"/>"></script>
        <script type="text/javascript" src="<c:out value="${ctx}/share/javascript/scriptaculous.js"/>"></script>
        <script type="text/javascript" src="<c:out value="${ctx}/share/javascript/effects.js"/>"></script>
        <script type="text/javascript" src="<c:out value="${ctx}/share/javascript/controls.js"/>"></script>
        <script type="text/javascript" src="<c:out value="${ctx}/share/javascript/Oscar.js"/>"></script>
<title><bean:message key="oscarMDS.selectProvider.title" /></title>
</head>
<%String docId=request.getParameter("doc_no");%>
<script language='JavaScript'>

function doStuff() {
  var docId='<%=docId%>';
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
        //self.close();
        var f=self.opener.document.forms['reassignForm_'+docId];
        f.selectedProviders.value = allSelected;
        forwardDocLab(docId);
        alert("Success!");
       // f.submit();
        self.close();
    }
}
function forwardDocLab(doclabid){
            
            if(doclabid){
                var data=Form.serialize(self.opener.document.forms['reassignForm_'+doclabid]);
                
                var url= '<%=request.getContextPath()%>' + "/oscarMDS/Forward.do";
                new Ajax.Request(url,{method: 'post',postBody:data, onSuccess:function(transport){
                        
                        
                    }});
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
