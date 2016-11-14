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

import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.util.MessageResources;
import org.oscarehr.caisi_integrator.ws.CachedDemographicLabResult;
import org.oscarehr.common.dao.OscarLogDao;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;
import org.w3c.dom.Document;

import oscar.OscarProperties;
import oscar.oscarLab.ca.all.parsers.MessageHandler;
import oscar.oscarLab.ca.all.web.LabDisplayHelper;
import oscar.oscarLab.ca.on.CommonLabResultData;
import oscar.oscarLab.ca.on.LabResultData;
import oscar.util.DateUtils;
import oscar.util.StringUtils;

public class EctDisplayLabAction2 extends EctDisplayAction {
	private static final Logger logger = MiscUtils.getLogger();
	private static final String cmd = "labs";

	public boolean getInfo(EctSessionBean bean, HttpServletRequest request, NavBarDisplayDAO Dao, MessageResources messages) {
		LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
		
		logger.debug("EctDisplayLabAction2");
		OscarLogDao oscarLogDao = (OscarLogDao) SpringUtils.getBean("oscarLogDao");

		if(!securityInfoManager.hasPrivilege(loggedInInfo, "_lab", "r", null)) {
			return true; // Lab result link won't show up on new CME screen.
		} else {

			CommonLabResultData comLab = new CommonLabResultData();
			ArrayList<LabResultData> labs = comLab.populateLabResultsData(loggedInInfo, "", bean.demographicNo, "", "", "", "U");
			logger.debug("local labs found : "+labs.size());

			if (loggedInInfo.getCurrentFacility().isIntegratorEnabled()) {
				ArrayList<LabResultData> remoteResults = CommonLabResultData.getRemoteLabs(loggedInInfo, Integer.parseInt(bean.demographicNo));
				logger.debug("remote labs found : "+remoteResults.size());
				labs.addAll(remoteResults);
			}

			Collections.sort(labs);

			// set text for lefthand module title
			Dao.setLeftHeading(messages.getMessage(request.getLocale(), "oscarEncounter.LeftNavBar.Labs"));

			// set link for lefthand module title
			String winName = "Labs" + bean.demographicNo;
			String url = "popupPage(700,599,'" + winName + "','" + request.getContextPath() + "/lab/DemographicLab.jsp?demographicNo=" + bean.demographicNo + "'); return false;";
			Dao.setLeftURL(url);

			// we're going to display popup menu of 2 selections - row display and grid display
			String menuId = "2";
			Dao.setRightHeadingID(menuId);
			Dao.setRightURL("return !showMenu('" + menuId + "', event);");
			Dao.setMenuHeader(messages.getMessage("oscarEncounter.LeftNavBar.LabMenuHeading"));

			winName = "AllLabs" + bean.demographicNo;

			if (OscarProperties.getInstance().getBooleanProperty("HL7TEXT_LABS", "yes")) {
				url = "popupPage(700,1000, '" + winName + "','" + request.getContextPath() + "/lab/CumulativeLabValues3.jsp?demographic_no=" + bean.demographicNo + "')";
				Dao.addPopUpUrl(url);
				Dao.addPopUpText(messages.getMessage("oscarEncounter.LeftNavBar.LabMenuItem1"));
				if (OscarProperties.getInstance().getProperty("labs.hide_old_grid_display", "false").equals("false")) {
					url = "popupPage(700,1000, '" + winName + "','" + request.getContextPath() + "/lab/CumulativeLabValues2.jsp?demographic_no=" + bean.demographicNo + "')";
					Dao.addPopUpUrl(url);
					Dao.addPopUpText(messages.getMessage("oscarEncounter.LeftNavBar.LabMenuItem1") + "-OLD");
				}
			} else {
				url = "popupPage(700,1000, '" + winName + "','" + request.getContextPath() + "/lab/CumulativeLabValues2.jsp?demographic_no=" + bean.demographicNo + "')";
				Dao.addPopUpUrl(url);
				Dao.addPopUpText(messages.getMessage("oscarEncounter.LeftNavBar.LabMenuItem1"));
			}
			url = "popupPage(700,1000, '" + winName + "','" + request.getContextPath() + "/lab/CumulativeLabValues.jsp?demographic_no=" + bean.demographicNo + "')";
			Dao.addPopUpUrl(url);
			Dao.addPopUpText(messages.getMessage("oscarEncounter.LeftNavBar.LabMenuItem2"));

			// now we add individual module items
			LabResultData result;
			String labDisplayName, label;
			// String bgcolour = "FFFFCC";
			StringBuilder func;
			int hash;

			LinkedHashMap<String,LabResultData> accessionMap = new LinkedHashMap<String,LabResultData>();

			for (int i = 0; i < labs.size(); i++) {
				result = labs.get(i);
				if (result.accessionNumber == null || result.accessionNumber.equals("")) {
					accessionMap.put("noAccessionNum" + i + result.labType, result);
				} else {
					if (!accessionMap.containsKey(result.accessionNumber + result.labType)) accessionMap.put(result.accessionNumber + result.labType, result);
				}
			}
			labs = new ArrayList<LabResultData>(accessionMap.values());
			
			for (int j = 0; j < labs.size(); j++) {
				result = labs.get(j);
                Date date = getServiceDate(loggedInInfo,result);
                String formattedDate = "";
                if(date != null) {
                	DateUtils.getDate(date, "dd-MMM-yyyy", request.getLocale());
                }
				// String formattedDate = DateUtils.getDate(date);
				func = new StringBuilder("popupPage(700,960,'");
				label = result.getLabel();

				String remoteFacilityIdQueryString = "";
				if (result.getRemoteFacilityId() != null) {
					try {
						remoteFacilityIdQueryString = "&remoteFacilityId=" + result.getRemoteFacilityId();
						String remoteLabKey = LabDisplayHelper.makeLabKey(Integer.parseInt(result.getLabPatientId()), result.getSegmentID(), result.labType, result.getDateTime());
						remoteFacilityIdQueryString = remoteFacilityIdQueryString + "&remoteLabKey=" + URLEncoder.encode(remoteLabKey, "UTF-8");
					} catch (Exception e) {
						logger.error("Error", e);
					}
				}

				if (result.isMDS()) {
					if (label == null || label.equals("")) labDisplayName = result.getDiscipline();
	            	else labDisplayName = label;
					url = request.getContextPath() + "/oscarMDS/SegmentDisplay.jsp?demographicId=" + bean.demographicNo + "&providerNo=" + bean.providerNo + "&segmentID=" + result.segmentID + "&multiID=" + result.multiLabId + "&status=" + result.getReportStatus() + remoteFacilityIdQueryString;
				} else if (result.isCML()) {
					if (label == null || label.equals("")) labDisplayName = result.getDiscipline();
	            	else labDisplayName = label;
					url = request.getContextPath() + "/lab/CA/ON/CMLDisplay.jsp?demographicId=" + bean.demographicNo + "&providerNo=" + bean.providerNo + "&segmentID=" + result.segmentID + "&multiID=" + result.multiLabId + remoteFacilityIdQueryString;
				} else if (result.isHL7TEXT()) {
					if (label == null || label.equals("")) labDisplayName = result.getDiscipline();
	            	else labDisplayName = label;
					// url = request.getContextPath() + "/lab/CA/ALL/labDisplay.jsp?providerNo="+bean.providerNo+"&segmentID="+result.segmentID;
					url = request.getContextPath() + "/lab/CA/ALL/labDisplay.jsp?demographicId=" + bean.demographicNo + "&providerNo=" + bean.providerNo + "&segmentID=" + result.segmentID + "&multiID=" + result.multiLabId + remoteFacilityIdQueryString;
				} else {
					if (label == null || label.equals("")) labDisplayName = result.getDiscipline();
	            	else labDisplayName = label;
					url = request.getContextPath() + "/lab/CA/BC/labDisplay.jsp?demographicId=" + bean.demographicNo + "&segmentID=" + result.segmentID + "&providerNo=" + bean.providerNo + "&multiID=" + result.multiLabId + remoteFacilityIdQueryString;
				}
				String labRead = "";
				if(!oscarLogDao.hasRead(( (String) request.getSession().getAttribute("user")   ),"lab",result.segmentID)){
                	labRead = "*";
                }

				NavBarDisplayDAO.Item item = NavBarDisplayDAO.Item();
				
				item.setLinkTitle(labDisplayName + " " + formattedDate);
				labDisplayName = StringUtils.maxLenString(labDisplayName, MAX_LEN_TITLE, CROP_LEN_TITLE, ELLIPSES); // +" "+formattedDate;
                if (labDisplayName == null) {
                    labDisplayName = "";
                }
				hash = winName.hashCode();
				hash = hash < 0 ? hash * -1 : hash;
				func.append(hash + "','" + url + "'); return false;");

				item.setTitle(labRead+labDisplayName+labRead);
				item.setURL(func.toString());
				item.setDate(date);
				if(result.isAbnormal()){
					item.setColour("red");
				}


				// item.setBgColour(bgcolour);
				Dao.addItem(item);
			}

			return true;
		}
	}

    public Date getServiceDate(LoggedInInfo loggedInInfo, LabResultData labData) {
        ServiceDateLoader loader = new ServiceDateLoader(labData);
        Date resultDate = loader.determineResultDate(loggedInInfo);
        if (resultDate != null) {
            return resultDate;
        }
        return labData.getDateObj();
    }

	public String getCmd() {
		return cmd;
	}

    /**
     * Attempts to determine service date for any given lab.
     */
    private static class ServiceDateLoader {

        private LabResultData labData;

        public ServiceDateLoader(LabResultData labData) {
            this.labData = labData;
        }

        /**
         * Attempts to determine service date for the aggregated
         * lab.
         *
         * @return
         * 		Returns the service date or null if date can
         * 		not be determined
         *
         */
        public Date determineResultDate(LoggedInInfo loggedInInfo) {
            String serviceDate = getServiceDate(loggedInInfo);
            if (serviceDate == null) {
                return null;
            }
            return parseServiceDate(serviceDate);
        }

        private Date parseServiceDate(String serviceDate) {
            Date result = null;
            String dateFormat[] = new String[] {"yyyy-MM-dd", "dd-MMM-yyyy"};
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat();
            
            for( int idx = 0; idx < dateFormat.length; ++idx) {
            	try {
            	
            		simpleDateFormat.applyPattern(dateFormat[idx]);
            		result = simpleDateFormat.parse(serviceDate);
            	} catch (ParseException e) {
                
            	}
            }
            return result;
        }

        private String getServiceDate(LoggedInInfo loggedInInfo) {
            String segmentId = labData.getSegmentID();
            MessageHandler handler = null;

            try {
                if (!labData.isRemoteLab()) {
                    handler = getLocalHandler(segmentId);
                } else {
                    handler = getRemoteHandler(loggedInInfo, labData);
                }
            } catch (Exception e) {
                logger.error("Unable to get handler for " + labData, e);
            }

            if (handler == null) {
                return null;
            }

            String serviceDate = handler.getServiceDate();
            return serviceDate;
        }

        public MessageHandler getRemoteHandler(LoggedInInfo loggedInInfo, LabResultData labData) {
            Integer labPatientId = null;
            try {
                labPatientId = Integer.parseInt(labData.getLabPatientId());
            } catch (Exception e) {
                logger.error("Unable to parse " + labData.getLabPatientId(), e);
                return null;
            }

            String remoteLabKey = LabDisplayHelper.makeLabKey(labPatientId, labData.getSegmentID(), labData.labType, labData.getDateTime());
            CachedDemographicLabResult remoteLabResult=LabDisplayHelper.getRemoteLab(loggedInInfo,labData.getRemoteFacilityId(), remoteLabKey, labPatientId);
            Document xmlData = null;
            try {
                xmlData = LabDisplayHelper.getXmlDocument(remoteLabResult);
            } catch (Exception e) {
                logger.error("Unable to get remote lab result", e);
                return null;
            }

            MessageHandler handler = LabDisplayHelper.getMessageHandler(xmlData);
            return handler;
        }

        public MessageHandler getLocalHandler(String segmentId) {
            return oscar.oscarLab.ca.all.parsers.Factory.getHandler(segmentId);
        }
    }

}
