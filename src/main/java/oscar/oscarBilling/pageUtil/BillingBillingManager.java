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


package oscar.oscarBilling.pageUtil;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.oscarehr.util.MiscUtils;

import oscar.oscarDB.DBHandler;


public class BillingBillingManager {

    public BillingItem[] getBillingItem(String[] service, String service1, String service2, String service3, String service1unit, String service2unit, String service3unit) {
        BillingItem[] arr ={};


        try {
            String service_code;
            double units;
            ArrayList<BillingItem> lst = new ArrayList<BillingItem>();
            BillingItem billingitem;

            lst = getDups2(service,service1,service2,service3,service1unit,service2unit,service3unit);

            for (int i=0; i< lst.size(); i++) {
                BillingItem bi = lst.get(i);
                service_code = bi.service_code;
                units = bi.units;




                ResultSet rs;
                String sql = "SELECT b.service_code, b.description , b.value, b.percentage "
                           + "FROM billingservice b WHERE b.service_code='" + service_code + "'";

                rs = DBHandler.GetSQL(sql);

                while(rs.next()) {
                    billingitem = new BillingItem(rs.getString("service_code"), rs.getString("description"),
                    rs.getString("value"), rs.getString("percentage"), units);
                    lst.add(billingitem);
                }

                rs.close();

            }

            arr = lst.toArray(arr);

        } catch (SQLException e) {
            MiscUtils.getLogger().error("Error", e);
        }


        return arr;
    }



    public ArrayList<BillingItem> getBillView(String billing_no) {

        ArrayList<BillingItem> billingItemsArray = new ArrayList<BillingItem>();

        try{

            ResultSet rs;
            //oscar.oscarBilling.pageUtil.BillingBillingManager bbm = new oscar.oscarBilling.pageUtil.BillingBillingManager();

            String sql;
            sql = "select billingmaster_no, billing_no, createdate, billingstatus,demographic_no, appointment_no, claimcode, datacenter, payee_no, practitioner_no, phn, name_verify, dependent_num,billing_unit,";
            sql = sql + "clarification_code, anatomical_area, after_hour, new_program, billing_code, bill_amount, payment_mode, service_date, service_to_day, submission_code, extended_submission_code, dx_code1, dx_code2, dx_code3, ";
            sql = sql + "dx_expansion, service_location, referral_flag1, referral_no1, referral_flag2, referral_no2, time_call, service_start_time, service_end_time, birth_date, office_number, correspondence_code, claim_comment ";
            sql = sql + "from billingmaster where billing_no='" + billing_no+"'";
            MiscUtils.getLogger().debug(sql);
            rs = DBHandler.GetSQL(sql);

            while(rs.next()){
                BillingItem billingItem = new BillingItem(rs.getString("billing_code"),rs.getString("billing_unit"));
                billingItem.fill();
                billingItemsArray.add(billingItem);

            }

            rs.close();

        } catch (SQLException e){ MiscUtils.getLogger().error("Error", e);  }

        return billingItemsArray;

    }
    public ArrayList<BillingItem> getDups(String[] service, String service1, String service2, String service3, String service1unit, String service2unit, String service3unit) {
        ArrayList<String> lst = new ArrayList<String>();
        for (int i =0 ; i < service.length ; i++){
            lst.add(service[i]);
        }
        if (service1.compareTo("") != 0){
            if (service1unit.compareTo("")==0){
                service1unit = "1";
            }
            for (int j=0; j < Integer.parseInt(service1unit); j++){
                lst.add(service1);
            }
        }
        if (service2.compareTo("") != 0){
            if (service2unit.compareTo("")==0){
                service2unit = "1";
            }
            for (int k=0; k < Integer.parseInt(service2unit); k++){
                lst.add(service2);
            }
        }
        if (service3.compareTo("") != 0){
            if (service3unit.compareTo("")==0){
                service3unit = "1";
            }
            for (int l=0; l < Integer.parseInt(service3unit); l++){
                lst.add(service3);
            }
        }

        ArrayList<BillingItem> billingItemsArray = new ArrayList<BillingItem>();
        for (int i = 0;i < lst.size() ;i++ ){
            String code = lst.get(i);
            BillingItem b = isBilled(billingItemsArray,code);
            if(b == null){
                BillingItem billingItem = new BillingItem(code,"1");
                billingItem.fill();
                billingItemsArray.add(billingItem);
            }else{
                b.addUnits(1);
            }
        }

        return billingItemsArray;

    }

    private BillingItem isBilled(ArrayList<BillingItem> ar,String code){
        for (int i = 0; i < ar.size() ;i++ ){
            BillingItem bi = ar.get(i);
            if (bi.service_code.equals(code)) {
                return bi;
            }
        }
        return null;
    }


    public ArrayList<BillingItem> getDups2(String[] service, String service1, String service2, String service3, String service1unit, String service2unit, String service3unit) {
        ArrayList<BillingItem> billingItemsArray = new ArrayList<BillingItem>();
        for (int i =0 ; i < service.length ; i++){
            addBillItem(billingItemsArray,service[i],"1");
        }
        if (service1.compareTo("") != 0){
            if (service1unit.compareTo("")==0){
                service1unit = "1";
            }
            //int numUnit = Integer.parseInt(service1unit);
            addBillItem(billingItemsArray,service1,service1unit);

        }
        if (service2.compareTo("") != 0){
            if (service2unit.compareTo("")==0){
                service2unit = "1";
            }
            //int numUnit = Integer.parseInt(service2unit);
            addBillItem(billingItemsArray,service2,service2unit);

        }
        if (service3.compareTo("") != 0){
            if (service3unit.compareTo("")==0){
                service3unit = "1";
            }
            //int numUnit = Integer.parseInt(service3unit);
            addBillItem(billingItemsArray,service3,service3unit);
        }



        return billingItemsArray;

    }


    private BillingItem addBillItem(ArrayList<BillingItem> ar,String code,String serviceUnits){
        boolean newCode = true;
        BillingItem bi = null;
        for (int i = 0; i < ar.size() ;i++ ){
            bi = ar.get(i);
            if (bi.service_code.equals(code)) {
                newCode = false;
                bi.addUnits(serviceUnits);
                i = ar.size();
            }
        }
        if(newCode){
            bi = new BillingItem(code,serviceUnits);
            bi.fill();
            ar.add(bi);
        }
        return bi;
    }


    public String getGrandTotal(ArrayList<BillingItem> ar){
        double grandtotal = 0.00;
        for (int i=0; i< ar.size(); i++){
            BillingItem bi = ar.get(i);
            grandtotal += bi.getLineTotal();
            MiscUtils.getLogger().debug("total:" + grandtotal);
        }
        BigDecimal bdFee = new BigDecimal(grandtotal).setScale(2, BigDecimal.ROUND_HALF_UP);
        return bdFee.toString();

    }


    public class BillingItem {

        String service_code;
        String description;
        double price;
        double percentage  ;
        double units;
        double lineTotal;

        public BillingItem(String service_code, String description, String price1, String percentage1, double units1) {
            this.service_code= service_code;
            this.description = description;
            this.price = Double.parseDouble(price1);
            this.percentage = Double.parseDouble(percentage1);
            this.units = units1;
        }

        public BillingItem(String service_code, String units1) {
            this.service_code= service_code;
            this.units = Double.parseDouble(units1);
        }
        public BillingItem(String service_code, int units1) {
            this.service_code= service_code;
            this.units = units1;
        }

        public void addUnits(int num){
            units += num;
        }

        public void addUnits(String num) {
            units += (num.compareTo("") != 0) ? Double.parseDouble(num) : 0;
        }

        public String getServiceCode() {
            return service_code;
        }
        public String getDescription() {
            return description;
        }

        public double getUnit() {
            return units;
        }

        public String getDispPrice(){
            BigDecimal bdFee = new BigDecimal(price).setScale(2, BigDecimal.ROUND_HALF_UP);
            return bdFee.toString();
        }
        public double getPrice() {
            return price * units;
        }
        public double getPercentage() {
            return percentage;
        }

        public void fill(){
            try{

                ResultSet rs;
                String sql;

                // SELECT b.service_code, b.description , b.value, b.percentage FROM billingservice b, ctl_billingservice c WHERE b.service_code=c.service_code and b.region='BC' and c.service_group='Group1';

                sql = "SELECT b.service_code, b.description , b.value, b.percentage "
                + "FROM billingservice b WHERE b.service_code='" + service_code + "'";
                MiscUtils.getLogger().debug(sql);
                rs = DBHandler.GetSQL(sql);

                while(rs.next()){
                    this.description =  rs.getString("description");
                    this.price =  Double.parseDouble(rs.getString("value"));
                    try{
                        this.percentage = Double.parseDouble(rs.getString("percentage"));
                    }catch (NumberFormatException eNum) { this.percentage = 100; }
                }

                rs.close();

            } catch (SQLException e){ MiscUtils.getLogger().error("Error", e);  }

        }
        public double getLineTotal() {

            this.lineTotal = price * units;
            return lineTotal;
        }
        public String getDispLineTotal(){
            BigDecimal bdFee = new BigDecimal(lineTotal).setScale(2, BigDecimal.ROUND_HALF_UP);
            return bdFee.toString();
        }


    }
}
