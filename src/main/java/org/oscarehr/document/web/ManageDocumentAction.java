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
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

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
import org.oscarehr.common.dao.PatientLabRoutingDao;
import org.oscarehr.common.dao.ProviderInboxRoutingDao;
import org.oscarehr.common.dao.SecRoleDao;
import org.oscarehr.common.model.PatientLabRouting;
import org.oscarehr.common.model.Provider;
import org.oscarehr.common.model.SecRole;
import org.oscarehr.document.dao.DocumentDAO;
import org.oscarehr.document.model.CtlDocument;
import org.oscarehr.document.model.Document;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import oscar.dms.EDocUtil;
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

	private DocumentDAO documentDAO = null;
	private ProviderInboxRoutingDao providerInboxRoutingDAO = null;

	public void setDocumentDAO(DocumentDAO documentDAO) {
		this.documentDAO = documentDAO;
	}

	public void setProviderInboxRoutingDAO(ProviderInboxRoutingDao providerInboxRoutingDAO) {
		this.providerInboxRoutingDAO = providerInboxRoutingDAO;
	}

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
					providerInboxRoutingDAO.addToProviderInbox(proNo, documentId, LabResultData.DOCUMENT);
				}

				// Removes the link to the "0" provider so that the document no longer shows up as "unclaimed"
				providerInboxRoutingDAO.removeLinkFromDocument("DOC", documentId, "0");
			} catch (Exception e) {
				MiscUtils.getLogger().error("Error", e);
			}
		}
                
		//Check to see if we have to route document to patient
		PatientLabRoutingDao patientLabRoutingDao = SpringUtils.getBean(PatientLabRoutingDao.class);
		PatientLabRouting patientLabRouting = patientLabRoutingDao.findDemographics(docType, Integer.parseInt(documentId));
		if(patientLabRouting == null) {
			patientLabRouting = new PatientLabRouting();
			patientLabRouting.setDemographicNo(Integer.parseInt(demog));
			patientLabRouting.setLabNo(Integer.parseInt(documentId));
			patientLabRouting.setLabType("DOC");
			patientLabRoutingDao.persist(patientLabRouting);
		}
                
		Document d = documentDAO.getDocument(documentId);

		d.setDocdesc(documentDescription);
		d.setDoctype(docType);
		Date obDate = UtilDateUtilities.StringToDate(observationDate);

		if (obDate != null) {
			d.setObservationdate(obDate);
		}

		documentDAO.save(d);

		try {

			CtlDocument ctlDocument = documentDAO.getCtrlDocument(Integer.parseInt(documentId));

			ctlDocument.setModuleId(Integer.parseInt(demog));
			documentDAO.saveCtlDocument(ctlDocument);
			// save a document created note
			if (ctlDocument.isDemographicDocument()) {
				// save note
				saveDocNote(request, d.getDocdesc(), demog, documentId);
			}
		} catch (Exception e) {
			MiscUtils.getLogger().error("Error", e);
		}
		if (flagproviders != null) {
			for (String str : flagproviders) {

			}
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
		hm.put("demoName", getDemoName(dn));
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

		providerInboxRoutingDAO.removeLinkFromDocument(docType, docId, providerNo);
		HashMap hm = new HashMap();
		hm.put("linkedProviders", providerInboxRoutingDAO.getProvidersWithRoutingForDocument(docType, docId));

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
					providerInboxRoutingDAO.addToProviderInbox(proNo, documentId, LabResultData.DOCUMENT);
				}
			} catch (Exception e) {
				MiscUtils.getLogger().error("Error", e);
			}
		}
		Document d = documentDAO.getDocument(documentId);

		d.setDocdesc(documentDescription);
		d.setDoctype(docType);
		Date obDate = UtilDateUtilities.StringToDate(observationDate);

		if (obDate != null) {
			d.setObservationdate(obDate);
		}

		documentDAO.save(d);

		try {

			CtlDocument ctlDocument = documentDAO.getCtrlDocument(Integer.parseInt(documentId));

			ctlDocument.setModuleId(Integer.parseInt(demog));
			documentDAO.saveCtlDocument(ctlDocument);
			// save a document created note
			if (ctlDocument.isDemographicDocument()) {
				// save note
				saveDocNote(request, d.getDocdesc(), demog, documentId);
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
		String demoName = getDemoName(demog);
		request.setAttribute("demoName", demoName);
		request.setAttribute("segmentID", documentId);
		request.setAttribute("providerNo", providerNo);
		request.setAttribute("searchProviderNo", searchProviderNo);
		request.setAttribute("status", ackStatus);

		return mapping.findForward("displaySingleDoc");

	}

	private String getDemoName(String demog) {
		DemographicData demoD = new DemographicData();
		org.oscarehr.common.model.Demographic demo = demoD.getDemographic(demog);
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
		cmm.saveNoteSimple(cmn);
		// Add a noteLink to casemgmt_note_link
		CaseManagementNoteLink cmnl = new CaseManagementNoteLink();
		cmnl.setTableName(CaseManagementNoteLink.DOCUMENT);
		cmnl.setTableId(Long.parseLong(documentId));
		cmnl.setNoteId(cmn.getId());
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

	private File hasCacheVersion(Document d) {
		File documentCacheDir = getDocumentCacheDir(oscar.OscarProperties.getInstance().getProperty("DOCUMENT_DIR"));
		File outfile = new File(documentCacheDir, d.getDocfilename() + ".png");
		if (!outfile.exists()) {
			outfile = null;
		}
		return outfile;
	}


	public static void deleteCacheVersion(org.oscarehr.document.model.Document d, int pageNum) {
		File documentCacheDir = getDocumentCacheDir(oscar.OscarProperties.getInstance().getProperty("DOCUMENT_DIR"));
		//pageNum=pageNum-1;
		File outfile = new File(documentCacheDir,d.getDocfilename()+"_"+pageNum+".png");
		if (outfile.exists()){
			outfile.delete();
		}
	}

	private File hasCacheVersion(org.oscarehr.document.model.Document d, int pageNum){
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
		return getPage(mapping, form, request, response, 1);//Jpedal index for first page is 1 (not 0)
	}

	// PNG version
	public ActionForward getPage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, int pageNum) {

		try {
			String doc_no = request.getParameter("doc_no");
			log.debug("Document No :" + doc_no);

			LogAction.addLog((String) request.getSession().getAttribute("user"), LogConst.READ, LogConst.CON_DOCUMENT, doc_no, request.getRemoteAddr());

			Document d = documentDAO.getDocument(doc_no);
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

			Document d = documentDAO.getDocument(doc_no);
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

	public ActionForward view2(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		// TODO: NEED TO CHECK FOR ACCESS
		String doc_no = request.getParameter("doc_no");
		log.debug("Document No :" + doc_no);

		LogAction.addLog((String) request.getSession().getAttribute("user"), LogConst.READ, LogConst.CON_DOCUMENT, doc_no, request.getRemoteAddr());

		String docdownload = oscar.OscarProperties.getInstance().getProperty("DOCUMENT_DIR");
		File documentDir = new File(docdownload);
		log.debug("Document Dir is a dir" + documentDir.isDirectory());

		Document d = documentDAO.getDocument(doc_no);
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
		Document d = documentDAO.getDocument(doc_no);
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

	public ActionForward display(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

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
			CtlDocument ctld = documentDAO.getCtrlDocument(Integer.parseInt(doc_no));
			if (ctld.isDemographicDocument()) {
				LogAction.addLog((String) request.getSession().getAttribute("user"), LogConst.READ, LogConst.CON_DOCUMENT, doc_no, request.getRemoteAddr(), "" + ctld.getModuleId());
			} else {
				LogAction.addLog((String) request.getSession().getAttribute("user"), LogConst.READ, LogConst.CON_DOCUMENT, doc_no, request.getRemoteAddr());
			}

			String docdownload = oscar.OscarProperties.getInstance().getProperty("DOCUMENT_DIR");

			File documentDir = new File(docdownload);
			log.debug("Document Dir is a dir" + documentDir.isDirectory());

			Document d = documentDAO.getDocument(doc_no);
			log.debug("Document Name :" + d.getDocfilename());

			docxml = d.getDocxml();
			contentType = d.getContenttype();

			File file = new File(documentDir, d.getDocfilename());
			filename = d.getDocfilename();

			if (file.exists()) contentBytes = FileUtils.readFileToByteArray(file);

		} else // remote document
		{
			FacilityIdIntegerCompositePk remotePk = new FacilityIdIntegerCompositePk();
			remotePk.setIntegratorFacilityId(remoteFacilityId);
			remotePk.setCaisiItemId(Integer.parseInt(doc_no));

			LoggedInInfo loggedInInfo = LoggedInInfo.loggedInInfo.get();

			CachedDemographicDocument remoteDocument = null;
			CachedDemographicDocumentContents remoteDocumentContents = null;

			try {
				if (!CaisiIntegratorManager.isIntegratorOffline()){
					DemographicWs demographicWs = CaisiIntegratorManager.getDemographicWs();
					remoteDocument = demographicWs.getCachedDemographicDocument(remotePk);
					remoteDocumentContents = demographicWs.getCachedDemographicDocumentContents(remotePk);
				}
			} catch (Exception e) {
				MiscUtils.getLogger().error("Unexpected error.", e);
				CaisiIntegratorManager.checkForConnectionError(e);
			}

			if(CaisiIntegratorManager.isIntegratorOffline()){
				Integer demographicId = IntegratorFallBackManager.getDemographicNoFromRemoteDocument(remotePk);
				MiscUtils.getLogger().debug("got demographic no from remote document "+demographicId);
				List<CachedDemographicDocument> remoteDocuments = IntegratorFallBackManager.getRemoteDocuments(demographicId);
				for(CachedDemographicDocument demographicDocument: remoteDocuments){
					if(demographicDocument.getFacilityIntegerPk().getIntegratorFacilityId() == remotePk.getIntegratorFacilityId() && demographicDocument.getFacilityIntegerPk().getCaisiItemId() == remotePk.getCaisiItemId() ){
						remoteDocument = demographicDocument;
						remoteDocumentContents = IntegratorFallBackManager.getRemoteDocument(demographicId, remotePk);
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
		response.setHeader("Content-Disposition", "inline; filename=\"" + filename + "\"");
		log.debug("about to Print to stream");
		ServletOutputStream outs = response.getOutputStream();
		outs.write(contentBytes);
		outs.flush();
		outs.close();
		return null;
	}

}
