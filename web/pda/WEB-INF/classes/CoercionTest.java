  
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

package bean;

import java.lang.Class;
import java.lang.ClassNotFoundException;
import java.lang.Object;
import java.lang.String;
import java.lang.System;
import java.lang.Character;

import java.math.BigDecimal;

import java.sql.Connection;
import java.sql.DataTruncation;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;

import java.io.InputStream;
import java.io.IOException;


public class CoercionTest {
	
	public static void main(String args[]) {
	
		//  - Can we find the Driver library and the native shared library
	
		try {
//			Class.forName("com.informagen.jdbc.dtF.Driver");
			Class.forName("org.gjt.mm.mysql.Driver");
		} catch(ClassNotFoundException e) {
			System.out.println(e.toString());
		}

		String database = "Coercion";
		Connection aConnection = createDatabase(database);

		if ( aConnection == null ) {
			System.out.println("Failed to create/access the coercion database");
			return;
		}
			
			
		Statement aStatement = null;
		
		try {

			aStatement = aConnection.createStatement();
			ResultSet theResults = aStatement.executeQuery("SELECT * FROM test");
			
			ResultSetMetaData theMetaData = theResults.getMetaData();
			
			System.out.println("Column count: " + theMetaData.getColumnCount());
			//System.out.println("   Row count: " + theMetaData.getRowCount());

			
		do {
		
				System.out.println("===========================================================");


				for (int index = 1; index <= theMetaData.getColumnCount(); ++index) {
				
					System.out.println("-----------------------------------------------------------");
					System.out.println("        Column name: " + theMetaData.getColumnName(index));
					System.out.println("   JDBC type number: " + theMetaData.getColumnType(index));
					System.out.println("     DBMS type name: " + theMetaData.getColumnTypeName(index));
					System.out.println();
					
					switch (theMetaData.getColumnType(index)) {
													
						case Types.TINYINT:	
						case Types.SMALLINT:	
						case Types.INTEGER:	
						case Types.BIGINT:
						case Types.REAL:
						case Types.FLOAT:	
						case Types.DOUBLE:	
						case Types.DECIMAL:	
						case Types.NUMERIC:	
						case Types.BIT:	
							System.out.println("          getByte(): " + theResults.getByte(index)); 
							System.out.println("         getShort(): " + theResults.getShort(index)); 
							System.out.println("           getInt(): " + theResults.getInt(index)); 
							System.out.println("          getLong(): " + theResults.getLong(index)); 
							System.out.println("         getFloat(): " + theResults.getFloat(index)); 
							System.out.println("        getDouble(): " + theResults.getDouble(index)); 
							System.out.println("    getBigDecimal(): " + theResults.getBigDecimal(index, 4)); 
							System.out.println("       getBoolean(): " + theResults.getBoolean(index)); 
							System.out.println("        getString(): " + theResults.getString(index)); 
							System.out.println("        getObject(): " + getObjectClassName(theResults, index)); 
							break;


						case Types.CHAR:	
						case Types.VARCHAR:	
						case Types.LONGVARCHAR:
							System.out.println("          getByte(): " + theResults.getByte(index)); 
							System.out.println("         getShort(): " + theResults.getShort(index)); 
							System.out.println("           getInt(): " + theResults.getInt(index)); 
							System.out.println("          getLong(): " + theResults.getLong(index)); 
							System.out.println("         getFloat(): " + theResults.getFloat(index)); 
							System.out.println("        getDouble(): " + theResults.getDouble(index)); 
							System.out.println("    getBigDecimal(): " + theResults.getBigDecimal(index, 4)); 
							System.out.println("       getBoolean(): " + theResults.getBoolean(index)); 
							System.out.println("        getString(): " + theResults.getString(index)); 
							System.out.println("          getDate(): " + theResults.getDate(index)); 
							System.out.println("          getTime(): " + theResults.getTime(index)); 
							System.out.println("     getTimestamp(): " + theResults.getTimestamp(index)); 
							System.out.println("   getAsciiStream(): " +  
							                       showAsciiStream(theResults.getAsciiStream(index), 25)); 
							System.out.println(" getUnicodeStream(): " + 
							                     showUnicodeStream(theResults.getUnicodeStream(index), 25)); 
							System.out.println("        getObject(): " + getObjectClassName(theResults, index)); 
							break;



						case Types.BINARY:	
						case Types.VARBINARY:	
						case Types.LONGVARBINARY:	
							System.out.println("        getString(): " + theResults.getString(index)); 
							System.out.println("         getBytes(): " + showBytes(theResults.getBytes(index),25));
							System.out.println("  getBinaryStream(): " + 
							             showBinaryStream(theResults.getAsciiStream(index), 25)); 
							System.out.println("   getAsciiStream(): " + 
							             showAsciiStream(theResults.getAsciiStream(index), 25)); 
							System.out.println(" getUnicodeStream(): " + 
							             showUnicodeStream(theResults.getAsciiStream(index), 25)); 
							System.out.println("        getObject(): " + getObjectClassName(theResults, index)); 
							break;



						case Types.DATE:		
							System.out.println("        getString(): " + theResults.getString(index)); 
							System.out.println("          getDate(): " + theResults.getDate(index)); 
							System.out.println("     getTimestamp(): " + theResults.getTimestamp(index)); 
							System.out.println("        getObject(): " + getObjectClassName(theResults, index)); 
							break;


						case Types.TIME:		
							System.out.println("        getString(): " + theResults.getString(index)); 
							System.out.println("          getTime(): " + theResults.getTime(index)); 
							System.out.println("     getTimestamp(): " + theResults.getTimestamp(index)); 
							System.out.println("        getObject(): " + getObjectClassName(theResults, index)); 
							break;

						case Types.TIMESTAMP:		
							System.out.println("        getString(): " + theResults.getString(index)); 
							System.out.println("          getDate(): " + theResults.getDate(index)); 
							System.out.println("          getTime(): " + theResults.getTime(index)); 
							System.out.println("     getTimestamp(): " + theResults.getTimestamp(index)); 
							System.out.println("        getObject(): " + getObjectClassName(theResults, index)); 
							break;
					}
	
					System.out.println("           Was NULL: " + theResults.wasNull());
					
					displayWarnings(theResults.getWarnings());
					theResults.clearWarnings();
				}
				
				
			} while (theResults.next());


			
		} catch(SQLException sqle) {
			sqle.printStackTrace();
			System.out.println(sqle.toString());
		} finally {
		
			try {
				aStatement.close();
				aConnection.close();
				System.out.println("Closing database connection.");
			} catch (SQLException e) {}
					
		}
			 
	}


	public static void displayWarnings(SQLWarning warning) {
		
		while ( warning != null ) {
			if ( warning instanceof DataTruncation )
				System.out.println("Data truncation error: " + 
				                   ((DataTruncation)warning).getDataSize() + " bytes should have been " +
				                   (((DataTruncation)warning).getRead() ? "read, " : "written, ") +
				                   ((DataTruncation)warning).getTransferSize() + " actually were.");
			else
				System.out.println(" Warning: " + warning.getMessage());
			warning = warning.getNextWarning();
		}
					
	
	}
	
	
	public static String getObjectClassName(ResultSet theResults, int index) throws SQLException {
	
		Object object = theResults.getObject(index);
		
		return ( object != null ) ? object.getClass().toString() : "null";
	
	}


	public static String showBytes(byte[] bytes, int show) {
	
		if ( bytes == null)
			return "null";
			
		StringBuffer sb = new StringBuffer("length=");
		sb.append(bytes.length).append(", ");
		
		int i = 0;
		
		while ((i < show) && (bytes.length > i))
			sb.append("0x").append(Integer.toHexString(bytes[i++]).toString()).append(" ");
	
		return sb.toString();
	}



	public static String showBinaryStream(InputStream in, int show) {
	
		if ( in == null)
			return "null";
			
		StringBuffer sb = new StringBuffer("");
		
		int i = 0;
		int c = 0;

		try {		
			while ((i++ < show) && ((c = in.read()) != -1) ) {
					sb.append("0x").append(Integer.toHexString(c).toString()).append(",");
			}
		} catch (IOException ioe) {}
		
		return sb.toString();
	}



	 public static String showAsciiStream(InputStream in, int show) {
	
		if ( in == null)
			return "null";
			
		StringBuffer sb = new StringBuffer("");
		
		int i = 0;
		int c = 0;

		try {		
			while ((i++ < show) && ((c = in.read()) != -1) ) {
//				if ( Character.isISOControl((char)c) )
//					sb.append("0x").append(Integer.toHexString(c).toString()).append(",");
// System.out.println(i+"---2-----"+new Character((char)c).charValue()+"==");

  			     if ( Character.isISOControl((char)c)) {
//                                 if ( (new Character(c)).equals("\n")){  

// System.out.println(i+"---1*****"+Character.getNumericValue((char)c)+"==");
// System.out.println(i+"---2-----"+new Character((char)c).charValue()+"==");
//                                  if ( (new Character((char)c).charValue()=='\u0010')||(new Character((char)c).charValue()==13) ){  
                                  if ( (new Character((char)c).charValue()==10)||(new Character((char)c).charValue()==13) ){  

// System.out.println("---1++++++++++==+++++++++++++++++++++++++++++++++++++++===");
//                                        if ( (Character.getNumericValue((char)c)== '\u0010')||(Character.getNumericValue((char)c)== '\u0013' )){  
//					sb.append(Integer.toHexString(c).toString());
      					sb.append("<br>");
  				       }else{
//					sb.append(Integer.toHexString(c).toString());
                                        
 					sb.append("");
//					sb.append("<br>");
  
                                       } 
				}else{
//					sb.append("'").append(new Character((char)c).toString()).append("',");
					sb.append(new Character((char)c).toString());
                                } 
			}
		} catch (IOException ioe) {}
		
		return sb.toString();
	}

	public static String showUnicodeStream(InputStream in, int show) {
	
		if ( in == null)
			return "null";
			
		StringBuffer sb = new StringBuffer("");
		
		int i = 0;
		int c = 0;

		try {		
			while ((i++ < show) && ((c = in.read()) != -1) )

					sb.append("\\u").append(Integer.toHexString(c).toString()).append(",");
		
		} catch (IOException ioe) {}
		

		return sb.toString();
	}



	public static Connection createDatabase(String database) {

		Connection aConnection = null;
		Statement aStatement = null;
		PreparedStatement preparedStmt = null;
		SQLWarning warning = null;
		int rows = 0;
		
		
		// Some BLOB (i.e. byte[]) data, ASCII and binary
		//	
		// This array is smaller than the maximum string size and 
		//  should be returned with getString() as well as getBytes();
		
		byte[] smallArray = new byte[200];
		for (int i=0; i<smallArray.length; i++) {
			byte aChar = (byte)((i%26) + 'A');
			smallArray[i] = aChar;
		}
	
		
		// An array containing ASCII bytes but larger than the
		//  maximum string size. getString() will return null but
		//  getBytes() will return the entire BLOB.
		
		byte[] asciiArray = new byte[512];
		for (int i=0; i<asciiArray.length; i++) {
			byte aChar = (byte)((i%26) + 'A');
			asciiArray[i] = aChar;
		}
		
		// A large array containing binary values.
		
		byte[] binaryArray = new byte[1024];
		for (int i=0; i<binaryArray.length; i++)
			binaryArray[i] = (byte)i;



			
		
//		String jdbcURL = "jdbc:dtF:" + database + ";Create=always";
		String jdbcURL = "jdbc:mysql:" + database + ";Create=always";		


		try {
			
 			aConnection = DriverManager.getConnection(jdbcURL, "dtfadm", "dtfadm");

//  jdbc:mysql:///mydb?user=zt&password=zt
 			
			if ( aConnection == null)
				return null;

			// Create the table
			
			aStatement = aConnection.createStatement();

			aStatement.executeUpdate("CREATE TABLE test (" +
			                             " rowID int," +
			                             " c1 byte," +
			                             " c2 byte," +
			                             " c3 char," +
			                             " c4 word," +
			                             " c5 short," +
			                             " c6 longword," +
			                             " c7 long," +
			                             " c8 real," +
			                             " c9 decimal(8,2)," +
			                             " c10 date," +
			                             " c11 time," +
			                             " c12 shortstring," +
			                             " c13 char(5)," +
			                             " c14 varchar(15)," +
			                             " c15 shortstring," +
			                             " c16 bit," +
			                             " c17 bit," +
			                             " c18 bit)"
			                           );

			displayWarnings(aStatement.getWarnings());
			aStatement.close();


			
			// Use a prepared statement to load a varied set of datatypes
			
			preparedStmt = 
			  aConnection.prepareStatement("INSERT INTO test (rowID,c1,c2,c3,c4,c5,c6,c7,c8,c9,c10,c11,c12,c13,c14,c15,c16) " +
			                                "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");

			// Row 1: set many different types of values
			int col = 1;
			int rowID = 1;
			
			preparedStmt.setInt(col++, rowID++);
			
			// c1, c2 - byte
			preparedStmt.setBoolean(col++, false);
			preparedStmt.setBoolean(col++, true);

			// c3 - char
			preparedStmt.setByte(col++, (byte)12);

			// c4, c5 - word and short
			preparedStmt.setShort(col++, (short)123);
			preparedStmt.setShort(col++, (short)1234);

			// c6, c7 - longword and long
			preparedStmt.setInt(col++, 12345);
			preparedStmt.setInt(col++, 123456);

			// c8, c9 - real and decimal
			preparedStmt.setFloat(col++, 99.123456f);
			preparedStmt.setBigDecimal(col++, new BigDecimal("99.12"));

			// c10, c11, c12 - time, date, timestamp as shortstring
			preparedStmt.setDate(col++, new Date(99, 2, 12));
			preparedStmt.setTime(col++, new Time(9, 10, 20));
			preparedStmt.setTimestamp(col++, new Timestamp(100, 0, 1, 12, 0, 1, 100000));

			// c13, c14, c15 - char(), varchar() and shortstring
			preparedStmt.setString(col++, "Hello");
			preparedStmt.setString(col++, "Hello World");
			preparedStmt.setString(col++, "The quick brown fox jumped over the lazy hounds back.");

			// c16 - bit (only one BIT (aka BLOB) field can be set with a prepared statement.
			preparedStmt.setBytes(col++, smallArray);


			// Execute, result must be one row
						
			rows = preparedStmt.executeUpdate();
			System.out.println("preparedStmt.executeUpdate() " + rows + ", S/B 1");
				
			displayWarnings(preparedStmt.getWarnings());

			
			// Row 2: set values as objects using the same Prepared Statement
			
			col = 1;
			
			preparedStmt.setInt(col++, rowID++);

			// c1, c2 - byte
			preparedStmt.setObject(col++, new Boolean(false), Types.BIT);
			preparedStmt.setObject(col++, new Boolean(true), Types.BIT);

			// c3 - char
			preparedStmt.setObject(col++, "12", Types.TINYINT);

			// c4, c5 - word and short
			preparedStmt.setObject(col++, "123", Types.SMALLINT);
			preparedStmt.setObject(col++, new Integer(1234), Types.SMALLINT);

			// c6, c7 - longword and long
			preparedStmt.setObject(col++, new Integer(12345), Types.INTEGER);
			preparedStmt.setObject(col++, new Long(123456));

			// c8, c9 - real and decimal
			preparedStmt.setObject(col++, new Float(99.12), Types.FLOAT);
			preparedStmt.setObject(col++, "99.12", Types.DECIMAL, 2);

			// c10, c11, c12 - time, date, timestamp as shortstring
			preparedStmt.setObject(col++, new Date(99, 2, 12), Types.DATE);
			preparedStmt.setObject(col++, new Time(9, 10, 20), Types.TIME);
			preparedStmt.setObject(col++, new Timestamp(100, 0, 1, 12, 0, 1, 100000), Types.TIMESTAMP);

			// c13, c14, c15 - char(), varchar() and shortstring
			preparedStmt.setObject(col++, (Object)"Hello");
			preparedStmt.setObject(col++, "Hello World", Types.VARCHAR);
			preparedStmt.setObject(col++, "The quick brown fox jumped over the lazy hounds back.", Types.VARCHAR);

			// c16 - bit (only one BIT field can be set with an INSERT statement.
			preparedStmt.setBytes(col++, binaryArray);

			// Execute, result must be one row
						
			rows = preparedStmt.executeUpdate();
			System.out.println("preparedStmt.executeUpdate() " + rows + ", S/B 1");
				
			displayWarnings(preparedStmt.getWarnings());


			
			// Row 3: NULL value testing 

			aStatement = aConnection.createStatement();
			rows = aStatement.executeUpdate("INSERT INTO test (rowID,c1,c2,c3,c4,c5,c6,c7,c8,c9,c10,c11,c12,c13,c14,c15) " +
			                           "VALUES (" + rowID + 
			                           ",null,null,null,null,null,null,null,null,null,null,null,null,null,null,null)");

			System.out.println("aStatement.executeUpdate() " + rows + ", S/B 1");
				
			displayWarnings(preparedStmt.getWarnings());
			aStatement.close();


			// Row 4: Alternate non-NULL value and NULL value looking for side-effects, set
			//   c16,c17, and c18 with UPDATE statements.


			aStatement = aConnection.createStatement();
			
			rows = aStatement.executeUpdate("INSERT INTO test (rowID,c1,c2,c3,c4,c5,c6,c7,c8,c9,c10,c11,c12,c13,c14,c15) " +
			                     "VALUES (" + ++rowID + ",0,null,12,null,1234,null,123456,null," +
			                     "99.12,null,'9:10:20',null,'Hello','1999-07-01','09:10:20')");
						
			System.out.println("aStatement.executeUpdate() " + rows + ", S/B 1");
			displayWarnings(aStatement.getWarnings());
			aStatement.close();


			// Use update to set c16 

			preparedStmt = aConnection.prepareStatement("UPDATE test SET c16 = ? WHERE rowID = 4");
			preparedStmt.setBytes(1, smallArray);

			// Execute, result should be one row
						
			rows = preparedStmt.executeUpdate();
			System.out.println("aStatement.executeUpdate() " + rows + ", S/B 1");
			displayWarnings(preparedStmt.getWarnings());
			preparedStmt.close();



			// Use update to set c17 

			preparedStmt = aConnection.prepareStatement("UPDATE test SET c17 = ? WHERE rowID = 4");
			preparedStmt.setBytes(1, asciiArray);

			// Execute, result should be one row
						
			rows = preparedStmt.executeUpdate();
			System.out.println("aStatement.executeUpdate() " + rows + ", S/B 1");
			displayWarnings(preparedStmt.getWarnings());
			preparedStmt.close();



			// Use update to set c18 

			preparedStmt = aConnection.prepareStatement("UPDATE test SET c18 = ? WHERE rowID = 4");
			preparedStmt.setBytes(1, binaryArray);

			// Execute, result should be one row
						
			rows = preparedStmt.executeUpdate();
			System.out.println("aStatement.executeUpdate() " + rows + ", S/B 1");
			displayWarnings(preparedStmt.getWarnings());
			preparedStmt.close();



		} catch(SQLException sqle) {
			System.out.println(sqle.toString());
			return null;
		} finally {
		
			try {
				if ( aStatement != null )
					aStatement.close();
					
				if ( preparedStmt != null )
					preparedStmt.close();
					
			} catch(SQLException sqle) {
				System.out.println("Error in finally: " + sqle.toString());
			}
			
		}
		
		return aConnection;
			 
	}




}
