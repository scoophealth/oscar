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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Date;
import java.util.List;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.oscarehr.PMmodule.utility.Utility;
import org.oscarehr.common.dao.DemographicDao;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.util.DbConnectionFilter;
import org.oscarehr.util.SpringUtils;

import oscar.OscarDocumentCreator;
import oscar.oscarBilling.ca.on.data.BillingONDataHelp;
import oscar.OscarAction;

/**
 *
 * @author Eugene Katyukhin
 */
public class PatientEndYearStatementAction extends OscarAction {
	private static final Logger _logger = Logger.getLogger(BillingStatusPrep.class);
	private static final String RES_SUCCESS = "success";
	private static final String RES_FAILURE = "failure";
    private static final String REPORTS_PATH = "oscar/oscarBilling/ca/on/reports/";
      
   public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)  {
	   
	   PatientEndYearStatementForm statement = (PatientEndYearStatementForm)form;
	   List<PatientEndYearStatementInvoiceBean> result = null;
	   PatientEndYearStatementBean summary = new PatientEndYearStatementBean("", "", "", "", "");
	   ActionMessages errors = this.getErrors(request);

	   if(request.getParameter("search") != null || request.getParameter("pdf") != null) {

		   request.setAttribute("fromDateParam",statement.getFromDateParam());
		   request.setAttribute("toDateParam",statement.getToDateParam());
		   Date fromDate = statement.getFromDate();
		   Date toDate = statement.getToDate();
		   SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd"); 

		   if(request.getParameter("search") != null) {
			   DemographicDao dao = (DemographicDao) SpringUtils.getBean("demographicDao"); 
			   List<Demographic> demographicList = dao.searchDemographic(statement.getLastNameParam()+","+statement.getFirstNameParam());
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
			   BillingONDataHelp dbObj = new BillingONDataHelp();
			   //testing data: 18812 2010-05-01 2010-05-02
			   String sql = "SELECT bch.id,bch.billing_date,bch.total,bch.paid,bch.demographic_name,d.address,d.hin,d.phone,d.phone2 " + 
			   " FROM billing_on_cheader1 bch JOIN demographic d ON bch.demographic_no=d.demographic_no " + 
			   " WHERE bch.demographic_no='" + demographic.getDemographicNo() + "' AND bch.pay_program='PAT' ";
			   if(fromDate != null) sql += " AND bch.billing_date >= '" + df.format(fromDate);
			   if(toDate != null) sql += "' AND bch.billing_date <= '" + df.format(toDate) + "'";
			   sql += " ORDER BY bch.id";
			   ResultSet rs = dbObj.searchDBRecord(sql);
			   if(rs == null) {
				   errors.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage("errors.billing.ca.on.database", "SQL error"));
				   saveErrors(request,errors);
				   _logger.error("Database error on query "+sql);
				   return mapping.findForward(RES_FAILURE);  
			   }
			   double totalInvoiced = 0;
			   double totalPaid = 0;
			   int invoiceCount = 0;
			   try {
				   if(rs.next()) {
					   result = new ArrayList<PatientEndYearStatementInvoiceBean>();
					   do {
						   String paid = Utility.toCurrency(rs.getString("paid"));
						   String invoiced = Utility.toCurrency(rs.getString("total"));
						   PatientEndYearStatementInvoiceBean bean = 
							   new PatientEndYearStatementInvoiceBean(rs.getInt("id"),
									   rs.getDate("billing_date"), invoiced, paid );
						   String sql2 = "SELECT bi.service_code,bi.fee FROM billing_on_item bi WHERE ch1_id="+rs.getInt("id")+" ORDER BY bi.service_code";
						   ResultSet rs2 = dbObj.searchDBRecord(sql2);
						   List<PatientEndYearStatementServiceBean> services = null;
						   if(rs2 == null) {
							   errors.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage("errors.billing.ca.on.database", "SQL error"));
							   saveErrors(request,errors);
							   _logger.error("Database error on query "+sql2);
							   return mapping.findForward(RES_FAILURE);  
						   }
						   try {
							   if(rs2.next()) {
								   services = new ArrayList<PatientEndYearStatementServiceBean>();
								   do {
									   String fee = Utility.toCurrency(rs2.getString("fee"));
									   PatientEndYearStatementServiceBean serviceBean = 
										   new PatientEndYearStatementServiceBean(rs2.getString("service_code"),fee );
									   services.add(serviceBean);
								   } while (rs2.next());
							   }
						   } catch (SQLException e) {
							   errors.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage("errors.billing.ca.on.database", "SQL error"));
							   saveErrors(request,errors);
							   _logger.error("Database error on query "+sql2,e);
							   return mapping.findForward(RES_FAILURE);
						   }
						   bean.setServices(services);
						   result.add(bean);
						   totalInvoiced += Double.parseDouble(invoiced);
						   totalPaid += Double.parseDouble(paid);
						   invoiceCount += 1;
						   request.setAttribute("result", result);
					   } while (rs.next());
				   }
				   summary.setInvoiced(Utility.toCurrency(totalInvoiced));
				   summary.setPaid(Utility.toCurrency(totalPaid));
				   summary.setCount(Integer.toString(invoiceCount));
				   request.getSession().setAttribute("summary", summary);
			   } catch (SQLException e) {
				   errors.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage("errors.billing.ca.on.database", "SQL error"));
				   saveErrors(request,errors);
				   _logger.error("Database error on query "+sql,e);
				   return mapping.findForward(RES_FAILURE);
			   }

		   } else if(request.getParameter("pdf") != null) {
			   summary = (PatientEndYearStatementBean) request.getSession().getAttribute("summary");
			   OscarDocumentCreator osc = new OscarDocumentCreator();
			   String docFmt = "pdf";
			   HashMap reportParams = new HashMap<String,Object>();
			   reportParams.put("patientId", summary.getPatientNo());
			   reportParams.put("patientName", summary.getPatientName());
			   reportParams.put("hin", summary.getHin());
			   reportParams.put("address", summary.getAddress());
			   reportParams.put("phone", summary.getPhone());
			   reportParams.put("fromDate", statement.getFromDate() != null ? df.format(statement.getFromDate()) : "");
			   reportParams.put("toDate", statement.getToDate() != null ? df.format(statement.getToDate()) : "");
			   reportParams.put("invoiceCount", summary.getCount());
			   reportParams.put("totalInvoiced", summary.getInvoiced());
			   reportParams.put("totalPaid", summary.getPaid());
			   request.setAttribute("fromDateParam",statement.getFromDateParam());
			   request.setAttribute("toDateParam",statement.getToDateParam());
			   reportParams.put("SUBREPORT_DIR", REPORTS_PATH);
			   
			   ServletOutputStream outputStream = getServletOstream(response);

			   //open corresponding Jasper Report Definition
			   InputStream reportInstream = osc.getDocumentStream(REPORTS_PATH + "end_year_statement_report.jrxml");

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
			   if(dbConn != null) osc.fillDocumentStream(reportParams, outputStream, docFmt, reportInstream, dbConn);
			   return null;	   
		   } 
	   } else if (request.getParameter("demosearch") != null) {
		   request.getSession().setAttribute("summary", null);			   
		   DemographicDao dao = (DemographicDao) SpringUtils.getBean("demographicDao"); 
		   List<Demographic> demographicList = dao.searchDemographic(statement.getLastNameParam()+","+statement.getFirstNameParam());
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
