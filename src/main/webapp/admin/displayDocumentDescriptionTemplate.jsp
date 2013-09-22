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
    
<%@page contentType="text/html"%>
    
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
    
<%@ page import="java.util.*,java.sql.*,java.util.ResourceBundle" errorPage="../provider/errorpage.jsp"%>
<%@ page import="org.oscarehr.util.SpringUtils" %>
<%@page import="org.oscarehr.common.dao.UserPropertyDAO"%>
<%@page import="org.oscarehr.common.model.UserProperty"%>
<%@page import="oscar.dms.EDocUtil"%>
    
<%
    String curProvider_no = (String) session.getAttribute("user");
    String roleName$ = (String) session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
    ArrayList docTypes = EDocUtil.getDoctypes("demographic");
    UserPropertyDAO userPropertyDAO = (UserPropertyDAO) SpringUtils.getBean("UserPropertyDAO");
    UserProperty uProp = userPropertyDAO.getProp(curProvider_no, UserProperty.DOCUMENT_DESCRIPTION_TEMPLATE);
    Boolean clinicDefault = true;
        
    if (uProp != null && uProp.getValue().equals(UserProperty.USER)) {
        clinicDefault = false;
    }
%>
    
    
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title><bean:message key="provider.setDocumentDescriptionTemplate.title" /></title>
        <script language="javascript" type="text/javascript" src="../share/javascript/Oscar.js" ></script>
        <script type="text/javascript" src="../share/javascript/prototype.js"></script>
            
        <script type="text/javascript">
            var useDocumentDescriptionTemplateType;
            function adddocDescription() {
                if(document.docDescriptionForm.docDescription.value.length>0 && document.docDescriptionForm.docDescriptionShortcut.value.length>0)
                {
                    var docType=document.getElementById('docType').options[document.getElementById('docType').selectedIndex].value;
                    var docDescription=document.docDescriptionForm.docDescription.value;
                    var docShortcut=document.docDescriptionForm.docDescriptionShortcut.value;
                    var url="<%=request.getContextPath()%>/DocumentDescriptionTemplate.do";
                    var providerNo=document.docDescriptionForm.providerNo.value;
                    var data='method=addDocumentDescription&description='+docDescription+'&shortcut='+docShortcut+'&doctype='+docType+'&providerNo='+providerNo;
                    new Ajax.Request(url,{method:'post',parameters:data,onSuccess:function(transport){
                            getDocumentDescriptionTemplateFromSelectedDocType();                            
                        }});
                }
                else {
                    alert("<bean:message key="provider.setDocumentDescriptionTemplate.DescriptionCannotBeEmpty" />");
                }
            }

            function updatedocDescription() {
                if(document.docDescriptionForm.docDescription.value.length>0 && document.docDescriptionForm.docDescriptionShortcut.value.length>0)
                {
                    var id=document.docDescriptionForm.descriptionId.value;
                    var docType=document.getElementById('docType').options[document.getElementById('docType').selectedIndex].value;
                    var docDescription=document.docDescriptionForm.docDescription.value;
                    var docShortcut=document.docDescriptionForm.docDescriptionShortcut.value;
                    var providerNo=document.docDescriptionForm.providerNo.value;
                    var url="<%=request.getContextPath()%>/DocumentDescriptionTemplate.do";
                    var data='method=updateDocumentDescription&description='+docDescription+'&shortcut='+docShortcut+'&doctype='+docType+'&id='+id+'&providerNo='+providerNo;
                    new Ajax.Request(url,{method:'post',parameters:data,onSuccess:function(transport){
                            getDocumentDescriptionTemplateFromSelectedDocType();
                        }});
                }
                else {
                    alert("<bean:message key="provider.setDocumentDescriptionTemplate.DescriptionCannotBeEmpty" />");
                }
            }
             
            function deletedocDescription() {
                if(document.docDescriptionForm.docDescription.value.length>0 && document.docDescriptionForm.docDescriptionShortcut.value.length>0)
                {
                     var id=document.docDescriptionForm.descriptionId.value;
                    var url="<%=request.getContextPath()%>/DocumentDescriptionTemplate.do";
                    var data='method=deleteDocumentDescription&id='+id;
                    new Ajax.Request(url,{method:'post',parameters:data,onSuccess:function(transport){
                            getDocumentDescriptionTemplateFromSelectedDocType();
                        }});
                }
                else {
                    alert("<bean:message key="provider.setDocumentDescriptionTemplate.DescriptionCannotBeEmpty" />");
                }
            }
             
            function getDocumentDescriptionTemplateFromSelectedDocType() {
                 
                var docType="";
                 
                var providerNo=document.docDescriptionForm.providerNo.value;
                var docDescriptionList;
                var adoc;
                docDescriptionList = $('docDescriptionList');

                while( docDescriptionList.hasChildNodes() ) { docDescriptionList.removeChild( docDescriptionList.lastChild ); }
                if(document.getElementById('docType').selectedIndex>0) {
                    docType=document.getElementById('docType').options[document.getElementById('docType').selectedIndex].value; 
                    document.getElementById('tblDesc').style.visibility="visible";
                }
                else
                {
                    document.getElementById('tblDesc').style.visibility="hidden";
                    document.docDescriptionForm.addDescription.style.visibility='hidden';
                    document.docDescriptionForm.updateDescription.style.visibility='hidden';
                    document.docDescriptionForm.deleteDescription.style.visibility='hidden';
                    document.docDescriptionForm.descriptionId.value="";
                    document.docDescriptionForm.docDescription.value="";
                    document.docDescriptionForm.docDescriptionShortcut.value="";
                    return;
                }                
                 
                adoc = document.createElement('div');
                docDescriptionList.appendChild(adoc);
                var url="<%=request.getContextPath()%>/DocumentDescriptionTemplate.do";
                var data='method=getDocumentDescriptionFromDocType&doctype='+docType+"&providerNo="+providerNo+"&useDocumentDescriptionTemplateType="+useDocumentDescriptionTemplateType;
                new Ajax.Request(url,{method:'post',parameters:data,onSuccess:function(transport){
                        var json=transport.responseText.evalJSON();
                         
                        if(json!=null ){
                             
                            var mySelect = document.createElement("select");
                            mySelect.setAttribute("id","docDescList");
                            mySelect.setAttribute("onchange","getDescriptionAndShortcutFromSelectedList()");
                            var myOption1 = document.createElement("option");
                            myOption1.text = "";
                            myOption1.value = "";
                            mySelect.appendChild(myOption1);
                            
                            for (var i = 0; i < json.documentDescriptionTemplate.length; i++) {

                                    myOption1 = document.createElement("option");
                                    myOption1.text = "("+json.documentDescriptionTemplate[i].descriptionShortcut+")      "+ json.documentDescriptionTemplate[i].description;
                                    myOption1.value = json.documentDescriptionTemplate[i].id;
                                     
                                    mySelect.appendChild(myOption1);
                            } 
                            docDescriptionList = $('docDescriptionList');
                            docDescriptionList.appendChild(mySelect);
                            getDescriptionAndShortcutFromSelectedList();
                        }
                    }});
                 
            }
            function getDescriptionAndShortcutFromSelectedList() {
                if(document.getElementById('docDescList').selectedIndex<=0) {
                    document.docDescriptionForm.addDescription.style.visibility='visible';
                    document.docDescriptionForm.updateDescription.style.visibility='hidden';
                    document.docDescriptionForm.deleteDescription.style.visibility='hidden';
                    document.docDescriptionForm.descriptionId.value="";
                    document.docDescriptionForm.docDescription.value="";
                    document.docDescriptionForm.docDescriptionShortcut.value="";
                } else {
                    var id=document.getElementById('docDescList').options[document.getElementById('docDescList').selectedIndex].value; 
                    var url="<%=request.getContextPath()%>/DocumentDescriptionTemplate.do";
                    var data='method=getDocumentDescriptionFromId&id='+id;
                    new Ajax.Request(url,{method:'post',parameters:data,onSuccess:function(transport){
                            var json=transport.responseText.evalJSON();
                            if(json!=null ){
                                document.docDescriptionForm.addDescription.style.visibility='hidden';
                                document.docDescriptionForm.updateDescription.style.visibility='visible';
                                document.docDescriptionForm.deleteDescription.style.visibility='visible';
                                document.docDescriptionForm.descriptionId.value=json.documentDescriptionTemplate.id;
                                document.docDescriptionForm.docDescription.value=json.documentDescriptionTemplate.description;
                                document.docDescriptionForm.docDescriptionShortcut.value=json.documentDescriptionTemplate.descriptionShortcut;
                            }
                        }});
                }
            }
             
            function checkClinicDefault()
            {
                 
                 if(document.getElementById('useclinicdefault').checked && document.docDescriptionForm.providerNo.value!="null") {
                    document.getElementById('docTypeTable').style.visibility='hidden';
                    document.getElementById('tblDesc').style.visibility='hidden';
                    document.docDescriptionForm.updateDescription.style.visibility='hidden';
                    document.docDescriptionForm.deleteDescription.style.visibility='hidden';
                    document.docDescriptionForm.addDescription.style.visibility='hidden';
                    var url="<%=request.getContextPath()%>/DocumentDescriptionTemplate.do";
                    var data='method=saveDocumentDescriptionTemplatePreference&defaultShortcut=<%=UserProperty.CLINIC%>';
                    new Ajax.Request(url,{method:'post',parameters:data,onSuccess:function(transport){
                        }});
                }
                 else
                {
                    useDocumentDescriptionTemplateType=document.docDescriptionForm.providerNo.value!="null"?"<%=UserProperty.USER%>":"<%=UserProperty.CLINIC%>";
                    var url="<%=request.getContextPath()%>/DocumentDescriptionTemplate.do";
                    var data='method=saveDocumentDescriptionTemplatePreference&defaultShortcut=<%=UserProperty.USER%>';
                    new Ajax.Request(url,{method:'post',parameters:data,onSuccess:function(transport){
                        }});
                    document.getElementById('docTypeTable').style.visibility='visible';
                    document.getElementById('docType').selectedIndex=-1;
                    document.getElementById('tblDesc').style.visibility='hidden';
                    getDocumentDescriptionTemplateFromSelectedDocType();
                }
            }
        </script>
    </head>
    <body  onload="checkClinicDefault()">
        <%
            String providerNo = curProvider_no;
            if (request.getParameter("setDefault") != null && request.getParameter("setDefault").equals("true")) {
                providerNo = null;
            }
        %>
        <form  method="post" name="docDescriptionForm" action="displayDocumentDescriptionTemplate.jsp" >
            <div id="usefault" style="<%=providerNo==null? "visibility:hidden" : ""%>">
                <input type="checkbox" name="useclinicdefault" <%=clinicDefault == true ? "checked='checked'" : ""%> id="useclinicdefault" onclick="checkClinicDefault()" ><bean:message key="provider.setDocumentDescriptionTemplate.useClinicDefault" />
            </div>
            <% if (providerNo==null) {%> 
            <bean:message key="provider.setDocumentDescriptionTemplate.setClinicDefault" />
            <%}%>
            <p>
                
            <table id="docTypeTable">
                <tr>
                    <td><bean:message key="provider.setDocumentDescriptionTemplate.Type" />: </td>
                    <td>
                        <select name ="docType" id="docType" onchange="getDocumentDescriptionTemplateFromSelectedDocType()" >
                            <option value=""><bean:message key="dms.incomingDocs.selectType" /></option>
                            <%for (int j = 0; j < docTypes.size(); j++) {
                                            String docType = (String) docTypes.get(j);%>
                            <option value="<%= docType%>" ><%= docType%></option>
                            <%}%>
                        </select>
                    </td>
                </tr>
                <tr><td>
                        <bean:message key="provider.setDocumentDescriptionTemplate.Description" />: </td>
                    <td><div id="docDescriptionList"></div></td>
                </tr>
            </table>
            <p><p>
                
            <table style="visibility:hidden" id="tblDesc">
                <tr><th align="left" ><bean:message key="provider.setDocumentDescriptionTemplate.DescriptionShortcut" /></th><th align="left"><bean:message key="provider.setDocumentDescriptionTemplate.Description" /></th></tr>
                <tr>
                    <td><input type="hidden" name="providerNo" value="<%=(providerNo==null?"null":providerNo)%>"><input type="hidden" name="descriptionId" ><input name="docDescriptionShortcut" maxlength="20" size="20" value=""></td>
                    <td><input name="docDescription" maxlength="255" size="60" value=""></td>
                </tr>
                <tr>
                    <td colspan="2"><input type="button" value="<bean:message key="provider.setDocumentDescriptionTemplate.Add" />" id="addDescription" onclick="adddocDescription()">
                        <input type="button" value="<bean:message key="provider.setDocumentDescriptionTemplate.Update" />" id="updateDescription" onclick="updatedocDescription()">
                        <input type="button" value="<bean:message key="provider.setDocumentDescriptionTemplate.Delete" />" id="deleteDescription" onclick="deletedocDescription()">
                    </td>
                </tr>
            </table>    
        </form>
    </body>
<html>