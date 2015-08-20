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
package oscar.form.pharmaForms.formBPMH.pdf;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.oscarehr.util.MiscUtils;

import com.lowagie.text.DocumentException;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.AcroFields;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;

/*
FIELD_TYPE_CHECKBOX 	2
FIELD_TYPE_COMBO 	6
FIELD_TYPE_LIST 	5
FIELD_TYPE_NONE 	0
FIELD_TYPE_PUSHBUTTON 	1
FIELD_TYPE_RADIOBUTTON 	3
FIELD_TYPE_SIGNATURE 	7
FIELD_TYPE_TEXT 	4

American Typewriter — 2.12
Baskerville — 2.51
Georgia — 2.27
Hoefler Text — 2.44
Palatino — 2.30
Times New Roman — 2.60
Arial — 2.31
Gill Sans — 2.51
Gill Sans 300 — 2.58
Helvetica Neue — 2.26
Lucida Grande — 2.07
Tahoma — 2.30
Trebuchet MS — 2.22
Verdana — 1.98
Courier New — 1.60
 */

/*
 * Author: Dennis Warren 
 * Company: Colcamex Resources
 * Date: November 2014
 * For: UBC Pharmacy Clinic and McMaster Department of Family Medicine
 */
public class PDFController {
	
	public enum SmartTagMeta {
		FIELD_TYPE, ANNOTATION, APPEARANCE
	}

	private static final Logger _Logger = MiscUtils.getLogger(); 
				
	private static String DATE_FORMAT = "MM-dd-yyyy";
	private static String TABLE_ROW_INDICATOR = "#";
	private static String STRING_FILTER = "[^a-zA-Z0-9_' '!.!" + TABLE_ROW_INDICATOR + "]";
	private static String LINE_BREAK = "\\r?\\n";
	private static String DEFAULT_FILENAME = "bpmh";
	
	private PDFControllerConfig pdfControllerConfig;
	private PdfReader pdfreader;
	private int numberOfPages;
	private int certificationLevel;
	private PdfStamper stamper;
	private String outputPath;
	// private File filePath;
	private URL filePath;
	private String fileName;
	private HashMap<String, String> pdfDataMap;
	private Map<String, Method> methodMap;
	private Object data;
	private String[] printPageNumbers;
	private ArrayList<String> smartTags;

	public PDFController(){
		// default constructor.
	}
	
	public PDFController( PDFControllerConfig config ){
		_init( config );
	}
	
//	public PDFController( File pdfPath ){
//		if( setFilePath(pdfPath) ) {
//			_init();
//		}
//
//	}
//
//	public PDFController( String pdfPath ){   	
//		if( setFilePath( new File(pdfPath) ) ) {
//			_init(); 
//		}
//	}
//
//	public PDFController( File pdfPath, PDFControllerConfig config ){
//		if( setFilePath(pdfPath) ) {
//			_init( config );
//		}
//
//	}
//
//	public PDFController( String pdfPath, PDFControllerConfig config ){   	
//		if( setFilePath( new File(pdfPath) ) ) {
//			_init( config ); 
//		}
//	}
	
	public PDFController( URL pdfPath, PDFControllerConfig config ){   	
		setFilePath( pdfPath );
		_init( config ); 
	}

	private void _init() {	
		setReader(null);	    	
		setPdfMetaData();
	}
	
	private void _init( PDFControllerConfig config ) {
		this.setPdfControllerConfig(config);
		_init();
	}

	public int getNumberOfPages() {
		return numberOfPages;
	}

	public PDFControllerConfig getPdfControllerConfig() {
		return pdfControllerConfig;
	}

	private void setPdfControllerConfig(PDFControllerConfig pdfControllerConfig) {
		this.pdfControllerConfig = pdfControllerConfig;
		PDFController.DATE_FORMAT = this.pdfControllerConfig.getDateFormat();
		PDFController.STRING_FILTER = this.pdfControllerConfig.getRegexStringFilter();		
	}

	public int getCertificationLevel() {
		return certificationLevel;
	}

	public String getOutputPath() {
		return outputPath;
	}

	public void setOutputPath(String outputPath) {       
		this.outputPath = outputPath;
	}

	public PdfReader getReader() {
		return this.pdfreader;
	}

	private void setReader(PdfReader reader) {

		try {
			if (reader == null) {
				this.pdfreader = new PdfReader( getFilePath() );				
			} else {
				this.pdfreader = reader;
			}
		} catch (IOException e) {
			_Logger.log(Level.FATAL, null, e);
		}

	}

//	private boolean setFilePath(File file){   	
//
//		if( file.exists() ) {			
//			this.filePath = file;
//			return Boolean.TRUE;
//		}
//
//		_Logger.log(Level.FATAL, "Template file location " + file.getAbsolutePath() + " not found.");
//
//		return Boolean.FALSE;
//	}

	public void setFilePath(URL pdfPath) {
		this.filePath = pdfPath;
	}

//	private String getFilePathString() {
//		if(getFilePath() == null) {
//			return "";
//		}
//		return getFilePath().getAbsolutePath();
//	}
	
	private URL getFilePath() {
		return this.filePath;
	}

//	private File getFilePath() {
//		return this.filePath;
//	}

	public String getFileName() {
		if( this.fileName == null ) {
			return DEFAULT_FILENAME;
		}
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public PdfStamper getStamper() {
		return stamper;
	}

	private void setStamper(PdfStamper stamper) {
		this.stamper = stamper;
	}

	public Map<String, Method> getMethodMap() {
		return methodMap;
	}

	private void setMethodMap(Map<String, Method> methodMap) {
		this.methodMap = methodMap;
	}

	public Object getDataObject() {
		return data;
	}

	public void setDataObject(Object data) {
		this.data = data;
		digestData( this.data );
	}

	public HashMap<String, String> getPdfDataMap() {
		return pdfDataMap;
	}

	public void setPdfDataMap(HashMap<String, String> pdfDataMap) {
		this.pdfDataMap = pdfDataMap;
	}

	public String[] getPrintPageNumbers() {
		return printPageNumbers;
	}

	public void setPrintPageNumbers( String[] printPageNumbers ) {
		this.printPageNumbers = printPageNumbers;
	}

	public void addPrintPageNumber( final String pageNumber ) {
		
		String[] currentPageNumbers = getPrintPageNumbers();
		int currentPageListLength = 0;
		int newPageListLength = 0;
		String[] newPageNumbers = null;
		Boolean pagePresent = Boolean.FALSE;
		String currentPageNumber = "";
		
		if( currentPageNumbers != null ) {
			
			for( int i = 0; i < currentPageListLength; i++ ) {
				currentPageNumber = currentPageNumbers[i];
				if( currentPageNumber.equals( pageNumber ) ) {
					pagePresent = Boolean.TRUE;
				}
			}
			
			if( ! pagePresent ) {
				
				currentPageListLength = currentPageNumbers.length;
				newPageListLength = currentPageListLength + 1;
				newPageNumbers = new String[ newPageListLength ];

				for( int j = 0; j < currentPageListLength; j++ ) {
					newPageNumbers[j] = currentPageNumbers[j];
				}
				
				newPageNumbers[newPageListLength - 1] = pageNumber; 
				
				setPrintPageNumbers( newPageNumbers );
				
			}
		}	
	}

	@SuppressWarnings("rawtypes")
	private void setPdfMetaData() {

		HashMap pdfInfoMap = getReader().getInfo();
		Iterator pdfInfoIt = pdfInfoMap.entrySet().iterator();

		numberOfPages = getReader().getNumberOfPages();
		certificationLevel = getReader().getCertificationLevel();

		while(pdfInfoIt.hasNext()) {
			_Logger.debug("PDF INFO: "+pdfInfoIt.next().toString());
		}

		_Logger.debug("PDF INFO: number pages="+getNumberOfPages());
		_Logger.debug("PDF INFO: certification level="+getCertificationLevel());

	}
	
	/**
	 * Prints the PDF with its smart tags displayed in each element
	 * Used for troubleshooting, communication or testing.
	 */
	public void printSmartTagsToPDF( String[] pages ) {
		writeDataToPDF(null, null, null, pages, "TEST", Boolean.TRUE);
	}
	
	private void addSmartTagsToPDF() {
		AcroFields acroFields = getStamper().getAcroFields();	                    
		List<String> smartTagList = getSmartTags();
		String smartTagName;
		for( int i = 0; i < smartTagList.size(); i++ ) {
			smartTagName = smartTagList.get(i);
			try {
				acroFields.setField(smartTagName, smartTagName);
			} catch (IOException e) {
				_Logger.log(Level.FATAL, "Failed to set " + smartTagName + " into PDF document", e);
			} catch (DocumentException e) {
				_Logger.log(Level.FATAL, "Failed to set " + smartTagName + " into PDF document", e);
			} 
		} 
	}
	
	/**
	 * Returns all meta data for a smart tag.
	 * @param tagName
	 * @return
	 */
	private Map<SmartTagMeta, Integer> getSmartTagMeta(String tagName) {
		
		AcroFields acroFields = getStamper().getAcroFields();
		int fieldType;
		Map<SmartTagMeta, Integer> smartTagMetaMap = new HashMap<SmartTagMeta, Integer>();
		fieldType = acroFields.getFieldType(tagName);
		_Logger.debug("Field Type: " + tagName + " = " + fieldType);

		// so far only field type.
		smartTagMetaMap.put(SmartTagMeta.FIELD_TYPE, fieldType);
		
		return smartTagMetaMap;
	}
	
	/**
	 * Returns specific meta data.
	 * @param tagName
	 * @param metaName
	 * @return
	 */
	public Integer getSmartTagMeta(String tagName, SmartTagMeta metaName) {		
		return getSmartTagMeta(tagName).get(metaName);
	}
	
	private List<String> getSmartTags() {
		return this.smartTags;
	}
	
	public void addSmartTag( final String tagName ) {
		if( this.smartTags == null ) {
			this.smartTags = new ArrayList<String>();
		}
		
		String cleanKey = tagName.replaceAll( STRING_FILTER, "" );
		if( ! cleanKey.isEmpty() ) {
			this.smartTags.add( tagName );
		}
	}

	/**
	 * Read the smart tags off of a pdf document.
	 * @return List<[smart tag name]>
	 */

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private void setSmartTags() {

		AcroFields acroFields = getStamper().getAcroFields();
		Map acroFieldsMap = acroFields.getFields();	
		Iterator<String> acroFieldsIt = acroFieldsMap.keySet().iterator();
		while( acroFieldsIt.hasNext() ) {			
			addSmartTag( acroFieldsIt.next() );			
		}
	}
	
	public void writeDataToPDF( String fileId ) {
		writeDataToPDF( null, fileId );
	}
	
	public void writeDataToPDF( String[] pages, String fileId ) {
		writeDataToPDF( null, pages, fileId );
	}
	
	public void writeDataToPDF( Object data, String fileId ) {
		writeDataToPDF( data, null, fileId );
	}

	/**
	 * Write data from an object to a PDF with matching smart tags.
	 * PDF should be preset with instantiation of this class.
	 * 
	 * This method assumes that pdf path and output path info
	 * has been set on instantiation.
	 * 
	 * See test class with same name for example.
	 * 
	 * @param Data : object or bean containing data to stamp.
	 * @param Pages : page numbers to stamp data onto
	 * @param fileId : An id, such as demographic number, to identify the file.
	 */
	public void writeDataToPDF( Object data, String[] pages, String fileId ) {

		if(getFilePath() == null) {
			_Logger.error("Set PDF file path first. [setFilePath()]");
			return;
		}

		if(getOutputPath() == null) {
			_Logger.error("Set output path first. [setOutputPath()]");
			return;
		}

		writeDataToPDF( null, null, data, pages, fileId, Boolean.FALSE );
	}
	
	public void writeDataToPDF( String pdfPath, String outPath, Object data, String fileId, Boolean printTest ) {		
		writeDataToPDF( pdfPath, outPath, data, null, fileId, printTest );
	}

	/**
	 * Writes data from an object to a PDF with matching smart tags.
	 * PDF should be preset with instantiation of this class.
	 * 
	 * See test class with same name for example.
	 * 
	 * @param Data : object or bean containing data to stamp.
	 * @param Pages : page numbers to stamp data onto
	 * @param outPath : outPut path for completed PDF.
	 * @param pdfPath : the absolute path to an editable pdf template.
	 * @param printData : if set to true, data will be ignored and only the smart tag names will print.  
	 */
	public synchronized void writeDataToPDF( String pdfPath, String outPath, 
			Object data, String[] pages, String fileId, Boolean printTest ) {
		
		setPrintPageNumbers( pages ); 
				
		if( getPrintPageNumbers() == null || getPrintPageNumbers().length <= 0 ) {
			_Logger.error("No page numbers provided.");
			return;
		} 
		
		if( fileId == null ) {
			_Logger.error("Identifiable file id is missing");
			return;
		} 
		
		if(data != null) {
			setDataObject( data );
		}

		if( (pdfPath != null) && (! pdfPath.isEmpty()) ) {
			try {
	            setFilePath( new URL(pdfPath) );
            } catch (MalformedURLException e) {
            	_Logger.log(Level.FATAL, null, e);
            }
			_init();
		}

		if( (outPath != null) && (! outPath.isEmpty()) ) {
			setOutputPath(outPath);
		}

		if( getReader() != null ) {

			getReader().selectPages( arrayToString( getPrintPageNumbers(), "," ) );

			try {
				
				if( ! getOutputPath().endsWith("/") ) {
					setOutputPath(getOutputPath() + "/");
				}
		
				setFileName(fileId + "_" + new Date().getTime() + "_" + getFileName());
				
				setOutputPath(getOutputPath() + getFileName());
				
				_Logger.info( "Writing output file to " + getOutputPath() );
				
				setFileName( new File( getOutputPath() ).getName());

				if(getStamper() == null) {
					setStamper( new PdfStamper(getReader(), 
							new FileOutputStream( getOutputPath() )) );
				}

				setSmartTags();

				if( printTest ) {
					addSmartTagsToPDF();	
				} else {
					addDataToPDF();
				}
				
				setPageNumbers();
				
				if( pdfControllerConfig != null ) {
					
					// add javascripts
					String[] javascripts = pdfControllerConfig.getJavaScript();
					for( int i = 0; i < javascripts.length; i++ ) {
						getStamper().getWriter().addJavaScript(javascripts[i], Boolean.FALSE);
					}
					
					// final touches
					getStamper().setFreeTextFlattening(pdfControllerConfig.getFreeTextFlattening());
					getStamper().setFormFlattening(pdfControllerConfig.getFormFlattening() );

				}

			} catch (FileNotFoundException e1) {
				_Logger.log(Level.FATAL, null, e1);
			} catch (DocumentException e1) {
				_Logger.log(Level.FATAL, null, e1);
			} catch (IOException e1) {
				_Logger.log(Level.FATAL, null, e1);
			} finally {	
				if(getStamper() != null) {
					try {
						getStamper().close();
					} catch (DocumentException e) {
						_Logger.log(Level.FATAL, null, e);
					} catch (IOException e) {
						_Logger.log(Level.FATAL, null, e);
					}
				}
			}

		} else {
			_Logger.warn("Unable to load document from"+ filePath);
		}

	}
	
	/**
	 * Use smart tags to 
	 * extract the property values from a POJO Object.
	 * 
	 * Assuming that the pdf input path has been preset.
	 * 
	 * @param data : data object that contains data.
	 */
	private void addDataToPDF() {

		AcroFields acroFields = getStamper().getAcroFields();	                    
		List<String> smartTagList = getSmartTags();
		String replaceWith = "";
		String smartTagName;
		for( int i = 0; i < smartTagList.size(); i++ ) {
					
			smartTagName = smartTagList.get(i);
	
			replaceWith = getPdfDataMap().get( smartTagName );

			try {
				_Logger.debug("Replacement Key and Value: Key = " + smartTagName + " Value = " + replaceWith );

				acroFields.setField( smartTagName, replaceWith );

			} catch (IOException e) {
				_Logger.log(Level.FATAL, "Failed to set method " + smartTagName + " with value " + replaceWith + " into PDF document", e);
			} catch (DocumentException e) {
				_Logger.log(Level.FATAL, "Failed to set method " + smartTagName + " with value " + replaceWith + " into PDF document", e);
			} 
		} 
	}
	
	// -----> Eventually split the data portion into a new class.

	private void digestData( Object data ) {
		
		Map<String,Method> getterMethods = getGetterMethods( data );
		setMethodMap( getterMethods );

		Iterator<String> keys = getterMethods.keySet().iterator();
		String element;
		Method method;
		String key;
		setPdfDataMap( new HashMap<String, String>() );
		while( keys.hasNext() ) {
			key = keys.next();
			method = getterMethods.get(key);
			element = invokeValue( key );

			if( "java.lang.String".equals(method.getReturnType().getName()) ) {
				element = handleCharacterLimits( key, element );	
			}
			
			if( ! element.isEmpty() ) {
				getPdfDataMap().put(key, element);
			}
		}

	}

	/**
	 * For pulling methods out of generic objects.
	 * This method will recurse through JPA entity beans returned by a method.
	 * The following methods could be pulled out and put into their own 
	 * utility class. 
	 * 
	 * Credit: http://www.java2s.com/Code/Java/Reflection/GetsthegettersofapojoasamapofStringaskeyandMethodasvalue.htm
	 */
	public synchronized Map<String, Method> getGetterMethods(Object data) {
		HashMap<String,Method> methods = new HashMap<String,Method>();

		try {
			fillGetterMethods(data, methods);
		} catch (IllegalArgumentException e) {
			_Logger.fatal("Failed to get generic object methods ", e);
		} catch (IllegalAccessException e) {
			_Logger.fatal("Failed to get generic object methods ", e);
		} catch (InvocationTargetException e) {
			_Logger.fatal("Failed to get generic object methods ", e);
		}

		return methods;
	}

	private synchronized void fillGetterMethods( Object data, Map<String, Method> baseMap ) 
			throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		fillGetterMethods(data, baseMap, "");
	}

	private synchronized void fillGetterMethods(Object data, Map<String, Method> baseMap, String prepend) 
			throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {

		Class<?> genericClass = data.getClass();
		Method[] methods = genericClass.getDeclaredMethods();
		Method method;
		int modifiers;
		String methodName;
		Class<?> returnType;
		Package packageType;
		Object subObject = null;
		List<?> genericList = null;
		Integer genericListSize = 0;
		
		for ( int i = 0; i < methods.length; i++ ) {

			method = methods[i];
			modifiers = method.getModifiers();
			returnType = method.getReturnType();
			packageType = returnType.getPackage();

			_Logger.debug("Package Type: " + packageType);
			_Logger.debug("Return Type: " + returnType.getName());

			if(! Modifier.isStatic( modifiers ) &&  
					method.getParameterTypes().length == 0 &&
					returnType != null && 
					Modifier.isPublic( modifiers) ) {

				methodName = method.getName();

				// a second page may be required depending on the list size and 
				// the list limit setting in the PDFControllerConfig class. 
				if( returnType.getName().equals("java.util.List") ) {
					
					_Logger.debug("Found a List: " + returnType.getName() + "; " + methodName);

					genericList = (List<?>) method.invoke( data );

					if( genericList != null ) {

						genericListSize = genericList.size();
						
						handleTableLimit( toProperty( "get".length(), methodName), genericListSize );
						
						for( int j = 0; genericListSize > j; j++ ) {
							subObject = genericList.get(j);
							if(subObject != null) {
								fillGetterMethods( subObject, baseMap, methodName + TABLE_ROW_INDICATOR + j );
							}							
						}
					}
				}
				

				if( isPackageAllowed( packageType ) ) {
					subObject = method.invoke(data);					
					if(subObject != null) {
						fillGetterMethods( subObject, baseMap, methodName );
					}

				} else {
					_Logger.debug("Package " + packageType + " is forbidden.");
				}

				if( ! prepend.isEmpty() ) {
					if ( prepend.startsWith("is") ) {
						prepend = toProperty( "is".length(), prepend) + ".";
					} else if (  prepend.startsWith("get") ) {
						prepend = toProperty("get".length(), prepend) + ".";
					}
				}

				if ( methodName.startsWith("is") ) {
					baseMap.put( prepend + toProperty( "is".length(), methodName), method );
				} else if ( methodName.startsWith("get") ) {
					baseMap.put( prepend + toProperty("get".length(), methodName), method );
				}
			}
		}
	}

	/**
	 * String return values only.
	 * This method will return data 2 methods deep from the 
	 * Method Map and POJO Object set in current Object state.  
	 * ie: demographic.phoneNumber
	 * 
	 * @param key
	 * @return String of data or empty if none.
	 */
	public String invokeValue(String methodName) {		
		return castToString( invokeValue( methodName, getMethodMap(), getDataObject() ) );
	}

	/**
	 * String return values only.
	 * @param <E>
	 * @param methodName
	 * @param methodMap
	 * @param data
	 * @returns generic Object, null on error.
	 */
	private Object invokeValue(String methodName, Map<String, Method> methodMap, Object data) {
				
		Object value = "";
		String[] methodParts = null;
		Object objectOne = null;
		Method method = null;
		String methodOne = "";
		int index = -1;

		if( methodMap.containsKey( methodName ) ) {			
			method = methodMap.get( methodName );			
		} else {
			return value;
		}

		if( methodName.contains(".") ) {
			methodParts = methodName.split("\\.");
			methodOne = methodParts[0];
		}

		if( methodOne.contains( TABLE_ROW_INDICATOR ) ) {
			methodParts = methodOne.split( TABLE_ROW_INDICATOR );	        	
			methodOne = methodParts[0];	        	
			index = stringToInt( methodParts[1] ); 	    	        		        	
		}

		try {
			_Logger.debug("Value state 1 = " + value);

			if( methodMap.containsKey( methodOne ) ) {
				objectOne = methodMap.get( methodOne ).invoke( data ); 	
			}

			if( objectOne != null ) {       		
				_Logger.debug("MethodName [MapKey]: " + methodName + " ObjectOne [GenericObject]: " 
						+ objectOne.toString() + " Index [ListIndex]: " + index);

				if( objectOne instanceof java.util.List && index > -1) {
					_Logger.debug("Method [ObjectList]: " + methodMap.get( methodName ).toGenericString());       			    			       			
					_Logger.debug("Method [ListPOJOBean]: " + method.getName()); 
					value = method.invoke( ( (java.util.List<?>) objectOne ).get(index) );
				} else {
					_Logger.debug("Method [POJOBean]: " + method.getName());
					value = method.invoke( objectOne );
				}

				_Logger.debug("Value state 2 = " + value);

			} else {        		
				_Logger.debug("Method [FormBean]: " + method.getName());       		
				value = method.invoke(data);
				_Logger.debug("Value state 3 = " + value);           	
			}

		} catch (IllegalArgumentException e) {
			_Logger.fatal("Failed to invoke method " + methodName, e);
		} catch (IllegalAccessException e) {
			_Logger.fatal("Failed to invoke method " + methodName, e);
		} catch (InvocationTargetException e) {
			_Logger.fatal("Failed to invoke method " + methodName, e);
		}

		if(value == null) {
			value = "";
			//_Logger.warn("Value for " + methodName + " not found in data object.");
		}

		return value;
	}
	
	/**
	 * Returns a substring matching the limit as predetermined.
	 * Adds the remaining string(s) to the appropriate array for 
	 * addition to the form elsewhere.
	 * 
	 * @param tagName
	 * @param text
	 * @return
	 */
	private final String handleCharacterLimits( final String tagName, final String text ) {

		PDFControllerConfig config = getPdfControllerConfig();
		HashMap<String, Integer[]> characterLimits = null;
		HashMap<String, Integer[]> lineLimits = null;
		
		int characters = text.length();		
		int linecount = text.split( LINE_BREAK ).length;
		
		int characterLimit = 0;
		int lineLimit = 0;		
		int lineLimitPage = 0;
		int characterLimitPage = 0;
		
		String string = new String( text ).trim();
		String[] splitString = null;
		String substring = "";
		String newTagName = "";
		
		if( config != null ) {			
			characterLimits = config.getTextLengthLimits();
			lineLimits = config.getTextBoxLineLimits();			
		}

		if( characterLimits != null && characterLimits.containsKey( tagName ) ) {
			characterLimit = characterLimits.get( tagName )[0];
			characterLimitPage = characterLimits.get( tagName )[1];
			
			_Logger.info( "Character Limit Page " + characterLimitPage + " Character limit " + characterLimit + " Actual " + characters );
		}

		if( lineLimits != null && lineLimits.containsKey( tagName ) ) {
			lineLimit = lineLimits.get( tagName )[0];
			lineLimitPage = lineLimits.get( tagName )[1];
			
			_Logger.info( "Line Limit Page " + lineLimitPage + " Line limit " + lineLimit + " Actual " + linecount );
		}
	
		if( lineLimit > 0 && linecount > lineLimit ) {
			splitString = splitLines( string, lineLimit );
			
			if( lineLimitPage > 0 ) {				
				addPrintPageNumber( lineLimitPage+"" );
				newTagName = concatTag( tagName, lineLimitPage  );
			}
		
		} else if( characterLimit > 0 && characters > characterLimit ) {
			splitString = splitString( string, characterLimit );
			if( characterLimitPage > 0 ) {				
				addPrintPageNumber( characterLimitPage+"" );
				newTagName = concatTag( tagName, characterLimitPage ); 
			}			
		}
		
		if( splitString != null ) {			
			string = splitString[0];
			substring = splitString[1];	
		}

		if( ! newTagName.isEmpty() ) {			
			getPdfDataMap().put( newTagName, substring );			
			handleCharacterLimits( newTagName, substring );
		}

		return string;
	}
	
	/**
	 * Determines if an additional table or page is required to handle table rows
	 * that exceed a given limit. 
	 * Limits are set in the PDFControllerConfig object.
	 * @param tableName
	 * @param numberRows
	 */
	private void handleTableLimit( String tableName, Integer numberRows ) {
		
		PDFControllerConfig config = getPdfControllerConfig();
		HashMap<String, Integer[]> tableRowLimits = null;
		Integer rowLimit = null;
		String page = null;
		Integer[] directives = null;
		
		_Logger.info("Getting directives for table " + tableName );
		
		if( config != null ) {
			tableRowLimits = config.getTableRowLimits();
		}
		
		if( tableRowLimits != null && tableRowLimits.containsKey( tableName ) ) {

			directives = tableRowLimits.get( tableName );
			rowLimit = directives[0];
			page = directives[1]+"";
			
			if( numberRows > rowLimit ) {
				addPrintPageNumber( page );
			}
		}

	}
	
	public static final String concatTag(final String string, final int integer) {
		return concatTag(string, integer+"");
	}
	
	public static final String concatTag(final String string, final String integer) {
		
		String newTag = "";

		if( string.toLowerCase().matches("^[a-z]*[1-9]{1,5}$") ) {
			String[] splitString = string.split( "(?=\\d*$)", 2 );
			newTag = splitString[0] + integer;
		} else {
			newTag = new String( string + integer );
		}
		
		return newTag;
	}
	
	/**
	 * Splits string at the space between words before the given 
	 * index.
	 * @return
	 */
	public static final String[] splitString( final String string, final int index ) {
		
		if( string.length() < index) {
			return null;
		}
		
		String[] splitString = new String[2];
		String substring = string.substring(0, index);
		splitString[0] = substring.substring( 0, substring.lastIndexOf(" ") ).trim();
		
		splitString[1] = string.substring( splitString[0].length() ).trim();

		return splitString;
	}
	
	/** 
	 * Split string by given number of lines.
	 * 
	 */
	public static final String[] splitLines( final String string, final int limit ) {

		String[] lines = string.split( LINE_BREAK );
		String[] splitString = new String[2];
		splitString[0] = "";
		splitString[1] = "";
		
		for(int i = 0; i < limit ; i++) {
			splitString[0] += lines[i].trim() + "\r\n";
		}
		
		for(int j = limit; j < lines.length; j++) {
			splitString[1] += lines[j].trim() + "\r\n";
		}

		return splitString;
	}
	
	private final Boolean isPackageAllowed( final Package packageType ) {
		
		String[] targetBeans = null;
		Boolean allowed = Boolean.FALSE;
		
		if( packageType == null ) {
			return allowed;
		}

		if( getPdfControllerConfig() != null ) {
			targetBeans = getPdfControllerConfig().getTargetBeans();
		}
		
		if( targetBeans != null ) {
			for( int i = 0; i < targetBeans.length; i++ ) {				
				if( packageType.equals( Package.getPackage( targetBeans[i] ) ) ) {
					_Logger.debug("Package Type " + packageType + " is allowed.");
					allowed = Boolean.TRUE;
				}				
			}
		}
		
		return allowed;
	}
	
	private void setPageNumbers() throws DocumentException, IOException {		
		
		int pages = getReader().getNumberOfPages();
		int i = 0;	
		PdfContentByte overContent;
		Rectangle pageSize = null;
		BaseFont font = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.WINANSI, BaseFont.EMBEDDED);

		while (i < pages) {
			i++;
			overContent = getStamper().getOverContent(i);
			pageSize = overContent.getPdfDocument().getPageSize();
			overContent.beginText();
			overContent.setFontAndSize(font, 9);
			overContent.setTextMatrix(pageSize.getWidth() - 50, pageSize.getHeight() - 70);
			overContent.showText("Page " + i + " of " + pages);
			overContent.endText();
		}
	}
	

	/**
	 * Cast select Objects into a String object.
	 * So far only Dates, Integers, and Strings are allowed through
	 * @param object
	 * @return
	 */
	private static final String castToString(final Object object) {
		String string = "";

		if(object instanceof java.lang.String) {

			string = (String) object;

		} else if( object instanceof java.util.Date ) {

			string = dateToString( (java.util.Date) object );

		} else if(object instanceof java.lang.Integer) {

			string = intToString((java.lang.Integer) object);

		}

		return string;
	}
	
	/**
	 * Converts a POJO method signature into the conventional camel-case
	 * format.
	 * @param start : starting point of the get or set prepend
	 * @param methodName : the method string being formatted.
	 * @return
	 */
	private static String toProperty(int start, String methodName) {

		char[] property = new char[methodName.length() - start];       
		methodName.getChars(start, methodName.length(), property, 0);        
		int firstLetter = property[0];
		property[0] = (char)(firstLetter<91 ? firstLetter + 32 : firstLetter);

		return new String(property);
	}

	/**
	 * Converts a Java array into a comma delimited String
	 * @return
	 */
	private static final String arrayToString(final String[] array, final String delimiter) {

		StringBuilder stringBuilder = new StringBuilder();	        

		for(int i = 0; array.length > i; i++) {
			stringBuilder.append(array[i]);
			if (i < array.length - 1) {
				stringBuilder.append(delimiter);
			}
		}

		_Logger.info("PRINTING PAGE NUMBERS: " + stringBuilder);

		return stringBuilder.toString();
	}

	private static final int stringToInt(final String integer) {
		int out = -1;
		if( (integer != null) && (! integer.isEmpty()) ) {
			String filteredInteger = integer.replaceAll("[^0-9]", "");
			try {
				out = Integer.parseInt(filteredInteger);
			} catch (NumberFormatException e) {
				_Logger.log(Level.FATAL, "Number format exception.", e);
			}
		}
		return out;
	}

	private static final String intToString(final Integer integer) {
		String number = "";

		if(integer != null) {
			number = String.valueOf(integer);
			number = number.replaceAll("[^0-9]", "");
		}

		return number;
	}

	private static final String dateToString(final Date date) {
		String formattedDate = "";
		SimpleDateFormat format = new SimpleDateFormat( DATE_FORMAT );	
		if(date != null) {
			formattedDate = format.format(date);
		}		
		return formattedDate;
	}

}
