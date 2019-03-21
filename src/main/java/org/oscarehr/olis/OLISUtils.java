package org.oscarehr.olis;
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
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.SchemaFactory;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.oscarehr.common.dao.Hl7TextInfoDao;
import org.oscarehr.common.model.Hl7TextInfo;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.OscarAuditLogger;
import org.oscarehr.util.SpringUtils;
import org.xml.sax.InputSource;

import ca.ssha._2005.hial.Response;
import oscar.OscarProperties;
import oscar.oscarLab.ca.all.parsers.Factory;
import oscar.oscarLab.ca.all.parsers.OLISHL7Handler;


public class OLISUtils {
	static Logger logger = MiscUtils.getLogger();
	
	static Hl7TextInfoDao hl7TextInfoDao = SpringUtils.getBean(Hl7TextInfoDao.class);
	
	static final public String CMLIndentifier = "2.16.840.1.113883.3.59.1:5407";// Canadian Medical Laboratories
	static final public String GammaDyancareIndentifier = "2.16.840.1.113883.3.59.1:5552";// Gamma Dynacare
	static final public String LifeLabsIndentifier = "2.16.840.1.113883.3.59.1:5687";// LifeLabs
	static final public String AlphaLabsIndetifier = "2.16.840.1.113883.3.59.1:5254";// Alpha Laboratories"

	public static String getOLISResponseContent(String response) throws Exception{
		response = response.replaceAll("<Content", "<Content xmlns=\"\" ");
		response = response.replaceAll("<Errors", "<Errors xmlns=\"\" ");
		
		DocumentBuilderFactory.newInstance().newDocumentBuilder();
		SchemaFactory factory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
		
		InputStream is = OLISPoller.class.getResourceAsStream("/org/oscarehr/olis/response.xsd");
		
		Source schemaFile = new StreamSource(is);
	
		if(OscarProperties.getInstance().getProperty("olis_response_schema") != null){
			schemaFile = new StreamSource(new File(OscarProperties.getInstance().getProperty("olis_response_schema")));
		}
		
		factory.newSchema(schemaFile);

		JAXBContext jc = JAXBContext.newInstance("ca.ssha._2005.hial");
		Unmarshaller u = jc.createUnmarshaller();
		@SuppressWarnings("unchecked")
		Response root = ((JAXBElement<Response>) u.unmarshal(new InputSource(new StringReader(response)))).getValue();
		
		return root.getContent();
	}
	

	
	
	
	public static boolean isDuplicate(LoggedInInfo loggedInInfo, String msg) {
		oscar.oscarLab.ca.all.parsers.OLISHL7Handler h = (oscar.oscarLab.ca.all.parsers.OLISHL7Handler) Factory.getHandler("OLIS_HL7", msg);
		return isDuplicate(loggedInInfo, h,msg);
	}
	
	public static boolean isDuplicate(LoggedInInfo loggedInInfo, File file) throws FileNotFoundException, IOException {
		String msg = null;
		InputStream in = null;
		try {
			in = new FileInputStream(file);
			msg = IOUtils.toString(in);
		} finally {
			IOUtils.closeQuietly(in);
		}
		return isDuplicate(loggedInInfo, msg);
	}
	
	
	public static boolean isDuplicate(LoggedInInfo loggedInInfo, OLISHL7Handler h,String msg) {
		
		String sendingFacility = h.getPlacerGroupNumber();//getPerformingFacilityNameOnly();
		logger.debug("SENDING FACILITY: " +sendingFacility);
		String accessionNumber = h.getAccessionNum();
		String hin = h.getHealthNum();
		String collectionDate = h.getCollectionDateTime(0);
		collectionDate = collectionDate.substring(0, 10).replaceAll("-", "");
	
		return isDuplicate(loggedInInfo, sendingFacility,accessionNumber,msg,hin, collectionDate);
	}
	
	
	public static boolean isDuplicate(LoggedInInfo loggedInInfo, String sendingFacility, String olisAccessionNum, String msg,String hin, String olisCollectionDate){
		logger.debug("Facility "+sendingFacility+" Accession # "+olisAccessionNum);

		//CML
		if(sendingFacility != null &&  sendingFacility.equals(CMLIndentifier)){ 
			//OLIS ACCESSION NUM LOOKS LIKE Q18OPUT-1215, CML ONE LOOKS LIKE Q18OPUT
			olisAccessionNum = olisAccessionNum.indexOf("-") != -1 ? olisAccessionNum.split("-")[0] : olisAccessionNum;
			
			for(Hl7TextInfo dupResult: hl7TextInfoDao.searchByAccessionNumber(olisAccessionNum)) {
				String cmlAccessionNum = dupResult.getAccessionNumber();
				cmlAccessionNum = cmlAccessionNum.indexOf("-1") != -1 ? cmlAccessionNum.split("-")[0]:cmlAccessionNum;
				
				if(hin.equals(dupResult.getHealthNumber())) {
					String collectionDate = dupResult.getObrDate().substring(0,10).replaceAll("-", "");
					if(!StringUtils.isEmpty(collectionDate) &&  olisCollectionDate.equals(collectionDate)) {
						OscarAuditLogger.getInstance().log(loggedInInfo, "Lab", "Skip", "Duplicate CML lab skipped - accession " + olisAccessionNum + "\n" + msg);
						return true;
					}
				}
			}
		} 
		//LIFELABS
		else if( sendingFacility != null && sendingFacility.equals(LifeLabsIndentifier)){
			//OLIS ACCESSION LOOKS LIKE 2015-Q20OUTPUT, DIRECT LOOKS LIKE 16660-Q20OUTPUT-1 (hl7TextInfo.accession would be Q20OUTPUT)
			olisAccessionNum = olisAccessionNum.substring(5);
			for(Hl7TextInfo dupResult:hl7TextInfoDao.searchByAccessionNumber(olisAccessionNum)) {
				logger.debug("LIFELABS "+dupResult.getAccessionNumber()+" "+olisAccessionNum+" == "+dupResult.getAccessionNumber().equals(olisAccessionNum.substring(5)));
				
				if(hin.equals(dupResult.getHealthNumber())) {
					String collectionDate = dupResult.getObrDate().substring(0,10).replaceAll("-", "");
					if(!StringUtils.isEmpty(collectionDate) && olisCollectionDate.equals(collectionDate)) {
						OscarAuditLogger.getInstance().log(loggedInInfo, "Lab", "Skip", "Duplicate LifeLabs lab skipped - accession " + olisAccessionNum + "\n" + msg);
						return true;
					}
				}
			}			
		}
		//GDML
		else if (sendingFacility != null && sendingFacility.equals(GammaDyancareIndentifier)){
			//OLIS ACCNUM LOOKS LIKE 201512Q19OUPUT and direct looks like 12-Q19OUPUT
			olisAccessionNum = olisAccessionNum.substring(4);
			
			List<Hl7TextInfo> dupResults = new ArrayList<Hl7TextInfo>();
			String directAcc = null;
			try {
				directAcc = olisAccessionNum.substring(0,2) + "-" + olisAccessionNum.substring(2);
				dupResults = hl7TextInfoDao.searchByAccessionNumber(directAcc);
			}catch(Exception e) {
				
			}
			
			for(Hl7TextInfo dupResult:dupResults) {
				logger.debug(dupResult.getAccessionNumber()+" == "+directAcc+" "+dupResult.getAccessionNumber().equals(directAcc));

				if(dupResult.getAccessionNumber().equals(directAcc)) {
					if(hin.equals(dupResult.getHealthNumber())) {
						String collectionDate = dupResult.getObrDate().substring(0, 10).replaceAll("-", "");
						if(!StringUtils.isEmpty(collectionDate) && olisCollectionDate.equals(collectionDate)) {
							OscarAuditLogger.getInstance().log(loggedInInfo, "Lab", "Skip", "Duplicate GAMMA lab skipped - accession " + olisAccessionNum + "\n" + msg);
							return true;
						}
					}
				}
			}		
		}
		//ALPHA
		else if (sendingFacility != null && sendingFacility.equals(AlphaLabsIndetifier)){
			List<Hl7TextInfo> dupResults = hl7TextInfoDao.searchByAccessionNumber(olisAccessionNum.substring(5));
			for(Hl7TextInfo dupResult:dupResults) {
				logger.debug("AlphaLabs "+dupResult.getAccessionNumber()+" "+olisAccessionNum+" == "+dupResult.getAccessionNumber().equals(olisAccessionNum.substring(5)));
				
				if(dupResult.getAccessionNumber().equals(olisAccessionNum.substring(5))) {
					if(hin.equals(dupResult.getHealthNumber())) {
						OscarAuditLogger.getInstance().log(loggedInInfo, "Lab", "Skip", "Duplicate AlphaLabs lab skipped - accession " + olisAccessionNum + "\n" + msg);
						return true;
					}
				}
			}		

		}
		
		
		
		return false;	
	}
	
	
	
}
