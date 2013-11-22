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

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.oscarehr.common.dao.AppointmentArchiveDao;
import org.oscarehr.common.dao.ClinicLocationDao;
import org.oscarehr.common.dao.OscarAppointmentDao;
import org.oscarehr.common.dao.ProfessionalSpecialistDao;
import org.oscarehr.common.model.Appointment;
import org.oscarehr.common.model.ClinicLocation;
import org.oscarehr.common.model.ProfessionalSpecialist;
import org.oscarehr.util.SpringUtils;

import oscar.SxmlMisc;
import oscar.service.OscarSuperManager;

public class JdbcBillingPageUtil {
	private static final Logger _logger = Logger.getLogger(JdbcBillingPageUtil.class);
	BillingONDataHelp dbObj = new BillingONDataHelp();
	AppointmentArchiveDao appointmentArchiveDao = (AppointmentArchiveDao)SpringUtils.getBean("appointmentArchiveDao");
    OscarAppointmentDao appointmentDao = (OscarAppointmentDao)SpringUtils.getBean("oscarAppointmentDao");
    ProfessionalSpecialistDao professionalSpecialistDao = (ProfessionalSpecialistDao) SpringUtils.getBean("professionalSpecialistDao");
    ClinicLocationDao clinicLocationDao = (ClinicLocationDao) SpringUtils.getBean("clinicLocationDao");

	public List getCurTeamProviderStr(String provider_no) {
		List retval = new Vector();
		String sql = "select provider_no,last_name,first_name,ohip_no,comments from provider "
				+ "where status='1' and ohip_no!='' and (provider_no='"+provider_no+"' or team=(select team from provider where provider_no='"+provider_no+"')) order by last_name, first_name";
		String proid = "";
		String proFirst = "";
		String proLast = "";
		String proOHIP = "";
		String specialty_code;
		String billinggroup_no;
		ResultSet rslocal = dbObj.searchDBRecord(sql);
		try {
			while (rslocal.next()) {
				proid = rslocal.getString("provider_no");
				proLast = rslocal.getString("last_name");
				proFirst = rslocal.getString("first_name");
				proOHIP = rslocal.getString("ohip_no");
				billinggroup_no = getXMLStringWithDefault(rslocal.getString("comments"), "xml_p_billinggroup_no",
						"0000");
				specialty_code = getXMLStringWithDefault(rslocal.getString("comments"), "xml_p_specialty_code", "00");
				retval.add(proid + "|" + proLast + "|" + proFirst + "|" + proOHIP + "|" + billinggroup_no + "|"
						+ specialty_code);
			}
		} catch (SQLException e) {
			_logger.error("getCurProviderStr(sql = " + sql + ")");
		}

		return retval;
	}

	public List getCurSiteProviderStr(String provider_no) {
		List retval = new Vector();
		String sql = "select provider_no,last_name,first_name,ohip_no,comments from provider p "
				+ "where status='1' and ohip_no!='' " +
						"and exists(select * from providersite s where p.provider_no = s.provider_no and s.site_id IN (SELECT site_id from providersite where provider_no="+provider_no+"))" +
						" order by last_name, first_name";
		String proid = "";
		String proFirst = "";
		String proLast = "";
		String proOHIP = "";
		String specialty_code;
		String billinggroup_no;
		ResultSet rslocal = dbObj.searchDBRecord(sql);
		try {
			while (rslocal.next()) {
				proid = rslocal.getString("provider_no");
				proLast = rslocal.getString("last_name");
				proFirst = rslocal.getString("first_name");
				proOHIP = rslocal.getString("ohip_no");
				billinggroup_no = getXMLStringWithDefault(rslocal.getString("comments"), "xml_p_billinggroup_no",
						"0000");
				specialty_code = getXMLStringWithDefault(rslocal.getString("comments"), "xml_p_specialty_code", "00");
				retval.add(proid + "|" + proLast + "|" + proFirst + "|" + proOHIP + "|" + billinggroup_no + "|"
						+ specialty_code);
			}
		} catch (SQLException e) {
			_logger.error("getCurProviderStr(sql = " + sql + ")");
		}

		return retval;
	}

	public List getCurProviderStr() {
		List retval = new Vector();
		String sql = "select provider_no,last_name,first_name,ohip_no,comments from provider "
				+ "where status='1' and ohip_no!='' order by last_name, first_name";
		String proid = "";
		String proFirst = "";
		String proLast = "";
		String proOHIP = "";
		String specialty_code;
		String billinggroup_no;
		ResultSet rslocal = dbObj.searchDBRecord(sql);
		try {
			while (rslocal.next()) {
				proid = rslocal.getString("provider_no");
				proLast = rslocal.getString("last_name");
				proFirst = rslocal.getString("first_name");
				proOHIP = rslocal.getString("ohip_no");
				billinggroup_no = getXMLStringWithDefault(rslocal.getString("comments"), "xml_p_billinggroup_no",
						"0000");
				specialty_code = getXMLStringWithDefault(rslocal.getString("comments"), "xml_p_specialty_code", "00");
				retval.add(proid + "|" + proLast + "|" + proFirst + "|" + proOHIP + "|" + billinggroup_no + "|"
						+ specialty_code);
			}
		} catch (SQLException e) {
			_logger.error("getCurProviderStr(sql = " + sql + ")");
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
		String sql = "select provider_no,ohip_no from provider "
				+ "where status='1' and ohip_no!='' order by last_name, first_name";
		String proid = "";
		String proOHIP = "";
		ResultSet rslocal = dbObj.searchDBRecord(sql);
		try {
			while (rslocal.next()) {
				proid = rslocal.getString("provider_no");
				proOHIP = rslocal.getString("ohip_no");
				retval.setProperty(proid, proOHIP);
			}
		} catch (SQLException e) {
			_logger.error("getPropProviderOHIP(sql = " + sql + ")");
		}
		return retval;
	}

	public Properties getPropProviderName() {
		Properties retval = new Properties();
		
		String sql = "select provider_no,last_name,first_name from provider "
				+ "where status='1' order by last_name, first_name";
		
		String proid = "";
		String proName = "";
		ResultSet rslocal = dbObj.searchDBRecord(sql);
		try {
			while (rslocal.next()) {
				proid = rslocal.getString("provider_no");
				proName = rslocal.getString("last_name") + "," + rslocal.getString("first_name");
				retval.setProperty(proid, proName);
			}
		} catch (SQLException e) {
			_logger.error("getPropProviderName(sql = " + sql + ")");
		}
		return retval;
	}

	public BillingProviderData getProviderObj(String providerNo) {
		BillingProviderData pObj = null;
		String sql = "select provider_no,last_name,first_name,ohip_no,rma_no,comments from provider ";
                sql += "where status='1'";
                if(!providerNo.equals("all")) sql += " and provider_no='" + providerNo + "'";
		_logger.info("getProviderObj(sql = " + sql + ")");
		ResultSet rslocal = dbObj.searchDBRecord(sql);
		String specialty_code;
		String billinggroup_no;
		try {
			while (rslocal.next()) {
				pObj = new BillingProviderData();
				billinggroup_no = getXMLStringWithDefault(rslocal.getString("comments"), "xml_p_billinggroup_no",
						"0000");
				specialty_code = getXMLStringWithDefault(rslocal.getString("comments"), "xml_p_specialty_code", "00");
				pObj.setProviderNo(rslocal.getString("provider_no"));
				pObj.setLastName(rslocal.getString("last_name"));
				pObj.setFirstName(rslocal.getString("first_name"));
				pObj.setOhipNo(rslocal.getString("ohip_no"));
				pObj.setRmaNo(rslocal.getString("rma_no"));
				pObj.setSpecialtyCode(specialty_code);
				pObj.setBillingGroupNo(billinggroup_no);
			}
		} catch (SQLException e) {
			_logger.error("getProviderObj(sql = " + sql + ")");
		}
		return pObj;
	}

        public List<BillingProviderData> getProviderObjList(String providerNo) {
		BillingProviderData pObj = null;
		List<BillingProviderData> res = new ArrayList<BillingProviderData>();
		String sql = "select provider_no,last_name,first_name,ohip_no,rma_no,comments from provider ";
		sql += "where status='1'";
		if(!providerNo.equals("all")) sql += " and provider_no='" + providerNo + "'";
		_logger.info("getProviderObj(sql = " + sql + ")");
		ResultSet rslocal = dbObj.searchDBRecord(sql);
		String specialty_code;
		String billinggroup_no;
		try {
			while (rslocal.next()) {
				pObj = new BillingProviderData();
				billinggroup_no = getXMLStringWithDefault(rslocal.getString("comments"), "xml_p_billinggroup_no",
						"0000");
				specialty_code = getXMLStringWithDefault(rslocal.getString("comments"), "xml_p_specialty_code", "00");
				pObj.setProviderNo(rslocal.getString("provider_no"));
				pObj.setLastName(rslocal.getString("last_name"));
				pObj.setFirstName(rslocal.getString("first_name"));
				pObj.setOhipNo(rslocal.getString("ohip_no"));
				pObj.setRmaNo(rslocal.getString("rma_no"));
				pObj.setSpecialtyCode(specialty_code);
				pObj.setBillingGroupNo(billinggroup_no);
				res.add(pObj);
			}
		} catch (SQLException e) {
			_logger.error("getProviderObj(sql = " + sql + ")");
		}
		return res;
	}

	public List getProvider(String diskId) {
		List retval = new Vector();
		String providerNo = null;
		String sql = "select providerno from billing_on_filename where disk_id=" + diskId;
		_logger.info("getProvider(sql = " + sql + ")");
		ResultSet rs = dbObj.searchDBRecord(sql);
		try {
			while (rs.next()) {
				providerNo = rs.getString("providerno");

				sql = "select provider_no,last_name,first_name,ohip_no,comments from provider " + "where provider_no='"
						+ providerNo + "' and status='1' and ohip_no!='' order by last_name, first_name";
				String specialty_code;
				String billinggroup_no;
				ResultSet rslocal = dbObj.searchDBRecord(sql);
				while (rslocal.next()) {
					billinggroup_no = getXMLStringWithDefault(rslocal.getString("comments"), "xml_p_billinggroup_no",
							"0000");
					specialty_code = getXMLStringWithDefault(rslocal.getString("comments"), "xml_p_specialty_code",
							"00");

					BillingProviderData pObj = new BillingProviderData();
					pObj.setProviderNo(rslocal.getString("provider_no"));
					pObj.setLastName(rslocal.getString("last_name"));
					pObj.setFirstName(rslocal.getString("first_name"));
					pObj.setOhipNo(rslocal.getString("ohip_no"));
					pObj.setSpecialtyCode(specialty_code);
					pObj.setBillingGroupNo(billinggroup_no);
					retval.add(pObj);
				}
			}
		} catch (SQLException e) {
			_logger.error("getProvider(sql = " + sql + ")");
		}
		return retval;
	}

	public List getCurSoloProvider() {
		List retval = new Vector();
		String sql = "select provider_no,last_name,first_name,ohip_no,comments from provider "
				+ "where status='1' and ohip_no!='' order by last_name, first_name";
		String specialty_code;
		String billinggroup_no;
		ResultSet rslocal = dbObj.searchDBRecord(sql);
		try {
			while (rslocal.next()) {
				billinggroup_no = getXMLStringWithDefault(rslocal.getString("comments"), "xml_p_billinggroup_no",
						"0000");
				specialty_code = getXMLStringWithDefault(rslocal.getString("comments"), "xml_p_specialty_code", "00");
				if (!"0000".equals(billinggroup_no))
					continue;
				BillingProviderData pObj = new BillingProviderData();
				pObj.setProviderNo(rslocal.getString("provider_no"));
				pObj.setLastName(rslocal.getString("last_name"));
				pObj.setFirstName(rslocal.getString("first_name"));
				pObj.setOhipNo(rslocal.getString("ohip_no"));
				pObj.setSpecialtyCode(specialty_code);
				pObj.setBillingGroupNo(billinggroup_no);
				retval.add(pObj);
			}
		} catch (SQLException e) {
			_logger.error("getCurSoloProvider(sql = " + sql + ")");
		}

		return retval;
	}

	public List getCurGrpProvider() {
		List retval = new Vector();
		String sql = "select provider_no,last_name,first_name,ohip_no,comments from provider "
				+ "where status='1' and ohip_no!='' order by last_name, first_name";
		String specialty_code;
		String billinggroup_no;
		ResultSet rslocal = dbObj.searchDBRecord(sql);
		try {
			while (rslocal.next()) {
				billinggroup_no = getXMLStringWithDefault(rslocal.getString("comments"), "xml_p_billinggroup_no",
						"0000");
				specialty_code = getXMLStringWithDefault(rslocal.getString("comments"), "xml_p_specialty_code", "00");
				if ("0000".equals(billinggroup_no))
					continue;
				BillingProviderData pObj = new BillingProviderData();
				pObj.setProviderNo(rslocal.getString("provider_no"));
				pObj.setLastName(rslocal.getString("last_name"));
				pObj.setFirstName(rslocal.getString("first_name"));
				pObj.setOhipNo(rslocal.getString("ohip_no"));
				pObj.setSpecialtyCode(specialty_code);
				pObj.setBillingGroupNo(billinggroup_no);
				retval.add(pObj);
			}
		} catch (SQLException e) {
			_logger.error("getCurGrpProvider(sql = " + sql + ")");
		}

		return retval;
	}

	public boolean updateApptStatus(String apptNo, String status, String userNo) {
                OscarSuperManager oscarSuperManager = (OscarSuperManager)SpringUtils.getBean("oscarSuperManager");
                Appointment appt = appointmentDao.find(Integer.parseInt(apptNo));
                appointmentArchiveDao.archiveAppointment(appt);
                int rowsAffected = oscarSuperManager.update("appointmentDao", "updatestatusc", new Object[]{status,userNo,apptNo});
                return (rowsAffected==1);
	}

	public String getApptStatus(String apptNo) {
		String retval = "T";
		String sql = "select status from appointment where appointment_no=" + apptNo + " ";
		ResultSet rslocal = dbObj.searchDBRecord(sql);
		try {
			while (rslocal.next()) {
				retval = rslocal.getString("status");
			}
		} catch (SQLException e) {
			_logger.error("getApptStatus(sql = " + sql + ")");
		}
		return retval;
	}

	// last_name,first_name,dob,hin,ver,hc_type,sex,family_doctor,provider_no,roster_status
	public List getPatientCurBillingDemographic(String demoNo) {
		List retval = null;
//		String sql = "select last_name,first_name,concat(year_of_birth,month_of_birth,date_of_birth) as dob,hin,ver,hc_type,sex, "
//				+ "family_doctor,provider_no,roster_status from demographic where demographic_no=" + demoNo + " ";
		String sql = "select last_name,first_name,concat(concat(year_of_birth,month_of_birth),date_of_birth) as dob,hin,ver,hc_type,sex, "
			+ "family_doctor,provider_no,roster_status from demographic where demographic_no=" + demoNo + " ";
		ResultSet rslocal = dbObj.searchDBRecord(sql);
		try {
			while (rslocal.next()) {
				retval = new Vector();
				retval.add(rslocal.getString("last_name"));
				retval.add(rslocal.getString("first_name"));
				retval.add(rslocal.getString("dob"));
				retval.add(rslocal.getString("hin") == null ? "" : rslocal.getString("hin"));
				retval.add(rslocal.getString("ver") == null ? "" : rslocal.getString("ver"));
				retval.add(rslocal.getString("hc_type") == null ? "" : rslocal.getString("hc_type"));
				retval.add(rslocal.getString("sex").startsWith("F") ? "2" : "1");
				retval.add(rslocal.getString("family_doctor") == null ? "" : rslocal.getString("family_doctor"));
				retval.add(rslocal.getString("provider_no") == null ? "" : rslocal.getString("provider_no"));
				retval.add(rslocal.getString("roster_status") == null ? "" : rslocal.getString("roster_status"));
			}
		} catch (SQLException e) {
			_logger.error("getPatientCurBillingDemographic(sql = " + sql + ")");
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

	// last_name,first_name,dob,hin,ver,hc_type,sex,family_doctor,provider_no
	public List getPatientCurBillingDemo(String demoNo) {
		List retval = null;
		String sql = "select last_name,first_name,concat(year_of_birth,month_of_birth,date_of_birth) as dob,hin,ver,hc_type,sex, "
				+ "family_doctor,provider_no from demographic where demographic_no=" + demoNo + " ";
		ResultSet rslocal = dbObj.searchDBRecord(sql);
		try {
			while (rslocal.next()) {
				retval = new Vector();
				retval.add(rslocal.getString("last_name"));
				retval.add(rslocal.getString("first_name"));
				retval.add(rslocal.getString("dob"));
				retval.add(rslocal.getString("hin") == null ? "" : rslocal.getString("hin"));
				retval.add(rslocal.getString("ver") == null ? "" : rslocal.getString("ver"));
				retval.add(rslocal.getString("hc_type") == null ? "" : rslocal.getString("hc_type"));
				retval.add(rslocal.getString("sex").startsWith("F") ? "2" : "1");
				retval.add(rslocal.getString("family_doctor") == null ? "" : rslocal.getString("family_doctor"));
				retval.add(rslocal.getString("provider_no") == null ? "" : rslocal.getString("provider_no"));
			}
		} catch (SQLException e) {
			_logger.error("getPatientCurBillingDemo(sql = " + sql + ")");
		}
		return retval;
	}

	// name : code|dx|
	public List getBillingFavouriteList() {
		List retval = new Vector();
		String sql = "select * from billing_on_favourite where deleted=false order by name ";
		ResultSet rslocal = dbObj.searchDBRecord(sql);
		try {
			while (rslocal.next()) {
				retval.add(rslocal.getString("name"));
				retval.add(rslocal.getString("service_dx"));
			}
		} catch (SQLException e) {
			_logger.error("getBillingFavouriteList(sql = " + sql + ")");
		}
		return retval;
	}

	public List getBillingFavouriteOne(String name) {
		List retval = new Vector();
		String sql = "select * from billing_on_favourite where name='" + name + "' and deleted=false";
		ResultSet rslocal = dbObj.searchDBRecord(sql);
		try {
			while (rslocal.next()) {
				retval.add(rslocal.getString("name"));
				retval.add(rslocal.getString("service_dx"));
			}
		} catch (SQLException e) {
			_logger.error("getBillingFavouriteOne(sql = " + sql + ")");
		}
		return retval;
	}

	public int addBillingFavouriteList(String name, String list, String providerNo) {
		int retval = 0;
		String sql = "insert into billing_on_favourite values(\\N, '" + name + "', '" + list + "', '" + providerNo
				+ "', \\N, 0)";
		retval = dbObj.saveBillingRecord(sql);
		if (retval == 0) {
			_logger.error("addBillingFavouriteList(sql = " + sql + ")");
		}
		return retval;
	}

//	 @ OSCARSERVICE
	public boolean delBillingFavouriteList(String name, String providerNo) {
		boolean retval = true;
		String sql = "update billing_on_favourite set deleted=true where name='" + name + "' and provider_no='" + providerNo + "'";
		retval = dbObj.updateDBRecord(sql);
		if (!retval) {
			_logger.error("delBillingFavouriteList(sql = " + sql + ")");
		}
		return retval;
	}
	// @ OSCARSERVICE

	public boolean updateBillingFavouriteList(String name, String list, String providerNo) {
		boolean retval = false;
		String sql = "update billing_on_favourite set service_dx='" + list + "', provider_no='" + providerNo
				+ "' where name='" + name + "'";
		retval = dbObj.updateDBRecord(sql);
		if (!retval) {
			_logger.error("updateBillingFavouriteList(sql = " + sql + ")");
		}
		return retval;
	}

	public List getPaymentType() {
		List retval = new Vector();
		String sql = "select * from billing_payment_type order by id ";
		ResultSet rslocal = dbObj.searchDBRecord(sql);
		try {
			while (rslocal.next()) {
				retval.add("" + rslocal.getInt("id"));
				retval.add(rslocal.getString("payment_type"));
			}
		} catch (SQLException e) {
			_logger.error("getPaymentType(sql = " + sql + ")");
		}
		return retval;
	}

	public List getFacilty_num() {
		List retval = new Vector();
		List<ClinicLocation> clinicLocations = clinicLocationDao.findByClinicNo(1);
		for(ClinicLocation clinicLocation:clinicLocations) {
			retval.add(clinicLocation.getClinicLocationNo());
			retval.add(clinicLocation.getClinicLocationName());
		}

		return retval;
	}

}
