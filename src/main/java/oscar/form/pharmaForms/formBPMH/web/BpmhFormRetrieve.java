/**
 * Copyright (c) 2015-2019. The Pharmacists Clinic, Faculty of Pharmaceutical Sciences, University of British Columbia. All Rights Reserved.
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
 * The Pharmacists Clinic
 * Faculty of Pharmaceutical Sciences
 * University of British Columbia
 * Vancouver, British Columbia, Canada
 */
package oscar.form.pharmaForms.formBPMH.web;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.ActionRedirect;
import org.apache.struts.actions.DispatchAction;

import oscar.OscarProperties;
import oscar.form.pharmaForms.formBPMH.bean.BpmhFormBean;
import oscar.form.pharmaForms.formBPMH.business.BpmhFormHandler;
import oscar.form.pharmaForms.formBPMH.pdf.PDFController;

/*
 * Author: Dennis Warren 
 * Company: Colcamex Resources
 * Date: November 2014
 * For: UBC Pharmacy Clinic and McMaster Department of Family Medicine
 */
public class BpmhFormRetrieve extends DispatchAction {
	
	private static final String BPMH_PDF_TEMPLATE = "/WEB-INF/classes/oscar/form/prop/bpmh_template_marked.pdf";
	private BpmhFormHandler bpmhFormHandler;
			
	public ActionForward unspecified(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response) {
		return fetch(mapping,form,request,response);
	}
	public ActionForward fetch(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response) {
		
		BpmhFormBean bpmh = (BpmhFormBean) form;
		bpmhFormHandler = new BpmhFormHandler( bpmh );
		
		Integer demographicNo = Integer.parseInt( request.getParameter("demographic_no") );
		Integer formHistoryNumber = Integer.parseInt( request.getParameter("formId") );
		
		if( formHistoryNumber != null && formHistoryNumber > 0 ) {
			bpmhFormHandler.setFormHistory(formHistoryNumber);
		}else if( demographicNo != null ) {	
			bpmhFormHandler.setDemographicNo(demographicNo);						
		}

		bpmhFormHandler.populateFormBean();
		
		return mapping.findForward("success");
	}
	
	public ActionForward save(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response) {
		
		BpmhFormBean bpmh = (BpmhFormBean) form;
		Integer demographicNo = Integer.parseInt( bpmh.getDemographicNo() ); 
		ActionMessages actionMessage = new ActionMessages();
		Integer formId = null;
		
		bpmhFormHandler = new BpmhFormHandler(bpmh);
		bpmhFormHandler.setDemographicNo( demographicNo ); 
		bpmhFormHandler.populateFormBean();
		bpmh.setEditDate( new Date() );
		
		formId = bpmhFormHandler.saveFormHistory();

		actionMessage.add("saved", new ActionMessage("Form Saved"));

		ActionRedirect actionRedirect = new ActionRedirect( mapping.findForward("saved") );
		actionRedirect.addParameter("demographic_no", demographicNo);
		actionRedirect.addParameter("formId", formId);
		actionRedirect.addParameter("provNo", null);
		
		return actionRedirect;

	}
	
	public ActionForward print(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		BpmhFormBean bpmh = (BpmhFormBean) form;

		FileInputStream input = null;
		OutputStream output = null;
		byte[] pdfContent = null;
		Integer demographicNo = Integer.parseInt( bpmh.getDemographicNo() );
		Integer formId = Integer.parseInt( bpmh.getFormId() );

		bpmhFormHandler = new BpmhFormHandler(bpmh); 
		
		// form Id greater than zero means that this is a saved instance.
		if( formId > 0 ) {
			bpmhFormHandler.setFormHistory( formId );
		} else if( demographicNo != null ) {	
			bpmhFormHandler.setDemographicNo(demographicNo);						
		}
			
		bpmhFormHandler.populateFormBean();

		PDFController pdfController = new PDFController(getServlet().getServletContext().getRealPath(BPMH_PDF_TEMPLATE));
		pdfController.setOutputPath(OscarProperties.getInstance().getProperty("DOCUMENT_DIR"));
		pdfController.writeDataToPDF(bpmh, new String[]{"1"}, demographicNo + "");
		
		bpmh.setEditDate( new Date() );
		
		if( formId == 0 ) {
			bpmhFormHandler.saveFormHistory();
		}
		
		input = new FileInputStream( pdfController.getOutputPath() );
        pdfContent = new byte[ input.available() ];
        input.read(pdfContent, 0, input.available());

        response.reset();
		response.setContentType("application/pdf");
		response.setHeader("Content-Disposition", "inline; filename=" + pdfController.getFileName());
		output = response.getOutputStream();
		
		if(output != null) {
			output.write(pdfContent);		
			output.close();
		}
		
		if(input != null) {
			input.close();
		}

		return null;
	}
		
}
