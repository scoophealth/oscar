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

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/rewrite-tag.tld" prefix="rewrite"%>
<%@ taglib uri="/WEB-INF/oscarProperties-tag.tld" prefix="oscarProp"%>
<%@ taglib uri="/WEB-INF/indivo-tag.tld" prefix="indivo"%>

<%@ page import="java.math.*, java.util.*, java.io.*, java.sql.*, oscar.*, oscar.util.*, java.net.*,oscar.MyDateFormat, oscar.dms.*, oscar.dms.data.*, oscar.oscarProvider.data.ProviderMyOscarIdData, oscar.oscarDemographic.data.DemographicData"%>
<%@ page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="org.springframework.web.context.WebApplicationContext"%>
<%@page import="org.oscarehr.util.SessionConstants"%>
<%@page import="oscar.oscarProvider.data.*"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
   <style type="text/css">
#myAutoComplete {
    width:15em; /* set width here or else widget will expand to fit its container */
    padding-bottom:2em;
}
       .yui-ac {
	    position:relative;font-family:arial;font-size:100%;
	}

	/* styles for input field */
	.yui-ac-input {
	    position:relative;width:100%;
	}

	/* styles for results container */
	.yui-ac-container {
	    position:absolute;top:0em;width:100%;
	}

	/* styles for header/body/footer wrapper within container */
	.yui-ac-content {
	    position:absolute;width:200px;border:1px solid #808080;background:#fff;overflow:hidden;z-index:9050;
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


<%
String module = "demographic";
String currentProvider=request.getParameter("currentProvider");
String updateParent="false";
String parentAjaxId = "";
Hashtable providerDocs=(Hashtable)request.getSession().getAttribute("providerDocs");

ArrayList doctypes = EDocUtil.getDoctypes(module);%>
        

<div id="DocumentProvider_<%=currentProvider%>" style="background-color: #f2f7ff;">
<%
if(providerDocs!=null && !providerDocs.isEmpty() && providerDocs.containsKey(currentProvider)){
                                        List<EDoc> docList=(List<EDoc>)providerDocs.get(currentProvider);
                                        for(EDoc curdoc:docList){
                                            String curDocId=curdoc.getDocId();
                                            String docUrl=request.getContextPath()+"/dms/ManageDocument.do?method=display&doc_no="+curDocId;
                                            String url = "ManageDocument.do?method=view&doc_no=" + curDocId;
                                            String contentType = "";
                                            int slash = 0;
                                            int tabindex = 1;
                                            if ((slash = curdoc.getContentType().indexOf('/')) != -1) {
                                                contentType = curdoc.getContentType().substring(slash + 1);
                                            }
                                            String numPageStr="unknown";
                                            int numPage=curdoc.getNumberOfPages();
                                            if(numPage!=0)
                                                numPageStr=(new Integer(numPage)).toString();
                                            %>
                                <div id="document<%=curDocId%>" >
                                    <table class="docTable">
                                        <tr>
                                            <td colspan="8">
                                                <a href="javascript:void(0);">
                                                    <img src="<%=url%>" onclick="popupPage(500,600,'name_<%=curDocId%>','<%=docUrl%>')"  title="Click to Download File" />
                                                </a>
                                            </td>

                                            <td align="left" valign="top">
                                                <fieldset >
                                                    <legend>Document Uploaded :<%=curdoc.getDateTimeStamp()%> - Content Type: <%=contentType%> <%=curdoc.getFileName() %> - Number of Pages: <%=numPageStr%></legend>
                                                    <form id="forms<%=curDocId%>" action="undocumentReport.jsp" onsubmit="return sendToServer('forms<%=curDocId%>');">
                                                        <input type="hidden" name="method" value="documentUpdate" />
                                                        <input type="hidden" name="documentId" value="<%=curDocId%>" />
                                                        <table>
                                                            <tr>
                                                                <td><bean:message key="dms.documentReport.msgDocType" />:</td>
                                                                <td>
                                                                    <select tabindex="<%=tabindex++%>" name="docType" id="docType">
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
                                                                <td><input tabindex="<%=tabindex++%>" type="text" name="documentDescription" value="<%=curdoc.getDescription()%>" /></td>
                                                            </tr>
                                                            <tr>
                                                                <td>Observation Date:</td>
                                                                <td>
                                                                    <input tabindex="<%=tabindex++%>" id="observationDate<%=curDocId%>" name="observationDate<%=curDocId%>" type="text" value="<%=curdoc.getObservationDate()%>">
                                                                    <a id="obsdate<%=curDocId%>" onmouseover="renderCalendar(this.id,'observationDate<%=curDocId%>' );" href="javascript:void(0);" ><img title="Calendar" src="../images/cal.gif" alt="Calendar"border="0" /></a>
                                                                </td>
                                                            </tr>
                                                            <tr>
                                                                <td>Demographic: <% if (request.getParameter("demo_linked_Id") != null && request.getParameter("demo_linked_Id").equals(curDocId)) {%>
                                                                    <%=request.getParameter("name")%> <% }%>
                                                                </td>
                                                                <td><input type="hidden" name="demog" id="demofind<%=curDocId%>" />
                                                                    <input tabindex="<%=tabindex++%>" type="text" id="autocompletedemo<%=curDocId%>" onchange="checkSave('<%=curDocId%>')" name="demographicKeyword"  />
                                                                    <div id="autocomplete_choices<%=curDocId%>"class="autocomplete"></div>
             <script type="text/javascript">

       YAHOO.example.BasicRemote = function() {
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

                // Instantiate the AutoComplete
                var oAC = new YAHOO.widget.AutoComplete("autocompletedemo<%=curDocId%>", "autocomplete_choices<%=curDocId%>", oDS);
                oAC.queryMatchSubset = true;
                oAC.minQueryLength = 3;
                oAC.maxResultsDisplayed = 25;
                oAC.formatResult = resultFormatter2;
                //oAC.typeAhead = true;
                oAC.queryMatchContains = true;
                //oscarLog(oAC);
                //oscarLog(oAC.itemSelectEvent);
                oAC.itemSelectEvent.subscribe(function(type, args) {
                    oscarLog(args);
                    oscarLog(args[0].getInputEl().id);
                    var str = args[0].getInputEl().id.replace("autocompletedemo","demofind");
                   oscarLog(str);
                   $(str).value = args[2][2];//li.id;
                   oscarLog("str value="+$(str).value);
                   //oscarLog(args[2][1]+"--"+args[2][0]);
                   args[0].getInputEl().value = args[2][0] + "("+args[2][1]+")";
                   //oscarLog("--"+args[0].getInputEl().value);
                   selectedDemos.push(args[0].getInputEl().value);
                   //enable Save button whenever a selection is made
                   $('save<%=curDocId%>').enable();

                });


                return {
                    oDS: oDS,
                    oAC: oAC
                };
            }();



                                                                    </script>
                                                                    <input type="checkbox" name="demoLink" >Send to MRP
                                                                </td>
                                                            </tr>


                                                            <tr>
                                                                <td valign="top">Flag Provider: </td>

                                                                <td>
                                                                    <div class="myAutoComplete">
                                                                        <input type="hidden" name="provi" id="provfind<%=curDocId%>" />
                                                                        <input tabindex="<%=tabindex++%>" type="text" id="autocompleteprov<%=curDocId%>" name="providerKeyword"/>
                                                                        <div id="autocomplete_choicesprov<%=curDocId%>" class="autocomplete"></div>
                                                                    </div>

                                                                    <!--script type="text/javascript">

                                                                    YAHOO.example.FnMultipleFields = function(){
                                                                        //oscarLog("FnMultipleFields ");
                                                                        //oscarLog(oDS.responseSchema);
                                                                        // Instantiate AutoComplete
                                                                        var oAC = new YAHOO.widget.AutoComplete("autocompleteprov<%=curDocId%>", "autocomplete_choicesprov<%=curDocId%>", oDS);
                                                                        oAC.useShadow = true;
                                                                        oAC.resultTypeList = false;

                                                                        // Custom formatter to highlight the matching letters
                                                                        oAC.formatResult = resultFormatter;


                                                                        // Define an event handler to populate a hidden form field
                                                                        // when an item gets selected and populate the input field
                                                                        //var myHiddenField = YAHOO.util.Dom.get("myHidden");
                                                                        var myHandler = function(sType, aArgs) {
                                                                            var myAC = aArgs[0]; // reference back to the AC instance
                                                                            var elLI = aArgs[1]; // reference to the selected LI element
                                                                            var oData = aArgs[2]; // object literal of selected item's result data

                                                                            // update hidden form field with the selected item's ID
                                                                            //myHiddenField.value = oData.id;
                                                                           // console.log('IN HERE myHandler');
                                                                            var bdoc = document.createElement('a');
                                                                            bdoc.setAttribute("onclick", "removeProv(this);");
                                                                            bdoc.appendChild(document.createTextNode(" -remove- "));

                                                                            var adoc = document.createElement('div');
                                                                            adoc.appendChild(document.createTextNode(oData.fname + " " + oData.lname));

                                                                            var idoc = document.createElement('input');
                                                                            idoc.setAttribute("type", "hidden");
                                                                            idoc.setAttribute("name","flagproviders");
                                                                            idoc.setAttribute("value",oData.id);
                                                                         //   console.log(oData.id);
                                                                         //   console.log(myAC);
                                                                         //   console.log(elLI);
                                                                         //   console.log(oData);
                                                                         //   console.log(aArgs);
                                                                         //   console.log(sType);
                                                                            adoc.appendChild(idoc);

                                                                            adoc.appendChild(bdoc);
                                                                            var providerList = $('providerList<%=curDocId%>');
                                                                        //    console.log('Now HERE'+providerList);
                                                                            providerList.appendChild(adoc);

                                                                            myAC.getInputEl().value = '';//;oData.fname + " " + oData.lname ;
                                                                        };
                                                                        oAC.itemSelectEvent.subscribe(myHandler);

                                                                        return {
                                                                            oDS: oDS,
                                                                            oAC: oAC
                                                                        };
                                                                    }();


                                                                    </script-->
        <script type="text/javascript">
              YAHOO.example.BasicRemote = function() {
                var url = "../provider/SearchProvider.do";
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

                // Instantiate the AutoComplete
                var oAC = new YAHOO.widget.AutoComplete("autocompleteprov<%=curDocId%>", "autocomplete_choicesprov<%=curDocId%>", oDS);
                oAC.queryMatchSubset = true;
                oAC.minQueryLength = 3;
                oAC.maxResultsDisplayed = 25;
                oAC.formatResult = resultFormatter3;
                //oAC.typeAhead = true;
                oAC.queryMatchContains = true;
                //oscarLog(oAC);
                //oscarLog(oAC.itemSelectEvent);
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
                    var providerList = $('providerList<%=curDocId%>');
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
                                                                    <div id="providerList<%=curDocId%>"></div>
                                                                </td>
                                                            </tr>



                                                            <tr>
                                                                <td><bean:message key="dms.documentReport.msgCreator"/>:</td>
                                                                <td><%=curdoc.getCreatorName()%></td>
                                                            </tr>

                                                            <tr>
                                                                <td colspan="2" align="right"><input id="save<%=curDocId%>" tabindex="<%=tabindex++%>" type="submit" name="save" value="Save"  disabled/></td>
                                                            </tr>
                                                        </table>

                                                    </form>
                                                </fieldset>

                                            </td>
                                        </tr>
                                    </table>
                                </div>
                                <%}%>
                                       
                        <%}  else {%>
                                <table>
                                    <tr>
                                        <td colspan="6">No documents to display</td>
                                    </tr>
                                </table>
                            <%}%>
</div>
