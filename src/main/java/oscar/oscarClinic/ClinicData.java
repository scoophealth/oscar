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


/*
 * ClinicData.java
 *
 * Created on May 2, 2003, 10:44 AM
 */

package oscar.oscarClinic;
import java.sql.SQLException;

import org.oscarehr.util.MiscUtils;

import oscar.oscarDB.DBHandler;
/**
 *
 * @author  Oscar
 */
public class ClinicData {
    /**
    +----------------------+-------------+------+-----+---------+----------------+
    | Field                | Type        | Null | Key | Default | Extra          |
    +----------------------+-------------+------+-----+---------+----------------+
    | clinic_no            | int(10)     |      | PRI | NULL    | auto_increment |
    | clinic_name          | varchar(50) | YES  |     | NULL    |                |
    | clinic_address       | varchar(60) | YES  |     |         |                |
    | clinic_city          | varchar(40) | YES  |     |         |                |
    | clinic_postal        | varchar(15) | YES  |     |         |                |
    | clinic_phone         | varchar(50) | YES  |     | NULL    |                |
    | clinic_fax           | varchar(20) | YES  |     |         |                |
    | clinic_location_code | varchar(10) | YES  |     | NULL    |                |
    | status               | char(1)     | YES  |     | NULL    |                |
    +----------------------+-------------+------+-----+---------+----------------+

     
   
    */
    /** Creates a new instance of clinicData */
    
    
    String clinic_no = null;
    String clinic_name = null;          
    String clinic_address = null;
    String clinic_city = null;       
    String clinic_postal = null;
    String clinic_phone = null;         
    String clinic_fax = null;           
    String clinic_location_code = null;
    
    String clinic_province = null;
    String clinic_delim_phone = null;
    String clinic_delim_fax = null;
    
    boolean filled = false;
    public ClinicData() {
    }
    
    void fillClinicData(){
        if (!filled){
            try{
               
               java.sql.ResultSet rs;
               String sql = "select * from clinic ";
               rs = DBHandler.GetSQL(sql);
                
               if(rs.next()){
                 clinic_no              = oscar.Misc.getString(rs, "clinic_no"); 
                 clinic_name            = oscar.Misc.getString(rs, "clinic_name");  
                 clinic_address         = oscar.Misc.getString(rs, "clinic_address"); 
                 clinic_city            = oscar.Misc.getString(rs, "clinic_city"); 
                 clinic_postal          = oscar.Misc.getString(rs, "clinic_postal"); 
                 clinic_phone           = oscar.Misc.getString(rs, "clinic_phone"); 
                 clinic_fax             = oscar.Misc.getString(rs, "clinic_fax"); 
                 clinic_location_code   = oscar.Misc.getString(rs, "clinic_location_code"); 
                 clinic_province        = oscar.Misc.getString(rs, "clinic_province"); 
                 clinic_delim_phone     = oscar.Misc.getString(rs, "clinic_delim_phone"); 
                 clinic_delim_fax       = oscar.Misc.getString(rs, "clinic_delim_fax"); 
                  
               }
            }
            catch(SQLException e)
            {
                MiscUtils.getLogger().error("Error", e);
            } 
            filled = true;
        }
    }
    
    public String getClinicNo() {
		fillClinicData();
		return clinic_no;    
	}
    
    public String getClinicName(){
       fillClinicData();
       return clinic_name;        
    }    
    public String getClinicAddress(){
       fillClinicData();
       return clinic_address;
    }
    public String getClinicCity(){
       fillClinicData();
       return clinic_city;
    }
    public String getClinicProvince(){
       fillClinicData();
       return clinic_province;
    }
    public String getClinicPostal(){
      fillClinicData();
       return  clinic_postal;
    }
    public String getClinicPhone(){
       fillClinicData();
       return clinic_phone;
    }
    public String getClinicFax(){
       fillClinicData();
       return clinic_fax;
    }
    public String getClinicLocationCode(){
       fillClinicData();
       return clinic_location_code;
    }
    
    public String getClinicDelimPhone(){
       fillClinicData();
       return  clinic_delim_phone;       
    }
    
    public String getClinicDelimFax(){
       fillClinicData();
       return  clinic_delim_fax;       
    }
    
    public void refreshClinicData(){
       filled = false;
       fillClinicData();
    }

    public void setClinic_address(String clinic_address) {
        this.clinic_address = clinic_address;
    }

    public void setClinic_city(String clinic_city) {
        this.clinic_city = clinic_city;
    }

    public void setClinic_fax(String clinic_fax) {
        this.clinic_fax = clinic_fax;
    }

    public void setClinic_name(String clinic_name) {
        this.clinic_name = clinic_name;
    }

    public void setClinic_phone(String clinic_phone) {
        this.clinic_phone = clinic_phone;
    }

    public void setClinic_postal(String clinic_postal) {
        this.clinic_postal = clinic_postal;
    }

    public void setClinic_province(String clinic_province) {
        this.clinic_province = clinic_province;
    }
    
    
}
