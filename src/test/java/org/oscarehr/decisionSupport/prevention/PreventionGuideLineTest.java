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
package org.oscarehr.decisionSupport.prevention;

import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.drools.RuleBase;

import org.junit.Test;
import org.oscarehr.util.MiscUtils;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

public class PreventionGuideLineTest {
	private static final Logger logger=MiscUtils.getLogger();

	byte[] getRuleSet() throws Exception{
		InputStream is = this.getClass().getResourceAsStream("/org/oscarehr/decisionSupport/prevention/guidelineExample.xml");
		byte[] bytes = IOUtils.toByteArray(is);
		return bytes;
	}
	
	@Test
	public void testLoading() throws Exception{
		RuleBase rulebase = DSPreventionDrools.createRuleBase(getRuleSet());
		assertNotNull(rulebase);
		assertEquals(rulebase.getRuleSets().size(),1);
		logger.error(rulebase.getRuleSets().get(0).getClass());
	}
		
}
