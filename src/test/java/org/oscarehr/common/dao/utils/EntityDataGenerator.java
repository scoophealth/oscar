package org.oscarehr.common.dao.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.Set;

import javassist.Modifier;

import javax.persistence.Id;

import org.oscarehr.common.model.DemographicExt;
import org.oscarehr.common.model.Provider;
import org.oscarehr.util.MiscUtils;

import antlr.collections.List;

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

	        int modifiers = f[i].getModifiers();
	        if((modifiers & Modifier.STATIC) == Modifier.STATIC) {
	        	continue;
	        }

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
	        }
	 	    else if(f[i].getType() == Set.class || f[i].getType() == List.class || f[i].getType() == Map.class) {
	 	    	//ignore
	 	    }else if(f[i].getType() == Provider.class || f[i].getType() == DemographicExt[].class) {
	 	    	//ignore
	        } else {
	        	MiscUtils.getLogger().warn("Can't generate test data for class type:" + f[i].getType());
	        }

        }

		return model;
	}
}
