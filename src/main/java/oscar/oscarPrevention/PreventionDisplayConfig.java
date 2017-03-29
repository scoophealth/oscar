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


package oscar.oscarPrevention;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.oscarehr.PMmodule.model.ProgramProvider;
import org.oscarehr.managers.PreventionManager;
import org.oscarehr.managers.ProgramManager2;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.OscarProperties;
import oscar.oscarDemographic.data.DemographicData;

public class PreventionDisplayConfig {
    private static Logger log = MiscUtils.getLogger();
    //private PreventionDisplayConfig preventionDisplayConfig = new PreventionDisplayConfig();
   
    private HashMap<String,HashMap<String,String>> prevHash = null;
    private ArrayList<HashMap<String,String>> prevList = null;

    private HashMap<String,Map<String,Object>> configHash = null;
    private ArrayList<Map<String,Object>> configList = null;

   private ProgramManager2 programManager2 = SpringUtils.getBean(ProgramManager2.class);
    
    private PreventionDisplayConfig() {
    	// use getInstance()
    }
    
    static public PreventionDisplayConfig getInstance(LoggedInInfo loggedInInfo){
     /// if (preventionDisplayConfig.prevList == null) {
     //    preventionDisplayConfig.loadPreventions(loggedInInfo);
    //   }
    //   return preventionDisplayConfig;
    	PreventionDisplayConfig pdc = new PreventionDisplayConfig();
    	pdc.loadPreventions(loggedInInfo);
    	return pdc;
    }

    public ArrayList<HashMap<String,String>> getPreventions(LoggedInInfo loggedInInfo) {
        if (prevList == null) {
            loadPreventions(loggedInInfo);
        }
        return prevList;
    }

    public HashMap<String,String> getPrevention(LoggedInInfo loggedInInfo, String s) {
        if (prevHash == null) {
            loadPreventions(loggedInInfo);
        }
        log.debug("getting " + s);
        return prevHash.get(s);
    }
    
	public void loadPreventions(LoggedInInfo loggedInInfo) {
		prevList = new ArrayList<HashMap<String, String>>();
		prevHash = new HashMap<String, HashMap<String, String>>();
		log.debug("STARTING2");

		InputStream is = null;
		try {
			if (OscarProperties.getInstance().getProperty("PREVENTION_ITEMS") != null) {
				String filename = OscarProperties.getInstance().getProperty("PREVENTION_ITEMS");
				if(filename.startsWith("classpath:")) {
					is = this.getClass().getClassLoader().getResourceAsStream(filename.substring(10));
				} else {
					is = new FileInputStream(filename);
				}
			}
			else {
				is = this.getClass().getClassLoader().getResourceAsStream("oscar/oscarPrevention/PreventionItems.xml");
			}


			SAXBuilder parser = new SAXBuilder();
			Document doc = parser.build(is);
			Element root = doc.getRootElement();
			List items = root.getChildren("item");
			Map<String,Boolean> shown = new HashMap<String,Boolean>();
			
			for (int i = 0; i < items.size(); i++) {
				Element e = (Element) items.get(i);
				List attr = e.getAttributes();
				HashMap<String, String> h = new HashMap<String, String>();
				for (int j = 0; j < attr.size(); j++) {
					Attribute att = (Attribute) attr.get(j);
					h.put(att.getName(), att.getValue());
				}
				

				if(!StringUtils.isEmpty(h.get("private"))) {
					String key = h.get("private");
					if(key != null) {
					
						String programs = OscarProperties.getInstance().getProperty(key);
						if(programs != null) {
							String[] programNos = programs.split(",");
							
							List<ProgramProvider> programProviders = programManager2.getProgramDomain(loggedInInfo,loggedInInfo.getLoggedInProviderNo());
							for(ProgramProvider programProvider:programProviders) {
								
								if(contains(programNos,String.valueOf(programProvider.getProgramId()))) {
									if(shown.get(h.get("name")) == null) {
										prevList.add(h);
										prevHash.put(h.get("name"), h);
										shown.put(h.get("name"), true);
									} 
								}
							}
						} else {
							log.warn("property " + programs + " should have a comma separated list of programNos");
						}
					} else {
						log.warn("prevention " + h.get("name") + " has an invalid private attribute. It should map to a property name");
					}
					
				} else {
					prevList.add(h);
					prevHash.put(h.get("name"), h);
				}
			}
		} catch (Exception e) {
			MiscUtils.getLogger().error("Error", e);
		} finally {
			try {
	            if (is != null) is.close();
            } catch (IOException e) {
	           log.error("Unexpected error", e);
            }
		}
	}

	
	private boolean contains(String[] arr, String val) {
		for(String a:arr) {
			if(val.equals(a)) {
				return true;
			}
		}
		return false;
	}
	
    public ArrayList<Map<String,Object>> getConfigurationSets(LoggedInInfo loggedInInfo) {
        log.debug("returning config sets");
        if (configList == null) {
            loadConfigurationSets(loggedInInfo);
        }
        log.debug("returning config sets" + configList);
        return configList;
    }

   
	public void loadConfigurationSets(LoggedInInfo loggedInInfo) {
		getPreventions(loggedInInfo);
		configHash = new HashMap<String, Map<String, Object>>();
		configList = new ArrayList<Map<String, Object>>();

		InputStream is = null;
		try {
			if (OscarProperties.getInstance().getProperty("PREVENTION_CONFIG_SETS") != null) {
				String filename = OscarProperties.getInstance().getProperty("PREVENTION_CONFIG_SETS");
				if(filename.startsWith("classpath:")) {
					is = this.getClass().getClassLoader().getResourceAsStream(filename.substring(10));
				} else {
					is = new FileInputStream(filename);
				}
			} else {
				is = this.getClass().getClassLoader().getResourceAsStream("oscar/oscarPrevention/PreventionConfigSets.xml");
			}

			SAXBuilder parser = new SAXBuilder();
			Document doc = parser.build(is);
			Element root = doc.getRootElement();
			List items = root.getChildren("set");
			for (int i = 0; i < items.size(); i++) {
				Element e = (Element) items.get(i);
				List attr = e.getAttributes();
				Map<String, Object> h = new HashMap<String, Object>();
				for (int j = 0; j < attr.size(); j++) {
					Attribute att = (Attribute) attr.get(j);
					if (att.getName().equals("prevList")) {
						h.put(att.getName(), att.getValue().split(","));
					} else {
						h.put(att.getName(), att.getValue());
					}

				}
				
				
				if(!StringUtils.isEmpty((String)h.get("private"))) {
					String key = (String)h.get("private");
					if(key != null) {
					
						String programs = OscarProperties.getInstance().getProperty(key);
						if(programs != null) {
							String[] programNos = programs.split(",");
							
							List<ProgramProvider> programProviders = programManager2.getProgramDomain(loggedInInfo,loggedInInfo.getLoggedInProviderNo());
							for(ProgramProvider programProvider:programProviders) {
								
								if(contains(programNos,String.valueOf(programProvider.getProgramId()))) {
									configList.add(h);
									configHash.put((String) h.get("title"), h);
								}
							}
						} else {
							log.warn("property " + programs + " should have a comma separated list of programNos");
						}
					} else {
						log.warn("prevention " + h.get("name") + " has an invalid private attribute. It should map to a property name");
					}
				} else {
					configList.add(h);
					configHash.put((String) h.get("title"), h);
				}
			}
		} catch (Exception e) {
			MiscUtils.getLogger().error("Error", e);
		} finally {
			try {
				if (is != null) is.close();
			} catch (IOException e) {
				log.error("Unexpected error", e);
			}
		}

	}


    public String getDisplay(LoggedInInfo loggedInInfo, Map<String,Object> setHash, String Demographic_no) {
        String display = "style=\"display:none;\"";
        DemographicData dData = new DemographicData();
        log.debug("demoage " + Demographic_no);
        org.oscarehr.common.model.Demographic demograph = dData.getDemographic(loggedInInfo, Demographic_no);
        try {
            String minAgeStr = (String) setHash.get("minAge");
            String maxAgeStr = (String) setHash.get("maxAge");
            String sex = (String) setHash.get("sex");
            int demoAge = demograph.getAgeInYears();
            String demoSex = demograph.getSex();
            boolean inAgeGroup = true;
            log.debug("min age " + minAgeStr + " max age " + maxAgeStr + " sex " + sex + " demoAge " + demoAge
                    + " demoSex " + demoSex);
            if (minAgeStr != null && maxAgeStr != null) { // between ages
                log.debug("HERE1");
                int minAge = Integer.parseInt(minAgeStr);
                int maxAge = Integer.parseInt(maxAgeStr);
                if (minAge <= demoAge && maxAge >= demoAge) {
                    display = "";
                } else {
                    inAgeGroup = false;
                }
            } else if (minAgeStr != null) { // older than
                log.debug("HERE2");
                int minAge = Integer.parseInt(minAgeStr);
                if (minAge <= demoAge) {
                    display = "";
                } else {
                    inAgeGroup = false;
                }
            } else if (maxAgeStr != null) { // younger than
                log.debug("HERE3");
                int maxAge = Integer.parseInt(maxAgeStr);
                if (maxAge >= demoAge) {
                    display = "";
                } else {
                    inAgeGroup = false;
                }
            }// else? neither defined should the default be to display it or
                // not?

            if (sex != null && inAgeGroup) {
                log.debug("HERE4");
                if (sex.equals(demoSex)) {
                    display = "";
                } else {
                    display = "style=\"display:none;\"";
                }
            }
        } catch (Exception e) {
            MiscUtils.getLogger().error("Error", e);
        }
        return display;
    }

    public boolean display(LoggedInInfo loggedInInfo, Map<String,String> setHash, String Demographic_no,int numberOfPrevs) {
        boolean display = false;
        PreventionManager preventionManager = SpringUtils.getBean(PreventionManager.class);
        DemographicData dData = new DemographicData();
        log.debug("demoage " + Demographic_no);       
        
        org.oscarehr.common.model.Demographic demograph = dData.getDemographic(loggedInInfo, Demographic_no);
        try {
        	
        	if(preventionManager.hideItem(setHash.get("name")) && numberOfPrevs ==0 ){
        		//move to hidden list
        		display=false;
        	}else{
        	
            String minAgeStr = setHash.get("minAge");
            String maxAgeStr = setHash.get("maxAge");
            String sex = setHash.get("sex");
            String minNumPrevs = setHash.get("showIfMinRecordNum");
            int demoAge = demograph.getAgeInYears();
            String demoSex = demograph.getSex();
            boolean inAgeGroup = true;
            //log.debug("min age " + minAgeStr + " max age " + maxAgeStr + " sex " + sex + " demoAge " + demoAge
            //        + " demoSex " + demoSex);
            
            if (minNumPrevs != null){
               int minNum = Integer.parseInt(minNumPrevs);
               if (numberOfPrevs >= minNum){
                  display = true;
               }
            }
            
            if(!display){
            
               if (minAgeStr != null && maxAgeStr != null) { // between ages
                   //log.debug("HERE1");
                   int minAge = Integer.parseInt(minAgeStr);
                   int maxAge = Integer.parseInt(maxAgeStr);
                   if (minAge <= demoAge && maxAge >= demoAge) {
                       display = true;
                   } else {
                       inAgeGroup = false;
                   }
               } else if (minAgeStr != null) { // older than
                   //log.debug("HERE2");
                   int minAge = Integer.parseInt(minAgeStr);
                   if (minAge <= demoAge) {
                       display = true;
                   } else {
                       inAgeGroup = false;
                   }
               } else if (maxAgeStr != null) { // younger than
                   //log.debug("HERE3");
                   int maxAge = Integer.parseInt(maxAgeStr);
                   if (maxAge >= demoAge) {
                       display = true;
                   } else {
                       inAgeGroup = false;
                   }
               }// else? neither defined should the default be to display it or
                   // not?
            
               if (sex != null && inAgeGroup) {
                   //log.debug("HERE4");
                   if (sex.equals(demoSex)) {
                       display = true;
                   } else {
                       display = false;
                   }
               }
            }
        	}//end check if prevention has been hidden
        } catch (Exception e) {
            MiscUtils.getLogger().error("Error", e);
        }
        return display;
    }
 }
