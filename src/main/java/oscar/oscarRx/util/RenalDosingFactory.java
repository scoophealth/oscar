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

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.oscarehr.util.MiscUtils;

/**
 * Parses xml file, creating DosingRecomendation Objects storing them in a hashtable with the ATC code as the key
 * @author jay
 */
public class RenalDosingFactory {

    static Hashtable<String,DosingRecomendation> currentDosingInformation = new Hashtable<String,DosingRecomendation>();

    static boolean loaded = false;


    /** Creates a new instance of RenalDosingFactory */
    protected RenalDosingFactory() {
    }

    static public DosingRecomendation getDosingInformation(String atc){
        loadDosingInformation();
        return  currentDosingInformation.get(atc);
    }


    static private void loadDosingInformation(){
        MiscUtils.getLogger().debug("current dosing size "+currentDosingInformation.size());
        if(!loaded){
            String dosing = "oscar/oscarRx/RenalDosing.xml";
            RenalDosingFactory rdf  = new RenalDosingFactory();
            InputStream is = rdf.getClass().getClassLoader().getResourceAsStream(dosing);

            try{
                SAXBuilder parser = new SAXBuilder();
                Document doc = parser.build(is);
                Element root = doc.getRootElement();


                /*
                 <medication name="metformin" atccode="A10BA02">
                    <dose clcrrange="&gt;50">give 50% of dose</dose>
                    <dose clcrrange="10-50">give 25% of dose</dose>
                    <dose clcrrange="&lt;10">AVOID</dose>
                    <moreinfo>
                        Normal dose: 500-850 mg BID

                        When CLcr< 50 mL/min:  Monitor blood glucose BID to QID with any medication change until stable.
                    </moreinfo>
                </medication>
                    */

                @SuppressWarnings("unchecked")
                List<Element> meas = root.getChildren("medication");
                for (int j = 0; j < meas.size(); j++){
                        Element e =  meas.get(j);
                        String atccode = e.getAttributeValue("atccode");
                        String name    = e.getAttributeValue("name");

                        DosingRecomendation rec = new DosingRecomendation();
                        rec.setAtccode(atccode);
                        rec.setName(name);
                        @SuppressWarnings("unchecked")
                        List<Element> doses = e.getChildren("dose");
                        ArrayList<Hashtable<String,String>> recDoses = new ArrayList<Hashtable<String,String>>();
                        for (int d = 0; d < doses.size(); d++){
                            Element dose = doses.get(d);
                            MiscUtils.getLogger().debug(dose.getName());
                            Hashtable<String,String> h = new Hashtable<String,String>();
                            String clcrrange = dose.getAttributeValue("clcrrange");
                            String recommendation = dose.getText();

                            MiscUtils.getLogger().debug("clcrrange "+clcrrange+" recommendation "+recommendation);

                            if(recommendation == null){recommendation = "";}
                            if (clcrrange == null){ clcrrange = ""; }

                            h.put("clcrrange",clcrrange);
                            h.put("recommendation",recommendation);
                            recDoses.add(h);
                        }
                        rec.setDose(recDoses);

                        @SuppressWarnings("unchecked")
                        List<Element> moreinformation = e.getChildren("moreinfo");
                        StringBuilder sb = new StringBuilder();
                        for (int m = 0; m < moreinformation.size(); m++){
                            Element info =  moreinformation.get(m);
                            sb.append(info.getText());
                        }
                        rec.setMoreinfo(sb.toString());
                        MiscUtils.getLogger().debug(rec.toString());
                        currentDosingInformation.put(rec.getAtccode(),rec);

                   }

                }catch(Exception e){
                    MiscUtils.getLogger().error("Error", e);
                }
                loaded = true;
            }

}



}
