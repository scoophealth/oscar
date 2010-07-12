/*
 *
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
 * <OSCAR TEAM>
 *
 * This software was written for the
 * Department of Family Medicine
 * McMaster University
 * Hamilton
 * Ontario, Canada
 */


package oscar.dms.data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.oscarehr.util.MiscUtils;

import oscar.oscarDB.DBHandler;

public class QueueData {
/*
+-------+-------------+------+-----+---------+----------------+
| Field | Type        | Null | Key | Default | Extra          |
+-------+-------------+------+-----+---------+----------------+
| id    | int(10)     | NO   | PRI | NULL    | auto_increment |
| name  | varchar(40) | NO   | UNI | NULL    |                |
+-------+-------------+------+-----+---------+----------------+

*/

   private String id="";
   private String name="";


   /** Creates a new instance of QueueData */
   public QueueData() {
   }

   public static List<String> getQueueNames(){
        List<String> queues=new ArrayList();
          try {
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
            ResultSet rs;
            String sql = "SELECT * FROM queue";
            rs = db.GetSQL(sql);
            while (rs.next()) {
                queues.add(db.getString(rs, "name"));
            }
            rs.close();
        } catch (SQLException e) {
            MiscUtils.getLogger().error("Error", e);
        }

       return queues;
   }
//get all queues' id and name pairs
   public static List<Hashtable> getQueues(){
       List retList=new ArrayList();
          try {
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
            ResultSet rs;
            String sql = "SELECT * FROM queue";
            rs = db.GetSQL(sql);
            while (rs.next()) {
                Hashtable queues=new Hashtable();
                queues.put("id",(db.getString(rs, "id")).trim());
                queues.put("queue", db.getString(rs, "name"));
                retList.add(queues);
            }
            rs.close();
        } catch (SQLException e) {
            MiscUtils.getLogger().error("Error", e);
        }
       return retList;
   }
   //set this queue data according to id
   /*private void getQueue(int id){
          try {
                DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
                ResultSet rs;
                String sql = "SELECT * FROM queue WHERE id = " + id +"";

                rs = db.GetSQL(sql);

                if (rs.next()) {
                   this.id = db.getString(rs, "id");
                   this.name = db.getString(rs, "name");
                }

                rs.close();

            } catch (SQLException e) {
                MiscUtils.getLogger().error("Error", e);
            }
   }*/
  //get queue name
  public static String getQueueName(int id){
      String retStr="";
          try {
                DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
                ResultSet rs;
                String sql = "SELECT * FROM queue WHERE id = " + id +"";
                rs = db.GetSQL(sql);
                if (rs.next()) {
                   retStr=db.getString(rs, "name");
                }
                rs.close();

            } catch (SQLException e) {
                MiscUtils.getLogger().error("Error", e);
            }
      return retStr;
   }
    //get queue id
  public static String getQueueid(String name){
      String retStr="";
          try {
                Integer retN=0;
                DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
                ResultSet rs;
                String sql = "SELECT * FROM queue WHERE name = '" + name +"'";
                rs = db.GetSQL(sql);
                if (rs.next()) {
                   retN=rs.getInt("id");
                }
                rs.close();
                if(retN>0)
                    retStr=retN.toString();
            } catch (SQLException e) {
                MiscUtils.getLogger().error("Error", e);
            }
      return retStr;
   }
   //set this queue data according to name, since name is unique.
 /*  private void getQueue(String name){
          try {
                DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
                ResultSet rs;
                String sql = "SELECT * FROM queue WHERE name = '" + name +"'";
                rs = db.GetSQL(sql);
                if (rs.next()) {
                   this.id = db.getString(rs, "id");
                   this.name = db.getString(rs, "name");
                }

                rs.close();

            } catch (SQLException e) {
                MiscUtils.getLogger().error("Error", e);
            }
   }*/
   public static boolean isQueueExist(String qn){
       String qid=getQueueid(qn);
       if(qid.length()==0)
           return false;
       else
           return true;
   }
//public static String getLastId(){return null;}
   public static String getLastId(){
       String retId="";
    try{
        DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
        String sql="select max(id) from queue";
        ResultSet rs = db.GetSQL(sql);
        if (rs.first()) {
           retId=oscar.Misc.getString(rs, 1);
        }

    }catch(SQLException e){
        MiscUtils.getLogger().error("Error", e);
    }
       return retId;
   }
   
   public static boolean addNewQueue(String qn){
       try{
           if(!isQueueExist(qn)){
                DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
                String sql = "INSERT INTO queue (name) " + "VALUES ('" + qn+ "')";
                MiscUtils.getLogger().debug("sql="+sql);
                db.RunSQL(sql);
                return true;
           }else
               return false;
       }catch (SQLException e) {
                MiscUtils.getLogger().error("Error", e);
                return false;
       }
   }
}
