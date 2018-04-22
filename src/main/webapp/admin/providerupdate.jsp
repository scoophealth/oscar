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
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
      String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
      boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_admin,_admin.userAdmin" rights="r" reverse="<%=true%>">
	<%response.sendRedirect("../securityError.jsp?type=_admin&type=_admin.userAdmin");%>
	<%authed=false; %>
</security:oscarSec>

<%
if(!authed) {
	return;
}
%>
<%@ page
	import="java.sql.*, oscar.login.*, java.util.*,oscar.*,oscar.oscarDB.*,oscar.oscarProvider.data.ProviderBillCenter"
	errorPage="errorpage.jsp"%>

<%@page import="org.oscarehr.common.dao.SiteDao"%>
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="org.oscarehr.common.model.Site"%>
<%@page import="org.oscarehr.common.model.Provider"%>
<%@page import="org.oscarehr.PMmodule.dao.ProviderDao"%>
<%@page import="org.oscarehr.common.model.ProviderArchive"%>
<%@page import="org.oscarehr.common.dao.ProviderArchiveDao"%>
<%@page import="org.oscarehr.util.SpringUtils"%>
<%@page import="org.apache.commons.beanutils.BeanUtils"%>
<%@page import="org.oscarehr.common.model.ProviderSite"%>
<%@page import="org.oscarehr.common.model.ProviderSitePK"%>
<%@page import="org.oscarehr.common.dao.ProviderSiteDao"%>
<%@page import="org.oscarehr.common.dao.UserPropertyDAO"%>
<%@page import="org.oscarehr.common.model.UserProperty"%>
<%
	ProviderDao providerDao = (ProviderDao)SpringUtils.getBean("providerDao");
	ProviderSiteDao providerSiteDao = SpringUtils.getBean(ProviderSiteDao.class);
%>
<html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title><bean:message key="admin.providerupdate.title" /></title>
</head>
<link rel="stylesheet" href="../web.css" />

<body bgproperties="fixed" topmargin="0" leftmargin="0" rightmargin="0">
<center>
<table border="0" cellspacing="0" cellpadding="0" width="100%">
	<tr bgcolor="#486ebd">
		<th><font face="Helvetica" color="#FFFFFF"><bean:message
			key="admin.providerupdate.description" /></font></th>
	</tr>
</table>

<%
  ProviderBillCenter billCenter = new ProviderBillCenter();
  billCenter.updateBillCenter(request.getParameter("provider_no"),request.getParameter("billcenter"));



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
    ProviderArchiveDao providerArchiveDao = (ProviderArchiveDao)SpringUtils.getBean("providerArchiveDao");
	Provider provider = providerDao.getProvider(request.getParameter("provider_no"));
	ProviderArchive pa = new ProviderArchive();
	BeanUtils.copyProperties(pa, provider);
	providerArchiveDao.persist(pa);



	  Provider p = providerDao.getProvider(request.getParameter("provider_no"));
	  if(p != null) {
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
                  String supervisor = request.getParameter("supervisor");
                  
                  if( supervisor == null || supervisor.equalsIgnoreCase("null") || supervisor.equals("")) {
                      p.setSupervisor(null);
                  }
                  else {
                    p.setSupervisor(supervisor);
                  }
		  
		  providerDao.updateProvider(p);
		  
		  
		  UserPropertyDAO userPropertyDAO = (UserPropertyDAO)SpringUtils.getBean("UserPropertyDAO");
		 
		  String clinicalConnectId = request.getParameter("clinicalConnectId");
		  String clinicalConnectType = request.getParameter("clinicalConnectType");
		  
		  userPropertyDAO.saveProp(provider.getProviderNo(), UserProperty.CLINICALCONNECT_ID, clinicalConnectId);
		  userPropertyDAO.saveProp(provider.getProviderNo(), UserProperty.CLINICALCONNECT_TYPE, clinicalConnectType);
		  
		  String officialFirstName = request.getParameter("officialFirstName");
		  String officialSecondName = request.getParameter("officialSecondName");
		  String officialLastName = request.getParameter("officialLastName");
		  String officialOlisIdtype = request.getParameter("officialOlisIdtype");
		
		  userPropertyDAO.saveProp(provider.getProviderNo(), UserProperty.OFFICIAL_FIRST_NAME, officialFirstName);
		  userPropertyDAO.saveProp(provider.getProviderNo(), UserProperty.OFFICIAL_SECOND_NAME, officialSecondName);
		  userPropertyDAO.saveProp(provider.getProviderNo(), UserProperty.OFFICIAL_LAST_NAME, officialLastName);
		  userPropertyDAO.saveProp(provider.getProviderNo(), UserProperty.OFFICIAL_OLIS_IDTYPE, officialOlisIdtype);
		  

		  String groupModule = request.getParameter("groupModule");
		  
		  if(groupModule != null && "true".equals(groupModule)) {
			  userPropertyDAO.saveProp(provider.getProviderNo(), "groupModule",groupModule);
		  } else {
			  userPropertyDAO.saveProp(provider.getProviderNo(), "groupModule","false");
		  }
			  
        if (org.oscarehr.common.IsPropertiesOn.isMultisitesEnable()) {
            String[] sites = request.getParameterValues("sites");
            DBPreparedHandler dbObj = new DBPreparedHandler();
            String provider_no = request.getParameter("provider_no");
            List<ProviderSite> pss = providerSiteDao.findByProviderNo(provider_no);
            for(ProviderSite ps:pss) {
            	providerSiteDao.remove(ps.getId());
            }
            if (sites!=null) {
                for (int i=0; i<sites.length; i++) {
                	ProviderSite ps = new ProviderSite();
                	ps.setId(new ProviderSitePK(provider_no,Integer.parseInt(sites[i])));
                	providerSiteDao.persist(ps);
                }
            }
        }
%>
<p>
<h2><bean:message key="admin.providerupdate.msgUpdateSuccess" />
<a href="providerupdateprovider.jsp?keyword=<%=request.getParameter("provider_no")%>"><%= request.getParameter("provider_no") %></a>
</h2>
<%
  } else {
%>
<h1><bean:message key="admin.providerupdate.msgUpdateFailure" /><%= request.getParameter("provider_no") %>.</h1>
<%
  }
}
else {
	if (!isProviderFormalize) {
		//output ProviderFormalize error message
	%>
		<h1><bean:message key="<%=errMsgProviderFormalize%>" />  </h1>
		Provider # range from : <%=min_value %> To : <%=max_value %>
	<%
	}
}
%>
<p></p>

</center>
</body>
</html:html>
