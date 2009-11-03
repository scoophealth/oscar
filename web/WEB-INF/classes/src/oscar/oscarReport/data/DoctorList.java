/*
 * DoctorList.java
 *
 * Created on August 27, 2007, 4:23 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package oscar.oscarReport.data;

import java.sql.SQLException;
import java.util.ArrayList;

import oscar.oscarDB.DBHandler;
import oscar.oscarProvider.bean.ProviderNameBean;

public class DoctorList {
    public DoctorList() {
    }
    
    public ArrayList getDoctorNameList(){

        ArrayList dnl = new ArrayList();
        try{
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
            
            java.sql.ResultSet rs;
            String sql = "select last_name, first_name, provider_no from provider where provider_type='doctor'";
            rs = db.GetSQL(sql);

            while(rs.next()){
                ProviderNameBean pb = new ProviderNameBean();
                pb.setProviderID(db.getString(rs,3));
                pb.setProviderName(db.getString(rs,2)+ " " +db.getString(rs,1));
                dnl.add(pb);
                
            }
        }
        catch(SQLException e)
        {
            System.out.println(e.getMessage());
        } 
        return dnl;
    }
}
