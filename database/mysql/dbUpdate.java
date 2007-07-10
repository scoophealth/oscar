import java.sql.*;
import java.util.UUID;

public class dbUpdate {
	public static void main( String[] args ) {

		try {
			Statement stmt, stmtUpdate;
			ResultSet rs, rsUpdate;

			Class.forName("com.mysql.jdbc.Driver");

			String url = "jdbc:mysql://localhost:3306/oscar_mcmaster";
			Connection con = DriverManager.getConnection(url, "root", "changeme");

			stmt = con.createStatement();			

			rs = stmt.executeQuery("SELECT note_id FROM casemgmt_note where uuid is null");

			stmtUpdate = con.createStatement();
			UUID uuid;
			String updateSQL;
			while(rs.next()) {
				uuid = UUID.randomUUID();
				String id = rs.getString("note_id");
				updateSQL = "UPDATE casemgmt_note set uuid = '" + uuid + "' WHERE note_id = " + id;
				if( stmtUpdate.executeUpdate(updateSQL) != 1 ) 
					throw new Exception(updateSQL);				
			}

			con.close();
		}
		catch( Exception e ) {

			System.out.println("DB ERROR: " + e.getMessage());
			e.printStackTrace();

		}
	}

}
