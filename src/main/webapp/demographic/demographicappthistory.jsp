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
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="org.springframework.web.context.WebApplicationContext"%>
<%@page import="org.oscarehr.caisi_integrator.ws.DemographicWs"%>
<%@page import="org.oscarehr.PMmodule.caisi_integrator.IntegratorFallBackManager" %>

<%@ page import="java.util.*, java.sql.*, java.net.*, oscar.*, oscar.oscarDB.*" errorPage="errorpage.jsp"%>
<%@ page import="org.oscarehr.PMmodule.caisi_integrator.CaisiIntegratorManager, org.oscarehr.caisi_integrator.ws.CachedAppointment, org.oscarehr.caisi_integrator.ws.CachedProvider, org.oscarehr.util.LoggedInInfo" %>
<%@ page import="org.oscarehr.caisi_integrator.ws.*"%>

<%@page import="oscar.util.DateUtils"%>
<%@page import="org.apache.commons.lang.StringUtils"%>
<%@page import="org.oscarehr.util.MiscUtils" %>
<%@page import="org.oscarehr.util.SpringUtils" %>

<%@page import="org.oscarehr.common.dao.SiteDao"%>
<%@page import="org.oscarehr.common.model.Site"%>

<%@page import="org.oscarehr.common.dao.OscarAppointmentDao" %>
<%@page import="org.oscarehr.common.model.Appointment" %>

<%@ page import="org.oscarehr.common.model.ProviderData"%>
<%@ page import="org.oscarehr.common.dao.ProviderDataDao"%>


<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="http://www.caisi.ca/plugin-tag" prefix="plugin" %>
<%@ taglib uri="/WEB-INF/caisi-tag.tld" prefix="caisi" %>
<%@ taglib uri="/WEB-INF/special_tag.tld" prefix="special" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/oscar-tag.tld" prefix="oscar"%>


<%!
	private List<Site> sites = new ArrayList<Site>();
	private HashMap<String,String[]> siteBgColor = new HashMap<String,String[]>();
%>

<%
OscarAppointmentDao appointmentDao = (OscarAppointmentDao)SpringUtils.getBean("oscarAppointmentDao");
ProviderDataDao providerDao = SpringUtils.getBean(ProviderDataDao.class);


if (org.oscarehr.common.IsPropertiesOn.isMultisitesEnable()) {
	SiteDao siteDao = (SiteDao)WebApplicationContextUtils.getWebApplicationContext(application).getBean("siteDao");
	sites = siteDao.getAllActiveSites(); 
	//get all sites bgColors
	for (Site st : sites) {
		siteBgColor.put(st.getName(), new String[]{st.getBgColor(), st.getShortName()});
	}
}

  String curProvider_no = (String) session.getAttribute("user");
  String demographic_no = request.getParameter("demographic_no");
  String strLimit1="0";
  String strLimit2="25";
  if(request.getParameter("limit1")!=null) strLimit1 = request.getParameter("limit1");
  if(request.getParameter("limit2")!=null) strLimit2 = request.getParameter("limit2");
  
  String demolastname = request.getParameter("last_name")==null?"":request.getParameter("last_name");
  String demofirstname = request.getParameter("first_name")==null?"":request.getParameter("first_name");
  String deepColor = "#CCCCFF" , weakColor = "#EEEEFF" ;
%>


<html:html locale="true">

<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.js"></script> 
   <script>
     jQuery.noConflict();
   </script>
<c:set var="ctx" value="${pageContext.request.contextPath}" scope="request"/>
<script>
	var ctx = '<%=request.getContextPath()%>';
</script>

<oscar:customInterface section="appthistory"/>
   
<title><bean:message key="demographic.demographicappthistory.title" /></title>
<link rel="stylesheet" type="text/css" href="../share/css/OscarStandardLayout.css">
<script type="text/javascript">

	function popupPageNew(vheight,vwidth,varpage) {
	  var page = "" + varpage;
	  windowprops = "height="+vheight+",width="+vwidth+",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes";
	  var popup=window.open(page, "demographicprofile", windowprops);
	  if (popup != null) {
	    if (popup.opener == null) {
	      popup.opener = self;
	    }
	  }
	}

	function printVisit() {
		printVisit('');               
	}
	
	function printVisit(cpp) {
		var sels = document.getElementsByName('sel');
		var ids = "";
		for(var x=0;x<sels.length;x++) {
			if(sels[x].checked) {
				if(ids.length>0)
					ids+= ",";
				ids += sels[x].value;
			}
		}		
		location.href=ctx+"/eyeform/Eyeform.do?method=print&apptNos="+ids+"&cpp="+cpp;                
	}
	
	function selectAllCheckboxes() {
		jQuery("input[name='sel']").each(function(){
			jQuery(this).attr('checked',true);
		});
	}
	
	function deselectAllCheckboxes() {
		jQuery("input[name='sel']").each(function(){
			jQuery(this).attr('checked',false);
		});
	}

</script>

<link rel="stylesheet" type="text/css" media="all" href="../share/css/extractedFromPages.css"  />
</head>

<body class="BodyStyle"	demographic.demographicappthistory.msgTitle=vlink="#0000FF" onLoad="setValues()">

<table class="MainTable" id="scrollNumber1" name="encounterTable">
	<tr class="MainTableTopRow">
		<td class="MainTableTopRowLeftColumn"><bean:message
			key="demographic.demographicappthistory.msgHistory" /></td>
		<td class="MainTableTopRowRightColumn">
		<table class="TopStatusBar">
			<tr>
				<td><bean:message key="demographic.demographicappthistory.msgResults" />: <%=demolastname%>,<%=demofirstname%>(<%=request.getParameter("demographic_no")%>)</td>
				<td>&nbsp;</td>
				<td style="text-align: right"><oscar:help keywords="appointment history" key="app.top1"/> | <a href="javascript:popupStart(300,400,'About.jsp')">
					<bean:message key="global.about" /></a> | <a href="javascript:popupStart(300,400,'License.jsp')">
					<bean:message key="global.license" /></a>
				</td>
			</tr>
		</table>
		</td>
	</tr>
	<tr>
		<td class="MainTableLeftColumn" valign="top"><a	href="javascript:history.go(-1)" onMouseOver="self.status=document.referrer;return true">
			<bean:message key="global.btnBack" /></a> 
	    </td>
		<td class="MainTableRightColumn">
		<table width="95%" border="0" bgcolor="#ffffff" id="apptHistoryTbl">
			<tr  bgcolor="<%=deepColor%>">				
				<TH width="10%"><b><bean:message key="demographic.demographicappthistory.msgApptDate" /></b></TH>
				<TH width="10%"><b><bean:message key="demographic.demographicappthistory.msgFrom" /></b></TH>
				<TH width="10%"><b><bean:message key="demographic.demographicappthistory.msgTo" /></b></TH>
				<TH width="15%"><b><bean:message key="demographic.demographicappthistory.msgReason" /></b></TH>
				<TH width="15%"><b><bean:message key="demographic.demographicappthistory.msgProvider" /></b></TH>
				<plugin:hideWhenCompExists componentName="specialencounterComp" reverse="true">
				<special:SpecialEncounterTag moduleName="eyeform">   
					<TH width="5%"><b>EYE FORM</b></TH>
				</special:SpecialEncounterTag>
				</plugin:hideWhenCompExists>
				<TH><b><bean:message key="demographic.demographicappthistory.msgComments" /></b></TH>
				
				<% if (org.oscarehr.common.IsPropertiesOn.isMultisitesEnable()) { %>
					<TH width="5%">Location</TH>
				<% } %>
			</tr>
<%
  int iRSOffSet=0;
  int iPageSize=10;
  int iRow=0;
  if(request.getParameter("limit1")!=null) iRSOffSet= Integer.parseInt(request.getParameter("limit1"));
  if(request.getParameter("limit2")!=null) iPageSize = Integer.parseInt(request.getParameter("limit2"));
  
  List<Appointment> appointmentList = appointmentDao.getAppointmentHistory(new Integer(demographic_no));

  boolean bodd=false;
  int nItems=0;
  
  List<CachedAppointment> cachedAppointments = null;
  if (LoggedInInfo.loggedInInfo.get().currentFacility.isIntegratorEnabled()){
		int demographicNo = Integer.parseInt(request.getParameter("demographic_no"));
		try {
			if (!CaisiIntegratorManager.isIntegratorOffline()){
				cachedAppointments = CaisiIntegratorManager.getDemographicWs().getLinkedCachedAppointments(demographicNo);
			}
		} catch (Exception e) {
			MiscUtils.getLogger().error("Unexpected error.", e);
			CaisiIntegratorManager.checkForConnectionError(e);
		}
		
		if(CaisiIntegratorManager.isIntegratorOffline()){
			cachedAppointments = IntegratorFallBackManager.getRemoteAppointments(demographicNo);	
		}	
  }
  
  if (cachedAppointments != null) {
	  for (CachedAppointment a : cachedAppointments) {
		  bodd=bodd?false:true;
		  iRow++;
		  nItems++;
		  FacilityIdStringCompositePk providerPk=new FacilityIdStringCompositePk();
		  providerPk.setIntegratorFacilityId(a.getFacilityIdIntegerCompositePk().getIntegratorFacilityId());
		  providerPk.setCaisiItemId(a.getCaisiProviderId());
		  CachedProvider p = CaisiIntegratorManager.getProvider(providerPk);
%>
	<tr bgcolor="<%=bodd?weakColor:"white"%>">
      <td align="center"><%=DateUtils.formatDate(a.getAppointmentDate(), request.getLocale())%></td>
      <td align="center"><%=DateUtils.formatTime(a.getStartTime(), request.getLocale())%></td>
      <td align="center"><%=DateUtils.formatTime(a.getEndTime(), request.getLocale())%></td>
      <td><%=StringUtils.trimToEmpty(a.getReason())%></td>
      <td>
      	<%=(p != null ? p.getLastName() +","+ p.getFirstName() : "") %> (remote)</td>
      <td>&nbsp;<%=a.getStatus()==null?"":(a.getStatus().contains("N")?"No Show":(a.getStatus().equals("C")?"Cancelled":"") ) %></td>
	</tr>
<%
		  
	  }
  }
  
  if(appointmentList==null) {
    out.println("failed!!!");
  } 
  else {
    for(Appointment appointment : appointmentList) {
      iRow ++;
      if(iRow>iPageSize) break;
      bodd=bodd?false:true; //for the color of rows
      nItems++; //to calculate if it is the end of records
    		  
    ProviderData provider = providerDao.findByProviderNo(appointment.getProviderNo());

       
%> 
<tr bgcolor="<%=bodd?weakColor:"white"%>" appt_no="<%=appointment.getId().toString()%>" demographic_no="<%=demographic_no%>">	  
      <td align="center"><a href=# onClick ="popupPageNew(360,680,'../appointment/appointmentcontrol.jsp?appointment_no=<%=appointment.getId().toString()%>&displaymode=edit&dboperation=search');return false;" ><%=appointment.getAppointmentDate()%></a></td>
      <td align="center"><%=appointment.getStartTime()%></td>
      <td align="center"><%=appointment.getEndTime()%></td>
      <td><%=appointment.getReason()%></td>
      <td><%=provider.getLastName() + "," + provider.getFirstName()%></td>
      <plugin:hideWhenCompExists componentName="specialencounterComp" reverse="true">
      <special:SpecialEncounterTag moduleName="eyeform">      
      <td><a href="#" onclick="popupPage(800,1000,'<%=request.getContextPath()%>/mod/specialencounterComp/EyeForm.do?method=view&appHis=true&demographicNo=<%=demographic_no%>&appNo=<%=appointment.getId().toString()%>')">eyeform</a></td>
      </special:SpecialEncounterTag>
      </plugin:hideWhenCompExists>

<% 
      String remarks = appointment.getRemarks();
      if(remarks == null)
    	  remarks="";
      
         String comments = "";
         boolean newline = false;

         if (appointment.getStatus()!=null) {
            if(appointment.getStatus().contains("N")) {
               comments = "No Show";
            }
            else if (appointment.getStatus().equals("C")) {
               comments = "Cancelled";
            }
        }

        if (!remarks.isEmpty() && !comments.isEmpty()) {
              newline=true;
         }
%>
      <td>&nbsp;<%=remarks%><% if(newline){%><br/>&nbsp;<%}%><%=comments%></td>
<% 
	if (org.oscarehr.common.IsPropertiesOn.isMultisitesEnable()) { 
	String[] sbc = siteBgColor.get(appointment.getLocation()); 
%>      
	<td style='background-color:<%= sbc[0] %>'><%= sbc[1] %></td>
<% 
	} 
%>      
</tr>
<%
    }
  }
%>
		</table>
		<br>
<%
  int nPrevPage=0,nNextPage=0;
  nNextPage=Integer.parseInt(strLimit2)+Integer.parseInt(strLimit1);
  nPrevPage=Integer.parseInt(strLimit1)-Integer.parseInt(strLimit2);
  if(nPrevPage>=0) {
%>
	<a href="demographiccontrol.jsp?demographic_no=<%=request.getParameter("demographic_no")%>&last_name=<%=URLEncoder.encode(demolastname,"UTF-8")%>&first_name=<%=URLEncoder.encode(demofirstname,"UTF-8")%>&displaymode=<%=request.getParameter("displaymode")%>&dboperation=<%=request.getParameter("dboperation")%>&orderby=<%=request.getParameter("orderby")%>&limit1=<%=nPrevPage%>&limit2=<%=strLimit2%>">
		<bean:message key="demographic.demographicappthistory.btnPrevPage" /></a> 
<%
  }
  if(nItems==Integer.parseInt(strLimit2)) {
%> 
	<a href="demographiccontrol.jsp?demographic_no=<%=request.getParameter("demographic_no")%>&last_name=<%=URLEncoder.encode(demolastname,"UTF-8")%>&first_name=<%=URLEncoder.encode(demofirstname,"UTF-8")%>&displaymode=<%=request.getParameter("displaymode")%>&dboperation=<%=request.getParameter("dboperation")%>&orderby=<%=request.getParameter("orderby")%>&limit1=<%=nNextPage%>&limit2=<%=strLimit2%>">
		<bean:message key="demographic.demographicappthistory.btnNextPage" /></a>
<%
}
%>
		<p>
		</td>
	</tr>
	<tr>
		<td class="MainTableBottomRowLeftColumn"></td>
		<td class="MainTableBottomRowRightColumn"></td>
	</tr>
</table>
</body>
</html:html>
