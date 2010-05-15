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
// * This software was written for the 
// * Department of Family Medicine 
// * McMaster University 
// * Hamilton 
// * Ontario, Canada 
// *
// -----------------------------------------------------------------------------------------------------------------------
package oscar.oscarWaitingList.bean;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import oscar.oscarDB.DBHandler;

public class WLWaitingListNameBeanHandler {
    
    List waitingListNameList = new ArrayList();
    List waitingListNames = new ArrayList();
    
    public WLWaitingListNameBeanHandler(String groupNo, String providerNo) {
        init(groupNo, providerNo);
    }

    public boolean init(String groupNo, String providerNo) {
        
        boolean verdict = true;
        try {
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
            
            String sql = " SELECT * FROM waitingListName WHERE group_no='" + groupNo + "' " +
                         " AND is_history='N' order by `name` asc";
            ResultSet rs;
            
            for(rs = db.GetSQL(sql); rs.next(); )
            {                
                WLWaitingListNameBean wLBean = new WLWaitingListNameBean(   db.getString(rs,"ID"),
                                                                            db.getString(rs,"name"),
                                                                            db.getString(rs,"group_no"),
                                                                            db.getString(rs,"provider_no"),
                                                                            db.getString(rs,"create_date"));                   
                waitingListNameList.add(wLBean);
                waitingListNames.add(db.getString(rs,"name"));
            }                            
            rs.close();
        }
        catch(SQLException e) {
            System.out.println(e.getMessage());
            verdict = false;
        }
        return verdict;
    }
        
    public List getWaitingListNameList(){
        return waitingListNameList;
    }    
        
    public List getWaitingListNames(){
        return waitingListNames;
    }    

}

