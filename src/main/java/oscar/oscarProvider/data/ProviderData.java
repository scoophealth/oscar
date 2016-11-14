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

/*
 * ProviderData.java
 *
 * Created on August 19, 2004, 2:15 PM
 */

package oscar.oscarProvider.data;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.oscarehr.common.dao.ProviderDataDao;
import org.oscarehr.common.dao.ProviderPreferenceDao;
import org.oscarehr.common.model.ProviderPreference;
import org.oscarehr.util.SpringUtils;

import oscar.util.ConversionUtils;

/**
 *
 * @author  Jay Gallagher
 * Used to access data in provider table
 * 
 * @deprecated use {@link org.oscarehr.common.model.ProviderData} instead
 */
public class ProviderData {
	private static final int EXTERNAL_PROVIDER_ID_CONSTRAINT = -1000;
	/*
	+------------------------+--------------+------+-----+------------+-------+
	| Field                  | Type         | Null | Key | Default    | Extra |
	+------------------------+----------f----+------+-----+------------+-------+
	| provider_no            | varchar(6)   | NO   | PRI |            |       |
	| last_name              | varchar(30)  | NO   |     |            |       |
	| first_name             | varchar(30)  | NO   |     |            |       |
	| provider_type          | varchar(15)  | NO   |     |            |       |
	| specialty              | varchar(40)  | NO   |     |            |       |
	| team                   | varchar(20)  | YES  |     |            |       |
	| sex                    | char(1)      | NO   |     |            |       |
	| dob                    | date         | YES  |     | NULL       |       |
	| address                | varchar(40)  | YES  |     | NULL       |       |
	| phone                  | varchar(20)  | YES  |     | NULL       |       |
	| work_phone             | varchar(50)  | YES  |     | NULL       |       |
	| ohip_no                | varchar(20)  | YES  |     | NULL       |       |
	| rma_no                 | varchar(20)  | YES  |     | NULL       |       |
	| billing_no             | varchar(20)  | YES  |     | NULL       |       |
	| hso_no                 | varchar(10)  | YES  |     | NULL       |       |
	| status                 | char(1)      | YES  |     | NULL       |       |
	| comments               | text         | YES  |     | NULL       |       |
	| provider_activity      | char(3)      | YES  |     | NULL       |       |
	| practitionerNo         | varchar(20)  | YES  |     | NULL       |       |
	| init                   | varchar(10)  | YES  |     | NULL       |       |
	| job_title              | varchar(100) | YES  |     | NULL       |       |
	| email                  | varchar(60)  | YES  |     | NULL       |       |
	| title                  | varchar(20)  | YES  |     | NULL       |       |
	| lastUpdateUser         | varchar(6)   | YES  |     | NULL       |       |
	| lastUpdateDate         | date         | YES  |     | NULL       |       |
	| signed_confidentiality | date         | YES  |     | 0001-01-01 |       |
	+------------------------+--------------+------+-----+------------+-------+
	*/

	private static final String PROVIDER_TYPE_DOCTOR = "doctor";
	String provider_no;
	String last_name;
	String first_name;
	String provider_type;
	String specialty;
	String team;
	String sex;
	String dob;
	String address;
	String phone;
	String work_phone;
	String ohip_no;
	String rma_no;
	String billing_no;
	String hso_no;
	String status;
	String comments;
	String provider_activity;
	String practitionerNo;
	String init;
	String job_title;
	String email;
	String title;
	String lastUpdateUser;
	String lastUpdateDate;
	String signed_confidentiality;

	/** Creates a new instance of ProviderData */
	public ProviderData() {
	}

	public ProviderData(String providerNo) {
		getProvider(providerNo);
	}

	public List<String> getProviderListWithInsuranceNo() {
		return getProviderListWithInsuranceNo("%");
	}

	/*
	 * Creates a deep copy of this instance, for proper interoperability with JPA classes.  
	 * 
	 * @return
	 * 		Returns a new {@link org.oscarehr.common.model.ProviderData} instance containing
	 * a copy of information in this instance. 
	 */
	private org.oscarehr.common.model.ProviderData toProvider() {
		org.oscarehr.common.model.ProviderData result = new org.oscarehr.common.model.ProviderData();
		result.set(provider_no);
		result.setLastName(last_name);
		result.setFirstName(first_name);
		result.setProviderType(provider_type);
		result.setSpecialty(specialty);
		result.setTeam(team);
		result.setSex(sex);
		result.setDob(ConversionUtils.fromDateString(dob));
		result.setAddress(address);
		result.setPhone(phone);
		result.setWorkPhone(work_phone);
		result.setOhipNo(ohip_no);
		result.setRmaNo(rma_no);
		result.setBillingNo(billing_no);
		result.setHsoNo(hso_no);
		result.setStatus(status);
		result.setComments(comments);
		result.setProviderActivity(provider_activity);
		result.setPractitionerNo(practitionerNo);
		result.setInit(init);
		result.setJobTitle(job_title);
		result.setEmail(email);
		result.setTitle(title);
		result.setLastUpdateUser(lastUpdateUser);
		result.setLastUpdateDate(ConversionUtils.fromDateString(lastUpdateDate));
		result.setSignedConfidentiality(ConversionUtils.fromDateString(signed_confidentiality));
		return result;
	}

	private void fromProvider(org.oscarehr.common.model.ProviderData p) {
		provider_no = p.getId();
		last_name = p.getLastName();
		first_name = p.getFirstName();
		provider_type = p.getProviderType();
		specialty = p.getSpecialty();
		team = p.getTeam();
		sex = p.getSex();
		dob = ConversionUtils.toDateString(p.getDob());
		address = p.getAddress();
		phone = p.getPhone();
		work_phone = p.getWorkPhone();
		ohip_no = p.getOhipNo();
		rma_no = p.getRmaNo();
		billing_no = p.getBillingNo();
		hso_no = p.getHsoNo();
		status = p.getStatus();
		comments = p.getComments();
		provider_activity = p.getProviderActivity();
		practitionerNo = p.getPractitionerNo();
		init = p.getInit();
		job_title = p.getJobTitle();
		email = p.getEmail();
		title = p.getTitle();
		lastUpdateUser = p.getLastUpdateUser();
		lastUpdateDate = ConversionUtils.toDateString(p.getLastUpdateDate());
		signed_confidentiality = ConversionUtils.toDateString(p.getSignedConfidentiality());
	}

	public List<String> getProviderListWithInsuranceNo(String insurerNo) {
		ProviderDataDao dao = SpringUtils.getBean(ProviderDataDao.class);
		List<org.oscarehr.common.model.ProviderData> providers = dao.findByTypeAndOhip(PROVIDER_TYPE_DOCTOR, insurerNo);

		List<String> result = new ArrayList<String>();
		for (org.oscarehr.common.model.ProviderData p : providers) {
			result.add(p.getId());
		}
		return result;
	}

	public void getProvider(String providerNo) {
		ProviderDataDao dao = SpringUtils.getBean(ProviderDataDao.class);
		org.oscarehr.common.model.ProviderData provider = dao.findByProviderNo(providerNo);
		if (provider != null) {
			fromProvider(provider);
		}
	}

	/**
	 * Getter for property provider_no.
	 * @return Value of property provider_no.
	 */
	public java.lang.String getProviderNo() {
		return provider_no;
	}

	/**
	 * Setter for property provider_no.
	 * @param provider_no New value of property provider_no.
	 */
	public void setProviderNo(java.lang.String provider_no) {
		this.provider_no = provider_no;
	}

	/**
	 * Getter for property last_name.
	 * @return Value of property last_name.
	 */
	public java.lang.String getLast_name() {
		return last_name;
	}

	/**
	 * Setter for property last_name.
	 * @param last_name New value of property last_name.
	 */
	public void setLast_name(java.lang.String last_name) {
		this.last_name = last_name;
	}

	/**
	 * Getter for property first_name.
	 * @return Value of property first_name.
	 */
	public java.lang.String getFirst_name() {
		return first_name;
	}

	/**
	 * Setter for property first_name.
	 * @param first_name New value of property first_name.
	 */
	public void setFirst_name(java.lang.String first_name) {
		this.first_name = first_name;
	}

	/**
	 * Getter for property provider_type.
	 * @return Value of property provider_type.
	 */
	public java.lang.String getProvider_type() {
		return provider_type;
	}

	/**
	 * Setter for property provider_type.
	 * @param provider_type New value of property provider_type.
	 */
	public void setProvider_type(java.lang.String provider_type) {
		this.provider_type = provider_type;
	}

	/**
	 * Getter for property specialty.
	 * @return Value of property specialty.
	 */
	public java.lang.String getSpecialty() {
		return specialty;
	}

	/**
	 * Setter for property specialty.
	 * @param specialty New value of property specialty.
	 */
	public void setSpecialty(java.lang.String specialty) {
		this.specialty = specialty;
	}

	/**
	 * Getter for property team.
	 * @return Value of property team.
	 */
	public java.lang.String getTeam() {
		return team;
	}

	/**
	 * Setter for property team.
	 * @param team New value of property team.
	 */
	public void setTeam(java.lang.String team) {
		this.team = team;
	}

	/**
	 * Getter for property sex.
	 * @return Value of property sex.
	 */
	public java.lang.String getSex() {
		return sex;
	}

	/**
	 * Setter for property sex.
	 * @param sex New value of property sex.
	 */
	public void setSex(java.lang.String sex) {
		this.sex = sex;
	}

	/**
	 * Getter for property dob.
	 * @return Value of property dob.
	 */
	public java.lang.String getDob() {
		return dob;
	}

	/**
	 * Setter for property dob.
	 * @param dob New value of property dob.
	 */
	public void setDob(java.lang.String dob) {
		this.dob = dob;
	}

	/**
	 * Getter for property address.
	 * @return Value of property address.
	 */
	public java.lang.String getAddress() {
		return address;
	}

	/**
	 * Setter for property address.
	 * @param address New value of property address.
	 */
	public void setAddress(java.lang.String address) {
		this.address = address;
	}

	/**
	 * Getter for property phone.
	 * @return Value of property phone.
	 */
	public java.lang.String getPhone() {
		return phone;
	}

	/**
	 * Setter for property phone.
	 * @param phone New value of property phone.
	 */
	public void setPhone(java.lang.String phone) {
		this.phone = phone;
	}

	/**
	 * Getter for property work_phone.
	 * @return Value of property work_phone.
	 */
	public java.lang.String getWork_phone() {
		return work_phone;
	}

	/**
	 * Setter for property work_phone.
	 * @param work_phone New value of property work_phone.
	 */
	public void setWork_phone(java.lang.String work_phone) {
		this.work_phone = work_phone;
	}

	/**
	 * Getter for property ohip_no.
	 * @return Value of property ohip_no.
	 */
	public java.lang.String getOhip_no() {
		return ohip_no;
	}

	/**
	 * Setter for property ohip_no.
	 * @param ohip_no New value of property ohip_no.
	 */
	public void setOhip_no(java.lang.String ohip_no) {
		this.ohip_no = ohip_no;
	}

	/**
	 * Getter for property rma_no.
	 * @return Value of property rma_no.
	 */
	public java.lang.String getRma_no() {
		return rma_no;
	}

	/**
	 * Setter for property rma_no.
	 * @param rma_no New value of property rma_no.
	 */
	public void setRma_no(java.lang.String rma_no) {
		this.rma_no = rma_no;
	}

	/**
	 * Getter for property billing_no.
	 * @return Value of property billing_no.
	 */
	public java.lang.String getBilling_no() {
		return billing_no;
	}

	/**
	 * Setter for property billing_no.
	 * @param billing_no New value of property billing_no.
	 */
	public void setBilling_no(java.lang.String billing_no) {
		this.billing_no = billing_no;
	}

	/**
	 * Getter for property hso_no.
	 * @return Value of property hso_no.
	 */
	public java.lang.String getHso_no() {
		return hso_no;
	}

	/**
	 * Setter for property hso_no.
	 * @param hso_no New value of property hso_no.
	 */
	public void setHso_no(java.lang.String hso_no) {
		this.hso_no = hso_no;
	}

	/**
	 * Getter for property status.
	 * @return Value of property status.
	 */
	public java.lang.String getStatus() {
		return status;
	}

	/**
	 * Setter for property status.
	 * @param status New value of property status.
	 */
	public void setStatus(java.lang.String status) {
		this.status = status;
	}

	/**
	 * Getter for property comments.
	 * @return Value of property comments.
	 */
	public java.lang.String getComments() {
		return comments;
	}

	/**
	 * Setter for property comments.
	 * @param comments New value of property comments.
	 */
	public void setComments(java.lang.String comments) {
		this.comments = comments;
	}

	/**
	 * Getter for property provider_activity.
	 * @return Value of property provider_activity.
	 */
	public java.lang.String getProvider_activity() {
		return provider_activity;
	}

	/**
	 * Setter for property provider_activity.
	 * @param provider_activity New value of property provider_activity.
	 */
	public void setProvider_activity(java.lang.String provider_activity) {
		this.provider_activity = provider_activity;
	}

	/**
	* getters && setters
	**/

	public java.lang.String getPractitionerNo() {
		return practitionerNo;
	}

	public void setPractitionerNo(java.lang.String practitionerNo) {
		this.practitionerNo = practitionerNo;
	}

	public java.lang.String getInit() {
		return init;
	}

	public void setInit(java.lang.String init) {
		this.init = init;
	}

	public java.lang.String getJob_title() {
		return job_title;
	}

	public void setJob_title(java.lang.String job_title) {
		this.job_title = job_title;
	}

	public java.lang.String getEmail() {
		return email;
	}

	public void setEmail(java.lang.String email) {
		this.email = email;
	}

	public java.lang.String getTitle() {
		return title;
	}

	public void setTitle(java.lang.String title) {
		this.title = title;
	}

	public java.lang.String getLastUpdateUser() {
		return lastUpdateUser;
	}

	public void setLastUpdateUser(java.lang.String lastUpdateUser) {
		this.lastUpdateUser = lastUpdateUser;
	}

	public java.lang.String getLastUpdateDate() {
		return lastUpdateDate;
	}

	public void setLastUpdateDate(java.lang.String lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}

	public java.lang.String getSigned_confidentiality() {
		return signed_confidentiality;
	}

	public void setSigned_confidentiality(java.lang.String signed_confidentiality) {
		this.signed_confidentiality = signed_confidentiality;
	}

	public static List<Map<String, String>> getProviderList() {
		ProviderDataDao dao = SpringUtils.getBean(ProviderDataDao.class);
		List<org.oscarehr.common.model.ProviderData> providers = dao.findByType(PROVIDER_TYPE_DOCTOR);

		List<Map<String, String>> result = new ArrayList<Map<String, String>>();
		for (org.oscarehr.common.model.ProviderData p : providers) {
			result.add(toMap(p));
		}
		return result;

	}

	private static Map<String, String> toMap(org.oscarehr.common.model.ProviderData p) {
	    Map<String, String> result = new HashMap<String, String>();
	    result.put("providerNo", p.getId());
	    result.put("firstName", p.getFirstName());
	    result.put("lastName", p.getLastName());
	    result.put("ohipNo", p.getOhipNo());
	    return result;
    }

	public static List<Map<String, String>>  searchProvider(String searchStr) {
		return searchProvider(searchStr, false);
	}

	public static List<Map<String, String>> searchProvider(String searchStr, boolean onlyActive) {
		String firstname = null;
		String lastname = null;
		if (searchStr.indexOf(",") != -1) {
			String[] array = new String[2];
			array = searchStr.split(",");
			lastname = array[0].trim();
			firstname = array[1].trim();
		} else {
			lastname = searchStr.trim();
		}
		
		ProviderDataDao dao = SpringUtils.getBean(ProviderDataDao.class);
		List<org.oscarehr.common.model.ProviderData> providers = dao.findByName(firstname, lastname, onlyActive);
		
		List<Map<String, String>> result = new ArrayList<Map<String,String>>();
		
		for(org.oscarehr.common.model.ProviderData p : providers) {
			result.add(toMap(p));
		}
		return result;
	}

	public static List<Map<String, String>> getProviderListOfAllTypes(boolean inactive) {
		ProviderDataDao dao = SpringUtils.getBean(ProviderDataDao.class);
		List<Map<String, String>> result = new ArrayList<Map<String,String>>();
		
		List<org.oscarehr.common.model.ProviderData> providers = dao.findAll(inactive);
		for(org.oscarehr.common.model.ProviderData p : providers) {
			result.add(toMap(p));
		}
		return result;
	}

	public static List<Map<String, String>> getProviderListOfAllTypes() {
		return getProviderListOfAllTypes(false);
	}

	public static String getProviderName(String providerNo) {
		ProviderDataDao dao = SpringUtils.getBean(ProviderDataDao.class);
		org.oscarehr.common.model.ProviderData p = dao.findByProviderNo(providerNo);
		if (p == null)
			return "";
		
		return p.getFirstName() + " " + p.getLastName();
	}

	public String getMyOscarId() {
		if (myOscarId == null) this.initMyOscarId();
		return myOscarId;
	}

	public void initMyOscarId() {
		this.myOscarId = ProviderMyOscarIdData.getMyOscarId(this.getProviderNo());
	}

	private String myOscarId = null;

	public String getDefaultBillingView(String providerNo) {

		ProviderPreferenceDao providerPreferenceDao = (ProviderPreferenceDao) SpringUtils.getBean("providerPreferenceDao");
		ProviderPreference providerPreference = providerPreferenceDao.find(providerNo);

		if (providerPreference != null) return (providerPreference.getDefaultServiceType());
		else return (null);
	}

	public void getProviderWithOHIP(String ohipNo) {
		if (filled(ohipNo)) {
			ProviderDataDao dao = SpringUtils.getBean(ProviderDataDao.class);
			org.oscarehr.common.model.ProviderData p = dao.findByOhipNumber(ohipNo);
			if (p != null)
				fromProvider(p);			
		}
	}

	public void getProviderWithNames(String firstName, String lastName) {
		if (filled(firstName) && filled(lastName)) {
			ProviderDataDao dao = SpringUtils.getBean(ProviderDataDao.class);
			List<org.oscarehr.common.model.ProviderData> providers = dao.findByName(firstName, lastName, false);
			if (providers.isEmpty()) {
				return;
			}
			fromProvider(providers.get(0));
		}
	}

	public void getExternalProviderWithNames(String firstName, String lastName) {
		if (filled(firstName) && filled(lastName)) {
			ProviderDataDao dao = SpringUtils.getBean(ProviderDataDao.class);
			List<org.oscarehr.common.model.ProviderData> providers = dao.findByName(firstName, lastName, false);
			if (providers.isEmpty()) {
				return;
			}
			
			for(org.oscarehr.common.model.ProviderData p : providers) {
				if (ConversionUtils.fromIntString(p.getId()) < EXTERNAL_PROVIDER_ID_CONSTRAINT) {
					fromProvider(p);
					return;
				}
			}
		}
	}

	public void addExternalProvider(String firstName, String lastName, String ohipNo, String cpsoNo) {
		if (!filled(firstName) && !filled(lastName) && !filled(ohipNo)) return; //no information at all!

		ProviderDataDao dao = SpringUtils.getBean(ProviderDataDao.class);
		Integer lastPN = dao.getLastId();

		// assign new external provider no
		if (lastPN != 0) {
			lastPN = lastPN < EXTERNAL_PROVIDER_ID_CONSTRAINT ? lastPN - 1 : -1001;
			this.provider_no = String.valueOf(lastPN);
		} else {
			this.provider_no = "-1001";
		}

		//create new external provider
		this.first_name = filled(firstName) ? firstName : "";
		this.last_name = filled(lastName) ? lastName : "";
		this.ohip_no = filled(ohipNo) ? ohipNo : "";
		this.practitionerNo = filled(cpsoNo) ? cpsoNo : "";
		this.provider_type = PROVIDER_TYPE_DOCTOR;
		this.status = "1";
		this.specialty = "";
		this.sex = "";
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		this.lastUpdateDate = sdf.format(new Date());
		
		dao.persist(toProvider());
	}

	boolean filled(String s) {
		return (s != null && !s.trim().equals(""));
	}
}
