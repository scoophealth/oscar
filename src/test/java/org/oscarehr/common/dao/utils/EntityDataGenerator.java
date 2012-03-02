package org.oscarehr.common.dao.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.util.Calendar;
import java.util.Date;

import javax.persistence.Id;

import org.oscarehr.util.MiscUtils;

public class EntityDataGenerator {

	public static Object generateTestDataForModelClass(Object model) throws Exception {

		Field f[] = model.getClass().getDeclaredFields();
		AccessibleObject.setAccessible(f, true);
		for (int i = 0; i < f.length; i++) {
			boolean isId=false;
	        Annotation annotations[] = f[i].getAnnotations();
	        for (int j = 0; j < annotations.length; j++) {
	            if(annotations[j].annotationType() == Id.class) {
	            	isId=true;
	            }
            }
	        if(isId)
	        	continue;


	        if(f[i].getType() == String.class) {
	        	f[i].set(model, f[i].getName() + ((int)(Math.random()*10000)));
	        }
	        else if(f[i].getType() == int.class || f[i].getType() == Integer.class) {
	        	f[i].set(model,(int)(Math.random()*10000));
	        }
	        else if(f[i].getType() == long.class || f[i].getType() == Long.class) {
	        	f[i].set(model,(long)(Math.random()*10000));
	        }
	        else if(f[i].getType() == Date.class) {
	        	f[i].set(model,new Date());
	        }
	        else if(f[i].getType() == Calendar.class) {
	        	f[i].set(model,Calendar.getInstance());
	        }
	        else if(f[i].getType() == boolean.class || f[i].getType() == Boolean.class) {
	 	        	f[i].set(model,true);
	        } else {
	        	MiscUtils.getLogger().warn("Can't generate test data for class type:" + f[i].getType());
	        }

        }

		return model;
	}
}
