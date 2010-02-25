package oscar.oscarBilling.ca.on.pageUtil;

import java.math.BigDecimal;
import java.util.List;
import java.util.Properties;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import oscar.appt.ApptUtil;
import oscar.oscarBilling.ca.on.data.BillingClaimHeader1Data;
import oscar.oscarBilling.ca.on.data.BillingDataHlp;
import oscar.oscarBilling.ca.on.data.BillingItemData;
import oscar.oscarBilling.ca.on.data.BillingProviderData;
import oscar.oscarBilling.ca.on.data.JdbcBilling3rdPartImpl;
import oscar.oscarBilling.ca.on.data.JdbcBillingClaimImpl;
import oscar.oscarBilling.ca.on.data.JdbcBillingCodeImpl;
import oscar.oscarBilling.ca.on.data.JdbcBillingCorrection;
import oscar.oscarBilling.ca.on.data.JdbcBillingPageUtil;
import oscar.oscarBilling.ca.on.data.JdbcBillingReviewImpl;
import oscar.util.StringUtils;

public class BillingCorrectionPrep {
	private static final Logger _logger = Logger
			.getLogger(BillingCorrectionPrep.class);

	JdbcBillingCorrection dbObj = new JdbcBillingCorrection();

	// 
	public List getBillingRecordObj(String id) {
		List ret = dbObj.getBillingRecordObj(id);
		return ret;
	}

	// get error code
	public List getBillingExplanatoryList(String id) {
		List ret = dbObj.getBillingExplanatoryList(id);
		return ret;
	}

	// get rejected code
	public List getBillingRejectList(String id) {
		List ret = dbObj.getBillingRejectList(id);
		return ret;
	}

	// compare the obj first, push old record to repo if needed.
	public boolean updateBillingClaimHeader(BillingClaimHeader1Data ch1Obj,
			HttpServletRequest requestData) {
		boolean ret = true;
		if (isChangedBillingClaimHeader(ch1Obj, requestData)) {
			int i = dbObj.addRepoClaimHeader(ch1Obj);
			_logger.info("updateBillingClaimHeader(old value = " + i
					+ ch1Obj.getStatus() + "|" + ch1Obj.getRef_num() + "|"
					+ ch1Obj.getAdmission_date() + "|"
					+ ch1Obj.getFacilty_num() + "|" + ch1Obj.getMan_review()
					+ "|" + ch1Obj.getBilling_date() + "|"
					+ ch1Obj.getProviderNo() + "|" + ch1Obj.getCreator());

			ch1Obj
					.setStatus(requestData.getParameter("status").substring(0,
							1));
			
			ch1Obj.setPay_program(requestData.getParameter("payProgram"));
			if(requestData.getParameter("status").substring(0,1).equals("N")){
				ch1Obj.setPay_program("NOT");				
			}
						
			ch1Obj.setRef_num(requestData.getParameter("rdohip"));
			ch1Obj.setVisittype(requestData.getParameter("visittype"));

			ch1Obj.setAdmission_date(requestData.getParameter("xml_vdate"));
			ch1Obj.setFacilty_num(requestData.getParameter("clinic_ref_code"));
			ch1Obj
					.setMan_review(requestData.getParameter("m_review") == null ? ""
							: "Y");
			ch1Obj.setBilling_date(requestData
					.getParameter("xml_appointment_date"));
			ch1Obj.setProviderNo(requestData.getParameter("provider_no"));
			ch1Obj.setComment(requestData.getParameter("comment"));

			// provider_ohip_no update as well
			BillingProviderData otemp = (new JdbcBillingPageUtil())
					.getProviderObj(requestData.getParameter("provider_no"));
			ch1Obj.setProvider_ohip_no(otemp.getOhipNo());
			ch1Obj.setProvider_rma_no(otemp.getRmaNo());
			ch1Obj.setCreator((String) requestData.getSession().getAttribute(
					"user"));
			
			ch1Obj.setClinic((String)requestData.getParameter("site"));
                        
                        ch1Obj.setProvince((String)requestData.getParameter("hc_type"));

			ret = dbObj.updateBillingClaimHeader(ch1Obj);
		}

		// 3rd party elements
		if (requestData.getParameter("payment") != null) {
			ret = update3rdPartyItem("payment", requestData);
                        ret = update3rdPartyItem("refund", requestData);
		}

		if (requestData.getParameter("billto") != null) {
			ret = update3rdPartyItem("billTo", requestData);
		}

		return ret;
	}

        public boolean update3rdPartyItem(String key, HttpServletRequest request) {
		boolean ret = true;
		JdbcBilling3rdPartImpl tobj = new JdbcBilling3rdPartImpl();
		String billingNo = request.getParameter("xml_billing_no");
		tobj.updateKeyValue(billingNo, key, request
				.getParameter(key));
		return ret;
	}

	public boolean update3rdPartyItem(HttpServletRequest request) {
		boolean ret = true;
		JdbcBilling3rdPartImpl tobj = new JdbcBilling3rdPartImpl();
		String billingNo = request.getParameter("xml_billing_no");
		tobj.updateKeyValue(billingNo, "payment", request
				.getParameter("payment"));
		tobj
				.updateKeyValue(billingNo, "refund", request
						.getParameter("refund"));
		return ret;
	}

	public boolean updateBillingItem(List lItemObj, HttpServletRequest request) {
		boolean ret = true; // dbObj.updateBillingClaimHeader(ch1Obj);
		// _logger.info("updateBillingItem(old value = ");
		Vector vecName = new Vector();
		Vector vecUnit = new Vector();
		Vector vecFee = new Vector();
		Vector vecStatus = new Vector();
		String dx = request.getParameter("xml_diagnostic_detail");
		dx = dx.length() > 2 ? dx.substring(0, 3) : dx;
		String serviceDate = request.getParameter("xml_appointment_date");

		for (int i = 0; i < BillingDataHlp.FIELD_MAX_SERVICE_NUM; i++) {
			if (request.getParameter("servicecode" + i) != null
					&& request.getParameter("servicecode" + i).length() > 0) { // == 5
				vecName.add(request.getParameter("servicecode" + i));
				vecUnit.add(request.getParameter("billingunit" + i));
				vecFee.add(request.getParameter("billingamount" + i));
				vecStatus
						.add(request.getParameter("itemStatus" + i) == null ? "O"
								: "S");
			}
		}
                
                //System.out.println("ItemObj size " + lItemObj.size() + " Vector Size " + vecName.size());

		// update item first
		String claimId = "0";
		for (int i = 0; i < lItemObj.size(); i++) {
			BillingItemData iObj = (BillingItemData) lItemObj.get(i);
			ret = changeItem(iObj, dx, serviceDate, vecName, vecUnit, vecFee,
					vecStatus);
			claimId = iObj.getCh1_id();
			_logger.info(iObj.getService_code() + " lItemObj = " + ret);
		}

		// add item if possible
		for (int i = 0; i < vecName.size(); i++) {
			String sName = (String) vecName.get(i);
			String sUnit = (String) vecUnit.get(i);
			String sFee = (String) vecFee.get(i);
			String sStatus = (String) vecStatus.get(i);
			ret = addItem(lItemObj, dx, serviceDate, sName, sUnit, sFee,
					sStatus);
			_logger.info(sName + " lItemObj(value = " + ret);
		}

		// recalculate amount
		String newAmount = sumFee(vecFee);
		_logger.info(" lItemObj(newAmount = " + newAmount);
		updateAmount(newAmount, claimId);
		// update paid if needed
		if (request.getParameter("status").substring(0, 1).equals("S")
				&& request.getParameter("payProgram").matches(
						BillingDataHlp.BILLINGMATCHSTRING_3RDPARTY)) {
			updatePaid(newAmount, claimId);
		}

		return ret;
	}

	// billing correction
	public String getBillingCodeDesc(String codeName) {
		String ret = null;
		JdbcBillingCodeImpl dbCodeObj = new JdbcBillingCodeImpl();
		List descL = dbCodeObj.getBillingCodeAttr(codeName);
		ret = descL.size() > 1 ? (String) descL.get(1) : "Unknown";
		return ret;
	}

	// billing correction
	public Properties getBillingCodeDesc(List codeName) {
		JdbcBillingCodeImpl dbCodeObj = new JdbcBillingCodeImpl();
		Properties ret = new Properties();
		for (int i = 0; i < codeName.size(); i++) {
			List descL = dbCodeObj.getBillingCodeAttr((String) codeName.get(i));
			String desc = descL.size() > 1 ? (String) descL.get(1) : "Unknown";
			ret.setProperty((String) codeName.get(i), desc);
		}
		return ret;
	}

	// from billing correction
	public List getBillingClaimHeaderObj(String ch1Id) {
		List recordObj = null;
		recordObj = getBillingRecordObj(ch1Id);
		return recordObj;
	}

	// sql - set key=value, key1=value1, ...
	private boolean isChangedBillingClaimHeader(
			BillingClaimHeader1Data existObj, HttpServletRequest request) {
		boolean ret = false;
		// _logger.info("isChangedBillingClaimHeader(old value = " +
		// existObj.getStatus() + "|" + existObj.getRef_num()
		// + "|" + existObj.getAdmission_date() + "|" +
		// existObj.getFacilty_num() + "|" + existObj.getMan_review()
		// + "|" + existObj.getBilling_date() + "|" + existObj.getProviderNo()
		// + "|" + existObj.getPay_program());
		String temp = request.getParameter("m_review") == null ? "" : "Y";
		if (!existObj.getStatus().equals(
				request.getParameter("status").substring(0, 1)))
			System.out.println("status");
		if (!existObj.getPay_program().equals(
				request.getParameter("payProgram")))
			System.out.println("payProgram");
		if (!existObj.getRef_num().equals(request.getParameter("rdohip")))
			System.out.println("rdohip");
		if (!existObj.getVisittype().equals(request.getParameter("visittype")))
			System.out.println("visittype");
		if (!existObj.getAdmission_date().equals(
				request.getParameter("xml_vdate")))
			System.out.println("xml_vdate");

		if (!existObj.getFacilty_num().equals(
				request.getParameter("clinic_ref_code")))
			System.out.println("facNum:" + existObj.getFacilty_num());
		if (!existObj.getMan_review().equals(temp))
			System.out.println("|" + existObj.getMan_review() + ":temp:" + temp
					+ "|");
		if (!existObj.getBilling_date().equals(
				request.getParameter("xml_appointment_date")))
			System.out.println("Billing_date");
		if (!existObj.getProviderNo().equals(
				request.getParameter("provider_no")))
			System.out.println("getProvider_no");

		if (!existObj.getStatus().equals(
				request.getParameter("status").substring(0, 1))
				|| !existObj.getPay_program().equals(
						request.getParameter("payProgram"))
				|| !existObj.getRef_num()
						.equals(request.getParameter("rdohip"))
				|| !existObj.getVisittype().equals(
						request.getParameter("visittype"))
				|| !existObj.getAdmission_date().equals(
						request.getParameter("xml_vdate"))
				|| !existObj.getFacilty_num().equals(
						request.getParameter("clinic_ref_code"))
				|| !existObj.getMan_review().equals(temp)
				|| !existObj.getBilling_date().equals(
						request.getParameter("xml_appointment_date"))
				|| !existObj.getComment().equals(
						request.getParameter("comment"))
				|| !existObj.getProviderNo().equals(
						request.getParameter("provider_no"))
				|| !StringUtils.nullSafeEquals(existObj.getClinic(), request.getParameter("site"))
                                || !existObj.getProvince().equals(
						request.getParameter("hc_type"))) {
			ret = true;
		}
		return ret;
	}

	// status...
	private boolean changeItem(BillingItemData oldObj, String sDx,
			String serviceDate, Vector vecName, Vector vecUnit, Vector vecFee,
			Vector vecStatus) {
		boolean ret = true;
		//System.out.println("status:" + oldObj.getService_code() + ":" +
		//vecName.get(0) + ":");
		if (vecName.contains(oldObj.getService_code())) {
			ret = true;
			int i = vecName.indexOf(oldObj.getService_code());

			// change to settle or not
			boolean bStatusChange = false;
			String cStatus = (String) vecStatus.get(i);
			// System.out.println("status: " + cStatus);
			if ((!oldObj.getStatus().equals("S") && cStatus.equals("S"))
					|| (oldObj.getStatus().equals("S") && !cStatus.equals("S"))) {
				bStatusChange = true;
			}

			if (!oldObj.getSer_num().equals((String) vecUnit.get(i))
					|| !oldObj.getFee().equals((String) vecFee.get(i))
					|| !oldObj.getDx().equals(sDx)
					|| !oldObj.getService_date().equals(serviceDate)
					|| bStatusChange) {
				int j = dbObj.addRepoOneItem(oldObj);
				if (j == 0)
					return false;
				oldObj.setSer_num(getUnit((String) vecUnit.get(i)));
				oldObj.setFee(getFee((String) vecFee.get(i),
						getUnit((String) vecUnit.get(i)), (String) vecName
								.get(i), serviceDate));
				oldObj.setService_date(serviceDate);
				oldObj.setDx(sDx);
				oldObj.setStatus(cStatus);
                                //System.out.println("Updating " + oldObj.getService_code());
				ret = dbObj.updateBillingOneItem(oldObj);
				if (!ret)
					return ret;
			}
		} else {
			// delete the old item
                        //System.out.println("Deleting old item " + oldObj.getService_code());
			oldObj.setStatus("D");
			ret = dbObj.updateBillingOneItem(oldObj);
		}

		return ret;
	}

	private boolean addItem(List lItemObj, String sDx, String serviceDate,
			String sName, String sUnit, String sFee, String sStatus) {
		boolean ret = true;
		BillingItemData oldObj = null;
                BillingItemData newObj = null;
		for (int i = 0; i < lItemObj.size(); i++) {
			oldObj = (BillingItemData) lItemObj.get(i);
                        //System.out.println("comparing oldobj to new " + oldObj.getService_code() + " : " + sName);
			if (sName.equals((String) oldObj.getService_code())) {
                                //System.out.println(oldObj.getService_code() + " already present skipping");
				ret = false;
				break;
			}
		}
		if (ret) {
                        newObj = new BillingItemData(oldObj);
			newObj.setService_code(sName);
			newObj.setSer_num(getUnit(sUnit));
			newObj.setFee(getFee(sFee, getUnit(sUnit), sName, serviceDate));
			newObj.setService_date(serviceDate);
			newObj.setDx(sDx);
			newObj.setStatus(sStatus);
			JdbcBillingClaimImpl myObj = new JdbcBillingClaimImpl();
                        //System.out.println("Adding item " + newObj.getService_code());
			int i = myObj.addOneItemRecord(newObj);
			if (i == 0)
				return false;
                        lItemObj.add(newObj);
		}
		ret = true;
		return ret;
	}

	// for appt unbill; 0 - id, 1 - status
	public List getBillingNoStatusByAppt(String apptNo) {
		List ret = dbObj.getBillingCH1NoStatusByAppt(apptNo);
		return ret;
	}

	// for appt unbill;
	public boolean deleteBilling(String id, String status, String providerNo) {
		boolean ret = dbObj.updateBillingStatus(id, status, providerNo);
		return ret;
	}

	public List getFacilty_num() {
		JdbcBillingPageUtil dbPageObj = new JdbcBillingPageUtil();
		List ret = dbPageObj.getFacilty_num();
		return ret;
	}

	private String sumFee(Vector vecFee) {
		String ret = "";
		BigDecimal fee = new BigDecimal(Double.parseDouble("0.00")).setScale(4,
				BigDecimal.ROUND_HALF_UP);
		for (int i = 0; i < vecFee.size(); i++) {
			String temp = (String) vecFee.get(i);
			if (temp.indexOf(".") < 0) {
				temp = temp + ".00";
			}
			BigDecimal tFee = new BigDecimal(Double.parseDouble(temp))
					.setScale(4, BigDecimal.ROUND_HALF_UP);
			fee = fee.add(tFee);
		}
		ret = fee.setScale(2, BigDecimal.ROUND_HALF_UP).toString();
		return ret;
	}

	private void updateAmount(String newAmount, String claimId) {
		String oldTotal = dbObj.getBillingTotal(claimId);
		if (!newAmount.equals(oldTotal)) {
			dbObj.updateBillingTotal(newAmount, claimId);
		}
	}

	private void updatePaid(String newAmount, String claimId) {
		String oldTotal = dbObj.getBillingPaid(claimId);
		if (!newAmount.equals(oldTotal)) {
			dbObj.updateBillingPaid(newAmount, claimId);
		}
	}

	private String getUnit(String unit) {
		String ret = unit;
		if (!ret.matches("\\d+")) {
			ret = "1";
		}
		return ret;
	}

	private String getFee(String fee, String unit, String codeName, String billReferenceDate) {
		String ret = fee;
		if (fee.length() == 0 || fee.equals(" ")) {
			JdbcBillingReviewImpl dbObj = new JdbcBillingReviewImpl();
			fee = dbObj.getCodeFee(codeName,billReferenceDate);
			// calculate fee
			BigDecimal bigCodeFee = new BigDecimal(fee);
			// System.out.println((String) vecUnit.get(i) + "big bigCodeFee: " +
			// bigCodeFee.toString());
			BigDecimal bigCodeUnit = new BigDecimal(unit);
			BigDecimal bigFee = bigCodeFee.multiply(bigCodeUnit);
			bigFee = bigFee.setScale(2, BigDecimal.ROUND_HALF_UP);
			// bigFee = bigFee.round(new MathContext(2));
			ret = bigFee.toString();
		}
		return ret;
	}

}
