package org.oscarehr.common.dao;

import java.util.List;

import javax.persistence.Query;

import org.oscarehr.common.model.FlowSheetDrug;
import org.springframework.stereotype.Repository;

@Repository
public class FlowSheetDrugDao extends AbstractDao<FlowSheetDrug>{

	public FlowSheetDrugDao() {
		super(FlowSheetDrug.class);
	}

    public FlowSheetDrug getFlowSheetDrug(String id){
        return this.find(Integer.valueOf(id));
    }

    public List<FlowSheetDrug> getFlowSheetDrugs(String flowsheet,String demographic){
    	Query query = entityManager.createQuery("SELECT fd FROM FlowSheetDrug fd WHERE fd.flowsheet=? and fd.archived=0 and fd.demographicNo=?");
    	query.setParameter(1, flowsheet);
    	query.setParameter(2, Integer.parseInt(demographic));

        @SuppressWarnings("unchecked")
        List<FlowSheetDrug> list = query.getResultList();
        return list;
    }

}
