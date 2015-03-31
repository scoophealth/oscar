/**
 * Copyright (c) 2001-2002. Andromedia. All Rights Reserved.
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
 * This software was written for
 * Andromedia, to be provided as
 * part of the OSCAR McMaster
 * EMR System
 */


package oscar.oscarBilling.ca.bc.administration;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.oscarehr.billing.CA.BC.dao.WcbDao;
import org.oscarehr.billing.CA.BC.model.Wcb;
import org.oscarehr.common.dao.BillingDao;
import org.oscarehr.common.dao.BillingServiceDao;
import org.oscarehr.common.model.Billing;
import org.oscarehr.common.model.BillingService;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.managers.DemographicManager;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.oscarBilling.ca.bc.MSP.MSPReconcile;
import oscar.oscarBilling.ca.bc.data.BillingHistoryDAO;
import oscar.oscarBilling.ca.bc.data.BillingmasterDAO;
import oscar.oscarProvider.data.ProviderData;
import oscar.util.ConversionUtils;
import oscar.util.StringUtils;

/*
 * @author Jef King
 * For The Oscar McMaster Project
 * Developed By Andromedia
 * www.andromedia.ca
 */
/*
 * Created on Mar 10, 2004
 */

public class TeleplanCorrectionActionWCB extends org.apache.struts.action.Action {
	
	static Logger log=MiscUtils.getLogger();
	
	private BillingDao billingDao = SpringUtils.getBean(BillingDao.class);
	private WcbDao wcbDao = SpringUtils.getBean(WcbDao.class);
	private DemographicManager demographicManager = SpringUtils.getBean(DemographicManager.class);

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response)
            throws IOException, ServletException {

        String where = "success";

        TeleplanCorrectionFormWCB data = (TeleplanCorrectionFormWCB) form;

        try {

            MSPReconcile msp = new MSPReconcile();
            String status = data.getStatus();

            log.debug("adj amount " + data.getAdjAmount());
            if (request.getParameter("settle") != null && request.getParameter("settle").equals("Settle Bill")) {
                status = "S";
            }

            if (!StringUtils.isNullOrEmpty(status)) {
                status = MSPReconcile.NOTSUBMITTED.equals(data.getStatus()) ? MSPReconcile.WCB : status;
                msp.updateBillingStatusWCB(data.getBillingNo(), status, data.getId());
            }
            BillingHistoryDAO dao = new BillingHistoryDAO();
            //If the adjustment amount field isn't empty, create an archive of the adjustment
            if (data.getAdjAmount() != null && !"".equals(data.getAdjAmount())) {
                double dblAdj = Math.abs(new Double(data.getAdjAmount()).doubleValue());
                //if 1 this adjustment is a debit
                if ("1".equals(data.getAdjType())) {
                    dblAdj = dblAdj * -1.0;
                }
                dao.createBillingHistoryArchive(data.getId(), dblAdj, MSPReconcile.PAYTYPE_IA);
                msp.settleIfBalanced(data.getId());
            } else {
                /**
                 * Ensure that an audit of the currently modified bill is captured
                 */
                dao.createBillingHistoryArchive(data.getId());
            }
            updateUnitValue(data.getBillingUnit(), data.getBillingNo());

           
            Billing billing = billingDao.find(Integer.parseInt(data.getBillingNo()));
            if(billing != null) {
            	billing.setStatus(data.getStatus());
            	billingDao.merge(billing);
            }
           
            String feeItem = data.getW_feeitem();
            String extraFeeItem = data.getW_extrafeeitem();
            String getItemAmt = this.GetFeeItemAmount(feeItem, extraFeeItem);
            log.debug("fee " + feeItem + " extra " + extraFeeItem + " item amt " + getItemAmt);
          
            Demographic d = demographicManager.getDemographic(LoggedInInfo.getLoggedInInfoFromSession(request), data.getDemographicNumber());
            
            for(Wcb w : wcbDao.findByBillingNo(Integer.parseInt(data.getBillingNo()))) {
            	w.setFormEdited(new Date());
            	w.setStatus("O");
            	w.setReportType(data.getW_reporttype());
            	w.setBillAmount(getItemAmt);
            	w.setfName(d.getFirstName());
            	w.setlName(d.getLastName());
            	w.setmName("");
            	w.setGender(d.getSex());
            	w.setDob(ConversionUtils.fromDateString(d.getYearOfBirth() + "-" + d.getMonthOfBirth() + "-" + d.getDateOfBirth()));
            	w.setAddress(d.getAddress());
            	w.setCity(d.getCity());
            	w.setPostal(d.getPostal());
            	w.setArea(oscar.Misc.areaCode(d.getPhone2()));
            	w.setPhone(oscar.Misc.phoneNumber(d.getPhone2()));
            	w.setPhn(d.getHin()+d.getVer());
            	w.setEmpName(data.getW_empname());
            	w.setEmpArea(data.getW_emparea());
            	w.setEmpPhone(data.getW_empphone());
            	w.setWcbNo(data.getW_wcbno());
            	w.setOpAddress(data.getW_opaddress());
            	w.setOpCity(data.getW_opcity());
            	w.setrPhysician(data.getW_rphysician());
            	w.setDuration(Integer.parseInt(data.getW_duration()));
            	w.setProblem(data.getW_problem());
            	w.setServiceDate(ConversionUtils.fromDateString(data.getW_servicedate()));
            	w.setDiagnosis(data.getW_diagnosis());
            	w.setIcd9(data.getW_icd9());
            	w.setBp(data.getW_bp());
            	w.setSide(data.getW_side());
            	w.setNoi(data.getW_noi());
            	w.setWork(data.getW_work());
            	w.setWorkDate(ConversionUtils.fromDateString(data.getW_workdate()));
            	w.setClinicInfo(data.getW_clinicinfo());
            	w.setCapability(data.getW_capability());
            	w.setCapReason(data.getW_capreason());
            	w.setEstimate(data.getW_estimate());
            	w.setRehab(data.getW_rehab());
            	w.setRehabType(data.getW_rehabtype());
            	w.setWcbAdbvisor(data.getW_wcbadvisor());
            	w.setfTreatment(data.getW_ftreatment());
            	w.setEstimateDate(ConversionUtils.fromDateString(data.getW_estimate()));
            	w.setToFollow(data.getW_tofollow());
            	w.setPracNo(data.getW_pracno());
            	w.setDoi(ConversionUtils.fromDateString(data.getW_doi()));
            	w.setServiceLocation(data.getServiceLocation());
            	w.setFeeItem(data.getW_feeitem());
            	w.setExtraFeeItem(data.getW_extrafeeitem());
            	
            	wcbDao.merge(w);
            }
            
            String providerNo = data.getProviderNo();

            ProviderData pd = new ProviderData(providerNo);
            String payee = pd.getBilling_no();
            String pracno = pd.getOhip_no();
            String billingNo = data.getBillingNo();

            
            for(Wcb wcb : wcbDao.findByBillingNo(Integer.parseInt(billingNo))) {
            	//TODO: This has to be eventually changed to a string
            	wcb.setProviderNo(Integer.parseInt(providerNo));
            	wcb.setPayeeNo(payee);
            	wcb.setPracNo(pracno);
            	wcbDao.merge(wcb);
            }

        } catch (Exception ex) {
            log.error("WCB Teleplan Correction Query Error: " +ex.getMessage() + " - ", ex);
        }

        String newURL = mapping.findForward(where).getPath();
        newURL = newURL + "?billing_no=" + data.getId();
        MiscUtils.getLogger().debug(newURL);

        ActionForward actionForward = new ActionForward();
        actionForward.setPath(newURL);
        actionForward.setRedirect(true);
        return actionForward;
    }
    private void updateUnitValue(String i, String billingno) {
    	BillingmasterDAO dao = (BillingmasterDAO) SpringUtils.getBean("BillingmasterDAO");
    	dao.updateBillingUnitForBillingNumber(i, Integer.parseInt(billingno));
    }

    private String GetFeeItemAmount(String fee1, String fee2) {
        BillingServiceDao dao = SpringUtils.getBean(BillingServiceDao.class);
        List<BillingService> services = dao.findByServiceCode(fee1);
        for(BillingService service : services)
        	return service.getValue(); 
        return "0.00";
    }
}
