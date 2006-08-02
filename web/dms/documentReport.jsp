<%
if(session.getValue("user") == null) response.sendRedirect("../logout.htm");
String user_no = (String) session.getAttribute("user");
String userfirstname = (String) session.getAttribute("userfirstname");
String userlastname = (String) session.getAttribute("userlastname");
%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<jsp:useBean id="oscarVariables" class="java.util.Properties" scope="page" />
<%@ page import="java.math.*, java.util.*, java.io.*, java.sql.*, oscar.*, oscar.util.*, java.net.*,oscar.MyDateFormat, oscar.dms.*, oscar.dms.data.*" %>
<%@ page import="org.apache.commons.lang.StringEscapeUtils" %>

<%
//if delete request is made
if (request.getParameter("delDocumentNo") != null) {
    EDocUtil.deleteDocument(request.getParameter("delDocumentNo"));
}

//preliminary JSP code

// "Module" and "function" is the same thing
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

String nowDate = EDocUtil.getDmsDateTime();

OscarProperties props = OscarProperties.getInstance();

AddEditDocumentForm formdata = new AddEditDocumentForm();
String defaultType = (String) props.getProperty("eDocAddTypeDefault", "");
String defaultDesc = "Enter Document Title"; //if defaultType isn't defined, this value is used for the title/description

Hashtable errors = new Hashtable();
if (request.getAttribute("errors") != null) {
    errors = (Hashtable) request.getAttribute("errors");
}

String mode = "";
if (request.getAttribute("mode") != null) {
    mode = (String) request.getAttribute("mode");
} else {
    mode = (String) request.getParameter("mode");
}

String editDocumentNo = "";
if (request.getAttribute("editDocumentNo") != null) {
    editDocumentNo = (String) request.getAttribute("editDocumentNo");
} else {
    editDocumentNo = (String) request.getParameter("editDocumentNo");
}

if (request.getAttribute("completedForm") != null) {
    formdata = (AddEditDocumentForm) request.getAttribute("completedForm");
} else if (editDocumentNo != null) {
    EDoc currentDoc = EDocUtil.getDoc(editDocumentNo);
    formdata.setFunction(currentDoc.getModule());
    formdata.setFunctionId(currentDoc.getModuleId());
    formdata.setDocType(currentDoc.getType());
    formdata.setDocDesc(currentDoc.getDescription());
    formdata.setDocCreator(user_no);
} else {
    formdata.setFunction(module);  //"module" and "function" are the same
    formdata.setFunctionId(moduleid);
    formdata.setDocType((String) props.getProperty("eDocAddTypeDefault", ""));
    formdata.setDocDesc(defaultType.equals("")?defaultDesc:defaultType);
    formdata.setDocCreator(user_no);
}
ArrayList doctypes = EDocUtil.getDoctypes(formdata.getFunction());
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
<link rel="stylesheet" type="text/css" href="../share/css/niftyPrint.css" media="print" />
<script type="text/javascript" src="../share/javascript/nifty.js"></script>
<script type="text/javascript">
window.onload=function(){
if(!NiftyCheck())
    return;

Rounded("div.topplane","top","transparent","#d1d5bd","small border #d1d5bd");
Rounded("div.topplane","bottom","transparent","#f2f7ff","small border #d1d5bd");

Rounded("div.doclist","top","transparent", "#ccccd7", "small border #ccccd7");
Rounded("div.doclist","bottom","transparent", "#e0ecff", "small border #ccccd7");
Rounded("div.leftplane","top", "transparent", "#CCCCFF","small border #ccccff");
Rounded("div.leftplane","bottom","transparent","#EEEEFF","small border #ccccff");
<%--request attribute "errors" is used to check if a document was just submitted --%>
<% if ((request.getAttribute("errors") != null) || (mode != null) || (editDocumentNo != null)) { %> 
showhide('addDocDiv', 'plusminusAddDocA')
<%}%>
}
</script>

<style type="text/css">
div.leftplane {
    width: 90%;
    margin-left: 3px;
    margin-right: 3px;
    margin-top: 3px;
}

div.leftplane h3 {
    background-color: #ccccff;
    font-variant: small-caps;
    font-weight: bold;
    font-size: 11px;
    margin: 0px;
    padding-left: 10px;
}


div.leftplane ul {
    list-style: none;
    list-style-type: none;
    list-style-position: outside;
    font-size: 12px;
    padding: 0px;
    margin: 0px;
}
    
div.leftplane li {
    padding-left: 5px;
    white-space: nowrap;
}

div.doclist {
    padding: 0px;
    margin-left: 5px;
    margin-right: 5px;
    margin-top: 5px;
}

div.documentLists {
    padding-bottom: 2px;
}
table.docTable {
    border: 0px;
    padding: 0px;
    width: 100%;
}

table.docTable td {
    border-bottom: solid 1px #afafaf;
    font-size: 12px;
    margin-top: 0px;
    padding-top: 0px;
    padding-bottom: 0px;
}

div.plusminus {
    font-size: 10px; 
    font-weight: bold;
    padding-left: 5px;
    padding-top: 0px;
    height: 20px;
}

div.docheading {
    padding-left: 20px;
    font-weight: bold;
    font-size: 13px;
    margin-top: 0px;
    padding-top: 0px;
}

div.headerline {
    background-color: #ccccd7;
    vertial-align: middle;
}

div.topplane {
    margin-right: 5px;
    margin-left: 5px;
}

/* Global */
a { 
    text-decoration: none; color: #7552ca;
}
a:hover {
    text-decoration: none; color: #bd528e;
}

input {
    border: 1px solid #7682b1;
}

input.tightCheckbox{
    margin:0px;
    padding:0px;
}

</style>

<script language="JavaScript">
var remote=null;
function refresh() {
  document.location.reload();
}
function rs(n,u,w,h,x) {
	args="width="+w+",height="+h+",resizable=yes,scrollbars=yes,status=0,top=60,left=30";
	remote=window.open(u,n,args);
	if (remote != null) {
		if (remote.opener == null)
		remote.opener = self;
	}
	if (x == 1) { return remote; }
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



function setfocus() {
	this.focus();
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

function checkSel(sel){
  theForm = sel.form;
  if ((theForm.docDesc.value == "") || (theForm.docDesc.value == "<%= defaultDesc%>")) {
       theForm.docDesc.value = theForm.docType.value;
       theForm.docDesc.focus();
       theForm.docDesc.select();
  }
}

function checkDefaultValue(object) {
  //selectBoxType = object.form.docType
  //var selectedType = selectBoxType.options[selectBoxType.selectedIndex].value;
  if ((object.value == "<%= defaultDesc%>") || (object.value == "<%= defaultType%>")) {
      object.value = "";
  }
}

function checkAll(checkboxId,parentEle){
   var f = document.getElementById(checkboxId);
   var val = f.checked;
   var chkList = document.getElementsByClassName('tightCheckbox',parentEle);
   for (i =0; i < chkList.length; i++){
      chkList[i].checked = val;
   }
}

function verifyChecks(t){
   if ( t.docNo == null ){ 
         alert("No documents selected");
         return false;
   }else{
      var oneChecked = 0;
      for ( i=0; i < t.docNo.length; i++){
         if(t.docNo[i].checked){
            oneChecked = oneChecked + 1;
         }
      }
      if ( oneChecked == 0 ){
         alert("No documents selected");
         return false
            request.setAttribute("functionid", request.getParameter("functionid"));
            e.printStackTrace();;   
      }
   }
   return true;
}

</script>
<script src="../share/javascript/prototype.js" type="text/javascript"></script>
</head>

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
             <html:form action="/dms/addEditDocument" method="POST" enctype="multipart/form-data">
                <div class="topplane">
                    <div class="docHeading" style="background-color: #d1d5bd;">
                        <a id="plusminusAddDocA" href="javascript: showhide('addDocDiv', 'plusminusAddDocA');">
                         + Add Document
                        </a>
                    </div>
                    <div id="addDocDiv" class="addDocDiv" style="background-color: #f2f5e3; display: none;">
                            <%-- Lists Errors --%>
                            <% for (Enumeration errorkeys = errors.keys(); errorkeys.hasMoreElements();) {%>
                            <font class="warning">Error: <bean:message key="<%=(String) errors.get(errorkeys.nextElement())%>"/></font><br/>
                            <% } %>
                            <input type="hidden" name="function" value="<%=formdata.getFunction()%>" size="20">
                            <input type="hidden" name="functionId" value="<%=formdata.getFunctionId()%>" size="20">
                            <input type="hidden" name="functionid" value="<%=moduleid%>" size="20">
                            <select name="docType" onchange="checkSel(this)"<% if (errors.containsKey("typemissing")) {%> class="warning"<%}%>>
                               <option value=""><bean:message key="dms.addDocument.formSelect"/></option>
                               <%
                               for (int i=0; i<doctypes.size(); i++) {
                                  String doctype = (String) doctypes.get(i); %>
                              <option value="<%= doctype%>"<%=(formdata.getDocType().equals(doctype))?" selected":""%>><%= doctype%></option>
                             <%}%>
                            </select>

                            <input type="text" name="docDesc" size="30" value="<%=formdata.getDocDesc()%>" onfocus="checkDefaultValue(this)"<% if (errors.containsKey("descmissing")) {%> class="warning"<%}%>>
                            <input type="hidden" name="docCreator" value="<%=formdata.getDocCreator()%>" size="20">
                            <input type="file" name="docFile" size="20"<% if (errors.containsKey("uploaderror")) {%> class="warning"<%}%>>
                            <br/>
                            <input type="hidden" name="mode" value="<% if (editDocumentNo != null) {out.print(editDocumentNo);} else {%>add<% } %>">
                            <input type="SUBMIT" name="Submit" value="<% if (editDocumentNo != null) {%>Update<%} else {%>Add<%}%>" onclick="javascript: this.disabled=true">
                            <input type="button" name="Button" value="<bean:message key="global.btnCancel"/>" onclick="javascript: window.location='documentReport.jsp?function=<%=module%>&functionid=<%=moduleid%>'">
                            <input type="button" name="Button" value="<bean:message key="dms.documentReport.btnAddHTML"/>" onclick="window.open('../dms/addhtmldocument.jsp?function=<%=module%>&functionid=<%=moduleid%>&creator=<%=user_no%>','', 'scrollbars=yes,resizable=yes,width=600,height=600')";>
                    </div>
               </div>
             </html:form>
         
           <html:form action="/dms/combinePDFs" onsubmit="return verifyChecks(this);">
           
           <div class="documentLists">  
              <div class="doclist">
                   <div class="headerline">
                         <div class="docHeading">
                                <a id="plusminusPrivateA" href="javascript: showhide('privateDocsDiv', 'plusminusPrivateA');">
                                  -- <%=moduleName%>'s Private Documents
                                </a>
                         </div>
                   </div>
                 <div id="privateDocsDiv" style="background-color: #f2f7ff;">
                        <table id="privateDocs" class="docTable">
                           <tr>
                               <td><input class="tightCheckbox" type="checkbox" id="pdfCheck1" onclick="checkAll('pdfCheck1','privateDocsDiv');"/></td>
                               <td width="34%"><b><bean:message key="dms.documentReport.msgDocDesc"/></b></td>
                               <td width="15%"><b><bean:message key="dms.documentReport.msgDocType"/></b></td>
                               <td width="17%"><b><bean:message key="dms.documentReport.msgCreator"/></b></td>
                               <td width="21%"><b>Last Modified</b></td>
                               <td width="13%"><b><bean:message key="dms.documentReport.msgAction"/></b></td>
                           </tr>

              <%
                ArrayList privatedocs = EDocUtil.listDocs(module, moduleid, EDocUtil.PRIVATE);

                for (int i=0; i<privatedocs.size(); i++) {
                    EDoc curdoc = (EDoc) privatedocs.get(i);
                    String dStatus = "";
                    if ((curdoc.getStatus() + "").compareTo("A") == 0) dStatus="active";
                    else if ((curdoc.getStatus() + "").compareTo("H") == 0) dStatus="html";
            %>
                           <tr>
                              <td>
                                  <% if (curdoc.isPDF()){%>
                                        <input class="tightCheckbox" type="checkbox" name="docNo" id="docNo<%=curdoc.getDocId()%>" value="<%=curdoc.getDocId()%>"/>
                                  <%}else{%>
                                        &nbsp;
                                  <%}%>
                              </td>
                              <td width="34%"><a href=# onClick="javascript:rs('new','documentGetFile.jsp?document=<%=StringEscapeUtils.escapeJavaScript(curdoc.getFileName())%>&type=<%=dStatus%>&doc_no=<%=curdoc.getDocId()%>', 480,480,1)"><%=curdoc.getDescription()%></td>
                              <td width="15%"><%=curdoc.getType()%></td>
                              <td width="17%"><%=curdoc.getCreatorName()%></td>
                              <td width="21%"><%=curdoc.getDateTimeStamp()%></td>
                              <td width="13%"><a href="javascript: checkDelete('documentReport.jsp?delDocumentNo=<%=curdoc.getDocId()%>&function=<%=module%>&functionid=<%=moduleid%>','<%=curdoc.getDescription()%>')"><bean:message key="dms.documentReport.btnDelete"/></a> &nbsp; &nbsp; 
                                              <a href="javascript: window.location='documentReport.jsp?editDocumentNo=<%=curdoc.getDocId()%>&function=<%=module%>&functionid=<%=moduleid%>'"><bean:message key="dms.documentReport.btnEdit"/></a></td>
                           </tr>

            <%   }
                 if (privatedocs.size() == 0) { %>
                           <tr><td colspan='5'><bean:message key="dms.documentReport.msgNoMatch"/></td></tr>
               <%}%>
                        </table>
                 </div>
              </div>
            <% if (module.compareTo("provider") == 0) { %>
              <div class="doclist">
                   <div class="headerline">
                         <div class="docHeading">
                                <a id="plusminusPublicA" href="javascript: showhide('publicDocs', 'plusminusPublicA');">
                                  -- <bean:message key="dms.documentReport.msgShareFolder"/>
                                </a>
                         </div>
                   </div>

                   <div id="publicDocsDiv" style="background-color: #f2f7ff;">
                        <table id="publicDocs" class="docTable">
                            <tr>
                              <td><input class="tightCheckbox" type="checkbox" id="pdfCheck2" onclick="checkAll('pdfCheck2','publicDocsDiv');"/></td>
                              <td width="34%"><b><bean:message key="dms.documentReport.msgDocDesc"/></b></td>
                              <td width="15%"><b><bean:message key="dms.documentReport.msgDocType"/></b></td>
                              <td width="17%"><b><%=module.substring(0,1).toUpperCase()%><%=module.substring(1)%></b></td>
                              <td width="21%"><b>Last Modified</b></td>
                              <td width="13%"><b><bean:message key="dms.documentReport.msgActive"/></b></td>
                            </tr>
            <%

                ArrayList publicdocs = EDocUtil.listDocs(module, moduleid, EDocUtil.PUBLIC);

                for (int i=0; i<publicdocs.size(); i++) {
                    EDoc curdoc = (EDoc) publicdocs.get(i);
                    String dStatus = "";
                    if ((curdoc.getStatus() + "").compareTo("A") == 0) dStatus="active";
                    else if ((curdoc.getStatus() + "").compareTo("H") == 0) dStatus="html";
              %>
                           <tr>
                              <td><%
                                   if (curdoc.isPDF()){%>
                                        <input class="tightCheckbox" type="checkbox" name="docNo" id="docNo<%=curdoc.getDocId()%>" value="<%=curdoc.getDocId()%>"/>
                                  <%}else{%>
                                        &nbsp;
                                  <%}%>
                              </td>
                              <td width="34%"><a href=# onClick="javascript:rs('new','documentGetFile.jsp?document=<%=StringEscapeUtils.escapeJavaScript(curdoc.getFileName())%>&type=<%=dStatus%>&doc_no=<%=curdoc.getDocId()%>', 480,480,1)"><%=curdoc.getDescription()%></td>
                              <td width="15%"><%=curdoc.getType()%></td>
                              <td width="17%"><%=curdoc.getCreatorName()%></td>
                              <td width="21%"><%=curdoc.getDateTimeStamp()%></td>
                              <td width="13%"><a href="javascript: checkDelete('documentReport.jsp?delDocumentNo=<%=curdoc.getDocId()%>&function=<%=module%>&functionid=<%=moduleid%>','<%=curdoc.getDescription()%>')"><bean:message key="dms.documentReport.btnDelete"/></a> &nbsp; &nbsp; 
                                              <a href="javascript: window.location='documentReport.jsp?editDocumentNo=<%=curdoc.getDocId()%>&function=<%=module%>&functionid=<%=moduleid%>'"><bean:message key="dms.documentReport.btnEdit"/></a></td>
                           </tr>

            <%   }
                 if (publicdocs.size() == 0) { %>
                            <tr><td colspan='5'><bean:message key="dms.documentReport.msgNoMatch"/></td></tr>
            <% } %>
            </table>
           <%}%>
               
                        
                   </div>
              </div>

            </div>
            <div style="float: left; clear: left;">
              <input type="button" name="Button" value="<bean:message key="dms.documentReport.btnDoneClose"/>" onclick=self.close();>
              <input type="button" name="print" value='<bean:message key="global.btnPrint"/>' onClick="window.print()">
              <input type="submit" value="Combine PDFs"/>
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
