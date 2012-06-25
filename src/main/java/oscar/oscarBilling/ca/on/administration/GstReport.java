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

/*
 * GstReport.java
 *
 * Created on August 15, 2007, 5:20 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package oscar.oscarBilling.ca.on.administration;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Vector;

import oscar.oscarDB.DBHandler;

/**
 *
 * @author andrewleung
 */
public class GstReport {
    

    
    public Vector getGST(String providerNo, String startDate, String endDate) throws SQLException{
        
        Properties props;
        Vector billno = new Vector();
        Vector list = new Vector();
        // First find all the billing_no referreing to the selected provider_no.
        String sql = "SELECT billing_no from billing_on_ext where key_val = 'provider_no' AND value ='" + providerNo + "';";
        ResultSet rs1 = DBHandler.GetSQL(sql);
        // Place all the billing_no in a list
        while (rs1.next()){
            billno.add(rs1.getString("billing_no"));
        }
        rs1.close();
        // For every bill the provider is involved with, search the gst value, date, demo no within the chosen dates
        for (int i = 0; i < billno.size(); i++){
            sql = "SELECT value, LEFT(date_time, 10) as date_time, demographic_no from billing_on_ext where billing_no = '" + (String)billno.get(i) + "' AND key_val = 'gst' AND date_time >= '" + startDate + "' AND date_time <= '" + endDate + "';";
            rs1 = DBHandler.GetSQL(sql);
            ResultSet rs2;
            while (rs1.next()){
                props = new Properties();
                props.setProperty("gst", rs1.getString("value"));
                props.setProperty("date", rs1.getString("date_time"));
                props.setProperty("demographic_no", rs1.getString("demographic_no"));
                sql = "select CONCAT(first_name, ' ', last_name) as name from demographic where demographic_no = '" + props.getProperty("demographic_no", "") + "';"; 
                rs2 = DBHandler.GetSQL(sql);
                if( rs2.next() ){
                    props.setProperty("name", rs2.getString("name"));
                }
                rs2.close();
                sql = "SELECT value from billing_on_ext where billing_no = '" + (String)billno.get(i) + "' AND key_val = 'total' AND date_time > '" + startDate + "' AND date_time < '" + endDate + "';";
                rs2 = DBHandler.GetSQL(sql);
                if( rs2.next() ){
                    props.setProperty("total", rs2.getString("value"));
                }
                rs2.close();
                list.add(props);
            }
            rs1.close();
        }
        return list;
    }
    
    public String getGstFlag(String code, String date) throws SQLException{
        
        String sql = "SELECT b.gstFlag from billingservice b where b.service_code ='" + code + "' and b.billingservice_date = (select max(b2.billingservice_date) from billingservice b2 where b2.service_code ='" + code + "' and b2.billingservice_date <= '" + date + "');";
        ResultSet rs = DBHandler.GetSQL(sql);
        String ret = "";
        if( rs.next() ){
            ret = rs.getString("gstFlag");
        }
        return ret;
    }
}
