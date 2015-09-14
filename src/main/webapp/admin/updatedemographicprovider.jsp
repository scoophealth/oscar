<!DOCTYPE html>
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
<security:oscarSec roleName="<%=roleName$%>" objectName="_admin,_admin.misc" rights="r" reverse="<%=true%>"> 
	<%authed=false; %>
	<%response.sendRedirect("../securityError.jsp?type=_admin&type=_admin.misc");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>


<%@ page import="java.util.*, java.sql.*, oscar.*, java.text.*, java.lang.*,java.net.*" errorPage="../appointment/errorpage.jsp"%>

<jsp:useBean id="novector" class="java.util.Vector" scope="page" />
<%@ page import="org.oscarehr.util.SpringUtils" %>
<%@ page import="org.oscarehr.common.model.DemographicCust" %>
<%@ page import="org.oscarehr.common.dao.DemographicCustDao" %>
<%@ page import="org.oscarehr.common.model.Provider" %>
<%@ page import="org.oscarehr.PMmodule.dao.ProviderDao" %>
<%
	DemographicCustDao demographicCustDao = (DemographicCustDao)SpringUtils.getBean("demographicCustDao");
	ProviderDao providerDao = SpringUtils.getBean(ProviderDao.class);
	
	List<String> names = new ArrayList<String>();
%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:html locale="true">
<head>
<title><bean:message key="admin.admin.btnUpdatePatientProvider" /></title>
<link href="<%=request.getContextPath() %>/css/bootstrap.min.css" rel="stylesheet">
</head>
<script language="javascript">
<!-- start javascript ---- check to see if it is really empty in database

function setregexp() {
	var exp = "^[" +document.ADDAPPT.last_name_from.value + "-" +document.ADDAPPT.last_name_to.value + "]" ;
	document.ADDAPPT.regexp.value = exp ;
	//alert(document.ADDAPPT.regexp.value);
}
function setregexp1() {
	var exp = "^[" +document.ADDAPPT1.last_name_from.value + "-" +document.ADDAPPT1.last_name_to.value + "]" ;
	document.ADDAPPT1.regexp.value = exp ;
	//alert(document.ADDAPPT1.regexp.value);
}
function setregexp2() {
	var exp = "^[" +document.ADDAPPT2.last_name_from.value + "-" +document.ADDAPPT2.last_name_to.value + "]" ;
	document.ADDAPPT2.regexp.value = exp ;
	//alert(document.ADDAPPT2.regexp.value);
}
// stop javascript -->
</script>

<%
 	for(Provider p : providerDao.getActiveProviders()) {
 	  names.add(p.getProviderNo());
 	  names.add(p.getFormattedName());
 	}
%>
<body onLoad="setfocus()">
<div class="container-fluid">
<h3><bean:message key="admin.admin.btnUpdatePatientProvider" /></h3>
<%
  if(request.getParameter("update")!=null && request.getParameter("update").equals(" Go ") ) {
   
    Integer demoNo = demographicCustDao.select_demoname(request.getParameter("oldcust2"), request.getParameter("regexp"));
    if (demoNo != null) {
        novector.add(demoNo.toString());
    }
    int nosize = novector.size();
    int rowsAffected = 0;
    if(nosize != 0) {
      String [] param = new String[nosize+2] ;
      param[0] = request.getParameter("newcust2") ;
      param[1] = request.getParameter("oldcust2") ;
      StringBuffer sbtemp = new StringBuffer("?") ;
      param[0+2] = (String) novector.get(0);

      if(nosize>1) {
          for(int i=1; i<nosize; i++) {
 	      sbtemp = sbtemp.append(",?");
              param[i+2] = (String) novector.get(i);
 	  }
      }
      String instrdemo = sbtemp.toString();
     
      List<Integer> demoList= new ArrayList<Integer>();
      for(int x=2;x<param.length;x++) {
    	  demoList.add(Integer.parseInt(param[x]));
      }
      List<DemographicCust> demographicCusts = demographicCustDao.findMultipleResident(demoList, param[1]);
      for(DemographicCust demographicCust:demographicCusts) {
    	  demographicCust.setResident(param[0]);
    	  demographicCustDao.merge(demographicCust);
      }
      rowsAffected = demographicCusts.size();
    } %>
<%=rowsAffected %>
<bean:message key="admin.updatedemographicprovider.msgRecords" />
<br>
<%}

  if(request.getParameter("update")!=null && request.getParameter("update").equals(" Submit ") ) {
	  Integer demoNo = demographicCustDao.select_demoname1(request.getParameter("oldcust1"), request.getParameter("regexp"));
	    if (demoNo != null) {
	    	 novector.add(demoNo.toString());
	    }

  
    int nosize = novector.size();
    int rowsAffected = 0;

    if(nosize != 0) {
      String [] param = new String[nosize+2] ;
      param[0] = request.getParameter("newcust1") ;
      param[1] = request.getParameter("oldcust1") ;

      StringBuffer sbtemp = new StringBuffer("?") ;
      param[0+2] = (String) novector.get(0);

      if(nosize>1) {
          for(int i=1; i<nosize; i++) {
 	      sbtemp = sbtemp.append(",?");
              param[i+2] = (String) novector.get(i);
 	  }
      }
     

      List<Integer> demoList= new ArrayList<Integer>();
      for(int x=2;x<param.length;x++) {
    	  demoList.add(Integer.parseInt(param[x]));
      }
      List<DemographicCust> demographicCusts = demographicCustDao.findMultipleNurse(demoList, param[1]);
      for(DemographicCust demographicCust:demographicCusts) {
    	  demographicCust.setNurse(param[0]);
    	  demographicCustDao.merge(demographicCust);
      }
      rowsAffected = demographicCusts.size();
    } %>
<%=rowsAffected %>
<bean:message key="admin.updatedemographicprovider.msgRecords" />
<br>
<%}

  if(request.getParameter("update")!=null && request.getParameter("update").equals("UpdateMidwife") ) {
	  Integer demoNo = demographicCustDao.select_demoname2(request.getParameter("oldcust4"), request.getParameter("regexp"));
	    if (demoNo != null) {
	    	 novector.add(demoNo.toString());
	    }

    int nosize = novector.size();
    int rowsAffected = 0;

    if(nosize != 0) {
      String [] param = new String[nosize+2] ;
      param[0] = request.getParameter("newcust4") ;
      param[1] = request.getParameter("oldcust4") ;

      StringBuffer sbtemp = new StringBuffer("?") ;
      param[0+2] = (String) novector.get(0);

      if(nosize>1) {
          for(int i=1; i<nosize; i++) {
 	      sbtemp = sbtemp.append(",?");
              param[i+2] = (String) novector.get(i);
 	  }
      }
      String instrdemo = sbtemp.toString();
     
      List<Integer> demoList= new ArrayList<Integer>();
      for(int x=2;x<param.length;x++) {
    	  demoList.add(Integer.parseInt(param[x]));
      }
      List<DemographicCust> demographicCusts = demographicCustDao.findMultipleMidwife(demoList, param[1]);
      for(DemographicCust demographicCust:demographicCusts) {
    	  demographicCust.setMidwife(param[0]);
    	  demographicCustDao.merge(demographicCust);
      }
      rowsAffected = demographicCusts.size();

    } %>
<%=rowsAffected %>
<bean:message key="admin.updatedemographicprovider.msgRecords" />
<br>
<%
  }
%>


<div class="well well-small">
<table class="table table-striped  table-condensed">
	<FORM NAME="ADDAPPT" METHOD="post"
		ACTION="updatedemographicprovider.jsp" onsubmit="return(setregexp())">
	<tr>
		<td><b><bean:message
			key="admin.updatedemographicprovider.msgResident" /></b></td>
	</tr>
	<tr>
		<td><bean:message key="admin.updatedemographicprovider.formUse" />
		<select name="newcust2">
			<%
 	 for(int i=0; i<names.size(); i=i+2) {
%>
			<option value="<%=names.get(i)%>"><%=names.get(i+1)%></option>
			<%
 	 }
%>
		</select> <bean:message key="admin.updatedemographicprovider.formReplace" /> <select
			name="oldcust2">
			<%
 	 for(int i=0; i<names.size(); i=i+2) {
%>
			<option value="<%=names.get(i)%>"><%=names.get(i+1)%></option>
			<%
 	 }
%>
		</select><br>
		<bean:message key="admin.updatedemographicprovider.formCondition" /> <select
			name="last_name_from">
			<%
   char cletter = 'A';
 	 for(int i=0; i<26; i++) {
%>
			<option value="<%=(char) (cletter+i) %>"><%=(char) (cletter+i)%></option>
			<%
 	 }
%>
		</select> <bean:message key="admin.updatedemographicprovider.formTo" /> <select
			name="last_name_to">
			<%
   cletter = 'A';
 	 for(int i=0; i<26; i++) {
%>
			<option value="<%=(char) (cletter+i) %>"><%=(char) (cletter+i)%></option>
			<%
 	 }
%>
		</select> <br>
		<INPUT TYPE="hidden" NAME="regexp" VALUE=""> <input
			type="hidden" name="update" value=" Go "> <INPUT class="btn btn-primary"
			TYPE="submit"
			VALUE="<bean:message key="global.update"/>">


		</td>
	</tr>
	</form>
</table>
</div>

<!-- for nurse -->
<div class="well well-small">
<table class="table table-striped  table-condensed">
	<FORM NAME="ADDAPPT1" METHOD="post"
		ACTION="updatedemographicprovider.jsp" onsubmit="return(setregexp1())">
	<tr>
		<td><b><bean:message
			key="admin.updatedemographicprovider.msgNurse" /></b></td>
	</tr>
	<tr>
		<td><bean:message key="admin.updatedemographicprovider.formUse" />
		<select name="newcust1">
			<%
 	 for(int i=0; i<names.size(); i=i+2) {
%>
			<option value="<%=names.get(i)%>"><%=names.get(i+1)%></option>
			<%
 	 }
%>
		</select> <bean:message key="admin.updatedemographicprovider.formReplace" /> <select
			name="oldcust1">
			<%
 	 for(int i=0; i<names.size(); i=i+2) {
%>
			<option value="<%=names.get(i)%>"><%=names.get(i+1)%></option>
			<%
 	 }
%>
		</select><br>
		<bean:message key="admin.updatedemographicprovider.formCondition" /> <select
			name="last_name_from">
			<%
   cletter = 'A';
 	 for(int i=0; i<26; i++) {
%>
			<option value="<%=(char) (cletter+i) %>"><%=(char) (cletter+i)%></option>
			<%
 	 }
%>
		</select> <bean:message key="admin.updatedemographicprovider.formTo" /> <select
			name="last_name_to">
			<%
   cletter = 'A';
 	 for(int i=0; i<26; i++) {
%>
			<option value="<%=(char) (cletter+i) %>"><%=(char) (cletter+i)%></option>
			<%
 	 }
%>
		</select> <br>
		<INPUT TYPE="hidden" NAME="regexp" VALUE=""> <input
			type="hidden" name="update" value=" Submit "> <INPUT class="btn btn-primary"
			TYPE="submit"
			VALUE="<bean:message key="global.update"/>">
		</td>
	</tr>
	</form>
</table>
</div>

<!-- for midwife -->
<div class="well well-small">
<table class="table table-striped  table-condensed">
	<FORM NAME="ADDAPPT2" METHOD="post"
		ACTION="updatedemographicprovider.jsp" onsubmit="return(setregexp2())">
	<tr>
		<td><b><bean:message key="admin.updatedemographicprovider.msgMidwife" /></b></td>
	</tr>
	<tr>
		<td><bean:message key="admin.updatedemographicprovider.formUse" />
		<select name="newcust4">
			<%
 	 for(int i=0; i<names.size(); i=i+2) {
%>
			<option value="<%=names.get(i)%>"><%=names.get(i+1)%></option>
			<%
 	 }
%>
		</select> <bean:message key="admin.updatedemographicprovider.formReplace" /> <select
			name="oldcust4">
			<%
 	 for(int i=0; i<names.size(); i=i+2) {
%>
			<option value="<%=names.get(i)%>"><%=names.get(i+1)%></option>
			<%
 	 }
%>
		</select><br>
		<bean:message key="admin.updatedemographicprovider.formCondition" /> <select
			name="last_name_from">
			<%
   cletter = 'A';
 	 for(int i=0; i<26; i++) {
%>
			<option value="<%=(char) (cletter+i) %>"><%=(char) (cletter+i)%></option>
			<%
 	 }
%>
		</select> <bean:message key="admin.updatedemographicprovider.formTo" /> <select
			name="last_name_to">
			<%
   cletter = 'A';
 	 for(int i=0; i<26; i++) {
%>
			<option value="<%=(char) (cletter+i) %>"><%=(char) (cletter+i)%></option>
			<%
 	 }
%>
		</select> <br>
		<INPUT TYPE="hidden" NAME="regexp" VALUE=""> <input
			type="hidden" name="update" value="UpdateMidwife"> <INPUT class="btn btn-primary"
			TYPE="submit"
			VALUE="<bean:message key="global.update"/>">
		</td>
	</tr>
	</form>
</table>
</div>


</div>
</body>
</html:html>
