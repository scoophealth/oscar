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
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<script type="text/javascript">
function setToFrom(dateFrom, dateTo){
	document.getElementById('from').value = dateFrom;
	document.getElementById('to').value = dateTo;
	document.getElementById('tofromForm').submit();
}
</script>
<%


java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
java.util.Calendar cal =  java.util.Calendar.getInstance();
java.util.Date now = cal.getTime();
cal.add(java.util.Calendar.MONTH,-1);
java.util.Date month1 = cal.getTime();
cal.add(java.util.Calendar.MONTH,-2);
java.util.Date month3 = cal.getTime();
cal.add(java.util.Calendar.MONTH,-3);
java.util.Date month6 = cal.getTime();
cal.add(java.util.Calendar.MONTH,-6);
java.util.Date month12 = cal.getTime();

%>

<a onclick="setToFrom('<%=sdf.format(month1)%>','<%=sdf.format(now)%>')"> Last 30 Days </a> - 
<a onclick="setToFrom('<%=sdf.format(month3)%>','<%=sdf.format(now)%>')"> Last 3 Months </a> - 
<a onclick="setToFrom('<%=sdf.format(month6)%>','<%=sdf.format(now)%>')"> Last 6 Months </a> -
<a onclick="setToFrom('<%=sdf.format(month12)%>','<%=sdf.format(now)%>')"> Last 12 Months </a> -
<a onclick="setToFrom('','')"> Clear </a> 

<html:form action="${request.contextPath}/oscarEncounter/myoscar/measurements_${param.sourcePage}.do" styleId="tofromForm" styleClass="form-inline">
  
  <div class="form-group">
  	<html:hidden name="EctMyOscarFilterForm" property="type" value="${param.sourcePage}" />
	<html:hidden name="EctMyOscarFilterForm" property="demoNo" value="${param.demoNo}" />
	<div class="input-group">
		<div class="input-group-addon">From</div>
		<html:text styleId="from" name="EctMyOscarFilterForm" property="from" value="${param.from}" styleClass="form-control" />
	</div> 
  </div>
  <div class="form-group">
 	<div class="input-group">
	<div class="input-group-addon">To</div>
	<html:text styleId="to" name="EctMyOscarFilterForm" property="to" value="${param.to}" styleClass="form-control"/>
	</div>
  </div>
  <div class="form-group">
	<html:submit value="Filter" styleClass="btn btn-default"/>
  </div>
</html:form>
