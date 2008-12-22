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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import javax.persistence.PersistenceException;
import javax.persistence.Query;

import org.oscarehr.common.model.Prevention;
import org.oscarehr.util.DbConnectionFilter;
import org.springframework.stereotype.Repository;

@Repository
public class PreventionDao extends AbstractDao {

	public Prevention find(Integer id) {
		return (entityManager.find(Prevention.class, id));
	}

	public List<Prevention> findNotDeletedByDemographicId(Integer demographicId) {
		Query query = entityManager.createQuery("select x from Prevention x where demographicId=?1 and deleted=?2");
		query.setParameter(1, demographicId);
		query.setParameter(2, false);

		@SuppressWarnings("unchecked")
		List<Prevention> results = query.getResultList();

		return (results);
	}

	public HashMap<String, String> getPreventionExt(Integer preventionId) {
		try {
			Connection c = DbConnectionFilter.getThreadLocalDbConnection();
			PreparedStatement ps = c.prepareStatement("select * from preventionsExt where prevention_id=?");
			ps.setInt(1, preventionId);
			ResultSet rs = ps.executeQuery();

			HashMap<String, String> results=new HashMap<String, String>();
			while (rs.next()) {
				results.put(rs.getString("keyval"), rs.getString("val"));
			}
			
			return(results);
		}
		catch (SQLException e) {
			throw(new PersistenceException(e));
		}
		finally {
			DbConnectionFilter.releaseThreadLocalDbConnection();
		}
	}
}
