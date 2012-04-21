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

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="java.util.*, oscar.util.*, oscar.OscarProperties, oscar.dms.*, oscar.dms.data.*"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
                <title>Documents In Queues</title>
<script type="text/javascript" src="<%= request.getContextPath() %>/share/javascript/oscarMDSIndex.js"></script>
<script type="text/javascript" src="<%= request.getContextPath() %>/share/javascript/prototype.js"></script>
   <!-- main calendar program -->
<script type="text/javascript" src="../share/calendar/calendar.js"></script>
<!-- language for the calendar -->
<script type="text/javascript" src="../share/calendar/lang/<bean:message key='global.javascript.calendar'/>"></script>
<!-- the following script defines the Calendar.setup helper function, which makes
       adding a calendar a matter of 1 or 2 lines of code. -->
<script type="text/javascript" src="../share/calendar/calendar-setup.js"></script>
<!-- calendar style sheet -->
<link rel="stylesheet" type="text/css" media="all" href="../share/calendar/calendar.css" title="win2k-cold-1" />
<link rel="stylesheet" type="text/css" href="../share/css/OscarStandardLayout.css">
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<script type="text/javascript" src="<%= request.getContextPath() %>/share/javascript/prototype.js"></script>
<script type="text/javascript" src="<%= request.getContextPath() %>/share/javascript/effects.js"></script>
<script type="text/javascript" src="<%= request.getContextPath() %>/share/javascript/Oscar.js"></script>

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
        <link rel="stylesheet" type="text/css" media="all" href="../share/css/oscarMDSIndex.css"  />
          <style type="text/css">
            .Cell{
                background-color:#9999CC;
border-color:#CCCCFF #6666CC #6666CC #CCCCFF;
border-left:thin solid #CCCCFF;
border-right:thin solid #6666CC;
border-style:solid;
border-width:thin;
height:0px;
            }
            </style>
    </head>

    <body>
        <%
        HashMap queueIdNames=(HashMap)request.getAttribute("queueIdNames");//each queue id has a corresponding name
        HashMap queueDocNos=(HashMap)request.getAttribute("queueDocNos");//one queue id is linked to a list of docs
        String providerNo=(String)request.getAttribute("providerNo");
        String searchProviderNo=(String)request.getAttribute("searchProviderNo");
        Set keys=queueDocNos.keySet();
        Iterator itr=keys.iterator();
String patientIdNamesStr=(String)request.getAttribute("patientIdNamesStr");
HashMap docStatus=(HashMap)request.getAttribute("docStatus");
String patientIdStr =(String)request.getAttribute("patientIdStr");
HashMap typeDocLab =(HashMap)request.getAttribute("typeDocLab");
HashMap docType=(HashMap)request.getAttribute("docType");
HashMap patientDocs=(HashMap)request.getAttribute("patientDocs");
List<String> normals=(List<String>)request.getAttribute("normals");
List<String> abnormals=(List<String>)request.getAttribute("abnormals");

%>
        
        <table id="pendingDocs" width="100%">
             <tr oldclass="MainTableTopRow">
                <td class="MainTableTopRowRightColumn" colspan="2" align="left">
                 <table width="100%">
                     <tr >
                         <td align="center" colspan="2" class="Nav" valign="center"><span class="white"><bean:message key="inboxmanager.documentsInQueues"/></span></td>
                     </tr>
                        <tr>
                            <td align="left" valign="center" >
                                <input type="hidden" name="providerNo" value="<%= providerNo %>">
                                <input type="hidden" name="searchProviderNo" value="<%= searchProviderNo %>">
                                <%= (request.getParameter("lname") == null ? "" : "<input type=\"hidden\" name=\"lname\" value=\""+request.getParameter("lname")+"\">") %>
                                <%= (request.getParameter("fname") == null ? "" : "<input type=\"hidden\" name=\"fname\" value=\""+request.getParameter("fname")+"\">") %>
                                <%= (request.getParameter("hnum") == null ? "" : "<input type=\"hidden\" name=\"hnum\" value=\""+request.getParameter("hnum")+"\">") %>

                                <input type="hidden" name="selectedProviders">

                                <input type="button" class="smallButton" onclick="window.close();" value="<bean:message key="oscarMDS.index.btnClose"/>"

                            </td>
                            
                            <td align="right" valign="center" width="30%">
                                <oscar:help keywords="inbox queue" key="app.top1"/>
                                | <a href="javascript:popupStart(300,400,'../oscarEncounter/About.jsp')" style="color: #FFFFFF;" ><bean:message key="global.about"/></a>
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
            <tr>
                <th class="Cell" align="left" valign="bottom"  nowrap>Queues</th>
                <th class="Cell" align="left" valign="bottom"  nowrap>Documents</th>
            </tr>
            <tr >
                <td valign="top"  id="queueNames" width="20%">
                    <%
                    while(itr.hasNext()){
                        Integer qId=(Integer)itr.next();
                        String name=(String)queueIdNames.get(qId);
                        List dos=(List)queueDocNos.get(qId);
                        Integer numberOfDocs=dos.size();
%>
<a href="javascript:void(0);" onclick="resetCurrentFirstDocLab();showDocInQueue('<%=qId%>')"><%=name%>&nbsp;(<span id="docNo_<%=qId%>"><%=numberOfDocs%></span>)</a><br/>
                    <%}%>

                </td>

                <td valign="top" id="docs" width="80%"></td>
            </tr>
        </table>
                    <script type="text/javascript">
                            var current_first_doclab=0;
                           var typeDocLab=initTypeDocLab('<%=typeDocLab%>');   //{DOC=[357, 317, 316], HL7=[38, 33, 30, 28]}
                           var docType=initDocType('<%=docType%>');   //{357=DOC, 38=HL7, 317=DOC, 316=DOC, 33=HL7, 30=HL7, 28=HL7}
                           var patientDocs=initPatientDocs('<%=patientDocs%>');//{2=[316, 30, 28], 1=[33], -1=[357, 317, 38]}
                           var patientIdNames=initPatientIdNames('<%=patientIdNamesStr%>');//;2=TEST2, PATIENT2;1=Zrrr, Srrr;-1=Not, Assigned
                           var docStatus=initDocStatus('<%=docStatus%>');//{357=A, 38=N, 317=A, 316=A, 33=N, 30=N, 28=N}
                           var normals=initNormals('<%=normals%>');//[357, 317, 316, 38, 33, 30, 28]
                           var abnormals=initAbnormals('<%=abnormals%>');//[123,567]
                           var patientIds=initPatientIds('<%=patientIdStr%>');
                        var queueDocNos=initHashtableWithList('<%=queueDocNos%>');
                        var providerNo='<%=providerNo%>';
                        var searchProviderNo='<%=searchProviderNo%>';
 /*console.log(typeDocLab);
 console.log(docType);
 console.log(patientDocs);
 console.log(patientIdNames);
 console.log(docStatus);
 console.log(normals);
 console.log(abnormals);
 console.log(patientIds);
 console.log(queueDocNos);
 console.log(providerNo);
 console.log(searchProviderNo);*/
                           var types=['DOC'];

                        var contextpath='<%=request.getContextPath()%>';
Event.observe(window,'scroll',function(){//check for scrolling
    bufferAndShow();
});




</script>
    </body>
</html>
