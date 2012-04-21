<%--

    Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
    This software is published under the GPL GNU General Public License.
    This program is free software; you can redistribute it and/or
    modify it under the terms of the GNU General Public License
    as published by the Free Software Foundation; either version 2
    of the License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program; if not, write to the Free Software
    Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.

    This software was written for the
    Department of Family Medicine
    McMaster University
    Hamilton
    Ontario, Canada

--%>

<%@page import="org.springframework.web.context.support.WebApplicationContextUtils,org.oscarehr.util.OntarioMD,java.util.Hashtable"%><%@page import="org.springframework.web.context.WebApplicationContext,org.oscarehr.common.dao.*,org.oscarehr.common.model.*"%><%

    WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
    UserPropertyDAO userPropertyDAO = (UserPropertyDAO) ctx.getBean("UserPropertyDAO");
    
    UserProperty username = userPropertyDAO.getProp((String) session.getAttribute("user"),  UserProperty.ONTARIO_MD_USERNAME);
    UserProperty password = userPropertyDAO.getProp((String) session.getAttribute("user"),  UserProperty.ONTARIO_MD_PASSWORD);

    String uname = "";
    if (username == null  || password == null){
        response.sendRedirect("../setProviderStaleDate.do?method=viewOntarioMDId");
        return;  
    }
    
    String pword  = "";
    if(password != null){
        pword = password.getValue();
    }
    
    if (username != null){
        uname=     username.getValue();
    }
    
    String keyword = request.getParameter("keyword");
    String params = request.getParameter("params");
    
    String requestor = OntarioMD.getIncomingRequestor();
    OntarioMD omd = new OntarioMD();
    
    Hashtable loginCreds = omd.loginToOntarioMD(uname,pword,requestor);
    String retCode = (String) loginCreds.get("returnCode");
    if ("-1".equals(retCode)){  //Invalid Username/Password
        %>
        Username or Password incorrect.  <a href="../setProviderStaleDate.do?method=viewOntarioMDId">Click here to enter you username and password</a>
        <%
        return;
    }else if("-2".equals(retCode)){ //Invalid requestor 
        %>
        Invalid Requestor Value. - Please contact your support vendor for configuration
        <%
        return;
    }
    
    
    
    if (keyword != null && keyword.equals("eCPS")){
        params = "&ecpsSearchValue="+params;
    }
            
%>
<html> 
<body> 
<form id="loginFormID" name="loginForm" action="https://www.ontariomd.ca/AutoAuthentication/redirect.jsp" method="post"> 
  <p>JSESSIONID:</p> 
  <input type="text" size="70" id="jsessionID" name="jsessionID" value="<%=loginCreds.get("jsessionID")%>"/> 
  <p>PT login Token:</p> 
  <input type="text" size="70" id="ptLoginToken" name="ptLoginToken" value="<%=loginCreds.get("ptLoginToken")%>"/> 
  <p>Keyword:</p> 
  <input type="text" size="100" id="keyword" name="keyword" value="<%=keyword%>"/> 
  <p>Params:</p> 
  <input type="text" size="200" id="params" name="params" value="<%=params%>"/> 
  <p>Requestor:</p> 
  <input type="text" size="50" id="requestor" name="requestor" value="<%=requestor%>"/> 
  <p>Username:</p> 
  <input type="text" size="50" id="username" name="username" value="<%=uname%>"/> 
  <p></p> 
  <input type="submit" value="Submit"/> 
 </form>
 <SCRIPT language="JavaScript">
//function submitform(){
  document.loginForm.submit();
//}
</SCRIPT> 
</body> 
</html>
