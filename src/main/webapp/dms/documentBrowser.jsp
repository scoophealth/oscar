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

<%@ page import="java.util.*" %>

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
<%@page import="oscar.util.UtilDateUtilities"%>


<%

    if (session.getValue("user") == null) {
        response.sendRedirect("../logout.jsp");
    }
    if (session.getAttribute("userrole") == null) {
        response.sendRedirect("../logout.jsp");
    }

    String roleName$ = (String) session.getAttribute("userrole") + "," + (String) session.getAttribute("user");

    String demographicID = request.getParameter("demographicID");
    String categoryKey = request.getParameter("categorykey");

//if delete request is made
    if (request.getParameter("delDocumentNo") != null && request.getParameter("delDocumentNo").length() > 0) {
        EDocUtil.deleteDocument(request.getParameter("delDocumentNo"));
    }

//if undelete request is made
    if (request.getParameter("undelDocumentNo") != null && request.getParameter("undelDocumentNo").length() > 0) {
        EDocUtil.undeleteDocument(request.getParameter("undelDocumentNo"));
    }

    String viewstatus = request.getParameter("viewstatus");
    if (viewstatus == null) {
        viewstatus = "active";
    }
    String view = "all";
    if (request.getParameter("view") != null) {
        view = (String) request.getParameter("view");
    } else if (request.getAttribute("view") != null) {
        view = (String) request.getAttribute("view");
    }
    view = URLDecoder.decode(view);
    String module = "";

    String moduleid = "";
    if (request.getParameter("function") != null) {
        module = request.getParameter("function");
        moduleid = request.getParameter("functionid");
    } else if (request.getAttribute("function") != null) {
        module = (String) request.getAttribute("function");
        moduleid = (String) request.getAttribute("functionid");
    }
    String winwidth="";
    String winheight="";
    if (request.getParameter("winwidth") != null) {
        winwidth = request.getParameter("winwidth");
    }
    
    if (request.getParameter("winheight") != null) {
        winheight = request.getParameter("winheight");
    }
    
    if (!"".equalsIgnoreCase(moduleid) && (demographicID == null || demographicID.equalsIgnoreCase("null"))) {
        demographicID = moduleid;
    }
    ArrayList doctypes = EDocUtil.getDoctypes(module);





    ArrayList<ArrayList<EDoc>> categories = new ArrayList<ArrayList<EDoc>>();
    ArrayList<EDoc> docs = new ArrayList<EDoc>();
    
    String sortorder = "";
    EDocUtil.EDocSort sort=null;
    if (request.getParameter("sortorder") != null && request.getParameter("sortorder").equals("Observation")) {
    	sort = EDocUtil.EDocSort.OBSERVATIONDATE;
        sortorder="Observation";
    }
    else if (request.getParameter("sortorder") != null && request.getParameter("sortorder").equals("Upload")) {
    	sort = EDocUtil.EDocSort.DATE;
        sortorder="Upload";
    }
    else  {
    	sort = EDocUtil.EDocSort.CONTENTDATE;
        sortorder="Content";
    }
   
    if (categoryKey.indexOf("Private") >= 0) {
        docs = EDocUtil.listDocs(module, moduleid, view, EDocUtil.PRIVATE, sort, viewstatus);

    } else if (categoryKey.indexOf("Public") >= 0) {
        docs = EDocUtil.listDocs(module, moduleid, view, EDocUtil.PUBLIC, sort, viewstatus);

    } else {%>
Remote documents not supported
<%}

%>

<html>
    <head>
        <title><bean:message key="dms.documentBrowser.title"/></title>

        <script type="text/javascript">  
            window.moveTo(0,0);

            function popup(vheight,vwidth,varpage) { //open a new popup window
                var page = "" + varpage;
                windowprops = "height="+vheight+",width="+vwidth+",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes,screenX=0,screenY=0,top=0,left=0";//360,680
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
                document.DisplayDoc.winwidth.value=getWidth();
                document.DisplayDoc.winheight.value=getHeight();
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
                    document.getElementById('docdisp').innerHTML = '<iframe	src="' +url2 +'"  width="' +(getWidth()-330) +'" height="' +(getHeight()-50) +'"></iframe>';
                            
                    var url4='<%=request.getContextPath()%>'+'/dms/ManageDocument.do?method=viewDocumentInfo&doc_no='+curdocid;
                    document.getElementById('docextrainfo').innerHTML = '<object data="' +url4 +'"  height=250px width="100%" type="text/html" ></object>';                    
                      
                    
                } else  
                {
                    document.getElementById('docdisp').innerHTML='';
                    document.getElementById('docextrainfo').innerHTML='';
                    
                }
                                                  
            }
            showPageCombineImg=function(doclist){
                
                var url2='<%=request.getContextPath()%>'+'/dms/combinePDFs.do?ContentDisposition=inline'+doclist;
                document.getElementById('docdisp').innerHTML = '<object	data="' +url2 +'" type="application/pdf" width="' +(getWidth()-330) +'" height="' +(getHeight()-50) +'"></object>';                    
                document.getElementById('docextrainfo').innerHTML='';
                                                  
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
                        alert("<bean:message key="dms.documentBrowser.msgOnlyPDFCanBeCombined"/>");
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
                    popup(450,600,'addedithtmldocument.jsp?editDocumentNo='+docid+'&function=<%=module%>&functionid=<%=demographicID%>','EditDoc');                                               
                }
                else {
                            
                    popup(350,500,'editDocument.jsp?editDocumentNo='+docid+'&function=<%=module%>&functionid=<%=demographicID%>','EditDoc');                                               
                }                                        
            }

        </script>
    </head>
    <body onload="window.innerWidth=<%=winwidth.length()>0?winwidth:"screen.availWidth*0.9"%>;window.innerHeight=<%=winheight.length()>0?winheight:"screen.availHeight*0.9"%>;" >
        <form name="DisplayDoc" method="post" action="documentBrowser.jsp">

            <table>
                <tr><td  align="left" valign="top" style="width: 400px" >
                        <oscar:nameage demographicNo="<%=moduleid%>"/> &nbsp; <oscar:phrverification demographicNo="<%=moduleid%>"><bean:message key="phr.verification.link"/></oscar:phrverification><br>
                        <%=categoryKey%>
                        <br>                     
                             
                        <input type="hidden" name="viewstatus" value="<%=viewstatus%>">
                        <input type="hidden" name="sortorder" value="<%=sortorder%>">
                        <input type="hidden" name="function" value="<%=module%>">
                        <input type="hidden" name="functionid" value="<%=moduleid%>">
                        <input type="hidden" name="categorykey" value="<%=categoryKey%>">

                        <bean:message key="dms.documentBrowser.msgViewStatus"/> <select id="selviewstatus" name="selviewstatus"
                                style="text-size: 8px; margin-bottom: -4px;"
                                onchange="ReLoadDoc()">
                            <option value="all"
                                    <%=viewstatus.equalsIgnoreCase("all") ? "selected" : ""%>><bean:message key="dms.documentBrowser.msgAll"/></option>
                            <option value="deleted"
                                    <%=viewstatus.equalsIgnoreCase("deleted") ? "selected" : ""%>><bean:message key="dms.documentBrowser.msgDeleted"/></option>
                            <option value="active"
                                    <%=viewstatus.equalsIgnoreCase("active") ? "selected" : ""%>><bean:message key="dms.documentBrowser.msgPublished"/></option>
                        </select>

                        <bean:message key="dms.documentBrowser.msgSortDate"/>
                        <select id="selsortorder" name="selsortorder"
				style="text-size: 8px; margin-bottom: -4px;" onchange="ReLoadDoc()">
                            <option value="Content"
                                    <%=sortorder.equalsIgnoreCase("Content") ? "selected":""%>><bean:message key="dms.documentBrowser.msgContent"/></option>
                            <option value="Observation"
                                    <%=sortorder.equalsIgnoreCase("Observation") ? "selected":""%>><bean:message key="dms.documentBrowser.msgObservation"/></option>
                            <option value="Upload"
                                    <%=sortorder.equalsIgnoreCase("Upload") ? "selected":""%>><bean:message key="dms.documentBrowser.msgUpload"/></option>
				
			</select>
                        <fieldset><legend><bean:message key="dms.documentBrowser.msgView"/>:</legend>      
                            <input type="hidden" name="view" value="<%=view%>">
                            <input type="hidden" name="demographic_no" value="<%=demographicID%>">
                            <input type="hidden" name="undelDocumentNo" value="">
                            <input type="hidden" name="delDocumentNo" value="">

                            <a
                                href="#" onclick="LoadView('all')" ><%=view.equals("all") ? "<b>":""%>All<%=view.equals("all") ? "</b>":""%></a> <% for (int i3 = 0; i3 < doctypes.size(); i3++) {%>
                            | <a
                                href="#" onclick="LoadView('<%=URLEncoder.encode((String) doctypes.get(i3))%>')"><%=view.equals(URLEncoder.encode((String) doctypes.get(i3))) ? "<b>":""%><%=(String) doctypes.get(i3)%><%=view.equals(URLEncoder.encode((String) doctypes.get(i3))) ? "</b>":""%></a>
                            <%}%> 
                        </fieldset>

                        <fieldset>
                            <legend><%
                        if(sortorder.equals("Content")) { %>
                        <bean:message key="dms.documentBrowser.msgContent"/><%} else {%>
                        <bean:message key="dms.documentBrowser.msgUpload"/> <%}%>
                        <bean:message key="dms.documentBrowser.ObservationTypeDescription"/></legend>                            
                            <SELECT MULTIPLE SIZE=15 id="doclist" onchange="getDoc();">
                                <%
                                    for (int i2 = 0; i2 < docs.size(); i2++) {
                                        EDoc cmicurdoc = (EDoc) docs.get(i2);
                                %>
                                <option VALUE="<%=cmicurdoc.getDocId()%>-<%=cmicurdoc.getContentType()%>"><%=sortorder.equals("Content")?UtilDateUtilities.DateToString(cmicurdoc.getContentDateTime(),"yyyy-MM-dd"):cmicurdoc.getDateTimeStamp()%>&nbsp;&nbsp; <%=cmicurdoc.getObservationDate()%> [<%=cmicurdoc.getType()%>] <%=cmicurdoc.getDescription()%>
                                </option> <%}%>
                            </SELECT>
                        </fieldset>
                            <div id="docbuttons">
                                <% if (viewstatus.equalsIgnoreCase("active")) {%>
                                <% if (module.equalsIgnoreCase("demographic")) {%>
                                <input type="button" value="Add Tickler" onclick="AddTickler();" > <%}%>
                                <input type="button" value="Annotate" onclick="DocAnnotation()" >
                                <input type="button" value="Edit" onclick="DocEdit();" >                        
                                <input type="button" value="Delete" onclick="DeleteDoc();" > <%} else if (viewstatus.equalsIgnoreCase("deleted")) {%>
                                <input type="button" value="UnDelete" onclick="UnDeleteDoc();" >   
                                <%}%>
                            </div>
                        <fieldset>
                                <div id="docextrainfo"></div>
                        </fieldset>

                    </td>

                    <td valign="top"> 

                        <table><tr><td> 
                                    <div id="docdisp"></div>                  
                                </td></tr></table>    

                    </td>
                </tr>
               
            </table>
            <input type="hidden" name="winwidth" value="">
            <input type="hidden" name="winheight" value="">

            <script type="text/javascript">
                setdefaultdoc();
            </script>

        </form>                        
    </body>
</html>

