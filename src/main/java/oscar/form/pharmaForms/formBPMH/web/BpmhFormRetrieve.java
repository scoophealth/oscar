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
package oscar.form.pharmaForms.formBPMH.web;

import java.io.FileInputStream;
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
import org.oscarehr.managers.DocumentManager;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.SpringUtils;

import oscar.OscarProperties;
import oscar.dms.ConvertToEdoc;
import oscar.dms.EDoc;
import oscar.form.pharmaForms.formBPMH.bean.BpmhFormBean;
import oscar.form.pharmaForms.formBPMH.business.BpmhFormHandler;
import oscar.form.pharmaForms.formBPMH.pdf.PDFController;
import oscar.form.pharmaForms.formBPMH.pdf.PDFControllerConfig;

/*
 * Author: Dennis Warren 
 * Company: Colcamex Resources
 * Date: November 2014
 * For: UBC Pharmacy Clinic and McMaster Department of Family Medicine
 */
public class BpmhFormRetrieve extends DispatchAction {
	
	private static final String BPMH_PDF_TEMPLATE = "/WEB-INF/classes/oscar/form/prop/bpmh_template_marked.pdf";
	private BpmhFormHandler bpmhFormHandler;
	private static DocumentManager documentManager = SpringUtils.getBean( DocumentManager.class );

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

		bpmhFormHandler.populateFormBean( LoggedInInfo.getLoggedInInfoFromSession(request) );
		
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
		bpmhFormHandler.populateFormBean( LoggedInInfo.getLoggedInInfoFromSession(request) );
		bpmh.setEditDate( new Date() );
		
		formId = bpmhFormHandler.saveFormHistory();

		actionMessage.add("saved", new ActionMessage("Form Saved"));

		ActionRedirect actionRedirect = new ActionRedirect( mapping.findForward("saved") );
		actionRedirect.addParameter("demographic_no", demographicNo);
		actionRedirect.addParameter("formId", formId);
		actionRedirect.addParameter("provNo", null);
		
		return actionRedirect;

	}
	
	public ActionForward eDocument(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		ActionMessages actionMessage = new ActionMessages();
		BpmhFormBean bpmh = (BpmhFormBean) form;
		
		String formId = processPDF( (BpmhFormBean) form, request, response,  Boolean.TRUE );
		
		actionMessage.add( "saved", new ActionMessage("Saved as EDoc") );
		
		ActionRedirect actionRedirect = new ActionRedirect( mapping.findForward("saved") );
		actionRedirect.addParameter("demographic_no",  bpmh.getDemographicNo() );
		actionRedirect.addParameter("formId", formId);
		actionRedirect.addParameter("provNo", null);
		
		return actionRedirect;
	}
	
	public ActionForward print(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response) throws Exception {
	
		return new ActionForward( processPDF( (BpmhFormBean) form, request, response,  Boolean.FALSE ) );
	}
	
	private String processPDF( BpmhFormBean bpmh, HttpServletRequest request, HttpServletResponse response,  boolean edocument ) 
			throws Exception {

		FileInputStream input = null;
		OutputStream output = null;
		byte[] pdfContent = null;
		Integer demographicNo = Integer.parseInt( bpmh.getDemographicNo() );
		Integer formId = Integer.parseInt( bpmh.getFormId() );
		LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);
		String actionForward = null;
		
		bpmhFormHandler = new BpmhFormHandler(bpmh); 
		
		// form Id greater than zero means that this is a saved instance.
		if( formId > 0 ) {
			bpmhFormHandler.setFormHistory( formId );
		} else if( demographicNo != null ) {	
			bpmhFormHandler.setDemographicNo(demographicNo);						
		}
			
		bpmhFormHandler.populateFormBean( loggedInInfo );

		PDFControllerConfig config = new PDFControllerConfig();
		config.setTargetBeans( new String[]{"org.oscarehr.common.model", "oscar.form.pharmaForms.formBPMH.bean"} );

		config.setJavaScript( new String[]{ "this.print({bUI: true, bSilent: true, bShrinkToFit:true});", "this.closeDoc(true);" } );		

		config.addTableRowLimit("drugs", 10, 2);
		config.addTableRowLimit("drugs", 20, 3);		

		config.addTextBoxLineLimits("note", 10, 4);
		config.addTextLengthLimits("note", 1200, 4);
		
		PDFController pdfController = new PDFController( getServlet().getServletContext().getResource( BPMH_PDF_TEMPLATE ), config );
		
		pdfController.setOutputPath(OscarProperties.getInstance().getProperty("DOCUMENT_DIR"));
		pdfController.writeDataToPDF(bpmh, new String[]{"1"}, demographicNo + "");
		
		bpmh.setEditDate( new Date() );
		
		if( formId == 0 ) {
			formId = bpmhFormHandler.saveFormHistory();
		}
		
		if( edocument ) {
			
			EDoc edoc = ConvertToEdoc.buildEDoc( pdfController.getFileName(), "BPMH Form ID " + bpmh.getFormId(), "", 
					loggedInInfo.getLoggedInProviderNo(), demographicNo + "", ConvertToEdoc.DocumentType.form );
			
			documentManager.saveDocument(loggedInInfo, edoc);

			actionForward = formId + "";

		} else {
			
			input = new FileInputStream( pdfController.getOutputPath() );
	        pdfContent = new byte[ input.available() ];
	        input.read(pdfContent, 0, input.available());

	        response.reset();
			response.setContentType("application/pdf");
			response.setHeader("Content-Disposition", "inline; filename=" + pdfController.getFileName());
			output = response.getOutputStream();

		}

		if(output != null) {
			output.write(pdfContent);		
			output.close();
		}
		
		if(input != null) {
			input.close();
		}

		return actionForward;
	}
	
		
}
