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
package oscar.dms;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.log4j.Logger;
import org.oscarehr.common.model.EFormData;
import org.oscarehr.util.MiscUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import org.w3c.tidy.Tidy;
import org.xhtmlrenderer.pdf.ITextRenderer;
import org.xml.sax.SAXException;
import com.lowagie.text.DocumentException;
import oscar.OscarProperties;
import oscar.form.util.FormTransportContainer;

/**
 * A utility that converts HTML into a PDF and returns an Oscar eDoc object.
 * 
 * This is useful for converting Forms and eForms with well structured HTML into 
 * PDF documents that can be attached to Consultation Requests, Faxes or transferred 
 * to other file systems.
 * 
 * NOT ALL DOCUMENTS ARE CONVERTABLE. USE AT OWN RISK.
 */
public class ConvertToEdoc {
	
	private static final Logger logger = MiscUtils.getLogger(); 

	public static final String CUSTOM_STYLESHEET_ID = "pdfMediaStylesheet";
	
	public static enum DocumentType { eForm, form }
	
	private static final String DEFAULT_FILE_PATH = String.format( "%1$s%2$stemp", 
				OscarProperties.getInstance().getProperty( "BASE_DOCUMENT_DIR", String.format( "%1$svar%1$slib", File.separator) )
					, File.separator 
			); 
	
	private static final String DEFAULT_IMAGE_DIRECTORY = String.format( "%1$s", OscarProperties.getInstance().getProperty( "eform_image" ) );
	private static final String DEFAULT_FILENAME = "convert_to_edoc_file";
	private static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";
	private static final String DEFAULT_CONTENT_TYPE = "application/pdf";
	private static final String SYSTEM_ID = "-1";
	private static final String DEFAULT_FILE_SUFFIX = ".pdf";
	private static enum PathAttribute { src, href }
	private static enum FileType { pdf, css, jpeg, png, gif }
	
	private static String contextPath;
	private static String realPath;

	/**
	 * Convert EForm to EDoc
	 * 
	 * Returns an EDoc Object with a filename that is saved in this class' 
	 * temporary DEFAULT_FILE_PATH. This file can be persisted by moving from the 
	 * temporary path to a file storage path prior to persisting this Object to the 
	 * database. 
	 */
	public synchronized static final EDoc from( EFormData eform ) {
		
		String eformString = eform.getFormData();	
		String demographicNo = eform.getDemographicId() + "";
		String filename = buildFilename( eform.getFormName(), demographicNo );
		EDoc edoc = null;
		
		if( execute( eformString, filename ) ) {
			edoc = buildEDoc( filename, 
					eform.getSubject(), 
					"", 
					eform.getProviderNo(), 
					demographicNo, 
					DocumentType.eForm );
		}
		
		return edoc;	
	}

	/**
	 * Convert Form to EDoc
	 * 
	 * Returns an EDoc Object with a filename that is saved in this class' 
	 * temporary DEFAULT_FILE_PATH. This file can be persisted by moving from the 
	 * temporary path to a file storage path prior to persisting this Object to the 
	 * database. 
	 */
	public synchronized static final EDoc from( FormTransportContainer formTransportContainer ) {
	
		String htmlString = formTransportContainer.getHTML();
		String demographicNo = formTransportContainer.getDemographicNo();
		String filename = buildFilename( formTransportContainer.getFormName(), demographicNo );
		String subject = formTransportContainer.getSubject();
		String providerNo = formTransportContainer.getProviderNo();
		if( providerNo == null ) {
			providerNo = formTransportContainer.getLoggedInInfo().getLoggedInProviderNo();
		}
		// this should be the same for every thread.
		ConvertToEdoc.contextPath = formTransportContainer.getContextPath();
		ConvertToEdoc.realPath = formTransportContainer.getRealPath();
		
		EDoc edoc = null;		

		if( execute( htmlString, filename ) ) {			
			edoc = buildEDoc( filename, 
					subject, 
					"", 
					providerNo, 
					demographicNo, 
					formTransportContainer.getDocumentType() );			
		}
		
		return edoc;
	}
	
	/**
	 * Execute building and saving PDF to temp directory.
	 */
	private static boolean execute( String eformString, String filename ) {
		
		String correctedDocument = tidyDocument( eformString );			
		Document document = buildDocument( correctedDocument );
		String filepath = String.format("%1$s%2$s%3$s", ConvertToEdoc.getFilePath(), File.separator, filename );
		ByteArrayOutputStream os = new ByteArrayOutputStream();		
		try {
			renderPDF( document, os );
			saveFile( filepath, os );
		} catch (DocumentException e1) {
			filepath = null;
			logger.error( "Exception parsing file to PDF. File not saved.", e1 );
		} finally {
			if( os != null ) {
				try {
					os.close();
				} catch (IOException e) {
					logger.error( "", e );
				}
			}
		}

		return ( filepath != null );
	}
	
	/**
	 * creates a filename
	 */
	private static final String buildFilename( String filename, String demographicNo ) {
		
		if( filename == null || filename.isEmpty() ) {
			filename = DEFAULT_FILENAME;
		}
		
		filename = filename.trim();
		filename = filename.replaceAll(" ", "_");
		filename = String.format("%1$s_%2$s", filename, demographicNo );
		filename = String.format("%1$s_%2$s", filename, new Date().getTime() );
		filename = String.format("%1$s%2$s", filename, DEFAULT_FILE_SUFFIX );

		return filename;
	}
	
	/**
	 * Builds an EDoc instance.
	 */
	public static EDoc buildEDoc( final String filename, final String subject, final String sourceHtml, 
			final String providerNo, final String demographicNo, final DocumentType documentType ) {

		final SimpleDateFormat simpleDateFormat = new SimpleDateFormat( DEFAULT_DATE_FORMAT ); 		
		final String todayDate = simpleDateFormat.format( new Date() );
		
		EDoc eDoc = new EDoc(
				( subject == null ) ? "" : subject,
				documentType.name(), 
				filename, 
				sourceHtml, 
				SYSTEM_ID, 
				providerNo, 
				"",
				EDocFactory.Status.ACTIVE.getStatusCharacter(), 
				todayDate,
				"", 
				new Date().toString(),
				EDocFactory.Module.demographic.name(), 
				demographicNo,
				Boolean.FALSE );
		
		eDoc.setContentType( DEFAULT_CONTENT_TYPE );
		eDoc.setContentDateTime( new Date() );
		eDoc.setNumberOfPages(0);

		return eDoc;
	}
	
	/**
	 * File manager to save final PDF output stream to the file system.
	 */
	private static final void saveFile( final String filepath, ByteArrayOutputStream os ) {

		File directory = new File( getFilePath() );
		File file = new File( filepath );
		FileOutputStream fop = null;
		
		try {

			if( ! directory.exists() ) {
				directory.mkdir();
			}
			
			if ( ! file.exists() ) {
				file.createNewFile();
			}
			
			fop = new FileOutputStream( file.getAbsoluteFile() );
			fop.write( os.toByteArray() );
			
		} catch (FileNotFoundException e) {
			logger.error( "No file was found at " + file.getAbsoluteFile(), e );
		} catch (IOException e) {
			logger.error( "Could not create new file at " + file.getAbsoluteFile() , e );
		} finally {
			if( fop != null ) {
				try {
					fop.flush();
				} catch (IOException e) {
					logger.error( "", e );
				} finally {
					try {
						fop.close();
					} catch (IOException e) {
						logger.error( "", e );
					}
				}
			}
		}
	}
	
	/**
	 * Use the Flying Saucer tools to render a PDF from a 
	 * well formed w3c XHTML document
	 * @throws DocumentException 
	 */
	private static final void renderPDF( final Document document, ByteArrayOutputStream os ) 
			throws DocumentException {		
		ITextRenderer renderer = new ITextRenderer();
		renderer.setDocument( document,null);	
		renderer.layout();
		renderer.createPDF( os );
	}
	
	/**
	 * Build this HTML document. 
	 * - adds translated image paths
	 * - inserts custom stylesheets.
	 */
	private static final Document buildDocument( final String documentString ) {
		
		Document document = getDocument( documentString );
		if( document != null ) {
			translateResourcePaths( document );
			setHeadElement( document );
			addCss( document );
		}
		
		logger.debug( printDocument( document ) );

		return document;
	}
	
	/**
	 * Get a W3C XML document from well formed XML
	 */
	private static Document getDocument( final String documentString ) {

		DocumentBuilder builder;
		Document document = null;					
		ByteArrayInputStream bais = new ByteArrayInputStream( documentString.getBytes() );

		try {
			builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			document = builder.parse(bais);			
		} catch (SAXException e) {
			logger.error( "", e );
		} catch (IOException e) {
			logger.error( "", e );
		} catch (ParserConfigurationException e) {
			logger.error( "", e );
		} finally {
			if( bais != null ) {
				try {
					bais.close();
				} catch (IOException e) {
					logger.error( "", e );
				}
			}
		}
		
		return document;
	}
	
	/**
	 * Adds custom CSS templates to the Document.
	 * 
	 * Normally the stylesheets should be included with the HTML being converted. This method may be
	 * required to alter the current style for better print to PDF. Or if the original stylesheet gets 
	 * stripped out of the HTML like with the Rich Text Letter Editor
	 * 
	 * A stylesheet reference can be set into the origin HTML document with a hidden input tag: 
	 * <input type="hidden" id="customStylesheet" name="customStylesheet" value="<stylesheet filename>" />
	 * This tag would be inserted between the section tag of a Rich Text Letter Template. 
	 * The custom stylesheet will be retrieved from Oscar's images directory. Only the filename needs to be 
	 * given by the input tag. This method will build the filepath.
	 * 
	 * An alternative method is to set the stylesheet into the ConvertToEdocConfig object prior to converting 
	 * the HTML into a PDF.  This may be done from inside an action class in order to retrieve custom stylesheets
	 * from anywhere in the file system.
	 * 
	 * - Adds a head element to the document if one does not exist.
	 *   
	 */
	private static void addCss( Document document ) {

		XPath xpath = XPathFactory.newInstance().newXPath();
		Element styleSheetElement = null;

		try {
			styleSheetElement = (Element) xpath.evaluate("//*[@id='" + CUSTOM_STYLESHEET_ID + "']", document, XPathConstants.NODE);
		} catch (XPathExpressionException e) {
			logger.error("Error", e);
		}

		if( styleSheetElement != null ) {
			setParameterInjectedCss( document, styleSheetElement.getAttribute("value") );
		}
	}
	
	/**
	 * It is critical that a head element is present for this given document. 
	 */
	private static void setHeadElement( Document document ) {
		
		Node headNode = null;
		Node htmlNode = document.getDocumentElement();
		NodeList nodeList = htmlNode.getChildNodes();
		
		for( int i = 0; i < nodeList.getLength(); i++ ) {
			Node node = nodeList.item(i);

			if( "head".equals( node.getNodeName() )) {
				headNode = node;
			}
		}
		
		if( headNode == null ) {              
	      	htmlNode.appendChild( document.createElement("head") );
		}
	}
	
	/**
	 * Returns the head element directly from the given Document 
	 */
	private static Element getHeadElement( Document document ) {
		return (Element) document.getElementsByTagName("head").item(0);
	}
	
	/**
	 * Create a Link element from the CSS filename that was inserted into the 
	 * origin HTML as a hidden input element. 
	 */
	private static void setParameterInjectedCss( Document document, String filename ) {
		filename = buildImageDirectoryPath( filename );
		filename = validateLink( filename );

		// add a stylesheet ie: link <link rel="stylesheet" href=".." />
		if( filename != null ) {
			Element linkElement = document.createElement("link");
			linkElement.setAttribute("rel", "stylesheet");
			linkElement.setAttribute("href", filename);
			getHeadElement( document ).appendChild( linkElement );
		}		
	}

	/**
	 * HTTP request paths routed through Struts need to be 
	 * translated into a relative path to the global images directory.
	 */
	private static void translateResourcePaths( Document document ) {
		translateLinkPaths( document );
		translateImagePaths( document );
	}
	
	/**
	 * This method handles paths set into a link element
	 */
	private static void translateLinkPaths( Document document ) {
		NodeList linkNodeList = document.getElementsByTagName("link");	
		if( linkNodeList != null ) {
			translatePaths( linkNodeList, PathAttribute.href );
		}
	}
	
	/**
	 * This method handles paths set into an image element.
	 */
	private static void translateImagePaths( Document document ) {		
		NodeList imageNodeList = document.getElementsByTagName("img");		
		if( imageNodeList != null ) {
			translatePaths( imageNodeList, PathAttribute.src );
		}
	}
	
	/**
	 * Translate any given Link or Image element resource path from 
	 * a Struts HTTP request parameter or HTTP relative context path.
	 * 
	 * All resource links in the document must be absolute for the PDF 
	 * creator to work.
	 */
	private static void translatePaths( NodeList nodeList, PathAttribute pathAttribute ) {

		for( int i = 0; i < nodeList.getLength(); i++ ) {
			
			Element element = (Element) nodeList.item(i);			
			String path = element.getAttribute( pathAttribute.name() );
			String validLink = null;
			String parameters = null;
			String[] parameterList = null;		
			List<String> potentialFilePaths = new ArrayList<String>();

			if( path.contains("?") ) {
				// image or link paths with parameters
				parameters = path.split("\\?")[1];
			} else {
				// these are most likely relative context paths
				path = getRealPath( path );
				potentialFilePaths.add( path );
			}
			
			// parse the parameters and test if any are links to the eForm 
			// images library. Otherwise these resources are no good.
			if( parameters != null && parameters.contains("&") ) {
				parameterList = parameters.split("&");
			}
			
			if( parameterList != null ) {
				for( String parameter : parameterList ) {
					if( parameter.contains("=") ) {
						// these are file names that need a path.
						path = buildImageDirectoryPath( parameter.split("=")[1] );
						potentialFilePaths.add( path );
					}	
				}
			} else if( parameters != null && parameters.contains("=") ) {
				path = buildImageDirectoryPath( parameters.split("=")[1] );
				potentialFilePaths.add( path );
			}
			
			// there really should be only one valid path.
			// Only use the one that validates
			validLink = validateLink( potentialFilePaths );
			
			// change the element resource link to something absolute  
			// that can be used by the PDF creator. 
			if( validLink != null ) {
				element.setAttribute( pathAttribute.name(), validLink );
			}
		}
	}
	
	/**
	 * Feed this method a filename and it will return a full path to the Oscar images directory. 
	 */
	private static String buildImageDirectoryPath( String filename ) {
		return String.format( "%1$s%2$s%3$s", getImageDirectory(), File.separator, filename );
	}
	
	/**
	 * Convert a given context path into a file system absolute path.
	 * @param contextPath
	 * @return
	 */
	private static final String getRealPath( String uri ) {
		String contextRealPath = "";
		
		logger.debug( "Context path set to " + contextPath );
		
		if( ConvertToEdoc.contextPath != null && ConvertToEdoc.realPath != null ) {
			
			logger.debug( "Relative file path " + uri );

			String filePath = uri.substring( contextPath.length(), uri.length() );
			filePath = filePath.replaceFirst( File.separator , "" );
			contextRealPath = ConvertToEdoc.realPath;
			if( ! contextRealPath.endsWith( File.separator )) {
				contextRealPath = contextRealPath + File.separator;
			}
			contextRealPath = String.format( "%1$s%2$s", contextRealPath, filePath );

			logger.debug( "Absolute file path " + contextRealPath );
	
		}
		
		return contextRealPath;
	}
	
	/**
	 * Returns a List of valid file links from a list of potential valid links.
	 */
	private static final List<String> validateLinks( List<String> potentialLinks ) {

		List<String> finalLinks = null;
		String validLink = null;
		
		for( String potentialLink : potentialLinks ) {
			if( potentialLink.isEmpty() ) {
				continue;
			}
			
			validLink = validateLink( potentialLink );
			
			if( finalLinks == null && validLink != null ) {
				finalLinks = new ArrayList<String>();
			}

			if( validLink != null ) {
				finalLinks.add( validLink );
			}
		}
		
		return finalLinks;
	}
	
	/**
	 * Returns the first valid file link from a list of potential valid links.
	 */
	private static final String validateLink( List<String> potentialLinks ) {
		
		logger.debug( "Validating potential file paths " + potentialLinks );
		
		List<String> validLinks = validateLinks( potentialLinks );
		
		if( validLinks != null ) {
			return validLinks.get(0);
		}
		
		return null;
	}
	
	/**
	 * Returns only 1 valid file link.
	 */
	private static final String validateLink( String potentialLink ) {
		
		File file = null;
		String absolutePath = null;
				
		for( FileType fileType : FileType.values() ) {
			if( ( potentialLink.endsWith( fileType.name().toLowerCase() ) )) {
				file = new File( potentialLink );
			}			
		}

		if( file != null && file.isFile() ) {
			absolutePath = file.getAbsolutePath(); 
			
			logger.debug( "Validated path " + absolutePath );
		}

		return absolutePath;
	}

	/**
	 * Clean up any artifacts or poorly formed XML
	 */
	private static String tidyDocument( final String documentString ) {

		Tidy tidy = getTidy();
		StringReader reader = new StringReader( documentString );
		StringWriter writer = new StringWriter();
		String correctedDocument = null;
		
		tidy.parse( reader, writer );
		correctedDocument = new String( writer.toString() );	

		writer.flush();
		
		try {
			writer.close();
		} catch (IOException e) {
			logger.error( "Error closing writer stream for JTidy", e );
		}

		return correctedDocument;
	}
	
	/**
	 * Instantiate the Tidy HTML validator
	 */
	private static final Tidy getTidy() {
		Tidy tidy = new Tidy();
		Properties properties = new Properties(); 
		InputStream is = null;
		
		// these can be overriden with the properties file.
		tidy.setForceOutput( Boolean.TRUE ); 	// output the XHTML even if it fails the validator.
		tidy.setXHTML( Boolean.TRUE ); // only reading XHTML here.
		tidy.setDropEmptyParas(false);
		tidy.setDocType( "omit" ); // everything will fail horribly if doctype is strict.
		tidy.setMakeClean( Boolean.TRUE );
		tidy.setLogicalEmphasis( Boolean.TRUE ); // replace the b and em tags with proper <strong> tags

		// logging
		if( logger.isDebugEnabled() ) {
			tidy.setHideComments( Boolean.FALSE );
			tidy.setQuiet( Boolean.FALSE );
		} else {
			tidy.setHideComments( Boolean.TRUE );
			tidy.setQuiet( Boolean.TRUE );
		}

		try {
			is = ConvertToEdoc.class.getClassLoader().getResourceAsStream( "/oscar/dms/ConvertToEdoc.properties" );
			if( is != null ) {
				properties.load( is );
			}
		} catch (IOException e) {
			logger.warn("Could not load Tidy properties ", e);
		} finally {
			if( is != null ) {
				try {
					is.close();
				} catch (IOException e) {
					logger.error("Error", e);
				}
			}
		}
		
		tidy.getConfiguration().addProps( properties );
		
		logger.debug( printTidyConfig( tidy ) );
		
		return tidy;
	}
	
	/**
	 * Returns the temporary path where the new PDF was saved. 
	 * It may be desired to move the file at this path to a permanent document directory
	 */
	public static final String getFilePath() {
		return new String( getTempDirectory().replaceAll("//", File.separator ) );
	}
	
	private static final String getTempDirectory() {
		return DEFAULT_FILE_PATH;
	}
	
	private static final String getImageDirectory() {
		return DEFAULT_IMAGE_DIRECTORY;
	}
	
	/**
	 * Prints the document contents to console. Used for debugging.
	 */
	private static String printDocument( Document doc ) {

	    TransformerFactory tf = TransformerFactory.newInstance();
	    StreamResult streamResult = null;
	    try {
			Transformer transformer = tf.newTransformer();
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
			transformer.setOutputProperty(OutputKeys.METHOD, "xhml");
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
			streamResult = new StreamResult();
			streamResult.setOutputStream( new ByteArrayOutputStream() );
			transformer.transform( new DOMSource(doc), streamResult );

		} catch (Exception e) {
			logger.error("error debugging document " + e );
		} finally {
			if( streamResult != null ) {
				try {
					streamResult.getOutputStream().close();
				} catch (IOException e) {
					logger.error("error debugging document " + e );
				}
			}
		}
	    
	    return streamResult.getOutputStream().toString();
	}
	
	public static String printTidyConfig( Tidy tidy ) {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		OutputStreamWriter osw = new OutputStreamWriter( baos );
		tidy.getConfiguration().printConfigOptions( osw, true );
		
		String log = new String( baos.toString() );
		
		try {
			baos.close();
		} catch (IOException e) {
			logger.error("Error", e);
		} finally {
			try {
				osw.close();
			} catch (IOException e) {
				logger.error("Error", e);
			}
		}
		
		return log;
	}

}
