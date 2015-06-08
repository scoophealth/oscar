
package oscar.oscarBilling.ca.bc.MSP;

import java.io.InputStream;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.oscarehr.util.DbConnectionFilter;
import org.oscarehr.util.MiscUtils;

import oscar.OscarAction;
import oscar.OscarDocumentCreator;
import oscar.entities.MSPBill;
import oscar.oscarBilling.ca.bc.MSP.MSPReconcile.BillSearch;
import oscar.oscarBilling.ca.bc.data.PayRefSummary;

/**
 *
 * <p>Title: CreateBillingReportAction</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: </p>
 * @author Joel Legris
 * @version 1.0
 */
public class CreateBillingReportAction extends OscarAction {

    private Properties reportCfg = new Properties();
    private OscarDocumentCreator osc = new OscarDocumentCreator();
    private boolean showICBC;
    private boolean showMSP;
    private boolean showPriv;
    private boolean showWCB;
    private static final String REPORTS_PATH = "oscar/oscarBilling/ca/bc/reports/";

    public CreateBillingReportAction() {
        this.cfgReports();

    }

    /**
     * Performs Report Generation Logic based on the supplied parameters form the submitted form
     */
    public ActionForward execute(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) {
        request.getSession().getServletContext().getServletContextName();
        if (!System.getProperties().containsKey("jasper.reports.compile.class.path")) {
            String classpath = (String)getServlet().getServletContext().getAttribute("org.apache.catalina.jsp_classpath");
            if (classpath==null) classpath = (String)request.getSession().getServletContext().getAttribute("com.ibm.websphere.servlet.application.classpath");
            System.setProperty("jasper.reports.compile.class.path", classpath);
        }
        if (!System.getProperties().containsKey("java.awt.headless")) {
            System.setProperty("java.awt.headless", "true");
        }

        CreateBillingReportActionForm frm = (CreateBillingReportActionForm)actionForm;

        //get form field data
        String docFmt = frm.getDocFormat();
        String repType = frm.getRepType();
        String account = frm.getSelAccount();
        String payee = frm.getSelPayee();
        String provider = frm.getSelProv();
        String startDate = frm.getXml_vdate();
        String endDate = frm.getXml_appointment_date();
        String repDef = docFmt + "_" + this.reportCfg.getProperty(repType);
        showICBC = new Boolean(frm.getShowICBC()).booleanValue();
        showMSP = new Boolean(frm.getShowMSP()).booleanValue();
        showPriv = new Boolean(frm.getShowPRIV()).booleanValue();
        showWCB = new Boolean(frm.getShowWCB()).booleanValue();
        String insurers = createInsurerList();

        //Map of insurer types to be used in bill search criteria
        HashMap reportParams = new HashMap();
        reportParams.put("startDate", startDate);
        reportParams.put("endDate", endDate);
        reportParams.put("insurers", insurers);
        ServletOutputStream outputStream = this.getServletOstream(response);
        MSPReconcile msp = new MSPReconcile();
        BillSearch billSearch = null;

        //open corresponding Jasper Report Definition
        InputStream reportInstream = osc.getDocumentStream(REPORTS_PATH + repDef);
        try {
	        //COnfigure Reponse Header
	        cfgHeader(response, repType, docFmt);
	        //select appropriate report retrieval method
	        if (repType.equals(MSPReconcile.REP_ACCOUNT_REC) || repType.equals(MSPReconcile.REP_INVOICE) || repType.equals(MSPReconcile.REP_WO)) {
	
	            billSearch = msp.getBillsByType(account, payee, provider, startDate, endDate, !showWCB, !showMSP, !showPriv, !showICBC, repType);
	            String billCnt = String.valueOf(msp.getDistinctFieldCount(billSearch.list, "billing_no"));
	            String demNoCnt = String.valueOf(msp.getDistinctFieldCount(billSearch.list, "demoNo"));
	            if (repType.equals(MSPReconcile.REP_ACCOUNT_REC)) {
	                reportParams.put("amtSubmitted", msp.getTotalPaidByStatus(billSearch.list, MSPReconcile.SUBMITTED));
	            }
	            else if (repType.equals(MSPReconcile.REP_WO)) {
	                oscar.entities.Provider payeeProv = msp.getProvider(payee, 1);
	                oscar.entities.Provider provProv = msp.getProvider(provider, 0);
	                reportParams.put("provider", provider.equals("ALL")?"ALL":payeeProv.getFullName());
	                reportParams.put("payee", payee.equals("ALL")?"ALL":provProv.getFullName());
	            }
	
	            oscar.entities.Provider acctProv = msp.getProvider(account, 0);
	            reportParams.put("billCnt", billCnt);
	            reportParams.put("demNoCnt", demNoCnt);
	            reportParams.put("account", account.equals("ALL")?"ALL":acctProv.getFullName());
	
	            //Fill document with report parameter data
	            osc.fillDocumentStream(reportParams, outputStream, docFmt, reportInstream, billSearch.list);
	
	        }
	        else if (repType.equals(MSPReconcile.REP_MSPREM)) {
	            oscar.entities.Provider payeeProv = msp.getProvider(payee, 1);
	            reportParams.put("payee", payeeProv.getFullName());
	            reportParams.put("payeeno", payee);
	            String s21id = request.getParameter("rano");
	            osc.fillDocumentStream(reportParams, outputStream, docFmt, reportInstream, msp.getMSPRemittanceQuery(payee, s21id));
	        }
	        else if (repType.equals(MSPReconcile.REP_MSPREMSUM)) {
	            String s21id = request.getParameter("rano");
	            oscar.entities.S21 s21 = msp.getS21Record(s21id);
	
	            oscar.entities.Provider payeeProv = msp.getProvider(provider, 1);
	            reportParams.put("mspBean", msp);
	            //set parameters for payee of provider
	            reportParams.put("provider", payeeProv.equals("ALL")?"ALL":payeeProv.getInitials());
	            reportParams.put("providerNo", payeeProv.getProviderNo());
	            //set parameters for S21 report header
	            reportParams.put("payeeName", s21.getPayeeName());
	            reportParams.put("amtBilled", s21.getAmtBilled());
	            reportParams.put("amtPaid", s21.getAmtPaid());
	            reportParams.put("cheque", s21.getCheque());
	            reportParams.put("s21id", s21id);
	            reportParams.put("paymentDate", s21.getPaymentDate());
	            reportParams.put("payeeNo", s21.getPayeeNo());
	
	            //This is the practitioner summary subreport stream
	            InputStream subPractSum = osc.getDocumentStream(REPORTS_PATH + this.reportCfg.getProperty("REP_MSPREMSUM_PRACTSUM"));
				try {
					reportParams.put("practSum", osc.getJasperReport(subPractSum));
				} 
				finally {
					IOUtils.closeQuietly(subPractSum);
				}

	
	            //This is the S23 summary subreport stream
	            InputStream subS23 = osc.getDocumentStream(REPORTS_PATH + this.reportCfg.getProperty("REP_MSPREMSUM_S23"));
				try {
		            reportParams.put("adj", osc.getJasperReport(subS23));
				} 
				finally {
					IOUtils.closeQuietly(subS23);
				}
				
	            InputStream subS23orphan = osc.getDocumentStream(REPORTS_PATH + this.reportCfg.getProperty("REP_MSPREMSUM_S23_ORPHAN"));
				try {
		            reportParams.put("orphanAdj", osc.getJasperReport(subS23orphan));
				} 
				finally {
					IOUtils.closeQuietly(subS23orphan);
				}
	           
	            //This is the broadcast messages subreport stream
	            InputStream msgs = osc.getDocumentStream(REPORTS_PATH + this.reportCfg.getProperty("MSGS"));
				try {
		            reportParams.put("msgs", osc.getJasperReport(msgs));
				} 
				finally {
					IOUtils.closeQuietly(msgs);
				}
	
	            try {
	                osc.fillDocumentStream(reportParams, outputStream, docFmt, reportInstream, DbConnectionFilter.getThreadLocalDbConnection());
	            }
	            catch (SQLException e) {
	                MiscUtils.getLogger().error("Error", e);
	            }
	
	        }
	
	        else if (repType.equals(MSPReconcile.REP_PAYREF) || repType.equals(MSPReconcile.REP_PAYREF_SUM)) {
	            billSearch = msp.getPayments(account, payee, provider, startDate, endDate, !showWCB, !showMSP, !showPriv, !showICBC);
	            oscar.entities.Provider payeeProv = msp.getProvider(payee, 1);
	            oscar.entities.Provider acctProv = msp.getProvider(account, 0);
	            oscar.entities.Provider provProv = msp.getProvider(provider, 0);
	            PayRefSummary sumPayed = new PayRefSummary();
	            PayRefSummary sumRefunded = new PayRefSummary();
	            for (Iterator iter = billSearch.list.iterator(); iter.hasNext();) {
	
	                MSPBill item = (MSPBill)iter.next();
	                double dblValue = Double.parseDouble(item.getAmount());
	                if (dblValue < 0) {
	                    sumRefunded.addIncValue(item.getPaymentMethod(), dblValue);
	                }
	                sumPayed.addIncValue(item.getPaymentMethod(), dblValue);
	                sumPayed.addAdjustmentAmount(item.getAdjustmentCodeAmt());
	            }
	            reportParams.put("sumPayed", sumPayed);
	            reportParams.put("sumRefunded", sumRefunded);
	
	            reportParams.put("account", account.equals("ALL")?"ALL":acctProv.getFullName());
	
	            reportParams.put("provider", provider.equals("ALL")?"ALL":provProv.getInitials());
	            reportParams.put("payee", payee.equals("ALL")?"ALL":payeeProv.getInitials());
	            reportParams.put("startDate", startDate);
	            reportParams.put("endDate", endDate);
	
	            //Fill document with report parameter data
	            osc.fillDocumentStream(reportParams, outputStream, docFmt, reportInstream, billSearch.list);
	
	        }
	
	        else if (repType.equals(MSPReconcile.REP_REJ)) {
	            billSearch = msp.getBillsByType(account, payee, provider, startDate, endDate, !showWCB, !showMSP, !showPriv, !showICBC, repType);
	
	            
	            oscar.entities.Provider payProv = msp.getProvider(payee, 1);
	            reportParams.put("account", account.equals("ALL")?"ALL":payProv.getFullName());
	            //Fill document with report parameter data
	            osc.fillDocumentStream(reportParams, outputStream, docFmt, reportInstream, billSearch.list);
	        }
	   }
	   finally {
		   IOUtils.closeQuietly(reportInstream);
	   }

        return actionMapping.findForward(this.target);
    }

    /**
     * A convenience method that returns a concatenated list of insurer types
     * to be passed into a report
     * @return String
     */
    private String createInsurerList() {
        String insurers = "";

        if (showICBC && showMSP && showPriv && showWCB) {
            insurers = "ALL";
        }
        else {
            if (showICBC) {
                insurers += "ICBC,";
            }
            if (showMSP) {
                insurers += "MSP,";
            }
            if (showPriv) {
                insurers += "Private,";
            }
            if (showWCB) {
                insurers += "WCB,";
            }
        }
        return insurers;
    }

    /**
     * Configures document association settings. Associates a selected report with
     * a specific jasper report definition
     */
    public void cfgReports() {
        this.reportCfg.setProperty(MSPReconcile.REP_INVOICE, "rep_invoice.jrxml");
        this.reportCfg.setProperty(MSPReconcile.REP_PAYREF, "rep_payref.jrxml");
        this.reportCfg.setProperty(MSPReconcile.REP_PAYREF_SUM, "rep_payref_sum.jrxml");
        this.reportCfg.setProperty(MSPReconcile.REP_ACCOUNT_REC, "rep_account_rec.jrxml");
        this.reportCfg.setProperty(MSPReconcile.REP_REJ, "rep_rej.jrxml");

        this.reportCfg.setProperty(MSPReconcile.REP_WO, "rep_wo.jrxml");
        this.reportCfg.setProperty(MSPReconcile.REP_MSPREM, "rep_msprem.jrxml");
        this.reportCfg.setProperty(MSPReconcile.REP_MSPREMSUM, "rep_mspremsum.jrxml");
        this.reportCfg.setProperty(MSPReconcile.REP_MSPREMSUM_PRACTSUM, "msppremsum.practsum.jrxml");
        this.reportCfg.setProperty(MSPReconcile.REP_MSPREMSUM_S23, "msppremsum.s23.jrxml");
        this.reportCfg.setProperty(MSPReconcile.REP_MSPREMSUM_S23_ORPHAN, "msppremsum.s23_orphan.jrxml");
        this.reportCfg.setProperty("MSGS", "broadcastmessages.jrxml");
    }

}
