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


package oscar.oscarEncounter.immunization.data;

import java.util.List;
import java.util.Vector;

import org.oscarehr.common.dao.ConfigImmunizationDao;
import org.oscarehr.common.model.ConfigImmunization;
import org.oscarehr.util.SpringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import oscar.util.UtilXML;

public class EctImmConfigData {

	private ConfigImmunizationDao configImmunizationDao = (ConfigImmunizationDao)SpringUtils.getBean("configImmunizationDao");

	public String getImmunizationConfig() {

		Document doc = UtilXML.newDocument();
		Element root = UtilXML.addNode(doc, "immunization");
		Node newSet;

		List<ConfigImmunization> configImmunations = configImmunizationDao.findAll();
		for(ConfigImmunization configImmunation:configImmunations) {
			Document setDoc = UtilXML.parseXML(configImmunation.getXmlDoc());
			Element setRoot = setDoc.getDocumentElement();
			newSet = doc.importNode(setRoot, true);
			root.appendChild(newSet);
		}

		return UtilXML.toXML(doc);
	}

	public Vector getImmunizationConfigName() {
		Vector ret = new Vector();

		List<ConfigImmunization> configImmunations = configImmunizationDao.findAll();
		for(ConfigImmunization configImmunation:configImmunations) {
			ret.add(configImmunation.getName());
		}

		return ret;
	}

	public Vector getImmunizationConfigId() {
		Vector ret = new Vector();

		List<ConfigImmunization> configImmunations = configImmunizationDao.findAll();
		for(ConfigImmunization configImmunation:configImmunations) {
			ret.add(configImmunation.getId());
		}

		return ret;
	}

}
