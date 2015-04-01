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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.List;

import org.apache.commons.collections.KeyValue;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;

import oscar.oscarEncounter.oscarMeasurements.bean.EctMeasurementsDataBean;
import oscar.oscarEncounter.oscarMeasurements.bean.EctMeasurementsDataBeanHandler;
import oscar.oscarMDS.data.ProviderData;

/**
 *
 * @author Jay
 */
public class ReportEvaluator {

    /** Creates a new instance of ReportEvaluator */
    int denominatorCount = 0;
    int numeratorCount = 0;
    Denominator denominator = null;
    Numerator numerator = null;
    private ArrayList<Hashtable<String,Object>> reportResultList = null;


    public ReportEvaluator() {
    }

    public void evaluate(LoggedInInfo loggedInInfo, Denominator deno, Numerator numer){
        evaluate(loggedInInfo, deno,numer,null);
    }

    public void evaluate(LoggedInInfo loggedInInfo, Denominator deno, Numerator numer,List<KeyValue> additionalFields){
        denominator = deno;
        numerator = numer;
        List demoList = deno.getDenominatorList();
        denominatorCount = demoList.size();
        setReportResultList(new ArrayList<Hashtable<String,Object>>());
        for (int i = 0; i < demoList.size(); i++){
            String demo = (String) demoList.get(i);
            boolean bool = numer.evaluate(loggedInInfo, demo);
            //Object obj = numer.getOutputValues();  // PROBLEM IS THAT THIS WILL ALWAYS HAVE A VALUE
            Hashtable<String,Object> h = new Hashtable<String,Object>();
            h.put("_demographic_no",demo);
            h.put("_report_result",new Boolean(bool));

            if (additionalFields != null){
                for(KeyValue field:additionalFields){
                    String key = (String) field.getKey();
                    String val = (String) field.getValue();

                    EctMeasurementsDataBeanHandler ect = new EctMeasurementsDataBeanHandler(Integer.valueOf(demo), val);
                    Collection<EctMeasurementsDataBean> v = ect.getMeasurementsDataVector();
                    //Execute for the value and attach it to the key in the hashtable
                    //Object obj =
                    if(v.iterator().hasNext()){
                        h.put(key, v.iterator().next());
                    }

                }
            }



            getReportResultList().add(h);
//            if (obj != null){
//                getReportResultList().add(obj);
//            }
            if (bool){
                numeratorCount++;
            }

        }

    }

    public int getDenominatorCount(){
        return denominatorCount;
    }

    public int getNumeratorCount(){
        return numeratorCount;
    }

    public float getPercentage(){
        float percentage = 0;
        try{
           percentage = ( (float) getNumeratorCount() / (float) getDenominatorCount() ) * 100;
        }catch(java.lang.ArithmeticException arithEx){
        	MiscUtils.getLogger().error("Error", arithEx);
            //request.setAttribute("divisionByZero",denominatorId);
            percentage = 0;
        }
        return percentage;
    }

    public int getPercentageInt(){
        return new Float(getPercentage()).intValue();
    }


    //TODO:HACK for now! replace with something more flexible
    public  String getCSV(){
        String csv = null;
        if (denominator.hasReplaceableValues()){
            String providerNo = (String) denominator.getReplaceableValues().get("provider_no");
            csv = "'"+getProviderStringName(providerNo)+"','"+getNumeratorCount()+"','"+getDenominatorCount()+"','"+getPercentageInt()+"'";
        }else{
            csv ="'"+getNumeratorCount()+"','"+getDenominatorCount()+"','"+getPercentageInt()+"'";
        }

        return csv;
    }


    public String getName(){
        StringBuilder  name = new StringBuilder();
        name.append(numerator.getNumeratorName());
        name.append("/");
        name.append(denominator.getDenominatorName());
        if ( denominator.hasReplaceableValues()){
           name.append(" (");
           String[] repKeys = denominator.getReplaceableKeys();
           Hashtable repVals = denominator.getReplaceableValues();
           for (int i = 0; i < repKeys.length;i++){
               //provider_no:999998  if key is provider_no look up provider name
               MiscUtils.getLogger().debug("repKeys "+repKeys[i]);
               if (repKeys[i] != null && repKeys[i].equals("provider_no")){
                  name.append("Provider: "+getProviderStringName(""+repVals.get(repKeys[i])));
               }else{
                  name.append(repKeys[i]+":"+repVals.get(repKeys[i]));
               }
           }
           name.append(")");
        }

        return  name.toString();
    }

    private String getProviderStringName(String providerNo){
        return  ProviderData.getProviderName(providerNo);
    }

    //private String getProvider

    public ArrayList<Hashtable<String,Object>> getReportResultList() {
        return reportResultList;
    }

    public void setReportResultList(ArrayList<Hashtable<String,Object>> reportResultList) {
        this.reportResultList = reportResultList;
    }
}
