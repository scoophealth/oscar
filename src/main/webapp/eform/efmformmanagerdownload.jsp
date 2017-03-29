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

<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
    String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
    boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_admin.eform" rights="w" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect(request.getContextPath() + "/securityError.jsp?type=_admin.eform");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>

<%  
  String deepColor = "#CCCCFF" , weakColor = "#EEEEFF" ;
  Vector<Hashtable> eforms = getEforms();
  boolean gridlayout = false;
  if(request.getParameter("grid") != null && request.getParameter("grid").equals("true")){
     gridlayout = true;
  }
%>
<%@ page import="oscar.eform.data.*, oscar.eform.*, java.util.*, oscar.util.*"%>
<%@ page import="java.util.*,oscar.oscarRx.data.*,oscar.oscarRx.pageUtil.*,java.io.*,org.apache.xmlrpc.*"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@page import="org.oscarehr.util.MiscUtils"%><html:html locale="true">
<head>
    <script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
    <title><bean:message key="eform.download.msgDownloadEform" /></title>
    <link rel="stylesheet" href="../share/css/OscarStandardLayout.css">
    <link rel="stylesheet" href="../share/css/eformStyle.css">
    <link rel="stylesheet" type="text/css" media="all" href="../share/css/extractedFromPages.css"  />
    <style type="text/css">
        table.listing td {border: 1px solid white;}
    </style>
</head>
<body>
    <div style="background: #CCCCFF; width: 100%; text-align:center; font-family:Helvetica,sans-serif; "><bean:message key="eform.download.msgDownloadEform" /> <a href="efmformmanagerdownload.jsp?grid=<%=!gridlayout%>">grid</a></div>

    <% if(gridlayout){%>
        <table class="listing" style="width:100%; background-color:#EEEEFF">
            <tr >
                <th><bean:message key="eform.download.msgName" /></th>
                <th><bean:message key="eform.download.msgCreator" /></th>
                <th><bean:message key="eform.download.msgCategory" /></th>
                <th><bean:message key="eform.download.msgCreated" /></th>
            </tr>

            <%
            for (Hashtable ht: eforms){
            %>

            <tr>
                <td><%=ht.get("name")%></td>
                <td><%=ht.get("creator")%></td>
                <td><%=ht.get("category")%></td>
                <td><%=ht.get("created_at")%></td>
                <td valign="middle">
                    <form action="../eform/manageEForm.do" method="POST" >
                           <input type="hidden" name="method" value="importEFormFromRemote"/>   <%--Look at just sending the filename from mydrugref  --%>
                           <input type="hidden" name="url" value="<%=stripDrugref(ht.get("url"))%>"/>
                           <input type="submit"  value="<bean:message key="eform.download.btnLoadEform" />"/>
                    </form>
                </td>
            </tr>
            <%}%>
        </table>
    <%}else{   /*Because i can't decide which looks better */%>

    <%

        for (Hashtable ht: eforms){
    %>

    <div style="background-color:#EEEEFF;margin-right:100px;margin-left:20px;margin-top:10px;padding-left:10px;padding-top:10px;padding-bottom:5px;border-bottom: 2px solid gray;border-right: 2px solid #999;border-top: 1px solid #CCC;border-left: 1px solid #CCC;">
        <table class="listing">
            <tr >
                <td><bean:message key="eform.download.msgName" />:</td>
                <td><%=ht.get("name")%></td>
            </tr>
            <tr>
                <td><bean:message key="eform.download.msgCreator" />:</td>
                <td> <%=ht.get("creator")%></td>
            </tr>
            <tr>
                <td><bean:message key="eform.download.msgCategory" />:</td>
                <td> <%=ht.get("category")%></td>
            </tr>
            <tr>
                <td><bean:message key="eform.download.msgCreated" />:</td>
                <td> <%=ht.get("created_at")%></td>
            </tr>
        </table>

        <form action="../eform/manageEForm.do" method="POST" >
               <input type="hidden" name="method" value="importEFormFromRemote"/>
               <input type="hidden" name="url" value="<%=stripDrugref(ht.get("url"))%>"/>
               <input type="submit"  value="<bean:message key="eform.download.btnLoadEform" />"/>
        </form>

    </div>

        <%}%>

    <%}%>
</body>
</html:html>
<%!

    String stripDrugref(Object obj){
        String s = "";
        if (obj !=null){
           s = (String) obj;

           return s.substring(26);
        }

        return "";
    }


   private Object callWebserviceLite(String procedureName,Vector params) throws Exception{
        Object object = null;
        String server_url = "http://know2act.org/backend/api";
        try{
            if (!System.getProperty("http.proxyHost","").isEmpty()) {
                //The Lite client won't recgonize JAVA_OPTS as it uses a customized http
                XmlRpcClient server = new XmlRpcClient(server_url);
                object = (Object) server.execute(procedureName, params);
            } else {
                XmlRpcClientLite server = new XmlRpcClientLite(server_url);
                object = (Object) server.execute(procedureName, params);
            }
        }catch (XmlRpcException exception) {

            MiscUtils.getLogger().error("JavaClient: XML-RPC Fault #" +exception.code, exception);

            throw new Exception("JavaClient: XML-RPC Fault #" +
                    Integer.toString(exception.code) + ": " +
                    exception.toString());

        } catch (Exception exception) {
        	MiscUtils.getLogger().error("JavaClient: ", exception);
            throw new Exception("JavaClient: " + exception.toString());
        }
        return object;
    }


     public Vector getEforms()throws Exception{
        Vector params = new Vector();
        Vector vec = new Vector();
        Object obj =  callWebserviceLite("GetEForms",params);
        if (obj instanceof Vector){
            vec = (Vector) obj;
        }else if(obj instanceof Hashtable){
            Object holbrook = ((Hashtable) obj).get("Holbrook Drug Interactions");
            if (holbrook instanceof Vector){
                vec = (Vector) holbrook;
            }
        }

        return vec;
    }

    public List getCategories(List<Hashtable> eforms){
        List catList=new ArrayList();
        for(Hashtable eform:eforms){
            if (!catList.contains(eform.get("category"))){
                catList.add(eform.get("category"));
            }
        }
        return catList;
    }


    public static void removeNullFromVector(Vector v){
        while(v != null && v.contains(null)){
            v.remove(null);
        }
    }

%>
