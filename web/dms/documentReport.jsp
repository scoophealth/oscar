<!--  
/*
 * 
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
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
 * This software was written for the 
 * Department of Family Medicine 
 * McMaster Unviersity test2
 * Hamilton 
 * Ontario, Canada 
 */
-->

<%
if(session.getValue("user") == null) response.sendRedirect("../logout.htm");
String user_no = (String) session.getAttribute("user");
String userfirstname = (String) session.getAttribute("userfirstname");
String userlastname = (String) session.getAttribute("userlastname");
%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/rewrite-tag.tld" prefix="rewrite" %>
<%@ taglib uri="/WEB-INF/oscarProperties-tag.tld" prefix="oscarProp" %>
<%@ taglib uri="/WEB-INF/indivo-tag.tld" prefix="indivo" %>

<jsp:useBean id="oscarVariables" class="java.util.Properties" scope="page" />
<%@ page import="java.math.*, java.util.*, java.io.*, java.sql.*, oscar.*, oscar.util.*, java.net.*,oscar.MyDateFormat, oscar.dms.*, oscar.dms.data.*, oscar.oscarProvider.data.ProviderMyOscarIdData, oscar.oscarDemographic.data.DemographicData" %>
<%@ page import="org.apache.commons.lang.StringEscapeUtils" %>

<%
for( Enumeration e = request.getParameterNames(); e.hasMoreElements(); ) {
    String name = (String)e.nextElement();
    System.out.println(name + " -> " + request.getParameter(name));
}
    
//if delete request is made
if (request.getParameter("delDocumentNo") != null) {
    EDocUtil.deleteDocument(request.getParameter("delDocumentNo"));
}

//view  - tabs
String view = "all";
if (request.getParameter("view") != null) {
    view = (String) request.getParameter("view");
} else if (request.getAttribute("view") != null) {
    view = (String) request.getAttribute("view");
}
//preliminary JSP code

// "Module" and "function" is the same thing (old dms module)
String module = "";
String moduleid = "";
if (request.getParameter("function") != null) {
    module = request.getParameter("function");
    moduleid = request.getParameter("functionid");
} else if (request.getAttribute("function") != null) {
    module = (String) request.getAttribute("function");
    moduleid = (String) request.getAttribute("functionid");
}
String moduleName = EDocUtil.getModuleName(module, moduleid);

String curUser = "";
if (request.getParameter("curUser") != null) {
    curUser = request.getParameter("curUser");    
} else if (request.getAttribute("curUser") != null) {
    curUser = (String) request.getAttribute("curUser");    
}

//sorting
String sort = EDocUtil.SORT_OBSERVATIONDATE;
String sortRequest = request.getParameter("sort");
if (sortRequest != null) {
    if (sortRequest.equals("description")) sort = EDocUtil.SORT_DESCRIPTION;
    else if (sortRequest.equals("type")) sort = EDocUtil.SORT_DOCTYPE;
    else if (sortRequest.equals("contenttype")) sort = EDocUtil.SORT_CONTENTTYPE;
    else if (sortRequest.equals("creator")) sort = EDocUtil.SORT_CREATOR;
    else if (sortRequest.equals("uploaddate")) sort = EDocUtil.SORT_DATE;
    else if (sortRequest.equals("observationdate")) sort = EDocUtil.SORT_OBSERVATIONDATE;
}

ArrayList doctypes = EDocUtil.getDoctypes(module);

//Retrieve encounter id for updating encounter navbar if info this page changes anything
String parentAjaxId;
if( request.getParameter("parentAjaxId") != null )
    parentAjaxId = request.getParameter("parentAjaxId");
else if( request.getAttribute("parentAjaxId") != null )
    parentAjaxId = (String)request.getAttribute("parentAjaxId");
else
    parentAjaxId = "";


String updateParent;
if( request.getParameter("updateParent") != null )
    updateParent = request.getParameter("updateParent");
else
    updateParent = "false";
%>
<html:html locale="true">
<head>
<title><bean:message key="dms.documentReport.title"/></title>
<meta http-equiv="Expires" content="Monday, 8 Aug 88 18:18:18 GMT">
<meta http-equiv="Cache-Control" content="no-cache">

<link rel="stylesheet" type="text/css" href="../share/css/OscarStandardLayout.css" />
<script type="text/javascript" src="../share/javascript/Oscar.js"></script>
<script type="text/javascript" src="../share/javascript/prototype.js"></script>

<link rel="stylesheet" type="text/css" href="../share/css/niftyCorners.css" />
<link rel="stylesheet" type="text/css" href="dms.css"/>
<link rel="stylesheet" type="text/css" href="../share/css/niftyPrint.css" media="print" />
<script type="text/javascript" src="../share/javascript/nifty.js"></script>
<script type="text/javascript">
window.onload=function(){
if(!NiftyCheck())
    return;

Rounded("div.doclist","top","transparent", "#ccccd7", "small border #ccccd7");
Rounded("div.doclist","bottom","transparent", "#e0ecff", "small border #ccccd7");
Rounded("div.leftplane","top", "transparent", "#CCCCFF","small border #ccccff");
Rounded("div.leftplane","bottom","transparent","#EEEEFF","small border #ccccff");
onloadfunction();
setup();  //reload parent content if necessary
}


var awnd=null;
function popPage(url) {
	awnd=rs('',url ,400,200,1);
	awnd.focus();
}


function checkDelete(url, docDescription){
// revision Apr 05 2004 - we now allow anyone to delete documents
	if(confirm("<bean:message key="dms.documentReport.msgDelete"/> " + docDescription)) {
		window.location = url;
	}
}

function showhide(hideelement, button) {
    var plus = "+";
    var minus = "--";
    if (document.getElementById) { // DOM3 = IE5, NS6
        if (document.getElementById(hideelement).style.display == 'none') {
              document.getElementById(hideelement).style.display = 'block';
              document.getElementById(button).innerHTML = document.getElementById(button).innerHTML.replace(plus, minus);
        }
        else {
              document.getElementById(hideelement).style.display = 'none';
              document.getElementById(button).innerHTML = document.getElementById(button).innerHTML.replace(minus, plus);;
        }
    }
}


function checkAll(checkboxId,parentEle, className){
   var f = document.getElementById(checkboxId);
   var val = f.checked;
   var chkList = document.getElementsByClassName(className, parentEle);
   for (i =0; i < chkList.length; i++){
      chkList[i].checked = val;
   }
}

function submitForm(actionPath) {
    
    var form = document.forms[2];
    if(verifyChecks(form)) {
        form.action = actionPath;
        form.submit();
        return true;
    }
    else
        return false;
}

function verifyChecks(t){
   
   if ( t.docNo == null ){ 
         alert("No documents selected");
         return false;
   }else{
      var oneChecked = 0;
      if( t.docNo.length ) {
        for ( i=0; i < t.docNo.length; i++){
            if(t.docNo[i].checked){
                ++oneChecked;
                break;
            }
        }
      }
      else 
        oneChecked = t.docNo.checked ? 1 : 0;
        
      if ( oneChecked == 0 ){
         alert("No documents selected");
         return false;            
      }
   }
   return true;
}

function popup1(height, width, url, windowName){   
  var page = url;  
  windowprops = "height="+height+",width="+width+",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes,screenX=0,screenY=0,top=0,left=0";  
  var popup=window.open(url, windowName, windowprops);  
  if (popup != null){  
    if (popup.opener == null){  
      popup.opener = self;  
    }  
  }  
  popup.focus();  
  
}


  function setup() {
    var update = "<%=updateParent%>";
    var parentId = "<%=parentAjaxId%>";
    var Url = window.opener.URLs;
    
    if( update == "true" && !window.opener.closed )
        window.opener.popLeftColumn(Url[parentId], parentId, parentId);
  }
</script>

<script src="../share/javascript/prototype.js" type="text/javascript"></script>

<body class="bodystyle">
   
  <table class="MainTable" id="scrollNumber1" name="encounterTable" style="margin: 0px;">
      <tr class="MainTableRowTop">
          <td class="MainTableTopRowLeftColumn" width="60px">
             eDocs
          </td>
          <td class="MainTableTopRowRightColumn">
              <table class="TopStatusBar">
                  <tr>
                     <td>Documents</td>
                     <td>&nbsp;</td>
                     <td style="text-align: right;">
                         <a href="javascript: popupStart(300, 400, 'Help.jsp')">Help</a> | 
                         <a href="javascript: popupStart(300, 400, 'About.jsp')">About</a> |
                         <a href="javascript: popupStart(300, 400, 'License.jsp')">License</a>
                     </td>
                  </tr>
              </table>
          </td>
      </tr>
      <tr>
      <%--
         <td class="MainTableLeftColumn" valign="top">
             <div class="leftplane">
                  <h3>&nbsp; Tags</h3>
                  <div style="background-color: #EEEEFF;">
                      <ul>
                         <li>Tag 1</li>
                         <li>Tag 2</li>
                         <li>Tag 3</li>
                      </ul>
                  </div>
             </div>
         </td>
         --%>
         <td class="MainTableRightColumn" colspan="2" valign="top">
           <jsp:include page="addDocument.jsp"/>
           
           <html:form action="/dms/combinePDFs">
           <input type="hidden" name="curUser" value="<%=curUser%>">
           <input type="hidden" name="demoId" value="<%=moduleid%>">
           <div class="documentLists">
               <%-- STUFF TO DISPLAY --%>
             <%
                ArrayList categories = new ArrayList();
                ArrayList categoryKeys = new ArrayList();
                ArrayList privatedocs = new ArrayList();
                privatedocs = EDocUtil.listDocs(module, moduleid, view, EDocUtil.PRIVATE, sort);

                categories.add(privatedocs);
                categoryKeys.add(moduleName + "'s Private Documents");
                if (module.equals("provider")) {
                    ArrayList publicdocs = new ArrayList();
                    publicdocs = EDocUtil.listDocs(module, moduleid, view, EDocUtil.PUBLIC, sort);
                    categories.add(publicdocs);
                    categoryKeys.add("Public Documents");
                }
                
                
                for (int i=0; i<categories.size();i++) {
                    String currentkey = (String) categoryKeys.get(i);
                    ArrayList category = (ArrayList) categories.get(i);
             %>
              <div class="doclist">
                   <div class="headerline">
                         <div class="docHeading">
                                <a id="plusminus<%=i%>" href="javascript: showhide('documentsInnerDiv<%=i%>', 'plusminus<%=i%>');">
                                  -- <%= currentkey%> 
                                </a>
                                <span class="tabs">
                                     View: <a href="?function=<%=module%>&functionid=<%=moduleid%>">All</a>
                                     <% for (int i3=0; i3<doctypes.size(); i3++) { %>
                                        | <a href="?function=<%=module%>&functionid=<%=moduleid%>&view=<%=(String) doctypes.get(i3)%>"><%=(String) doctypes.get(i3)%></a>
                                     <%}%>
                                </span>
                                
                         </div>
                   </div>
                 <div id="documentsInnerDiv<%=i%>" style="background-color: #f2f7ff;">
                        <table id="privateDocs" class="docTable">
                           <tr>
                               <td><input class="tightCheckbox" type="checkbox" id="pdfCheck<%=i%>" onclick="checkAll('pdfCheck<%=i%>','privateDocsDiv', 'tightCheckbox<%=i%>');"/></td>
                               <td width="30%"><b><a href="?sort=description&function=<%=module%>&functionid=<%=moduleid%>&view=<%=view%>"><bean:message key="dms.documentReport.msgDocDesc"/></b></a></td>
                               <td width="10%"><b><a href="?sort=contenttype&function=<%=module%>&functionid=<%=moduleid%>&view=<%=view%>">Content Type</b></a></td>
                               <td width="15%"><b><a href="?sort=type&function=<%=module%>&functionid=<%=moduleid%>&view=<%=view%>"><bean:message key="dms.documentReport.msgDocType"/></b></a></td>
                               <td width="20%"><b><a href="?sort=creator&function=<%=module%>&functionid=<%=moduleid%>&view=<%=view%>"><bean:message key="dms.documentReport.msgCreator"/></b></a></td>
                               <td width="15%"><a href="?sort=observationdate&function=<%=module%>&functionid=<%=moduleid%>&view=<%=view%>"><b>Observation Date</b></a></td>
                               <td width="10%"><b><bean:message key="dms.documentReport.msgAction"/></b></td>
                           </tr>

              <%
                for (int i2=0; i2<category.size(); i2++) {
                    EDoc curdoc = (EDoc) category.get(i2);
                    //content type (take everything following '/')
                    int slash = 0;
                    String contentType = "";
                    if ((slash = curdoc.getContentType().indexOf('/')) != -1) {
                        contentType = curdoc.getContentType().substring(slash+1);
                    }
                    String dStatus = "";
                    if ((curdoc.getStatus() + "").compareTo("A") == 0) dStatus="active";
                    else if ((curdoc.getStatus() + "").compareTo("H") == 0) dStatus="html";
            %>
                           <tr>
                              <td>
                                  <% if (curdoc.isPDF()){%>
                                        <input class="tightCheckbox<%=i%>" type="checkbox" name="docNo" id="docNo<%=curdoc.getDocId()%>" value="<%=curdoc.getDocId()%>" style="margin:0px; padding:0px;"/>
                                  <%}else{%>
                                        &nbsp;
                                  <%}%>
                              </td>
                              <td>
                              <% 
                              String url = "documentGetFile.jsp?document=" + StringEscapeUtils.escapeJavaScript(curdoc.getFileName()) + "&type=" + dStatus + "&doc_no=" + curdoc.getDocId();
                              if (curdoc.getStatus() == 'H') { %>
                                 <a href="<%=url%>" target="_blank">
                              <% } else { %>
                                 <a href="javascript:popup1(480, 480, '<%=url%>', 'edoc<%=i2%>')">
                              <% } %>
                                 <%=curdoc.getDescription()%>
                              </td>
                              <td><%=contentType%></td>
                              <td><%=curdoc.getType()%></td>
                              <td><%=curdoc.getCreatorName()%></td>
                              <td><%=curdoc.getObservationDate()%></td>
                              <td><a href="javascript: checkDelete('documentReport.jsp?delDocumentNo=<%=curdoc.getDocId()%>&function=<%=module%>&functionid=<%=moduleid%>','<%=curdoc.getDescription()%>')"><bean:message key="dms.documentReport.btnDelete"/></a> &nbsp; &nbsp; 
                              <% if (curdoc.getStatus() == 'H') { %>
                                <a href="#" onclick="popup(450, 600, 'addedithtmldocument.jsp?editDocumentNo=<%=curdoc.getDocId()%>&function=<%=module%>&functionid=<%=moduleid%>', 'EditDoc')">
                              <% } else { %>
                                <a href="#" onclick="popup(300, 500, 'editDocument.jsp?editDocumentNo=<%=curdoc.getDocId()%>&function=<%=module%>&functionid=<%=moduleid%>', 'EditDoc')">
                              <% } %>
                              <bean:message key="dms.documentReport.btnEdit"/></a></td>
                           </tr>

            <%}
            if (category.size() == 0) {%>
            <tr><td colspan="6">No documents to display</td></tr>
            <%}%>
                        </table>
                        
                   </div>
              </div>
              <%}%>

            </div>
            <div>
              <input type="button" name="Button" value="<bean:message key="dms.documentReport.btnDoneClose"/>" onclick=self.close();>
              <input type="button" name="print" value='<bean:message key="global.btnPrint"/>' onClick="window.print()">
              <input type="button" value="Combine PDFs" onclick="return submitForm('<rewrite:reWrite jspPage="combinePDFs.do"/>');"/>
              <%
                    if( module.equals("demographic") ) {
              %>
                        <oscarProp:oscarPropertiesCheck property="MY_OSCAR" value="yes">
                            <indivo:indivoRegistered demographic="<%=moduleid%>" provider="<%=curUser%>">    
                                
                                <input type="button" value="Send to MyOscar" onclick="return submitForm('<rewrite:reWrite jspPage="send2Indivo.do"/>');"/>
                                
                            </indivo:indivoRegistered>
                        </oscarProp:oscarPropertiesCheck>
              <%
                    }
              %>
            </div>
           </html:form>
         </td>
      </tr>
      <tr>
         <td colspan="2" class="MainTableBottomRowRightColumn">
         </td>
      </tr>
  </table>
   
  
</body>
</html:html>
