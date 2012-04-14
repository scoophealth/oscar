package org.oscarehr.common.dao;

import java.util.List;

import javax.persistence.Query;

import org.oscarehr.common.model.ScratchPad;
import org.springframework.stereotype.Repository;

@Repository
public class ScratchPadDao extends AbstractDao<ScratchPad>{

	public ScratchPadDao() {
		super(ScratchPad.class);
	}

	public boolean isScratchFilled(String providerNo) {
		String sSQL="SELECT s FROM ScratchPad s WHERE s.providerNo = ? order by s.id";
		Query query = entityManager.createQuery(sSQL);
		query.setParameter(1, providerNo);

		@SuppressWarnings("unchecked")
		List<ScratchPad> results = query.getResultList();
		if (results.size()>0 && results.get(0).getText().trim().length()>0){
		  return true;
		}
		return false;
	}
}
