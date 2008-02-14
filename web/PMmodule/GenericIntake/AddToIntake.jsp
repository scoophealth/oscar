<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@page import="java.sql.*,oscar.oscarDB.*" %>
<%@page import="java.util.*,org.oscarehr.PMmodule.dao.*,org.oscarehr.PMmodule.service.*,org.oscarehr.PMmodule.model.*,org.springframework.web.context.support.*,org.springframework.web.context.*" %>
<%
WebApplicationContext  ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
GenericIntakeManager  genericIntakeManager =  (GenericIntakeManager) ctx.getBean("genericIntakeManager");

if (request.getParameter("newpos") != null && request.getParameter("parent_intake_node_id") != null && request.getParameter("elementType") != null && request.getParameter("intake_node_label") != null){
    
    String npos                  = request.getParameter("newpos") ;
    String parent_intake_node_id = request.getParameter("parent_intake_node_id") ;
    String eleType               = request.getParameter("elementType") ;
    String intNodeLabel          = request.getParameter("intake_node_label") ;
    
    IntakeNodeLabel intakeNodeLabel = new IntakeNodeLabel();
    intakeNodeLabel.setLabel(intNodeLabel);
    int lblId = 1;
    if (intNodeLabel != null && !intNodeLabel.equals("")){
        genericIntakeManager.saveNodeLabel(intakeNodeLabel);
        lblId = intakeNodeLabel.getId();
    }
        
    IntakeNode intakeNode = new IntakeNode();
    IntakeNodeTemplate intakeNodeTemplate = new IntakeNodeTemplate();
    intakeNodeTemplate.setId(Integer.parseInt(eleType));
    intakeNode.setNodeTemplate(intakeNodeTemplate);
    intakeNode.setLabel(intakeNodeLabel);
    intakeNode.setPos(Integer.parseInt(npos));
    
    IntakeNode parentNode = new IntakeNode();
    parentNode.setId(Integer.parseInt(parent_intake_node_id));
    System.out.println(" "+intakeNode.toString()+ " \n\n\n"+parentNode.getId());
    intakeNode.setParent(parentNode);
    
    genericIntakeManager.saveIntakeNode(intakeNode);
    
    response.sendRedirect("close.jsp");
    return;
}


%>


<%
String id           = request.getParameter("id");
String nodeTemplate = request.getParameter("node");
String parentId     = request.getParameter("parentId");
String pos          = request.getParameter("pos");
String pSize        = request.getParameter("pSize");
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Add To Intake</title>
    </head>
    <body>
        <form method="post" action="AddToIntake.jsp">
            
            <input type="hidden" name="newpos"                value="<%=pSize%>"/>
            
            <input type="hidden" name="parent_intake_node_id" value="<%=id%>" />
            
            
            <%if (nodeTemplate == null) {%>
            NO ELEMENT!
            <%}else if(nodeTemplate.equals("1") ){%>
            Add Section  
            <input type="hidden" name="elementType" value="4"/>
            <%}else if(nodeTemplate.equals("3") ){%>
            NADA
            <%}else if(nodeTemplate.equals("4") ){%>
            +<input type="radio" name="elementType" value="5">question</input><br/>
            +<input type="radio" name="elementType" value="6"> answer compound</input> <br/>
            +<input type="radio" name="elementType" value="7"> answer scalar choice</input> <br/>
            +<input type="radio" name="elementType" value="8"> answer scalar text</input> <br/>
            +<input type="radio" name="elementType" value="13"> answer scalar note</input><br/>
            
            <%}else if(nodeTemplate.equals("5") ){%>
            +<input type="radio" name="elementType" value="5"> question</input><br/>
            +<input type="radio" name="elementType" value="6"> answer compound</input> <br/>
            +<input type="radio" name="elementType" value="7"> answer scalar choice</input> <br/>
            +<input type="radio" name="elementType" value="8"> answer scalar text</input> <br/>
            +<input type="radio" name="elementType" value="13"> answer scalar note</input><br/>
            <%}else if(nodeTemplate.equals("6") ){%>
            +<input type="radio" name="elementType" value="7"> answer scalar choice</input> <br/>
            +<input type="radio" name="elementType" value="8"> answer scalar text</input> <br/>
            +<input type="radio" name="elementType" value="13"> answer scalar note</input><br/>
            <%}else{%>
            
            <%}%>
            
      
            <br> Label Text (Leave blank for no text):
            <input type="text" name="intake_node_label" />
            
            <input type="submit" value="Add" />
        </form>  
    </body>
</html>