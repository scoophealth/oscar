<%
  if(session.getValue("user") == null) response.sendRedirect("../logout.jsp");
  int demographic_no = Integer.parseInt(request.getParameter("demographic_no")); 
  int fid = Integer.parseInt(request.getParameter("fid")) ; 
  String form_provider = (String) session.getAttribute("user");
  String form_name = request.getParameter("form_name"); //(session.getValue("form_name")).toString() ; 
  String deepColor = "#CCCCFF" , weakColor = "#EEEEFF" ;
%>  
<%@ page import = "java.sql.ResultSet,oscar.util.*,oscar.eform.*"   errorPage="../errorpage.jsp"%> 
<jsp:useBean id="dataBean" scope="session" class="oscar.eform.EfmDataOpt" />
<jsp:useBean id="beanMakeForm" scope="session" class="oscar.eform.EfmMakeForm" />
<%  
  // this requested data is for gererating the target HTML string
  String label = "" ;
  String date = "" ;
  String currentproblems = "" ;
  String currentmedications = "" ;
  String familysocialhistory  = "" ;
  String alert = "" ;
  String subject = "" ;

  if (request.getParameter("label")!=null){
    label = UtilMisc.charEscape(request.getParameter("label"), '\''); 
  }
  if (request.getParameter("date")!=null){
    date = UtilMisc.charEscape(request.getParameter("date"), '\''); 
  }
  if (request.getParameter("currentproblems")!=null){
    currentproblems = UtilMisc.charEscape(request.getParameter("currentproblems"), '\''); 
  }
  if (request.getParameter("currentmedications")!=null){
    currentmedications = UtilMisc.charEscape(request.getParameter("currentmedications"), '\''); 
  }
  if (request.getParameter("familysocialhistory")!=null){
    familysocialhistory = UtilMisc.charEscape(request.getParameter("familysocialhistory"), '\''); 
  }
  if (request.getParameter("alert")!=null){
    alert = UtilMisc.charEscape(request.getParameter("alert"), '\''); 
  }
  if (request.getParameter("subject")!=null){
    subject = UtilMisc.charEscape(request.getParameter("subject"), '\''); 
  }

// "formString" - from eforom
  String formString = dataBean.getFormString(fid);

// mix "formString" with new inputing data
  EfmPrepData prepData = new EfmPrepData(request, formString) ;  
  String form_data = beanMakeForm.addContent(formString, prepData.getMeta(), prepData.getValue(), prepData.getTagSymbol() ) ;
  
// save form
  dataBean.save_eform_data (demographic_no,fid,form_name,subject,form_provider,form_data);

//response.sendRedirect("MyForm.jsp?");
  response.sendRedirect("myform.jsp?demographic_no="+demographic_no);
%>  