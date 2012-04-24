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


package oscar.oscarBilling.ca.on.data;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Hashtable;

import org.apache.commons.lang.StringEscapeUtils;
import org.oscarehr.util.MiscUtils;

import oscar.oscarDB.DBHandler;

/**
 *
 * @author jay
 */
public class RAData {

    /** Creates a new instance of RAData */
    public RAData() {
    }

    // select * from radetail limit 100,10;
    // radetail_no | raheader_no | providerohip_no | billing_no | service_code |
    // service_count | hin | amountclaim | amountpay | service_date | error_code
    // | billtype |
    public ArrayList<Hashtable<String,String>> getRAData(String billingNo) {
        ArrayList<Hashtable<String,String>> list = new ArrayList<Hashtable<String,String>>();
        String sql = "Select * from radetail where billing_no = '" + StringEscapeUtils.escapeSql(billingNo) + "'";
        try {

            ResultSet rs = DBHandler.GetSQL(sql);
            while (rs.next()) {
                Hashtable<String,String> h = new Hashtable<String,String>();
                h.put("radetail_no", rs.getString("radetail_no"));
                h.put("raheader_no", rs.getString("raheader_no"));
                h.put("providerohip_no", rs.getString("providerohip_no"));
                h.put("billing_no", rs.getString("billing_no"));
                h.put("service_code", rs.getString("service_code"));
                h.put("service_count", rs.getString("service_count"));
                h.put("hin", rs.getString("hin"));
                h.put("amountclaim", rs.getString("amountclaim"));
                h.put("amountpay", rs.getString("amountpay"));
                h.put("service_date", rs.getString("service_date"));
                h.put("error_code", rs.getString("error_code"));
                h.put("billtype", rs.getString("billtype"));
                list.add(h);
            }
            rs.close();
        } catch (Exception e) {
            MiscUtils.getLogger().error("Error", e);
        }
        return list;
    }

    public ArrayList<Hashtable<String,String>> getRADataIntern(String billingNo, String service_date, String ohip_no) {
        ArrayList<Hashtable<String,String>> list = new ArrayList<Hashtable<String,String>>();
        String sql = "Select * from radetail where billing_no = '" + StringEscapeUtils.escapeSql(billingNo) + "'";
        sql += " and service_date='" + service_date + "' and providerohip_no='" + ohip_no + "'";
        try {

            ResultSet rs = DBHandler.GetSQL(sql);
            while (rs.next()) {
            	Hashtable<String,String> h = new Hashtable<String,String>();
                h.put("radetail_no", rs.getString("radetail_no"));
                h.put("raheader_no", rs.getString("raheader_no"));
                h.put("providerohip_no", rs.getString("providerohip_no"));
                h.put("billing_no", rs.getString("billing_no"));
                h.put("service_code", rs.getString("service_code"));
                h.put("service_count", rs.getString("service_count"));
                h.put("hin", rs.getString("hin"));
                h.put("amountclaim", rs.getString("amountclaim"));
                h.put("amountpay", rs.getString("amountpay"));
                h.put("service_date", rs.getString("service_date"));
                h.put("error_code", rs.getString("error_code"));
                h.put("billtype", rs.getString("billtype"));
                list.add(h);
            }
            rs.close();
        } catch (Exception e) {
            MiscUtils.getLogger().error("Error", e);
        }
        return list;
    }

    public String getErrorCodes(ArrayList a) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < a.size(); i++) {
            Hashtable h = (Hashtable) a.get(i);
            sb.append(h.get("error_code"));
            sb.append(" ");
        }
        return sb.toString();
    }

    public String getAmountPaid(ArrayList a) {
        BigDecimal total = new BigDecimal("0.00").setScale(2, BigDecimal.ROUND_HALF_UP);
        for (int i = 0; i < a.size(); i++) {
            Hashtable h = (Hashtable) a.get(i);
            BigDecimal valueToAdd = new BigDecimal("0.00");
            try {
                String amount = "" + h.get("amountpay");
                amount = amount.trim();
                valueToAdd = new BigDecimal(amount).setScale(2, BigDecimal.ROUND_HALF_UP);
            } catch (Exception badValueException) {
                MiscUtils.getLogger().debug(" Error calculating value for " + h.get("billing_no"));
                MiscUtils.getLogger().error("Error", badValueException);
            }
            total = total.add(valueToAdd);
        }
        return total.toString();
    }

    public String getAmountPaid(ArrayList a, String billingNo, String serviceCode) {
        BigDecimal total = new BigDecimal("0.00").setScale(2, BigDecimal.ROUND_HALF_UP);
        for (int i = 0; i < a.size(); i++) {
            Hashtable h = (Hashtable) a.get(i);
            if (!(h.get("billing_no").equals(billingNo)) || !(h.get("service_code").equals(serviceCode))) {
                continue;
            }
            BigDecimal valueToAdd = new BigDecimal("0.00");
            try {
                String amount = "" + h.get("amountpay");
                amount = amount.trim();
                valueToAdd = new BigDecimal(amount).setScale(2, BigDecimal.ROUND_HALF_UP);
            } catch (Exception badValueException) {
                MiscUtils.getLogger().debug(" Error calculating value for " + h.get("billing_no"));
                MiscUtils.getLogger().error("Error", badValueException);
            }
            total = total.add(valueToAdd);
        }
        return total.toString();
    }

    public boolean isErrorCode(String billingNo, String errorCode) {
        boolean ret = false;
        String sql = "Select error_code from radetail where billing_no = '" + StringEscapeUtils.escapeSql(billingNo) + "'";
        try {

            ResultSet rs = DBHandler.GetSQL(sql);
            while (rs.next()) {
                if (errorCode.equals(rs.getString("error_code"))) {
                    ret = true;
                    break;
                }
            }
            rs.close();
        } catch (Exception e) {
            MiscUtils.getLogger().error("Error", e);
        }
        return ret;
    }
}
