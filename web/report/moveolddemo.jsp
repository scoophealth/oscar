<%
  //moveolddemo.jsp?provider_no=&filename=
  //if(session.getValue("user") == null) response.sendRedirect("../logout.jsp");
  String curUser_no = (String) session.getAttribute("user");
  String deepcolor = "#CCCCFF", weakcolor = "#EEEEFF" ;
%>
<%@ page import="java.util.*, java.sql.*, oscar.*, java.text.*, oscar.oscarDB.*,java.net.*" errorPage="../appointment/errorpage.jsp" %>

<html>
<head>
<title>PATIENT DEMO LIST </title>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv=Expires content=-1>
<!--link rel="stylesheet" href="../web.css" -->
<script language="JavaScript">
<!--
function setfocus() {
  this.focus();
  //document.titlesearch.keyword.select();
}
function refresh() {
  history.go(0);
}

//-->
</SCRIPT>
</head>
<body bgproperties="fixed" onLoad="setfocus()" topmargin="0" leftmargin="0" rightmargin="0">
busy ... busy ... busy ..................................................<br>
<%
//display splash-msg first
out.flush();

// get total patientNum
String provider_no = request.getParameter("provider_no")!=null?request.getParameter("provider_no") : "456" ;
String filename = request.getParameter("filename")!=null?request.getParameter("filename") : "demo20031231lees.dump" ;
Vector props = new Vector();

RandomAccessFile raf = new RandomAccessFile(filename, "r");
while(true) {
	String s = raf.readLine();
	if(s == null ) {System.out.println(" break!!!!" +s);break;}

	s = s.trim()
	if (s.length() < 1) continue;

	String demoNo = "";
	StringTokenizer st = new StringTokenizer(s);
	if (st.hasMoreTokens()) {
		//demoNo = st.nextToken();
		props.add(st.nextToken());
	}
}
raf.close();


DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);

// set patNo
for (int i = 0; i < props.size(); i++) {
	String sql = "update demographic set provider_no=" + provider_no + " where demographic_no=" + ((String)props.get(i));
	if (db.RunSQL(sql)) out.println((String)props.get(i) + "<br>");
}

db.CloseConn();

%>

</body>
</html>