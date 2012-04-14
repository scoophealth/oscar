package org.oscarehr.common.dao;

import java.util.List;

import javax.persistence.Query;

import org.oscarehr.common.model.ProviderDefaultProgram;
import org.springframework.stereotype.Repository;

@Repository
public class ProviderDefaultProgramDao extends AbstractDao<ProviderDefaultProgram>{

	public ProviderDefaultProgramDao() {
		super(ProviderDefaultProgram.class);
	}

    public List<ProviderDefaultProgram> getProgramByProviderNo(String providerNo) {
        String q = "SELECT pdp FROM ProviderDefaultProgram pdp WHERE pdp.providerNo=?";
        Query query = entityManager.createQuery(q);
        query.setParameter(1, providerNo);
        @SuppressWarnings("unchecked")
        List<ProviderDefaultProgram> results = query.getResultList();
        return results;
    }

    public void setDefaultProgram(String providerNo, int programId) {
        List<ProviderDefaultProgram> rs = getProgramByProviderNo(providerNo);
        ProviderDefaultProgram pdp = null;
        if (rs.size() == 0) {
            pdp = new ProviderDefaultProgram();
            pdp.setProviderNo(providerNo);
            pdp.setSign(false);
            pdp.setProgramId(programId);
            merge(pdp);
        }
        else {
            pdp = rs.get(0);
            pdp.setProgramId(programId);
            persist(pdp);
        }
    }

    public List<ProviderDefaultProgram> getProviderSig(String providerNo) {
        List<ProviderDefaultProgram> rs = getProgramByProviderNo(providerNo);
        return rs;
    }

    public void saveProviderDefaultProgram(ProviderDefaultProgram pdp) {
       if(pdp.getId() == null || pdp.getId().intValue() == 0) {
    	   persist(pdp);
       } else {
    	   merge(pdp);
       }
    }

    public void toggleSig(String providerNo) {
       List<ProviderDefaultProgram> rs = this.getProgramByProviderNo(providerNo);
       for(ProviderDefaultProgram pdp:rs) {
    	   pdp.setSign(!pdp.isSign());
    	   merge(pdp);
       }
    }

}
