package org.oscarehr.PMmodule.dao;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import org.oscarehr.PMmodule.model.BedDemographic;
import org.oscarehr.PMmodule.utility.DateTimeFormatUtils;
import org.oscarehr.common.dao.AuditStrategyFactory;
import org.oscarehr.common.dao.AuditableEvent;
import org.oscarehr.common.dao.BaseAuditStrategy;

public class BedDemographicAuditStrategy extends BaseAuditStrategy {
	
	@Override
	public void registerEvents() {
		AuditStrategyFactory.register(BedDemographic.class, AuditableEvent.CREATE, this);
		AuditStrategyFactory.register(BedDemographic.class, AuditableEvent.UPDATE, this);
		AuditStrategyFactory.register(BedDemographic.class, AuditableEvent.DELETE, this);
	}

	public void auditCreate(Object entity, Serializable id, Object[] currentState, String[] propertyNames) {
	    System.out.println("BedDemographicAuditStrategy.auditCreate()");
    }

	public void auditUpdate(Object entity, Serializable id, Object[] currentState, Object[] previousState, String[] propertyNames) {
	    System.out.println("BedDemographicAuditStrategy.auditUpdate()");
	    
		// iterate once to get provider and role names
		// provider - string
		// role - string (program provider dao (provider, bed->room->program)

		for (int i = 0; i < propertyNames.length; i++) {
			String name = propertyNames[i];
			Object current = currentState[i];
			Object previous = previousState[i];
			
			if (!current.equals(previous)) {
				// write to delta tables
				
				// class - string
				// id - string
				// propertyName - string
				// previous - string
				// current - string
				// date - date
				// time - time
				
				Date now = Calendar.getInstance().getTime();
				
				StringBuilder builder = new StringBuilder();
				builder.append("Class : ").append(entity.getClass().getName()).append(" ");
				builder.append("Id : ").append(id).append(" ");
				builder.append("Name : ").append(name).append(" ");
				builder.append("Previous : ").append(previous).append(" ");
				builder.append("Current : ").append(current).append(" ");
				builder.append("Date : ").append(DateTimeFormatUtils.getDateFromDate(now)).append(" ");
				builder.append("Time : ").append(DateTimeFormatUtils.getTimeFromDate(now));
				
				System.out.println(builder);
			}
		}
    }
	
	public void auditDelete(Object entity, Serializable id, Object[] state, String[] propertyNames) {
		System.out.println("BedDemographicAuditStrategy.auditDelete()");
	}

}