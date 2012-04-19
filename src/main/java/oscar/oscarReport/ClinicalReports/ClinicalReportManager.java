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


package oscar.oscarReport.ClinicalReports;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.oscarehr.util.MiscUtils;

import oscar.OscarProperties;

/**
 *
 * @author jay
 */
public class ClinicalReportManager {

    static ClinicalReportManager clinicalReportManager = new ClinicalReportManager();

    List<Numerator> numeratorList = null ; //new ArrayList();
    List<Denominator> denominatorList = null ;// new ArrayList();

    Hashtable<String,Object> numeratorHash = null; // new Hashtable();
    Hashtable<String,Object> denominatorHash = null; //new Hashtable();

    boolean loaded = false;

    /** Creates a new instance of ClinicalReportManager */
    private ClinicalReportManager() {

    }

    static public ClinicalReportManager getInstance(){

        clinicalReportManager.loadReportsFromFile();

        return clinicalReportManager;
    }

    public void addNumerator(Numerator n){

        if(!numeratorList.contains(n)){
            numeratorList.add(n);
        }
        //if(!numeratorHash.contains(n)){
        //    numeratorHash.put(n.getId(),n);
        //}
    }

    public void addNumerator(Hashtable n,String id){

        //if(!numeratorList.contains(n)){
        //    numeratorList.add(n);
        //}
        if(!numeratorHash.contains(n)){
            numeratorHash.put(id,n);
        }
    }

    public void addDenominator(Denominator d){
        if(!denominatorList.contains(d)){
            denominatorList.add(d);
        }
        if(!denominatorHash.contains(d)){
            denominatorHash.put(d.getId(),d);
        }
    }


    public List<Denominator> getDenominatorList(){
        return denominatorList;
    }

    public List<Numerator> getNumeratorList(){
        return numeratorList;
    }

    public Numerator getNumeratorById(String id){
       //return (Numerator) numeratorHash.get(id);
       Hashtable numerHash = (Hashtable) numeratorHash.get(id);
       String type = (String) numerHash.get("type");
       if (type != null && type.equals("SQL")){
          SQLNumerator sqlN = new SQLNumerator();

            sqlN.setNumeratorName((String) numerHash.get("numeratorName"));
            sqlN.setId((String) numerHash.get("id"));
            sqlN.setSQL((String) numerHash.get("sql"));
            sqlN.parseOutputFields((String) numerHash.get("outputfields"));
            MiscUtils.getLogger().debug("output fields "+(String) numerHash.get("outputfields"));
            MiscUtils.getLogger().debug("create new sqlNumerator object");
            return sqlN;
       }
       if (type != null && type.equals("DROOLS")){
            DroolsNumerator droolsN = new DroolsNumerator();
            droolsN.setNumeratorName((String) numerHash.get("numeratorName"));
            droolsN.setId((String) numerHash.get("id"));
            droolsN.setFile((String) numerHash.get("file"));
            MiscUtils.getLogger().debug("create new DroolsNumerator object");
            return droolsN;
       }
       if (type != null && type.equals("DROOLS2")){
            DroolsNumerator2 droolsN = new DroolsNumerator2();
            droolsN.setNumeratorName((String) numerHash.get("numeratorName"));
            droolsN.setId((String) numerHash.get("id"));
            droolsN.parseReplaceValues((String) numerHash.get("replaceKeys"));
            MiscUtils.getLogger().debug("create new DroolsNumerator2 object");
            return droolsN;
       }
       return null;
    }

    public Denominator getDenominatorById(String id){
        return (Denominator) denominatorHash.get(id);
    }

    @SuppressWarnings("unchecked")
    private void loadReportsFromFile(){

        if(!loaded){

            numeratorList = new ArrayList<Numerator>();
            denominatorList = new ArrayList<Denominator>();
            numeratorHash = new Hashtable<String,Object>();
            denominatorHash = new Hashtable<String,Object>();


            String[] flowsheetsArray = {"oscar/oscarReport/ClinicalReports/ClinicalReports.xml"
                                        };

            OscarProperties properties = OscarProperties.getInstance();
            String userConfigFilePath = (String)properties.get("CLINICAL_REPORT_CONFIG_FILE");
            boolean userConfigLoaded = false;

            for ( int i = 0; i < flowsheetsArray.length;i++){
                InputStream is = null;

                if( userConfigFilePath != null && !userConfigLoaded ) {
                    try {
                        is = new FileInputStream(userConfigFilePath);
                        userConfigLoaded = true;
                    } catch( FileNotFoundException ex ) {MiscUtils.getLogger().error("Error", ex);
                        is = null;
                    }
                }

                if( is == null ) {
                    is = this.getClass().getClassLoader().getResourceAsStream(flowsheetsArray[i]);
                }

                try{
                    SAXBuilder parser = new SAXBuilder();
                    Document doc = parser.build(is);
                    Element root = doc.getRootElement();


                    List<Element> meas = root.getChildren("Numerator");
                    for (int j = 0; j < meas.size(); j++){
                        Element e = meas.get(j);
                        String type = e.getAttributeValue("type");
                        if (type != null && type.equals("SQL")){
                            SQLNumerator sqlN = new SQLNumerator();
                            //TODO: What if one of the values is null;
                            Hashtable<String,String> h = new Hashtable<String,String>();
                            h.put("type",type);
                            h.put("numeratorName",e.getAttributeValue("name"));
                            h.put("id",e.getAttributeValue("id"));
                            h.put("sql",e.getText());
                            if(e.getAttributeValue("outputfields") != null){
                            h.put("outputfields",e.getAttributeValue("outputfields"));
                            }
                            addNumerator(h,e.getAttributeValue("id"));


                            sqlN.setNumeratorName(e.getAttributeValue("name"));
                            sqlN.setId(e.getAttributeValue("id"));
                            sqlN.setSQL(e.getText());
                            sqlN.parseOutputFields(e.getAttributeValue("outputfields"));
                            addNumerator(sqlN);
                       }
                       if (type != null && type.equals("DROOLS")){
                            DroolsNumerator droolsN = new DroolsNumerator();
                            droolsN.setNumeratorName(e.getAttributeValue("name"));
                            droolsN.setId(e.getAttributeValue("id"));
                            droolsN.setFile(e.getAttributeValue("file"));

                            Hashtable<String,String> h = new Hashtable<String,String>();
                            h.put("type",type);
                            h.put("numeratorName",e.getAttributeValue("name"));
                            h.put("id",e.getAttributeValue("id"));
                            h.put("file",e.getAttributeValue("file"));
                            addNumerator(h,e.getAttributeValue("id"));

                            addNumerator(droolsN);
                       }
                       if (type != null && type.equals("DROOLS2")){
                            DroolsNumerator2 droolsN = new DroolsNumerator2();
                            droolsN.setNumeratorName(e.getAttributeValue("name"));
                            droolsN.setId(e.getAttributeValue("id"));
                            droolsN.setFile(e.getAttributeValue("file"));
                            droolsN.parseReplaceValues(e.getAttributeValue("replaceKeys"));
                            Hashtable<String,String> h = new Hashtable<String,String>();
                            h.put("type",type);
                            h.put("numeratorName",e.getAttributeValue("name"));
                            h.put("id",e.getAttributeValue("id"));
                            h.put("replaceKeys",e.getAttributeValue("replaceKeys"));
                            //h.put("file",e.getAttributeValue("file"));
                            addNumerator(h,e.getAttributeValue("id"));

                            addNumerator(droolsN);
                       }

                   }


                   meas = root.getChildren("Denominator");
                   for (int j = 0; j < meas.size(); j++){
                        Element e = meas.get(j);
                        String type = e.getAttributeValue("type");
                        if (type != null && type.equals("SQL")){
                            SQLDenominator sqlD = new SQLDenominator();
                            sqlD.setDenominatorName(e.getAttributeValue("name"));
                            sqlD.setId(e.getAttributeValue("id"));
                            sqlD.setSQL(e.getText());
                            sqlD.parseReplaceValues(e.getAttributeValue("replaceKeys"));
                            addDenominator(sqlD);
                       }
                        else if( type != null && type.equals("PatientSetDenominator")) {
                            PatientSetDenominator psD = new PatientSetDenominator();
                            psD.setDenominatorName(e.getAttributeValue("name"));
                            psD.setId(e.getAttributeValue("id"));
                            addDenominator(psD);
                        }
                   }
                }catch(Exception e){
                    MiscUtils.getLogger().error("Error", e);
                }
                loaded = true;
            }
        }
    }


}
