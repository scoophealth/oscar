package org.oscarehr.common.dao;

import java.util.HashMap;
import java.util.List;

import javax.persistence.Query;

import org.oscarehr.common.model.FlowSheetDx;
import org.springframework.stereotype.Repository;

@Repository
public class FlowSheetDxDao extends AbstractDao<FlowSheetDx>{

	public FlowSheetDxDao() {
		super(FlowSheetDx.class);
	}

    public List<FlowSheetDx> getFlowSheetDx(String flowsheet,String demographic){
    	Query query = entityManager.createQuery("select fd from FlowSheetDx fd where fd.flowsheet = ? and fd.archived=0 and fd.demographicNo=?");
    	query.setParameter(1,flowsheet);
    	query.setParameter(2, Integer.parseInt(demographic));
    	@SuppressWarnings("unchecked")
        List<FlowSheetDx> fds = query.getResultList();

    	return fds;
     }

     public HashMap<String,String> getFlowSheetDxMap(String flowsheet,String demographic){
         List<FlowSheetDx> fldx = getFlowSheetDx( flowsheet, demographic);
         HashMap<String,String> hm = new HashMap<String,String>();

         for (FlowSheetDx fs : fldx){
             hm.put(fs.getDxCodeType()+fs.getDxCode(), fs.getProviderNo());
         }
         return hm;

     }

}
