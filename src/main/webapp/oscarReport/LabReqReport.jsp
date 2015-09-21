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
<security:oscarSec roleName="<%=roleName$%>" objectName="_report,_admin.reporting" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../securityError.jsp?type=_report&type=_admin.reporting");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>

<%@ page import="java.util.*,oscar.oscarReport.data.*"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<link rel="stylesheet" type="text/css"
	href="../oscarEncounter/encounterStyles.css">
<%  //This could be done alot better.  
  String curUser_no,userfirstname,userlastname;
  curUser_no = (String) session.getAttribute("user");



String mons = "1";
String pros = curUser_no;
if (request.getParameter("numMonth") != null ){
mons = request.getParameter("numMonth");
}

if (request.getParameter("proNo") != null ){
pros = request.getParameter("proNo");
}

oscar.oscarReport.data.RptLabReportData conData  = new oscar.oscarReport.data.RptLabReportData();
conData.labReportGenerate(pros,mons);
ArrayList proList = conData.providerList();

Calendar cal = Calendar.getInstance();
int months = 12;
try{
months = Integer.parseInt(mons);
}catch(Exception e){}


cal.add(Calendar.MONTH,-months);

%>
<%!
  String selled (String i,String mons){
         String retval = "";
         if ( i.equals(mons) ){
            retval = "selected";
         }
     return retval;
  }
%>



<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title><bean:message key="oscarReport.LabReqReport.msgLabReqReport"/> <%= mons %></title>
<link rel="stylesheet" type="text/css" media="all" href="../share/css/extractedFromPages.css"  />

<script type="text/javascript">
   var remote=null;

   function rs(n,u,w,h,x) {
      args="width="+w+",height="+h+",resizable=yes,scrollbars=yes,status=0,top=60,left=30";
      remote=window.open(u,n,args);
     // if (remote != null) {
     //    if (remote.opener == null)
     //        remote.opener = self;
     // }
     // if (x == 1) { return remote; }
   }

   function popupOscarConsultationConfig(vheight,vwidth,varpage) { //open a new popup window
     var page = varpage;
     windowprops = "height="+vheight+",width="+vwidth+",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes,screenX=0,screenY=0,top=0,left=0";
     var popup=window.open(varpage, "OscarConsultationConfig", windowprops);
     if (popup != null) {
       if (popup.opener == null) {
        popup.opener = self;
       }
    }
  }
</script>

</head>

<body class="BodyStyle" vlink="#0000FF">
<!--  -->
<table class="MainTable" id="scrollNumber1" name="encounterTable">
	<tr class="MainTableTopRow">
		<td class="MainTableTopRowLeftColumn"><bean:message key="oscarReport.LabReqReport.msgReport"/></td>
		<td class="MainTableTopRowRightColumn">
		<table class="TopStatusBar">
			<form action="LabReqReport.jsp">
			<tr>
				<td><bean:message key="oscarReport.LabReqReport.msgLabReqReport"/></td>
				<td><select name="numMonth">
					<option value="1" <%=selled("1",mons)%>>1 <bean:message key="oscarReport.LabReqReport.msgMonth"/></option>
					<option value="2" <%=selled("2",mons)%>>2 <bean:message key="oscarReport.LabReqReport.msgMonths"/></option>
					<option value="3" <%=selled("3",mons)%>>3 <bean:message key="oscarReport.LabReqReport.msgMonths"/></option>
					<option value="4" <%=selled("4",mons)%>>4 <bean:message key="oscarReport.LabReqReport.msgMonths"/></option>
					<option value="5" <%=selled("5",mons)%>>5 <bean:message key="oscarReport.LabReqReport.msgMonths"/></option>
					<option value="6" <%=selled("6",mons)%>>6 <bean:message key="oscarReport.LabReqReport.msgMonths"/></option>
					<option value="7" <%=selled("7",mons)%>>7 <bean:message key="oscarReport.LabReqReport.msgMonths"/></option>
					<option value="8" <%=selled("8",mons)%>>8 <bean:message key="oscarReport.LabReqReport.msgMonths"/></option>
					<option value="9" <%=selled("9",mons)%>>9 <bean:message key="oscarReport.LabReqReport.msgMonths"/></option>
					<option value="10" <%=selled("10",mons)%>>10 <bean:message key="oscarReport.LabReqReport.msgMonths"/></option>
					<option value="11" <%=selled("11",mons)%>>11 <bean:message key="oscarReport.LabReqReport.msgMonths"/></option>
					<option value="12" <%=selled("12",mons)%>>12 <bean:message key="oscarReport.LabReqReport.msgMonths"/></option>
				</select> <select name="proNo">
					<option value="-1" <%=selled("-1",pros)%>><bean:message key="oscarReport.LabReqReport.msgAllProviders"/></option>
					<%
                                  for( int i = 0; i < proList.size(); i++){
                                     ArrayList w = (ArrayList) proList.get(i);
                                     String proNum  = (String) w.get(0);
                                     String proName = (String) w.get(1);
                              %>
					<option value="<%=proNum%>" <%=selled(proNum,pros)%>><%=proName%></option>
					<% 
                                  }
                              %>
				</select> <input type=submit value="<bean:message key="oscarReport.LabReqReport.msgUpdateReport" />"></td>
				<td style="text-align: right"><oscar:help keywords="report" key="app.top1"/> | <a
					href="javascript:popupStart(300,400,'About.jsp')"><bean:message key="global.about"/></a> | <a
					href="javascript:popupStart(300,400,'License.jsp')"><bean:message key="global.license"/></a></td>
			</tr>
			</form>
		</table>
		</td>
	</tr>
	<tr>            
		<td class="MainTableLeftColumn">&nbsp;</td>
		<td class="MainTableRightColumn">
		<table border=0 cellspacing=4 width=700>
			<%
oscar.oscarReport.data.RptLabReportData.DemoLabDataStruct  demoData;
for (int i = 0; i < conData.demoList.size(); i++ ){
demoData = (RptLabReportData.DemoLabDataStruct)  conData.demoList.get(i);
%>
			<tr>
				<td bgcolor="#eeeeff" width=700>
				<table border=0 cellspacing=2>
					<tr>
						<td class="nameBox" colspan=3><bean:message key="oscarReport.LabReqReport.msgPatientName"/>: <%= demoData.getDemographicName()%></td>
					</tr>
					<tr>

						<td valign=top width=300 class=sideLine>

						<table border=0 cellspacing=3 width=100%>
							<td colspan=4 class=nameBox><bean:message key="oscarReport.LabReqReport.msgLabReq"/></td>
							<tr>
								<th width=100 class="subTitles" align=left><bean:message key="oscarReport.LabReqReport.msgReqDate"/></th>
							</tr>
							<%
                  RptLabReportData.DemoLabDataStruct.Consult demoLab;
                  java.util.ArrayList labL =  demoData.getLabReqs();
                  for (int j = 0; j < labL.size(); j++){
                  demoLab = (RptLabReportData.DemoLabDataStruct.Consult)  labL.get(j);
                  %>
							<tr>
								<td class="fieldBox" bgcolor="#ddddff"><a
									href="javascript:popupOscarConsultationConfig(700,960,'../form/formlabreq.jsp?demographic_no=<%=demoData.demoNo%>&formId=<%=demoLab.requestId%>&provNo=<%=demoLab.proNo%>&readOnly=true')"><%=demoLab.referalDate%></a>
								</td>
							</tr>
							<%
                  }
     %>
						</table>
						</td>
						<td valign=top width=300>
						<table border=0 cellspacing=3>
							<tr>
								<td class=nameBox colspan=2><bean:message key="oscarReport.LabReqReport.msgScannedLabDocs"/></td>
							</tr>
							<tr>
								<th width=200 class="subTitles" align=left><bean:message key="oscarReport.LabReqReport.msgDocumentDesc"/></th>
								<th width=100 class="subTitles" align=left><bean:message key="oscarReport.LabReqReport.msgDate"/></th>
							</tr>
							<%
                  RptLabReportData.DemoLabDataStruct.ConLetter demoLetter;
                  java.util.ArrayList letL = demoData.getLabReplys();
                  for (int j = 0; j < letL.size(); j++){
                  demoLetter = (RptLabReportData.DemoLabDataStruct.ConLetter) letL.get(j);

                  %>
							<tr>
								<td class="fieldBox" bgcolor="#deddff"><a href=#
									onclick="javascript:rs('new','../dms/documentGetFile.jsp?document=<%=demoLetter.docfileName%>&type=active&doc_no=<%=demoLetter.document_no%>', 480,480,1)"><%=demoLetter.docdesc%></a>
								</td>
								<td class="fieldBox" bgcolor="#deddff"><%=demoLetter.docDate.toString()%></td>
							</tr>
							<%
                  }
                  %>


						</table>
						</td>
						<td valign=top width=300>
						<table border=0 cellspacing=3>
							<tr>
								<td class=nameBox colspan=2><bean:message key="oscarReport.LabReqReport.msgLabDocuments"/></td>
							</tr>
							<tr>
								<th width=200 class="subTitles" align=left><bean:message key="oscarReport.LabReqReport.msgDocumentDesc"/></th>
								<th width=100 class="subTitles" align=left><bean:message key="oscarReport.LabReqReport.msgDate"/></th>
							</tr>
							<%
                    ArrayList  labRep = demoData.getLabReports(demoData.demoNo, cal.getTime()); 
                    for (int j = 0; j < labRep.size(); j++){
                    Hashtable h = (Hashtable) labRep.get(j);
                    String colDate = (String) h.get("collectionDate");
                    String lab_no = (String)  h.get("id");
                    String labType = (String) h.get("labType");
                  //get labs
                    String labURL = "";
                    
                    if ( labType != null && labType.equals("CML") ){ 
                       labURL = "../lab/CA/ON/CMLDisplay.jsp?providerNo="+pros+"&segmentID="+lab_no;                                            
                    }else{ 
                       labURL = "../oscarMDS/SegmentDisplay.jsp?providerNo="+pros+"&segmentID="+lab_no;
                    } 

                  %>
							<tr>
								<td class="fieldBox" bgcolor="#deddff"><a href=#
									onclick="javascript:rs('new2','<%=labURL%>', 850,600,1)">lab</a>
								</td>
								<td class="fieldBox" bgcolor="#deddff"><%=((String) h.get("collectionDate"))%></td>
							</tr>
							<%
                  }
                  %>
						</table>
						</td>

					</tr>
				</table>
				</td>
			</tr>
			<%
}
%>
		</table>

		</td>
	</tr>
	<tr>
		<td class="MainTableBottomRowLeftColumn"></td>
		<td class="MainTableBottomRowRightColumn"></td>
	</tr>
</table>
</body>
</html>
