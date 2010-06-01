 /**
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
 * Ontario, Canada   Creates a new instance of FileUploadCheck
 *
 *
 * FileUploadCheck.java
 *
 * Created on August 31, 2005, 7:33 PM
 */

package oscar.oscarLab;

import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Hashtable;

import org.apache.commons.lang.StringEscapeUtils;

import oscar.oscarDB.DBHandler;
import oscar.util.MD5;

/**
 *
 * @author Jay Gallagher
 */
public class FileUploadCheck {
  /* 
  CREATE TABLE fileUploadCheck (
  id int(10) NOT NULL auto_increment,
  provider_no varchar(6) NOT NULL default '',
  filename varchar(255) NOT NULL default '',
  md5sum varchar(255),  
  date_time  datetime NOT NULL,    
  PRIMARY KEY  (id)
)
   */
   
   public FileUploadCheck() {
   }
   
   public boolean hasFileBeenUploaded(InputStream is){      
      String md5sum = getMd5Sum(is);     
      return hasFileBeenUploaded(md5sum);
   }
   
   
   public boolean hasFileBeenUploaded(String md5sum){
      boolean hasFileBeenUploaded = false;
      try{         
         DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
         String sql = "select * from fileUploadCheck where md5sum = '"+md5sum+"' ";
         ResultSet rs = db.GetSQL(sql);
         if(rs.next()){
            hasFileBeenUploaded = true;
         }         
      }catch(Exception e){
         e.printStackTrace();
      }
      return hasFileBeenUploaded;
   }
   
   public String getMd5Sum(InputStream is){  
      String md5sum = null;
      try{
         md5sum = MD5.getHashString(is);                  
      }catch(Exception e){
         e.printStackTrace();
      }
      return md5sum;
   }
   
   public Hashtable getFileInfo(String md5sum){
      Hashtable fileInfo = new Hashtable();
      try{         
         DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
         String sql = "select * from fileUploadCheck where md5sum = '"+md5sum+"' ";
         ResultSet rs = db.GetSQL(sql);
         if(rs.next()){
            fileInfo.put("providerNo",db.getString(rs,"provider_no"));
            fileInfo.put("filename", db.getString(rs,"filename"));
            fileInfo.put("md5sum", db.getString(rs,"md5sum"));
            fileInfo.put("dateTime",db.getString(rs,"date_time"));
         }         
      }catch(Exception e){
         e.printStackTrace();
      }
      return fileInfo;
   }
   
   
   public static final int UNSUCCESSFUL_SAVE = -1;
   /**
    *Used to add a new file to the database, checks to see if it already has been added
    */
   public synchronized int addFile(String name, InputStream is,String provider) throws Exception{
      int fileUploaded = UNSUCCESSFUL_SAVE;
      try{
         String md5sum = getMd5Sum(is);
         if(!hasFileBeenUploaded(md5sum)){           
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
            String sql = "insert into fileUploadCheck (provider_no,filename,md5sum,date_time) values ('"+provider+"','"+StringEscapeUtils.escapeSql(name)+"','"+md5sum+"',now())";
            System.out.println(sql);
            db.RunSQL(sql);
            ResultSet rs = db.GetSQL("SELECT LAST_INSERT_ID() ");
            if(rs.next()){
                fileUploaded= rs.getInt(1) ;
            }       
         }
      }catch(SQLException conE){
         conE.printStackTrace();            
         throw new Exception("Database Is not Running");
      }catch(Exception e){
         e.printStackTrace();
      }
      System.out.println("returning "+fileUploaded);
      return fileUploaded;
   }
  
}
