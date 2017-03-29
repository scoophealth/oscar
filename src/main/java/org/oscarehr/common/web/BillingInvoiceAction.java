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
package org.oscarehr.common.web;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionRedirect;
import org.apache.struts.actions.DispatchAction;
import org.oscarehr.common.service.PdfRecordPrinter;
import org.oscarehr.managers.BillingONManager;
import org.oscarehr.managers.SecurityInfoManager;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.util.ConcatPDF;
import oscar.util.UtilDateUtilities;

/**
 *
 * @author mweston4
 */
public class BillingInvoiceAction extends DispatchAction {
    
	private SecurityInfoManager securityInfoManager = SpringUtils.getBean(SecurityInfoManager.class);
	   
	
    public ActionForward getPrintPDF(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)  throws IOException {
        Integer invoiceNo = Integer.parseInt(request.getParameter("invoiceNo"));                
        String actionResult = "failure";
        
        if(!securityInfoManager.hasPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), "_billing", "r", null)) {
        	throw new SecurityException("missing required security object (_billing)");
        }

        
        if (invoiceNo != null) {
            response.setContentType("application/pdf"); // octet-stream
            response.setHeader("Content-Disposition", "attachment; filename=\"BillingInvoice" + invoiceNo + "_" + UtilDateUtilities.getToday("yyyy-MM-dd.hh.mm.ss") + ".pdf\"");
            boolean bResult = processPrintPDF(invoiceNo,request.getLocale(), response.getOutputStream());
            if (bResult) {
                actionResult = "success";
            }
        }
        return mapping.findForward(actionResult);
    }
    
    public ActionForward getListPrintPDF(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)  throws IOException {
       
    	 if(!securityInfoManager.hasPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), "_billing", "r", null)) {
         	throw new SecurityException("missing required security object (_billing)");
         }
    	 
        String actionResult = "failure";       
        String[] invoiceNos = request.getParameterValues("invoiceAction");
        
        ArrayList<Object> fileList = new ArrayList<Object>();
        OutputStream fos = null;
        if (invoiceNos != null) {
            for (String invoiceNoStr : invoiceNos) {
                try {               
                        Integer invoiceNo = Integer.parseInt(invoiceNoStr);
                        String filename = "BillingInvoice" + invoiceNo + "_" + UtilDateUtilities.getToday("yyyy-MM-dd.hh.mm.ss") + ".pdf";
                        String savePath = oscar.OscarProperties.getInstance().getProperty("INVOICE_DIR") + "/" + filename;
                        fos = new FileOutputStream(savePath);                
                        processPrintPDF(invoiceNo, request.getLocale(), fos);
                        fileList.add(savePath);                
                } catch (Exception e) {
                    MiscUtils.getLogger().error("Error", e);
                } finally {
                    if (fos != null) fos.close();
                }
            }
        }
        if (!fileList.isEmpty()) {
            response.setContentType("application/pdf"); // octet-stream
            response.setHeader("Content-Disposition", "attachment; filename=\"BillingInvoices" + "_" + UtilDateUtilities.getToday("yyyy-MM-dd.hh.mm.ss") + ".pdf\"");
            ConcatPDF.concat(fileList, response.getOutputStream());
            actionResult = "listSuccess";
        }
        
       return mapping.findForward(actionResult);
    }
    
    public ActionForward sendEmail(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)  {
        
    	 if(!securityInfoManager.hasPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), "_billing", "w", null)) {
         	throw new SecurityException("missing required security object (_billing)");
         }
    	 
        String invoiceNoStr = request.getParameter("invoiceNo");
        Integer invoiceNo = Integer.parseInt(invoiceNoStr);
        Locale locale = request.getLocale();
        String actionResult = "failure";
        
        if (invoiceNo != null) {
            BillingONManager billingManager = (BillingONManager) SpringUtils.getBean("billingONManager");
            billingManager.sendInvoiceEmailNotification(invoiceNo, locale);
            billingManager.addEmailedBillingComment(invoiceNo, locale); 
            actionResult = "success";
        }

        ActionRedirect redirect = new ActionRedirect(mapping.findForward(actionResult));
        redirect.addParameter("billing_no", invoiceNo);
        return redirect;
    }
    
    public ActionForward sendListEmail(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)  {
        
    	 if(!securityInfoManager.hasPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), "_billing", "w", null)) {
         	throw new SecurityException("missing required security object (_billing)");
         }
    	 
        String actionResult = "failure";       
        String[] invoiceNos = request.getParameterValues("invoiceAction");
        Locale locale = request.getLocale();
        
        if (invoiceNos != null) {
            for (String invoiceNoStr : invoiceNos) {
                Integer invoiceNo = Integer.parseInt(invoiceNoStr);
                BillingONManager billingManager = (BillingONManager) SpringUtils.getBean("billingONManager");
                billingManager.sendInvoiceEmailNotification(invoiceNo, locale);
                billingManager.addEmailedBillingComment(invoiceNo, locale);               
            }
            actionResult = "listSuccess";
        }

        return mapping.findForward(actionResult);
    }
    
    private boolean processPrintPDF(Integer invoiceNo, Locale locale, OutputStream os) {
        
        boolean bResult = false;
        
        if (invoiceNo != null){                                    
            //Create PDF of the invoice
            PdfRecordPrinter printer = new PdfRecordPrinter(os);                           
            printer.printBillingInvoice(invoiceNo, locale);

            BillingONManager billingManager = (BillingONManager) SpringUtils.getBean("billingONManager");
            billingManager.addPrintedBillingComment(invoiceNo, locale);
            bResult = true;          
        }
        
        return bResult;        
    }                

}
