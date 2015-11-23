/**
 * Copyright (c) 2008-2012 Indivica Inc.
 *
 * This software is made available under the terms of the
 * GNU General Public License, Version 2, 1991 (GPLv2).
 * License details are available via "indivica.ca/gplv2"
 * and "gnu.org/licenses/gpl-2.0.html".
 */


package oscar.oscarEncounter.pageUtil;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.Logger;
import org.apache.struts.util.MessageResources;
import org.oscarehr.common.dao.OscarLogDao;
import org.oscarehr.hospitalReportManager.HRMReport;
import org.oscarehr.hospitalReportManager.HRMReportParser;
import org.oscarehr.hospitalReportManager.dao.HRMDocumentDao;
import org.oscarehr.hospitalReportManager.dao.HRMDocumentToDemographicDao;
import org.oscarehr.hospitalReportManager.model.HRMDocument;
import org.oscarehr.hospitalReportManager.model.HRMDocumentToDemographic;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.oscarLab.ca.on.HRMResultsData;
import oscar.util.DateUtils;
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

			List<HRMDocument> allHrmDocsForDemo = new LinkedList<HRMDocument>();
			for (HRMDocumentToDemographic hrmDemoDocResult : hrmDocListDemographic) {
				List<HRMDocument> hrmDoc = hrmDocumentDao.findById(Integer.parseInt(hrmDemoDocResult.getHrmDocumentId()));
				allHrmDocsForDemo.addAll(hrmDoc);
			}


			List<Integer> doNotShowList = new LinkedList<Integer>();
			// the key = SendingFacility+':'+ReportNumber+':'+DeliverToUserID as per HRM spec can be used to signify duplicate report
			HashMap<String,HRMDocument> docsToDisplay = new HashMap<String,HRMDocument>();
			HashMap<String,HRMReport> labReports=new HashMap<String,HRMReport>();
			HashMap<String,ArrayList<Integer>> duplicateLabIds=new HashMap<String,ArrayList<Integer>>();
			for (HRMDocument doc : allHrmDocsForDemo) {
				// filter duplicate reports
				HRMReport hrmReport = HRMReportParser.parseReport(loggedInInfo, doc.getReportFile());
				if (hrmReport == null) continue;
				hrmReport.setHrmDocumentId(doc.getId());
				String duplicateKey=hrmReport.getSendingFacilityId()+':'+hrmReport.getSendingFacilityReportNo()+':'+hrmReport.getDeliverToUserId();

				
				List<HRMDocument> relationshipDocs = hrmDocumentDao.findAllDocumentsWithRelationship(doc.getId());

				HRMDocument oldestDocForTree = doc;
				for (HRMDocument relationshipDoc : relationshipDocs) {
					if (relationshipDoc.getId().intValue() != doc.getId().intValue()) {
						if (relationshipDoc.getReportDate().compareTo(oldestDocForTree.getReportDate()) >= 0 || relationshipDoc.getReportStatus().equalsIgnoreCase("C")) {
							doNotShowList.add(oldestDocForTree.getId());
							oldestDocForTree = relationshipDoc;
						}
					}
				}

				boolean addToList = true;
				for (HRMDocument displayDoc : docsToDisplay.values()) {
					if (displayDoc.getId().intValue() == oldestDocForTree.getId().intValue()) {
						addToList = false;
					}
				}
				
				for (Integer doNotShowId : doNotShowList) {
					if (doNotShowId.intValue() == oldestDocForTree.getId().intValue()) {
						addToList = false;
					}
				}

				if (addToList)
				{
					// if no duplicate
					if (!docsToDisplay.containsKey(duplicateKey))
					{
						docsToDisplay.put(duplicateKey,oldestDocForTree);
						labReports.put(duplicateKey, hrmReport);
					}
					else // there exists an entry like this one
					{
						HRMReport previousHrmReport=labReports.get(duplicateKey);
						
						logger.debug("Duplicate report found : previous="+previousHrmReport.getHrmDocumentId()+", current="+hrmReport.getHrmDocumentId());
						
						Integer duplicateIdToAdd;
						
						// if the current entry is newer than the previous one then replace it, other wise just keep the previous entry
						if (HRMResultsData.isNewer(hrmReport, previousHrmReport))
						{
							HRMDocument previousHRMDocument = docsToDisplay.get(duplicateKey);
							duplicateIdToAdd=previousHRMDocument.getId();
							
							docsToDisplay.put(duplicateKey,oldestDocForTree);
							labReports.put(duplicateKey, hrmReport);
						}
						else
						{
							duplicateIdToAdd=doc.getId();
						}

						ArrayList<Integer> duplicateIds=duplicateLabIds.get(duplicateKey);
						if (duplicateIds==null)
						{
							duplicateIds=new ArrayList<Integer>();
							duplicateLabIds.put(duplicateKey, duplicateIds);
						}
						
						duplicateIds.add(duplicateIdToAdd);						
					}
				}
			}

			for (Map.Entry<String, HRMDocument> entry : docsToDisplay.entrySet()) {
				
				String duplicateKey=entry.getKey();
				HRMDocument hrmDocument=entry.getValue();
				
				String reportStatus = hrmDocument.getReportStatus();
				String dispFilename = hrmDocument.getReportType();
				String dispDocNo    = hrmDocument.getId().toString();
				String description = hrmDocument.getDescription();
				
				String t = StringUtils.isNullOrEmpty(description)?dispFilename:description;
				if (reportStatus != null && reportStatus.equalsIgnoreCase("C")) {
					t = "(Cancelled) " + t;
				}

				title = StringUtils.maxLenString(t, MAX_LEN_TITLE, CROP_LEN_TITLE, ELLIPSES);
				
				DateFormat formatter = new SimpleDateFormat(dbFormat);
				String dateStr = hrmDocument.getTimeReceived().toString();
				NavBarDisplayDAO.Item item = NavBarDisplayDAO.Item();
				try {
					date = formatter.parse(dateStr);
					serviceDateStr =  DateUtils.formatDate(date,request.getLocale()); 
				}
				catch(ParseException ex ) {
					MiscUtils.getLogger().debug("EctDisplayHRMAction: Error creating date " + ex.getMessage());
					serviceDateStr = "Error";
					date = null;
				}

				String user = (String) request.getSession().getAttribute("user");
				item.setDate(date);
				hash = Math.abs(winName.hashCode());

				StringBuilder duplicateLabIdQueryString=new StringBuilder();
				ArrayList<Integer> duplicateIdList=duplicateLabIds.get(duplicateKey);
            	if (duplicateIdList!=null)
            	{
					for (Integer duplicateLabIdTemp : duplicateIdList)
	            	{
	            		if (duplicateLabIdQueryString.length()>0) duplicateLabIdQueryString.append(',');
	            		duplicateLabIdQueryString.append(duplicateLabIdTemp);
	            	}
				}

				url = "popupPage(700,800,'" + hash + "', '" + request.getContextPath() + "/hospitalReportManager/Display.do?id="+dispDocNo+"&duplicateLabIds="+duplicateLabIdQueryString+"');";

				String labRead = "";
				if(!oscarLogDao.hasRead(( (String) request.getSession().getAttribute("user")   ),"hrm",dispDocNo)){
                	labRead = "*";	
                }

				
				item.setLinkTitle(title + serviceDateStr);
				item.setTitle(labRead+title+labRead);
				key = StringUtils.maxLenString(dispFilename, MAX_LEN_KEY, CROP_LEN_KEY, ELLIPSES) + "(" + serviceDateStr + ")";
				key = StringEscapeUtils.escapeJavaScript(key);


				js = "itemColours['" + key + "'] = '" + BGCOLOUR + "'; autoCompleted['" + key + "'] = \"" + url + "\"; autoCompList.push('" + key + "');";
				javascript.append(js);
				url += "return false;";
				item.setURL(url);
				Dao.addItem(item);

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
