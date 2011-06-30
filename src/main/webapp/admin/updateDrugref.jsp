
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="oscar.oscarProvider.data.*,java.util.*,org.oscarehr.util.SpringUtils,org.oscarehr.common.dao.QueueDao" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<html>
    <head>
        <script type="text/javascript" src="<c:out value="${ctx}/share/javascript/prototype.js"/>"></script>
        <script type="text/javascript" src="<c:out value="${ctx}/share/javascript/Oscar.js"/>"></script>
        <script type="text/javascript" src="../share/yui/js/yahoo-dom-event.js"></script>
        <script type="text/javascript" src="../share/yui/js/connection-min.js"></script>
        <script type="text/javascript" src="../share/yui/js/animation-min.js"></script>
        <script type="text/javascript" src="../share/yui/js/datasource-min.js"></script>
        <script type="text/javascript" src="../share/yui/js/autocomplete-min.js"></script>
        <link rel="stylesheet" type="text/css" href="../share/yui/css/fonts-min.css"/>
        <link rel="stylesheet" type="text/css" href="../share/yui/css/autocomplete.css"/>
        <link rel="stylesheet" type="text/css" href="../share/css/OscarStandardLayout.css" />
        <style type="text/css">
            body.mainbody {
                padding: 0px;
                margin: 3px;
                font-size: 13px;
            }



        </style>
    </head>
    <body class="mainbody" onload="getUpdateTime();" >
         <table class="MainTable" id="scrollNumber1" name="encounterTable" style="margin: 0px;">
            <tr class="topbar">
                <td class="MainTableTopRowLeftColumn" width="60px">Drugref</td>
                <td class="MainTableTopRowRightColumn">
                    <table class="TopStatusBar">
                        <tr>
                            <td>Update Drugref</td>
                            <td style="text-align: right;"  >
                                    <oscar:help keywords="1.6.11" key="app.top1"/> |
                                    <a href="javascript: popupStart(300, 400, 'About.jsp')">About</a> |
                                    <a href="javascript: popupStart(300, 400, 'License.jsp')">License</a>
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
         </table>
        <table  cellspacing="0" id="queue_provider_table" style="margin: 0px;" >
            <tr><td colspan="2" ><a id="dbInfo"></a></td></tr>
            <tr><td><a id="updatedb" style="display:none" onclick="updateDB();" href="javascript:void(0);" >Update Drugref Database</a></td><td><a id="updateResult"></a></td></tr>
         </table>
    </body>
    <script type="text/javascript">
        function getUpdateTime(){
          var data="method=getLastUpdate";
                var url="<c:out value='${ctx}'/>"+"/oscarRx/updateDrugrefDB.do";
                new Ajax.Request(url,{method:'post',parameters:data,onSuccess:function(transport){
                        var json=transport.responseText.evalJSON();
                        if(json.lastUpdate==null){
                            $('dbInfo').innerHTML='Drugref database has not been updated,please update.';
                            $('updatedb').show();
                        }
                        else if(json.lastUpdate=='updating'){
                            $('dbInfo').innerHTML='Drugref database is updating';
                            $('updatedb').hide();
                        }
                        else{
                            $('dbInfo').innerHTML='Drugref has been updated on '+json.lastUpdate;
                            $('updatedb').show();
                        }
                },onFailure:function(transport){
                	$('dbInfo').innerHTML='Drugref database has not been updated,please update.';
                	$('updatedb').show(); 
                	}})

    }
         function updateDB(){
                var data="method=updateDB";
                var url="<c:out value='${ctx}'/>"+"/oscarRx/updateDrugrefDB.do";
                new Ajax.Request(url,{method:'post',parameters:data,onSuccess:function(transport){
                        var json=transport.responseText.evalJSON();
                        if(json.result=='running')
                            $('updateResult').innerHTML="Update has started, it'll take about 1 hour to finish";
                        else if (json.result=='updating')
                            $('updateResult').innerHTML="Some one has already been updating it";
                }})
            }
    </script>
</html>
