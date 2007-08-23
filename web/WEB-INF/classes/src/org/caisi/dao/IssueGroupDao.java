/*
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

package org.caisi.dao;

import java.util.List;

import org.caisi.model.IssueGroup;
import org.hibernate.Query;
import org.hibernate.Session;

public class IssueGroupDao extends AbstractDao {

	public IssueGroup find(int id) {

		Session session = sessionFactory.openSession();
		try {
			return((IssueGroup)session.get(IssueGroup.class, id));
		}
		finally {
			session.close();
		}
	}

    public List<IssueGroup> findAll() {

		Session session = sessionFactory.openSession();
		try {
			Query query=session.createQuery("from IssueGroup");
			
			@SuppressWarnings("unchecked")
			List<IssueGroup> results=query.list();
			
			return(results);
		}
		finally {
			session.close();
		}
	}

}
