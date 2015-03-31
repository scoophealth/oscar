/**
 * Copyright (c) 2006-. OSCARservice, OpenSoft System. All Rights Reserved.
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
 */

package oscar.oscarBilling.ca.on.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.oscarehr.PMmodule.dao.ProviderDao;
import org.oscarehr.billing.CA.ON.dao.BillingONFavouriteDao;
import org.oscarehr.billing.CA.ON.dao.BillingONFilenameDao;
import org.oscarehr.billing.CA.ON.model.BillingONFavourite;
import org.oscarehr.billing.CA.ON.model.BillingONFilename;
import org.oscarehr.common.dao.BillingPaymentTypeDao;
import org.oscarehr.common.dao.ClinicLocationDao;
import org.oscarehr.common.dao.OscarAppointmentDao;
import org.oscarehr.common.dao.ProfessionalSpecialistDao;
import org.oscarehr.common.dao.ProviderSiteDao;
import org.oscarehr.common.model.Appointment;
import org.oscarehr.common.model.BillingPaymentType;
import org.oscarehr.common.model.ClinicLocation;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.common.model.ProfessionalSpecialist;
import org.oscarehr.common.model.Provider;
import org.oscarehr.common.model.ProviderSite;
import org.oscarehr.managers.DemographicManager;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.SxmlMisc;

public class JdbcBillingPageUtil {
	
	private static final Logger _logger = MiscUtils.getLogger();
	
	private OscarAppointmentDao appointmentDao = (OscarAppointmentDao)SpringUtils.getBean("oscarAppointmentDao");
	private ProfessionalSpecialistDao professionalSpecialistDao = (ProfessionalSpecialistDao) SpringUtils.getBean("professionalSpecialistDao");
	private ClinicLocationDao clinicLocationDao = (ClinicLocationDao) SpringUtils.getBean("clinicLocationDao");
	private ProviderDao providerDao = SpringUtils.getBean(ProviderDao.class);
	private BillingPaymentTypeDao billingPaymentTypeDao = SpringUtils.getBean(BillingPaymentTypeDao.class);
	private BillingONFavouriteDao billingONFavouriteDao = SpringUtils.getBean(BillingONFavouriteDao.class);
	private DemographicManager demographicManager = SpringUtils.getBean(DemographicManager.class);
	private BillingONFilenameDao billingONFilenameDao = SpringUtils.getBean(BillingONFilenameDao.class);
	private ProviderSiteDao providerSiteDao = SpringUtils.getBean(ProviderSiteDao.class);

	public List<String> getCurTeamProviderStr(String provider_no) {
		List<String> retval = new ArrayList<String>();
		String proid = "";
		String proFirst = "";
		String proLast = "";
		String proOHIP = "";
		String specialty_code;
		String billinggroup_no;
		
		List<Provider> ps = providerDao.getCurrentTeamProviders(provider_no);
		for(Provider p:ps) {
			proid = p.getProviderNo();
			proLast = p.getLastName();
			proFirst = p.getFirstName();
			proOHIP = p.getOhipNo();
			billinggroup_no = getXMLStringWithDefault(p.getComments(), "xml_p_billinggroup_no", "0000");
			specialty_code = getXMLStringWithDefault(p.getComments(), "xml_p_specialty_code", "00");
			retval.add(proid + "|" + proLast + "|" + proFirst + "|" + proOHIP + "|" + billinggroup_no + "|" + specialty_code);
		}

		return retval;
	}

	public List<String> getCurSiteProviderStr(String provider_no) {
		List<String> retval = new ArrayList<String>();
		
		List<ProviderSite> sites = providerSiteDao.findByProviderNo(provider_no);
		List<Integer> siteIds =  new ArrayList<Integer>();
		for(ProviderSite site:sites) {
			siteIds.add(site.getId().getSiteId());
		}
		
		ProviderSiteDao dao = SpringUtils.getBean(ProviderSiteDao.class);
		
		String proid = "";
		String proFirst = "";
		String proLast = "";
		String proOHIP = "";
		String specialty_code;
		String billinggroup_no;
		
		try {
			for(Provider p : dao.findActiveProvidersWithSites(provider_no)) {
				proid = p.getProviderNo();
				proLast = p.getLastName();
				proFirst = p.getFirstName();
				proOHIP = p.getOhipNo();
				billinggroup_no = getXMLStringWithDefault(p.getComments(), "xml_p_billinggroup_no", "0000");
				specialty_code = getXMLStringWithDefault(p.getComments(), "xml_p_specialty_code", "00");
				
				retval.add(proid + "|" + proLast + "|" + proFirst + "|" + proOHIP + "|" + billinggroup_no + "|"
						+ specialty_code);
			}
		} catch (Exception e) {
			_logger.error("error", e);
		}

		return retval;
	}

	public List<String> getCurProviderStr() {
		List<String> retval = new ArrayList<String>();
		
		List<Provider> ps = providerDao.getBillableProviders();
		String proid = "";
		String proFirst = "";
		String proLast = "";
		String proOHIP = "";
		String specialty_code;
		String billinggroup_no;
		
		for(Provider p:ps) {
			proid = p.getProviderNo();
			proLast = p.getLastName();
			proFirst = p.getFirstName();
			proOHIP = p.getOhipNo();
			billinggroup_no = getXMLStringWithDefault(p.getComments(), "xml_p_billinggroup_no", "0000");
			specialty_code = getXMLStringWithDefault(p.getComments(), "xml_p_specialty_code", "00");
			retval.add(proid + "|" + proLast + "|" + proFirst + "|" + proOHIP + "|" + billinggroup_no + "|" + specialty_code);
		}
		
		return retval;
	}

	private String getXMLStringWithDefault(String xmlStr, String xmlName, String strDefault) {
		String retval = SxmlMisc.getXmlContent(xmlStr, "<" + xmlName + ">", "</" + xmlName + ">");
		retval = retval == null || "".equals(retval) ? strDefault : retval;
		return retval;
	}

	public Properties getPropProviderOHIP() {
		Properties retval = new Properties();
		List<Provider> ps = providerDao.getBillableProviders();
		
		String proid = "";
		String proOHIP = "";
		
		for(Provider p:ps) {
			proid = p.getProviderNo();
			proOHIP = p.getOhipNo();
			retval.setProperty(proid, proOHIP);
		}
		
		return retval;
	}

	public Properties getPropProviderName() {
		Properties retval = new Properties();
		
		List<Provider> ps = providerDao.getProviders();
		String proid = "";
		String proName = "";
		for(Provider p:ps) {
			proid = p.getProviderNo();
			proName = p.getLastName() + "," + p.getFirstName();
			retval.setProperty(proid, proName);
		}
		
		return retval;
	}

	public BillingProviderData getProviderObj(String providerNo) {
		BillingProviderData pObj = null;
		
		List<Provider> ps = new ArrayList<Provider>();
		if(providerNo.equals("all")) {
			ps=providerDao.getActiveProviders();
		} else {
			Provider p = providerDao.getProvider(providerNo);
			if(p.getStatus().equals("1"))
				ps.add(p);
		}
		String specialty_code;
		String billinggroup_no;
		
		for(Provider p:ps) {
			pObj = new BillingProviderData();
			billinggroup_no = getXMLStringWithDefault(p.getComments(), "xml_p_billinggroup_no", "0000");
			specialty_code = getXMLStringWithDefault(p.getComments(), "xml_p_specialty_code", "00");
			pObj.setProviderNo(p.getProviderNo());
			pObj.setLastName(p.getLastName());
			pObj.setFirstName(p.getFirstName());
			pObj.setOhipNo(p.getOhipNo());
			pObj.setRmaNo(p.getRmaNo());
			pObj.setSpecialtyCode(specialty_code);
			pObj.setBillingGroupNo(billinggroup_no);
		}
		
		return pObj;
	}

        public List<BillingProviderData> getProviderObjList(String providerNo) {
		BillingProviderData pObj = null;
		List<BillingProviderData> res = new ArrayList<BillingProviderData>();
		
		List<Provider> ps = new ArrayList<Provider>();
		if(providerNo.equals("all")) {
			ps=providerDao.getActiveProviders();
		} else {
			Provider p = providerDao.getProvider(providerNo);
			if(p.getStatus().equals("1"))
				ps.add(p);
		}
		String specialty_code;
		String billinggroup_no;
		for(Provider p:ps) {
			pObj = new BillingProviderData();
			billinggroup_no = getXMLStringWithDefault(p.getComments(), "xml_p_billinggroup_no","0000");
			specialty_code = getXMLStringWithDefault(p.getComments(), "xml_p_specialty_code", "00");
			pObj.setProviderNo(p.getProviderNo());
			pObj.setLastName(p.getLastName());
			pObj.setFirstName(p.getFirstName());
			pObj.setOhipNo(p.getOhipNo());
			pObj.setRmaNo(p.getRmaNo());
			pObj.setSpecialtyCode(specialty_code);
			pObj.setBillingGroupNo(billinggroup_no);
			res.add(pObj);
		}
		
		return res;
	}

	public List<BillingProviderData> getProvider(String diskId) {
		List<BillingProviderData> retval = new ArrayList<BillingProviderData>();
		String providerNo = null;
		
		List<BillingONFilename> fs = billingONFilenameDao.findByDiskId(Integer.parseInt(diskId));
		for(BillingONFilename f:fs) {
			providerNo = f.getProviderNo();

			Provider p = providerDao.getProvider(providerNo);
			if(p != null && p.getStatus().equals("1") && p.getOhipNo().length()>0) {
				String specialty_code;
				String billinggroup_no;
				billinggroup_no = getXMLStringWithDefault(p.getComments(), "xml_p_billinggroup_no","0000");
				specialty_code = getXMLStringWithDefault(p.getComments(), "xml_p_specialty_code","00");

				BillingProviderData pObj = new BillingProviderData();
				pObj.setProviderNo(p.getProviderNo());
				pObj.setLastName(p.getLastName());
				pObj.setFirstName(p.getFirstName());
				pObj.setOhipNo(p.getOhipNo());
				pObj.setSpecialtyCode(specialty_code);
				pObj.setBillingGroupNo(billinggroup_no);
				retval.add(pObj);
			}
		}
		return retval;
	}

	public List<BillingProviderData> getCurSoloProvider() {
		List<BillingProviderData> retval = new ArrayList<BillingProviderData>();
		String specialty_code;
		String billinggroup_no;
		
		List<Provider> ps = providerDao.getBillableProviders();
		for(Provider p:ps) {
			billinggroup_no = getXMLStringWithDefault(p.getComments(), "xml_p_billinggroup_no", "0000");
			specialty_code = getXMLStringWithDefault(p.getComments(), "xml_p_specialty_code", "00");
			if (!"0000".equals(billinggroup_no))
				continue;
			BillingProviderData pObj = new BillingProviderData();
			pObj.setProviderNo(p.getProviderNo());
			pObj.setLastName(p.getLastName());
			pObj.setFirstName(p.getFirstName());
			pObj.setOhipNo(p.getOhipNo());
			pObj.setSpecialtyCode(specialty_code);
			pObj.setBillingGroupNo(billinggroup_no);
			retval.add(pObj);
		}
		
		return retval;
	}

	public List<BillingProviderData> getCurGrpProvider() {
		List<BillingProviderData> retval = new ArrayList<BillingProviderData>();
		String specialty_code;
		String billinggroup_no;
		
		List<Provider> ps = providerDao.getBillableProviders();
		for(Provider p:ps) {
			billinggroup_no = getXMLStringWithDefault(p.getComments(), "xml_p_billinggroup_no","0000");
			specialty_code = getXMLStringWithDefault(p.getComments(), "xml_p_specialty_code", "00");
			if ("0000".equals(billinggroup_no))
				continue;
			BillingProviderData pObj = new BillingProviderData();
			pObj.setProviderNo(p.getProviderNo());
			pObj.setLastName(p.getLastName());
			pObj.setFirstName(p.getFirstName());
			pObj.setOhipNo(p.getOhipNo());
			pObj.setSpecialtyCode(specialty_code);
			pObj.setBillingGroupNo(billinggroup_no);
			retval.add(pObj);
		}

		return retval;
	}

	public boolean updateApptStatus(String apptNo, String status, String userNo) {
		Appointment appt = appointmentDao.find(Integer.valueOf(apptNo));
		if(appt != null) {
			appt.setStatus(status);
			appt.setLastUpdateUser(userNo);
			appt.setUpdateDateTime(new Date());
			appointmentDao.merge(appt);
			return true;
		}
		return false;
	}

	public String getApptStatus(String apptNo) {
		String retval = "T";
		
		Appointment appt = appointmentDao.find(Integer.valueOf(apptNo));
		if(appt != null) {
			retval = appt.getStatus();
		}
		
		return retval;
	}

	public List<String> getPatientCurBillingDemographic(LoggedInInfo loggedInInfo, String demoNo) {
		List<String> retval = null;
		Demographic d = demographicManager.getDemographic(loggedInInfo, demoNo);
		if(d != null) {
			retval = new ArrayList<String>();
			retval.add(d.getLastName());
			retval.add(d.getFirstName());
			retval.add(d.getYearOfBirth()+d.getMonthOfBirth()+d.getDateOfBirth());
			retval.add(d.getHin() == null ? "" : d.getHin());
			retval.add(d.getVer() == null ? "" : d.getVer());
			retval.add(d.getHcType() == null ? "" :d.getHcType());
			retval.add(d.getSex().startsWith("F") ? "2" : "1");
			retval.add(d.getFamilyDoctor() == null ? "" : d.getFamilyDoctor());
			retval.add(d.getProviderNo() == null ? "" : d.getProviderNo());
			retval.add(d.getRosterStatus() == null ? "" : d.getRosterStatus());
		}
		return retval;
	}

	public String getReferDocSpet(String billingNo) {
		String retval = null;
		ProfessionalSpecialist specialist = professionalSpecialistDao.getByReferralNo(billingNo);
        if(specialist != null) {
        	return specialist.getSpecialtyType();
        }

		return retval;
	}

	public List<String> getPatientCurBillingDemo(LoggedInInfo loggedInInfo, String demoNo) {
		List<String> retval = null;
		Demographic d = demographicManager.getDemographic(loggedInInfo, demoNo);
		if(d != null) {
			retval = new ArrayList<String>();
			retval.add(d.getLastName());
			retval.add(d.getFirstName());
			retval.add(d.getYearOfBirth()+d.getMonthOfBirth()+d.getDateOfBirth());
			retval.add(d.getHin());
			retval.add(d.getVer());
			retval.add(d.getHcType());
			retval.add(d.getSex().startsWith("F") ? "2" : "1");
			retval.add(d.getFamilyDoctor() == null ? "" : d.getFamilyDoctor());
			retval.add(d.getProviderNo() == null ? "" : d.getProviderNo());
		}
		return retval;
	}

	// name : code|dx|
	public List<String> getBillingFavouriteList() {
		List<String> retval = new ArrayList<String>();
		List<BillingONFavourite> bs = billingONFavouriteDao.findCurrent();
		Collections.sort(bs, BillingONFavourite.NAME_COMPARATOR);
		for(BillingONFavourite b:bs) {
			retval.add(b.getName());
			retval.add(b.getServiceDx());
		}
		return retval;
	}

	public List<String> getBillingFavouriteOne(String name) {
		List<String> retval = new ArrayList<String>();
		List<BillingONFavourite> bs = billingONFavouriteDao.findByName(name);
		for(BillingONFavourite b:bs) {
			if(b.getDeleted() == 1)
				continue;
			retval.add(b.getName());
			retval.add(b.getServiceDx());
		}
		return retval;
	}

	public int addBillingFavouriteList(String name, String list, String providerNo) {
		BillingONFavourite b = new BillingONFavourite();
		b.setName(name);
		b.setServiceDx(list);
		b.setProviderNo(providerNo);
		b.setTimestamp(new Date());
		b.setDeleted(0);
		billingONFavouriteDao.persist(b);
		
		return b.getId();
	}

//	 @ OSCARSERVICE
	public boolean delBillingFavouriteList(String name, String providerNo) {
		List<BillingONFavourite> bs = billingONFavouriteDao.findByNameAndProviderNo(name,providerNo);
		for(BillingONFavourite b:bs) {
			b.setDeleted(1);
			billingONFavouriteDao.merge(b);
		}
		return true;
	}
	// @ OSCARSERVICE

	public boolean updateBillingFavouriteList(String name, String list, String providerNo) {
		List<BillingONFavourite> bs = billingONFavouriteDao.findByName(name);
		for(BillingONFavourite b:bs) {
			b.setServiceDx(list);
			b.setProviderNo(providerNo);
			billingONFavouriteDao.merge(b);
		}
		return true;
	}

	public List<String> getPaymentType() {
		List<String> retval = new ArrayList<String>();
		List<BillingPaymentType> bs = billingPaymentTypeDao.findAll();
		for(BillingPaymentType b:bs) {
			retval.add("" + b.getId());
			retval.add(b.getPaymentType());
		}
		
		return retval;
	}

	public List<String> getFacilty_num() {
		List<String> retval = new ArrayList<String>();
		List<ClinicLocation> clinicLocations = clinicLocationDao.findByClinicNo(1);
		for(ClinicLocation clinicLocation:clinicLocations) {
			retval.add(clinicLocation.getClinicLocationNo());
			retval.add(clinicLocation.getClinicLocationName());
		}

		return retval;
	}

}
