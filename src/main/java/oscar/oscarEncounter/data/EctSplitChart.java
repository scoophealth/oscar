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


package oscar.oscarEncounter.data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Vector;

import org.oscarehr.util.MiscUtils;

import oscar.oscarDB.DBHandler;

/**
 *
 * @author Jay Gallagher
 */
public class EctSplitChart {
   
 
   public EctSplitChart() {
      
   }
   
   
   public Vector getSplitCharts(String demographicNo){
      Vector vec = null;//
      try{              
         vec = new Vector();
         
         String sql = "select eChartId, timeStamp from eChart where demographicNo= '"+demographicNo+"'  and subject = 'SPLIT CHART' order by timeStamp ";
         ResultSet rs = DBHandler.GetSQL(sql);
         while(rs.next()) {
            String[] s = new String[2];
             s[0] = oscar.Misc.getString(rs, "eChartId");            
             Timestamp timestamp = rs.getTimestamp("timeStamp");             
             java.util.Date d = new java.util.Date(timestamp.getTime());
             
             Format formatter = new SimpleDateFormat("yyyy-MM-dd");
             s[1] = formatter.format(d);
                                                    
            vec.add(s);
         }
         rs.close();
         
      }catch(SQLException e){
         MiscUtils.getLogger().error("Error", e);
         vec = null;
      }
      return vec;
   }
}
