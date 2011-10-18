/**
 * Copyright (c) 2007-2008. CAISI, Toronto. All Rights Reserved.
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
 * This software was written for 
 * CAISI, 
 * Toronto, Ontario, Canada 
 */
package org.oscarehr.common.dao;

import java.util.List;

import javax.persistence.Query;

import org.oscarehr.common.model.DemographicArchive;
import org.springframework.stereotype.Repository;
import oscar.util.StringUtils;

@Repository
public class DemographicArchiveDao extends AbstractDao {

	public DemographicArchiveDao() {
		super(DemographicArchive.class);
	}

    public List<DemographicArchive> findByDemographicNo(Integer demographicNo) {

    	String sqlCommand = "select x from DemographicArchive x where x.demographicNo=?1";

        Query query = entityManager.createQuery(sqlCommand);
        query.setParameter(1, demographicNo);

        @SuppressWarnings("unchecked")
        List<DemographicArchive> results = query.getResultList();
        
        return (results);
    }

    public List<DemographicArchive> findRosterStatusHistoryByDemographicNo(Integer demographicNo) {
        
    	String sqlCommand = "select x from DemographicArchive x where x.demographicNo=?1 order by x.id desc";

        Query query = entityManager.createQuery(sqlCommand);
        query.setParameter(1, demographicNo);

        @SuppressWarnings("unchecked")
        List<DemographicArchive> results = query.getResultList();

        //Remove entries with identical rosterStatus
        String this_rs, next_rs;
        for (int i=0; i<results.size()-1; i++) {
            this_rs = results.get(i).getRosterStatus();
            next_rs = results.get(i+1).getRosterStatus();
            if (StringUtils.empty(this_rs)) {
                if (StringUtils.empty(next_rs)) {
                    results.remove(i);
                    i--;
                }
            } else if (this_rs.equalsIgnoreCase(next_rs)) {
                results.remove(i);
                i--;
            }
        }
        return (results);
    }
}
