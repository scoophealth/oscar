<%@page import="java.util.*"%>
<%@page import="org.oscarehr.common.dao.DemographicExtDao" %>
<%@page import="org.oscarehr.util.SpringUtils" %>
<jsp:useBean id="apptMainBean" class="oscar.AppointmentMainBean" scope="session" />
<%
String demographic_no = request.getParameter("demo");
DemographicExtDao demographicExtDao = SpringUtils.getBean(DemographicExtDao.class);
Map<String,String> demoExt = demographicExtDao.getAllValuesForDemo(demographic_no);

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



