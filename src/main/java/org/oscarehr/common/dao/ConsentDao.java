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

import java.util.Date;
import java.util.List;

import javax.persistence.Query;
import javax.persistence.TemporalType;

import org.oscarehr.common.model.Consent;
import org.springframework.stereotype.Repository;

@Repository
public class ConsentDao extends AbstractDao<Consent> {

	protected ConsentDao() {
		super(Consent.class);
	}
	
	/**
	 * This query should never return more than one consentType. 
	 * @param int demographic_no
	 * @param int consentTypeId
	 */
	public Consent findByDemographicAndConsentTypeId( int demographic_no, int consentTypeId ) {
		String sql = "select x from "+modelClass.getSimpleName()+" x where x.demographicNo=?1 and x.consentTypeId=?2";
    	Query query = entityManager.createQuery(sql);
    	query.setParameter( 1, demographic_no );
    	query.setParameter( 2, consentTypeId );

        Consent consent = getSingleResultOrNull(query);
        return consent;
	}
	
	public List<Consent> findByDemographic( int demographic_no ) {
		String sql = "select x from "+modelClass.getSimpleName()+" x where x.demographicNo=?1";
    	Query query = entityManager.createQuery(sql);
    	query.setParameter( 1, demographic_no );

        @SuppressWarnings("unchecked")
		List<Consent> consent = query.getResultList();
        return consent;
	}
	
	public  List<Consent> findLastEditedByConsentTypeId( int consentTypeId, Date lastEditDate ) {
		String sql = "SELECT x FROM " 
					+ modelClass.getSimpleName() 
					+ " x WHERE x.consentTypeId = ?1"
					+ " AND x.editDate  > ?2 ";
		
    	Query query = entityManager.createQuery(sql);
    	query.setParameter( 1, consentTypeId );
    	query.setParameter( 2, lastEditDate, TemporalType.TIMESTAMP );

    	@SuppressWarnings("unchecked")
		List<Consent> consents = query.getResultList();
        return consents;
	}
	

}
