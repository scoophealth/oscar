package org.oscarehr.common.dao;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class AuditStrategyFactory {
	
	private static class AuditStrategyKey {
		private Class clazz;
		private AuditableEvent event;

		AuditStrategyKey(Class clazz, AuditableEvent event) {
			this.clazz = clazz;
			this.event = event;
		}

		Class getClazz() {
			return clazz;
		}

		AuditableEvent getEvent() {
			return event;
		}
		
		@Override
		public int hashCode() {
			return HashCodeBuilder.reflectionHashCode(this);
		}
		
		@Override
		public boolean equals(Object rhs) {
		    return EqualsBuilder.reflectionEquals(this, rhs);
		}
	}

	public static AuditStrategy create(Object entity, AuditableEvent event) {
		return(null);
	}

	private AuditStrategyFactory() {}

}