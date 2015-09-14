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
<%@include file="/casemgmt/taglibs.jsp"%>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
      String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
      boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_admin,_admin.userAdmin" rights="w" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../securityError.jsp?type=_admin&type=_admin.userAdmin");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>

<%@ page import="oscar.oscarProvider.data.ProviderData, java.util.ArrayList,java.util.Map, java.util.List, org.oscarehr.util.SpringUtils"%>
<%@ page import="org.oscarehr.common.dao.ProviderLabRoutingFavoritesDao, org.oscarehr.common.model.ProviderLabRoutingFavorite" %>
<%@ page import="org.oscarehr.PMmodule.dao.ProviderDao, org.oscarehr.common.model.Provider" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" scope="request"/>
<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title><bean:message key="oscarMDS.selectProvider.title" /></title>
<script type="text/javascript" src="../share/javascript/prototype.js"></script>
        <script type="text/javascript" src="../share/javascript/effects.js"></script>
        <script type="text/javascript" src="../share/javascript/controls.js"></script>

        <script type="text/javascript" src="../share/yui/js/yahoo-dom-event.js"></script>
        <script type="text/javascript" src="../share/yui/js/connection-min.js"></script>
        <script type="text/javascript" src="../share/yui/js/animation-min.js"></script>
        <script type="text/javascript" src="../share/yui/js/datasource-min.js"></script>
        <script type="text/javascript" src="../share/yui/js/autocomplete-min.js"></script>
        <script type="text/javascript" src="../js/demographicProviderAutocomplete.js"></script>

        <link rel="stylesheet" type="text/css" href="../share/yui/css/fonts-min.css"/>
        <link rel="stylesheet" type="text/css" href="../share/yui/css/autocomplete.css"/>
        <link rel="stylesheet" type="text/css" media="all" href="../share/css/demographicProviderAutocomplete.css"  />

</head>

<script type="text/javascript">
function prepSubmit() {

	if( $("fwdProviders").options.length == 0 ) {
		alert("Please select a provider first.")
		return false;
	}
	
	
    for (i=0; i < $("fwdProviders").options.length; i++) {
           $("fwdProviders").options[i].selected = true;
    }
    
	return true;
}


/*function pageLoad() {
var url = "<c:out value="${ctx}"/>/provider/SearchProvider.do";

new Ajax.Autocompleter("autocompleteProvider", "providerList", url, {
	  paramName: "value",
	  minChars: 2,
	  indicator: 'working',
	});


};
*/

</script>
</head>
<body>
<form name="providerSelectForm" method="post" action="../study/ManageStudy.do">
<input type="hidden" name="method" value="AddProvider" />
<input type="hidden" name="studyId" value="<%=request.getParameter("studyId")%>" />
<p style="text-align:center; background-color:#9999CC; border-color:#CCCCFF #6666CC #6666CC #CCCCFF;
border-left:thin solid #CCCCFF; border-right:thin solid #6666CC; border-style:solid; border-width:thin; color:white;
font-weight:bold;">
Type in a provider's last name and select it.
</p>
<div style="text-align:center;">
    <input id="autocompleteprov" type="text">
    <div id="autocomplete_choicesprov" class="autocomplete"></div>
</div>
<div style="text-align:center;">
	Selected Providers<br>
	<select id="fwdProviders" name="providerNo" size="10" style="width:250px;" multiple="multiple" ondblclick="removeProvider(this);"></select>
</div>
<div style="text-align:center;">	
	<input type="submit" value="Submit" onclick="return prepSubmit();">
</div>
</form>
<script type="text/javascript">
YAHOO.example.BasicRemote = function() {
    var url = "<%= request.getContextPath() %>/provider/SearchProvider.do";
    var oDS = new YAHOO.util.XHRDataSource(url,{connMethodPost:true,connXhrMode:'ignoreStaleResponses'});
    oDS.responseType = YAHOO.util.XHRDataSource.TYPE_JSON;// Set the responseType
    // Define the schema of the delimited resultsTEST, PATIENT(1985-06-15)
    oDS.responseSchema = {
        resultsList : "results",
        fields : ["providerNo","firstName","lastName"]
    };
    // Enable caching
    oDS.maxCacheEntries = 0;

    // Instantiate the AutoComplete
    var oAC = new YAHOO.widget.AutoComplete("autocompleteprov", "autocomplete_choicesprov", oDS);
    oAC.queryMatchSubset = true;
    oAC.minQueryLength = 3;
    oAC.maxResultsDisplayed = 25;
    oAC.formatResult = resultFormatter3;
    //oAC.typeAhead = true;
    oAC.queryMatchContains = true;

    oAC.itemSelectEvent.subscribe(function(type, args) {
    	$("autocompleteprov").value = "";
    	var name = args[2][2] + ", " + args[2][1];
    	var id = args[2][0];

    	var selectObj = $("fwdProviders");
    	var option = document.createElement("option");
    	option.text = name;
    	option.value = id;
    	option.id = id;

    	try {
    	  // for IE earlier than version 8
    	  selectObj.add(option,selectObj.options[null]);
    	}
    	catch (e) {
    		selectObj.add(option,null);
    	}

    });


    return {
        oDS: oDS,
        oAC: oAC
    };
}();

$("autocompleteprov").focus();

function removeProvider(selectObj) {
	selectObj.remove(selectObj.selectedIndex);
}
</script>
</body>
</html>
