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
 * McMaster Unviersity 
 * Hamilton 
 * Ontario, Canada 
 */
package oscar.util;


import org.apache.log4j.Category;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import java.sql.*;

import java.text.SimpleDateFormat;

import java.util.ArrayList;


public class SqlUtils {
    static Category cat = Category.getInstance(SqlUtils.class.getName());
    
    Connection conn = null;

    public SqlUtils() {
    }

    public boolean executeUpdate(String[] param) {
        boolean result = false;

        if (prepareSQL()) {
            if (executeUpdateSQL(param)) {
                result = true;
            }

            unPrepareSQL();
        }

        return result;
    }

    public boolean executeUpdate(String param) {
        boolean result = false;

        if (prepareSQL()) {
            if (executeUpdateSQL(param)) {
                result = true;
            }

            unPrepareSQL();
        }

        return result;
    }

    public ArrayList executeSelect(String param) {
        ArrayList result = null;

        if (prepareSQL()) {
            result = executeSelectSQL(param);
            unPrepareSQL();
        }

        return result;
    }

    public static ArrayList executeSelect(String param, Connection conn) {
        ArrayList result = null;

        if (conn != null) {
            result = executeSelectSQL(param, conn);
        }

        return result;
    }

    private boolean prepareSQL() {
        conn = SqlUtils.getConnection();

        if (conn == null) {
            cat.fatal("Sem conexao.");

            return false;
        }

        return true;
    }

    private boolean unPrepareSQL() {
        if (conn != null) {
            SqlUtils.freeConnection(conn);

            return false;
        }

        return true;
    }

    private boolean executeUpdateSQL(String[] sqlCommands) {
        Statement stmt = null;

        try {
            conn.setAutoCommit(false);

            if (sqlCommands != null) {
                for (int i = 0; i < sqlCommands.length; i++) {
                    cat.debug(sqlCommands[i]);
                    stmt = conn.createStatement();
                    stmt.executeUpdate(sqlCommands[i]);
                    stmt.close();
                    stmt = null;
                }

                conn.commit();
            }

            return true;
        } catch (Exception e) {
            try {
                conn.rollback();
            } catch (Exception e2) {
                return false;
            }

            cat.error("Transacao nao executada.", e);

            return false;
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }

                conn.setAutoCommit(true);
            } catch (Exception e3) {
            }

            ;
        }
    }

    private boolean executeUpdateSQL(String sqlCommand) {
        Statement stmt = null;

        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(sqlCommand);
            stmt.close();
            stmt = null;
            cat.debug(sqlCommand);

            return true;
        } catch (Exception e) {
            cat.error("Comando no ejecutado.", e);
            cat.debug(sqlCommand);

            return false;
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (Exception e3) {
            }

            ;
        }
    }

    private ArrayList executeSelectSQL(String sqlCommand) {
        Statement stmt = null;
        ResultSet rset = null;
        ArrayList records = new ArrayList();

        try {
            cat.debug("vai executar select");
            stmt = conn.createStatement();
            rset = stmt.executeQuery(sqlCommand);

            int cols = rset.getMetaData().getColumnCount();

            while (rset.next()) {
                String[] record = new String[cols];

                for (int i = 0; i < cols; i++) {
                    record[i] = rset.getString(i + 1);
                }

                records.add(record);
            }

            stmt.close();
            stmt = null;
            rset.close();
            rset = null;
            cat.debug(sqlCommand);

            return records;
        } catch (Exception e) {
            cat.error("Comando no ejecutado.", e);
            cat.debug(sqlCommand);

            return null;
        } finally {
            try {
                if (rset != null) {
                    rset.close();
                }

                if (stmt != null) {
                    stmt.close();
                }
            } catch (Exception e3) {
            }

            ;
        }
    }

    private static ArrayList executeSelectSQL(String sqlCommand, Connection conn) {
        Statement stmt = null;
        ResultSet rset = null;
        ArrayList records = new ArrayList();

        try {
            cat.debug("vai executar select");
            stmt = conn.createStatement();
            rset = stmt.executeQuery(sqlCommand);

            int cols = rset.getMetaData().getColumnCount();

            while (rset.next()) {
                String[] record = new String[cols];

                for (int i = 0; i < cols; i++) {
                    record[i] = rset.getString(i + 1);
                }

                records.add(record);
            }

            stmt.close();
            stmt = null;
            rset.close();
            rset = null;
            cat.debug(sqlCommand);

            return records;
        } catch (Exception e) {
            cat.error("Comando no ejecutado.", e);
            cat.debug(sqlCommand);

            return null;
        } finally {
            try {
                if (rset != null) {
                    rset.close();
                }

                if (stmt != null) {
                    stmt.close();
                }
            } catch (Exception e3) {
            }

            ;
        }
    }

    public static Connection getConnection() {
        Connection conn = null;

        try {
            conn = DriverManager.getConnection(
                    "jdbc:protomatter:pool:postgresPool");
            cat.debug("conexao obtida");
        } catch (SQLException e) {
            cat.error("Nao foi possivel obter a conexao do pool ", e);
        }

        if (conn == null) {
            return null;
        } else {
            return conn;
        }
    }

    public static void freeConnection(Connection conn) {
        try {
            if (conn != null) {
                conn.close();
                cat.debug("conexao devolvida");
            }
        } catch (SQLException e) {
            cat.error("Nao foi possivel obter a conexao do pool ", e);
        }
    }

    public static String getLastIdInserted(String noSeq, Connection conn) {
        if (noSeq == null) {
            cat.debug("[SqlUtils] - sequence nula");

            return null;
        }

        ArrayList sqlArray = SqlUtils.executeSelect("select currval('" + noSeq +
                "')", conn);

        if (sqlArray != null) {
            if (sqlArray.size() == 1) {
                String[] recordValue = (String[]) sqlArray.get(0);

                return recordValue[0];
            } else {
                cat.debug("[SqlUtils] - select currval('" + noSeq +
                    "') retornou mais q um registro.");
            }
        } else {
            cat.debug("[SqlUtils] - select currval('" + noSeq +
                "') nao foi executado.");
        }

        return null;
    }

    private static java.sql.Date createAppropriateDate(Object value) {
        if (value == null) {
            return null;
        }

        String valueStr = ((String) value).trim();

        if (valueStr.length() == 0) {
            return null;
        }

        SimpleDateFormat sdf = DateUtils.getDateFormatter();

        Date result = null;

        try {
            result = new java.sql.Date(sdf.parse(valueStr).getTime());
        } catch (Exception exc) {
            result = null;
        }

        if (result == null) {
            // Maybe date has been returned as a timestamp?
            try {
                result = new java.sql.Date(java.sql.Timestamp.valueOf(valueStr)
                                                             .getTime());
            } catch (java.lang.IllegalArgumentException ex) {
                // Try date
                cat.info("date = " + valueStr);
                result = java.sql.Date.valueOf(valueStr);
            }
        }

        return result;
    }

    private static java.math.BigDecimal createAppropriateNumeric(Object value) {
        if (value == null) {
            return null;
        }

        String valueStr = ((String) value).trim();

        if (valueStr.length() == 0) {
            return null;
        }

        return new java.math.BigDecimal(valueStr);
    }

    /**
    this utility-method assigns a particular value to a place holder of a PreparedStatement.
    it tries to find the correct setXxx() value, accoring to the field-type information
    represented by "fieldType".

    quality: this method is bloody alpha (as you migth see :=)
    */
    public static void fillPreparedStatement(PreparedStatement ps, int col,
        Object val, int fieldType) throws SQLException {
        try {
            cat.info("fillPreparedStatement( ps, " + col + ", " + val + ", " +
                fieldType + ")...");

            Object value = null;

            //Check for hard-coded NULL
            if (!("$null$".equals(val))) {
                value = val;
            }

            if (value != null) {
                switch (fieldType) {
                case FieldTypes.INTEGER:
                    ps.setInt(col, Integer.parseInt((String) value));

                    break;

                case FieldTypes.NUMERIC:
                    ps.setBigDecimal(col, createAppropriateNumeric(value));

                    break;

                case FieldTypes.CHAR:
                    ps.setString(col, (String) value);

                    break;

                case FieldTypes.DATE:
                    ps.setDate(col, createAppropriateDate(value));

                    break; //#checkme

                case FieldTypes.TIMESTAMP:
                    ps.setTimestamp(col,
                        java.sql.Timestamp.valueOf((String) value));

                    break;

                case FieldTypes.DOUBLE:
                    ps.setDouble(col,
                        Double.valueOf((String) value).doubleValue());

                    break;

                case FieldTypes.FLOAT:
                    ps.setFloat(col, Float.valueOf((String) value).floatValue());

                    break;

                case FieldTypes.LONG:
                    ps.setLong(col, Long.parseLong(String.valueOf(value)));

                    break;

                case FieldTypes.BLOB:

                    FileHolder fileHolder = (FileHolder) value;

                    try {
                        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
                        ObjectOutputStream out = new ObjectOutputStream(byteOut);
                        out.writeObject(fileHolder);
                        out.flush();

                        byte[] buf = byteOut.toByteArray();

                        byteOut.close();
                        out.close();

                        ByteArrayInputStream bytein = new ByteArrayInputStream(buf);
                        int byteLength = buf.length;

                        ps.setBinaryStream(col, bytein, byteLength);

                        // store fileHolder as a whole (this way we don't lose file meta-info!)
                    } catch (IOException ioe) {
                        ioe.printStackTrace();
                        cat.info(ioe.toString());
                        throw new SQLException(
                            "error storing BLOB in database - " +
                            ioe.toString(), null, 2);
                    }

                    break;

                case FieldTypes.DISKBLOB:
                    ps.setString(col, (String) value);

                    break;

                default:
                    ps.setObject(col, value); //#checkme
                }
            } else {
                switch (fieldType) {
                case FieldTypes.INTEGER:
                    ps.setNull(col, java.sql.Types.INTEGER);

                    break;

                case FieldTypes.NUMERIC:
                    ps.setNull(col, java.sql.Types.NUMERIC);

                    break;

                case FieldTypes.CHAR:
                    ps.setNull(col, java.sql.Types.CHAR);

                    break;

                case FieldTypes.DATE:
                    ps.setNull(col, java.sql.Types.DATE);

                    break;

                case FieldTypes.TIMESTAMP:
                    ps.setNull(col, java.sql.Types.TIMESTAMP);

                    break;

                case FieldTypes.DOUBLE:
                    ps.setNull(col, java.sql.Types.DOUBLE);

                    break;

                case FieldTypes.FLOAT:
                    ps.setNull(col, java.sql.Types.FLOAT);

                    break;

                case FieldTypes.BLOB:
                    ps.setNull(col, java.sql.Types.BLOB);

                case FieldTypes.DISKBLOB:
                    ps.setNull(col, java.sql.Types.CHAR);

                default:
                    ps.setNull(col, java.sql.Types.OTHER);
                }
            }
        } catch (Exception e) {
            throw new SQLException("Field type seems to be incorrect - " +
                e.toString(), null, 1);
        }
    }
}
