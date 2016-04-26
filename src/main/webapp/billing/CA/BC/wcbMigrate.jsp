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
<security:oscarSec roleName="<%=roleName$%>" objectName="_admin.billing,_admin" rights="w" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../../../securityError.jsp?type=_admin&type=_admin.billing");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>

<%@page import="org.oscarehr.util.DbConnectionFilter,java.sql.*" %>
<%@page import="org.oscarehr.util.SpringUtils" %>
<%@page import="org.oscarehr.common.dao.BillingServiceDao" %>
<%@page import="org.oscarehr.common.model.BillingService" %>
<%@page import="org.oscarehr.billing.CA.BC.dao.WcbDao" %>
<%@page import="org.oscarehr.billing.CA.BC.model.Wcb" %>
<%@page import="oscar.oscarBilling.ca.bc.data.BillingmasterDAO" %>
<%@page import="oscar.entities.Billingmaster" %>
<%
	BillingServiceDao billingServiceDao = SpringUtils.getBean(BillingServiceDao.class);
    WcbDao wcbDao = SpringUtils.getBean(WcbDao.class);
    BillingmasterDAO billingmasterDao = SpringUtils.getBean(BillingmasterDAO.class);
    		   
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <title>Migrating WCB old to new</title>
    </head>
    <body>
        <h1>Migrating WCB old to new</h1>




        <%
        Connection c = DbConnectionFilter.getThreadLocalDbConnection(); //select only
        try {
            PreparedStatement ps = c.prepareStatement("select * from wcb where billing_no = ?");
            PreparedStatement ps_findunlinkedWcbBills = c.prepareStatement("select * from billing,billingmaster where billing.billing_no = billingmaster.billing_no  and  billingtype = 'WCB' and wcb_id is null");
            ResultSet rs = ps_findunlinkedWcbBills.executeQuery();

            //For each bill that is of type WCB and has a wcb_id of null (or blank?)
            //Get WCB record for that billing_no
            //SET WCB id in billing_no
            //

            while (rs.next()) {
                int billingNo = rs.getInt("billing_no");
                String billingmaster = rs.getString("billingmaster_no");
                ps.setInt(1, billingNo);
                ResultSet wcbResult = ps.executeQuery();
                if (wcbResult.next()) {
                    String wcbId = wcbResult.getString("ID");
                    String bill_amount = wcbResult.getString("bill_amount");
                    String dob = wcbResult.getString("w_dob");
                    if (dob != null) {
                        dob = dob.replaceAll("-", "");
                    }
                    //w_phn
                    Date w_servicedate = wcbResult.getDate("w_servicedate");
                    String w_icd9 = wcbResult.getString("w_icd9");
                    String w_payeeno = wcbResult.getString("w_payeeno");

                    String w_pracno = wcbResult.getString("w_pracno");
                    String feeItem = wcbResult.getString("w_feeitem");
                    String sLocation = wcbResult.getString("w_servicelocation");

                   
                    int inResult = 0;
           			for(Billingmaster bm : billingmasterDao.getBillingMasterByBillingNo(String.valueOf(billingNo))) {
           				bm.setWcbId(Integer.parseInt(wcbId));
           				bm.setBillAmount(bill_amount);
           				bm.setBillingCode(feeItem);
           				bm.setServiceLocation(sLocation);
           				bm.setBirthDate(dob);
           				bm.setPayeeNo(w_payeeno);
           				bm.setPractitionerNo(w_pracno);
           				
           				billingmasterDao.update(bm);
           				inResult++;
           			}
           			

        %>

                ID= <%=wcbId%> &nbsp;&nbsp;
                Amount= <%=bill_amount%> &nbsp;&nbsp;
                DOB= <%=dob%> &nbsp;&nbsp;

                Service Date= <%=w_servicedate%> &nbsp;&nbsp;
                icd9= <%=w_icd9%> &nbsp;&nbsp;
                payee= <%=w_payeeno%> &nbsp;&nbsp;

                prac= <%=w_pracno%> &nbsp;&nbsp;
                fee= <%=feeItem%> &nbsp;&nbsp;
                location= <%=sLocation%> &nbsp;&nbsp;
                billing_no = <%=billingNo%> &nbsp;&nbsp;
                billingmaster_no= <%=billingmaster%> &nbsp;&nbsp;

        <%=inResult%>
               <br>
        <%
             }
         }

     } finally {
         c.close();
     }
        %>



    </body>
</html>
