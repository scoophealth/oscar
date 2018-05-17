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
<%@page import="org.oscarehr.common.dao.UserPropertyDAO"%>
<%@page import="org.oscarehr.common.model.UserProperty"%>
<%@page import="org.oscarehr.casemgmt.model.ProviderExt"%>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
    String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
    String curProvider_no = (String) session.getAttribute("user");

    boolean isSiteAccessPrivacy=false;
    boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_admin,_admin.userAdmin" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../securityError.jsp?type=_admin&type=_admin.userAdmin");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>


<security:oscarSec objectName="_site_access_privacy" roleName="<%=roleName$%>" rights="r" reverse="false">
	<%isSiteAccessPrivacy=true; %>
</security:oscarSec>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ page import="java.sql.*, java.util.*, oscar.*" errorPage="errorpage.jsp"%>
<%@ page import="oscar.log.LogAction,oscar.log.LogConst"%>
<%@ page import="oscar.log.*, oscar.oscarDB.*"%>

<%@page import="org.oscarehr.common.dao.SiteDao"%>
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="org.oscarehr.common.model.Site"%>

<%@ page import="org.apache.commons.lang.StringEscapeUtils,oscar.oscarProvider.data.ProviderBillCenter"%>

<%@ page import="org.oscarehr.util.SpringUtils" %>
<%@ page import="org.oscarehr.common.model.Provider" %>
<%@ page import="org.oscarehr.PMmodule.dao.ProviderDao" %>
<%@page import="org.oscarehr.common.model.ProviderSite"%>
<%@page import="org.oscarehr.common.model.ProviderSitePK"%>
<%@page import="org.oscarehr.common.dao.ProviderSiteDao"%>
<%
	ProviderDao providerDao = (ProviderDao)SpringUtils.getBean("providerDao");
	ProviderSiteDao providerSiteDao = SpringUtils.getBean(ProviderSiteDao.class);
	boolean alreadyExists=false;
%>
<html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title><bean:message key="admin.provideraddrecord.title" /></title>
<link rel="stylesheet" href="../web.css">
</head>

<body bgproperties="fixed" topmargin="0" leftmargin="0" rightmargin="0">
<center>
<table border="0" cellspacing="0" cellpadding="0" width="100%">
	<tr bgcolor="#486ebd">
		<th><font face="Helvetica" color="#FFFFFF">
			<bean:message key="admin.provideraddrecord.description" />
		</font></th>
	</tr>
</table>
<%
boolean isOk = false;
int retry = 0;
String curUser_no = (String)session.getAttribute("user");

Provider p = new Provider();
p.setProviderNo(request.getParameter("provider_no"));
p.setLastName(request.getParameter("last_name"));
p.setFirstName(request.getParameter("first_name"));
p.setProviderType(request.getParameter("provider_type"));
p.setSpecialty(request.getParameter("specialty"));
p.setTeam(request.getParameter("team"));
p.setSex(request.getParameter("sex"));
p.setDob(MyDateFormat.getSysDate(request.getParameter("dob")));
p.setAddress(request.getParameter("address"));
p.setPhone(request.getParameter("phone"));
p.setWorkPhone(request.getParameter("workphone"));
p.setEmail(request.getParameter("email"));
p.setOhipNo(request.getParameter("ohip_no"));
p.setRmaNo(request.getParameter("rma_no"));
p.setBillingNo(request.getParameter("billing_no"));
p.setHsoNo(request.getParameter("hso_no"));
p.setStatus(request.getParameter("status"));
p.setComments(SxmlMisc.createXmlDataString(request,"xml_p"));
p.setProviderActivity(request.getParameter("provider_activity"));
p.setPractitionerNo(request.getParameter("practitionerNo"));
p.setLastUpdateUser((String)session.getAttribute("user"));
p.setLastUpdateDate(new java.util.Date());
p.setSupervisor(request.getParameter("supervisor"));

//multi-office provide id formalize check, can be turn off on properties multioffice.formalize.provider.id
boolean isProviderFormalize = true;
String  errMsgProviderFormalize = "admin.provideraddrecord.msgAdditionFailure";
Integer min_value = 0;
Integer max_value = 0;

if (org.oscarehr.common.IsPropertiesOn.isProviderFormalizeEnable()) {

	String StrProviderId = request.getParameter("provider_no");
	OscarProperties props = OscarProperties.getInstance();

	String[] provider_sites = {};

	// get provider id ranger
	if (request.getParameter("provider_type").equalsIgnoreCase("doctor")) {
		//provider is doctor, get provider id range from Property
		min_value = new Integer(props.getProperty("multioffice.formalize.doctor.minimum.provider.id", ""));
		max_value = new Integer(props.getProperty("multioffice.formalize.doctor.maximum.provider.id", ""));
	}
	else {
		//non-doctor role
		provider_sites = request.getParameterValues("sites");
		provider_sites = (provider_sites == null ? new String[] {} : provider_sites);

		if (provider_sites.length > 1) {
			//non-doctor can only have one site
			isProviderFormalize = false;
			errMsgProviderFormalize = "admin.provideraddrecord.msgFormalizeProviderIdMultiSiteFailure";
		}
		else {
			if (provider_sites.length == 1) {
				//get provider id range from site
				String provider_site_id =  provider_sites[0];
				SiteDao siteDao = (SiteDao)WebApplicationContextUtils.getWebApplicationContext(application).getBean("siteDao");
				Site provider_site = siteDao.getById(new Integer(provider_site_id));
				min_value = provider_site.getProviderIdFrom();
				max_value = provider_site.getProviderIdTo();
			}
		}
	}
	if (isProviderFormalize) {
		try {
			    Integer providerId = Integer.parseInt(StrProviderId);
			    if (request.getParameter("provider_type").equalsIgnoreCase("doctor") ||  provider_sites.length == 1) {
				    if  (!(providerId >= min_value && providerId <=max_value)) {
				    	// providerId is not in the range
						isProviderFormalize = false;
						errMsgProviderFormalize = "admin.provideraddrecord.msgFormalizeProviderIdFailure";
				    }
			    }
		} catch(NumberFormatException e) {
			//providerId is not a number
			isProviderFormalize = false;
			errMsgProviderFormalize = "admin.provideraddrecord.msgFormalizeProviderIdFailure";
		}
	}
}

if (!org.oscarehr.common.IsPropertiesOn.isProviderFormalizeEnable() || isProviderFormalize) {

DBPreparedHandler dbObj = new DBPreparedHandler();

  // check if the provider no need to be auto generated
  if (OscarProperties.getInstance().isProviderNoAuto())
  {
  	p.setProviderNo(dbObj.getNewProviderNo());
  }
  
  if(providerDao.providerExists(p.getProviderNo())) {
	  isOk=false;
	  alreadyExists=true;
  } else {
  	providerDao.saveProvider(p);
 	 isOk=true;
  }

  
  if(request.getParameter("groupModule") != null) {
	String groupModuleEnabled =   request.getParameter("groupModule");
	if("true".equals(groupModuleEnabled)) {
		UserProperty up = new UserProperty();
		up.setName("groupModule");
		up.setProviderNo(p.getProviderNo());
		up.setValue("true");
		
		UserPropertyDAO upDao = SpringUtils.getBean(UserPropertyDAO.class);
		upDao.saveEntity(up);
	}
	
  }
  
if (isOk && org.oscarehr.common.IsPropertiesOn.isMultisitesEnable()) {
	String[] sites = request.getParameterValues("sites");
	if (sites!=null)
		for (int i=0; i<sites.length; i++) {
			ProviderSite ps = new ProviderSite();
        	ps.setId(new ProviderSitePK(p.getProviderNo(),Integer.parseInt(sites[i])));
        	providerSiteDao.persist(ps);
		}
}

if (isOk) {
	String proId = p.getPractitionerNo();
	String ip = request.getRemoteAddr();
	LogAction.addLog(curUser_no, LogConst.ADD, "adminAddUser", proId, ip);

	ProviderBillCenter billCenter = new ProviderBillCenter();
	billCenter.addBillCenter(request.getParameter("provider_no"),request.getParameter("billcenter"));

%>
<h1><bean:message key="admin.provideraddrecord.msgAdditionSuccess" />
</h1>
<%
  } else {
%>
<h1><bean:message key="admin.provideraddrecord.msgAdditionFailure" /></h1>
<%
	if(alreadyExists) {
		%><h2><bean:message key="admin.provideraddrecord.msgAlreadyExists" /></h2><%
	}

  }
}
else {
		if 	(!isProviderFormalize) {
	%>
		<h1><bean:message key="<%=errMsgProviderFormalize%>" /> </h1>
		Provider # range from : <%=min_value %> To : <%=max_value %>
	<%
		}
	}
%>
</center>
</body>
</html:html>
