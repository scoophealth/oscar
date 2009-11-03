/*
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
 * This software is published under the GPL GNU General Public License.
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version. *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details. * * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. *
 *
 * Jason Gallagher
 *
 * This software was written for the
 * Department of Family Medicine
 * McMaster University
 * Hamilton
 * Ontario, Canada   Creates a new instance of EfmData
 *
 *
 * EfmData.java
 *
 * Created on July 28, 2005, 1:54 PM
 */

package oscar.eform;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Hashtable;

import oscar.oscarDB.DBHandler;

/**
 * 
 * @author Jay Gallagher
+----------------+--------------+------+-----+---------+----------------+
| Field          | Type         | Null | Key | Default | Extra          |
+----------------+--------------+------+-----+---------+----------------+
| fdid           | int(8)       |      | PRI | NULL    | auto_increment |
| fid            | int(8)       |      |     | 0       |                |
| form_name      | varchar(255) | YES  |     | NULL    |                |
| subject        | varchar(255) | YES  |     | NULL    |                |
| demographic_no | int(10)      |      |     | 0       |                |
| status         | tinyint(1)   |      |     | 1       |                |
| form_date      | date         | YES  |     | NULL    |                |
| form_time      | time         | YES  |     | NULL    |                |
| form_provider  | varchar(255) | YES  |     | NULL    |                |
| form_data      | text         | YES  |     | NULL    |                |
+----------------+--------------+------+-----+---------+----------------+

 */
public class EfmData {
   
   public Hashtable getLastEformDate(String formName, String demographicNo){
      Hashtable ret = null;
      try {
         DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);         
         String sql = "select form_name,subject,form_date,form_time,form_provider from eform_data where demographic_no = '"+demographicNo+"' and form_name like '"+formName+"%' and status = '1' order by form_date,form_time desc";        
         //System.out.println(sql);            
         ResultSet rs = db.GetSQL(sql);                                                
         if (rs.next()){
            ret = new Hashtable();
            ret.put("formName", db.getString(rs,"form_name"));
            ret.put("subject", db.getString(rs,"subject"));
            ret.put("date", db.getString(rs,"form_date"));
            ret.put("time", db.getString(rs,"form_time"));
            ret.put("provider", db.getString(rs,"form_provider"));                          
         }            
      } catch (SQLException e) {
         System.out.println(e.getMessage());
      }
      return ret;
   }
   
   
   
   public EfmData() {
   }
   
}
