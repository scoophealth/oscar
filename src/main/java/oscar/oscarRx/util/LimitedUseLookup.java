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

package oscar.oscarRx.util;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.filter.ElementFilter;
import org.jdom.input.SAXBuilder;
import org.oscarehr.common.dao.ResourceStorageDao;
import org.oscarehr.common.model.ResourceStorage;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.OscarProperties;


/**
 * Parses xml file, creating DosingRecomendation Objects storing them in a hashtable with the ATC code as the key
 * @author jay
 */
public class LimitedUseLookup {

	private static Logger log = MiscUtils.getLogger();

	static Hashtable<String, ArrayList<LimitedUseCode>> luLookup = new Hashtable<String, ArrayList<LimitedUseCode>>();
	static boolean loaded = false;

	/** Creates a new instance of RenalDosingFactory */
	protected LimitedUseLookup() {
	}

	static public ArrayList<LimitedUseCode> getLUInfoForDin(String din) {
		loadLULookupInformation();
		if (din == null) {
			return null;
		}
		return luLookup.get(din);
	}

	static public LimitedUseCode makeLUNote(Element e) {
		LimitedUseCode lu = new LimitedUseCode();
		lu.setSeq(getVal(e, "seq"));
		lu.setUseId(getVal(e, "reasonForUseId"));
		lu.setType(getVal(e, "type"));
		lu.setTxt(e.getText());
		return lu;
	}

	static public String getVal(Element e, String name) {
		if (e.getAttribute(name) != null) {
			return e.getAttribute(name).getValue();
		}
		return "";
	}
	static public void reLoadLookupInformation(){
		loaded = false;
		loadLULookupInformation();
	}

	static private void loadLULookupInformation() {
		log.debug("current LU lookup size " + luLookup.size());
		if (!loaded) {
			LimitedUseLookup rdf = new LimitedUseLookup();
			InputStream is = null;
			ResourceStorageDao resourceStorageDao = SpringUtils.getBean(ResourceStorageDao.class);
			try {

				String fileName = OscarProperties.getInstance().getProperty("odb_formulary_file");
				if (fileName != null && !fileName.isEmpty()) {
					is = new BufferedInputStream(new FileInputStream(fileName));
					log.info("loading odb file from property "+fileName);

				} else {
					ResourceStorage resourceStorage = resourceStorageDao.findActive(ResourceStorage.LU_CODES);
		        	if(resourceStorage != null){
		        		is = new ByteArrayInputStream(resourceStorage.getFileContents());
		        		log.info("loading odb file from resource storage id"+resourceStorage.getId());
		        	}else{
						String dosing = "oscar/oscarRx/data_extract_20161124.xml";
						log.info("loading odb file from internal resource "+dosing);
						is = rdf.getClass().getClassLoader().getResourceAsStream(dosing);
		        	}
				}

				SAXBuilder parser = new SAXBuilder();
				Document doc = parser.build(is);
				Element root = doc.getRootElement();
				Element formulary = root.getChild("formulary");
				@SuppressWarnings("unchecked")
				Iterator<Element> items = formulary.getDescendants(new ElementFilter("pcgGroup"));

				while (items.hasNext()) {
					Element pcgGroup = items.next();
					@SuppressWarnings("unchecked")
					List<Element> lccNoteList = pcgGroup.getChildren("lccNote");

					if (lccNoteList.size() > 0) {
						ArrayList<LimitedUseCode> luList = new ArrayList<LimitedUseCode>();
						for (Element lccNo : lccNoteList) {
							luList.add(makeLUNote(lccNo));
						}

						@SuppressWarnings("unchecked")
						Iterator<Element> drugs = pcgGroup.getDescendants(new ElementFilter("drug"));
						while (drugs.hasNext()) {
							Element drug = drugs.next();
							String din = drug.getAttribute("id").getValue();
							luLookup.put(din, luList);
						}
					}
				}

				loaded = true;
			} catch (Exception e) {
				MiscUtils.getLogger().error("Error", e);
			} finally {
				if(is != null) {
					try {
						is.close();
					}catch(IOException e) {
						MiscUtils.getLogger().error("Error", e);
					}
				}
			}
		}

	}
}
