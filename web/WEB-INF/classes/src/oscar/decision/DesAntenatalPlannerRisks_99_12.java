package oscar.decision;

import org.xml.sax.*;
import javax.xml.parsers.*;
import java.io.IOException;
import org.xml.sax.helpers.*;
import java.util.*;

public class DesAntenatalPlannerRisks_99_12 {

	public String doStuff(String uri) {
		try {
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser saxParser = factory.newSAXParser();
			XMLReader reader = saxParser.getXMLReader();

			ContentHandler contentHandler = new DesAntenatalPlannerRisksHandler_99_12();
			reader.setContentHandler( contentHandler );
			reader.parse(uri);
			return ( ((DesAntenatalPlannerRisksHandler_99_12) contentHandler).getResults());

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

			ContentHandler contentHandler = new DesAntenatalPlannerRisksHandler_99_12();
			reader.setContentHandler( contentHandler );
			reader.parse(uri);
			return ((DesAntenatalPlannerRisksHandler_99_12) contentHandler).getRiskNameObj();

		} catch (IOException e) {
			System.out.println("Error reading URI: " + e.getMessage());
		} catch (SAXException e) {
			System.out.println("Error in parsing: " + e.getMessage());
		} catch (ParserConfigurationException e) {
			System.out.println("Error configuring parser: " + e.getMessage());
		}

		return null;
	}

  public static void main(String args[]) { 
  	DesAntenatalPlannerRisks_99_12 aE = new DesAntenatalPlannerRisks_99_12();
    System.out.print(aE.doStuff("desantenatalplannerrisks_99_12.xml"));
  }// End of main 

}
