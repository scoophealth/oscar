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
<%
  if(session.getAttribute("user") == null)
    response.sendRedirect("../../../logout.jsp");
%>
<%@ page
	import="java.util.*, java.sql.*, oscar.oscarBilling.ca.bc.MSP.*"%>
<%@ include file="../../../admin/dbconnection.jsp"%>
<jsp:useBean id="apptMainBean" class="oscar.AppointmentMainBean" scope="session" />
<jsp:useBean id="SxmlMisc" class="oscar.SxmlMisc" scope="session" />
<%@ include file="dbBilling.jspf"%>
<%@ page import="org.oscarehr.util.SpringUtils" %>
<%@ page import="org.oscarehr.billing.CA.model.BillActivity" %>
<%@ page import="org.oscarehr.billing.CA.dao.BillActivityDao" %>
<%
	BillActivityDao billActivityDao = SpringUtils.getBean(BillActivityDao.class);
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
         ResultSet rslocal = apptMainBean.queryResults("%", "search_provider_ohip_dt");
         while(rslocal.next()){
            proOHIP = rslocal.getString("ohip_no");
            billinggroup_no= rslocal.getString("billing_no");
            specialty_code = SxmlMisc.getXmlContent(rslocal.getString("comments"),"<xml_p_specialty_code>","</xml_p_specialty_code>");

            if (bCount == 1) {
               String[] param2 =new String[3];
                        param2[0]=request.getParameter("monthCode");
                        param2[1]=billinggroup_no;
                        param2[2]=curYear+"/01/01";
               ResultSet rslocal2;
               rslocal2 = apptMainBean.queryResults(param2, "search_billactivity_group_monthCode");
               while(rslocal2.next()){
                  batchCount = rslocal2.getString("batchcount");
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
         ResultSet rslocal;
         String providerBillingNo = request.getParameter("provider");
         providerBillingNo = providerBillingNo.substring(0, providerBillingNo.indexOf(",")).trim();
         rslocal = apptMainBean.queryResults(providerBillingNo, "search_provider_ohip_dt");
         while(rslocal.next()){
            proOHIP = rslocal.getString("ohip_no");
            billinggroup_no= rslocal.getString("billing_no");
            specialty_code = SxmlMisc.getXmlContent(rslocal.getString("comments"),"<xml_p_specialty_code>","</xml_p_specialty_code>");

            if (bCount == 1) {
               String[] param2 =new String[3];
                        param2[0]=request.getParameter("monthCode");
                        param2[1]=billinggroup_no;
                        param2[2]=curYear+"/01/01";
               ResultSet rslocal2;
               rslocal2 = apptMainBean.queryResults(param2, "search_billactivity_group_monthCode");
               while(rslocal2.next()){
                  batchCount = rslocal2.getString("batchcount");
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
