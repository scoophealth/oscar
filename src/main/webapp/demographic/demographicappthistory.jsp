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
<security:oscarSec roleName="<%=roleName$%>" objectName="_demographic" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect(request.getContextPath() + "/securityError.jsp?type=_demographic");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>

<%@page import="org.apache.commons.beanutils.BeanUtils"%>
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="org.springframework.web.context.WebApplicationContext"%>
<%@page import="org.oscarehr.caisi_integrator.ws.DemographicWs"%>
<%@page import="org.oscarehr.PMmodule.caisi_integrator.IntegratorFallBackManager" %>
<%@page import="org.apache.commons.lang.StringEscapeUtils" %>

<%@ page import="java.util.*, java.sql.*, java.net.*, oscar.*, oscar.oscarDB.*" errorPage="errorpage.jsp"%>
<%@ page import="org.oscarehr.PMmodule.caisi_integrator.CaisiIntegratorManager, org.oscarehr.caisi_integrator.ws.CachedAppointment, org.oscarehr.caisi_integrator.ws.CachedProvider, org.oscarehr.util.LoggedInInfo" %>
<%@ page import="org.oscarehr.caisi_integrator.ws.*"%>
<%@ page import="org.oscarehr.common.model.CachedAppointmentComparator" %>

<%@page import="oscar.util.DateUtils"%>
<%@page import="org.apache.commons.lang.StringUtils"%>
<%@page import="org.oscarehr.util.MiscUtils" %>
<%@page import="org.oscarehr.util.SpringUtils" %>

<%@page import="org.oscarehr.common.dao.SiteDao"%>
<%@page import="org.oscarehr.common.model.Site"%>

<%@page import="org.oscarehr.common.dao.OscarAppointmentDao" %>
<%@page import="org.oscarehr.common.model.Appointment" %>
<%@page import="org.oscarehr.common.model.AppointmentArchive" %>
<%@page import="org.oscarehr.common.dao.AppointmentStatusDao" %>
<%@page import="org.oscarehr.common.model.AppointmentStatus" %>


<%@ page import="org.oscarehr.common.model.ProviderData"%>
<%@ page import="org.oscarehr.common.dao.ProviderDataDao"%>

<%@page import="org.oscarehr.PMmodule.service.ProgramManager" %>
<%@page import="org.oscarehr.PMmodule.model.Program" %>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="http://www.caisi.ca/plugin-tag" prefix="plugin" %>
<%@ taglib uri="/WEB-INF/caisi-tag.tld" prefix="caisi" %>
<%@ taglib uri="/WEB-INF/special_tag.tld" prefix="special" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/oscar-tag.tld" prefix="oscar"%>


<%!
	private List<Site> sites = new java.util.ArrayList<Site>();
	private HashMap<String,String[]> siteBgColor = new HashMap<String,String[]>();
%>

<%
LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
OscarAppointmentDao appointmentDao = (OscarAppointmentDao)SpringUtils.getBean("oscarAppointmentDao");
ProviderDataDao providerDao = SpringUtils.getBean(ProviderDataDao.class);
AppointmentStatusDao appointmentStatusDao = SpringUtils.getBean(AppointmentStatusDao.class);
ProgramManager programManager = SpringUtils.getBean(ProgramManager.class);



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
  String strLimit2="50";
  if(request.getParameter("limit1")!=null) strLimit1 = request.getParameter("limit1");
  if(request.getParameter("limit2")!=null) strLimit2 = request.getParameter("limit2");
  
  String demolastname = request.getParameter("last_name")==null?"":request.getParameter("last_name");
  String demofirstname = request.getParameter("first_name")==null?"":request.getParameter("first_name");
  String deepColor = "#CCCCFF" , weakColor = "#EEEEFF" ;
  String showDeleted = request.getParameter("deleted");
  String orderby="";
  if(request.getParameter("orderby")!=null) orderby=request.getParameter("orderby");
  
  Map<String,ProviderData> providerMap = new HashMap<String,ProviderData>();
  
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
	
	function printVisit_eyeform2()
	{
		var cpp = '';
		var sels = document.getElementsByName('sel');
		var ids = "";	
		for(var x=0;x<sels.length;x++) {
			if(sels[x].checked) {
				if(ids.length>0)
					ids+= ",";
				ids += sels[x].value;
			}
		}	
		if(ids=="") {
			alert("Please select the appointments.");
			return false;
		}
		location.href=ctx+"/eyeform/Eyeform.do?method=print&from_cme_js=true&apptNos="+ids+"&cpp="+cpp;
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

	function toggleShowDeleted(value) {
		if(value) {
			//show deleted
			//appt_history_w_deleted
			location.href='<%=request.getContextPath()%>/demographic/demographiccontrol.jsp?demographic_no=<%=demographic_no%>&last_name=<%=StringEscapeUtils.escapeJavaScript(demolastname)%>&first_name=<%=StringEscapeUtils.escapeJavaScript(demofirstname)%>&orderby=<%=orderby%>&displaymode=appt_history&dboperation=appt_history_w_deleted&limit1=<%=strLimit1%>&limit2=<%=strLimit2%>&deleted=true';
		} else {
			//don't show deleted
			location.href='<%=request.getContextPath()%>/demographic/demographiccontrol.jsp?demographic_no=<%=demographic_no%>&last_name=<%=StringEscapeUtils.escapeJavaScript(demolastname)%>&first_name=<%=StringEscapeUtils.escapeJavaScript(demofirstname)%>&orderby=<%=orderby%>&displaymode=appt_history&dboperation=appt_history&limit1=<%=strLimit1%>&limit2=<%=strLimit2%>';
		}
	}
	
	jQuery(document).ready(function(){
		<%if(showDeleted != null && showDeleted.equals("true")) { %>
		jQuery("#showDeleted").attr('checked',true);
		<% } else {%>
		jQuery("#showDeleted").attr('checked',false);
		<%} %>
	});
	
	
	function filterByProvider(s) {
		var providerNo = s.options[s.selectedIndex].value;
		jQuery("#apptHistoryTbl tbody tr").not(":first").each(function(){
			if(!providerNo=='' && jQuery(this).attr('provider_no') != providerNo) {
				jQuery(this).hide();
			} else {
				jQuery(this).show();
			}
		});
	}
</script>

<link rel="stylesheet" type="text/css" media="all" href="../share/css/extractedFromPages.css"  />
</head>

<body class="BodyStyle"	demographic.demographicappthistory.msgTitle=vlink="#0000FF">

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
		<td class="MainTableLeftColumn" valign="top"><a	href="<%=request.getContextPath()%>/demographic/demographiccontrol.jsp?demographic_no=<%=request.getParameter("demographic_no")%>&apptProvider=<%=session.getAttribute("user") %>&displaymode=edit&dboperation=search_detail" onMouseOver="self.status=document.referrer;return true">
			<bean:message key="global.btnBack" /></a> 
			<br/>
			<input type="checkbox" name="showDeleted" id="showDeleted" onChange="toggleShowDeleted(this.checked);"/><bean:message key="demographic.demographicappthistory.msgShowDeleted" />
			<br/>
	    </td>
		<td class="MainTableRightColumn">
		<table width="95%" border="0" bgcolor="#ffffff" id="apptHistoryTbl">
			<tr  bgcolor="<%=deepColor%>">				
				<TH width="10%"><b><bean:message key="demographic.demographicappthistory.msgApptDate" /></b></TH>
				<TH width="10%"><b><bean:message key="demographic.demographicappthistory.msgFrom" /></b></TH>
				<TH width="10%"><b><bean:message key="demographic.demographicappthistory.msgTo" /></b></TH>
				<TH width="10%"><b><bean:message key="demographic.demographicappthistory.msgStatus" /></b></TH>
				<TH width="10%"><b><bean:message key="demographic.demographicappthistory.msgType" /></b></TH>
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
  List<Object> appointmentList;
  org.oscarehr.managers.AppointmentManager appointmentManager = SpringUtils.getBean(org.oscarehr.managers.AppointmentManager.class);
  
  if(!"true".equals(showDeleted)) {
	  appointmentList = new java.util.ArrayList<Object>();
	  appointmentList.addAll(appointmentManager.getAppointmentHistoryWithoutDeleted(loggedInInfo, new Integer(demographic_no), iRSOffSet, iPageSize));
  } else {
	appointmentList = appointmentManager.getAppointmentHistoryWithDeleted(loggedInInfo, new Integer(demographic_no), iRSOffSet, iPageSize);
  }
  
  //filter (CAISI)
  //we need to get our program domain, and go through the appts. If the programId is in our domain, we can show
  List<Object> filteredAppointmentList = new java.util.ArrayList<Object>();
  List<Program> programs = programManager.getActiveProgramByFacility(loggedInInfo.getLoggedInProviderNo(), loggedInInfo.getCurrentFacility().getId());
  for(Object o:appointmentList) {
	  int programId  = 0;
	  if(o instanceof Appointment) {
  			Appointment a  = (Appointment)o;
  			programId = a.getProgramId();
  		}
  		if(o instanceof AppointmentArchive) {
  			AppointmentArchive a  = (AppointmentArchive)o;
  			programId = a.getProgramId();
  		}
  		if(programId > 0) {
  			Program p = new Program();
  			p.setId(programId);
  			if(programs.contains(p)) {
  				//add
  				filteredAppointmentList.add(o);
  			}
  		} else {
  			//add
  			filteredAppointmentList.add(o);
  		}
  }
  
  appointmentList = filteredAppointmentList;
  
  boolean bodd=false;
  int nItems=0;
  
  List<CachedAppointment> cachedAppointments = null;
  Boolean showRemote = request.getParameter("showRemote") == null ? true : Boolean.parseBoolean(request.getParameter("showRemote"));
  if (loggedInInfo.getCurrentFacility().isIntegratorEnabled() && showRemote ){
		int demographicNo = Integer.parseInt(request.getParameter("demographic_no"));
		try {
			if (!CaisiIntegratorManager.isIntegratorOffline(session)){
				cachedAppointments = CaisiIntegratorManager.getDemographicWs(loggedInInfo, loggedInInfo.getCurrentFacility()).getLinkedCachedAppointments(demographicNo);
			}
		} catch (Exception e) {
			MiscUtils.getLogger().error("Unexpected error.", e);
			CaisiIntegratorManager.checkForConnectionError(session,e);
		}
		
		if(CaisiIntegratorManager.isIntegratorOffline(session)){
			cachedAppointments = IntegratorFallBackManager.getRemoteAppointments(loggedInInfo, demographicNo);	
		}	
  }
  
  
  
  if(appointmentList==null) {
    out.println("failed!!!");
  } 
  else {
      
    for(Object obj : appointmentList) {
    	boolean deleted=false;
    	
    	Appointment appointment = null;
    	AppointmentArchive appointmentArchive = null;
    	if(obj instanceof Appointment) {
    		appointment = (Appointment)obj;
    	}
    	if(obj instanceof AppointmentArchive) {
    		appointmentArchive = (AppointmentArchive)obj;
    		appointment = new Appointment();
    		
    		BeanUtils.copyProperties(appointment, appointmentArchive);
    		appointment.setId(appointmentArchive.getAppointmentNo());
    		
    		deleted=true;
    	}
      iRow++;
      
      if(iRow>iPageSize) break;
      bodd=bodd?false:true; //for the color of rows
      nItems++; //to calculate if it is the end of records
     		  
    ProviderData provider = providerDao.findByProviderNo(appointment.getProviderNo());
    AppointmentStatus as = appointmentStatusDao.findByStatus(appointment.getStatus());
    
    if(provider != null) {
    	providerMap.put(provider.getId(),provider);
    }
       
%> 
<tr <%=(deleted)?"style='text-decoration: line-through' ":"" %> bgcolor="<%=bodd?weakColor:"white"%>" appt_no="<%=appointment.getId().toString()%>" demographic_no="<%=demographic_no%>" provider_no="<%=provider!=null?provider.getId():""%>">	  
      <td align="center"><a href=# onClick ="popupPageNew(360,680,'../appointment/appointmentcontrol.jsp?demographic_no=<%=demographic_no%>&appointment_no=<%=appointment.getId().toString()%>&displaymode=edit&dboperation=search');return false;" ><%=appointment.getAppointmentDate()%></a></td>
      <td align="center"><%=appointment.getStartTime()%></td>
      <td align="center"><%=appointment.getEndTime()%></td>
      <td align="center">
      <%if(as != null && as.getDescription() != null) {%>
		<%=as.getDescription()%>
	  <% } %>
	  </td>
      <td><%=appointment.getType() %></td>
      <td><%=appointment.getReason()%></td>
      <% if( provider != null ) {%>
      <td><%=(provider.getLastName() == null ? "N/A" : provider.getLastName()) + "," + (provider.getFirstName() == null ? "N/A" : provider.getFirstName())%></td>
      <%}
	  	else { %>
	  	<td>N/A</td>
	  <%}%>
      
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


if (cachedAppointments != null) {
      Collections.sort(cachedAppointments, new CachedAppointmentComparator());
	  for (CachedAppointment a : cachedAppointments) {
		  bodd=bodd?false:true;
		  FacilityIdStringCompositePk providerPk=new FacilityIdStringCompositePk();
		  providerPk.setIntegratorFacilityId(a.getFacilityIdIntegerCompositePk().getIntegratorFacilityId());
		  providerPk.setCaisiItemId(a.getCaisiProviderId());
		  CachedProvider p = CaisiIntegratorManager.getProvider(loggedInInfo, loggedInInfo.getCurrentFacility(), providerPk);
		  AppointmentStatus as = appointmentStatusDao.findByStatus(a.getStatus());
%>
	<tr bgcolor="<%=bodd?weakColor:"white"%>">
      <td align="center"><%=DateUtils.formatDate(a.getAppointmentDate(), request.getLocale())%></td>
      <td align="center"><%=DateUtils.formatTime(a.getStartTime(), request.getLocale())%></td>
      <td align="center"><%=DateUtils.formatTime(a.getEndTime(), request.getLocale())%></td>
      <td align="center">
      <%if(as != null && as.getDescription() != null) {%>
		<%=as.getDescription()%>
	  <% } %>
	  </td>
      <td><%=a.getType() %></td>
      <td><%=StringUtils.trimToEmpty(a.getReason())%></td>
      <td>
      	<%=(p != null ? p.getLastName() +","+ p.getFirstName() : "") %> (remote)</td>
      <td>&nbsp;<%=a.getStatus()==null?"":(a.getStatus().contains("N")?"No Show":(a.getStatus().equals("C")?"Cancelled":"") ) %></td>
	</tr>
<%
		  
	  }
	  
	  showRemote = false;
  }
%>
		</table>
		<br>
<%
  int nPrevPage=0,nNextPage=0;
  nNextPage=Integer.parseInt(strLimit2)+Integer.parseInt(strLimit1);
  nPrevPage=Integer.parseInt(strLimit1)-Integer.parseInt(strLimit2);
  if(nPrevPage>=0) {
      String showRemoteStr;
      if( nPrevPage == 0 ) {
	  	showRemoteStr = "true";
      }
      else {
	  	showRemoteStr = String.valueOf(showRemote);
      }
%>
	<a href="demographiccontrol.jsp?demographic_no=<%=request.getParameter("demographic_no")%>&last_name=<%=URLEncoder.encode(demolastname,"UTF-8")%>&first_name=<%=URLEncoder.encode(demofirstname,"UTF-8")%>&displaymode=<%=request.getParameter("displaymode")%>&dboperation=<%=request.getParameter("dboperation")%>&orderby=<%=request.getParameter("orderby")%>&limit1=<%=nPrevPage%>&limit2=<%=strLimit2%>&showRemote=<%=showRemoteStr%>">
		<bean:message key="demographic.demographicappthistory.btnPrevPage" /></a> 
<%
  }
  
  if(nItems >=Integer.parseInt(strLimit2)) {
%> 
	<a href="demographiccontrol.jsp?demographic_no=<%=request.getParameter("demographic_no")%>&last_name=<%=URLEncoder.encode(demolastname,"UTF-8")%>&first_name=<%=URLEncoder.encode(demofirstname,"UTF-8")%>&displaymode=<%=request.getParameter("displaymode")%>&dboperation=<%=request.getParameter("dboperation")%>&orderby=<%=request.getParameter("orderby")%>&limit1=<%=nNextPage%>&limit2=<%=strLimit2%>&showRemote=<%=showRemote%>">
		<bean:message key="demographic.demographicappthistory.btnNextPage" /></a>
<%
}
%>
		<p>
		</td>
	</tr>
	<tr>
		<td class="MainTableBottomRowLeftColumn"></td>
		<td class="MainTableBottomRowRightColumn">
		Filter results on this page by provider:
		<select onChange="filterByProvider(this)">
			<option value="">ALL</option>
			<%
				for(ProviderData prov:providerMap.values()) {
			%>
				<option value="<%=prov.getId()%>"><%=prov.getLastName() + ", " + prov.getFirstName() %></option>
			<%
				}
			%>
		</select>
		</td>
	</tr>
</table>
</body>
</html:html>
