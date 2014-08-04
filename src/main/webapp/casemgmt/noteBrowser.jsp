<%--

    Copyright (c) 2012- Centre de Medecine Integree

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

    This software was written for
    Centre de Medecine Integree, Saint-Laurent, Quebec, Canada to be provided
    as part of the OSCAR McMaster EMR System

--%>

<%@page import="org.oscarehr.util.LoggedInInfo"%>
<%@page import="java.util.Hashtable"%>
<%@page import="oscar.util.UtilDateUtilities"%>
<%@page import="java.util.Collections"%>
<%@page import="oscar.MyDateFormat"%>
<%@page import="oscar.util.DateUtils"%>
<%@page import="java.util.ArrayList" %>

<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/rewrite-tag.tld" prefix="rewrite"%>
<%@ taglib uri="/WEB-INF/oscarProperties-tag.tld" prefix="oscarProp"%>
<%@ taglib uri="/WEB-INF/indivo-tag.tld" prefix="indivo"%>
<%@ taglib uri="/WEB-INF/oscar-tag.tld" prefix="oscar" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<jsp:useBean id="oscarVariables" class="java.util.Properties" scope="page" />

<%@page import="java.net.URLDecoder, java.net.URLEncoder,java.util.Date, java.util.List"%>
<%@page import="oscar.dms.EDocUtil,oscar.dms.EDoc"%>
<%@page import="org.oscarehr.casemgmt.web.NoteDisplay,org.oscarehr.casemgmt.web.NoteDisplayLocal"%>
<%@page import="org.oscarehr.util.SpringUtils"%>
<%@page import="org.oscarehr.casemgmt.service.CaseManagementManager,org.oscarehr.casemgmt.model.CaseManagementNote"%>
<%@page import="org.oscarehr.common.dao.CtlDocClassDao,org.oscarehr.common.dao.QueueDao" %>
<%@page import="org.springframework.web.context.WebApplicationContext"%>
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%
    if (session.getAttribute("userrole") == null) {
        response.sendRedirect("../logout.jsp");
    }

	LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);

    String roleName$ = (String) session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
    String demographicID = request.getParameter("demographic_no");
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_eChart"
	rights="r" reverse="<%=true%>">
            <bean:message key="oscarEncounter.noteBrowser.accessDenied"/>
<% response.sendRedirect("../noRights.html"); %>
</security:oscarSec>
<security:oscarSec roleName="<%=roleName$%>"
	objectName='<%="_eChart$"+demographicID%>' rights="o"
	reverse="<%=false%>">
<bean:message key="oscarEncounter.noteBrowser.accessDenied"/>
<% response.sendRedirect("../noRights.html"); %>
</security:oscarSec>

<%
    String errorMessage="";
//if delete request is made
    if (request.getParameter("delDocumentNo") != null && request.getParameter("delDocumentNo").length() > 0) {
        EDocUtil.deleteDocument(request.getParameter("delDocumentNo"));
    }

//if undelete request is made
    if (request.getParameter("undelDocumentNo") != null && request.getParameter("undelDocumentNo").length() > 0) {
        EDocUtil.undeleteDocument(request.getParameter("undelDocumentNo"));
    }

    if (request.getParameter("refileDocumentNo") != null && request.getParameter("refileDocumentNo").length() > 0) {
        try {
            EDocUtil.refileDocument(request.getParameter("refileDocumentNo"),request.getParameter("queueId"));
        } catch (Exception e) {
            errorMessage= e.getMessage();
        }
    }
    
    WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
    QueueDao queueDao = (QueueDao) ctx.getBean("queueDao");
    List<Hashtable> queues = queueDao.getQueues();
    int queueId=1;

    String viewstatus = request.getParameter("viewstatus");
    if (viewstatus == null) {
        viewstatus = "active";
    }
    String view = "all";
    if (request.getParameter("view") != null) {
        view = request.getParameter("view");
    }
    view = URLDecoder.decode(view,"UTF-8");

    String module="demographic"; 
     
    ArrayList doctypes = EDocUtil.getDoctypes("demographic");
    ArrayList<ArrayList<EDoc>> categories = new ArrayList<ArrayList<EDoc>>();
    ArrayList<EDoc> docs = new ArrayList<EDoc>();
    
    String sortorder = "";
    EDocUtil.EDocSort sort=null;
    if (request.getParameter("sortorder") != null && request.getParameter("sortorder").equals("Observation")) {
    	sort = EDocUtil.EDocSort.OBSERVATIONDATE;
        sortorder="Observation";
    } else  if (request.getParameter("sortorder") != null && request.getParameter("sortorder").equals("Update")) {
        sort = EDocUtil.EDocSort.DATE;
        sortorder="Update";
    } else {
    	sort = EDocUtil.EDocSort.CONTENTDATE;
        sortorder="Content";
    }
    docs = EDocUtil.listDocs(loggedInInfo, module, demographicID, view, EDocUtil.PRIVATE, sort, viewstatus);
    

%>

<html>
    <head>
        <title><bean:message key="oscarEncounter.noteBrowser.title"/> - <oscar:nameage demographicNo="<%=demographicID%>"/></title>
        <script type="text/javascript">  

            function popup(vheight,vwidth,varpage) { //open a new popup window
                var page = "" + varpage;
                windowprops = "height="+vheight+",width="+vwidth+",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes,screenX=0,screenY=0,top=0,left=0";
                var popup=window.open(page, "popup1", windowprops);
                if (popup != null) {
                    if (popup.opener == null) {
                        popup.opener = self;
                    }
                    popup.focus();
                }
            }
            function ReLoadDoc()
            {
                document.DisplayDoc.viewstatus.value=document.DisplayDoc.selviewstatus.options[document.DisplayDoc.selviewstatus.selectedIndex].value;
                document.DisplayDoc.sortorder.value=document.DisplayDoc.selsortorder.options[document.DisplayDoc.selsortorder.selectedIndex].value;
                document.DisplayDoc.submit();
            }

            function LoadView(viewstr)
            {
                document.DisplayDoc.view.value=viewstr;
                document.DisplayDoc.viewstatus.value=document.DisplayDoc.selviewstatus.options[document.DisplayDoc.selviewstatus.selectedIndex].value;
                document.DisplayDoc.submit();
            }
            function DeleteDoc()
            {
                document.DisplayDoc.delDocumentNo.value=docid;
                document.DisplayDoc.viewstatus.value=document.DisplayDoc.selviewstatus.options[document.DisplayDoc.selviewstatus.selectedIndex].value;
                document.DisplayDoc.submit();
            }
            
            function UnDeleteDoc()
            {
                document.DisplayDoc.undelDocumentNo.value=docid;
                document.DisplayDoc.viewstatus.value=document.DisplayDoc.selviewstatus.options[document.DisplayDoc.selviewstatus.selectedIndex].value;
                document.DisplayDoc.submit();
            }

            function RefileDoc()
            {
                document.DisplayDoc.refileDocumentNo.value=docid;
                document.DisplayDoc.viewstatus.value=document.DisplayDoc.selviewstatus.options[document.DisplayDoc.selviewstatus.selectedIndex].value;
                document.DisplayDoc.submit();
            }

            function setQueue(){
                document.DisplayDoc.queueId.value=document.getElementById('queueList').options[document.getElementById('queueList').selectedIndex].value;
            }

            function getWidth() {
                var myWidth = 0;
                if( typeof( window.innerWidth ) == 'number' ) {
                    //Non-IE
                    myWidth = window.innerWidth;
                } else if( document.documentElement &&  document.documentElement.clientWidth  ) {
                    //IE 6+ in 'standards compliant mode'
                    myWidth = document.documentElement.clientWidth;
                } else if( document.body && document.body.clientHeight  ) {
                    //IE 4 compatible
                    myWidth = document.body.clientWidth;
                }
                return myWidth;
            }
   
          
            function getHeight() {
                var myHeight = 0;
                if( typeof( window.innerHeight ) == 'number' ) {
                    //Non-IE                    
                    myHeight = window.innerHeight;
                } else if( document.documentElement && document.documentElement.clientHeight  ) {
                    //IE 6+ in 'standards compliant mode'                    
                    myHeight = document.documentElement.clientHeight;
                } else if( document.body && (document.body.clientHeight ) ) {
                    //IE 4 compatible                    
                    myHeight = document.body.clientHeight;
                }
                return myHeight;
            }
            
                     
            showPageImg=function(curdocid,doctype){
                if(curdocid!="0") {
                    var url2='<%=request.getContextPath()%>'+'/dms/ManageDocument.do?method=display&doc_no='
                        +curdocid;
                    document.getElementById('docdisp').innerHTML = '<iframe	src="' +url2 +'"  width="' +(getWidth()-40) +'" height="' +(getHeight()-50) +'"></iframe>';
                            
                    var url4='<%=request.getContextPath()%>'+'/dms/ManageDocument.do?method=viewDocumentDescription&doc_no='+curdocid;
                    document.getElementById('docextrainfo').innerHTML = '<object data="' +url4 +'"  height=250px width="100%" type="text/html" ></object>';
                    
                    var url5='<%=request.getContextPath()%>'+'/dms/ManageDocument.do?method=viewAnnotationAcknowledgementTickler&doc_no='+curdocid;
                    document.getElementById('docinfo').innerHTML = '<object data="' +url5 +'"  height=100px width="100%" type="text/html" ></object>';                    
                      
                    document.getElementById('printnotesbutton').style.visibility = 'hidden'; 
                } else  
                {
                    document.getElementById('docdisp').innerHTML='';
                    document.getElementById('docinfo').innerHTML='';
                    document.getElementById('docextrainfo').innerHTML='';
                }
                                                  
            }
            showPageCombineImg=function(doclist){
                
                var url2='<%=request.getContextPath()%>'+'/dms/combinePDFs.do?ContentDisposition=inline'+doclist;
                document.getElementById('docdisp').innerHTML = '<object	data="' +url2 +'" type="application/pdf" width="' +(getWidth()-40) +'" height="' +(getHeight()-50) +'"></object>';
                document.getElementById('docinfo').innerHTML='';
                document.getElementById('docextrainfo').innerHTML='';
                document.getElementById('printnotesbutton').style.visibility = 'hidden'; 
                                                  
            }
            function showEncounter(encList){
                 var url2='<%=request.getContextPath()%>'+'/CaseManagementEntry.do?method=displayNotes&demographicNo=<%=demographicID%>'+encList+'&printCPP=false&printRx=false';
                 document.getElementById('docdisp').innerHTML = '<object data="' +url2 +'"  width="' +(getWidth()-40) +'" height="' +(getHeight()-300) +'" type="text/html"></object>';
                 document.getElementById('docinfo').innerHTML='';
                 document.getElementById('docextrainfo').innerHTML = '';
                 document.getElementById('printnotesbutton').style.visibility = 'visible'; 
            }
            
            function getSelected(opt) {
                var selected = new Array();
                var index = 0;
                for (var intLoop=0; intLoop < opt.length; intLoop++) {
                    if (opt[intLoop].selected) {
                        index = selected.length;
                        selected[index] = new Object;
                        selected[index].value = opt[intLoop].value;
                        selected[index].title = opt[intLoop].title;
                        selected[index].index = intLoop;
                    }
                }
                return selected;
            }
            
            function getDoc()
            {
                var th = document.getElementById('doclist');
                var selected = new Array();
                selected=getSelected(th);
   
                if(selected.length==0) {
              
                    var div_ref = document.all("docbuttons");
                    div_ref.style.visibility = "hidden";
                    docid="0";                    
                    showPageImg(docid,"");
           
                }  else
                {
                    var th1 = document.getElementById('encounterlist');
                    th1.selectedIndex=-1;
                }
                if(selected.length>=2)
                {
                    var div_ref = document.all("docbuttons");
                    div_ref.style.visibility = "hidden";
                        
                    var docList='';
                    var combinePdf=true;
                    for(k=0;k<selected.length;k++) {
                        var docnoindexend=selected[k].value.indexOf('-');
                    
              
                        var docno=selected[k].value.substring(0,docnoindexend);
                        var doctype=selected[k].value.substring(docnoindexend+1,selected[k].value.length);
                        if(doctype=="text/html") combinePdf=false;
                        docList=docList+'&docNo='+docno;
                    }
                    
                    if(combinePdf==true) {
                        showPageCombineImg(docList);
                    }
                    else
                    {
                        alert("<bean:message key="oscarEncounter.noteBrowser.msgOnlyPDFCanBeCombined"/>");
                        setdefaultdoc();
                    }
              
                }
                else if(selected.length==1)
                {
                    var docidindexend=selected[0].value.indexOf('-');
                    docid=selected[0].value.substring(0,docidindexend);
                    doctype=selected[0].value.substring(docidindexend+1,selected[0].value.length);
                    
                    showPageImg(docid,doctype);
                    var div_ref = document.all("docbuttons");                    
                    div_ref.style.visibility = "visible";
                    if(doctype=="text/html") {
                        var div_ref = document.all("refilebutton");
                        div_ref.style.visibility = "hidden";
                    }
                    else
                    {
                            var div_ref = document.all("refilebutton");
                            div_ref.style.visibility = "visible";
                    }
                }
            }

            function setdefaultencounter()
            {
                var enclistObj = document.getElementById('encounterlist');
                if(enclistObj.length>=1)
                {
                    enclistObj.selectedIndex=0;
                    enclistObj.focus();
                    
                    getEncounter();
                    enclistObj.focus();
                }
            }
            
            function setdefaultdoc()
            {
    
                var doclistObj = document.getElementById('doclist');
                if(doclistObj.length>=1)
                {
                    doclistObj.selectedIndex=0;
                    doclistObj.focus();

                    getDoc();
                    doclistObj.focus();
                }
                else if(doclistObj.length==0)
                {
                    div_ref = document.all("docbuttons");
                    div_ref.style.visibility = "hidden";
                        
                }

            }
            
            function getEncounter()
            {
                var th = document.getElementById('encounterlist');
                var selected = new Array();
                selected=getSelected(th);
                
                var encList='';
                if(selected.length>=1) {
                    var th1 = document.getElementById('doclist');
                    th1.selectedIndex=-1;
                    getDoc();

                    for(k=0;k<selected.length;k++) {

                    if(encList=='') {encList='&notes2print='+selected[k].value;} else {
                    encList=encList+','+selected[k].value;}
                    }
                }
                showEncounter(encList);
            }
            
            function PrintEncounter()
            {
    
                var th = document.getElementById('encounterlist');
                var selected = new Array();
                selected=getSelected(th);
                var encList='';
                if(selected.length>=1) {
                    var th1 = document.getElementById('doclist');
                    th1.selectedIndex=-1;
                    getDoc();

                    for(k=0;k<selected.length;k++) {
                        if(encList=='') {encList='&notes2print='+selected[k].value;} else {
                        encList=encList+','+selected[k].value;}
                    }
             
                    popup(700,960,'<%=request.getContextPath()%>'+'/CaseManagementEntry.do?method=print&demographicNo=<%=demographicID%>'+encList+'&printCPP=false&printRx=false','PrintEncounter');          
                }
            }

            function AddTickler()
            {
                popup(450,600,'../tickler/ForwardDemographicTickler.do?docType=DOC&docId='+docid+'&demographic_no=<%=demographicID%>','tickler');                                            
            } 
            function DocAnnotation()
            {
                popup(350,500,'../annotation/annotation.jsp?display=Documents&table_id='+docid+'&demo=<%=demographicID%>','anwin');                                                
            }
  
            function DocEdit()
            {
                var th = document.getElementById('doclist');
                var selected = new Array();
                selected=getSelected(th);
                var docidindexend=selected[0].value.indexOf('-');
                docid=selected[0].value.substring(0,docidindexend);
                var doctype=selected[0].value.substring(docidindexend+1,selected[0].value.length);
                
                if (doctype== 'text/html') { 
                    popup(450,600,'../dms/addedithtmldocument.jsp?editDocumentNo='+docid+'&function=<%=module%>&functionid=<%=demographicID%>','EditDoc');                                               
                }
                else {
                            
                    popup(350,500,'../dms/editDocument.jsp?editDocumentNo='+docid+'&function=<%=module%>&functionid=<%=demographicID%>','EditDoc');                                               
                }                                        
            }

            function getParameter(paramName) {
                var searchString = window.location.search.substring(1);
                var i,val;
                var params = searchString.split("&");

                for (i=0;i<params.length;i++) {
                    val = params[i].split("=");
                    if (val[0] == paramName) {
                    return val[1];
                    }
                }
                return null;
            }
            
           function OnLoad()
           {
                
                var FirstTime=getParameter("FirstTime");
                if(FirstTime!=null && FirstTime=="1") {
                    window.resizeTo(screen.width-1045,screen.height-40);
                    window.moveTo(1045,0);
                }
                setdefaultencounter();
           }
        </script>
    </head>
    <body onload="OnLoad();" >
        <form name="DisplayDoc" method="post" action="noteBrowser.jsp">

            <table>
                <%if (errorMessage.length() > 0) {%><tr><td><b><font color="red"><%=errorMessage%></font></b></td></tr><%}%>
                <tr><td  align="left" valign="top" width="50%">
                        <oscar:nameage demographicNo="<%=demographicID%>"/><br>                     

                        <input type="hidden" name="viewstatus" value="<%=viewstatus%>">
                        <input type="hidden" name="sortorder" value="<%=sortorder%>">

                        <bean:message key="oscarEncounter.noteBrowser.msgViewStatus"/> <select id="selviewstatus" name="selviewstatus" onchange="ReLoadDoc()">
                            <option value="all"
                                    <%=viewstatus.equalsIgnoreCase("all") ? "selected" : ""%>><bean:message key="oscarEncounter.noteBrowser.msgAll"/></option>
                            <option value="deleted"
                                    <%=viewstatus.equalsIgnoreCase("deleted") ? "selected" : ""%>><bean:message key="oscarEncounter.noteBrowser.msgDeleted"/></option>
                            <option value="active"
                                    <%=viewstatus.equalsIgnoreCase("active") ? "selected" : ""%>><bean:message key="oscarEncounter.noteBrowser.msgPublished"/></option>
                        </select>

                        <bean:message key="oscarEncounter.noteBrowser.msgSortDate"/>
                        <select id="selsortorder" name="selsortorder" onchange="ReLoadDoc()">
                            <option value="Content"
                                    <%=sortorder.equalsIgnoreCase("Content") ? "selected":""%>><bean:message key="oscarEncounter.noteBrowser.msgContent"/></option>
                            <option value="Observation"
                                    <%=sortorder.equalsIgnoreCase("Observation") ? "selected":""%>><bean:message key="oscarEncounter.noteBrowser.msgObservation"/></option>
                            <option value="Update"
                                    <%=sortorder.equalsIgnoreCase("Update") ? "selected":""%>><bean:message key="oscarEncounter.noteBrowser.msgUpdate"/></option>
				
			</select>
                        <fieldset><legend><bean:message key="oscarEncounter.noteBrowser.msgView"/>:</legend>      
                            <input type="hidden" name="view" value="<%=view%>">
                            <input type="hidden" name="demographic_no" value="<%=demographicID%>">
                            <input type="hidden" name="undelDocumentNo" value="">
                            <input type="hidden" name="delDocumentNo" value="">
                            <input type="hidden" name="refileDocumentNo" value="">
                            <input type="hidden" name="queueId" value="<%=queueId%>">

                            <a
                                href="#" onclick="LoadView('all')" ><%=view.equals("all") ? "<b>":""%>All<%=view.equals("all") ? "</b>":""%></a> <% for (int i3 = 0; i3 < doctypes.size(); i3++) {%>
                            | <a
                                href="#" onclick="LoadView('<%=URLEncoder.encode((String) doctypes.get(i3),"UTF-8")%>')"><%=view.equals(doctypes.get(i3)) ? "<b>":""%><%=(String) doctypes.get(i3)%><%=view.equals(doctypes.get(i3)) ? "</b>":""%></a>
                            <%}%> 
                        </fieldset>
                        <div id="docbuttons">
                                <% if (viewstatus.equalsIgnoreCase("active")) {%>
                                <% if (module.equalsIgnoreCase("demographic")) {%>
                                <input type="button" value="<bean:message key="oscarEncounter.noteBrowser.msgAddTickler"/>" onclick="AddTickler();" > <%}%>
                                <input type="button" value="<bean:message key="oscarEncounter.noteBrowser.msgAnnotate"/>" onclick="DocAnnotation()" >
                                <input type="button" value="<bean:message key="oscarEncounter.noteBrowser.msgEdit"/>" onclick="DocEdit();" >
                                <input type="button" value="<bean:message key="oscarEncounter.noteBrowser.msgDelete"/>" onclick="DeleteDoc();" >
                                <div id="refilebutton">
                                    <input type="button" value="<bean:message key="oscarEncounter.noteBrowser.msgRefile"/>" onclick="RefileDoc();" >
                                    <select  id="queueList" name="queueList" onchange="setQueue();">
                                        <%
                                            for (Hashtable ht : queues) {
                                                int id = (Integer) ht.get("id");
                                                String qName = (String) ht.get("queue");
                                        %>
                                        <option value="<%=id%>" <%=((id == queueId) ? " selected" : "")%>><%= qName%> </option>
                                    <%}%>
                                    </select>
                                </div>
                                <%} else if (viewstatus.equalsIgnoreCase("deleted")) {%>
                                <input type="button" value="<bean:message key="oscarEncounter.noteBrowser.msgUndelete"/>" onclick="UnDeleteDoc();" >   
                                <%}%>
                        </div>
                            
                            
                        <div id="docinfo"></div>
                        <div id="printnotesbutton"><input type='image' src="../oscarEncounter/graphics/document-print.png" onclick="PrintEncounter();" title='<bean:message key="oscarEncounter.Index.btnPrint"/>' id="imgPrintEncounter"></div>  
                    </td><td valign="top">
                        <fieldset><legend><%
                        if(sortorder.equals("Content")) { %>
                        <bean:message key="oscarEncounter.noteBrowser.msgContent"/><%} else {%>
                        <bean:message key="oscarEncounter.noteBrowser.msgUpdate"/> <%}%>
                        <bean:message key="oscarEncounter.noteBrowser.ObservationTypeDescription"/></legend>
                            <SELECT MULTIPLE SIZE=5 id="doclist" onchange="getDoc();">
                                <%
                                    for (int i2 = 0; i2 < docs.size(); i2++) {
                                        EDoc cmicurdoc = docs.get(i2);
                                %>
                                <option VALUE="<%=cmicurdoc.getDocId()%>-<%=cmicurdoc.getContentType()%>" title="<%=cmicurdoc.getDescription()%>"><%=sortorder.equals("Content")?UtilDateUtilities.DateToString(cmicurdoc.getContentDateTime(),"yyyy-MM-dd"):cmicurdoc.getDateTimeStamp()%>&nbsp;&nbsp; <%=cmicurdoc.getObservationDate()%> [<%=cmicurdoc.getType()%>] <%=(cmicurdoc.getDescription().length()<30?cmicurdoc.getDescription():cmicurdoc.getDescription().substring(0,30)+"...")%>
                                </option> <%}%>
                            </SELECT>
                        </fieldset>
                            
                        <fieldset><legend><bean:message key="oscarEncounter.noteBrowser.encounterNote"/> </legend>
                            <select MULTIPLE SIZE=5 id="encounterlist" onchange="getEncounter();">
                            <% 
                                CaseManagementManager caseManagementManager=(CaseManagementManager)SpringUtils.getBean("caseManagementManager");
                                List<CaseManagementNote> notes =  caseManagementManager.getNotes(demographicID);
               
                                ArrayList<NoteDisplay> notesToDisplay = new ArrayList<NoteDisplay>();
                                for (CaseManagementNote noteTemp : notes)
                                    notesToDisplay.add(new NoteDisplayLocal(loggedInInfo, noteTemp));
                                Collections.sort(notesToDisplay, NoteDisplay.noteObservationDateComparator);
                                int noteSize = notes.size();
                                int idx=0;
                                for (idx = 0; idx < noteSize; ++idx)
                                { 
                                    NoteDisplay curNote = notesToDisplay.get(idx); 
                                    if (!(curNote.isDocument()) && !(curNote.isEformData()) && !(curNote.isRxAnnotation()) && !(curNote.isCpp())) {
                            %>
                                <option value="<%=curNote.getNoteId()%>"><%=DateUtils.getDate(MyDateFormat.getCalendar(curNote.getObservationDate()).getTime(), "yyyy-MM-dd  HH:mm ", request.getLocale())%> <%=curNote.getProviderName()%></option>
                            <%          
                                    }
                                }  
                        %>                   
                            </select>
                        </fieldset>
                    </td>
                </tr>
            </table>

            <table><tr><td ><div id="docdisp"></div></td></tr>
                   <tr><td><div id="docextrainfo"></div></td></tr>
            </table>    
        </form>                        
    </body>
</html>
