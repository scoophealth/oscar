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

package oscar.oscarBilling.ca.bc.quickbilling;


import java.util.ArrayList;
import java.util.List;
import org.apache.struts.action.ActionForm;
import org.oscarehr.common.model.ProviderData;

import oscar.oscarBilling.ca.bc.data.BillingFormData.BillingVisit;
import oscar.oscarBilling.ca.bc.pageUtil.BillingSessionBean;


public class QuickBillingBCFormBean extends ActionForm {

	/**
	 * @author Dennis Warren
	 * Company Colcamex Resources
	 * Date Jun 4, 2012
	 */
	private static final long serialVersionUID = 1L;
	private ArrayList<BillingSessionBean> billingData;
	private String billingProvider;
	private String billingProviderNo;
	private String serviceDate;
	private String visitLocation;
	private BillingVisit[] billingVisitTypes; 
	private List<ProviderData> providerList;
	private Boolean isHeaderSet;
	private String creator;
	private String halfBilling;


	public QuickBillingBCFormBean(){
		
		this.billingProvider = "";
		this.billingProviderNo = "";
		this.serviceDate = "";
		this.visitLocation = "";
		this.creator = "";
		this.isHeaderSet = false;
		this.halfBilling = "";
	
	}
	
	
	public String getHalfBilling() {
		return halfBilling;
	}


	public void setHalfBilling(String halfBilling) {
		this.halfBilling = halfBilling;
	}


	public String getCreator() {
		return creator;
	}


	public void setCreator(String creator) {
		this.creator = creator;
	}



	public void setIsHeaderSet(Boolean set) {
		this.isHeaderSet = set;
	}
	
	public Boolean getIsHeaderSet() {
		return isHeaderSet;
	}
		
	public List<ProviderData> getProviderList() {
		return providerList;
	}

	public void setProviderList(List<ProviderData> providerList) {
		this.providerList = providerList;
	}


	public String getBillingProviderNo() {
		return billingProviderNo;
	}



	public void setBillingProviderNo(String billingProviderNo) {
		this.billingProviderNo = billingProviderNo;
	}



	public BillingVisit[] getBillingVisitTypes() {
		return billingVisitTypes;
	}



	public void setBillingVisitTypes(BillingVisit[] billingVisitTypes) {
		this.billingVisitTypes = billingVisitTypes;
	}



	public String getBillingProvider() {
		return billingProvider;
	}



	public void setBillingProvider(String billingProvider) {
		this.billingProvider = billingProvider;
	}



	public String getServiceDate() {
		return serviceDate;
	}



	public void setServiceDate(String serviceDate) {
		this.serviceDate = serviceDate;
	}



	public String getVisitLocation() {
		return visitLocation;
	}



	public void setVisitLocation(String visitLocation) {
		this.visitLocation = visitLocation;
	}



	public ArrayList<BillingSessionBean> getBillingData() {
		return billingData;
	}

	public void setBillingData(ArrayList<BillingSessionBean> billingData) {		
		
		this.billingData = billingData;
	}
	
	@Override
	public String toString() {
		return (
				" PROVIDER="+billingProvider+
				" PROVIDER NUMBER="+billingProviderNo+
				" SERVICE DATE="+serviceDate+
				" VISIT LOCATION="+visitLocation+ 
				" IS HEADER SET="+isHeaderSet+
				" CREATOR="+creator+
				" BILL DATA="+billingData.size()+" ENTRY(S)"
		);
	}

	

}
