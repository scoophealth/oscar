<%@page contentType="text/html"%>
<%@ page import="java.util.*" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<html>
<head><title>E-Chart</title>
  
<script language="javascript">
<% 

GregorianCalendar cal = new GregorianCalendar();
int curYear = cal.get(Calendar.YEAR);
int curMonth = (cal.get(Calendar.MONTH)+1);
int curDay = cal.get(Calendar.DAY_OF_MONTH);

// System.out.println("Redirecting to: ../oscarEncounter/IncomingEncounter.do?demographicNo="+request.getParameter("demographicNo")+"&reason=Lab+Results-Notes&curDate="+curYear+"-"+curMonth+"-"+curDay+"&status="); 

%>

function popupPage(vheight,vwidth,varpage) { 
  var page = "" + varpage;
  windowprops = "height="+vheight+",width="+vwidth+",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes,screenX=50,screenY=50,top=0,left=0";
  var popup=window.open(page, "<bean:message key="provider.appointmentProviderAdminDay.apptProvider"/>", windowprops);
  if (popup != null) {
    if (popup.opener == null) {
      popup.opener = self; 
    }
  }
}

popupPage(700, 980, '../oscarEncounter/IncomingEncounter.do?demographicNo=<%=request.getParameter("demographicNo")%>&reason=Lab+Results-Notes&curDate=<%=curYear%>-<%=curMonth%>-<%=curDay%>&status=');
window.close();
</script>

</head>
<body>

<a href="javascript:popupPage(700, 980, '../oscarEncounter/IncomingEncounter.do?demographicNo=<%=request.getParameter("demographicNo")%>&reason=Lab+Results-Notes&curDate=<%=curYear%>-<%=curMonth%>-<%=curDay%>&status=');window.close();">Please click here to go to the patient's E-Chart.</a>

</body>
</html>
