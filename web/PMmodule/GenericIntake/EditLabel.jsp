<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@page import="java.sql.*,oscar.oscarDB.*" %>
<%@page import="java.util.*,org.oscarehr.PMmodule.dao.*,org.oscarehr.PMmodule.service.*,org.oscarehr.PMmodule.model.*,org.springframework.web.context.support.*,org.springframework.web.context.*" %>
<%@ include file="/taglibs.jsp" %>
<%
  WebApplicationContext  ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
  GenericIntakeManager  genericIntakeManager =  (GenericIntakeManager) ctx.getBean("genericIntakeManager");
    
    oscar.oscarDB.DBPreparedHandler dbH = new oscar.oscarDB.DBPreparedHandler();
        
    String lblEdit = request.getParameter("lbledit");
    String id = request.getParameter("id");
    
    if (lblEdit != null){
        IntakeNodeLabel iLabel = new IntakeNodeLabel();
        iLabel.setId(Integer.parseInt(id));
        iLabel.setLabel(lblEdit);
        genericIntakeManager.updateNodeLabel(iLabel);
        //String sqllbledit = "update intake_node_label set lbl = ? where intake_node_label_id = ?";
        //DBPreparedHandlerParam[] param=new DBPreparedHandlerParam[2];
        //param[0] = new DBPreparedHandlerParam(lblEdit);
        //param[1] = new DBPreparedHandlerParam(id);
        //dbH.queryExecuteUpdate(sqllbledit,param);
        response.sendRedirect("close.jsp");
        return;
    }
    
    
    String val = "";
    if(id !=null){
        IntakeNodeLabel eLabel = genericIntakeManager.getIntakeNodeLabel(Integer.parseInt(id));
        //String sqllbl = "select * from intake_node_label where intake_node_label_id = ?";
        //DBPreparedHandlerParam[] param=new DBPreparedHandlerParam[1];
        //param[0] = new DBPreparedHandlerParam(id);
        //System.out.println("before query");
        //ResultSet rs = dbH.queryResults(sqllbl,param);
        //System.out.println("after query");
        //if (rs.next()){
        //    System.out.println("getting value from query");
        //    val = dbH.getString(rs,"lbl");
        //}
        val = eLabel.getLabel();
    }
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Edit Label</title>
        <style type="text/css">
        @import "<html:rewrite page="/css/genericIntake.css"/>";
        </style>
    </head>
    <body>

    <h2>Edit Label</h2>
    <form action="EditLabel.jsp" method="post">
        <input type="text" name="lbledit" value="<%=val%>"/>
        <input type="hidden" name="id" value="<%=id%>"/>
        
        
        <input type="submit" value="update"/>
    </form>
    
    </body>
</html>
