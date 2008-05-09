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
 * Jason Gallagher
 *
 * UserPropertyDAO.java
 *
 * Created on December 19, 2007, 4:29 PM
 *
 *
 *
 */

package org.oscarehr.common.dao;

import java.util.List;

import org.oscarehr.common.model.CountryCode;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 *
 * @author Jason Gallagher
 */
public class CountryCodeDAO extends HibernateDaoSupport {
    
    /** Creates a new instance of UserPropertyDAO */
    public CountryCodeDAO() {
    }
   
    public List<CountryCode> getAllCountryCodes(){
        List<CountryCode> codeList = this.getHibernateTemplate().find("from CountryCode");
        return codeList;
    }
   
    //NOT USED YET
    public List<CountryCode> getAllCountryCodes(String locale){
        List<CountryCode> codeList = this.getHibernateTemplate().find("from CountryCode c where c.cLocale = ?",locale);
        return codeList;
    }
    
    public CountryCode getCountryCode(String countryCode){
        List<CountryCode> codeList = this.getHibernateTemplate().find("from CountryCode c where c.countryId = ?",countryCode);
        CountryCode code = null;
        if (codeList != null && codeList.size() >0){
            code = codeList.get(0);
        }
        return code;
    }
    
    
    //NOT USED YET
    public CountryCode getCountryCode(String countryCode,String locale){
        List<CountryCode> codeList = this.getHibernateTemplate().find("from CountryCode c where c.countryId = ? and c.cLocale = ?",new Object[] {countryCode,locale});
        CountryCode code = null;
        if (codeList != null && codeList.size() >0){
            code = codeList.get(0);
        }
        return code;
    }
    
    
    
//    public UserDSMessagePrefs getMessagePrefsOnType(String prov, String name) {
//        List list = this.getHibernateTemplate().find("from UserDSMessagePrefs p where p.providerNo = ? and p.resourceType = ? and p.archived = false ", new Object[] {prov,name});
//        if( list != null && list.size() > 0 ) {
//            UserDSMessagePrefs prop = (UserDSMessagePrefs)list.get(0);
//            return prop;
//        }
//        else
//            return null;            
//    }
//    
//    
//    
//    public Hashtable getHashofMessages(String providerNo,String name){
//        Hashtable retHash = new Hashtable();
//        List<UserDSMessagePrefs> list = this.getHibernateTemplate().find("from UserDSMessagePrefs p where p.providerNo = ? and p.resourceType = ? and p.archived = false", new Object[] {providerNo,name});
//        if( list != null && list.size() > 0 ) {
//            for(UserDSMessagePrefs pref: list){
//                retHash.put(pref.getResourceType()+pref.getResourceId(),pref.getResourceUpdatedDate().getTime());
//            }
//        }
//        return retHash;
//    }
    
}
