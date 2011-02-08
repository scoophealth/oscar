<!--
/*
*
* Copyright (c) 2001-2002. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved. *
* This software is published under the GPL GNU General Public License.
* This program is free software; you can redistribute it and/or
* modify it under the terms of the GNU General Public License
* as published by the Free Software Foundation; either version 2
* of the License, or (at your option) any later version. *
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU General Public License for more details. * * You should have received a copy of the GNU General Public License
* along with this program; if not, write to the Free Software
* Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. *
*
* <OSCAR TEAM>
*
* This software was written for
* Centre for Research on Inner City Health, St. Michael's Hospital,
* Toronto, Ontario, Canada
*/
-->
<%-- 
    Document   : synchroniseWithPHR
    Created on : Jan 12, 2011, 5:46:17 PM
    Author     : jackson
--%>
<%@ taglib uri="/WEB-INF/oscar-tag.tld" prefix="oscar" %>
<%@ taglib uri="/WEB-INF/indivo-tag.tld" prefix="indivo" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<html>
    <head>
<%
            String demoNo=request.getParameter("demoNo");
            String providerNo=request.getParameter("providerNo");


%>
        <script type="text/javascript" src="<c:out value="${ctx}/phr/phr.js"/>"></script>
        <script type="text/javascript" src="<c:out value="${ctx}/share/javascript/prototype.js"/>"></script>
        <script type="text/javascript" src="<c:out value="${ctx}/share/javascript/scriptaculous.js"/>"></script>
        <script type="text/javascript">
            function updateValues(){

               var url="<c:out value="${ctx}"/>" + "/oscarRx/WriteScript.do?parameterValue=getDemoNameAndHIN";
               var data = "demoNo=<%=demoNo%>";
               new Ajax.Request(url, {method: 'post',parameters:data,onSuccess:function(transport){
                            var json=transport.responseText.evalJSON();
                            $('patientName').innerHTML=json.patientName;
                            $('patientHIN').innerHTML=json.patientHIN;
                        }});

            }
           var Dom = {
                 get: function(e) {
                     if (typeof e === 'string') {
                        return document.getElementById(e);
                     } else {
                        return e;
                     }
                 },
                 add: function(e, d) {
                     var e = this.get(e);
                     var d = this.get(d);
                     d.appendChild(e);
                     e.focus();

                 },
                 remove: function(e) {
                     var e = this.get(e);
                     e.parentNode.removeChild(e);
                     $('contentdisplay').setAttribute('style','width:100%;');
                 },
                 expand: function(e){
                     document.getElementById('contentdisplay').innerHTML='';
                     var e=this.get(e);
                     console.log("e="+e);
                     document.getElementById('contentdisplay').appendChild(e);
                     e.setAttribute('height','100%');
                     $('contentdisplay').setAttribute('style','width:100%;height:100%');
                 }

            };
            var Event = {
                 add: function() {
                     if (window.addEventListener) {
                         return function(el, type, fn) {
                            Dom.get(el).addEventListener(type, fn, false);
                         };
                     } else if (window.attachEvent) {
                         return function(el, type, fn) {
                             var f = function() {
                                fn.call(Dom.get(el), window.event);
                             };
                                Dom.get(el).attachEvent('on' + type, f);
                         };
                     }
                 }()
            };

            ///////////////////////////////////////////////////////////////////////////////////////////
            Event.add(window, 'load', function() {

                 //send meds
                 Event.add('sendMedToPhr', 'click', function() {
                     var existing=document.getElementById('sendPhr_display');
                     if(existing){}
                     else{
                     var el = document.createElement('div');
                     el.setAttribute('id', 'sendPhr_display');
                     el.setAttribute('height','300');
                     el.innerHTML = "<a id='sendPhr_close' href='javascript:void(0);'>Hide</a>\n\
<a id='sendPhr_expand' href='javascript:void(0);'>Expand</a> <iframe src='../oscarRx/SendToPhr.do?demoId=<%=demoNo%>' width='100%' height='100%'>\n\
     <p>Your browser doesn't support iframe.</p></iframe>"
                     Dom.add(el, 'contentdisplay');
                     var close=document.getElementById('sendPhr_close');
                     Event.add(close, 'click', function(e) {
                        Dom.remove(el);
                     });
                     var expand=document.getElementById('sendPhr_expand');
                     Event.add(expand,'click',function(){
                        Dom.expand(el);
                     })
                     }
                 });
                 //getMedFromPhr
                 Event.add('getMedFromPhr', 'click', function() {
                     var existing=document.getElementById('fromPhr_display');
                     if(existing){}
                     else{
                     var el = document.createElement('div');
                     el.setAttribute('id', 'fromPhr_display');
                     el.setAttribute('height','300');
                     el.innerHTML = "<a id='fromPhr_close' href='javascript:void(0);'>Hide</a>\n\
<a id='fromPhr_expand' href='javascript:void(0);'>Expand</a> <iframe src='../phrExchange.do?method=displayNewMedsFromPhr&demoId=<%=demoNo%>' width='100%' height='100%'>\n\
     <p>Your browser doesn't support iframe.</p></iframe>"
                     Dom.add(el, 'contentdisplay');
                     var close=document.getElementById('fromPhr_close');
                     Event.add(close, 'click', function(e) {
                        Dom.remove(el);
                     });
                             var expand=document.getElementById('fromPhr_expand');
                             Event.add(expand,'click',function(){
                                Dom.expand(el);
                             })
                     }
                 });
                 //showPrevPhr
                 Event.add('showPrevPhr', 'click', function() {
                     var existing=document.getElementById('viewPrev_display');
                     if(existing){}
                     else{
                             var el = document.createElement('div');
                             el.setAttribute('id', 'viewPrev_display');
                             el.setAttribute('height','300');
                             el.innerHTML = "<a id='viewPrev_close' href='javascript:void(0);'>Hide</a>\n\
        <a id='viewPrev_expand' href='javascript:void(0);'>Expand</a> <iframe src='../phrExchange.do?method=displayPrevViewedMeds&demoId=<%=demoNo%>' width='100%' height='100%'>\n\
             <p>Your browser doesn't support iframe.</p></iframe>"
                             Dom.add(el, 'contentdisplay');
                             var close=document.getElementById('viewPrev_close');
                             Event.add(close, 'click', function(e) {
                                Dom.remove(el);
                             });
                             var expand=document.getElementById('viewPrev_expand');
                             Event.add(expand,'click',function(){
                                Dom.expand(el);
                             })
                     }
                 });

            });

            function clearPHRMeds(){

                                   var url="<c:out value="${ctx}"/>" + "/oscarRx/deleteRx.do?parameterValue=clearPHRMeds";
               var data = "";
               new Ajax.Request(url, {method: 'post',parameters:data,onSuccess:function(transport){
                            //updateCurrentInteractions();
            }});

            }
        </script>

    </head>
    <body onunload="clearPHRMeds();" onload="updateValues()" >
        <h1 align="middle" ><a id="patientName"></a>'s Personal Health Record</h1>
        <h2 align="middle">OHIP:<a id="patientHIN"></a></h2>
        <oscar:oscarPropertiesCheck property="MY_OSCAR" value="yes">
                <indivo:indivoRegistered demographic="<%=demoNo%>" provider="<%=providerNo%>">
                    <table width="100%" style="height:100%" >
                        <tr>
                            <td width="100px" valign="top" >
                                <a id="sendMedToPhr" href="javascript: void(0);">Send To PHR</a><br><br>
                                <a id="getMedFromPhr" href="javascript: void(0);" >Retrieve Med From PHR</a><br><br>
                                <a id="showPrevPhr" href="javascript: void(0);" >Show previously viewed drugs</a>
                            </td>
                            <td >
                                <div id="contentdisplay" style="width:100%"></div>
                            </td>
                        </tr>
                    </table>


                </indivo:indivoRegistered>
	</oscar:oscarPropertiesCheck>
    </body>
    
</html>
