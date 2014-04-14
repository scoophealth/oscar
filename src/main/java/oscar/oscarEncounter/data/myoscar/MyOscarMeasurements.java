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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.oscarehr.myoscar.commons.MedicalDataType;
import org.oscarehr.util.DateRange;

import oscar.util.Appender;
import oscar.util.ConversionUtils;

public class MyOscarMeasurements implements Serializable {

	private static final long serialVersionUID = 1L;

	private MedicalDataType dataType;

	private List<MyOscarMeasurement> measurements = new ArrayList<MyOscarMeasurement>();

	public MyOscarMeasurements() {
	}
	
	public void sort() {
		Collections.sort(getMeasurements());
	}

	public MyOscarMeasurements(MedicalDataType type, List<MyOscarMeasurement> measurements) {
		setDataType(type);
		setMeasurements(measurements);
	}

	public List<MyOscarMeasurement> getMeasurements() {
		return measurements;
	}

	public Map<String, String> getMinima() {
		return getValues(Mode.MIN);
	}

	private enum Mode {
		MIN, MAX, AVG
	}

	public int getMeasurementCount() {
		return getMeasurements().size();
	}

	public Map<String, String> getAsMap() {
		Map<String, String> result = new HashMap<String, String>();

		for (String type : getMeasurementTypes()) {
			List<Object[]> values = getMeasurementValuesByType(type);
			Appender buf = new Appender(",");
			for (Object[] o : values) {
				String date = ConversionUtils.toDateString((Date) o[0]);
				String value = String.valueOf(o[1]);
				
				if (!value.matches("\\d*")) {
					continue;
				}
				buf.append("['" + date + "', " + value + "]");
			}
			result.put(type, buf.toString());
		}
		return result;
	}

	private List<Object[]> getMeasurementValuesByType(String type) {
		List<Object[]> result = new ArrayList<Object[]>();
		for (MyOscarMeasurement m : getMeasurements()) {
			Date date = m.getDate();
			String value = m.getMeasurementValueByType(type);

			result.add(new Object[] { date, value });
		}
		return result;
	}

	private Map<String, String> getValues(Mode mode) {
		List<String> measurementTypes = getMeasurementTypes();

		if (measurementTypes == null) {
			return null;
		}

		for (String measurementType : measurementTypes) {
			boolean isUniformUnits = true;
			String units = null;
			for (MyOscarMeasurement m : getMeasurements()) {
				if (units == null) {
					units = m.getUnits(measurementType);
					if (units == null) {
						return null;
					}
				}

				String newUnits = m.getUnits(measurementType);
				if (!units.equals(newUnits)) {
					isUniformUnits = false;
					break;
				}
			}

			if (!isUniformUnits) {
				return null;
			}
		}

		Map<String, String> result = new HashMap<String, String>();

		for (String measurementType : measurementTypes) {
			String value = null;
			switch (mode) {
			case MIN:
				value = getMin(measurementType);
				break;
			case MAX:
				value = getMax(measurementType);
				break;
			case AVG:
				value = getAvg(measurementType);
				break;
			default:
				break;
			}
			result.put(measurementType, value);
		}

		return result;
	}

	public List<String> getMeasurementTypes() {
		return FieldToTypesMapping.getFields(getDataType());
	}

	private String getAvg(String measurementType) {
		if (getMeasurements().isEmpty()) {
			return "0";
		}
		
		double result = 0;
		for (MyOscarMeasurement m : getMeasurements()) {
			result += ConversionUtils.fromDoubleString(m.getValue(measurementType));
		}
		result /= getMeasurements().size();
		return "" + (int) result;
	}

	private String getMax(String measurementType) {
		if (getMeasurements().isEmpty()) {
			return "0";
		}
		
		double max = Double.MIN_VALUE;
		for (MyOscarMeasurement m : getMeasurements()) {
			double newValue = ConversionUtils.fromDoubleString(m.getValue(measurementType));
			if (newValue >= max) {
				max = newValue;
			}
		}
		return "" + (int) max;
	}

	private String getMin(String measurementType) {
		if (getMeasurements().isEmpty()) {
			return "0";
		}
		
		double max = Double.MAX_VALUE;
		for (MyOscarMeasurement m : getMeasurements()) {
			double newValue = ConversionUtils.fromDoubleString(m.getValue(measurementType));
			if (newValue < max) {
				max = newValue;
			}
		}
		return "" + (int) max;
	}

	public Map<String, String> getMaxima() {
		return getValues(Mode.MAX);
	}

	public Map<String, String> getAverage() {
		return getValues(Mode.AVG);
	}

	public void setMeasurements(List<MyOscarMeasurement> measurements) {
		this.measurements = measurements;
	}

	public MedicalDataType getDataType() {
		return dataType;
	}

	public void setDataType(MedicalDataType dataType) {
		this.dataType = dataType;
	}

	public void filter(DateRange range) {
		Iterator<MyOscarMeasurement> it = getMeasurements().iterator();
		while (it.hasNext()) {
			MyOscarMeasurement m = it.next();
			if (!range.isInRange(m.getDate())) {
				it.remove();
			}
		}
	}

	public MyOscarMeasurements clone() {
		MyOscarMeasurements result = new MyOscarMeasurements();
		result.setDataType(getDataType());
		result.setMeasurements(new ArrayList<MyOscarMeasurement>(getMeasurements()));
		return result;
	}

}
