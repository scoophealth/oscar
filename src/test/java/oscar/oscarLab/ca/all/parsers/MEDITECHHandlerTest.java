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

package oscar.oscarLab.ca.all.parsers;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.TreeSet;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.junit.AfterClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import ca.uhn.hl7v2.HL7Exception;
import junit.framework.Assert;
import oscar.oscarLab.ca.all.parsers.MEDITECHHandler.OBX_DATA_TYPES;
import oscar.oscarLab.ca.all.parsers.MEDITECHHandler.UNSTRUCTURED;

@RunWith(Parameterized.class)
public class MEDITECHHandlerTest {
	
	private static Logger logger = Logger.getLogger(MEDITECHHandlerTest.class);
	private static MEDITECHHandler handler;
	private static ZipFile zipFile;
	private static Document hl7XML;
		
	@Parameterized.Parameters
	public static Collection<String[]> hl7BodyArray() {
		
		logger.info( "Creating MEDITECHHandlerTest test parameters" );
	
		URL url = Thread.currentThread().getContextClassLoader().getResource("MEDITECH_test_data.zip");
		
		try {
			zipFile = new ZipFile(url.getPath());
        } catch (IOException e) {
        	 logger.error("Test Failed ", e);
        }
		
		Enumeration<? extends ZipEntry> enumeration = zipFile.entries();		
		StringWriter writer = null;
		InputStream is = null;
		List<String[]> hl7BodyArray = new ArrayList<String[]>();
		String hl7Body = "";

		while( enumeration.hasMoreElements() ) {
			
			ZipEntry zipEntry = enumeration.nextElement();
						
			if(zipEntry.getName().endsWith(".txt")) {
				
				logger.debug( zipEntry.getName() );
				
				writer = new StringWriter();
				
				try {					
					is = zipFile.getInputStream( zipEntry );					
					IOUtils.copy(is, writer, "UTF-8");														 
	            } catch (IOException e) {
	            	if( zipFile != null ) {
	            		try {
	                        zipFile.close();
	                        zipFile = null;
                        } catch (IOException e1) {
                        	 logger.error("Test Failed ", e);
                        }	            		
	            	}
	            	logger.error("Test Failed ", e);
	            }finally {
	            	if( is != null ) {
	            		try {
	    	                is.close();
	    	                is = null;
	                    } catch (IOException e) {
	                    	 logger.error("Test Failed ", e);
	                    }
	            	}	            	
	            }
				
				hl7Body = writer.toString();
				hl7BodyArray.add(new String[]{hl7Body});
			}
		}	

		return hl7BodyArray;
	}
	
	@AfterClass
	public static void close() {
		if( zipFile != null) {
			try {
	            zipFile.close();
	            zipFile = null;
            } catch (IOException e) {
	            logger.error("Test Failed ", e);
            }			
		}
	}

	public MEDITECHHandlerTest(String hl7Body) {
		handler = null;
		hl7XML = null;
		handler = new MEDITECHHandler();
		try {
	        handler.init( hl7Body );
	        hl7XML = buildDocumentObject( handler.getXML() );
        } catch (HL7Exception e) {
	        logger.error("Test Failed ", e);
        }
	}
	
	public Document buildDocumentObject( String xml ) {
		Document document = null;
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

	    try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			
			document = builder.parse( new ByteArrayInputStream( xml.getBytes() ) );
			document.getDocumentElement().normalize();

		} catch (Exception e) {
			logger.error("",e);
		} 
	    
	    return document;
	}
	
	private String getElement( String elementName ) {
		return getElement( elementName, null, null );
	}
	
	private String getElement( String elementName, String elementChild ) {
		return getElement( elementName, elementChild, null );
	}
	
	private String getElement( String elementName, String elementChild, String elementChild2 ) {
		
		NodeList nodeList = hl7XML.getElementsByTagName( elementName );
		StringBuilder stringBuilder = new StringBuilder("");

		if( nodeList != null ) {
			for( int i = 0; i < nodeList.getLength(); i++ ) {

				stringBuilder.append( getElement( nodeList.item(i), elementChild ) );
				if( elementChild2 != null ) {
					stringBuilder.append( " " + getElement( nodeList.item(i), elementChild2 ) );
				}
				
				stringBuilder.append( " " );
			}
		}

		return stringBuilder.toString().trim();
	}
	
	private String getElement( Node nNode, String elementChild ) {
		
		String string = "";
		Element element = null;
		
		if( elementChild == null ) {
			string = nNode.getTextContent().trim();
			nNode = null;
		}

		if( nNode != null && nNode.getNodeType() == Node.ELEMENT_NODE) {
			element = (Element) nNode;
			nNode = element.getElementsByTagName( elementChild ).item(0);
		}

		if( nNode != null ) {
			string = nNode.getTextContent().trim();
		}
		
		return string;
	}
	
	/**
	 * Converts a space delimited string into an ArrayList 
	 * Also eliminates duplicates and sorts alpha numerically.
	 * @return
	 */
	private static ArrayList<String> sortStringToList( String stringList ) {
		
		TreeSet<String> idSet = new TreeSet<String>( Arrays.asList( stringList.trim().split(" ") ) );		
		ArrayList<String> idList = new ArrayList<String>(  idSet );		
		Collections.sort(idList);

		return idList;
	}

	@Test
	public void testGetSpecimenSource() {
		logger.info("testGetSpecimenSource() " + handler.getSpecimenSource(0) );
		String result = "";
		for( int i = 0; i < handler.getOBRCount(); i++ ) {
			result += " " + handler.getSpecimenSource(i);
		}
		result = result.trim();
		Assert.assertEquals( getElement("OBR.15", "CM_SPS.2" ), result );
	}
	
	@Test
	public void testGetSpecimenDescription() {
		logger.info("testGetSpecimenDescription() " + handler.getSpecimenDescription(0) );
		String result = "";
		for( int i = 0; i < handler.getOBRCount(); i++ ) {
			result += " " + handler.getSpecimenDescription(i);
		}
		result = result.trim();
		Assert.assertEquals( getElement("OBR.15", "CM_SPS.3"), result );
	}
	
	@Test
	public void testGetDiscipline() {
		logger.info("testGetDiscipline() " + handler.getDiscipline() );
		
		String discipline =  handler.getDiscipline();
		String expected = getElement("OBR.4", "CE.2");
		
		if( discipline.contains("/") ) {
			discipline = discipline.replaceAll("/", " ");
		}

		if( expected.isEmpty() ) {
			expected = getElement("OBR.4", "CE.1");
		}
		
		if( expected.isEmpty() ) {
			expected = getElement("OBR.21");
		}
		
		if( expected.contains("/") ) {
			expected = expected.replaceAll("/", " ");
		}

		Assert.assertEquals( sortStringToList( expected ), sortStringToList( discipline ) );
	}
	
	@Test
	public void testIsReportData() {
		logger.info("testIsReportData() " + handler.isReportData() );
		Assert.assertEquals( ( OBX_DATA_TYPES.TX.name().equals( getElement("OBX.2") ) && ! handler.isUnstructured() ), handler.isReportData() );
	}
	
	@Test
	public void testIsUnstructured() {
		logger.info("testIsUnstructured() " + handler.isUnstructured() );
		
		boolean expected = Boolean.FALSE;
		for(UNSTRUCTURED lab : UNSTRUCTURED.values()) {
			if( lab.name().equalsIgnoreCase( getElement("MSH.3") ) ) {
				expected = Boolean.TRUE;
			}
		}
		
		Assert.assertEquals( expected, handler.isUnstructured() );
	}
	
	@Test
	public void testGetSendingApplication() {
		logger.info("testGetSendingApplication() " + handler.getSendingApplication());
		Assert.assertEquals( getElement("MSH.3"), handler.getSendingApplication() );
	}

	@Test
	public void testGetMsgType() {
		logger.info("testGetMsgType() " + handler.getMsgType());
		Assert.assertEquals( "MEDITECH", handler.getMsgType() );
	}

	@Test
	public void testGetMsgDate() {
		logger.info("testGetMsgDate() " + handler.getMsgDate());
		Assert.assertEquals( MEDITECHHandler.formatDateTime( getElement("MSH.7") ), handler.getMsgDate() );
	}

	@Test
	public void testGetMsgPriority() {
		logger.info("testGetMsgPriority() " + handler.getMsgPriority());
		Assert.assertEquals( "", handler.getMsgPriority() );
	}

	@Test
	public void testGetOBRCount() {
		logger.info("testGetOBRCount() " + handler.getOBRCount());
		Assert.assertEquals( hl7XML.getElementsByTagName( "OBR" ).getLength(), handler.getOBRCount() );
	}

	@Test
	public void testGetOBXCount() {

		int obrCount = handler.getOBRCount();
		int count = 0; 
		for( int i = 0; i < obrCount; i++ ) {
			count += handler.getOBXCount(i);
		}

		logger.info("testGetOBXCount() " + count );

		Assert.assertEquals( hl7XML.getElementsByTagName( "OBX" ).getLength(), count );
	}

	@Test
	public void testGetTimeStamp() {
		logger.info( "testGetTimeStamp() " + handler.getTimeStamp(0, 0) );
		Assert.assertEquals( MEDITECHHandler.formatDateTime( sortStringToList( getElement("OBR.7") ).get(0) ), handler.getTimeStamp(0, 0) );
	}

	/**
	 * OBR count is *almost* always 1 in these tests. Therefore the OBR row index will
	 * always be zero.
	 */
	@Test
	public void testIsOBXAbnormal() {

		boolean result = Boolean.FALSE;
		int obxCount = 0;		
		for( int i = 0; i < handler.getOBRCount(); i++ ) {
			obxCount = handler.getOBXCount(i);
			for( int j = 0; j < obxCount; j++ ) {
				 if( handler.isOBXAbnormal(i, j) ) {
					 result = Boolean.TRUE;
					 break;
				 }
			}
		}
		
		logger.info("testIsOBXAbnormal() " + result );

		ArrayList<String> labresultList = sortStringToList( getElement("OBX.8") );
		String resultExpected = MEDITECHHandler.NORMAL_LAB;
		if( ! labresultList.get(0).isEmpty() ) {
			for( String resultItem : labresultList ) {
				if( ! MEDITECHHandler.NORMAL_LAB.equalsIgnoreCase( resultItem ) ) {
					resultExpected = "A";
					break;
				}
			}
		}

		Assert.assertEquals( "A".equals( resultExpected ), result);

	}

	@Test
	public void testGetOBXAbnormalFlag() {

		StringBuilder stringBuilder = new StringBuilder("");
		int obrCount = handler.getOBRCount();
		int obxCount = 0;
		for( int j = 0; j < obrCount; j++ ) {
			obxCount = handler.getOBXCount( j );
			for( int i = 0; i < obxCount; i++ ) {
				stringBuilder.append( " " + handler.getOBXAbnormalFlag(j, i) );
			}
		}
		logger.info( "testGetOBXAbnormalFlag() " + stringBuilder.toString() );
		
		Assert.assertEquals( getElement("OBX.8"), stringBuilder.toString().trim() );
	}

	@Test
	public void testGetObservationHeader() {
	
		StringBuilder stringBuilder = new StringBuilder("");

		int obxCount = 0;
		for(int i = 0; i < handler.getOBRCount(); i++) {
			obxCount = handler.getOBXCount(i);
			
			for(int j = 0; j < obxCount; j++) {
				String header = handler.getObservationHeader(j, 0);
				if( ! header.isEmpty() ) {
					stringBuilder.append( " " + header ); 
				}				
			}
		}
		
		logger.info( "testGetObservationHeader() " + stringBuilder.toString() );

		String header = getElement("OBR.4", "CE.2");
		if( header.isEmpty() ) {
			header = getElement("OBR.4", "CE.1");
		}

		Assert.assertEquals( sortStringToList( header ), sortStringToList( stringBuilder.toString() ) );
	}

	@Test
	public void testGetOBXIdentifier() {
		
		StringBuilder stringBuilder = new StringBuilder("");
		int obxCount = 0;
	
		for(int i = 0; i < handler.getOBRCount(); i++) {
			obxCount = handler.getOBXCount(i);
		
			for(int j = 0; j < obxCount; j++) {
				String id = handler.getOBXIdentifier( i, j );
				if( ! id.isEmpty() ) {
					stringBuilder.append( " " + handler.getOBXIdentifier( i, j ) );
				}
			}
		}

		logger.info("testGetOBXIdentifier() " + stringBuilder.toString());

		Assert.assertEquals( sortStringToList( getElement("OBX.3", "CE.1").trim() ), sortStringToList( stringBuilder.toString() ) );
	}

	@Test
	public void testGetOBXValueType() {
		
		StringBuilder stringBuilder = new StringBuilder("");
		int obxCount = 0;

		for(int i = 0; i < handler.getOBRCount(); i++) {
			obxCount = handler.getOBXCount(i);
		
			for(int j = 0; j < obxCount; j++) {
				stringBuilder.append( " " + handler.getOBXValueType( i, j ) );  				
			}
		}

		logger.info( "testGetOBXValueType() " + stringBuilder.toString());
		Assert.assertEquals( getElement("OBX.2"), stringBuilder.toString().trim() );
	}

	@Test
	public void testGetOBXName() {

		StringBuilder stringBuilder = new StringBuilder("");		
		int obxCount = 0;

		for(int i = 0; i < handler.getOBRCount(); i++) {
			obxCount = handler.getOBXCount(i);
		
			for(int j = 0; j < obxCount; j++) {
				stringBuilder.append( " " +handler.getOBXName( i, j ) );  				
			}
		}

		logger.info( "testGetOBXName() " + stringBuilder.toString());
		Assert.assertEquals( getElement("OBX.3", "CE.2"), stringBuilder.toString().trim() );
	}

	@Test
	public void testGetOBXResult() {

		StringBuilder stringBuilder = new StringBuilder();

		int obxCount = 0;
		
		for(int i = 0; i < handler.getOBRCount(); i++) {
			obxCount = handler.getOBXCount(i);		
			for(int j = 0; j < obxCount; j++) {
				stringBuilder.append( " " + handler.getOBXResult( i, j ) ); 				
			}
		}
		logger.info( "testGetOBXResult() " + stringBuilder.toString()); 
		
		Assert.assertEquals( sortStringToList( getElement("OBX.5") ), sortStringToList( stringBuilder.toString().trim() ) );
	}

	@Test
	public void testGetOBXReferenceRange() {
		
		StringBuilder stringBuilder = new StringBuilder("");
		int obxCount = 0;
		for(int i = 0; i < handler.getOBRCount(); i++) {
			obxCount = handler.getOBXCount(i);		
			for(int j = 0; j < obxCount; j++) {
				stringBuilder.append( " " + handler.getOBXReferenceRange( i, j ) );
			}
		}

		logger.info( "testGetOBXReferenceRange() " + stringBuilder.toString());
		
		Assert.assertEquals( sortStringToList( getElement("OBX.7") ), sortStringToList( stringBuilder.toString().trim() ) );
	}

	@Test
	public void testGetOBXUnits() {
		
		StringBuilder stringBuilder = new StringBuilder("");
		int obxCount = 0;
		for(int i = 0; i < handler.getOBRCount(); i++) {
			obxCount = handler.getOBXCount(i);		
			for(int j = 0; j < obxCount; j++) {
				String unit = handler.getOBXUnits( i, j );
				if( unit != null && ! unit.isEmpty() ) {
					stringBuilder.append( " " + unit ); 
				}
			}
		}

		logger.info( "testGetOBXUnits() " + stringBuilder.toString() );
		
		Assert.assertEquals( sortStringToList( getElement("OBX.6", "CE.1") ), sortStringToList( stringBuilder.toString().trim() ) );
	}

	@Test
	public void testGetOBXResultStatus() {
		
		StringBuilder stringBuilder = new StringBuilder("");
		
		int obxCount = 0;
		for(int i = 0; i < handler.getOBRCount() ; i++) {
			obxCount = handler.getOBXCount(i);		
			for(int j = 0; j < obxCount; j++) {
				stringBuilder.append( " " + handler.getOBXResultStatus( i, j ) ); 
			}
		}

		logger.info( "testGetOBXResultStatus() " + stringBuilder.toString());
		Assert.assertEquals( getElement( "OBX.11" ), stringBuilder.toString().trim() );
	}

	@Test
	public void testGetCommentCount() {
		
		int count = 0;
		int obxCount = 0;
		int obrCount = handler.getOBRCount();
		for(int i = 0; i < obrCount; i++) {
			obxCount = handler.getOBXCount(i);
			for(int j = 0; j < obxCount; j++) {
				count += handler.getOBXCommentCount( i, j ); 
			}
		}
		
		for(int i = 0; i < obrCount; i++) {
			 count += handler.getOBRCommentCount( i );
		}
		
		logger.info("testGetOBXCommentCount() " + count );
		
		
		Assert.assertEquals( hl7XML.getElementsByTagName( "NTE" ).getLength(), count );
	}

	@Test
	public void testGetComments() {

		StringBuilder stringBuilder = new StringBuilder("");
		
		int obxCount = 0;
		int commentCount = 0;
		
		for(int i = 0; i < handler.getOBRCount() ; i++) {
			commentCount = handler.getOBRCommentCount( i );		
			for(int j = 0; j < commentCount; j++) {
				stringBuilder.append( " " + handler.getOBRComment( i, j ) ); 
			}
		}

		for(int i = 0; i < handler.getOBRCount(); i++) {
			obxCount = handler.getOBXCount(i);
			for(int j = 0; j < obxCount; j++) {
				commentCount = handler.getOBXCommentCount(i, j);
				for(int k = 0; k < commentCount; k++) {
					stringBuilder.append( " " + handler.getOBXComment( i, j, k ) );
				}
			}			
		}
		
		logger.info( "testGetComments() " + stringBuilder.toString());
		
		Assert.assertEquals( getElement("NTE", "NTE.3"), stringBuilder.toString().trim() );
	}

	@Test
	public void testGetMiddleName() {
		logger.info("testGetMiddleName() " + handler.getMiddleName() );

		String middle = getElement( "PID.5", "XPN.3" );
		if( middle == null ) {
			middle = "";
		}
		Assert.assertEquals(  middle, handler.getMiddleName() );
	}

	@Test
	public void testGetFirstName() {
		logger.info("testGetFirstName() " + handler.getFirstName());
		Assert.assertEquals( getElement( "PID.5", "XPN.2" ), handler.getFirstName() );
	}

	@Test
	public void testGetLastName() {
		logger.info("testGetLastName() " + handler.getLastName());
		Assert.assertEquals( getElement( "PID.5", "XPN.1" ), handler.getLastName() );
	}

	@Test
	public void testGetDOB() {
		logger.info("testGetDOB() " + handler.getDOB());
		Assert.assertEquals( MEDITECHHandler.formatDateTime( sortStringToList( getElement( "PID.7", "TS.1" ) ).get(0) ), handler.getDOB() );
	}

	@Test
	public void testGetAge() {
		logger.info("testGetAge() " + handler.getAge());

		String dob = MEDITECHHandler.formatDateTime( sortStringToList( getElement( "PID.7", "TS.1" ) ).get(0) );
		String service = MEDITECHHandler.formatDateTime( sortStringToList( getElement( "OBR.7", "TS.1" ) ).get(0) );
		
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Calendar a = Calendar.getInstance();
		Calendar b = Calendar.getInstance();
		
		try {
			java.util.Date birthDate = formatter.parse(dob);
					a.setTime(birthDate);
			java.util.Date serviceDate = formatter.parse(service);			
					b.setTime(serviceDate);
		} catch (ParseException e) {
			logger.error("error",e);
		}

		int age = b.get(Calendar.YEAR) - a.get(Calendar.YEAR);
	    if (a.get(Calendar.MONTH) > b.get(Calendar.MONTH) || 
            (a.get(Calendar.MONTH) == b.get(Calendar.MONTH) && a.get(Calendar.DATE) > b.get(Calendar.DATE))) {
            age--;
        }
	    
	    Assert.assertEquals( age + "", handler.getAge() );
	}

	@Test
	public void testGetSex() {
		logger.info("testGetSex() " + handler.getSex());
		Assert.assertEquals( getElement( "PID.8"), handler.getSex() );
	}

	@Test
	public void testGetHealthNum() {
		logger.info("testGetHealthNum() " + handler.getHealthNum());
		String healthnumber = getElement( "PID.2", "CX.1" );
		if( healthnumber.isEmpty() ) {
			 healthnumber = null;
		}
		Assert.assertEquals(  healthnumber, handler.getHealthNum() );
	}

	@Test
	public void testGetFirstNationsBandNumber() {
		logger.info( "testGetFirstNationsBandNumber() " + handler.getFirstNationsBandNumber() );
		Assert.assertEquals( getElement( "PID.2", "CX.5" ), handler.getFirstNationsBandNumber() );
	}
	
	@Test
	public void testGetHomePhone() {
		logger.info("testGetHomePhone() " + handler.getHomePhone());

		String homephone = getElement( "PID.13", "XTN.1" );
		if( homephone == null ) {
			homephone = "";
		}
		Assert.assertEquals( homephone, handler.getHomePhone() );
	}

	@Test
	public void testGetWorkPhone() {
		logger.info("testGetWorkPhone() " + handler.getWorkPhone());
		
		String workphone = getElement( "PID.14", "XTN.1" );
		if( workphone == null ) {
			workphone = "";
		}
		Assert.assertEquals( workphone, handler.getWorkPhone() );
	}

	@Test
	public void testGetPatientLocation() {
		logger.info("testGetPatientLocation() " + handler.getPatientLocation());		
		Assert.assertEquals( getElement( "PV1.3", "PL.1" ) + " " + getElement( "PV1.3", "PL.4" ), handler.getPatientLocation() );
	}

	@Test
	public void testGetServiceDate() {
		logger.info("testGetServiceDate() " + handler.getServiceDate());
		Assert.assertEquals( MEDITECHHandler.formatDateTime( sortStringToList( getElement( "OBR.7", "TS.1" ) ).get(0) ), handler.getServiceDate() );
	}

	@Test
	public void testGetRequestDate() {
		logger.info("testGetRequestDate() " + handler.getRequestDate(0));
		Assert.assertEquals( MEDITECHHandler.formatDateTime( sortStringToList( getElement("OBR.6", "TS.1") ).get(0) ), handler.getRequestDate(0) );
	}

	@Test
	public void testGetOrderStatus() {
		logger.info("testGetOrderStatus() " + handler.getOrderStatus());
		Assert.assertEquals( sortStringToList( getElement("OBR.25") ), sortStringToList( handler.getOrderStatus() ) );
	}

	@Test
	public void testGetOBXFinalResultCount() {
		logger.info("testGetOBXFinalResultCount() " + handler.getOBXFinalResultCount());

		String status = sortStringToList( getElement("OBR.25") ).get(0);
		int count = hl7XML.getElementsByTagName( "OBX.11" ).getLength();
		
		if( "C".equalsIgnoreCase( status ) ) {
			count = count + 150;
		} else if ( "F".equals( status ) ) {
			count = count + 100;
		}
		
		Assert.assertEquals( count, handler.getOBXFinalResultCount() );
	}

	@Test
	public void testGetClientRef() {
		logger.info("testGetClientRef() " + handler.getClientRef());
		Assert.assertEquals( sortStringToList( getElement("OBR.16", "XCN.1") ), sortStringToList( handler.getClientRef() ) );
	}

	@Test
	public void testGetAccessionNum() {
		logger.info("testGetAccessionNum() " + handler.getAccessionNum());
		Assert.assertEquals( sortStringToList( getElement("OBR.3", "EI.1") ), sortStringToList( handler.getAccessionNum() ) );
	}
	
	@Test
	public void testGetOtherHealthcareProviders() {
		logger.info("testGetOtherHealthcareProviders() " + handler.getOtherHealthcareProviders() );
		String otherproviders =  handler.getOtherHealthcareProviders();
		
		String[] otherProvidersArray = otherproviders.split(",");
		ArrayList<String> otherProvidersList = new ArrayList<String>();
		for( String otherProvider : otherProvidersArray ) {		
			otherProvidersList.addAll( Arrays.asList( otherProvider.trim().split(" ") ) );
		}

		ArrayList<String> otherProvidersListXML = new ArrayList<String>( 
					Arrays.asList( getElement("PV1.52", "XCN.3", "XCN.2").split(" ") )
				);

		Collections.sort(otherProvidersListXML);
		Collections.sort(otherProvidersList);

		Assert.assertEquals( otherProvidersListXML, otherProvidersList );
	}
	
	@Test
	public void testGetAttendingPhysician() {
		logger.info("testGetAttendingPhysician() " + handler.getAttendingPhysician() );
		Assert.assertEquals( getElement("PV1.7", "XCN.3", "XCN.2"), handler.getAttendingPhysician() );
	}
	
	@Test
	public void testGetAdmittingPhysician() {
		logger.info("testGetAdmittingPhysician() " + handler.getAdmittingPhysician());		
		Assert.assertEquals( getElement("PV1.17", "XCN.3", "XCN.2"), handler.getAdmittingPhysician() );
	}

	@Test
	public void testGetDocName() {
		logger.info("testGetDocName() " + handler.getDocName());
		
		String docname = getElement("OBR.16", "XCN.3", "XCN.2");
		
		if( docname.isEmpty() ) {
			docname = getElement("PV1.7", "XCN.3", "XCN.2");
		}
		
		Assert.assertEquals( sortStringToList( docname ), sortStringToList( handler.getDocName() ) );
	}

	@Test
	public void testGetCCDocs() {
		logger.info("testGetCCDocs() " + handler.getCCDocs());
		String otherproviders =  handler.getCCDocs();
		if( otherproviders.contains(",") ) {
			otherproviders = otherproviders.replaceAll(",", "");
		}
		Assert.assertEquals( sortStringToList( getElement("OBR.28", "XCN.3", "XCN.2") ), sortStringToList( otherproviders ) );
	}
	
	/**
	 * Also indirectly tests the getProviderMap method.
	 * Is a very bad sign if this method fails. 
	 */
	@Test
	public void testGetDocNums() {
		logger.info("testGetDocNums() " + handler.getDocNums());
		
		HashSet<String> providerNumberSet = new HashSet<String>();

		String[] admitting = getElement( "PV1.17", "XCN.1" ).split(" ");
		String[] attending = getElement( "PV1.7", "XCN.1" ).split(" ");
		String[] other = getElement( "PV1.52", "XCN.1" ).split(" ");
		String[] ordering = getElement( "OBR.16", "XCN.1" ).split(" ");
		String[] copies = getElement( "OBR.28", "XCN.1" ).split(" ");
		
		if( ! admitting[0].isEmpty() ) {
			providerNumberSet.addAll( Arrays.asList(admitting) );
		}
		
		if( ! attending[0].isEmpty() ) {
			providerNumberSet.addAll( Arrays.asList(attending) );
		}
		
		if( ! other[0].isEmpty() ) {
			providerNumberSet.addAll( Arrays.asList(other) );
		}
		
		if( ! ordering[0].isEmpty() ) {
			providerNumberSet.addAll( Arrays.asList(ordering) );
		}
		
		if( ! copies[0].isEmpty() ) {
			providerNumberSet.addAll( Arrays.asList(copies) );
		}
		
		ArrayList<String> providerNumberList = new ArrayList<String>( providerNumberSet );

		Assert.assertEquals( providerNumberList, handler.getDocNums() );
	}

	@Test
	public void testAudit() {
		logger.info("testAudit() " + handler.audit());
		Assert.assertEquals("success", handler.audit());
	}

	@Test
	public void testGetFillerOrderNumber() {
		logger.info("testGetFillerOrderNumber() " + handler.getFillerOrderNumber());
		Assert.assertEquals( sortStringToList( getElement("OBR.3", "EI.1") ), sortStringToList( handler.getFillerOrderNumber() ) );
	}


}
