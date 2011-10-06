package oscar.oscarDemographic.pageUtil;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Properties;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.oscarehr.util.MiscUtils;

import oscar.util.StringUtils;

public class SpokenLangProperties extends Properties {
	private static SpokenLangProperties spokenLangProperties = new SpokenLangProperties();
	private static SortedSet<String> langSorted = new TreeSet<String>();
	private static HashMap<String,String> langToCode = new HashMap<String,String>();
	
	private static Logger logger = MiscUtils.getLogger();

	static {
		String propFile = "/spoken_languages_codes.properties";
		InputStream is = SpokenLangProperties.class.getResourceAsStream(propFile);
		if (is==null) try {
	        is = new FileInputStream(propFile);
        } catch (FileNotFoundException e) {
	        logger.error("Spoken Languages file not found!", e);
        }
		
		try {
	        spokenLangProperties.load(is);
        } catch (IOException e) {
	        logger.error("Error loading Spoken Lanugages!", e);
        }
		
		if (langSorted.isEmpty()) {
			Set<Entry<Object, Object>> eset = spokenLangProperties.entrySet();
			
			String lang;
			boolean preferred;
			for (Entry e : eset) {
				lang = StringUtils.noNull((String) e.getValue());
				preferred = lang.startsWith("^");
				
				if (preferred) {
					lang = lang.substring(1);
				} else if (langToCode.get(lang)!=null) {
					continue;
				}
				
				langToCode.put(lang, (String) e.getKey());
			}
			langSorted.addAll(langToCode.keySet());
		}
	}
	
	public static SpokenLangProperties getInstance() {
		return spokenLangProperties;
	}
	
	public String getCodeByLang(String lang) {
		return langToCode.get(lang);
	}
	
	public String getLangByCode(String code) {
    	if (StringUtils.empty(code)) return null;
    	
		String lang = StringUtils.noNull(spokenLangProperties.getProperty(code));
		if (lang.startsWith("^")) lang = lang.substring(1);
		
		return lang;
	}
	
	public SortedSet<String> getLangSorted() {
		return langSorted;
	}
}
