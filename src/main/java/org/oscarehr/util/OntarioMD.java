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


package org.oscarehr.util;

import java.io.InputStream;
import java.util.Hashtable;
import java.util.Iterator;

import org.apache.commons.collections.OrderedMap;
import org.apache.commons.collections.map.LinkedMap;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.filter.ElementFilter;
import org.jdom.input.SAXBuilder;

import oscar.OscarProperties;

/**
 *
 * @author jaygallagher
 */
public class OntarioMD {
    
   
    
    static public boolean hasIncomingRequestor(){
        return OscarProperties.getInstance().hasProperty("ONTARIO_MD_INCOMINGREQUESTOR");
    }
    
    static public String getIncomingRequestor(){
        return OscarProperties.getInstance().getProperty("ONTARIO_MD_INCOMINGREQUESTOR");
    }
    
    public boolean showOntarioMDLink(){
        if (hasIncomingRequestor() && true );
        return true;
    }
    
    
    
     //HACKED SOAP CALL.  THIS SHOULD BE REPLACED BUT IT'S SO SIMPLE
    public Hashtable loginToOntarioMD(String username,String password,String incomingRequestor) throws Exception{
        //public ArrayList soapHttpCall(int siteCode, String userId, String passwd,		String xml) throws Exception
        Hashtable h  = null;
        PostMethod post = new PostMethod("https://www.ontariomd.ca/services/OMDAutomatedAuthentication");
        post.setRequestHeader("SOAPAction", "");
        post.setRequestHeader("Content-Type", "text/xml; charset=utf-8");

        String soapMsg = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\"><soap:Body><ns1:getSession xmlns:ns1=\"urn:OMDAutomatedAuthentication\"><username>"+username+"</username><password>"+password+"</password><incomingRequestor>"+incomingRequestor+"</incomingRequestor></ns1:getSession></soap:Body></soap:Envelope> ";

        RequestEntity re = new StringRequestEntity(soapMsg, "text/xml", "utf-8");

        post.setRequestEntity(re);

        HttpClient httpclient = new HttpClient();
        // Execute request
        try{
                httpclient.executeMethod(post);
                h = parseReturn(post.getResponseBodyAsStream());
        }catch(Exception e ){
            MiscUtils.getLogger().error("Error", e);
        } finally{
                // Release current connection to the connection pool
                post.releaseConnection();
        }
        return h;
    }
    
    private Hashtable parseReturn(InputStream is){
        Hashtable h = null;
        try {
            
           
            SAXBuilder parser = new SAXBuilder();
            Document doc = parser.build(is);
            Element root = doc.getRootElement();
             h  = new Hashtable();
            String jsessionID =g(root.getDescendants(new ElementFilter("jsessionID")));
            String ptLoginToken =g(root.getDescendants(new ElementFilter("ptLoginToken")));
            String returnCode =g(root.getDescendants(new ElementFilter("returnCode")));
                        
            h.put("returnCode",returnCode);
            h.put("ptLoginToken",ptLoginToken);
            h.put("jsessionID",jsessionID);
            
        }catch(Exception e){
            MiscUtils.getLogger().error("Error", e);
        }
        return h;
    }
     
    private String g(Iterator iter){
        String ret = null;
        if(iter.hasNext()){
            Element ele = (Element) iter.next();
            ret = ele.getValue();
        }
        return ret;
    }
    
    public OrderedMap getDiseaseList(){
        OrderedMap map = new LinkedMap();
     /*
      Disease Community Keyword 
      */ 
            map.put("Acne","Acne");
            map.put("AcuteBronchitis","Acute Bronchitis");
            map.put("AddictionMedicine","Addiction Medicine");
            map.put("Allergy","Allergy");
            map.put("AlzheimersDisease","Alzheimer's Disease");
            map.put("Andropause","Andropause");
            map.put("AnxietyDisorders","Anxiety Disorders");
            map.put("Appendicitis","Appendicitis");
            map.put("Asthma","Asthma");
            map.put("AtrialFibrillation","Atrial Fibrillation");
            map.put("AvianFlu","Avian Flu");
            map.put("BenignProstaticHyperplasia","Benign Prostatic Hyperplasia");
            map.put("BipolarDisorder","Bipolar Disorder");
            map.put("BreastCancer","Breast Cancer");
            map.put("CHF","CHF");
            map.put("COPD","COPD");
            map.put("CVA","CVA (Stroke)");

            map.put("Cholecystitis","Cholecystitis");
            map.put("ChronicPain","Chronic Pain");
            map.put("ColonCancer","Colon Cancer");
            map.put("Contraception","Contraception");
            map.put("CoronaryArteryDisease","Coronary Artery Disease");
            map.put("Depression","Depression");
            map.put("Diabetes","Diabetes");
            map.put("Dyslipidemia","Dyslipidemia");
            map.put("EatingDisorders","Eating Disorders");
            map.put("ErectileDysfunction","Erectile Dysfunction");
            map.put("GERD","GERD");
            map.put("HIVandAIDS","HIV and AIDS");
            map.put("Headache","Headache");
            map.put("Hepatitis","Hepatitis");
            map.put("Herpes","Herpes");
            map.put("Hypertension","Hypertension");
            map.put("Infertility","Infertility");
            map.put("InflammatoryBowelDisease","Inflammatory Bowel Disease");
            map.put("Influenza","Influenza");
            map.put("IrritableBowelSyndrome","Irritable Bowel Syndrome");
            map.put("LungCancer","Lung Cancer");
            map.put("Menopause","Menopause");
            map.put("Obesity","Obesity");
            map.put("Osteoporosis","Osteoporosis");
            map.put("OvarianCancer","Ovarian Cancer");
            map.put("PalliativeCare","Palliative Care");
            map.put("ParkinsonsDisease","Parkinson's Disease");
            map.put("ProstateCancer","Prostate Cancer");
            map.put("Psoriasis","Psoriasis");
            map.put("RheumatoidArthritis","Rheumatoid Arthritis/Autoimmune");
            map.put("SARS","SARS");
            map.put("STIs","STIs");
            map.put("Schizophrenia","Schizophrenia");
            map.put("Sinusitis","Sinusitis");
            map.put("SmokingCessation","Smoking Cessation");
            map.put("ThyroidDisorders","Thyroid Disorders");
            map.put("UrinaryIncontinence","Urinary Incontinence");
            map.put("WestNileVirus","West Nile Virus");
            return map;
       }

}
