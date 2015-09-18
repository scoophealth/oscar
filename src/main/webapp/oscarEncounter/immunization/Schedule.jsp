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
<security:oscarSec roleName="<%=roleName$%>" objectName="_eChart" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../../securityError.jsp?type=_eChart");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>

<%@page import="org.oscarehr.util.LoggedInInfo"%>
<%@ page import="oscar.oscarEncounter.immunization.data.*, oscar.util.*, oscar.oscarDemographic.data.*" %>
<%@ page import="oscar.oscarEncounter.immunization.pageUtil.*, java.util.*, org.w3c.dom.*" %>
<%
oscar.oscarEncounter.pageUtil.EctSessionBean bean = (oscar.oscarEncounter.pageUtil.EctSessionBean)request.getSession().getAttribute("EctSessionBean");

String sDoc = null;
String demoNo = request.getParameter("demographic_no");
String last_name = "";
String first_name = "";
String sex = "";
String age = "";
if( demoNo != null ) {
    DemographicData dData = new DemographicData();
    org.oscarehr.common.model.Demographic demographic = dData.getDemographic(LoggedInInfo.getLoggedInInfoFromSession(request), demoNo);
    last_name = demographic.getLastName();
    first_name = demographic.getFirstName();
    sex = demographic.getSex();
    age = demographic.getAge();
}
else {
    if (bean.demographicNo != null) {
      demoNo = bean.demographicNo;
      last_name = bean.getPatientLastName();
      first_name = bean.getPatientFirstName();
      sex = bean.getPatientSex();
      age = bean.getPatientAge();
    }
}

if( demoNo != null ) {
    sDoc = new EctImmImmunizationData().getImmunizations(demoNo);
}
if(sDoc == null){    
    String redirect = "loadConfig.do";
    
    if( demoNo != null )
        redirect += "?demographic_no=" + demoNo;
        
    response.sendRedirect(redirect);
    return;
} 
Document doc = UtilXML.parseXML(sDoc);
Element root = doc.getDocumentElement();
NodeList sets = root.getElementsByTagName("immunizationSet");
       
%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title><bean:message
	key="oscarEncounter.immunization.Schedule.title" /></title>
<link rel="stylesheet" type="text/css" href="../encounterStyles.css">
<link rel="stylesheet" type="text/css" media="all" href="../share/css/extractedFromPages.css"  />

<script language="javascript">
    function onClose() {
        var x = window.confirm("Are you sure your wish to exit without saving?");
        if(x) {window.close();}
    }

    function edit(nodeName, colName){
        windowprops = "height=443,width=630,location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes,screenX=0,screenY=0,top=0,left=0";
        window.open("ScheduleEdit.jsp?node=" + nodeName + "&name=" + colName,"<bean:message key='oscarEncounter.immunization.Schedule.msgRecordImm'/>",windowprops);
    }

    function returnEdit(nodeName, givenDate, refusedDate, lot, provider, comments){
        var frm = document.forms[0];
       
        var label;
        if(givenDate != ""){
            label = givenDate;
        }else{            
            if(refusedDate != ""){
                label = "<bean:message key="oscarEncounter.immunization.Schedule.msgRefused"/>"+" "+refusedDate;
            }else{   
                label = "";
            }
        }

        document.getElementById(nodeName + '_label').innerHTML = label;

        eval('frm.' + nodeName + '_givenDate').value   = givenDate;
        eval('frm.' + nodeName + '_refusedDate').value = refusedDate;
        eval('frm.' + nodeName + '_lot').value         = lot;
        eval('frm.' + nodeName + '_provider').value    = provider;
        eval('frm.' + nodeName + '_comments').value    = comments;
    }

    function showSet(tblName, ev) {
        var tbl = document.getElementById(tblName);
        var b = false;

        if(ev.target!=null){
            if(ev.target.checked){
                b = true;
            }
        }else{
            if(event.srcElement.checked){
                b = true;
            }
        }

        if(b==true){
            tbl.style.display=''; 
            //document.tblSetBar.style.display='';
            // document.getElementById("tblSetBar").style.display='';
        }else{
            tbl.style.display='none';
            // document.getElementById("tblSetBar").style.display='none';
        }
    }
    
    function showSetName(tblName, chkId) {
        var tbl = document.getElementById(tblName);
        var b = false;

        if(chkId !=null){
            if(document.getElementById(chkId).checked) {
            	document.getElementById(chkId).checked = false;
                b = false;
            } else {
            	document.getElementById(chkId).checked = true;
                b = true;
            }
        } 

        if(b==true) {
            tbl.style.display=''; 
            // document.getElementById("tblSetBar").style.display='';
        } else {
            tbl.style.display='none';
            // document.getElementById("tblSetBar").style.display='none';
        }
    }

</script>

</head>
<body class="BodyStyle" vlink="#0000FF">
<!--  -->
    <table  class="MainTable" id="scrollNumber1" name="encounterTable">
        <tr class="MainTableTopRow">
            <td class="MainTableTopRowLeftColumn">
                <bean:message key="oscarEncounter.immunization.Schedule.msgImm"/>
            </td>
            <td class="MainTableTopRowRightColumn">
                <table class="TopStatusBar">
                    <tr>
                        <td class="Header" style="padding-left:2px;padding-right:2px;border-right:2px solid #003399;text-align:left;font-size:80%;font-weight:bold;width:100%;" NOWRAP >
                            <%=last_name %>, <%=first_name%> <%=sex%> <%=age%>
                        </td>
                        <td  >
                        </td>
                        <td style="text-align:right" NOWRAP>
                                <a href="javascript:window.close();"  ><bean:message key="global.btnClose"/></a> |  
                        </td>
                    </tr>
                </table>
            </td>
        </tr>
        <tr style="vertical-align:top">
            <td class="MainTableLeftColumn">

            </td>
            <td class="MainTableRightColumn">
            <html:form action="/oscarEncounter/immunization/saveSchedule">
                <input type="hidden" name="demographic_no" value="<%=demoNo%>">
            <table name="encounterTableRightCol"  width="100%">
                <tr>
                    <td>

                        <table  border=0>
                            <tr>
                                <td>
                        
                        <input type="button" value="<bean:message key="global.btnSave"/>" onclick="formSubmit('Save');" style="width:100px" />
                                </td>
                                <td align="right">
                                    <input type="button" value="<bean:message key="oscarEncounter.immunization.Schedule.btnConf"/>" onclick="formSubmit('Configure');" style="width:100px" />
                                </td>
                                <td>
                                   <input type="button" value="Show All" onclick="formSubmit('ShowAll');" style="width:100px" />
                                </td>
                                <td>
                                   <input type="button" value="<bean:message key="global.btnClose"/>" onclick="window.close();" style="width:100px" />
                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>
                <tr>
                    <td>

                        <input type="hidden" name="xmlDoc" value='<%= UtilMisc.encode64(UtilXML.toXML(doc)) %>' />
                        <%

                        for(int i=0; i<sets.getLength(); i++){
                            Element set = (Element)sets.item(i);

                            String status = set.getAttribute("status"); 
                            String showDeleted = (String) request.getAttribute("showDeleted");
                            if (showDeleted == null) {showDeleted = "";}
                           
                            if ( (status != null && !status.equals("deleted"))  || showDeleted.equals("true") ){
                               String fontStyle = ""; 
                               if (status.equals("deleted")){ fontStyle= "style=\"text-decoration: line-through;\"";}  %> 
                               <div style="font-weight: bold">
                                  <input type="checkbox" onclick="javascript:showSet('tblSet<%=i%>', event);" id="chkSet<%=i%>"  />
                                     <a href=# onclick="javascript:showSetName('tblSet<%=i%>', 'chkSet<%=i%>');" <%=fontStyle%> >
                                     <%= set.getAttribute("name") %></a> 
                                     &nbsp;&nbsp;
                                     
                                     <% if (!status.equals("deleted")){ %>
                                     <a href="deleteSchedule.do?method=delete&tblSet=<%=i%>&demoNo=<%=demoNo%>" onclick="return confirm('Are you sure you want to delete this record ?');" >del</a> 
                                     <%}else{%>
                                     <a href="deleteSchedule.do?method=restore&tblSet=<%=i%>&demoNo=<%=demoNo%>" onclick="return confirm('Are you sure you want to restore this record ?');" >restore</a> 
                                     <%}%>
                                     
                               </div>
                            <%}%>
                               <table cellpadding=2 cellspacing=0 border="2px" rules="all" id="tblSet<%=i%>"  style="display:none" >
                               <%

                                  int colCount = -1;
                                  Element columnList = (Element)set.getElementsByTagName("columnList").item(0);
                                  NodeList columns = columnList.getElementsByTagName("column");

                                  if(set.getAttribute("headers").equalsIgnoreCase("true")){%>
						<tr>
							<td class="head">&nbsp;</td>
							<%
                                     for(int j=0; j<columns.getLength(); j++){
                                        Element column = (Element)columns.item(j);%>
							<td class="head"><%= column.getAttribute("name") %>&nbsp;</td>
							<%colCount = j+2;
                                     }%>
							<td class="head"><bean:message
								key="oscarEncounter.immunization.Schedule.msgComments" /></td>
						</tr>
						<%}
                                  Element rowList = (Element)set.getElementsByTagName("rowList").item(0);
                                  NodeList rows = rowList.getElementsByTagName("row");

                                  for(int j=0; j<rows.getLength(); j++){
                                     Element row = (Element)rows.item(j);

                                     String sName = row.getAttribute("name");
                                     if(sName.length()<1){
                                        String s = "tdSet" + i + "_Row" + j + "_name"; %>
						<tr>
							<td class="head" id="<%=s%>"><%=genText(s, "")%></td>
							<%   }else{%>
						
						<tr>
							<td class="head"><%= sName %></td>
							<%   }

                                     if(colCount>0){
                                        int n=0;
                                        NodeList cells = row.getElementsByTagName("cell");
                                        for(int k=1; k<colCount; k++){
                                           Element cell = (Element)cells.item(n);
                                           if(cell != null){
                                              if(String.valueOf(k).equals(cell.getAttribute("index"))){
                                                 String s = "tdSet" + i + "_Row" + j + "_Col" + k; %>
							<td class="normal" id="<%=s%>"><%= genCell(s, cell, sName+" - "+((Element)columns.item(k-1)).getAttribute("name"))%>
							</td>
							<%   n++;
                                              }else{ %>
							<td class="grey">&nbsp;</td>
							<%
                                              }
                                           }else{ %>
							<td class="grey">&nbsp;</td>
							<%}
                                        }
                                     }else{
                                        NodeList cells = row.getElementsByTagName("cell");
                                        for(int k=0; k<cells.getLength(); k++){
                                           Element cell = (Element)cells.item(k);

                                           if(cell.getAttribute("index").equals(String.valueOf(k+1))){
                                              String s = "tdSet" + i + "_Row" + j + "_Col" + (k+1); %>
							<td id="<%=s%>"><%= genCell(s, cell, "")%></td>
							<%}else{%>
							<td class="grey">&nbsp;</td>
							<%}
                                        }
                                     }

                                     String sID = "tdSet" + i + "_Row" + j + "_comments";
                                     String sValue = "";

                                     NodeList comments = row.getElementsByTagName("comments");
                                     if(comments.getLength()>0){
                                        sValue = UtilXML.getText(comments.item(0));
                                     }%>
							<td id="<%=sID%>"><%= genText(sID, sValue)%></td>
						</tr>
						<%
                                  }
                            %>
					</table>
					<%//}
                        }

                        %> <%!
                        String genText(String id, String value){
                            String s = "\n<span style='width:100%'>"
                                + "<input type=text style='width:100%;' name='"
                                + id + "_text' value='" + value + "'></input>"
                                + "</span>\n";

                            return s;
                        }

                        String genCell(String id, Element cell, String colName){
                            String s = "\n<span style='width:100%'>";

                            String givenDate = cell.getAttribute("givenDate");
                            String refusedDate = cell.getAttribute("refusedDate");
                            String lot = cell.getAttribute("lot");
                            String provider = cell.getAttribute("provider");
                            String comments = cell.getAttribute("comments");

                            s += "<input type=hidden name='" + id + "_givenDate' value='" + givenDate + "' />"
                                     + "<input type=hidden name='" + id + "_refusedDate' value='" + refusedDate + "' />"
                                     + "<input type=hidden name='" + id + "_lot' value='" + lot + "' />"
                                     + "<input type=hidden name='" + id + "_provider' value='" + provider + "' />"
                                     + "<input type=hidden name='" + id + "_comments' value='" + comments + "' />";

                            s += "<span id='" + id + "_label' style='font-size:8pt;width:75px'>";
                            if(givenDate.length()>0){
                                s += givenDate;
                            }else{
                                if(refusedDate.length()>0){
                                    s += "Refused "+refusedDate;
                                }else{
                                    s += "&nbsp;";
                                }
                            }
                            s += "</span>";

                            s += "<span style='text-align:right;width:15px'><a href=\"javascript:edit('" + id + "', '"+ colName +"');\"><img border=0 src='img/edit.gif' /></a></span>";
                            s += "</span>\n";

                            return s;
                        }
                        %> <script language="javascript">
                            function formSubmit(action)
                            {
                                document.forms[0].hdnAction.value = action;
                                document.forms[0].submit();
                            }
                        </script>
					<table>
						<tr>
							<td><input type="hidden" name="hdnAction" /></td>
							<td><input type="button"
								value="<bean:message key="global.btnSave"/>"
								onclick="formSubmit('Save');" style="width: 100px" /></td>
							<td><input type="button"
								value="<bean:message key="oscarEncounter.immunization.Schedule.btnConf"/>"
								onclick="formSubmit('Configure');" style="width: 100px" /></td>
							<td><input type="button" value="Show All"
								onclick="formSubmit('ShowAll');" style="width: 100px" /></td>
							<td><input type="button"
								value="<bean:message key="global.btnClose"/>"
								onclick="window.close();" style="width: 100px" /></td>
						</tr>
					</table>
					</td>
				</tr>
				<!----End new rows here-->
			</table>
		</html:form></td>
	</tr>
	<tr>
		<td class="MainTableBottomRowLeftColumn"></td>
		<td class="MainTableBottomRowRightColumn"></td>
	</tr>
</table>

</body>
</html>
