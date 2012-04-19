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


package oscar.oscarMessenger.pageUtil;



import org.apache.struts.action.ActionForm;

public final class MsgDemographicSearchForm extends ActionForm {


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
    String id;





    //////firstName/////////////////////////////////////////////////////////////
    public String getFirstName (){
        if ( this.firstName == null){
            this.firstName = new String();
        }
        return this.firstName ;
    }

    public void setFirstName (String str){
        this.firstName = str;
    }
    //--------------------------------------------------------------------------

    ///////////////lastName/////////////////////////////////////////////////////
    public String getLastName (){
        if ( this.lastName == null){
            this.lastName = new String();
        }
        return this.lastName ;
    }

    public void setLastName (String str){
        this.lastName = str;
    }
    //--------------------------------------------------------------------------

    /////////phone//////////////////////////////////////////////////////////////
    public String getPhone (){
        if ( this.phone == null){
            this.phone = new String();
        }
        return this.phone ;
    }

    public void setPhone (String str){
        this.phone = str;
    }
    //--------------------------------------------------------------------------

    //////////hin///////////////////////////////////////////////////////////////
    public String getHin (){
        if ( this.hin == null){
            this.hin = new String();
        }
        return this.hin ;
    }

    public void setHin (String str){
        this.hin = str;
    }
    //--------------------------------------------------------------------------

    ///////sex//////////////////////////////////////////////////////////////////
    public String getSex (){
        if ( this.sex == null){
            this.sex = new String();
        }
        return this.sex ;
    }

    public void setSex (String str){
        this.sex = str;
    }
    //--------------------------------------------------------------------------

    //////////age///////////////////////////////////////////////////////////////
    public String getYearOfBirth(){
        if ( this.yearOfBirth == null){
            this.yearOfBirth = new String();
        }
        return this.yearOfBirth ;
    }

    public void setYearOfBirth (String str){
        this.yearOfBirth = str;
    }
    //--------------------------------------------------------------------------

    /////////////address////////////////////////////////////////////////////////
    public String getAddress (){
        if ( this.address == null){
            this.address = new String();
        }
        return this.address ;
    }

    public void setAddress (String str){
        this.address = str;
    }
    //--------------------------------------------------------------------------

    ////////chartNumber/////////////////////////////////////////////////////////
    public String getChartNumber (){
        if ( this.chartNumber == null){
            this.chartNumber = new String();
        }
        return this.chartNumber ;
    }

    public void setChartNumber (String str){
        this.chartNumber = str;
    }
    //--------------------------------------------------------------------------

    ///////////monthOfBirth/////////////////////////////////////////////////////
    public String getMonthOfBirth (){
        if ( this.monthOfBirth == null){
            this.monthOfBirth = new String();
        }
        return this.monthOfBirth ;
    }

    public void setMonthOfBirth (String str){
        this.monthOfBirth = str;
    }
    //--------------------------------------------------------------------------

    ////////////////dayOfBirth//////////////////////////////////////////////////
    public String getDayOfBirth (){
        if ( this.dayOfBirth == null){
            this.dayOfBirth = new String();
        }
        return this.dayOfBirth ;
    }
    /**
     * I did this because the data in the data base is stored like 01 02 03
     * im not sure why it is becuase month is stored like 1 2 3.
     * Im not sure if its correct to put it in here but it seemed like a
     * good a place as any
     * @param str string of the day of there birth
     */
    public void setDayOfBirth (String str){
        this.dayOfBirth = str;
    }
    //--------------------------------------------------------------------------

    ////////city////////////////////////////////////////////////////////////////
    public String getCity (){
        if ( this.city == null){
            this.city = new String();
        }
        return this.city ;
    }

    public void setCity (String str){
        this.city = str;
    }
    //--------------------------------------------------------------------------

    /////id/////////////////////////////////////////////////////////////////////
    public String getId (){
        if ( this.id == null){
            this.id = new String();
        }
        return this.id ;
    }

    public void setId (String str){
        this.id = str;
    }
    //--------------------------------------------------------------------------
}
