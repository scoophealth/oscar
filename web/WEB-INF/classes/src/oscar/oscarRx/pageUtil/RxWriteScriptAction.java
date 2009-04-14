/*
 * 
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
 * This software is published under the GPL GNU General Public License. 
 * This program is free software; you can redistribute it and/or 
 * modify it under the terms of the GNU General Public License 
 * as published by the Free Software Foundation; either version 2 
 * of the License, or (at your option) any later version. * 
 * This program is distributed in the hope that it will be useful, 
 * but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
 * GNU General Public License for more details. * * You should have received a copy of the GNU General Public License 
 * along with this program; if not, write to the Free Software 
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. * 
 * 
 * <OSCAR TEAM>
 * 
 * This software was written for the 
 * Department of Family Medicine 
 * McMaster University 
 * Hamilton 
 * Ontario, Canada 
 */
package oscar.oscarRx.pageUtil;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.oscarehr.casemgmt.model.CaseManagementNote;
import org.oscarehr.casemgmt.model.CaseManagementNoteLink;
import org.oscarehr.casemgmt.service.CaseManagementManager;
import org.oscarehr.util.MiscUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import oscar.log.LogAction;
import oscar.log.LogConst;
import oscar.oscarRx.data.RxDrugData;
import oscar.oscarRx.data.RxPrescriptionData;
import oscar.oscarRx.util.RxUtil;

public final class RxWriteScriptAction extends Action {
	
	private static final Logger logger=MiscUtils.getLogger();
	
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, Exception {

		// Setup variables
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
			rx.setRepeat(frm.getRepeat());
			rx.setLastRefillDate(RxUtil.StringToDate(frm.getLastRefillDate(), "yyyy-MM-dd"));
			rx.setNosubs(frm.getNosubs());
			rx.setPrn(frm.getPrn());
			rx.setSpecial(frm.getSpecial());
			rx.setAtcCode(frm.getAtcCode());
			rx.setRegionalIdentifier(frm.getRegionalIdentifier());
			rx.setUnit(frm.getUnit());
			rx.setUnitName(frm.getUnitName());
			rx.setMethod(frm.getMethod());
			rx.setRoute(frm.getRoute());
			rx.setCustomInstr(frm.getCustomInstr());
			rx.setDosage(frm.getDosage());
			rx.setOutsideProviderName(frm.getOutsideProviderName());
			rx.setOutsideProviderOhip(frm.getOutsideProviderOhip());
			rx.setLongTerm(frm.getLongTerm());
			rx.setPastMed(frm.getPastMed());
			rx.setPatientCompliance(frm.getPatientComplianceY(), frm.getPatientComplianceN());
			rx.setDrugForm(drugData.getDrugForm(String.valueOf(frm.getGCN_SEQNO())));
			
			logger.debug("SAVING STASH " + rx.getCustomInstr());
			if (rx.getSpecial()==null) logger.error("Drug.special is null : "+rx.getSpecial()+" : "+frm.getSpecial());
			else if (rx.getSpecial().length()<6) logger.warn("Drug.special appears to be empty : "+rx.getSpecial()+" : "+frm.getSpecial());

			bean.setStashItem(bean.getStashIndex(), rx);
			rx = null;

			if (bean.getStashSize() > bean.getAttributeNames().size()) {
				String annotation_attrib = request.getParameter("annotation_attrib");
				if (annotation_attrib == null) annotation_attrib = "";
				bean.addAttributeName(annotation_attrib);
			}

			if (frm.getAction().equals("update")) {
				fwd = "refresh";
			}
			if (frm.getAction().equals("updateAddAnother")) {
				fwd = "addAnother";
			}
			if (frm.getAction().equals("updateAndPrint")) {
				int i;
				String scriptId = prescription.saveScript(bean);

				@SuppressWarnings("unchecked")
				ArrayList<String> attrib_names = bean.getAttributeNames();
				for (i = 0; i < bean.getStashSize(); i++) {
					rx = bean.getStashItem(i);
					rx.Save(scriptId);

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
						}
					}
					rx = null;
				}

				fwd = "viewScript";
				String ip = request.getRemoteAddr();
				request.setAttribute("scriptId", scriptId);
				LogAction.addLog((String) request.getSession().getAttribute("user"), LogConst.ADD, LogConst.CON_PRESCRIPTION, scriptId, ip, "" + bean.getDemographicNo());

			}
		}
		return mapping.findForward(fwd);
	}
}