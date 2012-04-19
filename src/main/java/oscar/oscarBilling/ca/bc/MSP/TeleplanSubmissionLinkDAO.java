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


package oscar.oscarBilling.ca.bc.MSP;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.oscarehr.util.DbConnectionFilter;
import org.oscarehr.util.MiscUtils;

/**
  CREATE TABLE `teleplan_submission_link` (
     `id` int(10) NOT NULL auto_increment,
     `bill_activity_id` int(10),
     `billingmaster_no` int(10) default NULL,
     PRIMARY KEY  (`id`),
     KEY (`bill_activity_id`),
     KEY (`billingmaster_no`)
    ) 
 * @author jay
 */
public class TeleplanSubmissionLinkDAO {
    
    /** Creates a new instance of TeleplanSubmissionLinkDAO */
    public TeleplanSubmissionLinkDAO() {
    }
    
    String nsql ="insert into teleplan_submission_link (bill_activity_id,billingmaster_no) values (?,?)";
    public void save(int billActId,List billingMasterList ){
         try {
            
            PreparedStatement pstmt = DbConnectionFilter.getThreadLocalDbConnection().prepareStatement(nsql);
            for (int i =0; i < billingMasterList.size(); i++){
               String bi = (String) billingMasterList.get(i);
               int b = Integer.parseInt(bi);
               executeUpdate(pstmt,billActId,b);
            }
            pstmt.close();
         }catch (SQLException e) {
            MiscUtils.getLogger().error("Error", e);
         }
    }
    
    private void executeUpdate(PreparedStatement pstmt, int billActId, int billingmasterNo) throws SQLException{
        pstmt.setInt(1,billActId);
        pstmt.setInt(2,billingmasterNo);    
        pstmt.executeUpdate();   
        pstmt.clearParameters();
    }
}
