
import java.io.FileInputStream;
import java.util.Enumeration;
import java.util.Properties;
import com.google.api.translate.Language;
import com.google.api.translate.Translate;


/*
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
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
 * Jason Gallagher
 *
 * This software was written for the
 * Department of Family Medicine
 * McMaster University
 * Hamilton
 * Ontario, Canada
 */
import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
/**
 *
 * @author jaygallagher
 */
public class ResourceCompareTask {
    private File firstLanguage = null;
    private File compareLanguage = null;

    Properties loadPropertiesFile(File filePath) throws Exception{
        Properties props = new Properties();
        props.load(new FileInputStream(filePath));
        return props;
    }

    Properties loadPropertiesFile(String filePath) throws Exception{
        Properties props = new Properties();
        props.load(new FileInputStream(filePath));
        return props;
    }

    /**
     * @param args the command line arguments
     */
    public void execute() throws Exception {
        ResourceCompareTask rct = new ResourceCompareTask();
        
        Properties english = rct.loadPropertiesFile(firstLanguage);
        Properties french  = rct.loadPropertiesFile(compareLanguage);

        System.out.println(firstLanguage.getName()+" :"+ english.size() + "  "+compareLanguage.getName()+" :" + french.size());

        Enumeration en = english.keys();
        List<String> masterKeys = new ArrayList<String>();

        while (en.hasMoreElements()) {
            String s = (String) en.nextElement();
            if (!french.containsKey(s)) {
                masterKeys.add(s);
            }
        }
        Collections.sort(masterKeys);

        for (String s : masterKeys) {
            String translatedText = "NOT TRANSLATED";
            try {
                //Would still like to find all other exact phrases in english already mapped and use if it has a french version already
                translatedText = Translate.translate(english.getProperty(s), getLanguage(firstLanguage), getLanguage(compareLanguage));
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            PrintStream out = new PrintStream(System.out, true, "UTF-8");
            out.println("####-----------------------" + english.getProperty(s));
            out.println(s + "=" + translatedText);
        }
        if (masterKeys.size() > 0){
           throw new Exception(compareLanguage.getName()+ " is missing "+masterKeys.size()+" keys");
        }
    }

    private String getLanguage(File file){

        String filename = file.getName();
        String s = filename.substring(filename.indexOf("_")+1, filename.indexOf("."));

        if(s == null){
            return null;
        }else if (s.equals("en")){
            return Language.ENGLISH;
        }else if (s.equals("fr")){
            return Language.FRENCH;
        }else if (s.equals("pl")){
            return Language.POLISH;
        }else if (s.equals("es")){
            return Language.SPANISH;
        }else if (s.equals("pt_BR")){
            return Language.PORTUGESE;
        }
        return null;
    }

    static String getLanguage(String s){
        if(s == null){
            return null;
        }else if (s.equals("en")){
            return Language.ENGLISH;
        }else if (s.equals("fr")){
            return Language.FRENCH;
        }else if (s.equals("sp")){
            return Language.SPANISH;
        }

        return null;
    }


    public static void main(String[] argv) throws Exception {
        ResourceCompareTask rct = new ResourceCompareTask();
        rct.execute();
    }

    /**
     * @param firstLanguage the firstLanguage to set
     */
    public void setFirstLanguage(File firstLanguage) {
        this.firstLanguage = firstLanguage;
    }

    /**
     * @param compareLanguage the compareLanguage to set
     */
    public void setCompareLanguage(File compareLanguage) {
        this.compareLanguage = compareLanguage;
    }
}
