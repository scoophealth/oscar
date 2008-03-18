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
public class TeleplanMessagesDAO {
    static Log log = LogFactory.getLog(TeleplanSequenceDAO.class);
    
    /** Creates a new instance of TeleplanSequenceDAO */
    public TeleplanMessagesDAO() {
    }
    
    private void setMessage(String sequenceNum){
        try{
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
            String query = "insert into property (name,value) values ('teleplan_message','"+sequenceNum+"') " ;
            db.RunSQL(query);           
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    private void updateMessage(String sequenceNum){
        try{
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
            String query = "update property set value = '"+sequenceNum+"' where name = 'teleplan_message' " ;
            db.RunSQL(query);            
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    private boolean hasMessage(){
        boolean hasSequence = false;
        try{
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
            String query = "select value from property where name = 'teleplan_message' " ;
            ResultSet rs = db.GetSQL(query); 
            if(rs.next()){
                hasSequence = true;
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return hasSequence;
    }
    
    public void saveUpdateMessage(String sequenceNum){
        if(hasMessage()){
            updateMessage(sequenceNum);
        }else{
            setMessage(sequenceNum);
        }  
    }
    
     
  
    
}
