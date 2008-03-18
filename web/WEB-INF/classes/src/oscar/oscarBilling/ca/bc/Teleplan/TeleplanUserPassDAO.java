/*
 *  Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
 *  This software is published under the GPL GNU General Public License.
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU General Public License
 *  as published by the Free Software Foundation; either version 2
 *  of the License, or (at your option) any later version.
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 *
 *  Jason Gallagher
 *
 *  This software was written for the
 *  Department of Family Medicine
 *  McMaster University
 *  Hamilton
 *  Ontario, Canada
 *
 * TeleplanSequenceDAO.java
 *
 * Created on January 31, 2007, 12:01 AM
 *
 */

package oscar.oscarBilling.ca.bc.Teleplan;

import java.sql.ResultSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import oscar.oscarDB.DBHandler;

/**
 *  Deals with storing the teleplan sequence #
 * @author jay
 */
public class TeleplanUserPassDAO {
    static Log log = LogFactory.getLog(TeleplanUserPassDAO.class);
    
    /** Creates a new instance of TeleplanSequenceDAO */
    public TeleplanUserPassDAO() {
    }
    
    private void setUsername(String username){
        try{
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
            String query = "insert into property (name,value) values ('teleplan_username','"+username+"') " ;
            db.RunSQL(query);           
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    private void setPassword(String password){
        try{
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
            String query = "insert into property (name,value) values ('teleplan_password','"+password+"') " ;
            db.RunSQL(query);           
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    
    private void updateUsername(String username){
        try{
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
            String query = "update property set value = '"+username+"' where name = 'teleplan_username' " ;
            db.RunSQL(query);            
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    private void updatePassword(String password){
        try{
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
            String query = "update property set value = '"+password+"' where name = 'teleplan_password' " ;
            db.RunSQL(query);            
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public boolean hasUsernamePassword(){
        return hasUsername();
    }
    
    private boolean hasUsername(){
        boolean hasSequence = false;
        try{
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
            String query = "select value from property where name = 'teleplan_username' " ;
            ResultSet rs = db.GetSQL(query); 
            if(rs.next()){
                log.debug("has user Sequence"+rs.getString("value"));
                hasSequence = true;
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return hasSequence;
    }
    
    private boolean hasPassword(){
        boolean hasSequence = false;
        try{
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
            String query = "select value from property where name = 'teleplan_password' " ;
            ResultSet rs = db.GetSQL(query); 
            if(rs.next()){
                log.debug("has pass Sequence"+rs.getString("value"));
                hasSequence = true;
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return hasSequence;
    }
    
    public void saveUpdateUsername(String username){
        if(hasUsername()){
            log.debug("has username"+username);
            updateUsername(username);
        }else{
            log.debug("not username"+username);
            setUsername(username);
        }  
    }
    
     public void saveUpdatePasssword(String password){
        if(hasPassword()){
            log.debug("has password"+password);
            updatePassword(password);
        }else{
            log.debug("not password"+password);
            setPassword(password);
        }  
    }
    
    public String[] getUsernamePassword(){
        String[] str = new String[2];
        try{
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
            String query = "select value from property where name = 'teleplan_username' " ;
            ResultSet rs = db.GetSQL(query); 
            if(rs.next()){
                str[0] = rs.getString("value");
            }
            rs.close();
            query = "select value from property where name = 'teleplan_password' " ;
            rs = db.GetSQL(query); 
            if(rs.next()){
                str[1] = rs.getString("value");
            }
            rs.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return str;
    }
     
  
    
}
