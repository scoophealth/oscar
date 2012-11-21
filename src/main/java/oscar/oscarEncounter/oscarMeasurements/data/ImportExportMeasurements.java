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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.oscarehr.common.dao.MeasurementDao;
import org.oscarehr.common.dao.MeasurementTypeDao;
import org.oscarehr.common.dao.MeasurementsExtDao;
import org.oscarehr.common.model.Measurement;
import org.oscarehr.common.model.MeasurementType;
import org.oscarehr.common.model.MeasurementsExt;
import org.oscarehr.util.SpringUtils;

public class ImportExportMeasurements {

	private static MeasurementDao measurementDao = SpringUtils.getBean(MeasurementDao.class);
	private static MeasurementTypeDao measurementTypeDao = SpringUtils.getBean(MeasurementTypeDao.class);
	private static MeasurementsExtDao measurementsExtDao = SpringUtils.getBean(MeasurementsExtDao.class);
	
	
    public static List<Measurements> getMeasurements(String demoNo) {
		List<Measurements> measList = new ArrayList<Measurements>();
		if (filled(demoNo)) {
	
			List<Measurement> ms = measurementDao.findByDemographicId(Integer.parseInt(demoNo));
			for(Measurement m:ms) {
				Measurements meas = new Measurements(Long.valueOf(demoNo));
				meas.setId(m.getId().longValue());
				meas.setType(m.getType());
				meas.setProviderNo(m.getProviderNo());
				meas.setDataField(m.getDataField());
				meas.setMeasuringInstruction(m.getMeasuringInstruction());
				meas.setComments(m.getComments());
				meas.setDateObserved(m.getDateObserved());
				meas.setDateEntered(m.getCreateDate());
				measList.add(meas);
			}
		}
		return measList;
    }

    public static List<LabMeasurements> getLabMeasurements(String demoNo)  {
		List<LabMeasurements> labmList = new ArrayList<LabMeasurements>();
	
		List<Measurements> measList = getMeasurements(demoNo);
		for (Measurements ms : measList) {
		    List<MeasurementsExt> mExt = getMeasurementsExt(ms.getId());
		    if (!mExt.isEmpty()) {
			LabMeasurements labm = new LabMeasurements();
			labm.setMeasure(ms);
			labm.setExts(mExt);
			labmList.add(labm);
		    }
		}
		return labmList;
    }

    public static Long saveMeasurements(String type, String demoNo, String providerNo, String dataField, Date dateObserved)  {
    	List<MeasurementType> mts = measurementTypeDao.findByType(type);
    	String mi="";
    	if(!mts.isEmpty()) {
    		mi = mts.get(0).getMeasuringInstruction();
    	}
		return saveMeasurements(type, demoNo, providerNo, dataField, mi, dateObserved);
    }

    public static Long saveMeasurements(String type, String demoNo, String providerNo, String dataField, String measuringInstruction, Date dateObserved)  {
		if (dateObserved==null) 
			dateObserved = new Date();
		
		Measurement m = new Measurement();
		m.setDemographicId(Integer.parseInt(demoNo));
		m.setType(type.toUpperCase());
		m.setProviderNo(providerNo);
		m.setDataField(dataField);
		m.setMeasuringInstruction(measuringInstruction);
		m.setDateObserved(dateObserved);
		
		measurementDao.persist(m);
		
		return m.getId().longValue();
    }

    public static void saveMeasurements(Measurements meas)  {

        String mi=meas.getMeasuringInstruction();
        if (!filled(mi)) {
        	List<MeasurementType> mts = measurementTypeDao.findByType(meas.getType());
        	if(!mts.isEmpty()) {
        		mi = mts.get(0).getMeasuringInstruction();
        	}
        }
        
        Measurement m = new Measurement();
		m.setDemographicId(meas.getDemographicNo().intValue());
		m.setType(meas.getType());
		m.setProviderNo(meas.getProviderNo());
		m.setDataField(meas.getDataField());
		m.setMeasuringInstruction(mi);
		m.setDateObserved(new Date());
		
		measurementDao.persist(m);
        
		meas.setId(m.getId().longValue());
		
    }

    public static void saveMeasurementsExt(MeasurementsExt mExt) {
    	MeasurementsExt m = new MeasurementsExt();
    	m.setMeasurementId( mExt.getMeasurementId());
    	m.setKeyVal(mExt.getKeyVal());
    	m.setVal(mExt.getVal());
    	
    	measurementsExtDao.persist(m);
    	mExt.setId(m.getId());

    }

    public static List<MeasurementsExt> getMeasurementsExt(Long measurementId) {
		if (measurementId!=null) {
			List<MeasurementsExt> mes = measurementsExtDao.getMeasurementsExtByMeasurementId(measurementId.intValue());
			return mes;
		}
		return new ArrayList<MeasurementsExt>();
    }

    public static MeasurementsExt getMeasurementsExtByKeyval(Long measurementId, String keyval) {
		MeasurementsExt measurementsExt = null;
		if (measurementId!=null) {
			return measurementsExtDao.getMeasurementsExtByMeasurementIdAndKeyVal(measurementId.intValue(), keyval);
		}
		return measurementsExt;
    }

    private static boolean filled(String s) {
    	return (s!=null && s.trim().length()>0);
    }
}
