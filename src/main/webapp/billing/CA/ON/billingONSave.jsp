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
<security:oscarSec roleName="<%=roleName$%>" objectName="_billing" rights="w" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect(request.getContextPath() + "/securityError.jsp?type=_billing");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="java.util.*,java.sql.*,oscar.util.*,oscar.oscarBilling.ca.on.pageUtil.*,oscar.oscarBilling.ca.on.data.*,oscar.oscarProvider.data.*,java.math.* ,oscar.oscarBilling.ca.on.administration.*"%>
<%@ page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@ page import="org.springframework.web.context.WebApplicationContext"%>
<%@ page errorPage="errorpage.jsp" import="java.util.*"%>
<%@ page import="oscar.oscarBilling.ca.on.pageUtil.*"%>
<%@ page import="oscar.oscarBilling.ca.on.data.*,org.oscarehr.common.model.*,org.oscarehr.common.dao.*"%>
<%@ page import="org.oscarehr.util.SpringUtils" %>
<%@page import="org.oscarehr.util.LoggedInInfo"%>
<%@page import="oscar.oscarPrevention.PreventionData" %>

<%
	WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
    UserPropertyDAO userPropertyDAO = (UserPropertyDAO) ctx.getBean("UserPropertyDAO");
    BillingONCHeader1Dao cheader1Dao = (BillingONCHeader1Dao) SpringUtils.getBean("billingONCHeader1Dao");
    BillingONExtDao extDao = (BillingONExtDao) SpringUtils.getBean("billingONExtDao");
    LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);

  

	//String user_no = (String) session.getAttribute("user");
	String apptNo = request.getParameter("appointment_no");

	if (request.getParameter("submit") != null && "Back to Edit".equals(request.getParameter("button"))) { %>
		<jsp:forward page="billingON.jsp" />
<%	}

	// save the billing if needed
	boolean ret = false;
	if (request.getParameter("submit") != null
			&& ("Settle & Print Invoice".equals(request.getParameter("submit"))
			    || "Save & Print Invoice".equals(request.getParameter("submit"))
			    || "Save".equals(request.getParameter("submit"))
			    || "Save and Back".equals(request.getParameter("submit"))
			    || "Save & Add Another Bill".equals(request.getParameter("submit")))) {
		BillingSavePrep bObj = new BillingSavePrep();
		Vector vecObj = bObj.getBillingClaimObj(request);
		ret = bObj.addABillingRecord(loggedInInfo, vecObj);
		if(request.getParameter("xml_billtype").substring(0,3).matches(BillingDataHlp.BILLINGMATCHSTRING_3RDPARTY)) {
			bObj.addPrivateBillExtRecord(request, vecObj);
		} else {
			// add transaction log here for ohip
			bObj.addOhipInvoiceTrans(vecObj);
		}
		int billingNo = bObj.getBillingId();
		String demographic_no="";	
		String time="";
		String value="";
		value=request.getParameter("payeename");		
        if(value.trim()=="")
        {
        	value=request.getParameter("payeename1");
        }

        BillingONCHeader1 billing = cheader1Dao.find(billingNo);
        if(billing!=null) {
        	demographic_no = billing.getDemographicNo().toString();
        	//time = billing.getTimestamp();
        	
        	BillingONExt ext = new BillingONExt();
        	ext.setBillingNo(billingNo);
        	ext.setDemographicNo(billing.getDemographicNo());
        	ext.setKeyVal("payee");
        	ext.setValue(value);
        	ext.setDateTime(billing.getTimestamp());
        	extDao.persist(ext);
        	
        }
        
     	// for prevention billing (after adding prevention, it will pop-up a billing page automatically if ENABLE_PREVENTION_BILLING was set to true)
  		if (org.oscarehr.common.IsPropertiesOn.propertiesOn("ENABLE_PREVENTION_BILLING")) {
  			int prevId = 0;
  			try {
  				prevId = Integer.valueOf(request.getParameter("prevId"));
  			} catch (Exception e) {}
  			if (prevId != 0) {
  				// add billed property to prevetions table
  				String billed = PreventionData.getExtValue(Integer.toString(prevId), "billed");
  				if (billed != null && !billed.isEmpty()) {
  					PreventionData.updatetExtValue(prevId, "billed", "1");
  				} else {
  					PreventionData.addPreventionKeyValue(Integer.toString(prevId), "billed", "1");
  				}
  			}
  		}
        
		// update appt and close the page
		if (ret) {
			if (apptNo != null && apptNo.length() > 0 && !apptNo.equals("0")) {
				String apptCurStatus = bObj.getApptStatus(apptNo);
				oscar.appt.ApptStatusData as = new oscar.appt.ApptStatusData();
				String billStatus = as.billStatus(apptCurStatus);
				bObj.updateApptStatus(apptNo, billStatus, (String)session.getAttribute("user"));
			}
					
				//if you are editing previous billing, the previous billing should be deleted(flag "D") after edit (insert a new billing)�      
		%>
			
			<jsp:include page="billingDeleteWithBillNo.jsp"/><!-- TODO: look at handling of actions from different submits -->
			<!-- let billingDeleteWithBillNo.jsp handle messages at this point to prevent duplicate or contradicting msgs-->  
			
		<% if (request.getParameter("submit") != null && "Save & Add Another Bill".equals(request.getParameter("submit"))) { %>
			<script LANGUAGE="JavaScript">
				self.opener.refresh();  
				self.location.href="<%=request.getParameter("url_back")%>";
			</script>
		<% }

		if (request.getParameter("submit") != null && "Save".equals(request.getParameter("submit"))) { %>
			<script LANGUAGE="JavaScript">
				self.opener.refresh();  
			</script>
		<% }

		if(!"Settle & Print Invoice".equals(request.getParameter("submit")) && !"Save & Print Invoice".equals(request.getParameter("submit"))) { %>
			<a href="billingON3rdInv.jsp?billingNo=<%=billingNo%>"> Print invoice</a>
		<% } %>


		<% if(!"Settle & Print Invoice".equals(request.getParameter("submit")) && !"Save & Print Invoice".equals(request.getParameter("submit"))) { %>
<script LANGUAGE="JavaScript">
                                            
        <% 
            //GET WORKLOAD MANAGEMENT SCREEN IF THERE IS ANY
            String curBilf = request.getParameter("curBillForm");
            
            String provider = (String) request.getSession().getAttribute("user");
            UserProperty prop = userPropertyDAO.getProp(provider, UserProperty.WORKLOAD_MANAGEMENT);

            String wrkloadmanagement =  prop!= null ? prop.getValue() : null;
            if ( wrkloadmanagement != null && !wrkloadmanagement.equals("") && !wrkloadmanagement.equals(curBilf) ){ 
        	    ///NEED TO CHECK IF THIS IS THE CURRENT FORM IF SO LET IT CLOSE!!!
            	String urlBack = request.getParameter("url_back")+"&curBillForm="+wrkloadmanagement;

        %>
            self.opener.refresh(); 
            self.location.href="<%=urlBack%>";
        
       	 <%}else{%>       	 
					    self.close(); 
					    
					    <% if(!"Save".equals(request.getParameter("submit"))) { %>
 					    self.opener.refresh();
 					    <% } %>
         <% }%>
</script>
	<% } else { %>

<script LANGUAGE="JavaScript">
	function popupPage(vheight,vwidth,varpage) {
	  var page = "" + varpage;
	  windowprops = "height="+vheight+",width="+vwidth+",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes,screenX=0,screenY=0,top=0,left=0";
	  var popup=window.open(page, "billcorrection", windowprops);
	    if (popup != null) {
	    if (popup.opener == null) {
	      popup.opener = self;
	    }
	    popup.focus();
	  }
	}
	popupPage(700,720,'billingON3rdInv.jsp?billingNo=<%=billingNo%>');
	self.close();
	self.opener.refresh();
</script>
<% } %>

<%} else { %>

<h1>Sorry, billing has failed. Please do it again!</h1>

<%}
}
%>


