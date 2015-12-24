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
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.Logger;
import org.apache.struts.util.MessageResources;
import org.oscarehr.common.dao.EncounterFormDao;
import org.oscarehr.common.model.EncounterForm;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.OscarProperties;
import oscar.oscarEncounter.data.EctFormData;
import oscar.oscarLab.LabRequestReportLink;
import oscar.util.DateUtils;
import oscar.util.StringUtils;

public class EctDisplayFormAction extends EctDisplayAction {

	private static Logger logger=MiscUtils.getLogger();
	
	private String cmd = "forms";
	private String menuId = "1";

	@Override
    public boolean getInfo(EctSessionBean bean, HttpServletRequest request, NavBarDisplayDAO Dao, MessageResources messages) {
		long timer=System.currentTimeMillis();
		
		LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
		
		String appointmentNo = bean.appointmentNo;
		if(appointmentNo == null  && request.getSession().getAttribute("cur_appointment_no") != null) {
			appointmentNo = (String)request.getSession().getAttribute("cur_appointment_no");
	   
		}
    	
		
		if (!securityInfoManager.hasPrivilege(loggedInInfo, "_form", "r", null)) {
			return true; // The link of form won't show up on new CME screen.
		} else {
			try {

		        logger.debug("getInfo part1 TimeMs : "+(System.currentTimeMillis()-timer));
		        timer=System.currentTimeMillis();

		        String winName = "Forms" + bean.demographicNo;
				StringBuilder url = new StringBuilder("popupPage(600, 700, '" + winName + "', '" + request.getContextPath() + "/oscarEncounter/formlist.jsp?demographic_no=" + bean.demographicNo + "')");

				// set text for lefthand module title
				Dao.setLeftHeading(messages.getMessage(request.getLocale(), "oscarEncounter.Index.msgForms"));
				// set link for lefthand module title
				Dao.setLeftURL(url.toString());

				// we're going to display a pop up menu of forms so we set the menu title and id num of menu
				Dao.setRightHeadingID(menuId);
				Dao.setMenuHeader(messages.getMessage("oscarEncounter.LeftNavBar.AddFrm"));
				StringBuilder javascript = new StringBuilder("<script type=\"text/javascript\">");
				String js = "";
				String dbFormat = "yy/MM/dd";
				String serviceDateStr;
				StringBuilder strTitle;
				String fullTitle;
				Date date;
				String key;
				int hash;
				// grab all of the forms
				EncounterFormDao encounterFormDao=(EncounterFormDao)SpringUtils.getBean("encounterFormDao");
				List<EncounterForm> encounterForms = encounterFormDao.findAll();
				Collections.sort(encounterForms, EncounterForm.BC_FIRST_COMPARATOR);
				
				logger.debug("getInfo part2 TimeMs : "+(System.currentTimeMillis()-timer));
		        timer=System.currentTimeMillis();

				String BGCOLOUR = request.getParameter("hC");
				for (EncounterForm encounterForm : encounterForms) {
					if (encounterForm.getFormName().equalsIgnoreCase("Discharge Summary")) {
						String caisiProperty = OscarProperties.getInstance().getProperty("caisi");
						if (caisiProperty != null && (caisiProperty.equalsIgnoreCase("yes")
								||caisiProperty.equalsIgnoreCase("true")
								||caisiProperty.equalsIgnoreCase("on"))) {
							; // form in
						}
						else {	
							continue; //form out
						}
					}
					winName = encounterForm.getFormName() + bean.demographicNo;

					String table = encounterForm.getFormTable();
					if (!table.equalsIgnoreCase("")) {
						new EctFormData();
						EctFormData.PatientForm[] pforms = EctFormData.getPatientFormsFromLocalAndRemote(loggedInInfo, bean.demographicNo, table);
						// if a form has been started for the patient, create a module item for it
						if (pforms.length > 0) {	
														
							NavBarDisplayDAO.Item item = NavBarDisplayDAO.Item();
							EctFormData.PatientForm pfrm = pforms[0];

							// convert date to that specified in base class
							DateFormat formatter = new SimpleDateFormat(dbFormat);
							String dateStr = pfrm.getCreated();
							try {
								date = formatter.parse(dateStr);
							} catch (ParseException ex) {
								logger.debug("EctDisplayFormAction: Error creating date " + ex.getMessage());
								// date = new Date(System.currentTimeMillis());
								date = null;
							}

							if (date != null) serviceDateStr = DateUtils.formatDate(date, request.getLocale());
							else serviceDateStr = "";

							item.setDate(date);

							fullTitle = encounterForm.getFormName();
							strTitle = new StringBuilder(StringUtils.maxLenString(fullTitle, MAX_LEN_TITLE, CROP_LEN_TITLE, ELLIPSES));

							if(table.equals("formLabReq07")) {
								Long reportId = null;
								
								HashMap<String,Object> res = LabRequestReportLink.getLinkByRequestId("formLabReq07",Long.valueOf(pfrm.getFormId()));
								reportId = (Long)res.get("report_id");
								
								if(reportId == null) {
									strTitle.insert(0,"*");
									strTitle.append("*");
								}
							}
							
							hash = Math.abs(winName.hashCode());
							url = new StringBuilder(
									"popupPage(700,960,'" + hash + "started', '" +
							        request.getContextPath() +
							        "/form/forwardshortcutname.jsp?formname="
							        + encounterForm.getFormName() +
							        "&demographic_no=" + bean.demographicNo +
							        (pfrm.getRemoteFacilityId()!=null?"&remoteFacilityId="+pfrm.getRemoteFacilityId():"") +
							        (appointmentNo!=null?"&appointmentNo="+appointmentNo:"")
										        
							        +"&formId="+pfrm.getFormId() + "');");

							key = StringUtils.maxLenString(fullTitle, MAX_LEN_KEY, CROP_LEN_KEY, ELLIPSES) + "(" + serviceDateStr + ")";
							key = StringEscapeUtils.escapeJavaScript(key);

							// auto completion arrays and colour code are set
							js = "itemColours['" + key + "'] = '" + BGCOLOUR + "'; autoCompList.push('" + key + "'); autoCompleted['" + key + "'] = \"" + url + "\";";
							javascript.append(js);

							// set item href text
							item.setTitle(strTitle.toString());
							// set item link
							url.append("return false;");
							item.setURL(url.toString());
							// set item link title text
							item.setLinkTitle(fullTitle + " " + serviceDateStr);

							
							//sorry I have to do this, since the "hidden" field, doesn't mean hidden.
							//this is a fix so that when they've migrated to the enhanced form, the 
							//regular one is hidden. It's still accessible from the list mode off
							//the tab header though, if they really need to get to it.
							boolean dontAdd=false;
							if(table.equals("formONAR")) {
								//check to see if we have an enhanced one
								EctFormData.PatientForm[] pf = EctFormData.getPatientFormsFromLocalAndRemote(loggedInInfo, bean.demographicNo, "formONAREnhancedRecord");
								if(pf.length>0) {
									dontAdd=true;
								}
							}
							if(!dontAdd)
								Dao.addItem(item);
						}
					}

					// we add all unhidden forms to the pop up menu
					if (!encounterForm.isHidden()) {
						hash = Math.abs(winName.hashCode());
						url = new StringBuilder("popupPage(700,960,'" + hash + "new', '" + encounterForm.getFormValue() + bean.demographicNo + "&formId=0&provNo=" + bean.providerNo + "&parentAjaxId=" + cmd + ((appointmentNo!=null)?"&appointmentNo="+ appointmentNo:"") +"')");
						Dao.addPopUpUrl(url.toString());
						key = StringUtils.maxLenString(encounterForm.getFormName(), MAX_LEN_KEY, CROP_LEN_KEY, ELLIPSES) + " (new)";
						Dao.addPopUpText(encounterForm.getFormName());
						key = StringEscapeUtils.escapeJavaScript(key);

						// auto completion arrays and colour code are set
						js = "itemColours['" + key + "'] = '" + BGCOLOUR + "'; autoCompList.push('" + key + "'); autoCompleted['" + key + "'] = \"" + url + ";\";";
						javascript.append(js);
					}
				}
				
				logger.debug("getInfo part3 TimeMs : "+(System.currentTimeMillis()-timer));
		        timer=System.currentTimeMillis();

				url = new StringBuilder("return !showMenu('" + menuId + "', event);");
				Dao.setRightURL(url.toString());

				javascript.append("</script>");
				Dao.setJavaScript(javascript.toString());

				// sort module items, i.e. forms, from most recently started to more distant
				Dao.sortItems(NavBarDisplayDAO.DATESORT_ASC);
			} catch (Exception e) {
				logger.error("EctDisplayFormAction SQL ERROR:", e);
				return false;
			}

			logger.debug("getInfo part4 TimeMs : "+(System.currentTimeMillis()-timer));

			return true;
		}
	}

	@Override
	public String getCmd() {
		return cmd;
	}
}
