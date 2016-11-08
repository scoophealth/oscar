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
 
package org.oscarehr.common.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;
import java.util.TreeSet;

import org.oscarehr.common.dao.EFormDao;
import org.oscarehr.common.dao.EFormDataDao;
import org.oscarehr.common.dao.EFormValueDao;
import org.oscarehr.common.dao.PropertyDao;
import org.oscarehr.common.dao.ProviderDataDao;
import org.oscarehr.common.model.EForm;
import org.oscarehr.common.model.EFormData;
import org.oscarehr.common.model.EFormValue;
import org.oscarehr.common.model.Property;
import org.oscarehr.common.model.ProviderData;
import org.oscarehr.util.SpringUtils;

import oscar.util.StringUtils;

public class FieldNoteManager {

	static TreeSet<Integer> fieldNoteEforms = new TreeSet<Integer>();
	static TreeSet<Integer> fieldNoteNameEforms = new TreeSet<Integer>();
	static HashMap<String, TreeSet<Integer>> residentFieldNotes = new HashMap<String, TreeSet<Integer>>();
	static HashMap<String, HashMap<String, Integer>> supervisorFieldNotes = new HashMap<String, HashMap<String, Integer>>();
	
	private static PropertyDao propertyDao = (PropertyDao) SpringUtils.getBean("propertyDao");
	private static EFormDao eformDao = (EFormDao) SpringUtils.getBean("EFormDao");
	private static EFormDataDao eformDataDao = (EFormDataDao) SpringUtils.getBean("EFormDataDao");
	private static EFormValueDao eformValueDao = (EFormValueDao) SpringUtils.getBean("EFormValueDao");
	private static ProviderDataDao providerDataDao = (ProviderDataDao) SpringUtils.getBean("providerDataDao");

	public static TreeSet<Integer> getFieldNoteEforms()
	{
		Property property = propertyDao.checkByName("fieldNoteEform");
		if (property == null)
		{
			fieldNoteEforms.clear();
		}
		else
		{
			String value = property.getValue();
			if (value != null && !value.trim().isEmpty())
			{
				String[] fids = value.split(",");
				for (String fid : fids){
					if (StringUtils.isInteger(fid)) fieldNoteEforms.add(Integer.valueOf(fid));
				}
			}
		}
		return fieldNoteEforms;
	}
	
	public static TreeSet<Integer> getFieldNoteNameEforms(String customName)
	{
		List<EForm> efList = eformDao.findByNameSimilar("field note");
		efList.addAll(eformDao.findByNameSimilar(customName));
		fieldNoteNameEforms.clear();
		
		for (EForm eform : efList)
		{
			if (eform != null)
			{
				fieldNoteNameEforms.add(eform.getId());
			}
		}
	
		//remove eforms already selected as field notes
		fieldNoteNameEforms.removeAll(fieldNoteEforms);

		return fieldNoteNameEforms;
	}
	
	public static void unSelectFieldNoteEform(String fidUnselect)
	{
		if (!StringUtils.isInteger(fidUnselect)) return;
		
		TreeSet<Integer> fieldNoteEformSet = getFieldNoteEforms();
		fieldNoteEformSet.remove(Integer.valueOf(fidUnselect));
		saveFieldNoteEformProperty(fieldNoteEformSet);
	}
	
	public static void selectFieldNoteEforms(String[] fids)
	{
		if (fids==null || fids.length==0) return;
		
		TreeSet<Integer> fieldNoteEformSet = getFieldNoteEforms();
		for (String fid : fids)
		{
			if (StringUtils.isInteger(fid)) fieldNoteEformSet.add(Integer.valueOf(fid));
		}
		saveFieldNoteEformProperty(fieldNoteEformSet);
	}
	
	public static TreeMap<String, String> getResidentNameList(TreeSet<Integer> fids, Date dateStart, Date dateEnd)
	{
		resetResidentList(fids, dateStart, dateEnd);
		TreeMap<String, String> residentNameList = new TreeMap<String, String>();
		
		for (String residentId : residentFieldNotes.keySet())
		{
			String residentName = getProviderName(residentId);
			if (residentName != null) residentNameList.put(residentName, residentId);
		}
		return residentNameList;
	}
	
	public static TreeMap<String, TreeMap<String, Integer>> getSupervisorResidentCountList()
	{
		TreeMap<String, TreeMap<String, Integer>> supervisorResidentCountList = new TreeMap<String, TreeMap<String, Integer>>();
		for (String supervisorId : supervisorFieldNotes.keySet()) {
			TreeMap<String, Integer> residentCountList = new TreeMap<String, Integer>();
			for (String residentId : supervisorFieldNotes.get(supervisorId).keySet()) {
				String residentName = getProviderName(residentId);
				if (residentName != null) {
					residentCountList.put(residentName, supervisorFieldNotes.get(supervisorId).get(residentId));
				}
			}
			String supervisorName = getProviderName(supervisorId);
			if (supervisorName != null) {
				supervisorResidentCountList.put(supervisorName, residentCountList);
			}
		}
		return supervisorResidentCountList;
	}
	
	public static HashMap<Integer, List<EFormValue>> getResidentFieldNoteValues(String residentId)
	{
		HashMap<Integer, List<EFormValue>> fieldNoteValues = new HashMap<Integer, List<EFormValue>>();
		TreeSet<Integer> fdids = residentFieldNotes.get(residentId);
		if (fdids != null)
		{
			for (Integer fdid : fdids)
			{
				List<EFormValue> valueList = eformValueDao.findByFormDataId(fdid);
				if (valueList != null)
				{
					fieldNoteValues.put(fdid, valueList);
				}
			}
		}
		return fieldNoteValues;
	}
	
	public static HashMap<Integer, List<EFormValue>> filterResidentFieldNoteValues(HashMap<Integer, List<EFormValue>> fieldNoteValues, String varName)
	{
		return filterResidentFieldNoteValues(fieldNoteValues, varName, "\b"); //"\b" is used as indicator for "match whatever value"
	}
	
	public static HashMap<Integer, List<EFormValue>> filterResidentFieldNoteValues(HashMap<Integer, List<EFormValue>> fieldNoteValues, String varName, String varValue)
	{	
		if (fieldNoteValues == null) return fieldNoteValues;
		if (StringUtils.empty(varName)) return fieldNoteValues;
		
		TreeSet<Integer> fdids = new TreeSet<Integer>();
		for (Integer fdid : fieldNoteValues.keySet())
		{
			for (EFormValue fieldNoteValue : fieldNoteValues.get(fdid))
			{
				if (!varName.equals(fieldNoteValue.getVarName())) continue;
				
				if (varValue.equals("\b") || StringUtils.nullSafeEquals(varValue, fieldNoteValue.getVarValue()))
				{
					fdids.add(fdid);
					break;
				}
			}
		}

		HashMap<Integer, List<EFormValue>> newValues = new HashMap<Integer, List<EFormValue>>(); 
		for (Integer fdid : fdids)
		{
			newValues.put(fdid, fieldNoteValues.get(fdid));
		}
		return newValues;
	}
	
	public static int countItem(HashMap<Integer, List<EFormValue>> fieldNoteValues, String varName)
	{
		if (fieldNoteValues == null) return 0;
		if (StringUtils.empty(varName)) return 0;

		int counter = 0;
		for (Integer fdid : fieldNoteValues.keySet())
		{
			for (EFormValue fieldNoteValue : fieldNoteValues.get(fdid))
			{
				if (varName.equals(fieldNoteValue.getVarName())) counter++;
			}
		}
		return counter;
	}
	
	public static int countItem(HashMap<Integer, List<EFormValue>> fieldNoteValues, String varName, String varValue)
	{
		if (fieldNoteValues == null) return 0;
		if (StringUtils.empty(varName)) return 0;
		if (StringUtils.empty(varValue)) return 0;

		int counter = 0;
		for (Integer fdid : fieldNoteValues.keySet())
		{
			for (EFormValue fieldNoteValue : fieldNoteValues.get(fdid))
			{
				if (varName.equals(fieldNoteValue.getVarName()) && varValue.trim().equals(fieldNoteValue.getVarValue().trim())) counter++;
			}
		}
		return counter;
	}
	
	public static String getValue(List<EFormValue> valuesOf1FieldNote, String varName)
	{
		if (valuesOf1FieldNote == null) return null;
		if (StringUtils.empty(varName)) return null;
		
		for (EFormValue oneValue : valuesOf1FieldNote)
		{
			if (varName.equals(oneValue.getVarName())) return oneValue.getVarValue();
		}
		return null;
	}
	
	public static String getValues(List<EFormValue> valuesOf1FieldNote, String ...varNames)
	{
		String values = null;
		for (String varName : varNames)
		{
			String value = getValue(valuesOf1FieldNote, varName);
			if (StringUtils.empty(value)) continue;
			
			if (StringUtils.filled(values)) values += ", " + value;
			else values = value;
		}
		return values;
	}
	
	public static int getTotalNumberOfFieldNotes(String residentId)
	{
		TreeSet<Integer> fdids = residentFieldNotes.get(residentId);
		return fdids!=null ? fdids.size() : 0;
	}
	
//*** Private methods ***
	
	private static void saveFieldNoteEformProperty(TreeSet<Integer> fieldNoteSet)
	{
		if (fieldNoteSet == null) return;
		
		String newList = null;
		for (Integer fid : fieldNoteSet)
		{
			if (newList == null) newList = fid.toString();
			else newList += "," + fid;
		}
		
		Property property = propertyDao.checkByName("fieldNoteEform");
		if (property == null)
		{
			property = new Property();
			property.setName("fieldNoteEform");
		}
		property.setValue(newList);
		propertyDao.merge(property);
	}
	
	private static void resetResidentList(TreeSet<Integer> fids, Date dateStart, Date dateEnd)
	{
		residentFieldNotes.clear();
		supervisorFieldNotes.clear();
		List<EFormData> efDatas = eformDataDao.findByFidsAndDates(fids, dateStart, dateEnd);
		HashMap<Integer, String> fdidProviderIdHash = getFdidProviderIdHash(efDatas);
		List<EFormValue> efValues = eformValueDao.findByFormDataIdList(new ArrayList<Integer>(fdidProviderIdHash.keySet()));
		if (efValues == null) return;
		
		for (EFormValue efValue : efValues) {
			if (!"residentId".equals(efValue.getVarName())) continue;
			
			String residentId = efValue.getVarValue();
			TreeSet<Integer> residentFdids = residentFieldNotes.get(residentId);
			if (residentFdids == null) {
				residentFdids = new TreeSet<Integer>();
				residentFieldNotes.put(residentId, residentFdids);
			}
			residentFdids.add(efValue.getFormDataId());
			
			String providerId = fdidProviderIdHash.get(efValue.getFormDataId());
			HashMap<String, Integer> residentIdCountFdid = supervisorFieldNotes.get(providerId);
			if (residentIdCountFdid == null) {
				residentIdCountFdid = new HashMap<String, Integer>();
				supervisorFieldNotes.put(providerId, residentIdCountFdid);
			}
			Integer countFdid = residentIdCountFdid.get(residentId);
			if (countFdid == null) countFdid = 0;
			residentIdCountFdid.put(residentId, ++countFdid);
		}
	}
	
	private static HashMap<Integer, String> getFdidProviderIdHash(List<EFormData> eformDatas) {
		HashMap<Integer, String> fdidProviderIdHash = new HashMap<Integer, String>();
		if (eformDatas != null) {
			for (EFormData eformData : eformDatas) {
				fdidProviderIdHash.put(eformData.getId(), eformData.getProviderNo());
			}
		}
		return fdidProviderIdHash;
	}
	
	private static String getProviderName(String providerId) {
		ProviderData pd = providerDataDao.find(providerId);
		if (pd != null) return pd.getLastName() + ", " + pd.getFirstName();
		else return null;
	}
}
