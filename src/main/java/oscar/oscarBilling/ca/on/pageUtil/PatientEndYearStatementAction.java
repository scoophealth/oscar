/**
 * Copyright (c) 2006-. OSCARservice, OpenSoft System. All Rights Reserved.
 * This software is published under the GPL GNU General Public License.
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 */

package oscar.oscarBilling.ca.on.pageUtil;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.oscarehr.PMmodule.utility.Utility;
import org.oscarehr.common.dao.BillingONCHeader1Dao;
import org.oscarehr.common.dao.BillingONItemDao;
import org.oscarehr.common.model.BillingONCHeader1;
import org.oscarehr.common.model.BillingONItem;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.managers.DemographicManager;
import org.oscarehr.util.DbConnectionFilter;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.SpringUtils;

import oscar.OscarAction;
import oscar.OscarDocumentCreator;

/**
 *
 * @author Eugene Katyukhin
 */
public class PatientEndYearStatementAction extends OscarAction {
	
	private static final Logger _logger = Logger.getLogger(BillingStatusPrep.class);
	private static final String RES_SUCCESS = "success";
	private static final String RES_FAILURE = "failure";
	
	private DemographicManager demographicManager = SpringUtils.getBean(DemographicManager.class); 
	
      
   public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)  {
	   
	   PatientEndYearStatementForm statement = (PatientEndYearStatementForm)form;
	   List<PatientEndYearStatementInvoiceBean> result = null;
	   PatientEndYearStatementBean summary = new PatientEndYearStatementBean("", "", 0, "", "", "", new Date(), new Date(), "","");
	   ActionMessages errors = this.getErrors(request);
	   LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);
	   
	   
	   if(request.getParameter("search") != null || request.getParameter("pdf") != null) {

		   request.setAttribute("fromDateParam",statement.getFromDateParam());
		   request.setAttribute("toDateParam",statement.getToDateParam());
		   Date fromDate = statement.getFromDate();
		   Date toDate = statement.getToDate();
		   SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd"); 

		   if(request.getParameter("search") != null) {
			   List<Demographic> demographicList = new ArrayList<Demographic>();
			   if(statement.getDemographicNoParam() != null && statement.getDemographicNoParam().length()>0) {
				   Demographic d =demographicManager.getDemographic(LoggedInInfo.getLoggedInInfoFromSession(request), statement.getDemographicNoParam());
				   if(d!=null) {
					   demographicList.add(d);
				   }
			   } else {
				   demographicList = demographicManager.searchDemographic(LoggedInInfo.getLoggedInInfoFromSession(request), statement.getLastNameParam()+","+statement.getFirstNameParam());
					  
			   }
			   if(demographicList == null || demographicList.size()==0) {
				   errors.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage("error.billingReport.invalidPatientName"));
				   saveErrors(request,errors);
				   _logger.error("Failed to find patient name: "+statement.getFirstNameParam()+","+statement.getLastNameParam());
				   return mapping.findForward(RES_FAILURE);			   			   
			   }
			   if(demographicList.size()>1) {
				   errors.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage("error.billingReport.notSelectivePatientName"));
				   saveErrors(request,errors);
				   _logger.error("Patient name is not selective enough: "+statement.getFirstNameParam()+","+statement.getLastNameParam());
				   return mapping.findForward(RES_FAILURE);			   
			   }
			   Demographic demographic = demographicList.get(0);
			   summary.setPatientNo(demographic.getDemographicNo().toString());
                           summary.setPatientName(demographic.getFormattedName());
			   summary.setHin(demographic.getHin());
			   summary.setAddress(demographic.getAddress()+" "+demographic.getCity()+" "+demographic.getProvince());
			   summary.setPhone(demographic.getPhone()+" "+demographic.getPhone2());
			   request.setAttribute("summary", summary);
			   
			   double totalInvoiced = 0;
			   double totalPaid = 0;
			   int invoiceCount = 0;
			   
			   BillingONCHeader1Dao hDao = SpringUtils.getBean(BillingONCHeader1Dao.class);
			   BillingONItemDao iDao = SpringUtils.getBean(BillingONItemDao.class);
			   try {
				   for(Object[] o : hDao.findBillingsAndDemographicsByDemoIdAndDates(demographic.getDemographicNo(), "PAT", fromDate, toDate)) {
					   BillingONCHeader1 bch = (BillingONCHeader1) o[0];
					   Demographic d = (Demographic) o[1];
						
					   result = new ArrayList<PatientEndYearStatementInvoiceBean>();
					   
					   double paid = bch.getPaid().doubleValue();
					   double invoiced = bch.getTotal().doubleValue();
					   PatientEndYearStatementInvoiceBean bean = new PatientEndYearStatementInvoiceBean(bch.getId(), bch.getBillingDate(), String.valueOf(invoiced), String.valueOf(paid) );
					   
					   List<PatientEndYearStatementServiceBean> services = null;
					   try {
						   services = new ArrayList<PatientEndYearStatementServiceBean>();
						   for(BillingONItem bi : iDao.findByCh1Id(bch.getId())) {
							   String fee = Utility.toCurrency(bi.getFee());
							   PatientEndYearStatementServiceBean serviceBean = 
								   new PatientEndYearStatementServiceBean(bi.getServiceCode(),fee );
							   services.add(serviceBean);
						   }
					   } catch (Exception e) {
						   _logger.error("error",e);
						   
						   errors.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage("errors.billing.ca.on.database", "SQL error"));
						   saveErrors(request,errors);
						   return mapping.findForward(RES_FAILURE);
					   }
					   
					   bean.setServices(services);
					   result.add(bean);
					   totalInvoiced += invoiced;
					   totalPaid += paid;
					   invoiceCount += 1;
					   request.setAttribute("result", result);
				   }
				   
				   summary.setInvoiced(Utility.toCurrency(totalInvoiced));
				   summary.setPaid(Utility.toCurrency(totalPaid));
				   summary.setCount(Integer.toString(invoiceCount));
                                   summary.setFromDate(fromDate);
                                   summary.setToDate(toDate);
                                   request.getSession().setAttribute("summary", summary);
			   } catch (Exception e) {
				   _logger.error("error", e);
				   
				   errors.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage("errors.billing.ca.on.database", "SQL error"));
				   saveErrors(request,errors);
				   return mapping.findForward(RES_FAILURE);
			   }

		   } else if(request.getParameter("pdf") != null) {
			   summary = (PatientEndYearStatementBean) request.getSession().getAttribute("summary");
			   OscarDocumentCreator osc = new OscarDocumentCreator();
			   String docFmt = "pdf";
			   
			   HashMap<String,Object> reportParams = new HashMap<String,Object>();
			   reportParams.put("patientId", summary.getPatientNo());
			   reportParams.put("patientName", summary.getPatientName());
			   reportParams.put("hin", summary.getHin());
			   reportParams.put("address", summary.getAddress());
			   reportParams.put("phone", summary.getPhone());
			   reportParams.put("fromDate", statement.getFromDateParam());
                           reportParams.put("toDate", statement.getToDateParam());
			   reportParams.put("invoiceCount", summary.getCount());
			   reportParams.put("totalInvoiced", summary.getInvoiced());
			   reportParams.put("totalPaid", summary.getPaid());
			   reportParams.put("fromDate", statement.getFromDateParam());
                           reportParams.put("toDate", statement.getToDateParam());
			   reportParams.put("SUBREPORT_DIR", "/oscar/oscarBilling/ca/on/reports/");
                           
			   ServletOutputStream outputStream = getServletOstream(response);

			   //open corresponding Jasper Report Definition
			   InputStream reportInstream = osc.getDocumentStream("/oscar/oscarBilling/ca/on/reports/" + "end_year_statement_report.jrxml");
			   try {
				   //COnfigure Reponse Header
				   cfgHeader(response, "end_year_statement_report.pdf", docFmt);
	                           //Fill document with report parameter data
				   Connection dbConn = null;
				   try {
					   dbConn = DbConnectionFilter.getThreadLocalDbConnection();
				   } catch (SQLException ex) {
					   errors.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage("errors.billing.ca.on.database", "Database access error"));
					   saveErrors(request,errors);
					   _logger.error("Can't get db connection",ex);
					   return mapping.findForward(RES_FAILURE);		   				
				   }
				   if(dbConn != null) {
					   osc.fillDocumentStream(reportParams, outputStream, docFmt, reportInstream, dbConn);
				   }
				   return null;	   
			   }
			   finally {
				   IOUtils.closeQuietly(reportInstream);
			   }
		   } 
	   } else if (request.getParameter("demosearch") != null) {
		   request.getSession().setAttribute("summary", null);			   
		   
		   List<Demographic> demographicList = new ArrayList<Demographic>();
		   if(request.getParameter("demographic_no") != null && request.getParameter("demographic_no").length() > 0) {
			   Demographic d =demographicManager.getDemographic(loggedInInfo, request.getParameter("demographic_no"));
			   if(d!=null) {
				   demographicList.add(d);
			   }
		   } else {
			   demographicList = demographicManager.searchDemographic(loggedInInfo, statement.getLastNameParam()+","+statement.getFirstNameParam());
				  
		   }
		   
		   if(demographicList == null || demographicList.size()==0) {
			   errors.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage("error.billingReport.invalidPatientName"));
			   saveErrors(request,errors);
			   _logger.error("Failed to find patient name: "+statement.getFirstNameParam()+","+statement.getLastNameParam());
			   return mapping.findForward(RES_FAILURE);			   			   
		   }
		   if(demographicList.size()>1) {
			   errors.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage("error.billingReport.notSelectivePatientName"));
			   saveErrors(request,errors);
			   _logger.error("Patient name is not selective enough: "+statement.getFirstNameParam()+","+statement.getLastNameParam());
			   return mapping.findForward(RES_FAILURE);			   
		   }
		   Demographic demographic = demographicList.get(0);
		   summary.setPatientNo(demographic.getChartNo());
                   summary.setPatientName(demographic.getFormattedName());
		   summary.setHin(demographic.getHin());
		   summary.setAddress(demographic.getAddress()+" "+demographic.getCity()+" "+demographic.getProvince());
		   summary.setPhone(demographic.getPhone()+" "+demographic.getPhone2());
		   request.setAttribute("summary", summary);
	   } else {	   
		   request.getSession().setAttribute("summary", null);			   
	   }
	   return mapping.findForward(RES_SUCCESS);	   
   }
   
}
