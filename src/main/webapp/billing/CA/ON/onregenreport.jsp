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

<%@page import="org.oscarehr.util.LoggedInInfo"%>
<%@page import="oscar.util.ConversionUtils"%>
<%@page import="org.oscarehr.util.DateRange"%>
<%if (session.getAttribute("user") == null)
				response.sendRedirect("../../../logout.jsp");
%>

<%@ page import="java.util.*" errorPage="errorpage.jsp"%>
<%@ page import="oscar.oscarBilling.ca.on.pageUtil.*"%>
<%@ page import="oscar.oscarBilling.ca.on.data.*"%>
<%@ page import="oscar.oscarProvider.data.ProviderBillCenter"%>


<%//
			String diskId = request.getParameter("diskId");
			String mohOffice = request.getParameter("billcenter");
			boolean useProviderMOH = "true".equals(request.getParameter("useProviderMOH"));
			String defaultMOH = mohOffice;

			//get date from JdbcBillingClamImpl.getPrevDiskCreateDate(id), getDiskCreateDate(id)
			JdbcBillingClaimImpl dateObj = new JdbcBillingClaimImpl();
			String dateBegin = dateObj.getPrevDiskCreateDate(diskId);
			dateBegin = dateBegin==null? "" : dateBegin;
			String dateEnd = dateObj.getDiskCreateDate(diskId);
			
			// solo disk
			BillingDiskCreatePrep obj = new BillingDiskCreatePrep();
			List lProvider = obj.getProvider(diskId);

			if (lProvider != null && lProvider.size() == 1 && ((BillingProviderData) lProvider.get(0)).getBillingGroupNo().equals("0000")) {
				BillingProviderData dataProvider = (BillingProviderData) lProvider.get(0);

				if (useProviderMOH) {
					ProviderBillCenter pbc = new ProviderBillCenter();
					String billCenter = pbc.getBillCenter(dataProvider.getProviderNo());
					if (billCenter != null && billCenter.length() == 1) {
						mohOffice = billCenter;						
					}
					else {
						mohOffice = defaultMOH;
					}
				}
				
				// create the billing file 
				int headerId = obj.updateBatchHeader(dataProvider, diskId, mohOffice, "1", (String) session
						.getAttribute("user"));
				String ohipFilename = obj.getOhipfilename(Integer.parseInt(diskId));
				String htmlFilename = obj.getHtmlfilename(Integer.parseInt(diskId), dataProvider.getProviderNo());
				JdbcBillingCreateBillingFile objFile = new JdbcBillingCreateBillingFile();
				objFile.setProviderNo(dataProvider.getProviderNo());
				objFile.setOhipFilename(ohipFilename);
				objFile.setHtmlFilename(htmlFilename);
				objFile.readInBillingNo();
				objFile.renameFile();
				// 
				DateRange dateRange = new DateRange(null, ConversionUtils.fromDateString(dateEnd));
				objFile.setDateRange(dateRange);
				
				objFile.createBillingFileStr(LoggedInInfo.getLoggedInInfoFromSession(request), "" + headerId, new String[] {"B"}, false, mohOffice, false, false);
				objFile.writeFile(objFile.getValue());
				objFile.writeHtml(objFile.getHtmlCode());
				// update the diskname 
				objFile.updateDisknameSum(Integer.parseInt(diskId));
			} else if (lProvider != null && lProvider.size() >= 1) {
				// group disk
				List providerNo = new Vector();
				List ohipNo = new Vector();
				String groupNo = null;
				String value = "";
				for (int i = 0; i < lProvider.size(); i++) {
					BillingProviderData dataProvider = (BillingProviderData) lProvider.get(i);
					groupNo = dataProvider.getBillingGroupNo();
					providerNo.add(dataProvider.getProviderNo());
					ohipNo.add(dataProvider.getOhipNo());
				}
				if (groupNo != null) {
					//boolean bU = obj.updateSoloDiskName(diskId, (String) session.getAttribute("user")); // need more to do

					JdbcBillingCreateBillingFile objFile = null;
					for (int i = 0; i < lProvider.size(); i++) {
						objFile = new JdbcBillingCreateBillingFile();
						BillingProviderData dataProvider = (BillingProviderData) lProvider.get(i);
						String ohipFilename = obj.getOhipfilename(Integer.parseInt(diskId));
						String htmlFilename = obj.getHtmlfilename(Integer.parseInt(diskId), dataProvider
								.getProviderNo());
						// create the billing file 
						int headerId = obj.updateBatchHeader(dataProvider, diskId, mohOffice, "" + (i + 1),
								(String) session.getAttribute("user"));
						objFile.setProviderNo(dataProvider.getProviderNo());
						objFile.setOhipFilename(ohipFilename);
						objFile.setHtmlFilename(htmlFilename);
						objFile.readInBillingNo();
						
						DateRange dateRange = new DateRange(null, ConversionUtils.fromDateString(dateEnd));
						objFile.setDateRange(dateRange);
						objFile.createBillingFileStr(LoggedInInfo.getLoggedInInfoFromSession(request), "" + headerId, new String[] {"B"}, false, mohOffice, false, false);						
						value += objFile.getValue() + "\n";
						objFile.writeHtml(objFile.getHtmlCode());
						objFile.updateDisknameSum(Integer.parseInt(diskId));
 					}
                                        objFile.renameFile();
					objFile.writeFile(value);
				}

			}

		%>

<jsp:forward page='billingONMRI.jsp'>
	<jsp:param name="year" value='' />
</jsp:forward>
