<%@ page
	import="java.sql.*, java.util.*, oscar.oscarDB.*, oscar.MyDateFormat, oscar.oscarWaitingList.WaitingList, org.oscarehr.common.OtherIdManager"
	errorPage="errorpage.jsp"%>
<%@ page import="oscar.log.*"%>
<%@ page import="oscar.oscarDemographic.data.*"%>
<jsp:useBean id="apptMainBean" class="oscar.AppointmentMainBean"
	scope="session" />
<jsp:useBean id="oscarVariables" class="java.util.Properties"
	scope="session" />

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
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
 * McMaster University
 * Hamilton
 * Ontario, Canada
 */
-->
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
<body onload="start()" bgproperties="fixed" topmargin="0" leftmargin="0"
	rightmargin="0">
<center>
<table border="0" cellspacing="0" cellpadding="0" width="100%">
	<tr bgcolor="#486ebd">
		<th align="CENTER"><font face="Helvetica" color="#FFFFFF">
		<bean:message key="demographic.demographicaddarecord.title" /></font></th>
	</tr>
</table>

<%
    String dem = null;

    String curUser_no = (String)session.getAttribute("user");
    
    //check to see if new case management is request
    ArrayList users = (ArrayList)session.getServletContext().getAttribute("CaseMgmtUsers");
    boolean newCaseManagement = false;
    
    if( users != null && users.size() > 0 )
        newCaseManagement = true;         

  //if action is good, then give me the result
	  //param[0]=Integer.parseIntdemographicaddarecord((new GregorianCalendar()).get(Calendar.MILLISECOND) ); //int
	  //temp variables for test/set null dates
	  String year, month, day;
      DBPreparedHandlerParam [] param =new DBPreparedHandlerParam[33];
	  param[0]=new DBPreparedHandlerParam(request.getParameter("last_name"));
	  param[1]=new DBPreparedHandlerParam(request.getParameter("first_name"));
	  param[2]=new DBPreparedHandlerParam(request.getParameter("address"));
	  param[3]=new DBPreparedHandlerParam(request.getParameter("city"));
	  param[4]=new DBPreparedHandlerParam(request.getParameter("province"));
	  param[5]=new DBPreparedHandlerParam(request.getParameter("postal"));
	  param[6]=new DBPreparedHandlerParam(request.getParameter("phone"));
	  param[7]=new DBPreparedHandlerParam(request.getParameter("phone2"));
	  param[8]=new DBPreparedHandlerParam(request.getParameter("email"));
	  param[9]=new DBPreparedHandlerParam(request.getParameter("pin"));
	  param[10]=new DBPreparedHandlerParam(request.getParameter("year_of_birth"));
	  param[11]=new DBPreparedHandlerParam(request.getParameter("month_of_birth")!=null && request.getParameter("month_of_birth").length()==1 ? "0"+request.getParameter("month_of_birth") : request.getParameter("month_of_birth"));
	  param[12]=new DBPreparedHandlerParam(request.getParameter("date_of_birth")!=null && request.getParameter("date_of_birth").length()==1 ? "0"+request.getParameter("date_of_birth") : request.getParameter("date_of_birth"));
	  param[13]=new DBPreparedHandlerParam(request.getParameter("hin"));
	  param[14]=new DBPreparedHandlerParam(request.getParameter("ver"));
	  param[15]=new DBPreparedHandlerParam(request.getParameter("roster_status"));
	  param[16]=new DBPreparedHandlerParam(request.getParameter("patient_status"));
	  // Databases have an alias for today. It is not necessary give the current date.
          // ** Overridden - we want to give users option to change if needed
          // ** Now defaults to current date on the add demographic screen
	  param[17]=new DBPreparedHandlerParam(MyDateFormat.getSysDate(request.getParameter("date_joined_year")+"-"+request.getParameter("date_joined_month")+"-"+request.getParameter("date_joined_date")));
	  param[18]=new DBPreparedHandlerParam(request.getParameter("chart_no"));
	  param[19]=new DBPreparedHandlerParam(request.getParameter("staff"));
	  param[20]=new DBPreparedHandlerParam(request.getParameter("sex"));

	  // If null, set year, month and date
	  if (request.getParameter("end_date_year").equals("")) {
	    year = "0001";
	  } else {
	    year = request.getParameter("end_date_year");
	  }

	  if (request.getParameter("end_date_month").equals("")) {
	    month = "01";
	  } else {
	    month = request.getParameter("end_date_month");
	  }

	  if (request.getParameter("end_date_date").equals("")) {
	    day = "01";
	  } else {
	    day = request.getParameter("end_date_date");
	  }

	  param[21] = new DBPreparedHandlerParam(MyDateFormat.getSysDate(year + "-" + month + "-" + day));

	  // If null, set year, month and date
	  if (request.getParameter("eff_date_year").equals("")) {
	    year = "0001";
	  } else {
	    year = request.getParameter("eff_date_year");
	  }

	  if (request.getParameter("eff_date_month").equals("")) {
	    month = "01";
	  } else {
	    month = request.getParameter("eff_date_month");
	  }

	  if (request.getParameter("eff_date_date").equals("")) {
	    day = "01";
	  } else {
	    day = request.getParameter("eff_date_date");
	  }

	  param[22] =  new DBPreparedHandlerParam(MyDateFormat.getSysDate(year + "-" + month + "-" + day));

	  param[23]=new DBPreparedHandlerParam(request.getParameter("pcn_indicator"));
	  param[24]=new DBPreparedHandlerParam(request.getParameter("hc_type"));

	  // If null, set year, month and date
	  if (request.getParameter("hc_renew_date_year").equals("")) {
	    year = "0001";
	  } else {
	    year = request.getParameter("hc_renew_date_year");
	  }

	  if (request.getParameter("hc_renew_date_month").equals("")) {
	    month = "01";
	  } else {
	    month = request.getParameter("hc_renew_date_month");
	  }

	  if (request.getParameter("hc_renew_date_date").equals("")) {
	    day = "01";
	  } else {
	    day = request.getParameter("hc_renew_date_date");
	  }

	  param[25] =new DBPreparedHandlerParam(MyDateFormat.getSysDate( year + "-" + month + "-" + day));
	  param[26] =new DBPreparedHandlerParam("<rdohip>" + request.getParameter("r_doctor_ohip") + "</rdohip>" + "<rd>" + request.getParameter("r_doctor") + "</rd>"+ (request.getParameter("family_doc")!=null? ("<family_doc>" + request.getParameter("family_doc") + "</family_doc>") : ""));
          param[27] =new DBPreparedHandlerParam(request.getParameter("countryOfOrigin"));
          param[28] =new DBPreparedHandlerParam(request.getParameter("newsletter"));     
          param[29] =new DBPreparedHandlerParam(request.getParameter("sin"));
	  param[30] =new DBPreparedHandlerParam(request.getParameter("title"));
	  param[31] =new DBPreparedHandlerParam(request.getParameter("official_lang"));
	  param[32] =new DBPreparedHandlerParam(request.getParameter("spoken_lang"));
          
	String[] paramName =new String[5];
	  paramName[0]=param[0].getStringValue().trim(); //last name
	  paramName[1]=param[1].getStringValue().trim(); //first name
	  paramName[2]=param[10].getStringValue().trim(); // year of dob
	  paramName[3]=param[11].getStringValue().trim(); // month of dob
	  paramName[4]=param[12].getStringValue().trim(); // day of dob
	  //System.out.println("from -------- :"+ param[0]+ ": next :"+param[1]);
    ResultSet rs = apptMainBean.queryResults(paramName, "search_lastfirstnamedob");

    if(rs.next()) {  %> ***<font color='red'><bean:message
	key="demographic.demographicaddarecord.msgDuplicatedRecord" /></font>***<br>
<br>
<a href=# onClick="history.go(-1);return false;"><b>&lt;-<bean:message
	key="global.btnBack" /></b></a> <% return;
    }

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
	    ResultSet rsHin = apptMainBean.queryResults(paramNameHin, "search_hin");
	    if(rsHin.next()) {  %> ***<font color='red'><bean:message
	key="demographic.demographicaddarecord.msgDuplicatedHIN" /></font>***<br>
<br>
<a href=# onClick="history.go(-1);return false;"><b>&lt;-<bean:message
	key="global.btnBack" /></b></a> <% return;
	    }
    }

    // int rowsAffected = apptMainBean.queryExecuteUpdate(intparam, param, request.getParameter("dboperation"));

  int rowsAffected = apptMainBean.queryExecuteUpdate(param, request.getParameter("dboperation")); //add_record
  if (rowsAffected ==1) {
  
    //find the demo_no and add democust record for alert
    String[] param1 =new String[7];
	  param1[0]=request.getParameter("last_name");
	  param1[1]=request.getParameter("first_name");
	  param1[2]=request.getParameter("year_of_birth");
	  param1[3]=request.getParameter("month_of_birth");
	  param1[4]=request.getParameter("date_of_birth");
	  param1[5]=request.getParameter("hin");
	  param1[6]=request.getParameter("ver");
 
    rs = apptMainBean.queryResults(param1, "search_demoaddno");
    if(rs.next()) { //
        
        //propagate demographic to caisi admission table
        if( newCaseManagement ) {
            //fetch programId associated with provider
            //if none(0) then check for OSCAR program; if available set it as default
            oscar.oscarEncounter.data.EctProgram program = new oscar.oscarEncounter.data.EctProgram(request.getSession());
            String progId = program.getProgram(request.getParameter("staff"));
            if( progId.equals("0") ) {                
                ResultSet rsProg = apptMainBean.queryResults("OSCAR", "search_program");
                if( rsProg.next() )
                    progId = rsProg.getString("id");
                    
                rsProg.close();
            }
            String[] caisiParam = new String[4];
            caisiParam[0] = apptMainBean.getString(rs,"demographic_no");
            caisiParam[1] = progId;
            caisiParam[2] = request.getParameter("staff");
            caisiParam[3] = request.getParameter("date_joined_year")+"-"+request.getParameter("date_joined_month")+"-"+request.getParameter("date_joined_date");
            apptMainBean.queryExecuteUpdate(caisiParam, "add2caisi_admission");
        }
        
        //add democust record for alert
        String[] param2 =new String[6];
	    param2[0]=apptMainBean.getString(rs,"demographic_no");
	    param2[1]=request.getParameter("cust1");
	    param2[2]=request.getParameter("cust2");
	    param2[3]=request.getParameter("cust3");
	    param2[4]=request.getParameter("cust4");
	    param2[5]="<unotes>"+request.getParameter("content")+"</unotes>";
	    //System.out.println("demographic_no" + param2[0] +param2[1]+param2[2]+param2[3]+param2[4]+param2[5] );
        rowsAffected = apptMainBean.queryExecuteUpdate(param2, "add_custrecord" ); //add_record

       dem = apptMainBean.getString(rs,"demographic_no");
       DemographicExt dExt = new DemographicExt();
	   OtherIdManager oidManager = new OtherIdManager();
	   
       String proNo = (String) session.getValue("user");
       dExt.addKey(proNo, dem, "hPhoneExt", request.getParameter("hPhoneExt"), "");
       dExt.addKey(proNo, dem, "wPhoneExt", request.getParameter("wPhoneExt"), "");
       dExt.addKey(proNo, dem, "demo_cell", request.getParameter("cellphone"), "");
       dExt.addKey(proNo, dem, "cytolNum",  request.getParameter("cytolNum"),  "");
       
       dExt.addKey(proNo, dem, "ethnicity",     request.getParameter("ethnicity"),     "");
       dExt.addKey(proNo, dem, "area",          request.getParameter("area"),          "");
       dExt.addKey(proNo, dem, "statusNum",     request.getParameter("statusNum"),     "");
       dExt.addKey(proNo, dem, "fNationCom",    request.getParameter("fNationCom"),    "");
       dExt.addKey(proNo, dem, "given_consent", request.getParameter("given_consent"), "");

       //for the IBD clinic
		oidManager.saveIdDemographic(dem, "meditech_id", request.getParameter("meditech_id"));
     
       // customized key
       if(oscarVariables.getProperty("demographicExt") != null) {
	       String [] propDemoExt = oscarVariables.getProperty("demographicExt","").split("\\|");
		   //System.out.println("propDemoExt:" + propDemoExt[0] );
	       for(int k=0; k<propDemoExt.length; k++) {
	           dExt.addKey(proNo,dem,propDemoExt[k],request.getParameter(propDemoExt[k].replace(' ','_')),"");
	       }
       }
       // customized key
       
		// add log
		String ip = request.getRemoteAddr();
		LogAction.addLog(curUser_no, "add", "demographic", param2[0], ip,param2[0]);

        //add to waiting list if the waiting_list parameter in the property file is set to true

        WaitingList wL = WaitingList.getInstance();
        if(wL.getFound()){

            String[] paramWLPosition = new String[1];
            paramWLPosition[0] = request.getParameter("list_id");
            if(paramWLPosition[0].compareTo("")!=0){
                ResultSet rsWL = apptMainBean.queryResults(paramWLPosition, "search_waitingListPosition");

                if(rsWL.next()){
                    System.out.println("max position: " + Integer.toString(rsWL.getInt("position")));
                    String[] paramWL = new String[6]; 
                    paramWL[0] = request.getParameter("list_id");
                    paramWL[1] = apptMainBean.getString(rs,"demographic_no");
                    paramWL[2] = request.getParameter("waiting_list_note");
                    paramWL[3] = Integer.toString(rsWL.getInt("position") + 1);
                    paramWL[4] = request.getParameter("waiting_list_referral_date");
                    paramWL[5] = "N"; 
                    if(paramWL[0]!=null && !paramWL[0].equals("") && !paramWL[0].equals("0"))
                        apptMainBean.queryExecuteUpdate(paramWL, "add2WaitingList");
                }
            }
        }

	  	if (request.getParameter("dboperation2") != null) {
	  	  	String[] parametros = new String[13];

	  	  	parametros[0]=apptMainBean.getString(rs,"demographic_no");
	  	  	parametros[1]=request.getParameter("cpf");
	  	  	parametros[2]=request.getParameter("rg");
	  	  	parametros[3]=request.getParameter("chart_address");
	  	  	parametros[4]=request.getParameter("marriage_certificate");
	  	  	parametros[5]=request.getParameter("birth_certificate");
	  	  	parametros[6]=request.getParameter("marital_state");
	  	  	parametros[7]=request.getParameter("partner_name");
	  	  	parametros[8]=request.getParameter("father_name");
	  	  	parametros[9]=request.getParameter("mother_name");
	  	  	parametros[10]=request.getParameter("district");
	  	  	parametros[11]=request.getParameter("address_no")==null || request.getParameter("address_no").trim().equals("")?"0":request.getParameter("address_no");
	  	  	parametros[12]=request.getParameter("complementary_address");


	  		rowsAffected = apptMainBean.queryExecuteUpdate(parametros, request.getParameter("dboperation2")); //add_record
	  	}

    }

%>
<p>
<h2><bean:message key="demographic.demographicaddarecord.msgSuccessful" /></h2>
    <a href="demographiccontrol.jsp?demographic_no=<%=dem%>&displaymode=edit&dboperation=search_detail"><bean:message key="demographic.demographicaddarecord.goToRecord"/></a>


<%
  } else {
%>
<p>
<h1><bean:message key="demographic.demographicaddarecord.msgFailed" /></h1>

<%
  }
  apptMainBean.closePstmtConn();
%>
<p></p>
<%@ include file="footer.jsp"%></center>
</body>
</html:html>
