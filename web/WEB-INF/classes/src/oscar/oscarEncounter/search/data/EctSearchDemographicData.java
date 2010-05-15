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
// * McMaster University 
// * Hamilton 
// * Ontario, Canada 
// *
// -----------------------------------------------------------------------------------------------------------------------
package oscar.oscarEncounter.search.data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import oscar.oscarDB.DBHandler;

public class EctSearchDemographicData
{

    private String makeSearchString(String fName, String lName, String phone, String hin, String yearOfBirth, String sex, String address, 
            String chartNumber, String monthOfBirth, String dayOfBirth, String city)
    {
        int items = 0;
        StringBuffer searchString = new StringBuffer("Select * from demographic where ");
        fName = fName.trim();
        lName = lName.trim();
        phone = phone.trim();
        hin = hin.trim();
        yearOfBirth = yearOfBirth.trim();
        address = address.trim();
        chartNumber = chartNumber.trim();
        monthOfBirth = monthOfBirth.trim();
        dayOfBirth = dayOfBirth.trim();
        city = city.trim();
        if(!fName.equals(""))
        {
            if(items == 0)
                searchString.append(String.valueOf(String.valueOf((new StringBuffer(" first_name like '")).append(fName).append("%' "))));
            else
                searchString.append(String.valueOf(String.valueOf((new StringBuffer(" and first_name like '")).append(fName).append("%' "))));
            items++;
        }
        if(!lName.equals(""))
        {
            if(items == 0)
                searchString.append(String.valueOf(String.valueOf((new StringBuffer(" last_name like '")).append(lName).append("%' "))));
            else
                searchString.append(String.valueOf(String.valueOf((new StringBuffer(" and last_name like '")).append(lName).append("%' "))));
            items++;
        }
        if(!phone.equals(""))
        {
            if(items == 0)
                searchString.append(String.valueOf(String.valueOf((new StringBuffer(" phone  like '")).append(phone).append("%' or phone2 like '").append(phone).append("%' "))));
            else
                searchString.append(String.valueOf(String.valueOf((new StringBuffer(" and phone  like '")).append(phone).append("%' or phone2 like '").append(phone).append("%' "))));
            items++;
        }
        if(!hin.equals(""))
        {
            if(items == 0)
                searchString.append(String.valueOf(String.valueOf((new StringBuffer(" hin like '")).append(hin).append("%' "))));
            else
                searchString.append(String.valueOf(String.valueOf((new StringBuffer(" and hin like '")).append(hin).append("%' "))));
            items++;
        }
        if(!yearOfBirth.equals(""))
        {
            if(items == 0)
                searchString.append(String.valueOf(String.valueOf((new StringBuffer(" year_of_birth  like '")).append(yearOfBirth).append("%' "))));
            else
                searchString.append(String.valueOf(String.valueOf((new StringBuffer(" and year_of_birth  like '")).append(yearOfBirth).append("%' "))));
            items++;
        }
        if(!sex.equals(""))
        {
            if(items == 0)
                searchString.append(String.valueOf(String.valueOf((new StringBuffer(" sex  like '")).append(sex).append("%' "))));
            else
                searchString.append(String.valueOf(String.valueOf((new StringBuffer(" and sex  like '")).append(sex).append("%' "))));
            items++;
        }
        if(!address.equals(""))
        {
            if(items == 0)
                searchString.append(String.valueOf(String.valueOf((new StringBuffer(" address  like '")).append(address).append("%' "))));
            else
                searchString.append(String.valueOf(String.valueOf((new StringBuffer(" and address  like '")).append(address).append("%' "))));
            items++;
        }
        if(!chartNumber.equals(""))
        {
            if(items == 0)
                searchString.append(String.valueOf(String.valueOf((new StringBuffer(" chart_no  like '")).append(chartNumber).append("%' "))));
            else
                searchString.append(String.valueOf(String.valueOf((new StringBuffer(" and chart_no  like '")).append(chartNumber).append("%' "))));
            items++;
        }
        if(!monthOfBirth.equals(""))
        {
            if(items == 0)
                searchString.append(String.valueOf(String.valueOf((new StringBuffer(" month_of_birth  like '")).append(monthOfBirth).append("%' "))));
            else
                searchString.append(String.valueOf(String.valueOf((new StringBuffer(" and month_of_birth  like '")).append(monthOfBirth).append("%' "))));
            items++;
        }
        if(!dayOfBirth.equals(""))
        {
            if(items == 0)
                searchString.append(String.valueOf(String.valueOf((new StringBuffer(" date_of_birth  like '")).append(dayOfBirth).append("%' "))));
            else
                searchString.append(String.valueOf(String.valueOf((new StringBuffer(" and date_of_birth  like '")).append(dayOfBirth).append("%' "))));
            items++;
        }
        if(!city.equals(""))
        {
            if(items == 0)
                searchString.append(String.valueOf(String.valueOf((new StringBuffer(" city  like '")).append(city).append("%' "))));
            else
                searchString.append(String.valueOf(String.valueOf((new StringBuffer(" and city  like '")).append(city).append("%' "))));
            items++;
        }
        if(items > 0)
        {
            searchString.append(" order by last_name, first_name ");
            System.out.println(searchString.toString());
            return searchString.toString();
        } else
        {
            return null;
        }
    }

    public Vector doSearch(String fName, String lName, String phone, String hin, String yearOfBirth, String sex, String address, 
            String chartNumber, String monthOfBirth, String dayOfBirth, String city)
    {
        Vector vector = null;
        String sql = makeSearchString(fName, lName, phone, hin, yearOfBirth, sex, address, chartNumber, monthOfBirth, dayOfBirth, city);
        if(sql != null)
        {
            vector = new Vector();
            try
            {
                DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
                ResultSet rs;
                EctDemographicData demographicData;
                for(rs = db.GetSQL(sql); rs.next(); vector.add(demographicData))
                {
                    demographicData = new EctDemographicData();
                    demographicData.demographicNo = db.getString(rs,"demographic_no");
                    demographicData.firstName = db.getString(rs,"first_name");
                    demographicData.lastName = db.getString(rs,"last_name");
                    demographicData.address = db.getString(rs,"address");
                    demographicData.city = db.getString(rs,"city");
                    demographicData.province = db.getString(rs,"province");
                    demographicData.phone = db.getString(rs,"phone");
                    demographicData.phone2 = db.getString(rs,"phone2");
                    demographicData.hin = db.getString(rs,"hin");
                    demographicData.sex = db.getString(rs,"sex");
                    demographicData.yearOfBirth = db.getString(rs,"year_of_birth");
                    demographicData.monthOfBirth = db.getString(rs,"month_of_birth");
                    demographicData.dayOfBirth = db.getString(rs,"date_of_birth");
                }

                rs.close();
            }
            catch(SQLException e)
            {
                System.out.println(e.getMessage());
            }
        }
        return vector;
    }
}
