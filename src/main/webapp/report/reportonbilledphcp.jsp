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

<%


  String   curUser_no   = (String)session.getAttribute("user");
  String   tdTitleColor = "#CCCC99";
  String   tdSubtitleColor = "#CCFF99";
  String   tdInterlColor = "white";
  String[] ROLE         = new String[]{"doctor", "resident", "nurse", "social worker", "other"};
  Vector[] VEC_PROVIDER = new Vector[]{new Vector(), new Vector(), new Vector(), new Vector(), new Vector()};
  String   codeType     = request.getParameter("codeType");
  String   startDate    = request.getParameter("startDate");
  String   endDate      = request.getParameter("endDate");
  String   providerNo   = null;
  String   providerName   = "";
  int[] total = {0,0,0,0,0, 0,0,0,0,0, 0,0,0,0,0, 0,0,0,0,0, 0,0};

  String[] providerNoName = {"providerNoDoctor", "providerNoResident", "providerNoNP", "providerNoSW"};
  for(int i=0; i<providerNoName.length; i++) {
    if(request.getParameter(providerNoName[i])!=null && !"".equals(request.getParameter(providerNoName[i]))) {
      providerNo   = request.getParameter(providerNoName[i]);
      break;
    }
  }
%>
<%@ page errorPage="../errorpage.jsp"%>
<%@ page import="java.util.*"%>
<%@ page import="java.sql.*"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%
  DBPreparedHandler dbObj = new DBPreparedHandler();
  // select provider list
  Properties        prop  = new Properties();
  String            sql   = "select u.*, p.first_name, p.last_name from secUserRole u, provider p ";

  sql += "where u.provider_no=p.provider_no  order by p.first_name, p.last_name";

  ResultSet rs = dbObj.queryResults(sql);

  while (rs.next()) {
    prop = new Properties();

    prop.setProperty("providerNo", Misc.getString(rs,"provider_no"));
    prop.setProperty("firstName", Misc.getString(rs,"first_name"));
    prop.setProperty("lastName", Misc.getString(rs,"last_name"));

    String roleName = Misc.getString(rs,"role_name");

    for (int i = 0; i < ROLE.length; i++) {
      if (ROLE[i].equals(roleName)) {
        VEC_PROVIDER[i].add(prop);
      }
    }

    if(Misc.getString(rs,"provider_no").equals(providerNo))
      providerName = Misc.getString(rs,"first_name") + " " + Misc.getString(rs,"last_name");
  }
%>
<%@page import="oscar.oscarDB.DBPreparedHandler"%>

<%@page import="oscar.Misc"%><html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>PHCP Report</title>
<link rel="stylesheet" href="../css/receptionistapptstyle.css">
<!-- calendar stylesheet -->
<link rel="stylesheet" type="text/css" media="all"
	href="../share/calendar/calendar.css" title="win2k-cold-1" />
<!-- main calendar program -->
<script type="text/javascript" src="../share/calendar/calendar.js"></script>
<!-- language for the calendar -->
<script type="text/javascript"
	src="../share/calendar/lang/<bean:message key="global.javascript.calendar"/>"></script>
<!-- the following script defines the Calendar.setup helper function, which makes
       adding a calendar a matter of 1 or 2 lines of code. -->
<script type="text/javascript" src="../share/calendar/calendar-setup.js"></script>
<script language="JavaScript">

                <!--
function setfocus() {
	this.focus();
	//  document.titlesearch.keyword.select();
}
function onSub() {
  if( document.myform.codeType.value=="" || document.myform.startDate.value=="" || document.myform.endDate.value==""
    || (document.myform.providerNoDoctor.value=="" && document.myform.providerNoResident.value==""
     && document.myform.providerNoNP.value=="" && document.myform.providerNoSW.value=="") ) {
    alert("Please select the codeType/period/provider item(s) from the drop down list before query.");
    return false ;
  } else {
    return true;
  }
}

//-->

      </script>
</head>
<body bgproperties="fixed" onLoad="setfocus()" topmargin="0"
	leftmargin="0" rightmargin="0">
<table border="0" cellspacing="0" cellpadding="0" width="100%">
	<tr bgcolor="#486ebd">
		<th align="CENTER" width="90%"><font face="Helvetica"
			color="#FFFFFF"> PHCP Encounter Report </font></th>
	</tr>
</table>
<form name="myform" action="reportonbilledphcp.jsp" method="POST"
	onSubmit="return ( onSub());">
<table width="100%" border="0" bgcolor="ivory" cellspacing="1"
	cellpadding="1">
	<tr bgcolor="lightsteelblue">
		<td>Code: <br>
		<select name="codeType">
			<option value="">--Code--</option>
			<option value="DxCode">DxCode</option>
			<option value="ServiceCode">ServiceCode</option>
		</select></td>
		<td nowrap>start <input type="text" name="startDate"
			id="startDate" value="<%=startDate!=null?startDate:""%>" size="10"
			readonly> <img src="../images/cal.gif" id="startDate_cal">
		end <input type="text" name="endDate" id="endDate"
			value="<%=endDate!=null?endDate:""%>" size="10" readonly> <img
			src="../images/cal.gif" id="endDate_cal"></td>
		<td>Provider: <select name="providerNoDoctor">
			<option value="">------Doctor------</option>
			<%
                for (int i = 0; i < VEC_PROVIDER[0].size(); i++) {
%>
			<option
				value="<%=((Properties)VEC_PROVIDER[0].get(i)).getProperty("providerNo", "")  %>">
			<%= ((Properties)VEC_PROVIDER[0].get(i)).getProperty("firstName", "") + " " +
                          ((Properties)VEC_PROVIDER[0].get(i)).getProperty("lastName", "") %>
			</option>
			<%
                }
%>
		</select> <select name="providerNoResident">
			<option value="">------Resident------</option>
			<%
                for (int i = 0; i < VEC_PROVIDER[1].size(); i++) {
%>
			<option
				value="<%=((Properties)VEC_PROVIDER[1].get(i)).getProperty("providerNo", "")  %>">
			<%= ((Properties)VEC_PROVIDER[1].get(i)).getProperty("firstName", "") + " " +
                          ((Properties)VEC_PROVIDER[1].get(i)).getProperty("lastName", "") %>
			</option>
			<%
                }
%>
		</select> <select name="providerNoNP">
			<option value="">------Nurse------</option>
			<%
                for (int i = 0; i < VEC_PROVIDER[2].size(); i++) {
%>
			<option
				value="<%=((Properties)VEC_PROVIDER[2].get(i)).getProperty("providerNo", "")  %>">
			<%= ((Properties)VEC_PROVIDER[2].get(i)).getProperty("firstName", "") + " " +
                          ((Properties)VEC_PROVIDER[2].get(i)).getProperty("lastName", "") %>
			</option>
			<%
                }
%>
		</select> <select name="providerNoSW">
			<option value="">------Social Worker------</option>
			<%
                for (int i = 0; i < VEC_PROVIDER[3].size(); i++) {
%>
			<option
				value="<%=((Properties)VEC_PROVIDER[3].get(i)).getProperty("providerNo", "")  %>">
			<%= ((Properties)VEC_PROVIDER[3].get(i)).getProperty("firstName", "") + " " +
                          ((Properties)VEC_PROVIDER[3].get(i)).getProperty("lastName", "") %>
			</option>
			<%
                }
%>
		</select></td>
		<td><input type="submit" name="submit" value="Go"></td>
	</tr>
</table>
</form>
<%
	out.flush();
      if (request.getParameter("submit") != null && providerNo!=null) {
      	// set dx/serviceCode mode
      	boolean bDx = true;
      	if ("DxCode".equals(request.getParameter("codeType"))) {
      		bDx = true;
      	} else {
      		bDx = false;
      	}

        // get dx code list
        Vector     vServiceCode = new Vector();
        Vector     vServiceDesc = new Vector();
        Properties props        = new Properties();

        // dx category
        Properties propCatCode        = new Properties();
        int indexNum = 0;
      Vector     vec   = new Vector();
      sql = "select * from dxphcpgroup order by dxcode, level1, level2 ";
      rs    = dbObj.queryResults(sql);
      while (rs.next()) {
        prop = new Properties();
        prop.setProperty("dxcode", "" + rs.getInt("dxcode"));
        prop.setProperty("level1", Misc.getString(rs,"level1"));
        prop.setProperty("level2", Misc.getString(rs,"level2"));
        vec.add(prop);
        propCatCode.setProperty("" + rs.getInt("dxcode"), ""+indexNum);
        indexNum++;
      }

if(bDx) {
        sql =
                "select distinct(bd.diagnostic_code), dt.description from billingdetail bd, diagnosticcode dt where bd.status!='D' and bd.diagnostic_code = dt.diagnostic_code and bd.appointment_date>='"
                 + startDate + "' and bd.appointment_date<='" + endDate + "' order by diagnostic_code";
        rs = dbObj.queryResults(sql);
        while (rs.next()) {
          vServiceCode.add(Misc.getString(rs,"bd.diagnostic_code"));
          vServiceDesc.add(Misc.getString(rs,"dt.description"));
        }
} else {
	// get service code list
	sql = "select distinct(service_code), service_desc from billingdetail bd where bd.status!='D' and bd.appointment_date>='" + startDate + "' and bd.appointment_date<='" + endDate + "' order by service_code";
    rs = dbObj.queryResults(sql);
	while (rs.next()) {
		vServiceCode.add(Misc.getString(rs,"service_code"));
		vServiceDesc.add(Misc.getString(rs,"service_desc"));
	}
}

        for (int i = 0; i < vServiceCode.size(); i++) {
          // get total pat
if(bDx) {
          sql =
                  "select count(distinct(b.demographic_no)) from billing b, billingdetail bd where b.billing_no=bd.billing_no and bd.diagnostic_code='"
                   + vServiceCode.get(i) + "' and b.creator='" + providerNo + "' and b.billing_date>='" + startDate +
                  "' and b.billing_date<='" + endDate + "' and b.status!='D' and bd.status!='D'";
} else {
	sql = "select count(distinct(b.demographic_no)) from billing b, billingdetail bd where b.billing_no=bd.billing_no  and b.billing_date>='"
	 + startDate + "' and b.billing_date<='" + endDate + "' and b.creator='" + providerNo + "' and b.status!='D' and bd.status!='D' and bd.service_code='" + vServiceCode.get(i) + "' and bd.service_desc='" + vServiceDesc.get(i) + "'";
}
          rs = dbObj.queryResults(sql);
          while (rs.next()) {
            props.setProperty(vServiceCode.get(i) + "pat" + vServiceDesc.get(i), Misc.getString(rs,
                    "count(distinct(b.demographic_no))"));
          }


          // get total vis
if(bDx) {
          sql =
                  "select count(distinct(b.billing_no)) from billing b, billingdetail bd  where b.billing_no=bd.billing_no and bd.diagnostic_code='"
                   + vServiceCode.get(i) + "' and b.creator='" + providerNo + "' and b.billing_date>='" + startDate +
                  "' and b.billing_date<='" + endDate + "' and b.status!='D' and bd.status!='D'";
} else {
	sql = "select count(distinct(b.billing_no)) from billing b, billingdetail bd where b.billing_no=bd.billing_no  and b.billing_date>='"
	 + startDate + "' and b.billing_date<='" + endDate + "' and b.creator='" + providerNo  + "' and b.status!='D' and bd.status!='D' and bd.service_code='" + vServiceCode.get(i) + "' and bd.service_desc='" + vServiceDesc.get(i) + "'";
}
          rs = dbObj.queryResults(sql);
          while (rs.next()) {
            props.setProperty(vServiceCode.get(i) + "vis" + vServiceDesc.get(i), Misc.getString(rs,"count(distinct(b.billing_no))"
                    ));
          }

          // get sex f
if(bDx) {
          sql =
                  "select count(distinct(b.demographic_no)) from billing b, billingdetail bd, demographic d  where b.billing_no=bd.billing_no and b.demographic_no=d.demographic_no and bd.diagnostic_code='"
                   + vServiceCode.get(i) + "' and b.creator='" + providerNo + "' and b.billing_date>='" + startDate +
                  "' and b.billing_date<='" + endDate + "' and b.status!='D' and bd.status!='D'" + " and d.sex='F'";
} else {
	sql = "select count(distinct(b.demographic_no)) from billing b, billingdetail bd, demographic d  where b.billing_no=bd.billing_no and b.demographic_no=d.demographic_no  and b.billing_date>='"
	 + startDate + "' and b.billing_date<='" + endDate + "' and b.creator='" + providerNo + "' and b.status!='D' and bd.status!='D' and bd.service_code='" + vServiceCode.get(i) + "' and bd.service_desc='" + vServiceDesc.get(i) + "'" + " and d.sex='F'";
}
          rs = dbObj.queryResults(sql);
          while (rs.next()) {
            props.setProperty(vServiceCode.get(i) + "patSexF" + vServiceDesc.get(i), Misc.getString(rs,
                    "count(distinct(b.demographic_no))"));
          }

if(bDx) {
          sql =
                  "select count(distinct(b.demographic_no)) from billing b, billingdetail bd, demographic d  where b.billing_no=bd.billing_no and b.demographic_no=d.demographic_no and bd.diagnostic_code='"
                   + vServiceCode.get(i) + "' and b.creator='" + providerNo + "' and b.billing_date>='" + startDate +
                  "' and b.billing_date<='" + endDate + "' and b.status!='D' and bd.status!='D'" + " and d.sex='M'";
} else {
	sql = "select count(distinct(b.demographic_no)) from billing b, billingdetail bd, demographic d  where b.billing_no=bd.billing_no and b.demographic_no=d.demographic_no  and b.billing_date>='"
	 + startDate + "' and b.billing_date<='" + endDate + "' and b.creator='" + providerNo + "' and b.status!='D' and bd.status!='D' and bd.service_code='" + vServiceCode.get(i) + "' and bd.service_desc='" + vServiceDesc.get(i) + "'" + " and d.sex='M'";
}
          rs = dbObj.queryResults(sql);
          while (rs.next()) {
            props.setProperty(vServiceCode.get(i) + "patSexM" + vServiceDesc.get(i), Misc.getString(rs,
                    "count(distinct(b.demographic_no))"));
          }

          // get visit sex m
if(bDx) {
          sql =
                  "select count(distinct(b.billing_no)) from billing b, billingdetail bd, demographic d  where b.billing_no=bd.billing_no and b.demographic_no=d.demographic_no and bd.diagnostic_code='"
                   + vServiceCode.get(i) + "' and b.creator='" + providerNo + "' and b.billing_date>='" + startDate +
                  "' and b.billing_date<='" + endDate + "' and b.status!='D' and bd.status!='D'" + " and d.sex='F'";
} else {
	sql = "select count(distinct(b.billing_no)) from billing b, billingdetail bd, demographic d where b.billing_no=bd.billing_no and b.demographic_no=d.demographic_no  and b.billing_date>='"
	 + startDate + "' and b.billing_date<='" + endDate + "' and b.creator='" + providerNo  + "' and b.status!='D' and bd.status!='D' and bd.service_code='" + vServiceCode.get(i) + "' and bd.service_desc='" + vServiceDesc.get(i) + "'" + " and d.sex='F'";
}
          rs = dbObj.queryResults(sql);
          while (rs.next()) {
            props.setProperty(vServiceCode.get(i) + "visSexF" + vServiceDesc.get(i), Misc.getString(rs,
                    "count(distinct(b.billing_no))"));
          }

if(bDx) {
          sql =
                  "select count(distinct(b.billing_no)) from billing b, billingdetail bd, demographic d  where b.billing_no=bd.billing_no and b.demographic_no=d.demographic_no and bd.diagnostic_code='"
                   + vServiceCode.get(i) + "' and b.creator='" + providerNo + "' and b.billing_date>='" + startDate +
                  "' and b.billing_date<='" + endDate + "' and b.status!='D' and bd.status!='D'" + " and d.sex='M'";
} else {
	sql = "select count(distinct(b.billing_no)) from billing b, billingdetail bd, demographic d where b.billing_no=bd.billing_no and b.demographic_no=d.demographic_no  and b.billing_date>='"
	 + startDate + "' and b.billing_date<='" + endDate + "' and b.creator='" + providerNo  + "' and b.status!='D' and bd.status!='D' and bd.service_code='" + vServiceCode.get(i) + "' and bd.service_desc='" + vServiceDesc.get(i) + "'" + " and d.sex='M'";
}
          rs = dbObj.queryResults(sql);
          while (rs.next()) {
            props.setProperty(vServiceCode.get(i) + "visSexM" + vServiceDesc.get(i), Misc.getString(rs,
                    "count(distinct(b.billing_no))"));
          }

          // get age 0-1
if(bDx) {
          sql =
                  "select count(distinct(b.demographic_no)) from billing b, billingdetail bd, demographic d  where b.billing_no=bd.billing_no and b.demographic_no=d.demographic_no and bd.diagnostic_code='"
                   + vServiceCode.get(i) + "' and b.creator='" + providerNo + "' and b.billing_date>='" + startDate +
                  "' and b.billing_date<='" + endDate + "' and b.status!='D' and bd.status!='D'" +
                  " and (YEAR(CURRENT_DATE)-YEAR(DATE_FORMAT(CONCAT((d.year_of_birth),'-',(d.month_of_birth),'-',(d.date_of_birth)),'%Y-%m-%d'))) - (RIGHT(CURRENT_DATE,5)<RIGHT(DATE_FORMAT(CONCAT((d.year_of_birth), '-', (d.month_of_birth), '-', (d.date_of_birth)), '%Y-%m-%d'),5)) <=1 "
                  ;
} else {
	sql = "select count(distinct(b.demographic_no)) from billing b, billingdetail bd, demographic d where b.billing_no=bd.billing_no and b.demographic_no=d.demographic_no  and b.billing_date>='"
	 + startDate + "' and b.billing_date<='" + endDate + "' and b.creator='" + providerNo + "' and b.status!='D' and bd.status!='D' and bd.service_code='" + vServiceCode.get(i) + "' and bd.service_desc='" + vServiceDesc.get(i) + "' "
     + " and (YEAR(CURRENT_DATE)-YEAR(DATE_FORMAT(CONCAT((d.year_of_birth),'-',(d.month_of_birth),'-',(d.date_of_birth)),'%Y-%m-%d'))) - (RIGHT(CURRENT_DATE,5)<RIGHT(DATE_FORMAT(CONCAT((d.year_of_birth), '-', (d.month_of_birth), '-', (d.date_of_birth)), '%Y-%m-%d'),5)) <=1 "
	 ;
}
          rs = dbObj.queryResults(sql);

          while (rs.next()) {
            props.setProperty(vServiceCode.get(i) + "pat0_1" + vServiceDesc.get(i), Misc.getString(rs,
                    "count(distinct(b.demographic_no))"));
          }

if(bDx) {
          sql =
                  "select count(distinct(b.billing_no)) from billing b, billingdetail bd, demographic d  where b.billing_no=bd.billing_no and b.demographic_no=d.demographic_no and bd.diagnostic_code='"
                   + vServiceCode.get(i) + "' and b.creator='" + providerNo + "' and b.billing_date>='" + startDate +
                  "' and b.billing_date<='" + endDate + "' and b.status!='D' and bd.status!='D'" +
                  " and (YEAR(CURRENT_DATE)-YEAR(DATE_FORMAT(CONCAT((d.year_of_birth),'-',(d.month_of_birth),'-',(d.date_of_birth)),'%Y-%m-%d'))) - (RIGHT(CURRENT_DATE,5)<RIGHT(DATE_FORMAT(CONCAT((d.year_of_birth), '-', (d.month_of_birth), '-', (d.date_of_birth)), '%Y-%m-%d'),5)) <=1 "
                  ;
} else {
	sql = "select count(distinct(b.billing_no)) from billing b, billingdetail bd, demographic d where b.billing_no=bd.billing_no and b.demographic_no=d.demographic_no  and b.billing_date>='"
	 + startDate + "' and b.billing_date<='" + endDate + "' and b.creator='" + providerNo  + "' and b.status!='D' and bd.status!='D' and bd.service_code='" + vServiceCode.get(i) + "' and bd.service_desc='" + vServiceDesc.get(i) + "'"
     + " and (YEAR(CURRENT_DATE)-YEAR(DATE_FORMAT(CONCAT((d.year_of_birth),'-',(d.month_of_birth),'-',(d.date_of_birth)),'%Y-%m-%d'))) - (RIGHT(CURRENT_DATE,5)<RIGHT(DATE_FORMAT(CONCAT((d.year_of_birth), '-', (d.month_of_birth), '-', (d.date_of_birth)), '%Y-%m-%d'),5)) <=1 "
	 ;
}
          rs = dbObj.queryResults(sql);

          while (rs.next()) {
            props.setProperty(vServiceCode.get(i) + "vis0_1" + vServiceDesc.get(i), Misc.getString(rs,
                    "count(distinct(b.billing_no))"));
          }

          // get age 2-11
if(bDx) {
          sql =
                  "select count(distinct(b.demographic_no)) from billing b, billingdetail bd, demographic d  where b.billing_no=bd.billing_no and b.demographic_no=d.demographic_no and bd.diagnostic_code='"
                   + vServiceCode.get(i) + "' and b.creator='" + providerNo + "' and b.billing_date>='" + startDate +
                  "' and b.billing_date<='" + endDate + "' and b.status!='D' and bd.status!='D'" +
                  " and (YEAR(CURRENT_DATE)-YEAR(DATE_FORMAT(CONCAT((d.year_of_birth),'-',(d.month_of_birth),'-',(d.date_of_birth)),'%Y-%m-%d'))) - (RIGHT(CURRENT_DATE,5)<RIGHT(DATE_FORMAT(CONCAT((d.year_of_birth), '-', (d.month_of_birth), '-', (d.date_of_birth)), '%Y-%m-%d'),5)) <=11 "
                   +
                  " and (YEAR(CURRENT_DATE)-YEAR(DATE_FORMAT(CONCAT((d.year_of_birth), '-', (d.month_of_birth), '-', (d.date_of_birth)), '%Y-%m-%d'))) - (RIGHT(CURRENT_DATE,5)<RIGHT(DATE_FORMAT(CONCAT((d.year_of_birth), '-', (d.month_of_birth), '-', (d.date_of_birth)), '%Y-%m-%d'),5)) >=2 "
                  ;
} else {
	sql = "select count(distinct(b.demographic_no)) from billing b, billingdetail bd, demographic d where b.billing_no=bd.billing_no and b.demographic_no=d.demographic_no  and b.billing_date>='"
	 + startDate + "' and b.billing_date<='" + endDate + "' and b.creator='" + providerNo + "' and b.status!='D' and bd.status!='D' and bd.service_code='" + vServiceCode.get(i) + "' and bd.service_desc='" + vServiceDesc.get(i) + "' " +
                  " and (YEAR(CURRENT_DATE)-YEAR(DATE_FORMAT(CONCAT((d.year_of_birth),'-',(d.month_of_birth),'-',(d.date_of_birth)),'%Y-%m-%d'))) - (RIGHT(CURRENT_DATE,5)<RIGHT(DATE_FORMAT(CONCAT((d.year_of_birth), '-', (d.month_of_birth), '-', (d.date_of_birth)), '%Y-%m-%d'),5)) <=11 "
                   +
                  " and (YEAR(CURRENT_DATE)-YEAR(DATE_FORMAT(CONCAT((d.year_of_birth), '-', (d.month_of_birth), '-', (d.date_of_birth)), '%Y-%m-%d'))) - (RIGHT(CURRENT_DATE,5)<RIGHT(DATE_FORMAT(CONCAT((d.year_of_birth), '-', (d.month_of_birth), '-', (d.date_of_birth)), '%Y-%m-%d'),5)) >=2 "
	 ;
}
          rs = dbObj.queryResults(sql);
          while (rs.next()) {
            props.setProperty(vServiceCode.get(i) + "pat2_11" + vServiceDesc.get(i), Misc.getString(rs,
                    "count(distinct(b.demographic_no))"));
          }

if(bDx) {
          sql =
                  "select count(distinct(b.billing_no)) from billing b, billingdetail bd, demographic d  where b.billing_no=bd.billing_no and b.demographic_no=d.demographic_no and bd.diagnostic_code='"
                   + vServiceCode.get(i) + "' and b.creator='" + providerNo + "' and b.billing_date>='" + startDate +
                  "' and b.billing_date<='" + endDate + "' and b.status!='D' and bd.status!='D'" +
                  " and (YEAR(CURRENT_DATE)-YEAR(DATE_FORMAT(CONCAT((d.year_of_birth),'-',(d.month_of_birth),'-',(d.date_of_birth)),'%Y-%m-%d'))) - (RIGHT(CURRENT_DATE,5)<RIGHT(DATE_FORMAT(CONCAT((d.year_of_birth), '-', (d.month_of_birth), '-', (d.date_of_birth)), '%Y-%m-%d'),5)) <=11 "
                   +
                  " and (YEAR(CURRENT_DATE)-YEAR(DATE_FORMAT(CONCAT((d.year_of_birth), '-', (d.month_of_birth), '-', (d.date_of_birth)), '%Y-%m-%d'))) - (RIGHT(CURRENT_DATE,5)<RIGHT(DATE_FORMAT(CONCAT((d.year_of_birth), '-', (d.month_of_birth), '-', (d.date_of_birth)), '%Y-%m-%d'),5)) >=2 "
                  ;
} else {
	sql = "select count(distinct(b.billing_no)) from billing b, billingdetail bd, demographic d where b.billing_no=bd.billing_no and b.demographic_no=d.demographic_no  and b.billing_date>='"
	 + startDate + "' and b.billing_date<='" + endDate + "' and b.creator='" + providerNo  + "' and b.status!='D' and bd.status!='D' and bd.service_code='" + vServiceCode.get(i) + "' and bd.service_desc='" + vServiceDesc.get(i) + "'" +
                  " and (YEAR(CURRENT_DATE)-YEAR(DATE_FORMAT(CONCAT((d.year_of_birth),'-',(d.month_of_birth),'-',(d.date_of_birth)),'%Y-%m-%d'))) - (RIGHT(CURRENT_DATE,5)<RIGHT(DATE_FORMAT(CONCAT((d.year_of_birth), '-', (d.month_of_birth), '-', (d.date_of_birth)), '%Y-%m-%d'),5)) <=11 "
                   +
                  " and (YEAR(CURRENT_DATE)-YEAR(DATE_FORMAT(CONCAT((d.year_of_birth), '-', (d.month_of_birth), '-', (d.date_of_birth)), '%Y-%m-%d'))) - (RIGHT(CURRENT_DATE,5)<RIGHT(DATE_FORMAT(CONCAT((d.year_of_birth), '-', (d.month_of_birth), '-', (d.date_of_birth)), '%Y-%m-%d'),5)) >=2 "
	 ;
}
          rs = dbObj.queryResults(sql);
          while (rs.next()) {
            props.setProperty(vServiceCode.get(i) + "vis2_11" + vServiceDesc.get(i), Misc.getString(rs,
                    "count(distinct(b.billing_no))"));
          }

          // get age 12-20
if(bDx) {
          sql =
                  "select count(distinct(b.demographic_no)) from billing b, billingdetail bd, demographic d  where b.billing_no=bd.billing_no and b.demographic_no=d.demographic_no and bd.diagnostic_code='"
                   + vServiceCode.get(i) + "' and b.creator='" + providerNo + "' and b.billing_date>='" + startDate +
                  "' and b.billing_date<='" + endDate + "' and b.status!='D' and bd.status!='D'" +
                  " and (YEAR(CURRENT_DATE)-YEAR(DATE_FORMAT(CONCAT((d.year_of_birth),'-',(d.month_of_birth),'-',(d.date_of_birth)),'%Y-%m-%d'))) - (RIGHT(CURRENT_DATE,5)<RIGHT(DATE_FORMAT(CONCAT((d.year_of_birth), '-', (d.month_of_birth), '-', (d.date_of_birth)), '%Y-%m-%d'),5)) <=20 "
                   +
                  " and (YEAR(CURRENT_DATE)-YEAR(DATE_FORMAT(CONCAT((d.year_of_birth), '-', (d.month_of_birth), '-', (d.date_of_birth)), '%Y-%m-%d'))) - (RIGHT(CURRENT_DATE,5)<RIGHT(DATE_FORMAT(CONCAT((d.year_of_birth), '-', (d.month_of_birth), '-', (d.date_of_birth)), '%Y-%m-%d'),5)) >=12 "
                  ;
} else {
	sql = "select count(distinct(b.demographic_no)) from billing b, billingdetail bd, demographic d where b.billing_no=bd.billing_no and b.demographic_no=d.demographic_no  and b.billing_date>='"
	 + startDate + "' and b.billing_date<='" + endDate + "' and b.creator='" + providerNo + "' and b.status!='D' and bd.status!='D' and bd.service_code='" + vServiceCode.get(i) + "' and bd.service_desc='" + vServiceDesc.get(i) + "' " +
                  " and (YEAR(CURRENT_DATE)-YEAR(DATE_FORMAT(CONCAT((d.year_of_birth),'-',(d.month_of_birth),'-',(d.date_of_birth)),'%Y-%m-%d'))) - (RIGHT(CURRENT_DATE,5)<RIGHT(DATE_FORMAT(CONCAT((d.year_of_birth), '-', (d.month_of_birth), '-', (d.date_of_birth)), '%Y-%m-%d'),5)) <=20 "
                   +
                  " and (YEAR(CURRENT_DATE)-YEAR(DATE_FORMAT(CONCAT((d.year_of_birth), '-', (d.month_of_birth), '-', (d.date_of_birth)), '%Y-%m-%d'))) - (RIGHT(CURRENT_DATE,5)<RIGHT(DATE_FORMAT(CONCAT((d.year_of_birth), '-', (d.month_of_birth), '-', (d.date_of_birth)), '%Y-%m-%d'),5)) >=12 "
	 ;
}
          rs = dbObj.queryResults(sql);
          while (rs.next()) {
            props.setProperty(vServiceCode.get(i) + "pat12_20" + vServiceDesc.get(i), Misc.getString(rs,
                    "count(distinct(b.demographic_no))"));
          }

if(bDx) {
          sql =
                  "select count(distinct(b.billing_no)) from billing b, billingdetail bd, demographic d  where b.billing_no=bd.billing_no and b.demographic_no=d.demographic_no and bd.diagnostic_code='"
                   + vServiceCode.get(i) + "' and b.creator='" + providerNo + "' and b.billing_date>='" + startDate +
                  "' and b.billing_date<='" + endDate + "' and b.status!='D' and bd.status!='D'" +
                  " and (YEAR(CURRENT_DATE)-YEAR(DATE_FORMAT(CONCAT((d.year_of_birth),'-',(d.month_of_birth),'-',(d.date_of_birth)),'%Y-%m-%d'))) - (RIGHT(CURRENT_DATE,5)<RIGHT(DATE_FORMAT(CONCAT((d.year_of_birth), '-', (d.month_of_birth), '-', (d.date_of_birth)), '%Y-%m-%d'),5)) <=20 "
                   +
                  " and (YEAR(CURRENT_DATE)-YEAR(DATE_FORMAT(CONCAT((d.year_of_birth), '-', (d.month_of_birth), '-', (d.date_of_birth)), '%Y-%m-%d'))) - (RIGHT(CURRENT_DATE,5)<RIGHT(DATE_FORMAT(CONCAT((d.year_of_birth), '-', (d.month_of_birth), '-', (d.date_of_birth)), '%Y-%m-%d'),5)) >=12 "
                  ;
} else {
	sql = "select count(distinct(b.billing_no)) from billing b, billingdetail bd, demographic d where b.billing_no=bd.billing_no and b.demographic_no=d.demographic_no  and b.billing_date>='"
	 + startDate + "' and b.billing_date<='" + endDate + "' and b.creator='" + providerNo  + "' and b.status!='D' and bd.status!='D' and bd.service_code='" + vServiceCode.get(i) + "' and bd.service_desc='" + vServiceDesc.get(i) + "'" +
                  " and (YEAR(CURRENT_DATE)-YEAR(DATE_FORMAT(CONCAT((d.year_of_birth),'-',(d.month_of_birth),'-',(d.date_of_birth)),'%Y-%m-%d'))) - (RIGHT(CURRENT_DATE,5)<RIGHT(DATE_FORMAT(CONCAT((d.year_of_birth), '-', (d.month_of_birth), '-', (d.date_of_birth)), '%Y-%m-%d'),5)) <=20 "
                   +
                  " and (YEAR(CURRENT_DATE)-YEAR(DATE_FORMAT(CONCAT((d.year_of_birth), '-', (d.month_of_birth), '-', (d.date_of_birth)), '%Y-%m-%d'))) - (RIGHT(CURRENT_DATE,5)<RIGHT(DATE_FORMAT(CONCAT((d.year_of_birth), '-', (d.month_of_birth), '-', (d.date_of_birth)), '%Y-%m-%d'),5)) >=12 "
	 ;
}
          rs = dbObj.queryResults(sql);
          while (rs.next()) {
            props.setProperty(vServiceCode.get(i) + "vis12_20" + vServiceDesc.get(i), Misc.getString(rs,
                    "count(distinct(b.billing_no))"));
          }

          // get age 21-34
if(bDx) {
          sql =
                  "select count(distinct(b.demographic_no)) from billing b, billingdetail bd, demographic d  where b.billing_no=bd.billing_no and b.demographic_no=d.demographic_no and bd.diagnostic_code='"
                   + vServiceCode.get(i) + "' and b.creator='" + providerNo + "' and b.billing_date>='" + startDate +
                  "' and b.billing_date<='" + endDate + "' and b.status!='D' and bd.status!='D'" +
                  " and (YEAR(CURRENT_DATE)-YEAR(DATE_FORMAT(CONCAT((d.year_of_birth),'-',(d.month_of_birth),'-',(d.date_of_birth)),'%Y-%m-%d'))) - (RIGHT(CURRENT_DATE,5)<RIGHT(DATE_FORMAT(CONCAT((d.year_of_birth), '-', (d.month_of_birth), '-', (d.date_of_birth)), '%Y-%m-%d'),5)) <=34 "
                   +
                  " and (YEAR(CURRENT_DATE)-YEAR(DATE_FORMAT(CONCAT((d.year_of_birth), '-', (d.month_of_birth), '-', (d.date_of_birth)), '%Y-%m-%d'))) - (RIGHT(CURRENT_DATE,5)<RIGHT(DATE_FORMAT(CONCAT((d.year_of_birth), '-', (d.month_of_birth), '-', (d.date_of_birth)), '%Y-%m-%d'),5)) >=21 "
                  ;
} else {
	sql = "select count(distinct(b.demographic_no)) from billing b, billingdetail bd, demographic d where b.billing_no=bd.billing_no and b.demographic_no=d.demographic_no  and b.billing_date>='"
	 + startDate + "' and b.billing_date<='" + endDate + "' and b.creator='" + providerNo + "' and b.status!='D' and bd.status!='D' and bd.service_code='" + vServiceCode.get(i) + "' and bd.service_desc='" + vServiceDesc.get(i) + "' " +
                  " and (YEAR(CURRENT_DATE)-YEAR(DATE_FORMAT(CONCAT((d.year_of_birth),'-',(d.month_of_birth),'-',(d.date_of_birth)),'%Y-%m-%d'))) - (RIGHT(CURRENT_DATE,5)<RIGHT(DATE_FORMAT(CONCAT((d.year_of_birth), '-', (d.month_of_birth), '-', (d.date_of_birth)), '%Y-%m-%d'),5)) <=34 "
                   +
                  " and (YEAR(CURRENT_DATE)-YEAR(DATE_FORMAT(CONCAT((d.year_of_birth), '-', (d.month_of_birth), '-', (d.date_of_birth)), '%Y-%m-%d'))) - (RIGHT(CURRENT_DATE,5)<RIGHT(DATE_FORMAT(CONCAT((d.year_of_birth), '-', (d.month_of_birth), '-', (d.date_of_birth)), '%Y-%m-%d'),5)) >=21 "
	 ;
}
          rs = dbObj.queryResults(sql);
          while (rs.next()) {
            props.setProperty(vServiceCode.get(i) + "pat21_34" + vServiceDesc.get(i), Misc.getString(rs,
                    "count(distinct(b.demographic_no))"));
          }

if(bDx) {
          sql =
                  "select count(distinct(b.billing_no)) from billing b, billingdetail bd, demographic d  where b.billing_no=bd.billing_no and b.demographic_no=d.demographic_no and bd.diagnostic_code='"
                   + vServiceCode.get(i) + "' and b.creator='" + providerNo + "' and b.billing_date>='" + startDate +
                  "' and b.billing_date<='" + endDate + "' and b.status!='D' and bd.status!='D'" +
                  " and (YEAR(CURRENT_DATE)-YEAR(DATE_FORMAT(CONCAT((d.year_of_birth),'-',(d.month_of_birth),'-',(d.date_of_birth)),'%Y-%m-%d'))) - (RIGHT(CURRENT_DATE,5)<RIGHT(DATE_FORMAT(CONCAT((d.year_of_birth), '-', (d.month_of_birth), '-', (d.date_of_birth)), '%Y-%m-%d'),5)) <=34 "
                   +
                  " and (YEAR(CURRENT_DATE)-YEAR(DATE_FORMAT(CONCAT((d.year_of_birth), '-', (d.month_of_birth), '-', (d.date_of_birth)), '%Y-%m-%d'))) - (RIGHT(CURRENT_DATE,5)<RIGHT(DATE_FORMAT(CONCAT((d.year_of_birth), '-', (d.month_of_birth), '-', (d.date_of_birth)), '%Y-%m-%d'),5)) >=21 "
                  ;
} else {
	sql = "select count(distinct(b.billing_no)) from billing b, billingdetail bd, demographic d where b.billing_no=bd.billing_no and b.demographic_no=d.demographic_no  and b.billing_date>='"
	 + startDate + "' and b.billing_date<='" + endDate + "' and b.creator='" + providerNo  + "' and b.status!='D' and bd.status!='D' and bd.service_code='" + vServiceCode.get(i) + "' and bd.service_desc='" + vServiceDesc.get(i) + "'" +
                  " and (YEAR(CURRENT_DATE)-YEAR(DATE_FORMAT(CONCAT((d.year_of_birth),'-',(d.month_of_birth),'-',(d.date_of_birth)),'%Y-%m-%d'))) - (RIGHT(CURRENT_DATE,5)<RIGHT(DATE_FORMAT(CONCAT((d.year_of_birth), '-', (d.month_of_birth), '-', (d.date_of_birth)), '%Y-%m-%d'),5)) <=34 "
                   +
                  " and (YEAR(CURRENT_DATE)-YEAR(DATE_FORMAT(CONCAT((d.year_of_birth), '-', (d.month_of_birth), '-', (d.date_of_birth)), '%Y-%m-%d'))) - (RIGHT(CURRENT_DATE,5)<RIGHT(DATE_FORMAT(CONCAT((d.year_of_birth), '-', (d.month_of_birth), '-', (d.date_of_birth)), '%Y-%m-%d'),5)) >=21 "
	 ;
}
          rs = dbObj.queryResults(sql);
          while (rs.next()) {
            props.setProperty(vServiceCode.get(i) + "vis21_34" + vServiceDesc.get(i), Misc.getString(rs,
                    "count(distinct(b.billing_no))"));
          }

          // get age 35-50
if(bDx) {
          sql =
                  "select count(distinct(b.demographic_no)) from billing b, billingdetail bd, demographic d  where b.billing_no=bd.billing_no and b.demographic_no=d.demographic_no and bd.diagnostic_code='"
                   + vServiceCode.get(i) + "' and b.creator='" + providerNo + "' and b.billing_date>='" + startDate +
                  "' and b.billing_date<='" + endDate + "' and b.status!='D' and bd.status!='D'" +
                  " and (YEAR(CURRENT_DATE)-YEAR(DATE_FORMAT(CONCAT((d.year_of_birth),'-',(d.month_of_birth),'-',(d.date_of_birth)),'%Y-%m-%d'))) - (RIGHT(CURRENT_DATE,5)<RIGHT(DATE_FORMAT(CONCAT((d.year_of_birth), '-', (d.month_of_birth), '-', (d.date_of_birth)), '%Y-%m-%d'),5)) <=50 "
                   +
                  " and (YEAR(CURRENT_DATE)-YEAR(DATE_FORMAT(CONCAT((d.year_of_birth), '-', (d.month_of_birth), '-', (d.date_of_birth)), '%Y-%m-%d'))) - (RIGHT(CURRENT_DATE,5)<RIGHT(DATE_FORMAT(CONCAT((d.year_of_birth), '-', (d.month_of_birth), '-', (d.date_of_birth)), '%Y-%m-%d'),5)) >=35 "
                  ;
} else {
	sql = "select count(distinct(b.demographic_no)) from billing b, billingdetail bd, demographic d where b.billing_no=bd.billing_no and b.demographic_no=d.demographic_no  and b.billing_date>='"
	 + startDate + "' and b.billing_date<='" + endDate + "' and b.creator='" + providerNo + "' and b.status!='D' and bd.status!='D' and bd.service_code='" + vServiceCode.get(i) + "' and bd.service_desc='" + vServiceDesc.get(i) + "' " +
                  " and (YEAR(CURRENT_DATE)-YEAR(DATE_FORMAT(CONCAT((d.year_of_birth),'-',(d.month_of_birth),'-',(d.date_of_birth)),'%Y-%m-%d'))) - (RIGHT(CURRENT_DATE,5)<RIGHT(DATE_FORMAT(CONCAT((d.year_of_birth), '-', (d.month_of_birth), '-', (d.date_of_birth)), '%Y-%m-%d'),5)) <=50 "
                   +
                  " and (YEAR(CURRENT_DATE)-YEAR(DATE_FORMAT(CONCAT((d.year_of_birth), '-', (d.month_of_birth), '-', (d.date_of_birth)), '%Y-%m-%d'))) - (RIGHT(CURRENT_DATE,5)<RIGHT(DATE_FORMAT(CONCAT((d.year_of_birth), '-', (d.month_of_birth), '-', (d.date_of_birth)), '%Y-%m-%d'),5)) >=35 "
	 ;
}
          rs = dbObj.queryResults(sql);
          while (rs.next()) {
            props.setProperty(vServiceCode.get(i) + "pat35_50" + vServiceDesc.get(i), Misc.getString(rs,
                    "count(distinct(b.demographic_no))"));
          }

if(bDx) {
          sql =
                  "select count(distinct(b.billing_no)) from billing b, billingdetail bd, demographic d  where b.billing_no=bd.billing_no and b.demographic_no=d.demographic_no and bd.diagnostic_code='"
                   + vServiceCode.get(i) + "' and b.creator='" + providerNo + "' and b.billing_date>='" + startDate +
                  "' and b.billing_date<='" + endDate + "' and b.status!='D' and bd.status!='D'" +
                  " and (YEAR(CURRENT_DATE)-YEAR(DATE_FORMAT(CONCAT((d.year_of_birth),'-',(d.month_of_birth),'-',(d.date_of_birth)),'%Y-%m-%d'))) - (RIGHT(CURRENT_DATE,5)<RIGHT(DATE_FORMAT(CONCAT((d.year_of_birth), '-', (d.month_of_birth), '-', (d.date_of_birth)), '%Y-%m-%d'),5)) <=50 "
                   +
                  " and (YEAR(CURRENT_DATE)-YEAR(DATE_FORMAT(CONCAT((d.year_of_birth), '-', (d.month_of_birth), '-', (d.date_of_birth)), '%Y-%m-%d'))) - (RIGHT(CURRENT_DATE,5)<RIGHT(DATE_FORMAT(CONCAT((d.year_of_birth), '-', (d.month_of_birth), '-', (d.date_of_birth)), '%Y-%m-%d'),5)) >=35 "
                  ;
} else {
	sql = "select count(distinct(b.billing_no)) from billing b, billingdetail bd, demographic d where b.billing_no=bd.billing_no and b.demographic_no=d.demographic_no  and b.billing_date>='"
	 + startDate + "' and b.billing_date<='" + endDate + "' and b.creator='" + providerNo  + "' and b.status!='D' and bd.status!='D' and bd.service_code='" + vServiceCode.get(i) + "' and bd.service_desc='" + vServiceDesc.get(i) + "'" +
                  " and (YEAR(CURRENT_DATE)-YEAR(DATE_FORMAT(CONCAT((d.year_of_birth),'-',(d.month_of_birth),'-',(d.date_of_birth)),'%Y-%m-%d'))) - (RIGHT(CURRENT_DATE,5)<RIGHT(DATE_FORMAT(CONCAT((d.year_of_birth), '-', (d.month_of_birth), '-', (d.date_of_birth)), '%Y-%m-%d'),5)) <=50 "
                   +
                  " and (YEAR(CURRENT_DATE)-YEAR(DATE_FORMAT(CONCAT((d.year_of_birth), '-', (d.month_of_birth), '-', (d.date_of_birth)), '%Y-%m-%d'))) - (RIGHT(CURRENT_DATE,5)<RIGHT(DATE_FORMAT(CONCAT((d.year_of_birth), '-', (d.month_of_birth), '-', (d.date_of_birth)), '%Y-%m-%d'),5)) >=35 "
	 ;
}
          rs = dbObj.queryResults(sql);
          while (rs.next()) {
            props.setProperty(vServiceCode.get(i) + "vis35_50" + vServiceDesc.get(i), Misc.getString(rs,
                    "count(distinct(b.billing_no))"));
          }

          // get age 51-64
if(bDx) {
          sql =
                  "select count(distinct(b.demographic_no)) from billing b, billingdetail bd, demographic d  where b.billing_no=bd.billing_no and b.demographic_no=d.demographic_no and bd.diagnostic_code='"
                   + vServiceCode.get(i) + "' and b.creator='" + providerNo + "' and b.billing_date>='" + startDate +
                  "' and b.billing_date<='" + endDate + "' and b.status!='D' and bd.status!='D'" +
                  " and (YEAR(CURRENT_DATE)-YEAR(DATE_FORMAT(CONCAT((d.year_of_birth),'-',(d.month_of_birth),'-',(d.date_of_birth)),'%Y-%m-%d'))) - (RIGHT(CURRENT_DATE,5)<RIGHT(DATE_FORMAT(CONCAT((d.year_of_birth), '-', (d.month_of_birth), '-', (d.date_of_birth)), '%Y-%m-%d'),5)) <=64 "
                   +
                  " and (YEAR(CURRENT_DATE)-YEAR(DATE_FORMAT(CONCAT((d.year_of_birth), '-', (d.month_of_birth), '-', (d.date_of_birth)), '%Y-%m-%d'))) - (RIGHT(CURRENT_DATE,5)<RIGHT(DATE_FORMAT(CONCAT((d.year_of_birth), '-', (d.month_of_birth), '-', (d.date_of_birth)), '%Y-%m-%d'),5)) >=51 "
                  ;
} else {
	sql = "select count(distinct(b.demographic_no)) from billing b, billingdetail bd, demographic d where b.billing_no=bd.billing_no and b.demographic_no=d.demographic_no  and b.billing_date>='"
	 + startDate + "' and b.billing_date<='" + endDate + "' and b.creator='" + providerNo + "' and b.status!='D' and bd.status!='D' and bd.service_code='" + vServiceCode.get(i) + "' and bd.service_desc='" + vServiceDesc.get(i) + "' " +
                  " and (YEAR(CURRENT_DATE)-YEAR(DATE_FORMAT(CONCAT((d.year_of_birth),'-',(d.month_of_birth),'-',(d.date_of_birth)),'%Y-%m-%d'))) - (RIGHT(CURRENT_DATE,5)<RIGHT(DATE_FORMAT(CONCAT((d.year_of_birth), '-', (d.month_of_birth), '-', (d.date_of_birth)), '%Y-%m-%d'),5)) <=64 "
                   +
                  " and (YEAR(CURRENT_DATE)-YEAR(DATE_FORMAT(CONCAT((d.year_of_birth), '-', (d.month_of_birth), '-', (d.date_of_birth)), '%Y-%m-%d'))) - (RIGHT(CURRENT_DATE,5)<RIGHT(DATE_FORMAT(CONCAT((d.year_of_birth), '-', (d.month_of_birth), '-', (d.date_of_birth)), '%Y-%m-%d'),5)) >=51 "
	 ;
}
          rs = dbObj.queryResults(sql);
          while (rs.next()) {
            props.setProperty(vServiceCode.get(i) + "pat51_64" + vServiceDesc.get(i), Misc.getString(rs,
                    "count(distinct(b.demographic_no))"));
          }

if(bDx) {
          sql =
                  "select count(distinct(b.billing_no)) from billing b, billingdetail bd, demographic d  where b.billing_no=bd.billing_no and b.demographic_no=d.demographic_no and bd.diagnostic_code='"
                   + vServiceCode.get(i) + "' and b.creator='" + providerNo + "' and b.billing_date>='" + startDate +
                  "' and b.billing_date<='" + endDate + "' and b.status!='D' and bd.status!='D'" +
                  " and (YEAR(CURRENT_DATE)-YEAR(DATE_FORMAT(CONCAT((d.year_of_birth),'-',(d.month_of_birth),'-',(d.date_of_birth)),'%Y-%m-%d'))) - (RIGHT(CURRENT_DATE,5)<RIGHT(DATE_FORMAT(CONCAT((d.year_of_birth), '-', (d.month_of_birth), '-', (d.date_of_birth)), '%Y-%m-%d'),5)) <=64 "
                   +
                  " and (YEAR(CURRENT_DATE)-YEAR(DATE_FORMAT(CONCAT((d.year_of_birth), '-', (d.month_of_birth), '-', (d.date_of_birth)), '%Y-%m-%d'))) - (RIGHT(CURRENT_DATE,5)<RIGHT(DATE_FORMAT(CONCAT((d.year_of_birth), '-', (d.month_of_birth), '-', (d.date_of_birth)), '%Y-%m-%d'),5)) >=51 "
                  ;
} else {
	sql = "select count(distinct(b.billing_no)) from billing b, billingdetail bd, demographic d where b.billing_no=bd.billing_no and b.demographic_no=d.demographic_no  and b.billing_date>='"
	 + startDate + "' and b.billing_date<='" + endDate + "' and b.creator='" + providerNo  + "' and b.status!='D' and bd.status!='D' and bd.service_code='" + vServiceCode.get(i) + "' and bd.service_desc='" + vServiceDesc.get(i) + "'" +
                  " and (YEAR(CURRENT_DATE)-YEAR(DATE_FORMAT(CONCAT((d.year_of_birth),'-',(d.month_of_birth),'-',(d.date_of_birth)),'%Y-%m-%d'))) - (RIGHT(CURRENT_DATE,5)<RIGHT(DATE_FORMAT(CONCAT((d.year_of_birth), '-', (d.month_of_birth), '-', (d.date_of_birth)), '%Y-%m-%d'),5)) <=64 "
                   +
                  " and (YEAR(CURRENT_DATE)-YEAR(DATE_FORMAT(CONCAT((d.year_of_birth), '-', (d.month_of_birth), '-', (d.date_of_birth)), '%Y-%m-%d'))) - (RIGHT(CURRENT_DATE,5)<RIGHT(DATE_FORMAT(CONCAT((d.year_of_birth), '-', (d.month_of_birth), '-', (d.date_of_birth)), '%Y-%m-%d'),5)) >=51 "
	 ;
}
          rs = dbObj.queryResults(sql);
          while (rs.next()) {
            props.setProperty(vServiceCode.get(i) + "vis51_64" + vServiceDesc.get(i), Misc.getString(rs,
                    "count(distinct(b.billing_no))"));
          }

          // get age 65-70
if(bDx) {
          sql =
                  "select count(distinct(b.demographic_no)) from billing b, billingdetail bd, demographic d  where b.billing_no=bd.billing_no and b.demographic_no=d.demographic_no and bd.diagnostic_code='"
                   + vServiceCode.get(i) + "' and b.creator='" + providerNo + "' and b.billing_date>='" + startDate +
                  "' and b.billing_date<='" + endDate + "' and b.status!='D' and bd.status!='D'" +
                  " and (YEAR(CURRENT_DATE)-YEAR(DATE_FORMAT(CONCAT((d.year_of_birth),'-',(d.month_of_birth),'-',(d.date_of_birth)),'%Y-%m-%d'))) - (RIGHT(CURRENT_DATE,5)<RIGHT(DATE_FORMAT(CONCAT((d.year_of_birth), '-', (d.month_of_birth), '-', (d.date_of_birth)), '%Y-%m-%d'),5)) <=70 "
                   +
                  " and (YEAR(CURRENT_DATE)-YEAR(DATE_FORMAT(CONCAT((d.year_of_birth), '-', (d.month_of_birth), '-', (d.date_of_birth)), '%Y-%m-%d'))) - (RIGHT(CURRENT_DATE,5)<RIGHT(DATE_FORMAT(CONCAT((d.year_of_birth), '-', (d.month_of_birth), '-', (d.date_of_birth)), '%Y-%m-%d'),5)) >=65 "
                  ;
} else {
	sql = "select count(distinct(b.demographic_no)) from billing b, billingdetail bd, demographic d where b.billing_no=bd.billing_no and b.demographic_no=d.demographic_no  and b.billing_date>='"
	 + startDate + "' and b.billing_date<='" + endDate + "' and b.creator='" + providerNo + "' and b.status!='D' and bd.status!='D' and bd.service_code='" + vServiceCode.get(i) + "' and bd.service_desc='" + vServiceDesc.get(i) + "' " +
                  " and (YEAR(CURRENT_DATE)-YEAR(DATE_FORMAT(CONCAT((d.year_of_birth),'-',(d.month_of_birth),'-',(d.date_of_birth)),'%Y-%m-%d'))) - (RIGHT(CURRENT_DATE,5)<RIGHT(DATE_FORMAT(CONCAT((d.year_of_birth), '-', (d.month_of_birth), '-', (d.date_of_birth)), '%Y-%m-%d'),5)) <=70 "
                   +
                  " and (YEAR(CURRENT_DATE)-YEAR(DATE_FORMAT(CONCAT((d.year_of_birth), '-', (d.month_of_birth), '-', (d.date_of_birth)), '%Y-%m-%d'))) - (RIGHT(CURRENT_DATE,5)<RIGHT(DATE_FORMAT(CONCAT((d.year_of_birth), '-', (d.month_of_birth), '-', (d.date_of_birth)), '%Y-%m-%d'),5)) >=65 "
	 ;
}
          rs = dbObj.queryResults(sql);
          while (rs.next()) {
            props.setProperty(vServiceCode.get(i) + "pat65_70" + vServiceDesc.get(i), Misc.getString(rs,
                    "count(distinct(b.demographic_no))"));
          }

if(bDx) {
          sql =
                  "select count(distinct(b.billing_no)) from billing b, billingdetail bd, demographic d  where b.billing_no=bd.billing_no and b.demographic_no=d.demographic_no and bd.diagnostic_code='"
                   + vServiceCode.get(i) + "' and b.creator='" + providerNo + "' and b.billing_date>='" + startDate +
                  "' and b.billing_date<='" + endDate + "' and b.status!='D' and bd.status!='D'" +
                  " and (YEAR(CURRENT_DATE)-YEAR(DATE_FORMAT(CONCAT((d.year_of_birth),'-',(d.month_of_birth),'-',(d.date_of_birth)),'%Y-%m-%d'))) - (RIGHT(CURRENT_DATE,5)<RIGHT(DATE_FORMAT(CONCAT((d.year_of_birth), '-', (d.month_of_birth), '-', (d.date_of_birth)), '%Y-%m-%d'),5)) <=70 "
                   +
                  " and (YEAR(CURRENT_DATE)-YEAR(DATE_FORMAT(CONCAT((d.year_of_birth), '-', (d.month_of_birth), '-', (d.date_of_birth)), '%Y-%m-%d'))) - (RIGHT(CURRENT_DATE,5)<RIGHT(DATE_FORMAT(CONCAT((d.year_of_birth), '-', (d.month_of_birth), '-', (d.date_of_birth)), '%Y-%m-%d'),5)) >=65 "
                  ;
} else {
	sql = "select count(distinct(b.billing_no)) from billing b, billingdetail bd, demographic d where b.billing_no=bd.billing_no and b.demographic_no=d.demographic_no  and b.billing_date>='"
	 + startDate + "' and b.billing_date<='" + endDate + "' and b.creator='" + providerNo  + "' and b.status!='D' and bd.status!='D' and bd.service_code='" + vServiceCode.get(i) + "' and bd.service_desc='" + vServiceDesc.get(i) + "'" +
                  " and (YEAR(CURRENT_DATE)-YEAR(DATE_FORMAT(CONCAT((d.year_of_birth),'-',(d.month_of_birth),'-',(d.date_of_birth)),'%Y-%m-%d'))) - (RIGHT(CURRENT_DATE,5)<RIGHT(DATE_FORMAT(CONCAT((d.year_of_birth), '-', (d.month_of_birth), '-', (d.date_of_birth)), '%Y-%m-%d'),5)) <=70 "
                   +
                  " and (YEAR(CURRENT_DATE)-YEAR(DATE_FORMAT(CONCAT((d.year_of_birth), '-', (d.month_of_birth), '-', (d.date_of_birth)), '%Y-%m-%d'))) - (RIGHT(CURRENT_DATE,5)<RIGHT(DATE_FORMAT(CONCAT((d.year_of_birth), '-', (d.month_of_birth), '-', (d.date_of_birth)), '%Y-%m-%d'),5)) >=65 "
	 ;
}
          rs = dbObj.queryResults(sql);
          while (rs.next()) {
            props.setProperty(vServiceCode.get(i) + "vis65_70" + vServiceDesc.get(i), Misc.getString(rs,
                    "count(distinct(b.billing_no))"));
          }

          // get age 71-
if(bDx) {
          sql =
                  "select count(distinct(b.demographic_no)) from billing b, billingdetail bd, demographic d  where b.billing_no=bd.billing_no and b.demographic_no=d.demographic_no and bd.diagnostic_code='"
                   + vServiceCode.get(i) + "' and b.creator='" + providerNo + "' and b.billing_date>='" + startDate +
                  "' and b.billing_date<='" + endDate + "' and b.status!='D' and bd.status!='D'" +
                  " and (YEAR(CURRENT_DATE)-YEAR(DATE_FORMAT(CONCAT((d.year_of_birth), '-', (d.month_of_birth), '-', (d.date_of_birth)), '%Y-%m-%d'))) - (RIGHT(CURRENT_DATE,5)<RIGHT(DATE_FORMAT(CONCAT((d.year_of_birth), '-', (d.month_of_birth), '-', (d.date_of_birth)), '%Y-%m-%d'),5)) >=71 "
                  ;
} else {
	sql = "select count(distinct(b.demographic_no)) from billing b, billingdetail bd, demographic d where b.billing_no=bd.billing_no and b.demographic_no=d.demographic_no  and b.billing_date>='"
	 + startDate + "' and b.billing_date<='" + endDate + "' and b.creator='" + providerNo + "' and b.status!='D' and bd.status!='D' and bd.service_code='" + vServiceCode.get(i) + "' and bd.service_desc='" + vServiceDesc.get(i) + "' " +
                  " and (YEAR(CURRENT_DATE)-YEAR(DATE_FORMAT(CONCAT((d.year_of_birth), '-', (d.month_of_birth), '-', (d.date_of_birth)), '%Y-%m-%d'))) - (RIGHT(CURRENT_DATE,5)<RIGHT(DATE_FORMAT(CONCAT((d.year_of_birth), '-', (d.month_of_birth), '-', (d.date_of_birth)), '%Y-%m-%d'),5)) >=71 "
	 ;
}
          rs = dbObj.queryResults(sql);
          while (rs.next()) {
            props.setProperty(vServiceCode.get(i) + "pat71_" + vServiceDesc.get(i), Misc.getString(rs,
                    "count(distinct(b.demographic_no))"));
          }

if(bDx) {
          sql =
                  "select count(distinct(b.billing_no)) from billing b, billingdetail bd, demographic d  where b.billing_no=bd.billing_no and b.demographic_no=d.demographic_no and bd.diagnostic_code='"
                   + vServiceCode.get(i) + "' and b.creator='" + providerNo + "' and b.billing_date>='" + startDate +
                  "' and b.billing_date<='" + endDate + "' and b.status!='D' and bd.status!='D'" +
                  " and (YEAR(CURRENT_DATE)-YEAR(DATE_FORMAT(CONCAT((d.year_of_birth), '-', (d.month_of_birth), '-', (d.date_of_birth)), '%Y-%m-%d'))) - (RIGHT(CURRENT_DATE,5)<RIGHT(DATE_FORMAT(CONCAT((d.year_of_birth), '-', (d.month_of_birth), '-', (d.date_of_birth)), '%Y-%m-%d'),5)) >=71 "
                  ;
} else {
	sql = "select count(distinct(b.billing_no)) from billing b, billingdetail bd, demographic d where b.billing_no=bd.billing_no and b.demographic_no=d.demographic_no  and b.billing_date>='"
	 + startDate + "' and b.billing_date<='" + endDate + "' and b.creator='" + providerNo  + "' and b.status!='D' and bd.status!='D' and bd.service_code='" + vServiceCode.get(i) + "' and bd.service_desc='" + vServiceDesc.get(i) + "'" +
                  " and (YEAR(CURRENT_DATE)-YEAR(DATE_FORMAT(CONCAT((d.year_of_birth), '-', (d.month_of_birth), '-', (d.date_of_birth)), '%Y-%m-%d'))) - (RIGHT(CURRENT_DATE,5)<RIGHT(DATE_FORMAT(CONCAT((d.year_of_birth), '-', (d.month_of_birth), '-', (d.date_of_birth)), '%Y-%m-%d'),5)) >=71 "
	 ;
}
          rs = dbObj.queryResults(sql);
          while (rs.next()) {
            props.setProperty(vServiceCode.get(i) + "vis71_" + vServiceDesc.get(i), Misc.getString(rs,
                    "count(distinct(b.billing_no))"));
          }


        }
%>
<table border="0" cellspacing="0" cellpadding="0" width="100%">
	<tr bgcolor="<%="#669999"%>">
		<th align="left"><font face="Helvetica" color="white"> <%=providerName%>
		- PATIENT VISIT LIST </font></th>
		<th width="10%" nowrap><input type="button" name="Button"
			value="Print" onClick="window.print()"> <input type="button"
			name="Button" value=" Exit " onClick="window.close()"></th>
	</tr>
</table>
<table border="0" cellspacing="0" cellpadding="0" width="100%">
	<tr>
		<td>Period: ( <%= startDate %> ~ <%= endDate %> )</td>
	</tr>
</table>
<table width="100%" border="1" bgcolor="#ffffff" cellspacing="0"
	cellpadding="0">
	<tr bgcolor="<%=tdTitleColor%>">
		<TH colspan="2" width="10%"><%=bDx?"Dx Code":"ServiceCode"%></TH>
		<TH colspan="2" width="6%">Total</TH>
		<TH colspan="2" width="6%">Sex F</TH>
		<TH colspan="2" width="6%">Sex M</TH>
		<TH colspan="2" width="6%">0-1 yr</TH>
		<TH colspan="2" width="6%">2-11 yr</TH>
		<TH colspan="2" width="6%">12-20 yr</TH>
		<TH colspan="2" width="6%">21-34 yr</TH>
		<TH colspan="2" width="6%">35-50 yr</TH>
		<TH colspan="2" width="6%">51-64 yr</TH>
		<TH colspan="2" width="6%">65-70 yr</TH>
		<TH colspan="2" width="6%">71+ yr</TH>
	</tr>
	<tr align="center" bgcolor="<%=tdTitleColor%>">
		<td>Code</td>
		<td>Description</td>
		<td>Pt.</td>
		<td>Visit</td>
		<td>Pt.</td>
		<td>Visit</td>
		<td>Pt.</td>
		<td>Visit</td>
		<td>Pt.</td>
		<td>Visit</td>
		<td>Pt.</td>
		<td>Visit</td>
		<td>Pt.</td>
		<td>Visit</td>
		<td>Pt.</td>
		<td>Visit</td>
		<td>Pt.</td>
		<td>Visit</td>
		<td>Pt.</td>
		<td>Visit</td>
		<td>Pt.</td>
		<td>Visit</td>
		<td>Pt.</td>
		<td>Visit</td>
	</tr>
	<%
String catName = "";
String color = "";
int codeNum = 0;
int vecNum = 0;
for (int i = 0; i < vServiceCode.size(); i++) {
  if(bDx) {
  // sync vServiceCode and vec
  codeNum = Integer.parseInt((String)vServiceCode.get(i));
  if (propCatCode.containsKey(""+codeNum )) {
    vecNum = Integer.parseInt(propCatCode.getProperty(""+codeNum));

    // display the category name if necessary
          String  curCatName = ((Properties)vec.get(vecNum)).getProperty("level1", "").toUpperCase() + " - " +
                  ((Properties)vec.get(vecNum)).getProperty("level2", "");

          if (!curCatName.equals(catName)) {
            // new level1
            if (catName.indexOf('-') > 0 && !curCatName.startsWith(catName.substring(0, catName.indexOf('-')))) {
              tdSubtitleColor = "#00FF99";
            } else {
              tdSubtitleColor = "mediumaquamarine";
            }

            // new level2
            catName = curCatName;
%>
	<tr bgcolor="<%=tdSubtitleColor%>">
		<td colspan="24"><%= curCatName %></td>
	</tr>
	<%
          }
    color = i%2==0?tdInterlColor:"white";
  } else {
    color = "gold";
  }
  }
%>
	<tr bgcolor="<%=color %>" align="center">
		<td><%=vServiceCode.get(i)%></td>
		<td><%=vServiceDesc.get(i)%></td>
		<td><%=getNumAndCalTotal(props.getProperty(vServiceCode.get(i) + "pat" + vServiceDesc.get(i)), 0)%></td>
		<td><%=getNumAndCalTotal(props.getProperty(vServiceCode.get(i) + "vis" + vServiceDesc.get(i)), 1)%></td>

		<td><%=getNumAndCalTotal(props.getProperty(vServiceCode.get(i) + "patSexF" + vServiceDesc.get(i)), 2)%></td>
		<td><%=getNumAndCalTotal(props.getProperty(vServiceCode.get(i) + "visSexF" + vServiceDesc.get(i)), 3)%></td>
		<td><%=getNumAndCalTotal(props.getProperty(vServiceCode.get(i) + "patSexM" + vServiceDesc.get(i)), 4)%></td>
		<td><%=getNumAndCalTotal(props.getProperty(vServiceCode.get(i) + "visSexM" + vServiceDesc.get(i)), 5)%></td>

		<td><%=getNumAndCalTotal(props.getProperty(vServiceCode.get(i) + "pat0_1" + vServiceDesc.get(i)), 6)%></td>
		<td><%=getNumAndCalTotal(props.getProperty(vServiceCode.get(i) + "vis0_1" + vServiceDesc.get(i)), 7)%></td>
		<td><%=getNumAndCalTotal(props.getProperty(vServiceCode.get(i) + "pat2_11" + vServiceDesc.get(i)), 8)%></td>
		<td><%=getNumAndCalTotal(props.getProperty(vServiceCode.get(i) + "vis2_11" + vServiceDesc.get(i)), 9)%></td>
		<td><%=getNumAndCalTotal(props.getProperty(vServiceCode.get(i) + "pat12_20" + vServiceDesc.get(i)), 10)%></td>
		<td><%=getNumAndCalTotal(props.getProperty(vServiceCode.get(i) + "vis12_20" + vServiceDesc.get(i)), 11)%></td>
		<td><%=getNumAndCalTotal(props.getProperty(vServiceCode.get(i) + "pat21_34" + vServiceDesc.get(i)), 12)%></td>
		<td><%=getNumAndCalTotal(props.getProperty(vServiceCode.get(i) + "vis21_34" + vServiceDesc.get(i)), 13)%></td>
		<td><%=getNumAndCalTotal(props.getProperty(vServiceCode.get(i) + "pat35_50" + vServiceDesc.get(i)), 14)%></td>
		<td><%=getNumAndCalTotal(props.getProperty(vServiceCode.get(i) + "vis35_50" + vServiceDesc.get(i)), 15)%></td>
		<td><%=getNumAndCalTotal(props.getProperty(vServiceCode.get(i) + "pat51_64" + vServiceDesc.get(i)), 16)%></td>
		<td><%=getNumAndCalTotal(props.getProperty(vServiceCode.get(i) + "vis51_64" + vServiceDesc.get(i)), 17)%></td>
		<td><%=getNumAndCalTotal(props.getProperty(vServiceCode.get(i) + "pat65_70" + vServiceDesc.get(i)), 18)%></td>
		<td><%=getNumAndCalTotal(props.getProperty(vServiceCode.get(i) + "vis65_70" + vServiceDesc.get(i)), 19)%></td>
		<td><%=getNumAndCalTotal(props.getProperty(vServiceCode.get(i) + "pat71_" + vServiceDesc.get(i)), 20)%></td>
		<td><%=getNumAndCalTotal(props.getProperty(vServiceCode.get(i) + "vis71_" + vServiceDesc.get(i)), 21)%></td>
	</tr>
	<%
          total=getNumAndCalTotal(total,props.getProperty(vServiceCode.get(i) + "pat" + vServiceDesc.get(i)), 0);
          total=getNumAndCalTotal(total,props.getProperty(vServiceCode.get(i) + "vis" + vServiceDesc.get(i)), 1);

          total=getNumAndCalTotal(total,props.getProperty(vServiceCode.get(i) + "patSexF" + vServiceDesc.get(i)), 2);
          total=getNumAndCalTotal(total,props.getProperty(vServiceCode.get(i) + "visSexF" + vServiceDesc.get(i)), 3);
          total=getNumAndCalTotal(total,props.getProperty(vServiceCode.get(i) + "patSexM" + vServiceDesc.get(i)), 4);
          total=getNumAndCalTotal(total,props.getProperty(vServiceCode.get(i) + "visSexM" + vServiceDesc.get(i)), 5);

          total=getNumAndCalTotal(total,props.getProperty(vServiceCode.get(i) + "pat0_1" + vServiceDesc.get(i)), 6);
          total=getNumAndCalTotal(total,props.getProperty(vServiceCode.get(i) + "vis0_1" + vServiceDesc.get(i)), 7);
          total=getNumAndCalTotal(total,props.getProperty(vServiceCode.get(i) + "pat2_11" + vServiceDesc.get(i)), 8);
          total=getNumAndCalTotal(total,props.getProperty(vServiceCode.get(i) + "vis2_11" + vServiceDesc.get(i)), 9);
          total=getNumAndCalTotal(total,props.getProperty(vServiceCode.get(i) + "pat12_20" + vServiceDesc.get(i)), 10);
          total=getNumAndCalTotal(total,props.getProperty(vServiceCode.get(i) + "vis12_20" + vServiceDesc.get(i)), 11);
          total=getNumAndCalTotal(total,props.getProperty(vServiceCode.get(i) + "pat21_34" + vServiceDesc.get(i)), 12);
          total=getNumAndCalTotal(total,props.getProperty(vServiceCode.get(i) + "vis21_34" + vServiceDesc.get(i)), 13);
          total=getNumAndCalTotal(total,props.getProperty(vServiceCode.get(i) + "pat35_50" + vServiceDesc.get(i)), 14);
          total=getNumAndCalTotal(total,props.getProperty(vServiceCode.get(i) + "vis35_50" + vServiceDesc.get(i)), 15);
          total=getNumAndCalTotal(total,props.getProperty(vServiceCode.get(i) + "pat51_64" + vServiceDesc.get(i)), 16);
          total=getNumAndCalTotal(total,props.getProperty(vServiceCode.get(i) + "vis51_64" + vServiceDesc.get(i)), 17);
          total=getNumAndCalTotal(total,props.getProperty(vServiceCode.get(i) + "pat65_70" + vServiceDesc.get(i)), 18);
          total=getNumAndCalTotal(total,props.getProperty(vServiceCode.get(i) + "vis65_70" + vServiceDesc.get(i)), 19);
          total=getNumAndCalTotal(total,props.getProperty(vServiceCode.get(i) + "pat71_" + vServiceDesc.get(i)), 20);
          total=getNumAndCalTotal(total,props.getProperty(vServiceCode.get(i) + "vis71_" + vServiceDesc.get(i)), 21);
	} %>
	<tr bgcolor="<%=tdTitleColor%>">
		<TH colspan="2" width="10%"><%=bDx?"Dx Code":"ServiceCode"%></TH>
		<TH colspan="2" width="6%">Total</TH>
		<TH colspan="2" width="6%">Sex F</TH>
		<TH colspan="2" width="6%">Sex M</TH>
		<TH colspan="2" width="6%">0-1 yr</TH>
		<TH colspan="2" width="6%">2-11 yr</TH>
		<TH colspan="2" width="6%">12-20 yr</TH>
		<TH colspan="2" width="6%">21-34 yr</TH>
		<TH colspan="2" width="6%">35-50 yr</TH>
		<TH colspan="2" width="6%">51-64 yr</TH>
		<TH colspan="2" width="6%">65-70 yr</TH>
		<TH colspan="2" width="6%">71+ yr</TH>
	</tr>
	<tr align="center" bgcolor="<%=tdTitleColor%>">
		<td>Code</td>
		<td>Description</td>
		<td>Pt.</td>
		<td>Visit</td>
		<td>Pt.</td>
		<td>Visit</td>
		<td>Pt.</td>
		<td>Visit</td>
		<td>Pt.</td>
		<td>Visit</td>
		<td>Pt.</td>
		<td>Visit</td>
		<td>Pt.</td>
		<td>Visit</td>
		<td>Pt.</td>
		<td>Visit</td>
		<td>Pt.</td>
		<td>Visit</td>
		<td>Pt.</td>
		<td>Visit</td>
		<td>Pt.</td>
		<td>Visit</td>
		<td>Pt.</td>
		<td>Visit</td>
	</tr>
	<tr align="center">
		<td colspan="2" align="right">Sub. Total:</td>
		<% for(int i=0; i<total.length; i++) { %>
		<td><%=total[i]%></td>
		<% } %>
	</tr>
	<!--tr align="center">
            <td colspan="2" align="right">Total:</td>
            < %for(int i=0; i<total1.length; i++) { %>
            <td>< %=total1[i]%></td>
            < % } %>
		  </tr-->
</table>
<%
      }
%>
<script type="text/javascript">
Calendar.setup({ inputField : "startDate", ifFormat : "%Y/%m/%d", showsTime :false, button : "startDate_cal", singleClick : true, step : 1 });
Calendar.setup({ inputField : "endDate", ifFormat : "%Y/%m/%d", showsTime :false, button : "endDate_cal", singleClick : true, step : 1 });
      </script>
</body>
<%! int[] total1 = {0,0,0,0,0, 0,0,0,0,0, 0,0,0,0,0, 0,0,0,0,0, 0,0};
%>
<%! String getNumAndCalTotal(String str, int i) {
	String ret = str;
	int j = Integer.parseInt(str);
	total1[i] += j;
	return ret;
}

 int[] getNumAndCalTotal(int[] sTotal, String str, int i) {
	//String ret = str;
	int j = Integer.parseInt(str);
	sTotal[i] += j;
	return sTotal;
}
%>
</html:html>
