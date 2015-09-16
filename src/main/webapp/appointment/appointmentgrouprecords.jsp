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
<security:oscarSec roleName="<%=roleName$%>" objectName="_appointment" rights="w" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../securityError.jsp?type=_appointment");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>

<%@page import="org.oscarehr.util.SessionConstants"%>
<%@page import="org.oscarehr.common.model.ProviderPreference"%>
<%@page import="org.oscarehr.common.dao.AppointmentArchiveDao" %>
<%@page import="org.oscarehr.common.dao.OscarAppointmentDao" %>
<%@page import="org.oscarehr.common.model.Appointment" %>
<%@page import="org.oscarehr.common.dao.MyGroupDao" %>
<%@page import="org.oscarehr.common.model.MyGroup" %>
<%@page import="org.oscarehr.common.model.Provider" %>
<%@page import="org.oscarehr.common.dao.ScheduleDateDao" %>
<%@page import="org.oscarehr.common.model.ScheduleDate" %>
<%@page import="org.oscarehr.util.SpringUtils" %>
<%
	AppointmentArchiveDao appointmentArchiveDao = (AppointmentArchiveDao)SpringUtils.getBean("appointmentArchiveDao");
	OscarAppointmentDao appointmentDao = (OscarAppointmentDao)SpringUtils.getBean("oscarAppointmentDao");
	MyGroupDao myGroupDao = SpringUtils.getBean(MyGroupDao.class);
	ScheduleDateDao scheduleDateDao = SpringUtils.getBean(ScheduleDateDao.class);
%>
<%
	String curProvider_no = request.getParameter("provider_no");
	ProviderPreference providerPreference=(ProviderPreference)session.getAttribute(SessionConstants.LOGGED_IN_PROVIDER_PREFERENCE);
	String mygroupno = providerPreference.getMyGroupNo();
    String deepcolor = "#CCCCFF", weakcolor = "#EEEEFF", tableTitle = "#99ccff";
	boolean bEdit = request.getParameter("appointment_no") != null ? true : false;
%>
<%@ page
	import="java.util.*, java.sql.*,java.net.*, oscar.*, oscar.util.*, org.oscarehr.common.OtherIdManager"
	errorPage="errorpage.jsp"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>

<%
  if (request.getParameter("groupappt") != null) {
    boolean bSucc = false;
    if (request.getParameter("groupappt").equals("Add Group Appointment")) {
        String[] param = new String[20];
        int rowsAffected = 0, datano = 0;
        StringBuffer strbuf = null;
		String createdDateTime = UtilDateUtilities.DateToString(new java.util.Date(),"yyyy-MM-dd HH:mm:ss");
		String userName = (String) session.getAttribute("userlastname") + ", " + (String) session.getAttribute("userfirstname");

        param[1]=request.getParameter("appointment_date");
        param[2]=MyDateFormat.getTimeXX_XX_XX(request.getParameter("start_time"));
        param[3]=MyDateFormat.getTimeXX_XX_XX(request.getParameter("end_time"));
        param[4]=request.getParameter("keyword");
        param[5]=request.getParameter("notes");
        param[6]=request.getParameter("reason");
        param[7]=request.getParameter("location");
        param[8]=request.getParameter("resources");
        param[9]=request.getParameter("type");
        param[10]=request.getParameter("style");
        param[11]=request.getParameter("billing");
        param[12]=request.getParameter("status");
        param[13]=createdDateTime;   //request.getParameter("createdatetime");
        param[14]=userName;  //request.getParameter("creator");
        param[15]=request.getParameter("remarks");
        param[17]=(String)request.getSession().getAttribute("programId_oscarView");
        param[18]=request.getParameter("urgency");
        param[19]=request.getParameter("reasonCode");

		String[] param2 = new String[7];
        for (Enumeration e = request.getParameterNames() ; e.hasMoreElements() ;) {
	        strbuf = new StringBuffer(e.nextElement().toString());
            if (strbuf.toString().indexOf("one")==-1 && strbuf.toString().indexOf("two")==-1) 
            	continue;
		    datano=Integer.parseInt(request.getParameter(strbuf.toString()) );
		    
		    
		    Appointment a = new Appointment();
			a.setProviderNo(request.getParameter("provider_no"+datano));
			a.setAppointmentDate(ConversionUtils.fromDateString(request.getParameter("appointment_date")));
			a.setStartTime(ConversionUtils.fromTimeStringNoSeconds(request.getParameter("start_time")));
			a.setEndTime(ConversionUtils.fromTimeStringNoSeconds(request.getParameter("end_time")));
			a.setName(request.getParameter("keyword"));
			a.setNotes(request.getParameter("notes"));
			a.setReason(request.getParameter("reason"));
			a.setLocation(request.getParameter("location"));
			a.setResources(request.getParameter("resources"));
			a.setType(request.getParameter("type"));
			a.setStyle(request.getParameter("style"));
			a.setBilling(request.getParameter("billing"));
			a.setStatus(request.getParameter("status"));
			a.setCreateDateTime(new java.util.Date());
			a.setCreator(userName);
			a.setRemarks(request.getParameter("remarks"));
			
			if (!(request.getParameter("demographic_no").equals("")) && strbuf.toString().indexOf("one") != -1) {
				a.setDemographicNo(Integer.parseInt(request.getParameter("demographic_no")));
     	    } else {
     	    	a.setDemographicNo(0);
     	    }
			
			a.setProgramId(Integer.parseInt((String)request.getSession().getAttribute("programId_oscarView")));
			a.setUrgency(request.getParameter("urgency"));
			a.setReasonCode(Integer.parseInt(request.getParameter("reasonCode")));
			
			appointmentDao.persist(a);
			rowsAffected=1;
		    

			param2[0]=param[0]; //provider_no
			param2[1]=param[1]; //appointment_date
			param2[2]=param[2]; //start_time
			param2[3]=param[3]; //end_time
			param2[4]=param[13]; //createdatetime
			param2[5]=param[14]; //creator
			param2[6]=param[16]; //demographic_no

			int demographicNo = 0;
			if (!(request.getParameter("demographic_no").equals("")) && strbuf.toString().indexOf("one") != -1) {
				demographicNo = Integer.parseInt(request.getParameter("demographic_no"));
     	    } 
			
			Appointment appt = appointmentDao.search_appt_no(request.getParameter("provider_no"+datano), ConversionUtils.fromDateString(request.getParameter("appointment_date")), 
					ConversionUtils.fromTimeStringNoSeconds(request.getParameter("start_time")), ConversionUtils.fromTimeStringNoSeconds(request.getParameter("end_time")), 
					ConversionUtils.fromTimestampString(createdDateTime), userName, demographicNo);
			
			if (appt != null) {
				Integer apptNo = appt.getId();
				String mcNumber = request.getParameter("appt_mc_number");
				OtherIdManager.saveIdAppointment(apptNo, "appt_mc_number", mcNumber);
			}
        }
        if (rowsAffected == 1) bSucc = true;
	}

    if (request.getParameter("groupappt").equals("Group Update") || request.getParameter("groupappt").equals("Group Cancel") ||
    		request.getParameter("groupappt").equals("Group Delete")) {
        int rowsAffected = 0, datano = 0;
        StringBuffer strbuf = null;
		String createdDateTime = UtilDateUtilities.DateToString(new java.util.Date(),"yyyy-MM-dd HH:mm:ss");
		String userName = (String) session.getAttribute("userlastname") + ", " + (String) session.getAttribute("userfirstname");

		for (Enumeration e = request.getParameterNames() ; e.hasMoreElements() ;) {
	        strbuf=new StringBuffer(e.nextElement().toString());
            if (strbuf.toString().indexOf("one")==-1 && strbuf.toString().indexOf("two")==-1) continue;
 		    datano=Integer.parseInt(request.getParameter(strbuf.toString()) );

            if (request.getParameter("groupappt").equals("Group Cancel")) {
	            Appointment appt = appointmentDao.find(Integer.parseInt(request.getParameter("appointment_no"+datano)));
	            appointmentArchiveDao.archiveAppointment(appt);
	            if(appt != null) {
	              	appt.setStatus("C");
	              	appt.setLastUpdateUser(userName);
	              	appointmentDao.merge(appt);
	              	rowsAffected=1;
	              }
			}

		    //delete the selected appts
            if (request.getParameter("groupappt").equals("Group Delete")) {
            	if( request.getParameter("appointment_no"+datano) != null ) {
            		Appointment appt = appointmentDao.find(Integer.parseInt(request.getParameter("appointment_no"+datano)));
            		if( appt != null ) {
	            		appointmentArchiveDao.archiveAppointment(appt);
	            		appointmentDao.remove(appt.getId());
	            		rowsAffected=1;
	            	
            		}
            	}
            }

            if (request.getParameter("groupappt").equals("Group Update")) {            	
            	if( request.getParameter("appointment_no"+datano) != null ) {
            		Appointment appt = appointmentDao.find(Integer.parseInt(request.getParameter("appointment_no"+datano)));
            		if( appt != null ) {
	            		appointmentArchiveDao.archiveAppointment(appt);
	            		appointmentDao.remove(appt.getId());
	            		rowsAffected=1;
	            	
            		}
            	}

            	String[] paramu = new String[20];
            	paramu[0]=request.getParameter("provider_no"+datano);
            	 paramu[1]=request.getParameter("appointment_date");
            	 paramu[2]=MyDateFormat.getTimeXX_XX_XX(request.getParameter("start_time"));
            	 paramu[3]=MyDateFormat.getTimeXX_XX_XX(request.getParameter("end_time"));
            	 paramu[4]=request.getParameter("keyword");
            	 paramu[5]=request.getParameter("notes");
            	 paramu[6]=request.getParameter("reason");
            	 paramu[7]=request.getParameter("location");
            	 paramu[8]=request.getParameter("resources");
            	 paramu[9]=request.getParameter("type");
            	 paramu[10]=request.getParameter("style");
            	 paramu[11]=request.getParameter("billing");
            	 paramu[12]=request.getParameter("status");
            	 paramu[13]=createdDateTime;   //request.getParameter("createdatetime");
            	 paramu[14]=userName;  //request.getParameter("creator");
            	 paramu[15]=request.getParameter("remarks");
            	 paramu[17]=(String)request.getSession().getAttribute("programId_oscarView");
            	 paramu[18]=request.getParameter("urgency");
            	 paramu[19]=request.getParameter("reasonCode");            	
              
		    	Appointment a = new Appointment();
				a.setProviderNo(request.getParameter("provider_no"+datano));
				a.setAppointmentDate(ConversionUtils.fromDateString(request.getParameter("appointment_date")));
				a.setStartTime(ConversionUtils.fromTimeStringNoSeconds(request.getParameter("start_time")));
				a.setEndTime(ConversionUtils.fromTimeStringNoSeconds(request.getParameter("end_time")));
				a.setName(request.getParameter("keyword"));
				a.setNotes(request.getParameter("notes"));
				a.setReason(request.getParameter("reason"));
				a.setLocation(request.getParameter("location"));
				a.setResources(request.getParameter("resources"));
				a.setType(request.getParameter("type"));
				a.setStyle(request.getParameter("style"));
				a.setBilling(request.getParameter("billing"));
				a.setStatus(request.getParameter("status"));
				a.setCreateDateTime(new java.util.Date());
				a.setCreator(userName);
				a.setRemarks(request.getParameter("remarks"));
				
				 if (!(request.getParameter("demographic_no").equals("")) && strbuf.toString().indexOf("one") != -1) {
					a.setDemographicNo(Integer.parseInt(request.getParameter("demographic_no")));
	     	    } else {
	     	    	a.setDemographicNo(0);
	     	    }
				
				a.setProgramId(Integer.parseInt((String)request.getSession().getAttribute("programId_oscarView")));
				a.setUrgency(request.getParameter("urgency"));
				a.setReasonCode(Integer.parseInt(request.getParameter("reasonCode")));
				
				appointmentDao.persist(a);
				rowsAffected=1;
		    	
				if (rowsAffected==1) {				
					
					int demographicNo=0;
					if (!(request.getParameter("demographic_no").equals("")) && strbuf.toString().indexOf("one") != -1) {
						demographicNo = Integer.parseInt(request.getParameter("demographic_no"));
		     	    }
					
					
					Appointment appt = appointmentDao.search_appt_no(request.getParameter("provider_no"+datano), ConversionUtils.fromDateString(request.getParameter("appointment_date")), 
							ConversionUtils.fromTimeStringNoSeconds(request.getParameter("start_time")), ConversionUtils.fromTimeStringNoSeconds(request.getParameter("end_time")), 
							ConversionUtils.fromTimestampString(createdDateTime), userName, demographicNo);
					

					if (appt != null) {
						Integer apptNo = appt.getId();
						String mcNumber = request.getParameter("appt_mc_number");
						OtherIdManager.saveIdAppointment(apptNo, "appt_mc_number", mcNumber);
					}
				}
			}
            if (rowsAffected != 1) break;
		}
        if (rowsAffected == 1) bSucc = true;
	}

	if (bSucc) {
%>
<h1><bean:message
	key="appointment.appointmentgrouprecords.msgAddSuccess" /></h1>
<script LANGUAGE="JavaScript">
	self.opener.refresh();
	self.close();
</script>
<%
	} else {
%>
<p>
<h1><bean:message
	key="appointment.appointmentgrouprecords.msgAddFailure" /></h1>

<%
	}
	return;
  } // if (request.getParameter("groupappt") != null)
%>
<html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title><bean:message
	key="appointment.appointmentgrouprecords.title" /></title>
<script language="JavaScript">
<!--

function onCheck(a) {
    if (a.checked) {
		var s ;
        if(a.name.indexOf("one") != -1) {
			s = "two"+a.name.substring(3) ;
		} else {
			s = "one"+a.name.substring(3) ;
		}
		unCheck(s);
    }
}
function unCheck(s) {
    for (var i =0; i <document.groupappt.elements.length; i++) {
        if (document.groupappt.elements[i].name == s) {
            document.groupappt.elements[i].checked = false;
    	}
	}
}
function isCheck(s) {
    for (var i =0; i <document.groupappt.elements.length; i++) {
        if (document.groupappt.elements[i].name == s) {
            return (document.groupappt.elements[i].checked);
    	}
    }
}
function checkAll(col, value, opo) {
    var f = document.groupappt.elements;
        for (var i=0; i < document.groupappt.elements.length; i++) {
            if (value == 'true') {
                if(document.groupappt.elements[i].name.indexOf(col) != -1) {
                    var tar = document.groupappt.elements[i].name;
                    var oposite = opo + tar.substring(3);
                    if (isCheck(oposite)) continue;
                    document.groupappt.elements[i].checked = true;
                }
            } else {
                if(document.groupappt.elements[i].name.indexOf(col) != -1) {
                    document.groupappt.elements[i].checked = false;
                }
            }
        }
    return false;
}
function onExit() {
    if (confirm("<bean:message key="appointment.appointmentgrouprecords.msgExitConfirmation"/>")) {
        window.close()
	}
}

var saveTemp=0;
function onButDelete() {
  saveTemp=1;
}
function onSub() {
  if( saveTemp==1 ) {
    return (confirm("<bean:message key="appointment.appointmentgrouprecords.msgDeleteConfirmation"/>")) ;
  }
}
//-->
</script>
</head>

<body bgcolor="ivory" onLoad="setfocus()" topmargin="0" leftmargin="0"
	rightmargin="0">
<form name="groupappt" method="POST"
	action="appointmentgrouprecords.jsp" onSubmit="return ( onSub());">
<INPUT TYPE="hidden" NAME="groupappt" value="">
<table width="100%" BGCOLOR="silver">
	<tr>
		<TD>
		<%    if (bEdit) {    %> <INPUT TYPE="button"
			onclick="document.forms['groupappt'].groupappt.value='Group Update'; document.forms['groupappt'].submit();"
			VALUE="<bean:message key="appointment.appointmentgrouprecords.btnGroupUpdate"/>">
		<INPUT TYPE="button"
			onclick="document.forms['groupappt'].groupappt.value='Group Cancel'; document.forms['groupappt'].submit();"
			VALUE="<bean:message key="appointment.appointmentgrouprecords.btnGroupCancel"/>">
		<INPUT TYPE="button"
			onclick="document.forms['groupappt'].groupappt.value='Group Delete'; document.forms['groupappt'].submit();"
			VALUE="<bean:message key="appointment.appointmentgrouprecords.btnGroupDelete"/>"
			onClick="onButDelete()"> <%    } else {    %> <INPUT
			TYPE="button"
			onclick="document.forms['groupappt'].groupappt.value='Add Group Appointment'; document.forms['groupappt'].submit();"
			VALUE="<bean:message key="appointment.appointmentgrouprecords.btnAddGroupAppt"/>">
		<%    }    %>
		</TD>
		<TD align="right"><INPUT TYPE="button"
			VALUE=" <bean:message key="global.btnBack"/> "
			onClick="window.history.go(-1);return false;"> <INPUT
			TYPE="button" VALUE=" <bean:message key="global.btnExit"/> "
			onClick="onExit()"></TD>
	</tr>
</table>

<table border=0 cellspacing=0 cellpadding=0 width="100%">
	<tr bgcolor="<%=deepcolor%>">
		<th><font face="Helvetica"><bean:message
			key="appointment.appointmentgrouprecords.msgLabel" /></font></th>
	</tr>
</table>

<%
    Properties otherAppt = new Properties();
	String eApptDate = request.getParameter("appointment_date");
	String eStartTime = MyDateFormat.getTimeXX_XX_XX(request.getParameter("start_time"));
	String eEndTime = MyDateFormat.getTimeXX_XX_XX(request.getParameter("end_time"));
	String eName = request.getParameter("keyword");

	if (bEdit) {
		Appointment appt = appointmentDao.find(Integer.parseInt(request.getParameter("appointment_no")));
		if(appt != null) {
			eApptDate = ConversionUtils.toDateString(appt.getAppointmentDate());
	        eStartTime = ConversionUtils.toTimeStringNoSeconds(appt.getStartTime());
			eEndTime = ConversionUtils.toTimeStringNoSeconds(appt.getEndTime());
			eName = appt.getName();
		}
	}

    String temp = "";
	String appt = "";
	String dotStr = "";
	boolean bOne = false;
	boolean bTwo = false;

	List<Appointment> otherAppts = appointmentDao.search_otherappt(ConversionUtils.fromDateString(eApptDate), ConversionUtils.fromTimeStringNoSeconds(eStartTime), ConversionUtils.fromTimeStringNoSeconds(eEndTime), 
			 ConversionUtils.fromTimeStringNoSeconds(eStartTime),  ConversionUtils.fromTimeStringNoSeconds(eStartTime));
	
	for (Appointment other : otherAppts) {
        bOne = false;
	    bTwo = false;

        if (eStartTime.equals(String.valueOf(ConversionUtils.toTimeStringNoSeconds(other.getStartTime()))) && eEndTime.equals(String.valueOf(ConversionUtils.toTimeStringNoSeconds(other.getEndTime()))) &&
			eName.equals(other.getName())) {
			if (other.getDemographicNo() != 0  ) {
	            bOne = true;
			} else {
                bTwo = true;
			}
		}
		if (other.getDemographicNo() != 0) dotStr = "";
		else dotStr = ".";

        if (bOne)    otherAppt.setProperty(other.getProviderNo()+"one", "checked");
        if (bTwo)    otherAppt.setProperty(other.getProviderNo()+"two", "checked");
        if (bOne || bTwo) {
			otherAppt.setProperty(other.getProviderNo()+"apptno", String.valueOf(other.getId()));
			appt += "<b>" + String.valueOf(ConversionUtils.toTimeStringNoSeconds(other.getStartTime())).substring(0,5) + "-" + String.valueOf(ConversionUtils.toTimeStringNoSeconds(other.getEndTime())).substring(0,5) + "|"
				 + dotStr + other.getName() + "</b>|" ; //+	rsdemo.getString("reason") + "<br>";
		} else {
			appt += String.valueOf(ConversionUtils.toTimeStringNoSeconds(other.getStartTime())).substring(0,5) + "-" + String.valueOf(ConversionUtils.toTimeStringNoSeconds(other.getEndTime())).substring(0,5) + "|"
				 + dotStr + other.getName() + "|" ; //+	rsdemo.getString("reason") + "<br>";
		}

		if (!String.valueOf(other.getProviderNo()).equals(temp))  { //new provider record
            otherAppt.setProperty(other.getProviderNo()+"appt", appt);
			temp = String.valueOf(other.getProviderNo());
			appt = "";
		} else {
		    if (otherAppt.getProperty(other.getProviderNo()+"appt") != null)
				appt = otherAppt.getProperty(other.getProviderNo() +"appt")+ "<br>" + appt;
            otherAppt.setProperty(other.getProviderNo()+"appt", appt);
    	    appt = "";
		}
    }


	for (Enumeration e = request.getParameterNames() ; e.hasMoreElements() ;) {
		temp=e.nextElement().toString();
		if(temp.equals("dboperation") ||temp.equals("displaymode") ||temp.equals("search_mode") ||temp.equals("chart_no")) continue;
  	    out.println("<input type='hidden' name='"+temp+"' value=\"" + UtilMisc.htmlEscape(request.getParameter(temp)) + "\">");
    }
%>
<table border=0 cellspacing=0 cellpadding=0 width="100%">
	<tr>
		<td nowrap><font color='black'><%=request.getParameter("appointment_date")%>
		| <%=request.getParameter("start_time")%> - <%=request.getParameter("end_time")%>
		| <%=UtilMisc.toUpperLowerCase(request.getParameter("keyword"))%></font></td>
		<td align='right' nowrap>Group : <%=mygroupno%></td>
	</tr>
</table>
<table BORDER="0" CELLPADDING="2" CELLSPACING="2" WIDTH="100%"
	BGCOLOR="white">
	<tr BGCOLOR="<%=tableTitle%>">
		<th width=30% nowrap><bean:message
			key="appointment.appointmentgrouprecords.msgProviderName" /></th>
		<th width=11% nowrap><bean:message
			key="appointment.appointmentgrouprecords.msgFirstAppointment" /></th>
		<th width=11% nowrap><bean:message
			key="appointment.appointmentgrouprecords.msgSecondAppointment" /></th>
		<th width=48% nowrap><bean:message
			key="appointment.appointmentgrouprecords.msgExistedAppointment" /></th>
	</tr>
	<%

    int i=0;
	boolean bDefProvider = false;
	boolean bAvailProvider = false;
	boolean bLooperCon = false;
	
	List<Provider> gps = myGroupDao.search_groupprovider(mygroupno);
	for (int j = 0; j < 2; j++) {
	  for (Provider provider : gps) {
        i++;

		
		ScheduleDate sd = scheduleDateDao.findByProviderNoAndDate(provider.getProviderNo(), ConversionUtils.fromDateString(request.getParameter("appointment_date")));
		
		bAvailProvider = (sd != null) ? true : false;
		if(bAvailProvider == bLooperCon) continue;

        bDefProvider = curProvider_no.equals(provider.getProviderNo()) ? true : false;
%>
	<tr
		BGCOLOR="<%=bDefProvider?deepcolor:(bAvailProvider?weakcolor:"#e0e0e0")%>">
		<td align='right'>&nbsp;<%=provider.getFormattedName()%></td>
		<td align='center'>&nbsp; <input type="checkbox" name="one<%=i%>"
			value="<%=i%>"
			<%=bEdit ? (otherAppt.getProperty(provider.getProviderNo()+"one")
		!= null ? otherAppt.getProperty(provider.getProviderNo()+"one") : "") : (bDefProvider? "checked":"")%>
			onclick="onCheck(this)"> <input type="hidden"
			name="provider_no<%=i%>"
			value="<%=provider.getProviderNo()%>"> <INPUT
			TYPE="hidden" NAME="last_name<%=i%>"
			VALUE='<%=provider.getLastName()%>'> <INPUT
			TYPE="hidden" NAME="first_name<%=i%>"
			VALUE='<%=provider.getFirstName()%>'> <%    if (otherAppt.getProperty(provider.getProviderNo()+"apptno") != null) {%>
		<input type="hidden" name="appointment_no<%=i%>"
			value="<%=otherAppt.getProperty(provider.getProviderNo()+"apptno")%>">
		<%    }    %>
		</td>
		<td align='center'>&nbsp; <input type="checkbox" name="two<%=i%>"
			value="<%=i%>"
			<%=bEdit ? (otherAppt.getProperty(provider.getProviderNo()+"two")
		!= null ? otherAppt.getProperty(provider.getProviderNo()+"two") : "") : ""%>
			onclick="onCheck(this)"></td>
		<td nowrap><%=otherAppt.getProperty(provider.getProviderNo()+"appt")
		!= null ? otherAppt.getProperty(provider.getProviderNo()+"appt") : ""%>
		<%--
    // <input type="text" name="orig<%=i%>" value="<%=bDefProvider? request.getParameter("reason"):""%>" style="width:100%">
--%> &nbsp;</td>
	</tr>
	<%
      }
      bLooperCon = true;
	  i = 0;
    }
%>
	<tr bgcolor='silver'>
		<td align='right' colspan=2><a href=#
			onClick='checkAll("one", "true", "two"); return false;'>Check All</a>
		|<a href=# onClick='checkAll("one", "false", "two"); return false;'>Clear
		All</a></td>
		<td colspan=2><a href=#
			onClick='checkAll("two", "true", "one"); return false;'>Check All</a>
		|<a href=# onClick='checkAll("two", "false", "one"); return false;'>Clear
		All</a></td>
	</tr>
</table>

<table width="100%" BGCOLOR="silver">
	<tr>
		<TD>
		<%    if (bEdit) {    %> <INPUT TYPE="button"
			onclick="document.forms['groupappt'].groupappt.value='Group Update'; document.forms['groupappt'].submit();"
			VALUE="<bean:message key="appointment.appointmentgrouprecords.btnGroupUpdate"/>">
		<INPUT TYPE="button"
			onclick="document.forms['groupappt'].groupappt.value='Group Cancel'; document.forms['groupappt'].submit();"
			VALUE="<bean:message key="appointment.appointmentgrouprecords.btnGroupCancel"/>">
		<INPUT TYPE="button"
			onclick="document.forms['groupappt'].groupappt.value='Group Delete'; document.forms['groupappt'].submit();"
			VALUE="<bean:message key="appointment.appointmentgrouprecords.btnGroupDelete"/>"
			onClick="onButDelete()"> <%    } else {    %> <INPUT
			TYPE="button"
			onclick="document.forms['groupappt'].groupappt.value='Add Group Appointment'; document.forms['groupappt'].submit();"
			VALUE="<bean:message key="appointment.appointmentgrouprecords.btnAddGroupAppt"/>">
		<%    }    %>
		</TD>
		<TD align="right"><INPUT TYPE="button"
			VALUE=" <bean:message key="global.btnBack"/> "
			onClick="window.history.go(-1);return false;"> <INPUT
			TYPE="button" VALUE=" <bean:message key="global.btnExit"/> "
			onClick="onExit()"></TD>
	</tr>
</table>

</form>
</body>
</html:html>
