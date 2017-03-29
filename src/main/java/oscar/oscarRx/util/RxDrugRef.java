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
/*
 * DrugRef.java
 *
 * Created on September 19, 2003, 2:16 PM
 */


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;
import org.apache.log4j.Logger;
import org.apache.xmlrpc.Base64;
import org.apache.xmlrpc.XmlRpcClient;
import org.apache.xmlrpc.XmlRpcClientLite;
import org.apache.xmlrpc.XmlRpcException;
import org.oscarehr.util.MiscUtils;
import oscar.OscarProperties;

/**
 *
 * @author  Jay
 */
public class RxDrugRef {

	
	// DRUG CATEGORIES FOR THE DRUG REF SEARCH TABLE.
	public static final int CAT_BRAND = 13;
	public static final int CAT_COMPOSITE_GENERIC = 12;
	public static final int CAT_GENERIC = 11;
	public static final int CAT_ATC = 8;
	public static final int CAT_AHFS = 10;
	public static final int CAT_ACTIVE_INGREDIENT = 14;
	public static final int CAT_AI_COMPOSITE_GENERIC = 19;
	public static final int CAT_AI_GENERIC = 18;

    private static Logger logger=MiscUtils.getLogger(); 

    private String server_url = null;
    		//"http://localhost:8080/drugref2/DrugrefService";
           // "http://www.hherb.com:8001";
           //"http://24.141.82.168:8001";
           //"http://192.168.42.3:8001";
    		
    /** Creates a new instance of DrugRef */
    public RxDrugRef() {
        server_url = OscarProperties.getInstance().getProperty("drugref_url");
        //server_url = System.getProperty("drugref_url");
  
    }
    
    public RxDrugRef(String url){
        server_url = url;
    }
    
    public String getDrugRefURL(){
       return server_url;
    }
    
    public Hashtable<String, Object> getDrugByDIN(String DIN, Boolean boolVal) throws Exception {
    	Vector params = new Vector();
        params.addElement(DIN);
        params.addElement(boolVal);
    	Vector<Hashtable<String, Object>> vec = (Vector<Hashtable<String, Object>>) callWebserviceLite("get_drug_by_DIN", params);
    	Hashtable<String, Object> returnVal = vec.get(0);
    	return returnVal;
    }
    
    /**
     *returns all matching ATC codes for a given (fraction of) a drug name.
     *Search is case insensitive
     *query = "select code, text from atc where text like '%s%%'" 
     *
     *
     *	 [{'code':'0', 'text':'None found'}]
     */
     public Vector atc(String drug){
         Vector params = new Vector();
         params.addElement(drug);
         Vector vec = (Vector) callWebservice("atc",params);         
         return vec;         
     }
     
     /**
     *returns all matching ATC codes for a given Drug Identification Number.
     *Search is case insensitive
     */
     public Vector atcFromDIN(String din){
    	 Vector params = new Vector();
    	 params.addElement(din);
    	 Vector vec = (Vector) callWebservice("get_atcs_by_din",params);         
    	 return vec;         
     }
     

     /**
     *returns all matching ATC codes for a given (fraction of) a drug brand name.
     *Search is case insensitive
     *query = "select atc.atccode, pm.brandname from link_product_manufacturer pm, product p, generic_drug_name g, link_drug_atc atc 
     *         where pm.id_product = p.id and p.id_drug = g.id_drug  and g.id_drug = atc.id_drug and pm.brandname like  '%s%%'"
     *
     *   [{'code':'0', 'text':'None found'}]
     */
     public Vector atcFromBrand(String drug){
         Vector params = new Vector();
         params.addElement(drug);
         Vector vec = (Vector) callWebservice("atcFromBrand",params);
         return vec;
     }


     /**
      *returns the English name of a drug that matches the stated ATC code
      *query = "select code, text from atc where code like '%s%%'" 
      *
      *return [{'code':'0', 'text':'None found'}]
      */
     public Vector  atc2text(String code){
	 Vector params = new Vector();
         params.addElement(code);
         Vector vec = (Vector) callWebservice("atc2text",params);    
         
         return  vec;   			
     }
     
     public Vector interaction(Vector atclist){
        return interaction(atclist,1);        
     }
     
     /**
      *returns a list of drug-drug interactions as list of "dicts"
      *atclist : list of ATC codes
      *minimum_significance: interactions below the stated significance level will be ignored
      *
      *query = "select drug, effect, affected_drug, significance, evidence, reference from simple_interactions where drug = '%s' and affected_drug = '%s' and significance >= %d" % 
      */
     public Vector interaction(Vector atclist,int minimum_significance){
         Vector params = new Vector();
         params.addElement(atclist);
         params.addElement(new Integer(minimum_significance));         
         Vector vec = (Vector) callWebservice("interaction",params);         
         return vec;         
     }
     
     public Vector interactionByRegionalIdentifier(List regionalIdentifierList,int minimum_significance){
         Vector params = new Vector();
         params.addElement(new Vector(regionalIdentifierList));
         params.addElement(new Integer(minimum_significance));
         Vector vec = (Vector) callWebservice("interaction_by_regional_identifier",params); 
         return vec;         
     }
     
     
     public Hashtable getDrug(String pKey, Boolean boolVal)throws Exception{
         Vector params = new Vector();
         params.addElement(pKey);
         params.addElement(boolVal);
         Vector vec = (Vector) callWebserviceLite("get_drug",params);             
         Hashtable returnVal = (Hashtable) vec.get(0);         
         return returnVal;		         
     }

     public Hashtable getDrug2(String pKey, Boolean boolVal)throws Exception{
         Vector params = new Vector();
         MiscUtils.getLogger().debug("Adding to params for get_drug_2 :"+pKey+" - "+boolVal);
         params.addElement(pKey);
         params.addElement(boolVal);
         Vector vec = (Vector) callWebserviceLite("get_drug_2",params);
         Hashtable returnVal = (Hashtable) vec.get(0);
         return returnVal;
     }

     public Hashtable getDrugForm(String pKey) throws Exception {
	 Vector params = new Vector();
	 params.addElement(pKey);
	 Vector vec = (Vector) callWebserviceLite("get_form",params);
         //if (vec == null || vec.isEmpty()){
         //    return null;
         //}
	 Hashtable returnVal = (Hashtable) vec.get(0);
	 return returnVal;
     }
        
     
     public Vector suggestAlias(String alias,String aliasComment,String id,String name,String provider)throws Exception{
         Vector params = new Vector();
         params.addElement(alias);
         params.addElement(aliasComment);
         params.addElement(id);
         params.addElement(name);
         params.addElement(provider);
         Vector vec = (Vector) callWebserviceLite("suggestAlias",params);                    
         return vec;
     }
     
     
     
     public Hashtable getGenericName(String pKey)throws Exception{
         Vector params = new Vector();
         params.addElement(pKey);

         Vector vec = (Vector) callWebserviceLite("get_generic_name",params);
       //  if (vec == null || vec.isEmpty()){

        //     return null;
       //  }
         Hashtable returnVal = (Hashtable) vec.get(0);         
         return returnVal;		         
     }	
     
     /**
      *Returns a list of atc codes without the drug
      *uses function atc on the back end and strips the name
      *
      */
     public Vector drug2atclist(String drug){
         Vector params = new Vector();
         params.addElement(drug);
         Vector vec = (Vector) callWebservice("drug2atclist",params);             
         return vec;		         
     }		

     public Vector druglist2atclist(Vector druglist){
         Vector params = new Vector();
         params.addElement(druglist);         
         Vector vec = (Vector) callWebservice("druglist2atclist",params);         
         return vec;         
     }


     public Vector interaction_by_drugnames(Vector druglist, int minimum_significance){
	Vector params = new Vector();
         params.addElement(druglist);
         params.addElement(new Integer(minimum_significance));         
         Vector vec = (Vector) callWebservice("interaction_by_drugnames",params);         
         return vec;         
     }
     
     /**
     returns all matching search element names, ids and categoeis for the given searchString
     Search is case insensitive
     */
     public Vector list_drug_element(String searchStr) throws Exception{
         Vector params = new Vector();
         params.addElement(searchStr);
         Vector vec = (Vector) callWebserviceLite("list_search_element",params);
         return vec;		         
     }
     public String updateDB() throws Exception{
         Vector params = new Vector();
         return (String) callWebserviceLite("updateDB",params);
         
     }
     public String getLastUpdateTime() throws Exception{
         Vector params = new Vector();
         String s = (String) callWebserviceLite("getLastUpdateTime",params);
         return s;
     }

     public Vector list_drug_element2(String searchStr) throws Exception{
         Vector params = new Vector();
         params.addElement(searchStr);
         Vector vec = (Vector) callWebserviceLite("list_search_element2",params);
         return vec;
     }

     public Vector list_drug_element3(String searchStr, boolean rightWildcardOnly) throws Exception{
         Vector params = new Vector();
         params.addElement(searchStr);
         Vector<Hashtable> vec = null;
         if(rightWildcardOnly) {
       	  vec  = (Vector)  callWebserviceLite("list_search_element3_right",params);
         } else {
       	  vec  = (Vector)  callWebserviceLite("list_search_element3",params);
         }
         return vec;
    }
     
     /**
     returns all matching search element names, ids and categoeis for the given searchString
     Search is limited by the given searchForm, and is case insensitive
     */
     public Vector list_drug_element_route(String searchStr, String searchRoute) throws Exception{
         Vector params = new Vector();
         params.addElement(searchStr);
	 params.addElement(searchRoute);
         Vector vec = (Vector) callWebserviceLite("list_search_element_route",params);
         return vec;		         
     }
     
     @Deprecated
     public Vector listComponents(String drugId){
        Vector params = new Vector();
        params.addElement(drugId);
        Vector vec = (Vector) callWebservice("getComponents",params);             
        return vec;	 
         
     }
     
     public Vector list_brands_from_element(String drugRefId) throws Exception{
         Vector params = new Vector();
         params.addElement(drugRefId);
         Vector vec = (Vector) callWebserviceLite("list_brands_from_element",params);             
         return vec;		         
     }	
     
     public Vector getDrugInfoPage(String drugRefId){
         Vector params = new Vector();
         params.addElement(drugRefId);
         Vector vec = (Vector) callWebservice("getDrugInfoPage",params);             
         return vec;		         
     }	
         
     public Vector list_search_element_select_categories(String searchStr,Vector catVec){
         return list_search_element_select_categories(searchStr,catVec,false);
      }
      
      public Vector list_search_element_select_categories(String searchStr,Vector catVec, boolean wildcardRightOnly){
          Vector params = new Vector();
          params.addElement(searchStr);
          params.addElement(catVec);
          Vector vec = null;
          if(wildcardRightOnly) {
         	 vec = (Vector) callWebservice("list_search_element_select_categories_right",params);             
          } else {
         	 vec = (Vector) callWebservice("list_search_element_select_categories",params);    
          }
          return vec;		         
      }	
      
     
     public Vector list_drug_class(Vector classVec){
         Vector params = new Vector();         
         params.addElement(classVec);
         Vector vec = (Vector) callWebservice("list_drug_class",params);             
         return vec;		         
     }
     
     
     
      public Vector getAISameByDrugCode(String drugRefId){
         Vector params = new Vector();
         params.addElement(drugRefId);
         Vector vec = (Vector) callWebservice("getAISameByDrugCode",params);             
         return vec;		         
     }	
     
      public Vector getFormFromDrugCode(String drugCode){
         Vector params = new Vector();
         params.addElement(drugCode);
         Vector vec = (Vector) callWebservice("getFormFromDrugCode",params);             
         return vec;	
      }
        
      
      
      public Vector getDistinctForms(){
         Vector params = new Vector();       
         Vector vec = (Vector) callWebservice("getDistinctForms",params);             
         return vec;	
      }
      
      public Vector getRouteFromDrugCode(String drugCode){
         Vector params = new Vector();
         params.addElement(drugCode);
         Vector vec = (Vector) callWebservice("getRouteFromDrugCode",params);             
         return vec;	
      }
       public Vector getStrengths(String drugCode){
         Vector params = new Vector();
         params.addElement(drugCode);
         Vector vec = (Vector) callWebservice("getStrengths",params);             
         return vec;	
      }
       
      
     public Vector getProductData(String drugRefId){
         Vector params = new Vector();
         params.addElement(drugRefId);
         Vector vec = (Vector) callWebservice("getProductData",params);             
         return vec;		         
     }	
     
     
     public String getGenericNamefromId(String drugRefId){
        Vector params = new Vector();
         params.addElement(drugRefId);
         Vector vec = (Vector) callWebservice("getGenericNamefromId",params);             
         return vec.get(0).toString();	                  
     }
     
     private Object callWebservice(String procedureName,Vector params) {
        MiscUtils.getLogger().debug("#CALLDRUGREF-"+procedureName);
         Object object = null;
         try{
            XmlRpcClient server = new XmlRpcClient(server_url);
            object = server.execute(procedureName, params);
         }catch (XmlRpcException exception) {
                logger.error("JavaClient: XML-RPC Fault #" +exception.code, exception);
         } catch (Exception exception) {
        	 logger.error("JavaClient: ", exception);
         }
         return object;
     }

     private Object callWebserviceLite(String procedureName,Vector params) throws Exception{

         Object object = null;
         try{
            if (!System.getProperty("http.proxyHost","").isEmpty()) {
                //The Lite client won't recgonize JAVA_OPTS as it uses a customized http
                XmlRpcClient server = new XmlRpcClient(server_url);
                object = server.execute(procedureName, params);
            } else {
                XmlRpcClientLite server = new XmlRpcClientLite(server_url);
                object = server.execute(procedureName, params);
            }                        
         }catch (XmlRpcException exception) {
                
             logger.error("JavaClient: XML-RPC Fault #" +exception.code, exception);
                                   
                throw new Exception("JavaClient: XML-RPC Fault #" +
                                   Integer.toString(exception.code) + ": " +
                                   exception.toString());
                
         } catch (Exception exception) {
        	 logger.error("JavaClient: ", exception);

                throw new Exception("JavaClient: " + exception.toString(),exception);
         }
         return object;
     }
     
     
     
     ////DRUGREF API
     /** applications only permitting one or few data sources will use this function to check for valid databases.
      *
      * Applications allowing more choice will expose all possible data sources to the end user
      * @param searchexpr mnemonic describing data source,
      *        e.g. mims, amh, rote liste, first database.
      *        Case insensitive, partial match possible if using % as wild card
      * @param tags see tags
      * @return array of structs alphabetically sorted by name with the following minimum keys:
      *
      * <B>pkey</B>: integer. Primary key      
      * <B>name</B>: string. Menemonic describing data source     
      * <B>revision</B>: string. Format and meaning depends on data source
      * <B>last_change</B>: string (date in ISO format yyyy-mm-dd)
      * <B>deprecated</B>: boolean. True if this source is deprecated and should no longer be used
      */
     @Deprecated
     public Vector list_sources(String searchexpr,Hashtable tags){
        return new Vector();
     }
     
     /**useful if searches should be constrained to a single source
      *
      *@param  pkey primary key.
      *@return tags: see [tags].
      */
     @Deprecated
     public Hashtable get_source_tag(int pkey){
         return new Hashtable();
     }

     /**returns basic identifiers of all drugs, drug products and drug classes available in the database which names match searchexpr, and which other criteria match the constraints in tags
      *
      *@param searchexpr (Partial) name of a drug (generic, brand name, composite drug) Case insensitive, partial match possible if using % as wild card
      *@param tags see [tags] Additional optional keys:
      *       classes : boolean. If true, class names (ATC) are listed
      *       generics : boolean. If true, generic names are listed
      *       branded : boolean. If true, branded product names are listed
      *       composites : if true, generic composite drugs are listed
      *
      *@return array of structs alphabetically sorted by name with the following minimum keys:
      *       pkey: integer. Primary key
      *       Name: string. Name of the drug
      *       Type: string(2):
      *         'cl' = class
      *         'ca' = anatomical class
      *         'cc' = chemical class,
      *         'ct' = therapeutic class
      *         'ge' = generic
      *         'gc' = composite generic (e.g. Co-Trimoxazole)
      *         'bp' = branded product
      *       If the parameter return_tags was given in the query, tags will be available too.
      */
     public Vector list_drugs(String searchexpr,Hashtable  tags){
         Vector params = new Vector();
         params.addElement(searchexpr);
         //params.addElement(tags);
         //Vector vec = (Vector) callWebservice("list_drugs",params); 
         Vector vec = (Vector) callWebservice("list_search_element",params);
         
         
         return vec;         
     }
         
     
         
     
     
     public Hashtable tagCreatorEx(int sources,String languages,String countries,int authors,Date  modified_after,boolean return_tags ){      
        Hashtable retHash = new Hashtable();
           retHash.put("source",new Integer(0));             
           retHash.put("sources",new Integer(sources));             
           retHash.put("language", "");
           retHash.put("languages", new Integer(languages));
           retHash.put("country","");
           retHash.put("countries",new Integer(countries));
           retHash.put("author", new Integer(0));
           retHash.put("authors", new Integer(authors));
           try{
           retHash.put("modified_after", new SimpleDateFormat("yyyy-MM-dd").parse(modified_after.toString()));            
           }catch (Exception e){
        	   MiscUtils.getLogger().error("error",e);
           }
           retHash.put("return_tags",Boolean.toString(return_tags));      //If true, the values returned by a query will include applicable tag bitstrings for each returned value (will slow down query considerably, but allows client-side sub-filtering)
           return retHash;
     }
     
     public Hashtable tagCreatorEx(int sources,String languages,String countries,int authors,boolean return_tags ){      
        Hashtable retHash = new Hashtable();
           retHash.put("source",new Integer(0));             
           retHash.put("sources",new Integer(sources));             
           retHash.put("language", "");
           retHash.put("languages", new Integer(languages));
           retHash.put("country","");
           retHash.put("countries",new Integer(countries));
           retHash.put("author", new Integer(0));
           retHash.put("authors", new Integer(authors));      
           retHash.put("return_tags",Boolean.toString(return_tags));      //If true, the values returned by a query will include applicable tag bitstrings for each returned value (will slow down query considerably, but allows client-side sub-filtering)
           return retHash;
     }
     
     
     
     /**For Creating tags 
      *@param source Primary key of the referenced information source (Drugref, MIMS, MULTUM, AMIS, Manufacturer, Inhouse, ...)
      *@param language  string. Three character ISO language code
      *@param country  string. Two character ISO country code
      *@param author  integer. Primary key of the submitter of the referenced information
      *@param modified_after  string. ISO date (yyyy-mm-dd). If set, records older than this date will be ignored.
      *@param return_tags  boolean. If true, the values returned by a query will include applicable tag bitstrings for each returned value (will slow down query considerably, but allows client-side sub-filtering)
      *@return Hashtable with values set from input for tags
      */
     public Hashtable tagCreator(int source,String language,String country,int author,Date  modified_after,boolean return_tags ){
        Hashtable retHash = new Hashtable();
            retHash.put("source",new Integer(source));             
            retHash.put("language", language);
            retHash.put("country",country);
            retHash.put("author", new Integer(author));
            try{
            retHash.put("modified_after", new SimpleDateFormat("yyyy-MM-dd").parse(modified_after.toString()));            
            }catch (Exception e){
            	 MiscUtils.getLogger().error("error",e);
            }
            retHash.put("return_tags",Boolean.toString(return_tags));      //If true, the values returned by a query will include applicable tag bitstrings for each returned value (will slow down query considerably, but allows client-side sub-filtering)
            return retHash;
     }
     
     
     /**For Creating tags 
      *@param source Primary key of the referenced information source (Drugref, MIMS, MULTUM, AMIS, Manufacturer, Inhouse, ...)
      *@param language  string. Three character ISO language code
      *@param country  string. Two character ISO country code
      *@param author  integer. Primary key of the submitter of the referenced information
      *@param return_tags  boolean. If true, the values returned by a query will include applicable tag bitstrings for each returned value (will slow down query considerably, but allows client-side sub-filtering)
      *@return Hashtable with values set from input for tags
      */
     public Hashtable tagCreator(int source,String language,String country,int author,boolean return_tags ){
        Hashtable retHash = new Hashtable();
            retHash.put("source",new Integer(source));             
            retHash.put("language", language);
            retHash.put("country",country);
            retHash.put("author", new Integer(author));            
            retHash.put("return_tags",Boolean.toString(return_tags));      //If true, the values returned by a query will include applicable tag bitstrings for each returned value (will slow down query considerably, but allows client-side sub-filtering)
            return retHash;
     }
     
     
     

     
      /**returns a fuill drug monograph formatted as HTML page, with all headings (= keys returned by get_drug) implemented as anchors.
       *@param pkeye4 primary key.
       *@param css CSS style sheet used to format the retunred HTML page. Details not finalized yet.
       *@return base64 encoded HTML page
       */
     	@Deprecated
       public Base64 get_drug_html(int pkeye4,Base64 css){ //returns base64
           return  new Base64();
       }



       /**returns all available products for a given drug as identified by pkey and constrained by tags
        *@param pkey primary key of a drug.
        *@param tags see [tags].
        *@return list of structs containing the following minimum keys:
        *           brandname : string
        *           form : string. (tablets, capsules, ...)
        *           strength : string. Brief human readable format
        *           package_size : string. Brief human readable format
        *           subsidies : string. Brief human readable format
        *           manufacturer : string. Company name
        */
       @Deprecated
       public Vector list_products(int pkey, Hashtable tags ){
           return new Vector();
       }

       /**returns product specific information for a given drug as identified by pkey and constrained by tags, including available package sizes and strengths, manufacturers, prices and available subsidies as well as legal / subsidy access restrictions.
        *
        *@param pkey primary key of a specific drug product
        *@param tags see [tags]
        *
        *@return returns the same struct as get_drug(), but with the following additional keys:
        *           form : integer. Primary key of drug forms tablets, capsules, syrup ...)
        *           form_str : string. Drug form in clear text
        *           units: struct. Key (string) is the generic ingredient, value (string) is the SI unit for the strength (mg, ml ...)
        *           strength : struct. Key (string) is the generic ingredient, value (Real) is the strength in units as stated above
        *           pkg_units : string
        *           pkg_size : real
        *           subsidies : struct. Key is name of subsidy, value is character:
        *                   y=yes
        *                   n=no
        *                   c=conditional
        *           subsidy_conditions : struct. Key is name of subsidy, value is a string (conditions in human readable text)
        *           subsidy_gap : struct. Key is name of subsidy, value (Real ) is th amount
        *           brand_price_premium : struct. Key is applicability (all, pensioners ), value (Real) is the price
        *           prices : struct. Key is price category (retail, wholesale, subsidized), value (Real) is the price
        *           currency : string. Currency the stated price / gap / premium is based on
        */
       @Deprecated
       public Hashtable get_product(int pkey,Hashtable tags){
           return new Hashtable();
       }




       /**returns the Consumer Product Information formatted as HTML, base64 encoded
        *@param pkey primary key of a drug product.
        *@param css CSS style sheet used to format the returned HTML page. Details not finalized yet.
        *@return base64 encoded HTML page
        */
       @Deprecated
       public Base64 get_product_CPI(int pkey,Base64 css){
           return new Base64();
       }

       /**returns an array of structs describing the possible interactions between any two drugs contained in drugs. Information used for interaction checking constrained by tags.
        *@param drugs array of integers. Primary keys of drugs
        *@param tags see [tags].
        *@return array of structs with the following minimum keys:
        *       affecting_drug : integer. Primary Key
        *       affected_drug : integer. Primary key.
        *       effect : string. Single character:
        *           a = augments
        *           i = inhibits
        *           n = no effect
        *           c = conflicting evidence
        *       clinical_effect : boolean. If false, the effect has no bearing on clinical situations
        *       significance : integer. Clinical significance graded 1-3, 1=mild, 2=moderate, 3=severe
        *       evidence : integer. Level of evidence graded 1-3, 1=poor, 2=fair, 3=good
        *       reference : integer. Primary key of reference
        */
       @Deprecated
       public Vector list_interactions(Vector drugs,Hashtable tags){
            return new Vector();
       }

       
       /**List all conditions and their codes / coding systems known to drugref, constrained by searchexpr as welll as by tags. Searchexpr accepts % as wildcard.
        *
        */
       @Deprecated
       public Vector list_conditions(String searchexpr,Hashtable tags){           
           return new Vector();
       }
       
       /**
        *@param indication : integer. Primary key.
        */
       @Deprecated
       public Vector list_drugs_for_indication(int indication ,Hashtable tags){
           return new Vector();
       }
       
       
       /**List all references this drugref database is based on. 
        *@param tags see [tags].
        *@return array of structs with following minimum keys:
        *           name : string. short name of reference source
        *           full title : string.
        *           authors : string
        *           publ_year : string
        */ 
       @Deprecated
       public Vector list_references(Hashtable tags){
           return new Vector();
       }
       //////DRUGREF Second Gen API 
       
       public static void removeNullFromVector(Vector v){
          while(v != null && v.contains(null)){
             v.remove(null);       
          }
       }
       
       public Vector getInteractions(Vector drugs)throws Exception{
         removeNullFromVector(drugs);
         Vector params = new Vector();
         params.addElement("interactions_byATC");         
         params.addElement(drugs);         
         //Vector vec = (Vector) callWebserviceLite("get",params); 
         Vector vec = new Vector();
         Object obj =  callWebserviceLite("fetch",params); 
         if (obj instanceof Vector){
            vec = (Vector) obj;
         }else if(obj instanceof Hashtable){
            Object holbrook = ((Hashtable) obj).get("Holbrook Drug Interactions");
            if (holbrook instanceof Vector){
               vec = (Vector) holbrook;   
            }                       
            Enumeration e = ((Hashtable) obj).keys();
            while (e.hasMoreElements()){
               String s = (String) e.nextElement();
               MiscUtils.getLogger().debug(s+" "+((Hashtable) obj).get(s)+" "+((Hashtable) obj).get(s).getClass().getName());
            }
         }
         
         return vec;
     }
       
    public Vector getAlergyWarnings(String drugs,Vector allergies)throws Exception{
         Vector params = new Vector();
         params.addElement(drugs);
         params.addElement(allergies);
         Vector vec = (Vector) callWebserviceLite("get_allergy_warnings",params);             
         return vec;		         
     }
    
    public Vector getAllergyClasses(Vector allergies)throws Exception{
        Vector params = new Vector();
        params.addElement(allergies);
        Vector vec = (Vector) callWebserviceLite("get_allergy_classes",params);             
        return vec;		         
    }


    public Vector getInactiveDate(String din) throws Exception{
        Vector params = new Vector();
        params.addElement(din);
        Vector vec = (Vector) callWebserviceLite("get_inactive_date",params);
        return vec;
    }
}
