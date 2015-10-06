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
<security:oscarSec roleName="<%=roleName$%>" objectName="_report,_admin.reporting,_admin" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../../../securityError.jsp?type=_report&type=_admin.reporting&type=_admin");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>

<%@ page import="java.util.*, java.sql.*, oscar.oscarBilling.ca.bc.MSP.*,oscar.*"%>
<%@ include file="../../../admin/dbconnection.jsp"%>

<%@ page import="org.oscarehr.util.SpringUtils" %>
<%@ page import="org.oscarehr.billing.CA.model.BillActivity" %>
<%@ page import="org.oscarehr.billing.CA.dao.BillActivityDao" %>
<%@ page import="org.oscarehr.common.model.Provider" %>
<%@ page import="org.oscarehr.PMmodule.dao.ProviderDao" %>
<%@page import="oscar.util.ConversionUtils" %>
<%
	BillActivityDao billActivityDao = SpringUtils.getBean(BillActivityDao.class);
	ProviderDao providerDao = SpringUtils.getBean(ProviderDao.class);
%>

<% GregorianCalendar now=new GregorianCalendar();
   int curYear = now.get(Calendar.YEAR);
   int bCount = 1, vsFlag = 0;
   String batchCount = "0";
   String oscar_home= oscarVariables.getProperty("project_home")+".properties";

   String provider = request.getParameter("provider");
   String proOHIP="";
   String specialty_code;
   String billinggroup_no;
   String groupFile = "";
   if (oscar.oscarBilling.ca.bc.MSP.ExtractBean.HasBillingItemsToSubmit()){
      if (provider.compareTo("all") == 0 ){
         batchCount = "0";
         int fileCount = 0;
         for(Provider p : providerDao.getActiveProviders()) {
        	 if(p.getOhipNo() == null || p.getOhipNo().isEmpty()) {
        		 continue;
        	 }
         
            proOHIP = p.getOhipNo();
            billinggroup_no= p.getBillingNo();
            specialty_code = SxmlMisc.getXmlContent(p.getComments(),"<xml_p_specialty_code>","</xml_p_specialty_code>");

            if (bCount == 1) {
              
               List<BillActivity> bas = billActivityDao.findCurrentByMonthCodeAndGroupNo(request.getParameter("monthCode"),billinggroup_no,ConversionUtils.fromDateString(curYear+"-01-01"));
               for(BillActivity ba:bas){
                  batchCount = String.valueOf(ba.getBatchCount());
               }
               fileCount = Integer.parseInt(batchCount) + 1;
               batchCount = String.valueOf(fileCount);
            } else {
                  batchCount = batchCount;
            }

            if (specialty_code == null || specialty_code.compareTo("") == 0 || specialty_code.compareTo("null")==0){
               specialty_code = "00";
            }
            if ( billinggroup_no == null ||  billinggroup_no.compareTo("") == 0 ||  billinggroup_no.compareTo("null")==0){
               billinggroup_no = "0000";
            }
            ExtractBean extract = new ExtractBean();
            extract.seteFlag("1");
            extract.setVSFlag(vsFlag);
            extract.setOscarHome(oscar_home);
            extract.setOhipVer(request.getParameter("verCode"));
            extract.setProviderNo(proOHIP);
            extract.setOhipCenter(request.getParameter("billcenter"));
            extract.setGroupNo(billinggroup_no);
            extract.setSpecialty(specialty_code);
            extract.setBatchCount(String.valueOf(bCount));
            extract.dbQuery();

            vsFlag = vsFlag +1;
            int fLength = 3 - batchCount.length();
            String zero ="";
            if (fLength == 1) zero = "0";
            if (fLength == 2) zero = "00";

    		BillActivity ba = new BillActivity();
    		ba.setMonthCode(request.getParameter("monthCode"));
    		ba.setBatchCount(Integer.parseInt(batchCount));
    		ba.setHtmlFilename("H" + request.getParameter("monthCode") + proOHIP + "_" + zero +  batchCount + ".htm");
    		ba.setOhipFilename("H" + request.getParameter("monthCode") + billinggroup_no + "." + zero + batchCount);
    		ba.setProviderOhipNo(proOHIP);
    		ba.setGroupNo(billinggroup_no);
    		ba.setCreator(request.getParameter("curUser"));
    		ba.setHtmlContext(extract.getHtmlCode());
    		ba.setOhipContext(extract.getValue());
    		ba.setClaimRecord(extract.getOhipClaim()+"/"+extract.getOhipRecord());
    		ba.setUpdateDateTime(new java.util.Date());
    		ba.setStatus("A");
    		ba.setTotal(extract.getTotalAmount());
    		billActivityDao.persist(ba);


            int rowsAffected = 1;

            extract.setHtmlFilename("H" + request.getParameter("monthCode") +proOHIP + "_" + zero + batchCount+".htm");
            extract.setOhipFilename("H" + request.getParameter("monthCode") + billinggroup_no + "." + zero + batchCount);
            String filecontext = extract.getValue();
            String htmlcontext = extract.getHtmlCode();
            // extract.writeFile(filecontext);
            extract.writeHtml(htmlcontext);
            groupFile = groupFile + filecontext ;
            bCount = bCount + 1;
            extract.writeFile(groupFile);
            //groupFile =  groupFile+"\n"  ;
         }
      }else {
         batchCount = "0";
         int fileCount = 0;
        
         String providerBillingNo = request.getParameter("provider");
         providerBillingNo = providerBillingNo.substring(0, providerBillingNo.indexOf(",")).trim();
         
         for(Provider p : providerDao.getBillableProvidersByOHIPNo(providerBillingNo)) {
        
            proOHIP = p.getOhipNo();
            billinggroup_no= p.getBillingNo();
            specialty_code = SxmlMisc.getXmlContent(p.getComments(),"<xml_p_specialty_code>","</xml_p_specialty_code>");

            if (bCount == 1) {
            	List<BillActivity> bas = billActivityDao.findCurrentByMonthCodeAndGroupNo(request.getParameter("monthCode"),billinggroup_no,ConversionUtils.fromDateString(curYear+"-01-01"));
                for(BillActivity ba:bas){
                   batchCount = String.valueOf(ba.getBatchCount());
                }
                
               fileCount = Integer.parseInt(batchCount) + 1;
               batchCount = String.valueOf(fileCount);
            }else{
               batchCount = batchCount;
            }

            if (specialty_code == null || specialty_code.compareTo("") == 0 || specialty_code.compareTo("null")==0){
               specialty_code = "00";
            }
            if ( billinggroup_no == null ||  billinggroup_no.compareTo("") == 0 ||  billinggroup_no.compareTo("null")==0){
               billinggroup_no = "0000";
            }
            ExtractBean extract = new ExtractBean();
                        extract.setOscarHome(oscar_home);
                        extract.seteFlag("1");
                        extract.setOhipVer(request.getParameter("verCode"));
                        extract.setProviderNo(proOHIP);
                        extract.setOhipCenter(request.getParameter("billcenter"));
                        extract.setGroupNo(billinggroup_no);
                        extract.setSpecialty(specialty_code);
                        extract.setBatchCount(String.valueOf(bCount));
                        extract.dbQuery();


            int fLength = 3 - batchCount.length();
            String zero ="";
            if (fLength == 1) zero = "0";
            if (fLength == 2) zero = "00";

    		BillActivity ba = new BillActivity();
    		ba.setMonthCode(request.getParameter("monthCode"));
    		ba.setBatchCount(Integer.parseInt(batchCount));
    		ba.setHtmlFilename("H" + request.getParameter("monthCode") + proOHIP + "_" + zero +  batchCount + ".htm");
    		ba.setOhipFilename("H" + request.getParameter("monthCode") + billinggroup_no + "." + zero + batchCount);
    		ba.setProviderOhipNo(proOHIP);
    		ba.setGroupNo(billinggroup_no);
    		ba.setCreator(request.getParameter("curUser"));
    		ba.setHtmlContext(extract.getHtmlCode());
    		ba.setOhipContext(extract.getValue());
    		ba.setClaimRecord(extract.getOhipClaim()+"/"+extract.getOhipRecord());
    		ba.setUpdateDateTime(new java.util.Date());
    		ba.setStatus("A");
    		ba.setTotal(extract.getTotalAmount());
    		billActivityDao.persist(ba);


            int rowsAffected = 1;

            extract.setHtmlFilename("H" + request.getParameter("monthCode") +proOHIP + "_" + zero + batchCount+".htm");
            extract.setOhipFilename("H" + request.getParameter("monthCode") + billinggroup_no  + "." + zero + batchCount);
            String filecontext = extract.getValue();
            String htmlcontext = extract.getHtmlCode();
            extract.writeFile(filecontext);
            extract.writeHtml(htmlcontext);
         }
      }
   }
 %>


<jsp:forward page='billingTeleplanGroupReport.jsp'>
	<jsp:param name="year" value='' />
</jsp:forward>
