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
package org.oscarehr.drools;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.time.DateUtils;
import org.drools.RuleBase;
import org.drools.io.RuleBaseLoader;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.QueueCache;

import oscar.OscarProperties;
import oscar.oscarEncounter.oscarMeasurements.MeasurementFlowSheet;

public final class RuleBaseFactory {

	private static QueueCache<String, RuleBase> ruleBaseInstances = new QueueCache<String, RuleBase>(4, 2048, DateUtils.MILLIS_PER_DAY, null);

	public static synchronized RuleBase getRuleBase(String sourceKey) {
		return (ruleBaseInstances.get(sourceKey));
	}

	public static synchronized void putRuleBase(String sourceKey, RuleBase ruleBase) {
		ruleBaseInstances.put(sourceKey, ruleBase);
	}

	public static synchronized void removeRuleBase(String sourceKey) {
		ruleBaseInstances.remove(sourceKey);
	}

	public static synchronized void flushAllCached() {
		ruleBaseInstances = new QueueCache<String, RuleBase>(4, 2048, DateUtils.MILLIS_PER_DAY, null);
	}

	public static RuleBase getMeasurementFileAsRuleBase(String fileName) {
		try {
			String measurementDirPath = OscarProperties.getInstance().getProperty("MEASUREMENT_DS_DIRECTORY");

			if (measurementDirPath != null) {
				File file = new File(measurementDirPath + fileName);

				RuleBase ruleBase = getRuleBase(file.getCanonicalPath());
				if (ruleBase!=null) return(ruleBase);

				if (file.isFile() || file.canRead()) {
					MiscUtils.getLogger().debug("Loading from file " + file.getName());
					FileInputStream fis = new FileInputStream(file);

					try {
						ruleBase = RuleBaseLoader.loadFromInputStream(fis);
					} finally {
						IOUtils.closeQuietly(fis);
					}

					putRuleBase(file.getCanonicalPath(), ruleBase);
					return(ruleBase);
				}
			}

			String urlString = "/oscar/oscarEncounter/oscarMeasurements/flowsheets/decisionSupport/" + fileName;
			RuleBase ruleBase = RuleBaseFactory.getRuleBase(urlString);
			if (ruleBase != null) return(ruleBase);
			
			URL url = MeasurementFlowSheet.class.getResource(urlString); //TODO: change this so it is configurable;
			MiscUtils.getLogger().debug("loading from URL " + url.getFile());
			ruleBase = RuleBaseLoader.loadFromUrl(url);
			RuleBaseFactory.putRuleBase(urlString, ruleBase);
		} catch (Exception e) {
			MiscUtils.getLogger().error("Error", e);
		}
		return null;
	}
}
