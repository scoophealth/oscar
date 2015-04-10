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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.oscarehr.util.MiscUtils;
import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.AcroFields;
import com.lowagie.text.pdf.AcroFields.Item;
import com.lowagie.text.pdf.PdfDictionary;
import com.lowagie.text.pdf.PdfName;
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

	private static final Logger _Logger = MiscUtils.getLogger();   
	private final String STRING_FILTER = "[^a-zA-Z0-9_' '!.!#]";
	
	private static String[] ALLOWED_BEAN_PACKAGES = new String[]{"org.oscarehr.common.model", 
		"oscar.form.pharmaForms.formBPMH.bean"};
	
	private static final String DATE_FORMAT = "MM-dd-yyyy";

	private PdfReader pdfreader;
	private int numberOfPages;
	private int certificationLevel;
	private PdfStamper stamper;
	private String outputPath;
	private File filePath;
	private String fileName;
	private Map<String, Method> methodMap;
	private Object data;

	public PDFController(){
		// default constructor.
	}

	public PDFController(File filePath){
		if( setFilePath(filePath) ) {
			_init();
		}

	}

	public PDFController(String filePath){   	
		if( setFilePath( new File(filePath) ) ) {
			_init(); 
		}
	}

	public void _init() {
		setReader(null);	    	
		setPdfMetaData();
	}

	public int getNumberOfPages() {
		return numberOfPages;
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
				this.pdfreader = new PdfReader( filePath.getAbsolutePath() );				
			} else {
				this.pdfreader = reader;
			}
		} catch (IOException e) {
			_Logger.log(Level.FATAL, null, e);
		}

	}

	private boolean setFilePath(File file){   	

		if( file.exists() ) {			
			this.filePath = file;
			return Boolean.TRUE;
		}

		_Logger.log(Level.FATAL, "Template file location " + file.getAbsolutePath() + " not found.");

		return Boolean.FALSE;
	}

	public boolean setFilePath(String filePath) { 
		return setFilePath( new File(filePath) );   	
	}

	public String getFilePathString() {
		if(getFilePath() == null) {
			return "";
		}
		return getFilePath().getAbsolutePath();
	}

	public File getFilePath() {
		return this.filePath;
	}

	public String getFileName() {
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
		digestData(data);
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
	 * Read the smart tags off of a pdf document and use them to 
	 * extract the property values from a POJO Object.
	 * 
	 * Assuming that the pdf input path has been preset.
	 * 
	 * @param data : data object that contains data.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void addDataToPDF() {

		AcroFields acroFields = getStamper().getAcroFields();	        
		Map acroFieldsMap = acroFields.getFields();	
		Iterator<String> acroFieldsIt = acroFieldsMap.keySet().iterator();              
		String replaceWith = "";
		String key = "";
		String cleanKey = "";
		int fieldType;
		String[] appStates;
		AcroFields.Item acroField;
		PdfDictionary annots;
		Iterator itannots;

		while( acroFieldsIt.hasNext() ) {

			key = acroFieldsIt.next().toString();
			cleanKey = key.replaceAll(STRING_FILTER, "");
			fieldType = acroFields.getFieldType(key);
			appStates = acroFields.getAppearanceStates(key);
			acroField = (Item) acroFieldsMap.get(key);
			annots = acroField.getWidget(0);

			_Logger.debug("Field Type: " + cleanKey + " = " + fieldType);

			itannots = annots.getKeys().iterator();	            
			while(itannots.hasNext()) {
				PdfName annotKey = (PdfName) itannots.next();
				_Logger.debug("ANNOT KEY: "+annotKey);
				_Logger.debug("ANNOT VALUE: "+annots.get(annotKey));
			} 

			if(appStates.length > 0) {
				for(int i =0; appStates.length > i; i++) {
					_Logger.debug("APPEARANCE STATE: "+appStates[i]);
				}
			}

			replaceWith = invokeValue( cleanKey );

			//count the characters and compare to limit.
			//			if(fieldType == AcroFields.FIELD_TYPE_TEXT) {
			//				replaceWith = contentSplicer(replaceWith, 30);
			//			}

			try {

				_Logger.debug("Replacement Key and Value: Key = " + cleanKey + " Value = " + replaceWith );

				acroFields.setField(cleanKey, replaceWith);

			} catch (IOException e) {
				_Logger.log(Level.FATAL, "Failed to set method " + cleanKey + " with value " + replaceWith + " into PDF document", e);
			} catch (DocumentException e) {
				_Logger.log(Level.FATAL, "Failed to set method " + cleanKey + " with value " + replaceWith + " into PDF document", e);
			} 

		} 

	}

	public void writeDataToPDF( Object data, String[] pages ) {
		writeDataToPDF(null, null, data, pages, "");
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

		writeDataToPDF( null, null, data, pages, fileId );
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
	 */
	public void writeDataToPDF( String pdfPath, String outPath, Object data, String[] pages, String fileId ) {

		if( pages.length <= 0) {
			_Logger.error("No page numbers provided.");
			return;
		}
		if( data == null ) {
			_Logger.error("No data object provided.");
			return;
		} else {
			setDataObject(data);
		}

		if( (pdfPath != null) && (! pdfPath.isEmpty()) ) {
			setFilePath(pdfPath);
			_init();
		}

		if( (outPath != null) && (! outPath.isEmpty()) ) {
			setOutputPath(outPath);
		}

		if( getReader() != null ) {

			getReader().selectPages( arrayToString( pages, "," ) );

			try {
				
				if( ! getOutputPath().endsWith("/") ) {
					setOutputPath(getOutputPath() + "/");
				}
				
				setOutputPath(getOutputPath() + fileId + "_" + new Date().getTime() + 
						"_" + getFilePath().getName());
				
				setFileName(new File( getOutputPath() ).getName());
				
				if(getStamper() == null) {
					setStamper( new PdfStamper(getReader(), 
							new FileOutputStream( getOutputPath() )) );
				}

				addDataToPDF();	
				
				getStamper().getWriter().addJavaScript( "this.print({bUI: true, bSilent: true, bShrinkToFit:true});", false );
				getStamper().getWriter().addJavaScript("this.closeDoc(true);");
				
				getStamper().setFreeTextFlattening(true);
				getStamper().setFormFlattening(true);
				getStamper().setFreeTextFlattening(true);

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

	private void digestData(Object data) {

		Map<String,Method> getterMethods = getGetterMethods(data);
		setMethodMap(getterMethods);

	}

	/**
	 * For pulling methods out of generic objects.
	 * This method will recurse through JPA entity beans returned by a method.
	 * The following methods could be pulled out and put into their own 
	 * utility class. 
	 * 
	 * Credit: http://www.java2s.com/Code/Java/Reflection/GetsthegettersofapojoasamapofStringaskeyandMethodasvalue.htm
	 */
	public synchronized static Map<String, Method> getGetterMethods(Object data) {
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

	private synchronized static void fillGetterMethods( Object data, Map<String, Method> baseMap ) 
			throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		fillGetterMethods(data, baseMap, "");
	}


	private synchronized static void fillGetterMethods(Object data, Map<String, Method> baseMap, String prepend) 
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

				if( returnType.getName().equals("java.util.List") ) {
					_Logger.debug("Found a List: " + returnType.getName());

					genericList = (List<?>) method.invoke( data ); 

					if(genericList != null) {

						for( int j = 0; genericList.size() > j; j++ ) {

							subObject = genericList.get(j);

							if(subObject != null) {
								fillGetterMethods( subObject, baseMap, methodName + "#" + j );
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
	 * @return
	 */
	public String invokeValue(String methodName) {
		String value = "";

		if( getMethodMap() != null && getDataObject() != null ) { 
			value = castToString( invokeValue(methodName, getMethodMap(), getDataObject()) );
		}

		return value;
	} 

	/**
	 * String return values only.
	 * @param <E>
	 * @param methodName
	 * @param methodMap
	 * @param data
	 * @returns generic Object, null on error.
	 */
	protected static Object invokeValue(String methodName, Map<String, Method> methodMap, Object data) {

		Object value = null;
		String[] methodParts = null;
		Object objectOne = null;
		Method method = null;
		String methodOne = "";
		int index = -1;

		if( ! methodMap.containsKey(methodName) ) {
			_Logger.debug("Method " + methodName + " not found in data object.");
			return value;
		} else {
			method = methodMap.get( methodName );
		}

		if( methodName.contains(".") ) {
			methodParts = methodName.split("\\.");
			methodOne = methodParts[0];
		}

		if( methodOne.contains("#") ) {
			methodParts = methodOne.split("#");	        	
			methodOne = methodParts[0];	        	
			index = stringToInt( methodParts[1] ); 	    	        		        	
		}

		try {

			_Logger.debug("Value state 1 = " + value);

			if( methodMap.containsKey( methodOne ) ) {
				objectOne = methodMap.get( methodOne ).invoke( data ); 	
			}

			if( objectOne != null ) {       		
				_Logger.debug("MethodName [MapKey]: " + methodName + " ObjectOne [GenericObject]: " + objectOne.toString() + " Index [ListIndex]: " + index);

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
			_Logger.warn("Value for " + methodName + " not found in data object.");
		}

		return value;
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

		} else if(object instanceof java.util.Date) {

			string = dateToString( (java.util.Date) object, new SimpleDateFormat(DATE_FORMAT));

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

	private static final String dateToString(final Date date, final SimpleDateFormat format) {
		String formattedDate = "";

		if(date != null) {
			formattedDate = format.format(date);
		}		
		return formattedDate;
	}

	private static final Boolean isPackageAllowed(final Package packageType) {
		
		Boolean allowed = Boolean.FALSE;
		String beanPackage = "";
		
		if( packageType == null ) {
			return allowed;
		}

		for(int i = 0; i < ALLOWED_BEAN_PACKAGES.length; i++ ) {			
			beanPackage = ALLOWED_BEAN_PACKAGES[i];
			if( packageType.equals( Package.getPackage( beanPackage ) ) ) {
				_Logger.debug("Package Type " + packageType + " is allowed.");
				allowed = Boolean.TRUE;
			}
		}
		
		return allowed;
	}

}
