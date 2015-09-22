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

import static org.oscarehr.integration.mcedt.McedtConstants.REQUEST_ATTR_KEY_RESOURCE_ID;
import static org.oscarehr.integration.mcedt.McedtConstants.SESSION_KEY_MCEDT_UPDATES;
import static org.oscarehr.integration.mcedt.McedtConstants.SESSION_KEY_MCEDT_UPLOADS;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.oscarehr.integration.mcedt.McedtConstants;
import org.oscarehr.integration.mcedt.ResourceForm;
import org.oscarehr.util.MiscUtils;

import oscar.OscarProperties;
import oscar.util.ConversionUtils;
import ca.ontario.health.edt.Detail;
import ca.ontario.health.edt.DetailData;
import ca.ontario.health.edt.EDTDelegate;
import ca.ontario.health.edt.ResponseResult;
import ca.ontario.health.edt.TypeListData;
import ca.ontario.health.edt.TypeListResult;
import ca.ontario.health.edt.UpdateRequest;
import ca.ontario.health.edt.UploadData;

/**
 * Defines utility methods for action classes.
 *
 */
public class ActionUtils {
	private static Logger logger = MiscUtils.getLogger();

	static ActionMessages addMessage(String messageId, String... messageParams) {
		ActionMessage message = null;
		if (messageParams != null) {
			message = new ActionMessage(messageId, messageParams);
		} else {
			message = new ActionMessage(messageId);
		}

		ActionMessages messages = new ActionMessages();
		messages.add(ActionMessages.GLOBAL_MESSAGE, message);
		return messages;
	}
	
	static ActionMessages addMoreMessage(ActionMessages messages, String messageId, String... messageParams) {
		ActionMessage message = null;
		if (messageParams != null) {
			message = new ActionMessage(messageId, messageParams);
		} else {
			message = new ActionMessage(messageId);
		}
		if (messages == null) messages = new ActionMessages();
		messages.add(ActionMessages.GLOBAL_MESSAGE, message);
		return messages;
	}

	static Detail getDetails(HttpServletRequest request) {
		Detail result = (Detail) request.getSession().getAttribute(McedtConstants.SESSION_KEY_RESOURCE_LIST);
		return result;
	}

	static void setDetails(HttpServletRequest request, Detail detail) {
		request.getSession().setAttribute(McedtConstants.SESSION_KEY_RESOURCE_LIST, detail);
	}

	static void removeDetails(HttpServletRequest request) {
		request.getSession().removeAttribute(McedtConstants.SESSION_KEY_RESOURCE_LIST);
	}
	
	static List<DetailDataCustom> getResourceList(HttpServletRequest request) {
		List<DetailDataCustom> result = (List<DetailDataCustom>) request.getSession().getAttribute(McedtConstants.SESSION_KEY_CUSTOM_RESOURCE_LIST);
		return result;
	}

	static void setResourceList(HttpServletRequest request, List<DetailDataCustom> resourceList) {
		request.getSession().setAttribute(McedtConstants.SESSION_KEY_CUSTOM_RESOURCE_LIST, resourceList);
	}

	static void removeResourceList(HttpServletRequest request) {
		request.getSession().removeAttribute(McedtConstants.SESSION_KEY_CUSTOM_RESOURCE_LIST);
	}

	static void clearUpdateList(HttpServletRequest request) {
		clear(request.getSession(), SESSION_KEY_MCEDT_UPDATES);
	}

	static List<UpdateRequest> getUpdateList(HttpServletRequest request) {
		return getUpdateList(request.getSession());
	}

	static List<UpdateRequest> getUpdateList(HttpSession session) {
		return getSessionList(session, SESSION_KEY_MCEDT_UPDATES);
	}

	static List<UploadData> getUploadList(HttpServletRequest request) {
		return getUploadList(request.getSession());
	}

	static void clearUploadList(HttpServletRequest request) {
		clear(request.getSession(), SESSION_KEY_MCEDT_UPLOADS);
	}

	static List<UploadData> getUploadList(HttpSession session) {
		return getSessionList(session, SESSION_KEY_MCEDT_UPLOADS);
	}

	private static void clear(HttpSession session, String sessionKey) {
		session.removeAttribute(sessionKey);
	}

	private static <T> List<T> getSessionList(HttpSession session, String sessionKey) {
		@SuppressWarnings("unchecked")
		List<T> result = (List<T>) session.getAttribute(sessionKey);
		if (result == null) {
			result = new ArrayList<T>();
			session.setAttribute(sessionKey, result);
		}
		return result;
	}

	static List<BigInteger> getResourceIds(HttpServletRequest request) {
		String[] resourceIds = request.getParameterValues(REQUEST_ATTR_KEY_RESOURCE_ID);

		List<BigInteger> ids = new ArrayList<BigInteger>();
		if (resourceIds == null) {
			return ids;
		}

		for (String i : resourceIds) {
			ids.add(BigInteger.valueOf(ConversionUtils.fromIntString(i)));
		}
		return ids;
	}

	static TypeListResult getTypeList(HttpServletRequest request) {
		return (TypeListResult) request.getSession().getAttribute(McedtConstants.SESSION_KEY_TYPE_LIST);
	}
	
	static String getServiceId(HttpServletRequest request) {
		return request.getParameter(McedtConstants.REQUEST_ATTR_KEY_SERVICE_ID);
	}

	static void setTypeList(HttpServletRequest request, TypeListResult result) {
		request.getSession().setAttribute(McedtConstants.SESSION_KEY_TYPE_LIST, result);
	}

	static void removeTypeList(HttpServletRequest request) {
		request.getSession().removeAttribute(McedtConstants.SESSION_KEY_TYPE_LIST);
	}
	
	/*KAI FUNCTIONS*/
	
	static BigInteger getUploadResourceId(HttpServletRequest request) {
		return (BigInteger) request.getSession().getAttribute(McedtConstants.SESSION_KEY_UPLOAD_RESOURCE_ID);
	}

	static void setUploadResourceId(HttpServletRequest request, BigInteger result) {
		request.getSession().setAttribute(McedtConstants.SESSION_KEY_UPLOAD_RESOURCE_ID, result);
	}

	static void removeUploadResourceId(HttpServletRequest request) {
		request.getSession().removeAttribute(McedtConstants.SESSION_KEY_UPLOAD_RESOURCE_ID);
	}
	
	static void setUploadedFileName(HttpServletRequest request, String result) {
		request.getSession().setAttribute(McedtConstants.SESSION_KEY_UPLOAD_FILENAME, result);
	}
	
	static void removeUploadFileName(HttpServletRequest request) {
		request.getSession().removeAttribute(McedtConstants.SESSION_KEY_UPLOAD_FILENAME);
	}
	
	@SuppressWarnings("unchecked")
    static List<File> getSuccessfulUploads(HttpServletRequest request) {
		return (List<File>) request.getSession().getAttribute(McedtConstants.SESSION_SUCCESSFUL_UPLOADS);
	}

	static void setSuccessfulUploads(HttpServletRequest request, File result) {
		List<File> files = ActionUtils.getSuccessfulUploads(request);
		if (files== null) files= new ArrayList<File>();
		files.add(result);
		request.getSession().setAttribute(McedtConstants.SESSION_SUCCESSFUL_UPLOADS, files);
	}

	static void removeSuccessfulUploads(HttpServletRequest request) {
		request.getSession().removeAttribute(McedtConstants.SESSION_SUCCESSFUL_UPLOADS);
	}
	
	@SuppressWarnings("unchecked")
    static List<ResponseResult> getUploadResponseResults(HttpServletRequest request) {
		return (List<ResponseResult>) request.getSession().getAttribute(McedtConstants.SESSION_KEY_UPLOAD_RESPONSE_RESULT);
	}

	static void setUploadResponseResults(HttpServletRequest request, ResponseResult result) {
		List<ResponseResult> results = ActionUtils.getUploadResponseResults(request);
		if (results== null) results= new ArrayList<ResponseResult>();
		results.add(result);
		request.getSession().setAttribute(McedtConstants.SESSION_KEY_UPLOAD_RESPONSE_RESULT, results);
	}

	static void removeUploadResponseResults(HttpServletRequest request) {
		request.getSession().removeAttribute(McedtConstants.SESSION_KEY_UPLOAD_RESPONSE_RESULT);
	}
	
	@SuppressWarnings("unchecked")
    static List<ResponseResult> getSubmitResponseResults(HttpServletRequest request) {
		return (List<ResponseResult>) request.getSession().getAttribute(McedtConstants.SESSION_KEY_SUBMIT_RESPONSE_RESULT);
	}

	static void setSubmitResponseResults(HttpServletRequest request, ResponseResult result) {
		List<ResponseResult> results = ActionUtils.getSubmitResponseResults(request);
		if (results== null) results= new ArrayList<ResponseResult>();
		results.add(result);
		request.getSession().setAttribute(McedtConstants.SESSION_KEY_SUBMIT_RESPONSE_RESULT, results);
	}

	static void removeSubmitResponseResults(HttpServletRequest request) {
		request.getSession().removeAttribute(McedtConstants.SESSION_KEY_SUBMIT_RESPONSE_RESULT);
	}
	
	public static List<File> getUploadList() {
		List<File> edtUploadList= new ArrayList<File>();
		OscarProperties props = OscarProperties.getInstance();
		File outbox = new File(props.getProperty("ONEDT_OUTBOX",""));
		FileFilter fileFilter = new FileFilter() {
			public boolean accept(File file) {
				return file.isFile() && !file.isHidden();
			}
		};
		File[] toEdt = outbox.listFiles(fileFilter);
		if (toEdt != null) {
			Arrays.sort(toEdt, new Comparator<File>(){
			    public int compare(File f1, File f2)
			    {
			        return Long.valueOf(f1.lastModified()).compareTo(f2.lastModified());
			    } });
		
	}
		for (File file: toEdt) {
			edtUploadList.add(file);
		}
		return edtUploadList;
	}
	
	public static boolean isOHIPFile(String filename) {
		String suffix = filename.substring(filename.lastIndexOf(".") + 1, filename.length());
		String prefix = filename.substring(0,2);
		if (suffix.length()== 3 && suffix.matches("\\d+") && prefix.matches("H[A-L]")) {
			return true;
		}
		return false;
	}
	
	public static boolean isOBECFile(String filename) {
		String suffix = filename.substring(filename.lastIndexOf(".") + 1, filename.length());
		String prefix = filename.substring(0,4);
		if (suffix.length()== 3 && suffix.equalsIgnoreCase("txt") && prefix.equals("OBEC")) {
			return true;
		}
		return false;
	}
	
	public static void moveOhipToOutBox(Date startDate, Date endDate) {
		try {
			OscarProperties props = OscarProperties.getInstance();
			File generatedFiles = new File(props.getProperty("HOME_DIR", ""));
			File outbox = new File(props.getProperty("ONEDT_OUTBOX", ""));
			FileFilter fileFilter = new FileFilter() {
				public boolean accept(File file) {
					return (file.isFile() && !file.isHidden() && ActionUtils.isOHIPFile(file.getName()));
				}
			};
			File[] toOutbox = generatedFiles.listFiles(fileFilter);
			if (toOutbox != null) {
				Arrays.sort(toOutbox, new Comparator<File>() {
					public int compare(File f1, File f2) {
						return Long.valueOf(f1.lastModified()).compareTo(f2.lastModified());
					}
				});
				for (File file : toOutbox) {
					if (new Date(file.lastModified()).after(startDate) && new Date(file.lastModified()).before(endDate)) copyFileToDirectory(file, outbox, false, true);
				}
			}
			for (File file : toOutbox) {
				if (new Date(file.lastModified()).after(startDate) && new Date(file.lastModified()).before(endDate)) copyFileToDirectory(file, outbox,false, true);
			}
		} catch (Exception e) {
			logger.error("Unable to copy OHIP files to outbox", e);
		}
	}
	
	public static void moveObecToOutBox(Date startDate, Date endDate) {
		try {
			OscarProperties props = OscarProperties.getInstance();
			File generatedFiles = new File(props.getProperty("DOCUMENT_DIR", ""));
			File outbox = new File(props.getProperty("ONEDT_OUTBOX", ""));
			FileFilter fileFilter = new FileFilter() {
				public boolean accept(File file) {
					return (file.isFile() && !file.isHidden() && ActionUtils.isOBECFile(file.getName()));
				}
			};
			File[] toOutbox = generatedFiles.listFiles(fileFilter);
			if (toOutbox != null) {
				Arrays.sort(toOutbox, new Comparator<File>() {
					public int compare(File f1, File f2) {
						return Long.valueOf(f1.lastModified()).compareTo(f2.lastModified());
					}
				});
				for (File file : toOutbox) {
					if (new Date(file.lastModified()).after(startDate) && new Date(file.lastModified()).before(endDate)) copyFileToDirectory(file, outbox, false, true);
				}
			}
			
		} catch (Exception e) {
			logger.error("Unable to copy OBEC files to outbox", e);
		}
	}
	
	public static Date getOutboxTimestamp() {
		Date startDate = new Date();
		try {
			SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm");
			OscarProperties props = OscarProperties.getInstance();
			//File dateFile = new File(props.getProperty("ONEDT_OUTBOX", "") + ".timestamp");
			File dateDir = new File(props.getProperty("ONEDT_OUTBOX", ""));
			if (!dateDir.exists()) dateDir.mkdirs();
			File dateFile = new File(dateDir,".timestamp");
			if (!dateFile.exists()) dateFile.createNewFile();
			BufferedReader br = new BufferedReader(new FileReader(dateFile));
			String temp= br.readLine().trim();
			if (temp!=null) {
				startDate = formatter.parse(temp);
			}
			br.close();
		} catch (Exception e) {
			logger.error("Unable to open outbox timestamp file", e);
		}
		return startDate;
	}
	
	public static void setOutboxTimestamp(Date endDate) {
		try {
			SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm");
			OscarProperties props = OscarProperties.getInstance();
			File dateFile = new File(props.getProperty("ONEDT_OUTBOX", "") + ".timestamp");
			if (!dateFile.exists()) dateFile.createNewFile();
			BufferedWriter bw = new BufferedWriter(new FileWriter(dateFile));
			bw.write(formatter.format(endDate));
			bw.close();
		} catch (Exception e) {
			logger.error("Unable to open outbox timestamp file", e);
		}
	}
	
	
	static TypeListResult getTypeList(HttpServletRequest request, EDTDelegate delegate) {
		
		TypeListResult result = getTypeList(request); 
		if (result == null) {
			try {
				result = delegate.getTypeList();
				setTypeList(request, result);
			} catch (Exception e) {
				logger.error("Unable to load type list", e);
				
				//saveErrors(request, ActionUtils.addMessage("resourceAction.getTypeList.fault", McedtMessageCreator.exceptionToString(e)));
			}
		}
		return result;
    }
	
	static String getTypeDescription(ResourceForm form, String typeCode){
		
		String typeDesc="";
			for(TypeListData typeListData:form.getTypeListResult().getData()){
				if(typeListData.getResourceType().trim().equalsIgnoreCase(typeCode.trim())){
					typeDesc = typeListData.getDescriptionEn();
					break;
				}
			}
		return typeDesc;
	} 
	
	static boolean filterResourceStatus(DetailData detailData){
		boolean displayResource = true;
		
		switch(detailData.getStatus()){
			case WIP:	
				displayResource = false;
				break;
			case DOWNLOADABLE:	
				displayResource = false;
				break;
			case DELETED:	
				displayResource = false;
				break;				
			default:
				displayResource = true;
		}
		
		return displayResource;
	}
	
	static boolean filterResourceStatus(DetailDataCustom detailData){
		boolean displayResource = true;
		
		switch(detailData.getStatus()){
			case WIP:	
				displayResource = false;
				break;
			case DOWNLOADABLE:	
				displayResource = false;
				break;
			case DELETED:	
				displayResource = false;
				break;				
			default:
				displayResource = true;
		}
		
		return displayResource;
	}
	
	static DetailDataCustom mapDetailData(ResourceForm form, DetailDataCustom detailDataK, DetailData detailData){
		
		detailDataK.setCreateTimestamp(detailData.getCreateTimestamp());
		detailDataK.setDescription(detailData.getDescription());
		detailDataK.setModifyTimestamp(detailData.getModifyTimestamp());
		detailDataK.setResourceID(detailData.getResourceID());							

		detailDataK.setResourceType(getTypeDescription(form,detailData.getResourceType()));
		
		detailDataK.setResult(detailData.getResult());
		detailDataK.setStatus(detailData.getStatus());
		detailDataK.setServiceId(form.getServiceIdSent());
		return detailDataK;
	}
	
	public static void moveFileToDirectory(File srcFile, File destDir, boolean createDestDir, boolean override) throws IOException {
		if (override) {
			File checkFile = new File(destDir.getAbsolutePath()+ File.separator +srcFile.getName());
			if (checkFile.exists()) FileUtils.forceDelete(checkFile);
		}
		FileUtils.moveFileToDirectory(srcFile, destDir, createDestDir);
	}
	
	public static void copyFileToDirectory(File srcFile, File destDir, boolean createDestDir, boolean override) throws IOException {
		if (override) {
			File checkFile = new File(destDir.getAbsolutePath()+ File.separator +srcFile.getName());
			if (checkFile.exists()) FileUtils.forceDelete(checkFile);
		}
		FileUtils.copyFileToDirectory(srcFile, destDir, createDestDir);
	}
	
	public static String getServiceId(String filename) {
		String serviceId;
		if(isOHIPFile(filename)) {
			serviceId=filename.substring(2,filename.lastIndexOf("."));
			if (serviceId.length()==4) {
				serviceId="0"+serviceId; //for group numbers, it has to be 5 digits
				return serviceId;
			}
			if(serviceId.length()==6) return serviceId;
		}
		OscarProperties props = OscarProperties.getInstance();
		serviceId= props.getProperty("mcedt.service.id");
		return serviceId;
	}
	public static List<String> getServiceIds() {
		OscarProperties props = OscarProperties.getInstance();
		String id = props.getProperty("mcedt.service.id");
		String ids = props.getProperty("mcedt.service.designated.ids");
		List<String> serviceIds= new ArrayList<String>();
		serviceIds.addAll(Arrays.asList(ids.trim().split(",")));
		if (serviceIds.contains(id)) serviceIds.remove(id);
		if (!serviceIds.contains(id)) serviceIds.add(0, id);
		return serviceIds;
	}
	public static String getDefaultServiceId() {
		OscarProperties props = OscarProperties.getInstance();
		String serviceId = props.getProperty("mcedt.service.id");
		return serviceId;
	}
}
