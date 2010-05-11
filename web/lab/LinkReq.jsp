<%@page contentType="text/html"%>
<%@page pageEncoding="ISO-8859-1"%> 

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<%@page	import="java.sql.ResultSet,
		java.sql.SQLException,
		java.util.Date,
		java.util.Hashtable,
		java.util.Vector,
		oscar.oscarDB.DBHandler,
		oscar.oscarLab.LabRequestReportLink,
		oscar.util.UtilDateUtilities"%>
<%
    String table = request.getParameter("table");
    String rptId = request.getParameter("rptid");
    String reqId = request.getParameter("reqid");
    String linkReqId = request.getParameter("linkReqId");
    
    String sql = null;
    String reqDateLink = "";
    
    Vector<String> req_id      = new Vector<String>();
    Vector<String> formCreated = new Vector<String>();
    Vector<String> patientName = new Vector<String>();
    if (linkReqId==null) {
	try {
	    reqDateLink = LabRequestReportLink.getRequestDate(reqId);
	    
	    sql = "SELECT ID, formCreated, patientName FROM formLabReq07";
	    DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
	    ResultSet rs = db.GetSQL(sql);

	    while (rs.next()) {
		req_id.add(rs.getString("ID"));
		patientName.add(rs.getString("patientName"));
		formCreated.add(UtilDateUtilities.DateToString(rs.getDate("formCreated"),"yyyy-MM-dd"));
	    }
	} catch (SQLException ex) {
		ex.printStackTrace();
	}
    } else { //Linked
	try {
	    sql = "SELECT formCreated FROM formLabReq07 WHERE ID="+linkReqId;
	    DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
	    ResultSet rs = db.GetSQL(sql);
	    String req_date = "";
	    if (rs.next()) req_date = UtilDateUtilities.DateToString(rs.getDate("formCreated"),"yyyy-MM-dd");
	
	    Long id = LabRequestReportLink.getIdByReport(table, Long.valueOf(rptId));
	    if (id==null) { //new report
		LabRequestReportLink.save("formLabReq07",Long.valueOf(linkReqId),req_date,table,Long.valueOf(rptId));
	    } else {
		LabRequestReportLink.update(id,"formLabReq07",Long.valueOf(linkReqId),req_date);
	    }
	} catch (SQLException ex) {
		ex.printStackTrace();
	}
	response.sendRedirect("../close.html");
    }
%>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
        <title>Link to Lab Requisition</title>
    </head>
    <body>

    <form action="LinkReq.jsp" method="post" onsubmit="return (linkReqId.value>0);">
	<input type="hidden" name="table" value="<%=table%>" />
	<input type="hidden" name="rptid" value="<%=rptId%>" />
	<input type="hidden" name="reqid" value="<%=reqId%>" />
	<p>&nbsp;</p>
	Requisition Date: <%=reqDateLink%><p>
	Link to Lab Requestion:
	<select name="linkReqId">
	    <option value="-1">---</option>
<%
    for (int i=0; i<req_id.size(); i++) {
	boolean sameID = false;
	if (reqId.equals(req_id.get(i))) sameID = true;
%>
	    <option value="<%=req_id.get(i)%>" <%=sameID?"selected":""%>><%=formCreated.get(i)%> : <%=patientName.get(i)%></option>
<%  } %>
	</select><p>
	<input type="submit" value="Link" />
	<input type="button" value="Cancel" onclick="window.close();" />
    </form>
    
    
    </body>
</html>
