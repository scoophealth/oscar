<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
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

<%@ page
	import="java.util.*, oscar.oscarReport.data.*, java.sql.*, oscar.login.*, java.net.*"
	errorPage="../appointment/errorpage.jsp"%>
<% java.util.Properties oscarVariables = oscar.OscarProperties.getInstance(); %>

<%
if(request.getParameter("submit")!=null && request.getParameter("submit").equals("Report in CSV")) {
    if(true) {
      out.clear();
      pageContext.forward("reportDownload"); //forward request&response to the target page
      return;
    }
}

String ARTYPE = "formBCAR";
if (request.getParameter("bcartype") != null && request.getParameter("bcartype").equals("BCAR2007")){
    ARTYPE = "formBCAR2007";
}

DBHelp dbObj = new DBHelp();

Properties propDemoSelect = new Properties();
Properties propSpecSelect = new Properties();
Properties propARSelect = new Properties();
ResourceBundle recprop = ResourceBundle.getBundle("oscarResources",request.getLocale());
propDemoSelect.setProperty("last_name", recprop.getString("oscarReport.oscarReportscpbDemo.msgLastName"));
propDemoSelect.setProperty("first_name", recprop.getString("oscarReport.oscarReportscpbDemo.msgFirstName"));
propDemoSelect.setProperty("date_joined", recprop.getString("oscarReport.oscarReportscpbDemo.msgDateJoined"));
propDemoSelect.setProperty("hin", recprop.getString("oscarReport.oscarReportscpbDemo.msgHIN"));
propDemoSelect.setProperty("hc_type", recprop.getString("oscarReport.oscarReportscpbDemo.msgHINType"));
propDemoSelect.setProperty("address", recprop.getString("oscarReport.oscarReportscpbDemo.msgAddress"));
propDemoSelect.setProperty("city", recprop.getString("oscarReport.oscarReportscpbDemo.msgCity"));
propDemoSelect.setProperty("postal", recprop.getString("oscarReport.oscarReportscpbDemo.msgPostalCode"));
propDemoSelect.setProperty("phone", recprop.getString("oscarReport.oscarReportscpbDemo.msgHPhone"));
propDemoSelect.setProperty("phone2", recprop.getString("oscarReport.oscarReportscpbDemo.msgWPhone"));
propDemoSelect.setProperty("email", recprop.getString("oscarReport.oscarReportscpbDemo.msgEmail"));
Vector vecSeqDemoSelect = new Vector();
vecSeqDemoSelect.add("last_name");
vecSeqDemoSelect.add("first_name");
vecSeqDemoSelect.add("date_joined");
vecSeqDemoSelect.add("hin");
vecSeqDemoSelect.add("hc_type");
vecSeqDemoSelect.add("address");
vecSeqDemoSelect.add("city");
vecSeqDemoSelect.add("postal");
vecSeqDemoSelect.add("phone");
vecSeqDemoSelect.add("phone2");
vecSeqDemoSelect.add("email");

Vector vecSeqSpecSelect = new Vector();
propSpecSelect.setProperty("prefer_language", recprop.getString("oscarReport.oscarReportscpbDemo.msgPrefLang"));
if(oscarVariables.getProperty("demographicExt") != null) {
	String [] propDemoExt = oscarVariables.getProperty("demographicExt","").split("\\|");
	for(int i=0; i<propDemoExt.length; i++) {
	    propSpecSelect.setProperty(propDemoExt[i].replace(' ', '_'), propDemoExt[i]);
	    vecSeqSpecSelect.add(propDemoExt[i].replace(' ', '_'));
	}
}

propARSelect.setProperty("c_EDD", recprop.getString("oscarReport.oscarReportscpbDemo.msgEDD"));
propARSelect.setProperty("pg1_famPhy", recprop.getString("oscarReport.oscarReportscpbDemo.msgFamPhys"));
propARSelect.setProperty("pg1_partnerName", recprop.getString("oscarReport.oscarReportscpbDemo.msgPartner"));
Vector vecSeqARSelect = new Vector();
vecSeqARSelect.add("c_EDD");
vecSeqARSelect.add("ga");
vecSeqARSelect.add("pg1_famPhy");
vecSeqARSelect.add("pg1_partnerName");

propARSelect.setProperty("ga", recprop.getString("oscarReport.oscarReportscpbDemo.msgGA"));
propARSelect.setProperty("b_primiparous", recprop.getString("oscarReport.oscarReportscpbDemo.msgPrimiparous"));

//get selection
boolean bDemoSelect = false;
boolean bARSelect = false;
boolean bSpecSelect = false;
String sDemoSelect = "";
String sSpecSelect = "";
String sARSelect = "";
boolean bARSelectGA = false;
boolean bARSelectPrimiparous = false;
String CHECK_BOX = "filter_";
String VALUE = "value_";
String DATE_FORMAT = "dateFormat_";
String VARNAME_FORMAT = "startDate\\d|endDate\\d";
Vector vecValue = new Vector();
Vector vecDateFormat = new Vector();
Properties propTempDemoSelect = new Properties();
Properties propTempARSelect = new Properties();
Properties propTempSpecSelect = new Properties();

Enumeration enumVar = request.getParameterNames();   
while (enumVar.hasMoreElements()) {
    String name = (String) enumVar.nextElement();
    if(propDemoSelect.containsKey(name)) { 
        bDemoSelect = true; 
        propTempDemoSelect.setProperty(name, "");
    }
    if(propARSelect.containsKey(name)) { 
        bARSelect = true; 
        if(name.equals("ga")) bARSelectGA = true;
        if(name.equals("b_primiparous")) bARSelectPrimiparous = true;
        
        if(!name.equals("ga") && !name.equals("b_primiparous"))
	        sARSelect += (sARSelect.length()<1?"":",") + ARTYPE+"."+name; 
    }
    if(propSpecSelect.containsKey(name)) { 
        bSpecSelect = true; 
        sSpecSelect += (sSpecSelect.length()<1?"":",") + "demographicExt."+name; 
    }
    
    if (name.startsWith(VALUE)) {
        String serialNo = name.substring(VALUE.length());
        if (request.getParameter(CHECK_BOX + serialNo) == null)
            continue;

        vecValue.add(request.getParameter(name));
        vecDateFormat.add(request.getParameter(DATE_FORMAT + serialNo));
    }
}
// get seq. select string
for(int i=0; i<vecSeqDemoSelect.size(); i++) {
    if(propTempDemoSelect.getProperty((String)vecSeqDemoSelect.get(i)) != null) {
        sDemoSelect += (sDemoSelect.length()<1?"":",") + "demographic." + vecSeqDemoSelect.get(i); 
    }
}
for(int i=0; i<vecSeqARSelect.size(); i++) {
    if(propTempARSelect.getProperty((String)vecSeqARSelect.get(i)) != null) {
        sARSelect += (sARSelect.length()<1?"":",") + ARTYPE+"." + vecSeqARSelect.get(i); 
    }
}
for(int i=0; i<vecSeqSpecSelect.size(); i++) {
    if(propTempSpecSelect.getProperty((String)vecSeqSpecSelect.get(i)) != null) {
        sSpecSelect += (sSpecSelect.length()<1?"":",") + "demographicExt." + vecSeqSpecSelect.get(i); 
    }
}

//get replaced filter
// filling the var with the real date value
Vector vecFilter = new Vector();
boolean bDemoFilter = false;
boolean bARFilter = false;
boolean bSpecFilter = false;
String sDemoFilter = "";
String sSpecFilter = "";
String sARFilter = "";
for (int i = 0; i < vecValue.size(); i++) {
	String tempVal = (String) vecValue.get(i);
	Vector vecVar = RptReportCreator.getVarVec(tempVal);
	Vector vecVarValue = new Vector();
	for (int j = 0; j < vecVar.size(); j++) {
		// conver date format if needed
		if (((String) vecVar.get(j)).matches(VARNAME_FORMAT) && ((String) vecDateFormat.get(i)).length() > 1) {
			vecVarValue.add(RptReportCreator.getDiffDateFormat(request.getParameter((String) vecVar.get(j)),
				(String) vecDateFormat.get(i), "yyyy-MM-dd"));
		} else {
			vecVarValue.add(request.getParameter((String) vecVar.get(j)));
		}
	}
        
        
	String strFilter = RptReportCreator.getWhereValueClause(tempVal, vecVarValue);
        
        
        
        
	if(strFilter.indexOf("demographic.")>=0) {
	    bDemoFilter = true;
	    sDemoFilter += (sDemoFilter.length()<1?"":" and ") + strFilter; 
	}
	if(strFilter.indexOf("demographicExt.")>=0) {
	    bSpecFilter = true;
	    sSpecFilter += (sSpecFilter.length()<1?"":" and ") + strFilter; 
	}
	if(strFilter.indexOf(ARTYPE+".")>=0) {
	    bARFilter = true;
        //"formBCAR.demographic_no in (select distinct demographic_no from formBCBirthSumMo)"
		if(strFilter.indexOf("formBCBirthSumMo") > 0) {
    		ResultSet rs = dbObj.searchDBRecord("select distinct demographic_no from formBCBirthSumMo");
    		String sBirthSumNo = "";
			while (rs.next()) {
			    sBirthSumNo += (sBirthSumNo.length()>0? ",":"") + rs.getInt("demographic_no") ;
			}
			sBirthSumNo = sBirthSumNo.length()>0 ? sBirthSumNo : "0";
			strFilter = ARTYPE+".demographic_no in (" + sBirthSumNo + ")";
		}

	    sARFilter += (sARFilter.length()<1?"":" and ") + strFilter; 
	}
	vecFilter.add(strFilter);
}

//query sub
//todo: filt out Delivered Clients 
// one table: demographic
Vector vecFieldCaption = new Vector();
Vector vecFieldName = new Vector();
Vector vecFieldValue = new Vector();
String ORDER_BY = " order by demographic.last_name, demographic.first_name";
if(bDemoSelect && !bARSelect && !bSpecSelect && bDemoFilter && !bARFilter && !bSpecFilter) {
	String sql = "select " + sDemoSelect + " from demographic where " + sDemoFilter + ORDER_BY;
	String [] temp = sDemoSelect.replaceAll("demographic.","").split(",");
	for(int i=0; i<temp.length; i++) {
	    vecFieldCaption.add(propDemoSelect.getProperty(temp[i].trim()));
	    vecFieldName.add(temp[i].trim());
	}
	vecFieldValue = (new RptReportCreator()).query(sql, vecFieldName);
}

// table: demographic and demographicExt
Vector vecSpecCaption = new Vector();
Properties propSpecValue = new Properties();
if( (bDemoSelect && !bARSelect && bSpecSelect && !bARFilter) || (!bARFilter && bSpecFilter) ) {
    if(bDemoSelect && !bARSelect && bSpecSelect && !bSpecFilter) {
        vecFieldName.add("demographic_no");
    	String sql = "select demographic_no," + sDemoSelect + " from demographic where " + sDemoFilter + ORDER_BY;
    	
    	String [] temp = sDemoSelect.replaceAll("demographic.","").split(",");
    	for(int i=0; i<temp.length; i++) {
    	    vecFieldCaption.add(propDemoSelect.getProperty(temp[i].trim()));
    	    vecFieldName.add(temp[i].trim());
    	    
    	}
    	vecFieldValue = (new RptReportCreator()).query(sql, vecFieldName);
    	vecFieldName.remove(0); // remove "demographic_no"

    	//get demographic_no
    	String strDemoNo = "";
    	for(int j=0; j<vecFieldValue.size(); j++) {
    		Properties prop = (Properties) vecFieldValue.get(j);
    		strDemoNo += (strDemoNo.length()<1? "" : ",") + prop.getProperty("demographic_no");
    	}
    	temp = sSpecSelect.replaceAll("demographicExt.","").split(",");
    	for(int i=0; i<temp.length; i++) {
    	    vecSpecCaption.add(propSpecSelect.getProperty(temp[i].trim()));
    		sql = "select demographic_no,value from demographicExt where key_val='" + temp[i] + "' and demographic_no in (";
    		sql += strDemoNo + ") order by date_time desc limit 1";
    		ResultSet rs = dbObj.searchDBRecord(sql);
			while (rs.next()) {
    			propSpecValue.setProperty(dbObj.getString(rs,"demographic_no")+temp[i], dbObj.getString(rs,"value"));
			}
    	}

    }
    if(bSpecFilter) {
        vecFieldName.add("demographic_no");
        // get demoNo
        String sql = null;
        ResultSet rs = null;
        String sTempEle = sSpecFilter.length()>0? (" and "+sSpecFilter) : "";
        String subQuery = "select distinct(demographic.demographic_no) from demographicExt, demographic where demographic.demographic_no=demographicExt.demographic_no ";
        subQuery += " and " + sDemoFilter + sTempEle + "  ";
    	
        String subFormDemoNo = "";
        rs = dbObj.searchDBRecord(subQuery);
    	while (rs.next()) {
    	    subFormDemoNo += (subFormDemoNo.length()>0? ",":"") +rs.getInt("demographic.demographic_no");
    	}
    	subFormDemoNo = subFormDemoNo.length()>0? subFormDemoNo : "0";
    	// get value for spec
        String [] temp = sSpecSelect.replaceAll("demographicExt.","").split(",");
    	for(int i=0; i<temp.length; i++) {
    	    vecSpecCaption.add(propSpecSelect.getProperty(temp[i].trim()));
    		sql = "select demographic_no,value from demographicExt where key_val='" + temp[i] + "' and demographic_no in (";
    		sql += subFormDemoNo + ") order by date_time desc limit 1";
    		rs = dbObj.searchDBRecord(sql);
			while (rs.next()) {
    			propSpecValue.setProperty(dbObj.getString(rs,"demographic_no")+temp[i], dbObj.getString(rs,"value"));
			}
    	}

    	//sTempEle = sSpecSelect.length()>0? (","+sSpecSelect) : "";
        sql = "select demographic.demographic_no," + sDemoSelect + " from demographic where ";
        sql += " demographic.demographic_no in (" + subFormDemoNo + ") " + ORDER_BY;
    	

    	temp = sDemoSelect.replaceAll("demographic.","").split(",");
    	for(int i=0; i<temp.length; i++) {
    	    vecFieldCaption.add(propDemoSelect.getProperty(temp[i].trim()));
    	    vecFieldName.add(temp[i].trim());
    	    
    	}
    	
    	vecFieldValue = (new RptReportCreator()).query(sql, vecFieldName);
    	vecFieldName.remove(0); // remove "demographic_no"
    }
}

// table: demographic and formBCAR
Vector vecARCaption = new Vector();
Properties propARValue = new Properties();
if( (bDemoSelect && bARSelect && !bSpecSelect && !bSpecFilter) || (!bSpecSelect && bARFilter && !bSpecFilter) ) {
    String sTempEle = sARFilter.length()>0? (" and "+sARFilter) : "";
    String subQuery = "select max(ID) from "+ARTYPE+", demographic where demographic.demographic_no="+ARTYPE+".demographic_no ";
    
    ///here is the prob
    
    subQuery += " and " + sDemoFilter + sTempEle + " group by "+ARTYPE+".demographic_no,"+ARTYPE+".formCreated ";
	
    String subFormId = "";
    ResultSet rs = dbObj.searchDBRecord(subQuery);
	while (rs.next()) {
	    subFormId += (subFormId.length()>0? ",":"") +rs.getInt("max(ID)");
	}

	sTempEle = sARSelect.length()>0? (","+sARSelect) : "";
	subFormId = subFormId.length()>0? subFormId : "0";
        
        
        
    String sql = "select demographic.demographic_no," + sDemoSelect + sTempEle + " from demographic,"+ARTYPE+" where ";
    sql += ARTYPE+".ID in (" + subFormId + ") and demographic.demographic_no="+ARTYPE+".demographic_no " + ORDER_BY;
	

	String [] temp = sDemoSelect.replaceAll("demographic.","").split(",");
	for(int i=0; i<temp.length; i++) {
	    vecFieldCaption.add(propDemoSelect.getProperty(temp[i].trim()));
	    vecFieldName.add(temp[i].trim());
	   
	}
	if(bARSelect) {
		temp = sARSelect.replaceAll(ARTYPE+".","").split(",");
		for(int i=0; i<temp.length; i++) {
		    vecFieldCaption.add(propARSelect.getProperty(temp[i].trim()));
		    vecFieldName.add(temp[i].trim());
		    
		}
	}
	vecFieldValue = (new RptReportCreator()).query(sql, vecFieldName);

	//vecFieldName.remove(0); // remove "demographic_no"
}

// table: all
if( (bDemoSelect && bARSelect && bSpecSelect) || (bARFilter && bSpecFilter) ) {
    if(bDemoSelect && bARSelect && bSpecSelect && !bSpecFilter) {
        vecFieldName.add("demographic_no");
        String sTempEle = sARFilter.length()>0? (" and "+sARFilter) : "";
        String subQuery = "select max(ID) from "+ARTYPE+", demographic where demographic.demographic_no="+ARTYPE+".demographic_no ";
        subQuery += " and " + sDemoFilter + sTempEle + " group by "+ARTYPE+".demographic_no,"+ARTYPE+".formCreated ";
        
        String subFormId = "";
        ResultSet rs = dbObj.searchDBRecord(subQuery);
    	while (rs.next()) {
    	    subFormId += (subFormId.length()>0? ",":"") +rs.getInt("max(ID)");
    	}

    	sTempEle = sARSelect.length()>0? (","+sARSelect) : "";
    	subFormId = subFormId.length()>0? subFormId : "0";
        String sql = "select demographic.demographic_no," + sDemoSelect + sTempEle + " from demographic,"+ARTYPE+" where ";
        sql += ARTYPE+".ID in (" + subFormId + ") and demographic.demographic_no="+ARTYPE+".demographic_no " + ORDER_BY;
        

    	String [] temp = sDemoSelect.replaceAll("demographic.","").split(",");
    	for(int i=0; i<temp.length; i++) {
    	    vecFieldCaption.add(propDemoSelect.getProperty(temp[i].trim()));
    	    vecFieldName.add(temp[i].trim());
    	    
    	}
    	if(bARSelect) {
    		temp = sARSelect.replaceAll(ARTYPE+".","").split(",");
    		for(int i=0; i<temp.length; i++) {
    		    vecFieldCaption.add(propARSelect.getProperty(temp[i].trim()));
    		    vecFieldName.add(temp[i].trim());
    		    
    		}
    	}
    	vecFieldValue = (new RptReportCreator()).query(sql, vecFieldName);
    	vecFieldName.remove(0); // remove "demographic_no"
        
    	//get demographic_no
    	String strDemoNo = "";
    	for(int j=0; j<vecFieldValue.size(); j++) {
    		Properties prop = (Properties) vecFieldValue.get(j);
    		strDemoNo += (strDemoNo.length()<1? "" : ",") + prop.getProperty("demographic_no");
    	}

    	temp = sSpecSelect.replaceAll("demographicExt.","").split(",");
    	for(int i=0; i<temp.length; i++) {
    	    vecSpecCaption.add(propSpecSelect.getProperty(temp[i].trim()));
    		sql = "select demographic_no,value from demographicExt where key_val='" + temp[i] + "' and demographic_no in (";
    		sql += strDemoNo + ") order by date_time ";
    		rs = dbObj.searchDBRecord(sql);
			while (rs.next()) {
    			propSpecValue.setProperty(dbObj.getString(rs,"demographic_no")+temp[i], dbObj.getString(rs,"value"));
			}
    	}
    }
	
	
	if(bARFilter && bSpecFilter) {
	    // spec first
        vecFieldName.add("demographic_no");
        // get demoNo
        String sql = null;
        ResultSet rs = null;
        String sTempEle = sSpecFilter.length()>0? (" and "+sSpecFilter) : "";
        String subQuery = "select distinct(demographic.demographic_no) from demographicExt, demographic where demographic.demographic_no=demographicExt.demographic_no ";
        subQuery += " and " + sDemoFilter + sTempEle + "  ";
    	
        String subFormDemoNo = "";
        rs = dbObj.searchDBRecord(subQuery);
    	while (rs.next()) {
    	    subFormDemoNo += (subFormDemoNo.length()>0? ",":"") +rs.getInt("demographic.demographic_no");
    	}
    	// get value for spec
        String [] temp = sSpecSelect.replaceAll("demographicExt.","").split(",");
    	for(int i=0; i<temp.length; i++) {
    	    vecSpecCaption.add(propSpecSelect.getProperty(temp[i].trim()));
    		sql = "select demographic_no,value from demographicExt where key_val='" + temp[i] + "' and demographic_no in (";
    		sql += subFormDemoNo + ") order by date_time desc limit 1";
    		rs = dbObj.searchDBRecord(sql);
			while (rs.next()) {
    			propSpecValue.setProperty(dbObj.getString(rs,"demographic_no")+temp[i], dbObj.getString(rs,"value"));
			}
    	}

    	// formAR second
        sTempEle = sARFilter.length()>0? (" and "+sARFilter) : "";
        subQuery = "select max(ID) from "+ARTYPE+", demographic where demographic.demographic_no="+ARTYPE+".demographic_no ";
        subQuery += " and " + sDemoFilter + sTempEle + " group by "+ARTYPE+".demographic_no,"+ARTYPE+".formCreated ";
    	
        String subFormId = "";
        rs = dbObj.searchDBRecord(subQuery);
    	while (rs.next()) {
    	    subFormId += (subFormId.length()>0? ",":"") +rs.getInt("max(ID)");
    	}

    	// total
    	sTempEle = sARSelect.length()>0? (","+sARSelect) : "";
    	subFormId = subFormId.length()>0? subFormId : "0";
        sql = "select demographic.demographic_no," + sDemoSelect + sTempEle + " from demographic,"+ARTYPE+" where ";
        sql += " demographic.demographic_no in (" +  subFormDemoNo + ") and ";
        sql += ARTYPE+".ID in (" + subFormId + ") and demographic.demographic_no="+ARTYPE+".demographic_no " + ORDER_BY;
    	

    	temp = sDemoSelect.replaceAll("demographic.","").split(",");
    	for(int i=0; i<temp.length; i++) {
    	    vecFieldCaption.add(propDemoSelect.getProperty(temp[i].trim()));
    	    vecFieldName.add(temp[i].trim());
    	    
    	}
    	if(bARSelect) {
    		temp = sARSelect.replaceAll(ARTYPE+".","").split(",");
    		for(int i=0; i<temp.length; i++) {
    		    vecFieldCaption.add(propARSelect.getProperty(temp[i].trim()));
    		    vecFieldName.add(temp[i].trim());
    		    
    		}
    	}
    	vecFieldValue = (new RptReportCreator()).query(sql, vecFieldName);
    	vecFieldName.remove(0); // remove "demographic_no"
    	
	}
}
%>

<html lang="en">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title><bean:message key="oscarReport.oscarReportscpbDemo.title"/></title>
<LINK REL="StyleSheet" HREF="../web.css" TYPE="text/css">
<!-- calendar stylesheet -->
<link rel="stylesheet" type="text/css" media="all"
	href="../share/calendar/calendar.css" title="win2k-cold-1" />
<!-- main calendar program -->
<script type="text/javascript" src="../share/calendar/calendar.js"></script>
<!-- language for the calendar -->
<script type="text/javascript"
	src="../share/calendar/lang/calendar-en.js"></script>
<!-- the following script defines the Calendar.setup helper function, which makes
       adding a calendar a matter of 1 or 2 lines of code. -->
<script type="text/javascript" src="../share/calendar/calendar-setup.js"></script>
<script language="JavaScript">

		<!--
		function setfocus() {
		  this.focus();
		  //document.forms[0].service_code.focus();
		}
	    function onAdd() {
			if(document.baseurl.name.value.length < 2) {
				alert("Please type in a valid name!");
				return false;
			} else {
	        	return true;
	        }
	    }

	    function goPage(id) {
			self.location.href = "reportFilter.jsp?id=" + id;
	    }
		//-->

      </script>
</head>
<body bgcolor="ivory" onLoad="setfocus()" topmargin="0" leftmargin="0"
	rightmargin="0">
<table BORDER="0" CELLPADDING="0" CELLSPACING="0" WIDTH="100%">
	<tr>
		<td align="left">&nbsp;</td>
	</tr>
</table>

<center>

<table BORDER="0" CELLPADDING="0" CELLSPACING="0" WIDTH="90%">
	<tr>
		<th BGCOLOR="#CCFFFF"><bean:message key="oscarReport.oscarReportscpbDemo.msgHeader"/></th>
		<th width="10%" nowrap><a href=# onclick="history.back();">
		<bean:message key="oscarReport.oscarReportscpbDemo.btnBack"/></a></th>
	</tr>
</table>

<table BORDER="0" CELLPADDING="1" CELLSPACING="1" WIDTH="100%"
	class="sortable tabular_list">
	<thead>
		<tr BGCOLOR="#66CCCC">
			<th width="6%"><bean:message key="oscarReport.oscarReportscpbDemo.msgNoHeader"/></th>
			<% for(int i=0; i<vecFieldCaption.size(); i++) { %>
			<th><%=(String) vecFieldCaption.get(i)%></th>
			<% } %>
			<% if(bSpecSelect) {
       for(int i=0; i<vecSpecCaption.size(); i++) {
%>
			<th><%=(String) vecSpecCaption.get(i)%></th>
			<% } }%>
		</tr>
	</thead>
	<% 
for(int i=0; i<vecFieldValue.size(); i++) {
	String color = i%2==0? "#EEEEFF" : "#DDDDFF";
	Properties prop = (Properties) vecFieldValue.get(i);
%>
	<tr BGCOLOR="<%=color%>">
		<td align="center"><%=i + 1 %></td>
		<% for(int j=0; j<vecFieldName.size(); j++) { %>
		<td><%=prop.getProperty((String) vecFieldName.get(j), "")%>&nbsp;</td>
		<% } %>
		<%  if(bSpecSelect) {
		String demoNo = prop.getProperty("demographic_no");
       for(int j=0; j<vecSpecCaption.size(); j++) {
%>
		<td><%=propSpecValue.getProperty(demoNo+((String) vecSpecCaption.get(j)).replaceAll(" ","_"), "")%>&nbsp;</td>
		<%	} } %>
	</tr>
	<%} %>
</table>

</center>
<script language="javascript" src="../commons/scripts/sort_table/css.js">
      <script language="javascript" src="../commons/scripts/sort_table/common.js">
      <script language="javascript" src="../commons/scripts/sort_table/standardista-table-sorting.js">
    </body>
  </html>
