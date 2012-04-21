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

<%-- 
    Document   : drugrefInfo
    Created on : Mar 29, 2011, 11:46:18 AM
    Author     : jackson
--%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<html>
    <head>
        <title>Drugref Info</title>
        <script type="text/javascript" src="<c:out value="${ctx}/share/javascript/prototype.js"/>"></script>

        <script type="text/javascript" src="<c:out value="${ctx}/share/javascript/Oscar.js"/>"></script>
    </head>
     <script type="text/javascript">
        function getUpdateTime(){
          var data="method=getLastUpdate";
                var url="<c:out value='${ctx}'/>"+"/oscarRx/updateDrugrefDB.do";
                new Ajax.Request(url,{method:'post',parameters:data,onSuccess:function(transport){
                        var json=transport.responseText.evalJSON();
                        if(json.lastUpdate==null)
                            $('dbInfo').innerHTML='Drugref database has not been updated,please update.';
                        else if(json.lastUpdate=='updating')
                            $('dbInfo').innerHTML='Drugref database is updating';
                        else
                            $('dbInfo').innerHTML='Drugref has been updated on '+json.lastUpdate;
                }})

    }
    </script>
    <body onload="getUpdateTime();" >
        <a id="dbInfo"></a>
    </body>
</html>
