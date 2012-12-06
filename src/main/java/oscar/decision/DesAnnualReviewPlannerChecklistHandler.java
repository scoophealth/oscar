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


package oscar.decision;

import java.util.Enumeration;
import java.util.Properties;

import org.oscarehr.util.MiscUtils;
import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class DesAnnualReviewPlannerChecklistHandler extends DefaultHandler {

    private Locator locator;
    private String results = ""; //currentElement
    private boolean display = false;
    private String risks, sectionno;
    private Properties savedar1params;

    public DesAnnualReviewPlannerChecklistHandler(String savedltcrisks) {
        risks = savedltcrisks;
    }

    public DesAnnualReviewPlannerChecklistHandler(Properties savedltcrisks) {
        savedar1params = savedltcrisks;
        for (Enumeration e = savedar1params.propertyNames() ; e.hasMoreElements() ;) {
            MiscUtils.getLogger().debug("x :");
            MiscUtils.getLogger().debug("&& :" + e.nextElement());
        }
    }

    public void setDocumentLocator(Locator locator) {

        this.locator = locator;
    }

    public void startDocument() throws SAXException {
        results = "<table width='100%' border='0' cellpadding='0' cellspaceing='0' BGCOLOR='#009966'>";
        results += "<tr><td width='5%'><font color='yellow'><B>Done</B></font></td><td width='5%'><font color='yellow'><B>N/A</B></font></td><td align='center' width='90%' ><font size=-1 color='#FFFFFF'>Long Term Care Checklist Based on Presented Risk Factors</font></td></tr></table>";
        results += "<center><table width='100%' border='0' cellpadding='0' CELLSPACING='0' BGCOLOR='ivory' datasrc='#xml_list'><tr><td>\n";
        MiscUtils.getLogger().debug("savedar1params: " + savedar1params);
    }

    public void endDocument() throws SAXException {
        results += "</td></tr></table></center>\n";
    }

    public void processingInstruction(String target, String data) throws SAXException {
        MiscUtils.getLogger().debug("PI: Target: " + target + " and Data: " + data);
    }

    public void startElement(String namespaceURI, String localName, String rawName, Attributes atts)
            throws SAXException {
        MiscUtils.getLogger().debug("startElement: localName " + localName + " rawName " + rawName);
        String starttag, endtag, riskName, clName;

        if ((rawName.toLowerCase()).equals("section")) {
            sectionno = "";
            results += "<center><table width='100%' border='0' cellpadding='0' CELLSPACING='0' datasrc='#xml_list'>";
            for (int i = 0; i < atts.getLength(); i++) {
                if (atts.getQName(i).toLowerCase().equals("number"))
                    sectionno = atts.getValue(i);
            }
        }
        if (rawName.equals("section_title")) {
            results += "<tr BGCOLOR='#CCFFCC'><td colspan='3' align='center'>" + sectionno + ". ";
            display = true;
        }
        if (rawName.equals("risk")) {

            for (int i = 0; i < atts.getLength(); i++) {
                if (atts.getQName(i) == "riskname") { //not riskname
                    riskName = atts.getValue(i);
                    MiscUtils.getLogger().debug("Mapping ends for prefix :" + riskName);
                    
                    for (Enumeration e = savedar1params.propertyNames() ; e.hasMoreElements() ;) {
                        MiscUtils.getLogger().debug("x :");
                        MiscUtils.getLogger().debug("&& :" + e.nextElement());
                    }
                    
                    starttag = "<checklist_" + riskName + ">";
                    endtag = "</checklist_" + riskName + ">";
                    //		    SxmlMisc sxmlrisks=new SxmlMisc(); savedriskname =
                    // sxmlrisks.getXmlContent(risks,starttag,endtag); if (savedriskname != null &&
                    // savedriskname.compareTo("checked")==0){
                    if (savedar1params.getProperty(riskName) != null) {

                        display = true;
                        break;
                    }
                }
            }
        }
        if (rawName.equals("item") && display) {
            results += "<tr>";
            for (int i = 0; i < atts.getLength(); i++) {
                if (atts.getQName(i) == "clname") {
                    clName = atts.getValue(i);
                    results += "<td width='5%'>";
                    results += "<input type=checkbox name=\"checklist_" + clName
                            + "_done\" value='checked' id='checklist_" + clName + "_done'>";
                    results += "</td>";
                    results += "<td width='5%'>";
                    results += "<input type=checkbox name=\"checklist_" + clName
                            + "_na\" value='checked' id='checklist_" + clName + "_na'>";
                    results += "</td>";
                }
            }
            results += "<td width='90%'>";
        }

        if (rawName.equals("B") || rawName.equals("b")) {
            if (display)
                results += "<b>";
        }
        if (rawName.equals("I") || rawName.equals("i")) {
            if (display)
                results += "<i>";
        }
        if (rawName.equals("FONT") || rawName.equals("font")) {
            if (display) {
                results += "<font ";
                for (int i = 0; i < atts.getLength(); i++) {
                    results += atts.getQName(i) + "='" + atts.getValue(i) + "' ";
                }
                results += ">";
            }
        }
        if (rawName.equals("A") || rawName.equals("a")) {
            if (display) {
                results += "<a href=# onClick=\"popupPage(400,500,'";
                for (int i = 0; i < atts.getLength(); i++) {
                    results += atts.getValue(i) + "');return false;\">";
                } //end for
            } //end if
        }//end if

    }

    public void endElement(String namespaceURI, String localName, String rawName) throws SAXException {
        if (rawName.equals("section")) {
            results += "</table></center><br>\n";
        }
        if (rawName.equals("section_title")) {
            results += "</td></tr>\n";
            display = false;
        }
        if (rawName.equals("risk")) {
            display = false;
        }
        if (rawName.equals("item") && display) {
            results += "</td></tr>\n";
        }
        if (rawName.equals("B") || rawName.equals("b")) {
            if (display)
                results += "</b>";
        }
        if (rawName.equals("I") || rawName.equals("i")) {
            if (display)
                results += "</i>";
        }
        if (rawName.equals("FONT") || rawName.equals("font")) {
            if (display)
                results += "</font>";
        }
        if (rawName.equals("A") || rawName.equals("a")) {
            if (display) {
                results += "</a>";
            }
        }
    }

    public void characters(char[] ch, int start, int length) throws SAXException {
        String s = new String(ch, start, length);

        if (display) {
            results += s;
        }
    }

    public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
        new String(ch, start, length);

    }

    public void skippedEntity(String name) throws SAXException {
        MiscUtils.getLogger().debug("Skipping entity " + name);
    }

    public String getResults() {
        return results;
    }

}
