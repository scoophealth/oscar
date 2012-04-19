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
