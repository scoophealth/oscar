package org.oscarehr.common.dao;

import java.util.List;
import javax.persistence.Query;
import org.springframework.stereotype.Repository;
import org.oscarehr.common.model.AppointmentType;

@Repository
public class AppointmentTypeDao extends AbstractDao<AppointmentType>{
	
	public AppointmentTypeDao() {
		super(AppointmentType.class);
	}
	  
   public List<AppointmentType> listAll() {
	   	String sqlCommand = "select x from AppointmentType x order by x.name desc";
		Query query = entityManager.createQuery(sqlCommand);
						
		@SuppressWarnings("unchecked")
		List<AppointmentType> results=query.getResultList();
		
		return (results);  
	  
   }
    
   public AppointmentType findByAppointmentTypeByName(String name) {
	   Query query = entityManager.createQuery("from AppointmentType atype where atype.name = :_name").setParameter("_name", name);
	   return (AppointmentType)query.getSingleResult();
   }

}
