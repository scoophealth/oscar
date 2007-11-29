package org.oscarehr.common.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.oscarehr.util.DbConnectionFilter;

public class IntakeRequiredFieldsDao {
    
    public static final String FIELD_FIRST_NAME="FIRST_NAME";
    public static final String FIELD_LAST_NAME="LAST_NAME";
    public static final String FIELD_GENDER="GENDER";
    public static final String FIELD_BIRTH_DATE="BIRTH_DATE";
    public static final String FIELD_EMAIL="EMAIL";
    public static final String FIELD_PHONE="PHONE";
    public static final String FIELD_PHONE2="PHONE2";
    public static final String FIELD_STREET="STREET";
    public static final String FIELD_CITY="CITY";
    public static final String FIELD_PROVINCE="PROVINCE";
    public static final String FIELD_POSTAL_CODE="POSTAL_CODE";
    
    public static boolean isRequired(String fieldKey) throws SQLException {
        Connection c = DbConnectionFilter.getThreadLocalDbConnection();
        try {
            PreparedStatement ps = c.prepareStatement("select isRequired from IntakeRequiredFields where fieldKey=?");
            ps.setString(1, fieldKey);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return (rs.getBoolean(1));
            else return (false);
        }
        finally {
            c.close();
        }
    }

    public static void setIsRequired(String fieldKey, boolean isRequired) throws SQLException {
        Connection c = DbConnectionFilter.getThreadLocalDbConnection();
        try {
            PreparedStatement ps = c.prepareStatement("update IntakeRequiredFields set isRequired=? where fieldKey=?");
            ps.setBoolean(1, isRequired);
            ps.setString(2, fieldKey);
            
            int result=ps.executeUpdate();
            
            if (result==0) {
                ps = c.prepareStatement("insert into IntakeRequiredFields values (?,?)");
                ps.setString(1, fieldKey);
                ps.setBoolean(2, isRequired);
                ps.executeUpdate();
            }
        }
        finally {
            c.close();
        }
    }
}
