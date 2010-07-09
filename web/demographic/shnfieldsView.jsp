<%@page import="java.util.*"%>
<jsp:useBean id="apptMainBean" class="oscar.AppointmentMainBean"
	scope="session" />
<%
String demographic_no = request.getParameter("demo");
oscar.oscarDemographic.data.DemographicExt ext = new oscar.oscarDemographic.data.DemographicExt();
Hashtable demoExt = ext.getAllValuesForDemo(demographic_no);

Hashtable h = new Hashtable();
            h.put("-1","Not Asked");
            h.put("1","Has Given Consent");
            h.put("2","Has Refused Consent");                
%>
<li>Consent: <b><%=getText(h,apptMainBean.getString(demoExt.get("given_consent")) )%></b>
</li>

<%!

String getText(Hashtable h,String s){
    if (s != null && h.get(s) != null ){
        return (String) h.get(s);
    }
    return "";
} 
%>



