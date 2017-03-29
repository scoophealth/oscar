<%--

    Copyright (c) 2006-. OSCARservice, OpenSoft System. All Rights Reserved.
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

--%>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
      String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
      boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_admin.billing,_admin" rights="w" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect(request.getContextPath() + "/securityError.jsp?type=_admin&type=_admin.billing");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>

<%@ page import="java.math.*, java.util.*, java.io.*, java.sql.*, oscar.*, java.net.*,oscar.MyDateFormat"%>

<%@ page import="org.oscarehr.util.SpringUtils" %>
<%@ page import="org.oscarehr.common.model.CtlBillingService" %>
<%@ page import="org.oscarehr.common.dao.CtlBillingServiceDao" %>
<%
	CtlBillingServiceDao ctlBillingServiceDao = SpringUtils.getBean(CtlBillingServiceDao.class);
%>
<%
String typeid = request.getParameter("typeid");
String type = request.getParameter("type");

for(CtlBillingService b:ctlBillingServiceDao.findByServiceType(typeid)) {
	ctlBillingServiceDao.remove(b.getId());
}

String[] group = new String[4];
int rowsAffected = -100;

for(int j=1;j<4;j++){
	group[j] = request.getParameter("group"+j);

	for (int i=0; i<20; i++){
		if(request.getParameter("group"+j+"_service"+i).length() !=0){
			CtlBillingService cbs = new CtlBillingService();
			cbs.setServiceTypeName(type);
			cbs.setServiceType(typeid);
			cbs.setServiceCode(request.getParameter("group"+j+"_service"+i));
			cbs.setServiceGroupName(group[j]);
			cbs.setServiceGroup("Group"+j);
			cbs.setStatus("A");
			cbs.setServiceOrder(Integer.parseInt(request.getParameter("group"+j+"_service"+i+"_order")));
		    ctlBillingServiceDao.persist(cbs);
		}
	}
}

response.sendRedirect("manageBillingform.jsp");
%>
