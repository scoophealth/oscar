package oscar.decision;

import org.xml.sax.*;
import javax.xml.parsers.*;
import java.io.IOException;
import org.xml.sax.helpers.*;
import java.util.*;

public class DesAnnualReviewPlannerChecklistHandler extends DefaultHandler {

	private Locator locator;
	private String results=""; //currentElement
	private int display = 0;
	private String risks,sectionno;
	private Properties savedar1params;
	
	public DesAnnualReviewPlannerChecklistHandler (String savedltcrisks) {
                 risks = savedltcrisks;
	}

	public DesAnnualReviewPlannerChecklistHandler (Properties savedltcrisks) {
                 savedar1params = savedltcrisks;
	}

	public void setDocumentLocator(Locator locator) {
		// System.out.println("    * setDocumentLocator() called");
		this.locator = locator;
	}

	public void startDocument() throws SAXException {
		results = "<table width='100%' border='0' cellpadding='0' cellspaceing='0' BGCOLOR='#009966'>";
                results += "<tr><td width='5%'><font color='yellow'><B>Done</B></font></td><td width='5%'><font color='yellow'><B>N/A</B></font></td><td align='center' width='90%' ><font size=-1 color='#FFFFFF'>Long Term Care Checklist Based on Presented Risk Factors</font></td></tr></table>";

		results += "<center><table width='100%' border='0' cellpadding='0' CELLSPACING='0' BGCOLOR='ivory' datasrc='#xml_list'><tr><td>\n";
	}

	public void endDocument() throws SAXException {
		results += "</td></tr></table></center>\n";
	}

	public void processingInstruction(String target, String data) throws SAXException {
		 System.out.println("PI: Target: " + target + " and Data: " + data);
	}

	public void startPrefixMapping(String prefix, String uri) {
		// System.out.println("Mapping starts for prefix " + prefix + " mapped to URI " + uri);
	}

	public void endPrefixMapping(String prefix) {
		// System.out.println("Mapping ends for prefix " + prefix);
	}

	public void startElement(String namespaceURI, String localName, String rawName, Attributes atts) throws SAXException {
		// System.out.println("startElement: " + localName);
                String starttag, endtag,savedriskname,riskName, clName;
		
		if ((localName.toLowerCase()).equals("section")) {
			sectionno="";
			results += "<center><table width='100%' border='0' cellpadding='0' CELLSPACING='0' datasrc='#xml_list'>";
			for (int i=0; i < atts.getLength(); i++) {
			 if ( atts.getLocalName(i).toLowerCase().equals("number") )
		    	 sectionno = atts.getValue(i);
			}
		}
                if (localName.equals("section_title")){
		      	results += "<tr BGCOLOR='#CCFFCC'><td colspan='3' align='center'>"+sectionno+". "; 
                        display = 1;
		}
		if (localName.equals("risk")) {
                
                 for (int i=0; i < atts.getLength(); i++) {
                  if (atts.getLocalName(i) == "riskno") { //not riskname
                    riskName = atts.getValue(i); 
                    //		 System.out.println("Mapping ends for prefix " + riskName);
                    starttag = "<checklist_"+riskName+">";
                    endtag = "</checklist_"+riskName+">";
//		    SxmlMisc sxmlrisks=new SxmlMisc(); savedriskname = sxmlrisks.getXmlContent(risks,starttag,endtag); if (savedriskname != null && savedriskname.compareTo("checked")==0){
                    if (savedar1params.getProperty(riskName) != null){
				 display = 1;
				 break;
                                }
                        }
	          }		
		}
                if (localName.equals("item") && display == 1) {
		 results +="<tr>";
                 for (int i=0; i < atts.getLength(); i++) {
                  if (atts.getLocalName(i) == "clname") {
                    clName = atts.getValue(i);
		    results += "<td width='5%'>";
               	    results += "<input type=checkbox name=\"checklist_" + clName + "_done\" value='checked' datafld='checklist_" +clName+ "_done'>";
		    results += "</td>";
		    results += "<td width='5%'>";	
		    results += "<input type=checkbox name=\"checklist_" + clName + "_na\" value='checked' datafld='checklist_" +clName+ "_na'>"; 
		    results += "</td>";
                  }
                 }
		 results += "<td width='90%'>";
	       }

                if (localName.equals("B") || localName.equals("b")) {
                        if(display == 1)
                        results += "<b>";
                }
                if (localName.equals("I") || localName.equals("i")) {
                        if(display == 1)
                        results += "<i>";
                }
                if (localName.equals("FONT") || localName.equals("font")) {
                        if(display == 1) {
                        results += "<font ";
                        for (int i=0; i < atts.getLength(); i++) {
                         results += atts.getLocalName(i) + "='" + atts.getValue(i) +"' ";
                	}
                        results += ">";
                        }
                }
                if (localName.equals("A") || localName.equals("a")) {
                        if(display == 1) {
                        results += "<a href=# onClick=\"popupPage(400,500,'";
                	for (int i=0; i < atts.getLength(); i++) {
                         results += atts.getValue(i) +"');return false;\">";
         		} //end for
                	} //end if
                }//end if
	
	}

	public void endElement(String namespaceURI, String localName, String rawName) throws SAXException {
		if (localName.equals("section")) {
			results +=  "</table></center><br>\n"; }
                if (localName.equals("section_title")) {
                        results += "</td></tr>\n"; 
                        display = 0; }
		if (localName.equals("risk")) {
			display = 0; }
		if (localName.equals("item")&& display==1 ) {
			results += "</td></tr>\n"; }
                
                if (localName.equals("B") || localName.equals("b")) {
                        if(display == 1)
                        results += "</b>";
                }
                if (localName.equals("I") || localName.equals("i")) {
                        if(display == 1)
                        results += "</i>";
                }
                if (localName.equals("FONT") || localName.equals("font")) {
                        if(display == 1) 
                        results += "</font>";
                }

                if (localName.equals("A") || localName.equals("a")) {
                        if(display == 1) {
                        results += "</a>";
                        } 
                }
	}

	public void characters(char[] ch, int start, int length) throws SAXException {
		String s = new String(ch, start, length);
		// System.out.println("characters: " + s);
                if (display == 1){
		  results += s;
                }
	}

	public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
		String s = new String(ch, start, length);
		// System.out.println("ignorableWhiteSpace: [" + s + "]");
	}

	public void skippedEntity(String name) throws SAXException {
		System.out.println("Skipping entity " + name);
	}

	public String getResults() {
		return results;
	}

}

