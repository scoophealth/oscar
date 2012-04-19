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


package oscar.oscarEncounter.search.pageUtil;

import org.apache.struts.action.ActionForm;

public final class EctDemographicSearchForm extends ActionForm
{

    public String getFirstName()
    {
        if(firstName == null)
            firstName = new String();
        return firstName;
    }

    public void setFirstName(String str)
    {
        firstName = str;
    }

    public String getLastName()
    {
        if(lastName == null)
            lastName = new String();
        return lastName;
    }

    public void setLastName(String str)
    {
        lastName = str;
    }

    public String getPhone()
    {
        if(phone == null)
            phone = new String();
        return phone;
    }

    public void setPhone(String str)
    {
        phone = str;
    }

    public String getHin()
    {
        if(hin == null)
            hin = new String();
        return hin;
    }

    public void setHin(String str)
    {
        hin = str;
    }

    public String getSex()
    {
        if(sex == null)
            sex = new String();
        return sex;
    }

    public void setSex(String str)
    {
        sex = str;
    }

    public String getYearOfBirth()
    {
        if(yearOfBirth == null)
            yearOfBirth = new String();
        return yearOfBirth;
    }

    public void setYearOfBirth(String str)
    {
        yearOfBirth = str;
    }

    public String getAddress()
    {
        if(address == null)
            address = new String();
        return address;
    }

    public void setAddress(String str)
    {
        address = str;
    }

    public String getChartNumber()
    {
        if(chartNumber == null)
            chartNumber = new String();
        return chartNumber;
    }

    public void setChartNumber(String str)
    {
        chartNumber = str;
    }

    public String getMonthOfBirth()
    {
        if(monthOfBirth == null)
            monthOfBirth = new String();
        return monthOfBirth;
    }

    public void setMonthOfBirth(String str)
    {
        monthOfBirth = str;
    }

    public String getDayOfBirth()
    {
        if(dayOfBirth == null)
            dayOfBirth = new String();
        return dayOfBirth;
    }

    public void setDayOfBirth(String str)
    {
        dayOfBirth = str;
    }

    public String getCity()
    {
        if(city == null)
            city = new String();
        return city;
    }

    public void setCity(String str)
    {
        city = str;
    }

    String firstName;
    String lastName;
    String phone;
    String hin;
    String sex;
    String yearOfBirth;
    String address;
    String chartNumber;
    String monthOfBirth;
    String dayOfBirth;
    String city;
}
