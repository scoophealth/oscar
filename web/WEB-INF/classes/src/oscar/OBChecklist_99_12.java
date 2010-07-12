package oscar;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Properties;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.oscarehr.util.MiscUtils;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

public class OBChecklist_99_12 {

	public String doStuff(String uri, Properties savedar1params) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
		try {
			df.parse(savedar1params.getProperty("finalEDB")); 
		} catch (java.text.ParseException pe) {
		  return "Error: final EDB";
		}

		try {
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser saxParser = factory.newSAXParser();
			XMLReader reader = saxParser.getXMLReader();

			ContentHandler contentHandler = new OBChecklistHandler_99_12(savedar1params);
			reader.setContentHandler( contentHandler );
			reader.parse(uri);

			return ((OBChecklistHandler_99_12) contentHandler).getResults();

		} catch (IOException e) {
			MiscUtils.getLogger().debug("Error reading URI: " + e.getMessage());
		} catch (SAXException e) {
			MiscUtils.getLogger().debug("Error in parsing: " + e.getMessage());
		} catch (ParserConfigurationException e) {
			MiscUtils.getLogger().debug("Error configuring parser: " + e.getMessage());
		}

		return "Error: unable to parse the checklist xml file";
  }

}
