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

package oscar.oscarPrevention;

//import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.URL;


import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.drools.RuleBase;
import org.drools.WorkingMemory;
import org.drools.io.RuleBaseLoader;
import org.oscarehr.common.dao.ResourceStorageDao;
import org.oscarehr.common.model.ResourceStorage;
import org.oscarehr.decisionSupport.prevention.DSPreventionDrools;
import org.oscarehr.drools.RuleBaseFactory;
import org.oscarehr.util.MiscUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import oscar.OscarProperties;

/**
 *
 * @author Jay Gallagher
 */
@Component
public class PreventionDS {

	private static Logger log = MiscUtils.getLogger();

	@Autowired
	private ResourceStorageDao resourceStorageDao;// = SpringUtils.getBean(ResourceStorageDao.class);

	private RuleBase getRuleBase() {
		long timer = System.currentTimeMillis();

		try {
			String preventionPath = OscarProperties.getInstance().getProperty("PREVENTION_FILE");

			if (preventionPath != null) {
				File file = new File(OscarProperties.getInstance().getProperty("PREVENTION_FILE"));
				RuleBase ruleBase = RuleBaseFactory.getRuleBase(file.getCanonicalPath());
				if (ruleBase != null) return (ruleBase);

				if (file.isFile() || file.canRead()) {
					log.debug("Loading from file " + file.getName());

					FileInputStream fis = new FileInputStream(file);
					try {
						ruleBase = RuleBaseLoader.loadFromInputStream(fis);
						RuleBaseFactory.putRuleBase(file.getCanonicalPath(), ruleBase);
						return (ruleBase);
					} catch(Exception e) {
			            	   MiscUtils.getLogger().error("Error loading preventions",e);
					} finally {
						IOUtils.closeQuietly(fis);
					}
				}

				if (preventionPath.startsWith("classpath:")) {
					String urlString = preventionPath.substring(10);

					ruleBase = RuleBaseFactory.getRuleBase(urlString);
					if (ruleBase != null) return (ruleBase);

					URL url = PreventionDS.class.getResource(urlString);
					log.debug("loading from URL " + url.getFile());
					ruleBase = RuleBaseLoader.loadFromUrl(url);
					RuleBaseFactory.putRuleBase(urlString, ruleBase);
					return (ruleBase);
				}
			}

			log.debug("getRuleBase part1 TimeMs : " + (System.currentTimeMillis() - timer));
			timer = System.currentTimeMillis();

			ResourceStorage resourceStorage = resourceStorageDao.findActive(ResourceStorage.PREVENTION_RULES);
			if (resourceStorage != null) {
				RuleBase ruleBase = RuleBaseFactory.getRuleBase("ResourceStorage:" + resourceStorage.getId());
				if (ruleBase != null) return (ruleBase);

				try {
					ruleBase = DSPreventionDrools.createRuleBase(resourceStorage.getFileContents());
					log.info("Loading prevention rule base from " + resourceStorage.getResourceName());
					RuleBaseFactory.putRuleBase("ResourceStorage:" + resourceStorage.getId(), ruleBase);
					return (ruleBase);
				} catch (Exception resourceError) {
					log.error("ERROR LOADING from resource Storage", resourceError);
				}
			}

			log.debug("getRuleBase part2 TimeMs : " + (System.currentTimeMillis() - timer));
			timer = System.currentTimeMillis();

			String urlString = "/oscar/oscarPrevention/prevention.drl";
			RuleBase ruleBase = RuleBaseFactory.getRuleBase(urlString);
			if (ruleBase != null) return (ruleBase);

			URL url = PreventionDS.class.getResource(urlString); //TODO: change this so it is configurable;
			log.debug("loading from URL " + url.getFile());
			ruleBase = RuleBaseLoader.loadFromUrl(url);
			RuleBaseFactory.putRuleBase(urlString, ruleBase);
			return (ruleBase);

		} catch (Exception e) {
			MiscUtils.getLogger().error("Error", e);
		} finally {
			log.debug("getRuleBase part3 TimeMs : " + (System.currentTimeMillis() - timer));
		}
		return (null);
	}

	public Prevention getMessages(Prevention p) throws Exception {
		try {
			RuleBase ruleBase = getRuleBase();
			WorkingMemory workingMemory = ruleBase.newWorkingMemory();
			workingMemory.assertObject(p);
			workingMemory.fireAllRules();
		} catch (Exception e) {
			MiscUtils.getLogger().error("Error", e);
			throw new Exception("ERROR: Drools ", e);
		}
		return p;
	}
}
