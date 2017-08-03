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
<security:oscarSec roleName="<%=roleName$%>" objectName="_demographic" rights="w" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect(request.getContextPath() + "/securityError.jsp?type=_demographic");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>

<%@page import="org.oscarehr.managers.DemographicManager"%>
<%@page import="org.oscarehr.util.LoggedInInfo"%>
<%@ page import="java.sql.*, java.util.*, java.net.URLEncoder, oscar.oscarDB.*, oscar.MyDateFormat, oscar.oscarWaitingList.WaitingList, org.oscarehr.common.OtherIdManager" errorPage="errorpage.jsp"%>
<%@ page import="oscar.log.*"%>
<%@ page import="org.oscarehr.util.SpringUtils" %>
<%@ page import="org.apache.commons.lang.StringUtils"%>

<%@ page import="org.oscarehr.common.model.Admission" %>
<%@ page import="org.oscarehr.common.dao.AdmissionDao" %>
<%@ page import="org.oscarehr.common.dao.WaitingListDao" %>

<%@ page import="org.oscarehr.common.model.DemographicExt" %>
<%@ page import="org.oscarehr.common.dao.DemographicExtDao" %>

<%@ page import="org.oscarehr.common.model.Demographic" %>
<%@ page import="org.oscarehr.common.dao.DemographicDao" %>
<%@ page import="org.oscarehr.common.model.DemographicCust" %>
<%@ page import="org.oscarehr.common.dao.DemographicCustDao" %>

<%@ page import="org.oscarehr.PMmodule.dao.ProgramDao" %>
<%@ page import="org.oscarehr.PMmodule.model.Program" %>
<%@page import="org.oscarehr.PMmodule.web.GenericIntakeEditAction" %>
<%@page import="org.oscarehr.PMmodule.service.ProgramManager" %>
<%@page import="org.oscarehr.PMmodule.service.AdmissionManager" %>

<%@page import="org.oscarehr.common.dao.DemographicArchiveDao" %>
<%@page import="org.oscarehr.common.model.DemographicArchive" %>
<%@page import="org.oscarehr.common.dao.DemographicExtArchiveDao" %>
<%@page import="org.oscarehr.common.model.DemographicExtArchive" %>

<%@page import="org.oscarehr.common.dao.DemographicContactDao"%>
<%@page import="org.oscarehr.common.model.DemographicContact"%>
<%@page import="org.oscarehr.managers.PatientConsentManager" %>
<%@page import="org.oscarehr.common.model.Consent" %>
<%@page import="org.oscarehr.common.model.ConsentType" %>
<%@page import="oscar.OscarProperties" %>
<%@page import="org.oscarehr.common.dao.DemographicSiteDao" %>
<%@page import="org.oscarehr.common.model.DemographicSite" %>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@taglib uri="/WEB-INF/caisi-tag.tld" prefix="caisi"%>

<% 
	java.util.Properties oscarVariables = oscar.OscarProperties.getInstance();

	AdmissionDao admissionDao = (AdmissionDao)SpringUtils.getBean("admissionDao");
	ProgramManager pm = SpringUtils.getBean(ProgramManager.class);
	AdmissionManager am = SpringUtils.getBean(AdmissionManager.class);
	WaitingListDao waitingListDao = (WaitingListDao)SpringUtils.getBean("waitingListDao");
	DemographicExtDao demographicExtDao = SpringUtils.getBean(DemographicExtDao.class);
	DemographicDao demographicDao = (DemographicDao)SpringUtils.getBean("demographicDao");
	DemographicCustDao demographicCustDao = (DemographicCustDao)SpringUtils.getBean("demographicCustDao");

	ProgramDao programDao = (ProgramDao)SpringUtils.getBean("programDao");
	
	DemographicExtArchiveDao demographicExtArchiveDao = SpringUtils.getBean(DemographicExtArchiveDao.class);
	DemographicArchiveDao demographicArchiveDao = (DemographicArchiveDao)SpringUtils.getBean("demographicArchiveDao");
	DemographicContactDao demographicContactDao = SpringUtils.getBean(DemographicContactDao.class);
%>

<html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<link rel="stylesheet" href="../web.css" />
<script LANGUAGE="JavaScript">
    <!--
    function start(){
      this.focus();
      this.resizeTo(1000,700);
    }
    function closeit() {
      //parent.refresh();
      close();
    }
    //-->
</script>
</head>

<body onload="start()" bgproperties="fixed" topmargin="0" leftmargin="0" rightmargin="0">
<center>
<table border="0" cellspacing="0" cellpadding="0" width="100%">
	<tr bgcolor="#486ebd">
		<th align="CENTER"><font face="Helvetica" color="#FFFFFF">
		<bean:message key="demographic.demographicaddarecord.title" /></font></th>
	</tr>
</table>
<form method="post" name="addappt">
<%
	LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);

        //If this is from adding appointment screen, then back to there
        String fromAppt = request.getParameter("fromAppt");
        String originalPage2 = request.getParameter("originalPage");
        String provider_no2 = request.getParameter("provider_no");
        String bFirstDisp2 = request.getParameter("bFirstDisp");
        String year2 = request.getParameter("year");
        String month2 = request.getParameter("month");
        String day2 = request.getParameter("day");
        String start_time2 = request.getParameter("start_time");
        String end_time2 = request.getParameter("end_time");
        String duration2 = request.getParameter("duration");

    String dem = null;
	String year, month, day;
    String curUser_no = (String)session.getAttribute("user");

	DBPreparedHandlerParam [] param =new DBPreparedHandlerParam[34];

	Demographic demographic = new Demographic();
	demographic.setLastName(request.getParameter("last_name").trim());
	demographic.setFirstName(request.getParameter("first_name").trim());
	demographic.setAddress(request.getParameter("address"));
	demographic.setCity(request.getParameter("city"));
	demographic.setProvince(request.getParameter("province"));
	demographic.setPostal(request.getParameter("postal"));
	demographic.setPhone(request.getParameter("phone"));
	demographic.setPhone2(request.getParameter("phone2"));
	demographic.setEmail(request.getParameter("email"));
	demographic.setMyOscarUserName(StringUtils.trimToNull(request.getParameter("myOscarUserName")));
	demographic.setYearOfBirth(request.getParameter("year_of_birth"));
	demographic.setMonthOfBirth(request.getParameter("month_of_birth")!=null && request.getParameter("month_of_birth").length()==1 ? "0"+request.getParameter("month_of_birth") : request.getParameter("month_of_birth"));
	demographic.setDateOfBirth(request.getParameter("date_of_birth")!=null && request.getParameter("date_of_birth").length()==1 ? "0"+request.getParameter("date_of_birth") : request.getParameter("date_of_birth"));
	demographic.setHin(request.getParameter("hin"));
	demographic.setVer(request.getParameter("ver"));
	demographic.setRosterStatus(request.getParameter("roster_status"));
	demographic.setPatientStatus(request.getParameter("patient_status"));
	demographic.setDateJoined(MyDateFormat.getSysDate(request.getParameter("date_joined_year")+"-"+request.getParameter("date_joined_month")+"-"+request.getParameter("date_joined_date")));
	demographic.setChartNo(request.getParameter("chart_no"));
	demographic.setProviderNo(request.getParameter("staff"));
	demographic.setSex(request.getParameter("sex"));

	year = StringUtils.trimToNull(request.getParameter("end_date_year"));
	month = StringUtils.trimToNull(request.getParameter("end_date_month"));
	day = StringUtils.trimToNull(request.getParameter("end_date_date"));
	if (year!=null && month!=null && day!=null) {
	 		demographic.setEndDate(MyDateFormat.getSysDate(year + "-" + month + "-" + day));
	} else {
		demographic.setEndDate(null);
	}
	
	year = StringUtils.trimToNull(request.getParameter("eff_date_year"));
	month = StringUtils.trimToNull(request.getParameter("eff_date_month"));
	day = StringUtils.trimToNull(request.getParameter("eff_date_date"));
	if (year!=null && month!=null && day!=null) {
		demographic.setEffDate(MyDateFormat.getSysDate(year + "-" + month + "-" + day));
	} else {
		demographic.setEffDate(null);
	}

	demographic.setPcnIndicator(request.getParameter("pcn_indicator"));
	demographic.setHcType(request.getParameter("hc_type"));
	
	year = StringUtils.trimToNull(request.getParameter("roster_date_year"));
	month = StringUtils.trimToNull(request.getParameter("roster_date_month"));
	day = StringUtils.trimToNull(request.getParameter("roster_date_date"));
	if (year!=null && month!=null && day!=null) {
		demographic.setRosterDate(MyDateFormat.getSysDate( year + "-" + month + "-" + day));
	} else {
		demographic.setRosterDate(null);
	}
	          
	year = StringUtils.trimToNull(request.getParameter("hc_renew_date_year"));
	month = StringUtils.trimToNull(request.getParameter("hc_renew_date_month"));
	day = StringUtils.trimToNull(request.getParameter("hc_renew_date_date"));
	if (year!=null && month!=null && day!=null) {
		demographic.setHcRenewDate(MyDateFormat.getSysDate( year + "-" + month + "-" + day));
	} else {
		demographic.setHcRenewDate(null);
	}
	         
	demographic.setFamilyDoctor("<rdohip>" + request.getParameter("r_doctor_ohip") + "</rdohip>" + "<rd>" + request.getParameter("r_doctor") + "</rd>"+ (request.getParameter("family_doc")!=null? ("<family_doc>" + request.getParameter("family_doc") + "</family_doc>") : ""));
	demographic.setCountryOfOrigin(request.getParameter("countryOfOrigin"));
	demographic.setNewsletter(request.getParameter("newsletter"));
	demographic.setSin(request.getParameter("sin"));
	demographic.setTitle(request.getParameter("title"));
	demographic.setOfficialLanguage(request.getParameter("official_lang"));
	demographic.setSpokenLanguage(request.getParameter("spoken_lang"));
	demographic.setLastUpdateUser(curUser_no);
	demographic.setLastUpdateDate(new java.util.Date());
	demographic.setPatientStatusDate(new java.util.Date());
	
	
	
	StringBuilder bufChart = null, bufName = null, bufNo = null, bufDoctorNo = null;
    // add checking hin duplicated record, if there is a HIN number
    // added check to see if patient has a bc health card and has a version code of 66, in this case you are aloud to have dup hin
    boolean hinDupCheckException = false;
     String hcType = request.getParameter("hc_type");
     String ver  = request.getParameter("ver");
     if (hcType != null && ver != null && hcType.equals("BC") && ver.equals("66")){
        hinDupCheckException = true;
     }

    if(request.getParameter("hin")!=null && request.getParameter("hin").length()>5 && !hinDupCheckException) {
  		//oscar.oscarBilling.ca.on.data.BillingONDataHelp dbObj = new oscar.oscarBilling.ca.on.data.BillingONDataHelp();
		//String sql = "select demographic_no from demographic where hin=? and year_of_birth=? and month_of_birth=? and date_of_birth=?";
		String paramNameHin =new String();
		paramNameHin=request.getParameter("hin").trim();
		List<Demographic> demographics = demographicDao.searchByHealthCard(paramNameHin);
		if(demographics.size()>0){ 
%>
		***<font color='red'><bean:message key="demographic.demographicaddarecord.msgDuplicatedHIN" /></font>***<br><br>
		<a href=# onClick="history.go(-1);return false;"><b>&lt;-<bean:message key="global.btnBack" /></b></a>
<% 
		return; 
		}  
	}
    
    bufName = new StringBuilder(request.getParameter("last_name")+ ","+ request.getParameter("first_name") );
    bufNo = new StringBuilder( (StringUtils.trimToEmpty("demographic_no")) );
    bufChart = new StringBuilder(StringUtils.trimToEmpty("chart_no"));
    bufDoctorNo = new StringBuilder( StringUtils.trimToEmpty("provider_no") );

    demographicDao.save(demographic);
  	//multiple site, update site
	if (org.oscarehr.common.IsPropertiesOn.isMultisitesEnable()) { 
		 DemographicSiteDao demographicSiteDao = (DemographicSiteDao)SpringUtils.getBean("demographicSiteDao");
		 DemographicSite ds = new DemographicSite();
		 String[] sites = request.getParameterValues("sites");
		 //demographicSiteDao.removeSitesByDemographicId(Integer.valueOf(demographic.getDemographicNo()));
			
		 if(sites!=null) {
			int demoNo = demographic.getDemographicNo();
			for (int i = 0; i < sites.length; i++) {				
				DemographicSite demographicSite = new DemographicSite();
				demographicSite.setDemographicId(Integer.valueOf(demoNo));
				demographicSite.setSiteId(Integer.valueOf(sites[i]));
				demographicSiteDao.persist(demographicSite);				
			}
		}
	}


          GenericIntakeEditAction gieat = new GenericIntakeEditAction();
          gieat.setAdmissionManager(am);
          gieat.setProgramManager(pm);
          String bedP = request.getParameter("rps");
          gieat.admitBedCommunityProgram(loggedInInfo, demographic.getDemographicNo(),loggedInInfo.getLoggedInProviderNo(),Integer.parseInt(bedP),"","",new java.util.Date());

          String[] servP = request.getParameterValues("sp");
          if(servP!=null&&servP.length>0){
	  Set<Integer> s = new HashSet<Integer>();
            for(String _s:servP) s.add(Integer.parseInt(_s));
            gieat.admitServicePrograms(loggedInInfo, demographic.getDemographicNo(),loggedInInfo.getLoggedInProviderNo(),s,"", new java.util.Date(), null);
          }
        

        //add democust record for alert
        String[] param2 =new String[6];
	    param2[0]=demographic.getDemographicNo().toString();

        DemographicCust demographicCust = new DemographicCust();
       	demographicCust.setResident(request.getParameter("cust2"));
    	demographicCust.setNurse(request.getParameter("cust1"));
    	demographicCust.setAlert(request.getParameter("cust3"));
    	demographicCust.setMidwife(request.getParameter("cust4"));
    	demographicCust.setNotes("<unotes>"+ request.getParameter("content")+"</unotes>");
    	demographicCust.setId(demographic.getDemographicNo());
    	demographicCustDao.persist(demographicCust);
        int rowsAffected=1;

       dem = demographic.getDemographicNo().toString();
       
       // Save the patient consent values.
	   if( OscarProperties.getInstance().getBooleanProperty("USE_NEW_PATIENT_CONSENT_MODULE", "true") ) {
	
			PatientConsentManager patientConsentManager = SpringUtils.getBean( PatientConsentManager.class );
			List<ConsentType> consentTypes = patientConsentManager.getConsentTypes();
			String consentTypeId = null;
			int patientConsentIdInt = 0; 
			
			for( ConsentType consentType : consentTypes ) {
				consentTypeId = request.getParameter( consentType.getType() );
				// checked box means add or edit consent. 
				if( consentTypeId != null ) {		
					patientConsentManager.addConsent(loggedInInfo, demographic.getDemographicNo(), Integer.parseInt( consentTypeId ) );
				} 	
			}
		}

       String proNo = (String) session.getValue("user");

       demographicExtDao.addKey(proNo, demographic.getDemographicNo(), "hPhoneExt", request.getParameter("hPhoneExt"), "");
       demographicExtDao.addKey(proNo, demographic.getDemographicNo(), "wPhoneExt", request.getParameter("wPhoneExt"), "");
       demographicExtDao.addKey(proNo, demographic.getDemographicNo(), "demo_cell", request.getParameter("demo_cell"), "");
       demographicExtDao.addKey(proNo, demographic.getDemographicNo(), "aboriginal", request.getParameter("aboriginal"), "");
       demographicExtDao.addKey(proNo, demographic.getDemographicNo(), "cytolNum",  request.getParameter("cytolNum"),  "");
       demographicExtDao.addKey(proNo, demographic.getDemographicNo(), "ethnicity",     request.getParameter("ethnicity"),     "");
       demographicExtDao.addKey(proNo, demographic.getDemographicNo(), "area",          request.getParameter("area"),          "");
       demographicExtDao.addKey(proNo, demographic.getDemographicNo(), "statusNum",     request.getParameter("statusNum"),     "");
       demographicExtDao.addKey(proNo, demographic.getDemographicNo(), "fNationCom",    request.getParameter("fNationCom"),    "");
       demographicExtDao.addKey(proNo, demographic.getDemographicNo(), "given_consent", request.getParameter("given_consent"), "");
       demographicExtDao.addKey(proNo, demographic.getDemographicNo(), "rxInteractionWarningLevel", request.getParameter("rxInteractionWarningLevel"), "");
       demographicExtDao.addKey(proNo, demographic.getDemographicNo(), "primaryEMR", request.getParameter("primaryEMR"), "");
       demographicExtDao.addKey(proNo, demographic.getDemographicNo(), "phoneComment", request.getParameter("phoneComment"), "");
       demographicExtDao.addKey(proNo, demographic.getDemographicNo(), "usSigned", request.getParameter("usSigned"), "");
       demographicExtDao.addKey(proNo, demographic.getDemographicNo(), "privacyConsent", request.getParameter("privacyConsent"), "");
       demographicExtDao.addKey(proNo, demographic.getDemographicNo(), "informedConsent", request.getParameter("informedConsent"), "");
       demographicExtDao.addKey(proNo, demographic.getDemographicNo(), "IPHISClientNumber", request.getParameter("IPHISClientNumber"),"");

       demographicExtDao.addKey(proNo, demographic.getDemographicNo(), "PanoramaClientNumber", request.getParameter("PanoramaClientNumber"),"");
       demographicExtDao.addKey(proNo, demographic.getDemographicNo(), "IscisClientNumber", request.getParameter("IscisClientNumber"),"");
       demographicExtDao.addKey(proNo, demographic.getDemographicNo(), "OhissClientNumber", request.getParameter("OhissClientNumber"),"");
       demographicExtDao.addKey(proNo, demographic.getDemographicNo(), "EpiInfoClientNumber", request.getParameter("EpiInfoClientNumber"),"");
       demographicExtDao.addKey(proNo, demographic.getDemographicNo(), "HedgehogClientNumber", request.getParameter("HedgehogClientNumber"),"");

       demographicExtDao.addKey(proNo, demographic.getDemographicNo(), "HasPrimaryCarePhysician", request.getParameter("HasPrimaryCarePhysician"), "");
       demographicExtDao.addKey(proNo, demographic.getDemographicNo(), "EmploymentStatus", request.getParameter("EmploymentStatus"), "");
       demographicExtDao.addKey(proNo, demographic.getDemographicNo(), "recipientLocation", request.getParameter("recipientLocation"), "");
       demographicExtDao.addKey(proNo, demographic.getDemographicNo(), "lhinConsumerResides", request.getParameter("lhinConsumerResides"), "");
       demographicExtDao.addKey(proNo, demographic.getDemographicNo(), "address2", request.getParameter("address2"), "");
       demographicExtDao.addKey(proNo, demographic.getDemographicNo(), "middleName", request.getParameter("middleName"), "");
       demographicExtDao.addKey(proNo, demographic.getDemographicNo(), "lastNameAtBirth", request.getParameter("lastNameAtBirth"), "");
       demographicExtDao.addKey(proNo, demographic.getDemographicNo(), "preferredName", request.getParameter("preferredName"), "");
       demographicExtDao.addKey(proNo, demographic.getDemographicNo(), "maritalStatus", request.getParameter("maritalStatus"), "");
      
       demographicExtDao.addKey(proNo, demographic.getDemographicNo(), "fNationFamilyNumber",    request.getParameter("fNationFamilyNumber"),    "");
       demographicExtDao.addKey(proNo, demographic.getDemographicNo(), "fNationFamilyPosition",    request.getParameter("fNationFamilyPosition"),    "");

       //for the IBD clinic
		OtherIdManager.saveIdDemographic(dem, "meditech_id", request.getParameter("meditech_id"));

       // customized key
       if(oscarVariables.getProperty("demographicExt") != null) {
	       String [] propDemoExt = oscarVariables.getProperty("demographicExt","").split("\\|");
	       for(int k=0; k<propDemoExt.length; k++) {
	    	   demographicExtDao.addKey(proNo,demographic.getDemographicNo(),propDemoExt[k],request.getParameter(propDemoExt[k].replace(' ','_')),"");
	       }
       }
       // customized key

		// add log
		String ip = request.getRemoteAddr();
		LogAction.addLog(curUser_no, "add", "demographic", param2[0], ip,param2[0]);

		//archive the original too
		Long archiveId = demographicArchiveDao.archiveRecord(demographicDao.getDemographic(dem));
		List<DemographicExt> extensions = demographicExtDao.getDemographicExtByDemographicNo(Integer.parseInt(dem));
		for (DemographicExt extension : extensions) {
			DemographicExtArchive archive = new DemographicExtArchive(extension);
			archive.setArchiveId(archiveId);
			archive.setValue(request.getParameter(archive.getKey()));
			demographicExtArchiveDao.saveEntity(archive);	
		}	
		
        //add to waiting list if the waiting_list parameter in the property file is set to true
        

        WaitingList wL = WaitingList.getInstance();
        if(wL.getFound()){

            String[] paramWLPosition = new String[1];
            paramWLPosition[0] = request.getParameter("list_id");
            if(paramWLPosition[0].compareTo("")!=0){

                List<Long> positionList = new ArrayList<Long>();
                List<org.oscarehr.common.model.WaitingList> waitingListList = waitingListDao.findByWaitingListId(new Integer(1));

                if(waitingListList != null) {
                	
	                for(org.oscarehr.common.model.WaitingList waitingList : waitingListList) {
	                	positionList.add(waitingList.getPosition());
	                }
	                Long maxPosition = 0L;
	                if(positionList.size()> 0) {
	                	maxPosition = Collections.max(positionList);
	                }
            	
                    String listId = request.getParameter("list_id");
                    if(listId != null && !listId.equals("") && !listId.equals("0")) {
	                    org.oscarehr.common.model.WaitingList waitingList = new org.oscarehr.common.model.WaitingList();
	                    waitingList.setListId(Integer.parseInt(request.getParameter("list_id")));
	                    waitingList.setDemographicNo(demographic.getDemographicNo());
	                    waitingList.setNote(request.getParameter("waiting_list_note"));
	                    waitingList.setPosition(maxPosition.longValue()+1);
	                    waitingList.setOnListSince(MyDateFormat.getSysDate(request.getParameter("waiting_list_referral_date")));
	                    waitingList.setIsHistory("N");
	                    waitingList.setOnListSince(new java.util.Date());
	                    waitingListDao.persist(waitingList);
                    }
                }
            }


        } //end of waitingl list

        //enable_child_record
        String strMaxChildRecords=request.getParameter("max_child_record");
        
        int maxChildRecords = 0;
        try {
        	maxChildRecords = Integer.parseInt(strMaxChildRecords);
        }catch(NumberFormatException e) {
        	//ignore
        }
        
        for(int x=0;x<maxChildRecords+1;x++) {
	        String childLastName = request.getParameter("child_last_name" +x);
	        String childFirstName = request.getParameter("child_first_name"+x);
	        String childGender = request.getParameter("child_gender"+x);
	        String childDateOfBirth = request.getParameter("child_dob"+x);
	        
	        if(!StringUtils.isEmpty(childLastName) && !StringUtils.isEmpty(childFirstName) && !StringUtils.isEmpty(childGender) && !StringUtils.isEmpty(childDateOfBirth)) {
	        	Demographic childDemographic = new Demographic();
	        	childDemographic.setLastName(childLastName);
	        	childDemographic.setFirstName(childFirstName);
	        	childDemographic.setSex(childGender);
	        	if(childDateOfBirth.split("-").length==3) {
	        		String[] dateParts = childDateOfBirth.split("-");
	        		childDemographic.setYearOfBirth(dateParts[0]);
	        		childDemographic.setMonthOfBirth(dateParts[1]);
	        		childDemographic.setDateOfBirth(dateParts[2]);
	        	}
	        	childDemographic.setTitle("");

			childDemographic.setAddress(demographic.getAddress());
                        childDemographic.setCity(demographic.getCity());
                        childDemographic.setProvince(demographic.getProvince());
                        childDemographic.setPostal(demographic.getPostal());
                        childDemographic.setPhone(demographic.getPhone());


	        	childDemographic.setPhone2("");
	        	childDemographic.setEmail("");
	        	childDemographic.setHin("");
	        	childDemographic.setVer("");
	        	childDemographic.setPatientStatus("AC");
	        	childDemographic.setPatientStatusDate(new java.util.Date());
	        	childDemographic.setDateJoined(new java.util.Date());
	        	childDemographic.setChartNo("");
	        	childDemographic.setSpokenLanguage("");
	        	childDemographic.setRosterStatus("");
	        	childDemographic.setProviderNo("");
	        	childDemographic.setHcType(demographic.getHcType());
	        	childDemographic.setFamilyDoctor("<rdohip></rdohip><rd></rd>");
	        	childDemographic.setSin("");
	        	childDemographic.setCountryOfOrigin("-1");
	        	childDemographic.setNewsletter("Unknown");
	        	childDemographic.setLastUpdateUser(loggedInInfo.getLoggedInProviderNo());
	        	
	        	demographicDao.save(childDemographic);
	        	
	        	
	        	//setup relationship
	        	DemographicContact dc1 = new DemographicContact();
	       		dc1.setActive(true);
	       		dc1.setCategory("personal");
	       		dc1.setConsentToContact(true);
	       		dc1.setCreated(new java.util.Date());
	       		dc1.setCreator(loggedInInfo.getLoggedInProviderNo());
	       		dc1.setEc("");
	       		dc1.setFacilityId(loggedInInfo.getCurrentFacility().getId());
	       		dc1.setNote("");
	       		dc1.setSdm("");
	       		dc1.setType(1);
	       		
	       		dc1.setDemographicNo(demographic.getDemographicNo());
	       		dc1.setContactId(childDemographic.getDemographicNo().toString());
	       		
	       		if(childDemographic.getSex().equalsIgnoreCase("M")) {
	       			dc1.setRole("Son");
	       		}else if(childDemographic.getSex().equalsIgnoreCase("F")) {
	       			dc1.setRole("Daughter");
	       		} else {
	       			dc1.setRole("Child");
	       		}
	       		
	       		dc1.setUpdateDate(new java.util.Date());
	       		demographicContactDao.persist(dc1);
	       		
	       		DemographicContact dc2 = new DemographicContact();
	       		dc2.setActive(true);
	       		dc2.setCategory("personal");
	       		dc2.setConsentToContact(true);
	       		dc2.setCreated(new java.util.Date());
	       		dc2.setCreator(loggedInInfo.getLoggedInProviderNo());
	       		dc2.setEc("");
	       		dc2.setFacilityId(loggedInInfo.getCurrentFacility().getId());
	       		dc2.setNote("");
	       		dc2.setSdm("");
	       		dc2.setType(1);
	       		
	       		dc2.setDemographicNo(childDemographic.getDemographicNo());
	       		dc2.setContactId(demographic.getDemographicNo().toString());
	       		
	       		if(demographic.getSex().equalsIgnoreCase("M")) {
	       			dc2.setRole("Father");
	       		}else if(demographic.getSex().equalsIgnoreCase("F")) {
	       			dc2.setRole("Mother");
	       		} else {
	       			dc2.setRole("Parent");
	       		}
	       		
	       		dc2.setUpdateDate(new java.util.Date());
	       		demographicContactDao.persist(dc2);
	       	
	       		//admit the child in same programs as the parent
	            gieat = new GenericIntakeEditAction();
	            gieat.setAdmissionManager(am);
	            gieat.setProgramManager(pm);
	            gieat.admitBedCommunityProgram(loggedInInfo, childDemographic.getDemographicNo(),loggedInInfo.getLoggedInProviderNo(),Integer.parseInt(bedP),"","",new java.util.Date());
	
	    
	            if(servP!=null&&servP.length>0){
	  	  		  Set<Integer> s = new HashSet<Integer>();
	              for(String _s:servP) s.add(Integer.parseInt(_s));
	              gieat.admitServicePrograms(loggedInInfo, childDemographic.getDemographicNo(),loggedInInfo.getLoggedInProviderNo(),s,"",new java.util.Date(), loggedInInfo.getCurrentFacility().getId());
	            }
	          
	            LogAction.addLog(curUser_no, "add", "demographic", childDemographic.getDemographicNo().toString(), ip, childDemographic.getDemographicNo().toString());
	
	        }
	       		
        }
        //end of enable_child_record
        
        
        //if(request.getParameter("fromAppt")!=null && request.getParameter("provider_no").equals("1")) {
        if(start_time2!=null && !start_time2.equals("null")) {
	%>
	<script language="JavaScript">
	<!--
	document.addappt.action="../appointment/addappointment.jsp?user_id=<%=request.getParameter("creator")%>&provider_no=<%=provider_no2%>&bFirstDisp=<%=bFirstDisp2%>&appointment_date=<%=request.getParameter("appointment_date")%>&year=<%=year2%>&month=<%=month2%>&day=<%=day2%>&start_time=<%=start_time2%>&end_time=<%=end_time2%>&duration=<%=duration2%>&name=<%=URLEncoder.encode(bufName.toString())%>&chart_no=<%=URLEncoder.encode(bufChart.toString())%>&bFirstDisp=false&demographic_no=<%=dem.toString()%>&messageID=<%=request.getParameter("messageId")%>&doctor_no=<%=bufDoctorNo.toString()%>&notes=<%=request.getParameter("notes")%>&reason=<%=request.getParameter("reason")%>&location=<%=request.getParameter("location")%>&resources=<%=request.getParameter("resources")%>&type=<%=request.getParameter("type")%>&style=<%=request.getParameter("style")%>&billing=<%=request.getParameter("billing")%>&status=<%=request.getParameter("status")%>&createdatetime=<%=request.getParameter("createdatetime")%>&creator=<%=request.getParameter("creator")%>&remarks=<%=request.getParameter("remarks")%>";
	document.addappt.submit();
	//-->
	</SCRIPT> 
	<% } %>
</form>



<p>
<h2><bean:message key="demographic.demographicaddarecord.msgSuccessful" /></h2>

<a href="demographiccontrol.jsp?demographic_no=<%=dem%>&displaymode=edit&dboperation=search_detail"><bean:message key="demographic.demographicaddarecord.goToRecord"/></a>

<caisi:isModuleLoad moduleName="caisi">
<br/>
<a href="../PMmodule/ClientManager.do?id=<%=dem%>"><bean:message key="demographic.demographicaddarecord.goToCaisiRecord"/> (<a href="#"  onclick="popup(700,1027,'demographiccontrol.jsp?demographic_no=<%=dem%>&displaymode=edit&dboperation=search_detail')">New Window</a>)</a>
</caisi:isModuleLoad>


<caisi:isModuleLoad moduleName="caisi">
<br/>
<a href="../PMmodule/ClientManager.do?id=<%=dem%>"><bean:message key="demographic.demographicaddarecord.goToCaisiRecord"/></a>
</caisi:isModuleLoad>


<p></p>
<%@ include file="footer.jsp"%></center>
</body>
</html:html>
