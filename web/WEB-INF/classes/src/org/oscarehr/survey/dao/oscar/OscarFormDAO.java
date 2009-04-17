/*
* 
* Copyright (c) 2001-2002. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved. *
* This software is published under the GPL GNU General Public License. 
* This program is free software; you can redistribute it and/or 
* modify it under the terms of the GNU General Public License 
* as published by the Free Software Foundation; either version 2 
* of the License, or (at your option) any later version. * 
* This program is distributed in the hope that it will be useful, 
* but WITHOUT ANY WARRANTY; without even the implied warranty of 
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
* GNU General Public License for more details. * * You should have received a copy of the GNU General Public License 
* along with this program; if not, write to the Free Software 
* Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. * 
* 
* <OSCAR TEAM>
* 
* This software was written for 
* Centre for Research on Inner City Health, St. Michael's Hospital, 
* Toronto, Ontario, Canada 
*/

package org.oscarehr.survey.dao.oscar;

import java.io.OutputStream;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.oscarehr.survey.model.oscar.OscarForm;
import org.oscarehr.survey.model.oscar.OscarFormData;
import org.oscarehr.survey.model.oscar.OscarFormInstance;

public interface OscarFormDAO {
	public List getOscarForms();
	
	public void saveOscarForm(OscarForm form);
	public void updateStatus(Long formId, Short status);
	public OscarForm getOscarForm(Long formId);
	
	
	public void saveOscarFormInstance(OscarFormInstance instance);
	public void saveOscarFormData(OscarFormData data);
	public OscarFormInstance getOscarFormInstance(Long formId, Long clientId);
	public List getOscarForms(Long formId, Long clientId);
	public List getOscarFormsByClientId(Long clientId);
	
	public void generateCSV(Long formId, OutputStream out);
	public void generateInverseCSV(Long formId, OutputStream out);
	public void convertFormXMLToDb(Long formId);
	public Map<String[],String> getFormReport(Long formId, Date startDate, Date endDate) ;
}
