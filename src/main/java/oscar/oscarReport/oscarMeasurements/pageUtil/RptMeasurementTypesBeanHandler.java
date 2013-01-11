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

package oscar.oscarReport.oscarMeasurements.pageUtil;

import java.util.Collections;
import java.util.List;
import java.util.Vector;

import org.apache.commons.beanutils.BeanComparator;
import org.oscarehr.common.dao.MeasurementGroupDao;
import org.oscarehr.common.dao.MeasurementTypeDao;
import org.oscarehr.common.model.MeasurementGroup;
import org.oscarehr.common.model.MeasurementType;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

public class RptMeasurementTypesBeanHandler {

	Vector<RptMeasurementTypesBean> measurementTypeVector = new Vector<RptMeasurementTypesBean>();
	Vector<RptMeasuringInstructionBeanHandler> measuringInstrcBeanVector = new Vector<RptMeasuringInstructionBeanHandler>();

	public RptMeasurementTypesBeanHandler(String groupName) {
		init(groupName);
	}

	@SuppressWarnings("unchecked")
	public boolean init(String groupName) {
		boolean verdict = true;
		try {
			MeasurementGroupDao mgDao = SpringUtils.getBean(MeasurementGroupDao.class);
			MeasurementTypeDao mtDao = SpringUtils.getBean(MeasurementTypeDao.class);
			List<MeasurementGroup> groups = mgDao.findByName(groupName);
			Collections.sort(groups, new BeanComparator("typeDisplayName"));
			for (MeasurementGroup g : groups) {
				String typeDisplayName = g.getTypeDisplayName();

				List<MeasurementType> mts = mtDao.findByTypeDisplayName(typeDisplayName);
				Collections.sort(mts, new BeanComparator("typeDescription"));
				for (MeasurementType mt : mts) {
					RptMeasurementTypesBean measurementTypes = new RptMeasurementTypesBean(mt.getId(), mt.getType(), mt.getTypeDisplayName(), mt.getTypeDescription(), mt.getMeasuringInstruction(), mt.getValidation());
					measurementTypeVector.add(measurementTypes);

					RptMeasuringInstructionBeanHandler hd = new RptMeasuringInstructionBeanHandler(typeDisplayName);
					measuringInstrcBeanVector.add(hd);
				}
			}
		} catch (Exception e) {
			MiscUtils.getLogger().error("Error", e);
			verdict = false;
		}
		return verdict;
	}

	public Vector<RptMeasurementTypesBean> getMeasurementTypeVector() {
		return measurementTypeVector;
	}

	public Vector<RptMeasuringInstructionBeanHandler> getMeasuringInstrcBeanVector() {
		return measuringInstrcBeanVector;
	}
}
