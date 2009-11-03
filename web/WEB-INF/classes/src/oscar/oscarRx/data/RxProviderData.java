  
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

import java.sql.ResultSet;
import java.sql.SQLException;

import oscar.oscarDB.DBHandler;

public class RxProviderData {
    public Provider getProvider(String providerNo) {
        Provider provider = null;

        try {
            //Get Provider from database
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
            ResultSet rs;
            String sql = "SELECT * FROM provider WHERE provider_no = '" + providerNo + "'";
            rs = db.GetSQL(sql);

            String providerClinicPhone=null, surname=null, firstName=null,  clinicName=null, clinicAddress=null, clinicCity=null, clinicPostal=null, clinicPhone=null, clinicFax=null, clinicProvince=null, practitionerNo=null;
            if (rs.next()) {
                surname = db.getString(rs,"last_name");
                firstName = db.getString(rs,"first_name");
                practitionerNo = db.getString(rs,"practitionerNo");
                if(firstName.indexOf("Dr.")<0) {
                    firstName = "Dr. " + firstName;
                }
                providerClinicPhone = db.getString(rs,"work_phone");
            }

            sql = "SELECT value FROM property WHERE name = 'faxnumber' AND provider_no = '" + providerNo + "'";
            rs = db.GetSQL(sql);
            
            if( rs.next() ) {
                clinicFax = db.getString(rs,"value");
            }
            
            sql = "SELECT * FROM clinic";
            rs = db.GetSQL(sql);

            if (rs.next()) {
                clinicName = db.getString(rs,"clinic_name");
                clinicAddress = db.getString(rs,"clinic_address");
                clinicCity = db.getString(rs,"clinic_city");
                clinicPostal = db.getString(rs,"clinic_postal");
                clinicPhone = db.getString(rs,"clinic_phone");
                clinicProvince = db.getString(rs,"clinic_province");
                
                if( clinicFax == null )
                    clinicFax = db.getString(rs,"clinic_fax");
            }

            if((clinicPhone.length()>15) && (providerClinicPhone != null && !providerClinicPhone.equals(""))) clinicPhone = providerClinicPhone;

            provider = new Provider(providerNo, surname, firstName, clinicName, clinicAddress,
                    clinicCity, clinicPostal, clinicPhone, clinicFax, clinicProvince, practitionerNo);

            rs.close();
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
        String practitionerNo;

        public Provider(String providerNo, String surname, String firstName,
        String clinicName, String clinicAddress, String clinicCity,
        String clinicPostal, String clinicPhone, String clinicFax, String practitionerNo){
            this.providerNo = providerNo;
            this.surname = surname;
            this.firstName = firstName;
            this.clinicName = clinicName;
            this.clinicAddress = clinicAddress;
            this.clinicCity = clinicCity;
            this.clinicPostal = clinicPostal;
            this.clinicPhone = clinicPhone;
            this.clinicFax = clinicFax;
	    this.practitionerNo = practitionerNo;
        }
        
        public Provider(String providerNo, String surname, String firstName,
        String clinicName, String clinicAddress, String clinicCity,
        String clinicPostal, String clinicPhone, String clinicFax,String clinicProvince, String practitionerNo){
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
	    this.practitionerNo = practitionerNo;
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

	public String getPractitionerNo() {
	   return this.practitionerNo;
	}
        
    }
}
