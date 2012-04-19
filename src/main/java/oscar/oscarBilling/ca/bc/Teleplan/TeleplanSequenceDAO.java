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


package oscar.oscarBilling.ca.bc.Teleplan;

import java.sql.ResultSet;

import org.apache.log4j.Logger;
import org.oscarehr.util.MiscUtils;

import oscar.oscarDB.DBHandler;

/**
 *  Deals with storing the teleplan sequence #
 * @author jay
 */
public class TeleplanSequenceDAO {
    static Logger log=MiscUtils.getLogger();
    
    /** Creates a new instance of TeleplanSequenceDAO */
    public TeleplanSequenceDAO() {
    }
    
    private void setSequence(int sequenceNum){
        try{
            
            String query = "insert into property (name,value) values ('teleplan_sequence','"+sequenceNum+"') " ;
            DBHandler.RunSQL(query);           
        }catch(Exception e){
            MiscUtils.getLogger().error("Error", e);
        }
    }
    
    private void updateSequence(int sequenceNum){
        try{
            
            String query = "update property set value = '"+sequenceNum+"' where name = 'teleplan_sequence' " ;
            DBHandler.RunSQL(query);            
        }catch(Exception e){
            MiscUtils.getLogger().error("Error", e);
        }
    }
    
    private boolean hasSequence(){
        boolean hasSequence = false;
        try{
            
            String query = "select value from property where name = 'teleplan_sequence' " ;
            ResultSet rs = DBHandler.GetSQL(query); 
            if(rs.next()){
                hasSequence = true;
            }
        }catch(Exception e){
            MiscUtils.getLogger().error("Error", e);
        }
        return hasSequence;
    }
    
    public void saveUpdateSequence(int sequenceNum){
        if(hasSequence()){
            updateSequence(sequenceNum);
        }else{
            setSequence(sequenceNum);
        }  
    }
    
     public int getLastSequenceNumber(){
        //insert into property (name,value) values ('teleplan_sequence','3430') 
        int sequenceNum = 0; 
        try{
            
            String query = "select value from property where name='teleplan_sequence'  " ;
            log.debug("1st billing query "+query);
            ResultSet rs = DBHandler.GetSQL(query);
        while (rs.next()){
            String value = rs.getString("value");
            sequenceNum = Integer.parseInt(value);
        }     
        }catch(Exception e){
            MiscUtils.getLogger().error("Error", e);
        }
        return sequenceNum;   
    }
     
  
    
}
