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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.oscarehr.common.model.Measurement;
import org.oscarehr.common.service.myoscar.MeasurementsManager;
import org.oscarehr.myoscar.commons.MedicalDataType;
import org.oscarehr.myoscar.utils.MyOscarLoggedInInfo;
import org.oscarehr.util.DateRange;

public class EctMyOscarFilterAction extends Action {

	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		EctMyOscarFilterForm filterForm = (EctMyOscarFilterForm) form;

		MyOscarMeasurements measurements = getMeasurements(request, filterForm);
		if (measurements != null) {
			DateRange range = filterForm.getDateRange();
			measurements.filter(range);
		}
		request.setAttribute("measurements", measurements);
		
		ActionForward forward = mapping.findForward("success");
		return forward;
	}

	private MyOscarMeasurements getMeasurements(HttpServletRequest request, EctMyOscarFilterForm filterForm) {
		MyOscarLoggedInInfo myOscarLoggedInInfo = MyOscarLoggedInInfo.getLoggedInInfo(request.getSession());
		
		Map<MedicalDataType, List<Measurement>> mm = MeasurementsManager.getMeasurementsFromMyOscar(myOscarLoggedInInfo, 
				filterForm.getDemoNoAsInt(), new MedicalDataType [] {filterForm.getMedicalDataType()} );
		
		
		List<Measurement> measurementList = mm.get(filterForm.getMedicalDataType());
		List<MyOscarMeasurement> moms = new ArrayList<MyOscarMeasurement>();
		if(measurementList !=null){
			for(Measurement m : measurementList) {
				moms.add(toMyOscarMeasurement(filterForm.getMedicalDataType(), m));
			}
		}
		Collections.sort(moms);
		
		MyOscarMeasurements result = new MyOscarMeasurements(filterForm.getMedicalDataType(), moms );
		return result;
	}

	private MyOscarMeasurement toMyOscarMeasurement(MedicalDataType mdt, Measurement m) {
		MyOscarMeasurement result = null;
		switch (mdt) {
		case GLUCOSE:
			result = new GlucoseMeasurement();
			break;
		case HEIGHT_AND_WEIGHT:
			result = new WeightMeasurement();
			break;
		case BLOOD_PRESSURE:
			result = new BloodPressureMeasurement();
			break;
		default:
			break;
		}
		
		if (result != null) {
			result.setMeasurement(m);
		}
		
	    return result;
    }
	
}
