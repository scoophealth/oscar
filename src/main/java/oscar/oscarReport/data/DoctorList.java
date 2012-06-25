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

import org.oscarehr.util.MiscUtils;

import oscar.oscarDB.DBHandler;
import oscar.oscarProvider.bean.ProviderNameBean;

public class DoctorList {
    public DoctorList() {
    }

    public ArrayList<ProviderNameBean> getDoctorNameList(){

        ArrayList<ProviderNameBean> dnl = new ArrayList<ProviderNameBean>();
        try{


            java.sql.ResultSet rs;
            String sql = "select last_name, first_name, provider_no from provider where provider_type='doctor'";
            rs = DBHandler.GetSQL(sql);

            while(rs.next()){
                ProviderNameBean pb = new ProviderNameBean();
                pb.setProviderID(oscar.Misc.getString(rs, 3));
                pb.setProviderName(oscar.Misc.getString(rs, 2)+ " " +oscar.Misc.getString(rs, 1));
                dnl.add(pb);

            }
        }
        catch(SQLException e)
        {
            MiscUtils.getLogger().error("Error", e);
        }
        return dnl;
    }
}
