<%@page import="java.util.*"%>
<%@page import="org.oscarehr.common.dao.DemographicExtDao" %>
<%@page import="org.oscarehr.util.SpringUtils" %>
<jsp:useBean id="apptMainBean" class="oscar.AppointmentMainBean" scope="session" />
<%
String demographic_no = request.getParameter("demo");
DemographicExtDao demographicExtDao = SpringUtils.getBean(DemographicExtDao.class);
Map<String,String> demoExt = demographicExtDao.getAllValuesForDemo(demographic_no);

%>
<tr>
	<td align="right"><b>Consent:</b></td>
	<td align="left" colspan="3">
	<% String given_consent = apptMainBean.getString(demoExt.get("given_consent")); %>
	<select name="given_consent">
		<option value="-1" <%=getSel(given_consent,"-1")%>>Not Asked</option>
		<option value="1" <%=getSel(given_consent,"1")%>>Has Given
		Consent</option>
		<option value="2" <%=getSel(given_consent,"2")%>>Has Refused
		Consent</option>
	</select> <input type="hidden" name="ethnicityOrig"
		value="<%=apptMainBean.getString(demoExt.get("given_consent"))%>">
	</td>

</tr>
<%!
    String getSel(String s, String s2){
        if (s != null && s2 != null && s.equals(s2)){
            return "selected";
        }
        return "";
    }

%>