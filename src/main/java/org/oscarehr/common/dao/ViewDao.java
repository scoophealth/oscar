package org.oscarehr.common.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import org.oscarehr.common.model.View;
import org.springframework.stereotype.Repository;

@Repository
public class ViewDao extends AbstractDao<View>{

	public ViewDao() {
		super(View.class);
	}

    public Map<String, View> getView(String view, String role) {
    	Query query = entityManager.createQuery("select v from View v where v.view_name=? and v.role=?");
    	query.setParameter(1, view);
    	query.setParameter(2, role);

        @SuppressWarnings("unchecked")
        List<View> list = query.getResultList();
        Map<String,View>map = new HashMap<String,View>();

        for( View v : list ) {
            map.put(v.getName(),v);
        }

        return map;
    }

    public void saveView(View v) {
       if(v.getId() != null && v.getId().intValue()>0) {
    	   merge(v);
       } else {
    	   persist(v);
       }
    }

    public void delete(View v) {
        remove(v.getId());
    }
}
