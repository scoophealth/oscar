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

import java.util.List;

import org.apache.log4j.Logger;
import org.oscarehr.common.dao.PropertyDao;
import org.oscarehr.common.model.Property;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

/**
 *  Deals with storing the teleplan sequence #
 * @author jay
 */
public class TeleplanSequenceDAO {
    static Logger log=MiscUtils.getLogger();
    private PropertyDao propertyDao = SpringUtils.getBean(PropertyDao.class);
    
    
    /** Creates a new instance of TeleplanSequenceDAO */
    public TeleplanSequenceDAO() {
    }
    
    private void setSequence(int sequenceNum){
    	Property p = new Property();
    	p.setName("teleplan_sequence");
    	p.setValue(String.valueOf(sequenceNum));
    	propertyDao.persist(p);
    }
    
    private void updateSequence(int sequenceNum){
    	List<Property> ps = propertyDao.findByName("teleplan_sequence");
    	for(Property p:ps) {
    		p.setValue(String.valueOf(sequenceNum));
    		propertyDao.merge(p);
    	}
    }
    
    private boolean hasSequence(){
        boolean hasSequence = false;
        List<Property> ps = propertyDao.findByName("teleplan_sequence");
        if(!ps.isEmpty())
        	hasSequence=true;
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
        int sequenceNum = 0; 
        List<Property> ps = propertyDao.findByName("teleplan_sequence");
        for(Property p:ps) {
        	sequenceNum = Integer.parseInt(p.getValue());
        }
        return sequenceNum;   
    }

}
