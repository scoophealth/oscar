<%@page import="java.util.*"%>
<jsp:useBean id="apptMainBean" class="oscar.AppointmentMainBean"
	scope="session" />
<%
String demographic_no = request.getParameter("demo");
oscar.oscarDemographic.data.DemographicExt ext = new oscar.oscarDemographic.data.DemographicExt();
Hashtable demoExt = ext.getAllValuesForDemo(demographic_no);

Hashtable h = new Hashtable();
            h.put("-1","Not Set");
            h.put("1","Status On-reserve");
            h.put("2","Status Off-reserve");	
            h.put("3","Non-status on-reserve");	
            h.put("4","Non-status off-reserve");	
            h.put("5","Metis");	
            h.put("6","Inuit");	
            h.put("7","Asian");	
            h.put("8","Caucasian");	
            h.put("9","Hispanic");	
            h.put("10","Black");	
            h.put("11","Other");	

Hashtable h2 = new Hashtable();
    h2.put("-1","Not Set");
    h2.put("1", "CHA1");
    h2.put("2", "CHA2");
    h2.put("3", "CHA3");
    h2.put("4", "CHA4");
    h2.put("5", "CHA5");
    h2.put("6", "CHA6");
    h2.put("7", "Richmond");
    h2.put("8", "North or West Vancouver");
    h2.put("9", "Surrey");
    h2.put("10", "On-Reserve");
    h2.put("11", "Homeless");
    h2.put("12", "Out of Country Residents");
    h2.put("13","Other");
    h2.put("14","Off-Reserve");
%>
<li>Area: <b><%=getArea(h2,apptMainBean.getString(demoExt.get("area")))%></b>
Status #: <b><%=apptMainBean.getString(demoExt.get("statusNum"))%></b>
Ethinicity: <b><%=getEth(h,apptMainBean.getString(demoExt.get("ethnicity")) )%></b>
</li>
<li>First Nations Community: <b><%=apptMainBean.getString(demoExt.get("fNationCom"))%></b></li>

<%!

String getEth(Hashtable h,String s){
    if (s != null && h.get(s) != null ){
        return (String) h.get(s);
    }
    return "";
} 


String getArea(Hashtable h2,String s){
    if (s != null && h2.get(s) != null ){
        return (String) h2.get(s);
    }
    return "";
}


          
 %>