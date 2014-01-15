/**
 * Copyright (c) 2013. Department of Family Practice, University of British Columbia. All Rights Reserved.
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
 * Department of Family Practice
 * Faculty of Medicine
 * University of British Columbia
 * Vancouver, Canada
 */


package org.oscarehr.medextract;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.oscarehr.common.dao.DrugDao;
import org.oscarehr.common.model.Drug;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.oscarRx.util.RxDrugRef;
import edu.uwm.jiaoduan.i2b2.utils.LancetParser;

/**
 *
 * @author sdiemert
 * @date July-August 2013
 */
public class MedicationExtractor{
		private static Logger logger=MiscUtils.getLogger();
        private String text;
        private String list_text;
        private ArrayList<String> medList;
        private ArrayList<String> medListVerbose;
        private ArrayList<HashMap<String,String>> medMap;
        private String demographicId;
        private List<Drug> currentPrescriptions;
        private RxDrugRef drugref = new RxDrugRef();
        private ArrayList<DrugPair> pairs = null;
        private String webRoot = null;

        //Data model access.
        protected static DrugDao drugDao = SpringUtils.getBean(DrugDao.class);


        public class StringIntPair{
            private String s;
            private int i;
            public StringIntPair(String s, int i){
                this.s = s;
                this.i = i;
            }
            public int getInt(){
                return i;
            }
            public String getStr(){
                return s;
            }
        }

        public class DrugPair{
            private Drug currentDrug;
            private HashMap<String, String> newDrug;

            public DrugPair(Drug c, HashMap<String, String> n){
                this.currentDrug = c;
                this.newDrug = n;
            }

            public Drug getCurrentDrug(){
                return currentDrug;
            }
            public HashMap<String, String> getNewDrug(){
                return newDrug;
            }
        }

        /**
         * Constructor - calls methods in the class to identify medications in the 
         * text of the d/c summary.
         * @param text
         * @param demographicId 
         */
		public MedicationExtractor(String text, String demographicId, String webRoot){

            this.webRoot = webRoot;
            //get the patients prescription info through the database layer based on the demographicId.
            try{
                if(demographicId != null){
                    this.demographicId = String.valueOf(demographicId);
                }
                logger.info("demo id (in MedicationExtractor)"+this.demographicId);
            }catch(NullPointerException npe){
                logger.error(npe.toString());
            }

            try{
                this.currentPrescriptions = drugDao.getUniquePrescriptions(demographicId);
            }catch(NullPointerException npe){
                logger.error("Error was from the drugDao");
                logger.error(npe.toString());

            }
            //parse the content of d/c summary.
            try{
                this.text = text.replace("@@@@","\n");
                this.list_text = this.getListText();
                this.getMedsFromLancet(this.list_text);
                this.medMap = this.parseLancetOutput();
                this.getDataForMeds();
                pairs = this.reconcilMedications(this.medMap, this.currentPrescriptions);
            }catch(InterruptedException inte){
                logger.error(inte.toString());
            }catch(IOException ioe){
                logger.error(ioe.toString());
            }catch(Exception e){
                logger.error(e.toString());
            }
            logger.info(this.medMap.toString());
            logger.info(this.pairs.toString());
        }

        public ArrayList<DrugPair> reconcilMedications(ArrayList<HashMap<String, String>> map, List<Drug> drugs){
            ArrayList<DrugPair> return_list = new ArrayList<DrugPair>();
            //compare based on ATCs
            Drug d = null;
            HashMap<String, String> hm = null;
            for(Iterator<Drug> it = drugs.iterator(); it.hasNext();){
                d = it.next();
                for(Iterator<HashMap<String,String>> hmit = map.iterator(); hmit.hasNext();){
                    hm = hmit.next();
                    if(d.getAtc().equals(hm.get("atc"))){
                        //found matching atcs
                        return_list.add(new DrugPair(d,hm));
                        it.remove();
                        hmit.remove();
                    }
                }
                //if nothing was found, add the drug seperately with no new drug
                //this will map to the case where it is a current prescription
                return_list.add(new DrugPair(d, null));
            }
            //iterate over the reamining drugs and add them as pairs without current drugs
            //these will correspond to the case where the drug is not in the EMR.
            for(HashMap<String, String> m : map){
                return_list.add(new DrugPair(null, m));
            }
            return return_list;
        }


        public ArrayList<DrugPair> getPairs(){
            return this.pairs;
        }

        /**
         * @return a String that is the text that was parsed. 
         */
        public String getText(){
              return this.text;
        }

        /**
        * Returns the text with the new lines replaced with <br> tags for html.
        * 
        * @return String
        */
        public String getTextForHTML(){
            String return_string =  this.text.replace("\\n","<br>");
            return_string = return_string.replace("\n", "<br>");
            logger.info("getTextForHTML() returning:  "+return_string);
            return return_string;
        }

        /**
         * 
         * @return  An ArrayList of HashMaps (associative arrays) with all the 
         * data for each medication found in the text.
         */
        public ArrayList<HashMap<String,String>> getMedMap(){
            return this.medMap;
        }

        /**
         * 
         * @return the portion of the text that was found to be a list.
         * @throws IOException
         * @throws InterruptedException 
         */
        private String getListText() throws IOException, InterruptedException{
           logger.info("in getListText()");


           //check if tmp/medications is created, if it isn't create it.
           File tmpDir = new File("/tmp/medications");
           if(!tmpDir.exists()){
               tmpDir.setReadable(true);
               tmpDir.setWritable(true);
               tmpDir.setExecutable(true);
               tmpDir.mkdir();
               logger.info("Created new 777 temp foler under /tmp/medications");
           }
           
           //Create a new file in the tmp dir to act as input to the python script.
           File fin = new File("/tmp/medications/input");
           if(!fin.exists()){
               logger.info("Before creating input file");
               fin.createNewFile();
               logger.info("after creating input file");
           }

           FileWriter fw = new FileWriter(fin.getAbsoluteFile());
           BufferedWriter bw = new BufferedWriter(fw);

           //write the text to this file.
           if(this.text != null || this.text.isEmpty()){
               bw.write(this.text);
           }
           else{
               bw.write("");
           }
           bw.close();
           logger.info("Completed writing to file in getListText()"); 
           
           
           //Create a new process for the python script
           logger.info("WEBROOT IS: "+webRoot);
           ProcessBuilder pb = new ProcessBuilder("python","/var/lib/tomcat6/webapps"+webRoot+"/WEB-INF/classes/MedRecPython/src/driver.py");
           logger.info("Executed python script");
           Process p = pb.start();


           //Setup the error logging for the python script.
           InputStream in = p.getInputStream();
           InputStream es = p.getErrorStream();
           InputStreamReader isr = new InputStreamReader(in); 
           InputStreamReader esr = new InputStreamReader(es); 
           BufferedReader b = new BufferedReader(isr);
           BufferedReader be = new BufferedReader(esr);

           //print the error logging for hte pyhton script
           logger.info("PYTHON OUTPUT");
           String line = null;
           String e_line = null;
           while ( (line = b.readLine()) != null){
                logger.info(line);
           }
           logger.info("COMPLETE PYTHON OUTPUT");
           logger.info("PYTHON ERROR");
           while ( (e_line = be.readLine()) != null){
                logger.info(e_line);
           }
           logger.info("COMPLETE PYTHON ERROR");

           p.waitFor();
           logger.info("EXIT VALUE: "+p.exitValue());

           //remove the input file as it is no longer needed.
           fin.delete();
           
           //Open and read the output file from the pyhton script.
           File f = new File("/tmp/medications/output");
           Scanner sc = new Scanner(f); 

           String list_string = "";

           while(sc.hasNext()){
               list_string += sc.nextLine()+"\n";
           }
           logger.info(list_string);

           f.delete(); //delete the file when we are done with it.
           logger.info("Completed getListText()");
           return list_string; //return the string that is a list.
        }


        /**
         * Sets the listMeds array in this object.
         * @param s - The string of text for the Lancet parser to parse. 
         */
        private void getMedsFromLancet(String s){
            logger.info("getMedsFromLancet()");
            logger.info("input was:"+s+":end of text");
            try
            {
                ArrayList<String> listMeds;
                ArrayList<String> listMedsVerbose;
                LancetParser lancet = new LancetParser(s);
                logger.info("LancetParser lancet = new LancetParser(s);");
                lancet.summary();
                logger.info("lancet.summary();");
                if(!s.equals("") && s != null && !s.isEmpty()){
                    listMedsVerbose = lancet.GetDrugList();
                    listMeds = lancet.drugsToi2b2();
                }else{
                    listMeds = new ArrayList<String>();
                    listMedsVerbose = new ArrayList<String>();
                }
                logger.info(" ArrayList listMeds = lancet.drugsToi2b2();");
                if(listMeds != null){
                    logger.info("LIST OF MEDS FROM LANCET:  "+listMeds.toString());
                }else{
                    logger.info("List of meds is empty");
                }
                logger.info("end getMedsFromLancet()");
                logger.info("list of meds: "+listMeds.toString());
                this.medListVerbose = listMedsVerbose;
                this.medList = listMeds;
            }
            catch(Exception e)
            {
                logger.info(e.toString());
            }
            logger.info("end getMedsFromLancet(), returning null");
        }

        /**
         * Converts the Lancet output from a list of strings to a list of associative arrays
         *
         * @return ArrayList of HashMaps that give the details of each medication found.
         */
        private ArrayList<HashMap<String, String>> parseLancetOutput(){
            logger.info("parseLancetOutput()");
            ArrayList<HashMap<String,String>> return_list = new ArrayList<HashMap<String,String>>();
            String[] split_s;
            HashMap<String,String> hm;
            Pattern p = Pattern.compile(".*\"(.*)\".*");
            String value;
            for(String s: this.medList){
               logger.info("Parsing: "+s);
               split_s  = s.split("\\|\\|");  
               hm = new HashMap<String,String>();
               logger.info("split_s: "+Arrays.toString(split_s));
               for(int i=0; i<split_s.length; i++){
                    String[] split_sub_s = split_s[i].split("=");
                    logger.info("split string: "+split_s[i]+" into: "+Arrays.toString(split_sub_s));
                    logger.info("Running regex matches on: "+split_sub_s[1]);
                    Matcher matcher = p.matcher(split_sub_s[1]);
                    if(matcher.matches()){
                        value = matcher.group(1);
                        logger.info("Found match for: "+split_sub_s[0]+"="+value);
                        if(value.equals("nm")){
                            value = null;   
                        }if(split_sub_s[0].equals("m")){
                            hm.put("name", value);
                        }else if(split_sub_s[0].equals("do")){
                            hm.put("dosage", value);
                        }else if(split_sub_s[0].equals("mo")){
                            hm.put("route", value);
                        }else if(split_sub_s[0].equals("f")){
                            hm.put("freq", value);
                        }else if(split_sub_s[0].equals("du")){
                            hm.put("duration", value);
                        }else if(split_sub_s[0].equals("r")){
                            hm.put("reason", value);
                        }else if(split_sub_s[0].equals("ln")){
                            hm.put("context", value);
                        }else{
                            logger.info("Unable to find tag match for: "+split_sub_s[0]+"="+value);
                        }
                    }
                }
                return_list.add(hm);
            }
            logger.info("End of parseLancetOutput, returning: "+return_list.toString());
            return return_list;
        }

        private void getDataForMeds(){
            HashMap<String, String> temp = null;
            for(int i =0; i<this.medMap.size(); i++){
                 temp = getMedData(this.medMap.get(i)); //should alter the address
                 this.medMap.set(i, temp);
            }
        }

        private HashMap<String, String> getMedData(HashMap<String, String> map){
           String s = map.get("name");
           logger.info("in getMedData() searching for data for: '"+s+"'");
           HashMap<String, String> dataMap = new HashMap<String, String>();
           ArrayList<HashMap<String,String>> working_list = new ArrayList<HashMap<String, String>>();
           Vector<Hashtable<String,String>> vec;
           try{
               vec = this.drugref.list_drug_element3(s,true);
               //logger.info("Webservice returned with: " + vec.toString());
               //The webservice returns things as Hashtables, convert to HashMaps
               //and make another webservice call to get more info.
               for(Hashtable ht :vec){
                    HashMap<String,String> hm = new HashMap<String, String>();
                    Iterator<Hashtable<String,String>> it = ht.entrySet().iterator();
                    while(it.hasNext()){
                        Map.Entry pairs = (Map.Entry)it.next();
                        //logger.info("(key, value) = ("+pairs.getKey().toString()+", "+pairs.getValue().toString()+")");
                        hm.put(pairs.getKey().toString(), pairs.getValue().toString());
                    }
                    logger.info("ID IS HERE: "+hm.get("id"));
                    if(!hm.get("id").equals("0")){
                        Hashtable<String, String> drug2table = this.drugref.getDrug2(hm.get("id"),true);
                        //logger.info("getDrug2() returned with: "+drug2table.toString());
                        Iterator<Map.Entry<String,String>> it2 = drug2table.entrySet().iterator();
                        while(it2.hasNext()){
                            Map.Entry pairs = (Map.Entry)it2.next();
                            if(pairs.getKey().toString().equals("name")){
                                hm.put("ingredient_name", pairs.getValue().toString());
                            }else if(pairs.getKey().toString().equals("product")){
                                continue;
                            }else if(pairs.getKey().toString().equals("components")){
                                //TODO: Implement this to deal with multiple ingredients
                                continue;
                            }
                            else{
                                hm.put(pairs.getKey().toString(), pairs.getValue().toString());
                            }
                        }
                        //logger.info("INFO: Medication is now represented by: "+hm.toString());
                        working_list.add(hm);
                    }
               }
           }catch(Exception e){
               logger.info("caught expection in getMedData(String): "+e.toString());
           }
           dataMap = findBestMedicationMatch(working_list, map);
           return dataMap;
        }

        private HashMap<String, String> findBestMedicationMatch(ArrayList<HashMap<String,String>> inList,
                                                                    HashMap<String, String> map){
            //logger.info("in findBestMedicationMatch(), with inList: "+inList.toString()+" and map: "+map.toString());
            if(inList.size() < 1){
                return map;
            }

            StringIntPair commonATC = findMostCommon(inList, "atc");

            HashMap<String, String> hm = null;
            //check to see if there was a reasonable number of matches, or just 1.
            //if there was just one we will not use ATC to find a the best medication match.
            if(commonATC.getInt() > 1){
                //Narrow down list so that it doesn't contain meds with ATCs that aren't the most common.
                //for(HashMap<String, String> hm : inList){
                for(Iterator<HashMap<String,String>> it = inList.iterator(); it.hasNext();){
                    hm = it.next();
                    if(hm.get("atc").equals(commonATC.getStr())){
                        //do nothing
                    }else{
                        it.remove();
                    }
                }
            }

            //check to see if the dosage for the med is null, if it isn't we will use
            //it to find a better match.
            if(map.get("dosage") != null && !map.get("dosage").equals("null")){
                for(Iterator<HashMap<String,String>> it = inList.iterator(); it.hasNext();){
                    hm = it.next();
                    //use regexs to see if the dosage is listed in the name of the medication
                    Pattern p = Pattern.compile(map.get("dosage").toUpperCase().replace(" ",""));
                    Matcher m = p.matcher(hm.get("name"));
                    if(m.find()){
                        logger.info("Found match for "+map.get("dosage")+" in: "+hm.get("name"));
                        //do nothing leave it in the list.
                    }else{
                        logger.info("Could not find match for: " + map.get("dosage") + " in: " + hm.get("name"));
                        it.remove();
                    }
                }
            }
            //add more checks here maybe, otherwise return the first item in the list.

            if(inList.size()<1){
                logger.info("could not find a match in the database in findBestMedicationMatch(), returning with:"+map);
                return this.standardizeMappingFieldsFromLancet(map);
            }else{
                try{
                    HashMap<String ,String> return_map = this.combineDrugRefWithLancet(inList.get(0), map);
                    logger.info("returning from findBestMedicationMatch() with:"+return_map.toString());
                    return return_map;
                }catch (Exception e){
                    logger.error(e.toString());
                    logger.info(e);
                    /*StringWriter sw = new StringWriter();
                    e.print StackTrace(new PrintWriter(sw));
                    logger.info(sw.toString());*/
                    return inList.get(0);
                }
            }

        }

    /**
     * Combines the results from both the lancet and drugref values, takes the drugref data over the lancet data.
     * @param drugRefMap
     * @param lancetMap
     * @return the medication with the data combined from the lancet med.  in the form:
      {duration=value, route=value, freq=value, name=value, dose=value, atc=value, din=value, form=value, ingredient=value, dbid=value}
     */
    private HashMap<String, String> combineDrugRefWithLancet(HashMap<String, String> drugRefMap, HashMap<String, String> lancetMap){
        logger.info("in combineDrugRefWithLancet()");
        logger.info("--------------------------------");
        HashMap<String, String> return_hm = new HashMap<String, String>();

        if(drugRefMap.containsKey("name") && !drugRefMap.get("name").equals("")){
           return_hm.put("name", drugRefMap.get("name"));
        }else if(lancetMap.containsKey("name") && !lancetMap.get("name").equals("")){
            return_hm.put("name", lancetMap.get("name"));
        }else{
            return_hm.put("name", null);
        }
        logger.info("name: "+return_hm.get("name"));

        if(drugRefMap.containsKey("ingredient_name") && drugRefMap.get("ingredient_name") != null && !drugRefMap.get("ingredient_name").equals("")){
           return_hm.put("ingredient", drugRefMap.get("ingredient_name"));
        }else{
           return_hm.put("ingredient", null);
        }
        logger.info("ingredient: "+return_hm.get("ingredient"));


        if(drugRefMap.containsKey("atc") && drugRefMap.get("atc") != null && !drugRefMap.get("atc").equals("")){
            return_hm.put("atc", drugRefMap.get("atc"));
        }else{
            return_hm.put("atc",null);
        }
        logger.info("atc: "+return_hm.get("atc"));

        if(drugRefMap.containsKey("regional_identifier") && drugRefMap.get("regional_identifier") != null && !drugRefMap.get("regional_identifier").equals("")){
            return_hm.put("din",drugRefMap.get("regional_identifier"));
        }else{
            return_hm.put("din",null);
        }
        logger.info("din: "+return_hm.get("din"));

        if(drugRefMap.containsKey("drugForm") && !drugRefMap.get("drugForm").equals("") && drugRefMap.get("drugForm") != null){
            return_hm.put("form",drugRefMap.get("drugForm").toLowerCase());
        }else{
            return_hm.put("form",null);
        }
        logger.info("form: "+return_hm.get("form"));

        if(drugRefMap.containsKey("drugRoute") || lancetMap.containsKey("route")){
            if(drugRefMap.containsKey("drugRoute") && drugRefMap.get("drugRoute") != null && !drugRefMap.get("drugRoute").equals("")){
                if(drugRefMap.get("drugRoute").contains("]") && drugRefMap.get("drugRoute").contains("[")){
                   logger.info("found match for [ --- ] in "+drugRefMap.get("drugRoute"));
                   drugRefMap.put("drugRoute", drugRefMap.get("drugRoute").replace("[", ""));
                   drugRefMap.put("drugRoute", drugRefMap.get("drugRoute").replace("]", ""));
                }
                Pattern p = Pattern.compile("[Pp]\\.?[Oo]\\.?");
                Matcher matcher = p.matcher(drugRefMap.get("drugRoute"));
                if(matcher.find()){
                    logger.info("found match for PO (or variant) in "+drugRefMap.get("drugRoute"));
                    drugRefMap.put("drugRoute", "oral");
                }
                return_hm.put("route", drugRefMap.get("drugRoute").toLowerCase());
            }else if(lancetMap.containsKey("route") && lancetMap.get("route") != null && !lancetMap.get("route").equals("")){
                Pattern p = Pattern.compile("[Pp]\\.?[Oo]\\.?");
                Matcher matcher = p.matcher(lancetMap.get("route"));
                if(matcher.find()){
                    logger.info("found match for PO (or variant) in "+lancetMap.get("route"));
                    lancetMap.put("route", "oral");
                }
                return_hm.put("route",lancetMap.get("route"));
            }else{
                logger.info("PUTTING NULL INTO ROUTE");
                return_hm.put("route", null);
            }
        }
        logger.info("route: "+return_hm.get("route"));

        if(lancetMap.containsKey("freq") && lancetMap.get("freq") != null  && !lancetMap.get("freq").equals("")){
            logger.info("FREQ IS: "+lancetMap.get("freq"));
            return_hm.put("freq",lancetMap.get("freq").toLowerCase());
        }else{
            logger.info("PUTTING NULL INTO FREQ");
            return_hm.put("freq",null);
        }
        logger.info("freq: "+return_hm.get("freq"));

        if(lancetMap.containsKey("duration") && lancetMap.get("duration") != null && !lancetMap.get("duration").equals("")){
            return_hm.put("duration",drugRefMap.get("duration").toLowerCase().replace("'",""));
        }else{
            return_hm.put("duration",null);
        }
        logger.info("dur: "+return_hm.get("duration"));
        logger.info("------------------");
        return return_hm;
    }

    /**
     * Converts: {duration=null, reason=pain, route=p.o., freq=q.6h. p.r.n., name=percocet, context=narrative, dosage=one tablet}
     * to: {duration=value, route=value, freq=value, name=value, dose=value, atc=value, din=value, form=value, ingredient=value, dbid=value}
     * @param inHm
     * @return
     */
    private HashMap<String, String> standardizeMappingFieldsFromLancet(HashMap<String,String> inHm){
       HashMap<String, String> return_hm = new HashMap<String, String>();

        return_hm.put("name", inHm.get("name"));
        return_hm.put("duration",inHm.get("duration"));
        return_hm.put("freq",inHm.get("freq"));
        return_hm.put("dose",inHm.get("dosage"));
        return_hm.put("route",inHm.get("route"));
        return_hm.put("atc",null);
        return_hm.put("din",null);
        return_hm.put("form",null);
        return_hm.put("ingredient",null);
        return_hm.put("dbid",null);

        return return_hm;
    }

    /**
     * converts: {id=78577, category=18, isInactive=false, regional_identifier=02163748, name=CODEINE  100MG TABLET (EXTENDED-RELEASE), atc=R05DA04, ingredient_name=CODEINE (CODEINE MONOHYDRATE, Codeine Sulfate Trihydrate), drugForm=TABLET (EXTENDED-RELEASE), drugRoute=[ORAL]}
     * to: {duration=value, route=value, freq=value, name=value, dose=value, atc=value, din=value, form=value, ingredient=value, dbid=value}
     * @param inHm
     * @return
     */
    private HashMap<String, String> standardizeMappingFieldsFromDrugRef(HashMap<String,String> inHm){
        HashMap<String, String> return_hm = new HashMap<String, String>();

        return_hm.put("name", inHm.get("name"));
        return_hm.put("duration",null);
        return_hm.put("freq",null);
        return_hm.put("dose",null);
        return_hm.put("route",inHm.get("drugRoute"));
        return_hm.put("atc",inHm.get("atc"));
        return_hm.put("din",inHm.get("regional_identifier"));
        return_hm.put("form",inHm.get("drugForm"));
        return_hm.put("ingredient",inHm.get("ingredient_name"));
        return_hm.put("dbid",inHm.get("id"));
        return return_hm;
    }

        /*
         Finds them the most common atc code in a list of hashMaps the represnt medicaitons.
         */
        private StringIntPair findMostCommon(ArrayList<HashMap<String,String>> inList, String index){
            int total_count = 0;
            String total_value = "";
            int working_count = 0;
            String working_value = "";
            for(HashMap<String, String> hm : inList){
                working_value = hm.get(index);
                working_count = 0;
                for(HashMap<String, String> newHm : inList){
                   if(working_value.equals(newHm.get(index))){
                       working_count++;
                   }
                }
                if(working_count > total_count){
                    total_value = working_value;
                }
            }
            logger.info("Returning from findMostCommon() with "+index+": "+total_value+", "+total_count);
            return new StringIntPair(total_value, total_count);
        }

}
