package oscar.oscarDemographic.data;

import java.sql.ResultSet;
import java.sql.SQLException;
import oscar.oscarDB.DBHandler;
//import oscar.oscarMessenger.util.*;

import oscar.util.*;
public class DemographicData {
    
    
    
    public Demographic getDemographic(String DemographicNo) {
        Demographic demographic = null;
        
        try {
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
            ResultSet rs;
            String sql = "SELECT * FROM demographic WHERE demographic_no = '" + DemographicNo +"'";
            
            rs = db.GetSQL(sql);
            
            if (rs.next()) {
                demographic = new Demographic(DemographicNo, rs.getString("last_name"),
                rs.getString("first_name"), rs.getString("address"), rs.getString("city"),
                rs.getString("province"), rs.getString("postal"), rs.getString("phone"),
                rs.getString("phone2"), rs.getString("email"), rs.getString("pin"),
                rs.getString("year_of_birth"), rs.getString("month_of_birth"), rs.getString("date_of_birth"),
                rs.getString("hin"), rs.getString("ver"), rs.getString("roster_status"),
                rs.getString("patient_status"), rs.getString("date_joined"), rs.getString("chart_no"),
                rs.getString("provider_no"), rs.getString("sex"), rs.getString("end_date"),
                rs.getString("eff_date"), rs.getString("pcn_indicator"), rs.getString("hc_type"),
                rs.getString("hc_renew_date"), rs.getString("family_doctor"));
            }
            
            rs.close();
            db.CloseConn();
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        
        return demographic;
    }
    
    
    public String getDemographicDateJoined(String DemographicNo){
        String date = null;
        try {
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
            ResultSet rs;
            String sql = "SELECT date_joined FROM demographic WHERE demographic_no = '" + DemographicNo +"'";
            
            rs = db.GetSQL(sql);
            
            if (rs.next()) {
                date = rs.getString("date_joined");//getString("date_joined");                
            }
            
            rs.close();
            db.CloseConn();
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return date;
    }
    
    
    public class Demographic {
        
        String demographic_no;
        String last_name;
        String first_name;
        String address   ;
        String city      ;
        String province  ;
        String postal    ;
        String phone     ;
        String phone2    ;
        String email     ;
        String pin       ;
        String year_of_birth ;
        String month_of_birth;
        String date_of_birth ;
        String hin           ;
        String ver           ;
        String roster_status ;
        String patient_status;
        String date_joined   ;
        String chart_no      ;
        String provider_no   ;
        String sex           ;
        String end_date      ;
        String eff_date      ;
        String pcn_indicator;
        String hc_type      ;
        String hc_renew_date;
        String family_doctor;
        
        public Demographic(String DemographicNo, String last_name,String first_name, String address,
        String city, String province, String postal, String phone, String phone2, String email,
        String pin, String year_of_birth, String month_of_birth, String date_of_birth, String hin,
        String ver, String roster_status, String patient_status, String date_joined, String chart_no,
        String provider_no, String sex, String end_date, String eff_date, String pcn_indicator,
        String hc_type, String hc_renew_date, String family_doctor) {
            this.demographic_no= DemographicNo;
            this.last_name = last_name;
            this.first_name = first_name;
            this.address = address;
            this.city = city;
            this.province = province;
            this.postal = postal;
            this.phone= phone;
            this.phone2 = phone2;
            this.email = email;
            this.pin = pin;
            this.year_of_birth = year_of_birth;
            this.month_of_birth = month_of_birth;
            this.date_of_birth = date_of_birth;
            this.hin = hin;
            this.ver = ver;
            this.roster_status=roster_status;
            this.patient_status= patient_status;
            this.date_joined = date_joined;
            this.chart_no = chart_no;
            this.provider_no = provider_no;
            this.sex = sex;
            this.end_date = end_date;
            this.eff_date = eff_date;
            this.pcn_indicator= pcn_indicator;
            this.hc_type = hc_type;
            this.hc_renew_date = hc_renew_date;
            this.family_doctor = family_doctor;
            
            
            
        }
        
        
        
        public String getDemographicNo() {
            return demographic_no;
        }
        public String getLastName() {
            return last_name;
        }
        public String getFirstName() {
            return first_name;
        }
        public String getAddress() {
            return address;
        }
        public String getCity() {
            return city;
        }
        public String getProvince() {
            return province;
        }
        
        public String getPostal() {
            return postal;
        }
        public String getPhone() {
            return phone;
        }
        public String getPhone2() {
            return phone2;
        }
        public String getEmail() {
            return email;
        }
        public String getPin() {
            return pin;
        }
                
        public String getAge() {
           return (String.valueOf(oscar.util.UtilDateUtilities.calcAge(year_of_birth,month_of_birth,date_of_birth)));            
        }
        
        public String getDob() {
           return addZero(year_of_birth,4)+addZero(month_of_birth,2)+addZero(date_of_birth,2);
        }
        
        public String getDob(String seperator){
	   return this.getYearOfBirth() + seperator + this.getMonthOfBirth() + seperator + this.getDateOfBirth();
	}
        
        public String getYearOfBirth(){
	   return addZero(year_of_birth, 4);
	}

        public String getMonthOfBirth(){
	   return addZero(month_of_birth, 2);
        }
	
        public String getDateOfBirth(){
	   return addZero(date_of_birth, 2);
	}
		        
        public String getHIN() {
            return hin + ver;
        }
        public String getRosterStatus() {
            return roster_status;
        }
        
        public String getPatientStatus() {
            return patient_status;
        }
        public String getDateJoined() {
            return date_joined;
        }
        
        public String getChartNo() {
            return chart_no;
        }
        public String getProviderNo() {
            return provider_no;
        }
        
        public String getSex() {
            return sex;
        }
        public String getEndDate() {
            return end_date;
        }
        public String getEffDate() {
            return eff_date;
        }
        public String getPCNindicator() {
            return pcn_indicator;
        }
        
        public String getHCType() {
            return hc_type;
        }
        public String getHCRenewDate() {
            return hc_renew_date;
        }
        public String getFamilyDoctor() {
            return family_doctor;
        }
        
        public String addZero(String text, int num){
            text = text.trim();
            String zero = "0";
            for (int i = text.length();i<num; i++){
                text = zero + text;
            }
            return text;
        }
        
        
    }
    
}

