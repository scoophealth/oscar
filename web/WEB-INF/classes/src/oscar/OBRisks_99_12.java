package oscar;

import java.io.IOException;
import java.util.Properties;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

public class OBRisks_99_12 {

  public static void main(String args[]) { 
  	//OBRisks_99_12 aE = new OBRisks_99_12();
    //System.out.print(aE.doStuff("obarrisks_99_12.xml"));
  }// End of main 

	public String doStuff(String uri) {
		try {
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser saxParser = factory.newSAXParser();
			XMLReader reader = saxParser.getXMLReader();

			ContentHandler contentHandler = new OBRisksHandler_99_12();
			reader.setContentHandler( contentHandler );
			reader.parse(uri);

			return ( ((OBRisksHandler_99_12) contentHandler).getResults());
		} catch (IOException e) {
			System.out.println("Error reading URI: " + e.getMessage());
		} catch (SAXException e) {
			System.out.println("Error in parsing: " + e.getMessage());
		} catch (ParserConfigurationException e) {
			System.out.println("Error configuring parser: " + e.getMessage());
		}

		return "Error: unable to find/parse the risks xml file";
	}

	public Properties getRiskName(String uri) {
		try {
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser saxParser = factory.newSAXParser();
			XMLReader reader = saxParser.getXMLReader();

			ContentHandler contentHandler = new OBRisksHandler_99_12();
			reader.setContentHandler( contentHandler );
			reader.parse(uri);

			return ((OBRisksHandler_99_12) contentHandler).getRiskNameObj();
		} catch (IOException e) {
			System.out.println("Error reading URI: " + e.getMessage());
		} catch (SAXException e) {
			System.out.println("Error in parsing: " + e.getMessage());
		} catch (ParserConfigurationException e) {
			System.out.println("Error configuring parser: " + e.getMessage());
		}

		return null;
	}

}
