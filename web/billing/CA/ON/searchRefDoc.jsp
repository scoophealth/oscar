<!--
/*
 *
 * This software is published under the GPL GNU General Public License.
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version. *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. *
 *
 * <OSCAR TEAM>
 */
-->
<%
  if (session.getAttribute("user") == null) {
    response.sendRedirect("../logout.jsp");
  }
  String strLimit1="0";
  String strLimit2="10";
  if(request.getParameter("limit1")!=null) strLimit1 = request.getParameter("limit1");
  if(request.getParameter("limit2")!=null) strLimit2 = request.getParameter("limit2");

  int nItems = 0;
  Vector vec = new Vector();
  Properties prop = null;
  String param = request.getParameter("param")==null?"":request.getParameter("param") ;
  String param2 = request.getParameter("param2")==null?"":request.getParameter("param2") ;
  String keyword = request.getParameter("keyword");

	if (request.getParameter("submit") != null && (request.getParameter("submit").equals("Search")
		|| request.getParameter("submit").equals("Next Page") || request.getParameter("submit").equals("Last Page")) ) {
	  BillingONDataHelp dbObj = new BillingONDataHelp();
	  String search_mode = request.getParameter("search_mode");
	  String orderBy = request.getParameter("orderby");
	  String where = "";
	  if("search_name".equals(search_mode)) {
	    String[] temp = keyword.split("\\,\\p{Space}*");
	    if(temp.length>1) {
	      where = "last_name like '" + StringEscapeUtils.escapeSql(temp[0]) + "%' and first_name like '" + StringEscapeUtils.escapeSql(temp[1]) + "%'";
	    } else {
	      where = "last_name like '" + StringEscapeUtils.escapeSql(temp[0]) + "%'";
	    }
	  } else {
	    where = search_mode + " like '" + StringEscapeUtils.escapeSql(keyword) + "%'";
	  }
	  String	sql   = "select * from billingreferral where " + where + " order by " + orderBy + " limit " +strLimit1+"," +strLimit2;
	  ResultSet rs = dbObj.searchDBRecord(sql);
		System.out.println(sql);
	  while (rs.next()) {
	  	prop = new Properties();
	  	prop.setProperty("referral_no",rs.getString("referral_no"));
	  	prop.setProperty("last_name",rs.getString("last_name"));
	  	prop.setProperty("first_name",rs.getString("first_name"));
	  	prop.setProperty("specialty",rs.getString("specialty"));
	  	prop.setProperty("phone",rs.getString("phone"));
	  	vec.add(prop);
	  }
	}
%>
  <%@ page errorPage="../appointment/errorpage.jsp"import="java.util.*,
                                                           java.sql.*" %>
  <%@ page import="oscar.oscarBilling.ca.on.data.BillingONDataHelp" %>
  <%@ page import="org.apache.commons.lang.StringEscapeUtils" %>
  <%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
  <%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
  <html:html locale="true">
    <head>
      <title>
        Add/Edit Service Code
      </title>
      <meta http-equiv="Expires" content="Monday, 8 Aug 88 18:18:18 GMT">
      <meta http-equiv="Cache-Control" content="no-cache">
      <style type="text/css">
          BODY{
              FONT-SIZE: Normal;
              FONT-FAMILY: Verdana, Tahoma, Arial, sans-serif;
          }
          TABLE{
              font-family: Arial, Verdana, Tahoma, Helvetica, sans-serif;
          }
          TD{
              font-size:11pt;
          }
          TH{
              font-size:11pt;
          }
          .blueText{
              font-size:9pt;
              vertical-align: top;
          }
          .mbttn {
              background: #D7DBF2;
              border-bottom: 1px solid #104A7B;
              border-right: 1px solid #104A7B;
              border-left: 1px solid #AFC4D5;
              border-top:1px solid #AFC4D5;
              color:#000066;height:19px;
              text-decoration:none;
              cursor: hand
          }
          .subject {
              background-color: #003399;
              color: #FFFFFF;
              font-size: 14pt;
              font-weight: bold;
              text-align: centre;
          }
          .searchTitle {
              background-color: #6699CC;
              color: #000000;
              font-weight: bold;
              text-align: left;
              height="20px"
          }
          .title {
              background-color: #6699CC;
              color: #000000;
              font-weight: bold;
              text-align: center;
              height="20px"
          }
      </style>
      <script language="JavaScript">

<!--
		function setfocus() {
		  this.focus();
		  document.forms[0].keyword.focus();
		  document.forms[0].keyword.select();
		}
		<%if(param.length()>0) {%>
		function typeInData1(data) {
		  self.close();
		  opener.<%=param%> = data;
		}
		<%if(param2.length()>0) {%>
		function typeInData2(data1, data2) {
		  opener.<%=param%> = data1;
		  opener.<%=param2%> = data2;
		  self.close();
		}
		<%}}%>
-->

      </script>
    </head>
    <body bgcolor="white" bgproperties="fixed" onload="setfocus()" topmargin="0" leftmargin="0" rightmargin="0">
      <table border="0" cellpadding="1" cellspacing="0" width="100%" bgcolor="#CCCCFF">
        <form method="post" name="titlesearch" action="searchRefDoc.jsp" onsubmit="return checkTypeIn()">
          <tr>
            <td class="searchTitle" colspan="4">
              Search Referral Doctor
            </td>
          </tr>
          <tr>
            <td class="blueText" width="10%" nowrap>
              <input type="radio" name="search_mode" value="search_name" checked>
              Name
            </td>
            <td class="blueText" nowrap>
              <input type="radio" name="search_mode" value="specialty">
              Specialty
            </td>
            <td valign="middle" rowspan="2" align="left">
              <input type="text" name="keyword" value="" size="17" maxlength="100">
              <input type="hidden" name="orderby" value="last_name, first_name">
              <input type="hidden" name="limit1" value="<%=strLimit1%>">
              <input type="hidden" name="limit2" value="<%=strLimit2%>">
              <input type="submit" class="mbttn" name="submit" value='Search' size="17">
            </td>
          </tr>
          <input type='hidden' name='param' value="<%=StringEscapeUtils.escapeHtml(param)%>">
          <input type='hidden' name='param2' value="<%=StringEscapeUtils.escapeHtml(param2)%>">
        </form>
      </table>
		<table width="95%" border="0">
		<tr>
		<td align="left">Results based on keyword(s): <%=keyword==null?"":keyword%></td>
		</tr>
		</table>
      <script language="JavaScript">
        var fullname = "";

        function addName(demographic_no, lastname, firstname, chartno, messageID) {
          fullname = lastname + "," + firstname;
          document.addform.action = "../appointment/addappointment.jsp?demographic_no=" + demographic_no + "&name=" +
                  fullname + "&chart_no=" + chartno + "&bFirstDisp=false" + "&messageID=" + messageID;

          //+"\"" ;
          document.addform.submit();
          //
  //return;

        }
      </script>
      <center>
      <table width="100%" border="0" cellpadding="0" cellspacing="2" bgcolor="#C0C0C0">
        <tr class="title"><th width="20%"><b>Ref. No.</b>
          </th>
          <th width="20%">
            Last Name</b>
          </th>
          <th width="20%">
            First Name</b>
          </th>
          <th width="20%">
            Specialty</b>
          </th>
          <th width="20%">
            Phone</b>
          </th>
        </tr>
        <%for(int i=0; i<vec.size(); i++) {
        	prop = (Properties) vec.get(i);
			String bgColor = i%2==0?"#EEEEFF":"ivory";
			String strOnClick = param2.length()>0? "typeInData2('" + prop.getProperty("referral_no", "") + "','"+prop.getProperty("last_name", "")+ "," + prop.getProperty("first_name", "") + "')"
				: "typeInData1('" + prop.getProperty("referral_no", "") + "')";
        %>
		<tr align="center"  bgcolor="<%=bgColor%>" align="center"
onMouseOver="this.style.cursor='hand';this.style.backgroundColor='pink';" onMouseout="this.style.backgroundColor='<%=bgColor%>';"
onClick="<%=strOnClick%>" >
		  <td><%=prop.getProperty("referral_no", "")%></td>
		  <td><%=prop.getProperty("last_name", "")%></td>
		  <td><%=prop.getProperty("first_name", "")%></td>
		  <td><%=prop.getProperty("specialty", "")%></td>
		  <td><%=prop.getProperty("phone", "")%></td>
		</tr>
		<% } %>
      </table>

<%
  nItems=vec.size();
  int nLastPage=0,nNextPage=0;
  nNextPage=Integer.parseInt(strLimit2)+Integer.parseInt(strLimit1);
  nLastPage=Integer.parseInt(strLimit1)-Integer.parseInt(strLimit2);
%>


<%
  if(nItems==0 && nLastPage<=0) {
%>
  <bean:message key="demographic.search.noResultsWereFound"/>
<%
  }
%>
<script language="JavaScript">
<!--
function last() {
  document.nextform.action="searchRefDoc.jsp?keyword=<%=request.getParameter("keyword")%>&search_mode=<%=request.getParameter("search_mode")%>&orderby=<%=request.getParameter("orderby")%>&limit1=<%=nLastPage%>&limit2=<%=strLimit2%>" ;
  //document.nextform.submit();
}
function next() {
  document.nextform.action="searchRefDoc.jsp?keyword=<%=request.getParameter("keyword")%>&search_mode=<%=request.getParameter("search_mode")%>&orderby=<%=request.getParameter("orderby")%>&limit1=<%=nNextPage%>&limit2=<%=strLimit2%>" ;
  //document.nextform.submit();
}
//-->
</SCRIPT>

<form method="post" name="nextform" action="searchRefDoc.jsp" >
<%
  if(nLastPage>=0) {
%>
<input type="submit" class="mbttn" name="submit" value="<bean:message key="demographic.demographicsearch2apptresults.btnPrevPage"/>"  onClick="last()">
<%
  }
  if(nItems==Integer.parseInt(strLimit2)) {
%>
<input type="submit" class="mbttn" name="submit" value="<bean:message key="demographic.demographicsearch2apptresults.btnNextPage"/>"  onClick="next()">
<%
}
%>
</form>
<br>
  <a href="addEditRefDoc.jsp">Add/Edit Referral Doctor</a>
      </center></body>
  </html:html>
