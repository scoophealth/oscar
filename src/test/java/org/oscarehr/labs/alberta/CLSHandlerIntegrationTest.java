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
package org.oscarehr.labs.alberta;

import static org.junit.Assert.assertEquals;

import java.io.InputStream;
import java.security.PrivateKey;
import java.security.PublicKey;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.oscarehr.common.dao.utils.AuthUtils;
import org.oscarehr.common.dao.utils.ConfigUtils;
import org.oscarehr.common.dao.utils.SchemaUtils;
import org.oscarehr.common.hl7.v2.oscar_to_oscar.SendingUtils;
import org.oscarehr.common.model.Provider;
import org.oscarehr.research.eaaps.EaapsIntegrationTest;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.SpringUtils;
import org.springframework.context.support.ClassPathXmlApplicationContext;

//Ignored until we fix foreign keys with tests
@Ignore
public class CLSHandlerIntegrationTest {

	private static Logger logger = Logger.getLogger(EaapsIntegrationTest.class);
	
	/* SYSTEM-SPECIFIC TEST VALUES */
	
	/**
	 * Public OSCAR key
	 */
	private static final String OSCAR_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDxvzlAHc8QOI30bT+OtUs7GiebFbT0Fp+Cn2kj 5lYqp9c8ejOXzGFBY6A80CA+a6DGLQoqCPkmCKZ8OFH01xGgkAQIzGuLF90iAZV348RHUvwu7qg1 f3zi8k7O+1uq6/cWidSb2jUnAiYpa21uBSMNfSwtxIp2LzmuRQF+xEKhTQIDAQAB";

	
	/**
	 * Public client key
	 */
	private static final String CLIENT_KEY = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAMbA/Di5Xtk+4qqzAw1KkF5W8GEm k9uYvAY8H6A+6NcFV+vblPzg1WNvp5OSeqoMggvDb0r+AdAkcefkcXherQ0Fv3YCtCJt+DAGMG+0 OHmDMdKSIfNoG7gh/fV2btzyw7AZ5B/fP/cOXvjv3R2TCEHxmepnQGCRCbNFkchJwSbZAgMBAAEC gYBNOX7Gq3/m+UApAxUUfPxLK4yKsuqQUG/+HC5NnSPrJ/BZfCAPgWxoDmIWPLvchq+g0nbTtI4P yZlYeLJ+CotB7KlbAIF7xm+xE8iqkSojclJ2myU8lOpQ39GiiO+KDkfA2cHA4ftIwkDqudmnnRGs lIAWabYG1P8sgjSD2MHAAQJBAOoJ5XTPoIO9QDXV63q4GhlUIqkmZ0U51l0VjrEDW4Wu8puOw38a uO10ht+lkCbniiGEoqMZyJSD9H6U2waan9kCQQDZZ3khl6gJzaQYzQ+/XZTu6ecbc8n2k/hZB5Vk pSRQMH8RFkeB4g7xSTggvbC+JpKX5eEfeLRYyuXFSepQXV8BAkEAuWzl00q9TiMfQIggbbZ3VyIF 5CZ9I6fTYyS1TSHv3Vbi+MR/t7CgW+I7Ce7O60P/eNbxVHAVLzXs/G1Lq0vO4QJAGZ9kW10wZNdj u7iPXpJ89xuCLW4cI3+VCYknRlFgUkMk9rKVgu1NrYpfnxw8NGz/Yf+p5LepKb3gDryDbS1UAQJA cHIjxjgUrJUtRqA5fHmcu/jgt65vMjXhdG8DLs3BV9l6VYus9G+jmsRYutaF5CSYoOndMNfWL2FQ 5oNTJ6TWxw==";
	
	@BeforeClass
	public static void init() throws Exception {
		SchemaUtils.restoreAllTables();
		
		if(SpringUtils.beanFactory==null) {
			oscar.OscarProperties p = oscar.OscarProperties.getInstance();
			p.setProperty("db_name", ConfigUtils.getProperty("db_schema") + ConfigUtils.getProperty("db_schema_properties"));
			p.setProperty("db_username", ConfigUtils.getProperty("db_user"));
			p.setProperty("db_password", ConfigUtils.getProperty("db_password"));
			p.setProperty("db_uri", ConfigUtils.getProperty("db_url_prefix"));
			p.setProperty("db_driver", ConfigUtils.getProperty("db_driver"));
			ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext();
			context.setConfigLocations(new String[]{"/applicationContext.xml","/applicationContextBORN.xml"});
			context.refresh();
			SpringUtils.beanFactory = context;
		}
		
		AuthUtils.initLoginContext();
		
	}
	@Test
	@Ignore
	/*
	 * Just used during my own testing..but thought it be useful to leave for someone else
	 */
	public void playAround() throws Exception {
		
		String url = "http://localhost:8080/oscar_alberta/lab/newLabUpload.do";
		String publicOscarKeyString = OSCAR_KEY;
		String publicServiceKeyString = CLIENT_KEY;

		PublicKey publicOscarKey = SendingUtils.getPublicOscarKey(publicOscarKeyString); 
		PrivateKey publicServiceKey = SendingUtils.getPublicServiceKey(publicServiceKeyString);
		
		InputStream is = this.getClass().getResourceAsStream("/labs/HL7-CLS/MillenniumUpgrade2010_Clinic_Validation_Current.hl7");
		byte[] bytes = IOUtils.toByteArray(is);
		
		Provider provider = new Provider();
		provider.setProviderNo("1");
		
		LoggedInInfo l = new LoggedInInfo();
		l.setLoggedInProvider(provider);
		int statusCode = SendingUtils.send(l, bytes, url, publicOscarKey, publicServiceKey, "CLS");
		logger.info("Completed Labs upload call with status " + statusCode);
		assertEquals(200, statusCode);
		
		

	}
}
