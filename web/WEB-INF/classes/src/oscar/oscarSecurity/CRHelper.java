package oscar.oscarSecurity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

import javax.servlet.http.HttpServletRequest;

import net.sf.cookierevolver.CRFactory;
import net.sf.cookierevolver.service.RolesProvider;

import org.apache.log4j.Logger;

import oscar.OscarProperties;
import oscar.oscarDB.DBHandler;

public class CRHelper implements RolesProvider {
	private static Logger log = Logger.getLogger(CRHelper.class);
	
	static boolean enabled=false;
	static HashMap userNameToProviderNO=new HashMap();
	static{
		enabled = OscarProperties.getInstance().getProperty("cr_security","").equals("on");
	}
	
	private static void setProviderNoForUser(String user, String provNo){
		userNameToProviderNO.put(user,provNo);
	}
	
	public static boolean isCRFrameworkEnabled(){
		return enabled;
	}
	public static void recordLoginSuccess(String username, String providerNo, HttpServletRequest req){
		if(enabled){
			setProviderNoForUser(username,providerNo);
			CRFactory.getManager().recordLoginSuccess(req,username);
		}
	}
	public static void recordLoginFailure(String username, HttpServletRequest req){
		if(enabled) CRFactory.getManager().recordLoginFailure(req,username);
	}
	
	public String[] getRolesForUser(String user) {
		DBHandler db = null;
		try {
			db=new DBHandler(DBHandler.OSCAR_DATA);
			Connection con = DBHandler.getConnection();
			String sql = establishStringToUse(con);
			if(sql==null){
				log.warn("No userSecRole table found!");
				return new String[0];
			}
			PreparedStatement st= con.prepareStatement(sql+" WHERE provider_no=?");
			st.setString(1,(String)userNameToProviderNO.get(user));
			ResultSet rs = st.executeQuery();
			HashSet list = new HashSet();
			while(rs.next()){
				list.add(db.getString(rs,1));
			}
			if(queryCaisiRoles(con)){
				rs.close();
				st.close();
				st= con.prepareStatement(CAISI_ROLES_SQL+" WHERE provider_no=?");
				st.setString(1,(String)userNameToProviderNO.get(user));
				rs = st.executeQuery();
				while(rs.next()){
					list.add(db.getString(rs,1));
				}
			}
			rs.close();
			st.close();
			return (String[]) list.toArray(new String[list.size()]);
		} catch (SQLException e) {
			e.printStackTrace();
			return new String[0];
		}finally{
			if(db!=null) try{}catch(Throwable t){}
		}
	}
	
	
	static String sqlToUse=null;
	
	private static final String CAISI_ROLES_SQL = "SELECT name FROM program_provider pp inner join `caisi_role` c on pp.role_id=c.role_id";
	private static final String LOW_CAP_SECUSERROLE_SQL = "SELECT role_name FROM secuserrole";
	private static final String HIGH_CAP_SECUSERROLE_SQL = "SELECT role_name FROM secUserRole";
	
	static Boolean caisiIsAvailable=null;
	
	private boolean queryCaisiRoles(Connection con){
		if(caisiIsAvailable!=null) return caisiIsAvailable.booleanValue();
		caisiIsAvailable = new Boolean(sqlWorks(con,CAISI_ROLES_SQL + " WHERE 1=0"));
		if(caisiIsAvailable.booleanValue()) log.info("Using caisi_role and program_provider tables");
		return caisiIsAvailable.booleanValue();
	}
	
	private String establishStringToUse(Connection con){
		if(sqlToUse!=null) return sqlToUse;
		if(sqlWorks(con,LOW_CAP_SECUSERROLE_SQL + " WHERE 1=0")){
			log.info("Using: "+LOW_CAP_SECUSERROLE_SQL);
			return sqlToUse=LOW_CAP_SECUSERROLE_SQL;
		}
		if(sqlWorks(con,HIGH_CAP_SECUSERROLE_SQL + " WHERE 1=0")){
			log.info("Using: "+HIGH_CAP_SECUSERROLE_SQL);
			return sqlToUse=HIGH_CAP_SECUSERROLE_SQL;
		}
		return null;
	}
	private boolean sqlWorks(Connection con, String s){
		try{
			PreparedStatement st= con.prepareStatement(s);
			ResultSet rs = st.executeQuery();
			LinkedList list = new LinkedList();
			while(rs.next()){
				list.add(oscar.Misc.getString(rs,1));
			}
			return true;
		} catch (Throwable e) {
			return false;
		}
	}

}
