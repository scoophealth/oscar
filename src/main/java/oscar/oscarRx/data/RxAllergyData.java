  
/*
 * 
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
 * This software is published under the GPL GNU General Public License. 
 * This program is free software; you can redistribute it and/or 
 * modify it under the terms of the GNU General Public License 
 * as published by the Free Software Foundation; either version 2 
 * of the License, or (at your option) any later version. * 
 * This program is distributed in the hope that it will be useful, 
 * but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
 * GNU General Public License for more details. * * You should have received a copy of the GNU General Public License 
 * along with this program; if not, write to the Free Software 
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. * 
 * 
 * <OSCAR TEAM>
 * 
 * This software was written for the 
 * Department of Family Medicine 
 * McMaster University 
 * Hamilton 
 * Ontario, Canada 
 */

package oscar.oscarRx.data;

import org.oscarehr.util.MiscUtils;

public class RxAllergyData {
    public Allergy getAllergy(String DESCRIPTION, int HICL_SEQNO, int HIC_SEQNO,
    int AGCSP, int AGCCS, int TYPECODE) {
        return new Allergy(DESCRIPTION, HICL_SEQNO, HIC_SEQNO, AGCSP, AGCCS, TYPECODE);
    }
    
    public Allergy getAllergy(){
        return new Allergy();
    }
    
    
    public class Allergy {
        int PickID;          
        String DESCRIPTION;
        int HICL_SEQNO;
        int HIC_SEQNO;
        int AGCSP;
        int AGCCS;
        int TYPECODE;
        String reaction;
        java.util.Date startDate = null;
        String ageOfOnset = null;
        String severityOfReaction = null;
        String onSetOfReaction = null;
        private String regionalIdentifier = null;
        private String lifeStage = null;
        
        public String getLifeStage() {
        	return lifeStage;
        }

		public void setLifeStage(String lifeStage) {
        	this.lifeStage = lifeStage;
        }

		public Allergy(){
        }
        
        public Allergy(int PickID, String DESCRIPTION, int HICL_SEQNO, int HIC_SEQNO,
        int AGCSP, int AGCCS, int TYPECODE) {
            this.PickID = PickID;
            this.DESCRIPTION = DESCRIPTION;
            this.HICL_SEQNO = HICL_SEQNO;
            this.HIC_SEQNO = HIC_SEQNO;
            this.AGCSP = AGCSP;
            this.AGCCS = AGCCS;
            this.TYPECODE = TYPECODE;
        }
        
        public Allergy(String DESCRIPTION, int HICL_SEQNO, int HIC_SEQNO,
        int AGCSP, int AGCCS, int TYPECODE) {
            this(0, DESCRIPTION, HICL_SEQNO, HIC_SEQNO, AGCSP, AGCCS, TYPECODE);
        }
        
        public String getReaction() {
            return this.reaction;
        }
        
        public void setReaction(String reaction) {
            this.reaction = reaction;
        }
        
        public int getPickID() {
            return this.PickID;
        }
        public void setPickID(int RHS) {
            this.PickID=RHS;
        }
        
        public String getDESCRIPTION() {
            return this.DESCRIPTION;
        }
        
  
        /**
         * use to create shorten descriptions of alergy.
         *
         * ie BENZOIC ACID/ AGRIMONY/ GOLDENROD/ PAREIRA BRAVA/
         *
         * with maxlength 13 and shorted 8 and added "..."
         *
         * would equal
         *
         * BENZOIC ...
         * @param maxlength The maximum string length before truncating the string
         * @param shortend length string will be truncated if maxlength is met
         * @param added string added to original string if maxlength is met.  ie ...
         * @return either full description if its less than maxlength or shortened string if its not
         */         
        public String getShortDesc(int maxlength, int shorted, String added){
           String desc = this.getDESCRIPTION();                      
           if( (maxlength > shorted) && (desc.length() > maxlength) ){
              desc = desc.substring(0, 8) + "..." ;
           }           
           return desc;
        }
        
        public void setDESCRIPTION(String desc) {
            this.DESCRIPTION = desc;
        }
        
        public int getHICL_SEQNO() {
            return this.HICL_SEQNO;
        }
        
        public int getHIC_SEQNO() {
            return this.HIC_SEQNO;
        }
        
        public int getAGCSP() {
            return this.AGCSP;
        }
        
        public int getAGCCS() {
            return this.AGCCS;
        }
        
        public int getTYPECODE() {
            return this.TYPECODE;
        }
        public void setTYPECODE(int typecode) {
            this.TYPECODE=typecode;
        }
        
        //Used for LogAction to insert into data column of log table
        public String getAuditString() {
            return getAllergyDisp();
        }
        
        public String getAllergyDisp() {
            return this.DESCRIPTION + " (" + this.getTypeDesc() + ")";
        }
        
        public String getTypeDesc() {
            return(RxAllergyData.getTypeDesc(this.TYPECODE));
        }
        
	
	public java.util.Date getStartDate() {
	    return startDate;
	}
	
	public void setStartDate(java.util.Date startDate) {
	    this.startDate = startDate;
	}
	
        /**
         * Getter for property ageOfOnset.
         * @return Value of property ageOfOnset.
         */
        public java.lang.String getAgeOfOnset() {
           return ageOfOnset;
        }
	
        /**
         * Setter for property ageOfOnset.
         * @param ageOfOnset New value of property ageOfOnset.
         */
        public void setAgeOfOnset(java.lang.String ageOfOnset) {
           this.ageOfOnset = ageOfOnset;
        }
        
        /**
         * Getter for property severityOfReaction.
         * @return Value of property severityOfReaction.
         */
        public java.lang.String getSeverityOfReaction() {
           return severityOfReaction;
        }
   
        public String getSeverityOfReactionDesc(){       
        	return(RxAllergyData.getSeverityOfReactionDesc(getSeverityOfReaction()));
        }        
        
        /**
         * Setter for property severityOfReaction.
         * @param severityOfReaction New value of property severityOfReaction.
         */
        public void setSeverityOfReaction(java.lang.String severityOfReaction) {
           this.severityOfReaction = severityOfReaction;
        }
        
        /**
         * Getter for property onSetOfReaction.
         * @return Value of property onSetOfReaction.
         */
        public java.lang.String getOnSetOfReaction() {
           return onSetOfReaction;
        }
        
        public String getOnSetOfReactionDesc(){
            return(RxAllergyData.getOnSetOfReactionDesc(getOnSetOfReaction()));
         }
        
        
        
        public String getLifeStageDesc(){
        	String s = getLifeStage();
        	if("N".equals(s)){
        		return "Newborn"; //"oscarEncounter.lifestage.opt.newborn"; //  = Newborn: Birth to 28 days
        	}else if ("I".equals(s)){
        		return "Infant";  // "oscarEncounter.lifestage.opt.infant"; // = Infant: 29 days to 2 years
        	}else if ("C".equals(s)){
        		return "Child"; //"oscarEncounter.lifestage.opt.child"; // = Child: 2 years to 15 years
        	}else if ("T".equals(s)){
        		return "Adolescent"; //  "oscarEncounter.lifestage.opt.adolescent"; // = Adolescent: 16 to 17 
        	}else if ("A".equals(s)){
        		return "Adult";  //"oscarEncounter.lifestage.opt.adult"; // = Adult: 18 years
        	}
        		return "Not Set"; //"oscarEncounter.lifestage.opt.notset"; // = Not Set   		
        }
        
        
        /**
         * Setter for property onSetOfReaction.
         * @param onSetOfReaction New value of property onSetOfReaction.
         */
        public void setOnSetOfReaction(java.lang.String onSetOfReaction) {
           this.onSetOfReaction = onSetOfReaction;
        }

        public String getRegionalIdentifier() {
            return regionalIdentifier;
        }

        public void setRegionalIdentifier(String regionalIdentifier) {
            this.regionalIdentifier = regionalIdentifier;
        }
        
    }
    
    public class Reaction {
        int GCN_SEQNO;
        Allergy allergy;
        int severity;
        
        public Reaction(int GCN_SEQNO, Allergy allergy, int severity) {
            this.GCN_SEQNO = GCN_SEQNO;
            this.allergy = allergy;
            this.severity = severity;
        }
        
        public int getGCN_SEQNO() {
            return this.GCN_SEQNO;
        }
        public String getGenericName() {
            try{                
            return new RxDrugData().getGenericName(this.GCN_SEQNO);
            }catch(Exception e){ 
                MiscUtils.getLogger().error("Error", e);                
            }
            return "";
        }
        public Allergy getAllergy() {
            return this.allergy;
        }
        public int getSeverity() {
            return severity;
        }
    }
    
    public static String getOnSetOfReactionDesc(String onsetCode){
        if ("1".equals(onsetCode)) return("Immediate");
        if ("2".equals(onsetCode)) return("Gradual");
        if ("3".equals(onsetCode)) return("Slow");
        else return("Unknown "+onsetCode);
     }

    public static String getTypeDesc(int typeCode) {
        String s;
        /** 6 |  1 | generic
            7 |  2 | compound
            8 |  3 | brandname
            9 |  4 | ther_class
           10 |  5 | chem_class
           13 |  6 | ingredient
        **/
        switch(typeCode) {
            /*
            *|  8 | anatomical class
            *|  9 | chemical class
            *| 10 | therapeutic class
            *| 11 | generic
            *| 12 | composite generic
            *| 13 | branded product
            *| 14 | ingredient
            */
            case 11:
                s = "Generic Name";
                break;                
            case 12:
                s = "Compound";
                break;
            case 13:
                s = "Brand Name";
                break;
            case 8:
                s = "ATC Class";
                break;
            case 10:
                s = "AHFS Class";
                break;
            case 14:
                s = "Ingredient";
                break;
            default:
                s = "";
        }
        return s;
    }

    //TODO: NEEDS I18N
    public static String getSeverityOfReactionDesc(String severityCode){           
        if ("1".equals(severityCode)) return("Mild");
        if ("2".equals(severityCode)) return("Moderate");
        if ("3".equals(severityCode)) return("Severe");
        else return("Unknown "+severityCode);
    }

}