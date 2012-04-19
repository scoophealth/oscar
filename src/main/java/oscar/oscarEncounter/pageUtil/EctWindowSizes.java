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
 * EctWindowSizes.java
 *
 * Created on April 2, 2004, 2:47 PM
 */

package oscar.oscarEncounter.pageUtil;

import java.sql.ResultSet;
import java.util.Properties;

import oscar.oscarDB.DBHandler;

/**
 *
 * @author  root
 */
public class EctWindowSizes {
    
    /** Creates a new instance of EctWindowSizes */
    public EctWindowSizes() {
    }
    
    public static Properties getWindowSizes(String provNo) {
        Properties props = new Properties();        
        
        try {

            
            String sql = "select * from encounterWindow where provider_no='"+provNo+"'";
            ResultSet rs = DBHandler.GetSQL(sql);
            
            rs.next(); // we WANT this to throw an exception if there is no corresponding row in the DB
            
            props.setProperty("rowOneSize", oscar.Misc.getString(rs, "rowOneSize"));
            props.setProperty("rowTwoSize", oscar.Misc.getString(rs, "rowTwoSize"));
            props.setProperty("rowThreeSize", oscar.Misc.getString(rs, "rowThreeSize"));
            props.setProperty("presBoxSize", oscar.Misc.getString(rs, "presBoxSize"));                                       
            rs.close();
        } catch (Exception e) {
            //MiscUtils.getLogger().error("Error", e);
            props.setProperty("rowOneSize", "60");
            props.setProperty("rowTwoSize", "60");
            props.setProperty("rowThreeSize", "378");
            props.setProperty("presBoxSize", "30");
        }        
        return props;
    }
}
