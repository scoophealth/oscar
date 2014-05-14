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

package org.oscarehr.sharingcenter.util;

import java.util.List;
import org.marc.everest.datatypes.NullFlavor;

import org.marc.shic.cda.datatypes.Code;
import org.oscarehr.common.dao.MeasurementDao;
import org.oscarehr.common.dao.MeasurementTypeDao;
import org.oscarehr.common.model.Measurement;
import org.oscarehr.common.model.MeasurementType;
import org.oscarehr.util.SpringUtils;

/**
 * A utility class for access measurement data.
 *
 * @author Christine Gibson
 *
 */
public class MeasurementUtil {

	public static final String DATE_OBSERVED = "Date Observed";
	public static final String MEASUREMENT_INSTRUCTION = "Measurement Instruction";
	public static final String NAME = "Name";
	public static final String VALUE = "Value";

	public static final String[] COLUMNS = { DATE_OBSERVED, NAME, VALUE, MEASUREMENT_INSTRUCTION };

	private static final MeasurementDao measurementDao = SpringUtils.getBean(MeasurementDao.class);
	private static final MeasurementTypeDao measurementTypeDao = SpringUtils.getBean(MeasurementTypeDao.class);

	private MeasurementUtil() {
	}

	public static Code codeFromMeasurement(Measurement measurement) {
		String displayName = measurement.getType();
		Code result;
		if (displayName.equals("WT")) {
			result = new Code("3141-9", "2.16.840.1.113883.6.1", "BODY WEIGHT (MEASURED)", "LOINC");
		} else if (displayName.equals("HT")) {
			result = new Code("8302-2", "2.16.840.1.113883.6.1", "BODY HEIGHT (MEASURED)", "LOINC");
		} else {
			result = Code.unavailable(NullFlavor.Other, displayName);
		}
		return result;
	}

	public static String getMeasurementUnits(Measurement measurement) {
		String displayName = measurement.getType();
		if (displayName.equals("WT")) {
			return "kg";
		} else if (displayName.equals("HT")) {
			return "cm";
		}
		return "units";
	}

	public static boolean isMeasurementValueBool(Measurement measurement) {
		String measureInstruction = measurement.getMeasuringInstruction();
		return measureInstruction.equalsIgnoreCase("Yes/No") || measureInstruction.equalsIgnoreCase("Normal") || measureInstruction.equalsIgnoreCase("Changed");
	}

	public static boolean getMeasurementValueBool(Measurement measurement) {
		String measureVal = measurement.getDataField();
		return measureVal.equalsIgnoreCase("Yes");
	}

	public static boolean isMeasurementVitalSign(Measurement measurement) {
		String displayName = measurement.getType();
		if (displayName.equals("WT") || displayName.equals("HT")) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Gets a list of measurements for a patient.
	 *
	 * @param demographicNo The demographic number of the patient.
	 *
	 * @return Returns a list of measurements for the patient.
	 */
	public static List<Measurement> getMeasurements(int demographicNo) {
		List<Measurement> measurementList = measurementDao.findByDemographicId(demographicNo);
		return measurementList;
	}

	/**
	 * Gets the MeasurementType based on the measurement instruction and type.
	 *
	 * @param type The type of measurement.
	 * @param measurementInstruction The measuring instruction.
	 *
	 * @return Returns a MeasurementType
	 */
	public static MeasurementType findByTypeMeasurementInstruction(String type, String measurementInstruction) {
		List<MeasurementType> measurementTypes = measurementTypeDao.findByTypeAndMeasuringInstruction(type, measurementInstruction);
		return measurementTypes.get(0);
	}
}
