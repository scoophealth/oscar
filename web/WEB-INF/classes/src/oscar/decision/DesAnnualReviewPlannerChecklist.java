package oscar.decision;

import org.xml.sax.*;
import javax.xml.parsers.*;
import java.io.IOException;
import org.xml.sax.helpers.*;
import java.util.*;
import java.lang.*;

public class DesAnnualReviewPlannerChecklist {

	public String doStuff(String uri,String savedltcrisks) {

		try {
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser saxParser = factory.newSAXParser();
			ContentHandler contentHandler = new DesAnnualReviewPlannerChecklistHandler(savedltcrisks);
      saxParser.parse(uri, (DefaultHandler) contentHandler);

			return ((DesAnnualReviewPlannerChecklistHandler) contentHandler).getResults();
		} catch (IOException e) {
			System.out.println("Error reading URI: " + e.getMessage());
		} catch (SAXException e) {
			System.out.println("Error in parsing: " + e.getMessage());
		} catch (ParserConfigurationException e) {
			System.out.println("Error configuring parser: " + e.getMessage());
		}

		return "Error: unable to find/parse the risks xml file, CHECK if the format is correct";
	}

	public String doStuff(String uri,Properties savedltcrisks) {

		try {
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser saxParser = factory.newSAXParser();
			ContentHandler contentHandler = new DesAnnualReviewPlannerChecklistHandler(savedltcrisks);
      saxParser.parse(uri, (DefaultHandler) contentHandler);

			return ((DesAnnualReviewPlannerChecklistHandler) contentHandler).getResults();
		} catch (IOException e) {
			System.out.println("Error reading URI: " + e.getMessage());
		} catch (SAXException e) {
			System.out.println("Error in parsing: " + e.getMessage());
		} catch (ParserConfigurationException e) {
			System.out.println("Error configuring parser: " + e.getMessage());
		}

		return "Error: unable to find/parse the risks xml file, CHECK if the format is correct";
	}
}
