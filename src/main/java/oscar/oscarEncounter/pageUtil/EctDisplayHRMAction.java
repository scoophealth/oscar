/**
 * Copyright (c) 2008-2012 Indivica Inc.
 *
 * This software is made available under the terms of the
 * GNU General Public License, Version 2, 1991 (GPLv2).
 * License details are available via "indivica.ca/gplv2"
 * and "gnu.org/licenses/gpl-2.0.html".
 */


package oscar.oscarEncounter.pageUtil;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.Logger;
import org.apache.struts.util.MessageResources;
import org.oscarehr.common.dao.OscarLogDao;
import org.oscarehr.hospitalReportManager.dao.HRMCategoryDao;
import org.oscarehr.hospitalReportManager.dao.HRMDocumentDao;
import org.oscarehr.hospitalReportManager.dao.HRMDocumentToDemographicDao;
import org.oscarehr.hospitalReportManager.model.HRMCategory;
import org.oscarehr.hospitalReportManager.model.HRMDocument;
import org.oscarehr.hospitalReportManager.model.HRMDocumentToDemographic;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.util.StringUtils;

public class EctDisplayHRMAction extends EctDisplayAction {

	private static Logger logger = MiscUtils.getLogger();
	private static final String cmd = "HRM";
	private HRMDocumentToDemographicDao hrmDocumentToDemographicDao = (HRMDocumentToDemographicDao) SpringUtils.getBean("HRMDocumentToDemographicDao");
	private HRMDocumentDao hrmDocumentDao = (HRMDocumentDao) SpringUtils.getBean("HRMDocumentDao");
	private OscarLogDao oscarLogDao = (OscarLogDao) SpringUtils.getBean("oscarLogDao");
	
	public boolean getInfo(EctSessionBean bean, HttpServletRequest request, NavBarDisplayDAO Dao, MessageResources messages) {
		LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
		
    	if(!securityInfoManager.hasPrivilege(loggedInInfo, "_hrm", "r", null)) {
			return true; //Prevention link won't show up on new CME screen.
		} else {

			String winName = "docs" + bean.demographicNo;
			String url = "popupPage(500,1115,'" + winName + "', '" + request.getContextPath() + "/hospitalReportManager/displayHRMDocList.jsp?demographic_no=" + bean.demographicNo + "')";
			Dao.setLeftURL(url);
			Dao.setLeftHeading(messages.getMessage(request.getLocale(), "oscarEncounter.Index.msgHRMDocuments"));

			Dao.setRightHeadingID(cmd); //no menu so set div id to unique id for this action

			StringBuilder javascript = new StringBuilder("<script type=\"text/javascript\">");
			String js = "";
			List<HRMDocumentToDemographic> hrmDocListDemographic = hrmDocumentToDemographicDao.findByDemographicNo(bean.demographicNo);
			String dbFormat = "yyyy-MM-dd";
			String serviceDateStr = "";
			String key;
			String title;
			int hash;
			String BGCOLOUR = request.getParameter("hC");
			Date date;


			for (HRMDocumentToDemographic matched: hrmDocListDemographic) {
				
				HRMDocument hrmDocument = hrmDocumentDao.find(Integer.parseInt(matched.getHrmDocumentId()));
				
				if(hrmDocument == null) {
					logger.warn("can't load HRMDocument " + matched.getHrmDocumentId());
					continue;
				}
				
				if(hrmDocument.getParentReport() != null) {
					//this is a child report. IE. it's been replaced by the parent..so skip
					continue;
				}
				
				//choose the BEST name we can
				// 1) description is best
				// 2) OSCAR Category 
				// 3) Class (with subclass title)
				
				String reportStatus = hrmDocument.getReportStatus();
				String dispFilename = hrmDocument.getReportType();
				String dispDocNo    = hrmDocument.getId().toString();
				String description = hrmDocument.getDescription();
				
				
				
				title = hrmDocument.getReportType();
				if(hrmDocument.getHrmCategoryId() != null ) {
					HRMCategoryDao hrmCategoryDao = SpringUtils.getBean(HRMCategoryDao.class);
					HRMCategory category = hrmCategoryDao.find(hrmDocument.getHrmCategoryId());
					if(category != null) {
						title = category.getCategoryName();
					}
				}
				if(!StringUtils.isNullOrEmpty(hrmDocument.getDescription())) {
					title = hrmDocument.getDescription();
				}
				
				if("C".equals(reportStatus)) {
					title += "(Cancelled)";
				}

				title = StringUtils.maxLenString(title, MAX_LEN_TITLE, CROP_LEN_TITLE, ELLIPSES);
				
				
				date = hrmDocument.getReportDate();
				
				NavBarDisplayDAO.Item item = NavBarDisplayDAO.Item();
				item.setDate(date);
				hash = Math.abs(winName.hashCode());

				url = "popupPage(700,800,'" + hash + "', '" + request.getContextPath() + "/hospitalReportManager/Display.do?id="+dispDocNo+"');";

				String labRead = "";
				if(!oscarLogDao.hasRead(( (String) request.getSession().getAttribute("user")   ),"hrm",dispDocNo)){
                	labRead = "*";	
                }

				
				item.setLinkTitle(title);
				item.setTitle(labRead+title+labRead);
				key = StringUtils.maxLenString(dispFilename, MAX_LEN_KEY, CROP_LEN_KEY, ELLIPSES) + "(" + serviceDateStr + ")";
				key = StringEscapeUtils.escapeJavaScript(key);


				js = "itemColours['" + key + "'] = '" + BGCOLOUR + "'; autoCompleted['" + key + "'] = \"" + url + "\"; autoCompList.push('" + key + "');";
				javascript.append(js);
				url += "return false;";
				item.setURL(url);
				Dao.addItem(item);


				javascript.append(js);
			}
			
			javascript.append("</script>");
			Dao.setJavaScript(javascript.toString());
			return true;
		}
	}

	@Override
	public String getCmd() {
		return cmd;
	}

}
