/**
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
 * This software is published under the GPL GNU General Public License.
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 *
 * This software was written for the
 * Department of Family Medicine
 * McMaster University
 * Hamilton
 * Ontario, Canada
 */
package oscar.util;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.persistence.Column;

public class Textualizer {

	private static final Class<?>[] PRIMITIVES = { String.class, Integer.class, Date.class, Boolean.class, Long.class, Character.class, Byte.class, Short.class, Float.class, Double.class, int.class, boolean.class, long.class, char.class, byte.class, short.class, float.class, double.class };

	public SortedMap<String, String> toMap(Object object) throws Exception {
		return toMap(object, new ToTemplate());
	}

	public <T> T fromMap(T object, Properties props, FromTemplate template) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		for (Entry<Object, Object> p : props.entrySet()) {
			map.put("" + p.getKey(), "" + p.getValue());
		}
		return fromMap(object, map, template);
	}

	public <T> T fromMap(T object, Map<String, String> map) throws Exception {
		return fromMap(object, map, new DefaultTemplate());
	}

	public <T> T fromMap(T object, Map<String, String> map, FromTemplate template) throws Exception {
		Class<?> objectClass = object.getClass();
		BeanInfo beanInfo = Introspector.getBeanInfo(objectClass);

		for (PropertyDescriptor descriptor : beanInfo.getPropertyDescriptors()) {
			if (!isPrimitive(descriptor.getPropertyType())) {
				continue;
			}

			if (descriptor.getWriteMethod() == null) {
				continue;
			}

			String propertyName = getPropertyName(descriptor, objectClass);
			String propertyValue = map.get(propertyName);

			Object value = template.convert(propertyValue, descriptor);
			Method writeMethod = descriptor.getWriteMethod();
			if (writeMethod == null) {
				throw new IllegalStateException("Unable to find write method for " + propertyName);
			}

			try {
				writeMethod.invoke(object, value);
			} catch (Exception e) {
				throw new IllegalStateException("Unable to invoke write method");
			}
		}
		return object;
	}

	public SortedMap<String, String> toMap(Object object, ToTemplate template) throws Exception {
		SortedMap<String, String> result = new TreeMap<String, String>();

		Class<?> objectClass = object.getClass();
		BeanInfo beanInfo = Introspector.getBeanInfo(objectClass);
		for (PropertyDescriptor descriptor : beanInfo.getPropertyDescriptors()) {
			if (!isPrimitive(descriptor.getPropertyType())) {
				continue;
			}

			// get property name - first check the column annotation, and if it's not there
			// default it to the property name
			String propertyName = getPropertyName(descriptor, objectClass);
			if (propertyName == null) {
				continue;
			}

			// now try to get the property value - with fancy sophisticated conversion
			Object value;
			try {
				Method readMethod = descriptor.getReadMethod();
				if (readMethod == null) {
					throw new IllegalStateException("Read method doesn't exist for " + propertyName);
				}
				value = readMethod.invoke(object, new Object[] {});
			} catch (Exception e) {
				throw new RuntimeException("Unable to read " + propertyName, e);
			}

			String propertyValue;
			if (value == null) {
				propertyValue = "";
			} else {
				propertyValue = template.convert(value, descriptor);
			}

			result.put(propertyName, propertyValue);
		}
		return result;
	}

	public static boolean isPrimitive(Class<?> propertyType) {
		for (Class<?> c : PRIMITIVES) {
			if (c.isAssignableFrom(propertyType)) {
				return true;
			}
		}
		return false;
	}

	private String getPropertyName(PropertyDescriptor descriptor, Class<?> objectClass) {
		String propertyName = null;

		// if we get no field - just halt
		Field field = null;
		try {
			field = objectClass.getDeclaredField(descriptor.getName());
		} catch (Exception e) {
			return null;
		}

		Column c = field.getAnnotation(Column.class);
		if (c != null) {
			propertyName = c.name();
		}

		if (propertyName == null || propertyName.isEmpty()) {
			propertyName = descriptor.getName();
		}
		return propertyName;
	}

	public static class ToTemplate {
		public String convert(Object object, PropertyDescriptor descriptor) {
			if (object == null || descriptor == null) {
				return "";
			}
			return object.toString();
		}
	}

	/**
	 * Converts value from string into the appropriate instance type
	 *
	 */
	public static class FromTemplate {
		public Object convert(String str, PropertyDescriptor descriptor) {
			if (str == null || descriptor == null) {
				return null;
			}
			return null;
		}
	}

	public static class DefaultTemplate extends FromTemplate {

		@Override
		public Object convert(String str, PropertyDescriptor descriptor) {
			if (str == null) {
				return null;
			}

			Method readMethod = descriptor.getReadMethod();
			if (readMethod == null) {
				throw new IllegalArgumentException("Write method doesn't exist for " + str + " with descriptor " + descriptor);
			}

			Class<?> returnType = readMethod.getReturnType();

			if (String.class.isAssignableFrom(returnType)) {
				return String.valueOf(str);
			}

			if ("".equals(str)) {
				return null;
			}

			if (Date.class.isAssignableFrom(returnType)) {
				// time patter is the default Date.toString() time pattern
				return ConversionUtils.fromDateString(str, "EEE MMM dd HH:mm:ss zzz yyyy");
			}

			if (Integer.class.isAssignableFrom(returnType) || int.class.isAssignableFrom(returnType)) {
				return ConversionUtils.fromIntString(str);
			}

			if (Boolean.class.isAssignableFrom(returnType) || boolean.class.isAssignableFrom(returnType)) {
				return new Boolean(ConversionUtils.fromBoolString(str));
			}

			if (Long.class.isAssignableFrom(returnType) || long.class.isAssignableFrom(returnType)) {
				return ConversionUtils.fromLongString(str);
			}

			if (Character.class.isAssignableFrom(returnType) || char.class.isAssignableFrom(returnType)) {
				str = String.valueOf(str);
				if (str.isEmpty()) {
					return null;
				}
				return new Character(str.charAt(0));
			}

			if (Byte.class.isAssignableFrom(returnType) || byte.class.isAssignableFrom(returnType)) {
				return new Byte(Byte.parseByte(str));
			}

			if (Short.class.isAssignableFrom(returnType) || short.class.isAssignableFrom(returnType)) {
				return new Short(Short.parseShort(str));
			}

			if (Float.class.isAssignableFrom(returnType) || float.class.isAssignableFrom(returnType)) {
				return new Float(Float.parseFloat(str));
			}

			if (Double.class.isAssignableFrom(returnType) || double.class.isAssignableFrom(returnType)) {
				return new Double(Double.parseDouble(str));
			}

			throw new IllegalStateException("Unsupported type " + returnType + " for property " + descriptor.getName());
		}

	}

}
