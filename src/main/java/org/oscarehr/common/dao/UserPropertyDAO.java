/**
 *
 * Copyright (c) 2005-2012. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved.
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
 * This software was written for
 * Centre for Research on Inner City Health, St. Michael's Hospital,
 * Toronto, Ontario, Canada
 */


package org.oscarehr.common.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import org.oscarehr.common.model.UserProperty;
import org.springframework.stereotype.Repository;

/**
 *
 * @author rjonasz
 */
@Repository
public class UserPropertyDAO extends AbstractDao<UserProperty> {

	public final static String COLOR_PROPERTY = "ProviderColour";

    /** Creates a new instance of UserPropertyDAO */
    public UserPropertyDAO() {
    	super(UserProperty.class);
    }


    public void delete(UserProperty prop) {
        remove(prop.getId());
    }
    
    public void saveProp(String provider, String userPropertyName, String value){
    	UserProperty prop = getProp(provider, userPropertyName);
        if( prop == null ) {
           prop = new UserProperty();
           prop.setProviderNo(provider);
           prop.setName(userPropertyName);
        }
	    prop.setValue(value);     
		saveProp(prop);
    }
    

    public void saveProp(UserProperty prop) {
        if(prop.getId() != null && prop.getId().intValue()>0) {
        	merge(prop);
        } else {
        	persist(prop);
        }
    }

    //Should properties be updateable?
    public void saveProp(String name, String val) {
        if (val != null) {
                UserProperty prop = getProp(name);
                if (prop == null) {
                        prop = new UserProperty();
                        prop.setName(name);
                }
                prop.setValue(val);
                saveProp(prop);
        }
    }

    public String getStringValue(String provider,String propertyName){
    	try {
    		return getProp(provider,propertyName).getValue();
        } catch (Exception e) {
        	return null;
        }
    }
    
    public List<UserProperty> getAllProperties(String name, List<String> list) {
        Query query = entityManager.createQuery("select p from UserProperty p where p.name = :name and p.provider_no in :list");
        query.setParameter("name", name);
        query.setParameter("list", list);
        
        return query.getResultList();
    }
    
    public List<UserProperty> getPropValues(String name, String value) {
        Query query = entityManager.createQuery("select p from UserProperty p where p.name = :name and p.value = :value");
        query.setParameter("name", name);
        query.setParameter("value", value);
        
        return query.getResultList();
    }
    
    public UserProperty getProp(String prov, String name) {
    	Query query = entityManager.createQuery("select p from UserProperty p where p.providerNo = ? and p.name = ?");
    	query.setParameter(1, prov);
    	query.setParameter(2, name);

        @SuppressWarnings("unchecked")
        List<UserProperty> list = query.getResultList();
        if( list != null && list.size() > 0 ) {
            UserProperty prop = list.get(0);
            return prop;
        }
        else
            return null;
    }

    public UserProperty getProp(String name) {
    	Query query = entityManager.createQuery("select p from UserProperty p where p.name = ?");
    	query.setParameter(1, name);

        @SuppressWarnings("unchecked")
        List<UserProperty> list = query.getResultList();
        if( list != null && list.size() > 0 ) {
            UserProperty prop = list.get(0);
            return prop;
        }
        else
            return null;
    }

    public List<UserProperty> getDemographicProperties(String providerNo) {
    	Query query = entityManager.createQuery("select p from UserProperty p where p.providerNo = ?");
    	query.setParameter(1, providerNo);

        @SuppressWarnings("unchecked")
        List<UserProperty> list = query.getResultList();

    	return list;
    }

    public Map<String,String> getProviderPropertiesAsMap(String providerNo) {
    	Map<String,String> map = new HashMap<String,String>();

    	Query query = entityManager.createQuery("select p from UserProperty p where p.providerNo = ?");
    	query.setParameter(1, providerNo);

    	@SuppressWarnings("unchecked")
    	List<UserProperty> list = query.getResultList();
    	for(UserProperty p:list) {
    		map.put(p.getName(), p.getValue());
    	}
    	return map;
    }

    public void saveProperties(String providerNo, Map<String,String> props) {
    	for(String key:props.keySet()) {
    		String value = props.get(key);
    		if(value == null) value = new String();
    		UserProperty prop  = null;
    		if((prop=this.getProp(providerNo, key)) != null) {
    			prop.setValue(value);
    		} else {
    			prop = new UserProperty();
    			prop.setName(key);
    			prop.setProviderNo(providerNo);
    			prop.setValue(value);
    		}
    		saveProp(prop);
    	}
    }
}
