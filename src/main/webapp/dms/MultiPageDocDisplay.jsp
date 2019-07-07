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

<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
    String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
    boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_edoc" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../securityError.jsp?type=_edoc");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>

<%@page import="oscar.util.UtilDateUtilities"%>
<%@ page import="oscar.dms.*,java.util.*" %>
<%@ page import="org.oscarehr.phr.util.MyOscarUtils,org.oscarehr.myoscar.utils.MyOscarLoggedInInfo,org.oscarehr.util.WebUtils"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/rewrite-tag.tld" prefix="rewrite"%>
<%@ taglib uri="/WEB-INF/oscar-tag.tld" prefix="oscar"%>
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils,oscar.oscarLab.ca.all.*,oscar.oscarMDS.data.*,oscar.oscarLab.ca.all.util.*"%>
<%@page import="org.springframework.web.context.WebApplicationContext,org.oscarehr.common.dao.*,org.oscarehr.common.model.*, org.oscarehr.PMmodule.dao.ProviderDao"%>
<%
            WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
            ProviderInboxRoutingDao providerInboxRoutingDao = (ProviderInboxRoutingDao) ctx.getBean("providerInboxRoutingDAO");
            ProviderDao providerDao = (ProviderDao) ctx.getBean("providerDao");
            DemographicDao demographicDao = (DemographicDao)ctx.getBean("demographicDao");

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
            
            Provider provider = providerDao.getProvider(providerNo);
            String creator = (String) session.getAttribute("user");
            ArrayList doctypes = EDocUtil.getActiveDocTypes("demographic");
            EDoc curdoc = EDocUtil.getDoc(documentNo);

            String demographicID = curdoc.getModuleId();
            
            if(demoName == null || "".equals(demoName)) {
            	Demographic d = demographicDao.getDemographic(demographicID);
            	if(d != null) {
            		demoName = d.getFormattedName();
            	}
            }

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
            String url = request.getContextPath()+"/dms/ManageDocument.do?method=viewDocPage&doc_no=" + docId+"&curPage=1";
            String url2 = request.getContextPath()+"/dms/ManageDocument.do?method=display&doc_no=" + docId;
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
        <script type="text/javascript" src="<%= request.getContextPath() %>/share/javascript/jquery/jquery-1.4.2.js"></script>       

        <link rel="stylesheet" type="text/css" href="../share/yui/css/fonts-min.css"/>
        <link rel="stylesheet" type="text/css" href="../share/yui/css/autocomplete.css"/>
        <link rel="stylesheet" type="text/css" media="all" href="../share/css/demographicProviderAutocomplete.css"  />
        
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
    </head>
    <body >
        <div id="labdoc_<%=docId%>">
            <table class="docTable">
                <tr>


                    <td colspan="8">
                        <div style="text-align: right; font-weight: bold"> 
                        <% if( numOfPage > 1 ) {%> 
                        <a id="firstP" style="display: none;" href="javascript:void(0);" onclick="firstPage('<%=docId%>');">First</a>
                        <a id="prevP" style="display: none;" href="javascript:void(0);" onclick="prevPage('<%=docId%>');">Prev</a>
                        <a id="nextP" href="javascript:void(0);" onclick="nextPage('<%=docId%>');">Next</a>
                        <a id="lastP" href="javascript:void(0);" onclick="lastPage('<%=docId%>');">Last</a>                        
                        <%}%>
                        </div>
                        <a href="<%=url2%>" ><img alt="document" src="<%=url%>" id="docImg_<%=docId%>" /></a>
              
                        
                   </td>
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
                                    <td><span id="viewedPage_<%=docId%>" class="<%= numOfPage > 1 ? "multiPage" : "singlePage" %>">1</span>&nbsp; of &nbsp;<span id="numPages_<%=docId %>" class="<%= numOfPage > 1 ? "multiPage" : "singlePage" %>"><%=numOfPageStr%></span></td>
                                </tr>
                            </table>

                            <form id="forms_<%=docId%>" onsubmit="return updateDocument(this.id);" >
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
                                            <input id="saved<%=docId%>" type="hidden" name="saved" value="true"/>
                                            <input type="hidden" value="<%=demographicID%>" name="demog" id="demofind<%=docId%>" />
                                            <%=demoName%><%}else{%>
                                            <input id="saved<%=docId%>" type="hidden" name="saved" value="false"/>
                                            <input type="hidden" name="demog" value="<%=demographicID%>" id="demofind<%=docId%>" />
                                            <input tabindex="<%=tabindex++%>" type="text" id="autocompletedemo<%=docId%>" onchange="checkSave('<%=docId%>')" name="demographicKeyword" />
                                            <div id="autocomplete_choices<%=docId%>"class="autocomplete"></div>
                                            <%}%>

                                            <input id="mrp_<%=docId%>" tabindex="<%=tabindex++%>" onclick="sendMRP(this)" type="checkbox" name="demoLink" >Send to MRP
                                            <a id="mrp_fail_<%=docId%>" style="color:red;font-style: italic;display: none;" >Failed to send MRP</a>
                                        </td>
                                    </tr>

                                    <tr>
                                        <td valign="top">Flag Provider: </td>
                                        <td>
                                            <input type="hidden" name="provi" id="provfind<%=docId%>" />
                                            <input tabindex="<%=tabindex++%>" type="text" id="autocompleteprov<%=docId%>" name="demographicKeyword"/>
                                            <div id="autocomplete_choicesprov<%=docId%>" class="autocomplete"></div>

                                            <script type="text/javascript">
                                            jQuery.noConflict();
                                            
                                            
                                            function addDocComment(docId, status) {
                                            	var url="<%=request.getContextPath()%>/oscarMDS/UpdateStatus.do";                                            	
                                            	var formid = "#acknowledgeForm_" + docId;
                                            	
                                            	jQuery("#ackStatus").val(status);
                                            	var data= jQuery(formid).serialize();
                                            	data += "&method=addComment";
                                            	
                                            	jQuery.ajax({
                                            		type: "POST",
                                            		url: url,
                                            		data: data,
                                            		success: function(data) {
                                            			window.location.reload();
                                            		}
                                            	});
                                            	
                                            }

                                            
                                            function forwardDocument(docId) {
                                            	var frm = "#reassignForm_" + docId;
                                            	var query = jQuery(frm).serialize();
                                            	
                                            	jQuery.ajax({
                                            		type: "POST",
                                            		url:  "<%=request.getContextPath()%>/oscarMDS/ReportReassign.do",
                                            		data: query,
                                            		success: function (data) {
                                            			window.location.reload();
                                            		},
                                            		error: function(jqXHR, err, exception) {
                                            			alert(jqXHR.status);
                                            		}
                                            	});
                                            }

                                                var curPage=1;
                                                var totalPage=<%=numOfPage%>;
                                                showPageImg=function(docid,pn){
                                                    if(docid&&pn){
                                                        var e=$('docImg_'+docid);
                                                        var url='<%=request.getContextPath()%>'+'/dms/ManageDocument.do?method=viewDocPage&doc_no='
                                                            +docid+'&curPage='+pn;
                                                        e.setAttribute('src',url);
                                                    }
                                                }
                                                nextPage=function(docid){
                                                    curPage++;
                                                    
                                                    	$('viewedPage_'+docid).innerHTML = curPage;
                                                        showPageImg(docid,curPage);
                                                        if(curPage==totalPage){
                                                            hideNext();
                                                            showPrev();
                                                        } else{
                                                            showNext();
                                                            showPrev();
                                                        }
                                                }
                                                prevPage=function(docid){
                                                    curPage--;
                                                    if(curPage<1){
                                                        curPage=1;
                                                        hidePrev();
                                                    }
                                                    $('viewedPage_'+docid).innerHTML = curPage;
                                                        showPageImg(docid,curPage);
                                                       if(curPage==1){
                                                           hidePrev();
                                                           showNext();
                                                        }else{
                                                            showPrev();
                                                            showNext();
                                                        }
                                                    
                                                }
                                                firstPage=function(docid){
                                                    curPage=1;
                                                    $('viewedPage_'+docid).innerHTML = 1;
                                                    showPageImg(docid,curPage);
                                                    hidePrev();
                                                    showNext();
                                                }
                                                lastPage=function(docid){
                                                    curPage=totalPage;
                                                    $('viewedPage_'+docid).innerHTML = totalPage;
                                                    showPageImg(docid,curPage);
                                                    hideNext();
                                                    showPrev();
                                                }
                                                hidePrev=function(){
                                                    //disable previous link
                                                    $("prevP").setStyle({display:'none'});
                                                    $("firstP").setStyle({display:'none'});
                                                    $("prevP2").setStyle({display:'none'});
                                                    $("firstP2").setStyle({display:'none'});
                                                }
                                                hideNext=function(){
                                                    //disable next link
                                                    $("nextP").setStyle({display:'none'});
                                                    $("lastP").setStyle({display:'none'});
                                                    $("nextP2").setStyle({display:'none'});
                                                    $("lastP2").setStyle({display:'none'});
                                                }
                                                showPrev=function(){
                                                    //disable previous link
                                                    $("prevP").setStyle({display:'inline'});
                                                    $("firstP").setStyle({display:'inline'});
                                                    $("prevP2").setStyle({display:'inline'});
                                                    $("firstP2").setStyle({display:'inline'});
                                                }
                                                showNext=function(){
                                                    //disable next link
                                                    $("nextP").setStyle({display:'inline'});
                                                    $("lastP").setStyle({display:'inline'});
                                                    $("nextP2").setStyle({display:'inline'});
                                                    $("lastP2").setStyle({display:'inline'});
                                                }
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
                                                            bdoc.setAttribute("id", "removeProv<%=docId%>");
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
                                                    var num=formid.split("_");
                                                        var doclabid=num[1];
                                                        if(doclabid){
                                                            var demoId=$('demofind'+doclabid).value;
                                                            var saved=$('saved'+doclabid).value;
                                                            if(demoId=='-1'|| saved=='false' ||saved==false){
                                                                alert('Document is not assigned to a patient,please file it');
                                                            }else{
                                                                var url='<%=request.getContextPath()%>'+"/oscarMDS/UpdateStatus.do";
                                                            var data=$(formid).serialize(true);

                                                            new Ajax.Request(url,{method:'post',parameters:data,onSuccess:function(transport){
                                                                            refreshParent();
                                                                     window.close();
                                                                }});
                                                           }
                                                        }
                                                   }

                                        fileDoc=function(docId){
                                           if(docId){
                                                docId=docId.replace(/\s/,'');
                                             if(docId.length>0){
                                                    var demoId=$('demofind'+docId).value;
                                                    var saved=$('saved'+docId).value;
                                                    var isFile=true;
                                                     if(demoId=='-1' || saved=='false' ||saved==false){
                                                        isFile=confirm('Document is not assigned and saved to any patient, do you still want to file it?');
                                                                }
                                                     if(isFile) {
                                                             var type='DOC';
                                                             if(type){
                                                                var url='../oscarMDS/FileLabs.do';
                                                                var data='method=fileLabAjax&flaggedLabId='+docId+'&labType='+type;
                                                                new Ajax.Request(url, {method: 'post',parameters:data,onSuccess:function(transport){
                                                                        refreshParent();
                                                                        window.close();
                                                            }});
                                                    }
                                                    }
                                              }
                                           }
                                       }
function sendMRP(ele){
                                                var doclabid=ele.id;
                                                doclabid=doclabid.split('_')[1];
                                                var demoId=$('demofind'+doclabid).value;
                                            if(demoId=='-1'){
                                                alert('Please enter a valid demographic');
                                                ele.checked=false;
                                            }else{
                                                if(confirm('Send to Most Responsible Provider?')){
                                                    var type='DOC';
                                                    var url= "../oscarMDS/SendMRP.do";
                                                    var data='demoId='+demoId+'&docLabType='+type+'&docLabId='+doclabid;
                                                    new Ajax.Request(url, {method: 'post',parameters:data,onSuccess:function(transport){
                                                        ele.disabled=true;
                                                        $('mrp_fail_'+doclabid).hide();
                                                    },onFailure:function(transport){
                                                        ele.checked=false;
                                                        $('mrp_fail_'+doclabid).show();
                                                    }});
                                                }else{
                                                    ele.checked=false;
                                                }
                                             }
                                          }

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
                                                    fields : ["formattedName","fomattedDob","demographicNo","status"]
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

                        updateDocument=function(eleId){
                        	if (!checkObservationDate(eleId)) {
                        		return false;
                        	}
                        	//save doc info
                                                    var url="../dms/ManageDocument.do",data=$(eleId).serialize(true);
                                                    new Ajax.Request(url,{method:'post',parameters:data,onSuccess:function(transport){
                                                                var ar=eleId.split("_");
                                                                var num=ar[1];
                                                                num=num.replace(/\s/g,'');
                                                           if($("saveSucessMsg_"+num))     $("saveSucessMsg_"+num).show();
                                                           if($('saved'+num))      $('saved'+num).value='true';
                                                           if($('autocompletedemo'+num))
                                                               $('autocompletedemo'+num).disabled=true;
                                                           if($('removeProv'+num))
                                                                    $('removeProv'+num).remove();

                                                refreshParent();

                                                    }});
                                                    return false;
                                        }

                        function checkObservationDate(formid) {
                            // regular expression to match required date format
                            re = /^\d{4}\-\d{1,2}\-\d{1,2}$/;
                            re2 = /^\d{4}\/\d{1,2}\/\d{1,2}$/;

                            var form = document.getElementById(formid);
                            if(form.elements["observationDate"].value == "") {
                            	alert("Blank Date: " + form.elements["observationDate"].value);
                        		form.elements["observationDate"].focus();
                        		return false;
                            }
                            
                            if(!form.elements["observationDate"].value.match(re)) {
                            	if(!form.elements["observationDate"].value.match(re2)) {
                            		alert("Invalid date format: " + form.elements["observationDate"].value);
                            		form.elements["observationDate"].focus();
                            		return false;
                            	} else if(form.elements["observationDate"].value.match(re2)) {
                            		form.elements["observationDate"].value=form.elements["observationDate"].value.replace("/","-");
                            		form.elements["observationDate"].value=form.elements["observationDate"].value.replace("/","-");
                            	}
                            }
                            regs= form.elements["observationDate"].value.split("-");
                            // day value between 1 and 31
                            if(regs[2] < 1 || regs[2] > 31) {
                              alert("Invalid value for day: " + regs[2]);
                              form.elements["observationDate"].focus();
                              return false;
                            }
                            // month value between 1 and 12
                            if(regs[1] < 1 || regs[1] > 12) {
                              alert("Invalid value for month: " + regs[1]);
                              form.elements["observationDate"].focus();
                              return false;
                            }
                            // year value between 1902 and 2015
                            if(regs[0] < 1902 || regs[0] > (new Date()).getFullYear()) {
                              alert("Invalid value for year: " + regs[0] + " - must be between 1902 and " + (new Date()).getFullYear());
                              form.elements["observationDate"].focus();
                              return false;
                            }
                            return true;
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
                                        <td colspan="2" align="right"><a id="saveSucessMsg_<%=docId%>" style="display:none;color:blue;"><bean:message key="inboxmanager.document.SuccessfullySavedMsg"/></a><%if(!demographicID.equals("-1")){%><input type="submit" name="save" tabindex="<%=tabindex++%>" id="save<%=docId%>" value="Save" /><%} else{%><input type="submit" name="save" tabindex="<%=tabindex++%>" id="save<%=docId%>" disabled value="Save" /> <%}%></td>
                                    </tr>

                                    <tr>
                                        <td colspan="2">
                                            Linked Providers:
                                            <%
            Properties p = (Properties) session.getAttribute("providerBean");
            List<ProviderInboxItem> routeList = providerInboxRoutingDao.getProvidersWithRoutingForDocument("DOC", Integer.parseInt(docId));
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
                            ArrayList ackList = AcknowledgementData.getAcknowledgements("DOC",docId);
							String curAckStatus = "N";
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
                                                                        	if( providerNo.equals(report.getOscarProviderNo()) ) {
                                                                        	    curAckStatus = ackStatus;
                                                                        	}
                                                                            if(ackStatus.equals("A")){
                                                                                ackStatus = "Acknowledged";
                                                                            }else if(ackStatus.equals("F")){
                                                                                ackStatus = "Filed but not Acknowledged";
                                                                            }else{
                                                                                ackStatus = "Not Acknowledged";
                                                                            }
                                                                        %>
                                                                        <font color="red"><%= ackStatus %></font>
                                                                       		&nbsp;
                                                                            <%= report.getTimestamp()== null ? "" : report.getTimestamp() %>,&nbsp;
                                                                            comment: <%= ( report.getComment() == null || report.getComment().equals("") ? "no comment" : report.getComment() ) %>
                                                                       
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
                            <legend><span class="FieldData"><i>Next Appointment: <oscar:nextAppt demographicNo="<%=demographicID%>"/></i></span></legend>
                            <form id="reassignForm_<%=docId%>" name="reassignForm_<%=docId%>" method="post" action="">
                                <input type="hidden" name="flaggedLabs" value="<%=docId%>" />
                                <input type="hidden" name="selectedProviders" value="" />
                                <input type="hidden" name="labType" value="DOC" />
                                <input type="hidden" name="labType<%=docId%>DOC" value="imNotNull" />
                                <input type="hidden" name="providerNo" value="<%=providerNo%>" />
                                <input type="hidden" name="favorites" value="" />
                                <input type="hidden" name="ajax" value="yes" />
                            </form>
                            </fieldset>
                         <fieldset>
                         	<legend><bean:message key="inboxmanager.document.Comment"/></legend>
                                <form name="acknowledgeForm_<%=docId%>" id="acknowledgeForm_<%=docId%>" onsubmit="updateStatus('acknowledgeForm_<%=docId%>');" method="post" action="javascript:void(0);">

                                <table width="100%" height="100%" border="0" cellspacing="0" cellpadding="0">
                                    <tr>
                                        <td valign="top">
                                            <table width="100%" border="0" cellspacing="0" cellpadding="3">
                                                <tr>
                                                    <td align="left" class="" width="100%">
                                                        <input type="hidden" name="segmentID" value="<%= docId%>"/>
                                                        <input type="hidden" name="multiID" value="<%= docId%>" />
                                                        <input type="hidden" name="providerNo" value="<%= providerNo%>"/>
                                                        <input type="hidden" name="status" value="A" id="ackStatus"/>
                                                        <input type="hidden" name="labType" value="DOC"/>
                                                        <input type="hidden" name="ajaxcall" value="yes"/>
                                                        <textarea  tabindex="<%=tabindex++%>" name="comment" cols="40" rows="4"></textarea>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td>
                                                        <input type="submit"  tabindex="<%=tabindex++%>" value="<bean:message key="oscarMDS.segmentDisplay.btnAcknowledge"/>" >
                                                        <input type="button"  tabindex="<%=tabindex++%>" class="smallButton" value="Comment" onclick="addDocComment('<%=docId%>','<%=curAckStatus%>')"/>
                                                        <input type="button"  tabindex="<%=tabindex++%>" class="smallButton" value="<bean:message key="oscarMDS.index.btnForward"/>" onClick="popup(323, 685, '../oscarMDS/SelectProvider.jsp?docId=<%=docId%>&providerNo=<%=providerNo%>&searchProviderNo=<%=searchProviderNo%>&status=<%=status%>', 'providerselect')">
                                                        <input type="button"  tabindex="<%=tabindex++%>" class="smallButton" value="<bean:message key="oscarMDS.index.btnFile"/>" onclick="fileDoc('<%=documentNo%>');" >
                                                        <input type="button"  tabindex="<%=tabindex++%>" value=" <bean:message key="global.btnClose"/> " onClick="window.close()">
                                                        <input type="button"  tabindex="<%=tabindex++%>" value=" <bean:message key="global.btnPrint"/> " onClick="popup(700,960,'<%=url2%>','file download')">
                                                        <% if (demographicID != null && !demographicID.equals("") && !demographicID.equalsIgnoreCase("null") && !demographicID.equals("-1")) {
                                                        	String  eURL = "../oscarEncounter/IncomingEncounter.do?providerNo=" + providerNo + "&appointmentNo=&demographicNo=" + demographicID + "&curProviderNo=&reason=" + java.net.URLEncoder.encode("Document Notes","UTF-8") + "&encType=" + java.net.URLEncoder.encode("encounter without client","UTF-8") + "&userName=" + java.net.URLEncoder.encode( provider.getFullName(),"UTF-8") + "&curDate=" + UtilDateUtilities.getToday("yyyy-MM-dd")+ "&appointmentDate=&startTime=&status=";
                                                       	%>
                                                        <input type="button"  tabindex="<%=tabindex++%>" value="Msg" onclick="popup(700,960,'../oscarMessenger/SendDemoMessage.do?demographic_no=<%=demographicID%>','msg')"/>
                                                        <input type="button"  tabindex="<%=tabindex++%>" value="Tickler" onclick="popup(450,600,'../tickler/ForwardDemographicTickler.do?docType=DOC&docId=<%=docId%>&demographic_no=<%=demographicID%>','tickler')"/>
                                                        <input type="button"  tabindex="<%=tabindex++%>" value="eChart" onclick="popup(710,1024,'<%=eURL%>','encounter')"/>
                                                        <%if (MyOscarUtils.isMyOscarEnabled((String) session.getAttribute("user"))){
															MyOscarLoggedInInfo myOscarLoggedInInfo=MyOscarLoggedInInfo.getLoggedInInfo(session);
															boolean enabledMyOscarButton=MyOscarUtils.isMyOscarSendButtonEnabled(myOscarLoggedInInfo, Integer.valueOf(demographicID));
														%>
														<input type="button" <%=WebUtils.getDisabledString(enabledMyOscarButton)%> tabindex="<%=tabindex++%>" value="<bean:message key="global.btnSendToPHR"/>" onclick="popup(450, 600, '../phr/SendToPhrPreview.jsp?module=document&documentNo=<%=docId%>&demographic_no=<%=demographicID%>', 'sendtophr')"/>
				
														<%}
		
                                                         }%>
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
                <tr>
                	 <td colspan="8">
                        <div style="text-align: right; font-weight: bold"> 
                        <% if( numOfPage > 1 ) {%> 
                        <a id="firstP2" style="display: none;" href="javascript:void(0);" onclick="firstPage('<%=docId%>');">First</a>
                        <a id="prevP2" style="display: none;" href="javascript:void(0);" onclick="prevPage('<%=docId%>');">Prev</a>
                        <a id="nextP2" href="javascript:void(0);" onclick="nextPage('<%=docId%>');">Next</a>
                        <a id="lastP2" href="javascript:void(0);" onclick="lastPage('<%=docId%>');">Last</a>                        
                        <%}%>
                        </div>
                                  
                   </td>
                   <td>&nbsp;</td>
                </tr>
                <tr><td colspan="9" ><hr width="100%" color="blue"></td></tr>
            </table>
        </div>

    </body>
</html>
