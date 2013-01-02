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

package oscar.oscarEncounter.oscarMeasurements.data;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.oscarehr.common.dao.MeasurementTypeDao;
import org.oscarehr.common.dao.ValidationsDao;
import org.oscarehr.common.model.MeasurementType;
import org.oscarehr.common.model.Validations;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.oscarEncounter.oscarMeasurements.bean.EctMeasurementTypesBean;
import oscar.util.ConversionUtils;

/**
 * @deprecated use MeasurementTypeDao instead (2012-01-23)
 */
public class MeasurementTypes {

	private static Logger log = MiscUtils.getLogger();

	private static MeasurementTypes measurementTypes;

	private Map<String, EctMeasurementTypesBean> byId = new HashMap<String, EctMeasurementTypesBean>();
	private Map<String, EctMeasurementTypesBean> byType = new HashMap<String, EctMeasurementTypesBean>();

	/** Creates a new instance of MeasurementTypes */
	private MeasurementTypes() {
	}

	public EctMeasurementTypesBean getByType(String type) {
		return byType.get(type);
	}

	public EctMeasurementTypesBean getById(String type) {
		return byId.get(type);
	}

	public static synchronized MeasurementTypes getInstance() {
		if (measurementTypes == null) {
			measurementTypes = new MeasurementTypes();
			measurementTypes.reInit();
		}
		return measurementTypes;
	}

	public synchronized void reInit() {
		byId.clear();
		byType.clear();

		try {
			MeasurementTypeDao mtDao = SpringUtils.getBean(MeasurementTypeDao.class);
			ValidationsDao vDao = SpringUtils.getBean(ValidationsDao.class);

			for (MeasurementType t : mtDao.findAll()) {
				EctMeasurementTypesBean ret = new EctMeasurementTypesBean(t.getId(), t.getType(), t.getTypeDisplayName(), t.getTypeDescription(), t.getMeasuringInstruction(), t.getValidation());

				Validations v = vDao.find(ConversionUtils.fromIntString(t.getValidation()));
				if (v != null) {
					ret.setValidationName(v.getName());
				}
				byId.put(t.getId().toString(), ret);
				byType.put(t.getType(), ret);

			}
		} catch (Exception e) {
			log.debug(e.getMessage());
		}
	}
}
