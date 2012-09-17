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

package oscar.oscarBilling.ca.bc.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.oscarehr.PMmodule.dao.ProviderDao;
import org.oscarehr.billing.CA.BC.dao.BillingStatusTypesDao;
import org.oscarehr.billing.CA.BC.model.BillingStatusTypes;
import org.oscarehr.common.dao.BillingBCDao;
import org.oscarehr.common.dao.BillingPaymentTypeDao;
import org.oscarehr.common.dao.BillingServiceDao;
import org.oscarehr.common.dao.CtlBillingServiceDao;
import org.oscarehr.common.dao.CtlDiagCodeDao;
import org.oscarehr.common.dao.DiagnosticCodeDao;
import org.oscarehr.common.model.Billing;
import org.oscarehr.common.model.BillingPaymentType;
import org.oscarehr.common.model.CtlBillingService;
import org.oscarehr.common.model.DiagnosticCode;
import org.oscarehr.common.model.Provider;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.entities.BillingStatusType;
import oscar.entities.PaymentType;
import oscar.util.UtilDateUtilities;

public class BillingFormData {
	
	private DiagnosticCodeDao diagnosticCodeDao = SpringUtils.getBean(DiagnosticCodeDao.class);

	public ArrayList<PaymentType> getPaymentTypes() {
		ArrayList<PaymentType> types = new ArrayList<PaymentType>();

		BillingPaymentTypeDao dao = (BillingPaymentTypeDao) SpringUtils.getBean(BillingPaymentTypeDao.class);
		for (BillingPaymentType type : dao.findAll()) {
			PaymentType tp = new PaymentType();
			tp.setId(type.getId().toString());
			tp.setPaymentType(type.getPaymentType());
			types.add(tp);
		}
		return types;
	}

	public String getProviderNo(String billno) {
		BillingBCDao dao = (BillingBCDao) SpringUtils.getBean(BillingBCDao.class);
		Billing billing = dao.find(billno);
		if (billing != null) return billing.getProviderNo();

		MiscUtils.getLogger().error("Unable to find provider for " + billno);
		return null;
	}

	/**
	 * Returns a list of status type instances according to the supplied String array of allowable status codes
	 * If the supplied array is null or empty, a full list is returned
	 * 
	 * @return List
	 */
	public List<BillingStatusType> getStatusTypes(String[] codes) {
		BillingStatusTypesDao dao = SpringUtils.getBean(BillingStatusTypesDao.class);
		List<BillingStatusTypes> types = null;
		if (codes == null || codes.length == 0) types = dao.findAll();
		else types = dao.findByCodes(Arrays.asList(codes));

		ArrayList<BillingStatusType> head = new ArrayList<BillingStatusType>();
		// prepends a default empty bean to the list
		BillingStatusType tp = new BillingStatusType();
		tp.setBillingstatus("");
		tp.setDisplayNameExt("");
		head.add(tp);

		for (BillingStatusTypes t : types) {
			tp = new BillingStatusType();
			tp.setBillingstatus(t.getId().toString());
			tp.setDisplayNameExt(t.getDisplayNameExt());

			head.add(tp);
		}
		return head;
	}

	public String getBillingFormDesc(BillingForm[] billformlist, String billForm) {
		for (int i = 0; i < billformlist.length; i++) {
			if (billformlist[i].getFormCode().equals(billForm)) {
				return billformlist[i].getDescription();
			}
		}

		return "";
	}

	public BillingService[] getServiceList(String serviceGroup, String serviceType, String billRegion) {
		List<BillingService> result = new ArrayList<BillingService>();

		BillingBCDao dao = SpringUtils.getBean(BillingBCDao.class);
		List<Object[]> services = dao.findBillingServices(billRegion, serviceGroup, serviceType);
		for (Object[] s : services)
			result.add(new BillingService(s));
		return result.toArray(new BillingService[result.size()]);
	}

	public BillingService[] getServiceList(String serviceGroup, String serviceType, String billRegion, Date billingDate) {
		String billReferenceDate = UtilDateUtilities.DateToString(billingDate);

		List<BillingService> result = new ArrayList<BillingService>();
		BillingBCDao dao = SpringUtils.getBean(BillingBCDao.class);
		List<Object[]> services = dao.findBillingServices(billRegion, serviceGroup, serviceType, billReferenceDate);
		for (Object[] s : services) {
			result.add(new BillingService(s));
		}
		return result.toArray(new BillingService[result.size()]);
	}

	public Diagnostic[] getDiagnosticList(String serviceType, String billRegion) {
		CtlDiagCodeDao dao = SpringUtils.getBean(CtlDiagCodeDao.class);

		List<Diagnostic> result = new ArrayList<Diagnostic>();
		for (Object[] d : dao.getDiagnostics(billRegion, serviceType)) {
			result.add(new Diagnostic(d));
		}
		return result.toArray(new Diagnostic[result.size()]);
	}

	public Location[] getLocationList(String billRegion) {
		BillingBCDao dao = SpringUtils.getBean(BillingBCDao.class);

		List<Location> result = new ArrayList<Location>();
		for (Object[] l : dao.findBillingLocations(billRegion)) {
			result.add(new Location(l));
		}
		return result.toArray(new Location[result.size()]);
	}

	public BillingVisit[] getVisitType(String billRegion) {
		BillingBCDao dao = SpringUtils.getBean(BillingBCDao.class);

		List<BillingVisit> result = new ArrayList<BillingVisit>();
		for (Object[] v : dao.findBillingVisits(billRegion)) {
			result.add(new BillingVisit(v));
		}
		return result.toArray(new BillingVisit[result.size()]);
	}

	public BillingPhysician[] getProviderList() {
		ArrayList<BillingPhysician> lst = new ArrayList<BillingPhysician>();
		ProviderDao dao = SpringUtils.getBean(ProviderDao.class);
		for (Provider provider : dao.getProvidersWithNonEmptyOhip())
			lst.add(new BillingPhysician(provider.getFormattedName(), provider.getProviderNo()));
		return lst.toArray(new BillingPhysician[lst.size()]);
	}

	public BillingForm[] getFormList() {
		List<BillingForm> result = new ArrayList<BillingForm>();

		CtlBillingServiceDao dao = SpringUtils.getBean(CtlBillingServiceDao.class);
		for (Object[] b : dao.getAllServiceTypes()) {
			result.add(new BillingForm(b));
		}
		return result.toArray(new BillingForm[result.size()]);
	}

	public class BillingService {

		String service_code;
		String description;
		String price;
		String percentage;

		public BillingService(Object[] s) {
			this(String.valueOf(s[0]), String.valueOf(s[1]), String.valueOf(s[2]), String.valueOf(s[3]));
		}

		public BillingService(String service_code, String description, String price, String percentage) {
			this.service_code = service_code;
			this.description = description;
			this.price = price;
			this.percentage = percentage;

		}

		public String getServiceCode() {
			return service_code;
		}

		public String getDescription() {
			return description;
		}

		public String getPrice() {
			return price;
		}

		public String getPercentage() {
			return percentage;
		}

	}

	public class Diagnostic {
		String diagnostic_code;
		String description;

		public Diagnostic(Object[] codes) {
			this(String.valueOf(codes[0]), String.valueOf(codes[1]));
		}

		public Diagnostic(String diagnostic_code, String description) {
			this.diagnostic_code = diagnostic_code;
			this.description = description;

		}

		public String getDiagnosticCode() {
			return diagnostic_code;
		}

		public String getDescription() {
			return description;
		}

	}

	public class Location {
		String billinglocation;
		String description;

		public Location(Object[] l) {
			this(String.valueOf(l[0]), String.valueOf(l[1]));
		}

		public Location(String billinglocation, String description) {
			this.billinglocation = billinglocation;
			this.description = description;

		}

		public String getBillingLocation() {
			return billinglocation;
		}

		public String getDescription() {
			return description;
		}

	}

	public class BillingVisit {
		String billingvisit = "";
		String description = "";
		String displayName = "";

		public BillingVisit(Object[] o) {
			this(String.valueOf(o[0]), String.valueOf(o[1]));
		}

		public BillingVisit(String billingvisit, String description) {
			this.billingvisit = billingvisit;
			this.description = description;

		}

		public String getVisitType() {
			return billingvisit;
		}

		public String getDescription() {
			return description;
		}

		public String getDisplayName() {
			return billingvisit + "|" + description;
		}

	}

	public class BillingForm {
		String formcode;
		String description;

		public BillingForm(Object[] b) {
			this(String.valueOf(b[0]), String.valueOf(b[1]));
		}

		public BillingForm(String description, String formcode) {
			this.formcode = formcode;
			this.description = description;

		}

		public String getFormCode() {
			return formcode;
		}

		public String getDescription() {
			return description;
		}

	}

	public class BillingPhysician {
		String providername;
		String provider_no;

		public BillingPhysician(String providername, String provider_no) {
			this.providername = providername;
			this.provider_no = provider_no;

		}

		public String getProviderName() {
			return providername;
		}

		public String getProviderNo() {
			return provider_no;
		}

	}

	public String getBillingType(String billNo) {
		BillingBCDao dao = SpringUtils.getBean(BillingBCDao.class);
		Billing billing = dao.find(billNo);
		if (billing != null) return billing.getBillingtype();
		return null;
	}

	public String getProviderName(String provider_no) {
		ProviderDao dao = SpringUtils.getBean(ProviderDao.class);
		Provider provider = dao.getProvider(provider_no);
		if (provider == null) return "";
		return provider.getFormattedName();
	}

	public String getPracNo(String provider_no) {
		ProviderDao dao = SpringUtils.getBean(ProviderDao.class);
		Provider provider = dao.getProvider(provider_no);
		if (provider == null) return "";

		return provider.getOhipNo();
	}

	public String getGroupNo(String provider_no) {
		ProviderDao dao = SpringUtils.getBean(ProviderDao.class);
		Provider provider = dao.getProvider(provider_no);
		if (provider == null) return "";

		return provider.getBillingNo();
	}

	public String getDiagDesc(String dx, String reg) {
		String dxdesc = "";
		for (DiagnosticCode dcode : diagnosticCodeDao.findByDiagnosticCodeAndRegion(dx, reg)) {
			dxdesc = dcode.getDescription();
		}
		return dxdesc;
	}

	public String getServiceDesc(String code, String reg) {
		BillingServiceDao dao = SpringUtils.getBean(BillingServiceDao.class);
		List<org.oscarehr.common.model.BillingService> services = dao.findBillingCodesByCode(code, reg);
		if (services.isEmpty()) 
			return "";
		int lastPosition = services.size() - 1;
		org.oscarehr.common.model.BillingService service = services.get(lastPosition);
		if (service != null) 
			return service.getDescription();
		return "";
	}

	public String getServiceGroupName(String serviceGroup) {
		CtlBillingServiceDao dao = SpringUtils.getBean(CtlBillingServiceDao.class);
		List<CtlBillingService> services = dao.findByServiceGroup(serviceGroup);
		for (CtlBillingService service : services)
			return service.getServiceGroupName();
		return "";
	}

	public String getServiceGroupName(String serviceGroup, String serviceType) {
		CtlBillingServiceDao dao = SpringUtils.getBean(CtlBillingServiceDao.class);
		for (CtlBillingService b : dao.findByServiceGroupAndServiceType(serviceGroup, serviceType))
			return b.getServiceGroupName();
		return "";
	}

	public void setPrivateFees(BillingFormData.BillingService[] svc) {
		BillingServiceDao dao = SpringUtils.getBean(BillingServiceDao.class);

		for (int i = 0; i < svc.length; i++) {
			String serviceCode = "A" + svc[i].getServiceCode();

			for (org.oscarehr.common.model.BillingService b : dao.findByServiceCode(serviceCode))
				svc[i].price = b.getValue();
		}
	}

	/**
	 * Returns a list of InjuryLocation instances
	 * @return List
	 */
	public List<InjuryLocation> getInjuryLocationList() {
		BillingBCDao dao = SpringUtils.getBean(BillingBCDao.class);
		List<InjuryLocation> result = new ArrayList<InjuryLocation>();
		for(Object[] l : dao.findInjuryLocations())
			result.add(new InjuryLocation(l));
		return result;
	}

	/**
	 * Returns a Properties instance that contains the followign key/value pair
	 * * key = A Billing Status Type Code
	 * * value =  A Billing Status Type Extended Description
	 * @param statusType List
	 * @return Properties
	 */
	@SuppressWarnings("rawtypes")
    public Properties getStatusProperties(List statusType) {
		Properties p = new Properties();
		for (Iterator iter = statusType.iterator(); iter.hasNext();) {
			oscar.entities.BillingStatusType item = (oscar.entities.BillingStatusType) iter.next();
			p.setProperty(item.getBillingstatus(), item.getDisplayNameExt());
		}
		return p;
	}

}
