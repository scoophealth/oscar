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


package org.oscarehr.common.service.myoscar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.oscarehr.common.dao.MeasurementDao;
import org.oscarehr.common.dao.MeasurementTypeDao;
import org.oscarehr.common.dao.SentToPHRTrackingDao;
import org.oscarehr.common.model.Measurement;
import org.oscarehr.common.model.MeasurementType;
import org.oscarehr.common.model.SentToPHRTracking;
import org.oscarehr.myoscar.commons.MedicalDataType;
import org.oscarehr.myoscar.utils.MyOscarLoggedInInfo;
import org.oscarehr.myoscar_server.ws.InvalidRequestException_Exception;
import org.oscarehr.myoscar_server.ws.MedicalDataTransfer4;
import org.oscarehr.myoscar_server.ws.NoSuchItemException_Exception;
import org.oscarehr.myoscar_server.ws.NotAuthorisedException_Exception;
import org.oscarehr.myoscar_server.ws.UnsupportedEncodingException_Exception;
import org.oscarehr.phr.util.MyOscarUtils;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;
import org.oscarehr.util.XmlUtils;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public final class MeasurementsManager {
	private static final int[] DATASETS_ADAPTIVE_SIZES = {1000, 10000, 100000, 1000000, 10000000, 100000000};
	
	private static final Logger logger = MiscUtils.getLogger();
	private static final String OSCAR_MEASUREMENTS_DATA_TYPE = "MEASUREMENT";
	private static final SentToPHRTrackingDao sentToPHRTrackingDao = (SentToPHRTrackingDao) SpringUtils.getBean("sentToPHRTrackingDao");
	private static MeasurementDao measurementDao = (MeasurementDao) SpringUtils.getBean("measurementDao");
	private static MeasurementTypeDao measurementTypeDao = (MeasurementTypeDao) SpringUtils.getBean("measurementTypeDao");

	static class HeightWeight {
		public Measurement height;
		public Measurement weight;

		public String getCompositeId() {
			StringBuilder sb = new StringBuilder();
			if (height != null) sb.append(height.getId());
			sb.append(':');
			if (weight != null) sb.append(weight.getId());
			return (sb.toString());
		}

		public Date getDateObserved() {
			if (height != null && height.getDateObserved() != null) return (height.getDateObserved());
			else if (weight != null && weight.getDateObserved() != null) return (weight.getDateObserved());
			else return (null);
		}

		public String getProviderNo() {
			if (height != null && height.getProviderNo() != null) return (height.getProviderNo());
			else if (weight != null && weight.getProviderNo() != null) return (weight.getProviderNo());
			else return (null);
		}

		public Integer getDemographicId() {
			if (height != null && height.getDemographicId() != null) return (height.getDemographicId());
			else if (weight != null && weight.getDemographicId() != null) return (weight.getDemographicId());
			else return (null);
		}
	}

	public static Map<MedicalDataType, List<Measurement>> getMeasurementsFromMyOscar(MyOscarLoggedInInfo myOscarLoggedInInfo, Integer demoId, MedicalDataType ... types) {
			if (logger.isInfoEnabled()) {
				logger.info("Loading MyOSCAR measurements for " + demoId);
			}

			Long myOscarDemoId = MyOscarUtils.getMyOscarUserIdFromOscarDemographicId(myOscarLoggedInInfo, demoId);

			Map<MedicalDataType, List<Measurement>> result = new HashMap<MedicalDataType, List<Measurement>>();
			for (MedicalDataType type : types) {
				String typeName = type.name();

				List<MedicalDataTransfer4> medData = null;
				try {
					medData = getMeasurementsFromMyOscar(myOscarLoggedInInfo, myOscarDemoId, typeName);
				} catch (Exception e) {
					logger.error("Unable to pull med data for " + demoId + "/" + myOscarDemoId + " of type " + typeName, e);
					result.put(type, null);
					continue;
				}
				
				List<Measurement> oscarMeasurements = new ArrayList<Measurement>();
				for(MedicalDataTransfer4 mdt : medData) {
					oscarMeasurements.add(toMeasurement(mdt));
				}

				result.put(type, oscarMeasurements);
			}

			if (logger.isInfoEnabled()) {
				logger.info("Retrived MyOSCAR measurements for " + demoId + ": " + result);
			}

			return result;
	}

	private static List<MedicalDataTransfer4> getMeasurementsFromMyOscar(MyOscarLoggedInInfo myOscarLoggedInInfo, Long myOscarDemoId, String typeName) {
	    List<MedicalDataTransfer4> medData = null;
	    for(int size : DATASETS_ADAPTIVE_SIZES) {
	    	medData = MyOscarMedicalDataManagerUtils.getMedicalData(myOscarLoggedInInfo, myOscarDemoId, typeName, true, 0, size);
	    	if (medData.size() < size) {
	    		return medData;
	    	}
	    }
	    return medData;
    }
	
	private static Measurement toMeasurement(MedicalDataTransfer4 mdt) {
	    Measurement measurement = new Measurement();
	    measurement.setDataField(mdt.getData());
	    if (mdt.getDateOfData() != null) {
	    	measurement.setDateObserved(mdt.getDateOfData().getTime());
	    }
	    measurement.setType(mdt.getMedicalDataType());
	    return measurement;
    }

	public static void sendMeasurementsToMyOscar(LoggedInInfo loggedInInfo,MyOscarLoggedInInfo myOscarLoggedInInfo, Integer demographicId) throws ClassCastException {
		// get last synced info

		// get the measurements for the person which are changed since last sync
		// for each measurements
		// make sure a measurement category exists (or is one of the special pre-defined categories)
		// send the measurements or update it

		Date startSyncTime = new Date();
		SentToPHRTracking sentToPHRTracking = MyOscarMedicalDataManagerUtils.getExistingOrCreateInitialSentToPHRTracking(demographicId, OSCAR_MEASUREMENTS_DATA_TYPE, MyOscarLoggedInInfo.getMyOscarServerBaseUrl());
		logger.debug("sendMeasurementsToMyOscar : demographicId=" + demographicId + ", lastSyncTime=" + sentToPHRTracking.getSentDatetime());

		Long patientMyOscarId=MyOscarUtils.getMyOscarUserIdFromOscarDemographicId(myOscarLoggedInInfo, demographicId);
		List<Measurement> newMeasurements = measurementDao.findByDemographicIdUpdatedAfterDate(demographicId, sentToPHRTracking.getSentDatetime());

		// height weight is done separately so they can be paired up.
		sendHeightWeight(loggedInInfo,myOscarLoggedInInfo, newMeasurements);

		// everything other then height/weight
		for (Measurement measurement : newMeasurements) {
			logger.debug("sendMeasurementsToMyOscar : measurementId=" + measurement.getId());

			try {
				List<MeasurementType> measurementTypes = measurementTypeDao.findByType(measurement.getType());
				MeasurementType measurementType = null;
				if (measurementTypes.size() > 0) measurementType = measurementTypes.get(0);

				if (measurementType == null) {
					logger.warn("Missing measurement type, item not send, type=" + measurement.getType());
					continue;
				}

				if ("HT".equals(measurementType.getType()) || "WT".equals(measurementType.getType())) {
					// do nothing, processed in another loop
				} else if ("BP".equals(measurementType.getType())) {
					sendBloodPressure(loggedInInfo,myOscarLoggedInInfo, measurement);
				} else if ("A1C".equals(measurementType.getType())) {
					sendA1C(loggedInInfo,myOscarLoggedInInfo, measurement);
				} else {
					sendOtherHealthTracker(loggedInInfo,myOscarLoggedInInfo, patientMyOscarId, measurement, measurementType);
				}
			} catch (Exception e) {
				logger.error("Error", e);
			}
		}

		sentToPHRTracking.setSentDatetime(startSyncTime);
		sentToPHRTrackingDao.merge(sentToPHRTracking);
	}

	
	private static void sendHeightWeight(LoggedInInfo loggedInInfo,MyOscarLoggedInInfo myOscarLoggedInInfo, List<Measurement> newMeasurements) throws ClassCastException { // best way to do this would be to make a map of key=createTime, value=height&weight
		// populate the map, then process the new map.
		// this is so the height / weight are paired up properly.

		HashMap<Date, HeightWeight> hwMap = new HashMap<Date, HeightWeight>();

		for (Measurement measurement : newMeasurements) {
			List<MeasurementType> measurementTypes = measurementTypeDao.findByType(measurement.getType());
			MeasurementType measurementType = null;
			if (measurementTypes.size() > 0) measurementType = measurementTypes.get(0);

			if (measurementType == null) {
				logger.warn("Missing measurement type, item not send, type=" + measurement.getType());
				continue;
			}

			if ("HT".equals(measurementType.getType())) {
				HeightWeight hw = getOrCreateEmpty(hwMap, measurement.getCreateDate());
				hw.height = measurement;
			} else if ("WT".equals(measurementType.getType())) {
				HeightWeight hw = getOrCreateEmpty(hwMap, measurement.getCreateDate());
				hw.weight = measurement;
			}
		}

		for (Map.Entry<Date, HeightWeight> entry : hwMap.entrySet()) {

			try {
				HeightWeight hw = entry.getValue();

				logger.debug("sendHeightWeight : getCompositeId=" + hw.getCompositeId());

				MedicalDataTransfer4 medicalDataTransfer = toHeightWeightMedicalDataTransfer( loggedInInfo,myOscarLoggedInInfo, hw);
				MyOscarMedicalDataManagerUtils.addMedicalData(loggedInInfo.getLoggedInProviderNo(), myOscarLoggedInInfo, medicalDataTransfer, OSCAR_MEASUREMENTS_DATA_TYPE, hw.getCompositeId(), true, true);
			} catch (Exception e) {
				logger.error("Error", e);
			}
		}
	}

	private static MedicalDataTransfer4 toHeightWeightMedicalDataTransfer(LoggedInInfo loggedInInfo, MyOscarLoggedInInfo myOscarLoggedInInfo, HeightWeight hw) throws ParserConfigurationException, ClassCastException, ClassNotFoundException, InstantiationException, IllegalAccessException {
		MedicalDataTransfer4 medicalDataTransfer = MyOscarMedicalDataManagerUtils.getEmptyMedicalDataTransfer(myOscarLoggedInInfo, hw.getDateObserved(), hw.getProviderNo(), hw.getDemographicId());

		Document doc = toHeightWeightXml(hw);
		medicalDataTransfer.setData(XmlUtils.toString(doc, false));

		medicalDataTransfer.setMedicalDataType(MedicalDataType.HEIGHT_AND_WEIGHT.name());

		medicalDataTransfer.setOriginalSourceId(MyOscarMedicalDataManagerUtils.generateSourceId(loggedInInfo.getCurrentFacility().getName(), OSCAR_MEASUREMENTS_DATA_TYPE, hw.getCompositeId()));

		return (medicalDataTransfer);
	}

	private static Document toHeightWeightXml(HeightWeight hw) throws ParserConfigurationException {
		Document doc = XmlUtils.newDocument("Measurement");

		if (hw.weight != null) {
			XmlUtils.appendChildToRootIgnoreNull(doc, "Weight", hw.weight.getDataField());

			if (hw.weight.getMeasuringInstruction() != null) {
				String units = null;

				if (hw.weight.getMeasuringInstruction().startsWith("in ")) {
					units = hw.weight.getMeasuringInstruction().substring(3);
				} else {
					units = hw.weight.getMeasuringInstruction();
				}

				XmlUtils.appendChildToRootIgnoreNull(doc, "WeightUnits", units);
			}
		}

		if (hw.height != null) {
			XmlUtils.appendChildToRootIgnoreNull(doc, "Height", hw.height.getDataField());

			if (hw.height.getMeasuringInstruction() != null) {
				String units = null;

				if (hw.height.getMeasuringInstruction().startsWith("in ")) {
					units = hw.height.getMeasuringInstruction().substring(3);
				} else {
					units = hw.height.getMeasuringInstruction();
				}

				XmlUtils.appendChildToRootIgnoreNull(doc, "HeightUnits", units);
			}
		}

		return (doc);
	}

	private static HeightWeight getOrCreateEmpty(HashMap<Date, HeightWeight> hwMap, Date d) {
		HeightWeight hw = hwMap.get(d);
		if (hw == null) {
			hw = new HeightWeight();
			hwMap.put(d, hw);
		}
		return (hw);
	}

	private static void sendBloodPressure(LoggedInInfo loggedInInfo,MyOscarLoggedInInfo myOscarLoggedInInfo, Measurement measurement) {
		try {
			MedicalDataTransfer4 medicalDataTransfer = toBloodPressureMedicalDataTransfer( loggedInInfo,myOscarLoggedInInfo, measurement);

			MyOscarMedicalDataManagerUtils.addMedicalData(loggedInInfo.getLoggedInProviderNo(), myOscarLoggedInInfo, medicalDataTransfer, OSCAR_MEASUREMENTS_DATA_TYPE, measurement.getId(), true, true);
		} catch (Exception e) {
			logger.error("Error", e);
		}
	}

	private static MedicalDataTransfer4 toBloodPressureMedicalDataTransfer(LoggedInInfo loggedInInfo, MyOscarLoggedInInfo myOscarLoggedInInfo, Measurement measurement) throws ClassCastException, ClassNotFoundException, InstantiationException, IllegalAccessException, ParserConfigurationException {
		MedicalDataTransfer4 medicalDataTransfer = MyOscarMedicalDataManagerUtils.getEmptyMedicalDataTransfer(myOscarLoggedInInfo, measurement.getDateObserved(), measurement.getProviderNo(), measurement.getDemographicId());

		Document doc = toBloodPressureXml(measurement);
		medicalDataTransfer.setData(XmlUtils.toString(doc, false));

		medicalDataTransfer.setMedicalDataType(MedicalDataType.BLOOD_PRESSURE.name());

		medicalDataTransfer.setOriginalSourceId(MyOscarMedicalDataManagerUtils.generateSourceId(loggedInInfo.getCurrentFacility().getName(), OSCAR_MEASUREMENTS_DATA_TYPE, measurement.getId()));

		return (medicalDataTransfer);
	}

	private static Document toBloodPressureXml(Measurement measurement) throws ParserConfigurationException {
		Document doc = XmlUtils.newDocument("Measurement");

		XmlUtils.appendChildToRootIgnoreNull(doc, "Units", "mm[Hg]");

		String value = measurement.getDataField();
		String[] valueSplit = value.split("/");
		String systolic = valueSplit[0];
		String diastolic = valueSplit[1];

		XmlUtils.appendChildToRootIgnoreNull(doc, "SystolicValue", systolic);
		XmlUtils.appendChildToRootIgnoreNull(doc, "DiastolicValue", diastolic);

		return (doc);
	}

	private static void sendA1C(LoggedInInfo loggedInInfo,MyOscarLoggedInInfo myOscarLoggedInInfo, Measurement measurement) {
		try {
			MedicalDataTransfer4 medicalDataTransfer = toA1CMedicalDataTransfer( loggedInInfo,myOscarLoggedInInfo, measurement);

			MyOscarMedicalDataManagerUtils.addMedicalData(loggedInInfo.getLoggedInProviderNo(), myOscarLoggedInInfo, medicalDataTransfer, OSCAR_MEASUREMENTS_DATA_TYPE, measurement.getId(), true, true);
		} catch (Exception e) {
			logger.error("Error", e);
		}
	}

	private static MedicalDataTransfer4 toA1CMedicalDataTransfer(LoggedInInfo loggedInInfo, MyOscarLoggedInInfo myOscarLoggedInInfo, Measurement measurement) throws ClassCastException, ClassNotFoundException, InstantiationException, IllegalAccessException, ParserConfigurationException {
		MedicalDataTransfer4 medicalDataTransfer = MyOscarMedicalDataManagerUtils.getEmptyMedicalDataTransfer(myOscarLoggedInInfo, measurement.getDateObserved(), measurement.getProviderNo(), measurement.getDemographicId());

		Document doc = toA1CXml(measurement);
		medicalDataTransfer.setData(XmlUtils.toString(doc, false));

		medicalDataTransfer.setMedicalDataType(MedicalDataType.GLUCOSE_A_1_C.name());

		medicalDataTransfer.setOriginalSourceId(MyOscarMedicalDataManagerUtils.generateSourceId(loggedInInfo.getCurrentFacility().getName(), OSCAR_MEASUREMENTS_DATA_TYPE, measurement.getId()));

		return (medicalDataTransfer);
	}

	private static Document toA1CXml(Measurement measurement) throws ParserConfigurationException {
		Document doc = XmlUtils.newDocument("Measurement");

		XmlUtils.appendChildToRootIgnoreNull(doc, "Units", "%");
		XmlUtils.appendChildToRootIgnoreNull(doc, "Glucose", measurement.getDataField());

		return (doc);
	}

	private static void sendOtherHealthTracker(LoggedInInfo loggedInInfo, MyOscarLoggedInInfo myOscarLoggedInInfo, Long patientMyOscarId, Measurement measurement, MeasurementType measurementType) throws ClassCastException {
		// for each measurement, check that a category exists, if not create a category first
		// then send the measurement tied to the category.

		try {
			MedicalDataTransfer4 ohtCategory = ensureOHTCategoryExists(loggedInInfo, myOscarLoggedInInfo, patientMyOscarId, measurement, measurementType);
			MedicalDataTransfer4 medicalDataTransfer = toOHTMedicalDataTransfer(loggedInInfo, myOscarLoggedInInfo, measurement, ohtCategory);

			MyOscarMedicalDataManagerUtils.addMedicalData(loggedInInfo.getLoggedInProviderNo(), myOscarLoggedInInfo, medicalDataTransfer, OSCAR_MEASUREMENTS_DATA_TYPE, measurement.getId(), true, true);
		} catch (Exception e) {
			logger.error("Error", e);
		}
	}

	private static MedicalDataTransfer4 toOHTMedicalDataTransfer(LoggedInInfo loggedInInfo, MyOscarLoggedInInfo myOscarLoggedInInfo, Measurement measurement, MedicalDataTransfer4 ohtCategory) throws ParserConfigurationException, ClassCastException, ClassNotFoundException, InstantiationException, IllegalAccessException {
		MedicalDataTransfer4 medicalDataTransfer = MyOscarMedicalDataManagerUtils.getEmptyMedicalDataTransfer(myOscarLoggedInInfo, measurement.getDateObserved(), measurement.getProviderNo(), measurement.getDemographicId());

		Document doc = toOHTXml(measurement);
		medicalDataTransfer.setData(XmlUtils.toString(doc, false));

		medicalDataTransfer.setMedicalDataType(MedicalDataType.OTHER_HEALTH_TRACKER_CATEGORY.name() + '.' + ohtCategory.getId());

		medicalDataTransfer.setOriginalSourceId(MyOscarMedicalDataManagerUtils.generateSourceId(loggedInInfo.getCurrentFacility().getName(), OSCAR_MEASUREMENTS_DATA_TYPE, measurement.getId()));

		return (medicalDataTransfer);
	}

	private static Document toOHTXml(Measurement measurement) throws ParserConfigurationException {
		Document doc = XmlUtils.newDocument("Measurement");

		XmlUtils.appendChildToRootIgnoreNull(doc, "Value", measurement.getDataField());

		return (doc);
	}

	/**
	 * get the oht category, create one if it doesn't already exist
	 */
	private static MedicalDataTransfer4 ensureOHTCategoryExists(LoggedInInfo loggedInInfo, MyOscarLoggedInInfo myOscarLoggedInInfo, Long patientMyOscarId, Measurement measurement, MeasurementType measurementType) throws IOException, SAXException, ParserConfigurationException, NoSuchItemException_Exception, NotAuthorisedException_Exception, ClassCastException, ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedEncodingException_Exception, InvalidRequestException_Exception {
		// we'll assume there's not more than 1024 categories.
		int MAX_SIZE = 1024;		
		List<MedicalDataTransfer4> categories = MyOscarMedicalDataManagerUtils.getMedicalData(myOscarLoggedInInfo, patientMyOscarId, MedicalDataType.OTHER_HEALTH_TRACKER_CATEGORY.name(), true, 0, MAX_SIZE);
		if (categories.size() >= MAX_SIZE) logger.error("Some one has run into our hard coded limit for categoriy size retrieval.");

		for (MedicalDataTransfer4 category : categories) {
			category = MyOscarMedicalDataManagerUtils.materialiseDataIfRequired(myOscarLoggedInInfo, category);
			String categoryName = getOHTCategoryName(category);
			if (categoryName != null && categoryName.equals(measurementType.getType())) return (category);
		}

		// non exist, create one.
		MedicalDataTransfer4 medicalDataTransfer = MyOscarMedicalDataManagerUtils.getEmptyMedicalDataTransfer(myOscarLoggedInInfo, new Date(), measurement.getProviderNo(), measurement.getDemographicId());
		medicalDataTransfer.setData(makeOHTCategoryXmlString(measurementType));
		medicalDataTransfer.setMedicalDataType(MedicalDataType.OTHER_HEALTH_TRACKER_CATEGORY.name());
		// not sure why we're allowed to modify OHT records, but we are... *shrugs*
		Long resultId=MyOscarMedicalDataManagerUtils.addMedicalData(loggedInInfo.getLoggedInProviderNo(), myOscarLoggedInInfo, medicalDataTransfer, "OHT_CATEGORY", "OHT_CATEGORY", false, true);
		medicalDataTransfer=MyOscarMedicalDataManagerUtils.getMedicalData(myOscarLoggedInInfo, medicalDataTransfer.getOwningPersonId(), resultId);
		
		return (medicalDataTransfer);
	}

	private static String getOHTCategoryName(MedicalDataTransfer4 category) throws IOException, SAXException, ParserConfigurationException {
		String xmlDataString = category.getData();
		Document doc = XmlUtils.toDocument(xmlDataString);
		String result = XmlUtils.getChildNodeTextContents(doc.getFirstChild(), "Name");
		return (result);
	}

	private static String makeOHTCategoryXmlString(MeasurementType measurementType) throws ParserConfigurationException, ClassCastException, ClassNotFoundException, InstantiationException, IllegalAccessException {
		Document doc = XmlUtils.newDocument("OtherHealthDataCategory");

		XmlUtils.appendChildToRootIgnoreNull(doc, "Name", measurementType.getType());
		XmlUtils.appendChildToRootIgnoreNull(doc, "Description", measurementType.getTypeDescription());
		XmlUtils.appendChildToRootIgnoreNull(doc, "Units", measurementType.getMeasuringInstruction());

		return (XmlUtils.toString(doc, false));
	}

}
