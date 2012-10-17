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
public class TeleplanUserPassDAO {
    static Logger log=MiscUtils.getLogger();
    private PropertyDao propertyDao = SpringUtils.getBean(PropertyDao.class);
    
    
    public TeleplanUserPassDAO() {
    }
    
    private void setUsername(String username){
    	Property p = new Property();
    	p.setName("teleplan_username");
    	p.setValue(username);
    	propertyDao.persist(p);
    }
    
    private void setPassword(String password){
    	Property p = new Property();
    	p.setName("teleplan_password");
    	p.setValue(password);
    	propertyDao.persist(p);
    }
    
    
    private void updateUsername(String username){
    	List<Property> ps = propertyDao.findByName("teleplan_username");
    	for(Property p:ps) {
    		p.setValue(username);
    		propertyDao.merge(p);
    	}
    }
    
    private void updatePassword(String password){
    	List<Property> ps = propertyDao.findByName("teleplan_password");
    	for(Property p:ps) {
    		p.setValue(password);
    		propertyDao.merge(p);
    	}
    }
    
    public boolean hasUsernamePassword(){
        return hasUsername();
    }
    
    private boolean hasUsername(){
    	boolean hasSequence = false;
        List<Property> ps = propertyDao.findByName("teleplan_username");
        if(!ps.isEmpty())
        	hasSequence=true;
        return hasSequence;
    }
    
    private boolean hasPassword(){
    	boolean hasSequence = false;
        List<Property> ps = propertyDao.findByName("teleplan_password");
        if(!ps.isEmpty())
        	hasSequence=true;
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
        List<Property> ps = propertyDao.findByName("teleplan_username");
        for(Property p:ps) {
        	str[0] = p.getValue();
        }
        ps = propertyDao.findByName("teleplan_password");
        for(Property p:ps) {
        	str[1] = p.getValue();
        }
        return str;
    }
     
  
    
}
