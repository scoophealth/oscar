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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Properties;

import org.oscarehr.util.MiscUtils;
import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class DesAntenatalPlannerChecklistHandler_99_12 extends DefaultHandler {

	private Locator locator;
	private String results=""; 
	private int count = 0;
	private boolean disprisk = false;
	private boolean dispitem = true;
	private boolean checkbox = false;
	private Properties savedar1params;
	
	GregorianCalendar cal, now;
	int[] weekDivisions = { 0, 12, 16, 20, 24, 28, 32, 34, 36, 37, 38, 39, 40 };
	String[] monthNames = { "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December" };

	public DesAntenatalPlannerChecklistHandler_99_12 (Properties newsavedparams) {
		savedar1params = newsavedparams;
	}

	public void setDocumentLocator(Locator locator) {
		this.locator = locator;
	}

	public void startDocument() throws SAXException {
		init();
		results += "<center><table BORDER=0 CELLSPACING=0 CELLPADDING=1 WIDTH='100%' BGCOLOR='#009966'>\n";
		results += "<tr><td width='5%' bgcolor='ivory' align='center'><b><font color='black'>Done</font></b></td>\n<td width='5%' bgcolor='ivory' align='center'><b><font color='black'>N/A</font></b></td>";
                results += "<td align='center'><b><font color='white'>Antenatal Checklist</font></b></td></tr></table></center>";
		results += "<center><table width='100%' border='0' cellpadding='0' CELLSPACING='0' BGCOLOR='ivory'><tr><td>\n";
	}

	public void endDocument() throws SAXException {

		results += "</td></tr></table></center>\n";
	}


        public void startElement(String namespaceURI, String localName, String rawName, Attributes atts) throws SAXException {
            if (rawName.equals("recommendations")) {
                results += "<center><table border=0 cellspacing=1 cellpadding=1 width=\"100%\">\n\n";
            }
            
            if (rawName.equals("week")) {
                for (int i=0; i < atts.getLength(); i++) {
                    if (atts.getQName(i) == "number") {
                        count = Integer.parseInt(atts.getValue(i));
                    }
                }
                cal.set(now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH));
                cal.add(Calendar.DATE, count*7);
                results += "<table border=0 cellspacing=1 cellpadding=1 width=\"100%\" datasrc='#xml_list'>\n";
                results += "<tr bgcolor='#CCFFCC'><td width='5%'></td><td width='5%'></td><td colspan='1'><span CLASS='.title'>\n";
                if (count == 0) {
                    results += "Initial Assessment - Week "+count;
                } else {
                    results += monthNames[cal.get(Calendar.MONTH)] + " " + cal.get(Calendar.DAY_OF_MONTH) + ", " + cal.get(Calendar.YEAR) +" - Week "+count;
                }
                
                results += "</span></td></tr>";
                
            }
            
            if (rawName.equals("item")) {
                checkbox = false;
                count = 3;
                String clname = "";
                String riskname = "";
                for (int i=0; i < atts.getLength(); i++) {
                    count = 1;
                    if (atts.getQName(i) == "name") clname = atts.getValue(i);
                    if (atts.getQName(i) == "risk") riskname = atts.getValue(i);
                    if (atts.getQName(i) == "checkbox") checkbox = true;
                }

                if(riskname.equals("") || savedar1params.getProperty(riskname)!=null ) {
                    results += "<tr>";
                    
                    if(checkbox) {
                        results += "<td width='5%' align='center'><input type='checkbox' name='checklist_" + clname +"d' value='checked' id='checklist_" +clname+ "d'></td><td width='5%' align='center'><input type='checkbox' name='checklist_" + clname +"na' value='checked' id='checklist_" +clname+ "na'></td>\n";
                    } else {
                        results += "<td></td><td></td>";
                    }
                    results += "<td colspan='" +count+ "'>";
                    if(savedar1params.getProperty(riskname)!=null) {
                        results += "<b>";
                        disprisk = true;
                    } else  disprisk = false;
                    dispitem = true;
                } else {
                    disprisk = false;
                    dispitem = false;
                }
                
            }
            
            if (rawName.equals("B") || rawName.equals("b")) {
                if(dispitem)
                    results += "<b>";
            }
            if (rawName.equals("I") || rawName.equals("i")) {
                if(dispitem)
                    results += "<i>";
            }
            if (rawName.equals("FONT") || rawName.equals("font")) {
                if(dispitem) {
                    results += "<font ";
                    for (int i=0; i < atts.getLength(); i++) {
                        results += atts.getQName(i) + "='" + atts.getValue(i) +"' ";
                    }
                    results += ">";
                }
            }
            if (rawName.equals("A") || rawName.equals("a")) {
                if(dispitem) {
                    results += "<a href=# onClick=\"popupPage(400,500,'";
                    for (int i=0; i < atts.getLength(); i++) {
                        results += atts.getValue(i) +"');return false;\">";
                    }
                }
            }
        }

	public void endElement(String namespaceURI, String localName, String rawName) throws SAXException {
            if (rawName.equals("recommendations")) {
                results += "</td></tr></table></center>\n";
            }
            if (rawName.equals("week")) {
                results += "</table>\n";
            }
            if (rawName.equals("item")) {
                if(disprisk) results += "</b>" ;
                if(dispitem) results += "</td></tr>\n";
                disprisk = true;
                dispitem = true;
            }
            if (rawName.equals("B") || rawName.equals("b")) {
                if(dispitem)			results += "</b>";
            }
            if (rawName.equals("I") || rawName.equals("i")) {
                if(dispitem)			results += "</i>";
            }
            if (rawName.equals("FONT") || rawName.equals("font")) {
                if(dispitem)			results += "</font>";
            }
            if (rawName.equals("A") || rawName.equals("a")) {
                if(dispitem)			results += "</a>";
            }
        }

	public void characters(char[] ch, int start, int length) throws SAXException {
            if(disprisk || dispitem) {
                String s = new String(ch, start, length);
                results += s;
            }
        }

	public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
		String s = new String(ch, start, length);

	}

	public void skippedEntity(String name) throws SAXException {
		MiscUtils.getLogger().debug("Skipping entity " + name);
	}

	public String getResults() {
		return results;
	}

	private void init() {
		cal = new GregorianCalendar();
		now = new GregorianCalendar();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		try {
			now.setTime(df.parse(savedar1params.getProperty("finalEDB")) ); 
		} catch (java.text.ParseException pe) {
			//ignore
		}
		now.add(Calendar.DATE, -280);
	}

}
