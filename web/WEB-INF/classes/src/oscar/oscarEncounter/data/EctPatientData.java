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

import java.io.PrintStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import oscar.oscarDB.DBHandler;
import oscar.util.*;

public class EctPatientData {
    public class Patient {
        int demographicNo;
        String surname;
        String firstName;
        String sex;
        Date DOB;
        String address;
        String city;
        String postal;
        String phone;
        String roster;

        public int getDemographicNo() {
            return demographicNo;
        }

        public String getSurname() {
            return surname;
        }

        public String getFirstName() {
            return firstName;
        }

        public String getSex() {
            return sex;
        }

        public Date getDOB() {
            return DOB;
        }

        public String getAge() {
            return UtilDateUtilities.calcAge(getDOB());
        }

        public String getAddress() {
            return address;
        }

        public String getCity() {
            return city;
        }

        public String getPostal() {
            return postal;
        }

        public String getPhone() {
            return phone;
        }

        public String getRosterStatus() {
            return roster;
        }

        public Patient(int demographicNo, String surname, String firstName, String sex, Date DOB, String address, 
                String city, String postal, String phone, String roster) {
            this.demographicNo = demographicNo;
            this.surname = surname;
            this.firstName = firstName;
            this.sex = sex;
            this.DOB = DOB;
            this.address = address;
            this.city = city;
            this.postal = postal;
            this.phone = phone;
            this.roster = roster;
        }
    }

    public Patient getPatient(String demographicNo) throws SQLException {
        DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
        Patient p = null;
        try {
            ResultSet rs = db.GetSQL("SELECT demographic_no, last_name, first_name, sex, year_of_birth, month_of_birth, date_of_birth, address, city, postal, phone, roster_status FROM demographic WHERE demographic_no = "+demographicNo );
            if(rs.next())
                p = new Patient(rs.getInt("demographic_no"), rs.getString("last_name"), rs.getString("first_name"), rs.getString("sex"), UtilDateUtilities.calcDate(rs.getString("year_of_birth"), rs.getString("month_of_birth"), rs.getString("date_of_birth")), rs.getString("address"), rs.getString("city"), rs.getString("postal"), rs.getString("phone"), rs.getString("roster_status"));
            rs.close();
            db.CloseConn();
        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }
        return p;
    }
}
