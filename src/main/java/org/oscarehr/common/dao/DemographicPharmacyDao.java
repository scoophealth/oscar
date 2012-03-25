package org.oscarehr.common.dao;

import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.oscarehr.common.model.DemographicPharmacy;
import org.springframework.stereotype.Repository;

@Repository
public class DemographicPharmacyDao extends AbstractDao<DemographicPharmacy>{

	public DemographicPharmacyDao() {
		super(DemographicPharmacy.class);
	}

	   public void addPharmacyToDemographic(String pharmacyId,String demographicNo){
		   DemographicPharmacy dp = new DemographicPharmacy();
		   dp.setAddDate(new Date());
		   dp.setStatus("1");
		   dp.setDemographicNo(Integer.parseInt(demographicNo));
		   dp.setPharmacyId(Integer.parseInt(pharmacyId));
		   persist(dp);
	   }

	   public DemographicPharmacy findByDemographicId(String demographicNo){
		      DemographicPharmacy record = null;
		      String sql = "select x from DemographicPharmacy x where x.status=? and x.demographicNo=? order by x.addDate desc";
		      Query query = entityManager.createQuery(sql);
		      query.setParameter(1,"1");
		      query.setParameter(2,Integer.parseInt(demographicNo));
		      @SuppressWarnings("unchecked")
		      List<DemographicPharmacy> results = query.getResultList();
		      if(results.size()>0) {
		    	  return results.get(0);
		      }
		      return record;
	   }
}
