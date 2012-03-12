package org.oscarehr.common.dao;

import java.util.ArrayList;

import javax.persistence.Query;

import org.oscarehr.common.model.ClinicNbr;
import org.springframework.stereotype.Repository;

@Repository
public class ClinicNbrDao extends AbstractDao<ClinicNbr> {
	
	public ClinicNbrDao() {
		super(ClinicNbr.class);
	}
	
	public ArrayList<ClinicNbr> findAll() {
		Query query = entityManager.createQuery("select x from "+modelClass.getSimpleName()+" x where nbr_status != 'D' order by nbr_value asc");
		@SuppressWarnings("unchecked")
		ArrayList<ClinicNbr> results= new ArrayList<ClinicNbr>(query.getResultList());
		return(results);
	}	
	
	public Integer removeEntry(Integer id) {
		try {
			ClinicNbr clinicNbr = find(id);
			clinicNbr.setNbrStatus("D");
			merge(clinicNbr);
			return id;
		} catch (Exception e) {
			return 0;
		}
	}
	
	public int addEntry(String nbrValue, String nbrString) {
		try {
			ClinicNbr clinicNbr = new ClinicNbr();
			clinicNbr.setNbrValue(nbrValue);
			clinicNbr.setNbrString(nbrString);
			persist(clinicNbr);
			return clinicNbr.getId();
		} catch (Exception e) {
			return 0;
		}
	}	
}
