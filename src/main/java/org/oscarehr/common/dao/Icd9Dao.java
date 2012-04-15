/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.oscarehr.common.dao;

import java.util.List;

import javax.persistence.Query;

import org.oscarehr.common.model.Icd9;
import org.springframework.stereotype.Repository;

/**
 *
 * @author toby
 */
@Repository
public class Icd9Dao extends AbstractDao<Icd9>{

	public Icd9Dao() {
		super(Icd9.class);
	}

	public List<Icd9> getIcd9Code(String icdCode){
		Query query = entityManager.createQuery("select i from Icd9 where i.icd9=?");
		query.setParameter(1, icdCode);

		@SuppressWarnings("unchecked")
		List<Icd9> results = query.getResultList();

		return results;
	}


    public List<Icd9> getIcd9(String query) {
		Query q = entityManager.createQuery("select i from Icd9 where i.icd9 like ? or i.description like ? order by i.description");
		q.setParameter(1, "%"+query+"%");
		q.setParameter(2, "%"+query+"%");

		@SuppressWarnings("unchecked")
		List<Icd9> results = q.getResultList();

		return results;
    }

}
