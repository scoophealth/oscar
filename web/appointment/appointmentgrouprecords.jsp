
<% 
    
    String curProvider_no = request.getParameter("provider_no");
    String mygroupno = (String) session.getAttribute("groupno");  
    String deepcolor = "#CCCCFF", weakcolor = "#EEEEFF", tableTitle = "#99ccff";
	boolean bEdit = request.getParameter("appointment_no") != null ? true : false;
%>

<%@ page
	import="java.util.*, java.sql.*,java.net.*, oscar.*, oscar.util.*"
	errorPage="errorpage.jsp"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>

<jsp:useBean id="groupApptBean" class="oscar.AppointmentMainBean"
	scope="page" />
<jsp:useBean id="providerBean" class="java.util.Properties"
	scope="session" />
<%@ include file="../admin/dbconnection.jsp"%>
<% 
    String [][] dbQueries=new String[][] { 
        {"search_groupprovider", "select p.last_name, p.first_name, p.provider_no from mygroup m, provider p where m.mygroup_no=? and m.provider_no=p.provider_no order by p.last_name"}, 
        {"add_appt", "insert into appointment (provider_no,appointment_date,start_time,end_time,name, notes,reason,location,resources,type, style,billing,status,createdatetime,creator, remarks, demographic_no) values(?,?,?,?,?, ?,?,?,?,?, ?,?,?,?,?, ?,?)"}, 
        {"delete", "delete from appointment where appointment_date=? and start_time=? and end_time=? and name=? and creator=?"}, 
        {"search_scheduledate_single", "select * from scheduledate where sdate=? and provider_no=?" }, 
        {"search_appt", "select * from appointment where appointment_no = ?" }, 
        {"delete_appt", "delete from appointment where appointment_no = ?" }, 
        //{"cancel_appt", "update appointment set status = ?, createdatetime = ?, creator = ? where appointment_no = ?" }, 
        {"cancel_appt", "update appointment set status = ?, updatedatetime = ?, creator = ? where appointment_no = ?" }, 
        //{"update_appt", "update appointment set demographic_no=?,appointment_date=?,start_time=?,end_time=?,name=?, notes=?,reason =?,location=?, resources=?, type=?,style=?,billing =?,status=?,createdatetime=?,creator=?,remarks=? where appointment_no=? " }, 
        {"update_appt", "update appointment set demographic_no=?,appointment_date=?,start_time=?,end_time=?,name=?, notes=?,reason =?,location=?, resources=?, type=?,style=?,billing =?,status=?,updatedatetime=?,creator=?,remarks=? where appointment_no=? " }, 
        {"search_otherappt", "select * from appointment where appointment_date=? and ((start_time <= ? and end_time >= ?) or (start_time > ? and start_time < ?) ) order by provider_no, start_time" }, 
    };
    groupApptBean.doConfigure(dbParams,dbQueries);
%>

<%
  if (request.getParameter("groupappt")!=null) {
    boolean bSucc = false;
    if (request.getParameter("groupappt")!=null && request.getParameter("groupappt").equals("Add Group Appointment") ) {
        String[] param =new String[16];
        int rowsAffected=0, datano=0;
        StringBuffer strbuf=new StringBuffer();
		String createdDateTime = UtilDateUtilities.DateToString(UtilDateUtilities.now(),"yyyy-MM-dd HH:mm:ss");
		String userName =  (String) session.getAttribute("userlastname") + ", " + (String) session.getAttribute("userfirstname");

        for (Enumeration e = request.getParameterNames() ; e.hasMoreElements() ;) {
	        strbuf=new StringBuffer(e.nextElement().toString());
            if (strbuf.toString().indexOf("one")==-1 && strbuf.toString().indexOf("two")==-1) continue;
          
		    datano=Integer.parseInt(request.getParameter(strbuf.toString()) );
     	    param[0]=request.getParameter("provider_no"+datano);
	        param[1]=request.getParameter("appointment_date");
    	    param[2]=MyDateFormat.getTimeXX_XX_XX(request.getParameter("start_time"));
	        param[3]=MyDateFormat.getTimeXX_XX_XX(request.getParameter("end_time"));
            param[4] = request.getParameter("keyword");
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
	        int[] intparam=new int [1];
	        if (!(request.getParameter("demographic_no").equals("")) && strbuf.toString().indexOf("one") != -1) {
				intparam[0]= Integer.parseInt(request.getParameter("demographic_no"));
     	    } else intparam[0]=0;
            rowsAffected = groupApptBean.queryExecuteUpdate(param,intparam,"add_appt");

            if (rowsAffected != 1) break;
        }
        if (rowsAffected == 1) bSucc = true;
	}


    if (request.getParameter("groupappt")!=null && (request.getParameter("groupappt").equals("Group Update") 
		    || request.getParameter("groupappt").equals("Group Cancel") || request.getParameter("groupappt").equals("Group Delete"))) {
        int rowsAffected=0, datano=0;
        StringBuffer strbuf=new StringBuffer();
		String createdDateTime = UtilDateUtilities.DateToString(UtilDateUtilities.now(),"yyyy-MM-dd HH:mm:ss");
		String userName =  (String) session.getAttribute("userlastname") + ", " + (String) session.getAttribute("userfirstname");

		for (Enumeration e = request.getParameterNames() ; e.hasMoreElements() ;) {
	        strbuf=new StringBuffer(e.nextElement().toString());
            if (strbuf.toString().indexOf("one")==-1 && strbuf.toString().indexOf("two")==-1) continue;
 		    datano=Integer.parseInt(request.getParameter(strbuf.toString()) );

            if (request.getParameter("groupappt").equals("Group Cancel")) {
                String[] paramc =new String[4];
	            paramc[0]="C";
	            paramc[1]=createdDateTime;
     	        paramc[2]=userName;   //request.getParameter("createdatetime");
	            paramc[3]=request.getParameter("appointment_no" + datano);  //request.getParameter("creator");

                rowsAffected = groupApptBean.queryExecuteUpdate(paramc , "cancel_appt");
			}

			//can find and save them to recyclebin first
		    //delete the selected appts
            if (request.getParameter("groupappt").equals("Group Delete")) {
                rowsAffected = groupApptBean.queryExecuteUpdate(request.getParameter("appointment_no" + datano) , "delete_appt");
			}

			if (request.getParameter("groupappt").equals("Group Update")) {
                rowsAffected = groupApptBean.queryExecuteUpdate(request.getParameter("appointment_no" + datano) , "delete_appt");
     	        
                String[] paramu =new String[16];
				paramu[0]=request.getParameter("provider_no"+datano);
				paramu[1]=request.getParameter("appointment_date");
	    	    paramu[2]=MyDateFormat.getTimeXX_XX_XX(request.getParameter("start_time"));
		        paramu[3]=MyDateFormat.getTimeXX_XX_XX(request.getParameter("end_time"));
			    paramu[4] = request.getParameter("keyword");
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
			    int[] intparam=new int [1];
				if (!(request.getParameter("demographic_no").equals("")) && strbuf.toString().indexOf("one") != -1) {
					intparam[0]= Integer.parseInt(request.getParameter("demographic_no"));
		 	    } else intparam[0]=0;
			    
				rowsAffected = groupApptBean.queryExecuteUpdate(paramu,intparam,"add_appt");
	            if (rowsAffected != 1) break;
			}
		}
        if (rowsAffected == 1) bSucc = true;
	}
%>
<%   
    if (bSucc) {
%>
<h1><bean:message
	key="appointment.appointmentgrouprecords.msgAddSuccess" /></h1>
<script LANGUAGE="JavaScript">
self.close();
self.opener.refresh();
</script>
<%
        }  else {
%>
<p>
<h1><bean:message
	key="appointment.appointmentgrouprecords.msgAddFailure" /></h1>
</p>
<%  
    }
    groupApptBean.closePstmtConn();
    return;
  }
%>
<!--  
/*
 * 
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
 * This software is published under the GPL GNU General Public License. 
 * This program is free software; you can redistribute it and/or 
 * modify it under the terms of the GNU General Public License 
 * as published by the Free Software Foundation; either version 2 
 * of the License, or (at your option) any later version. * 
 * This program is distributed in the hope that it will be useful, 
 * but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
 * GNU General Public License for more details. * * You should have received a copy of the GNU General Public License 
 * along with this program; if not, write to the Free Software 
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. * 
 * 
 * <OSCAR TEAM>
 * 
 * This software was written for the 
 * Department of Family Medicine 
 * McMaster Unviersity 
 * Hamilton 
 * Ontario, Canada 
 */
-->
<html:html locale="true">
<head>
<title><bean:message
	key="appointment.appointmentgrouprecords.title" /></title>
<meta http-equiv="Expires" content="Monday, 8 Aug 88 18:18:18 GMT">
<meta http-equiv="Cache-Control" content="no-cache">
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
</SCRIPT>
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
    ResultSet rsdemo = null;

	if (bEdit) {
		rsdemo = groupApptBean.queryResults(request.getParameter("appointment_no"), "search_appt");
		while(rsdemo.next()) {
			eApptDate = rsdemo.getString("appointment_date");
	        eStartTime = rsdemo.getString("start_time");
			eEndTime = rsdemo.getString("end_time");
			eName = rsdemo.getString("name");
		}
	}


    String [] param0 = new String[5];
    param0[0] = eApptDate;
    param0[1] = eStartTime ;
    param0[2] = param0[1] ;
    param0[3] = param0[1] ;
    param0[4] = eEndTime ;
    String temp = "";
	String appt = "";
	String dotStr = "";
	boolean bOne = false;
	boolean bTwo = false;

    rsdemo = groupApptBean.queryResults(param0, "search_otherappt");
	while(rsdemo.next()) {
        bOne = false;
	    bTwo = false;
							
        if (eStartTime.equals(rsdemo.getString("start_time")) && eEndTime.equals(rsdemo.getString("end_time")) && 
			eName.equals(rsdemo.getString("name"))) {
			if (rsdemo.getString("demographic_no") != null && !rsdemo.getString("demographic_no").equals("0")  ) {
	            bOne = true;
			} else {
                bTwo = true;
			}
		}
		if (rsdemo.getString("demographic_no") != null && !rsdemo.getString("demographic_no").equals("0")) dotStr = "";
		else dotStr = ".";

        if (bOne)    otherAppt.setProperty(rsdemo.getString("provider_no")+"one", "checked");
        if (bTwo)    otherAppt.setProperty(rsdemo.getString("provider_no")+"two", "checked");
        if (bOne || bTwo) {
			otherAppt.setProperty(rsdemo.getString("provider_no")+"apptno", rsdemo.getString("appointment_no"));
			appt += "<b>" + rsdemo.getString("start_time").substring(0,5) + "-" + rsdemo.getString("end_time").substring(0,5) + "|" 
				 + dotStr + rsdemo.getString("name") + "</b>|" ; //+	rsdemo.getString("reason") + "<br>";
		} else {
			appt += rsdemo.getString("start_time").substring(0,5) + "-" + rsdemo.getString("end_time").substring(0,5) + "|" 
				 + dotStr + rsdemo.getString("name") + "|" ; //+	rsdemo.getString("reason") + "<br>";
		}

		if (!rsdemo.getString("provider_no").equals(temp) )  { //new provider record
            otherAppt.setProperty(rsdemo.getString("provider_no")+"appt", appt);
			temp = rsdemo.getString("provider_no");
			appt = "";
		} else {
		    if (otherAppt.getProperty(rsdemo.getString("provider_no")+"appt") != null)	
				appt = otherAppt.getProperty(rsdemo.getString("provider_no") +"appt")+ "<br>" + appt;
            otherAppt.setProperty(rsdemo.getString("provider_no")+"appt", appt);
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
    String [] param1 = new String[2];
    param1[0] = request.getParameter("appointment_date");

    int i=0;
	boolean bDefProvider = false;
	boolean bAvailProvider = false;
	boolean bLooperCon = false;
    rsdemo = groupApptBean.queryResults(mygroupno, "search_groupprovider");

	for (int j = 0; j < 2; j++) {
      while (rsdemo.next()) { 
        i++;

		param1[1] = rsdemo.getString("p.provider_no");
        ResultSet rsgroup = groupApptBean.queryResults(param1, "search_scheduledate_single");

		bAvailProvider = rsgroup.next() ? true : false;  
		if(bAvailProvider == bLooperCon) continue;

        bDefProvider = curProvider_no.equals(rsdemo.getString("p.provider_no")) ? true : false;
%>
	<tr
		BGCOLOR="<%=bDefProvider?deepcolor:(bAvailProvider?weakcolor:"#e0e0e0")%>">
		<td align='right'>&nbsp;<%=rsdemo.getString("p.last_name")%>, <%=rsdemo.getString("p.first_name")%></td>
		<td align='center'>&nbsp; <input type="checkbox" name="one<%=i%>"
			value="<%=i%>"
			<%=bEdit ? (otherAppt.getProperty(rsdemo.getString("p.provider_no")+"one")
		!= null ? otherAppt.getProperty(rsdemo.getString("p.provider_no")+"one") : "") : (bDefProvider? "checked":"")%>
			onclick="onCheck(this)"> <input type="hidden"
			name="provider_no<%=i%>"
			value="<%=rsdemo.getString("p.provider_no")%>"> <INPUT
			TYPE="hidden" NAME="last_name<%=i%>"
			VALUE='<%=rsdemo.getString("p.last_name")%>'> <INPUT
			TYPE="hidden" NAME="first_name<%=i%>"
			VALUE='<%=rsdemo.getString("p.first_name")%>'> <%    if (otherAppt.getProperty(rsdemo.getString("p.provider_no")+"apptno") != null) {%>
		<input type="hidden" name="appointment_no<%=i%>"
			value="<%=otherAppt.getProperty(rsdemo.getString("p.provider_no")+"apptno")%>">
		<%    }    %>
		</td>
		<td align='center'>&nbsp; <input type="checkbox" name="two<%=i%>"
			value="<%=i%>"
			<%=bEdit ? (otherAppt.getProperty(rsdemo.getString("p.provider_no")+"two")
		!= null ? otherAppt.getProperty(rsdemo.getString("p.provider_no")+"two") : "") : ""%>
			onclick="onCheck(this)"></td>
		<td nowrap><%=otherAppt.getProperty(rsdemo.getString("p.provider_no")+"appt")
		!= null ? otherAppt.getProperty(rsdemo.getString("p.provider_no")+"appt") : ""%>
		<%--  
    // <input type="text" name="orig<%=i%>" value="<%=bDefProvider? request.getParameter("reason"):""%>" style="width:100%">
--%> &nbsp;</td>
	</tr>
	<%
      }
      bLooperCon = true; 
	  i = 0;
      rsdemo.beforeFirst();
    }
    groupApptBean.closePstmtConn();
%>
	</tr>
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
