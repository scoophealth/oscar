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

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="oscar.oscarProvider.data.*,java.util.*,org.oscarehr.util.SpringUtils,org.oscarehr.common.dao.QueueDao" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
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
            .swfupload {
                border: 1px solid black;
            }
            body.mainbody {
                padding: 0px;
                margin: 3px;
                font-size: 13px;
            }
            div.maindiv {
                background-color: #f2f7ff;
                border: 1px solid #acb3f5;
                height: 390px;
            }
            div.maindivheading {
                background-color: #acb3f5;
                font-size: 14px;
            }
            label.labels {
                float: left;
                clear: left;
                width: 160px;
            }
            .field {
                float: left;
            }
            table.layouttable {
                font-family: Verdana, Tahoma, Arial, sans-serif;
                font-size: 12px;
            }

            #myAutoComplete {
                width:15em; /* set width here or else widget will expand to fit its container */
                padding-bottom:2em;
            }
            .match {
                    font-weight:bold;
                }
        .yui-ac {
	    position:relative;font-family:arial;font-size:100%;
	}

	/* styles for input field */
	.yui-ac-input {
	    position:relative;width:97%;
	}

	/* styles for results container */
	.yui-ac-container {
	    position:absolute;top:0em;width:100%;
	}

	/* styles for header/body/footer wrapper within container */
	.yui-ac-content {
	    position:absolute;width:100%;border:1px solid #808080;background:#fff;overflow:hidden;z-index:9050;
	}

	/* styles for container shadow */
	.yui-ac-shadow {
	    position:absolute;margin:.0em;width:100%;background:#000;-moz-opacity: 0.10;opacity:.10;filter:alpha(opacity=10);z-index:9049;
	}

	/* styles for results list */
	.yui-ac-content ul{
	    margin:0;padding:0;width:100%;
	}

	/* styles for result item */
	.yui-ac-content li {
	    margin:0;padding:0px 0px;cursor:default;white-space:nowrap;
	}

	/* styles for prehighlighted result item */
	.yui-ac-content li.yui-ac-prehighlight {
	    background:#B3D4FF;
	}

	/* styles for highlighted result item */
	.yui-ac-content li.yui-ac-highlight {
	    background:#426FD9;color:#FFF;
	}

        </style>
    </head>
    <body class="mainbody">
         <table class="MainTable" id="scrollNumber1" name="encounterTable" style="margin: 0px;">
            <tr class="topbar">
                <td class="MainTableTopRowLeftColumn" width="60px">Queues</td>
                <td class="MainTableTopRowRightColumn">
                    <table class="TopStatusBar">
                        <tr>
                            <td>Add new queues</td>
                            <td style="text-align: right;"  >
                                    <oscar:help keywords="queue" key="app.top1"/> |
                                    <a href="javascript: popupStart(300, 400, 'About.jsp')">About</a> |
                                    <a href="javascript: popupStart(300, 400, 'License.jsp')">License</a>
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
         </table>
        <table  cellspacing="0" id="queue_provider_table" style="margin: 0px;" >
            <tr>
                <td>
                    Queue Name:
                </td>
                <td>
                    <input type="text" id="newQueueName" value="Type queue name" onfocus="clearInput(this)" style="color:gray;width:97%"/>
                </td>
                <td>
                    <input type="button" onclick="saveQueue('newQueueName');" value="Submit" />
                </td>
                <td colspan="3">
                    <a id="addQueueSuccessMsg"></a>
                </td>
            </tr>
            <tr>
                <td><br><br></td>
            </tr>
            <tr>
                <td valign="top">
                    Existing queues:
                </td>
                <td>  
                       <%

                        QueueDao queueDao = (QueueDao) SpringUtils.getBean("queueDao");
                        List<Hashtable> queues=queueDao.getQueues();
                        for(Hashtable qht:queues){
                        %>                            
                                <%=(String)qht.get("queue")%>     <br>
                        <%}%>
                </td>
            </tr>
            <tr>
                <td> </td>
                <td><div id="addProviderList"></div></td>
            </tr>
         </table>
    </body>
    <script type="text/javascript">
        function clearInput(ele){
            var val=ele.value;
            if(val=='Type queue name' ){
                ele.value='';
                ele.setStyle({color:'black'});
            }
        }


                                     
        function saveQueue(element){
            var qn=$(element).getValue();
            qn=qn.replace(/^\s+/g,"");
            qn=qn.replace(/\s+$/g,"");//trim qn
            if(qn.length>0){                
                var data="newQueueName="+qn;
                var url="<c:out value='${ctx}'/>" + "/dms/inboxManage.do?method=addNewQueue";
                new Ajax.Request(url, {method: 'post',parameters:data,onSuccess:function(transport){
                        var json=transport.responseText.evalJSON();
                         if(json!=null){
                                 var success=json.addNewQueue;
                                 if(success!=null){
                                     if(success==true){
                                        $('addQueueSuccessMsg').innerHTML="Queue Name "+qn+" has been added.";
                                        $('newQueueName').value="";
                                     }else{
                                         $('addQueueSuccessMsg').innerHTML="Queue Name "+qn+" has NOT been added which is probably because it already exists.";
                                         $('newQueueName').value="";
                                     }
                                 }
                             }
                }});
            }else{
                $('addQueueSuccessMsg').innerHTML="Type a queue name";
                $('newQueueName').value="";
            }
        }
    </script>
</html>
