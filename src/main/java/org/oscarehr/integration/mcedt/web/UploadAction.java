/**
 * Copyright (c) 2014-2015. KAI Innovations Inc. All Rights Reserved.
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
package org.oscarehr.integration.mcedt.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.cxf.helpers.FileUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.upload.FormFile;
import org.oscarehr.integration.mcedt.DelegateFactory;
import org.oscarehr.integration.mcedt.McedtMessageCreator;
import org.oscarehr.util.MiscUtils;

import ca.ontario.health.edt.EDTDelegate;
import ca.ontario.health.edt.Faultexception;
import ca.ontario.health.edt.ResourceResult;
import ca.ontario.health.edt.ResponseResult;
import ca.ontario.health.edt.UploadData;
import oscar.OscarProperties;

public class UploadAction extends DispatchAction {
	
	private static Logger logger = MiscUtils.getLogger();

	@Override
	public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, 
			HttpServletResponse response) throws Exception {
		ActionUtils.removeSuccessfulUploads(request);
		ActionUtils.removeUploadResponseResults(request);
		ActionUtils.removeSubmitResponseResults(request);
		Date startDate= ActionUtils.getOutboxTimestamp();
		Date endDate = new Date();
		if (startDate!=null && endDate!=null) {
			ActionUtils.moveOhipToOutBox(startDate,endDate);
			ActionUtils.moveObecToOutBox(startDate,endDate);			
			ActionUtils.setOutboxTimestamp(endDate);
		}
		ActionUtils.setUploadResourceId(request, new BigInteger("-1"));
		
		return mapping.findForward("success");
	}
	
	public ActionForward cancelUpload(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		ActionUtils.removeUploadResourceId(request);
		ActionUtils.removeUploadFileName(request);
		List<File> files = ActionUtils.getSuccessfulUploads(request);
		OscarProperties props = OscarProperties.getInstance();
		File sent = new File(props.getProperty("ONEDT_SENT",""));
		if (!sent.exists())
			FileUtils.mkDir(sent);
		
		try {
		if (files!=null && files.size()>0) {
			for (File file: files) {
				ActionUtils.moveFileToDirectory(file, sent, false, true);
			}
		}
		} catch (IOException e){
			logger.error("A exception has occured while moving files at " + new Date());
			saveErrors(request, ActionUtils.addMessage("uploadAction.upload.faultException", McedtMessageCreator.exceptionToString(e)));
			return mapping.findForward("failure");
		}
		ActionUtils.removeSuccessfulUploads(request);
		ActionUtils.removeUploadResponseResults(request);
		ActionUtils.removeSubmitResponseResults(request);


		//request.getSession().removeAttribute(McedtConstants.SESSION_KEY_UPLOAD_RESOURCE_ID);
		//request.getSession().removeAttribute(McedtConstants.SESSION_KEY_UPLOAD_FILENAME);
		return mapping.findForward("cancel");
	}

	public ActionForward addNew(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response)  {
		return mapping.findForward("addNew");
	}
	
	public ActionForward removeSelected(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response)  {
		return mapping.findForward("success");
	}

	public ActionForward uploadToMcedt(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)  {
		UploadForm uploadForm = (UploadForm) form;
		//uploadForm.setResourceType("HE");

		if (uploadForm.getResourceId().equals(new BigInteger("-1"))) {
			List<UploadData> uploads = new ArrayList<UploadData>();
			uploads.add(toUpload(uploadForm));

			/*try {
			    Thread.sleep(5000);                 //1000 milliseconds is one second.
			} catch(InterruptedException ex) {
			    Thread.currentThread().interrupt();
			}*/

			try {
				EDTDelegate delegate = DelegateFactory.newDelegate(ActionUtils.getServiceId(uploadForm.getDescription()));
				ResourceResult result = new ResourceResult();
				
				try {
					result = delegate.upload(uploads);
				}
				catch (Faultexception e){
					logger.error("A fault exception has occured while auto uploading MCEDT files at " + new Date());
					saveErrors(request, ActionUtils.addMessage("uploadAction.upload.faultException", McedtMessageCreator.exceptionToString(e)));
					return mapping.findForward("failure");
				}
				
				if (result.getResponse().get(0).getResult().getCode().equals("IEDTS0001")) {
					ActionUtils.setUploadResourceId(request, result.getResponse().get(0).getResourceID());
					OscarProperties props = OscarProperties.getInstance();
					File file = new File(props.getProperty("ONEDT_OUTBOX", "") + uploadForm.getFileName());
					ActionUtils.setSuccessfulUploads(request, file);
				} else {
					ActionUtils.setUploadResourceId(request, new BigInteger("-2"));
					result.getResponse().get(0).setDescription(uploadForm.getFileName()); //this is done because error response has null description
					ActionUtils.setSubmitResponseResults(request, result.getResponse().get(0));// if upload fails, submission is also assumed failed

				}
				ActionUtils.setUploadedFileName(request, uploadForm.getFileName());
				ActionUtils.setUploadResponseResults(request, result.getResponse().get(0));
				
				return mapping.findForward("success");
			
			} catch (Exception e) {
				logger.error("Unable to upload to MCEDT", e);				
				saveErrors(request, ActionUtils.addMessage("uploadAction.upload.failure", McedtMessageCreator.exceptionToString(e)));
				return mapping.findForward("failure");
			}

		}

		//ResourceResult result= new ResourceResult();
		//ActionUtils.setUploadResourceId(request, result.getResponse().get(0).getResourceID());

		//request.getSession().setAttribute(McedtConstants.SESSION_KEY_UPLOAD_RESOURCE_ID, new BigInteger("123"));
		//request.getSession().setAttribute(McedtConstants.SESSION_KEY_UPLOAD_FILENAME, uploadForm.getFileName());

		return mapping.findForward("success");
	}
	
	public ActionForward submitToMcedt(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)  {
		UploadForm submitForm = (UploadForm) form;
		//submitForm.setResourceId(new BigInteger("-1"));
		// if resourceId is -2, it indicates upload was not successful, submission will not be attempted
		if (!submitForm.getResourceId().equals(new BigInteger("-2"))) {
			List<BigInteger> ids = new ArrayList<BigInteger>();
			ids.add(submitForm.getResourceId());
			/*try {
			Thread.sleep(5000);                 //1000 milliseconds is one second.
			} catch(InterruptedException ex) {
			Thread.currentThread().interrupt();
			}*/

			try {
				EDTDelegate delegate = DelegateFactory.newDelegate(ActionUtils.getServiceId(submitForm.getFileName()));
				ResourceResult result = new ResourceResult();
				
				try {
					result = delegate.submit(ids);
				}
				catch (Faultexception e){
					logger.error("A fault exception has occured while auto submitting MCEDT files at " + new Date());
					saveErrors(request, ActionUtils.addMessage("uploadAction.upload.faultException", McedtMessageCreator.exceptionToString(e)));
					return mapping.findForward("failure");
				}
				
				if (!result.getResponse().get(0).getResult().getCode().equals("IEDTS0001")) {
					result.getResponse().get(0).setDescription(submitForm.getFileName());
				}
				ActionUtils.setSubmitResponseResults(request, result.getResponse().get(0));
				ActionUtils.setUploadResourceId(request, new BigInteger("-1"));
				return mapping.findForward("success");
				//reset(mapping, form, request, response);
				//saveMessages(request, ActionUtils.addMessage("uploadAction.submit.success", McedtMessageCreator.resourceResultToString(result)));
			} catch (Exception e) {
				logger.error("Unable to submit", e);
				saveErrors(request, ActionUtils.addMessage("uploadAction.submit.failure", McedtMessageCreator.exceptionToString(e)));
				return mapping.findForward("failure");
			}

		} else {//if file has failed at upload level, no need to try submit
			ActionUtils.setUploadResourceId(request, new BigInteger("-1"));
			return mapping.findForward("success");
		}
	}
	
	public ActionForward uploadSubmitToMcedt(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)  {
		try {
			List<String> successUploads= new ArrayList<String>();
			List<String> failUploads= new ArrayList<String>();
			List<String> successSubmits= new ArrayList<String>();
			List<String> failSubmits= new ArrayList<String>();
			List<UploadData> uploads =toUploadMultipe((UploadForm)form);
			for (UploadData upload: uploads) {
				List<UploadData> uploadData= new ArrayList<UploadData>();
				uploadData.add(upload);
				EDTDelegate delegate = DelegateFactory.newDelegate(ActionUtils.getServiceId(upload.getDescription()));
				ResourceResult result = new ResourceResult();
				
				try {
					result = delegate.upload(uploadData);
				}
				catch (Faultexception e){
					logger.error("A fault exception has occured while manually uploading MCEDT files at " + new Date());
					saveErrors(request, ActionUtils.addMessage("uploadAction.upload.faultException", McedtMessageCreator.exceptionToString(e)));
					return mapping.findForward("failure");
				}
				
				List<BigInteger> ids = new ArrayList<BigInteger>();
				OscarProperties props = OscarProperties.getInstance();
				File sent = new File(props.getProperty("ONEDT_SENT",""));
				if (!sent.exists())
					FileUtils.mkDir(sent);
				for (ResponseResult edtResponse: result.getResponse()) {
					if (edtResponse.getResult().getCode().equals("IEDTS0001")) {
						ids.add(edtResponse.getResourceID());
						File file = new File(props.getProperty("ONEDT_OUTBOX", "") + edtResponse.getDescription());
						ActionUtils.moveFileToDirectory(file, sent,false,true);
						//saveMessages(request, ActionUtils.addMessage("uploadAction.upload.success", McedtMessageCreator.resourceResultToString(result)));
						successUploads.add(McedtMessageCreator.resourceResultToString(result));
					} else {
						edtResponse.setDescription(upload.getDescription());
						//saveErrors(request, ActionUtils.addMessage("uploadAction.upload.failure", edtResponse.getDescription()+": "+edtResponse.getResult().getMsg()));
						failUploads.add(edtResponse.getDescription()+": "+edtResponse.getResult().getMsg());
					}
				}
				if (ids.size()>0) {
					
					try {
						result =delegate.submit(ids);
					}
					catch (Faultexception e){
						logger.error("A fault exception has occured while manually submitting MCEDT files at " + new Date());
						saveErrors(request, ActionUtils.addMessage("uploadAction.submit.faultException", McedtMessageCreator.exceptionToString(e)));
						return mapping.findForward("failure");
					}
					
					for (ResponseResult edtResponse: result.getResponse()) {
						if (edtResponse.getResult().getCode().equals("IEDTS0001")) {
							//saveMessages(request, ActionUtils.addMessage("uploadAction.submit.success", McedtMessageCreator.resourceResultToString(result)));
							successSubmits.add(McedtMessageCreator.resourceResultToString(result));
						} else {
							edtResponse.setDescription(upload.getDescription());
							//saveErrors(request, ActionUtils.addMessage("uploadAction.submit.failure", edtResponse.getDescription()+": "+edtResponse.getResult().getMsg()));
							failSubmits.add(edtResponse.getDescription()+": "+edtResponse.getResult().getMsg());
						}
					}
				}
			}
			// Finally save all the messages/errors
			ActionMessages messages = new ActionMessages();
			// we don't need to find out if upload is successful, we rather get info about submit status of that file
			//if ( successUploads!=null && successUploads.size()>0 ) messages = ActionUtils.addMoreMessage(messages, "uploadAction.upload.success", McedtMessageCreator.stringListToString(successUploads));
			if ( successSubmits!=null && successSubmits.size()>0 ) messages = ActionUtils.addMoreMessage(messages, "uploadAction.submit.success", McedtMessageCreator.stringListToString(successSubmits));
			saveMessages(request, messages);

			ActionMessages errors = new ActionMessages();
			if ( failUploads!=null && failUploads.size()>0 ) errors = ActionUtils.addMoreMessage(errors,"uploadAction.upload.failure", McedtMessageCreator.stringListToString(failUploads));
			if ( failSubmits!=null && failSubmits.size()>0 ) errors = ActionUtils.addMoreMessage(errors,"uploadAction.submit.failure", McedtMessageCreator.stringListToString(failSubmits));		
			saveErrors(request, errors);
			
		} catch (IOException e) {
			logger.error("An IO Exception has occured while moving the files to the sent folder at " + new Date(), e);
			saveErrors(request, ActionUtils.addMessage("uploadAction.upload.submit.IOException", McedtMessageCreator.exceptionToString(e)));
		} catch (Exception e) {
			logger.error("Unable to Upload/Submit file", e);
			saveErrors(request, ActionUtils.addMessage("uploadAction.upload.submit.failure", McedtMessageCreator.exceptionToString(e)));			
		}
		return mapping.findForward("success");
	}
	
	public ActionForward deleteUpload(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)  {
		try {
			UploadForm uploadForm=(UploadForm) form;
			List<String> fileNames = Arrays.asList(uploadForm.getFileName().trim().split(","));
			OscarProperties props = OscarProperties.getInstance();
			for (String fileName: fileNames) {
				File file = new File(props.getProperty("ONEDT_OUTBOX","")+fileName);
				file.delete();
			}
			
		} catch (Exception e) {
			logger.error("Unable to Delete file", e);
			saveErrors(request, ActionUtils.addMessage("uploadAction.upload.submit.failure", McedtMessageCreator.exceptionToString(e)));			
		}
		return mapping.findForward("success");
	}
	
	public ActionForward addUpload(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)  {
		try {
			FormFile formFile = ((UploadForm) form).getAddUploadFile();
			if (!ActionUtils.isOBECFile(formFile.getFileName()) && !ActionUtils.isOHIPFile(formFile.getFileName())) {
				saveErrors(request, ActionUtils.addMessage("uploadAction.upload.add.failure", formFile.getFileName()+" is not a supported file Name. Please upload only claim/OBEC files"));
				return mapping.findForward("failure");
			} else {
				OscarProperties props = OscarProperties.getInstance();
				File myFile = new File(props.getProperty("ONEDT_OUTBOX", "")+formFile.getFileName());
				FileOutputStream outputStream = new FileOutputStream(myFile);
				outputStream.write(formFile.getFileData());
				outputStream.close();
				saveMessages(request, ActionUtils.addMessage("uploadAction.upload.add.success", formFile.getFileName()+ "is succesfully added to the uploads list!"));
			}
			
		} catch (IOException e) {
			logger.error("An error has occured with the addUpload file at " + new Date(), e);
			saveErrors(request, ActionUtils.addMessage("uploadAction.upload.add.failure", McedtMessageCreator.exceptionToString(e)));
			return mapping.findForward("failure");
		} catch (Exception e) {
			logger.error("Unable to Add file upload", e);
			saveErrors(request, ActionUtils.addMessage("uploadAction.upload.add.failure", McedtMessageCreator.exceptionToString(e)));
			return mapping.findForward("failure");
		}
		return mapping.findForward("success");
		
		
	}
	
	public UploadData toUpload(UploadForm form) {
		UploadData result = new UploadData();
		result.setDescription(form.getDescription());
		result.setResourceType(form.getResourceType());
		try {
			OscarProperties props = OscarProperties.getInstance();
			File file = new File(props.getProperty("ONEDT_OUTBOX","")+form.getFileName());
			FileInputStream fis = new FileInputStream(file);
			byte[] data = new byte[fis.available()];
			fis.read(data);
			fis.close();
			result.setContent(data);
		} catch (Exception e) {
			logger.error("Unable to read upload file", e);
			
			throw new RuntimeException("Unable to read upload file", e);
		}
		return result;
	}
	
	public List<UploadData> toUploadMultipe(UploadForm form) {
		List<UploadData> results = new ArrayList<UploadData>();
		List<String> fileNames = Arrays.asList(form.getFileName().trim().split(","));
		List<String> resourceTypes = Arrays.asList(form.getResourceType().trim().split(","));
		try {
			if (fileNames.size()==resourceTypes.size()) {
				for (int i=0;i<fileNames.size();i++) {
					UploadData result = new UploadData();
					result.setDescription(fileNames.get(i));
					result.setResourceType(resourceTypes.get(i));
					OscarProperties props = OscarProperties.getInstance();
					File file = new File(props.getProperty("ONEDT_OUTBOX","")+fileNames.get(i));
					FileInputStream fis = new FileInputStream(file);
					byte[] data = new byte[fis.available()];
					fis.read(data);
					fis.close();
					result.setContent(data);
					results.add(result);
				}
				
			}
			
		} catch (Exception e) {
			logger.error("Unable to read upload file", e);
			
			throw new RuntimeException("Unable to read upload file", e);
		}
		return results;
	}
}
