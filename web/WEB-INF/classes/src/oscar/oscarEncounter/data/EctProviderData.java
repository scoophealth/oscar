// -----------------------------------------------------------------------------------------------------------------------
// *
// *
// * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
// * This software is published under the GPL GNU General Public License. 
// * This program is free software; you can redistribute it and/or 
// * modify it under the terms of the GNU General Public License 
// * as published by the Free Software Foundation; either version 2 
// * of the License, or (at your option) any later version. * 
// * This program is distributed in the hope that it will be useful, 
// * but WITHOUT ANY WARRANTY; without even the implied warranty of 
// * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
// * GNU General Public License for more details. * * You should have received a copy of the GNU General Public License 
// * along with this program; if not, write to the Free Software 
// * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. * 
// * 
// * <OSCAR TEAM>
// * This software was written for the 
// * Department of Family Medicine 
// * McMaster Unviersity 
// * Hamilton 
// * Ontario, Canada 
// *
// -----------------------------------------------------------------------------------------------------------------------
package oscar.oscarEncounter.data;

import java.sql.ResultSet;
import java.sql.SQLException;

import oscar.SxmlMisc;
import oscar.oscarClinic.ClinicData;
import oscar.oscarDB.DBHandler;
import oscar.oscarProvider.data.ProviderMyOscarIdData;

public class EctProviderData {
    public class Provider {
        String providerNo;
        String surname;
        String firstName;
        String clinicName;
        String clinicAddress;
        String clinicCity;
        String clinicPostal;
        String clinicPhone;
        String clinicFax;
        String indivoId;
        String indivoPasswd;

        public String getProviderNo() {
            return providerNo;
        }

        public String getSurname()        {
            return surname;
        }

        public String getFirstName()        {
            return firstName;
        }

        public String getClinicName()        {
            return clinicName;
        }

        public String getClinicAddress()        {
            return clinicAddress;
        }

        public String getClinicCity()        {
            return clinicCity;
        }

        public String getClinicPostal()        {
            return clinicPostal;
        }

        public String getClinicPhone()        {
            return clinicPhone;
        }

        public String getClinicFax()        {
            return clinicFax;
        }
        
        public String getIndivoId() {
            return indivoId;
        }
        
        public String getIndivoPasswd() {
            return indivoPasswd;
        }

        public Provider(String providerNo, String surname, String firstName, String clinicName, String clinicAddress, String clinicCity, 
                String clinicPostal, String clinicPhone, String clinicFax)        {
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
        
        public Provider(String providerNo, String surname, String firstName, String clinicName, String clinicAddress, String clinicCity, 
                String clinicPostal, String clinicPhone, String clinicFax, String indivoId, String indivoPasswd)        {
            this.providerNo = providerNo;
            this.surname = surname;
            this.firstName = firstName;
            this.clinicName = clinicName;
            this.clinicAddress = clinicAddress;
            this.clinicCity = clinicCity;
            this.clinicPostal = clinicPostal;
            this.clinicPhone = clinicPhone;
            this.clinicFax = clinicFax;
            this.indivoId = indivoId;
            this.indivoPasswd = indivoPasswd;
        }
    }

    public Provider getProvider(String providerNo)    {
        Provider provider = null;
        try        {
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
            String sql = "SELECT * FROM provider WHERE provider_no = '" +providerNo+ "'";
            ResultSet rs = db.GetSQL(sql);
            if(rs.next()) {
                String surname = db.getString(rs,"last_name");
                String firstName = db.getString(rs,"first_name");
                String comments = db.getString(rs,"comments");                                           
                String selfLearningPasswd = SxmlMisc.getXmlContent(comments,"xml_p_slppassword");
                
                if(firstName.indexOf("Dr.") < 0)
                    firstName = "Dr. "+firstName;
                
                ClinicData clinic = new ClinicData();
                clinic.refreshClinicData();
                String clinicName = clinic.getClinicName(); 
                String clinicAddress = clinic.getClinicAddress();
                String clinicCity = clinic.getClinicCity();
                String clinicPostal = clinic.getClinicPostal();
                String clinicPhone = clinic.getClinicPhone();
                String clinicFax = clinic.getClinicFax();
                
                ProviderMyOscarIdData myOscar = new ProviderMyOscarIdData(providerNo);
                String myOscarLoginId = myOscar.getMyOscarId();
                
                provider = new Provider(providerNo, surname, firstName, clinicName, clinicAddress, clinicCity, clinicPostal, clinicPhone, clinicFax, myOscarLoginId, selfLearningPasswd);
            }
            rs.close();
        }  catch(SQLException e) {
            System.out.println(e.getMessage());
        }
        return provider;
    }
}
