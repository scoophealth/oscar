package org.caisi.tickler.prepared.runtime;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.caisi.service.ConsultationManager;
import org.caisi.tickler.prepared.PreparedTickler;

public abstract class AbstractPreparedTickler implements PreparedTickler {

	public void setDependency(String name, Object o) {
		String methodName = "set" + name.substring(0,1).toUpperCase() + name.substring(1,name.length());
		try {
			Method methods[] = this.getClass().getMethods();
			for(int x=0;x<methods.length;x++) {
				if(methods[x].getName().equals(methodName)) {
					methods[x].invoke(this,new Object[] {o});
				}
			}	
		}catch(InvocationTargetException e) {
			e.printStackTrace();
		}catch(IllegalAccessException e) {
			e.printStackTrace();
		}
	}

}
