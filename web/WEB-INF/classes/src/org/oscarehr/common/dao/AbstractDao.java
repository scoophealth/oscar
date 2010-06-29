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

import javax.persistence.EntityManager;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.oscarehr.common.model.AbstractModel;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public abstract class AbstractDao<T extends AbstractModel<?>>
{
	protected Class<T> modelClass;
    
    @PersistenceContext
    protected EntityManager entityManager = null;

    protected AbstractDao(Class<T> modelClass)
    {
    	this.modelClass=modelClass;
    }
    
    /**
     * aka update
     */
	public void merge(T o)
	{
        entityManager.merge(o);
    }

    /**
     * aka create
     */
	public void persist(T o)
	{
        entityManager.persist(o);
    }

    /**
     * You can only remove attached instances.
     */
	public void remove(T o)
	{
        entityManager.remove(o);
    }

    /**
     * You can only refresh attached instances.
     */
	public void refresh(T o)
	{
        entityManager.refresh(o);
    }

    public T find(Object id)
	{
		return(entityManager.find(modelClass, id));
	}

	public void remove(Object id)
	{
		T abstractModel=find(id);
		if (abstractModel!=null) remove(abstractModel);
	}

    protected T getSingleResultOrNull(Query query)
    {
        @SuppressWarnings("unchecked")
		List<T> results=query.getResultList();
        if (results.size()==1) return(results.get(0));
        else if (results.size()==0) return(null);
        else throw(new NonUniqueResultException("SingleResult requested but result was not unique : "+results.size()));
    }
    
	public int getCountAll()
	{
		String sqlCommand="select count(*) from "+modelClass.getSimpleName();
		Query query = entityManager.createNativeQuery(sqlCommand, Integer.class);		
		return((Integer)query.getSingleResult());
	}
}
