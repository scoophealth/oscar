<%@ page import="java.math.*, java.util.*, java.io.*, java.sql.*, oscar.*, java.net.*,oscar.MyDateFormat"  %>
<%@ include file="../admin/dbconnection.jsp" %>
<jsp:useBean id="apptMainBean" class="oscar.AppointmentMainBean" scope="session" /> 
<%@ include file="dbDMS.jsp" %>
<%

GregorianCalendar now=new GregorianCalendar();
  int curYear = now.get(Calendar.YEAR);
  int curMonth = (now.get(Calendar.MONTH)+1);
  int curDay = now.get(Calendar.DAY_OF_MONTH);
  
  String nowDate = String.valueOf(curYear)+"/"+String.valueOf(curMonth) + "/" + String.valueOf(curDay)+ " " +now.get(Calendar.HOUR_OF_DAY)+":"+now.get(Calendar.MINUTE) + ":"+now.get(Calendar.SECOND);

String module="", module_id="", doctype="", docdesc="", docxml="", doccreator="", docdate="", docfilename="", document_no="";


doctype = request.getParameter("doctype");
docdesc = request.getParameter("docdesc");
docdate = request.getParameter("docdate");
document_no = request.getParameter("document_no");
doccreator =request.getParameter("doccreator");
%>

<%



    String[] param =new String[4];
	  param[0]=doctype;
	  param[1]=docdesc;
	  param[2]=docdate;
	  param[3]=document_no;
	  int rowsAffected = apptMainBean.queryExecuteUpdate(param,"update_document");
	    
	
	           
	    
	    




%>
<jsp:forward page='documentList.jsp' >
<jsp:param name="orderby" value='updatedatetime desc' />
<jsp:param name="creator" value='<%=doccreator%>' />
<jsp:param name="doctype" value='' />
<jsp:param name="docdesc" value='' />
<jsp:param name="docfilename" value='' />
</jsp:forward>
