package oscar.oscarBilling.data;

import java.util.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import oscar.oscarDB.DBHandler;


public class BillingFormData {
    
    
    
    public BillingService[] getServiceList(String serviceGroup, String serviceType, String billRegion) {
        BillingService[] arr ={};
        
        try {
            
            ArrayList lst = new ArrayList();
            BillingService billingservice;
            
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
            ResultSet rs;
            String sql;
            
            
            // SELECT b.service_code, b.description , b.value, b.percentage FROM billingservice b, ctl_billingservice c WHERE b.service_code=c.service_code and b.region='BC' and c.service_group='Group1';
            
            sql = "SELECT b.service_code, b.description , b.value, b.percentage "
            + "FROM billingservice b, ctl_billingservice c WHERE b.service_code="
            + "c.service_code and b.region='" + billRegion +"' and c.service_group='"
            + serviceGroup + "' and c.servicetype='" + serviceType + "'";
            
            System.out.println("getServiceList "+sql);
            rs = db.GetSQL(sql);
            
            while(rs.next()) {
                billingservice = new BillingService(rs.getString("service_code"), rs.getString("description"),
                rs.getString("value"), rs.getString("percentage"));
                lst.add(billingservice);
            }
            
            rs.close();
            db.CloseConn();
            
            arr = (BillingService[])lst.toArray(arr);
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        
        
        return arr;
    }
    
    
    public Diagnostic[] getDiagnosticList(String serviceType, String billRegion) {
        Diagnostic[] arr ={};
        
        try {
            
            ArrayList lst = new ArrayList();
            Diagnostic diagnostic;
            
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
            ResultSet rs;
            String sql;
            
            
            // SELECT b.service_code, b.description , b.value, b.percentage FROM billingservice b, ctl_billingservice c WHERE b.service_code=c.service_code and b.region='BC' and c.service_group='Group1';
            
            sql = "SELECT d.diagnostic_code, d.description FROM diagnosticcode d, "
            + "ctl_diagcode c WHERE d.diagnostic_code=c.diagnostic_code and "
            + "d.region='" + billRegion +"' and c.servicetype='" + serviceType + "'";
            
            System.out.println("getDiagnosticList "+sql);
            rs = db.GetSQL(sql);
            
            while(rs.next()) {
                diagnostic = new Diagnostic(rs.getString("diagnostic_code"), rs.getString("description"));
                lst.add(diagnostic);
            }
            
            rs.close();
            db.CloseConn();
            
            arr = (Diagnostic[])lst.toArray(arr);
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        
        
        return arr;
    }
    
    
    
    public Location[] getLocationList(String billRegion) {
        Location[] arr ={};
        
        try {
            
            ArrayList lst = new ArrayList();
            Location location;
            
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
            ResultSet rs;
            String sql;
            
            
            // SELECT b.service_code, b.description , b.value, b.percentage FROM billingservice b, ctl_billingservice c WHERE b.service_code=c.service_code and b.region='BC' and c.service_group='Group1';
            
            sql = "SELECT billinglocation,billinglocation_desc FROM billinglocation "
            + " WHERE region='" + billRegion +"'";
            
            System.out.println(" getLocationList "+sql);
            rs = db.GetSQL(sql);
            
            while(rs.next()) {
                location = new Location(rs.getString("billinglocation"), rs.getString("billinglocation_desc"));
                lst.add(location);
            }
            
            rs.close();
            db.CloseConn();
            
            arr = (Location[])lst.toArray(arr);
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        
        
        return arr;
    }
    
    
    
    public BillingVisit[] getVisitType(String billRegion) {
        BillingVisit[] arr ={};
        
        try {
            
            ArrayList lst = new ArrayList();
            BillingVisit billingvisit;
            
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
            ResultSet rs;
            String sql;
            
            
            // SELECT b.service_code, b.description , b.value, b.percentage FROM billingservice b, ctl_billingservice c WHERE b.service_code=c.service_code and b.region='BC' and c.service_group='Group1';
            
            sql = "SELECT visittype,visit_desc FROM billingvisit "
            + " WHERE region='" + billRegion +"'";
            System.out.println("getVisitType"+sql);
            
            rs = db.GetSQL(sql);
            
            while(rs.next()) {
                billingvisit = new BillingVisit(rs.getString("visittype"), rs.getString("visit_desc"));
                lst.add(billingvisit);
            }
            
            rs.close();
            db.CloseConn();
            
            arr = (BillingVisit[])lst.toArray(arr);
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        
        
        return arr;
    }
    
    
    
    public BillingPhysician[] getProviderList() {
        BillingPhysician[] arr ={};
        
        try {
            
            ArrayList lst = new ArrayList();
            BillingPhysician billingphysician;
            
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
            ResultSet rs;
            String sql;
            
            
            // SELECT p.last_name, p.first_name, p.provider_no FROM provider p WHERE p.ohip_no <>''
            
            sql = "SELECT p.last_name, p.first_name, p.provider_no FROM provider p "
            + " WHERE p.ohip_no <>''";
            System.out.println("getProviderList "+sql);
            
            rs = db.GetSQL(sql);
            
            while(rs.next()) {
                billingphysician = new BillingPhysician(rs.getString("last_name")+", "+rs.getString("first_name"), rs.getString("provider_no"));
                lst.add(billingphysician);
            }
            
            rs.close();
            db.CloseConn();
            
            arr = (BillingPhysician[])lst.toArray(arr);
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        
        
        return arr;
    }
    
    
    
    
    
    public BillingForm[] getFormList() {
        BillingForm[] arr ={};
        
        try {
            
            ArrayList lst = new ArrayList();
            BillingForm billingForm;
            
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
            ResultSet rs;
            String sql;
            
            
            // SELECT b.service_code, b.description , b.value, b.percentage FROM BillingForm b, ctl_BillingForm c WHERE b.service_code=c.service_code and b.region='BC' and c.service_group='Group1';
            
            sql = "select servicetype_name, servicetype from ctl_billingservice "
            + "group by servicetype, servicetype_name";
            
            System.out.println("getFormList "+sql);
            rs = db.GetSQL(sql);
            
            while(rs.next()) {
                billingForm = new BillingForm(rs.getString("servicetype_name"), rs.getString("servicetype"));
                lst.add(billingForm);
            }
            
            rs.close();
            db.CloseConn();
            
            arr = (BillingForm[])lst.toArray(arr);
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
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
            
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
            ResultSet rs;
            String sql;
            
            
            // SELECT b.service_code, b.description , b.value, b.percentage FROM BillingForm b, ctl_BillingForm c WHERE b.service_code=c.service_code and b.region='BC' and c.service_group='Group1';
            
            sql = "SELECT last_name, first_name from provider where provider_no='" + provider_no + "'";
            
            
            rs = db.GetSQL(sql);
            System.out.println("getProviderName "+sql);
            while(rs.next()) {
                
                provider_n = rs.getString("last_name")+", " + rs.getString("first_name");
            }
            rs.close();
            db.CloseConn();
            
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return provider_n;
        
    }
    
    
    public String getPracNo(String provider_no){
        
        String prac_no="";
        try{
            
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
            ResultSet rs;
            String sql;
            
            
            // SELECT b.service_code, b.description , b.value, b.percentage FROM BillingForm b, ctl_BillingForm c WHERE b.service_code=c.service_code and b.region='BC' and c.service_group='Group1';
            
            sql = "SELECT ohip_no from provider where provider_no='" + provider_no + "'";
            
            
            rs = db.GetSQL(sql);
            System.out.println("getPracNo "+sql);
            
            while(rs.next()) {
                
                prac_no = rs.getString("ohip_no");
            }
            rs.close();
            db.CloseConn();
            
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return prac_no;
        
        
    }
    
    public String getGroupNo(String provider_no){
        
        String prac_no="";
        try{
            
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
            ResultSet rs;
            String sql;
            
            
            // SELECT b.service_code, b.description , b.value, b.percentage FROM BillingForm b, ctl_BillingForm c WHERE b.service_code=c.service_code and b.region='BC' and c.service_group='Group1';
            
            sql = "SELECT billing_no from provider where provider_no='" + provider_no + "'";
            
            System.out.println("getGroupNo "+sql);
            rs = db.GetSQL(sql);
            
            while(rs.next()) {
                
                prac_no = rs.getString("billing_no");
            }
            rs.close();
            db.CloseConn();
            
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return prac_no;
        
        
    }
    
    
    
    public String getDiagDesc(String dx, String reg){
        
        String dxdesc="";
        try{
            
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
            ResultSet rs;
            String sql;
            
            
            // SELECT b.service_code, b.description , b.value, b.percentage FROM BillingForm b, ctl_BillingForm c WHERE b.service_code=c.service_code and b.region='BC' and c.service_group='Group1';
            
            sql = "SELECT description from diagnosticcode where diagnostic_code='" + dx + "' and region='" + reg + "'";
            
            
            rs = db.GetSQL(sql);
            System.out.println("getDiagDesc "+sql);
            
            while(rs.next()) {
                
                dxdesc = rs.getString("description");
            }
            rs.close();
            db.CloseConn();
            
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return dxdesc;
        
        
    }
    
    
    
}
