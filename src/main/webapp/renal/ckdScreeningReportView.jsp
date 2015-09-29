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
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%@page import="org.oscarehr.util.LoggedInInfo"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@ include file="/taglibs.jsp"%>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
      String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
      boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_report" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../securityError.jsp?type=_report");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>

<%@page import="java.util.*"%>
<%@page import="org.oscarehr.util.SpringUtils" %>
<%@page import="org.oscarehr.common.model.Provider" %>
<%@page import="org.oscarehr.renal.CkdScreener" %>
<%@page import="org.oscarehr.renal.CkdScreenerReportHandler" %>
<%@page import="org.oscarehr.renal.CKDReportContainer" %>
<%@page import="org.oscarehr.PMmodule.dao.ProviderDao" %>
<%@page import="javax.xml.bind.JAXBContext" %>
<%@page import="javax.xml.bind.Unmarshaller" %>
<%@page import="org.oscarehr.renal.CkdScreeningReportContainer" %>
<%@page import="org.oscarehr.common.dao.ORNCkdScreeningReportLogDao" %>
<%@page import="org.oscarehr.common.model.ORNCkdScreeningReportLog" %>

<%
	ProviderDao providerDao = SpringUtils.getBean(ProviderDao.class);
	
	List<CKDReportContainer> ckds = null;
	
	String labReqVer = oscar.OscarProperties.getInstance().getProperty("onare_labreqver","07");
	if(labReqVer.equals("")) {labReqVer="07";}
	
	LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
	Provider provider = loggedInInfo.getLoggedInProvider();

	CkdScreeningReportContainer r = null;

	java.text.DecimalFormat df = new java.text.DecimalFormat("0.00");
	String id = request.getParameter("id");
	ORNCkdScreeningReportLogDao dao = SpringUtils.getBean(ORNCkdScreeningReportLogDao.class);
	ORNCkdScreeningReportLog report = dao.find(Integer.parseInt(id));
	if(report != null) {
		JAXBContext context = JAXBContext.newInstance(CkdScreeningReportContainer.class);
	    Unmarshaller um = context.createUnmarshaller();
	     r = (CkdScreeningReportContainer) um.unmarshal(new java.io.StringReader(report.getReportData()));
	     ckds = r.getItems();
	} else {
		ckds = new ArrayList<CKDReportContainer>();
	}
%>


<html:html locale="true">
<head>
<script src="<%=request.getContextPath() %>/js/jquery-1.7.1.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath() %>/js/jquery.dataTables.js" type="text/javascript"></script>

<script>
	function createTickler(id) {
		jQuery.ajax({url:'../renal/SendTickler.do?method=sendtickler&demographic_no='+id,async:false, success:function(data) {
			  alert('tickler sent');		  
		}});	
	}
	
	function generateLetter(demographicNo) {
		window.open('../renal/Renal.do?method=generatePatientLetter&demographic_no='+demographicNo);
	}
	
	function generateLetterAndEmail(demographicNo) {
		
		jQuery.ajax({url:'../renal/Renal.do?method=sendPatientLetterAsEmail&demographic_no='+demographicNo,async:false, dataType:'json', success:function(data) {
			if(data.success == 'true')
			  alert('email sent');		
			else
				alert(data.error);
		}});	
		//window.open('../renal/Renal.do?method=generatePatientLetter&demographic_no='+demographicNo);
	}
	
	function generateLabReq(demographicNo) {
		var url = '<%=request.getContextPath()%>/form/formlabreq<%=labReqVer%>.jsp?demographic_no='+demographicNo+'&formId=0&provNo=<%=session.getAttribute("user")%>&fromSession=true';
		jQuery.ajax({url:'<%=request.getContextPath()%>/renal/Renal.do?method=createLabReq&demographicNo='+demographicNo,async:false, success:function(data) {
			popPage(url,'LabReq');
		}});
	}
	
    function popPage(varpage,pageName) {
        windowprops = "height=700,width=960"+
            ",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=no,screenX=50,screenY=50,top=20,left=20";
        var popup = window.open(varpage,pageName, windowprops);
        //if (popup.opener == null) {
        //    popup.opener = self;
        //}
        popup.focus();
    }
    
    function popupPage(vheight,vwidth,varpage) { //open a new popup window
        var page = "" + varpage;
        windowprops = "height="+vheight+",width="+vwidth+",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes";
        var popup=window.open(page, "attachment", windowprops);
        if (popup != null) {
          if (popup.opener == null) {
            popup.opener = self;
          }
        }
      }
    
</script>
<title>CKD Report</title>
<link rel="stylesheet" type="text/css" href="../share/css/OscarStandardLayout.css">


<style title="currentStyle" type="text/css">
			@import "../css/demo_page.css";
			@import "../css/demo_table.css";
</style>

<style>
body
{
	//text-align: center;
}

div#demo
{
	margin-left: auto;
	margin-right: auto;
	width: 100%;
	text-align: left;
}
</style>
<script type="text/javascript">
	$(document).ready(function() {
		$('#ckdTable').dataTable({
	        "aaSorting": [[ 1, "desc" ]]
	    });
	} );
</script>
<style>
body{
    font-family:Arial, Helvetica, sans-serif; 
    font-size:13px;
}
.info {
    border: 1px solid;
    margin: 10px 0px;
    padding:15px 10px 15px 50px;
    background-repeat: no-repeat;
    background-position: 10px center;
}
.info {
    color: #00529B;
    background-color: #BDE5F8;
    background-image: url('../images/info_msgbox.png');
}

</style>
</head>

<body>

<Br/>
<h2 style="text-align:center">CKD Screening Report</h2>
<br/>
<div class="info">This table includes the set of patients which the ORN CKD algorithm has flagged as candidates for CKD screening</div>
<br/>
<div id="demo">
			<table id="ckdTable" cellpadding="0" cellspacing="0" border="0" class="display" width="100%">
				<thead>
					<tr>
						<th style="text-align:left">Patient</th>
						<th style="text-align:center">Phone</th>
						<th style="text-align:center">Sex</th>
						<th style="text-align:center">Age</th>
						<th style="text-align:center">Aboriginal</th>
						<th style="text-align:center">Diabetic</th>
						<th style="text-align:center">Hypertensive</th>
						<th style="text-align:center" title="Current or previous use of hypertensive medications">Medication</th>
						<th style="text-align:center" title="50% or more blood pressure readings > 140/90">BP</th>
						<th style="text-align:center">Family Hx</th>
						<th style="text-align:center">Overdue Labs</th>
						<th style="text-align:left">Last Visit</th>
						<th style="text-align:center">MRP</th>
						<th></th>
					</tr>
				</thead>
				<tbody>
					<%
						SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
						
						for(int x=0;x<ckds.size();x++) {
							CKDReportContainer ckd = ckds.get(x);

					%>
					<tr class="gradeB">
						<td style="text-align:left">
							<%=ckd.getDemographic().getStandardIdentificationHTML() %>
						</td>
						<td style="text-align:left">
							Home: <%=ckd.getDemographic().getPhone() %><br/>
							Work: <%=ckd.getDemographic().getPhone2() %>
						</td>
						<td style="text-align:center"><%=ckd.getDemographic().getSex() %></td>
						<td style="text-align:right"><%=ckd.getDemographic().getAge() %></td>
						<td style="text-align:center"><%=printStandardAboriginal(ckd.getAboriginalStr()) %></td>
						<td style="text-align:center"><%=printStandardBoolean(ckd.isDiabetic()) %></td>
						<td style="text-align:center"><%=printStandardBoolean(ckd.isHypertensive()) %></td>
						<td style="text-align:center"><%=printStandardBoolean(ckd.isMedication()) %></td>
						<td style="text-align:center"><%=printStandardBoolean(ckd.isBp()) %></td>
						<td style="text-align:center"><%=printStandardBoolean(ckd.isHx())%></td>
						<td style="text-align:center"><%=printStandardBoolean(ckd.isLabs())%></td>
						<td><%=ckd.getLastVisit() %></td>
						<td style="text-align:left">
							<%
								String providerNo = ckd.getDemographic().getProviderNo();
								String providerName = "";
								if(providerNo != null && !providerNo.isEmpty()) {
									Provider p = providerDao.getProvider(providerNo);
									if(p != null) {
										providerName = p.getFormattedName();
									}
								}
							%>
							<%=providerName %>
						</td>
						
						<td nowrap="nowrap">
							<a title="Generate Patient Letter <%=ckd.getLastPatientLetter()==null?"":" | " + ckd.getLastPatientLetter() %>" href="javascript:void(0);" onclick="generateLetter(<%=ckd.getDemographic().getDemographicNo()%>);return false;"><img src="../images/notepad_blank.gif" border="0"/></a>
							<a title="Email Patient Letter <%=ckd.getLastPatientLetter()==null?"":" | " + ckd.getLastPatientLetter() %>" href="javascript:void(0);" onclick="generateLetterAndEmail(<%=ckd.getDemographic().getDemographicNo()%>);return false;"><img src="../images/email.jpg" border="0"/></a>						
							<a title="Create Lab Requisition  <%=ckd.getLastLabReq()==null?"":" | " + ckd.getLastLabReq() %>" href="javascript:void(0);" onclick="generateLabReq(<%=ckd.getDemographic().getDemographicNo()%>);return false;"><img src="../images/lab_icon.png" height="16" border="0"/></a>
							<a title="Add Chronic Renal Failure to Disease Registry, and disable further notifications" href="javascript.void(0);" onclick="popupPage(580,900,'../oscarResearch/oscarDxResearch/dxResearch.do?selectedCodingSystem=icd9&xml_research1=585&xml_research2=&xml_research3=&xml_research4=&xml_research5=&demographicNo=<%=ckd.getDemographic().getDemographicNo()%>&quickList=default&forward=');return false;"><img src="../images/kidney.jpg" height="16" border="0"/></a>
						</td>
					</tr>
					<%
						}
					%>
				</tbody>
			</table>
</div>


</html:html>

<%!
	String printStandardBoolean(boolean value) {
		if(value) {
			return "<span style=\"color:red\">True</span>";
		}
		return "False";

	}

	String printStandardAboriginal(String value) {
	if(value.equalsIgnoreCase("yes")) {
		return "<span style=\"color:red\">True</span>";
	}
	if(value.equalsIgnoreCase("no")) {
		return "False";
	}
	return "--";

}
%>
