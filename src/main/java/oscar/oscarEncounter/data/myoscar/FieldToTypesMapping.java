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
package oscar.oscarEncounter.data.myoscar;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.oscarehr.myoscar.commons.MedicalDataType;

public class FieldToTypesMapping {

	private static Map<MedicalDataType, List<String>> mapping;
	static {
		Map<MedicalDataType, List<String>> mapping = new HashMap<MedicalDataType, List<String>>();
		mapping.put(MedicalDataType.BLOOD_PRESSURE, Collections.unmodifiableList(Arrays.asList(new String[] {"SystolicValue", "DiastolicValue", "HeartRate"})));
		mapping.put(MedicalDataType.GLUCOSE, Collections.unmodifiableList(Arrays.asList(new String[] {"Glucose"})));
		mapping.put(MedicalDataType.HEIGHT_AND_WEIGHT, Collections.unmodifiableList(Arrays.asList(new String[] { "Weight", "Height" })));
		FieldToTypesMapping.mapping = mapping;
	}
	
	public static List<String> getFields(MedicalDataType type) {
		return mapping.get(type);
	}
	
	public static Map<MedicalDataType, List<String>> getMap() {
		return mapping;
	}
	
}
