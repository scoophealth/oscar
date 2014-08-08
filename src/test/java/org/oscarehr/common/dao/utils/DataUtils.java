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
package org.oscarehr.common.dao.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.log4j.Logger;
import org.oscarehr.PMmodule.dao.ProviderDao;
import org.oscarehr.common.dao.DaoTestFixtures;
import org.oscarehr.common.dao.DemographicDao;
import org.oscarehr.common.dao.ProviderInboxRoutingDao;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.common.model.Provider;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import oscar.dms.EDoc;
import oscar.dms.EDocUtil;
import oscar.oscarLab.ca.all.upload.HandlerClassFactory;
import oscar.oscarLab.ca.all.upload.handlers.MessageHandler;
import oscar.util.ConversionUtils;

public class DataUtils {

	private static Logger logger = MiscUtils.getLogger();
	
	private static final String DUMMY_LAB = "MSH|^~\\&|GDML|GDML|||20111021115053||ORU^R01|STNCHFHC.23344411|P|2.3\r" + 
			"PID|1|<HIN>^ON^ON|18-45526111||<LAST>^<FIRST>^||19990101|F|||5500 SOME RD E^HOPE^ONTARIO^ON^L5R2X0||(416)234-5678\r" + 
			"OBR|1|<ACCNUM>||418^HEMOGLOBIN^L418A^HEMATOLOGY|R|201110201435|201110201435||0000||N|||||<PROV_ID>^S. SAMSON||||||20111021115053|||||\r" + 
			"OBX|1|NM|418^HEMOGLOBIN|1|139.|g/L|115.-155.000^F: 115 - 155  g/L|N||F||||||\r";
	
	private static String initLab(Demographic demo, String labId) {
		String hin = demo.getHin();
		String last = demo.getLastName();
		String first = demo.getFirstName();
		String provider = "0000000";
		if (demo.getProvider() != null) {
			provider = demo.getProvider().getOhipNo();
		}
		
		String result = DUMMY_LAB;
		for(String[] s : new String[][] {
				{"<HIN>", hin},
				{"<LAST>", last},
				{"<FIRST>", first},
				{"<ACCNUM>", labId},
				{"<PROV_ID>", provider}
		}) {
			if (s[1] == null) {
				logger.warn("Null for " + s[0]);
				continue;
			}
			result = result.replaceAll(s[0], s[1]);
		}
		return result;
	}
	
	public static void populateProviders() {
		DemographicDao demographicDao = SpringUtils.getBean(DemographicDao.class);
		ProviderDao providerDao = SpringUtils.getBean(ProviderDao.class);
		
		List<Integer> demos = demographicDao.getActiveDemographicIds();
		List<Provider> providers = providerDao.getActiveProviders();
		
		for(int j = 0; j < demos.size(); j++) {
			Provider p = providers.get(j % providers.size());
			Demographic d = demographicDao.getDemographic("" + demos.get(j));
			d.setProvider(p);
			d.setProviderNo(p.getProviderNo());
			
			demographicDao.save(d);
			
			if (logger.isInfoEnabled()) {
				logger.info("Set provider " + p + " for " + d);
			}
		}
	}
	
	public static void populateDocs() {
		DemographicDao demographicDao = SpringUtils.getBean(DemographicDao.class);
				
		List<Integer> demos = demographicDao.getActiveDemographicIds();
		for (Integer demoId : demos) {
			for (int i = 1; i <= 10; i++) {
				String user = demoId.toString();
				Demographic demo = demographicDao.getDemographic(user);
				
				String fileName = "sample_upload_for_" + user + "_" + i + "_" + System.currentTimeMillis() + ".txt";
				String source = "";
				EDoc newDoc = new EDoc("", "", fileName, "", user, user, source, 'A',
									   oscar.util.UtilDateUtilities.getToday("yyyy-MM-dd"), "", "", "demographic", "-1", 0);
				newDoc.setDocPublic("0");
				fileName = newDoc.getFileName();
				
				String savePath = getCanonicalPath(fileName);
				byte[] bytes = ("Sample content for " + user + " " + i + " " + System.currentTimeMillis() + ".").getBytes();
				
				save(savePath, bytes);
				
				newDoc.setContentType("text/plain");
				newDoc.setNumberOfPages(1);
				String doc_no = EDocUtil.addDocumentSQL(newDoc);
				
				ProviderInboxRoutingDao providerInboxRoutingDao = SpringUtils.getBean(ProviderInboxRoutingDao.class);
				providerInboxRoutingDao.addToProviderInbox(demo.getProviderNo(), ConversionUtils.fromIntString(doc_no), "DOC");
				
				if (logger.isInfoEnabled()) {
					logger.info("Added doc: " + fileName);
				}
			}
		}
	}

	private static void save(String savePath, byte[] bytes) {
	    try {
	    	FileOutputStream fos = new FileOutputStream(savePath);
	    	fos.write(bytes);
	    	fos.flush();
	    	fos.close();
	    } catch (Exception e ) {
	    	logger.error("Unable to generate file", e);
	    	throw new RuntimeException();
	    }
    }

	private static String getCanonicalPath(String fileName) {
		String docDir = oscar.OscarProperties.getInstance().getProperty("DOCUMENT_DIR");
		if (docDir == null || docDir.trim().equals("")) {
			// docDir = "/usr/local/OscarDocument/oscar_mcmaster/document";
			// docDir = "d:/Work/OSCAR/documents/";
			docDir = "/var/lib/OscarDocument";
		}
	    String savePath = docDir  + "/" + fileName;
	    return savePath;
    }
	
	public static void populateLabs() {
		String type = "CML";
		MessageHandler msgHandler = HandlerClassFactory.getHandler(type);
		
		DemographicDao demographicDao = SpringUtils.getBean(DemographicDao.class);
		
		List<Integer> demos = demographicDao.getActiveDemographicIds();
		for (Integer demoId : demos) {
			for (int i = 1; i <= 10; i++) {
				String labId = demoId + "_" + i + "_" + UUID.randomUUID().toString() + ".hl7";
				String lab = initLab(demographicDao.getDemographic(demoId.toString()), labId);
				
				String savePath = getCanonicalPath(labId);
				save(savePath, lab.getBytes());
				String status = msgHandler.parse(DaoTestFixtures.getLoggedInInfo(),"", savePath, 99, "127.0.0.1");
				if (logger.isInfoEnabled()) {
					if ("success".equals(status)) {
						logger.info("Added lab: " + labId);
					} else {
						logger.info("Error adding lab: " + labId);
					}
				}
			}
		}
		
	}
	
	public static void populateDemographicsAndProviders() {
		InputStream is = DataUtils.class.getClassLoader().getResourceAsStream("demographicsAndProviders.xml");
		populateDemographicsAndProviders(is);
	}
	
	public static void populateDemographicsAndProviders(String fileName) {
		try {
			populateDemographicsAndProviders(new FileInputStream(new File(fileName)));
        } catch (FileNotFoundException e) {
	        logger.error("Unable to populate", e);
        } 
	}
	
	public static void populateDemographicsAndProviders(InputStream is) {
		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser parser;
		try {
			parser = factory.newSAXParser();
			EntityManager entityManager = new EntityManagerImpl();
			DefaultHandler dh = new LoadingHandler(entityManager);
			parser.parse(is, dh);
		} catch (Exception e) {
			logger.error("Unable to populate", e);
		}
	}

	/**
	 * A SAX handler for parsing test data
	 *
	 */
	private static class LoadingHandler extends DefaultHandler {

		private EntityManager entityManager;

		public LoadingHandler(EntityManager entityManager) {
			this.entityManager = entityManager;
		}

		@Override
		public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
			super.startElement(uri, localName, qName, attributes);

			EntityKind entityKind = getEntityKind(qName);
			if (entityKind == null) {
				return;
			}
			
			switch (entityKind) {
			case DEMO: {
				entityManager.addDemographic(attributes);
				break;
			}
			case PROVIDER: {
				entityManager.addProvider(attributes);
				break;
			}
			default:
				break;
			}
		}

		private EntityKind getEntityKind(String elementName) {
			if (elementName.equals("demographic")) {
				return EntityKind.DEMO;
			} else if (elementName.equals("provider")) {
				return EntityKind.PROVIDER;
			}
			return null;
		}
	}

	/**
	 * Enumeration of the supported types of entities
	 *
	 */
	private enum EntityKind {
		DEMO, PROVIDER;
	}

	/**
	 * Interface to add test data entities
	 *
	 */
	private interface EntityManager {

		void addDemographic(Attributes attributes);

		void addProvider(Attributes attributes);
	}

	/**
	 * Implementing an interface to add test data entities
	 *
	 */
	private static class EntityManagerImpl implements EntityManager {

		private static DemographicDao demographicDao = SpringUtils.getBean(DemographicDao.class);
		private static ProviderDao providerDao = SpringUtils.getBean(ProviderDao.class);

		@Override
		public void addDemographic(Attributes attributes) {
			Demographic demograhic = new Demographic();

			String gender = attributes.getValue("gender");
			if (gender != null) {
				demograhic.setSex("" + gender.charAt(0));
			} 
			demograhic.setTitle(attributes.getValue("title"));
			demograhic.setFirstName(attributes.getValue("firstName"));
			demograhic.setLastName(attributes.getValue("lastName"));
			demograhic.setAddress(attributes.getValue("address"));
			demograhic.setCity(attributes.getValue("city"));
			demograhic.setEmail(attributes.getValue("email"));
			demograhic.setPhone(attributes.getValue("phone"));
			demograhic.setYearOfBirth("1999");
			demograhic.setMonthOfBirth("01");
			demograhic.setDateOfBirth("01");
			demograhic.setLastUpdateDate(new Date());
			demograhic.setActiveCount(1);
			demograhic.setPatientStatus("AC");
			
			demographicDao.save(demograhic);
			
			demograhic.setHin("" + demograhic.getDemographicNo());
			demograhic.setHcType("ON");
			
			demographicDao.save(demograhic);
			
			if (logger.isInfoEnabled()) {
				logger.info("Created new demographic: " + demograhic);
			}
		}

		@Override
		public void addProvider(Attributes attributes) {
			Provider provider = new Provider();

			provider.setProviderNo(attributes.getValue("no"));
			String gender = attributes.getValue("gender");
			if (gender != null) {
				provider.setSex("" + gender.charAt(0));
			} 
			provider.setTitle(attributes.getValue("title"));
			provider.setFirstName(attributes.getValue("firstName"));
			provider.setLastName(attributes.getValue("lastName"));
			provider.setAddress(attributes.getValue("address"));
			provider.setEmail(attributes.getValue("email"));
			provider.setPhone(attributes.getValue("phone"));
			provider.setProviderType(attributes.getValue("type"));
			provider.setSpecialty("specialty");
			provider.setLastUpdateDate(new Date());
			provider.setStatus("1");

			providerDao.saveProvider(provider);
			
			provider.setOhipNo(provider.getProviderNo());
			provider.setBillingNo(provider.getProviderNo());
			
			providerDao.updateProvider(provider);
			
			if (logger.isInfoEnabled()) {
				logger.info("Created new provider: " + provider);
			}
		}
	} 
	
}