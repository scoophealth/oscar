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

package oscar.oscarEncounter.oscarMeasurements.pageUtil;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.validator.GenericValidator;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.oscarehr.casemgmt.model.CaseManagementNote;
import org.oscarehr.casemgmt.service.CaseManagementManager;
import org.oscarehr.common.dao.FlowSheetCustomizationDao;
import org.oscarehr.common.dao.MeasurementDao;
import org.oscarehr.common.dao.MeasurementDao.SearchCriteria;
import org.oscarehr.common.dao.SecRoleDao;
import org.oscarehr.common.model.FlowSheetCustomization;
import org.oscarehr.common.model.Measurement;
import org.oscarehr.common.model.SecRole;
import org.oscarehr.common.model.Validations;
import org.oscarehr.managers.SecurityInfoManager;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.SpringUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import net.sf.json.JSONObject;
import oscar.oscarEncounter.data.EctProgram;
import oscar.oscarEncounter.oscarMeasurements.MeasurementFlowSheet;
import oscar.oscarEncounter.oscarMeasurements.MeasurementTemplateFlowSheetConfig;
import oscar.util.ConversionUtils;

public class EctMeasurementsAction extends Action {

	private SecurityInfoManager securityInfoManager = SpringUtils.getBean(SecurityInfoManager.class);

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (!securityInfoManager.hasPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), "_measurement", "w", null)) {
			throw new SecurityException("missing required security object (_measurement)");
		}

		boolean ajax = (request.getParameter("ajax") != null) ? Boolean.valueOf(request.getParameter("ajax")) : false;
		boolean skipCreateNote = (request.getParameter("skipCreateNote") != null) ? Boolean.valueOf(request.getParameter("skipCreateNote")) : false;

		EctMeasurementsForm frm = (EctMeasurementsForm) form;

		HttpSession session = request.getSession();

		
		String demographicNo = request.getParameter("demographic_no");
		String providerNo = (String) session.getAttribute("user");
		String prog_no = new EctProgram(session).getProgram(providerNo);

		String template = request.getParameter("template");
		MeasurementFlowSheet mFlowsheet = null;
		if (template != null) {
			WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(session.getServletContext());
			FlowSheetCustomizationDao flowSheetCustomizationDao = (FlowSheetCustomizationDao) ctx.getBean("flowSheetCustomizationDao");
			MeasurementTemplateFlowSheetConfig templateConfig = MeasurementTemplateFlowSheetConfig.getInstance();

			List<FlowSheetCustomization> custList = flowSheetCustomizationDao.getFlowSheetCustomizations(template, (String) session.getAttribute("user"), Integer.parseInt(demographicNo));
			mFlowsheet = templateConfig.getFlowSheet(template, custList);
		}

		String numType = (String) frm.getValue("numType");
		int iType = Integer.parseInt(numType);

		String textOnEncounter = ""; //"**"+StringUtils.rightPad(by,80,"*")+"\\n";

		//if parent window content has changed then we need to propagate change so
		//we do not write to parent
		String parentChanged = (String) frm.getValue("parentChanged");
		request.setAttribute("parentChanged", parentChanged);

		boolean valid = true;

		EctValidation ectValidation = new EctValidation();
		ActionMessages errors = new ActionMessages();

		String inputValueName, inputTypeName, inputTypeDisplayName, mInstrcName, commentsName;
		String dateName, validationName, inputValue, inputType, inputTypeDisplay, mInstrc;
		String comments, dateObserved, validation;

		String regExp = null;
		Double dMax = new Double(0);
		Double dMin = new Double(0);
		Integer iMax = 0;
		Integer iMin = 0;

		List<Validations> vs = null;
		String regCharExp;
		//goes through each type to check if the input value is valid
		for (int i = 0; i < iType; i++) {
			inputValueName = "inputValue-" + i;
			inputTypeName = "inputType-" + i;
			inputTypeDisplayName = "inputTypeDisplayName-" + i;
			mInstrcName = "inputMInstrc-" + i;
			commentsName = "comments-" + i;
			dateName = "date-" + i;
			inputValue = (String) frm.getValue(inputValueName);
			inputType = (String) frm.getValue(inputTypeName);
			inputTypeDisplay = (String) frm.getValue(inputTypeDisplayName);
			mInstrc = (String) frm.getValue(mInstrcName);
			comments = (String) frm.getValue(commentsName);
			dateObserved = (String) frm.getValue(dateName);

			regExp = null;
			dMax = new Double(0);
			dMin = new Double(0);
			iMax = 0;
			iMin = 0;

			vs = ectValidation.getValidationType(inputType, mInstrc);
			regCharExp = ectValidation.getRegCharacterExp();

			if (!vs.isEmpty()) {
				Validations v = vs.iterator().next();
				dMax = v.getMaxValue();
				dMin = v.getMinValue();
				iMax = v.getMaxLength();
				iMin = v.getMinLength();
				regExp = v.getRegularExp();
			}

			if (dMax == null) {
				dMax = new Double(0);
			}
			if (dMin == null) {
				dMin = new Double(0);
			}
			if (iMax == null) {
				iMax = 0;
			}
			if (iMin == null) {
				iMin = 0;
			}
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
				errors.add(dateName, new ActionMessage("errors.invalidDate", inputTypeDisplay));
				saveErrors(request, errors);
				valid = false;
			}
		}

		//Write to database and to encounter form if all the input values are valid
		if (valid) {
			for (int i = 0; i < iType; i++) {

				inputValueName = "inputValue-" + i;
				inputTypeName = "inputType-" + i;
				mInstrcName = "inputMInstrc-" + i;
				commentsName = "comments-" + i;
				validationName = "validation-" + i;
				dateName = "date-" + i;

				inputValue = (String) frm.getValue(inputValueName);
				inputType = (String) frm.getValue(inputTypeName);
				mInstrc = (String) frm.getValue(mInstrcName);
				comments = (String) frm.getValue(commentsName);
				comments = org.apache.commons.lang.StringEscapeUtils.escapeSql(comments);
				validation = (String) frm.getValue(validationName);
				dateObserved = (String) frm.getValue(dateName);

				org.apache.commons.validator.GenericValidator gValidator = new org.apache.commons.validator.GenericValidator();
				if (!GenericValidator.isBlankOrNull(inputValue)) {

					//Find if the same data has already been entered into the system
					MeasurementDao dao = SpringUtils.getBean(MeasurementDao.class);
					SearchCriteria sc = new SearchCriteria();
					sc.setDemographicNo(demographicNo);
					sc.setDataField(inputValue);
					sc.setMeasuringInstrc(mInstrc);
					sc.setComments(comments);
					sc.setDateObserved(ConversionUtils.fromDateString(dateObserved));
					sc.setType(inputType);
					List<Measurement> ms = dao.find(sc);

					if (ms.isEmpty()) {
						//Write to the Dababase if all input values are valid
						Measurement m = new Measurement();
						m.setType(inputType);
						m.setDemographicId(Integer.parseInt(demographicNo));
						m.setProviderNo(providerNo);
						m.setDataField(inputValue);
						m.setMeasuringInstruction(mInstrc);
						m.setComments(comments);
						m.setDateObserved(ConversionUtils.fromDateString(dateObserved));
						m.setAppointmentNo(0);
						dao.persist(m);

						//prepare input values for writing to the encounter form
						if (mFlowsheet == null) {
							textOnEncounter = textOnEncounter + inputType + "    " + inputValue + " " + mInstrc + " " + comments + "\n";
						} else {
							textOnEncounter += mFlowsheet.getFlowSheetItem(inputType).getDisplayName() + "    " + inputValue + " " + comments + "\n";
						}
					}
				}

			}
			// textOnEncounter = textOnEncounter + "**********************************************************************************\\n";

		} else {
			String groupName = (String) frm.getValue("groupName");
			String css = (String) frm.getValue("css");
			request.setAttribute("groupName", groupName);
			request.setAttribute("css", css);
			request.setAttribute("demographicNo", demographicNo);
			return (new ActionForward(mapping.getInput()));
		}

		if (valid && !skipCreateNote) {
			//create note
			CaseManagementManager cmm = (CaseManagementManager) SpringUtils.getBean("caseManagementManager");

			SecRoleDao secRoleDao = (SecRoleDao) SpringUtils.getBean("secRoleDao");
			SecRole doctorRole = secRoleDao.findByName("doctor");
			String reporter_caisi_role = doctorRole.getId().toString();

			SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
			Date date = new Date();
			String formattedDate = "[" + df.format(date) + " .: ]";
			textOnEncounter = formattedDate + "\n" + textOnEncounter;

			CaseManagementNote cmn = new CaseManagementNote();
			cmn.setUpdate_date(date);
			cmn.setObservation_date(date);
			cmn.setDemographic_no(demographicNo);
			cmn.setProviderNo(providerNo);
			cmn.setNote(textOnEncounter);
			cmn.setSigned(true);
			cmn.setSigning_provider_no(providerNo);
			cmn.setProgram_no(prog_no);
			cmn.setReporter_caisi_role(reporter_caisi_role);

			cmn.setReporter_program_team("0");
			cmn.setPassword("NULL");
			cmn.setLocked(false);
			cmn.setHistory(textOnEncounter + "-----hi story----");
			cmn.setPosition(0);
			cmn.setAppointmentNo(0);

			cmm.saveNoteSimple(cmn);

		} //create note

		if(ajax) {
			JSONObject json = new JSONObject();
			json.put("encounterText", textOnEncounter);
			json.write(response.getWriter());
			return null;
		} else {
			request.setAttribute("textOnEncounter", StringEscapeUtils.escapeJavaScript(textOnEncounter));
			return mapping.findForward("success");
		}
	}

}
