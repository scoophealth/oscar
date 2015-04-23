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
import org.oscarehr.managers.SecurityInfoManager;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.oscarEncounter.oscarMeasurements.pageUtil.EctValidation;
import oscar.oscarReport.oscarMeasurements.data.RptMeasurementsData;
import oscar.util.ConversionUtils;

public class RptInitializeFrequencyOfRelevantTestsCDMReportAction extends Action {

	private SecurityInfoManager securityInfoManager = SpringUtils.getBean(SecurityInfoManager.class);
	
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		if(!securityInfoManager.hasPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), "_report", "r", null)) {
  		  throw new SecurityException("missing required security object (_report)");
  	  	}
		
		RptInitializeFrequencyOfRelevantTestsCDMReportForm frm = (RptInitializeFrequencyOfRelevantTestsCDMReportForm) form;
		request.getSession().setAttribute("RptInitializeFrequencyOfRelevantTestsCDMReportForm", frm);
		MessageResources mr = getResources(request);
		ArrayList<String> reportMsg = new ArrayList<String>();
		ArrayList<String> headings = new ArrayList<String>();
		RptMeasurementsData mData = new RptMeasurementsData();
		String[] patientSeenCheckbox = frm.getPatientSeenCheckbox();
		String startDateA = frm.getStartDateA();
		String endDateA = frm.getEndDateA();
		int nbPatient = 0;

		if (!validate(frm, request)) {
			return (new ActionForward(mapping.getInput()));
		}

		addHeading(headings, request);
		if (patientSeenCheckbox != null) {
			nbPatient = mData.getNbPatientSeen(startDateA, endDateA);
			String msg = mr.getMessage("oscarReport.CDMReport.msgPatientSeen", Integer.toString(nbPatient), startDateA, endDateA);
			MiscUtils.getLogger().debug(msg);
			reportMsg.add(msg);
			reportMsg.add("");
		}
		getFrequenceOfTestPerformed(frm, reportMsg, request);

		String title = mr.getMessage("oscarReport.CDMReport.msgFrequencyOfRelevantTestsBeingPerformed");
		request.setAttribute("title", title);
		request.setAttribute("messages", reportMsg);
		return mapping.findForward("success");
	}

	private ArrayList<String> addHeading(ArrayList<String> headings, HttpServletRequest request) {
		MessageResources mr = getResources(request);
		String hd = mr.getMessage("oscarReport.CDMReport.msgFrequency");
		MiscUtils.getLogger().debug(hd);
		headings.add(hd);
		hd = mr.getMessage("oscarReport.CDMReport.msgPercentage");
		MiscUtils.getLogger().debug(hd);
		headings.add(hd);
		return headings;
	}

	private boolean validate(RptInitializeFrequencyOfRelevantTestsCDMReportForm frm, HttpServletRequest request) {
		EctValidation ectValidation = new EctValidation();
		ActionMessages errors = new ActionMessages();
		String[] startDateD = frm.getStartDateD();
		String[] endDateD = frm.getEndDateD();
		String[] frequencyCheckbox = frm.getFrequencyCheckbox();
		boolean valid = true;

		if (frequencyCheckbox != null) {

			for (int i = 0; i < frequencyCheckbox.length; i++) {
				int ctr = Integer.parseInt(frequencyCheckbox[i]);
				String startDate = startDateD[ctr];
				String endDate = endDateD[ctr];
				String measurementType = (String) frm.getValue("measurementTypeD" + ctr);

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
			}
		}
		return valid;
	}

	/**
	* Gets the frequency of tests performed during a time period
	*
	* @return 
	* 	ArrayList which contain the result in String format
	*/
	private ArrayList<String> getFrequenceOfTestPerformed(RptInitializeFrequencyOfRelevantTestsCDMReportForm frm, ArrayList<String> percentageMsg, HttpServletRequest request) {
		String[] startDateD = frm.getStartDateD();
		String[] endDateD = frm.getEndDateD();
		int[] exactly = frm.getExactly();
		int[] moreThan = frm.getMoreThan();
		int[] lessThan = frm.getLessThan();
		String[] frequencyCheckbox = frm.getFrequencyCheckbox();
		MessageResources mr = getResources(request);
		RptMeasurementsData mData = new RptMeasurementsData();

		if (frequencyCheckbox != null) {
			try {

				for (int i = 0; i < frequencyCheckbox.length; i++) {
					int ctr = Integer.parseInt(frequencyCheckbox[i]);
					String startDate = startDateD[ctr];
					String endDate = endDateD[ctr];
					int exact = exactly[ctr];
					int more = moreThan[ctr];
					int less = lessThan[ctr];

					String measurementType = (String) frm.getValue("measurementTypeD" + ctr);
					String sNumMInstrc = (String) frm.getValue("mNbInstrcsD" + ctr);
					int iNumMInstrc = Integer.parseInt(sNumMInstrc);
					ArrayList patients = mData.getPatientsSeen(startDate, endDate);
					int nbPatients = patients.size();

					for (int j = 0; j < iNumMInstrc; j++) {

						double exactPercentage = 0;
						double morePercentage = 0;
						double lessPercentage = 0;
						int nbExact = 0;
						int nbMore = 0;
						int nbLess = 0;
						int nbTest = 0;

						String mInstrc = (String) frm.getValue("mInstrcsCheckboxD" + ctr + j);
						if (mInstrc != null) {
							MeasurementDao dao = SpringUtils.getBean(MeasurementDao.class);
							for (int k = 0; k < nbPatients; k++) {
								String patient = (String) patients.get(k);

								List<Measurement> ms = dao.findByDemoNoTypeDateAndMeasuringInstruction(ConversionUtils.fromIntString(patient), ConversionUtils.fromDateString(startDate), ConversionUtils.fromDateString(endDate), measurementType, mInstrc);
								nbTest = ms.size();

								if (nbTest == exact) {
									nbExact++;
								}
								if (nbTest > more) {
									nbMore++;
								}
								if (nbTest < less) {
									nbLess++;
								}

								if (nbPatients != 0) {
									exactPercentage = Math.round(((double) nbExact / (double) nbPatients) * 100);
									morePercentage = Math.round(((double) nbMore / (double) nbPatients) * 100);
									lessPercentage = Math.round(((double) nbLess / (double) nbPatients) * 100);
								}

							}

							String[] param0 = { startDate, endDate, measurementType, mInstrc, Double.toString(nbExact) + "/" + Double.toString(nbPatients) + " (" + Double.toString(exactPercentage) + "%)", Integer.toString(exact) };
							String msg = mr.getMessage("oscarReport.CDMReport.msgFrequencyOfRelevantTestsExact", param0);
							MiscUtils.getLogger().debug(msg);
							percentageMsg.add(msg);
							String[] param1 = { startDate, endDate, measurementType, mInstrc, Double.toString(nbMore) + "/" + Double.toString(nbPatients) + " (" + Double.toString(morePercentage) + "%)", Integer.toString(more) };
							msg = mr.getMessage("oscarReport.CDMReport.msgFrequencyOfRelevantTestsMoreThan", param1);
							MiscUtils.getLogger().debug(msg);
							percentageMsg.add(msg);
							String[] param2 = { startDate, endDate, measurementType, mInstrc, Double.toString(nbLess) + "/" + Double.toString(nbPatients) + " (" + Double.toString(lessPercentage) + "%)", Integer.toString(less) };
							msg = mr.getMessage("oscarReport.CDMReport.msgFrequencyOfRelevantTestsLessThan", param2);
							MiscUtils.getLogger().debug(msg);
							percentageMsg.add(msg);
							percentageMsg.add("");
						}
					}
				}
			} catch (Exception e) {
				MiscUtils.getLogger().error("Error", e);
			}
		} else {
			MiscUtils.getLogger().debug("guideline checkbox is null");
		}
		return percentageMsg;
	}

}
