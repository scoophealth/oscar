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
        
        System.out.println("LABEL ID == "+lblId);
        //oscar.oscarDB.DBPreparedHandler dbH = new oscar.oscarDB.DBPreparedHandler();
        //String sqllbl = "insert into intake_node_label (lbl) values (?)";
        //DBPreparedHandlerParam[] param=new DBPreparedHandlerParam[1];
        //param[0] = new DBPreparedHandlerParam(intNodeLabel);
        //lblId = dbH.queryExecuteInsertReturnId(sqllbl,param);
    }
    oscar.oscarDB.DBHandler db = new oscar.oscarDB.DBHandler(oscar.oscarDB.DBHandler.OSCAR_DATA);
    
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
    
   
    //String sql ="insert into intake_node (intake_node_template_id,intake_node_label_id,pos,parent_intake_node_id) values ('"+eleType+"','"+intNodeLabel+"','"+npos+"','"+parent_intake_node_id+"')";
    //String sql ="insert into intake_node (intake_node_template_id,intake_node_label_id,pos,parent_intake_node_id) values ('"+eleType+"','"+lblId+"','"+npos+"','"+parent_intake_node_id+"')";
    
    //System.out.println(sql);
    //db.RunSQL(sql);
    
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
        <%--
        <h1>JSP Page <%=id%> + <%=nodeTemplate%> + <%=parentId%> + <%=pos%> + <%=pSize%></h1>
        
        <pre>
| intake_node_template_id |intake_node_type_id | intake_node_label_id | lbl                  | type                 |
+-------------------------+--------------------+----------------------+----------------------+----------------------+
|                       1 |                  1 |                    2 | Registration Intake2 | intake               |
|                       3 |                  2 |                    4 | Page                 | page                 |
|                       4 |                  3 |                    5 | Section              | section              |
|                       5 |                  4 |                    6 | Question             | question             |
|                       6 |                  5 |                    7 | Compound Answer      | answer compound      |        
    

| intake               | + section + question
| section              | + question + answer compound + answer scalar choice + answer scalar text + answer scalar note
| question             | + answer compound + answer scalar choice + answer scalar text + answer scalar note
| answer compound      | + answer scalar choice + answer scalar text + answer scalar note 



NEED to INSERT INTO INTAKE_NODE

insert into intake_node (intake_node_template_id,intake_node_label_id,pos,parent_intake_node_id) values (selected value below,selected from a list of lbls,<%=pSize%>,<%=id%>);  

        </pre>
        --%>
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
            +<input type="radio" name="elementType" value="5">question</input>
            +<input type="radio" name="elementType" value="6"> answer compound</input> 
            +<input type="radio" name="elementType" value="7"> answer scalar choice</input> 
            +<input type="radio" name="elementType" value="8"> answer scalar text</input> 
            +<input type="radio" name="elementType" value="13"> answer scalar note</input>
            
            <%}else if(nodeTemplate.equals("5") ){%>
            +<input type="radio" name="elementType" value="5"> question</input>
            +<input type="radio" name="elementType" value="6"> answer compound</input> 
            +<input type="radio" name="elementType" value="7"> answer scalar choice</input> 
            +<input type="radio" name="elementType" value="8"> answer scalar text</input> 
            +<input type="radio" name="elementType" value="13"> answer scalar note</input>
            <%}else if(nodeTemplate.equals("6") ){%>
            +<input type="radio" name="elementType" value="7"> answer scalar choice</input> 
            +<input type="radio" name="elementType" value="8"> answer scalar text</input> 
            +<input type="radio" name="elementType" value="13"> answer scalar note</input>
            <%}else{%>
            
            <%}%>
            
      
            <br> Label Text (Leave blank for no text):
            <input type="text" name="intake_node_label" />
            
            <input type="submit" value="Add" />
        </form>  
    </body>
</html>