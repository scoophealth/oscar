/**
 * Copyright (c) 2008-2012 Indivica Inc.
 *
 * This software is made available under the terms of the
 * GNU General Public License, Version 2, 1991 (GPLv2).
 * License details are available via "indivica.ca/gplv2"
 * and "gnu.org/licenses/gpl-2.0.html".
 */

package oscar.oscarEncounter.oscarMeasurements.pageUtil;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.validator.GenericValidator;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.oscarehr.common.dao.FlowSheetCustomizationDao;
import org.oscarehr.common.dao.MeasurementDao;
import org.oscarehr.common.model.FlowSheetCustomization;
import org.oscarehr.common.model.Measurement;
import org.oscarehr.common.model.Validations;
import org.oscarehr.util.SpringUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import oscar.oscarEncounter.oscarMeasurements.FlowSheetItem;
import oscar.oscarEncounter.oscarMeasurements.MeasurementFlowSheet;
import oscar.oscarEncounter.oscarMeasurements.MeasurementTemplateFlowSheetConfig;
import oscar.oscarEncounter.oscarMeasurements.bean.EctMeasurementTypeBeanHandler;
import oscar.oscarEncounter.oscarMeasurements.bean.EctMeasurementTypesBean;

public class FormUpdateAction extends Action {

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String dateEntered = request.getParameter("date");

		String testOutput = "";
		String textOnEncounter = ""; // ********CDM Indicators Update******** \\n";
		boolean valid = true;
		boolean errorPage = false;

		HttpSession session = request.getSession();

		String temp = "diab3";
		session.setAttribute("temp", "diab3");
		String demographic_no = request.getParameter("demographic_no");
		String providerNo = (String) session.getAttribute("user");
		String apptNo = (String) session.getAttribute("cur_appointment_no");

		WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(session.getServletContext());

		FlowSheetCustomizationDao flowSheetCustomizationDao = (FlowSheetCustomizationDao) ctx.getBean("flowSheetCustomizationDao");

		List<FlowSheetCustomization> custList = flowSheetCustomizationDao.getFlowSheetCustomizations(temp, providerNo, Integer.parseInt(demographic_no));

		MeasurementTemplateFlowSheetConfig templateConfig = MeasurementTemplateFlowSheetConfig.getInstance();
		MeasurementFlowSheet mFlowsheet = templateConfig.getFlowSheet(temp, custList);

		List<MeasurementTemplateFlowSheetConfig.Node> nodes = mFlowsheet.getItemHeirarchy();

		EctMeasurementTypeBeanHandler mType = new EctMeasurementTypeBeanHandler();

		FlowSheetItem item;
		String measure;

		for (int i = 0; i < nodes.size(); i++) {
			MeasurementTemplateFlowSheetConfig.Node node = nodes.get(i);

			for (int j = 0; j < node.children.size(); j++) {
				MeasurementTemplateFlowSheetConfig.Node child = node.children.get(j);
				if (child.children == null && child.flowSheetItem != null) {
					item = child.flowSheetItem;
					measure = item.getItemName();
					mFlowsheet.getMeasurementFlowSheetInfo(measure);
					EctMeasurementTypesBean mtypeBean = mType.getMeasurementType(measure);

					String name = child.flowSheetItem.getDisplayName().replaceAll("\\W", "");

					if (request.getParameter(name) != null && !request.getParameter(name).equals("")) {

						String comment = "";
						if (request.getParameter(name + "_comments") != null && !request.getParameter(name + "_comments").equals("")) {
							comment = request.getParameter(name + "_comments");
						}
						valid = doInput(item, mtypeBean, mFlowsheet, mtypeBean.getType(), mtypeBean.getMeasuringInstrc(), request.getParameter(name), comment, dateEntered, apptNo, request);

						if (!valid) {
							testOutput += child.flowSheetItem.getDisplayName() + ": " + request.getParameter(name) + "\n";
							errorPage = true;
						} else {
							textOnEncounter += name + " " + request.getParameter(name) + "\\n";
						}

					} else if (request.getParameter(name) != null && request.getParameter(name + "_comments") != null && !request.getParameter(name + "_comments").equals("")) {
						String comment = request.getParameter(name + "_comments");
						doCommentInput(item, mtypeBean, mFlowsheet, mtypeBean.getType(), mtypeBean.getMeasuringInstrc(), comment, dateEntered, apptNo, request);
					}

				}
			}
		}

		//if (request.getParameter("ycoord") != null) {
		//	request.setAttribute("ycoord", request.getParameter("ycoord"));
		//}

		if (errorPage) {
			request.setAttribute("testOutput", testOutput);
			return mapping.findForward("failure");
		}
		session.setAttribute("textOnEncounter", textOnEncounter);
		if (request.getParameter("submit").equals("Update")) {
			return mapping.findForward("reload");
		} else {
			return mapping.findForward("success");
		}
	}

	public void doCommentInput(FlowSheetItem item, EctMeasurementTypesBean mtypeBean, MeasurementFlowSheet mFlowsheet, String inputType, String mInstructions, String comment, String date, String apptNo, HttpServletRequest request) {
		String demographicNo = request.getParameter("demographic_no");
		HttpSession session = request.getSession();
		String providerNo = (String) session.getAttribute("user");
		String comments = comment;

		String[] dateComp = date.split("-");
		Date dateObs = new Date();
		dateObs.setYear(Integer.parseInt(dateComp[0]) - 1900);
		dateObs.setMonth(Integer.parseInt(dateComp[1]) - 1);
		dateObs.setDate(Integer.parseInt(dateComp[2]));

		MeasurementDao measurementDao = (MeasurementDao) SpringUtils.getBean("measurementDao");

		Measurement measurement = new Measurement();
		measurement.setDemographicId(Integer.parseInt(demographicNo));
		measurement.setDataField("");
		measurement.setMeasuringInstruction(mInstructions);
		measurement.setComments(comments);
		measurement.setDateObserved(dateObs);
		measurement.setType(inputType);
		if (apptNo != null) {
			measurement.setAppointmentNo(Integer.parseInt(apptNo));
		} else {
			measurement.setAppointmentNo(0);
		}
		measurement.setProviderNo(providerNo);

		measurementDao.persist(measurement);

	}

	public boolean doInput(FlowSheetItem item, EctMeasurementTypesBean mtypeBean, MeasurementFlowSheet mFlowsheet, String inputType, String mInstructions, String value, String comment, String date, String apptNo, HttpServletRequest request) {
		EctValidation ectValidation = new EctValidation();
		ActionMessages errors = new ActionMessages();

		String demographicNo = request.getParameter("demographic_no");
		HttpSession session = request.getSession();
		String providerNo = (String) session.getAttribute("user");

		String regExp = null;
		Double dMax = 0.0;
		Double dMin = 0.0;
		Integer iMax = 0;
		Integer iMin = 0;

		List<Validations> vs = ectValidation.getValidationType(inputType, mInstructions);
		ectValidation.getRegCharacterExp();

		boolean valid = true;

		if (!vs.isEmpty()) {
			Validations v = vs.iterator().next();
			dMax = v.getMaxValue();
			dMin = v.getMinValue();
			iMax = v.getMaxLength();
			iMin = v.getMinLength();
			regExp = v.getRegularExp();
		}

		String inputTypeDisplay = mtypeBean.getTypeDisplayName();
		String inputValueName = item.getDisplayName();
		String inputValue = value;
		String comments = comment;
		String dateObserved = date;

		if (!ectValidation.isInRange(dMax, dMin, inputValue)) {
			errors.add(inputValueName, new ActionMessage("errors.range", inputTypeDisplay, Double.toString(dMin), Double.toString(dMax)));
			saveErrors(request, errors);
			valid = false;
		}
		if (!ectValidation.maxLength(iMax, inputValue)) {
			errors.add(inputValueName, new ActionMessage("errors.maxlength", inputTypeDisplay, Integer.toString(iMax)));
			saveErrors(request, errors);
			valid = false;
		}
		if (!ectValidation.minLength(iMin, inputValue)) {
			errors.add(inputValueName, new ActionMessage("errors.minlength", inputTypeDisplay, Integer.toString(iMin)));
			saveErrors(request, errors);
			valid = false;
		}

		if (!ectValidation.matchRegExp(regExp, inputValue)) {
			errors.add(inputValueName, new ActionMessage("errors.invalid", inputTypeDisplay));
			saveErrors(request, errors);
			valid = false;
		}
		if (!ectValidation.isValidBloodPressure(regExp, inputValue)) {
			errors.add(inputValueName, new ActionMessage("error.bloodPressure"));
			saveErrors(request, errors);
			valid = false;
		}
		if (!ectValidation.isDate(dateObserved) && inputValue.compareTo("") != 0) {
			errors.add("Date", new ActionMessage("errors.invalidDate", inputTypeDisplay));
			saveErrors(request, errors);
			valid = false;
		}

		if (valid) {
			comments = org.apache.commons.lang.StringEscapeUtils.escapeSql(comments);
			if (!GenericValidator.isBlankOrNull(inputValue)) {

				String[] dateComp = date.split("-");
				Date dateObs = new Date();
				dateObs.setYear(Integer.parseInt(dateComp[0]) - 1900);
				dateObs.setMonth(Integer.parseInt(dateComp[1]) - 1);
				dateObs.setDate(Integer.parseInt(dateComp[2]));

				Measurement measurement = new Measurement();
				measurement.setDemographicId(Integer.parseInt(demographicNo));
				measurement.setDataField(inputValue);
				measurement.setMeasuringInstruction(mInstructions);
				if (comments.equals("")) {
					comments = " ";
				}
				measurement.setComments(comments);
				measurement.setDateObserved(dateObs);
				measurement.setType(inputType);
				if (apptNo != null) {
					measurement.setAppointmentNo(Integer.parseInt(apptNo));
				} else {
					measurement.setAppointmentNo(0);
				}
				measurement.setProviderNo(providerNo);

				//Find if the same data has already been entered into the system
				MeasurementDao measurementDao = (MeasurementDao) SpringUtils.getBean("measurementDao");
				List<Measurement> measurements = measurementDao.findMatching(measurement);

				if (measurements.size() == 0) {
					//Write to the Dababase if all input values are valid
					measurementDao.persist(measurement);
				}
			}

		}

		return valid;
	}

}
