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

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.oscarehr.common.dao.DiagnosticCodeDao;
import org.oscarehr.common.model.DiagnosticCode;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.oscarDB.DBHandler;



public class BillingFormData {

	private DiagnosticCodeDao diagnosticCodeDao = SpringUtils.getBean(DiagnosticCodeDao.class);

    public String getBillingFormDesc(BillingForm[] billformlist,String billForm){
        for (int i = 0; i < billformlist.length; i++){
             if(billformlist[i].getFormCode().equals( billForm)){
                  return billformlist[i].getDescription();
             }
        }

        return "";
    }

    public BillingService[] getServiceList(String serviceGroup, String serviceType, String billRegion) {
        BillingService[] arr ={};

        try {

            ArrayList<BillingService> lst = new ArrayList<BillingService>();
            BillingService billingservice;


            ResultSet rs;
            String sql;


            // SELECT b.service_code, b.description , b.value, b.percentage FROM billingservice b, ctl_billingservice c WHERE b.service_code=c.service_code and b.region='BC' and c.service_group='Group1';

            sql = "SELECT b.service_code, b.description , b.value, b.percentage "
            + "FROM billingservice b, ctl_billingservice c WHERE b.service_code="
            + "c.service_code and b.region='" + billRegion +"' and c.service_group='"
            + serviceGroup + "' and c.servicetype='" + serviceType + "' order by c.service_order";

            rs = DBHandler.GetSQL(sql);

            while(rs.next()) {
                billingservice = new BillingService(rs.getString("service_code"), rs.getString("description"),
                rs.getString("value"), rs.getString("percentage"));
                lst.add(billingservice);
            }

            rs.close();
            arr = lst.toArray(arr);

        } catch (SQLException e) {
            MiscUtils.getLogger().error("Error", e);
        }


        return arr;
    }


    public Diagnostic[] getDiagnosticList(String serviceType, String billRegion) {
        Diagnostic[] arr ={};

        try {

            ArrayList<Diagnostic> lst = new ArrayList<Diagnostic>();
            Diagnostic diagnostic;


            ResultSet rs;
            String sql;


            // SELECT b.service_code, b.description , b.value, b.percentage FROM billingservice b, ctl_billingservice c WHERE b.service_code=c.service_code and b.region='BC' and c.service_group='Group1';

            sql = "SELECT d.diagnostic_code, d.description FROM diagnosticcode d, "
            + "ctl_diagcode c WHERE d.diagnostic_code=c.diagnostic_code and "
            + "d.region='" + billRegion +"' and c.servicetype='" + serviceType + "'";

            MiscUtils.getLogger().debug("getDiagnosticList "+sql);
            rs = DBHandler.GetSQL(sql);

            while(rs.next()) {
                diagnostic = new Diagnostic(rs.getString("diagnostic_code"), rs.getString("description"));
                lst.add(diagnostic);
            }

            rs.close();
            arr = lst.toArray(arr);

        } catch (SQLException e) {
            MiscUtils.getLogger().error("Error", e);
        }


        return arr;
    }



    public Location[] getLocationList(String billRegion) {
        Location[] arr ={};

        try {

            ArrayList<Location> lst = new ArrayList<Location>();
            Location location;


            ResultSet rs;
            String sql;


            // SELECT b.service_code, b.description , b.value, b.percentage FROM billingservice b, ctl_billingservice c WHERE b.service_code=c.service_code and b.region='BC' and c.service_group='Group1';

            sql = "SELECT billinglocation,billinglocation_desc FROM billinglocation "
            + " WHERE region='" + billRegion +"'";

            MiscUtils.getLogger().debug(" getLocationList "+sql);
            rs = DBHandler.GetSQL(sql);

            while(rs.next()) {
                location = new Location(rs.getString("billinglocation"), rs.getString("billinglocation_desc"));
                lst.add(location);
            }

            rs.close();
            arr = lst.toArray(arr);

        } catch (SQLException e) {
            MiscUtils.getLogger().error("Error", e);
        }


        return arr;
    }



    public BillingVisit[] getVisitType(String billRegion) {
        BillingVisit[] arr ={};

        try {

            ArrayList<BillingVisit> lst = new ArrayList<BillingVisit>();
            BillingVisit billingvisit;


            ResultSet rs;
            String sql;


            // SELECT b.service_code, b.description , b.value, b.percentage FROM billingservice b, ctl_billingservice c WHERE b.service_code=c.service_code and b.region='BC' and c.service_group='Group1';

            sql = "SELECT visittype,visit_desc FROM billingvisit "
            + " WHERE region='" + billRegion +"'";
            MiscUtils.getLogger().debug("getVisitType"+sql);

            rs = DBHandler.GetSQL(sql);

            while(rs.next()) {
                billingvisit = new BillingVisit(rs.getString("visittype"), rs.getString("visit_desc"));
                lst.add(billingvisit);
            }

            rs.close();
            arr = lst.toArray(arr);

        } catch (SQLException e) {
            MiscUtils.getLogger().error("Error", e);
        }


        return arr;
    }



    public BillingPhysician[] getProviderList() {
        BillingPhysician[] arr ={};

        try {

            ArrayList<BillingPhysician> lst = new ArrayList<BillingPhysician>();
            BillingPhysician billingphysician;


            ResultSet rs;
            String sql;


            // SELECT p.last_name, p.first_name, p.provider_no FROM provider p WHERE p.ohip_no <>''

            sql = "SELECT p.last_name, p.first_name, p.provider_no FROM provider p "
            + " WHERE p.ohip_no <>''";
            MiscUtils.getLogger().debug("getProviderList "+sql);

            rs = DBHandler.GetSQL(sql);

            while(rs.next()) {
                billingphysician = new BillingPhysician(rs.getString("last_name")+", "+rs.getString("first_name"), rs.getString("provider_no"));
                lst.add(billingphysician);
            }

            rs.close();
            arr = lst.toArray(arr);

        } catch (SQLException e) {
            MiscUtils.getLogger().error("Error", e);
        }


        return arr;
    }





    public BillingForm[] getFormList() {
        BillingForm[] arr ={};

        try {

            ArrayList<BillingForm> lst = new ArrayList<BillingForm>();
            BillingForm billingForm;


            ResultSet rs;
            String sql;


            // SELECT b.service_code, b.description , b.value, b.percentage FROM BillingForm b, ctl_BillingForm c WHERE b.service_code=c.service_code and b.region='BC' and c.service_group='Group1';

            sql = "select servicetype_name, servicetype from ctl_billingservice "
            + "group by servicetype, servicetype_name";

            MiscUtils.getLogger().debug("getFormList "+sql);
            rs = DBHandler.GetSQL(sql);

            while(rs.next()) {
                billingForm = new BillingForm(rs.getString("servicetype_name"), rs.getString("servicetype"));
                lst.add(billingForm);
            }

            rs.close();
            arr = lst.toArray(arr);

        } catch (SQLException e) {
            MiscUtils.getLogger().error("Error", e);
        }


        return arr;
    }






    public class BillingService {

        String service_code;
        String description;
        String price;
        String percentage  ;

        public BillingService(String service_code, String description, String price, String percentage) {
            this.service_code= service_code;
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
            this.diagnostic_code=diagnostic_code;
            this.description=description;


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
            this.billinglocation=billinglocation;
            this.description=description;


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
            this.billingvisit=billingvisit;
            this.description=description;


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
            this.formcode=formcode;
            this.description=description;


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
            this.providername=providername;
            this.provider_no=provider_no;


        }

        public String getProviderName() {
            return providername;
        }

        public String getProviderNo() {
            return provider_no;
        }

    }


    public String getProviderName(String provider_no) {
        String provider_n="";
        try{


            ResultSet rs;
            String sql;


            // SELECT b.service_code, b.description , b.value, b.percentage FROM BillingForm b, ctl_BillingForm c WHERE b.service_code=c.service_code and b.region='BC' and c.service_group='Group1';

            sql = "SELECT last_name, first_name from provider where provider_no='" + provider_no + "'";


            rs = DBHandler.GetSQL(sql);
            MiscUtils.getLogger().debug("getProviderName "+sql);
            while(rs.next()) {

                provider_n = rs.getString("last_name")+", " + rs.getString("first_name");
            }
            rs.close();


        } catch (SQLException e) {
            MiscUtils.getLogger().error("Error", e);
        }
        return provider_n;

    }


    public String getPracNo(String provider_no){

        String prac_no="";
        try{


            ResultSet rs;
            String sql;


            // SELECT b.service_code, b.description , b.value, b.percentage FROM BillingForm b, ctl_BillingForm c WHERE b.service_code=c.service_code and b.region='BC' and c.service_group='Group1';

            sql = "SELECT ohip_no from provider where provider_no='" + provider_no + "'";


            rs = DBHandler.GetSQL(sql);
            MiscUtils.getLogger().debug("getPracNo "+sql);

            while(rs.next()) {

                prac_no = rs.getString("ohip_no");
            }
            rs.close();


        } catch (SQLException e) {
            MiscUtils.getLogger().error("Error", e);
        }
        return prac_no;


    }

    public String getGroupNo(String provider_no){

        String prac_no="";
        try{


            ResultSet rs;
            String sql;


            // SELECT b.service_code, b.description , b.value, b.percentage FROM BillingForm b, ctl_BillingForm c WHERE b.service_code=c.service_code and b.region='BC' and c.service_group='Group1';

            sql = "SELECT billing_no from provider where provider_no='" + provider_no + "'";

            MiscUtils.getLogger().debug("getGroupNo "+sql);
            rs = DBHandler.GetSQL(sql);

            while(rs.next()) {

                prac_no = rs.getString("billing_no");
            }
            rs.close();


        } catch (SQLException e) {
            MiscUtils.getLogger().error("Error", e);
        }
        return prac_no;


    }



    public String getDiagDesc(String dx, String reg){
        String dxdesc="";
        List<DiagnosticCode> dcodes = diagnosticCodeDao.findByDiagnosticCodeAndRegion(dx, reg);
        for(DiagnosticCode dcode:dcodes) {
        	dxdesc = dcode.getDescription();
        }
        return dxdesc;
    }


    public String getServiceDesc(String code, String reg){
        String codeDesc="";
        try{

            ResultSet rs;
            String sql;
            sql = "select description from billingservice where service_code = '"+code+"' and region = '"+reg+"' ";
            rs = DBHandler.GetSQL(sql);
            while(rs.next()) {
                codeDesc = rs.getString("description");
            }
            rs.close();
        } catch (SQLException e) {
            MiscUtils.getLogger().error("Error", e);
        }
        return codeDesc;
    }




    public String getServiceGroupName(String serviceGroup) {
        String ret = "";

        try {

            ResultSet rs;
            String sql = "SELECT service_group_name FROM ctl_billingservice WHERE service_group='"
            + serviceGroup +"'";

            rs = DBHandler.GetSQL(sql);

            if(rs.next()) {
                ret = rs.getString("service_group_name");
            }

            rs.close();
        } catch (SQLException e) {
            MiscUtils.getLogger().error("Error", e);
        }

        return ret;
    }


}
