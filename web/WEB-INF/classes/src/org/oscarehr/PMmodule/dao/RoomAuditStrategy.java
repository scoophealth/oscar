package org.oscarehr.PMmodule.dao;

import java.io.Serializable;

import org.oscarehr.PMmodule.model.Room;
import org.oscarehr.common.dao.AuditStrategyFactory;
import org.oscarehr.common.dao.AuditableEvent;
import org.oscarehr.common.dao.BaseAuditStrategy;

public class RoomAuditStrategy extends BaseAuditStrategy {
	
	@Override
	public void registerEvents() {
		AuditStrategyFactory.register(Room.class, AuditableEvent.CREATE, this);
		AuditStrategyFactory.register(Room.class, AuditableEvent.UPDATE, this);
		AuditStrategyFactory.register(Room.class, AuditableEvent.DELETE, this);
	}

	public void auditCreate(Object entity, Serializable id, Object[] currentState, String[] propertyNames) {
	    System.out.println("BedDemographicAuditStrategy.auditCreate()");
    }

	public void auditUpdate(Object entity, Serializable id, Object[] currentState, Object[] previousState, String[] propertyNames) {
	    System.out.println("BedDemographicAuditStrategy.auditUpdate()");
    }
	
	public void auditDelete(Object entity, Serializable id, Object[] state, String[] propertyNames) {
		System.out.println("BedDemographicAuditStrategy.auditDelete()");
	}

}