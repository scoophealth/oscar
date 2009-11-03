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
// * Date         Implemented By  Company                 Comments
// * 29-09-2004   Ivy Chan        iConcept Technologies   initial version
// *
// * This software was written for the 
// * Department of Family Medicine 
// * McMaster Unviersity 
// * Hamilton 
// * Ontario, Canada 
// *
// -----------------------------------------------------------------------------------------------------------------------
package oscar.oscarProvider.bean;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Vector;

import oscar.oscarDB.DBHandler;
import oscar.oscarProvider.data.ProviderData;

public class ProviderNameBeanHandler {
    
    Vector providerNameVector = new Vector();
    Vector doctorNameVector = new Vector();
    Vector thisGroupProviderVector = new Vector();
 
    public ProviderNameBeanHandler() {
        init();
    }
    
    public boolean init() {
        
        boolean verdict = true;
        try {
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
            String sql = "SELECT DISTINCT provider_no, provider_type from provider ORDER BY last_name";
            //System.out.println("Sql Statement: " + sql);
            ResultSet rs;
            for(rs = db.GetSQL(sql); rs.next(); )
            {
                ProviderData pData = new ProviderData(db.getString(rs,"provider_no"));
                ProviderNameBean pNameBean = new ProviderNameBean(pData.getLast_name() + ", " + pData.getFirst_name(), db.getString(rs,"provider_no"));
                providerNameVector.add(pNameBean);
                if(db.getString(rs,"provider_type").equalsIgnoreCase("doctor")){
                    doctorNameVector.add(pNameBean);
                    //System.out.println("doctor name added");
                }
                    
            }

            rs.close();
        }
        catch(SQLException e) {
            System.out.println(e.getMessage());
            verdict = false;
        }
        return verdict;
    }

    public Collection getProviderNameVector(){
        return providerNameVector;
    }
    
    public Collection getDoctorNameVector(){
        return doctorNameVector;
    }
    
    public void setThisGroupProviderVector(String groupNo){
        try{
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
            String sql = "select provider_no, last_name, first_name from mygroup where mygroup_no='" + groupNo + "' order by first_name";
            ResultSet rs;
            for(rs = db.GetSQL(sql); rs.next(); )
            {                
                ProviderNameBean pNameBean = new ProviderNameBean(db.getString(rs,"last_name") + ", " + db.getString(rs,"first_name"), db.getString(rs,"provider_no"));
                thisGroupProviderVector.add(pNameBean);
            }
        }
         catch(SQLException e) {
            System.out.println(e.getMessage());         
        }
    }
    
    public Collection getThisGroupProviderVector(){
        return thisGroupProviderVector;
    }
}

