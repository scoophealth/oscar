/**
 * Copyright (c) 2008-2012 Indivica Inc.
 *
 * This software is made available under the terms of the
 * GNU General Public License, Version 2, 1991 (GPLv2).
 * License details are available via "indivica.ca/gplv2"
 * and "gnu.org/licenses/gpl-2.0.html".
 */

package oscar.dms.actions;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.upload.FormFile;
import org.oscarehr.common.dao.ProviderInboxRoutingDao;
import org.oscarehr.common.dao.QueueDocumentLinkDao;
import org.oscarehr.util.MiscUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import oscar.dms.EDoc;
import oscar.dms.EDocUtil;
import oscar.dms.data.DocumentUploadForm;
import oscar.log.LogAction;
import oscar.log.LogConst;

import com.lowagie.text.pdf.PdfReader;

public class DocumentUploadAction extends DispatchAction {
	
	private static Logger logger = MiscUtils.getLogger();
	
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
								 HttpServletResponse response) throws Exception {
		DocumentUploadForm fm = (DocumentUploadForm) form;

		HashMap<String,Object>map = new HashMap<String,Object>();
		FormFile docFile = fm.getFiledata();
		if (docFile == null) { 
			map.put("error", 4);
		}
		else {
			int numberOfPages = 0;
			String fileName = docFile.getFileName();
			String user = (String) request.getSession().getAttribute("user");
			EDoc newDoc = new EDoc("", "", fileName, "", user, user, fm.getSource(), 'A',
								   oscar.util.UtilDateUtilities.getToday("yyyy-MM-dd"), "", "", "demographic", "-1", 0);
			newDoc.setDocPublic("0");
			fileName = newDoc.getFileName();
			// save local file;
			if (docFile.getFileSize() == 0) {
				map.put("error", 4);
				throw new FileNotFoundException();
			}
	
			// write file to local dir
			writeLocalFile(docFile, fileName);
			newDoc.setContentType(docFile.getContentType());
			if (fileName.endsWith(".PDF") || fileName.endsWith(".pdf")) {
				newDoc.setContentType("application/pdf");
				// get number of pages when document is a PDF
				numberOfPages = countNumOfPages(fileName);
			}
			newDoc.setNumberOfPages(numberOfPages);
			String doc_no = EDocUtil.addDocumentSQL(newDoc);
			LogAction.addLog((String) request.getSession().getAttribute("user"), LogConst.ADD, LogConst.CON_DOCUMENT, doc_no, request.getRemoteAddr());
	
			String providerId = request.getParameter("provider");
			if (providerId != null) { 
				WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getSession().getServletContext());
				ProviderInboxRoutingDao providerInboxRoutingDao = (ProviderInboxRoutingDao) ctx.getBean("providerInboxRoutingDAO");
				providerInboxRoutingDao.addToProviderInbox(providerId, doc_no, "DOC");
			}
	
			String queueId=request.getParameter("queue");
	        if (queueId !=null &&!queueId.equals("-1")) {
	            WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getSession().getServletContext());
	            QueueDocumentLinkDao queueDocumentLinkDAO = (QueueDocumentLinkDao) ctx.getBean("queueDocumentLinkDAO");
	            Integer qid=Integer.parseInt(queueId.trim());
	            Integer did=Integer.parseInt(doc_no.trim());
	            queueDocumentLinkDAO.addToQueueDocumentLink(qid,did);
	            request.getSession().setAttribute("preferredQueue", queueId);
	        }
			
	        map.put("name", docFile.getFileName());
	        map.put("size", docFile.getFileSize());
	        
			if (docFile != null) {
				docFile.destroy();
				docFile = null;
			}
		}
		JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = JSONObject.fromObject(map);        
        jsonArray.add(jsonObject);
        response.getOutputStream().write(jsonArray.toString().getBytes());
		return null;
	}

	/**
	 * Counts the number of pages in a local pdf file.
	 * @param fileName the name of the file
	 * @return the number of pages in the file
	 */
	public int countNumOfPages(String fileName) {// count number of pages in a
													// local pdf file
		int numOfPage = 0;
		String docdownload = oscar.OscarProperties.getInstance().getProperty("DOCUMENT_DIR");
		String filePath = docdownload + fileName;

		try {
			PdfReader reader = new PdfReader(filePath);
			numOfPage = reader.getNumberOfPages();
			reader.close();
		} catch (IOException e) {
			logger.debug(e.toString());
		}
		return numOfPage;
	}

	/**
	 * Writes an uploaded file to disk
	 * @param docFile the uploaded file
	 * @param fileName the name for the file on disk
	 * @throws Exception when an error occurs
	 */
	private void writeLocalFile(FormFile docFile, String fileName) throws Exception {
		InputStream fis = null;
		FileOutputStream fos = null;
		try {
			fis = docFile.getInputStream();
			String savePath = oscar.OscarProperties.getInstance().getProperty("DOCUMENT_DIR") + "/" + fileName;
			fos = new FileOutputStream(savePath);
			byte[] buf = new byte[128 * 1024];
			int i = 0;
			while ((i = fis.read(buf)) != -1) {
				fos.write(buf, 0, i);
			}
		} catch (Exception e) {
			logger.debug(e.toString());
		} finally {
			if (fis != null)
				fis.close();
			if (fos != null)
				fos.close();
		}
	}
}