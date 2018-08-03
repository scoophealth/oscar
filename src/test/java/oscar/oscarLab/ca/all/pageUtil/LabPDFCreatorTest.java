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

package oscar.oscarLab.ca.all.pageUtil;

import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;

import java.io.FileOutputStream;

// import static org.junit.Assert.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.commons.io.IOUtils;
import org.junit.BeforeClass;
import org.junit.Test;
import org.oscarehr.caisi_integrator.util.MiscUtils;
import com.lowagie.text.DocumentException;

import ca.uhn.hl7v2.HL7Exception;
import oscar.oscarLab.ca.all.parsers.IHAPOIHandler;
import oscar.oscarLab.ca.all.parsers.MEDITECHHandler;
import oscar.oscarLab.ca.all.parsers.MessageHandler;
import oscar.oscarLab.ca.all.parsers.PATHL7Handler;

/**
 * 
 * This unit test is used for verifying the PDF output of various lab systems
 * 
 * To set up:
 * 
 * 1.) uncomment the @Test annotations
 * 2.) set the outputFilePath attribute to a output directory and file name of your choice.
 * 3.) run unit test.
 * 4.) uncomment the Test annotations before making a commit.
 *
 */
public class LabPDFCreatorTest {

	private static String outputFilePath;
	private static ZipFile zipFile;

	
	@BeforeClass
	public static void setUpBeforeClass(){		
		outputFilePath = Thread.currentThread().getContextClassLoader().getResource("").getFile();
	}

	/**
	 * HL7 format used by most of the rural Ontario health authorities 
	 */
	 @Test
	public void testPrintMeditech() {				
		Enumeration<?> zipFile = openZipFile( "MEDITECH_test_data.zip" );

		while( zipFile.hasMoreElements() ) {
			ZipEntry zipEntry =  (ZipEntry) zipFile.nextElement();
			MEDITECHHandler handler = new MEDITECHHandler();
			Path path = Paths.get( createPDF( zipEntry, handler ) );
			assertTrue(Files.exists(path));
		}
	}
	
	/**
	 * HL7 format used by most of the rural BC Health Authorities. Specifically the
	 * Interior Health Authority.
	 */
	@Test
	public void testPrintIHAPOI() {
		Enumeration<?> zipFile = openZipFile( "IHAPOI_test_data.zip" );

		while( zipFile.hasMoreElements() ) {
			ZipEntry zipEntry =  (ZipEntry) zipFile.nextElement();
			IHAPOIHandler handler = new IHAPOIHandler();
			Path path = Paths.get( createPDF( zipEntry, handler ) );
			assertTrue(Files.exists(path));
		}
	}
	
	/**
	 * Format used by Excelleris in both BC and Ontario
	 */
	@Test
	public void testPrintPathHl7() {

		Enumeration<?> zipFile = openZipFile( "excelleris_test_lab_data.zip" );

		while( zipFile.hasMoreElements() ) {
			ZipEntry zipEntry =  (ZipEntry) zipFile.nextElement();
			PATHL7Handler handler = new PATHL7Handler();
			Path path = Paths.get( createPDF( zipEntry, handler ) );
			assertTrue(Files.exists(path));
		}
	}
	
	private static String createPDF( ZipEntry zipEntry, MessageHandler handler ) {

		String hl7Body = getHL7Body( zipEntry );
		LabPDFCreator lpdfc = null;
		FileOutputStream output = null; 
		String filePath = "";
		
		if( ! hl7Body.isEmpty() ) {
			
			lpdfc = new LabPDFCreator();
			lpdfc.setOs( new ByteArrayOutputStream() );
			String filename = zipEntry.getName();
			
			try {
				MiscUtils.getLogger().info("Trying lab file " + filename);
		        handler.init( hl7Body );
				lpdfc.setHandler( handler );
				lpdfc.printPdf();
			
				if( filename.contains("/") ) {
					filename = filename.replaceAll( "/", "_" );
				}
				filePath = outputFilePath + filename + ".pdf";
				output = new FileOutputStream( filePath );
				output.write( ( (ByteArrayOutputStream) lpdfc.getOs() ).toByteArray());
	
				MiscUtils.getLogger().info("PDF file created at " + filePath);
	        } catch (HL7Exception e) {
		       e.printStackTrace();
	        } catch (IOException e) {
				e.printStackTrace();
			} catch (DocumentException e) {
				e.printStackTrace();
			} finally {
				try {
					if( output != null ) {
						output.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					try {
						lpdfc.closeOs();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		
		return filePath;
	}
	
	private static String getHL7Body( ZipEntry zipEntry ) {
		
		StringWriter writer = new StringWriter();
		InputStream is = null;
		
		try {
			is = zipFile.getInputStream( zipEntry );
			IOUtils.copy( is, writer, "UTF-8" );
		} catch (IOException e) {
			
			e.printStackTrace();
			
        	if( zipFile != null ) {
        		try {
                    zipFile.close();
                    zipFile = null;
                } catch (IOException e1) {
                	 e1.printStackTrace();
                }	            		
        	}
		}					

		
		return writer.toString();
	}
	
	private static Enumeration<? extends ZipEntry> openZipFile( String filename ) {
		
		if( zipFile != null ) {
    		try {
                zipFile.close();
                zipFile = null;
            } catch (IOException e1) {
            	 e1.printStackTrace();
            }	            		
    	}
		
		URL url = Thread.currentThread().getContextClassLoader().getResource( filename );

		try {
			zipFile = new ZipFile(url.getPath());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return zipFile.entries();			
	}

}
