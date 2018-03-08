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
import org.oscarehr.common.model.Contact;
import org.oscarehr.util.MiscUtils;
import org.springframework.stereotype.Repository;

@Repository
public class ContactDao extends AbstractDao<Contact> {

	public ContactDao() {
		super(Contact.class);
	}
	
	public List<Contact> search(String searchMode, String orderBy, String keyword, String programNo, String providerNo, String relatedTo) {
		StringBuilder where = new StringBuilder();
		List<Object> paramList = new ArrayList<Object>();
		int maxIDUsed =1;
		
		if(searchMode.equals("search_name")) {
			String[] temp = keyword.split("\\,\\p{Space}*");
			if(temp.length>1) {
		      where.append("c.lastName like ?1 and c.firstName like ?2");
		      paramList.add(temp[0].trim()+"%");
		      paramList.add(temp[1].trim()+"%");
		      maxIDUsed=2;
		    } else {
		      where.append("c.lastName like ?1");
		      paramList.add(temp[0].trim()+"%");
		    }
		}else {		
			where.append("c." + StringEscapeUtils.escapeSql(searchMode) + " like ?1");
			paramList.add(keyword.trim()+"%");
		}			
		/*
		if(programNo != null && !programNo.equals("0")) {
			where.append(" AND c.programNo = ?" + (maxIDUsed+1));
			paramList.add(Integer.parseInt(programNo));
		}*/
		
		if(programNo != null && !programNo.equals("") && !programNo.equals("0")) {
			where.append(" AND c.programNo IN (select pp.ProgramId from ProgramProvider pp where pp.ProviderNo = '"+providerNo+"') ");
		}
		
		//relatedTo is the patient we are searching a contact for
		//c.programNo (Integer)
		
		if(relatedTo != null && relatedTo.length()>0) {
			where.append(" AND c.programNo IN ( SELECT x.programId FROM Admission x WHERE x.programId IN (SELECT a.programId FROM Admission a WHERE a.clientId = " + Integer.parseInt(relatedTo) +" ) )  ");
		}
		
		
		String sql = "SELECT c from Contact c where " + where.toString() + " order by " + orderBy;
		MiscUtils.getLogger().debug(sql);
		Query query = entityManager.createQuery(sql);
		for(int x=0;x<paramList.size();x++) {
			query.setParameter(x+1,paramList.get(x));
		}		
		
		@SuppressWarnings("unchecked")
		List<Contact> contacts = query.getResultList();
		return contacts;
	}
	
}
