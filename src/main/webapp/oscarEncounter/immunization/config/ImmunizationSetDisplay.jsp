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

<%@ page import="java.util.*, org.w3c.dom.*, oscar.util.*"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
      String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
      boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_eChart" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../../../securityError.jsp?type=_eChart");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>

<html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title><bean:message
	key="oscarEncounter.immunization.config.immunizationSetDisplay.title" />
</title>
<link rel="stylesheet" type="text/css" media="all" href="../share/css/extractedFromPages.css"  />

<script language="javascript">
    function edit(nodeName)
    {
        window.open('../ScheduleEdit.jsp?node=' + nodeName);
    }
	
     function returnEdit(nodeName, givenDate, refusedDate, lot, provider, comments)
    {
        var frm = document.forms[0];

        var label;
        if(givenDate != null)
        {
            label = givenDate;
        }
        else
        {
            if(refusedDate != null)
            {
                label = "Refused";
            }
        }

        document.getElementById(nodeName + '_label').innerHTML = label;

        eval('frm.' + nodeName + '_givenDate').value   = givenDate;
        eval('frm.' + nodeName + '_refusedDate').value = refusedDate;
        eval('frm.' + nodeName + '_lot').value         = lot;
        eval('frm.' + nodeName + '_provider').value    = provider;
        eval('frm.' + nodeName + '_comments').value    = comments;
    }
    


    function showSet(tblName, ev)
    {
        var tbl = document.getElementById(tblName);
        var b = false;

        if(ev.target!=null)
        {
            if(ev.target.checked)
            {
                b = true;
            }
        }
        else
        {
            if(event.srcElement.checked)
            {
                b = true;
            }
        }

        if(b==true)
        {
            tbl.style.display='';
        }
        else
        {
            tbl.style.display='none';
        }
    }

</script>

</head>
<body onload="window.focus();">

<form name="schedule">
<%
String setId = (String) request.getAttribute("setId");
oscar.oscarEncounter.immunization.config.data.EctImmImmunizationSetData setData = new oscar.oscarEncounter.immunization.config.data.EctImmImmunizationSetData();
String xmlString = setData.getSetXMLDoc(setId);

Document xmlDoc = UtilXML.parseXML(xmlString);
Element set = xmlDoc.getDocumentElement();
String setNamed = set.getAttribute("name");

int i = 0;
%>
<h1><bean:message
	key="oscarEncounter.immunization.config.immunizationSetDisplay.msgSet" />:
<%=setNamed%></h1>
<table cellpadding=2 cellspacing=0 border="2px" rules="all"
	id="tblSet<%=i%>" style="margin-bottom: 10px">
	<%
    int colCount = -1;

if(set.getAttribute("headers").equalsIgnoreCase("true"))
    {
        %><tr>
		<td class="head">&nbsp;</td>
		<%

        Element columnList = (Element)set.getElementsByTagName("columnList").item(0);
        NodeList columns = columnList.getElementsByTagName("column");

        for(int j=0; j<columns.getLength(); j++){
            Element column = (Element)columns.item(j);

            %><td class="head"><%= column.getAttribute("name") %>&nbsp;</td>
		<%

            colCount = j+2;
        }

        %><td class="head">Comments</td>
	</tr>
	<%
    }

    Element rowList = (Element)set.getElementsByTagName("rowList").item(0);
    NodeList rows = rowList.getElementsByTagName("row");
    for(int j=0; j<rows.getLength(); j++)
    {
        Element row = (Element)rows.item(j);

        String sName = row.getAttribute("name");
        if(sName.length()<1){
            String s = "tdSet" + i + "_Row" + j + "_name";
            %><tr>
		<td class="head" id="<%=s%>"><%=genText(s)%></td>
		<%
        }else{
            %>
	
	<tr>
		<td class="head"><%= sName %></td>
		<%
        }

        if(colCount>0){
            int n=0;
            NodeList cells = row.getElementsByTagName("cell");
            for(int k=1; k<colCount; k++){
                Element cell = (Element)cells.item(n);
                    if (cell != null){
                        if (String.valueOf(k).equals(cell.getAttribute("index") ) ){
                            String s = "tdSet" + i + "_Row" + j + "_Col" + k;
                            %><td id="<%=s%>"><%= genCell(s, cell)%></td>
		<%
                            n++;
                        }else{
                            %><td class="grey">&nbsp;</td>
		<%
                        }
                    }else{
                            %><td class="grey">&nbsp;</td>
		<%
                    }
            }
        }
        else
        {
            NodeList cells = row.getElementsByTagName("cell");
            for(int k=0; k<cells.getLength(); k++)
            {
                Element cell = (Element)cells.item(k);
                if(cell.getAttribute("index").equals(String.valueOf(k+1))){
                    String s = "tdSet" + i + "_Row" + j + "_Col" + (k+1);
                    %><td id="<%=s%>"><%= genCell(s, cell)%></td>
		<%
                }
                else{
                    %><td class="grey">&nbsp;</td>
		<%
                }
            }
        }

        String s = "tdSet" + i + "_Row" + j + "_comments";

        %><td id="<%=s%>"><%= genText(s)%></td>
		<%


        %>
	</tr>
	<%
    }

    %>
</table>
<%


%> <%!
String genText(String id)
{
    String s = "<span style='width:100%'>";

  //  s += "<input type=text style='width:100%;' id='" + id + "_text'></input>";
    s += "&nbsp;";
    s += "</span>";

    return s;
}

String genCell(String id, Element cell)
{
    String s = "<span style='width:100%'>";

    String givenDate = cell.getAttribute("givenDate");
    String refusedDate = cell.getAttribute("refusedDate");
    String lot = cell.getAttribute("lot");
    String provider = cell.getAttribute("provider");
	String comments = cell.getAttribute("comments");
	
    s += "<input type=hidden id='" + id + "_givenDate'>"
             + givenDate + "</input>"
             + "<input type=hidden id='" + id + "_refusedDate'>"
             + refusedDate + "</input>"
             + "<input type=hidden id='" + id + "_lot'>"
             + lot + "</input>"
             + "<input type=hidden id='" + id + "_provider'>"
             + provider + "</input>"
             +"<input type=hidden id='" + id + "_comments'>"
             + comments + "</input>";

    s += "<span id='" + id + "_label'>";
    if(givenDate.length()>0)
    {
        s += givenDate;
    }
    else
    {
        if(refusedDate.length()>0)
        {
            s += "Refused";
        }
    }
    s += "</span>";

    s += "<span style='width:100%; text-align:right'><a href=\"javascript:edit('" + id + "');\"><img border=0 src='../img/edit.gif' /></a></span>";

    s += "</span>";

    return s;
}




%> <%/*++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/%>
</form>

</body>
</html:html>
