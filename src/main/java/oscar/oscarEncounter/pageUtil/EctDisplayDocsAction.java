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


package oscar.oscarEncounter.pageUtil;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.Logger;
import org.apache.struts.util.MessageResources;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;

import oscar.dms.EDoc;
import oscar.dms.EDocUtil;
import oscar.dms.EDocUtil.EDocSort;
import oscar.util.DateUtils;
import oscar.util.StringUtils;

public class EctDisplayDocsAction extends EctDisplayAction {
	private static Logger logger = MiscUtils.getLogger();

	private static final String cmd = "docs";

	public boolean getInfo(EctSessionBean bean, HttpServletRequest request, NavBarDisplayDAO Dao, MessageResources messages) {
    
		LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
		
    	if (!securityInfoManager.hasPrivilege(loggedInInfo, "_edoc", "r", null)) {
    		return true; // documents link won't show up on new CME screen.
    	} else {
    
    		String omitTypeStr = request.getParameter("omit");
    		String[] omitTypes = new String[0];
    		if (omitTypeStr != null) {
    			omitTypes = omitTypeStr.split(",");
    		}
    		// add for inbox manager
    		boolean inboxflag = oscar.util.plugin.IsPropertiesOn.propertiesOn("inboxmnger");
    		// set lefthand module heading and link
    		String winName = "docs" + bean.demographicNo;
    		String url = "popupPage(500,1115,'" + winName + "', '" + request.getContextPath() + "/dms/documentReport.jsp?" + "function=demographic&doctype=lab&functionid=" + bean.demographicNo + "&curUser=" + bean.providerNo + "')";
    
    		Dao.setLeftHeading(messages.getMessage(request.getLocale(), "oscarEncounter.Index.msgDocuments"));
    		if (inboxflag) {
    			url = "popupPage(600,1024,'" + winName + "', '" + request.getContextPath() + "/mod/docmgmtComp/DocList.do?method=list&&demographic_no=" + bean.demographicNo + "');";
    			Dao.setLeftHeading(messages.getMessage("oscarEncounter.Index.inboxManager"));
    		}
    		Dao.setLeftURL(url);
    
    		// set the right hand heading link to call addDocument in index jsp
    		winName = "addDoc" + bean.demographicNo;
    		url = "popupPage(500,1115,'" + winName + "','" + request.getContextPath() + "/dms/documentReport.jsp?" + "function=demographic&doctype=lab&functionid=" + bean.demographicNo + "&curUser=" + bean.providerNo + "&mode=add" + "&parentAjaxId=" + cmd + "');return false;";
    
    		if (inboxflag) {
    			url = "popupPage(300,600,'" + winName + "','" + request.getContextPath() + "/mod/docmgmtComp/FileUpload.do?method=newupload&demographic_no=" + bean.demographicNo + "');return false;";
    		}
    		Dao.setRightURL(url);
    		Dao.setRightHeadingID(cmd); // no menu so set div id to unique id for this action
    
    		StringBuilder javascript = new StringBuilder("<script type=\"text/javascript\">");
    		String js = "";
    		ArrayList<EDoc> docList = EDocUtil.listDocs(loggedInInfo, "demographic", bean.demographicNo, null, EDocUtil.PRIVATE, EDocSort.OBSERVATIONDATE, "active");
    		String dbFormat = "yyyy-MM-dd";
    		String serviceDateStr = "";
    		String key;
    		String title;
    		int hash;
    		String BGCOLOUR = request.getParameter("hC");
    		Date date;
    
    		// --- add remote documents ---
    		
    		if (loggedInInfo.getCurrentFacility().isIntegratorEnabled()) {
    			try {
    				ArrayList<EDoc> remoteDocuments = EDocUtil.getRemoteDocuments(loggedInInfo, Integer.parseInt(bean.demographicNo));
    				docList.addAll(remoteDocuments);
    			} catch (Exception e) {
    				logger.error("error getting remote documents", e);
    			}
    		}
    
    		boolean isURLjavaScript;
    		for (int i = 0; i < docList.size(); i++) {
    			isURLjavaScript = false;
    			EDoc curDoc = docList.get(i);
    			String dispFilename = org.apache.commons.lang.StringUtils.trimToEmpty(curDoc.getFileName());
    			String dispStatus = String.valueOf(curDoc.getStatus());
    
    			boolean skip = false;
    			for (int x = 0; x < omitTypes.length; x++) {
    				if (omitTypes[x].equals(curDoc.getType())) {
    					skip = true;
    					break;
    				}
    			}
    			if (skip) continue;
    
    			if (dispStatus.equals("A")) dispStatus = "active";
    			else if (dispStatus.equals("H")) dispStatus = "html";
    
    			String dispDocNo = curDoc.getDocId();
    			title = StringUtils.maxLenString(curDoc.getDescription(), MAX_LEN_TITLE, CROP_LEN_TITLE, ELLIPSES);
    
    			if (EDocUtil.getDocUrgentFlag(dispDocNo)) title = StringUtils.maxLenString("!" + curDoc.getDescription(), MAX_LEN_TITLE, CROP_LEN_TITLE, ELLIPSES);
    
    			DateFormat formatter = new SimpleDateFormat(dbFormat);
    			String dateStr = curDoc.getObservationDate();
    			NavBarDisplayDAO.Item item = NavBarDisplayDAO.Item();
    			try {
    				date = formatter.parse(dateStr);
    				serviceDateStr = DateUtils.formatDate(date, request.getLocale());
    			} catch (ParseException ex) {
    				MiscUtils.getLogger().debug("EctDisplayDocsAction: Error creating date " + ex.getMessage());
    				serviceDateStr = "Error";
    				date = null;
    			}
    
    			String user = (String) request.getSession().getAttribute("user");
    			item.setDate(date);
    			hash = Math.abs(winName.hashCode());
    			
    			if (inboxflag) {
    				String path = oscar.util.plugin.IsPropertiesOn.getProperty("DOCUMENT_DIR");
    				url = "popupPage(700,800,'" + hash + "', '" + request.getContextPath() + "/mod/docmgmtComp/FillARForm.do?method=showInboxDocDetails&path=" + path + "&demoNo=" + bean.demographicNo + "&name=" + StringEscapeUtils.escapeHtml(dispFilename) + "'); return false;";
    				isURLjavaScript = true;
    			}
    			else if( curDoc.getRemoteFacilityId()==null && curDoc.isPDF() ) {
    				url = "popupPage(window.screen.width,window.screen.height,'" + hash + "','" + request.getContextPath() + "/dms/MultiPageDocDisplay.jsp?segmentID=" + dispDocNo + "&providerNo=" + user + "&searchProviderNo=" + user + "&status=A'); return false;";
    				isURLjavaScript = true;
    			}
    			else {
    				url = "popupPage(700,800,'" + hash + "', '" +  request.getContextPath() + "/dms/ManageDocument.do?method=display&doc_no=" + dispDocNo + "&providerNo=" + user + (curDoc.getRemoteFacilityId()!=null?"&remoteFacilityId="+curDoc.getRemoteFacilityId():"") + "'); return false;";
    			}
    			
    			item.setLinkTitle(title + serviceDateStr);
    			item.setTitle(title);
    			key = StringUtils.maxLenString(curDoc.getDescription(), MAX_LEN_KEY, CROP_LEN_KEY, ELLIPSES) + "(" + serviceDateStr + ")";
    			key = StringEscapeUtils.escapeJavaScript(key);
    
    			if (inboxflag) {
    				if (!EDocUtil.getDocReviewFlag(dispDocNo)) item.setColour("FF0000");
    			}
    			js = "itemColours['" + key + "'] = '" + BGCOLOUR + "'; autoCompleted['" + key + "'] = \"" + url + "\"; autoCompList.push('" + key + "');";
    			javascript.append(js);				
    			item.setURL(url);
    			item.setURLJavaScript(true);
    			
    			Dao.addItem(item);
    
    		}
    		javascript.append("</script>");
    
    		Dao.setJavaScript(javascript.toString());
    		return true;
    	}
    }

	public String getCmd() {
		return cmd;
	}
}
