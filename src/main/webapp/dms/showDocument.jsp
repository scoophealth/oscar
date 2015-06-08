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

<%@ page import="oscar.dms.*,java.util.*" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/rewrite-tag.tld" prefix="rewrite"%>
<%@ taglib uri="/WEB-INF/oscar-tag.tld" prefix="oscar"%>
<%@ page import="oscar.OscarProperties"%>
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils,oscar.oscarLab.ca.all.*,oscar.oscarMDS.data.*,oscar.oscarLab.ca.all.util.*"%>
<%@page import="org.springframework.web.context.WebApplicationContext,org.oscarehr.common.dao.*,org.oscarehr.common.model.*"%><%

            WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
            ProviderInboxRoutingDao providerInboxRoutingDao = (ProviderInboxRoutingDao) ctx.getBean("providerInboxRoutingDAO");
            String demoName=request.getParameter("demoName");
            String documentNo = request.getParameter("segmentID");

            String providerNo = request.getParameter("providerNo");
            String searchProviderNo = request.getParameter("searchProviderNo");
            String status = request.getParameter("status");
            String inQueue=request.getParameter("inQueue");
            boolean inQueueB=false;
            if(inQueue!=null && inQueue.equals("true"))
                inQueueB=true;

            String creator = (String) session.getAttribute("user");
            ArrayList doctypes = EDocUtil.getActiveDocTypes("demographic");
            EDoc curdoc = EDocUtil.getDoc(documentNo);

            String demographicID = curdoc.getModuleId();

            String docId = curdoc.getDocId();

            int slash = 0;
            String contentType = "";
            if ((slash = curdoc.getContentType().indexOf('/')) != -1) {
                contentType = curdoc.getContentType().substring(slash + 1);
            }
            String dStatus = "";
            if ((curdoc.getStatus() + "").compareTo("A") == 0) {
                dStatus = "active";
            } else if ((curdoc.getStatus() + "").compareTo("H") == 0) {
                dStatus = "html";
            }
            int numOfPage=curdoc.getNumberOfPages();
            String numOfPageStr="";
            if(numOfPage==0)
                numOfPageStr="unknown";
            else
                numOfPageStr=(new Integer(numOfPage)).toString();
            String cp=request.getContextPath() ;
            String url = cp+"/dms/ManageDocument.do?method=viewDocPage&doc_no=" + docId+"&curPage=1";
            String url2 = cp+"/dms/ManageDocument.do?method=display&doc_no=" + docId;

%>
<% if (request.getParameter("inWindow") != null && request.getParameter("inWindow").equalsIgnoreCase("true")) {  %>
<script type="text/javascript" src="<%= request.getContextPath() %>/share/calendar/calendar.js"></script>
<!-- language for the calendar -->
<script type="text/javascript" src="<%= request.getContextPath() %>/share/calendar/lang/<bean:message key='global.javascript.calendar'/>"></script>
<!-- the following script defines the Calendar.setup helper function, which makes
       adding a calendar a matter of 1 or 2 lines of code. -->
<script type="text/javascript" src="<%= request.getContextPath() %>/share/calendar/calendar-setup.js"></script>
<!-- calendar stylesheet -->
<link rel="stylesheet" type="text/css" media="all" href="<%= request.getContextPath() %>/share/calendar/calendar.css" title="win2k-cold-1" />
		<!-- jquery -->
		<script type="text/javascript" src="<%= request.getContextPath() %>/share/javascript/jquery/jquery-1.4.2.js"></script>
        <script language="javascript" type="text/javascript" src="<%= request.getContextPath() %>/share/javascript/Oscar.js" ></script>
        <script type="text/javascript" src="<%= request.getContextPath() %>/share/javascript/prototype.js"></script>
        <script type="text/javascript" src="<%= request.getContextPath() %>/share/javascript/effects.js"></script>
        <script type="text/javascript" src="<%= request.getContextPath() %>/share/javascript/controls.js"></script>

        <script type="text/javascript" src="<%= request.getContextPath() %>/share/yui/js/yahoo-dom-event.js"></script>
        <script type="text/javascript" src="<%= request.getContextPath() %>/share/yui/js/connection-min.js"></script>
        <script type="text/javascript" src="<%= request.getContextPath() %>/share/yui/js/animation-min.js"></script>
        <script type="text/javascript" src="<%= request.getContextPath() %>/share/yui/js/datasource-min.js"></script>
        <script type="text/javascript" src="<%= request.getContextPath() %>/share/yui/js/autocomplete-min.js"></script>
        <script type="text/javascript" src="<%= request.getContextPath() %>/js/demographicProviderAutocomplete.js"></script>

        <script type="text/javascript" src="<%= request.getContextPath() %>/share/javascript/oscarMDSIndex.js"></script>

        <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/share/yui/css/fonts-min.css"/>
        <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/share/yui/css/autocomplete.css"/>
        <link rel="stylesheet" type="text/css" media="all" href="<%= request.getContextPath() %>/share/css/demographicProviderAutocomplete.css"  />

        <style type="text/css">
        	.multiPage {
        		background-color: RED;
        		color: WHITE;
        		font-weight:bold;
				padding: 0px 5px;
				font-size: medium;
        	}
        	.singlePage {

        	}
        </style>

        <script type="text/javascript">        

        function rotate90(id) {
        	jQuery("#rotate90btn_" + id).attr('disabled', 'disabled');

        	new Ajax.Request(contextpath + "/dms/SplitDocument.do", {method: 'post', parameters: "method=rotate90&document=" + id, onSuccess: function(data) {
        		jQuery("#rotate90btn_" + id).removeAttr('disabled');
        		jQuery("#docImg_" + id).attr('src', contextpath + "/dms/ManageDocument.do?method=showPage&doc_no=" + id + "&page=1&rand=" + (new Date().getTime()));

        	}});
        }

        function split(id) {
        	var loc = "<%= request.getContextPath()%>/oscarMDS/Split.jsp?document=" + id;
        	popupStart(1100, 1100, loc, "Splitter");
        }

        var _in_window = <%=( "true".equals(request.getParameter("inWindow")) ? "true" : "false" )%>;
        var contextpath = "<%=request.getContextPath()%>";
        </script>

<% } %>



         <script type="text/javascript">
       renderCalendar=function(id,inputFieldId){
           Calendar.setup({ inputField : inputFieldId, ifFormat : "%Y-%m-%d", showsTime :false, button : id });
  	   }

        var tmp;

        YAHOO.util.Event.onDOMReady(function() {
                                          if($("autocompletedemo<%=docId%>") && $("autocomplete_choices<%=docId%>")){
                                                 //oscarLog('in basic remote');
                                                //var oDS = new YAHOO.util.XHRDataSource("http://localhost:8080/drugref2/test4.jsp");
                                                var url = "<%= request.getContextPath() %>/demographic/SearchDemographic.do";
                                                var oDS = new YAHOO.util.XHRDataSource(url,{connMethodPost:true,connXhrMode:'ignoreStaleResponses'});
                                                oDS.responseType = YAHOO.util.XHRDataSource.TYPE_JSON;// Set the responseType
                                                // Define the schema of the delimited resultsTEST, PATIENT(1985-06-15)
                                                oDS.responseSchema = {
                                                    resultsList : "results",
                                                    fields : ["formattedName","fomattedDob","demographicNo","providerNo","providerName","nextAppointment", "cust1", "cust1Name", "cust2", "cust2Name", "cust4", "cust4Name"]
                                                };
                                                // Enable caching
                                                oDS.maxCacheEntries = 0;
                                                var oAC = new YAHOO.widget.AutoComplete("autocompletedemo<%=docId%>","autocomplete_choices<%=docId%>",oDS);
                                                oAC.queryMatchSubset = true;
                                                oAC.minQueryLength = 3;
                                                oAC.maxResultsDisplayed = 25;
                                                oAC.formatResult = resultFormatter2;
                                                //oAC.typeAhead = true;
                                                oAC.queryMatchContains = true;
                                                //oscarLog(oAC);
                                                //oscarLog(oAC.itemSelectEvent);
                                                oAC.itemSelectEvent.subscribe(function(type, args) {
													tmp = args;
                                                    //oscarLog(args);
                                                    //oscarLog(args[0].getInputEl().id);
                                                    var str = args[0].getInputEl().id.replace("autocompletedemo","demofind");
                                                   //oscarLog(str);
                                                   $(str).value = args[2][2];//li.id;

                                                   args[0].getInputEl().value = args[2][0] + "("+args[2][1]+")";
                                                   selectedDemos.push(args[0].getInputEl().value);
                                               	   	if (args[2][3] !== undefined) {
                                                   		addDocToList(args[2][3], args[2][4] + " (MRP)", "<%=docId%>");
                                               	   	}
                                                   //enable Save button whenever a selection is made
                                                   $('save<%=docId%>').enable();
                                                   $('saveNext<%=docId%>').enable();

                                                   $('nextAppointment_<%=docId%>').innerHTML = args[2][5];

                                                });


                                                return {
                                                    oDS: oDS,
                                                    oAC: oAC
                                                };
                                            }
                                            });

        YAHOO.util.Event.onDOMReady(function() {
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
            var oAC = new YAHOO.widget.AutoComplete("autocompleteprov<%=docId%>", "autocomplete_choicesprov<%=docId%>", oDS);
            oAC.queryMatchSubset = true;
            oAC.minQueryLength = 3;
            oAC.maxResultsDisplayed = 25;
            oAC.formatResult = resultFormatter3;
            //oAC.typeAhead = true;
            oAC.queryMatchContains = true;
            //oscarLog(oAC);
            //oscarLog(oAC.itemSelectEvent);
            oAC.itemSelectEvent.subscribe(function(type, args) {
                //oscarLog(args);
               tmp = args;
               var myAC = args[0];
               var str = myAC.getInputEl().id.replace("autocompleteprov","provfind");
               //oscarLog(str);
               //oscarLog(args[2]);
               var oData=args[2];
               $(str).value = args[2][0];//li.id;
               //oscarLog("str value="+$(str).value);
               //oscarLog(args[2][1]+"--"+args[2][0]);
               myAC.getInputEl().value = args[2][2] + ","+args[2][1];
               //oscarLog("--"+args[0].getInputEl().value);
               //selectedDemos.push(args[0].getInputEl().value);

               //enable Save button whenever a selection is made
                addDocToList(oData[0], oData[2] + " " +oData[1], "<%=docId%>");
                $('save<%=docId%>').enable();
                $('saveNext<%=docId%>').enable();

                myAC.getInputEl().value = '';//;oData.fname + " " + oData.lname ;

            });


            return {
                oDS: oDS,
                oAC: oAC
            };
        });

</script>

        <div id="labdoc_<%=docId%>">
            <table class="docTable">
                <tr>


                    <td colspan="8">
                        <div style="text-align: right;font-weight: bold">
                        	<a id="firstP_<%=docId%>" href="javascript:void(0);" onclick="firstPage('<%=docId%>','<%=cp%>');">First</a>
                            <a id="prevP_<%=docId%>" href="javascript:void(0);" onclick="prevPage('<%=docId%>','<%=cp%>');">Prev</a>
                            <a id="nextP_<%=docId%>" href="javascript:void(0);" onclick="nextPage('<%=docId%>','<%=cp%>');">Next</a>
                            <a id="lastP_<%=docId%>" href="javascript:void(0);" onclick="lastPage('<%=docId%>','<%=cp%>');">Last</a>
                        </div>
                        <a href="<%=url2%>" target="_blank"><img alt="document" id="docImg_<%=docId%>"  src="<%=url%>" /></a></td>


                    <td align="left" valign="top">
                        <fieldset><legend><bean:message key="inboxmanager.document.PatientMsg"/><span id="assignedPId_<%=docId%>"><%=demoName%></span> </legend>
                            <table border="0">
                                <tr>
                                    <td><bean:message key="inboxmanager.document.DocumentUploaded"/></td>
                                    <td><%=curdoc.getDateTimeStamp()%></td>
                                </tr>
                                <tr>
                                    <td><bean:message key="inboxmanager.document.ContentType"/></td>
                                    <td><%=contentType%></td>
                                </tr>
                                <tr>
                                    <td><bean:message key="inboxmanager.document.NumberOfPages"/></td>
                                    <td>
                                    	<input id="shownPage_<%=docId %>" type="hidden" value="1" />
                                    	<span id="viewedPage_<%=docId%>" class="<%= numOfPage > 1 ? "multiPage" : "singlePage" %>">1</span>&nbsp; of &nbsp;<span id="numPages_<%=docId %>" class="<%= numOfPage > 1 ? "multiPage" : "singlePage" %>"><%=numOfPageStr%></span>
                                    </td>
                                </tr>

                                <tr><td></td>
                                	<td><input onclick="split('<%=docId%>')" type="button" value="<bean:message key="inboxmanager.document.split" />" />
                                    	<input id="rotate180btn_<%=docId %>" onclick="rotate180('<%=docId %>')" type="button" value="<bean:message key="inboxmanager.document.rotate180" />" />
                                    	<input id="rotate90btn_<%=docId %>" onclick="rotate90('<%=docId %>')" type="button" value="<bean:message key="inboxmanager.document.rotate90" />" />
                                    	<% if (numOfPage > 1) { %><input id="removeFirstPagebtn_<%=docId %>" onclick="removeFirstPage('<%=docId %>')" type="button" value="<bean:message key="inboxmanager.document.removeFirstPage" />" /><% } %>
                                    </td>
                                </tr>

                            </table>

                            <form id="forms_<%=docId%>" onsubmit="return updateDocument('forms_<%=docId%>');">
                                <input type="hidden" name="method" value="documentUpdateAjax" />
                                <input type="hidden" name="documentId" value="<%=docId%>" />
                                <input type="hidden" name="curPage_<%=docId%>" id="curPage_<%=docId%>" value="1"/>
                                <input type="hidden" name="totalPage_<%=docId%>" id="totalPage_<%=docId%>" value="<%=numOfPage%>"/>
                                <table border="0">
                                    <tr>
                                        <td><bean:message key="dms.documentReport.msgDocType" />:</td>
                                        <td>
                                            <select name ="docType" id="docType_<%=docId%>">
                                                <option value=""><bean:message key="dms.addDocument.formSelect" /></option>
                                                <%for (int j = 0; j < doctypes.size(); j++) {
                String doctype = (String) doctypes.get(j);%>
                                                <option value="<%= doctype%>" <%=(curdoc.getType().equals(doctype)) ? " selected" : ""%>><%= doctype%></option>
                                                <%}%>
                                            </select>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td><bean:message key="dms.documentReport.msgDocDesc" />:</td>
                                        <td><input id="docDesc_<%=docId%>"  type="text" name="documentDescription" value="<%=curdoc.getDescription()%>" /></td>
                                    </tr>
                                    <tr>
                                        <td><bean:message key="inboxmanager.document.ObservationDateMsg" /></td>
                                        <td>
                                            <input   id="observationDate<%=docId%>" name="observationDate" type="text" value="<%=curdoc.getObservationDate()%>">
                                            <a id="obsdate<%=docId%>" onmouseover="renderCalendar(this.id,'observationDate<%=docId%>' );" href="javascript:void(0);" ><img title="Calendar" src="<%=request.getContextPath()%>/images/cal.gif" alt="Calendar"border="0" /></a>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td><bean:message key="inboxmanager.document.DemographicMsg" /></td>
                                        <td><%
                                        if(!demographicID.equals("-1")){%>
                                            <input id="saved<%=docId%>" type="hidden" name="saved" value="true"/>
                                            <input type="hidden" value="<%=demographicID%>" name="demog" id="demofind<%=docId%>" />
                                            <%=demoName%><%}else{%>
                                            <input id="saved<%=docId%>" type="hidden" name="saved" value="false"/>
                                            <input type="hidden" name="demog" value="<%=demographicID%>" id="demofind<%=docId%>" />
                                            <input type="text" id="autocompletedemo<%=docId%>" onchange="checkSave('<%=docId%>');" name="demographicKeyword" />
                                            <div id="autocomplete_choices<%=docId%>"class="autocomplete"></div>
                                            <%}%>
											<input type="button" id="createNewDemo" value="Create New Demographic"  onclick="popup(700,960,'<%= request.getContextPath() %>/demographic/demographiccontrol.jsp?displaymode=Create')"/>
											

                                                   <input id="saved_<%=docId%>" type="hidden" name="saved" value="false"/>
                                                   <br><input id="mrp_<%=docId%>" style="display: none;" type="checkbox" onclick="sendMRP(this)"  name="demoLink" >
                                                   <a id="mrp_fail_<%=docId%>" style="color:red;font-style: italic;display: none;" ><bean:message key="inboxmanager.document.SendToMRPFailedMsg" /></a>
                                        </td>
                                    </tr>

                                    <tr>
                                        <td valign="top"><bean:message key="inboxmanager.document.FlagProviderMsg" /> </td>
                                        <td>
                                            <input type="hidden" name="provi" id="provfind<%=docId%>" />
                                            <input type="text" id="autocompleteprov<%=docId%>" name="demographicKeyword"/>
                                            <div id="autocomplete_choicesprov<%=docId%>" class="autocomplete"></div>


                                            <div id="providerList<%=docId%>"></div>
                                        </td>
                                    </tr>



                                    <tr>
                                        <td><bean:message key="dms.documentReport.msgCreator"/>:</td>
                                        <td><%=curdoc.getCreatorName()%></td>
                                    </tr>

                                    <tr>
                                        <td width="30%" colspan="1" align="left"><a id="saveSucessMsg_<%=docId%>" style="display:none;color:blue;"><bean:message key="inboxmanager.document.SuccessfullySavedMsg"/></a></td>
                                        <td width="30%" colspan="1" align="left"><%if(demographicID.equals("-1")){%><input type="submit" name="save" disabled id="save<%=docId%>" value="Save" /><input type="button" name="save" id="saveNext<%=docId%>" onclick="saveNext(<%=docId%>)" disabled value='<bean:message key="inboxmanager.document.SaveAndNext"/>' /><%}
            else{%><input type="submit" name="save" id="save<%=docId%>" value="Save" /><input type="button" name="save" onclick="saveNext(<%=docId%>)" id="saveNext<%=docId%>" value='<bean:message key="inboxmanager.document.SaveAndNext"/>' /> <%}%>

                                    </tr>

                                    <tr>
                                        <td colspan="2">
                                            <bean:message key="inboxmanager.document.LinkedProvidersMsg"/>
                                            <%
            Properties p = (Properties) session.getAttribute("providerBean");
            List<ProviderInboxItem> routeList = providerInboxRoutingDao.getProvidersWithRoutingForDocument("DOC", docId);
                                            %>
                                            <ul>
                                                <%for (ProviderInboxItem pItem : routeList) {
                                                    String s=p.getProperty(pItem.getProviderNo(), pItem.getProviderNo());

                                                    if(!s.equals("0")&&!s.equals("null")&& !pItem.getStatus().equals("X")){  %>
                                                        <li><%=s%><a href="#" onclick="removeLink('DOC', '<%=docId %>', '<%=pItem.getProviderNo() %>', this);return false;"><bean:message key="inboxmanager.document.RemoveLinkedProviderMsg" /></a></li>
                                                <%}
                                                }%>
                                            </ul>
                                        </td>
                                    </tr>
                                </table>

                            </form>
                        </fieldset>


                            <%
                            ArrayList ackList = AcknowledgementData.getAcknowledgements("DOC",docId);

                                            if (ackList.size() > 0){%>
                                            <fieldset>
                                                <table width="100%" height="20" cellpadding="2" cellspacing="2">
                                                    <tr>
                                                            <td align="center" bgcolor="white">
                                                            <div class="FieldData">
                                                                <!--center-->
                                                                    <% for (int i=0; i < ackList.size(); i++) {
                                                                        ReportStatus report = (ReportStatus) ackList.get(i); %>
                                                                        <%= report.getProviderName() %> :

                                                                        <% String ackStatus = report.getStatus();
                                                                            if(ackStatus.equals("A")){
                                                                                ackStatus = "Acknowledged";
                                                                            }else if(ackStatus.equals("F")){
                                                                                ackStatus = "Filed but not Acknowledged";
                                                                            }else{
                                                                                ackStatus = "Not Acknowledged";
                                                                            }
                                                                        %>
                                                                        <font color="red"><%= ackStatus %></font>
                                                                        <% if ( ackStatus.equals("Acknowledged") ) { %>
                                                                            <%= report.getTimestamp() %>,
                                                                            <%= ( report.getComment().equals("") ? "no comment" : "comment : "+report.getComment() ) %>
                                                                        <% } %>
                                                                        <br>
                                                                    <% }
                                                                    if (ackList.size() == 0){
                                                                        %><font color="red">N/A</font><%
                                                                    }
                                                                    %>
                                                                <!--/center-->
                                                            </div>
                                                        </td>
                                                    </tr>
                                                </table>
                                            </fieldset>
                                            <%}

%>

                        <fieldset>
                          <%--  <input id="test1Regex_<%=docId%>" type="text"/><input id="test2Regex_<%=docId%>" type="text"/>
                            <a href="javascript:void(0);" onclick="testShowDoc();">click</a>--%>
                            <legend><span class="FieldData"><i><bean:message key="inboxmanager.document.NextAppointmentMsg"/> <oscar:nextAppt demographicNo="<%=demographicID%>"/></i></span></legend>
                            <form name="reassignForm_<%=docId%>" >
                                <input type="hidden" name="flaggedLabs" value="<%= docId%>" />
                                <input type="hidden" name="selectedProviders" value="" />
                                <input type="hidden" name="labType" value="DOC" />
                                <input type="hidden" name="labType<%= docId%>DOC" value="imNotNull" />
                                <input type="hidden" name="providerNo" value="<%= providerNo%>" />
                                <input type="hidden" name="ajax" value="yes" />
                            </form>
                         </fieldset>
                         <fieldset>
                         	<legend><bean:message key="inboxmanager.document.Comment"/></legend>
                                <form name="acknowledgeForm_<%=docId%>" id="acknowledgeForm_<%=docId%>" onsubmit="updateStatus('acknowledgeForm_<%=docId%>',<%=inQueueB%>);" method="post" action="javascript:void(0);">

                                <table width="100%" height="100%" border="0" cellspacing="0" cellpadding="0">
                                    <tr>
                                        <td valign="top">
                                            <table width="100%" border="0" cellspacing="0" cellpadding="3">
                                                <tr>
                                                    <td align="left" class="" width="100%">
                                                        <input type="hidden" name="segmentID" value="<%= docId%>"/>
                                                        <input type="hidden" name="multiID" value="<%= docId%>" />
                                                        <input type="hidden" name="providerNo" value="<%= providerNo%>"/>
                                                        <input type="hidden" name="status" value="A"/>
                                                        <input type="hidden" name="labType" value="DOC"/>
                                                        <input type="hidden" name="ajaxcall" value="yes"/>
                                                        <textarea id="comment_<%=docId%>" name="comment" cols="40" rows="4"></textarea>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td>
                                                        <input type="submit" id="ackBtn_<%=docId%>" value="<bean:message key="oscarMDS.segmentDisplay.btnAcknowledge"/>" >
                                                        <input type="button" id="fwdBtn_<%=docId%>"  value="<bean:message key="oscarMDS.index.btnForward"/>" onClick="popupStart(300, 400, '../oscarMDS/SelectProviderAltView.jsp?doc_no=<%=documentNo%>&providerNo=<%=providerNo%>&searchProviderNo=<%=searchProviderNo%>&status=<%=status%>', 'providerselect')">
                                                        <input type="button" id="fileBtn_<%=docId%>"  value="<bean:message key="oscarMDS.index.btnFile"/>" onclick="fileDoc('<%=docId%>');">
                                                        <input type="button" id="closeBtn_<%=docId%>" value=" <bean:message key="global.btnClose"/> " onClick="window.close()">
                                                        <input type="button" id="printBtn_<%=docId%>" value=" <bean:message key="global.btnPrint"/> " onClick="popup(700,960,'<%=url2%>','file download')">
                                                        <% if (demographicID != null && !demographicID.equals("") && !demographicID.equalsIgnoreCase("null")) {%>
                                                        <input type="button" id="msgBtn_<%=docId%>" value="Msg" onclick="popup(700,960,'<%= request.getContextPath() %>/oscarMessenger/SendDemoMessage.do?demographic_no=<%=demographicID%>','msg')"/>
                                                        <input type="button" id="ticklerBtn_<%=docId%>" value="Tickler" onclick="popupStart(450,600,'<%= request.getContextPath() %>/tickler/ForwardDemographicTickler.do?docType=DOC&docId=<%= docId %>&demographic_no=<%=demographicID%>','tickler')"/>
                                                        <input type="button" value=" <bean:message key="oscarMDS.segmentDisplay.btnEChart"/> " onClick="popupStart(360, 680, '<%= request.getContextPath() %>/oscarMDS/SearchPatient.do?labType=DOC&segmentID=<%= docId %>&name=<%=java.net.URLEncoder.encode(demoName)%>', 'searchPatientWindow')">
                                                        <% }

                                                        %>
                                                    </td>
                                                </tr>
                                            </table>
                                        </td>
                                    </tr>
                                </table>
                            </form>
                        </fieldset>
                    </td>
                </tr>
                <tr><td colspan="9" ><hr width="100%" color="red"></td></tr>
            </table>
        </div>
