/*
 * ClinicData.java
 *
 * Created on May 2, 2003, 10:44 AM
 */

package oscar.oscarClinic;
import oscar.oscarDB.*;
import java.sql.*;
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
               DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
               java.sql.ResultSet rs;
               String sql = "select * from clinic ";
               rs = db.GetSQL(sql);
                
               if(rs.next()){
                 clinic_no              = rs.getString("clinic_no"); 
                 clinic_name            = rs.getString("clinic_name");  
                 clinic_address         = rs.getString("clinic_address"); 
                 clinic_city            = rs.getString("clinic_city"); 
                 clinic_postal          = rs.getString("clinic_postal"); 
                 clinic_phone           = rs.getString("clinic_phone"); 
                 clinic_fax             = rs.getString("clinic_fax"); 
                 clinic_location_code   = rs.getString("clinic_location_code"); 
                 clinic_province        = rs.getString("clinic_province"); 
                 clinic_delim_phone     = rs.getString("clinic_delim_phone"); 
                 clinic_delim_fax       = rs.getString("clinic_delim_fax"); 
                  
               }
               
                db.CloseConn();
            }
            catch(SQLException e)
            {
                System.out.println(e.getMessage());
            } 
            filled = true;
        }
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
    
    
}

