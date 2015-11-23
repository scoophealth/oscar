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


package oscar.oscarBilling.ca.on.OHIP;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.oscarehr.common.dao.BillingServiceDao;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.oscarBilling.ca.on.data.BillingCodeData;

/**
 *
 * @author Jay Gallagher
 */
public class ScheduleOfBenefits {

	boolean forceUpdate, addNewCodes, addChangedCodes;
	int newfees, oldfees, total;
	BillingCodeData bc;

	public ScheduleOfBenefits() {
	}


	public List processNewFeeSchedule(InputStream is, boolean addNewCodes , boolean addChangedCodes, boolean forceUpdate, BigDecimal updateAssistantFeesValue, BigDecimal updateAnaesthetistFeesValue){
		ArrayList changes = new ArrayList();
		bc = new BillingCodeData();
		StringBuilder codesThatHaveBothGPSpec = new StringBuilder();
		this.addNewCodes = addNewCodes;
		this.addChangedCodes = addChangedCodes;
		this.forceUpdate = forceUpdate;

		try {
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader in = new BufferedReader(isr);
			String str;
			int total = 0;
			int newfees = 0;
			int oldfees = 0;
			Map change;
			while ((str = in.readLine()) != null) {
				total++;
				Hashtable newPricingInfo = breakLine(str);
				Hashtable<String,String> billingInfo = bc.searchBillingCode((String) newPricingInfo.get("feeCode")+"A");

				BigDecimal gpBD   = getJBD((String) newPricingInfo.get("gpFees"));
				BigDecimal specBD = getJBD((String) newPricingInfo.get("specFee"));
				BigDecimal zeroBD = new BigDecimal("0.00");

				if ( gpBD.compareTo(zeroBD) != 0 && specBD.compareTo(zeroBD) != 0 && gpBD.compareTo(specBD) != 0){
					codesThatHaveBothGPSpec.append(( (String) newPricingInfo.get("feeCode"))+":"+gpBD+" "+specBD+"\n");
				}

				String moreprices = "(gp.:"+getJBD((String) newPricingInfo.get("gpFees"))+
						")  (asst.:"+getJBD((String) newPricingInfo.get("assistantCompFee"))+
						")  (spec.:"+getJBD((String) newPricingInfo.get("specFee"))+
						")  (anaes:"+getJBD((String) newPricingInfo.get("anaesthetistFee"))+
						")  (non-a:"+getJBD((String) newPricingInfo.get("nonAnaesthetistFee"))+")";

				BillingServiceDao bsd = (BillingServiceDao)SpringUtils.getBean("billingServiceDao");
				String defaultDescription = bsd.searchDescBillingCode((String) newPricingInfo.get("feeCode"), "ON");  

				String newPrice = (String) newPricingInfo.get("gpFees");
				double newDoub = (Double.parseDouble(newPrice))/10000;
				BigDecimal newPriceDec = BigDecimal.valueOf(newDoub).setScale(2, BigDecimal.ROUND_HALF_UP);

				if( newPriceDec.compareTo(zeroBD) == 0){
					newPriceDec = getJBD((String) newPricingInfo.get("specFee"));
					if( newPriceDec.compareTo(zeroBD) == 0){
						newPriceDec = getJBD((String) newPricingInfo.get("assistantCompFee"));
					}
				}

				change = processBillingCode(newPricingInfo, billingInfo, newPriceDec, "A", moreprices, defaultDescription);
				if (change != null) { changes.add(change); }

				if (updateAssistantFeesValue != null && updateAssistantFeesValue.compareTo(BigDecimal.ZERO) != 0) {
					billingInfo = bc.searchBillingCode((String) newPricingInfo.get("feeCode") + "B");
					newPriceDec = updateAssistantFeesValue;
					change = processBillingCode(newPricingInfo, billingInfo, newPriceDec, "B", moreprices, defaultDescription);
					if (change != null) { changes.add(change); }
				}

				if (updateAnaesthetistFeesValue != null && updateAnaesthetistFeesValue.compareTo(BigDecimal.ZERO) != 0) {
					billingInfo = bc.searchBillingCode((String) newPricingInfo.get("feeCode") + "C");
					newPriceDec = updateAnaesthetistFeesValue;
					change = processBillingCode(newPricingInfo, billingInfo, newPriceDec, "C", moreprices, defaultDescription);
					if (change != null) { changes.add(change); }
				}
			}
			in.close();
		}
		catch (IOException e) {
			MiscUtils.getLogger().error("SOB Upload error", e);
		}
		return changes;		
	}

	public Map processBillingCode(Map newPricingInfo, Map oldPricingInfo, BigDecimal fee, String feeType, String moreprices, String defaultDescription) {
		Hashtable change = new Hashtable();
		String oldPrice = oldPricingInfo == null ? "0.00" : (String) oldPricingInfo.get("value");
		double oldDoub = 0.00;
		try {
			oldDoub = Double.parseDouble(oldPrice);
		} catch (Exception e) {
			oldDoub = 0.00;
		}
		BigDecimal oldPriceDec = BigDecimal.valueOf(oldDoub).setScale(2, BigDecimal.ROUND_HALF_UP);
		BigDecimal diffPriceDec = fee.subtract(oldPriceDec);
		boolean feeChanged = oldPriceDec.compareTo(fee) != 0;
		boolean feeNonZero = fee.compareTo(BigDecimal.ZERO) != 0;

		// No need to add a billing code if it doesn't exist and we're not adding new codes or forcing an update.
		if (oldPricingInfo == null && !forceUpdate && !addNewCodes) { return null; }

		// No need to add a code if we're adding new codes (or forcing an update) and the fee for this code is 0.
		if (oldPricingInfo == null && (addNewCodes || forceUpdate) && !feeNonZero) { return null; }

		// No need to add a billing code if it exists and we're not adding changes to codes or forcing an update.
		if (oldPricingInfo != null && !forceUpdate && !addChangedCodes) { return null; }

		// No need to edit a billing code if the price hasn't changed.  
		if (oldPricingInfo != null && addChangedCodes && !feeChanged) { return null; }

		// Check for date change
		if (oldPricingInfo != null) {
			String oldServiceDate = (String)oldPricingInfo.get("billingservice_date");
			oldServiceDate = oldServiceDate.replace("-",  "");
			String newServiceDate = (String)newPricingInfo.get("effectiveDate");
			if(oldServiceDate.equals(newServiceDate)) {return null; }
		}

		change.put("newprice", fee);
		change.put("feeCode", newPricingInfo.get("feeCode") + feeType);
		change.put("prices", moreprices);
		change.put("effectiveDate", newPricingInfo.get("effectiveDate"));
		change.put("terminactionDate", newPricingInfo.get("terminactionDate"));

		if (oldPricingInfo == null) {
			newfees++;				
			change.put("oldprice", "--");
			change.put("diff", "");
			change.put("description", defaultDescription);
		} else {
			oldfees++;
			change.put("oldprice", oldPriceDec);
			change.put("diff", diffPriceDec);
			change.put("numCodes", "" + bc.searchNumBillingCode((String) newPricingInfo.get("feeCode")));					 
			change.put("description", isEmptyDescription((String)oldPricingInfo.get("description")) ? defaultDescription : oldPricingInfo.get("description"));		
		}
		return change;
	}

	private boolean isEmptyDescription(String desc) {
		return desc == null || desc.trim().length() == 0 || desc.trim().equals("----");		
	}

	BigDecimal getBD(String s){
		BigDecimal bd = new BigDecimal(s);
		bd.setScale(2,BigDecimal.ROUND_UP);
		return bd;
	}

	BigDecimal getBD4digit(String s){
		double dgpFees = Double.parseDouble(s);
		BigDecimal bd = BigDecimal.valueOf((dgpFees/10000));
		bd.setScale(2,BigDecimal.ROUND_HALF_UP);
		return bd;
	}


	BigDecimal getJBD(String s){
		double newDoub = (Double.parseDouble(s))/10000;
		return BigDecimal.valueOf(newDoub).setScale(2, BigDecimal.ROUND_HALF_UP);
	}

	Hashtable breakLine(String s){

		Hashtable h = null;
		if ( s != null && s.length() == 75){
			String feeCode            = s.substring(0,4);
			String effectiveDate      = s.substring(4,12);
			String terminactionDate   = s.substring(12,20);
			String gpFees             = s.substring(20,31);
			String assistantCompFee   = s.substring(31,42);
			String specFee            = s.substring(42,53);
			String anaesthetistFee    = s.substring(53,64);
			String nonAnaesthetistFee = s.substring(64,75);

			h = new Hashtable();
			h.put("feeCode", feeCode);
			h.put("effectiveDate", effectiveDate);
			h.put("terminactionDate", terminactionDate);
			h.put("gpFees", gpFees);
			h.put("assistantCompFee", assistantCompFee);
			h.put("specFee",specFee);
			h.put("anaesthetistFee", anaesthetistFee);
			h.put("nonAnaesthetistFee",nonAnaesthetistFee);
			double dgpFees = Double.parseDouble(gpFees);
			BigDecimal bd = BigDecimal.valueOf(dgpFees);
			bd.setScale(2);
			MiscUtils.getLogger().debug(feeCode+" "+effectiveDate+" "+terminactionDate+" "+gpFees+" "+assistantCompFee+" "+specFee+" "+anaesthetistFee+" "+nonAnaesthetistFee);
			MiscUtils.getLogger().debug(feeCode+" "+effectiveDate+" "+terminactionDate+" "+getJBD(gpFees)+" "+getJBD(assistantCompFee)+" "+getJBD(specFee)+" "+getJBD(anaesthetistFee)+" "+getJBD(nonAnaesthetistFee));
			MiscUtils.getLogger().debug(dgpFees+" "+(dgpFees/10000)+" "+bd.toString());

		}
		return h;
	}
}
