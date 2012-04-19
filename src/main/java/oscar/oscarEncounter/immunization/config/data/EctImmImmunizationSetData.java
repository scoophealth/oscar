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


package oscar.oscarEncounter.immunization.config.data;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import org.oscarehr.common.dao.ConfigImmunizationDao;
import org.oscarehr.common.model.ConfigImmunization;
import org.oscarehr.util.SpringUtils;
import org.w3c.dom.Document;

import oscar.util.UtilXML;

public class EctImmImmunizationSetData {

	private ConfigImmunizationDao configImmunizationDao = (ConfigImmunizationDao)SpringUtils.getBean("configImmunizationDao");

	public Vector createDateVec;
	public Vector setIdVec;
	public Vector setNameVec;

	public EctImmImmunizationSetData() {
		setNameVec = new Vector();
		setIdVec = new Vector();
		createDateVec = new Vector();
	}

	public void addImmunizationSet(String setName, Document setXmlDoc, String providerNo) {

		ConfigImmunization configImmunization = new ConfigImmunization();
		configImmunization.setName(setName);
		configImmunization.setXmlDoc(UtilXML.toXML(setXmlDoc));
		configImmunization.setCreateDate(new Date());
		configImmunization.setProviderNo(providerNo);
		configImmunizationDao.persist(configImmunization);

	}

	public boolean estImmunizationVecs() {
		boolean verdict = true;

		SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
		List<ConfigImmunization> configImmunizations =  configImmunizationDao.findByArchived(0,false);
		for(ConfigImmunization configImmunization:configImmunizations) {
			createDateVec.add(dateFormatter.format(configImmunization.getCreateDate()));
			setNameVec.add(configImmunization.getName());
			setIdVec.add(String.valueOf(configImmunization.getId()));
		}

		return verdict;
	}

	public boolean estImmunizationVecs(int stat) {
		boolean verdict = true;

		SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
		List<ConfigImmunization> configImmunizations =  configImmunizationDao.findByArchived(stat,true);
		for(ConfigImmunization configImmunization:configImmunizations) {
			createDateVec.add(dateFormatter.format(configImmunization.getCreateDate()));
			setNameVec.add(configImmunization.getName());
			setIdVec.add(String.valueOf(configImmunization.getId()));
		}

		return verdict;
	}

	public String getSetXMLDoc(String setId) {
		String xmlDoc = null;

		ConfigImmunization configImmunization = this.configImmunizationDao.find(Integer.parseInt(setId));
		if(configImmunization != null) {
			xmlDoc = configImmunization.getXmlDoc();
		}

		return xmlDoc;
	}

	public void updateImmunizationSetStatus(String setId, int stat) {
		ConfigImmunization configImmunization = this.configImmunizationDao.find(Integer.parseInt(setId));
		if(configImmunization != null) {
			configImmunization.setArchived(stat);
			configImmunizationDao.merge(configImmunization);
		}
	}
}
