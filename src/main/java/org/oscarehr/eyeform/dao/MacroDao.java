package org.oscarehr.eyeform.dao;

import java.util.List;

import javax.persistence.Query;

import org.oscarehr.common.dao.AbstractDao;
import org.oscarehr.eyeform.model.Macro;
import org.springframework.stereotype.Repository;

@Repository
public class MacroDao extends AbstractDao<Macro> {

	public MacroDao() {
		super(Macro.class);
	}
	
	public List<Macro> getAll() {
		String sql="select x from "+modelClass.getSimpleName()+" x";
		Query query = entityManager.createQuery(sql);
		
		@SuppressWarnings("unchecked")
	    List<Macro> results=query.getResultList();
	    return(results);		   
	}
}
