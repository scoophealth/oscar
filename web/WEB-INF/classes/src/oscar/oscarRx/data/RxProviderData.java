  
/*
 * 
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
 * This software is published under the GPL GNU General Public License. 
 * This program is free software; you can redistribute it and/or 
 * modify it under the terms of the GNU General Public License 
 * as published by the Free Software Foundation; either version 2 
 * of the License, or (at your option) any later version. * 
 * This program is distributed in the hope that it will be useful, 
 * but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
 * GNU General Public License for more details. * * You should have received a copy of the GNU General Public License 
 * along with this program; if not, write to the Free Software 
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. * 
 * 
 * <OSCAR TEAM>
 * 
 * This software was written for the 
 * Department of Family Medicine 
 * McMaster Unviersity 
 * Hamilton 
 * Ontario, Canada 
 */
package oscar.oscarRx.data;

import oscar.oscarDB.*;

import java.util.*;
import java.sql.*;

public class RxProviderData {
    public Provider getProvider(String providerNo) {
        Provider provider = null;

        try {
            //Get Provider from database
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
            ResultSet rs;
            String sql = "SELECT * FROM provider WHERE provider_no = '" + providerNo + "'";
            rs = db.GetSQL(sql);

            String providerClinicPhone=null, surname=null, firstName=null,  clinicName=null, clinicAddress=null, clinicCity=null, clinicPostal=null, clinicPhone=null, clinicFax=null,clinicProvince = null ;
            if (rs.next()) {
                surname = rs.getString("last_name");
                firstName = rs.getString("first_name");
                if(firstName.indexOf("Dr.")<0) {
                    firstName = "Dr. " + firstName;
                }
                providerClinicPhone = rs.getString("work_phone");
            }

            sql = "SELECT value FROM property WHERE name = 'faxnumber' AND provider_no = '" + providerNo + "'";
            rs = db.GetSQL(sql);
            
            if( rs.next() ) {
                clinicFax = rs.getString("value");
            }
            
            sql = "SELECT * FROM clinic";
            rs = db.GetSQL(sql);

            if (rs.next()) {
                clinicName = rs.getString("clinic_name");
                clinicAddress = rs.getString("clinic_address");
                clinicCity = rs.getString("clinic_city");
                clinicPostal = rs.getString("clinic_postal");
                clinicPhone = rs.getString("clinic_phone");
                clinicProvince = rs.getString("clinic_province");
                
                if( clinicFax == null )
                    clinicFax = rs.getString("clinic_fax");
            }

            if((clinicPhone.length()>15) && (providerClinicPhone != null && !providerClinicPhone.equals(""))) clinicPhone = providerClinicPhone;

            provider = new Provider(providerNo, surname, firstName, clinicName, clinicAddress,
                    clinicCity, clinicPostal, clinicPhone, clinicFax, clinicProvince);

            rs.close();
            db.CloseConn();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return provider;
    }

    public class Provider{
        String providerNo;
        String surname;
        String firstName;
        String clinicName;
        String clinicAddress;
        String clinicCity;
        String clinicPostal;
        String clinicPhone;
        String clinicFax;
        String clinicProvince;

        public Provider(String providerNo, String surname, String firstName,
        String clinicName, String clinicAddress, String clinicCity,
        String clinicPostal, String clinicPhone, String clinicFax){
            this.providerNo = providerNo;
            this.surname = surname;
            this.firstName = firstName;
            this.clinicName = clinicName;
            this.clinicAddress = clinicAddress;
            this.clinicCity = clinicCity;
            this.clinicPostal = clinicPostal;
            this.clinicPhone = clinicPhone;
            this.clinicFax = clinicFax;
        }
        
        public Provider(String providerNo, String surname, String firstName,
        String clinicName, String clinicAddress, String clinicCity,
        String clinicPostal, String clinicPhone, String clinicFax,String clinicProvince){
            this.providerNo = providerNo;
            this.surname = surname;
            this.firstName = firstName;
            this.clinicName = clinicName;
            this.clinicAddress = clinicAddress;
            this.clinicCity = clinicCity;
            this.clinicPostal = clinicPostal;
            this.clinicPhone = clinicPhone;
            this.clinicFax = clinicFax;
            this.clinicProvince = clinicProvince;
        }
        

        public String getProviderNo(){
            return this.providerNo;
        }

        public String getSurname(){
            return this.surname;
        }

        public String getFirstName(){
            return this.firstName;
        }

        public String getClinicName(){
            return this.clinicName;
        }

        public String getClinicAddress(){
            return this.clinicAddress;
        }

        public String getClinicCity(){
            return this.clinicCity;
        }

        public String getClinicPostal(){
            return this.clinicPostal;
        }

        public String getClinicPhone(){
            return this.clinicPhone;
        }

        public String getClinicFax(){
            return this.clinicFax;
        }
        
        public String getClinicProvince(){
            return this.clinicProvince;
        }

        
    }
}
