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
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.oscarehr.common.dao.DrugDao;
import org.oscarehr.common.model.Drug;
import org.oscarehr.managers.SecurityInfoManager;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.log.LogAction;
import oscar.log.LogConst;
import oscar.oscarRx.data.RxPrescriptionData;
import oscar.oscarRx.data.RxPrescriptionData.Prescription;
import oscar.oscarRx.util.RxUtil;

public final class RxRePrescribeAction extends DispatchAction {
	
	private static final String PRIVILEGE_READ = "r"; 
	private static final String PRIVILEGE_WRITE = "w";

	private static final Logger logger = MiscUtils.getLogger();
	private SecurityInfoManager securityInfoManager = SpringUtils.getBean(SecurityInfoManager.class);

	public ActionForward reprint(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException {

		LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
		checkPrivilege(loggedInInfo, PRIVILEGE_READ);
		
		oscar.oscarRx.pageUtil.RxSessionBean sessionBeanRX = (oscar.oscarRx.pageUtil.RxSessionBean) request.getSession().getAttribute("RxSessionBean");
		if (sessionBeanRX == null) {
			response.sendRedirect("error.html");
			return null;
		}

		oscar.oscarRx.pageUtil.RxSessionBean beanRX = new oscar.oscarRx.pageUtil.RxSessionBean();
		beanRX.setDemographicNo(sessionBeanRX.getDemographicNo());
		beanRX.setProviderNo(sessionBeanRX.getProviderNo());

		RxDrugListForm frm = (RxDrugListForm) form;
		String script_no = frm.getDrugList();

		String ip = request.getRemoteAddr();

		RxPrescriptionData rxData = new RxPrescriptionData();
		List<Prescription> list = rxData.getPrescriptionsByScriptNo(Integer.parseInt(script_no), sessionBeanRX.getDemographicNo());
		RxPrescriptionData.Prescription p = null;
		StringBuilder auditStr = new StringBuilder();
		for (int idx = 0; idx < list.size(); ++idx) {
			p = list.get(idx);
			beanRX.setStashIndex(beanRX.addStashItem(loggedInInfo, p));
			auditStr.append(p.getAuditString() + "\n");
		}

		// save print date/time to prescription table
		if (p != null) {
			p.Print(loggedInInfo);
		}

		String comment = rxData.getScriptComment(script_no);

		request.getSession().setAttribute("tmpBeanRX", beanRX);
		request.setAttribute("rePrint", "true");
		request.setAttribute("comment", comment);

		LogAction.addLog((String) request.getSession().getAttribute("user"), LogConst.REPRINT, LogConst.CON_PRESCRIPTION, script_no, ip, "" + beanRX.getDemographicNo(), auditStr.toString());

		return mapping.findForward("reprint");
	}

	public ActionForward reprint2(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException {

		LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
		checkPrivilege(loggedInInfo, PRIVILEGE_READ);

		oscar.oscarRx.pageUtil.RxSessionBean sessionBeanRX = (oscar.oscarRx.pageUtil.RxSessionBean) request.getSession().getAttribute("RxSessionBean");
		if (sessionBeanRX == null) {
			response.sendRedirect("error.html");
			return null;
		}

		oscar.oscarRx.pageUtil.RxSessionBean beanRX = new oscar.oscarRx.pageUtil.RxSessionBean();
		beanRX.setDemographicNo(sessionBeanRX.getDemographicNo());
		beanRX.setProviderNo(sessionBeanRX.getProviderNo());

		// RxDrugListForm frm = (RxDrugListForm) form;
		String script_no = request.getParameter("scriptNo");
		String ip = request.getRemoteAddr();
		RxPrescriptionData rxData = new RxPrescriptionData();
		List<Prescription> list = rxData.getPrescriptionsByScriptNo(Integer.parseInt(script_no), sessionBeanRX.getDemographicNo());
		RxPrescriptionData.Prescription p = null;
		StringBuilder auditStr = new StringBuilder();
		for (int idx = 0; idx < list.size(); ++idx) {
			p = list.get(idx);
			beanRX.setStashIndex(beanRX.addStashItem(loggedInInfo, p));
			auditStr.append(p.getAuditString() + "\n");
		}
		// p("auditStr "+auditStr.toString());
		// save print date/time
		if (p != null) {
			p.Print(loggedInInfo);
		}

		String comment = rxData.getScriptComment(script_no);
		request.getSession().setAttribute("tmpBeanRX", beanRX);
		request.getSession().setAttribute("rePrint", "true");
		request.getSession().setAttribute("comment", comment);
		LogAction.addLog((String) request.getSession().getAttribute("user"), LogConst.REPRINT, LogConst.CON_PRESCRIPTION, script_no, ip, "" + beanRX.getDemographicNo(), auditStr.toString());

		return mapping.findForward(null);
	}

	public ActionForward represcribe(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException {
		LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);
		checkPrivilege(loggedInInfo, PRIVILEGE_WRITE);
		
		oscar.oscarRx.pageUtil.RxSessionBean beanRX = (oscar.oscarRx.pageUtil.RxSessionBean) request.getSession().getAttribute("RxSessionBean");
		if (beanRX == null) {
			response.sendRedirect("error.html");
			return null;
		}
		RxDrugListForm frm = (RxDrugListForm) form;
		StringBuilder auditStr = new StringBuilder();
		try {
			RxPrescriptionData rxData = new RxPrescriptionData();

			String drugList = frm.getDrugList();

			String[] drugArr = drugList.split(",");

			int drugId;
			int i;

			for (i = 0; i < drugArr.length; i++) {
				try {
					drugId = Integer.parseInt(drugArr[i]);
				} catch (Exception e) {
					logger.error("Unexpected error.", e);
					break;
				}

				// get original drug
				RxPrescriptionData.Prescription oldRx = rxData.getPrescription(drugId);

				// create copy of Prescription
				RxPrescriptionData.Prescription rx = rxData.newPrescription(beanRX.getProviderNo(), beanRX.getDemographicNo(), oldRx);

				beanRX.setStashIndex(beanRX.addStashItem(loggedInInfo, rx));
				auditStr.append(rx.getAuditString() + "\n");

				// allocate space for annotation
				beanRX.addAttributeName(rx.getAtcCode() + "-" + String.valueOf(beanRX.getStashIndex()));
				// p("beanRX.getStashIndex() in represcribe after", "" + beanRX.getStashIndex());
				request.setAttribute("BoxNoFillFirstLoad", "true");
			}
		} catch (Exception e) {
			logger.error("Unexpected error occurred.", e);
		}

		return (mapping.findForward("success"));
	}

	public ActionForward saveReRxDrugIdToStash(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException {
		MiscUtils.getLogger().debug("================in saveReRxDrugIdToStash  of RxRePrescribeAction.java=================");
		LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);
		
		oscar.oscarRx.pageUtil.RxSessionBean bean = (oscar.oscarRx.pageUtil.RxSessionBean) request.getSession().getAttribute("RxSessionBean");
		if (bean == null) {
			response.sendRedirect("error.html");
			return null;
		}
		StringBuilder auditStr = new StringBuilder();

		RxPrescriptionData rxData = new RxPrescriptionData();

		// String strId = (request.getParameter("drugId").split("_"))[1];
		String strId = request.getParameter("drugId");
		try {
			int drugId = Integer.parseInt(strId);
			// get original drug
			RxPrescriptionData.Prescription oldRx = rxData.getPrescription(drugId);
			// create copy of Prescription
			RxPrescriptionData.Prescription rx = rxData.newPrescription(bean.getProviderNo(), bean.getDemographicNo(), oldRx); // set writtendate, rxdate ,enddate=null.
			Long rand = Math.round(Math.random() * 1000000);
			rx.setRandomId(rand);

			request.setAttribute("BoxNoFillFirstLoad", "true");
			String qText = rx.getQuantity();
			MiscUtils.getLogger().debug("qText in represcribe2=" + qText);
			if (qText != null && RxUtil.isStringToNumber(qText)) {
			} else {
				rx.setQuantity(RxUtil.getQuantityFromQuantityText(qText));
				rx.setUnitName(RxUtil.getUnitNameFromQuantityText(qText));
			}
			MiscUtils.getLogger().debug("quantity, unitName represcribe2=" + rx.getQuantity() + "; " + rx.getUnitName());
			// trim Special
			String spec = RxUtil.trimSpecial(rx);
			rx.setSpecial(spec);

			List<RxPrescriptionData.Prescription> listReRx = new ArrayList<Prescription>();
			rx.setDiscontinuedLatest(RxUtil.checkDiscontinuedBefore(rx));
			// add rx to rx list
			if (RxUtil.isRxUniqueInStash(bean, rx)) {
				listReRx.add(rx);
			}
			// save rx to stash
			int rxStashIndex = bean.addStashItem(loggedInInfo, rx);
			bean.setStashIndex(rxStashIndex);

			auditStr.append(rx.getAuditString() + "\n");
			bean.addAttributeName(rx.getAtcCode() + "-" + String.valueOf(bean.getStashIndex()));		
			// p("brandName saved in stash", rx.getBrandName());
			// p("stashIndex becomes", "" + beanRX.getStashIndex());
			
			// RxUtil.printStashContent(beanRX);
		} catch (Exception e) {
			MiscUtils.getLogger().error("Error", e);
		}
		MiscUtils.getLogger().debug("================end saveReRxDrugIdToStash of RxRePrescribeAction.java=================");
		return null;
	}

	public ActionForward represcribe2(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException {
		MiscUtils.getLogger().debug("================in represcribe2 of RxRePrescribeAction.java=================");
		LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);
		checkPrivilege(loggedInInfo, PRIVILEGE_WRITE);
		
		oscar.oscarRx.pageUtil.RxSessionBean beanRX = (oscar.oscarRx.pageUtil.RxSessionBean) request.getSession().getAttribute("RxSessionBean");
		if (beanRX == null) {
			response.sendRedirect("error.html");
			return null;
		}
		request.setAttribute("action", "represcribe");
		StringBuilder auditStr = new StringBuilder();

		RxPrescriptionData rxData = new RxPrescriptionData();

		// String strId = (request.getParameter("drugId").split("_"))[1];
		String strId = request.getParameter("drugId");
		// p("!!!!!!!!!s", strId);
		try {

			int drugId = Integer.parseInt(strId);
			// get original drug
			RxPrescriptionData.Prescription oldRx = rxData.getPrescription(drugId);
			// create copy of Prescription
			RxPrescriptionData.Prescription rx = rxData.newPrescription(beanRX.getProviderNo(), beanRX.getDemographicNo(), oldRx); // set writtendate, rxdate ,enddate=null.
			Long rand = Math.round(Math.random() * 1000000);
			rx.setRandomId(rand);

			request.setAttribute("BoxNoFillFirstLoad", "true");
			String qText = rx.getQuantity();
			MiscUtils.getLogger().debug("qText in represcribe2=" + qText);
			if (qText != null && RxUtil.isStringToNumber(qText)) {
			} else {
				rx.setQuantity(RxUtil.getQuantityFromQuantityText(qText));
				rx.setUnitName(RxUtil.getUnitNameFromQuantityText(qText));
			}
			MiscUtils.getLogger().debug("quantity, unitName represcribe2=" + rx.getQuantity() + "; " + rx.getUnitName());
			// trim Special
			String spec = RxUtil.trimSpecial(rx);
			rx.setSpecial(spec);

			List<RxPrescriptionData.Prescription> listReRx = new ArrayList<Prescription>();
			rx.setDiscontinuedLatest(RxUtil.checkDiscontinuedBefore(rx));
			// add rx to rx list
			if (RxUtil.isRxUniqueInStash(beanRX, rx)) {
				listReRx.add(rx);
			}
			// save rx to stash
			int rxStashIndex = beanRX.addStashItem(loggedInInfo, rx);
			beanRX.setStashIndex(rxStashIndex);

			auditStr.append(rx.getAuditString() + "\n");
			beanRX.addAttributeName(rx.getAtcCode() + "-" + String.valueOf(beanRX.getStashIndex()));	
			// p("brandName saved in stash", rx.getBrandName());
			// p("stashIndex becomes", "" + beanRX.getStashIndex());

			// RxUtil.printStashContent(beanRX);
			request.setAttribute("listRxDrugs", listReRx);
		} catch (Exception e) {
			MiscUtils.getLogger().error("Error", e);
		}

		return (mapping.findForward("represcribe"));
	}

	public ActionForward repcbAllLongTerm(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException {
		LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);
		checkPrivilege(loggedInInfo, PRIVILEGE_WRITE);
		
		oscar.oscarRx.pageUtil.RxSessionBean beanRX = (oscar.oscarRx.pageUtil.RxSessionBean) request.getSession().getAttribute("RxSessionBean");
		if (beanRX == null) {
			response.sendRedirect("error.html");
			return null;
		}
		request.setAttribute("action", "represcribe");
		StringBuilder auditStr = new StringBuilder();
		// String idList = request.getParameter("drugIdList");

		String demoNo = request.getParameter("demoNo");
		String strShow = request.getParameter("showall");
		// p("demoNo",demoNo);
		// p("showall",strShow);

		boolean showall = false;
		if (strShow.equalsIgnoreCase("true")) {
			showall = true;
		}
		// p("here");
		// get a list of long term meds
		DrugDao drugDao = (DrugDao) SpringUtils.getBean("drugDao");
		List<Drug> prescriptDrugs = drugDao.getPrescriptions(demoNo, showall);
		List<Integer> listLongTermMed = new ArrayList<Integer>();
		// p("size of prescriptDrugs",""+prescriptDrugs.size());
		for (Drug prescriptDrug : prescriptDrugs) {
			// p("id of drug returned",""+prescriptDrug.getId());
			// add all long term med drugIds to an array.
			if (prescriptDrug.isLongTerm()) {

				listLongTermMed.add(prescriptDrug.getId());
			}
		}


        oscar.oscarRx.pageUtil.RxSessionBean bean = (oscar.oscarRx.pageUtil.RxSessionBean) request.getSession().getAttribute("RxSessionBean");
        
        List<String> reRxDrugIdList=bean.getReRxDrugIdList();
        
		List<RxPrescriptionData.Prescription> listLongTerm = new ArrayList<Prescription>();
		for (int i = 0; i < listLongTermMed.size(); i++) {
			Long rand = Math.round(Math.random() * 1000000);

			// loop this
			int drugId = listLongTermMed.get(i);
			
            //add drug to re-prescribe drug list
            reRxDrugIdList.add(Integer.toString(drugId));
			
			// get original drug
			RxPrescriptionData rxData = new RxPrescriptionData();
			RxPrescriptionData.Prescription oldRx = rxData.getPrescription(drugId);

			// create copy of Prescription
			RxPrescriptionData.Prescription rx = rxData.newPrescription(beanRX.getProviderNo(), beanRX.getDemographicNo(), oldRx);

			request.setAttribute("BoxNoFillFirstLoad", "true");

			// give rx a random id.
			rx.setRandomId(rand);
			String qText = rx.getQuantity();
			MiscUtils.getLogger().debug("qText in represcribe2=" + qText);
			if (qText != null && RxUtil.isStringToNumber(qText)) {
			} else {
				rx.setQuantity(RxUtil.getQuantityFromQuantityText(qText));
				rx.setUnitName(RxUtil.getUnitNameFromQuantityText(qText));
			}
			MiscUtils.getLogger().debug("quantity, unitName represcribe2=" + rx.getQuantity() + "; " + rx.getUnitName());
			String spec = RxUtil.trimSpecial(rx);
			rx.setSpecial(spec);

			if (RxUtil.isRxUniqueInStash(beanRX, rx)) {
				listLongTerm.add(rx);
			}
			int rxStashIndex = beanRX.addStashItem(loggedInInfo, rx);
			beanRX.setStashIndex(rxStashIndex);
			auditStr.append(rx.getAuditString() + "\n");

			// allocate space for annotation
			beanRX.addAttributeName(rx.getAtcCode() + "-" + String.valueOf(beanRX.getStashIndex()));
		}
		// RxUtil.printStashContent(beanRX);
		request.setAttribute("listRxDrugs", listLongTerm);

		return (mapping.findForward("repcbLongTerm"));
	}

	public ActionForward represcribeMultiple(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException {
		MiscUtils.getLogger().debug("================in represcribeMultiple of RxRePrescribeAction.java=================");
		LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);
		checkPrivilege(loggedInInfo, PRIVILEGE_WRITE);
		
		oscar.oscarRx.pageUtil.RxSessionBean bean = (oscar.oscarRx.pageUtil.RxSessionBean) request.getSession().getAttribute("RxSessionBean");
		if (bean == null) {
			response.sendRedirect("error.html");
			return null;
		}
		List<String> reRxDrugList = new ArrayList<String>();
		request.setAttribute("action", "represcribe");
		reRxDrugList = bean.getReRxDrugIdList();
		MiscUtils.getLogger().debug(reRxDrugList);
		List<RxPrescriptionData.Prescription> listReRxDrug = new ArrayList<Prescription>();
		for (String drugId : reRxDrugList) {
			Long rand = Math.round(Math.random() * 1000000);
			RxPrescriptionData rxData = new RxPrescriptionData();
			RxPrescriptionData.Prescription oldRx = rxData.getPrescription(Integer.parseInt(drugId));
			RxPrescriptionData.Prescription rx = rxData.newPrescription(bean.getProviderNo(), bean.getDemographicNo(), oldRx);
			rx.setRandomId(rand);
			String qText = rx.getQuantity();
			MiscUtils.getLogger().debug("qText in represcribe2=" + qText);
			if (qText != null && RxUtil.isStringToNumber(qText)) {
			} else {
				rx.setQuantity(RxUtil.getQuantityFromQuantityText(qText));
				rx.setUnitName(RxUtil.getUnitNameFromQuantityText(qText));
			}
			MiscUtils.getLogger().debug("quantity, unitName represcribe2=" + rx.getQuantity() + "; " + rx.getUnitName());
			String spec = RxUtil.trimSpecial(rx);
			rx.setSpecial(spec);
			if (RxUtil.isRxUniqueInStash(bean, rx)) {
				listReRxDrug.add(rx);
			}
			int rxStashIndex = bean.addStashItem(loggedInInfo, rx);
			bean.setStashIndex(rxStashIndex);
			bean.addAttributeName(rx.getAtcCode() + "-" + String.valueOf(bean.getStashIndex()));
		}
		MiscUtils.getLogger().debug(listReRxDrug);
		request.setAttribute("listRxDrugs", listReRxDrug);
		MiscUtils.getLogger().debug("================END represcribeMultiple of RxRePrescribeAction.java=================");
		return (mapping.findForward("represcribe"));
	}

	public void p(String s) {
		MiscUtils.getLogger().debug(s);
	}

	public void p(String s, String s1) {
		MiscUtils.getLogger().debug(s + "=" + s1);
	}

	
	private void checkPrivilege(LoggedInInfo loggedInInfo, String privilege) {
		if (!securityInfoManager.hasPrivilege(loggedInInfo, "_rx", privilege, null)) {
			throw new RuntimeException("missing required security object (_rx)");
		}
	}
}
