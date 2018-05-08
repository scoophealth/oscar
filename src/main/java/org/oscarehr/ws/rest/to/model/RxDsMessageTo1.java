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
package org.oscarehr.ws.rest.to.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.oscarehr.util.MiscUtils;


@XmlRootElement
public class RxDsMessageTo1 implements Serializable {
	
	@Override
    public String toString(){
		return(ReflectionToStringBuilder.toString(this));
	}

	
	//////////////
	/*
	   if(significanceStr==null || significanceStr.equals("")) {significanceStr="0";}
	   int significance = Integer.valueOf(significanceStr);
	 */
	private String id;
	private String summary;
	private String heading;
	private String messageSource;
	private String significanceStr = "0";
	private String evidence;
	private int significance = 0;
	private String effectStr;
	private String effect;
	private String name;
    private String drug2;
    private Date updated_at;
    private Date created_at;
    private String interactStr="";
    private Boolean trustedResource;
    private Boolean agree;
    	//List<Map> commentsVec; //= (Map) ht.get("comments");
    private String type;
    private String author;
    private Integer updated_by;
    private String atc;
    private String atc2;
    private Integer created_by;
    private String reference;
    private String body;
    private String management;
    private String copyright;
    private boolean hidden =false;
    	
    //	RETURNED [{drugs=[], atc=C09AA01, , updated_by=7, trusted=true, atc2=M04AA01, created_by=7, reference=Holbrook Interactions, comments=[], drug2=ALLOPURINOL, agree=false, effect=A, id=2458, evidence=P, name=CAPTOPRIL, }, {drugs=[], atc=M04AA01, created_at=Fri Jun 19 18:01:15 EDT 2009, significance=2, type=Interaction, author=David Chan, updated_by=7, trusted=true, atc2=B01AA03, created_by=7, reference=Holbrook Interactions, comments=[], drug2=WARFARIN, body=, agree=false, effect=A, id=1693, evidence=P, name=ALLOPURINOL, updated_at=Wed Jun 24 21:02:42 EDT 2009}, {drugs=[], atc=M04AA01, created_at=Fri Oct 26 16:41:29 EDT 2007, significance=2, type=Interaction, author=David Chan, updated_by=1, trusted=true, atc2=B01AA03, created_by=7, reference=Holbrook Drug Interactions, comments=[], drug2=WARFARIN, body=., agree=false, effect=A, id=1469, evidence=P, name=ALLOPURINOL, updated_at=Wed Jun 24 21:03:05 EDT 2009}]

    	

    	public RxDsMessageTo1() {}
    	
    	public RxDsMessageTo1(Map ht,ResourceBundle mr, Locale locale) {
    	
    		name = (String) ht.get("name");
    		updated_at =(Date)ht.get("updated_at");
    		if(ht.get("id") instanceof String) {
    			//logger.error("---$string instance");
    			id = (String) ht.get("id");
    		}else if(ht.get("id") instanceof Integer) {
    			id = ((Integer) ht.get("id")).toString();
    		}
    		significanceStr = (String)ht.get("significance");
    		try {
    			significance = Integer.valueOf(significanceStr);
    		}catch(Exception e) {
    			significance = 0;
    		}
    		
    		
    		effect=(String)ht.get("effect");
    		effectStr=(String)ht.get("effect");
    		if(effect!=null){
    			if(effect.equals("a"))
    				effect=mr.getString("oscarRx.interactions.msgAugmentsNoClinical");
    			else if(effect.equals("A"))
    				effect=mr.getString("oscarRx.interactions.msgAugments");
    			else if(effect.equals("i"))
    				effect=mr.getString("oscarRx.interactions.msgInhibitsNoClinical");
    			else if(effect.equals("I"))
    				effect=mr.getString("oscarRx.interactions.msgInhibits");
    			else if(effect.equals("n"))
    				effect=mr.getString("oscarRx.interactions.msgNoEffect");
    			else if(effect.equals("N"))
    				effect=mr.getString("oscarRx.interactions.msgNoEffect");
    			else if(effect.equals(" "))
    				effect=mr.getString("oscarRx.interactions.msgUnknownEffect");
    			interactStr=ht.get("name")+" "+effect+" "+ht.get("drug2");
    		}else {
    			if(ht.containsKey("effectdesc")) {
    				effect=(String) ht.get("effectdesc");
    			}
    		}
    		
    		MiscUtils.getLogger().error("WHATS inn ht"+ht);
    		type = (String) ht.get("type");
        	author = (String) ht.get("author");
        	updated_by = (Integer) ht.get("updated_by");
        	atc = (String) ht.get("atc");
        	atc2 = (String) ht.get("atc2");
        	created_by = (Integer) ht.get("created_by");
        	reference = (String) ht.get("reference");
        	body = (String) ht.get("body");
        	drug2 = (String) ht.get("drug2");
        	created_at = (Date) ht.get("created_at");
        	trustedResource = (Boolean) ht.get("trusted");
        	agree = (Boolean) ht.get("agree");
        	evidence = (String) ht.get("evidence");
         
     
             
    		
    	}
/*

   String significance(String s){
       Map<String,String> h = new HashMap<String,String>();
       h.put("1","minor");
       h.put("2","moderate");
       h.put("3","major");
       h.put(" ","unknown");

       String retval=null;
       if(s!=null){
         retval = (String) h.get(s);
         if (retval == null) {
            retval = "unknown";
         }
       }else retval = "unknown";
        return retval;
   }

   String evidence(String s){
       Map<String,String> h = new HashMap<String,String>();
       h.put("P","poor");
       h.put("F","fair");
       h.put("G","good");
       h.put(" ","unknown");

       String retval;
      if(s!=null){
           retval = (String) h.get(s);
        if (retval == null) {retval = "unknown";}
      }
       else retval="unkown";
        return retval;
   }


   String sigColor(String s){
       Map<String,String> h = new HashMap<String,String>();
       h.put("1","yellow");
       h.put("2","orange");
       h.put("3","red");
       h.put(" ","greenyellow");
       String retval;
       if(s!=null){
            retval = (String) h.get(s);
            if (retval == null) {retval = "greenyellow";}
       }else retval = "greenyellow";
        return retval;
   }

   String severityOfReaction(String s){
       Map<String,String> h = new HashMap<String,String>();
       h.put("1","Mild");
       h.put("2","Moderate");
       h.put("3","Severe");

       String retval = (String) h.get(s);
       if (retval == null) {retval = "Unknown";}
       return retval;
   }

   String severityOfReactionColor(String s){
       Map<String,String> h = new HashMap<String,String>();
       h.put("1","yellow");
       h.put("2","orange");
       h.put("3","red");

       String retval = (String) h.get(s);
       if (retval == null) {retval = "red";}
       return retval;
   }

   String onSetOfReaction(String s){
       Map<String,String> h = new HashMap<String,String>();
       h.put("1","Immediate");
       h.put("2","Gradual");
       h.put("3","Slow");

       String retval = (String) h.get(s);
       if (retval == null) {retval = "Unknown";}
       return retval;
   }

   String displayKeys(Map ht) {
       StringBuilder sb = new StringBuilder();
       if (ht != null){
            for (Object o :ht.keySet()){
                String s  = "key:"+o+" val "+ht.get(o)+"  class : "+ht.get(o).getClass().getName();
                sb.append(s);

                if ( o instanceof Map){
                   Map v =  (Map) o;
                }
            }
       }

       return sb.toString();
   }

  boolean trusted(Object o){
           boolean b = false;
           if (o != null && o instanceof Boolean){
              Boolean c  = (Boolean) o;
              b = c.booleanValue();
           }
           return b;
       }
*/

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getSignificanceStr() {
			return significanceStr;
		}

		public void setSignificanceStr(String significanceStr) {
			this.significanceStr = significanceStr;
		}

		public int getSignificance() {
			return significance;
		}

		public void setSignificance(int significance) {
			this.significance = significance;
		}

		public String getEffect() {
			return effect;
		}

		public void setEffect(String effect) {
			this.effect = effect;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getDrug2() {
			return drug2;
		}

		public void setDrug2(String drug2) {
			this.drug2 = drug2;
		}

		public Date getUpdated_at() {
			return updated_at;
		}

		public void setUpdated_at(Date updated_at) {
			this.updated_at = updated_at;
		}

		public Date getCreated_at() {
			return created_at;
		}

		public void setCreated_at(Date created_at) {
			this.created_at = created_at;
		}

		public String getInteractStr() {
			return interactStr;
		}

		public void setInteractStr(String interactStr) {
			this.interactStr = interactStr;
		}

		public Boolean getTrustedResource() {
			return trustedResource;
		}

		public void setTrustedResource(Boolean trustedResource) {
			this.trustedResource = trustedResource;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getAuthor() {
			return author;
		}

		public void setAuthor(String author) {
			this.author = author;
		}

		public Integer getUpdated_by() {
			return updated_by;
		}

		public void setUpdated_by(Integer updated_by) {
			this.updated_by = updated_by;
		}

		public String getAtc() {
			return atc;
		}

		public void setAtc(String atc) {
			this.atc = atc;
		}

		public String getAtc2() {
			return atc2;
		}

		public void setAtc2(String atc2) {
			this.atc2 = atc2;
		}

		public Integer getCreated_by() {
			return created_by;
		}

		public void setCreated_by(Integer created_by) {
			this.created_by = created_by;
		}

		public String getReference() {
			return reference;
		}

		public void setReference(String reference) {
			this.reference = reference;
		}

		public String getBody() {
			return body;
		}

		public void setBody(String body) {
			this.body = body;
		}

		public String getEvidence() {
			return evidence;
		}

		public void setEvidence(String evidence) {
			this.evidence = evidence;
		}

		public String getEffectStr() {
			return effectStr;
		}

		public void setEffectStr(String effectStr) {
			this.effectStr = effectStr;
		}

		public Boolean getAgree() {
			return agree;
		}

		public void setAgree(Boolean agree) {
			this.agree = agree;
		}

		public String getSummary() {
			return summary;
		}

		public void setSummary(String summary) {
			this.summary = summary;
		}

		public String getManagement() {
			return management;
		}

		public void setManagement(String management) {
			this.management = management;
		}

		public String getCopyright() {
			return copyright;
		}

		public void setCopyright(String copyright) {
			this.copyright = copyright;
		}

		public boolean isHidden() {
			return hidden;
		}

		public void setHidden(boolean hidden) {
			this.hidden = hidden;
		}

		public String getHeading() {
			return heading;
		}

		public void setHeading(String heading) {
			this.heading = heading;
		}

		public String getMessageSource() {
			return messageSource;
		}

		public void setMessageSource(String messageSource) {
			this.messageSource = messageSource;
		}

   }   