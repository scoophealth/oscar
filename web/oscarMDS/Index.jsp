<%@ page language="java" %>
<%@ page import="java.util.*" %>
<%@ page import="oscar.oscarMDS.data.ProviderData" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%
    if(session.getValue("user") == null) response.sendRedirect("../logout.jsp");

    oscar.oscarMDS.data.MDSResultsData mDSData = new oscar.oscarMDS.data.MDSResultsData();
    String providerNo = request.getParameter("providerNo");
    String searchProviderNo = request.getParameter("searchProviderNo");
    String ackStatus = request.getParameter("status");
    String demographicNo = request.getParameter("demographicNo"); // used when searching for labs by patient instead of provider

    if ( ackStatus == null ) { ackStatus = "N"; } // default to only new lab reports
    if ( providerNo == null ) { providerNo = ""; }
    if ( searchProviderNo == null ) { searchProviderNo = providerNo; }
    mDSData.populateMDSResultsData(searchProviderNo, demographicNo, request.getParameter("fname"), request.getParameter("lname"), request.getParameter("hnum"), ackStatus);
%>
<link rel="stylesheet" type="text/css" href="encounterStyles.css">
<style type="text/css">
<!--
.RollRes     { font-weight: 700; font-size: 8pt; color: white; font-family: 
               Verdana, Arial, Helvetica }
.RollRes a:link { color: white }
.RollRes a:hover { color: white }
.RollRes a:visited { color: white }
.RollRes a:active { color: white }
.AbnormalRollRes { font-weight: 700; font-size: 8pt; color: red; font-family: 
               Verdana, Arial, Helvetica }
.AbnormalRollRes a:link { color: red }
.AbnormalRollRes a:hover { color: red }
.AbnormalRollRes a:visited { color: red }
.AbnormalRollRes a:active { color: red }
.CorrectedRollRes { font-weight: 700; font-size: 8pt; color: yellow; font-family: 
               Verdana, Arial, Helvetica }
.CorrectedRollRes a:link { color: yellow }
.CorrectedRollRes a:hover { color: yellow }
.CorrectedRollRes a:visited { color: yellow }
.CorrectedRollRes a:active { color: yellow }
.AbnormalRes { font-weight: bold; font-size: 8pt; color: red; font-family: 
               Verdana, Arial, Helvetica; height: 25px }
.AbnormalRes a:link { color: red }
.AbnormalRes a:hover { color: red }
.AbnormalRes a:visited { color: red }
.AbnormalRes a:active { color: red }
.NormalRes   { font-weight: bold; font-size: 8pt; color: black; font-family: 
               Verdana, Arial, Helvetica; height: 25px }
.NormalRes a:link { color: black }
.NormalRes a:hover { color: black }
.NormalRes a:visited { color: black }
.NormalRes a:active { color: black }
.HiLoRes     { font-weight: bold; font-size: 8pt; color: blue; font-family: 
               Verdana, Arial, Helvetica; height: 25px }
.HiLoRes a:link { color: blue }
.HiLoRes a:hover { color: blue }
.HiLoRes a:visited { color: blue }
.HiLoRes a:active { color: blue }
.CorrectedRes { font-weight: bold; font-size: 8pt; color: #4d8977; font-family: 
               Verdana, Arial, Helvetica }
.CorrectedRes a:link { color: #6da997 }
.CorrectedRes a:hover { color: #6da997 }
.CorrectedRes a:visited { color: #6da997 }
.CorrectedRes a:active { color: #6da997 }
.Field       { font-weight: bold; font-size: 8.5pt; color: black; font-family: 
               Verdana, Arial, Helvetica }
div.Field a:link { color: black }
div.Field a:hover { color: black }
div.Field a:visited { color: black }
div.Field a:active { color: black }
.Field2      { font-weight: bold; font-size: 8pt; color: #ffffff; font-family: 
               Verdana, Arial, Helvetica }
div.Field2   { font-weight: bold; font-size: 8pt; color: #ffffff; font-family: 
               Verdana, Arial, Helvetica }
div.FieldData { font-weight: normal; font-size: 8pt; color: black; font-family: 
               Verdana, Arial, Helvetica }
div.Field3   { font-weight: normal; font-size: 8pt; color: black; font-style: italic; 
               font-family: Verdana, Arial, Helvetica }
div.Title    { font-weight: 800; font-size: 10pt; color: white; font-family: 
               Verdana, Arial, Helvetica; padding-top: 4pt; padding-bottom: 
               2pt }
div.Title a:link { color: white }
div.Title a:hover { color: white }
div.Title a:visited { color: white }
div.Title a:active { color: white }
div.Title2   { font-weight: bolder; font-size: 9pt; color: black; text-indent: 5pt; 
               font-family: Verdana, Arial, Helvetica; padding-bottom: 2pt }
div.Title2 a:link { color: black }
div.Title2 a:hover { color: black }
div.Title2 a:visited { color: black }
div.Title2 a:active { color: black }
.Cell        { background-color: #9999CC; border-left: thin solid #CCCCFF; 
               border-right: thin solid #6666CC; 
               border-top: thin solid #CCCCFF; 
               border-bottom: thin solid #6666CC;
               height: 40px;
               font-weight: bold; 
               font-size: 8pt; 
               color: #ffffff; 
               font-family: Verdana, Arial, Helvetica } 
.Cell2       { background-color: #376c95; border-left-style: none; border-left-width: medium; 
               border-right-style: none; border-right-width: medium; 
               border-top: thin none #bfcbe3; border-bottom-style: none; 
               border-bottom-width: medium }
.Cell3       { background-color: #add9c7; border-left: thin solid #dbfdeb; 
               border-right: thin solid #5d9987; 
               border-top: thin solid #dbfdeb; 
               border-bottom: thin solid #5d9987 }
.CellHdr     { background-color: #cbe5d7; border-right-style: none; border-right-width: 
               medium; border-bottom-style: none; border-bottom-width: medium }
.Nav         { font-weight: bold; font-size: 8pt; color: white; font-family: 
               Verdana, Arial, Helvetica }
div.Nav a:link { color: white }
div.Nav a:hover { color: #eeeeee }
div.Nav a:visited { color: white }
div.Nav a:active { color: #eeeeee }
.PageLink a:link { font-size: 8pt; color: white }
.PageLink a:hover { color: red }
.PageLink a:visited { font-size: 9pt; color: yellow }
.PageLink a:active { font-size: 12pt; color: yellow }
.PageLink    { font-family: Verdana }
.text1       { font-size: 8pt; color: black; font-family: Verdana, Arial, Helvetica }
div.txt1     { font-size: 8pt; color: black; font-family: Verdana, Arial }
div.txt2     { font-weight: bolder; font-size: 6pt; color: black; font-family: Verdana, Arial }
div.Title3   { font-weight: bolder; font-size: 12pt; color: black; font-family: 
               Verdana, Arial }
.red         { color: red }
.text2       { font-size: 7pt; color: black; font-family: Verdana, Arial }
.white       { color: white }
.title1      { font-size: 9pt; color: black; font-family: Verdana, Arial }
div.Title4   { font-weight: 600; font-size: 8pt; color: white; font-family: 
               Verdana, Arial, Helvetica }
.smallButton { font-size: 8pt; }
-->
</style>

<html>
<head>
<title>
<bean:message key="oscarMDS.index.title"/>
</title>
</head>

<script type="text/javascript" language=javascript>

function popupStart(vheight,vwidth,varpage) {
    popupStart(vheight,vwidth,varpage,"helpwindow");
}

function popupStart(vheight,vwidth,varpage,windowname) {
    var page = varpage;
    windowprops = "height="+vheight+",width="+vwidth+",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes";
    var popup=window.open(varpage, windowname, windowprops);
}

function reportWindow(page) {
    windowprops="height=660, width=960, location=no, scrollbars=yes, menubars=no, toolbars=no, resizable=yes, top=0, left=0";
    var popup = window.open(page, "labreport", windowprops);
    popup.focus();
}

function checkSelected() {
    aBoxIsChecked = false;    
    if (this.reassignForm.flaggedLabs.length == undefined) {
        if (this.reassignForm.flaggedLabs.checked == true) {
            aBoxIsChecked = true;
        }
    } else {
        for (i=0; i < this.reassignForm.flaggedLabs.length; i++) {
            if (this.reassignForm.flaggedLabs[i].checked == true) {
                aBoxIsChecked = true;
            }
        }
    }
    if (aBoxIsChecked) {
        popupStart(300, 400, 'SelectProvider.jsp', 'providerselect');
    } else {
        alert('<bean:message key="oscarMDS.index.msgSelectOneLab"/>');
    }
}

</script>

<body oldclass="BodyStyle" vlink="#0000FF" >
    <form name="reassignForm" method="post" action="ReportReassign.do">
        <table  oldclass="MainTable" id="scrollNumber1" border="0" name="encounterTable" cellspacing="0" cellpadding="3" width="100%">
            <tr oldclass="MainTableTopRow">
                <td class="MainTableTopRowRightColumn" colspan="9" align="left">                       
                    <table width="100%">
                        <tr> 
                            <td align="left" valign="center" width="30%">
                                <input type="hidden" name="providerNo" value="<%= providerNo %>">
                                <input type="hidden" name="searchProviderNo" value="<%= searchProviderNo %>">
                                <%= (request.getParameter("lname") == null ? "" : "<input type=\"hidden\" name=\"lname\" value=\""+request.getParameter("lname")+"\">") %>
                                <%= (request.getParameter("fname") == null ? "" : "<input type=\"hidden\" name=\"fname\" value=\""+request.getParameter("fname")+"\">") %>
                                <%= (request.getParameter("hnum") == null ? "" : "<input type=\"hidden\" name=\"hnum\" value=\""+request.getParameter("hnum")+"\">") %>                                
                                <input type="hidden" name="status" value="<%= ackStatus %>">                                
                                <input type="hidden" name="selectedProviders">
                                <% if (demographicNo == null) { %>
                                    <input type="button" class="smallButton" value="<bean:message key="oscarMDS.index.btnSearch"/>" onClick="window.location='Search.jsp?providerNo=<%= providerNo %>'">
                                <% } %>                                
                                <input type="button" class="smallButton" value="<bean:message key="oscarMDS.index.btnClose"/>" onClick="window.close()">
                                <% if (demographicNo == null && request.getParameter("fname") != null) { %>
                                    <input type="button" class="smallButton" value="<bean:message key="oscarMDS.index.btnDefaultView"/>" onClick="window.location='Index.jsp?providerNo=<%= providerNo %>'">
                                <% } %>
                                <% if (demographicNo == null && mDSData.segmentID.size() > 0) { %>
                                    <!-- <input type="button" class="smallButton" value="Reassign" onClick="popupStart(300, 400, 'SelectProvider.jsp', 'providerselect')"> -->
                                    <input type="button" class="smallButton" value="<bean:message key="oscarMDS.index.btnForward"/>" onClick="checkSelected()">
                                <% } %>
                            </td>
                            <td align="center" valign="center" width="40%" class="Nav">
                                &nbsp;&nbsp;&nbsp;
                                <% if (demographicNo == null) { %>
                                    <span class="white"><%= ( ackStatus.equals("N") ? "New" : ( ackStatus.equals("A") ? "Acknowledged" : "All" )) %> lab reports for <%= ( searchProviderNo.equals("") ? "all physicians" : ( searchProviderNo.equals("0") ? "unclaimed" : ProviderData.getProviderName(searchProviderNo) ) ) %></span>                                
                                <% } %>
                            </td>                            
                            <td align="right" valign="center" width="30%">
                                <a href="javascript:popupStart(300,400,'../oscarEncounter/Help.jsp')"><bean:message key="global.help"/></a> | <a href="javascript:popupStart(300,400,'../oscarEncounter/About.jsp')" ><bean:message key="global.about"/></a>
                            </td>
                        </tr>
                    </table>                        
                </td>
            </tr>
            <tr>
                <th align="left" valign="bottom" class="cell">
                    <bean:message key="oscarMDS.index.msgHealthNumber"/>
                </th>
                <th align="left" valign="bottom" class="cell">
                    <bean:message key="oscarMDS.index.msgPatientName"/>
                </th>
                <th align="left" valign="bottom" class="cell">
                    <bean:message key="oscarMDS.index.msgSex"/>
                </th>
                <th align="left" valign="bottom" class="cell">
                    <bean:message key="oscarMDS.index.msgResultStatus"/>
                </th>
                <th align="left" valign="bottom" class="cell">
                    <bean:message key="oscarMDS.index.msgDateTest"/>
                </th>
                <th align="left" valign="bottom" class="cell">
                    <bean:message key="oscarMDS.index.msgOrderPriority"/>
                </th>
                <th align="left" valign="bottom" class="cell">
                    <bean:message key="oscarMDS.index.msgRequestingClient"/>
                </th>
                <th align="left" valign="bottom" class="cell">
                    <bean:message key="oscarMDS.index.msgDiscipline"/>
                </th>
                <th align="left" valign="bottom" class="cell">
                    <bean:message key="oscarMDS.index.msgReportStatus"/>
                </th>

            </tr>

        <%  int pageNum = 1;
            if ( request.getParameter("pageNum") != null ) {
                pageNum = Integer.parseInt(request.getParameter("pageNum"));
            }
            int startIndex = 0;
            if ( request.getParameter("startIndex") != null ) {
                startIndex = Integer.parseInt(request.getParameter("startIndex"));
            }
            int endIndex = startIndex+20;            
            if ( mDSData.segmentID.size() < endIndex ) {
                endIndex = mDSData.segmentID.size();
            }

            for (int i = startIndex; i < endIndex; i++) {
                String segmentID        = (String) mDSData.segmentID.get(i);
                String status           = (String) mDSData.acknowledgedStatus.get(i);

                String resultStatus     = (String) mDSData.resultStatus.get(i); %>

            <tr bgcolor="<%=(i % 2 == 0 ? "#e0e0ff" : "#ccccff" )%>" class="<%= (resultStatus.equals("0") ? "NormalRes" : "AbnormalRes") %>">
                <td nowrap>
                    <input type="checkbox" name="flaggedLabs" value="<%=segmentID%>"> 
                    <%= ((String) mDSData.healthNumber.get(i)).substring(1) %>
                </td>
                <td nowrap>                                    
                    <a href="javascript:reportWindow('SegmentDisplay.jsp?segmentID=<%=segmentID%>&providerNo=<%=providerNo%>&searchProviderNo=<%=searchProviderNo%>&status=<%=status%>')"><%=(String) mDSData.patientName.get(i)%></a>
                </td>
                <td nowrap>
                    <center><%= (String) mDSData.sex.get(i) %></center>
                </td>
                <td nowrap>
                    <%= (resultStatus.equals("0") ? "" : "Abnormal") %>
                </td>
                <td nowrap>
                    <%= (String) mDSData.dateTime.get(i)%>
                </td>
                <td nowrap>
                    <%= (String) mDSData.priority.get(i)%>
                </td>
                <td nowrap>
                    <%= (String) mDSData.requestingClient.get(i)%>
                </td>
                <td nowrap>
                    <%= (String) mDSData.discipline.get(i)%>
                </td>
                <td nowrap>                                    
                    <%= ( ((String) mDSData.reportStatus.get(i)).equals("0") ? "Partial" : "Final" ) %>
                </td>
            </tr>
         <% } 
            if ( endIndex == 0 ) { %>
            <tr>
                <td colspan="9" align="center">
                    <i><bean:message key="oscarMDS.index.msgNoReports"/></i>
                </td>
            </tr>
         <% } %>
            <tr class="MainTableBottomRow">
                <td class="MainTableBottomRowRightColumn" bgcolor="#003399" colspan="9" align="left">                       
                    <table width="100%">
                        <tr> 
                            <td align="left" valign="middle" width="30%">                                
                                <% if (demographicNo == null) { %>
                                    <input type="button" class="smallButton" value="<bean:message key="oscarMDS.index.btnSearch"/>" onClick="window.location='Search.jsp?providerNo=<%= providerNo %>'">
                                <% } %>                                
                                <input type="button" class="smallButton" value="<bean:message key="oscarMDS.index.btnClose"/>" onClick="window.close()">
                                <% if (request.getParameter("fname") != null) { %>
                                    <input type="button" class="smallButton" value="<bean:message key="oscarMDS.index.btnDefaultView"/>" onClick="window.location='Index.jsp?providerNo=<%= providerNo %>'">
                                <% } %>                             
                                <% if (demographicNo == null && mDSData.segmentID.size() > 0) { %>
                                    <!-- <input type="button" class="smallButton" value="Reassign" onClick="popupStart(300, 400, 'SelectProvider.jsp', 'providerselect')"> -->
                                    <input type="button" class="smallButton" value="<bean:message key="oscarMDS.index.btnFoward"/>" onClick="checkSelected()">
                                <% } %>
                            </td>
                            <td align="center" valign="middle" width="40%">
                                <div class="Nav">
                                <% if ( pageNum > 1 || mDSData.segmentID.size() > endIndex ) {
                                    if ( pageNum > 1 ) { %>
                                        <a href="Index.jsp?providerNo=<%=providerNo%><%= (demographicNo == null ? "" : "&demographicNo="+demographicNo ) %>&searchProviderNo=<%=searchProviderNo%>&status=<%=ackStatus%><%= (request.getParameter("lname") == null ? "" : "&lname="+request.getParameter("lname")) %><%= (request.getParameter("fname") == null ? "" : "&fname="+request.getParameter("fname")) %><%= (request.getParameter("hnum") == null ? "" : "&hnum="+request.getParameter("hnum")) %>&pageNum=<%=pageNum-1%>&startIndex=<%=startIndex-20%>">< <bean:message key="oscarMDS.index.btnPrevious"/></a>
                                 <% } else { %>
                                        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                 <% } %>
                                       [<%= pageNum %>] 
                                 <% if ( mDSData.segmentID.size() > endIndex ) { %>
                                        <a href="Index.jsp?providerNo=<%=providerNo%><%= (demographicNo == null ? "" : "&demographicNo="+demographicNo ) %>&searchProviderNo=<%=searchProviderNo%>&status=<%=ackStatus%><%= (request.getParameter("lname") == null ? "" : "&lname="+request.getParameter("lname")) %><%= (request.getParameter("fname") == null ? "" : "&fname="+request.getParameter("fname")) %><%= (request.getParameter("hnum") == null ? "" : "&hnum="+request.getParameter("hnum")) %>&pageNum=<%=pageNum+1%>&startIndex=<%=startIndex+20%>"><bean:message key="oscarMDS.index.btnNext"/> ></a>
                                 
                                 <% } else { %>
                                        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                 <% }
                                   } %>
                                 </div>
                            </td>
                            <td align="right" width="30%">
                               &nbsp;
                            </td>
                        </tr>
                    </table>                        
                </td>
            </tr>
        </table>
    </form>
</body>
</html>
