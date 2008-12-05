  
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

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Vector;

import org.apache.commons.lang.StringEscapeUtils;

import oscar.OscarProperties;
import oscar.oscarDB.DBHandler;
import oscar.oscarProvider.data.ProSignatureData;
import oscar.oscarRx.util.RxUtil;
import org.oscarehr.util.DbConnectionFilter;

public class RxPrescriptionData {
    public Prescription getPrescription(int drugId) {
        Prescription prescription = null;
        
        try {
            //Get Prescription from database
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
            ResultSet rs;
            String sql = "SELECT * FROM drugs WHERE drugid = " + drugId;
            
            rs = db.GetSQL(sql);
            
            if (rs.next()) {
                prescription = new Prescription(drugId,
                db.getString(rs,"provider_no"), rs.getInt("demographic_no"));
                prescription.setRxDate(rs.getDate("rx_date"));
                prescription.setRxCreatedDate(rs.getDate("create_date"));
                prescription.setEndDate(rs.getDate("end_date"));
                prescription.setBrandName(db.getString(rs,"BN"));
                prescription.setGCN_SEQNO(rs.getInt("GCN_SEQNO"));
                prescription.setCustomName(db.getString(rs,"customName"));
                prescription.setTakeMin(rs.getFloat("takemin"));
                prescription.setTakeMax(rs.getFloat("takemax"));
                prescription.setFrequencyCode(db.getString(rs,"freqcode"));
                prescription.setDuration(db.getString(rs,"duration"));
                prescription.setDurationUnit(db.getString(rs,"durunit"));
                prescription.setQuantity(db.getString(rs,"quantity"));
                prescription.setRepeat(rs.getInt("repeat"));
                prescription.setNosubs(rs.getInt("nosubs"));
                prescription.setPrn(rs.getInt("prn"));
                prescription.setSpecial(db.getString(rs,"special"));
                prescription.setGenericName(db.getString(rs,"GN"));
                prescription.setAtcCode(db.getString(rs,"ATC"));
                prescription.setScript_no(db.getString(rs,"script_no"));
                prescription.setRegionalIdentifier(db.getString(rs,"regional_Identifier"));
                prescription.setUnit(db.getString(rs,"unit"));
                prescription.setUnitName(db.getString(rs,"unitName"));
                prescription.setMethod(db.getString(rs,"method"));
                prescription.setRoute(db.getString(rs,"route"));
                prescription.setCustomInstr(rs.getBoolean("custom_instructions"));
                prescription.setDosage(db.getString(rs,"dosage"));
            }
            
            
            rs.close();            
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        finally {
            DbConnectionFilter.releaseThreadLocalDbConnection();
        }
        
        return prescription;
    }
    
    public Prescription newPrescription(String providerNo, int demographicNo) {
        //Create new prescription (only in memory)
        return new Prescription(0, providerNo, demographicNo);
    }
    
    public Prescription newPrescription(String providerNo, int demographicNo, Favorite favorite) {
        //Create new prescription from favorite (only in memory)
        Prescription prescription = new Prescription(0, providerNo, demographicNo);
        
        prescription.setRxDate(RxUtil.Today());
        prescription.setEndDate(null);
        prescription.setBrandName(favorite.getBN());
        prescription.setGCN_SEQNO(favorite.getGCN_SEQNO());
        prescription.setCustomName(favorite.getCustomName());
        prescription.setTakeMin(favorite.getTakeMin());
        prescription.setTakeMax(favorite.getTakeMax());
        prescription.setFrequencyCode(favorite.getFrequencyCode());
        prescription.setDuration(favorite.getDuration());
        prescription.setDurationUnit(favorite.getDurationUnit());
        prescription.setQuantity(favorite.getQuantity());
        prescription.setRepeat(favorite.getRepeat());
        prescription.setNosubs(favorite.getNosubs());
        prescription.setPrn(favorite.getPrn());
        prescription.setSpecial(favorite.getSpecial());
        prescription.setGenericName(favorite.getGN());
        prescription.setAtcCode(favorite.getAtcCode());
        prescription.setRegionalIdentifier(favorite.getRegionalIdentifier());
        prescription.setUnit(favorite.getUnit());
        prescription.setUnitName(favorite.getUnitName());
        prescription.setMethod(favorite.getMethod());
        prescription.setRoute(favorite.getRoute());
        prescription.setCustomInstr(favorite.getCustomInstr());
        prescription.setDosage(favorite.getDosage());
        
        
        return prescription;
    }
    
    public Prescription newPrescription(String providerNo, int demographicNo, Prescription rePrescribe) {
        //Create new prescription
        Prescription prescription = new Prescription(0, providerNo, demographicNo);
        
        prescription.setRxDate(RxUtil.Today());
        prescription.setEndDate(null);
        prescription.setBrandName(rePrescribe.getBrandName());
        prescription.setGCN_SEQNO(rePrescribe.getGCN_SEQNO());
        prescription.setCustomName(rePrescribe.getCustomName());
        prescription.setTakeMin(rePrescribe.getTakeMin());
        prescription.setTakeMax(rePrescribe.getTakeMax());
        prescription.setFrequencyCode(rePrescribe.getFrequencyCode());
        prescription.setDuration(rePrescribe.getDuration());
        prescription.setDurationUnit(rePrescribe.getDurationUnit());
        prescription.setQuantity(rePrescribe.getQuantity());
        prescription.setRepeat(rePrescribe.getRepeat());
        prescription.setNosubs(rePrescribe.getNosubs());
        prescription.setPrn(rePrescribe.getPrn());
        prescription.setSpecial(rePrescribe.getSpecial());
        prescription.setGenericName(rePrescribe.getGenericName());
        prescription.setAtcCode(rePrescribe.getAtcCode());
        prescription.setScript_no(rePrescribe.getScript_no());
        prescription.setRegionalIdentifier(rePrescribe.getRegionalIdentifier());
        prescription.setUnit(rePrescribe.getUnit());
        prescription.setUnitName(rePrescribe.getUnitName());
        prescription.setMethod(rePrescribe.getMethod());
        prescription.setRoute(rePrescribe.getRoute()); 
        prescription.setCustomInstr(rePrescribe.getCustomInstr());
        prescription.setDosage(rePrescribe.getDosage());
        return prescription;
    }
    //JAY CHANGED THIS FUNCTION on desc 3 2002.
    //I changed the sql query from select * from drugs where archived = 0 and demographic_no ....
    //to
    //select * from drugs where demographic_no ...
    //I also added the function getPrescriptionByPatientHideDeleted
    public Prescription[] getPrescriptionsByPatient(int demographicNo) {
        Prescription[] arr = {};
        ArrayList lst = new ArrayList();
        
        try {
            //Get Prescription from database
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
            ResultSet rs;
            String sql = "SELECT * FROM drugs WHERE  "
            + "demographic_no = " + demographicNo + " "
            + "ORDER BY rx_date DESC, drugId DESC";
            
            Prescription p;
            
            rs = db.GetSQL(sql);
            
            while(rs.next()) {
                p = new Prescription(rs.getInt("drugid"), db.getString(rs,"provider_no"), demographicNo);
                p.setRxDate(rs.getDate("rx_date"));
                p.setRxCreatedDate(rs.getDate("create_date"));
                p.setEndDate(rs.getDate("end_date"));
                p.setBrandName(db.getString(rs,"BN"));
                p.setGCN_SEQNO(rs.getInt("GCN_SEQNO"));
                p.setCustomName(db.getString(rs,"customName"));
                p.setTakeMin(rs.getFloat("takemin"));
                p.setTakeMax(rs.getFloat("takemax"));
                p.setFrequencyCode(db.getString(rs,"freqcode"));
                p.setDuration(db.getString(rs,"duration"));
                p.setDurationUnit(db.getString(rs,"durunit"));
                p.setQuantity(db.getString(rs,"quantity"));
                p.setRepeat(rs.getInt("repeat"));
                p.setNosubs(rs.getInt("nosubs"));
                p.setPrn(rs.getInt("prn"));
                p.setSpecial(db.getString(rs,"special"));
                p.setArchived(db.getString(rs,"archived"));
                p.setGenericName(db.getString(rs,"GN"));
                p.setAtcCode(db.getString(rs,"ATC"));
                p.setScript_no(db.getString(rs,"script_no"));
                p.setRegionalIdentifier(db.getString(rs,"regional_identifier"));
                p.setUnit(db.getString(rs,"unit"));
                p.setUnitName(db.getString(rs,"unitName"));
                p.setMethod(db.getString(rs,"method"));
                p.setRoute(db.getString(rs,"route"));
                p.setCustomInstr(rs.getBoolean("custom_instructions"));
                p.setDosage(db.getString(rs,"dosage"));
                lst.add(p);
            }
            
            rs.close();
            
            arr = (Prescription[])lst.toArray(arr);
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        finally {
            DbConnectionFilter.releaseThreadLocalDbConnection();
        }
        
        return arr;
    }
    
    
    /*
     *Limit returned prescriptions to those which have an entry in both drugs and prescription table     
     */
    public Prescription[] getPrescriptionScriptsByPatient(int demographicNo) {
        Prescription[] arr = {};
        ArrayList lst = new ArrayList();
        
        try {
            //Get Prescription from database
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
            ResultSet rs;
            String sql = "SELECT d.*, p.date_printed, p.dates_reprinted FROM drugs d, prescription p WHERE  "
            + "d.demographic_no = " + demographicNo + " and d.script_no = p.script_no "
            + "ORDER BY rx_date DESC, drugId DESC";
            
            Prescription p;
            String datesRePrinted;
            
            rs = db.GetSQL(sql);
            
            while(rs.next()) {
                p = new Prescription(rs.getInt("drugid"), db.getString(rs,"provider_no"), demographicNo);
                p.setRxDate(rs.getDate("rx_date"));
                p.setRxCreatedDate(rs.getDate("create_date"));
                p.setEndDate(rs.getDate("end_date"));
                p.setBrandName(db.getString(rs,"BN"));
                p.setGCN_SEQNO(rs.getInt("GCN_SEQNO"));
                p.setCustomName(db.getString(rs,"customName"));
                p.setTakeMin(rs.getFloat("takemin"));
                p.setTakeMax(rs.getFloat("takemax"));
                p.setFrequencyCode(db.getString(rs,"freqcode"));
                p.setDuration(db.getString(rs,"duration"));
                p.setDurationUnit(db.getString(rs,"durunit"));
                p.setQuantity(db.getString(rs,"quantity"));
                p.setRepeat(rs.getInt("repeat"));
                p.setNosubs(rs.getInt("nosubs"));
                p.setPrn(rs.getInt("prn"));
                p.setSpecial(db.getString(rs,"special"));
                p.setArchived(db.getString(rs,"archived"));
                p.setGenericName(db.getString(rs,"GN"));
                p.setAtcCode(db.getString(rs,"ATC"));
                p.setScript_no(db.getString(rs,"script_no"));
                p.setRegionalIdentifier(db.getString(rs,"regional_identifier"));
                p.setUnit(db.getString(rs,"unit"));
                p.setUnitName(db.getString(rs,"unitName"));
                p.setMethod(db.getString(rs,"method"));
                p.setRoute(db.getString(rs,"route"));
                p.setCustomInstr(rs.getBoolean("custom_instructions"));
                p.setDosage(db.getString(rs,"dosage"));
                
                datesRePrinted = db.getString(rs,"dates_reprinted");                
                if( datesRePrinted != null && datesRePrinted.length() > 0 ) {
                    p.setNumPrints(datesRePrinted.split(",").length);
                }
                else {
                    p.setNumPrints(1);
                }
                
                lst.add(p);
            }
            
            rs.close();
            db.getConnection().close();
            
            arr = (Prescription[])lst.toArray(arr);
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        
        return arr;
    }    
    
    public ArrayList<Prescription> getPrescriptionsByScriptNo(int script_no, int demographicNo) {         
         ArrayList<Prescription> lst = new ArrayList<Prescription>();
         
         try {
            //Get Prescription from database
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
            ResultSet rs;
            String sql = "select drugs.*, p.date_printed, p.dates_reprinted from drugs, prescription p where p.script_no = drugs.script_no and " + 
                    "drugs.script_no = " + script_no;
            
            Prescription p;
            String datesRePrinted;
            
            rs = db.GetSQL(sql);
            
            while(rs.next()) {
                p = new Prescription(rs.getInt("drugid"), db.getString(rs,"provider_no"), demographicNo);
                p.setRxDate(rs.getDate("rx_date"));
                p.setRxCreatedDate(rs.getDate("create_date"));
                p.setEndDate(rs.getDate("end_date"));
                p.setBrandName(db.getString(rs,"BN"));
                p.setGCN_SEQNO(rs.getInt("GCN_SEQNO"));
                p.setCustomName(db.getString(rs,"customName"));
                p.setTakeMin(rs.getFloat("takemin"));
                p.setTakeMax(rs.getFloat("takemax"));
                p.setFrequencyCode(db.getString(rs,"freqcode"));
                p.setDuration(db.getString(rs,"duration"));
                p.setDurationUnit(db.getString(rs,"durunit"));
                p.setQuantity(db.getString(rs,"quantity"));
                p.setRepeat(rs.getInt("repeat"));
                p.setNosubs(rs.getInt("nosubs"));
                p.setPrn(rs.getInt("prn"));
                p.setSpecial(db.getString(rs,"special"));
                p.setGenericName(db.getString(rs,"GN"));
                p.setAtcCode(db.getString(rs,"ATC"));
                p.setScript_no(db.getString(rs,"script_no"));
                p.setRegionalIdentifier(db.getString(rs,"regional_identifier"));
                p.setUnit(db.getString(rs,"unit"));
                p.setUnitName(db.getString(rs,"unitName"));
                p.setMethod(db.getString(rs,"method"));
                p.setRoute(db.getString(rs,"route"));
                p.setCustomInstr(rs.getBoolean("custom_instructions"));
                p.setDosage(db.getString(rs,"dosage"));
                p.setPrintDate(rs.getDate("date_printed"));
                
                datesRePrinted = db.getString(rs,"dates_reprinted");
                if( datesRePrinted != null  && datesRePrinted.length() > 0 )
                    p.setNumPrints(datesRePrinted.split(",").length);
                else
                    p.setNumPrints(1);

                lst.add(p);
            }
            
            rs.close();
            db.getConnection().close();
                        
         }
         catch(SQLException e) {
             e.printStackTrace();
         }
         
         return lst;
    }
    /////
    public Prescription[] getPrescriptionsByPatientHideDeleted(int demographicNo) {
        Prescription[] arr = {};
        ArrayList lst = new ArrayList();
        
        try {
            //Get Prescription from database
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
            ResultSet rs;
            String sql = "SELECT * FROM drugs WHERE archived = 0 AND "
            + "demographic_no = " + demographicNo + " "
            + "ORDER BY rx_date DESC, drugId DESC";
            
            Prescription p;
            
            rs = db.GetSQL(sql);
            //java.sql.Connection conn = db.GetConnection();
            //conn.createStatement()
            
            while(rs.next()) {
                p = new Prescription(rs.getInt("drugid"), db.getString(rs,"provider_no"), demographicNo);
                p.setRxDate(rs.getDate("rx_date"));
                p.setRxCreatedDate(rs.getDate("create_date"));
                p.setEndDate(rs.getDate("end_date"));
                p.setBrandName(db.getString(rs,"BN"));
                p.setGCN_SEQNO(rs.getInt("GCN_SEQNO"));
                p.setCustomName(db.getString(rs,"customName"));
                p.setTakeMin(rs.getFloat("takemin"));
                p.setTakeMax(rs.getFloat("takemax"));
                p.setFrequencyCode(db.getString(rs,"freqcode"));
                p.setDuration(db.getString(rs,"duration"));
                p.setDurationUnit(db.getString(rs,"durunit"));
                p.setQuantity(db.getString(rs,"quantity"));
                p.setRepeat(rs.getInt("repeat"));
                p.setNosubs(rs.getInt("nosubs"));
                p.setPrn(rs.getInt("prn"));
                p.setSpecial(db.getString(rs,"special"));
                p.setGenericName(db.getString(rs,"GN"));
                p.setAtcCode(db.getString(rs,"ATC"));
                p.setScript_no(db.getString(rs,"script_no"));
                p.setRegionalIdentifier(db.getString(rs,"regional_identifier"));
                p.setUnit(db.getString(rs,"unit"));
                p.setUnitName(db.getString(rs,"unitName"));
                p.setMethod(db.getString(rs,"method"));
                p.setRoute(db.getString(rs,"route"));
                p.setCustomInstr(rs.getBoolean("custom_instructions"));
                p.setDosage(db.getString(rs,"dosage"));
                lst.add(p);
            }
            
            rs.close();
            
            arr = (Prescription[])lst.toArray(arr);
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        finally {
            DbConnectionFilter.releaseThreadLocalDbConnection();
        }

        return arr;
    }
    
    
    public Vector getCurrentATCCodesByPatient(int demographicNo) {
        Vector vec = new Vector();
        Prescription[] p =  getPrescriptionsByPatientHideDeleted(demographicNo);
        for( int i =0 ; i < p.length; i++){
           if(p[i].isCurrent()){
              System.out.println(p[i].getAtcCode()+" "+p[i].getBrandName());
              if (!vec.contains(p[i].getAtcCode())){
                 System.out.println("Actually Adding "+p[i].getAtcCode()+" "+p[i].getBrandName());
                 if (p[i].isValidAtcCode()){
                    vec.add(p[i].getAtcCode());
                 }
              }
           }
        }
        return vec;
     }
    
    ///////////////////////
    
    public Prescription[] getUniquePrescriptionsByPatient(int demographicNo) {
        Prescription[] arr = {};
        ArrayList lst = new ArrayList();
        
        try {
            //Get Prescription from database
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
            ResultSet rs, rs2;
            String sql = "SELECT * FROM drugs d WHERE d.archived = 0 AND d.demographic_no = " + demographicNo + " ORDER BY rx_date DESC, drugId DESC";
            String indivoSql = "SELECT indivoDocIdx FROM indivoDocs i WHERE i.oscarDocNo = ? and docType = 'Rx' limit 1";
            boolean myOscarEnabled = OscarProperties.getInstance().getProperty("MY_OSCAR", "").trim().equalsIgnoreCase("YES");
            Prescription p;
            
            rs = db.GetSQL(sql);
            
            while(rs.next()) {
                boolean b = true;
                
                for (int i=0; i < lst.size(); i++) {
                    Prescription p2 = (Prescription)lst.get(i);
                    
                    if(p2.getGCN_SEQNO() == rs.getInt("GCN_SEQNO")) {
                        if(p2.getGCN_SEQNO() != 0) // not custom - safe GCN
                        {
                            b = false;
                        }
                        else // custom
                        {
                            if(p2.getCustomName() != null && db.getString(rs,"customName") != null) {
                                if(p2.getCustomName().equals(db.getString(rs,"customName"))) // same custom
                                {
                                    b = false;
                                }
                            }
                        }
                    }
                }
                
                if(b) {
                    p = new Prescription(rs.getInt("drugid"), db.getString(rs,"provider_no"), demographicNo);
                    p.setRxDate(rs.getDate("rx_date"));
                    p.setRxCreatedDate(rs.getDate("create_date"));
                    p.setEndDate(rs.getDate("end_date"));
                    p.setBrandName(db.getString(rs,"BN"));
                    p.setGCN_SEQNO(rs.getInt("GCN_SEQNO"));
                    p.setCustomName(db.getString(rs,"customName"));
                    p.setTakeMin(rs.getFloat("takemin"));
                    p.setTakeMax(rs.getFloat("takemax"));
                    p.setFrequencyCode(db.getString(rs,"freqcode"));
                    p.setDuration(db.getString(rs,"duration"));
                    p.setDurationUnit(db.getString(rs,"durunit"));
                    p.setQuantity(db.getString(rs,"quantity"));
                    p.setRepeat(rs.getInt("repeat"));
                    p.setNosubs(rs.getInt("nosubs"));
                    p.setPrn(rs.getInt("prn"));
                    p.setSpecial(db.getString(rs,"special"));
                    p.setGenericName(db.getString(rs,"GN"));
                    p.setAtcCode(db.getString(rs,"ATC"));
                    p.setScript_no(db.getString(rs,"script_no"));
                    p.setRegionalIdentifier(db.getString(rs,"regional_identifier"));
                    p.setUnit(db.getString(rs,"unit"));
                    p.setUnitName(db.getString(rs,"unitName"));
                    p.setMethod(db.getString(rs,"method"));
                    p.setRoute(db.getString(rs,"route")); 
                    p.setCustomInstr(rs.getBoolean("custom_instructions"));
                    p.setDosage(db.getString(rs,"dosage"));
                    
                    if( myOscarEnabled ) {
                        String tmp = indivoSql.replaceFirst("\\?", db.getString(rs,"drugid"));
                        rs2 = db.GetSQL(tmp);
                        
                        if( rs2.next() ) { 
                            p.setIndivoIdx(rs2.getString("indivoDocIdx"));                                                
                            if( p.getIndivoIdx() != null && p.getIndivoIdx().length() > 0 )
                                p.setRegisterIndivo();
                        }
                        rs2.close();
                    }
                    
                    lst.add(p);
                }
            }
            
            rs.close();
            
            arr = (Prescription[])lst.toArray(arr);
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        finally {
            DbConnectionFilter.releaseThreadLocalDbConnection();
        }
        
        return arr;
    }
    
    public Prescription[] getSimilarPrescriptions(int demographicNo, int GCN_SEQNO, String customName) {
        Prescription[] arr = {};
        ArrayList lst = new ArrayList();
        
        try {
            //Get Prescription from database
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
            ResultSet rs;
            String sql;
            if(GCN_SEQNO!=0) {
                sql = "SELECT * FROM drugs WHERE  "
                + "demographic_no = " + demographicNo + " AND "
                + "GCN_SEQNO = " + GCN_SEQNO + " "
                + "ORDER BY rx_date DESC, drugId DESC";
            }
            else {
                sql = "SELECT * FROM drugs WHERE  "
                + "demographic_no = " + demographicNo + " AND "
                + "GCN_SEQNO = 0 AND customName = '" + customName + "' "
                + "ORDER BY rx_date DESC, drugId DESC";
            }
            
            Prescription p;            
            
            rs = db.GetSQL(sql);
            
            while(rs.next()) {
                p = new Prescription(rs.getInt("drugid"), db.getString(rs,"provider_no"), demographicNo);
                p.setRxDate(rs.getDate("rx_date"));
                p.setRxCreatedDate(rs.getDate("create_date"));
                p.setEndDate(rs.getDate("end_date"));
                p.setBrandName(db.getString(rs,"BN"));
                p.setGCN_SEQNO(rs.getInt("GCN_SEQNO"));
                p.setCustomName(db.getString(rs,"customName"));
                p.setTakeMin(rs.getFloat("takemin"));
                p.setTakeMax(rs.getFloat("takemax"));
                p.setFrequencyCode(db.getString(rs,"freqcode"));
                p.setDuration(db.getString(rs,"duration"));
                p.setDurationUnit(db.getString(rs,"durunit"));
                p.setQuantity(db.getString(rs,"quantity"));
                p.setRepeat(rs.getInt("repeat"));
                p.setNosubs(rs.getInt("nosubs"));
                p.setPrn(rs.getInt("prn"));
                p.setArchived(db.getString(rs,"archived"));
                p.setSpecial(db.getString(rs,"special"));
                p.setGenericName(db.getString(rs,"GN"));
                p.setAtcCode(db.getString(rs,"ATC"));
                p.setScript_no(db.getString(rs,"script_no"));
                p.setRegionalIdentifier(db.getString(rs,"regional_identifier"));
                p.setUnit(db.getString(rs,"unit"));
                p.setUnitName(db.getString(rs,"unitName"));
                p.setMethod(db.getString(rs,"method"));
                p.setRoute(db.getString(rs,"route"));
                p.setCustomInstr(rs.getBoolean("custom_instructions"));
                p.setDosage(db.getString(rs,"dosage"));
                lst.add(p);
            }
            
            rs.close();
            
            arr = (Prescription[])lst.toArray(arr);
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        finally {
            DbConnectionFilter.releaseThreadLocalDbConnection();
        }
        
        return arr;
    }
    
    public Favorite[] getFavorites(String providerNo) {
        Favorite[] arr = {};
        LinkedList lst = new LinkedList();
        
        try {
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
            ResultSet rs;
            Favorite favorite;
            
            rs = db.GetSQL("SELECT * FROM favorites WHERE provider_no = '"
            + providerNo + "' ORDER BY favoritename");
            
            while(rs.next()) {
                favorite = new Favorite(rs.getInt("favoriteid"), db.getString(rs,"provider_no"),
                db.getString(rs,"favoritename"),
                db.getString(rs,"BN"), rs.getInt("GCN_SEQNO"), db.getString(rs,"customName"),
                rs.getFloat("takemin"), rs.getFloat("takemax"), db.getString(rs,"freqcode"),
                db.getString(rs,"duration"), db.getString(rs,"durunit"),
                db.getString(rs,"quantity"),
                rs.getInt("repeat"), rs.getInt("nosubs"),
                rs.getInt("prn"), db.getString(rs,"special"),db.getString(rs,"GN"),db.getString(rs,"ATC"),db.getString(rs,"regional_identifier"),
                db.getString(rs,"unit"),db.getString(rs, "unitName"),db.getString(rs,"method"),db.getString(rs,"route"),rs.getBoolean("custom_instructions"),db.getString(rs,"dosage"));
                
              
                
                lst.add(favorite);
            }
            
            rs.close();
            
            arr = (Favorite[])lst.toArray(arr);
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        finally {
            DbConnectionFilter.releaseThreadLocalDbConnection();
        }
        
        return arr;
    }
    
    public Favorite getFavorite(int favoriteId) {
        Favorite favorite = null;
        
        try {
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
            ResultSet rs;
            
            rs = db.GetSQL("SELECT * FROM favorites WHERE favoriteid = " + favoriteId);
            
            if(rs.next()) {
                favorite = new Favorite(rs.getInt("favoriteid"), db.getString(rs,"provider_no"),
                db.getString(rs,"favoritename"),
                db.getString(rs,"BN"), rs.getInt("GCN_SEQNO"), db.getString(rs,"customName"),
                rs.getFloat("takemin"), rs.getFloat("takemax"), db.getString(rs,"freqcode"),
                db.getString(rs,"duration"), db.getString(rs,"durunit"),
                db.getString(rs,"quantity"),
                rs.getInt("repeat"), rs.getInt("nosubs"),
                rs.getInt("prn"), db.getString(rs,"special"),db.getString(rs,"GN"),db.getString(rs,"ATC"),db.getString(rs,"regional_identifier"),
                db.getString(rs,"unit"),db.getString(rs,"unitName"),db.getString(rs,"method"),db.getString(rs,"route"),rs.getBoolean("custom_instructions"),db.getString(rs,"dosage"));
            }
            
            rs.close();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        finally {
            DbConnectionFilter.releaseThreadLocalDbConnection();
        }
        
        return favorite;
    }
    
    
    
    public boolean deleteFavorite(int favoriteId) {
        boolean ret = false;
        
        try {
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
            String sql = "DELETE FROM favorites WHERE favoriteid = " + favoriteId;
            
            db.RunSQL(sql);
            db.getConnection().close();
            ret = true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        
        return ret;
    }
    
    
    /** This function is used to save a set of prescribed drugs to as one
     * prescription.  This is for historical purposes
     * @param bean This is the oscarRx session bean
     * @return This returns the insert id of the script to be included
     * the drugs table
     */
    public String saveScript(oscar.oscarRx.pageUtil.RxSessionBean bean){
        /*
         *  create table prescription (
         *  script_no int(10) auto_increment primary key,
         *  provider_no varchar(6),
         *  demographic_no int(10),
         *  date_prescribed date,
         *  date_printed date,
         *  dates_reprinted text,
         *  textView text);
         */
        String provider_no    = bean.getProviderNo();
        int demographic_no        = bean.getDemographicNo();
    String date_prescribed = oscar.oscarRx.util.RxUtil.DateToString(oscar.oscarRx.util.RxUtil.Today(), "yyyy/MM/dd");
        String date_printed = date_prescribed;
        
        StringBuffer textView = new StringBuffer();
        String retval = null;
        
        Prescription rx;
        
        /////create full text view
        oscar.oscarRx.data.RxPatientData.Patient patient = null;
        oscar.oscarRx.data.RxProviderData.Provider provider = null;
        try{
           patient = new oscar.oscarRx.data.RxPatientData().getPatient(demographic_no);
           provider = new oscar.oscarRx.data.RxProviderData().getProvider(provider_no);
        }catch(Exception e){
            e.printStackTrace();              
        }
        ProSignatureData sig = new ProSignatureData();
        boolean hasSig = sig.hasSignature(bean.getProviderNo());
        String doctorName = "";
        if (hasSig){
            doctorName = sig.getSignature(bean.getProviderNo());
        }else{
            doctorName = (provider.getFirstName() + ' ' + provider.getSurname());
        }
        
        textView.append(doctorName+"\n");
        textView.append(provider.getClinicName()+"\n");
        textView.append(provider.getClinicAddress()+"\n");
        textView.append(provider.getClinicCity()+"\n");
        textView.append(provider.getClinicPostal()+"\n");
        textView.append(provider.getClinicPhone()+"\n");
        textView.append(provider.getClinicFax()+"\n");
        textView.append(patient.getFirstName()+" "+patient.getSurname()+"\n");
        textView.append(patient.getAddress()+"\n");
        textView.append(patient.getCity()+" "+patient.getPostal()+"\n");
        textView.append(patient.getPhone()+"\n");
        textView.append(oscar.oscarRx.util.RxUtil.DateToString(oscar.oscarRx.util.RxUtil.Today(), "MMMM d, yyyy")+"\n");
        
        String txt;
        for(int i=0;i<bean.getStashSize();i++){
            rx = bean.getStashItem(i);
            txt = rx.getFullOutLine().replaceAll(";","\n");
            textView.append("\n" + txt);            
        }
        //textView.append();        
                        
        String sql =  " insert into prescription "
        +" (provider_no,demographic_no,date_prescribed,date_printed,textView) "
        +" values "
        +" ( '"+provider_no+"', "
        +"   '"+demographic_no+"', "
        +"   '"+date_prescribed+"', "
        +"   '"+date_printed+"', "
        +"   '"+StringEscapeUtils.escapeSql(textView.toString())+"') ";
        try{

            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
                        
            db.RunSQL(sql);
            
            ResultSet rs;
            // NEEDS TO BE UPDATED FOR POSTGRES
            rs = db.GetSQL("SELECT LAST_INSERT_ID() ");
            
            if(rs.next()){
                retval = Integer.toString( rs.getInt(1) );
            }
            db.getConnection().close();
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
                
        System.out.println("INSERT INTO PRESCRIPTION " + retval);
        return retval;
    }
    
    
    
    
//erased an orfin }
public class Prescription {
    int drugId;
    String providerNo;
    int demographicNo;
    java.util.Date rxDate = null;
    java.util.Date rxCreatedDate = null;
    java.util.Date endDate = null;
    java.util.Date printDate = null;
    
    int numPrints = 0;
    
    String BN = null;       //regular
    int GCN_SEQNO = 0;      //regular
    
    String customName = null;   //custom
    
    float takeMin = 0;
    float takeMax = 0;
    String frequencyCode = null;
    String duration = null;
    String durationUnit = null;
    String quantity = null;
    int repeat = 0;
    boolean nosubs = false;
    boolean prn = false;
    String special = null;
    String genericName = null;
    boolean archived = false;  // ADDED BY JAY DEC 3 2002
    String atcCode = null;
    String script_no = null;
    String regionalIdentifier = null;
    String method = null;
    String unit = null; 
    String unitName = null;
    String route = null;
    String dosage = null;
    boolean custom = false;
    private String indivoIdx = null;        //indivo document index for this prescription
    private boolean registerIndivo = false;
    private final String docType = "Rx";
    //RxDrugData.GCN gcn = null;
    
    public Prescription(int drugId, String providerNo, int demographicNo) {
        this.drugId = drugId;
        this.providerNo = providerNo;
        this.demographicNo = demographicNo;
    }
    
    public int getNumPrints() {
        return this.numPrints;
    }
    
    public void setNumPrints(int numPrints) {
        this.numPrints = numPrints;
    }
    
    public java.util.Date getPrintDate() {
        return this.printDate;
    }
    
    public void setPrintDate(java.util.Date printDate) {
        this.printDate = printDate;
    }
    
    public void setScript_no(String script_no) {
        this.script_no = script_no;
    }
    
    public String getScript_no() {
        return this.script_no;
    }
    
    public void setIndivoIdx(String idx) {
        indivoIdx = idx;
    }
    
    public String getIndivoIdx() {
        return indivoIdx;
    }
    
    public void setRegisterIndivo() {
        registerIndivo = true;
    }
    
    public boolean isRegisteredIndivo() {
        return registerIndivo;
    }
    
    public String getGenericName(){
        return genericName;
    }
    
    public void setGenericName(String genericName){
        this.genericName = genericName;
    }
    
    
    //ADDED BY JAY DEC 03 2002
    public boolean isArchived(){
        return this.archived;
    }
    public void setArchived(String tf){
        if (!tf.equals("0")){
            this.archived = true;
        }
    }
    //////////////////////////////
    
    public int getDrugId() {
        return this.drugId;
    }
    
    public String getProviderNo() {
        return this.providerNo;
    }
    
    public int getDemographicNo() {
        return this.demographicNo;
    }
    
    public java.util.Date getRxDate() {
        return this.rxDate;
    }
    
    public void setRxDate(java.util.Date RHS) {
        this.rxDate = RHS;
    }
    
    public java.util.Date getEndDate() {
        return this.endDate;
    }
    
    public void setEndDate(java.util.Date RHS) {
        this.endDate = RHS;
    }
    
    public boolean isCurrent() {
        boolean b = false;
        
        try {
            GregorianCalendar cal = new GregorianCalendar(Locale.CANADA);
            cal.add(GregorianCalendar.DATE, -1);
            
            if (this.getEndDate().after(cal.getTime())) {
                b = true;
            }
        } catch (Exception e) {
            b = false;
        }
        
        return b;
    }
    
    public void calcEndDate() {
        GregorianCalendar cal = new GregorianCalendar(Locale.CANADA);
        int days = 0;
        
        cal.setTime(this.getRxDate());
        
        if (this.getDuration().length() > 0) {
            if (Integer.parseInt(this.getDuration()) > 0) {
                int i = Integer.parseInt(this.getDuration());
                
                if (this.getDurationUnit().equals("D")) {
                    days = i;
                }
                if (this.getDurationUnit().equals("W")) {
                    days = i * 7;
                }
                if (this.getDurationUnit().equals("M")) {
                    days = i * 30;
                }
                
                if (this.getRepeat() > 0) {
                    int r = this.getRepeat();
                    
                    r++;    // if we have a repeat of 1, multiply days by 2
                    
                    days = days * r;
                }
                
                if (days > 0) {
                    cal.add(GregorianCalendar.DATE, days);
                }
            }
        }
        
        this.endDate = cal.getTime();
    }
    
    public String getBrandName() {
        return this.BN;
    }
    
    public void setBrandName(String RHS) {
        this.BN = RHS;
        //this.gcn=null;
    }
    
    public int getGCN_SEQNO() {
        return this.GCN_SEQNO;
    }
    
    public void setGCN_SEQNO(int RHS) {
        this.GCN_SEQNO = RHS;
        //this.gcn=null;
    }
    
   /* public RxDrugData.GCN getGCN() {
        if (this.gcn==null) {
            this.gcn = new RxDrugData().getGCN(this.BN, this.GCN_SEQNO);
        }
        
        return gcn;
    }
    */
    
    public boolean isCustom() {
        boolean b = false;
        
        if(this.customName != null){
            b = true;
        }
        else if(this.GCN_SEQNO == 0){
            b = true;
        }
        return b;
    }
    
    public String getCustomName() {
        return this.customName;
    }
    public void setCustomName(String RHS) {
        this.customName = RHS;
        if(this.customName!=null) {
            if(this.customName.equalsIgnoreCase("null") || this.customName.equalsIgnoreCase("")) {
                this.customName = null;
            }
        }
    }
    
    public float getTakeMin() {
        return this.takeMin;
    }
    
    public void setTakeMin(float RHS) {
        this.takeMin = RHS;
    }
    
    public String getTakeMinString() {
        return RxUtil.FloatToString(this.takeMin);
    }
    
    public float getTakeMax() {
        return this.takeMax;
    }
    
    public void setTakeMax(float RHS) {
        this.takeMax = RHS;
    }
    
    public String getTakeMaxString() {
        return RxUtil.FloatToString(this.takeMax);
    }
    
    public String getFrequencyCode() {
        return this.frequencyCode;
    }
    
    public void setFrequencyCode(String RHS) {
        this.frequencyCode = RHS;
    }
    
    public String getDuration() {
        return this.duration;
    }
    
    public void setDuration(String RHS) {
        this.duration = RHS;
    }
    
    public String getDurationUnit() {
        return this.durationUnit;
    }
    
    public void setDurationUnit(String RHS) {
        this.durationUnit = RHS;
    }
    
    public String getQuantity() {
        if (this.quantity == null) this.quantity = "";
        return this.quantity;
    }
    public void setQuantity(String RHS) {
        if (RHS==null || RHS.equals("null") || RHS.length()<1) {
            this.quantity = "0";
        }
        else {
            this.quantity = RHS;
        }
    }
    
    public int getRepeat() {
        return this.repeat;
    }
    
    public void setRepeat(int RHS) {
        this.repeat = RHS;
    }
    
    public boolean getNosubs() {
        return this.nosubs;
    }
    
    public int getNosubsInt() {
        if (this.getNosubs()==true) {
            return 1;
        } else {
            return 0;
        }
    }
    
    public void setNosubs(boolean RHS) {
        this.nosubs = RHS;
    }
    
    public void setNosubs(int RHS) {
        if(RHS==0) {
            this.setNosubs(false);
        } else {
            this.setNosubs(true);
        }
    }
    
    public boolean getPrn() {
        return this.prn;
    }
    
    public int getPrnInt() {
        if (this.getPrn()==true) {
            return 1;
        } else {
            return 0;
        }
    }
    
    public void setPrn(boolean RHS) {
        this.prn = RHS;
    }
    
    public void setPrn(int RHS) {
        if(RHS==0) {
            this.setPrn(false);
        } else {
            this.setPrn(true);
        }
    }
    
    public String getSpecial() {
        return this.special;
    }
    
    public void setSpecial(String RHS) {
        if(RHS != null) {
            if(! RHS.equals("null")) {
                this.special = RHS;
            }
            else {
                this.special = null;
            }
        }
        else {
            this.special = null;
        }
    }
    
    public String getSpecialDisplay() {
        String ret = "";
        
        String s = this.getSpecial();
        
        if(s!=null) {
            if(s.length()>0) {
                ret = "<br>";
                
                int i;
                String[] arr = s.split("\n");
                
                for(i=0;i<arr.length; i++) {
                    ret += arr[i].trim();
                    if(i<arr.length-1) {
                        ret += "; ";
                    }
                }
            }
        }
        
        return ret;
    }
    
    public String getFullOutLine(){
        String ret = "";
        String s = this.getSpecial();
        if(s!=null){
            if(s.length()>0){
                int i;
                String[] arr = s.split("\n");
                for(i=0;i<arr.length; i++){
                    ret += arr[i].trim();
                    if(i<arr.length-1){
                        ret += "; ";
                    }
                }
            }
        }
        return ret;
    }
    
    
    public String getDosageDisplay(){
       String ret = "";
       if(this.getTakeMin() != this.getTakeMax()){
          ret += this.getTakeMinString() + "-" + this.getTakeMaxString();
       }else{
          ret += this.getTakeMinString();
       }            
       return ret;
    }
    
    public String getFreqDisplay(){
       String ret = this.getFrequencyCode();            
       if(this.getPrn()){
          ret += " PRN ";
       }
       return ret;
    }
    
    public String getRxDisplay(){
        try{
            String ret;
            
            if(this.isCustom()){
                if(this.customName != null){
                    ret = this.customName + " ";
                }else{
                    ret = "Unknown ";
                }
            }else{
                //RxDrugData.GCN gcn = this.getGCN();
                
                ret = this.getBrandName()+ " "; //gcn.getBrandName() + " ";
                //+ gcn.getStrength() + " "
                //+ gcn.getDoseForm() + " "
                //+ gcn.getRoute() + " ";
            }
            
            if(this.getTakeMin() != this.getTakeMax()){
                ret += this.getTakeMinString() + "-" + this.getTakeMaxString();
            }else{
                ret += this.getTakeMinString();
            }
            
            ret += " " + this.getFrequencyCode();
            
            if(this.getPrn()){
                ret += " PRN ";
            }
            ret += " " + this.getDuration() + " ";
            
            if(this.getDurationUnit().equals("D")){
                ret += "Day";
            }
            if(this.getDurationUnit().equals("W")){
                ret += "Week";
            }
            if(this.getDurationUnit().equals("M")){
                ret += "Month";
            }
            
            if(Integer.parseInt(this.getDuration()) > 1){
                ret += "s";
            }
            
            ret += "  ";
            ret += this.getQuantity();
            ret += " Qty  Repeats: ";
            ret += String.valueOf(this.getRepeat());
            
            if(this.getNosubs()){
                ret += " No subs ";
            }
            
            
            return ret;
        } catch (Exception e){
	    e.printStackTrace();
            System.out.println(e.getMessage());
            return null;
        }
    }
    
    public String getDrugName(){
        String ret;            
        if(this.isCustom()){
            if(this.customName != null){
                ret = this.customName + " ";
            }else{
                ret = "Unknown ";
            }
        }else{            
            ret = this.getBrandName()+ " ";          
        }        
        return ret;
    }
    
    public String getFullFrequency(){
        String ret = "";
        if(this.getTakeMin() != this.getTakeMax()){
            ret += this.getTakeMinString() + "-" + this.getTakeMaxString();
        }else{
            ret += this.getTakeMinString();
        }

        ret += " " + this.getFrequencyCode();
        return ret;
    }
    
    
    public String getFullDuration(){
        String ret = this.getDuration()+" ";
        if(this.getDurationUnit().equals("D")){
            ret += "Day";
        }
        if(this.getDurationUnit().equals("W")){
            ret += "Week";
        }
        if(this.getDurationUnit().equals("M")){
            ret += "Month";
        }

        if(Integer.parseInt(this.getDuration()) > 1){
            ret += "s";
        }
        return ret;
    }
    public void Delete() {
        try {
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
            String sql;
            
            if(this.getDrugId()>0) {
                sql = "UPDATE drugs SET archived = 1 WHERE drugid = " + this.getDrugId();
                db.RunSQL(sql);
            }
            
            db.getConnection().close();
        }
        catch (Exception e) {
            e.printStackTrace(System.out);
        }
    }
    
    public boolean Save() {
        return Save(null);
    }
    
    public boolean registerIndivo() {
        boolean ret = false;
        try {
            String update;
            if( isRegisteredIndivo() )
                update = "U";
            else
                update = "I";
            
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);            
            String sql = "INSERT INTO indivoDocs (oscarDocNo, indivoDocIdx, docType, dateSent, `update`)" + 
                    " VALUES(" + String.valueOf(getDrugId()) + ",'" + getIndivoIdx() + "','" + docType + 
                    "',now(),'" + update + "')";
            
            db.RunSQL(sql);
            ret = true;
        }
        catch(SQLException e) {
            System.out.println("DATABASE ERROR: " + e.getMessage());
        }
        
        return ret;
    }
    
    public boolean Print() {
        boolean ret = false;
        try {
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
            ResultSet rs;
            String sql = "SELECT dates_reprinted, now() FROM prescription WHERE script_no = " + this.getScript_no();
            
            rs = db.GetSQL(sql);
                
            if(rs.next()) {
                String dates_reprinted = db.getString(rs,1);
                String now = db.getString(rs,2);
                if( dates_reprinted != null  && dates_reprinted.length() > 0  )
                    dates_reprinted += "," + now;
                else
                    dates_reprinted = now;
                
                sql = "UPDATE prescription SET dates_reprinted = '" + dates_reprinted + "' WHERE script_no = " + this.getScript_no();
                db.RunSQL(sql);
                ret = true;
                
                
                this.setNumPrints(this.getNumPrints()+1);
                

            }
            
            
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return ret;
    }
    
    public boolean Save(String scriptId) {
        boolean b = false;
        
        // calculate end date
        this.calcEndDate();
        
        // clean up fields
        if(this.takeMin > this.takeMax) {
            this.takeMax = this.takeMin;
        }
        
        try {
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
            ResultSet rs;
            String sql;
            
            // if drugid = 0 this is an add, else update
            if(this.getDrugId()==0) {
                // check to see if there is an identitical prescription in
                // the database. If there is we'll return that drugid instead
                // of adding a new prescription.
                sql = "SELECT drugid FROM drugs WHERE archived = 0 AND "
                + "provider_no = '" + this.getProviderNo() + "' AND "
                + "demographic_no = " + this.getDemographicNo() + " AND "
                + "rx_date = '" + RxUtil.DateToString(this.getRxDate()) + "' AND "
                + "end_date = '" + RxUtil.DateToString(this.getEndDate()) + "' AND "
                + "BN = '" + this.getBrandName() + "' AND "
                + "GCN_SEQNO = " + this.getGCN_SEQNO() + " AND "
                + "customName = '" + this.getCustomName() + "' AND "
                + "takemin = " + this.getTakeMin() + " AND "
                + "takemax = " + this.getTakeMax() + " AND "
                + "freqcode = '" + this.getFrequencyCode() + "' AND "
                + "duration = '" + this.getDuration() + "' AND "
                + "durunit = '" + this.getDurationUnit() + "' AND "
                + "quantity = '" + this.getQuantity() + "' AND "
                + "unitName = '" + this.getUnitName() + "' AND "
                + "`repeat` = " + this.getRepeat() + " AND "
                + "nosubs = " + this.getNosubsInt() + " AND "
                + "prn = " + this.getPrnInt() + " AND "
                + "special = '" + RxUtil.replace(this.getSpecial(), "'", "") + "' AND "
                + "custom_instructions = " + this.getCustomInstr();
                
                rs = db.GetSQL(sql);
                
                if(rs.next()) {
                    this.drugId = rs.getInt("drugid");
                }
                
                rs.close();
                
                b = true;
                
                 
                
                // if it doesn't already exist add it.
                if(this.getDrugId() == 0) {
                    sql = "INSERT INTO drugs (provider_no, demographic_no, "
                    + "rx_date, end_date, BN, GCN_SEQNO, customName, "
                    + "takemin, takemax, "
                    + "freqcode, duration, durunit, quantity, "
                    + "`repeat`, nosubs, prn, special,GN,script_no,ATC,regional_identifier,unit,method,route,create_date,custom_instructions,dosage,unitName) "
                    + "VALUES ('" + this.getProviderNo() + "', " + this.getDemographicNo() + ", '"
                    + RxUtil.DateToString(this.getRxDate()) + "', '"
                    + RxUtil.DateToString(this.getEndDate()) + "', '"
                    + StringEscapeUtils.escapeSql(this.getBrandName()) + "', " + this.getGCN_SEQNO()
                    + ", '" + StringEscapeUtils.escapeSql(this.getCustomName()) + "', "
                    + this.getTakeMin() + ", " + this.getTakeMax() + ", '"
                    + this.getFrequencyCode() + "', '" + this.getDuration() + "', '"
                    + this.getDurationUnit() + "', '" + this.getQuantity() + "', "
                    + this.getRepeat() + ", " + this.getNosubsInt() + ", "
                    + this.getPrnInt() + ", '"
                    + RxUtil.replace(this.getSpecial(), "'", "") + "','"+this.getGenericName()+"','"+scriptId+"', '"
                    + this.getAtcCode() +"', '"+this.getRegionalIdentifier()+"','"+this.getUnit()+"','"+this.getMethod()+"','"
                    + this.getRoute()+"',now()," + this.getCustomInstr() + ",'"+this.getDosage()+ "', '"+ this.getUnitName()+ "')";
                    
                     
                    
                    db.RunSQL(sql);
                    
                    // it's added, so get the top (most recent) drugid
                    sql = "SELECT Max(drugid) FROM drugs";
                    
                    rs = db.GetSQL(sql);
                    
                    if (rs.next()) {
                        this.drugId = rs.getInt(1);
                    }
                    
                    rs.close();
                    
                    b = true;
                }
                
            } else { // update the daterbase          
                sql = "UPDATE drugs SET "
                + "provider_no = '" + this.getProviderNo() + "', "
                + "demographic_no = " + this.getDemographicNo() + ", "
                + "rx_date = '" + RxUtil.DateToString(this.getRxDate()) + "', "
                + "end_date = '" + RxUtil.DateToString(this.getEndDate()) + "', "
                + "BN = '" + StringEscapeUtils.escapeSql(this.getBrandName()) + "', "
                + "GCN_SEQNO = " + this.getGCN_SEQNO() + ", "
                + "customName = '" + StringEscapeUtils.escapeSql(this.getCustomName()) + "', "
                + "takemin = " + this.getTakeMin() + ", "
                + "takemax = " + this.getTakeMax() + ", "
                + "freqcode = '" + this.getFrequencyCode() + "', "
                + "duration = '" + this.getDuration() + "', "
                + "durunit = '" + this.getDurationUnit() + "', "
                + "quantity = '" + this.getQuantity() + "', "
                + "`repeat` = " + this.getRepeat() + ", "
                + "nosubs = " + this.getNosubsInt() + ", "
                + "prn = " + this.getPrnInt() + ", "
                + "special = '" + RxUtil.replace(this.getSpecial(), "'", "") + "', "
                + "ATC = '" +this.atcCode+"', "
                + "regional_identifier = '"+this.regionalIdentifier+"', "
                + "unit = '"+this.getUnit()+"', "
                + "method = '"+this.getMethod()+"', "
                + "route = '"+this.getRoute()+"', "
                + "dosage = '"+this.getDosage()+"', "        
                + "custom_instructions = " + this.getCustomInstr()+"', " 
                + "unitName = " + this.getUnitName() + " "        
                + "WHERE drugid = " + this.getDrugId();
                
                db.RunSQL(sql);
                
                b = true;
            }
            
            // close by conn
            db.getConnection().close();
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        
        return b;
    }
    
    public boolean AddToFavorites(String providerNo, String favoriteName) {
        boolean b = false;
        
        Favorite fav = new Favorite(0, providerNo, favoriteName,
        this.getBrandName(), this.getGCN_SEQNO(), this.getCustomName(),
        this.getTakeMin(), this.getTakeMax(),
        this.getFrequencyCode(), this.getDuration(),
        this.getDurationUnit(), this.getQuantity(),
        this.getRepeat(), this.getNosubsInt(), this.getPrnInt(), 
        this.getSpecial(),this.getGenericName(),
        this.getAtcCode(),this.getRegionalIdentifier(),this.getUnit(),this.getUnitName(),this.getMethod(),this.getRoute(), this.getCustomInstr(),this.getDosage());
        
        return fav.Save();
    }
    
    /** Getter for property atcCode.
     * @return Value of property atcCode.
     *
     */
    public java.lang.String getAtcCode() {
        return atcCode;
    }
    
    
    /**
     * Checks to see if atcCode is not null or an emtpy string
     */
    public boolean isValidAtcCode(){
        if (atcCode != null && !atcCode.trim().equals("")){
            return true;
        }
        return false;
    }
    
    /** Setter for property atcCode.
     * @param atcCode New value of property atcCode.
     *
     */
    public void setAtcCode(java.lang.String atcCode) {
        this.atcCode = atcCode;
    }
    
    /** Getter for property regionalIdentifier.
     * @return Value of property regionalIdentifier.
     *
     */
    public java.lang.String getRegionalIdentifier() {
        return regionalIdentifier;
    }
    
    /** Setter for property regionalIdentifier.
     * @param regionalIdentifier New value of property regionalIdentifier.
     *
     */
    public void setRegionalIdentifier(java.lang.String regionalIdentifier) {
        this.regionalIdentifier = regionalIdentifier;
    }
    
    /**
     * Getter for property method.
     * @return Value of property method.
     */
    public java.lang.String getMethod() {
       return method;
    }
    
    /**
     * Setter for property method.
     * @param method New value of property method.
     */
    public void setMethod(java.lang.String method) {
       this.method = method;
    }
    
    /**
     * Getter for property unit.
     * @return Value of property unit.
     */
    public java.lang.String getUnit() {
       return unit;
    }
    
    /**
     * Setter for property unit.
     * @param unit New value of property unit.
     */
    public void setUnit(java.lang.String unit) {
       this.unit = unit;
    }

    /**
     * Getter for property unitName
     * @return Value of property unitName.
     */
    public java.lang.String getUnitName() {
       return unitName;
    }
    
    /**
     * Setter for property unitName.
     * @param unit New value of property unitName.
     */
    public void setUnitName(java.lang.String unitName) {
       this.unitName = unitName;
    }    
    
    /**
     * Getter for property route.
     * @return Value of property route.
     */
    public java.lang.String getRoute() {
       return route;
    }
    
    /**
     * Setter for property route.
     * @param route New value of property route.
     */
    public void setRoute(java.lang.String route) {
       this.route = route;
    }
    
    /**
     *Setter for property custom (does it have customized directions)
     *@param boolean value for custom
     */
    public void setCustomInstr(boolean custom) {
        this.custom = custom;
    }
    
    public boolean getCustomInstr() {
        return this.custom;
    }
    
    /**
     * Getter for property rxCreatedDate.
     * @return Value of property rxCreatedDate.
     */
    public java.util.Date getRxCreatedDate() {
       return rxCreatedDate;
    }
    
    /**
     * Setter for property rxCreatedDate.
     * @param rxCreatedDate New value of property rxCreatedDate.
     */
    public void setRxCreatedDate(java.util.Date rxCreatedDate) {
       this.rxCreatedDate = rxCreatedDate;
    }
    
    /**
     * Getter for property dosage.
     * @return Value of property dosage.
     */
    public java.lang.String getDosage() {
       return dosage;
    }
    
    /**
     * Setter for property dosage.
     * @param dosage New value of property dosage.
     */
    public void setDosage(java.lang.String dosage) {
       this.dosage = dosage;
    }
    
}

public class Favorite {
    int favoriteId;
    String providerNo;
    String favoriteName;
    String BN;
    int GCN_SEQNO;
    String customName;
    float takeMin;
    float takeMax;
    String frequencyCode;
    String duration;
    String durationUnit;
    String quantity;
    int repeat;
    boolean nosubs;
    boolean prn;
    boolean customInstr;
    String special;
    String GN;
    String atcCode;
    String regionalIdentifier;
    String unit;
    String unitName;
    String method;
    String route;
    String dosage;
    
    public Favorite(int favoriteId, String providerNo, String favoriteName,
    String BN, int GCN_SEQNO, String customName,
    float takeMin, float takeMax, String frequencyCode, String duration,
    String durationUnit, String quantity,
    int repeat, int nosubs, int prn, String special,String GN,String atc,String regionalIdentifier,String unit,String unitName,String method,String route,boolean customInstr,String dosage) {
        this.favoriteId = favoriteId;
        this.providerNo = providerNo;
        this.favoriteName = favoriteName;
        this.BN = BN;
        this.GCN_SEQNO = GCN_SEQNO;
        this.customName = customName;
        this.takeMin = takeMin;
        this.takeMax = takeMax;
        this.frequencyCode = frequencyCode;
        this.duration = duration;
        this.durationUnit = durationUnit;
        this.quantity = quantity;
        this.repeat = repeat;
        this.nosubs = RxUtil.IntToBool(nosubs);
        this.prn = RxUtil.IntToBool(prn);
        this.special = special;
        this.GN =GN;
        this.atcCode = atc;
        this.regionalIdentifier= regionalIdentifier;
        this.unit = unit;
        this.unitName = unitName;
        this.method = method;
        this.route = route;
        this.customInstr = customInstr;
        this.dosage = dosage;
    }
    
   
    public String getGN() {
        return this.GN;
    }
    
    public void setGN(String RHS) {
        this.GN = RHS;
    }
    
    public int getFavoriteId() {
        return this.favoriteId;
    }
    
    public String getProviderNo() {
        return this.providerNo;
    }
    
    public String getFavoriteName() {
        return this.favoriteName;
    }
    
    public void setFavoriteName(String RHS) {
        this.favoriteName = RHS;
    }
    
    public String getBN() {
        return this.BN;
    }
    
    public void setBN(String RHS) {
        this.BN = RHS;
    }
    
    public int getGCN_SEQNO() {
        return this.GCN_SEQNO;
    }
    
    public void setGCN_SEQNO(int RHS) {
        this.GCN_SEQNO = RHS;
    }
    
    public String getCustomName() {
        return this.customName;
    }
    
    public void setCustomName(String RHS) {
        this.customName = RHS;
    }
    
    public float getTakeMin() {
        return this.takeMin;
    }
    
    public void setTakeMin(float RHS) {
        this.takeMin = RHS;
    }
    
    public String getTakeMinString() {
        return RxUtil.FloatToString(this.takeMin);
    }
    
    public float getTakeMax() {
        return this.takeMax;
    }
    
    public void setTakeMax(float RHS) {
        this.takeMax = RHS;
    }
    
    public String getTakeMaxString() {
        return RxUtil.FloatToString(this.takeMax);
    }
    
    public String getFrequencyCode() {
        return this.frequencyCode;
    }
    
    public void setFrequencyCode(String RHS) {
        this.frequencyCode = RHS;
    }
    
    public String getDuration() {
        return this.duration;
    }
    
    public void setDuration(String RHS) {
        this.duration = RHS;
    }
    
    public String getDurationUnit() {
        return this.durationUnit;
    }
    
    public void setDurationUnit(String RHS) {
        this.durationUnit = RHS;
    }
    
    public String getQuantity() {
        return this.quantity;
    }
    
    public void setQuantity(String RHS) {
        this.quantity = RHS;
    }
    
    public int getRepeat() {
        return this.repeat;
    }
    
    public void setRepeat(int RHS) {
        this.repeat = RHS;
    }
    
    public boolean getNosubs() {
        return this.nosubs;
    }
    
    public void setNosubs(boolean RHS) {
        this.nosubs = RHS;
    }
    
    public int getNosubsInt() {
        if (this.getNosubs()==true) {
            return 1;
        } else {
            return 0;
        }
    }
    
    public boolean getPrn() {
        return this.prn;
    }
    
    public void setPrn(boolean RHS) {
        this.prn = RHS;
    }
    
    public int getPrnInt() {
        if (this.getPrn()==true) {
            return 1;
        } else {
            return 0;
        }
    }
    
    public String getSpecial() {
        return this.special;
    }
    
    public void setSpecial(String RHS) {
        this.special = RHS;
    }
    
    public boolean getCustomInstr() {
        return this.customInstr;
    }
    
    public void setCustomInstr(boolean customInstr) {
        this.customInstr = customInstr;
    }
    
    public boolean Save() {
        boolean b = false;
        
        // clean up fields
        if(this.takeMin > this.takeMax) {
            this.takeMax = this.takeMin;
        }
        
        try {
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
            ResultSet rs;
            String sql;
            
            if(this.getFavoriteId()==0) {
                sql = "SELECT favoriteid FROM favorites WHERE "
                + "provider_no = '" + this.getProviderNo() + "' AND "
                + "favoritename = '" + this.getFavoriteName() + "' AND "
                + "BN = '" + this.getBN() + "' AND "
                + "GCN_SEQNO = " + this.getGCN_SEQNO() + " AND "
                + "customName = '" + this.getCustomName() + "' AND "
                + "takemin = " + this.getTakeMin() + " AND "
                + "takemax = " + this.getTakeMax() + " AND "
                + "freqcode = '" + this.getFrequencyCode() + "' AND "
                + "duration = '" + this.getDuration() + "' AND "
                + "durunit = '" + this.getDurationUnit() + "' AND "
                + "quantity = '" + this.getQuantity() + "' AND "
                + "`repeat` = " + this.getRepeat() + " AND "
                + "nosubs = " + this.getNosubsInt() + " AND "
                + "prn = " + this.getPrnInt() + " AND "
                + "special = '" + RxUtil.replace(this.getSpecial(), "'", "") + "' AND "
                + "GN = '" +this.getGN() +"' AND "
                + "unitName = '" +this.getUnitName() +"' AND "        
                + "custom_instructions = " + this.getCustomInstr();
                
                rs = db.GetSQL(sql);
                
                if(rs.next()) {
                    this.favoriteId = rs.getInt("favoriteid");
                }
                
                rs.close();
                
                b = true;
                
                if(this.getFavoriteId()==0) {
                    sql = "INSERT INTO favorites (provider_no, favoritename, "
                    + "BN, GCN_SEQNO, customName, takemin, takemax, "
                    + "freqcode, duration, durunit, quantity, "
                    + "`repeat`, nosubs, prn, special,GN,ATC,regional_identifier,unit,unitName,method,route,custom_instructions,dosage) "
                    + "VALUES ('" + this.getProviderNo() + "', '" + StringEscapeUtils.escapeSql(this.getFavoriteName()) + "', '"
                    + StringEscapeUtils.escapeSql(this.getBN()) + "', " + this.getGCN_SEQNO() + ", '"
                    + StringEscapeUtils.escapeSql(this.getCustomName()) + "', "
                    + this.getTakeMin() + ", " + this.getTakeMax() + ", '"
                    + this.getFrequencyCode() + "', '" + this.getDuration() + "', '"
                    + this.getDurationUnit() + "', '" + this.getQuantity() + "', "
                    + this.getRepeat() + ", " + this.getNosubsInt() + ", "
                    + this.getPrnInt() + ", '"
                    + RxUtil.replace(this.getSpecial(), "'", "") + "', '"
                    + this.getGN()+"', ' "
                    + this.getAtcCode()+"', '"
                    + this.getRegionalIdentifier()+"', '"
                    + this.getUnit()+"', '"
                    + this.getUnitName()+"', '"        
                    + this.getMethod()+"', '"
                    + this.getRoute()+"', "
                    + this.getCustomInstr() + ", '"
                    + this.getDosage()+"')"; 
                    

                    db.RunSQL(sql);
                    
                    sql = "SELECT Max(favoriteid) FROM favorites";
                    
                    rs = db.GetSQL(sql);
                    
                    if (rs.next()) {
                        this.favoriteId = rs.getInt(1);
                    }
                    
                    rs.close();
                    
                    b = true;
                }
                
            } else {
                sql = "UPDATE favorites SET "
                + "provider_no = '" + this.getProviderNo() + "', "
                + "favoritename = '" + this.getFavoriteName() + "', "
                + "BN = '" + StringEscapeUtils.escapeSql(this.getBN()) + "', "
                + "GCN_SEQNO = " + this.getGCN_SEQNO() + ", "
                + "customName = '" + StringEscapeUtils.escapeSql(this.getCustomName()) + "', "
                + "takemin = " + this.getTakeMin() + ", "
                + "takemax = " + this.getTakeMax() + ", "
                + "freqcode = '" + this.getFrequencyCode() + "', "
                + "duration = '" + this.getDuration() + "', "
                + "durunit = '" + this.getDurationUnit() + "', "
                + "quantity = '" + this.getQuantity() + "', "
                + "`repeat` = " + this.getRepeat() + ", "
                + "nosubs = " + this.getNosubsInt() + ", "
                + "prn = " + this.getPrnInt() + ", "
                + "special = '" + RxUtil.replace(this.getSpecial(), "'", "") + "', "
                + "GN = '"+this.getGN()+"', "
                + "ATC = '"+this.getAtcCode()+"', "
                + "regional_identifier = '"+this.getRegionalIdentifier()+"', "
                + "unit = '"+this.getUnit()+"', " 
                + "unitName = '"+this.getUnitName()+"', "         
                + "method = '"+this.getMethod()+"', "
                + "route = '"+this.getRoute()+"', "
                + "custom_instructions = " + this.getCustomInstr() + ", "
                + "dosage = '"+this.getDosage()+"' "
                + "WHERE favoriteid = " + this.getFavoriteId();
                
                db.RunSQL(sql);
                
                b = true;
            }
            
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        finally {
            DbConnectionFilter.releaseThreadLocalDbConnection();
        }
        
        return b;
    }
    
    /** Getter for property atcCode.
     * @return Value of property atcCode.
     *
     */
    public java.lang.String getAtcCode() {
        return atcCode;
    }
    
    /** Setter for property atcCode.
     * @param atcCode New value of property atcCode.
     *
     */
    public void setAtcCode(java.lang.String atcCode) {
        this.atcCode = atcCode;
    }
    
    /** Getter for property regionalIdentifier.
     * @return Value of property regionalIdentifier.
     *
     */
    public java.lang.String getRegionalIdentifier() {
        return regionalIdentifier;
    }
    
    /** Setter for property regionalIdentifier.
     * @param regionalIdentifier New value of property regionalIdentifier.
     *
     */
    public void setRegionalIdentifier(java.lang.String regionalIdentifier) {
        this.regionalIdentifier = regionalIdentifier;
    }
    
    /**
     * Getter for property unit.
     * @return Value of property unit.
     */
    public java.lang.String getUnit() {
       return unit;
    }
    
    /**
     * Setter for property unit.
     * @param unit New value of property unit.
     */
    public void setUnit(java.lang.String unit) {
       this.unit = unit;
    }
    
    /**
     * Getter for property unitName.
     * @return Value of property unitName.
     */
    public java.lang.String getUnitName() {
       return unitName;
    }
    
    /**
     * Setter for property unitName.
     * @param unitName New value of property unitName.
     */
    public void setUnitName(java.lang.String unitName) {
       this.unitName = unitName;
    }    
    
    /**
     * Getter for property method.
     * @return Value of property method.
     */
    public java.lang.String getMethod() {
       return method;
    }
    
    /**
     * Setter for property method.
     * @param method New value of property method.
     */
    public void setMethod(java.lang.String method) {
       this.method = method;
    }
    
    /**
     * Getter for property route.
     * @return Value of property route.
     */
    public java.lang.String getRoute() {
       return route;
    }
    
    /**
     * Setter for property route.
     * @param route New value of property route.
     */
    public void setRoute(java.lang.String route) {
       this.route = route;
    }
    
     /**
     * Getter for property dosage.
     * @return Value of property dosage.
     */
    public java.lang.String getDosage() {
       return dosage;
    }
    
    /**
     * Setter for property dosage.
     * @param dosage New value of property dosage.
     */
    public void setDosage(java.lang.String dosage) {
       this.dosage = dosage;
    }
    
}
}
