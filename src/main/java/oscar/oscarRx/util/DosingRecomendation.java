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

import java.util.ArrayList;
import java.util.Hashtable;

import org.oscarehr.util.MiscUtils;

/**
 *
 * @author jay
 */
public class DosingRecomendation {

    private String name = null;
    private String atccode = null;
    private String moreinfo = null;
    private ArrayList<Hashtable<String,String>> Dose = null;

    public String toString(){
        return "name: "+name+" atccode: "+atccode;

    }

    /** Creates a new instance of DosingRecomendation */
    public DosingRecomendation() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAtccode() {
        return atccode;
    }

    public void setAtccode(String atccode) {
        this.atccode = atccode;
    }

    public String getMoreinfo() {
        return moreinfo;
    }

    public void setMoreinfo(String moreinfo) {
        this.moreinfo = moreinfo;
    }

    public ArrayList<Hashtable<String,String>> getDose() {
        return Dose;
    }

    public void setDose(ArrayList<Hashtable<String,String>> Dose) {
        this.Dose = Dose;
    }



    /*
     *Evaluate string to form a comparison
     *Values are stored in a hashtable with the key clcrrange
     *Values will be in the form of:

     "30-50"    between 30 and 50
     "&lt;15"   less than 15
     "&gt;50"   greater than 50
     */
    public boolean valueInRangeOfDose(int val , Hashtable<String,String> doseVal){
        boolean valueInRange = false;
        try{
            String toParse  =  doseVal.get("clcrrange");

            MiscUtils.getLogger().debug("TO PARSE: "+toParse);
            if (toParse == null){
               return false;
            }

            if (toParse.indexOf("-") != -1){ //between style
                String[] betweenVals = toParse.split("-");
                if (betweenVals.length == 2 ){
                    int lower = Integer.parseInt(betweenVals[0]);
                    int upper = Integer.parseInt(betweenVals[1]);

                    if (val >= lower && val <= upper){
                        valueInRange = true;
                    }
                }

            }else if (toParse.indexOf("&gt;") != -1 ||  toParse.indexOf(">") != -1 ){ // greater than style
                toParse = toParse.replaceFirst("&gt;","");
                toParse = toParse.replaceFirst(">","");

                int gt = Integer.parseInt(toParse);
                if(val >= gt){
                    valueInRange = true;
                }
            }else if (toParse.indexOf("&lt;") != -1  ||  toParse.indexOf("<") != -1 ){ // less than style
                toParse = toParse.replaceFirst("&lt;","");
                toParse = toParse.replaceFirst("<","");

                int lt = Integer.parseInt(toParse);
                if(val <= lt){
                    valueInRange = true;
                }
            }else if (!toParse.equals("")){ // less than style
                int eq = Integer.parseInt(toParse);
                if(val == eq){
                    valueInRange = true;
                }
            }


        }catch(Exception e){
            MiscUtils.getLogger().error("Error", e);
        }
        return valueInRange;
    }



}
