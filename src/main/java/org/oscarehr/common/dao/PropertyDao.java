package org.oscarehr.common.dao;

import java.util.List;

import javax.persistence.Query;

import org.oscarehr.common.model.Property;
import org.springframework.stereotype.Repository;

@Repository
public class PropertyDao extends AbstractDao<Property> {

	public PropertyDao() {
		super(Property.class);
	}

	/**
     * Find all ordered by name.
     * @param active, null is find all, true is find only active, false is find only inactive.
     */
    public List<Property> findByName(String name)
	{
    	String sqlCommand="select x from "+modelClass.getSimpleName()+" x where x.name=?1";
		Query query = entityManager.createQuery(sqlCommand);

		query.setParameter(1, name);
		
		@SuppressWarnings("unchecked")
		List<Property> results = query.getResultList();

		return(results);
	}
    
    public Property checkByName(String name) {
    	
		String sql = " select x from " + this.modelClass.getName() + " x where x.name='"+name+"'";
		Query query = entityManager.createQuery(sql);		

		return (Property)query.getSingleResult();
		
	}
}
