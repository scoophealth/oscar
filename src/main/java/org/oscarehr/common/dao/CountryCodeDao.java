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


package org.oscarehr.common.dao;

import java.util.List;

import javax.persistence.Query;

import org.oscarehr.common.model.CountryCode;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Jason Gallagher
 */
@Repository
public class CountryCodeDao extends AbstractDao<CountryCode> {

    public CountryCodeDao() {
    	super(CountryCode.class);
    }
    
    public List<CountryCode> findAll(){
    	Query query = entityManager.createQuery("SELECT cc from CountryCode cc");
        @SuppressWarnings("unchecked")
        List<CountryCode> codeList = query.getResultList();
        return codeList;
    }

    public List<CountryCode> getAllCountryCodes(){
    	return findAll();
    }

    //NOT USED YET
    public List<CountryCode> getAllCountryCodes(String locale){
    	Query query = entityManager.createQuery("SELECT cc from CountryCode cc where cc.clocale = ?");
    	query.setParameter(1, locale);
        @SuppressWarnings("unchecked")
        List<CountryCode> codeList = query.getResultList();
        return codeList;
    }

    public CountryCode getCountryCode(String countryCode){
    	Query query = entityManager.createQuery("SELECT cc from CountryCode  cc where cc.countryId = ?");
    	query.setParameter(1, countryCode);
        @SuppressWarnings("unchecked")
        List<CountryCode> codeList = query.getResultList();
        if (codeList.size() >0){
            return  codeList.get(0);
        }
        return null;
    }


    //NOT USED YET
    public CountryCode getCountryCode(String countryCode,String locale){
    	Query query = entityManager.createQuery("SELECT cc from CountryCode cc where cc.countryId = ? and cc.clocale=?");
    	query.setParameter(1, countryCode);
        query.setParameter(2, locale);
        @SuppressWarnings("unchecked")
        List<CountryCode> codeList = query.getResultList();

    	CountryCode code = null;
        if (codeList != null && codeList.size() >0){
            code = codeList.get(0);
        }
        return code;
    }
}
