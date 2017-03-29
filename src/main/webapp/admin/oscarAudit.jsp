<%--

    Copyright (c) 2013-2015. Department of Computer Science, University of Victoria. All Rights Reserved.
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
    Department of Computer Science
    LeadLab
    University of Victoria
    Victoria, Canada

--%>

<%@page contentType="text/html" language="java" %>
<%@include file="/casemgmt/taglibs.jsp"%>
<html>
    <link rel="stylesheet" type="text/css" href="../oscarEncounter/encounterStyles.css">
    <link href="<%=request.getContextPath() %>/css/bootstrap.css" rel="stylesheet" type="text/css">
    <style>
        pre {
            white-space: pre-wrap;
            word-break: normal;
        }
    </style>
    <title>Oscar Audit</title>
    <head> 
        <script type="text/javascript" src="<%=request.getContextPath() %>/js/jquery-1.9.1.js"></script>
        <script>
            $(document).ready(function() {	
                parent.parent.resizeIframe($('html').height());	
                
            });
        </script>	
        <div class="page-header">
            <h4>OSCAR Audit</h4>
        </div>
    </head>
    <body> 
        <h5>Server Information:</h5>
        <pre>${serverVersion}</pre>

        <h5>Database Information:</h5>
        <pre>${databaseInfo}</pre>

        <h5>Verify Tomcat:</h5>
        <pre>${verifyTomcat}</pre>

        <h5>Verify Oscar:</h5>
        <pre>${verifyOscar}</pre>
        
        <h5>Verify Drugref:</h5>
        <pre>${verifyDrugref}</pre>

        <h5>Tomcat Reinforcement:</h5>
        <pre>${tomcatReinforcement}</pre>
    </body>
</html>
