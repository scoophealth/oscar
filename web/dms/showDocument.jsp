<%@ page import="oscar.dms.*,java.util.*" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/rewrite-tag.tld" prefix="rewrite"%>
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
            ArrayList doctypes = EDocUtil.getDoctypes("demographic");
            EDocUtil edocUtil = new EDocUtil();
            EDoc curdoc = edocUtil.getDoc(documentNo);

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
         <script type="text/javascript">

        renderCalendar=function(id,inputFieldId){
                Calendar.setup({ inputField : inputFieldId, ifFormat : "%Y-%m-%d", showsTime :false, button : id });
        }
                    
                                        YAHOO.example.BasicRemote = function() {
                                          if($("autocompletedemo<%=docId%>") && $("autocomplete_choices<%=docId%>")){
                                                 //oscarLog('in basic remote');
                                                //var oDS = new YAHOO.util.XHRDataSource("http://localhost:8080/drugref2/test4.jsp");
                                                var url = "../demographic/SearchDemographic.do";
                                                var oDS = new YAHOO.util.XHRDataSource(url,{connMethodPost:true,connXhrMode:'ignoreStaleResponses'});
                                                oDS.responseType = YAHOO.util.XHRDataSource.TYPE_JSON;// Set the responseType
                                                // Define the schema of the delimited resultsTEST, PATIENT(1985-06-15)
                                                oDS.responseSchema = {
                                                    resultsList : "results",
                                                    fields : ["formattedName","fomattedDob","demographicNo"]
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
                                                    //oscarLog(args);
                                                    //oscarLog(args[0].getInputEl().id);
                                                    var str = args[0].getInputEl().id.replace("autocompletedemo","demofind");
                                                   //oscarLog(str);
                                                   $(str).value = args[2][2];//li.id;
                                                   args[0].getInputEl().value = args[2][0] + "("+args[2][1]+")";
                                                   selectedDemos.push(args[0].getInputEl().value);
                                                   //enable Save button whenever a selection is made
                                                   $('save<%=docId%>').enable();
                                                   $('saveNext<%=docId%>').enable();

                                                });


                                                return {
                                                    oDS: oDS,
                                                    oAC: oAC
                                                };
                                            }
                                            }();
                                         



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
                                                            var bdoc = document.createElement('a');
                                                            bdoc.setAttribute("onclick", "removeProv(this);");
                                                            bdoc.appendChild(document.createTextNode(" -remove- "));
                                                            //oscarLog("--");
                                                            var adoc = document.createElement('div');
                                                            adoc.appendChild(document.createTextNode(oData[2] + " " +oData[1]));
                                                            //oscarLog("--==");
                                                            var idoc = document.createElement('input');
                                                            idoc.setAttribute("type", "hidden");
                                                            idoc.setAttribute("name","flagproviders");
                                                            idoc.setAttribute("value",oData[0]);
                                                            //console.log(oData[0]);
                                                            //console.log(myAC);
                                                         //   console.log(elLI);
                                                         //   console.log(oData);
                                                         //   console.log(aArgs);
                                                         //   console.log(sType);
                                                            adoc.appendChild(idoc);

                                                            adoc.appendChild(bdoc);
                                                            var providerList = $('providerList<%=docId%>');
                                                        //    console.log('Now HERE'+providerList);
                                                            providerList.appendChild(adoc);

                                                            myAC.getInputEl().value = '';//;oData.fname + " " + oData.lname ;

                                                        });


                                                        return {
                                                            oDS: oDS,
                                                            oAC: oAC
                                                        };
                                                    }();


                                            </script>

        <div id="labdoc_<%=docId%>">
            <table class="docTable">
                <tr>


                    <td colspan="8">
                        <div style="text-align: right;font-weight: bold"> 
                            <a id="firstP_<%=docId%>" href="javascript:void(0);" onclick="firstPage('<%=docId%>','<%=cp%>');"><<</a>
                            <a id="prevP_<%=docId%>" href="javascript:void(0);" onclick="prevPage('<%=docId%>','<%=cp%>');"><</a>
                            <a id="nextP_<%=docId%>" href="javascript:void(0);" onclick="nextPage('<%=docId%>','<%=cp%>');">></a>
                            <a id="lastP_<%=docId%>" href="javascript:void(0);" onclick="lastPage('<%=docId%>','<%=cp%>');">>></a>
                        </div>
                        <a href="<%=url2%>"><img alt="document" id="docImg_<%=docId%>"  src="<%=url%>" /></a></td>


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
                                    <td><%=numOfPageStr%></td>
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
                                            <input type="text" id="autocompletedemo<%=docId%>" onchange="checkSave('<%=docId%>')" name="demographicKeyword" />
                                            <div id="autocomplete_choices<%=docId%>"class="autocomplete"></div>
                                            <%}%>


                                                   <input id="saved_<%=docId%>" type="hidden" name="saved" value="false"/>
                                                   <br><input id="mrp_<%=docId%>" type="checkbox" onclick="sendMRP(this)"  name="demoLink" ><bean:message key="inboxmanager.document.SendToMRPMsg" />
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
                                        <td width="30%" colspan="1" align="right"><a id="saveSucessMsg_<%=docId%>" style="display:none;color:blue;"><bean:message key="inboxmanager.document.SuccessfullySavedMsg"/></a></td>
                                        <td width="30%" colspan="1" align="right"><%if(!demographicID.equals("-1")){%><input type="submit" name="save" id="save<%=docId%>" value="Save" /><input type="button" name="save" id="saveNext<%=docId%>" onclick="saveNext(<%=docId%>)" value='<bean:message key="inboxmanager.document.SaveAndNext"/>' /><%}
            else{%><input type="submit" name="save" id="save<%=docId%>" disabled value="Save" /><input type="button" name="save" onclick="saveNext(<%=docId%>)" id="saveNext<%=docId%>" disabled value='<bean:message key="inboxmanager.document.SaveAndNext"/>' /> <%}%>
                                       
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
                                                  
                                                    if(!s.equals("0")&&!s.equals("null")){  %>
                                                        <li><%=s%></li>
                                                <%}
                                                }%>
                                            </ul>
                                        </td>
                                    </tr>
                                </table>

                            </form>
                        </fieldset>


                            <%
                            AcknowledgementData ackData = new AcknowledgementData();
                            ArrayList ackList = ackData.getAcknowledgements("DOC",docId);

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
                                                        <input type="button" id="fwdBtn_<%=docId%>" class="smallButton" value="<bean:message key="oscarMDS.index.btnForward"/>" onClick="popupStart(300, 400, '../oscarMDS/SelectProviderAltView.jsp?doc_no=<%=documentNo%>&providerNo=<%=providerNo%>&searchProviderNo=<%=searchProviderNo%>&status=<%=status%>', 'providerselect')">
                                                        <input type="button" id="fileBtn_<%=docId%>" class="smallButton" value="<bean:message key="oscarMDS.index.btnFile"/>" onclick="fileDoc('<%=docId%>');">
                                                        <input type="button" id="closeBtn_<%=docId%>" value=" <bean:message key="global.btnClose"/> " onClick="window.close()">
                                                        <input type="button" id="printBtn_<%=docId%>" value=" <bean:message key="global.btnPrint"/> " onClick="popup(700,960,'<%=url2%>','file download')">
                                                        <% if (demographicID != null && !demographicID.equals("") && !demographicID.equalsIgnoreCase("null")) {%>
                                                        <input type="button" id="msgBtn_<%=docId%>" value="Msg" onclick="popupMsg(700,960,'<%=docId%>');"/>
                                                        <input type="button" id="ticklerBtn_<%=docId%>" value="Tickler" onclick="popupTickler(450,600,'<%=docId%>')"/>
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

