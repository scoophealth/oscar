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
import org.oscarehr.common.dao.forms.FormsDao;
import org.oscarehr.common.model.Measurement;
import org.oscarehr.common.model.Validations;
import org.oscarehr.managers.SecurityInfoManager;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.oscarEncounter.oscarMeasurements.pageUtil.EctValidation;
import oscar.oscarReport.oscarMeasurements.data.RptMeasurementsData;
import oscar.util.ConversionUtils;

public class RptInitializePatientsMetGuidelineCDMReportAction extends Action {

	private SecurityInfoManager securityInfoManager = SpringUtils.getBean(SecurityInfoManager.class);
	
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		if(!securityInfoManager.hasPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), "_report", "r", null)) {
	  		  throw new SecurityException("missing required security object (_report)");
	  	  	}
		
		RptInitializePatientsMetGuidelineCDMReportForm frm = (RptInitializePatientsMetGuidelineCDMReportForm) form;
		request.getSession().setAttribute("RptInitializePatientsMetGuidelineCDMReportForm", frm);
		MessageResources mr = getResources(request);
		RptMeasurementsData mData = new RptMeasurementsData();
		String[] patientSeenCheckbox = frm.getPatientSeenCheckbox();
		String startDateA = frm.getStartDateA();
		String endDateA = frm.getEndDateA();

		ArrayList reportMsg = new ArrayList();

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

		getMetGuidelinePercentage(frm, reportMsg, request);
		//getPatientsMetAllSelectedGuideline(db, frm, reportMsg, request);

		String title = mr.getMessage("oscarReport.CDMReport.msgPercentageOfPatientWhoMetGuideline");
		request.setAttribute("title", title);
		request.setAttribute("messages", reportMsg);

		return mapping.findForward("success");
	}

	/*****************************************************************************************
	 * validate the input value
	 *
	 * @return boolean
	 ******************************************************************************************/
	private boolean validate(RptInitializePatientsMetGuidelineCDMReportForm frm, HttpServletRequest request) {
		EctValidation ectValidation = new EctValidation();
		ActionMessages errors = new ActionMessages();
		String[] startDateB = frm.getStartDateB();
		String[] endDateB = frm.getEndDateB();
		String[] idB = frm.getIdB();
		String[] guidelineB = frm.getGuidelineB();
		String[] guidelineCheckbox = frm.getGuidelineCheckbox();

		boolean valid = true;

		if (guidelineCheckbox != null) {
			for (int i = 0; i < guidelineCheckbox.length; i++) {
				int ctr = Integer.parseInt(guidelineCheckbox[i]);
				String startDate = startDateB[ctr];
				String endDate = endDateB[ctr];
				String guideline = guidelineB[ctr];
				String measurementType = (String) frm.getValue("measurementType" + ctr);
				String sNumMInstrc = (String) frm.getValue("mNbInstrcs" + ctr);
				int iNumMInstrc = Integer.parseInt(sNumMInstrc);

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

					String mInstrc = (String) frm.getValue("mInstrcsCheckbox" + ctr + j);
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

						if (!ectValidation.isInRange(dMax, dMin, guideline)) {
							errors.add(guideline, new ActionMessage("errors.range", measurementType, Double.toString(dMin), Double.toString(dMax)));
							saveErrors(request, errors);
							valid = false;
						} else if (!ectValidation.matchRegExp(regExp, guideline)) {
							errors.add(guideline, new ActionMessage("errors.invalid", measurementType));
							saveErrors(request, errors);
							valid = false;
						} else if (!ectValidation.isValidBloodPressure(regExp, guideline)) {
							errors.add(guideline, new ActionMessage("error.bloodPressure"));
							saveErrors(request, errors);
							valid = false;
						}
					}
				}
			}
		}
		return valid;
	}

	/*****************************************************************************************
	* get the number of Patient met the specific guideline during aspecific time period
	*
	* @return ArrayList which contain the result in String format
	******************************************************************************************/
	private ArrayList getMetGuidelinePercentage(RptInitializePatientsMetGuidelineCDMReportForm frm, ArrayList metGLPercentageMsg, HttpServletRequest request) {
		String[] startDateB = frm.getStartDateB();
		String[] endDateB = frm.getEndDateB();
		String[] idB = frm.getIdB();
		String[] guidelineB = frm.getGuidelineB();
		String[] guidelineCheckbox = frm.getGuidelineCheckbox();
		RptCheckGuideline checkGuideline = new RptCheckGuideline();
		MessageResources mr = getResources(request);

		if (guidelineCheckbox == null) {
			return metGLPercentageMsg;
		}

		MeasurementDao dao = SpringUtils.getBean(MeasurementDao.class);
		FormsDao fDao = SpringUtils.getBean(FormsDao.class);
		MiscUtils.getLogger().debug("the length of guideline checkbox is " + guidelineCheckbox.length);
		for (int i = 0; i < guidelineCheckbox.length; i++) {
			int ctr = Integer.parseInt(guidelineCheckbox[i]);
			MiscUtils.getLogger().debug("the value of guildline Checkbox is: " + guidelineCheckbox[i]);
			String startDate = startDateB[ctr];
			String endDate = endDateB[ctr];
			String guideline = guidelineB[ctr];
			String measurementType = (String) frm.getValue("measurementType" + ctr);
			String aboveBelow = (String) frm.getValue("aboveBelow" + ctr);
			String sNumMInstrc = (String) frm.getValue("mNbInstrcs" + ctr);
			int iNumMInstrc = Integer.parseInt(sNumMInstrc);
			double metGLPercentage = 0;
			double nbMetGL = 0;

			for (int j = 0; j < iNumMInstrc; j++) {
				metGLPercentage = 0;
				nbMetGL = 0;
				String mInstrc = (String) frm.getValue("mInstrcsCheckbox" + ctr + j);

				if (mInstrc != null) {
					double nbGeneral = 0;

					List<Object[]> os = dao.findLastEntered(ConversionUtils.fromDateString(startDate), ConversionUtils.fromDateString(endDate), measurementType, mInstrc);
					if (measurementType.compareTo("BP") == 0) {

						for (Object[] o : os) {
							Integer demographicNo = (Integer) o[0];
							Date maxDateEntered = (Date) o[1];
							for (Measurement m : dao.findByDemographicNoTypeAndDate(demographicNo, maxDateEntered, measurementType, mInstrc)) {
								if (checkGuideline.isBloodPressureMetGuideline(m.getDataField(), guideline, aboveBelow)) {
									nbMetGL++;
								}
							}
							nbGeneral++;
						}
						if ((int)nbGeneral != 0) {
							metGLPercentage = Math.round((nbMetGL / nbGeneral) * 100);
						}
						String[] param = { startDate, endDate, measurementType, mInstrc, "(" + nbMetGL + "/" + nbGeneral + ") " + Double.toString(metGLPercentage), aboveBelow, guideline };
						String msg = mr.getMessage("oscarReport.CDMReport.msgNbOfPatientsMetGuideline", param);
						MiscUtils.getLogger().debug(msg);
						metGLPercentageMsg.add(msg);
					} else if (checkGuideline.getValidation(measurementType) == 1) {
						for (Object[] o : os) {
							Integer demographicNo = (Integer) o[0];
							Date maxDateEntered = (Date) o[1];

							String sql = "SELECT dataField FROM measurements WHERE dateEntered = '" + ConversionUtils.toDateString(maxDateEntered) + "' AND demographicNo = '" + demographicNo + "' AND type='" + measurementType + "' AND measuringInstruction='" + mInstrc + "' AND dataField" + aboveBelow + "'" + guideline + "'";
							List<Object[]> rs = fDao.runNativeQuery(sql);

							if (!rs.isEmpty()) {
								nbMetGL++;
							}
							nbGeneral++;
						}

						if ((int)nbGeneral != 0) {
							metGLPercentage = Math.round((nbMetGL / nbGeneral) * 100);
						}
						String[] param = { startDate, endDate, measurementType, mInstrc, "(" + nbMetGL + "/" + nbGeneral + ") " + Double.toString(metGLPercentage), aboveBelow, guideline };

						String msg = mr.getMessage("oscarReport.CDMReport.msgNbOfPatientsMetGuideline", param);
						MiscUtils.getLogger().debug(msg);
						metGLPercentageMsg.add(msg);
					} else {
						for (Object[] o : os) {
							Integer demographicNo = (Integer) o[0];
							Date maxDateEntered = (Date) o[1];

							for (Measurement m : dao.findByDemographicNoTypeAndDate(demographicNo, maxDateEntered, measurementType, mInstrc)) {
								if (checkGuideline.isYesNoMetGuideline(m.getDataField(), guideline)) {
									nbMetGL++;
								}
								break;
							}
							nbGeneral++;
						}
						if ((int)nbGeneral != 0) {
							metGLPercentage = Math.round((nbMetGL / nbGeneral) * 100);
						}
						String[] param = { startDate, endDate, measurementType, mInstrc, guideline, "(" + nbMetGL + "/" + nbGeneral + ") " + Double.toString(metGLPercentage) };
						String msg = mr.getMessage("oscarReport.CDMReport.msgNbOfPatientsIs", param);
						MiscUtils.getLogger().debug(msg);
						metGLPercentageMsg.add(msg);
					}
				}
			}

			//percentage of patients who meet guideline for the same test with all measuring instruction

			metGLPercentage = 0;
			nbMetGL = 0;
			
			List<Object[]> os = dao.findLastEntered(ConversionUtils.fromDateString(startDate), ConversionUtils.fromDateString(endDate), measurementType);
			double nbGeneral = 0;

			if (measurementType.compareTo("BP") == 0) {
				for (Object[] o : os) {
					Integer demographicNo = (Integer) o[0];
					Date maxDateEntered = (Date) o[1];
					
					for(Measurement m : dao.findByDemoNoDateAndType(demographicNo, maxDateEntered, measurementType)) {
						if (checkGuideline.isBloodPressureMetGuideline(m.getDataField(), guideline, aboveBelow)) {
							nbMetGL++;
						}
						break;
					}
					nbGeneral++;
				}
				if ((int)nbGeneral != 0) {
					metGLPercentage = Math.round((nbMetGL / nbGeneral) * 100);
				}
				
				String[] param = { startDate, endDate, measurementType, "", "(" + nbMetGL + "/" + nbGeneral + ") " + Double.toString(metGLPercentage), aboveBelow, guideline };
				String msg = mr.getMessage("oscarReport.CDMReport.msgNbOfPatientsMetGuideline", param);
				MiscUtils.getLogger().debug(msg);
				metGLPercentageMsg.add(msg);
			} else if (checkGuideline.getValidation(measurementType) == 1) {
				for (Object[] o : os) {
					Integer demographicNo = (Integer) o[0];
					Date maxDateEntered = (Date) o[1];
					
					String sql = "SELECT dataField FROM measurements WHERE dateEntered = '" + ConversionUtils.toDateString(maxDateEntered) + "' AND demographicNo = '"
							+ demographicNo + "' AND type='" + measurementType + "' AND dataField" + aboveBelow + "'" + guideline + "'";
					List<Object[]> rs = fDao.runNativeQuery(sql);
					if (!rs.isEmpty()) {
						nbMetGL++;
					}
					nbGeneral++;
				}

				if ((int)nbGeneral != 0) {
					metGLPercentage = Math.round((nbMetGL / nbGeneral) * 100);
				}
				String[] param = { startDate, endDate, measurementType, "", "(" + nbMetGL + "/" + nbGeneral + ") " + Double.toString(metGLPercentage), aboveBelow, guideline };
				String msg = mr.getMessage("oscarReport.CDMReport.msgNbOfPatientsMetGuideline", param);
				MiscUtils.getLogger().debug(msg);
				metGLPercentageMsg.add(msg);
			} else {
				for (Object[] o : os) {
					Integer demographicNo = (Integer) o[0];
					Date maxDateEntered = (Date) o[1];
					
					for(Measurement m : dao.findByDemoNoDateAndType(demographicNo, maxDateEntered, measurementType)) {
						if (checkGuideline.isYesNoMetGuideline(m.getDataField(), guideline)) {
							nbMetGL++;
						}
						break;
					}
					nbGeneral++;
				}
				if ((int)nbGeneral != 0) {
					metGLPercentage = Math.round((nbMetGL / nbGeneral) * 100);
				}
				String[] param = { startDate, endDate, measurementType, "", guideline, "(" + nbMetGL + "/" + nbGeneral + ") " + Double.toString(metGLPercentage) };
				String msg = mr.getMessage("oscarReport.CDMReport.msgNbOfPatientsIs", param);
				MiscUtils.getLogger().debug(msg);
				metGLPercentageMsg.add(msg);
			}
		}

		return metGLPercentageMsg;
	}

	/*****************************************************************************************
	 * get the number of Patient met all the selected guideline during aspecific time period
	 *
	 * @return ArrayList which contain the result in String format
	 ******************************************************************************************/
	/*
	private ArrayList getPatientsMetAllSelectedGuideline(RptInitializePatientsMetGuidelineCDMReportForm frm, ArrayList reportMsg, HttpServletRequest request) {
		String[] guidelineCheckbox = frm.getGuidelineCheckbox();
		String[] guidelineB = frm.getGuidelineB();
		String startDate = frm.getStartDateA();
		String endDate = frm.getEndDateA();
		RptCheckGuideline checkGuideline = new RptCheckGuideline();
		RptMeasurementsData mData = new RptMeasurementsData();
		ArrayList patients = mData.getPatientsSeen(startDate, endDate);
		int nbPatients = patients.size();
		double nbPatientsDoneAllTest = 0;
		double nbPatientsPassAllTest = 0;
		double passAllTestsPercentage = 0;
		MessageResources mr = getResources(request);

		if (guidelineCheckbox != null) {
			try {
				MiscUtils.getLogger().debug("Number of Patients: " + nbPatients);
				for (int i = 0; i < nbPatients; i++) {

					String patient = (String) patients.get(i);
					boolean passAllTests = false;
					boolean doneAllTests = false;
					ArrayList testsDone = new ArrayList();
					ArrayList mInstrc = new ArrayList();
					ArrayList data = new ArrayList();

					ResultSet rs;
					ResultSet rsData;

					
					MeasurementDao dao = SpringUtils.getBean(MeasurementDao.class);
					for(Object[] o : dao.findTypesAndMeasuringInstructionByDemographicId(ConversionUtils.fromIntString(patient))) {
						String type = String.valueOf(o[0]);
						String measuringInstruction = String.valueOf(o[1]);
						
						List<Measurement> ms = dao.findByDemographicNoAndType(ConversionUtils.fromIntString(patient), type);
						if (!ms.isEmpty()) {
							Collections.sort(ms, new BeanComparator("createDate"));
							Measurement m = ms.get(0);
							testsDone.add(m.getType());
							data.add(m.getDataField());
						}
					}
					MiscUtils.getLogger().debug("guidelineCheckbox length: " + guidelineCheckbox.length);
					MiscUtils.getLogger().debug("testDone size: " + testsDone.size());
					if (guidelineCheckbox.length <= testsDone.size()) {

						for (int j = 0; j < guidelineCheckbox.length; j++) {

							int ctr = Integer.parseInt(guidelineCheckbox[j]);
							String guideline = guidelineB[ctr];
							String measurementType = (String) frm.getValue("measurementType" + ctr);
							String aboveBelow = (String) frm.getValue("aboveBelow" + ctr);
							MiscUtils.getLogger().debug("guideline: " + guideline);
							for (int k = 0; k < testsDone.size(); k++) {
								doneAllTests = false;
								String testDone = (String) testsDone.get(k);
								MiscUtils.getLogger().debug("testdone: " + testDone);
								if (measurementType.compareTo(testDone) == 0) {

									if (checkGuideline.getValidation(measurementType) == 1) {
										passAllTests = checkGuideline.isNumericValueMetGuideline(Double.parseDouble((String) data.get(k)), guideline, aboveBelow);
									} else if (measurementType.compareTo("BP") == 0) {
										passAllTests = checkGuideline.isBloodPressureMetGuideline((String) data.get(k), guideline, aboveBelow);
									} else {
										passAllTests = checkGuideline.isYesNoMetGuideline((String) data.get(k), guideline);
									}
									data.remove(k);
									testsDone.remove(k);
									k--;
									doneAllTests = true;
									break;
								}
							}
							if (!doneAllTests) {
								break;
							}
						}
					} else {
						doneAllTests = false;
					}
					if (doneAllTests) {
						MiscUtils.getLogger().debug("doneAllTest is true");
						nbPatientsDoneAllTest++;
						if (passAllTests) {
							nbPatientsPassAllTest++;
						}
					}
				}

				if (nbPatientsDoneAllTest != 0) {
					passAllTestsPercentage = Math.round((nbPatientsPassAllTest / nbPatientsDoneAllTest) * 100);
				}

				String[] param = { startDate, endDate, Double.toString(nbPatientsDoneAllTest), Double.toString(passAllTestsPercentage) };
				String msg = mr.getMessage("oscarReport.CDMReport.msgPassAllSeletectedTest", param);
				MiscUtils.getLogger().debug(msg);

				reportMsg.add(msg);
				reportMsg.add("");
			} catch (Exception e) {
				MiscUtils.getLogger().error("Error", e);
			}
		}

		return reportMsg;
	}
	*/
}
