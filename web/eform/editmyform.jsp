<%
  if(session.getValue("user") == null) response.sendRedirect("../logout.jsp");
  int demographic_no = Integer.parseInt(request.getParameter("demographic_no")); 
  int fdid = Integer.parseInt(request.getParameter("fdid")) ; 
  String deepColor = "#CCCCFF" , weakColor = "#EEEEFF" ;
%>  
<%@ page import = "java.sql.ResultSet,oscar.util.*, oscar.eform.*" %> 
<jsp:useBean id="dataBean" scope="session" class="oscar.eform.EfmDataOpt" />
<jsp:useBean id="beanMakeForm" scope="session" class="oscar.eform.EfmMakeForm" />
 
<%  
  String form_name = request.getParameter("form_name");
//  String subject = request.getParameter("subject");
  String form_date = request.getParameter("form_date");
  String form_time = request.getParameter("form_time");
  String form_provider = request.getParameter("form_provider");

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

  int fid = dataBean.getFID(fdid) ;
  String formString = dataBean.getFormString(fid);

  EfmPrepData prepData = new EfmPrepData(request, formString) ;  
  String form_data = beanMakeForm.addContent(formString, prepData.getMeta(), prepData.getValue(), prepData.getTagSymbol() ) ;

  dataBean.save_eform_data (demographic_no,fid,form_name,subject,form_provider,form_data);
  response.sendRedirect("showmyform.jsp?demographic_no="+demographic_no);
%>
