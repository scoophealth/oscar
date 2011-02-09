<%@page
	import="java.util.Collections, java.util.Arrays, java.util.ArrayList, java.sql.ResultSet, java.sql.SQLException, oscar.oscarDB.DBHandler"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01//EN"
   "http://www.w3.org/TR/html4/strict.dtd">

<%@page import="org.oscarehr.util.MiscUtils"%>
<%@page import="org.oscarehr.common.dao.ProviderPreferenceDao"%>
<%@page import="org.oscarehr.util.SpringUtils"%>
<%@page import="org.oscarehr.common.model.ProviderPreference"%><html>
<head>
	<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
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
	String userNo = (String) session.getAttribute("user");
    ArrayList<String> newDocArr = (ArrayList<String>)request.getSession().getServletContext().getAttribute("CaseMgmtUsers");    
    
    ProviderPreferenceDao providerPreferenceDao=(ProviderPreferenceDao)SpringUtils.getBean("providerPreferenceDao");
    ProviderPreference providerPreference=providerPreferenceDao.find(userNo);
    
	

	String newCME = null;
	if(providerPreference!=null){
		newCME = providerPreference.getDefaultNewOscarCme();			
	}
	
	if(newDocArr.isEmpty()) {
		if("enabled".equals(newCME)){
			newDocArr.add(userNo);
			request.getSession().getServletContext().setAttribute("CaseMgmtUsers",newDocArr);
		}
    }
    

    if( userNo != null && newDocArr != null ) {
        
        if( request.getMethod().equalsIgnoreCase("get") ) {
%>
<h3>Assign New Casemanagement Screen to:</h3>

<form method="post" action="newCaseManagementEnable.jsp" id="sbForm">
<%            
            try {
                String sql = "SELECT provider.provider_no, last_name, first_name from provider, security where provider.provider_no = security.provider_no order by last_name";
                ResultSet rs = DBHandler.GetSQL(sql);                            
                
                while(rs.next()) {
                    String provNo = oscar.Misc.getString(rs,"provider_no");
                    if(!userNo.equals(provNo)) 
                    	continue;
                    if( newDocArr.contains("all") || newDocArr.contains(provNo)) {
%> <input type="checkbox" name="encTesters" value="<%=provNo%>" checked><%=oscar.Misc.getString(rs,"last_name")%>,
<%=oscar.Misc.getString(rs,"first_name")%><br>
<%
                    }
                    else {
%> <input type="checkbox" name="encTesters" value="<%=provNo%>"><%=oscar.Misc.getString(rs,"last_name")%>,
<%=oscar.Misc.getString(rs,"first_name")%><br>
<%                   
                    }                
                }
%> <input type="submit" value="Update"> <%                
            }catch(SQLException ex ) {
            	MiscUtils.getLogger().error("SQL Error", ex);
            }
%>
</form>
<%
        }
        else {
            String encTesters = request.getParameter("encTesters");

            if( encTesters == null ) {                
                
                if( newDocArr.contains("all") ) {
                    newDocArr.clear();                    
                    String sql = "SELECT provider.provider_no, last_name, first_name from provider, security where provider.provider_no = security.provider_no order by last_name";
                    ResultSet rs = DBHandler.GetSQL(sql); 
                    while(rs.next()) {
                        String provNo = oscar.Misc.getString(rs,"provider_no");
                        if( userNo.equals(provNo) ) 
                            continue;
                            
                        newDocArr.add(provNo);
                   }
                }
                else {
                    newDocArr.remove(userNo);
                }
                
            }
            else
                newDocArr.add(encTesters);
            
            Collections.sort(newDocArr);
        
            request.getSession().getServletContext().setAttribute("CaseMgmtUsers", newDocArr);

            if (providerPreference==null)
            {
            	// insert a default preference
				providerPreference=new ProviderPreference();
            	providerPreference.setProviderNo(userNo);
            	providerPreferenceDao.persist(providerPreference);
            } 
            	
			if(encTesters!=null && !encTesters.isEmpty()) {
				providerPreference.setDefaultNewOscarCme("enabled");
			} else {
				providerPreference.setDefaultNewOscarCme("disabled");
			}
			providerPreferenceDao.merge(providerPreference);
			
%>
<h3>Casemanagement Users Update Complete!</h3>

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
