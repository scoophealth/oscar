<%-- 
    Document   : drugrefInfo
    Created on : Mar 29, 2011, 11:46:18 AM
    Author     : jackson
--%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
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
