<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@page import="java.util.Collections, java.util.Arrays, java.util.ArrayList, java.sql.ResultSet, java.sql.SQLException, oscar.oscarDB.DBHandler"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01//EN"
   "http://www.w3.org/TR/html4/strict.dtd">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>New Encounter Access</title>
    </head>
    <body>
  
<%  
    ArrayList newDocArr = (ArrayList) request.getSession().getServletContext().getAttribute("newDocArr");
    String userNo = (String) request.getSession().getAttribute("user"); 
    if( userNo != null && newDocArr != null ) {
        
        if( request.getMethod().equalsIgnoreCase("get") ) {
%>
            <h3>Aassign New Encounter to:</h3>
        
            <form method="post" action="newEncounter.jsp">
<%                                        
        
            
            try {
                String sql = "SELECT provider.provider_no, last_name, first_name from provider, security where provider.provider_no = security.provider_no";
                DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
                ResultSet rs = db.GetSQL(sql);                            
                
                while(rs.next()) {
                    String provNo = rs.getString("provider_no");
                    if( newDocArr.contains(provNo)) {
%>
                        <input type="checkbox" name="encTesters" value="<%=provNo%>" checked><%=rs.getString("last_name")%>, <%=rs.getString("first_name")%><br>
<%
                    }
                    else {
%>
                        <input type="checkbox" name="encTesters" value="<%=provNo%>"><%=rs.getString("last_name")%>, <%=rs.getString("first_name")%><br>
<%                   
                    }                
                }
%>
                <input type="submit" value="Update">
<%                
            }catch(SQLException ex ) {
                System.out.println("SQL Error " + ex.getMessage());
            }
%>           
        </form>
<%
        }
        else {
            String[] encTesters = request.getParameterValues("encTesters");

            if( encTesters == null )
                newDocArr.clear();
            else
                newDocArr = new ArrayList(Arrays.asList(encTesters));
        
            request.getSession().getServletContext().setAttribute("newDocArr", newDocArr);
        
%>
        <h3>New Testers Update Complete!</h3>

<%
        }

    }
    else {
%>
        <p>You have to be logged in to use this form</p>

<%
    }
%>
    
    </body>
</html>
