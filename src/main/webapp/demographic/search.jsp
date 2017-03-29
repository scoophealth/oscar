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

<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
    String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
    boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_search" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect(request.getContextPath() + "/securityError.jsp?type=_search");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/oscar-tag.tld" prefix="oscar"%>

<% Boolean isMobileOptimized = session.getAttribute("mobileOptimized") != null; %>

<html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title><bean:message key="demographic.search.title" /></title>
<script type="text/javascript">

        function setfocus() {
            document.titlesearch.keyword.focus();
            document.titlesearch.keyword.select();            
        }
        
        function checkTypeIn() {
          var dob = document.titlesearch.keyword; typeInOK = true;          
          if (dob.value.indexOf('%b610054') == 0 && dob.value.length > 18){
             document.titlesearch.keyword.value = dob.value.substring(8,18);
             document.titlesearch.search_mode[4].checked = true;             
          }

			if(document.titlesearch.search_mode[0].checked) {
				var keyword = document.titlesearch.keyword.value; 
      			var keywordLowerCase = keyword.toLowerCase();
      			document.titlesearch.keyword.value = keywordLowerCase;		
			}
			
          if(document.titlesearch.search_mode[2].checked) {
            if(dob.value.length==8) {
              dob.value = dob.value.substring(0, 4)+"-"+dob.value.substring(4, 6)+"-"+dob.value.substring(6, 8);
            }
            if(dob.value.length != 10) {
              alert('<bean:message key="demographic.search.msgWrongDOB"/>');
              typeInOK = false;
            }
            return typeInOK ;
          } else {
            return true;
          }
        }        
        
        function searchInactive() {
            document.titlesearch.ptstatus.value="inactive";
            if (checkTypeIn()) document.titlesearch.submit();
        }
            
        function searchAll() {
            document.titlesearch.ptstatus.value="";
            if (checkTypeIn()) document.titlesearch.submit();
        }

        function searchOutOfDomain() {
            document.titlesearch.outofdomain.value="true";
            if (checkTypeIn()) document.titlesearch.submit();
        }       
         
        </script>
<% if (isMobileOptimized) { %>
   <meta name="viewport" content="initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no, width=device-width" />
   <link rel="stylesheet" type="text/css" href="../mobile/searchdemographicstyle.css">
<% } else { %>
 <link rel="stylesheet" type="text/css" media="all" href="../demographic/searchdemographicstyle.css"  />
 <link rel="stylesheet" type="text/css" href="../share/css/searchBox.css" />

<% } %>
</head>
<body onload="setfocus()">
<div id="demographicSearch">
</div>
    <!-- Search Box -->
    <%@ include file="zdemographicfulltitlesearch.jsp"%>
<p>
<!-- we may want to not allow students to create new patients? -->
<!-- <security:oscarSec roleName="<%=roleName$%>" objectName="_demographic.addnew" rights="r">  -->
    <div class="createNew">
	<a href="demographicaddarecordhtm.jsp"><b><font size="+1"><bean:message
	key="demographic.search.btnCreateNew" /></font></b></a> 
    </div>
<!-- </security:oscarSec> -->
		
	<oscar:oscarPropertiesCheck
	property="SHOW_FILE_IMPORT_SEARCH" value="yes">
           &nbsp;&nbsp;&nbsp;<a href="demographicImport.jsp"><b><font
		size="+1"><bean:message	key="demographic.search.importNewDemographic" /></font></a>
</oscar:oscarPropertiesCheck></p>
<p><!--a href="http://204.92.240.253:8080/test/slt/Search.jsp"><font size="+1"><bean:message key="demographic.search.btnELearning"/></font></a--></p>
</body>
</html:html>
