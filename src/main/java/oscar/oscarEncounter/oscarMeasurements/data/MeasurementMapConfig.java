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
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import org.oscarehr.util.DbConnectionFilter;
import org.oscarehr.util.MiscUtils;

import oscar.oscarDB.DBHandler;
import oscar.util.UtilDateUtilities;

/**
 *
 * @author wrighd
 */
public class MeasurementMapConfig {

    Logger logger = Logger.getLogger(MeasurementMapConfig.class);

    /** Creates a new instance of MeasurementMapConfig */
    public MeasurementMapConfig() {
    }

    public List<String> getLabTypes() {
        List<String> ret = new LinkedList<String>();
        String sql = "select distinct lab_type from measurementMap";
        try {

            Connection conn = DbConnectionFilter.getThreadLocalDbConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            logger.info(sql);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                ret.add(oscar.Misc.getString(rs, "lab_type"));
            }
            pstmt.close();
        } catch (SQLException e) {
            logger.error("Exception in getLoincCodes", e);
        }
        return ret;

    }

    public List<Hashtable<String,String>> getMappedCodesFromLoincCodes(String loincCode) {
        List<Hashtable<String,String>> ret = new LinkedList<Hashtable<String,String>>();
        String sql = "select * from measurementMap where loinc_code = ?";

        try {

            Connection conn = DbConnectionFilter.getThreadLocalDbConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, loincCode);
            logger.info(sql);

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Hashtable<String, String> ht = new Hashtable<String,String>();
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

    public Hashtable<String, Hashtable<String,String>> getMappedCodesFromLoincCodesHash(String loincCode) {
        Hashtable<String, Hashtable<String,String>> ret = new Hashtable<String,Hashtable<String,String>>();
        String sql = "select * from measurementMap where loinc_code = ?";

        try {

            Connection conn = DbConnectionFilter.getThreadLocalDbConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, loincCode);
            logger.info(sql);

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Hashtable<String, String> ht = new Hashtable<String,String>();
                ht.put("id", getString(oscar.Misc.getString(rs, "id")));
                ht.put("loinc_code", getString(oscar.Misc.getString(rs, "loinc_code")));
                ht.put("ident_code", getString(oscar.Misc.getString(rs, "ident_code")));
                ht.put("name", getString(oscar.Misc.getString(rs, "name")));
                ht.put("lab_type", getString(oscar.Misc.getString(rs, "lab_type")));
                ret.put(getString(oscar.Misc.getString(rs, "lab_type")), ht);
            }

            pstmt.close();
        } catch (SQLException e) {
            logger.error("Exception in getMeasurementMap", e);
        }
        return ret;
    }

    public List<String> getDistinctLoincCodes() {
        List<String> ret = new LinkedList<String>();
        String sql = "SELECT DISTINCT loinc_code FROM measurementMap ORDER BY name";

        try {

            Connection conn = DbConnectionFilter.getThreadLocalDbConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            logger.info(sql);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                ret.add(oscar.Misc.getString(rs, "loinc_code"));
            }
            pstmt.close();
        } catch (SQLException e) {
            logger.error("Exception in getLoincCodes", e);
        }
        return ret;
    }

    public String getLoincCodeByIdentCode(String identifier) throws SQLException {
        if (identifier != null && identifier.trim().length() > 0) {

            String sql = "SELECT loinc_code FROM measurementMap WHERE ident_code='" + identifier + "'";
            ResultSet rs = DBHandler.GetSQL(sql);

            if (rs.next()) {
                return rs.getString("loinc_code");
            }
        }
        return null;
    }

    public boolean isTypeMappedToLoinc(String measurementType) throws SQLException {
        String sql = "SELECT mm.id, mm.loinc_code, mm.ident_code, mm.name, mm.lab_type FROM measurementMap mm WHERE ident_code='" + measurementType + "'";

        ResultSet rs = DBHandler.GetSQL(sql);
        return rs.next();
    }

    //--------these methods are currently used for sending to indivo, feel free to use them elsewhere (Paul)
    public LoincMapEntry getLoincMapEntryByIdentCode(String identCode) {
        String sql = "SELECT mm.id, mm.loinc_code, mm.ident_code, mm.name, mm.lab_type FROM measurementMap mm WHERE ident_code='" + identCode + "'";
        try {

            ResultSet rs = DBHandler.GetSQL(sql);
            if (rs.next()) return rsToLoincMapEntry(rs);
            else return null;
        } catch (SQLException sqe) {
            MiscUtils.getLogger().error("Error", sqe);
        }
        return null;
    }

    private ArrayList<LoincMapEntry> rsToLoincMapEntries(ResultSet rs) throws SQLException {
        ArrayList<LoincMapEntry> loincMapEntries = new ArrayList<LoincMapEntry>();
        while (rs.next()) {
            loincMapEntries.add(this.rsToLoincMapEntry(rs));
        }
        return loincMapEntries;
    }

    private LoincMapEntry rsToLoincMapEntry(ResultSet rs) throws SQLException {
        LoincMapEntry loincMapEntry = new LoincMapEntry();
        loincMapEntry.setId(rs.getString("id"));
        loincMapEntry.setLoincCode(rs.getString("loinc_code"));
        loincMapEntry.setIdentCode(rs.getString("ident_code"));
        loincMapEntry.setName(rs.getString("name"));
        loincMapEntry.setLabType(rs.getString("lab_type"));
        return loincMapEntry;
    }
    // ------------------------------------------------------------------------------------------------------

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

    public void mapMeasurement(String identifier, String loinc, String name, String type) throws SQLException {

        String sql = "INSERT INTO measurementMap (loinc_code, ident_code, name, lab_type) VALUES ('" + loinc + "', '" + identifier + "', '" + name + "', '" + type + "')";


        Connection conn = DbConnectionFilter.getThreadLocalDbConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql);
        logger.info(sql);

        pstmt.executeUpdate();
        pstmt.close();

    }

    public void removeMapping(String id, String provider_no) throws SQLException {

        String ident_code = "";
        String loinc_code = "";
        String name = "";
        String lab_type = "";


        Connection conn = DbConnectionFilter.getThreadLocalDbConnection();

        String sql = "SELECT * FROM measurementMap WHERE id='" + id + "'";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        ResultSet rs = pstmt.executeQuery();
        if (rs.next()) {
            ident_code = getString(oscar.Misc.getString(rs, "ident_code"));
            loinc_code = getString(oscar.Misc.getString(rs, "loinc_code"));
            name = getString(oscar.Misc.getString(rs, "name"));
            lab_type = getString(oscar.Misc.getString(rs, "lab_type"));
        }

        sql = "DELETE FROM measurementMap WHERE id='" + id + "'";
        pstmt = conn.prepareStatement(sql);
        if (!pstmt.execute()) {
            logger.info("we should be writing to the recycle bin");
            pstmt.close();
            sql = "insert into recyclebin (provider_no,updatedatetime,table_name,keyword,table_content) values(";
            sql += "'" + provider_no + "',";
            sql += "'" + UtilDateUtilities.getToday("yyyy-MM-dd HH:mm:ss") + "',";
            sql += "'" + "measurementMap" + "',";
            sql += "'" + id + "',";
            sql += "'" + "<id>" + id + "</id><ident_code>" + ident_code + "</ident_code><loinc_code>" + loinc_code + "</loinc_code><name>" + name + "</name><lab_type>" + lab_type + "</lab_type>')";

            pstmt = conn.prepareStatement(sql);
            pstmt.executeUpdate();
        }

        pstmt.close();

    }

    /**
     *  Only one identifier per type is allowed to be mapped to a single loinc code
     *  Return true if there is already an identifier mapped to the loinc code.
     */
    public boolean checkLoincMapping(String loinc, String type) throws SQLException {

        boolean ret = false;
        String sql = "SELECT * from measurementMap WHERE loinc_code='" + loinc + "' AND lab_type='" + type + "'";


        Connection conn = DbConnectionFilter.getThreadLocalDbConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql);
        logger.info(sql);

        ResultSet rs = pstmt.executeQuery();
        ret = rs.next();

        return ret;
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
