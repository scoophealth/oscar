<%@ page import="oscar.dms.*,java.util.*" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/rewrite-tag.tld" prefix="rewrite"%>
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils,oscar.oscarLab.ca.all.*,oscar.oscarMDS.data.*,oscar.oscarLab.ca.all.util.*"%>
<%@page import="org.springframework.web.context.WebApplicationContext,org.oscarehr.common.dao.*,org.oscarehr.common.model.*"%>
<%
//System.out.println("start first part java");
            WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
            ProviderInboxRoutingDao providerInboxRoutingDao = (ProviderInboxRoutingDao) ctx.getBean("providerInboxRoutingDAO");

            String demoName, documentNo,providerNo,searchProviderNo,status;

             demoName=(String)request.getAttribute("demoName");
             documentNo = (String)request.getAttribute("segmentID");
             providerNo = (String)request.getAttribute("providerNo");
             searchProviderNo = (String)request.getAttribute("searchProviderNo");
             status = (String)request.getAttribute("status");
            if(demoName==null && documentNo==null &&providerNo==null &&searchProviderNo==null &&status==null ){
                         demoName=request.getParameter("demoName");
                         documentNo = request.getParameter("segmentID");
                         providerNo = request.getParameter("providerNo");
                         searchProviderNo = request.getParameter("searchProviderNo");
                         status = request.getParameter("status");
            }
            String creator = (String) session.getAttribute("user");
            ArrayList doctypes = EDocUtil.getDoctypes("demographic");
            EDocUtil edocUtil = new EDocUtil();
            EDoc curdoc = edocUtil.getDoc(documentNo);

            String demographicID = curdoc.getModuleId();

            String docId = curdoc.getDocId();
            int tabindex = 0;
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
            String url = request.getContextPath()+"/dms/ManageDocument.do?method=view&doc_no=" + docId;
            String url2 = request.getContextPath()+"/dms/ManageDocument.do?method=display&doc_no=" + docId;
            //System.out.println("done first part java");
%>
        
<html>
    <head>
            <!-- main calendar program -->
<script type="text/javascript" src="../share/calendar/calendar.js"></script>
<!-- language for the calendar -->
<script type="text/javascript" src="../share/calendar/lang/<bean:message key='global.javascript.calendar'/>"></script>
<!-- the following script defines the Calendar.setup helper function, which makes
       adding a calendar a matter of 1 or 2 lines of code. -->
<script type="text/javascript" src="../share/calendar/calendar-setup.js"></script>
<!-- calendar stylesheet -->
<link rel="stylesheet" type="text/css" media="all" href="../share/calendar/calendar.css" title="win2k-cold-1" />
        <script language="javascript" type="text/javascript" src="../share/javascript/Oscar.js" ></script>
        <script type="text/javascript" src="../share/javascript/prototype.js"></script>
        <script type="text/javascript" src="../share/javascript/effects.js"></script>
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
    </head>
    <body>
        <div id="labdoc_<%=docId%>">
            <table class="docTable">
                <tr>


                    <td colspan="8"><a href="<%=url2%>"><img  src="<%=url%>" /></a></td>


                    <td align="left" valign="top">
                        <fieldset><legend>Patient:<%=demoName%> </legend>
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

                                    <form id="forms_<%=docId%>" action="ManageDocument.do" onsubmit="refreshParent();" >
                                <input type="hidden" name="method" value="documentUpdate" />
                                <input type="hidden" name="documentId" value="<%=docId%>" />
                                <input type="hidden" name="providerNo" value="<%=providerNo%>" />
                                <input type="hidden" name="searchProviderNo" value="<%=searchProviderNo%>" />
                                <input type="hidden" name="status" value="<%=status%>" />
                                <table border="0">
                                    <tr>
                                        <td><bean:message key="dms.documentReport.msgDocType" />:</td>
                                        <td>
                                            <select tabindex="<%=tabindex++%>" name ="docType" id="docType">
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
                                        <td><input tabindex="<%=tabindex++%>"  type="text" name="documentDescription" value="<%=curdoc.getDescription()%>" /></td>
                                    </tr>
                                    <tr>
                                        <td>Observation Date:</td>
                                        <td>
                                            <input tabindex="<%=tabindex++%>"  id="observationDate<%=docId%>" name="observationDate" type="text" value="<%=curdoc.getObservationDate()%>">
                                            <a id="obsdate<%=docId%>" onmouseover="renderCalendar(this.id,'observationDate<%=docId%>' );" href="javascript:void(0);" ><img title="Calendar" src="<%=request.getContextPath()%>/images/cal.gif" alt="Calendar"border="0" /></a>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>Demographic:
                                        </td>
                                        <td><%if(!demographicID.equals("-1")){%>
                                            <input type="hidden" value="<%=demographicID%>" name="demog" id="demofind<%=docId%>" />
                                            <%=demoName%><%}else{%>
                                            <input type="hidden" name="demog" value="<%=demographicID%>" id="demofind<%=docId%>" />
                                            <input tabindex="<%=tabindex++%>" type="text" id="autocompletedemo<%=docId%>" onchange="checkSave('<%=docId%>')" name="demographicKeyword" />
                                            <div id="autocomplete_choices<%=docId%>"class="autocomplete"></div>
                                            <%}%>

         <script type="text/javascript">

        renderCalendar=function(id,inputFieldId){
                Calendar.setup({ inputField : inputFieldId, ifFormat : "%Y-%m-%d", showsTime :false, button : id });
        }

                                        YAHOO.example.BasicRemote = function() {
                                          if($("autocompletedemo<%=docId%>") && $("autocomplete_choices<%=docId%>")){
                                                 oscarLog('in basic remote');
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
                                                //oDS.connXhrMode ="cancelStaleRequests";
                                                //oscarLog("autocompletedemo<%=docId%>");
                                                //oscarLog("autocomplete_choices<%=docId%>");

                                                //var elinput=window.frames[0].document.getElementById("autocompletedemo<%=docId%>");
                                                //var elcontainer=window.frames[0].document.getElementById("autocomplete_choices<%=docId%>");
                                                //oscarLog('elinput='+elinput+';elcontainer='+elcontainer);
                                                // Instantiate the AutoComplete
                                                //var oAC = new YAHOO.widget.AutoComplete("autocompletedemo<%=docId%>", "autocomplete_choices<%=docId%>", oDS);
                                                var oAC = new YAHOO.widget.AutoComplete("autocompletedemo<%=docId%>","autocomplete_choices<%=docId%>",oDS);
                                                //oscarLog('oAc='+oAC);
                                                //oscarLog('oDs='+oDS);
                                                //oscarLog('resultFormatter2='+resultFormatter2);
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
                                                   //oscarLog("str value="+$(str).value);
                                                   //oscarLog(args[2][1]+"--"+args[2][0]);
                                                   args[0].getInputEl().value = args[2][0] + "("+args[2][1]+")";
                                                   //oscarLog("--"+args[0].getInputEl().value);
                                                   selectedDemos.push(args[0].getInputEl().value);
                                                   //enable Save button whenever a selection is made
                                                   $('save<%=docId%>').enable();

                                                });


                                                return {
                                                    oDS: oDS,
                                                    oAC: oAC
                                                };
                                            }
                                            }();

                                            </script>
                                            <input tabindex="<%=tabindex++%>" type="checkbox" name="demoLink" >Send to MRP
                                        </td>
                                    </tr>

                                    <tr>
                                        <td valign="top">Flag Provider: </td>
                                        <td>
                                            <input type="hidden" name="provi" id="provfind<%=docId%>" />
                                            <input tabindex="<%=tabindex++%>" type="text" id="autocompleteprov<%=docId%>" name="demographicKeyword"/>
                                            <div id="autocomplete_choicesprov<%=docId%>" class="autocomplete"></div>

                                            <script type="text/javascript">

                                                popupStart=function(vheight,vwidth,varpage,windowname) {
                                                    oscarLog("in popupStart ");
                                                    if(!windowname)
                                                        windowname="helpwindow";
                                                    var page = varpage;
                                                    var windowprops = "height="+vheight+",width="+vwidth+",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes";
                                                    oscarLog(varpage);
                                                    oscarLog(windowname);
                                                    oscarLog(windowprops);
                                                    var popup=window.open(varpage, windowname, windowprops);
                                                }
                                         /*       updateDocument=function(eleId){

                                                    var url="../dms/ManageDocument.do",data=$(eleId).serialize(true);
                                                    new Ajax.Request(url,{method:'post',parameters:data,onSuccess:function(transport){
                                                            //location.reload(true);
                                                            var ar=eleId.split("_");
                                                            var num=ar[1];
                                                            $("saveSucessMsg_"+num).show();
                                                    }});
                                                    return false;
                                                }
                                             */
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
                                                        //oDS.connXhrMode ="cancelStaleRequests";
                                                        //oscarLog("autocompleteprov<%=docId%>");
                                                        //oscarLog("autocomplete_choicesprov<%=docId%>");
                                                        //oscarLog($("autocompleteprov<%=docId%>"));
                                                        //oscarLog($("autocomplete_choicesprov<%=docId%>"));
                                                        // Instantiate the AutoComplete
                                                        var oAC = new YAHOO.widget.AutoComplete("autocompleteprov<%=docId%>", "autocomplete_choicesprov<%=docId%>", oDS);
                                                        oAC.queryMatchSubset = true;
                                                        oAC.minQueryLength = 3;
                                                        oAC.maxResultsDisplayed = 25;
                                                        oAC.formatResult = resultFormatter3;
                                                        //oAC.typeAhead = true;
                                                        oAC.queryMatchContains = true;
                                                        oscarLog(oAC);
                                                        oscarLog(oAC.itemSelectEvent);
                                                        oAC.itemSelectEvent.subscribe(function(type, args) {
                                                            oscarLog(args);
                                                           var myAC = args[0];
                                                           var str = myAC.getInputEl().id.replace("autocompleteprov","provfind");
                                                           oscarLog(str);
                                                           oscarLog(args[2]);
                                                           var oData=args[2];
                                                           $(str).value = args[2][0];//li.id;
                                                           oscarLog("str value="+$(str).value);
                                                           oscarLog(args[2][1]+"--"+args[2][0]);
                                                           myAC.getInputEl().value = args[2][2] + ","+args[2][1];
                                                           oscarLog("--"+args[0].getInputEl().value);
                                                           //selectedDemos.push(args[0].getInputEl().value);

                                                           //enable Save button whenever a selection is made
                                                            var bdoc = document.createElement('a');
                                                            bdoc.setAttribute("onclick", "removeProv(this);");
                                                            bdoc.appendChild(document.createTextNode(" -remove- "));
                                                            oscarLog("--");
                                                            var adoc = document.createElement('div');
                                                            adoc.appendChild(document.createTextNode(oData[2] + " " +oData[1]));
                                                            oscarLog("--==");
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
                                                    refreshParent=function(){
                                                        window.opener.location.reload();
                                                    }
                                                    updateStatus=function(formid){
                                                        var url='<%=request.getContextPath()%>'+"/oscarMDS/UpdateStatus.do";
                                                        var data=$(formid).serialize(true);

                                                        new Ajax.Request(url,{method:'post',parameters:data,onSuccess:function(transport){
                                                                var num=formid.split("_");
                                                                //Effect.BlindUp('labdoc_'+num[1]);
                                                                window.close();

                                                    }});

                                                    }
                                            </script>
                                            <div id="providerList<%=docId%>"></div>
                                        </td>
                                    </tr>



                                    <tr>
                                        <td><bean:message key="dms.documentReport.msgCreator"/>:</td>
                                        <td><%=curdoc.getCreatorName()%></td>
                                    </tr>

                                    <tr>
                                        <td colspan="2" align="right"><%if(!demographicID.equals("-1")){%><input type="submit" name="save" tabindex="<%=tabindex++%>" id="save<%=docId%>" value="Save" /><%} else{%><input type="submit" name="save" tabindex="<%=tabindex++%>" id="save<%=docId%>" disabled value="Save" /> <%}%></td>
                                    </tr>

                                    <tr>
                                        <td colspan="2">
                                            Linked Providers:
                                            <%
            Properties p = (Properties) session.getAttribute("providerBean");
            List<ProviderInboxItem> routeList = providerInboxRoutingDao.getProvidersWithRoutingForDocument("DOC", docId);
                                            %>
                                            <ul>
                                                <%for (ProviderInboxItem pItem : routeList) {
                                                    String s=p.getProperty(pItem.getProviderNo(), pItem.getProviderNo());
                                                    if(!s.equals("0")){  %>
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

//System.out.println("done second part java");
%>
                        

                        <fieldset>
                            <legend><span class="FieldData"><i>Next Appointment: <oscar:nextAppt demographicNo="<%=demographicID%>"/></i></span></legend>
                            <form name="reassignForm" method="post" action="Forward.do">
                                <input type="hidden" name="flaggedLabs" value="<%= docId%>" />
                                <input type="hidden" name="selectedProviders" value="" />
                                <input type="hidden" name="labType" value="DOC" />
                                <input type="hidden" name="labType<%= docId%>DOC" value="imNotNull" />
                                <input type="hidden" name="providerNo" value="<%= providerNo%>" />
                            </form>
                                <form name="acknowledgeForm_<%=docId%>" id="acknowledgeForm_<%=docId%>" onsubmit="updateStatus('acknowledgeForm_<%=docId%>');refreshParent();" method="post" action="javascript:void(0);">

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
                                                        <textarea  tabindex="<%=tabindex++%>" name="comment" cols="40" rows="4"></textarea>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td>
                                                        <input type="submit"  tabindex="<%=tabindex++%>" value="<bean:message key="oscarMDS.segmentDisplay.btnAcknowledge"/>" >
                                                        <input type="button"  tabindex="<%=tabindex++%>" class="smallButton" value="<bean:message key="oscarMDS.index.btnForward"/>" onClick="popupStart(300, 400, '../oscarMDS/SelectProvider.jsp?doc_no=<%=documentNo%>&providerNo=<%=providerNo%>&searchProviderNo=<%=searchProviderNo%>&status=<%=status%>', 'providerselect')">
                                                        <input type="button"  tabindex="<%=tabindex++%>" value=" <bean:message key="global.btnClose"/> " onClick="window.close()">
                                                        <input type="button"  tabindex="<%=tabindex++%>" value=" <bean:message key="global.btnPrint"/> " onClick="popup(700,960,'<%=url2%>','file download')">
                                                        <% if (demographicID != null && !demographicID.equals("") && !demographicID.equalsIgnoreCase("null")) {%>
                                                        <input type="button"  tabindex="<%=tabindex++%>" value="Msg" onclick="popup(700,960,'../oscarMessenger/SendDemoMessage.do?demographic_no=<%=demographicID%>','msg')"/>
                                                        <input type="button"  tabindex="<%=tabindex++%>" value="Tickler" onclick="popup(450,600,'../tickler/ForwardDemographicTickler.do?docType=DOC&docId=<%=docId%>&demographic_no=<%=demographicID%>','tickler')"/>
                                                        <% }

                                                        //System.out.println("done third part java");
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
                <tr><td colspan="9" ><hr width="100%" color="blue"></td></tr>
            </table>
        </div>

    </body>
</html>