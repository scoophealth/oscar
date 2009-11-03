package oscar.form.study.HSFO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import oscar.oscarDB.DBHandler;
import oscar.oscarDemographic.data.DemographicData;
import oscar.oscarDemographic.data.DemographicData.Demographic;
/**
 *Class used by the HSFO Study
 * 
 */
public class RecommitDAO {
	 public RecommitDAO() {
	 }
	 
	 public RecommitSchedule getLastSchedule(boolean statusFlag) throws SQLException{
		 
		 PreparedStatement st = null;
	     String sqlstatement ="select * from hsfo_recommit_schedule ";
	     if (statusFlag) sqlstatement+="where status='D' ";
	     sqlstatement+="order by id desc";
	     DBHandler db =null;
	     ResultSet rs = null;
	     RecommitSchedule reSchedule=null;
	     SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	     try {
	            db = new DBHandler(DBHandler.OSCAR_DATA);
	            Connection connect = DBHandler.getConnection();
	            st = connect.prepareStatement(sqlstatement);
	            st.execute();
	            rs = st.getResultSet();
	            
	            if (rs.next()){
	            	reSchedule=new RecommitSchedule();
	            	reSchedule.setId(new Integer(rs.getInt("id")));
	            	reSchedule.setStatus(db.getString(rs,"status"));
	            	reSchedule.setMemo(db.getString(rs,"memo"));
	            	reSchedule.setSchedule_time(sf.parse(db.getString(rs,"schedule_time")));
	            	reSchedule.setUser_no(db.getString(rs,"user_no"));
	            	reSchedule.setCheck_flag(rs.getBoolean("check_flag"));
	            }
				
				
	     }catch (SQLException se) {
	            System.out.println("SQL Error while getting the latest record from the hsfo_xml_recommit table : "+ se.toString());
	        }catch (Exception ne) {
	            System.out.println("Other Error while getting the latest record from the hsfo_xml_recommit table : "+ ne.toString());
	        }finally {
	        	if (rs != null)
	    			try {
	    				
	    				rs.close();
	    			} catch (SQLException e) {
				}
				if (st != null)
					try {
						st.close();
					} catch (SQLException e) {
					}
			}
	        return reSchedule;
	 }
	 
	 public void updateLastSchedule(RecommitSchedule rd)throws SQLException{
		 PreparedStatement st = null;
	     String sqlstatement ="update hsfo_recommit_schedule set id=?, status=?, "+
	     		"memo=?, schedule_time=?,user_no=?,check_flag=? where id=?";
	     SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	     DBHandler db =null;
	     
	     try {
	    	 db = new DBHandler(DBHandler.OSCAR_DATA);
	         Connection connect = DBHandler.getConnection();
	         st = connect.prepareStatement(sqlstatement);
	         st.setString(1,rd.getId().toString());
	         st.setString(2,rd.getStatus());
	         st.setString(3,rd.getMemo());
	         st.setString(4,sf.format(rd.getSchedule_time()));
	         st.setString(5, rd.getUser_no());
	         st.setBoolean(6,rd.isCheck_flag());
	         st.setString(7, rd.getId().toString());
	         st.executeUpdate();
	     }catch (SQLException se) {
	         System.out.println("SQL Error while update the latest record to the hsfo_xml_recommit table : "+ se.toString());
	     }catch (Exception ne) {
	         System.out.println("Other Error while update the latest record to the hsfo_xml_recommit table : "+ ne.toString());
	     }finally {
	        	
	    	 if (st != null)
				try {
						st.close();
					} catch (SQLException e) {
					}
			}
	     
	 }
	 
	 public void insertchedule(RecommitSchedule rd)throws SQLException{
		 PreparedStatement st = null;
	     String sqlstatement ="insert into hsfo_recommit_schedule set status=?, "+
	     		"memo=?, schedule_time=?,user_no=?,check_flag=?";
	     SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	     DBHandler db =null;
	     
	     try {
	    	 db = new DBHandler(DBHandler.OSCAR_DATA);
	         Connection connect = DBHandler.getConnection();
	         st = connect.prepareStatement(sqlstatement);
	         
	         st.setString(1,rd.getStatus());
	         st.setString(2,rd.getMemo());
	         st.setString(3,sf.format(rd.getSchedule_time()));
	         st.setString(4, rd.getUser_no());
	         st.setBoolean(5,rd.isCheck_flag());
	         
	         st.executeUpdate();
	     }catch (SQLException se) {
	         System.out.println("SQL Error while insert a new record to the hsfo_xml_recommit table : "+ se.toString());
	     }catch (Exception ne) {
	         System.out.println("Other Error while insert a new record to the hsfo_xml_recommit table : "+ ne.toString());
	     }finally {
	        	
	    	 if (st != null)
				try {
						st.close();
					} catch (SQLException e) {
					}
			}
	     
	 }
	 
	 public boolean isLastActivExpire() throws SQLException{
		 boolean exp=false;
		 RecommitSchedule rd=getLastSchedule(false);
		 if (rd!=null && !"D".equalsIgnoreCase(rd.getStatus())){
			 if (rd.getSchedule_time().before(new Date())) exp=true;
		 }
		 return exp;
	 }
	 
	 public void deActiveLast() throws SQLException{
		 RecommitSchedule rd=getLastSchedule(false);
		 if (rd!=null && !"D".equalsIgnoreCase(rd.getStatus())){
			 rd.setStatus("D");
			 updateLastSchedule(rd);
		 }
	 }
	 
	 public String SynchronizeDemoInfo() throws SQLException{
		 HSFODAO hsfoDao=new HSFODAO();
		 List idList=hsfoDao.getAllPatientId();
		 Iterator itr=idList.iterator();
		 DemographicData demoData = new DemographicData();
		 while (itr.hasNext()){
			 String pid=(String)itr.next();
			 PatientData pd=hsfoDao.retrievePatientRecord(pid);
			 Demographic demo=demoData.getDemographic(pid);
			 String internalId=demo.getProviderNo();
			 if(internalId==null || internalId.length()==0){
				 return demo.getLastName()+","+demo.getFirstName();
			 }
			 pd.setBirthDate(demo.getDOBObj());
			 pd.setSex(demo.getSex().toLowerCase());
			 if (demo.getLastName()!=null 
					 && demo.getLastName().trim().length()>0)
				 pd.setLName(demo.getLastName());
			 if (demo.getFirstName()!=null 
					 && demo.getFirstName().trim().length()>0) 
				 pd.setFName(demo.getFirstName());
			 
			 String pcode=demo.getPostal().trim();
			 if (pcode!=null && pcode.length()>=3)
				 pd.setPostalCode(pcode.substring(0, 3));
			 hsfoDao.updatePatient(pd);
		 }
		 return null;
	 }
	 
	 public String checkProvider()throws SQLException{
		 HSFODAO hsfoDao=new HSFODAO();
		 List idList=hsfoDao.getAllPatientId();
		 Iterator itr=idList.iterator();
		 DemographicData demoData = new DemographicData();
		 while (itr.hasNext()){
			 String pid=(String)itr.next();
			 Demographic demo=demoData.getDemographic(pid);
			 String internalId=demo.getProviderNo();
			 if(internalId==null || internalId.length()==0){
				 return demo.getLastName()+","+demo.getFirstName();
			 }
			 
		 }
		 return null;
	 }
	 
	 
}