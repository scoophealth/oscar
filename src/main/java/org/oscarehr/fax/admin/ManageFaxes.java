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
package org.oscarehr.fax.admin;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.lang.time.DateUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.jpedal.PdfDecoder;
import org.jpedal.exception.PdfException;
import org.jpedal.fonts.FontMappings;
import org.oscarehr.common.dao.FaxConfigDao;
import org.oscarehr.common.dao.FaxJobDao;
import org.oscarehr.common.model.FaxConfig;
import org.oscarehr.common.model.FaxJob;
import org.oscarehr.managers.SecurityInfoManager;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.log.LogAction;
import oscar.log.LogConst;

public class ManageFaxes extends DispatchAction {
	
	private Logger log = MiscUtils.getLogger();
	private SecurityInfoManager securityInfoManager = SpringUtils.getBean(SecurityInfoManager.class);
	 
	
	private File hasCacheVersion2(FaxJob faxJob, Integer pageNum) {
		int index;
		String filename;
		if( (index = faxJob.getFile_name().lastIndexOf("/")) > -1 ) {
			filename = faxJob.getFile_name().substring(index+1);
		}
		else {
			filename = faxJob.getFile_name();
		}
		File documentCacheDir = getDocumentCacheDir(oscar.OscarProperties.getInstance().getProperty("DOCUMENT_DIR"));
		File outfile = new File(documentCacheDir, filename + "_" + pageNum + ".png");
		if (!outfile.exists()) {
			outfile = null;
		}
		return outfile;
	}
	
	private File getDocumentCacheDir(String docdownload) {
	
		File docDir = new File(docdownload);
		String documentDirName = docDir.getName();
		File parentDir = docDir.getParentFile();

		File cacheDir = new File(parentDir, documentDirName + "_cache");

		if (!cacheDir.exists()) {
			cacheDir.mkdir();
		}
		return cacheDir;
	}
	
	private File createCacheVersion2(FaxJob faxJob, Integer pageNum) {
		
		String docdownload = oscar.OscarProperties.getInstance().getProperty("DOCUMENT_DIR");
		File documentDir = new File(docdownload);
		File documentCacheDir = getDocumentCacheDir(docdownload);
		
		log.debug("Document Dir is a dir" + documentDir.isDirectory());
		
		int index;
		String filename;
		
		if( (index = faxJob.getFile_name().lastIndexOf("/")) > -1 ) {
			filename = faxJob.getFile_name().substring(index+1);
		}else {
			filename = faxJob.getFile_name();
		}
		
		File file = new File(documentDir, filename);
		PdfDecoder decode_pdf  = new PdfDecoder(true);
		File ofile = null;		
		FontMappings.setFontReplacements();
		decode_pdf.useHiResScreenDisplay(true);
		decode_pdf.setExtractionMode(0, 96, 96/72f);
		FileInputStream is = null;
		
		try {
			is = new FileInputStream(file);
			decode_pdf.openPdfFileFromInputStream(is, false);
			BufferedImage image_to_save = decode_pdf.getPageAsImage(pageNum);
			decode_pdf.getObjectStore().saveStoredImage( documentCacheDir.getCanonicalPath() + "/" + filename + "_" + pageNum + ".png", image_to_save, true, false, "png");
			ofile = new File(documentCacheDir, filename + "_" + pageNum + ".png");
		} catch (FileNotFoundException e) {
			log.error("PDF file not found " + filename, e);
		} catch (PdfException e) {
			log.error("PDF error during file decode of " + filename, e);
		} catch (IOException e) {
			log.error("IO error during file decode of " + filename, e);
		} finally {
			
			if( is != null ) {
				try {
					is.close();
				} catch (IOException e) {
					log.error("Error closing InputStream ", e);
				}
			}
			
			decode_pdf.flushObjectValues(true);
			decode_pdf.closePdfFile();

		}			

		return ofile;
	}
	
	public ActionForward CancelFax(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		
		String jobId = request.getParameter("jobId");
				
		if(!securityInfoManager.hasPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), "_admin", "w", null)) {
        	throw new SecurityException("missing required security object (_admin)");
        }
		
		FaxJobDao faxJobDao = SpringUtils.getBean(FaxJobDao.class);
		FaxConfigDao faxConfigDao = SpringUtils.getBean(FaxConfigDao.class);
		
		FaxJob faxJob = faxJobDao.find(Integer.parseInt(jobId));
		
		FaxConfig faxConfig = faxConfigDao.getConfigByNumber(faxJob.getFax_line());
		
		DefaultHttpClient client = new DefaultHttpClient();
		
		String result = "{success:false}";				
		
		log.info("TRYING TO CANCEL FAXJOB " + faxJob.getJobId());
		
		if( faxConfig.isActive() ) {
			
			if( faxJob.getStatus().equals(FaxJob.STATUS.SENT)) {
				faxJob.setStatus(FaxJob.STATUS.CANCELLED);
				faxJobDao.merge(faxJob);
				result = "{success:true}";
				
			}
			
			if( faxJob.getJobId() != null ) {	
				
				if( faxJob.getStatus().equals(FaxJob.STATUS.WAITING)) {
			
					client.getCredentialsProvider().setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(faxConfig.getSiteUser(), faxConfig.getPasswd()));
					
					HttpPut mPut = new HttpPut(faxConfig.getUrl() + "/fax/" + faxJob.getJobId());
					mPut.setHeader("accept", "application/json");
					mPut.setHeader("user", faxConfig.getFaxUser());
					mPut.setHeader("passwd", faxConfig.getFaxPasswd());					
					
					try {
						HttpResponse httpResponse = client.execute(mPut);	                
		                
		                if( httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK ) {
		                	
		                	HttpEntity httpEntity = httpResponse.getEntity();
		                	result = EntityUtils.toString(httpEntity);
		                	
		                	faxJob.setStatus(FaxJob.STATUS.CANCELLED);
		                	faxJobDao.merge(faxJob);
		                }
		                
					}
					catch( ClientProtocolException e ) {
						
						log.error("PROBLEM COMM WITH WEB SERVICE");
									
					}catch (IOException e) {
			        
						log.error("PROBLEM COMM WITH WEB SERVICE");
					
		            }
				}
			}
		}					
			
        try {
        	JSONObject json = JSONObject.fromObject(result);        	
	        json.write(response.getWriter());
        
        }catch (IOException e) {
			log.error(e.getMessage(), e);
        }
		
		return null;
		
	}
	
	public ActionForward ResendFax(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		
		String JobId = request.getParameter("jobId");
		String faxNumber = request.getParameter("faxNumber");
		JSONObject jsonObject;
		
		if(!securityInfoManager.hasPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), "_admin", "w", null)) {
        	throw new SecurityException("missing required security object (_admin)");
        }

		
		try {
		
			FaxJobDao faxJobDao = SpringUtils.getBean(FaxJobDao.class);
		
		
			FaxJob faxJob = faxJobDao.find(Integer.parseInt(JobId));
			
			faxJob.setDestination(faxNumber);
			faxJob.setStatus(FaxJob.STATUS.SENT);
			faxJob.setStamp(new Date());
			faxJob.setJobId(null);
			
			faxJobDao.merge(faxJob);
			
			jsonObject = JSONObject.fromObject("{success:true}");
		
		}catch( Exception e ) {
			
			jsonObject = JSONObject.fromObject("{success:false}");
			log.error("ERROR RESEND FAX " + JobId);
			
		}
		
		try {		
			
			jsonObject.write(response.getWriter());
			
        } catch (IOException e) {
	        MiscUtils.getLogger().error("JSON WRITER ERROR", e);
        }
		return null;
	}
	

	public ActionForward viewFax(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		
		if(!securityInfoManager.hasPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), "_edoc", "r", null)) {
        	throw new SecurityException("missing required security object (_edoc)");
        }

		
		try {
			String doc_no = request.getParameter("jobId");
			String pageNum = request.getParameter("curPage");
			if (pageNum == null) {
				pageNum = "0";
			}
			Integer pn = Integer.parseInt(pageNum);
			log.debug("Document No :" + doc_no);
			LogAction.addLog((String) request.getSession().getAttribute("user"), LogConst.READ, LogConst.CON_DOCUMENT, doc_no, request.getRemoteAddr());

			FaxJobDao faxJobDao = SpringUtils.getBean(FaxJobDao.class);
			FaxJob faxJob = faxJobDao.find(Integer.parseInt(doc_no));
			
			int index;
			String filename;
			if( (index = faxJob.getFile_name().lastIndexOf("/")) > -1 ) {
				filename = faxJob.getFile_name().substring(index+1);
			}
			else {
				filename = faxJob.getFile_name();
			}
			
			String name = filename + "_" + pn + ".png";
			log.debug("name " + name);

			File outfile = null;

			outfile = hasCacheVersion2(faxJob, pn);
			if (outfile != null) {
				log.debug("got doc from local cache   ");
			} else {
				outfile = createCacheVersion2(faxJob, pn);
				if (outfile != null) {
					log.debug("create new doc  ");
				}
			}
			response.setContentType("image/png");
			ServletOutputStream outs = response.getOutputStream();
			response.setHeader("Content-Disposition", "attachment;filename=" + name);

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
					log.info("Unable to retrieve content for " + faxJob + ". This may indicate previous upload or save errors...");
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
	
	public ActionForward fetchFaxStatus(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		
		String statusStr = request.getParameter("status");
		String teamStr = request.getParameter("team");
		String dateBeginStr = request.getParameter("dateBegin");
		String dateEndStr = request.getParameter("dateEnd");
		String provider_no = request.getParameter("oscarUser");
		String demographic_no = request.getParameter("demographic_no");
		
		if( provider_no.equalsIgnoreCase("-1") ) {
			provider_no = null;
		}
		
		if( statusStr.equalsIgnoreCase("-1") ) {
			statusStr = null;
		}
		
		if( teamStr.equalsIgnoreCase("-1") ) {
			teamStr = null;
		}
		
		if( "null".equalsIgnoreCase(demographic_no) || "".equals(demographic_no) ) {
			demographic_no = null;
		}
		
		Calendar calendar = GregorianCalendar.getInstance();
		Date dateBegin=null, dateEnd = null;
		String datePattern[] = new String[] {"yyyy-MM-dd"};
		try {
			dateBegin = DateUtils.parseDate(dateBeginStr, datePattern);
			calendar.setTime(dateBegin);
			calendar.set(Calendar.HOUR, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.MILLISECOND, 0);
			dateBegin = calendar.getTime();
		}
		catch( ParseException e ) {
			dateBegin = null;
			MiscUtils.getLogger().error("UNPARSEABLE DATE " + dateBeginStr);
		}
		
		try {
			dateEnd = DateUtils.parseDate(dateEndStr, datePattern);
			calendar.setTime(dateEnd);
			calendar.set(Calendar.HOUR, 23);
			calendar.set(Calendar.MINUTE, 59);
			calendar.set(Calendar.MILLISECOND, 59);
			dateEnd = calendar.getTime();

		}
		catch( ParseException e ) {
			dateEnd = null;
			MiscUtils.getLogger().error("UNPARSEABLE DATE " + dateEndStr);
		}
		
		FaxJobDao faxJobDao = SpringUtils.getBean(FaxJobDao.class);
		
		List<FaxJob> faxJobList = faxJobDao.getFaxStatusByDateDemographicProviderStatusTeam(demographic_no, provider_no, statusStr, teamStr, dateBegin, dateEnd);
		
		request.setAttribute("faxes", faxJobList);
		
		return mapping.findForward("faxstatus");
	}
	
	public ActionForward SetCompleted(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		
		if(!securityInfoManager.hasPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), "_admin", "w", null)) {
        	throw new SecurityException("missing required security object (_admin)");
        }

		
		String id = request.getParameter("jobId");		
		FaxJobDao faxJobDao = SpringUtils.getBean(FaxJobDao.class);		
		
		FaxJob faxJob = faxJobDao.find(Integer.parseInt(id));		
		faxJob.setStatus(FaxJob.STATUS.RESOLVED);		
		faxJobDao.merge(faxJob);
		
		return null;
	}
}
