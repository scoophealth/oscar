package org.oscarehr.common.dao;

import java.util.List;

import javax.persistence.Query;

import org.oscarehr.common.model.Desaprisk;
import org.springframework.stereotype.Repository;

@Repository
public class DesapriskDao extends AbstractDao<Desaprisk> {

	public DesapriskDao() {
		super(Desaprisk.class);
	}

	public Desaprisk search(Integer formNo, Integer demographicNo) {

    	String sqlCommand = "select x from Desaprisk x where x.formNo <= ? and x.demographicNo=? order by x.formNo DESC, x.desapriskDate DESC, x.desapriskTime DESC";

        Query query = entityManager.createQuery(sqlCommand);
        query.setParameter(1, formNo);
        query.setParameter(2, demographicNo);

        @SuppressWarnings("unchecked")
        List<Desaprisk> results = query.getResultList();

        if(results.size()>0 ){
        	return results.get(0);
        }
        return null;
    }
}
