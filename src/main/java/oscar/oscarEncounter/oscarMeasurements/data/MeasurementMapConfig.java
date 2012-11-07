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


/*
 * MeasurementMapConfig.java
 *
 * Created on September 28, 2007, 10:15 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package oscar.oscarEncounter.oscarMeasurements.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import org.oscarehr.common.dao.MeasurementMapDao;
import org.oscarehr.common.dao.RecycleBinDao;
import org.oscarehr.common.model.MeasurementMap;
import org.oscarehr.common.model.RecycleBin;
import org.oscarehr.util.DbConnectionFilter;
import org.oscarehr.util.SpringUtils;

/**
 *
 * @author wrighd
 */
public class MeasurementMapConfig {

    Logger logger = Logger.getLogger(MeasurementMapConfig.class);
    private MeasurementMapDao dao = SpringUtils.getBean(MeasurementMapDao.class);


    public MeasurementMapConfig() {
    }

    public List<String> getLabTypes() {
       return dao.findDistinctLabTypes();
    }

    public List<Hashtable<String,String>> getMappedCodesFromLoincCodes(String loincCode) {
        List<Hashtable<String,String>> ret = new LinkedList<Hashtable<String,String>>();
        
        for(MeasurementMap map:dao.findByLoincCode(loincCode)) {
        	Hashtable<String, String> ht = new Hashtable<String,String>();
            ht.put("id", map.getId().toString());
            ht.put("loinc_code", map.getLoincCode());
            ht.put("ident_code", map.getIdentCode());
            ht.put("name", map.getName());
            ht.put("lab_type", map.getLabType());
            ret.add(ht);
        }
        return ret;
    }

    public Hashtable<String, Hashtable<String,String>> getMappedCodesFromLoincCodesHash(String loincCode) {
        Hashtable<String, Hashtable<String,String>> ret = new Hashtable<String,Hashtable<String,String>>();
        
        for(MeasurementMap map:dao.findByLoincCode(loincCode)) {
        	Hashtable<String, String> ht = new Hashtable<String,String>();
            ht.put("id", map.getId().toString());
            ht.put("loinc_code", map.getLoincCode());
            ht.put("ident_code", map.getIdentCode());
            ht.put("name", map.getName());
            ht.put("lab_type", map.getLabType());
            ret.put(map.getLabType(),ht);
        }
        return ret;
    }

    public List<String> getDistinctLoincCodes() {
    	List<String> results = dao.findDistinctLoincCodes();
    	Collections.sort(results);
    	return results;
    }

    public String getLoincCodeByIdentCode(String identifier) {
        if (identifier != null && identifier.trim().length() > 0) {

        	for(MeasurementMap map:dao.getMapsByIdent(identifier)) {
        		return map.getLoincCode();
        	}
        }
        return null;
    }

    public boolean isTypeMappedToLoinc(String measurementType) {
    	int size = dao.getMapsByIdent(measurementType).size();
    	if(size>0)
    		return true;
        return false;
    }

    public LoincMapEntry getLoincMapEntryByIdentCode(String identCode) {
    	for(MeasurementMap map:dao.getMapsByIdent(identCode)) {
    		LoincMapEntry loincMapEntry = new LoincMapEntry();
            loincMapEntry.setId(map.getId().toString());
            loincMapEntry.setLoincCode(map.getLoincCode());
            loincMapEntry.setIdentCode(map.getIdentCode());
            loincMapEntry.setName(map.getName());
            loincMapEntry.setLabType(map.getLabType());
            return loincMapEntry;
    	}
    	
        return null;
    }

    public ArrayList<Hashtable<String,Object>> getLoincCodes(String searchString) {
        searchString = "%" + searchString.replaceAll("\\s", "%") + "%";
        ArrayList<Hashtable<String,Object>> ret = new ArrayList<Hashtable<String,Object>>();
        String sql = "SELECT DISTINCT loinc_code, name FROM measurementMap WHERE loinc_code=ident_code and name like '" + searchString + "' ORDER BY name";

        try {

            Connection conn = DbConnectionFilter.getThreadLocalDbConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            logger.info(sql);

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Hashtable<String,Object> ht = new Hashtable<String,Object>();
                ht.put("code", oscar.Misc.getString(rs, "loinc_code"));
                ht.put("name", oscar.Misc.getString(rs, "name"));
                ret.add(ht);
            }

            pstmt.close();
        } catch (SQLException e) {
            logger.error("Exception in getLoincCodes", e);
        }

        return ret;

    }

    public ArrayList<Hashtable<String,Object>> getMeasurementMap(String searchString) {
        searchString = "%" + searchString.replaceAll("\\s", "%") + "%";
        ArrayList<Hashtable<String,Object>> ret = new ArrayList<Hashtable<String,Object>>();
        String sql = "SELECT DISTINCT * FROM measurementMap WHERE name LIKE '" + searchString + "' ORDER BY name";

        try {

            Connection conn = DbConnectionFilter.getThreadLocalDbConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            logger.info(sql);

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
            	Hashtable<String,Object> ht = new Hashtable<String,Object>();
                ht.put("id", getString(oscar.Misc.getString(rs, "id")));
                ht.put("loinc_code", getString(oscar.Misc.getString(rs, "loinc_code")));
                ht.put("ident_code", getString(oscar.Misc.getString(rs, "ident_code")));
                ht.put("name", getString(oscar.Misc.getString(rs, "name")));
                ht.put("lab_type", getString(oscar.Misc.getString(rs, "lab_type")));
                ret.add(ht);
            }

            pstmt.close();
        } catch (SQLException e) {
            logger.error("Exception in getMeasurementMap", e);
        }

        return ret;
    }

    public ArrayList<Hashtable<String,Object>> getUnmappedMeasurements(String type) {
        ArrayList<Hashtable<String,Object>> ret = new ArrayList<Hashtable<String,Object>>();
        String sql = "SELECT DISTINCT h.type, me1.val AS identifier, me2.val AS name " +
                "FROM measurementsExt me1 " +
                "JOIN measurementsExt me2 ON me1.measurement_id = me2.measurement_id AND me2.keyval='name' " +
                "JOIN measurementsExt me3 ON me1.measurement_id = me3.measurement_id AND me3.keyval='lab_no' " +
                "JOIN hl7TextMessage h ON me3.val = h.lab_id " +
                "WHERE me1.keyval='identifier' AND h.type LIKE '" + type + "%' " +
                "AND me1.val NOT IN (SELECT ident_code FROM measurementMap) ORDER BY h.type";

        try {

            Connection conn = DbConnectionFilter.getThreadLocalDbConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            logger.info(sql);

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
            	Hashtable<String,Object> ht = new Hashtable<String,Object>();
                ht.put("type", getString(oscar.Misc.getString(rs, "type")));
                ht.put("identifier", getString(oscar.Misc.getString(rs, "identifier")));
                ht.put("name", getString(oscar.Misc.getString(rs, "name")));
                ret.add(ht);
            }

            pstmt.close();
        } catch (SQLException e) {
            logger.error("Exception in getUnmappedMeasurements", e);
        }

        return ret;
    }

    public void mapMeasurement(String identifier, String loinc, String name, String type)  {
    	MeasurementMapDao dao = SpringUtils.getBean(MeasurementMapDao.class);
    	MeasurementMap mm = new MeasurementMap();
    	mm.setLoincCode(loinc);
    	mm.setIdentCode(identifier);
    	mm.setName(name);
    	mm.setLabType(type);
    	dao.persist(mm);
    }

    public void removeMapping(String id, String provider_no)  {
    	 String ident_code = "";
         String loinc_code = "";
         String name = "";
         String lab_type = "";
         
    	MeasurementMap map = dao.find(Integer.parseInt(id));
    	if(map != null) {
    		ident_code = map.getIdentCode();
    		loinc_code = map.getLoincCode();
    		name = map.getName();
    		lab_type = map.getLabType();
    	}
       
    	dao.remove(map.getId());
    	
    	RecycleBin rb = new RecycleBin();
        rb.setProviderNo(provider_no);
        rb.setUpdateDateTime(new Date());
        rb.setTableName("measurementMap");
        rb.setKeyword(id);
        rb.setTableContent("<id>" + id + "</id><ident_code>" + ident_code + "</ident_code><loinc_code>" + loinc_code + "</loinc_code><name>" + name + "</name><lab_type>" + lab_type + "</lab_type>");

        RecycleBinDao recycleBinDao = SpringUtils.getBean(RecycleBinDao.class);
        recycleBinDao.persist(rb);
    }

    /**
     *  Only one identifier per type is allowed to be mapped to a single loinc code
     *  Return true if there is already an identifier mapped to the loinc code.
     */
    public boolean checkLoincMapping(String loinc, String type) {
    	List<MeasurementMap> maps = dao.findByLoincCodeAndLabType(loinc,type);
    	return maps.size()>0;   	
    }

    private String getString(String input) {
        String ret = "";
        if (input != null) {
            ret = input;
        }
        return ret;
    }

    public class mapping {
        public String code;
        public String name;

    }

}
