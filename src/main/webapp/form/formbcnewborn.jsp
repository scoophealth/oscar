<%@ page import="oscar.form.*"%>
<%
    int demoNo = Integer.parseInt(request.getParameter("demographic_no"));
    int formId = Integer.parseInt(request.getParameter("formId"));

	// for oscarcitizens
    String historyet = request.getParameter("historyet") == null ? "" : ("&historyet=" + request.getParameter("historyet"));

	if(true) {
        out.clear();
		if (formId == 0) {
			pageContext.forward("formbcnewbornpg1.jsp?demographic_no=" + demoNo + "&formId=" + formId) ; 
 		} else {
			FrmRecord rec = (new FrmRecordFactory()).factory("BCNewBorn");
			java.util.Properties props = rec.getFormRecord(demoNo, formId);

			pageContext.forward("formbcnewborn" + props.getProperty("c_lastVisited", "pg1") 
				+ ".jsp?demographic_no=" + demoNo + "&formId=" + formId + historyet)  ;
		}

		return;
    }
%>
