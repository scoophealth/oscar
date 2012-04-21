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
    if (linkReqId == null || linkReqId.length()==0) {
		try {
			if(reqId != null && reqId.length()>0) {
		    	reqDateLink = LabRequestReportLink.getRequestDate(reqId);
			}
		    
		    sql = "SELECT ID, formCreated, patientName FROM formLabReq07";
		    
		    ResultSet rs = DBHandler.GetSQL(sql);
	
		    while (rs.next()) {
				req_id.add(rs.getString("ID"));
				patientName.add(rs.getString("patientName"));
				formCreated.add(UtilDateUtilities.DateToString(rs.getDate("formCreated"),"yyyy-MM-dd"));
		    }
		} catch (SQLException ex) {
			MiscUtils.getLogger().error("Error", ex);
		}
    } else { //Make the link
		try {
		    sql = "SELECT formCreated FROM formLabReq07 WHERE ID="+linkReqId;
		    
		    ResultSet rs = DBHandler.GetSQL(sql);
		    String req_date = "";
		    if (rs.next()) 
		    	req_date = UtilDateUtilities.DateToString(rs.getDate("formCreated"),"yyyy-MM-dd");
		
		    Long id = LabRequestReportLink.getIdByReport(table, Long.valueOf(rptId));
		    if (id==null) { //new report
				LabRequestReportLink.save("formLabReq07",Long.valueOf(linkReqId),req_date,table,Long.valueOf(rptId));
		    } else {
				LabRequestReportLink.update(id,"formLabReq07",Long.valueOf(linkReqId),req_date);
		    }
		} catch (SQLException ex) {
			MiscUtils.getLogger().error("Error", ex);
		}
		response.sendRedirect("../close.html");
    }
%>


<%@page import="org.oscarehr.util.MiscUtils"%><html>
    <head>
        <title>Link to Lab Requisition</title>
    </head>
    <body>

    <form action="LinkReq.jsp" method="post" onsubmit="return (linkReqId.value>0);">
	<input type="hidden" name="table" value="<%=table%>" />
	<input type="hidden" name="rptid" value="<%=rptId%>" />
	<input type="hidden" name="reqid" value="<%=reqId%>" />
	<p>&nbsp;</p>
	Requisition Date: <%=reqDateLink%><p>
	Link to Lab Requisition:
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
