<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@page import="java.sql.*,oscar.oscarDB.*" %>
<%@ include file="/taglibs.jsp" %>

<%--
The taglib directive below imports the JSTL library. If you uncomment it,
you must also add the JSTL library to the project. The Add Library... action
on Libraries node in Projects view can be used to add the JSTL 1.1 library.


select intake_node.intake_node_id, intake_node.intake_node_label_id, intake_node.pos, intake_node_label.lbl from intake_node , intake_node_label where parent_intake_node_id = ? and intake_node.intake_node_label_id = intake_node_label.intake_node_label_id;


+----------------+----------------------+------+-------------------------------------------------+
| intake_node_id | intake_node_label_id | pos  | lbl                                             |
+----------------+----------------------+------+-------------------------------------------------+
|           4401 |                 4401 |    0 | Physical or Mental Health, including Medication |
|           4402 |                 4402 |    1 | Obtaining Identification                        |
|           4403 |                 4403 |    2 | Addictions                                      |
|           4404 |                 4404 |    3 | Housing Issues                                  |
|           4405 |                 4405 |    4 | Education Issues                                |
|           4406 |                 4406 |    5 | Employment Issues                               |
|           4407 |                 4407 |    6 | Financial Issues                                |
|           4408 |                 4408 |    7 | Legal Issues                                    |
|           4409 |                 4409 |    8 | Immigration Issues                              |
|          41324 |                 4608 |    9 | Worker #                                        |
+----------------+----------------------+------+-------------------------------------------------+



--%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Reorder Childer</title>
    </head>
    <body>

    
    <%--
    This example uses JSTL, uncomment the taglib directive above.
    To test, display the page like this: index.jsp?sayHello=true&name=Murphy
    --%>
    <%--
    <c:if test="${param.sayHello}">
        <!-- Let's welcome the user ${param.name} -->
        Hello ${param.name}!
    </c:if>
    --%>
    <select>
    <%
    oscar.oscarDB.DBPreparedHandler dbH = new oscar.oscarDB.DBPreparedHandler();
        
    String lblEdit = request.getParameter("lbledit");
    String id = request.getParameter("id");
    
    /*
    if (lblEdit != null){
        System.out.println("Entering update");
        String sqllbledit = "update intake_node_label set lbl = ? where intake_node_label_id = ?";
        DBPreparedHandlerParam[] param=new DBPreparedHandlerParam[2];
        param[0] = new DBPreparedHandlerParam(lblEdit);
        param[1] = new DBPreparedHandlerParam(id);
        dbH.queryExecuteUpdate(sqllbledit,param);
        
    }
    */
    
    String val = "";
    if(id !=null){
        String sqllbl = "select intake_node.intake_node_id, intake_node.intake_node_label_id, intake_node.pos, intake_node_label.lbl from intake_node , intake_node_label where parent_intake_node_id = ? and intake_node.intake_node_label_id = intake_node_label.intake_node_label_id order by pos";
        DBPreparedHandlerParam[] param=new DBPreparedHandlerParam[1];
        param[0] = new DBPreparedHandlerParam(id);
        System.out.println("before query");
        ResultSet rs = dbH.queryResults(sqllbl,param);
        System.out.println("after query");
        if (rs.next()){
            System.out.println("getting value from query");
            val = dbH.getString(rs,"lbl");
            String intake_node_id = dbH.getString("intake_node_id");
            %>
            <option value=""></option>
            <%
        }
    }
%>
</select>
    
    
    
    
    </body>
</html>
