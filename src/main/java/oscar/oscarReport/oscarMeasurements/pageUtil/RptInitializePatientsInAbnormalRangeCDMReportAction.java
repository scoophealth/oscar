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

package oscar.oscarReport.oscarMeasurements.pageUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.util.MessageResources;
import org.oscarehr.common.dao.MeasurementDao;
import org.oscarehr.common.model.Measurement;
import org.oscarehr.common.model.Validations;
import org.oscarehr.managers.SecurityInfoManager;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.oscarEncounter.oscarMeasurements.pageUtil.EctValidation;
import oscar.oscarReport.oscarMeasurements.data.RptMeasurementsData;
import oscar.util.ConversionUtils;

public class RptInitializePatientsInAbnormalRangeCDMReportAction extends Action {

	private SecurityInfoManager securityInfoManager = SpringUtils.getBean(SecurityInfoManager.class);
	
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		if(!securityInfoManager.hasPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), "_report", "r", null)) {
	  		  throw new SecurityException("missing required security object (_report)");
	  	  	}
		
		RptInitializePatientsInAbnormalRangeCDMReportForm frm = (RptInitializePatientsInAbnormalRangeCDMReportForm) form;
		request.getSession().setAttribute("RptInitializePatientsInAbnormalRangeCDMReportForm", frm);
		MessageResources mr = getResources(request);
		RptMeasurementsData mData = new RptMeasurementsData();
		String[] patientSeenCheckbox = frm.getPatientSeenCheckbox();
		String startDateA = frm.getStartDateA();
		String endDateA = frm.getEndDateA();

		ArrayList<String> reportMsg = new ArrayList<String>();

		if (!validate(frm, request)) {
			MiscUtils.getLogger().debug("the form is invalid");
			return (new ActionForward(mapping.getInput()));
		}

		if (patientSeenCheckbox != null) {
			int nbPatient = mData.getNbPatientSeen(startDateA, endDateA);
			String msg = mr.getMessage("oscarReport.CDMReport.msgPatientSeen", Integer.toString(nbPatient), startDateA, endDateA);
			MiscUtils.getLogger().debug(msg);
			reportMsg.add(msg);
			reportMsg.add("");
		}

		getInAbnormalRangePercentage(frm, reportMsg, request);
		String title = mr.getMessage("oscarReport.CDMReport.msgPercentageOfPatientInAbnormalRange");
		request.setAttribute("title", title);
		request.setAttribute("messages", reportMsg);

		return mapping.findForward("success");
	}

	/*****************************************************************************************
	* validate the input value
	*
	* @return boolean
	******************************************************************************************/
	private boolean validate(RptInitializePatientsInAbnormalRangeCDMReportForm frm, HttpServletRequest request) {
		EctValidation ectValidation = new EctValidation();
		ActionMessages errors = new ActionMessages();
		String[] startDateC = frm.getStartDateC();
		String[] endDateC = frm.getEndDateC();
		String[] upperBound = frm.getUpperBound();
		String[] lowerBound = frm.getLowerBound();
		String[] abnormalCheckbox = frm.getAbnormalCheckbox();
		boolean valid = true;

		if (abnormalCheckbox != null) {

			for (int i = 0; i < abnormalCheckbox.length; i++) {
				int ctr = Integer.parseInt(abnormalCheckbox[i]);
				String startDate = startDateC[ctr];
				String endDate = endDateC[ctr];
				String upper = upperBound[ctr];
				String lower = lowerBound[ctr];
				String measurementType = (String) frm.getValue("measurementTypeC" + ctr);
				String sNumMInstrc = (String) frm.getValue("mNbInstrcsC" + ctr);
				int iNumMInstrc = Integer.parseInt(sNumMInstrc);
				String upperMsg = "The upper bound value of " + measurementType;
				String lowerMsg = "The lower bound value of " + measurementType;

				if (!ectValidation.isDate(startDate)) {
					errors.add(startDate, new ActionMessage("errors.invalidDate", measurementType));
					saveErrors(request, errors);
					valid = false;
				}
				if (!ectValidation.isDate(endDate)) {
					errors.add(endDate, new ActionMessage("errors.invalidDate", measurementType));
					saveErrors(request, errors);
					valid = false;
				}
				for (int j = 0; j < iNumMInstrc; j++) {

					String mInstrc = (String) frm.getValue("mInstrcsCheckboxC" + ctr + j);
					if (mInstrc != null) {
						List<Validations> vs = ectValidation.getValidationType(measurementType, mInstrc);
						String regExp = null;
						double dMax = 0;
						double dMin = 0;

						if (!vs.isEmpty()) {
							Validations v = vs.iterator().next();
							dMax = v.getMaxValue();
							dMin = v.getMinValue();
							regExp = v.getRegularExp();
						}

						if (!ectValidation.isInRange(dMax, dMin, upper)) {
							errors.add(upper, new ActionMessage("errors.range", upperMsg, Double.toString(dMin), Double.toString(dMax)));
							saveErrors(request, errors);
							valid = false;
						} else if (!ectValidation.isInRange(dMax, dMin, lower)) {
							errors.add(lower, new ActionMessage("errors.range", lowerMsg, Double.toString(dMin), Double.toString(dMax)));
							saveErrors(request, errors);
							valid = false;
						} else if (!ectValidation.matchRegExp(regExp, upper)) {
							errors.add(upper, new ActionMessage("errors.invalid", upperMsg));
							saveErrors(request, errors);
							valid = false;
						} else if (!ectValidation.matchRegExp(regExp, lower)) {
							errors.add(lower, new ActionMessage("errors.invalid", lowerMsg));
							saveErrors(request, errors);
							valid = false;
						} else if (!ectValidation.isValidBloodPressure(regExp, upper)) {
							errors.add(upper, new ActionMessage("error.bloodPressure"));
							saveErrors(request, errors);
							valid = false;
						} else if (!ectValidation.isValidBloodPressure(regExp, lower)) {
							errors.add(lower, new ActionMessage("error.bloodPressure"));
							saveErrors(request, errors);
							valid = false;
						}

					}
				}
			}
		}
		return valid;
	}

	/**
	 * Gets the number of Patient in the abnormal range during a time period
	 *
 	 * @return 
 	 * 		ArrayList which contain the result in String format
	 */
	private ArrayList<String> getInAbnormalRangePercentage(RptInitializePatientsInAbnormalRangeCDMReportForm frm, ArrayList<String> metGLPercentageMsg, HttpServletRequest request) {
		String[] startDateC = frm.getStartDateC();
		String[] endDateC = frm.getEndDateC();
		String[] upperBound = frm.getUpperBound();
		String[] lowerBound = frm.getLowerBound();
		String[] abnormalCheckbox = frm.getAbnormalCheckbox();
		RptCheckGuideline checkGuideline = new RptCheckGuideline();
		MessageResources mr = getResources(request);
		MeasurementDao dao = SpringUtils.getBean(MeasurementDao.class);
		
		if (abnormalCheckbox != null) {
			try {
				MiscUtils.getLogger().debug("the length of abnormal range checkbox is " + abnormalCheckbox.length);

				for (int i = 0; i < abnormalCheckbox.length; i++) {
					int ctr = Integer.parseInt(abnormalCheckbox[i]);
					MiscUtils.getLogger().debug("the value of abnormal range Checkbox is: " + abnormalCheckbox[i]);
					String startDate = startDateC[ctr];
					String endDate = endDateC[ctr];
					String upper = upperBound[ctr];
					String lower = lowerBound[ctr];
					String measurementType = (String) frm.getValue("measurementTypeC" + ctr);
					String sNumMInstrc = (String) frm.getValue("mNbInstrcsC" + ctr);
					int iNumMInstrc = Integer.parseInt(sNumMInstrc);
					double nbMetGL = 0;
					double metGLPercentage = 0;
					
					for (int j = 0; j < iNumMInstrc; j++) {
						metGLPercentage = 0;
						nbMetGL = 0;

						String mInstrc = (String) frm.getValue("mInstrcsCheckboxC" + ctr + j);
						if (mInstrc != null) {
							List<Object[]> os = dao.findLastEntered(ConversionUtils.fromDateString(startDate), 
									ConversionUtils.fromDateString(endDate), measurementType, mInstrc);
							double nbGeneral = 0;
							if (measurementType.compareTo("BP") == 0) {
								for(Object[] o : os) {
									Integer demographicNo = (Integer) o[0]; 
									Date maxDateEntered = (Date) o[1];
									
									for(Measurement m : dao.findByDemographicNoTypeAndDate(demographicNo, maxDateEntered, measurementType, mInstrc)) {
										if (checkGuideline.isBloodPressureMetGuideline(m.getDataField(), upper, "<") 
												&& checkGuideline.isBloodPressureMetGuideline(m.getDataField(), lower, ">")) {
											nbMetGL++;
										}
									}
									nbGeneral++;
								}
								if ((int)nbGeneral != 0) {
									MiscUtils.getLogger().debug("the total number of patients seen: " 
											+ nbGeneral + " nb of them pass the test: " + nbMetGL);
									metGLPercentage = Math.round(nbMetGL / nbGeneral * 100);
								}
								
								String[] param = { startDate, endDate, measurementType, mInstrc, lower, upper, "(" + nbMetGL + "/" + nbGeneral + ")" + Double.toString(metGLPercentage) };
								String msg = mr.getMessage("oscarReport.CDMReport.msgNbOfPatientsInAbnormalRange", param);
								MiscUtils.getLogger().debug(msg);
								metGLPercentageMsg.add(msg);
							} else if (checkGuideline.getValidation(measurementType) == 1) {
								for(Object[] o : os) {
									Integer demographicNo = (Integer) o[0]; 
									Date maxDateEntered = (Date) o[1];
									
									List<Object[]> dfs = dao.findByDemoNoDateTypeMeasuringInstrAndDataField(demographicNo, 
											maxDateEntered, measurementType, mInstrc, upper, lower);

									if (!dfs.isEmpty()) {
										nbMetGL++;
									}
									nbGeneral++;
								}

								if ((int)nbGeneral != 0) {
									metGLPercentage = Math.round(nbMetGL / nbGeneral * 100);
								}
								
								String[] param = { startDate, endDate, measurementType, mInstrc, lower, upper, "(" + nbMetGL + "/" + nbGeneral + ")" + Double.toString(metGLPercentage) };
								String msg = mr.getMessage("oscarReport.CDMReport.msgNbOfPatientsInAbnormalRange", param);
								MiscUtils.getLogger().debug(msg);
								metGLPercentageMsg.add(msg);
							} else {
								for(Object[] o : os) {
									Integer demographicNo = (Integer) o[0]; 
									Date maxDateEntered = (Date) o[1];
									
									for(Measurement m : dao.findByDemographicNoTypeAndDate(demographicNo, maxDateEntered, measurementType, mInstrc)) {
										if (checkGuideline.isYesNoMetGuideline(m.getDataField(), upper)) {
											nbMetGL++;
										}
										break;
									}
									nbGeneral++;
								}
								if ((int)nbGeneral != 0) {
									metGLPercentage = Math.round(nbMetGL / nbGeneral * 100);
								}
								String[] param = { startDate, endDate, measurementType, mInstrc, upper, "(" + nbMetGL + "/" + nbGeneral + ")" + Double.toString(metGLPercentage) };
								String msg = mr.getMessage("oscarReport.CDMReport.msgNbOfPatientsIs", param);
								MiscUtils.getLogger().debug(msg);
								metGLPercentageMsg.add(msg);
							}
						}
					}

					// percentage of patients who are in abnormal range for the same test with all measuring instruction
					metGLPercentage = 0;
					nbMetGL = 0;

					List<Object[]> os = dao.findLastEntered(ConversionUtils.fromDateString(startDate), ConversionUtils.fromDateString(endDate), measurementType);
					double nbGeneral = 0;

					if (measurementType.compareTo("BP") == 0) {
						for(Object[] o : os) {
							Integer demographicNo = (Integer) o[0]; 
							Date maxDateEntered = (Date) o[1];
							
							for(Measurement m : dao.findByDemoNoDateAndType(demographicNo, maxDateEntered, measurementType)) {
								if (checkGuideline.isBloodPressureMetGuideline(m.getDataField(), upper, "<") 
										&& checkGuideline.isBloodPressureMetGuideline(m.getDataField(), lower, ">")) {
									nbMetGL++;
								}
							}
							nbGeneral++;
						}
						if ((int)nbGeneral != 0) {
							MiscUtils.getLogger().debug("the total number of patients seen: " + nbGeneral + " nb of them pass the test: " + nbMetGL);
							metGLPercentage = Math.round(nbMetGL / nbGeneral * 100);
						}
						String[] param = { startDate, endDate, measurementType, "", lower, upper, "(" + nbMetGL + "/" + nbGeneral + ")" + Double.toString(metGLPercentage) };
						String msg = mr.getMessage("oscarReport.CDMReport.msgNbOfPatientsInAbnormalRange", param);
						MiscUtils.getLogger().debug(msg);
						metGLPercentageMsg.add(msg);
					} else if (checkGuideline.getValidation(measurementType) == 1) {
						for(Object[] o : os) {
							Integer demographicNo = (Integer) o[0]; 
							Date maxDateEntered = (Date) o[1];
							List<Object[]> data = dao.findByDemoNoDateTypeAndDataField(demographicNo, maxDateEntered, measurementType, upper, lower); 
							if (!data.isEmpty()) {
								nbMetGL++;
							}
							nbGeneral++;
						}

						if ((int)nbGeneral != 0) {
							metGLPercentage = Math.round(nbMetGL / nbGeneral * 100);
						}
						String[] param = { startDate, endDate, measurementType, "", lower, upper, "(" + nbMetGL + "/" + nbGeneral + ")" + Double.toString(metGLPercentage) };
						String msg = mr.getMessage("oscarReport.CDMReport.msgNbOfPatientsInAbnormalRange", param);
						MiscUtils.getLogger().debug(msg);
						metGLPercentageMsg.add(msg);
					} else {
						for(Object[] o : os) {
							Integer demographicNo = (Integer) o[0]; 
							Date maxDateEntered = (Date) o[1];
							
							for(Measurement m : dao.findByDemoNoDateAndType(demographicNo, maxDateEntered, measurementType)) {
								if (checkGuideline.isYesNoMetGuideline(m.getDataField(), upper)) {
									nbMetGL++;
								}
							}
							nbGeneral++;
						}
						if ((int)nbGeneral != 0) {
							metGLPercentage = Math.round(nbMetGL / nbGeneral * 100);
						}
						String[] param = { startDate, endDate, measurementType, "", upper, "(" + nbMetGL + "/" + nbGeneral + ")" + Double.toString(metGLPercentage) };
						String msg = mr.getMessage("oscarReport.CDMReport.msgNbOfPatientsIs", param);
						MiscUtils.getLogger().debug(msg);
						metGLPercentageMsg.add(msg);
					}
				}
			} catch (Exception e) {
				MiscUtils.getLogger().error("Error", e);
			}
		} else {
			MiscUtils.getLogger().debug("guideline checkbox is null");
		}
		return metGLPercentageMsg;
	}

}
