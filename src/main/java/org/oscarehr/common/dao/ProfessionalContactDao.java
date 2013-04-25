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

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import org.apache.commons.lang.StringEscapeUtils;
import org.oscarehr.common.model.ProfessionalContact;
import org.springframework.stereotype.Repository;

@Repository
public class ProfessionalContactDao extends AbstractDao<ProfessionalContact> {

	public ProfessionalContactDao() {
		super(ProfessionalContact.class);
	}
	
	@Deprecated
	@SuppressWarnings("unchecked")
	//Only used in the migrate script.bad idea to try this under normal conditions.
	public List<ProfessionalContact> findAll() {
		Query query = createQuery("x", null);
		return query.getResultList();
	}
	
	public List<ProfessionalContact> search(String searchMode, String orderBy, String keyword) {
		StringBuilder where = new StringBuilder();
		List<String> paramList = new ArrayList<String>();
	    
		if(searchMode.equals("search_name")) {
			String[] temp = keyword.split("\\,\\p{Space}*");
			if(temp.length>1) {
		      where.append("c.lastName like ?1 and c.firstName like ?2");
		      paramList.add(temp[0]+"%");
		      paramList.add(temp[1]+"%");
		    } else {
		      where.append("c.lastName like ?1");
		      paramList.add(temp[0]+"%");
		    }
		}else {		
			where.append("c." + StringEscapeUtils.escapeSql(searchMode) + " like ?1");
			paramList.add(keyword+"%");
		}			
		String sql = "SELECT c from ProfessionalContact c where " + where.toString() + " order by " + orderBy;
		
		Query query = entityManager.createQuery(sql);
		for(int x=0;x<paramList.size();x++) {
			query.setParameter(x+1,paramList.get(x));
		}		
		
		@SuppressWarnings("unchecked")
		List<ProfessionalContact> contacts = query.getResultList();
		return contacts;
	}
	
}
