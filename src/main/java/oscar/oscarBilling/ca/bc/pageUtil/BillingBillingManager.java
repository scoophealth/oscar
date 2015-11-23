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
package oscar.oscarBilling.ca.bc.pageUtil;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.oscarehr.common.dao.BillingDao;
import org.oscarehr.common.dao.BillingServiceDao;
import org.oscarehr.common.model.Billing;
import org.oscarehr.common.model.BillingService;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.entities.Billingmaster;
import oscar.entities.WCB;
import oscar.oscarBilling.ca.bc.data.BillingmasterDAO;
import oscar.util.ConversionUtils;

public class BillingBillingManager {
	private String billTtype;

	public BillingItem[] getBillingItem(String[] service, String service1, String service2, String service3, String service1unit, String service2unit, String service3unit) {
		BillingItem[] arr = {};

		String service_code;
		double units;
		ArrayList<BillingItem> lst = new ArrayList<BillingItem>();
		BillingItem billingitem;

		lst = getDups2(service, service1, service2, service3, service1unit, service2unit, service3unit);

		BillingServiceDao dao = SpringUtils.getBean(BillingServiceDao.class);
		for (int i = 0; i < lst.size(); i++) {
			BillingItem bi = lst.get(i);
			service_code = bi.service_code;
			units = bi.units;

			for (BillingService bs : dao.findByServiceCode(service_code)) {
				billingitem = new BillingItem(bs.getServiceCode(), bs.getDescription(), bs.getValue(), bs.getPercentage(), units);
				lst.add(billingitem);
			}
		}

		arr = lst.toArray(arr);

		return arr;
	}

	public ArrayList<BillingItem> getBillView(String billing_no) {
		ArrayList<BillingItem> billingItemsArray = new ArrayList<BillingItem>();

		BillingDao dao = SpringUtils.getBean(BillingDao.class);

		///is msp or wcb?
		String billingType = "";
		Billing billing = dao.find(ConversionUtils.fromIntString(billing_no));
		if (billing != null) {
			billingType = billing.getBillingtype();
		}

		BillingmasterDAO masterDao = SpringUtils.getBean(BillingmasterDAO.class);
		List<Billingmaster> masters = masterDao.getBillingmasterByBillingNo(ConversionUtils.fromIntString(billing_no));

		String billingCode = "";
		String billingUnit = "";

		for (Billingmaster m : masters) {

			if (billingType.equals("WCB")) {
				WCB wcb = masterDao.getWcbByBillingNo(ConversionUtils.fromIntString(billing_no));

				if (wcb != null) {
					billingCode = wcb.getW_feeitem();
					billingUnit = "1";
				}
			} else {
				billingCode = m.getBillingCode();
				billingUnit = m.getBillingUnit();
			}
			BillingItem billingItem = new BillingItem(billingCode, billingUnit);
			billingItem.fill(this.billTtype);
			billingItem.price = m.getBillAmountAsDouble();
			billingItem.setLineNo(m.getBillingmasterNo());

			billingItemsArray.add(billingItem);
		}

		return billingItemsArray;
	}

	public ArrayList<BillingItem> getDups(String[] service, String service1, String service2, String service3, String service1unit, String service2unit, String service3unit) {
		ArrayList<String> lst = new ArrayList<String>();
		for (int i = 0; i < service.length; i++) {
			lst.add(service[i]);
		}
		if (service1.compareTo("") != 0) {
			if (service1unit.compareTo("") == 0) {
				service1unit = "1";
			}
			for (int j = 0; j < Integer.parseInt(service1unit); j++) {
				lst.add(service1);
			}
		}
		if (service2.compareTo("") != 0) {
			if (service2unit.compareTo("") == 0) {
				service2unit = "1";
			}
			for (int k = 0; k < Integer.parseInt(service2unit); k++) {
				lst.add(service2);
			}
		}
		if (service3.compareTo("") != 0) {
			if (service3unit.compareTo("") == 0) {
				service3unit = "1";
			}
			for (int l = 0; l < Integer.parseInt(service3unit); l++) {
				lst.add(service3);
			}
		}

		ArrayList<BillingItem> billingItemsArray = new ArrayList<BillingItem>();
		for (int i = 0; i < lst.size(); i++) {
			String code = lst.get(i);
			BillingItem b = isBilled(billingItemsArray, code);
			if (b == null) {
				BillingItem billingItem = new BillingItem(code, "1");
				billingItem.fill(this.billTtype);
				billingItemsArray.add(billingItem);
			} else {
				b.addUnits(1);
			}
		}

		return billingItemsArray;

	}

	private BillingItem isBilled(ArrayList<BillingItem> ar, String code) {
		for (int i = 0; i < ar.size(); i++) {
			BillingItem bi = ar.get(i);
			if (bi.service_code.equals(code)) {
				return bi;
			}
		}
		return null;
	}

	public ArrayList<BillingItem> getDups2(String[] service, String service1, String service2, String service3, String service1unit, String service2unit, String service3unit) {
		ArrayList<BillingItem> billingItemsArray = new ArrayList<BillingItem>();
		for (int i = 0; i < service.length; i++) {
			addBillItem(billingItemsArray, service[i], "1");
		}
		if (service1.compareTo("") != 0) {
			if (service1unit.compareTo("") == 0) {
				service1unit = "1";
			}
			//int numUnit = Integer.parseInt(service1unit);
			addBillItem(billingItemsArray, service1, service1unit);

		}
		if (service2.compareTo("") != 0) {
			if (service2unit.compareTo("") == 0) {
				service2unit = "1";
			}
			//int numUnit = Integer.parseInt(service2unit);
			addBillItem(billingItemsArray, service2, service2unit);

		}
		if (service3.compareTo("") != 0) {
			if (service3unit.compareTo("") == 0) {
				service3unit = "1";
			}
			//int numUnit = Integer.parseInt(service3unit);
			addBillItem(billingItemsArray, service3, service3unit);
		}

		return billingItemsArray;

	}

	private BillingItem addBillItem(ArrayList<BillingItem> ar, String code, String serviceUnits) {
		boolean newCode = true;
		BillingItem bi = null;
		for (int i = 0; i < ar.size(); i++) {
			bi = ar.get(i);
			if (bi.service_code.equals(code)) {
				newCode = false;
				bi.addUnits(serviceUnits);
				i = ar.size();
			}
		}
		if (newCode) {
			bi = new BillingItem(code, serviceUnits);
			bi.fill(this.billTtype);
			ar.add(bi);
		}
		return bi;
	}

	public String getGrandTotal(ArrayList<BillingItem> ar) {
		double grandtotal = 0.00;
		for (int i = 0; i < ar.size(); i++) {
			BillingItem bi = ar.get(i);
			grandtotal += bi.getLineTotal();
			MiscUtils.getLogger().debug("total:" + grandtotal);
		}
		BigDecimal bdFee = BigDecimal.valueOf(grandtotal).setScale(2, BigDecimal.ROUND_HALF_UP);
		return bdFee.toString();

	}

	public class BillingItem {

		String service_code;
		String description;
		double price;
		double percentage;
		double units;
		double lineTotal;
		int lineNo;

		public BillingItem(String service_code, String description, String price1, String percentage1, double units1) {
			this.service_code = service_code;
			this.description = description;
			this.price = Double.parseDouble(price1);
			this.percentage = Double.parseDouble(percentage1);
			this.units = units1;
		}

		public BillingItem(String service_code, String units1) {
			this.service_code = service_code;
			this.units = Double.parseDouble(units1);
		}

		public BillingItem(String service_code, int units1) {
			this.service_code = service_code;
			this.units = units1;
		}

		public void addUnits(int num) {
			units += num;
		}

		public void addUnits(String num) {
			units += (num.compareTo("") != 0) ? Double.parseDouble(num) : 0;
		}

		public String getServiceCode() {
			return service_code;
		}

		public String getDescription() {
			return description;
		}

		public double getUnit() {
			return units;
		}

		public String getDispPrice() {
			BigDecimal bdFee = BigDecimal.valueOf(price).setScale(2,
			//BigDecimal.ROUND_HALF_UP);
			        RoundingMode.HALF_UP);
			MiscUtils.getLogger().debug("price" + price + " fee" + bdFee.toString());
			return bdFee.toString();
		}

		public double getPrice() {
			return price;
		}

		public double getPercentage() {
			return percentage;
		}

		public void setLineNo(int lineNo) {
			this.lineNo = lineNo;
		}

		public int getLineNo() {
			return this.lineNo;
		}

		public void fill(String billType) {
			BillingServiceDao dao = SpringUtils.getBean(BillingServiceDao.class);
			List<BillingService> bss = null;
			//make sure to load private fee if required,but defaqult to MSP fee if Private fee unavailable
			if ("pri".equalsIgnoreCase(billType)) {
				bss = dao.findByServiceCodes(Arrays.asList(new String[] { service_code, "A" + service_code }));
			} else {
				bss = dao.findByServiceCode(service_code);
			}

			for (BillingService bs : bss) {
				this.description = bs.getDescription();
				this.price = Double.parseDouble(bs.getValue());

				try {
					String percRes = bs.getPercentage();
					if (percRes != null && !"".equals(percRes)) {
						this.percentage = Double.parseDouble(percRes);
					} else {
						this.percentage = 100.00;
					}
				} catch (NumberFormatException eNum) {
					this.percentage = 100;
				}
			}
		}

		public double getLineTotal() {

			this.lineTotal = price * units;
			return lineTotal;
		}

		public String getDispLineTotal() {
			BigDecimal bdFee = new BigDecimal("" + lineTotal).setScale(2, RoundingMode.HALF_UP);
			return bdFee.toString();
		}
	}

	public void setBillTtype(String billTtype) {
		this.billTtype = billTtype;
	}

	public String getBillTtype() {
		return billTtype;
	}
}
