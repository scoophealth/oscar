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


package org.oscarehr.document.web;

import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.jpedal.PdfDecoder;
import org.jpedal.fonts.FontMappings;
import org.oscarehr.PMmodule.caisi_integrator.CaisiIntegratorManager;
import org.oscarehr.PMmodule.caisi_integrator.IntegratorFallBackManager;
import org.oscarehr.caisi_integrator.ws.CachedDemographicDocument;
import org.oscarehr.caisi_integrator.ws.CachedDemographicDocumentContents;
import org.oscarehr.caisi_integrator.ws.DemographicWs;
import org.oscarehr.caisi_integrator.ws.FacilityIdIntegerCompositePk;
import org.oscarehr.casemgmt.model.CaseManagementNote;
import org.oscarehr.casemgmt.model.CaseManagementNoteLink;
import org.oscarehr.casemgmt.service.CaseManagementManager;
import org.oscarehr.common.dao.CtlDocumentDao;
import org.oscarehr.common.dao.DocumentDao;
import org.oscarehr.common.dao.PatientLabRoutingDao;
import org.oscarehr.common.dao.ProviderInboxRoutingDao;
import org.oscarehr.common.dao.SecRoleDao;
import org.oscarehr.common.model.CtlDocument;
import org.oscarehr.common.model.Document;
import org.oscarehr.common.model.PatientLabRouting;
import org.oscarehr.common.model.Provider;
import org.oscarehr.common.model.SecRole;
import org.oscarehr.sharingcenter.SharingCenterUtil;
import org.oscarehr.sharingcenter.model.DemographicExport;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import oscar.dms.EDoc;
import oscar.dms.EDocUtil;
import oscar.dms.IncomingDocUtil;
import oscar.log.LogAction;
import oscar.log.LogConst;
import oscar.oscarDemographic.data.DemographicData;
import oscar.oscarEncounter.data.EctProgram;
import oscar.oscarLab.ca.on.LabResultData;
import oscar.util.UtilDateUtilities;

import com.lowagie.text.pdf.PdfReader;
import com.sun.pdfview.PDFFile;
import com.sun.pdfview.PDFPage;

/**
 * @author jaygallagher
 */
public class ManageDocumentAction extends DispatchAction {

	private static Logger log = MiscUtils.getLogger();

	private DocumentDao documentDao = SpringUtils.getBean(DocumentDao.class);
	private CtlDocumentDao ctlDocumentDao = SpringUtils.getBean(CtlDocumentDao.class);
	private ProviderInboxRoutingDao providerInboxRoutingDAO = SpringUtils.getBean(ProviderInboxRoutingDao.class);
	
	public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

		return null;
	}

	public ActionForward documentUpdateAjax(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

		String ret = "";

		String observationDate = request.getParameter("observationDate");// :2008-08-22<
		String documentDescription = request.getParameter("documentDescription");// :test2<
		String documentId = request.getParameter("documentId");// :29<
		String docType = request.getParameter("docType");// :consult<

		LogAction.addLog((String) request.getSession().getAttribute("user"), LogConst.ADD, LogConst.CON_DOCUMENT, documentId, request.getRemoteAddr());

		String demog = request.getParameter("demog");

		String[] flagproviders = request.getParameterValues("flagproviders");
		// String demoLink=request.getParameter("demoLink");

		// TODO: if demoLink is "on", check if msp is in flagproviders, if not save to providerInboxRouting, if yes, don't save.

		// DONT COPY THIS !!!
		if (flagproviders != null && flagproviders.length > 0) { // TODO: THIS NEEDS TO RUN THRU THE lab forwarding rules!
			try {
				for (String proNo : flagproviders) {
					providerInboxRoutingDAO.addToProviderInbox(proNo, Integer.parseInt(documentId), LabResultData.DOCUMENT);
				}

				// Removes the link to the "0" provider so that the document no longer shows up as "unclaimed"
				providerInboxRoutingDAO.removeLinkFromDocument("DOC", Integer.parseInt(documentId), "0");
			} catch (Exception e) {
				MiscUtils.getLogger().error("Error", e);
			}
		}
		
		//Check to see if we have to route document to patient
		PatientLabRoutingDao patientLabRoutingDao = SpringUtils.getBean(PatientLabRoutingDao.class);
		List<PatientLabRouting>patientLabRoutingList = patientLabRoutingDao.findByLabNoAndLabType(Integer.parseInt(documentId), docType);
		if( patientLabRoutingList == null || patientLabRoutingList.size() == 0 ) {
			PatientLabRouting patientLabRouting = new PatientLabRouting();
			patientLabRouting.setDemographicNo(Integer.parseInt(demog));
			patientLabRouting.setLabNo(Integer.parseInt(documentId));
			patientLabRouting.setLabType("DOC");
			patientLabRoutingDao.persist(patientLabRouting);
		}
		
		
		Document d = documentDao.getDocument(documentId);

		if(d != null) {
			d.setDocdesc(documentDescription);
			d.setDoctype(docType);
			Date obDate = UtilDateUtilities.StringToDate(observationDate);
	
			if (obDate != null) {
				d.setObservationdate(obDate);
			}
	
			documentDao.merge(d);
		}

		
		try {

			CtlDocument ctlDocument = ctlDocumentDao.getCtrlDocument(Integer.parseInt(documentId));
			if(ctlDocument != null) {
				
				CtlDocument matchedCtlDocument = new CtlDocument();
				matchedCtlDocument.getId().setDocumentNo(ctlDocument.getId().getDocumentNo());
				matchedCtlDocument.getId().setModule(ctlDocument.getId().getModule());
				matchedCtlDocument.getId().setModuleId(Integer.parseInt(demog));
				matchedCtlDocument.setStatus(ctlDocument.getStatus());
				
				ctlDocumentDao.persist(matchedCtlDocument);
				
				ctlDocumentDao.remove(ctlDocument.getId());
				
				// save a document created note
				if (ctlDocument.isDemographicDocument()) {
					// save note
					saveDocNote(request, d.getDocdesc(), demog, documentId);
				}
			}
		} catch (Exception e) {
			MiscUtils.getLogger().error("Error", e);
		}
		
		
		if (ret != null && !ret.equals("")) {
			// response.getOutputStream().print(ret);
		}
		HashMap hm = new HashMap();
		hm.put("patientId", demog);
		JSONObject jsonObject = JSONObject.fromObject(hm);
		try {
			response.getOutputStream().write(jsonObject.toString().getBytes());
		} catch (IOException e) {
			MiscUtils.getLogger().error("Error", e);
		}

		return null;

	}

	public ActionForward getDemoNameAjax(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		String dn = request.getParameter("demo_no");
		HashMap hm = new HashMap();
		hm.put("demoName", getDemoName(LoggedInInfo.getLoggedInInfoFromSession(request), dn));
		JSONObject jsonObject = JSONObject.fromObject(hm);
		try {
			response.getOutputStream().write(jsonObject.toString().getBytes());
		} catch (IOException e) {
			MiscUtils.getLogger().error("Error", e);
		}

		return null;
	}

	public ActionForward removeLinkFromDocument(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String docType = request.getParameter("docType");
		String docId = request.getParameter("docId");
		String providerNo = request.getParameter("providerNo");

		providerInboxRoutingDAO.removeLinkFromDocument(docType, Integer.parseInt(docId), providerNo);
		HashMap hm = new HashMap();
		hm.put("linkedProviders", providerInboxRoutingDAO.getProvidersWithRoutingForDocument(docType, Integer.parseInt(docId)));

		JSONObject jsonObject = JSONObject.fromObject(hm);
		try {
			response.getOutputStream().write(jsonObject.toString().getBytes());
		} catch (IOException e) {
			MiscUtils.getLogger().error("Error",e);
		}

		return null;
	}	

	public ActionForward documentUpdate(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

		String ret = "";

		String observationDate = request.getParameter("observationDate");// :2008-08-22<
		String documentDescription = request.getParameter("documentDescription");// :test2<
		String documentId = request.getParameter("documentId");// :29<
		String docType = request.getParameter("docType");// :consult<

		LogAction.addLog((String) request.getSession().getAttribute("user"), LogConst.ADD, LogConst.CON_DOCUMENT, documentId, request.getRemoteAddr());

		String demog = request.getParameter("demog");

		String[] flagproviders = request.getParameterValues("flagproviders");
		// String demoLink=request.getParameter("demoLink");

		// TODO: if demoLink is "on", check if msp is in flagproviders, if not save to providerInboxRouting, if yes, don't save.

		// DONT COPY THIS !!!
		if (flagproviders != null && flagproviders.length > 0) { // TODO: THIS NEEDS TO RUN THRU THE lab forwarding rules!
			try {
				for (String proNo : flagproviders) {
					providerInboxRoutingDAO.addToProviderInbox(proNo, Integer.parseInt(documentId), LabResultData.DOCUMENT);
				}
			} catch (Exception e) {
				MiscUtils.getLogger().error("Error", e);
			}
		}
		Document d = documentDao.getDocument(documentId);

		if(d != null) {
			d.setDocdesc(documentDescription);
			d.setDoctype(docType);
			Date obDate = UtilDateUtilities.StringToDate(observationDate);
	
			if (obDate != null) {
				d.setObservationdate(obDate);
			}
	
			documentDao.merge(d);
		}

		try {

			CtlDocument ctlDocument = ctlDocumentDao.getCtrlDocument(Integer.parseInt(documentId));
			if(ctlDocument != null) {
				ctlDocument.getId().setModuleId(Integer.parseInt(demog));
				ctlDocumentDao.merge(ctlDocument);
				// save a document created note
				if (ctlDocument.isDemographicDocument()) {
					// save note
					saveDocNote(request, d.getDocdesc(), demog, documentId);
				}
			}
		} catch (Exception e) {
			MiscUtils.getLogger().error("Error", e);
		}

		if (ret != null && !ret.equals("")) {
			// response.getOutputStream().print(ret);
		}
				
		
		String providerNo = request.getParameter("providerNo");
		String searchProviderNo = request.getParameter("searchProviderNo");
		String ackStatus = request.getParameter("status");
		String demoName = getDemoName(LoggedInInfo.getLoggedInInfoFromSession(request) , demog);
		request.setAttribute("demoName", demoName);
		request.setAttribute("segmentID", documentId);
		request.setAttribute("providerNo", providerNo);
		request.setAttribute("searchProviderNo", searchProviderNo);
		request.setAttribute("status", ackStatus);

		return mapping.findForward("displaySingleDoc");

	}

	private String getDemoName(LoggedInInfo loggedInInfo, String demog) {
		DemographicData demoD = new DemographicData();
		org.oscarehr.common.model.Demographic demo = demoD.getDemographic(loggedInInfo, demog);
		String demoName = demo.getLastName() + ", " + demo.getFirstName();
		return demoName;
	}

	private void saveDocNote(final HttpServletRequest request, String docDesc, String demog, String documentId) {

		Date now = EDocUtil.getDmsDateTimeAsDate();
		// String docDesc=d.getDocdesc();
		CaseManagementNote cmn = new CaseManagementNote();
		cmn.setUpdate_date(now);
		cmn.setObservation_date(now);
		cmn.setDemographic_no(demog);
		HttpSession se = request.getSession();
		String user_no = (String) se.getAttribute("user");
		String prog_no = new EctProgram(se).getProgram(user_no);
		WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(se.getServletContext());
		CaseManagementManager cmm = (CaseManagementManager) ctx.getBean("caseManagementManager");
		cmn.setProviderNo("-1");// set the provider no to be -1 so the editor appear as 'System'.
		Provider provider = EDocUtil.getProvider(user_no);
		String provFirstName = "";
		String provLastName = "";
		if(provider!=null) {
			provFirstName=provider.getFirstName();
			provLastName=provider.getLastName();
		}
		String strNote = "Document" + " " + docDesc + " " + "created at " + now + " by " + provFirstName + " " + provLastName + ".";

		// String strNote="Document"+" "+docDesc+" "+ "created at "+now+".";
		cmn.setNote(strNote);
		cmn.setSigned(true);
		cmn.setSigning_provider_no("-1");
		cmn.setProgram_no(prog_no);
		
		SecRoleDao secRoleDao = (SecRoleDao) SpringUtils.getBean("secRoleDao");
		SecRole doctorRole = secRoleDao.findByName("doctor");		
		cmn.setReporter_caisi_role(doctorRole.getId().toString());
		
		cmn.setReporter_program_team("0");
		cmn.setPassword("NULL");
		cmn.setLocked(false);
		cmn.setHistory(strNote);
		cmn.setPosition(0);

		Long note_id = cmm.saveNoteSimpleReturnID(cmn);
		// Debugging purposes on the live server
		MiscUtils.getLogger().info("Document Note ID: "+note_id.toString());
		
		// Add a noteLink to casemgmt_note_link
		CaseManagementNoteLink cmnl = new CaseManagementNoteLink();
		cmnl.setTableName(CaseManagementNoteLink.DOCUMENT);
		cmnl.setTableId(Long.parseLong(documentId));
		cmnl.setNoteId(note_id);
		EDocUtil.addCaseMgmtNoteLink(cmnl);
	}

	/*
	 * private void savePatientLabRouting(String demog,String docId,String docType){ CommonLabResultData.updatePatientLabRouting(docId, demog, docType); }
	 */

	private static File getDocumentCacheDir(String docdownload) {
		// File cacheDir = new File(docdownload+"_cache");
		File docDir = new File(docdownload);
		String documentDirName = docDir.getName();
		File parentDir = docDir.getParentFile();

		File cacheDir = new File(parentDir, documentDirName + "_cache");

		if (!cacheDir.exists()) {
			cacheDir.mkdir();
		}
		return cacheDir;
	}

	private File hasCacheVersion2(Document d, Integer pageNum) {
		File documentCacheDir = getDocumentCacheDir(oscar.OscarProperties.getInstance().getProperty("DOCUMENT_DIR"));
		File outfile = new File(documentCacheDir, d.getDocfilename() + "_" + pageNum + ".png");
		if (!outfile.exists()) {
			outfile = null;
		}
		return outfile;
	}

	public static void deleteCacheVersion(Document d, int pageNum) {
		File documentCacheDir = getDocumentCacheDir(oscar.OscarProperties.getInstance().getProperty("DOCUMENT_DIR"));
		//pageNum=pageNum-1;
		File outfile = new File(documentCacheDir,d.getDocfilename()+"_"+pageNum+".png");
		if (outfile.exists()){
			outfile.delete();
		}
	}

	private File hasCacheVersion(Document d, int pageNum){
		File documentCacheDir = getDocumentCacheDir(oscar.OscarProperties.getInstance().getProperty("DOCUMENT_DIR"));
		//pageNum= pageNum-1;
		File outfile = new File(documentCacheDir,d.getDocfilename()+"_"+pageNum+".png");
		if (!outfile.exists()){
			outfile = null;
		}
		return outfile;
	}

	public File createCacheVersion2(Document d, Integer pageNum) {
		String docdownload = oscar.OscarProperties.getInstance().getProperty("DOCUMENT_DIR");
		File documentDir = new File(docdownload);
		File documentCacheDir = getDocumentCacheDir(docdownload);
		log.debug("Document Dir is a dir" + documentDir.isDirectory());
		File file = new File(documentDir, d.getDocfilename());
		PdfDecoder decode_pdf  = new PdfDecoder(true);
		File ofile = null;
		try {

			FontMappings.setFontReplacements();

			decode_pdf.useHiResScreenDisplay(true);

			decode_pdf.setExtractionMode(0, 96, 96/72f);

			FileInputStream is = new FileInputStream(file);

			decode_pdf.openPdfFileFromInputStream(is, false);

			BufferedImage image_to_save = decode_pdf.getPageAsImage(pageNum);



			decode_pdf.getObjectStore().saveStoredImage( documentCacheDir.getCanonicalPath() + "/" + d.getDocfilename() + "_" + pageNum + ".png", image_to_save, true, false, "png");

			decode_pdf.flushObjectValues(true);

			decode_pdf.closePdfFile();

			ofile = new File(documentCacheDir, d.getDocfilename() + "_" + pageNum + ".png");



		}catch(Exception e) {
			log.error("Error decoding pdf file " + d.getDocfilename());
			decode_pdf.closePdfFile();
		}

		return ofile;

		/*

		String docdownload = oscar.OscarProperties.getInstance().getProperty("DOCUMENT_DIR");
		File documentDir = new File(docdownload);
		File documentCacheDir = getDocumentCacheDir(docdownload);
		log.debug("Document Dir is a dir" + documentDir.isDirectory());

		File file = new File(documentDir, d.getDocfilename());

		RandomAccessFile raf = new RandomAccessFile(file, "r");
		FileChannel channel = raf.getChannel();
		ByteBuffer buf = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
		PDFFile pdffile = new PDFFile(buf);
		if(raf != null) raf.close();
		if(channel != null) channel.close();
		// long readfile = System.currentTimeMillis() - start;
		// draw the first page to an image
		PDFPage ppage = pdffile.getPage(pageNum);

		log.debug("WIDTH " + (int) ppage.getBBox().getWidth() + " height " + (int) ppage.getBBox().getHeight());

		// get the width and height for the doc at the default zoom
		Rectangle rect = new Rectangle(0, 0, (int) ppage.getBBox().getWidth(), (int) ppage.getBBox().getHeight());

		log.debug("generate the image");
		Image img = ppage.getImage(rect.width, rect.height, // width & height
		        rect, // clip rect
		        null, // null for the ImageObserver
		        true, // fill background with white
		        true // block until drawing is done
		        );

		log.debug("about to Print to stream");
		File outfile = new File(documentCacheDir, d.getDocfilename() + "_" + pageNum + ".png");

		OutputStream outs = null;
		try {
			outs = new FileOutputStream(outfile);

			RenderedImage rendImage = (RenderedImage) img;
			ImageIO.write(rendImage, "png", outs);
			outs.flush();
		} finally {
			if (outs != null) outs.close();
		}
		return outfile;
*/
	}

	public File createCacheVersion(Document d) throws Exception {

		String docdownload = oscar.OscarProperties.getInstance().getProperty("DOCUMENT_DIR");
		File documentDir = new File(docdownload);
		File documentCacheDir = getDocumentCacheDir(docdownload);
		log.debug("Document Dir is a dir" + documentDir.isDirectory());

		File file = new File(documentDir, d.getDocfilename());

		RandomAccessFile raf = new RandomAccessFile(file, "r");
		FileChannel channel = raf.getChannel();
		ByteBuffer buf = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
		PDFFile pdffile = new PDFFile(buf);
		if(raf != null) raf.close();
		if(channel != null) channel.close();
		// long readfile = System.currentTimeMillis() - start;
		// draw the first page to an image
		PDFPage ppage = pdffile.getPage(0);

		log.debug("WIDTH " + (int) ppage.getBBox().getWidth() + " height " + (int) ppage.getBBox().getHeight());

		// get the width and height for the doc at the default zoom
		Rectangle rect = new Rectangle(0, 0, (int) ppage.getBBox().getWidth(), (int) ppage.getBBox().getHeight());

		log.debug("generate the image");
		Image img = ppage.getImage(rect.width, rect.height, // width & height
		        rect, // clip rect
		        null, // null for the ImageObserver
		        true, // fill background with white
		        true // block until drawing is done
		        );

		log.debug("about to Print to stream");
		File outfile = new File(documentCacheDir, d.getDocfilename() + ".png");

		OutputStream outs = null;
		try {
			outs = new FileOutputStream(outfile);

			RenderedImage rendImage = (RenderedImage) img;
			ImageIO.write(rendImage, "png", outs);
			outs.flush();
		} finally {
			if (outs != null) outs.close();
		}

		return outfile;

	}

	public ActionForward showPage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		return getPage(mapping, form, request, response, Integer.parseInt(request.getParameter("page")));
	}

	public ActionForward view(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response){
		return getPage(mapping, form, request, response, 1);
	}

	// PNG version
	public ActionForward getPage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, int pageNum) {

		try {
			String doc_no = request.getParameter("doc_no");
			log.debug("Document No :" + doc_no);

			LogAction.addLog((String) request.getSession().getAttribute("user"), LogConst.READ, LogConst.CON_DOCUMENT, doc_no, request.getRemoteAddr());

			Document d = documentDao.getDocument(doc_no);
			log.debug("Document Name :" + d.getDocfilename());

			File outfile = hasCacheVersion(d, pageNum);
			if (outfile == null){
				outfile = createCacheVersion2( d, pageNum);
			}
			response.setContentType("image/png");
			// response.setHeader("Content-Disposition", "attachment;filename=\"" + filename+ "\"");
			// read the file name.

			log.debug("about to Print to stream");
			ServletOutputStream outs = response.getOutputStream();

			response.setHeader("Content-Disposition", "attachment;filename=" + d.getDocfilename());
			BufferedInputStream bfis = null;
			try {
				bfis = new BufferedInputStream(new FileInputStream(outfile));
				int data;
				while ((data = bfis.read()) != -1) {
					outs.write(data);
					// outs.flush();
				}
			} finally {
				if (bfis!=null) bfis.close();
			}
			outs.flush();
			outs.close();
		} catch (java.net.SocketException se) {
			MiscUtils.getLogger().error("Error", se);
		} catch (Exception e) {
			MiscUtils.getLogger().error("Error", e);
		}
		return null;
	}

	public ActionForward viewDocPage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		log.debug("in viewDocPage");
		try {
			String doc_no = request.getParameter("doc_no");
			String pageNum = request.getParameter("curPage");
			if (pageNum == null) {
				pageNum = "0";
			}
			Integer pn = Integer.parseInt(pageNum);
			log.debug("Document No :" + doc_no);
			LogAction.addLog((String) request.getSession().getAttribute("user"), LogConst.READ, LogConst.CON_DOCUMENT, doc_no, request.getRemoteAddr());

			Document d = documentDao.getDocument(doc_no);
			log.debug("Document Name :" + d.getDocfilename());
			String name = d.getDocfilename() + "_" + pn + ".png";
			log.debug("name " + name);

			File outfile = null;

			outfile = hasCacheVersion2(d, pn);
			if (outfile != null) {
				log.debug("got doc from local cache   ");
			} else {
				outfile = createCacheVersion2(d, pn);
				if (outfile != null) {
					log.debug("create new doc  ");
				}
			}
			response.setContentType("image/png");
			ServletOutputStream outs = response.getOutputStream();
			response.setHeader("Content-Disposition", "attachment;filename=" + d.getDocfilename());

			BufferedInputStream bfis = null;
			try {
				if (outfile != null) {
					bfis = new BufferedInputStream(new FileInputStream(outfile));
					int data;
					while ((data = bfis.read()) != -1) {
						outs.write(data);
						// outs.flush();
					}
				} else {
					log.info("Unable to retrieve content for " + d + ". This may indicate previous upload or save errors...");
				}
			} finally {
				if (bfis!=null) {
					bfis.close();
				}
			}

			outs.flush();
			outs.close();
		} catch (java.net.SocketException se) {
			MiscUtils.getLogger().error("Error", se);
		} catch (Exception e) {
			MiscUtils.getLogger().error("Error", e);
		}
		return null;
	}

	public ActionForward view2(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		// TODO: NEED TO CHECK FOR ACCESS
		String doc_no = request.getParameter("doc_no");
		log.debug("Document No :" + doc_no);

		LogAction.addLog((String) request.getSession().getAttribute("user"), LogConst.READ, LogConst.CON_DOCUMENT, doc_no, request.getRemoteAddr());

		String docdownload = oscar.OscarProperties.getInstance().getProperty("DOCUMENT_DIR");
		File documentDir = new File(docdownload);
		log.debug("Document Dir is a dir" + documentDir.isDirectory());

		Document d = documentDao.getDocument(doc_no);
		log.debug("Document Name :" + d.getDocfilename());

		// TODO: Right now this assumes it's a pdf which it shouldn't

		response.setContentType("image/png");
		// response.setHeader("Content-Disposition", "attachment;filename=\"" + filename+ "\"");
		// read the file name.
		File file = new File(documentDir, d.getDocfilename());

		RandomAccessFile raf = new RandomAccessFile(file, "r");
		FileChannel channel = raf.getChannel();
		ByteBuffer buf = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
		PDFFile pdffile = new PDFFile(buf);
		// long readfile = System.currentTimeMillis() - start;
		// draw the first page to an image
		PDFPage ppage = pdffile.getPage(0);

		log.debug("WIDTH " + (int) ppage.getBBox().getWidth() + " height " + (int) ppage.getBBox().getHeight());

		// get the width and height for the doc at the default zoom
		Rectangle rect = new Rectangle(0, 0, (int) ppage.getBBox().getWidth(), (int) ppage.getBBox().getHeight());

		log.debug("generate the image");
		Image img = ppage.getImage(rect.width, rect.height, // width & height
		        rect, // clip rect
		        null, // null for the ImageObserver
		        true, // fill background with white
		        true // block until drawing is done
		        );

		log.debug("about to Print to stream");
		ServletOutputStream outs = response.getOutputStream();

		RenderedImage rendImage = (RenderedImage) img;
		ImageIO.write(rendImage, "png", outs);
		outs.flush();
		outs.close();
		return null;
	}

	public ActionForward getDocPageNumber(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

		String doc_no = request.getParameter("doc_no");
		String docdownload = oscar.OscarProperties.getInstance().getProperty("DOCUMENT_DIR");
		// File documentDir = new File(docdownload);
		Document d = documentDao.getDocument(doc_no);
		String filePath = docdownload + d.getDocfilename();

		int numOfPage = 0;
		try {
			PdfReader reader = new PdfReader(filePath);
			numOfPage = reader.getNumberOfPages();

			HashMap hm = new HashMap();
			hm.put("numOfPage", numOfPage);
			JSONObject jsonObject = JSONObject.fromObject(hm);
			response.getOutputStream().write(jsonObject.toString().getBytes());
		} catch (IOException e) {
			MiscUtils.getLogger().error("Error", e);
		}

		return null;// execute2(mapping, form, request, response);
	}
	
	public ActionForward downloadCDS(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		DemographicExport export = SharingCenterUtil.retrieveDemographicExport(Integer.valueOf(request.getParameter("doc_no")));
		String contentType = "application/zip";
		String filename = String.format("%s_%s", export.getDocumentType(), export.getId());
		byte[] contentBytes = export.getDocument();

		response.setContentType(contentType);
		response.setContentLength(contentBytes.length);
		response.setHeader("Content-Disposition", "inline; filename=" + filename);
		log.debug("about to Print to stream");
		ServletOutputStream outs = response.getOutputStream();
		outs.write(contentBytes);
		outs.flush();
		outs.close();
		return null;
	}

	public ActionForward display(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
		
		String temp = request.getParameter("remoteFacilityId");
		Integer remoteFacilityId = null;
		if (temp != null) remoteFacilityId = Integer.parseInt(temp);

		String doc_no = request.getParameter("doc_no");
		log.debug("Document No :" + doc_no);

		String docxml = null;
		String contentType = null;
		byte[] contentBytes = null;
		String filename = null;

		// local document
		if (remoteFacilityId == null) {
			CtlDocument ctld = ctlDocumentDao.getCtrlDocument(Integer.parseInt(doc_no));
			if (ctld.isDemographicDocument()) {
				LogAction.addLog((String) request.getSession().getAttribute("user"), LogConst.READ, LogConst.CON_DOCUMENT, doc_no, request.getRemoteAddr(), "" + ctld.getId().getModuleId());
			} else {
				LogAction.addLog((String) request.getSession().getAttribute("user"), LogConst.READ, LogConst.CON_DOCUMENT, doc_no, request.getRemoteAddr());
			}

			String docdownload = oscar.OscarProperties.getInstance().getProperty("DOCUMENT_DIR");

			File documentDir = new File(docdownload);
			log.debug("Document Dir is a dir" + documentDir.isDirectory());

			Document d = documentDao.getDocument(doc_no);
			log.debug("Document Name :" + d.getDocfilename());

			docxml = d.getDocxml();
			contentType = d.getContenttype();

			File file = new File(documentDir, d.getDocfilename());
			filename = d.getDocfilename();
                        
                        if (contentType != null && !contentType.trim().equals("text/html")) {
                            if (file.exists()) {
                            	contentBytes = FileUtils.readFileToByteArray(file);
                            } else {
                            	throw new IllegalStateException("Local document doesn't exist for eDoc (ID " + d.getId() + "): " + file.getAbsolutePath());
                            }
                        }
		} else // remote document
		{
			FacilityIdIntegerCompositePk remotePk = new FacilityIdIntegerCompositePk();
			remotePk.setIntegratorFacilityId(remoteFacilityId);
			remotePk.setCaisiItemId(Integer.parseInt(doc_no));

			
			CachedDemographicDocument remoteDocument = null;
			CachedDemographicDocumentContents remoteDocumentContents = null;

			try {
				if (!CaisiIntegratorManager.isIntegratorOffline(request.getSession())){
					DemographicWs demographicWs = CaisiIntegratorManager.getDemographicWs(loggedInInfo, loggedInInfo.getCurrentFacility());
					remoteDocument = demographicWs.getCachedDemographicDocument(remotePk);
					remoteDocumentContents = demographicWs.getCachedDemographicDocumentContents(remotePk);
				}
			} catch (Exception e) {
				MiscUtils.getLogger().error("Unexpected error.", e);
				CaisiIntegratorManager.checkForConnectionError(request.getSession(),e);
			}

			if(CaisiIntegratorManager.isIntegratorOffline(request.getSession())){
				Integer demographicId = IntegratorFallBackManager.getDemographicNoFromRemoteDocument(loggedInInfo,remotePk);
				MiscUtils.getLogger().debug("got demographic no from remote document "+demographicId);
				List<CachedDemographicDocument> remoteDocuments = IntegratorFallBackManager.getRemoteDocuments(loggedInInfo,demographicId);
				for(CachedDemographicDocument demographicDocument: remoteDocuments){
					if(demographicDocument.getFacilityIntegerPk().getIntegratorFacilityId() == remotePk.getIntegratorFacilityId() && demographicDocument.getFacilityIntegerPk().getCaisiItemId() == remotePk.getCaisiItemId() ){
						remoteDocument = demographicDocument;
						remoteDocumentContents = IntegratorFallBackManager.getRemoteDocument(loggedInInfo,demographicId, remotePk);
						break;
					}
					MiscUtils.getLogger().error("End of the loop and didn't find the remoteDocument");
				}
			}


			docxml = remoteDocument.getDocXml();
			contentType = remoteDocument.getContentType();
			filename = remoteDocument.getDocFilename();
			contentBytes = remoteDocumentContents.getFileContents();
		}

		if (docxml != null && !docxml.trim().equals("")) {
			ServletOutputStream outs = response.getOutputStream();
			outs.write(docxml.getBytes());
			outs.flush();
			outs.close();
			return null;
		}

		// TODO: Right now this assumes it's a pdf which it shouldn't
		if (contentType == null) {
			contentType = "application/pdf";
		}

		response.setContentType(contentType);
		response.setContentLength(contentBytes.length);
		response.setHeader("Content-Disposition", "inline; filename=" + filename);
		log.debug("about to Print to stream");
		ServletOutputStream outs = response.getOutputStream();
		outs.write(contentBytes);
		outs.flush();
		outs.close();
		return null;
	}

        public ActionForward viewDocumentInfo(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
                response.setContentType("text/html");
		doViewDocumentInfo(request, response.getWriter(),true,true);
		return null;
	}
        
        public ActionForward viewDocumentDescription(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
                response.setContentType("text/html");
		doViewDocumentInfo(request, response.getWriter(),false,true);
		return null;
	}
        public ActionForward viewAnnotationAcknowledgementTickler(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
                response.setContentType("text/html");
		doViewDocumentInfo(request, response.getWriter(),true,false);
		return null;
	}
     
        public void doViewDocumentInfo(HttpServletRequest request, PrintWriter out,boolean viewAnnotationAcknowledgementTicklerFlag, boolean viewDocumentDescriptionFlag) {
        	LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
            String doc_no = request.getParameter("doc_no");
            Locale locale=request.getLocale();
                
            String annotation= "",acknowledgement="",tickler="";
                if(doc_no!=null && doc_no.length()>0) { 
                    annotation=EDocUtil.getHtmlAnnotation(doc_no);
                    acknowledgement=EDocUtil.getHtmlAcknowledgement(locale,doc_no);
                    if(acknowledgement==null) {acknowledgement="";}
                    tickler=EDocUtil.getHtmlTicklers(loggedInInfo, doc_no);
                }
                
                out.println("<!DOCTYPE html><html><head><meta http-equiv='Content-Type' content='text/html; charset=UTF-8'></head><body>");
                
                if(viewAnnotationAcknowledgementTicklerFlag) {
                    if(annotation.length()>0)  {
                        out.println(annotation);
                    }
                    if(tickler.length()>0)
                    {
                        out.println(tickler+"<br>");
                    }
                    if(acknowledgement.length()>0) {
                        out.println(acknowledgement+"<br>");
                    }
                }
                
                if(viewDocumentDescriptionFlag) {
                    EDoc curDoc= EDocUtil.getDoc(doc_no);
                    ResourceBundle props = ResourceBundle.getBundle("oscarResources", locale);
                    out.println("<br>"+props.getString("dms.documentBrowser.DocumentUpdated")+": "+curDoc.getDateTimeStamp());
                    out.println("<br>"+props.getString("dms.documentBrowser.ContentUpdated")+": "+curDoc.getContentDateTime());
                    out.println("<br>"+props.getString("dms.documentBrowser.ObservationDate")+": "+curDoc.getObservationDate());
                    out.println("<br>"+props.getString("dms.documentBrowser.Type")+": "+curDoc.getType());
                    out.println("<br>"+props.getString("dms.documentBrowser.Class")+": "+curDoc.getDocClass());
                    out.println("<br>"+props.getString("dms.documentBrowser.Subclass")+": "+curDoc.getDocSubClass());
                    out.println("<br>"+props.getString("dms.documentBrowser.Description")+": "+curDoc.getDescription());
                    out.println("<br>"+props.getString("dms.documentBrowser.Creator")+": "+curDoc.getCreatorName());
                    out.println("<br>"+props.getString("dms.documentBrowser.Responsible")+": "+curDoc.getResponsibleName());
                    out.println("<br>"+props.getString("dms.documentBrowser.Reviewer")+": "+curDoc.getReviewerName());
                    out.println("<br>"+props.getString("dms.documentBrowser.Source")+": "+curDoc.getSource());
                }
                
                out.println("</body></html>");
		out.flush();
                out.close();
                
        }

        public ActionForward addIncomingDocument(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        String pdfDir = request.getParameter("pdfDir");
        String pdfName = request.getParameter("pdfName");
        String demographic_no = request.getParameter("demog");
        String observationDate = request.getParameter("observationDate");
        String documentDescription = request.getParameter("documentDescription");
        String docType = request.getParameter("docType");
        String docClass = request.getParameter("docClass");
        String docSubClass = request.getParameter("docSubClass");
        String[] flagproviders = request.getParameterValues("flagproviders");
        String queueId1 = request.getParameter("queueId");
        String sourceFilePath = IncomingDocUtil.getIncomingDocumentFilePathName(queueId1, pdfDir, pdfName);
        String destFilePath;

        String savePath = oscar.OscarProperties.getInstance().getProperty("DOCUMENT_DIR");
        if (!savePath.endsWith(File.separator)) {
            savePath += File.separator;
        }

        Date obDate = UtilDateUtilities.StringToDate(observationDate);
        String formattedDate = UtilDateUtilities.DateToString(obDate, EDocUtil.DMS_DATE_FORMAT);
        String source = "";


        int numberOfPages = 0;
        String fileName = pdfName;
        String user = (String) request.getSession().getAttribute("user");
        EDoc newDoc = new EDoc(documentDescription, docType, fileName, "", user, user, source, 'A', formattedDate, "", "", "demographic", demographic_no, 0);
        newDoc.setDocClass(docClass);
        newDoc.setDocSubClass(docSubClass);
        newDoc.setDocPublic("0");
        fileName = newDoc.getFileName();
        destFilePath = savePath + fileName;
        String doc_no = "";


        newDoc.setContentType(docType);
        File f1 = new File(sourceFilePath);
        boolean success = f1.renameTo(new File(destFilePath));
        if (!success) {
            log.error("Not able to move " + f1.getName() + " to " + destFilePath);
            // File was not successfully moved
        } else {

            newDoc.setContentType("application/pdf");
            if (fileName.endsWith(".PDF") || fileName.endsWith(".pdf")) {
                newDoc.setContentType("application/pdf");
                numberOfPages = countNumOfPages(fileName);
            }
            newDoc.setNumberOfPages(numberOfPages);
            doc_no = EDocUtil.addDocumentSQL(newDoc);
            LogAction.addLog((String) request.getSession().getAttribute("user"), LogConst.ADD, LogConst.CON_DOCUMENT, doc_no, request.getRemoteAddr());


            if (flagproviders != null && flagproviders.length > 0) { 
                try {
                    for (String proNo : flagproviders) {
                        providerInboxRoutingDAO.addToProviderInbox(proNo, Integer.parseInt(doc_no), LabResultData.DOCUMENT);
                    }
                } catch (Exception e) {
                    MiscUtils.getLogger().error("Error", e);
                }
            }

            //Check to see if we have to route document to patient
            PatientLabRoutingDao patientLabRoutingDao = SpringUtils.getBean(PatientLabRoutingDao.class);
            List<PatientLabRouting> patientLabRoutingList = patientLabRoutingDao.findByLabNoAndLabType(Integer.parseInt(doc_no), docType);
            if (patientLabRoutingList == null || patientLabRoutingList.isEmpty()) {
                PatientLabRouting patientLabRouting = new PatientLabRouting();
                patientLabRouting.setDemographicNo(Integer.parseInt(demographic_no));
                patientLabRouting.setLabNo(Integer.parseInt(doc_no));
                patientLabRouting.setLabType("DOC");
                patientLabRoutingDao.persist(patientLabRouting);
            }

            try {

                CtlDocument ctlDocument = ctlDocumentDao.getCtrlDocument(Integer.parseInt(doc_no));

                ctlDocument.getId().setModuleId(Integer.parseInt(demographic_no));
                ctlDocumentDao.merge(ctlDocument);
                //save a document created note
                if (ctlDocument.isDemographicDocument()) {
                    //save note
                    saveDocNote(request, documentDescription, demographic_no, doc_no);
                }
            } catch (Exception e) {
                MiscUtils.getLogger().error("Error", e);
            }
        }

        return (mapping.findForward("nextIncomingDoc"));
    }
        
    public ActionForward viewIncomingDocPageAsPdf(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {


        String pageNum = request.getParameter("curPage");
        String queueId = request.getParameter("queueId");
        String pdfDir = request.getParameter("pdfDir");
        String pdfName = request.getParameter("pdfName");
        String filePath = IncomingDocUtil.getIncomingDocumentFilePathName(queueId, pdfDir, pdfName);
        Locale locale=request.getLocale();
        ResourceBundle props = ResourceBundle.getBundle("oscarResources", locale);
        
        if (pageNum == null) {
            pageNum = "0";
        }

        Integer pn = Integer.parseInt(pageNum);

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "inline; filename=\"" + pdfName + UtilDateUtilities.getToday("yyyy-MM-dd.hh.mm.ss") + ".pdf\"");
        
        com.itextpdf.text.pdf.PdfCopy extractCopy = null;
        com.itextpdf.text.Document document = null;
        com.itextpdf.text.pdf.PdfReader reader = null;

        try {
            reader = new com.itextpdf.text.pdf.PdfReader(filePath);
            document = new com.itextpdf.text.Document(reader.getPageSizeWithRotation(1));
            extractCopy = new com.itextpdf.text.pdf.PdfCopy(document, response.getOutputStream());

            document.open();
            extractCopy.addPage(extractCopy.getImportedPage(reader, pn));


        } catch (Exception ex) {
            response.setContentType("text/html");
            response.getWriter().print(props.getString("dms.incomingDocs.errorInOpening") + pdfName);
            response.getWriter().print("<br>"+props.getString("dms.incomingDocs.PDFCouldBeCorrupted"));

            MiscUtils.getLogger().error("Error", ex);
        } finally {
            if (extractCopy != null) {
                extractCopy.close();
            }
            if (document != null) {
                document.close();
            }
            if (reader != null) {
                reader.close();
            }
        }

        return null;
    }

    public int countNumOfPages(String fileName) { // count number of pages in a pdf file
        int numOfPage = 0;
        String docdownload = oscar.OscarProperties.getInstance().getProperty("DOCUMENT_DIR");

        if (!docdownload.endsWith(File.separator)) {
            docdownload += File.separator;
        }

        String filePath = docdownload + fileName;
        PdfReader reader = null;

        try {
            reader = new PdfReader(filePath);
            numOfPage = reader.getNumberOfPages();

        } catch (IOException e) {
            MiscUtils.getLogger().error("Error", e);
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
        return numOfPage;
    }

    public ActionForward displayIncomingDocs(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

        String queueId = request.getParameter("queueId");
        String pdfDir = request.getParameter("pdfDir");
        String pdfName = request.getParameter("pdfName");
        String filePath = IncomingDocUtil.getIncomingDocumentFilePathName(queueId, pdfDir, pdfName);

        String contentType = "application/pdf";
        File file = new File(filePath);

        response.setContentType(contentType);
        response.setContentLength((int) file.length());
        response.setHeader("Content-Disposition", "inline; filename=" + pdfName);

        BufferedInputStream bfis = null;
        ServletOutputStream outs = response.getOutputStream();

        try {

            bfis = new BufferedInputStream(new FileInputStream(file));

            org.apache.commons.io.IOUtils.copy(bfis,outs);
            outs.flush();
            
        } catch (Exception e) {
            MiscUtils.getLogger().error("Error", e);
        } finally {
            if (bfis != null) {
                bfis.close();
            }
        }

        return null;
    }
        
    public ActionForward viewIncomingDocPageAsImage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {


        String pageNum = request.getParameter("curPage");
        String queueId = request.getParameter("queueId");
        String pdfDir = request.getParameter("pdfDir");
        String pdfName = request.getParameter("pdfName");

        if (pageNum == null) {
            pageNum = "0";
        }

        BufferedInputStream bfis = null;
        ServletOutputStream outs = null;

        try {
            Integer pn = Integer.parseInt(pageNum);
            File outfile = createIncomingCacheVersion(queueId, pdfDir, pdfName, pn);
            outs = response.getOutputStream();

            if (outfile != null) {
                bfis = new BufferedInputStream(new FileInputStream(outfile));


                response.setContentType("image/png");
                response.setHeader("Content-Disposition", "inline;filename=" + pdfName);
                org.apache.commons.io.IOUtils.copy(bfis,outs);
                outs.flush();
                
            } else {
                log.info("Unable to retrieve content for " + queueId + "/" + pdfDir + "/" + pdfName);
            }
        } catch (Exception e) {
            MiscUtils.getLogger().error("Error", e);

        } finally {
            if (bfis != null) {
                bfis.close();
            }
        }
        return null;
    }

    public File createIncomingCacheVersion(String queueId, String pdfDir, String pdfName, Integer pageNum) throws Exception {


        String incomingDocPath = IncomingDocUtil.getIncomingDocumentFilePath(queueId, pdfDir);
        File documentDir = new File(incomingDocPath);
        File documentCacheDir = getDocumentCacheDir(incomingDocPath);
        File file = new File(documentDir, pdfName);

        PdfDecoder decode_pdf = new PdfDecoder(true);
        File ofile = null;
        FileInputStream is = null;
        
        try {

            FontMappings.setFontReplacements();

            decode_pdf.useHiResScreenDisplay(true);

            decode_pdf.setExtractionMode(0, 96, 96 / 72f);

            is = new FileInputStream(file);

            decode_pdf.openPdfFileFromInputStream(is, false);

            BufferedImage image_to_save = decode_pdf.getPageAsImage(pageNum);

            decode_pdf.getObjectStore().saveStoredImage(documentCacheDir.getCanonicalPath() + "/" + pdfName + "_" + pageNum + ".png", image_to_save, true, false, "png");

            decode_pdf.flushObjectValues(true);

        } catch (Exception e) {
            log.error("Error decoding pdf file " + pdfDir + pdfName);
        } finally {
            if (decode_pdf != null) {
                decode_pdf.closePdfFile();
            }
            if (is!=null) {
                is.close();
            }
                
        }
        
        ofile = new File(documentCacheDir, pdfName + "_" + pageNum + ".png");
        
        return ofile;

    }
}
