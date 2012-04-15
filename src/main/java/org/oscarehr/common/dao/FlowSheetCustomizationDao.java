package org.oscarehr.common.dao;

import java.util.List;

import javax.persistence.Query;

import org.oscarehr.common.model.FlowSheetCustomization;
import org.springframework.stereotype.Repository;

@Repository
public class FlowSheetCustomizationDao extends AbstractDao<FlowSheetCustomization>{

	public FlowSheetCustomizationDao() {
		super(FlowSheetCustomization.class);
	}

    public FlowSheetCustomization getFlowSheetCustomization(String id){
    	return this.find(Integer.valueOf(id));
    }

    public List<FlowSheetCustomization> getFlowSheetCustomizations(String flowsheet,String provider,String demographic){
    	Query query = entityManager.createQuery("SELECT fd FROM FlowSheetDrug fd WHERE fd.flowsheet=? and fd.archived=0 and ( ( fd.providerNo = ?  and fd.demographicNo = 0) or (fd.providerNo =? and fd.demographicNo = ?  ) )");
    	query.setParameter(1, flowsheet);
    	query.setParameter(2, provider);
    	query.setParameter(3, provider);
    	query.setParameter(4, Integer.parseInt(demographic));

        @SuppressWarnings("unchecked")
        List<FlowSheetCustomization> list = query.getResultList();
        return list;
    }
}
