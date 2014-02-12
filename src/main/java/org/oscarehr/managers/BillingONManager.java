/**
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
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
 *
 * This software was written for the
 * Department of Family Medicine
 * McMaster University
 * Hamilton
 * Ontario, Canada
 */
package org.oscarehr.managers;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Locale;
import java.util.Properties;
import org.apache.commons.io.IOUtils;
import org.apache.commons.mail.EmailException;
import org.apache.velocity.VelocityContext;
import org.oscarehr.common.dao.BillingONCHeader1Dao;
import org.oscarehr.common.dao.ClinicDAO;
import org.oscarehr.common.dao.DemographicDao;
import org.oscarehr.common.model.BillingONCHeader1;
import org.oscarehr.common.model.Clinic;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.common.web.BillingInvoiceAction;
import org.oscarehr.common.service.BillingONService;
import org.oscarehr.util.EmailUtilsOld;
import org.oscarehr.util.LocaleUtils;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.VelocityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import oscar.util.DateUtils;
import oscar.OscarProperties;
import java.text.NumberFormat;
import org.oscarehr.util.SpringUtils;
import org.oscarehr.common.model.BillingONExt;
import org.oscarehr.common.dao.BillingONExtDao;

/**
 *
 * @author mweston4
 */
@Service
public class BillingONManager {
    
    private static final String BILLING_INVOICE_EMAIL_TEMPLATE_FILE = "/billing_invoice_email_notification_template.txt";
    private static final String BILLING_INVOICE_EMAIL_PROPERTIES_FILE = "/billing_invoice_email.properties";
    
    @Autowired
    private BillingONCHeader1Dao billingONCHeader1Dao;
    
    @Autowired
    private BillingONExtDao billingONExtDao;
        
    @Autowired
    private ClinicDAO clinicDAO;
    
    @Autowired
    private DemographicDao demographicDao;
    
    protected static Properties emailProperties = getBillingEmailProperties();
    
    private static Properties getBillingEmailProperties() {
        
        Properties p = new Properties();
        InputStream is = null;
        try {           
            is = BillingInvoiceAction.class.getResourceAsStream(BILLING_INVOICE_EMAIL_PROPERTIES_FILE);
            p.load(is);
        } catch (java.io.IOException e) {
                MiscUtils.getLogger().error("Error reading properties file : " + BILLING_INVOICE_EMAIL_PROPERTIES_FILE, e);
        } finally {
            try {
                if (is != null) is.close();
            }
            catch (java.io.IOException e) {
                 MiscUtils.getLogger().error("Error closing properties file : " + BILLING_INVOICE_EMAIL_PROPERTIES_FILE, e);
            }
        }
        return (p);
    }
    
    
    public void sendInvoiceEmailNotification(Integer invoiceNo, Locale locale) {
        BillingONCHeader1 billingONCHeader1 = billingONCHeader1Dao.find(invoiceNo);   
        if (billingONCHeader1 != null) {
            Integer demoNo = billingONCHeader1.getDemographicNo();
            Demographic demographic = demographicDao.getDemographic(String.valueOf(demoNo));
            String emailAddress = demographic.getEmail();

            if (EmailUtilsOld.isValidEmailAddress(emailAddress)) {
                Clinic clinic = clinicDAO.getClinic();

                //Get Due Date of Invoice
                String dueDateStr="";
                if (OscarProperties.getInstance().hasProperty("invoice_due_date")) {
                    BillingONExt dueDateExt = billingONExtDao.getDueDate(billingONCHeader1);
                    if (dueDateExt != null) {
                        dueDateStr = dueDateExt.getValue();
                    } else {
                        Integer numDaysTilDue = Integer.parseInt(OscarProperties.getInstance().getProperty("invoice_due_date", "0")); 
                        Date serviceDate = billingONCHeader1.getBillingDate();
                        dueDateStr = DateUtils.sumDate(serviceDate, numDaysTilDue, locale);
                    }  
                }
                                        
                BillingONService billingONService = (BillingONService) SpringUtils.getBean("billingONService");
                BigDecimal bdBalance = billingONService.calculateBalanceOwing(invoiceNo);
                
                //Compile email                    
                VelocityContext velocityContext = VelocityUtils.createVelocityContextWithTools();
                velocityContext.put("clinic", clinic);
                velocityContext.put("demographic", demographic);
                velocityContext.put("billingONCHeader1", billingONCHeader1);

                String startHour = emailProperties.getProperty("clinic_open_hour","");
                velocityContext.put("start_hour",startHour);

                String endHour = emailProperties.getProperty("clinic_close_hour","");
                velocityContext.put("end_hour",endHour);

                velocityContext.put("date_due", dueDateStr);
                velocityContext.put("balance_owing",NumberFormat.getCurrencyInstance().format(bdBalance));

                InputStream is = null;
                String emailTemplate = null;
                try {
                    is = BillingInvoiceAction.class.getResourceAsStream(BILLING_INVOICE_EMAIL_TEMPLATE_FILE);
                    emailTemplate = IOUtils.toString(is);
                } 
                catch (java.io.IOException e) {
                    MiscUtils.getLogger().error("Cannot read billing invoices email template:",e);
                } 
                finally {
                    try {
                        if (is != null) is.close();
                    } 
                    catch (java.io.IOException e) {
                        MiscUtils.getLogger().error("Cannot close billing invoice email template:",e);
                    }
                }
                String fromAddress = emailProperties.getProperty("from_address","");
                String emailSubject = emailProperties.getProperty("billing_invoice_subject","");
                String mergedSubject = VelocityUtils.velocityEvaluate(velocityContext, emailSubject);
                String mergedBody = VelocityUtils.velocityEvaluate(velocityContext, emailTemplate);

                try {
                        EmailUtilsOld.sendEmail(emailAddress, demographic.getFormattedName(), fromAddress, clinic.getClinicName(), mergedSubject, mergedBody, null);
                } catch (EmailException e) {
                        MiscUtils.getLogger().error("Unexpected error.", e);
                }
            } else {
                MiscUtils.getLogger().warn("Email Address is invalid:" + emailAddress + " for DemoNo:" + demographic.getDemographicNo());
            }    
        }
        else {
            MiscUtils.getLogger().error("Cannot find BillingONCHeader1 JPA entity for Invoice No." + invoiceNo + ". Email not sent");
        }  
    }
    
    public void addPrintedBillingComment(Integer invoiceNo, Locale locale) {
        String printedMsg = LocaleUtils.getMessage(locale, "billing.billing3rdInv.msgPrinted");
        addBillingComment(printedMsg,invoiceNo,locale);
    }
    
    public void addEmailedBillingComment(Integer invoiceNo, Locale locale) {
        String emailedMsg = LocaleUtils.getMessage(locale, "billing.billing3rdInv.msgEmailed");
        addBillingComment(emailedMsg,invoiceNo,locale);
    }
     
    private void addBillingComment(String comment, Integer invoiceNo, Locale locale) {
        BillingONCHeader1 billingONCHeader1 = billingONCHeader1Dao.find(invoiceNo);                
        //Log that we printed the invoice in billing comments
        StringBuilder sb = new StringBuilder(billingONCHeader1.getComment().trim());
        
        if (!sb.toString().isEmpty()) {
            sb.append("\n");
        }

        billingONCHeader1.setComment(sb.append(comment).append(": ").append(DateUtils.formatDateTime(new Date(), locale)).toString());
        billingONCHeader1Dao.merge(billingONCHeader1);
    }
}
