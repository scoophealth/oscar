<%@page contentType="text/html"%>
<%@page pageEncoding="ISO-8859-1"%> 
<%@page
	import="java.util.Collections, java.util.Arrays, java.util.ArrayList, java.sql.ResultSet, java.sql.SQLException, oscar.oscarDB.DBHandler"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01//EN"
   "http://www.w3.org/TR/html4/strict.dtd">
<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>New Encounter Access</title>
<script type="text/javascript" LANGUAGE="JavaScript">

        function checkAll(formId){
	   var f = document.getElementById(formId);
           var val = f.checkA.checked;
	   for (i =0; i < f.encTesters.length; i++){
	   	f.encTesters[i].checked = val;
	   }
	}
        </script>

</head>
<body>

<%  
    ArrayList newDocArr = (ArrayList) request.getSession().getServletContext().getAttribute("newDocArr");
    Boolean useNewEchart = (Boolean) request.getSession().getServletContext().getAttribute("useNewEchart");
    
    String userNo = (String) request.getSession().getAttribute("user"); 
    if( userNo != null && (newDocArr != null || (useNewEchart != null && useNewEchart.equals(Boolean.TRUE))) ) {
        
        if( newDocArr == null ) newDocArr = new ArrayList();
        
        if( request.getMethod().equalsIgnoreCase("get") ) {
%>
<h3>Assign New Encounter to:</h3>

<form method="post" action="newEncounter.jsp" id="sbForm"><input
	type="checkbox" name="checkAll2" onclick="checkAll('sbForm')"
	id="checkA" /> Check All<br>
<%            
            try {
                String sql = "SELECT provider.provider_no, last_name, first_name from provider, security where provider.provider_no = security.provider_no order by last_name";
                DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
                ResultSet rs = db.GetSQL(sql);                            
                
                while(rs.next()) {
                    String provNo = db.getString(rs,"provider_no");
                    if( newDocArr.contains(provNo)) {
%> <input type="checkbox" name="encTesters" value="<%=provNo%>" checked><%=db.getString(rs,"last_name")%>,
<%=db.getString(rs,"first_name")%><br>
<%
                    }
                    else {
%> <input type="checkbox" name="encTesters" value="<%=provNo%>"><%=db.getString(rs,"last_name")%>,
<%=db.getString(rs,"first_name")%><br>
<%                   
                    }                
                }
%> <input type="submit" value="Update"> <%                
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
            
            Collections.sort(newDocArr);
        
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
