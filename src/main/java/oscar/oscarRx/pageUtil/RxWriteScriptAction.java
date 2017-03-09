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


package oscar.oscarRx.pageUtil;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.util.MessageResources;
import org.oscarehr.casemgmt.model.CaseManagementNote;
import org.oscarehr.casemgmt.model.CaseManagementNoteLink;
import org.oscarehr.casemgmt.service.CaseManagementManager;
import org.oscarehr.common.dao.DrugDao;
import org.oscarehr.common.dao.DrugReasonDao;
import org.oscarehr.common.dao.PartialDateDao;
import org.oscarehr.common.dao.UserPropertyDAO;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.common.model.Drug;
import org.oscarehr.common.model.DrugReason;
import org.oscarehr.common.model.PartialDate;
import org.oscarehr.common.model.UserProperty;
import org.oscarehr.managers.CodingSystemManager;
import org.oscarehr.managers.DemographicManager;
import org.oscarehr.managers.SecurityInfoManager;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import oscar.OscarProperties;
import oscar.log.LogAction;
import oscar.log.LogConst;
import oscar.oscarRx.data.RxDrugData;
import oscar.oscarRx.data.RxDrugData.DrugMonograph.DrugComponent;
import oscar.oscarRx.data.RxPrescriptionData;
import oscar.oscarRx.util.RxUtil;
import oscar.util.StringUtils;

public final class RxWriteScriptAction extends DispatchAction {
	private SecurityInfoManager securityInfoManager = SpringUtils.getBean(SecurityInfoManager.class);
	
	private static final String PRIVILEGE_READ = "r";
	private static final String PRIVILEGE_WRITE = "w";

	private static final Logger logger = MiscUtils.getLogger();
	private static UserPropertyDAO userPropertyDAO;
	private static final String DEFAULT_QUANTITY = "30";
	private static final PartialDateDao partialDateDao = (PartialDateDao)SpringUtils.getBean("partialDateDao");

	DemographicManager demographicManager = SpringUtils.getBean(DemographicManager.class) ;
    
	String removeExtraChars(String s){
		return s.replace(""+((char) 130 ),"").replace(""+((char) 194 ),"").replace(""+((char) 195 ),"").replace(""+((char) 172 ),"");
	}
	

	public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, Exception {
		LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);
		checkPrivilege(loggedInInfo, PRIVILEGE_WRITE);
		
		RxWriteScriptForm frm = (RxWriteScriptForm) form;
		String fwd = "refresh";
		oscar.oscarRx.pageUtil.RxSessionBean bean = (oscar.oscarRx.pageUtil.RxSessionBean) request.getSession().getAttribute("RxSessionBean");

		if (bean == null) {
			response.sendRedirect("error.html");
			return null;
		}

		if (frm.getAction().startsWith("update")) {

			RxDrugData drugData = new RxDrugData();
			RxPrescriptionData.Prescription rx = bean.getStashItem(bean.getStashIndex());
			RxPrescriptionData prescription = new RxPrescriptionData();

			if (frm.getGCN_SEQNO() != 0) { // not custom
				if (frm.getBrandName().equals(rx.getBrandName()) == false) {
					rx.setBrandName(frm.getBrandName());
				} else {
					rx.setGCN_SEQNO(frm.getGCN_SEQNO());
				}
			} else { // custom
				rx.setBrandName(null);
				rx.setGCN_SEQNO(0);
				rx.setCustomName(frm.getCustomName());
			}

			rx.setRxDate(RxUtil.StringToDate(frm.getRxDate(), "yyyy-MM-dd"));
			rx.setWrittenDate(RxUtil.StringToDate(frm.getWrittenDate(), "yyyy-MM-dd"));
			rx.setTakeMin(frm.getTakeMinFloat());
			rx.setTakeMax(frm.getTakeMaxFloat());
			rx.setFrequencyCode(frm.getFrequencyCode());
			rx.setDuration(frm.getDuration());
			rx.setDurationUnit(frm.getDurationUnit());
			rx.setQuantity(frm.getQuantity());
			rx.setDispensingUnits(frm.getDispensingUnits());
			rx.setRepeat(frm.getRepeat());
			rx.setLastRefillDate(RxUtil.StringToDate(frm.getLastRefillDate(), "yyyy-MM-dd"));
			rx.setNosubs(frm.getNosubs());
			rx.setPrn(frm.getPrn());
			rx.setSpecial(removeExtraChars(frm.getSpecial()));
			rx.setAtcCode(frm.getAtcCode());
			rx.setRegionalIdentifier(frm.getRegionalIdentifier());
			rx.setUnit(removeExtraChars(frm.getUnit()));
			rx.setUnitName(frm.getUnitName());
			rx.setMethod(frm.getMethod());
			rx.setRoute(frm.getRoute());
			rx.setCustomInstr(frm.getCustomInstr());
			rx.setDosage(removeExtraChars(frm.getDosage()));
			rx.setOutsideProviderName(frm.getOutsideProviderName());
			rx.setOutsideProviderOhip(frm.getOutsideProviderOhip());
			rx.setLongTerm(frm.getLongTerm());
			rx.setShortTerm(frm.getShortTerm());
			rx.setPastMed(frm.getPastMed());
			rx.setPatientCompliance(frm.getPatientComplianceY(), frm.getPatientComplianceN());

			try {
				rx.setDrugForm(drugData.getDrugForm(String.valueOf(frm.getGCN_SEQNO())));
			} catch (Exception e) {
				logger.error("Unable to get DrugForm from drugref");
			}

			logger.debug("SAVING STASH " + rx.getCustomInstr());
			if (rx.getSpecial() == null) {
				logger.error("Drug.special is null : " + rx.getSpecial() + " : " + frm.getSpecial());
			} else if (rx.getSpecial().length() < 6) {
				logger.warn("Drug.special appears to be empty : " + rx.getSpecial() + " : " + frm.getSpecial());
			}

			String annotation_attrib = request.getParameter("annotation_attrib");
			if (annotation_attrib == null) {
				annotation_attrib = "";
			}

			bean.addAttributeName(annotation_attrib, bean.getStashIndex());
			bean.setStashItem(bean.getStashIndex(), rx);
			rx = null;

			if (frm.getAction().equals("update")) {
				fwd = "refresh";
			}
			if (frm.getAction().equals("updateAddAnother")) {
				fwd = "addAnother";
			}
			if (frm.getAction().equals("updateAndPrint")) {
				// SAVE THE DRUG
				int i;
				String scriptId = prescription.saveScript(loggedInInfo, bean);
				@SuppressWarnings("unchecked")
				ArrayList<String> attrib_names = bean.getAttributeNames();
				// p("attrib_names", attrib_names.toString());
				StringBuilder auditStr = new StringBuilder();
				for (i = 0; i < bean.getStashSize(); i++) {
					rx = bean.getStashItem(i);

					rx.Save(scriptId);
					auditStr.append(rx.getAuditString());
					auditStr.append("\n");

					/* Save annotation */
					HttpSession se = request.getSession();
					WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(se.getServletContext());
					CaseManagementManager cmm = (CaseManagementManager) ctx.getBean("caseManagementManager");
					String attrib_name = attrib_names.get(i);
					if (attrib_name != null) {
						CaseManagementNote cmn = (CaseManagementNote) se.getAttribute(attrib_name);
						if (cmn != null) {
							cmm.saveNoteSimple(cmn);
							CaseManagementNoteLink cml = new CaseManagementNoteLink();
							cml.setTableName(CaseManagementNoteLink.DRUGS);
							cml.setTableId((long) rx.getDrugId());
							cml.setNoteId(cmn.getId());
							cmm.saveNoteLink(cml);
							se.removeAttribute(attrib_name);
							LogAction.addLog(cmn.getProviderNo(), LogConst.ANNOTATE, CaseManagementNoteLink.DISP_PRESCRIP, scriptId, request.getRemoteAddr(), cmn.getDemographic_no(), cmn.getNote());
						}
					}
					rx = null;
				}
				fwd = "viewScript";
				String ip = request.getRemoteAddr();
				request.setAttribute("scriptId", scriptId);
				LogAction.addLog((String) request.getSession().getAttribute("user"), LogConst.ADD, LogConst.CON_PRESCRIPTION, scriptId, ip, "" + bean.getDemographicNo(), auditStr.toString());
			}
		}
		return mapping.findForward(fwd);
	}

	public ActionForward updateReRxDrug(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException {
		checkPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), PRIVILEGE_WRITE);
		
		oscar.oscarRx.pageUtil.RxSessionBean bean = (oscar.oscarRx.pageUtil.RxSessionBean) request.getSession().getAttribute("RxSessionBean");
		if (bean == null) {
			response.sendRedirect("error.html");
			return null;
		}
		List<String> reRxDrugIdList = bean.getReRxDrugIdList();
		String action = request.getParameter("action");
		String drugId = request.getParameter("reRxDrugId");
		if (action.equals("addToReRxDrugIdList") && !reRxDrugIdList.contains(drugId)) {
			reRxDrugIdList.add(drugId);
		} else if (action.equals("removeFromReRxDrugIdList") && reRxDrugIdList.contains(drugId)) {
			reRxDrugIdList.remove(drugId);
		} else if (action.equals("clearReRxDrugIdList")) {
			bean.clearReRxDrugIdList();
		} else {
			logger.warn("WARNING: reRxDrugId not updated");
		}

		return null;

	}

	public ActionForward saveCustomName(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException {
		checkPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), PRIVILEGE_WRITE);
		
		oscar.oscarRx.pageUtil.RxSessionBean bean = (oscar.oscarRx.pageUtil.RxSessionBean) request.getSession().getAttribute("RxSessionBean");
		if (bean == null) {
			response.sendRedirect("error.html");
			return null;
		}
		try {
			String randomId = request.getParameter("randomId");
			String customName = request.getParameter("customName");
			RxPrescriptionData.Prescription rx = bean.getStashItem2(Integer.parseInt(randomId));
			if (rx == null) {
				logger.error("rx is null", new NullPointerException());
				return null;
			}
			rx.setCustomName(customName);
			rx.setBrandName(null);
			rx.setGenericName(null);
			bean.setStashItem(bean.getIndexFromRx(Integer.parseInt(randomId)), rx);

		} catch (Exception e) {
			logger.error("Error", e);
		}

		return null;
	}

	private void setDefaultQuantity(final HttpServletRequest request) {
		try {
			WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getSession().getServletContext());
			String provider = (String) request.getSession().getAttribute("user");
			if (provider != null) {
				userPropertyDAO = (UserPropertyDAO) ctx.getBean("UserPropertyDAO");
				UserProperty prop = userPropertyDAO.getProp(provider, UserProperty.RX_DEFAULT_QUANTITY);
				if (prop != null) RxUtil.setDefaultQuantity(prop.getValue());
				else RxUtil.setDefaultQuantity(DEFAULT_QUANTITY);
			} else {
				logger.error("Provider is null", new NullPointerException());
			}
		} catch (Exception e) {
			logger.error("Error", e);
		}
	}

	private RxPrescriptionData.Prescription setCustomRxDurationQuantity(RxPrescriptionData.Prescription rx) {
		String quantity = rx.getQuantity();
		if (RxUtil.isMitte(quantity)) {
			String duration = RxUtil.getDurationFromQuantityText(quantity);
			String durationUnit = RxUtil.getDurationUnitFromQuantityText(quantity);
			rx.setDuration(duration);
			rx.setDurationUnit(durationUnit);
			rx.setQuantity(RxUtil.getQuantityFromQuantityText(quantity));
			rx.setUnitName(RxUtil.getUnitNameFromQuantityText(quantity));// this is actually an indicator for Mitte rx
		} else rx.setDuration(RxUtil.findDuration(rx));

		return rx;
	}

	public ActionForward newCustomNote(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException {
		logger.debug("=============Start newCustomNote RxWriteScriptAction.java===============");
		LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);
		checkPrivilege(loggedInInfo, PRIVILEGE_WRITE);
		
		oscar.oscarRx.pageUtil.RxSessionBean bean = (oscar.oscarRx.pageUtil.RxSessionBean) request.getSession().getAttribute("RxSessionBean");
		if (bean == null) {
			response.sendRedirect("error.html");
			return null;
		}

		try {
			RxPrescriptionData rxData = new RxPrescriptionData();

			// create Prescription
			RxPrescriptionData.Prescription rx = rxData.newPrescription(bean.getProviderNo(), bean.getDemographicNo());
			String ra = request.getParameter("randomId");
			rx.setRandomId(Integer.parseInt(ra));
			rx.setCustomNote(true);
			rx.setGenericName(null);
			rx.setBrandName(null);
			rx.setDrugForm("");
			rx.setRoute("");
			rx.setDosage("");
			rx.setUnit("");
			rx.setGCN_SEQNO(0);
			rx.setRegionalIdentifier("");
			rx.setAtcCode("");
			RxUtil.setDefaultSpecialQuantityRepeat(rx);
			rx = setCustomRxDurationQuantity(rx);
			bean.addAttributeName(rx.getAtcCode() + "-" + String.valueOf(bean.getStashIndex()));
			List<RxPrescriptionData.Prescription> listRxDrugs = new ArrayList();

			if (RxUtil.isRxUniqueInStash(bean, rx)) {
				listRxDrugs.add(rx);
			}
			int rxStashIndex = bean.addStashItem(loggedInInfo, rx);
			bean.setStashIndex(rxStashIndex);

			String today = null;
			Calendar calendar = Calendar.getInstance();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			try {
				today = dateFormat.format(calendar.getTime());
				// p("today's date", today);
			} catch (Exception e) {
				logger.error("Error", e);
			}
			Date tod = RxUtil.StringToDate(today, "yyyy-MM-dd");
			rx.setRxDate(tod);
			rx.setWrittenDate(tod);

			request.setAttribute("listRxDrugs", listRxDrugs);
		} catch (Exception e) {
			logger.error("Error", e);
		}
		logger.debug("=============END newCustomNote RxWriteScriptAction.java===============");
		return (mapping.findForward("newRx"));
	}

	public ActionForward listPreviousInstructions(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException {
		checkPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), PRIVILEGE_READ);
		
		logger.debug("=============Start listPreviousInstructions RxWriteScriptAction.java===============");
		String randomId = request.getParameter("randomId");
		randomId = randomId.trim();
		// get rx from randomId.
		// if rx is normal drug, if din is not null, use din to find it
		// if din is null, use BN to find it
		// if rx is custom drug, use customName to find it.
		// append results to a list.
		RxSessionBean bean = (RxSessionBean) request.getSession().getAttribute("RxSessionBean");
		if (bean == null) {
			response.sendRedirect("error.html");
			return null;
		}
		// create Prescription
		RxPrescriptionData.Prescription rx = bean.getStashItem2(Integer.parseInt(randomId));
		List<HashMap<String, String>> retList = new ArrayList();
		retList = RxUtil.getPreviousInstructions(rx);

		bean.setListMedHistory(retList);
		return null;
	}

	public ActionForward newCustomDrug(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException {
		logger.debug("=============Start newCustomDrug RxWriteScriptAction.java===============");
		LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);
		checkPrivilege(loggedInInfo, PRIVILEGE_WRITE);
		
		MessageResources messages = getResources(request);
		// set default quantity;
		setDefaultQuantity(request);

		oscar.oscarRx.pageUtil.RxSessionBean bean = (oscar.oscarRx.pageUtil.RxSessionBean) request.getSession().getAttribute("RxSessionBean");
		if (bean == null) {
			response.sendRedirect("error.html");
			return null;
		}
		
		String customDrugName = request.getParameter("name");

		try {
			RxPrescriptionData rxData = new RxPrescriptionData();

			// create Prescription
			RxPrescriptionData.Prescription rx = rxData.newPrescription(bean.getProviderNo(), bean.getDemographicNo());
			String ra = request.getParameter("randomId");
			
			if(customDrugName != null && !customDrugName.isEmpty()) {
				rx.setCustomName(customDrugName);
			}
			rx.setRandomId(Integer.parseInt(ra));
			rx.setGenericName(null);
			rx.setBrandName(null);
			rx.setDrugForm("");
			rx.setRoute("");
			rx.setDosage("");
			rx.setUnit("");
			rx.setGCN_SEQNO(0);
			rx.setRegionalIdentifier("");
			rx.setAtcCode("");
			RxUtil.setDefaultSpecialQuantityRepeat(rx);// 1 OD, 20, 0;
			rx = setCustomRxDurationQuantity(rx);
			bean.addAttributeName(rx.getAtcCode() + "-" + String.valueOf(bean.getStashIndex()));
			List<RxPrescriptionData.Prescription> listRxDrugs = new ArrayList();

			if (RxUtil.isRxUniqueInStash(bean, rx)) {
				listRxDrugs.add(rx);
			}
			int rxStashIndex = bean.addStashItem(loggedInInfo, rx);
			bean.setStashIndex(rxStashIndex);

			String today = null;
			Calendar calendar = Calendar.getInstance();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			try {
				today = dateFormat.format(calendar.getTime());
				// p("today's date", today);
			} catch (Exception e) {
				logger.error("Error", e);
			}
			Date tod = RxUtil.StringToDate(today, "yyyy-MM-dd");
			rx.setRxDate(tod);
			rx.setWrittenDate(tod);

			request.setAttribute("listRxDrugs", listRxDrugs);
		} catch (Exception e) {
			logger.error("Error", e);
		}
		return (mapping.findForward("newRx"));
	}

	public ActionForward normalDrugSetCustom(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException {
		checkPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), PRIVILEGE_WRITE);
		
		oscar.oscarRx.pageUtil.RxSessionBean bean = (oscar.oscarRx.pageUtil.RxSessionBean) request.getSession().getAttribute("RxSessionBean");
		if (bean == null) {
			response.sendRedirect("error.html");
			return null;
		}
		String randomId = request.getParameter("randomId");
		String customDrugName = request.getParameter("customDrugName");
		logger.debug("radomId=" + randomId);
		if (randomId != null && customDrugName != null) {
			RxPrescriptionData.Prescription normalRx = bean.getStashItem2(Integer.parseInt(randomId));
			if (normalRx != null) {// set other fields same as normal drug, set some fields null like custom drug, remove normal drugfrom stash,add customdrug to stash,
				// forward to prescribe.jsp
				RxPrescriptionData rxData = new RxPrescriptionData();
				RxPrescriptionData.Prescription customRx = rxData.newPrescription(bean.getProviderNo(), bean.getDemographicNo());
				customRx = normalRx;
				customRx.setCustomName(customDrugName);
				customRx.setRandomId(Long.parseLong(randomId));
				customRx.setGenericName(null);
				customRx.setBrandName(null);
				customRx.setDrugForm("");
				customRx.setRoute("");
				customRx.setDosage("");
				customRx.setUnit("");
				customRx.setGCN_SEQNO(0);
				customRx.setRegionalIdentifier("");
				customRx.setAtcCode("");
				bean.setStashItem(bean.getIndexFromRx(Integer.parseInt(randomId)), customRx);
				List<RxPrescriptionData.Prescription> listRxDrugs = new ArrayList();
				if (RxUtil.isRxUniqueInStash(bean, customRx)) {
					// p("unique");
					listRxDrugs.add(customRx);
				}
				request.setAttribute("listRxDrugs", listRxDrugs);
				return (mapping.findForward("newRx"));
			} else {

				return null;
			}
		} else {

			return null;
		}
	}

	public ActionForward createNewRx(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException {
		logger.debug("=============Start createNewRx RxWriteScriptAction.java===============");
		LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);
		checkPrivilege(loggedInInfo, PRIVILEGE_WRITE);
		
		String success = "newRx";
		// set default quantity
		setDefaultQuantity(request);
		userPropertyDAO = (UserPropertyDAO) SpringUtils.getBean("UserPropertyDAO");
		UserProperty propUseRx3 = userPropertyDAO.getProp( (String) request.getSession().getAttribute("user"), UserProperty.RX_USE_RX3);

		oscar.oscarRx.pageUtil.RxSessionBean bean = (oscar.oscarRx.pageUtil.RxSessionBean) request.getSession().getAttribute("RxSessionBean");
		if (bean == null) {
			response.sendRedirect("error.html");
			return null;
		}

		try {
			RxPrescriptionData rxData = new RxPrescriptionData();
			RxDrugData drugData = new RxDrugData();

			// create Prescription
			RxPrescriptionData.Prescription rx = rxData.newPrescription(bean.getProviderNo(), bean.getDemographicNo());

			String ra = request.getParameter("randomId");
			int randomId = Integer.parseInt(ra);
			rx.setRandomId(randomId);
			String drugId = request.getParameter("drugId");
			String text = request.getParameter("text");

			// TODO: Is this to slow to do here? It's possible to do this in ajax, as in when this comes back launch an ajax request to fill in.
			logger.debug("requesting drug from drugref id="+drugId);
			RxDrugData.DrugMonograph dmono = drugData.getDrug2(drugId);

			String brandName = null;
			ArrayList<DrugComponent> drugComponents = dmono.getDrugComponentList();	
			
			if(StringUtils.isNullOrEmpty(brandName)) {
				brandName = text;
			}

			if( drugComponents != null && drugComponents.size() > 0 ) {

				StringBuilder stringBuilder = new StringBuilder();
				int count = 0;
				for( RxDrugData.DrugMonograph.DrugComponent drugComponent : drugComponents ) {
					
					stringBuilder.append( drugComponent.getName() );
					stringBuilder.append(" ");
					stringBuilder.append( drugComponent.getStrength() );
					stringBuilder.append( drugComponent.getUnit() );
					
					count++;
					if( count > 0 && count != drugComponents.size() ) {
						stringBuilder.append( " / " );
					}
				}
				
				rx.setGenericName(stringBuilder.toString()); 
			} else {
				rx.setGenericName(dmono.name); 
			}

			rx.setBrandName(brandName);
			

			//there's a change there's multiple forms. Select the first one by default
			//save the list in a separate variable to make a drop down in the interface.
			if(dmono != null && dmono.drugForm!=null && dmono.drugForm.indexOf(",")!=-1) {
				String[] forms = dmono.drugForm.split(",");
				rx.setDrugForm(forms[0]);
			} else if(dmono.drugForm != null){
				rx.setDrugForm(dmono.drugForm);
			} else if(dmono.drugForm == null) {
				rx.setDrugForm("");
			}
			rx.setDrugFormList(dmono.drugForm);

			// TO DO: cache the most used route from the drugs table.
			// for now, check to see if ORAL present, if yes use that, if not use the first one.
			boolean oral = false;
			for (int i = 0; i < dmono.route.size(); i++) {
				if (((String) dmono.route.get(i)).equalsIgnoreCase("ORAL")) {
					oral = true;
				}
			}
			if (oral) {
				rx.setRoute("ORAL");
			} else {
				if (dmono.route.size() > 0) {
					rx.setRoute((String) dmono.route.get(0));
				}
			}
			// if user specified route in instructions, it'll be changed to the one specified.
			String dosage = "";
			String unit = "";
			Vector comps = dmono.components;
			for (int i = 0; i < comps.size(); i++) {
				RxDrugData.DrugMonograph.DrugComponent drugComp = (RxDrugData.DrugMonograph.DrugComponent) comps.get(i);
				String strength = drugComp.strength;
				unit = drugComp.unit;
				dosage = dosage + " " + strength + " " + unit;// get drug dosage from strength and unit.
			}
			rx.setDosage(removeExtraChars(dosage));
			rx.setUnit(removeExtraChars(unit));
			rx.setGCN_SEQNO(Integer.parseInt(drugId));
			rx.setRegionalIdentifier(dmono.regionalIdentifier);
			String atcCode = dmono.atc;
			rx.setAtcCode(atcCode);
			RxUtil.setSpecialQuantityRepeat(rx);
			rx = setCustomRxDurationQuantity(rx);
			List<RxPrescriptionData.Prescription> listRxDrugs = new ArrayList();
			if (RxUtil.isRxUniqueInStash(bean, rx)) {
				listRxDrugs.add(rx);
			}
			bean.addAttributeName(rx.getAtcCode() + "-" + String.valueOf(bean.getStashIndex()));
			int rxStashIndex = bean.addStashItem(loggedInInfo, rx);
			bean.setStashIndex(rxStashIndex);
			String today = null;
			Calendar calendar = Calendar.getInstance();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			try {
				today = dateFormat.format(calendar.getTime());
			} catch (Exception e) {
				logger.error("Error", e);
			}
			Date tod = RxUtil.StringToDate(today, "yyyy-MM-dd");
			rx.setRxDate(tod);
			rx.setWrittenDate(tod);
			rx.setDiscontinuedLatest(RxUtil.checkDiscontinuedBefore(rx));// check and set if rx was discontinued before.
			request.setAttribute("listRxDrugs", listRxDrugs);
		} catch (Exception e) {
			logger.error("Error", e);
		}
		logger.debug("=============END createNewRx RxWriteScriptAction.java===============");

//		Place holder for new Medication Module proposal.
//		if( OscarProperties.getInstance().getBooleanProperty("ENABLE_RX4", "yes") && 
//				( ! BooleanUtils.toBoolean(propUseRx3.getValue()) ) ) {
//			success = "newRx4";
//		}
	
		return ( mapping.findForward(success) );
	}

	public ActionForward updateDrug(ActionMapping mapping, ActionForm aform, HttpServletRequest request, HttpServletResponse response) throws IOException {
		checkPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), PRIVILEGE_WRITE);
		
		oscar.oscarRx.pageUtil.RxSessionBean bean = (oscar.oscarRx.pageUtil.RxSessionBean) request.getSession().getAttribute("RxSessionBean");
		if (bean == null) {
			response.sendRedirect("error.html");
			return null;
		}

		String action = request.getParameter("action");

		if (action != null && action.equals("parseInstructions")) {

			try {
				String randomId = request.getParameter("randomId");
				// p("randomId from request",randomId);
				RxPrescriptionData.Prescription rx = bean.getStashItem2(Integer.parseInt(randomId));
				if (rx == null) {
					logger.error("rx is null", new NullPointerException());
				}

				String instructions = request.getParameter("instruction");
				logger.debug("instruction:"+instructions);
				rx.setSpecial(instructions);
				RxUtil.instrucParser(rx);
				bean.addAttributeName(rx.getAtcCode() + "-" + String.valueOf(bean.getIndexFromRx(Integer.parseInt(randomId))));
				bean.setStashItem(bean.getIndexFromRx(Integer.parseInt(randomId)), rx);
				HashMap hm = new HashMap();

				if (rx.getRoute() == null || rx.getRoute().equalsIgnoreCase("null")) {
					rx.setRoute("");
				}

				hm.put("method", rx.getMethod());
				hm.put("takeMin", rx.getTakeMin());
				hm.put("takeMax", rx.getTakeMax());
				hm.put("duration", rx.getDuration());
				hm.put("frequency", rx.getFrequencyCode());
				hm.put("route", rx.getRoute());
				hm.put("durationUnit", rx.getDurationUnit());
				hm.put("prn", rx.getPrn());
				hm.put("calQuantity", rx.getQuantity());
				hm.put("unitName", rx.getUnitName());
				hm.put("policyViolations", rx.getPolicyViolations());
				JSONObject jsonObject = JSONObject.fromObject(hm);
				logger.debug("jsonObject:"+jsonObject.toString());
				response.getOutputStream().write(jsonObject.toString().getBytes());
			} catch (Exception e) {
				logger.error("Error", e);
			}
			return null;
		} else if (action != null && action.equals("updateQty")) {
			try {
				String quantity = request.getParameter("quantity");
				String randomId = request.getParameter("randomId");
				RxPrescriptionData.Prescription rx = bean.getStashItem2(Integer.parseInt(randomId));
				// get rx from randomId
				if (quantity == null || quantity.equalsIgnoreCase("null")) {
					quantity = "";
				}
				// check if quantity is same as rx.getquantity(), if yes, do nothing.
				if (quantity.equals(rx.getQuantity()) && rx.getUnitName() == null) {
					// do nothing
				} else {

					if (RxUtil.isStringToNumber(quantity)) {
						rx.setQuantity(quantity);
						rx.setUnitName(null);
					} else if (RxUtil.isMitte(quantity)) {// set duration for mitte

						String duration = RxUtil.getDurationFromQuantityText(quantity);
						String durationUnit = RxUtil.getDurationUnitFromQuantityText(quantity);
						rx.setDuration(duration);
						rx.setDurationUnit(durationUnit);
						rx.setQuantity(RxUtil.getQuantityFromQuantityText(quantity));
						rx.setUnitName(RxUtil.getUnitNameFromQuantityText(quantity));// this is actually an indicator for Mitte rx
					} else {
						rx.setQuantity(RxUtil.getQuantityFromQuantityText(quantity));
						rx.setUnitName(RxUtil.getUnitNameFromQuantityText(quantity));
					}

					String frequency = rx.getFrequencyCode();
					String takeMin = rx.getTakeMinString();
					String takeMax = rx.getTakeMaxString();
					String durationUnit = rx.getDurationUnit();
					double nPerDay = 0d;
					double nDays = 0d;
					if (rx.getUnitName() != null || takeMin.equals("0") || takeMax.equals("0") || frequency.equals("")) {
					} else {
						if (durationUnit.equals("")) {
							durationUnit = "D";
						}

						nPerDay = RxUtil.findNPerDay(frequency);
						nDays = RxUtil.findNDays(durationUnit);
						if (RxUtil.isStringToNumber(quantity) && !rx.isDurationSpecifiedByUser()) {// don't not caculate duration if it's already specified by the user
							double qtyD = Double.parseDouble(quantity);
							// quantity=takeMax * nDays * duration * nPerDay
							double durD = qtyD / ((Double.parseDouble(takeMax)) * nPerDay * nDays);
							int durI = (int) durD;
							rx.setDuration(Integer.toString(durI));
						} else {
							// don't calculate duration if quantity can't be parsed to string
						}
						rx.setDurationUnit(durationUnit);
					}
					// duration=quantity divide by no. of pills per duration period.
					// if not, recalculate duration based on frequency if frequency is not empty
					// if there is already a duration uni present, use that duration unit. if not, set duration unit to days, and output duration in days
				}
				bean.addAttributeName(rx.getAtcCode() + "-" + String.valueOf(bean.getIndexFromRx(Integer.parseInt(randomId))));
				bean.setStashItem(bean.getIndexFromRx(Integer.parseInt(randomId)), rx);
				// RxUtil.printStashContent(bean);
				if (rx.getRoute() == null) {
					rx.setRoute("");
				}
				HashMap hm = new HashMap();
				hm.put("method", rx.getMethod());
				hm.put("takeMin", rx.getTakeMin());
				hm.put("takeMax", rx.getTakeMax());
				hm.put("duration", rx.getDuration());
				hm.put("frequency", rx.getFrequencyCode());
				hm.put("route", rx.getRoute());
				hm.put("durationUnit", rx.getDurationUnit());
				hm.put("prn", rx.getPrn());
				hm.put("calQuantity", rx.getQuantity());
				hm.put("unitName", rx.getUnitName());
				JSONObject jsonObject = JSONObject.fromObject(hm);
				response.getOutputStream().write(jsonObject.toString().getBytes());
			} catch (Exception e) {
				logger.error("Error", e);
			}
			return null;
		} else {
			return null;
		}
	}

	public ActionForward iterateStash(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)  {
		oscar.oscarRx.pageUtil.RxSessionBean bean = (oscar.oscarRx.pageUtil.RxSessionBean) request.getSession().getAttribute("RxSessionBean");
		List<RxPrescriptionData.Prescription> listP = Arrays.asList(bean.getStash());
		if (listP.size() == 0) {
			return null;
		} else {
			request.setAttribute("listRxDrugs", listP);
			return (mapping.findForward("newRx"));
		}

	}

	public ActionForward updateSpecialInstruction(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		checkPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), PRIVILEGE_WRITE);
		
		// get special instruction from parameter
		// get rx from random Id
		// rx.setspecialisntruction
		String randomId = request.getParameter("randomId");
		String specialInstruction = request.getParameter("specialInstruction");
		oscar.oscarRx.pageUtil.RxSessionBean bean = (oscar.oscarRx.pageUtil.RxSessionBean) request.getSession().getAttribute("RxSessionBean");
		RxPrescriptionData.Prescription rx = bean.getStashItem2(Integer.parseInt(randomId));
		if (specialInstruction.trim().length() > 0 && !specialInstruction.trim().equalsIgnoreCase("Enter Special Instruction")) {
			rx.setSpecialInstruction(specialInstruction.trim());
		} else {
			rx.setSpecialInstruction(null);
		}

		return null;
	}

	public ActionForward updateProperty(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		checkPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), PRIVILEGE_WRITE);
		
		oscar.oscarRx.pageUtil.RxSessionBean bean = (oscar.oscarRx.pageUtil.RxSessionBean) request.getSession().getAttribute("RxSessionBean");
		String elem = request.getParameter("elementId");
		String val = request.getParameter("propertyValue");
		val = val.trim();
		if (elem != null && val != null) {
			String[] strArr = elem.split("_");
			if (strArr.length > 1) {
				String num = strArr[1];
				num = num.trim();
				RxPrescriptionData.Prescription rx = bean.getStashItem2(Integer.parseInt(num));
				if (elem.equals("method_" + num)) {
					if (!val.equals("") && !val.equalsIgnoreCase("null")) rx.setMethod(val);
				} else if (elem.equals("route_" + num)) {
					if (!val.equals("") && !val.equalsIgnoreCase("null")) rx.setRoute(val);
				} else if (elem.equals("frequency_" + num)) {
					if (!val.equals("") && !val.equalsIgnoreCase("null")) rx.setFrequencyCode(val);
				} else if (elem.equals("minimum_" + num)) {
					if (!val.equals("") && !val.equalsIgnoreCase("null")) rx.setTakeMin(Float.parseFloat(val));
				} else if (elem.equals("maximum_" + num)) {
					if (!val.equals("") && !val.equalsIgnoreCase("null")) rx.setTakeMax(Float.parseFloat(val));
				} else if (elem.equals("duration_" + num)) {
					if (!val.equals("") && !val.equalsIgnoreCase("null")) rx.setDuration(val);
				} else if (elem.equals("durationUnit_" + num)) {
					if (!val.equals("") && !val.equalsIgnoreCase("null")) rx.setDurationUnit(val);
				} else if (elem.equals("prnVal_" + num)) {
					if (!val.equals("") && !val.equalsIgnoreCase("null")) {
						if (val.equalsIgnoreCase("true")) rx.setPrn(true);
						else rx.setPrn(false);
					} else rx.setPrn(false);
				}
			}
		}
		return null;
	}

	public ActionForward updateSaveAllDrugs(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, Exception {
		checkPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), PRIVILEGE_WRITE);
		
		oscar.oscarRx.pageUtil.RxSessionBean bean = (oscar.oscarRx.pageUtil.RxSessionBean) request.getSession().getAttribute("RxSessionBean");
		request.getSession().setAttribute("rePrint", null);// set to print.
		String onlyPrint = request.getParameter("onlyPrint");
		List<String> paramList = new ArrayList();
		Enumeration em = request.getParameterNames();
		List<String> randNum = new ArrayList();
		while (em.hasMoreElements()) {
			String ele = em.nextElement().toString();
			paramList.add(ele);
			if (ele.startsWith("drugName_")) {
				String rNum = ele.substring(9);
				if (!randNum.contains(rNum)) {
					randNum.add(rNum);
				}
			}
		}

		List<Integer> allIndex = new ArrayList();
		for (int i = 0; i < bean.getStashSize(); i++) {
			allIndex.add(i);
		}

		List<Integer> existingIndex = new ArrayList();
		for (String num : randNum) {
			int stashIndex = bean.getIndexFromRx(Integer.parseInt(num));
			try {
				if (stashIndex == -1) {
					continue;
				} else {
					existingIndex.add(stashIndex);
					RxPrescriptionData.Prescription rx = bean.getStashItem(stashIndex);

					boolean patientComplianceY = false;
					boolean patientComplianceN = false;
					boolean isOutsideProvider = false;
					boolean isLongTerm = false;
					boolean isShortTerm = false;
					boolean isPastMed = false;
					boolean isDispenseInternal=false;
					boolean isStartDateUnknown = false;
	                boolean isNonAuthoritative = false;
	                Date pickupDate;
	                Date pickupTime;
                    int dispenseInterval;
                    int refillDuration;
                    int refillQuantity;

					em = request.getParameterNames();
					while (em.hasMoreElements()) {
						String elem = (String) em.nextElement();
						String val = request.getParameter(elem);
						val = val.trim();
						if (elem.startsWith("drugName_" + num)) {
							if (rx.isCustom()) {
								rx.setCustomName(val);
								rx.setBrandName(null);
								rx.setGenericName(null);
							} else {
								rx.setBrandName(val);
							}
							
						} else if (elem.equals("repeats_" + num)) {
							if (val.equals("") || val == null) {
								rx.setRepeat(0);
							} else {
								rx.setRepeat(Integer.parseInt(val));
							}
						
						} else if(elem.equals("codingSystem_" + num)) {
							if(val != null) {
								rx.setDrugReasonCodeSystem(val);
							}
							
						} else if(elem.equals("reasonCode_" + num)) {
							if(val != null) {
								rx.setDrugReasonCode(val);
							}
						} else if (elem.equals("dispensingUnits_" + num)) {
							rx.setDispensingUnits(val);
						} else if (elem.equals("instructions_" + num)) {
							rx.setSpecial(val);
						} else if (elem.equals("quantity_" + num)) {
							if (val.equals("") || val == null) {
								rx.setQuantity("0");
							} else {
								if (RxUtil.isStringToNumber(val)) {
									rx.setQuantity(val);
									rx.setUnitName(null);
								} else {
									rx.setQuantity(RxUtil.getQuantityFromQuantityText(val));
									rx.setUnitName(RxUtil.getUnitNameFromQuantityText(val));
								}
							}
						} else if (elem.equals("longTerm_" + num)) {
							if (val.equals("on")) {
								isLongTerm = true;
							} else {
								isLongTerm = false;
							}
						} else if (elem.equals("shortTerm_" + num)) {
							if (val.equals("on")) {
								isShortTerm = true;
							} else {
								isShortTerm = false;
							}	
                        } else if (elem.equals("nonAuthoritativeN_" + num)) {
							if (val.equals("on")) {
								isNonAuthoritative = true;
							} else {
								isNonAuthoritative = false;
							}
                        } else if(elem.equals("refillDuration_"+num)) {
                        	rx.setRefillDuration(Integer.parseInt(val));
                        } else if(elem.equals("refillQuantity_"+num)) {
                        	rx.setRefillQuantity(Integer.parseInt(val));
                        } else if(elem.equals("dispenseInterval_"+num)) {
                        	rx.setDispenseInterval(Integer.parseInt(val));
						} else if (elem.equals("lastRefillDate_" + num)) {
							rx.setLastRefillDate(RxUtil.StringToDate(val, "yyyy-MM-dd"));
						} else if (elem.equals("rxDate_" + num)) {
							if ((val == null) || (val.equals(""))) {
								rx.setRxDate(RxUtil.StringToDate("1900-01-01", "yyyy-MM-dd"));
							} else {
								rx.setRxDate(RxUtil.StringToDate(val, "yyyy-MM-dd"));
							}
                                                } else if (elem.equals("pickupDate_" + num)) {
							if ((val == null) || (val.equals(""))) {
								rx.setPickupDate(null);
                                                                rx.setPickupTime(null);
							} else {
								rx.setPickupDate(RxUtil.StringToDate(val, "yyyy-MM-dd"));
							}
                                                } else if (elem.equals("pickupTime_" + num)) {
							if ((val != null) && (!val.equals(""))) {
								rx.setPickupTime(RxUtil.StringToDate(val, "hh:mm"));
							}
						} else if (elem.equals("writtenDate_" + num)) {
							if (val == null || (val.equals(""))) {
								rx.setWrittenDate(RxUtil.StringToDate("1900-01-01", "yyyy-MM-dd"));
							} else {
								rx.setWrittenDateFormat(partialDateDao.getFormat(val));
								rx.setWrittenDate(partialDateDao.StringToDate(val));
							}

						} else if (elem.equals("outsideProviderName_" + num)) {
							rx.setOutsideProviderName(val);
						} else if (elem.equals("outsideProviderOhip_" + num)) {
							if (val.equals("") || val == null) {
								rx.setOutsideProviderOhip("0");
							} else {
								rx.setOutsideProviderOhip(val);
							}
						} else if (elem.equals("ocheck_" + num)) {
							if (val.equals("on")) {
								isOutsideProvider = true;
							} else {
								isOutsideProvider = false;
							}
						} else if (elem.equals("pastMed_" + num)) {
							if (val.equals("on")) {
								isPastMed = true;
							} else {
								isPastMed = false;
							}
						} else if (elem.equals("dispenseInternal_" + num)) {
							if (val.equals("on")) {
								isDispenseInternal = true;
							} else {
								isDispenseInternal = false;
							}
						} else if (elem.equals("startDateUnknown_" + num)) {
							if (val.equals("on")) {
								isStartDateUnknown = true;
							} else {
								isStartDateUnknown = false;
							}
						} else if (elem.equals("comment_" + num)) {
							rx.setComment(val);
						} else if (elem.equals("patientComplianceY_" + num)) {
							if (val.equals("on")) {
								patientComplianceY = true;
							} else {
								patientComplianceY = false;
							}
						} else if (elem.equals("patientComplianceN_" + num)) {
							if (val.equals("on")) {
								patientComplianceN = true;
							} else {
								patientComplianceN = false;
							}
						} else if (elem.equals("eTreatmentType_"+num)){
							if("--".equals(val)){
							   rx.setETreatmentType(null);
							}else{
							   rx.setETreatmentType(val);
							}
						} else if (elem.equals("rxStatus_"+num)){
							if("--".equals(val)){
							   rx.setRxStatus(null);
							}else{
							   rx.setRxStatus(val);
							}
						} else if (elem.equals("drugForm_"+num)){
							rx.setDrugForm(val);
						}

					}

					// get Methadone or Suboxone information
					int rxMod = 1;
					if (OscarProperties.getInstance().isPropertyActive("enable_rx_custom_methodone_suboxone")) {
						String rxModules = request.getParameter("rxModules_" + num);
						if (rxModules != null) {
							try {
								rxMod = Integer.parseInt(rxModules);
							} catch (Exception e) {}
						}
						if (rxMod == 2) { // Methadone
							// start date and end date
							String val = request.getParameter("methadoneStartDate_"+num);
							if ((val == null) || (val.equals(""))) {
								rx.setRxDate(RxUtil.StringToDate("1900-01-01", "yyyy-MM-dd"));
							} else {
								rx.setRxDate(RxUtil.StringToDate(val, "yyyy-MM-dd"));
							}
							val = request.getParameter("methdoneEndDate_"+num);
							if (val == null || val.isEmpty()) {
								rx.setEndDate(RxUtil.StringToDate("1900-01-01", "yyyy-MM-dd"));
							} else {
								rx.setEndDate(RxUtil.StringToDate(val, "yyyy-MM-dd"));
							}
							
							// days circled
							StringBuilder sb = new StringBuilder(); 
							val = request.getParameter("drkMon_"+num);
							if (val != null && val.equals("on")) {
								sb.append("MON,");
							}
							val = request.getParameter("drkTues_"+num);
							if (val != null && val.equals("on")) {
								sb.append("TUES,");
							}
							val = request.getParameter("drkWed_"+num);
							if (val != null && val.equals("on")) {
								sb.append("WED,");
							}
							val = request.getParameter("drkThurs_"+num);
							if (val != null && val.equals("on")) {
								sb.append("THURS,");
							}
							val = request.getParameter("drkFri_"+num);
							if (val != null && val.equals("on")) {
								sb.append("FRI,");
							}
							val = request.getParameter("drkSat_"+num);
							if (val != null && val.equals("on")) {
								sb.append("SAT,");
							}
							val = request.getParameter("drkSun_"+num);
							if (val != null && val.equals("on")) {
								sb.append("SUN,");
							}
							if (sb.length() > 0) {
								sb.deleteCharAt(sb.length() - 1);
							}
							sb.append(";");
							int len = sb.length();
							
							// take home doses
							boolean bTakeHome = false;
							val = request.getParameter("homedoseMonMeth_"+num);
							if (val != null && val.equals("on")) {
								sb.append("MON,");
								bTakeHome = true;
							}
							val = request.getParameter("homedoseTuesMeth_"+num);
							if (val != null && val.equals("on")) {
								sb.append("TUES,");
								bTakeHome = true;
							}
							val = request.getParameter("homedoseWedMeth_"+num);
							if (val != null && val.equals("on")) {
								sb.append("WED,");
								bTakeHome = true;
							}
							val = request.getParameter("homedoseThursMeth_"+num);
							if (val != null && val.equals("on")) {
								sb.append("THURS,");
								bTakeHome = true;
							}
							val = request.getParameter("homedoseFriMeth_"+num);
							if (val != null && val.equals("on")) {
								sb.append("FRI,");
								bTakeHome = true;
							}
							val = request.getParameter("homedoseSatMeth_"+num);
							if (val != null && val.equals("on")) {
								sb.append("SAT,");
								bTakeHome = true;
							}
							val = request.getParameter("homedoseSunMeth_"+num);
							if (val != null && val.equals("on")) {
								sb.append("SUN,");
								bTakeHome = true;
							}
							val = request.getParameter("homedoseNoMeth_"+num);
							if (val != null && val.equals("on") && bTakeHome) {
								sb.append("CARRY,");
							}
							if (sb.length() > len) {
								sb.deleteCharAt(sb.length() - 1);
							} else {
								sb.append("NONE");
							}
							sb.append(";" + request.getParameter("carryLevelMeth_"+num));
							rx.setComment(sb.toString());
							
							// exceed dosage from methadone
							/*
							val = request.getParameter("excQtyMeth_"+num);
							if (val == null) {
								val = "";
							}
							rx.setDosage(val);
							*/
						} else if (rxMod == 3) { // Suboxone
							String val = request.getParameter("suboxoneStartDate_"+num);
							if ((val == null) || (val.equals(""))) {
								rx.setRxDate(RxUtil.StringToDate("1900-01-01", "yyyy-MM-dd"));
							} else {
								rx.setRxDate(RxUtil.StringToDate(val, "yyyy-MM-dd"));
							}
							val = request.getParameter("suboxoneEndDate_"+num);
							if (val == null || val.isEmpty()) {
								rx.setEndDate(RxUtil.StringToDate("1900-01-01", "yyyy-MM-dd"));
							} else {
								rx.setEndDate(RxUtil.StringToDate(val, "yyyy-MM-dd"));
							}
							
							// days circled
							StringBuilder sb = new StringBuilder(); 
							val = request.getParameter("doseMonSub_"+num);
							if (val != null && val.equals("on")) {
								sb.append("MON,");
							}
							val = request.getParameter("doseTuesSub_"+num);
							if (val != null && val.equals("on")) {
								sb.append("TUES,");
							}
							val = request.getParameter("doseWedSub_"+num);
							if (val != null && val.equals("on")) {
								sb.append("WED,");
							}
							val = request.getParameter("doseThursSub_"+num);
							if (val != null && val.equals("on")) {
								sb.append("THURS,");
							}
							val = request.getParameter("doseFriSub_"+num);
							if (val != null && val.equals("on")) {
								sb.append("FRI,");
							}
							val = request.getParameter("doseSatSub_"+num);
							if (val != null && val.equals("on")) {
								sb.append("SAT,");
							}
							val = request.getParameter("doseSunSub_"+num);
							if (val != null && val.equals("on")) {
								sb.append("SUN,");
							}
							if (sb.length() > 0) {
								sb.deleteCharAt(sb.length() - 1);
							}
							sb.append(";");
							int len = sb.length();
							
							// take home doses
							boolean bTakeHome = false;
							val = request.getParameter("homedoseMonSub_"+num);
							if (val != null && val.equals("on")) {
								sb.append("MON,");
								bTakeHome = true;
							}
							val = request.getParameter("homedoseTuesSub_"+num);
							if (val != null && val.equals("on")) {
								sb.append("TUES,");
								bTakeHome = true;
							}
							val = request.getParameter("homedoseWedSub_"+num);
							if (val != null && val.equals("on")) {
								sb.append("WED,");
								bTakeHome = true;
							}
							val = request.getParameter("homedoseThursSub_"+num);
							if (val != null && val.equals("on")) {
								sb.append("THURS,");
								bTakeHome = true;
							}
							val = request.getParameter("homedoseFriSub_"+num);
							if (val != null && val.equals("on")) {
								sb.append("FRI,");
								bTakeHome = true;
							}
							val = request.getParameter("homedoseSatSub_"+num);
							if (val != null && val.equals("on")) {
								sb.append("SAT,");
								bTakeHome = true;
							}
							val = request.getParameter("homedoseSunSub_"+num);
							if (val != null && val.equals("on")) {
								sb.append("SUN,");
								bTakeHome = true;
							}
							val = request.getParameter("homedoseNoSub_"+num);
							if (val != null && val.equals("on") && bTakeHome) {
								sb.append("CARRY,");
							}
							
							if (sb.length() > len) {
								sb.deleteCharAt(sb.length() - 1);
							} else {
								sb.append("NONE");
							}
							sb.append(";" + request.getParameter("carryLevelSub_"+num));
							rx.setComment(sb.toString());
							
							// exceed dosage from subonxone
							/* do not save and overwrite dosage
							val = request.getParameter("excQtySub_"+num);
							if (val == null) {
								val = "";
							}
							rx.setDosage(val);
							*/
						}
					}
					
					if (!isOutsideProvider) {
						rx.setOutsideProviderName("");
						rx.setOutsideProviderOhip("");
					}
					rx.setPastMed(isPastMed);
					rx.setDispenseInternal(isDispenseInternal);
					rx.setStartDateUnknown(isStartDateUnknown);
					rx.setLongTerm(isLongTerm);
					rx.setShortTerm(isShortTerm);
                                        rx.setNonAuthoritative(isNonAuthoritative);
					String newline = System.getProperty("line.separator");
					rx.setPatientCompliance(patientComplianceY, patientComplianceN);
					String special;
					if (rx.isCustomNote()) {
						rx.setQuantity(null);
						rx.setUnitName(null);
						rx.setRepeat(0);
						special = rx.getCustomName() + newline + rx.getSpecial();
						if (rx.getSpecialInstruction() != null && !rx.getSpecialInstruction().equalsIgnoreCase("null") && rx.getSpecialInstruction().trim().length() > 0) special += newline + rx.getSpecialInstruction();
					} else if (rx.isCustom()) {// custom drug
						if (rx.getUnitName() == null) {
							special = rx.getCustomName() + newline + (rx.getSpecial()==null?"":rx.getSpecial());
							if (rx.getSpecialInstruction() != null && !rx.getSpecialInstruction().equalsIgnoreCase("null") && rx.getSpecialInstruction().trim().length() > 0) special += newline + rx.getSpecialInstruction();
							if (rxMod == 1) {
								special += newline + "Qty:" + rx.getQuantity() + " " + rx.getDispensingUnits() + " Repeats:" + "" + rx.getRepeat();
							}
						} else {
							special = rx.getCustomName() + newline + (rx.getSpecial()==null?"":rx.getSpecial());
							if (rx.getSpecialInstruction() != null && !rx.getSpecialInstruction().equalsIgnoreCase("null") && rx.getSpecialInstruction().trim().length() > 0) special += newline + rx.getSpecialInstruction();
							if (rxMod == 1) {
								special += newline + "Qty:" + rx.getQuantity() + " " + rx.getUnitName() + " Repeats:" + "" + rx.getRepeat();
							}
						}
					} else {// non-custom drug
						if (rx.getUnitName() == null) {
							special = rx.getBrandName() + newline + (rx.getSpecial()==null?"":rx.getSpecial());
							if (rx.getSpecialInstruction() != null && !rx.getSpecialInstruction().equalsIgnoreCase("null") && rx.getSpecialInstruction().trim().length() > 0) special += newline + rx.getSpecialInstruction();
							if (rxMod == 1) {
								special += newline + "Qty:" + rx.getQuantity() + " Repeats:" + "" + rx.getRepeat();
							}
						} else {
							special = rx.getBrandName() + newline + (rx.getSpecial()==null?"":rx.getSpecial());
							if (rx.getSpecialInstruction() != null && !rx.getSpecialInstruction().equalsIgnoreCase("null") && rx.getSpecialInstruction().trim().length() > 0) special += newline + rx.getSpecialInstruction();
							if (rxMod == 1) {
								special += newline + "Qty:" + rx.getQuantity() + " " + rx.getUnitName() + " Repeats:" + "" + rx.getRepeat();
							}
						}
					}

					if (!rx.isCustomNote() && rx.isMitte()) {
						special = special.replace("Qty", "Mitte");
					}

					rx.setSpecial(special.trim());

					bean.addAttributeName(rx.getAtcCode() + "-" + String.valueOf(stashIndex));
					bean.setStashItem(stashIndex, rx);
				}
			} catch (Exception e) {
				logger.error("Error", e);
				continue;
			}
		}
		for (Integer n : existingIndex) {
			if (allIndex.contains(n)) {
				allIndex.remove(n);
			}
		}
		List<Integer> deletedIndex = allIndex;
		// remove closed Rx from stash
		for (Integer n : deletedIndex) {
			bean.removeStashItem(n);
			if (bean.getStashIndex() >= bean.getStashSize()) {
				bean.setStashIndex(bean.getStashSize() - 1);
			}
		}

		if (!"true".equals(onlyPrint)) { // #331 feature 
			saveDrug(request);
		}
		return null;
	}

        public ActionForward getDemoNameAndHIN(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, Exception {
        	LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);
    		if (!securityInfoManager.hasPrivilege(loggedInInfo, "_demographic", PRIVILEGE_READ, null)) {
    			throw new RuntimeException("missing required security object (_demographic)");
    		}
        	
            String demoNo=request.getParameter("demoNo").trim();
            Demographic d=demographicManager.getDemographic(loggedInInfo, demoNo);
            HashMap hm=new HashMap();
            if(d!=null){
                hm.put("patientName", d.getDisplayName());
                hm.put("patientHIN", d.getHin());
            }else{
                hm.put("patientName", "Unknown");
                hm.put("patientHIN", "Unknown");
            }
            JSONObject jo=JSONObject.fromObject(hm);
            response.getOutputStream().write(jo.toString().getBytes());
            return null;
        }
        
	public ActionForward changeToLongTerm(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, Exception {
		checkPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), PRIVILEGE_WRITE);
		
		String strId = request.getParameter("ltDrugId");
		if (strId != null) {
			int drugId = Integer.parseInt(strId);
			RxSessionBean bean = (RxSessionBean) request.getSession().getAttribute("RxSessionBean");
			if (bean == null) {
				response.sendRedirect("error.html");
				return null;
			}

			RxPrescriptionData rxData = new RxPrescriptionData();
			RxPrescriptionData.Prescription oldRx = rxData.getPrescription(drugId);
			oldRx.setLongTerm(true);
			oldRx.setShortTerm(false);
			boolean b = oldRx.Save(oldRx.getScript_no());
			HashMap hm = new HashMap();
			if (b) hm.put("success", true);
			else hm.put("success", false);
			JSONObject jsonObject = JSONObject.fromObject(hm);
			response.getOutputStream().write(jsonObject.toString().getBytes());
			return null;
		} else {
			HashMap hm = new HashMap();
			hm.put("success", false);
			JSONObject jsonObject = JSONObject.fromObject(hm);
			response.getOutputStream().write(jsonObject.toString().getBytes());
			return null;
		}
	}

	public void saveDrug(final HttpServletRequest request) throws Exception {
		LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);
		checkPrivilege(loggedInInfo, PRIVILEGE_WRITE);
		
		oscar.oscarRx.pageUtil.RxSessionBean bean = (oscar.oscarRx.pageUtil.RxSessionBean) request.getSession().getAttribute("RxSessionBean");

		RxPrescriptionData.Prescription rx = null;
		RxPrescriptionData prescription = new RxPrescriptionData();
		String scriptId = prescription.saveScript(loggedInInfo, bean);
		StringBuilder auditStr = new StringBuilder();
		ArrayList<String> attrib_names = bean.getAttributeNames();
		
		for (int i = 0; i < bean.getStashSize(); i++) {
			try {
				rx = bean.getStashItem(i);
				rx.Save(scriptId);// new drug id available after this line			
				bean.addRandomIdDrugIdPair(rx.getRandomId(), rx.getDrugId());
				auditStr.append(rx.getAuditString());
				auditStr.append("\n");
				
				// save drug reason. Method borrowed from 
				// RxReasonAction. 
				if( ! StringUtils.isNullOrEmpty( rx.getDrugReasonCode() ) ) {
					addDrugReason( rx.getDrugReasonCodeSystem(), 
							"false", "", rx.getDrugReasonCode(), 
							rx.getDrugId()+"", rx.getDemographicNo()+"",  
							rx.getProviderNo(), request );
				}

				//write partial date
				if (StringUtils.filled(rx.getWrittenDateFormat()))
					partialDateDao.setPartialDate(PartialDate.DRUGS, rx.getDrugId(), PartialDate.DRUGS_WRITTENDATE, rx.getWrittenDateFormat());
			} catch (Exception e) {
				logger.error("Error", e);
			}

			// Save annotation
			HttpSession se = request.getSession();
			WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(se.getServletContext());
			CaseManagementManager cmm = (CaseManagementManager) ctx.getBean("caseManagementManager");
			String attrib_name = attrib_names.get(i);
			if (attrib_name != null) {
				CaseManagementNote cmn = (CaseManagementNote) se.getAttribute(attrib_name);
				if (cmn != null) {
					cmm.saveNoteSimple(cmn);
					CaseManagementNoteLink cml = new CaseManagementNoteLink();
					cml.setTableName(CaseManagementNoteLink.DRUGS);
					cml.setTableId((long) rx.getDrugId());
					cml.setNoteId(cmn.getId());
					cmm.saveNoteLink(cml);
					se.removeAttribute(attrib_name);
					LogAction.addLog(cmn.getProviderNo(), LogConst.ANNOTATE, CaseManagementNoteLink.DISP_PRESCRIP, scriptId, request.getRemoteAddr(), cmn.getDemographic_no(), cmn.getNote());
				}
			}
			rx = null;
		}

		String ip = request.getRemoteAddr();
		request.setAttribute("scriptId", scriptId);
        
		List<String> reRxDrugList = new ArrayList<String>();
        reRxDrugList=bean.getReRxDrugIdList();        
        
        Iterator<String> i = reRxDrugList.iterator();
        
        DrugDao drugDao = (DrugDao) SpringUtils.getBean("drugDao"); 
        
        while (i.hasNext()) {
        
        String item = i.next();
        
        //archive drug(s)
        Drug drug = drugDao.find(Integer.parseInt(item));
        drug.setArchived(true);
        drug.setArchivedDate(new Date());
        drug.setArchivedReason(Drug.REPRESCRIBED);       
        drugDao.merge(drug);
              
        //log that this med is being re-prescribed
        LogAction.addLog((String) request.getSession().getAttribute("user"), LogConst.REPRESCRIBE, LogConst.CON_MEDICATION, "drugid="+item, ip, "" + bean.getDemographicNo(), auditStr.toString());
        
        //log that the med is being discontinued buy the system
        LogAction.addLog("-1", LogConst.DISCONTINUE, LogConst.CON_MEDICATION, "drugid="+item, "", "" + bean.getDemographicNo(), auditStr.toString());
        
        }
		LogAction.addLog((String) request.getSession().getAttribute("user"), LogConst.ADD, LogConst.CON_PRESCRIPTION, scriptId, ip, "" + bean.getDemographicNo(), auditStr.toString());

		return;
	}

	public ActionForward checkNoStashItem(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, Exception {
		oscar.oscarRx.pageUtil.RxSessionBean bean = (oscar.oscarRx.pageUtil.RxSessionBean) request.getSession().getAttribute("RxSessionBean");
		int n = bean.getStashSize();
		HashMap hm = new HashMap();
		hm.put("NoStashItem", n);
		JSONObject jsonObject = JSONObject.fromObject(hm);
		response.getOutputStream().write(jsonObject.toString().getBytes());
		return null;
	}
	
	
	private void checkPrivilege(LoggedInInfo loggedInInfo, String privilege) {
		if (!securityInfoManager.hasPrivilege(loggedInInfo, "_rx", privilege, null)) {
			throw new RuntimeException("missing required security object (_rx)");
		}
	}

	private void addDrugReason(String codingSystem, 
			String primaryReasonFlagStr, String comments, 
			String code, String drugIdStr, String demographicNo,  
			String providerNo, HttpServletRequest request ) {
		
		MessageResources mResources = MessageResources.getMessageResources( "oscarResources" );
		DrugReasonDao drugReasonDao = (DrugReasonDao) SpringUtils.getBean("drugReasonDao");
		Integer drugId = Integer.parseInt(drugIdStr);
		
		// should this be instantiated with the Spring Utilities?
		CodingSystemManager codingSystemManager = new CodingSystemManager();

		if ( ! codingSystemManager.isCodeAvailable(codingSystem, code) ){
			request.setAttribute("message", mResources.getMessage("SelectReason.error.codeValid"));
			return;
		}

        if(drugReasonDao.hasReason(drugId, codingSystem, code, true)){
        	request.setAttribute("message", mResources.getMessage("SelectReason.error.duplicateCode"));
        	return;
        }

        MiscUtils.getLogger().debug("addDrugReasonCalled codingSystem "+codingSystem+ " code "+code+ " drugIdStr "+drugId);

        boolean primaryReasonFlag = true;
        if(!"true".equals(primaryReasonFlagStr)){
        	primaryReasonFlag = false;
        }

        DrugReason dr = new DrugReason();

        dr.setDrugId(drugId);
        dr.setProviderNo(providerNo);
        dr.setDemographicNo(Integer.parseInt(demographicNo));

        dr.setCodingSystem(codingSystem);
        dr.setCode(code);
        dr.setComments(comments);
        dr.setPrimaryReasonFlag(primaryReasonFlag);
        dr.setArchivedFlag(false);
        dr.setDateCoded(new Date());

        drugReasonDao.addNewDrugReason(dr);

        String ip = request.getRemoteAddr();
        LogAction.addLog((String) request.getSession().getAttribute("user"), LogConst.ADD, LogConst.CON_DRUGREASON, ""+dr.getId() , ip,demographicNo,dr.getAuditString());

	}
}
