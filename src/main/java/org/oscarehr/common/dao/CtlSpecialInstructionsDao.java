package org.oscarehr.common.dao;

import java.util.List;

import javax.persistence.Query;

import org.oscarehr.common.model.CtlSpecialInstructions;
import org.springframework.stereotype.Repository;

@Repository
public class CtlSpecialInstructionsDao extends AbstractDao<CtlSpecialInstructions>{

	public CtlSpecialInstructionsDao() {
		super(CtlSpecialInstructions.class);
	}

	public List<CtlSpecialInstructions> findAll() {
	  	String sql = "select x from CtlSpecialInstructions x";
    	Query query = entityManager.createQuery(sql);

        @SuppressWarnings("unchecked")
        List<CtlSpecialInstructions> results = query.getResultList();
        return results;
	}
}
