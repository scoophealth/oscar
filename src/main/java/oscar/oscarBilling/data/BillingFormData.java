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

package oscar.oscarBilling.data;

import java.util.ArrayList;
import java.util.List;

import org.oscarehr.PMmodule.dao.ProviderDao;
import org.oscarehr.common.dao.BillingBCDao;
import org.oscarehr.common.dao.BillingServiceDao;
import org.oscarehr.common.dao.CtlBillingServiceDao;
import org.oscarehr.common.dao.DiagnosticCodeDao;
import org.oscarehr.common.model.CtlBillingService;
import org.oscarehr.common.model.DiagnosticCode;
import org.oscarehr.common.model.Provider;
import org.oscarehr.util.SpringUtils;

public class BillingFormData {

	private DiagnosticCodeDao diagnosticCodeDao = SpringUtils.getBean(DiagnosticCodeDao.class);

	public String getBillingFormDesc(BillingForm[] billformlist, String billForm) {
		for (int i = 0; i < billformlist.length; i++) {
			if (billformlist[i].getFormCode().equals(billForm)) {
				return billformlist[i].getDescription();
			}
		}

		return "";
	}

	public BillingService[] getServiceList(String serviceGroup, String serviceType, String billRegion) {
		List<BillingService> lst = new ArrayList<BillingService>();
		BillingServiceDao dao = SpringUtils.getBean(BillingServiceDao.class);
		for (org.oscarehr.common.model.BillingService bs : dao.findByRegionGroupAndType(billRegion, serviceGroup, serviceType)) {
			BillingService billingservice = new BillingService(bs.getServiceCode(), bs.getDescription(), bs.getValue(), bs.getPercentage());
			lst.add(billingservice);
		}
		return lst.toArray(new BillingService[] {});
	}

	public Diagnostic[] getDiagnosticList(String serviceType, String billRegion) {
		List<Diagnostic> lst = new ArrayList<Diagnostic>();
		DiagnosticCodeDao dao = SpringUtils.getBean(DiagnosticCodeDao.class);

		for (DiagnosticCode dc : dao.findByRegionAndType(billRegion, serviceType)) {
			lst.add(new Diagnostic(dc.getDiagnosticCode(), dc.getDescription()));
		}

		return lst.toArray(new Diagnostic[] {});
	}

	public Location[] getLocationList(String billRegion) {
		List<Location> lst = new ArrayList<Location>();
		BillingBCDao dao = SpringUtils.getBean(BillingBCDao.class);
		for (Object[] bl : dao.findBillingLocations(billRegion)) {
			Location location = new Location(String.valueOf(bl[0]), String.valueOf(bl[1]));
			lst.add(location);
		}
		return lst.toArray(new Location[] {});
	}

	public BillingVisit[] getVisitType(String billRegion) {
		List<BillingVisit> lst = new ArrayList<BillingVisit>();
		BillingBCDao dao = SpringUtils.getBean(BillingBCDao.class);
		for (Object[] bv : dao.findBillingVisits(billRegion)) {
			lst.add(new BillingVisit(String.valueOf(bv[0]), String.valueOf(bv[1])));
		}
		return lst.toArray(new BillingVisit[] {});
	}

	public BillingPhysician[] getProviderList() {
		List<BillingPhysician> lst = new ArrayList<BillingPhysician>();
		ProviderDao dao = SpringUtils.getBean(ProviderDao.class);
		for (Provider p : dao.getProvidersWithNonEmptyOhip()) {
			BillingPhysician billingphysician = new BillingPhysician(p.getFormattedName(), p.getProviderNo());
			lst.add(billingphysician);
		}

		return lst.toArray(new BillingPhysician[] {});
	}

	public BillingForm[] getFormList() {
		List<BillingForm> lst = new ArrayList<BillingForm>();
		CtlBillingServiceDao dao = SpringUtils.getBean(CtlBillingServiceDao.class);
		for (CtlBillingService c : dao.findAll()) {
			BillingForm billingForm = new BillingForm(c.getServiceTypeName(), c.getServiceType());
			lst.add(billingForm);
		}
		return lst.toArray(new BillingForm[] {});
	}

	public class BillingService {
		String service_code;
		String description;
		String price;
		String percentage;

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
		String billingvisit;
		String description;

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

	}

	public class BillingForm {
		String formcode;
		String description;

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

	private Provider getProvider(String provider_no) {
		ProviderDao dao = SpringUtils.getBean(ProviderDao.class);
		Provider provider = dao.getProvider(provider_no);
		return provider;
	}

	public String getProviderName(String provider_no) {
		Provider provider = getProvider(provider_no);
		if (provider == null) return "";
		return provider.getFormattedName();
	}

	public String getPracNo(String provider_no) {
		Provider provider = getProvider(provider_no);
		if (provider == null) return "";
		return provider.getOhipNo();
	}

	public String getGroupNo(String provider_no) {
		Provider provider = getProvider(provider_no);
		if (provider == null) return "";
		return provider.getBillingNo();
	}

	public String getDiagDesc(String dx, String reg) {
		String dxdesc = "";
		List<DiagnosticCode> dcodes = diagnosticCodeDao.findByDiagnosticCodeAndRegion(dx, reg);
		for (DiagnosticCode dcode : dcodes) {
			dxdesc = dcode.getDescription();
		}
		return dxdesc;
	}

	public String getServiceDesc(String code, String reg) {
		String codeDesc = "";
		BillingServiceDao dao = SpringUtils.getBean(BillingServiceDao.class);
		for (org.oscarehr.common.model.BillingService bs : dao.findBillingCodesByCode(code, reg)) {
			codeDesc = bs.getDescription();
		}
		return codeDesc;
	}

	public String getServiceGroupName(String serviceGroup) {
		String ret = "";

		CtlBillingServiceDao dao = SpringUtils.getBean(CtlBillingServiceDao.class);
		for (CtlBillingService c : dao.findByServiceGroup(serviceGroup)) {
			ret = c.getServiceGroupName();
		}
		return ret;
	}
}