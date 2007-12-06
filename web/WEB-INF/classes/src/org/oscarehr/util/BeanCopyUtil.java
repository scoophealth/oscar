/*
 * 
 * Copyright (c) 2001-2002. Centre for Research on Inner City 
 * Health, St. Michael's Hospital, Toronto. All Rights Reserved. 
 *
 * This software is published under the GPL GNU General Public License. 
 * This program is free software; you can redistribute it and/or 
 * modify it under the terms of the GNU General Public License 
 * as published by the Free Software Foundation; either version 2 
 * of the License, or (at your option) any later version. 
 * This program is distributed in the hope that it will be useful, 
 * but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
 * GNU General Public License for more details. 
 *
 * You should have received a copy of the GNU General Public License 
 * along with this program; if not, write to the Free Software 
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 * 
 * This software was written for 
 * Centre for Research on Inner City Health, St. Michael's Hospital, 
 * Toronto, Ontario, Canada 
 */

package org.oscarehr.util;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.FatalBeanException;
import org.springframework.util.Assert;

/**
 */
public class BeanCopyUtil {

	/**
	 * Copy the property values of the given source bean into the given target bean,
	 * ignoring the given "ignoreProperties".
	 * <p>Note: The source and target classes do not have to match or even be derived
	 * from each other, as long as the properties match. Any bean properties that the
	 * source bean exposes but the target bean does not will silently be ignored.
	 * <p>This is just a convenience method. For more complex transfer needs,
	 * consider using a full BeanWrapper.
	 * @param source the source bean
	 * @param target the target bean
	 * @param ignoreProperties array of property names to ignore
     * @param skipNull flag to indicate if null values should be skipped
     * @throws BeansException if the copying failed
	 * @see org.springframework.beans.BeanWrapper
	 */
	public static void copyProperties(Object source, Object target, String[] ignoreProperties, boolean skipNull)
			throws BeansException {

		copyProperties(source, target, null, ignoreProperties, skipNull);
	}

    /**
	 * Copy the property values of the given source bean into the given target bean.
	 * <p>Note: The source and target classes do not have to match or even be derived
	 * from each other, as long as the properties match. Any bean properties that the
	 * source bean exposes but the target bean does not will silently be ignored.
     *
     * A direct copy of BeanUtils copyProperties with the addition that it skips null properties
     *
	 * @param source the source bean
	 * @param target the target bean
	 * @param editable the class (or interface) to restrict property setting to
	 * @param ignoreProperties array of property names to ignore
	 * @param skipNull flag to indicate if null values should be skipped
     * @throws org.springframework.beans.BeansException if the copying failed
	 * @see org.springframework.beans.BeanWrapper
	 */
	private static void copyProperties(Object source, Object target, Class<?> editable, String[] ignoreProperties,
                                       boolean skipNull)
			throws BeansException {

		Assert.notNull(source, "Source must not be null");
		Assert.notNull(target, "Target must not be null");

		Class<?> actualEditable = target.getClass();
		if (editable != null) {
			if (!editable.isInstance(target)) {
				throw new IllegalArgumentException("Target class [" + target.getClass().getName() +
						"] not assignable to Editable class [" + editable.getName() + "]");
			}
			actualEditable = editable;
		}
		PropertyDescriptor[] targetPds = BeanUtils.getPropertyDescriptors(actualEditable);
		List<String> ignoreList = (ignoreProperties != null) ? Arrays.asList(ignoreProperties) : null;

        for (PropertyDescriptor targetPd : targetPds) {
            if (targetPd.getWriteMethod() != null &&
                    (ignoreProperties == null || (!ignoreList.contains(targetPd.getName())))) {
                PropertyDescriptor sourcePd = BeanUtils.getPropertyDescriptor(source.getClass(), targetPd.getName());
                if (sourcePd != null && sourcePd.getReadMethod() != null) {
                    try {
                        Method readMethod = sourcePd.getReadMethod();
                        if (!Modifier.isPublic(readMethod.getDeclaringClass().getModifiers())) {
                            readMethod.setAccessible(true);
                        }
                        Object value = readMethod.invoke(source);

                        /** skip the value if it's null
                         */
                        if (value == null && skipNull)
                            continue;

                        Method writeMethod = targetPd.getWriteMethod();
                        if (!Modifier.isPublic(writeMethod.getDeclaringClass().getModifiers())) {
                            writeMethod.setAccessible(true);
                        }
                        writeMethod.invoke(target, value);
                    }
                    catch (Throwable ex) {
                        throw new FatalBeanException("Could not copy properties from source to target", ex);
                    }
                }
            }
        }
	}
}
