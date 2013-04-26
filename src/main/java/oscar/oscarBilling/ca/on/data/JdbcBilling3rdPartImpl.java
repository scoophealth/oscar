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
import java.util.List;
import java.util.Properties;
import java.util.Vector;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.Logger;
import org.oscarehr.common.dao.ClinicDAO;
import org.oscarehr.common.model.Clinic;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.oscarDB.DBHandler;
import oscar.util.UtilDateUtilities;


public class JdbcBilling3rdPartImpl {
	private static final Logger _logger = Logger
			.getLogger(JdbcBilling3rdPartImpl.class);
	private ClinicDAO clinicDao = (ClinicDAO)SpringUtils.getBean("clinicDAO");

	public static final String ACTIVE = "1";
	public static final String INACTIVE = "0";

	BillingONDataHelp dbObj = new BillingONDataHelp();

	public Properties get3rdPartBillProp(String invNo) {
		Properties retval = new Properties();
		String sql = "select * from billing_on_ext where billing_no=" + invNo + " and status = '" + ACTIVE + "'";
		ResultSet rs = dbObj.searchDBRecord(sql);

		try {
			while (rs.next()) {
				retval.setProperty(rs.getString("key_val"), rs
						.getString("value"));
			}
		} catch (SQLException e) {
			_logger.error("get3rdPartBillProp(sql = " + sql + ")");
		}
		return retval;
	}
	
	public Properties get3rdPartBillPropInactive(String invNo) {
		Properties retval = new Properties();
		String sql = "select * from billing_on_ext where billing_no=" + invNo + " and status = '" + INACTIVE + "'";
		ResultSet rs = dbObj.searchDBRecord(sql);

		try {
			while (rs.next()) {
				retval.setProperty(rs.getString("key_val"), rs
						.getString("value"));
			}
		} catch (SQLException e) {
			_logger.error("get3rdPartBillProp(sql = " + sql + ")");
		}
		return retval;
	}

	public Properties getLocalClinicAddr() {
		Properties retval = new Properties();

		Clinic clinic = clinicDao.getClinic();
		if(clinic != null) {
			retval.setProperty("clinic_name", clinic.getClinicName());
			retval.setProperty("clinic_address", clinic.getClinicAddress());
			retval.setProperty("clinic_city", clinic.getClinicCity());
			retval.setProperty("clinic_province", clinic.getClinicProvince());
            retval.setProperty("clinic_postal", clinic.getClinicPostal());
			retval.setProperty("clinic_fax", clinic.getClinicFax());
			retval.setProperty("clinic_phone", clinic.getClinicPhone());
			retval.setProperty("clinic_fax", clinic.getClinicFax());
		}

		return retval;
	}

	public Properties get3rdPayMethod() {
		Properties retval = new Properties();
		String sql = "select * from billing_payment_type";
		ResultSet rs = dbObj.searchDBRecord(sql);

		try {
			while (rs.next()) {
				retval.setProperty(("" + rs.getInt("id")), rs
						.getString("payment_type"));
			}
		} catch (SQLException e) {
			_logger.error("get3rdPayMethod(sql = " + sql + ")");
		}
		return retval;
	}

	// 3rd bill ins. address
	public int addOne3rdAddrRecord(Properties val) {
		int retval = 0;
		String sql = "insert into billing_on_3rdPartyAddress values(\\N, "
				+ " '"
				+ StringEscapeUtils.escapeSql(val.getProperty("attention", ""))
				+ "' ,'"
				+ StringEscapeUtils.escapeSql(val.getProperty("company_name",
						"")) + "'," + "'"
				+ StringEscapeUtils.escapeSql(val.getProperty("address", ""))
				+ "'," + "'"
				+ StringEscapeUtils.escapeSql(val.getProperty("city", ""))
				+ "'," + "'"
				+ StringEscapeUtils.escapeSql(val.getProperty("province", ""))
				+ "'," + "'"
				+ StringEscapeUtils.escapeSql(val.getProperty("postcode", ""))
				+ "'," + "'"
				+ StringEscapeUtils.escapeSql(val.getProperty("telephone", ""))
				+ "'," + "'"
				+ StringEscapeUtils.escapeSql(val.getProperty("fax", ""))
				+ "')";
		_logger.info("addOne3rdAddrRecord(sql = " + sql + ")");
		retval = dbObj.saveBillingRecord(sql);

		if (retval == 0) {
			_logger.error("addOne3rdAddrRecord(sql = " + sql + ")");
		}
		return retval;
	}

	public boolean update3rdAddr(String id, Properties val) {
		String sql = "update billing_on_3rdPartyAddress set attention='"
				+ StringEscapeUtils.escapeSql(val.getProperty("attention", ""))
				+ "', company_name='"
				+ StringEscapeUtils.escapeSql(val.getProperty("company_name",
						"")) + "', address='"
				+ StringEscapeUtils.escapeSql(val.getProperty("address", ""))
				+ "', city='"
				+ StringEscapeUtils.escapeSql(val.getProperty("city", ""))
				+ "', province='"
				+ StringEscapeUtils.escapeSql(val.getProperty("province", ""))
				+ "', postcode='" + val.getProperty("postcode", "")
				+ "', telephone='" + val.getProperty("telephone", "")
				+ "', fax='" + val.getProperty("fax", "") + "' where id="
				+ val.getProperty("id", "");
		boolean retval = dbObj.updateDBRecord(sql);

		if (!retval) {
			_logger.error("update3rdAddr(sql = " + sql + ")");
		}
		return retval;
	}

        public boolean add3rdBillExt(String billingNo, String demoNo, String key, String value) {
		boolean retval = false;

		String dateTime = UtilDateUtilities.getToday("yyyy-MM-dd HH:mm:ss");

		String sql = "insert into billing_on_ext values(\\N, " + billingNo + "," + demoNo + ", '" + key + "', '"
					+ value + "', '" + dateTime + "', '" + ACTIVE + "' )";
                retval = dbObj.updateDBRecord(sql);
                if (!retval) {
                        _logger.error("add3rdBillExt(sql = " + sql + ")");
                        return retval;
                }

		return retval;
	}

        public boolean keyExists(String billingNo, String key) {
            boolean ret = false;

            String sql = "select billing_no from billing_on_ext where billing_no="
                    + billingNo + " and key_val = '" + StringEscapeUtils.escapeSql(key) + "'";

            ResultSet rs = dbObj.searchDBRecord(sql);
            try {
                if( rs.next() ) {
                    ret = true;
                }
            }
            catch( SQLException e ) {
                MiscUtils.getLogger().error("Error", e);
            }

            return ret;
        }
        
    public boolean updateKeyStatus(String billingNo, String key, String status) {
		String sql = "update billing_on_ext set status = '" + status + "' where billing_no="
				+ billingNo + " and key_val='"
				+ StringEscapeUtils.escapeSql(key) + "'";
		boolean retval = dbObj.updateDBRecord(sql);

		if (!retval) {
			_logger.error("updateKeyValue(sql = " + sql + ")");
		}
		return retval;
	}

    /*
     * We're updating a key--make sure it is active as well
     */
	public boolean updateKeyValue(String billingNo, String key, String value) {
		String sql = "update billing_on_ext set value='"
				+ StringEscapeUtils.escapeSql(value) + "', status = '" + ACTIVE + "' where billing_no="
				+ billingNo + " and key_val='"
				+ StringEscapeUtils.escapeSql(key) + "'";
		boolean retval = dbObj.updateDBRecord(sql);

		if (!retval) {
			_logger.error("updateKeyValue(sql = " + sql + ")");
		}
		return retval;
	}

	public List get3rdAddrNameList() {
		List ret = new Vector();
		String sql = "select id, company_name from billing_on_3rdPartyAddress order by company_name ";
		ResultSet rsdemo = dbObj.searchDBRecord(sql);
		try {
			while (rsdemo.next()) {
				String id = rsdemo.getString("id");
				// String attention = rsdemo.getString("attention");
				String company_name = rsdemo.getString("company_name");
				Properties prop = new Properties();
				prop.setProperty("id", id);
				// prop.setProperty("attention", attention);
				prop.setProperty("company_name", company_name);
				ret.add(prop);
			}
		} catch (SQLException e) {
			_logger.error("get3rdAddrNameList(sql = " + sql + ")");
		}
		return ret;
	}

	public List get3rdAddrList(String keyword, String field) {
		List ret = new Vector();
		String sql = "select * from billing_on_3rdPartyAddress where " + field
				+ " like '" + keyword + "%' order by attention, company_name";
		ResultSet rsdemo = dbObj.searchDBRecord(sql);
		try {
			while (rsdemo.next()) {
				String id = rsdemo.getString("id");
				String attention = rsdemo.getString("attention");
				String company_name = rsdemo.getString("company_name");
				String address = rsdemo.getString("address");
				String city = rsdemo.getString("city");
				String province = rsdemo.getString("province");
				String postcode = rsdemo.getString("postcode");
				String telephone = rsdemo.getString("telephone");
				String fax = rsdemo.getString("fax");
				Properties prop = new Properties();
				prop.setProperty("id", id);
				prop.setProperty("attention", attention);
				prop.setProperty("company_name", company_name);
				prop.setProperty("address", address);
				prop.setProperty("city", city);
				prop.setProperty("province", province);
				prop.setProperty("postcode", postcode);
				prop.setProperty("telephone", telephone);
				prop.setProperty("fax", fax);
				ret.add(prop);
			}
		} catch (SQLException e) {
			_logger.error("get3rdAddrList(sql = " + sql + ")");
		}
		return ret;
	}

	public Properties get3rdAddr(String id) {
		Properties prop = new Properties();
		String sql = "select * from billing_on_3rdPartyAddress where id=" + id;
		ResultSet rsdemo = dbObj.searchDBRecord(sql);
		try {
			while (rsdemo.next()) {
				String attention = rsdemo.getString("attention");
				String company_name = rsdemo.getString("company_name");
				String address = rsdemo.getString("address");
				String city = rsdemo.getString("city");
				String province = rsdemo.getString("province");
				String postcode = rsdemo.getString("postcode");
				String telephone = rsdemo.getString("telephone");
				String fax = rsdemo.getString("fax");
				// Properties prop = new Properties();
				prop.setProperty("id", id);
				prop.setProperty("attention", attention);
				prop.setProperty("company_name", company_name);
				prop.setProperty("address", address);
				prop.setProperty("city", city);
				prop.setProperty("province", province);
				prop.setProperty("postcode", postcode);
				prop.setProperty("telephone", telephone);
				prop.setProperty("fax", fax);
			}
		} catch (SQLException e) {
			_logger.error("get3rdAddr(sql = " + sql + ")");
		}
		return prop;
	}

	public Properties get3rdAddrProp(String name) {
		Properties prop = new Properties();
		String sql = "select * from billing_on_3rdPartyAddress where company_name = '" + StringEscapeUtils.escapeSql(name) + "'";
		ResultSet rsdemo = dbObj.searchDBRecord(sql);
		try {
			while (rsdemo.next()) {
				String id = "" + rsdemo.getInt("id");
				String attention = rsdemo.getString("attention");
				String company_name = rsdemo.getString("company_name");
				String address = rsdemo.getString("address");
				String city = rsdemo.getString("city");
				String province = rsdemo.getString("province");
				String postcode = rsdemo.getString("postcode");
				String telephone = rsdemo.getString("telephone");
				String fax = rsdemo.getString("fax");
				// Properties prop = new Properties();
				prop.setProperty("id", id);
				prop.setProperty("attention", attention);
				prop.setProperty("company_name", company_name);
				prop.setProperty("address", address);
				prop.setProperty("city", city);
				prop.setProperty("province", province);
				prop.setProperty("postcode", postcode);
				prop.setProperty("telephone", telephone);
				prop.setProperty("fax", fax);
			}
		} catch (SQLException e) {
			_logger.error("get3rdAddrProp(sql = " + sql + ")");
		}
		return prop;
	}
        public Properties getGstTotal(String invNo) throws SQLException{
            String sql = "SELECT value from billing_on_ext where key_val = 'gst' AND billing_no = '" + invNo + "';";
            _logger.info("getGstTotal(sql= " + sql + ")");

            ResultSet rs = DBHandler.GetSQL(sql);
            Properties props = new Properties();
            if (rs.next()){
                props.setProperty("gst", rs.getString("value"));
            }
            return props;
        }
}
