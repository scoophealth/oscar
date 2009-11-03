// -----------------------------------------------------------------------------------------------------------------------
// *
// *
// * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
// * This software is published under the GPL GNU General Public License. 
// * This program is free software; you can redistribute it and/or 
// * modify it under the terms of the GNU General Public License 
// * as published by the Free Software Foundation; either version 2 
// * of the License, or (at your option) any later version. * 
// * This program is distributed in the hope that it will be useful, 
// * but WITHOUT ANY WARRANTY; without even the implied warranty of 
// * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
// * GNU General Public License for more details. * * You should have received a copy of the GNU General Public License 
// * along with this program; if not, write to the Free Software 
// * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. * 
// * 
// * <OSCAR TEAM>
// * This software was written for the 
// * Department of Family Medicine 
// * McMaster Unviersity 
// * Hamilton 
// * Ontario, Canada 
// *
// -----------------------------------------------------------------------------------------------------------------------
package oscar.oscarMessenger.config.data;



import java.util.Locale;
import java.util.ResourceBundle;

import javax.servlet.jsp.JspWriter;

import oscar.oscarDB.DBHandler;
/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class MsgMessengerGroupData {
   public  java.util.Vector groupMemberVector;
   public  int numGroups;


///////////---------------------------------------------------------------------
   public String getMyName(String grpNo){
      String retval = new String("Root");
      if (Integer.parseInt(grpNo) > 0){
         try{

              DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
              java.sql.ResultSet rs;
              String sql = new String("select groupDesc from groups_tbl where groupID = '"+grpNo+"'");
              rs = db.GetSQL(sql);
                 if (rs.next()){
                    retval = db.getString(rs,"groupDesc");
                 }
              rs.close();
         }catch (Exception e){ e.printStackTrace(System.out); }
      }


      return retval;
   }
///////////---------------------------------------------------------------------
   public String parentDirectory(String grpNo){
         String retval = new String();
         try{
           DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
           java.sql.ResultSet rs;
           String sql = new String("select parentID from groups_tbl where groupID = '"+grpNo+"'");
           rs = db.GetSQL(sql);
              if (rs.next()){
                 //out.print("<a href=\"Admin.jsp?groupNo="+db.getString(rs,"parentID")+"\">Click here to got the parent group</a><br>");
                 retval = db.getString(rs,"parentID");
              }
           rs.close();
         }catch (Exception e){ e.printStackTrace(System.out); }
      return retval;
   }

//////////////////--------------------------------------------------------------

   public String printGroups(String groupNo){
      StringBuffer stringBuffer = new StringBuffer();
      numGroups = 0;
      try{
           DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
           java.sql.ResultSet rs;
           String sql = new String("select * from groups_tbl where parentID = '"+groupNo+"'");
           rs = db.GetSQL(sql);
              while (rs.next()){
                 //out.print("<a href=\"Admin.jsp?groupNo="+db.getString(rs,"groupID")+"\">"+db.getString(rs,"groupDesc")+"</a><br>");
                 stringBuffer.append("<a href=\"MessengerAdmin.jsp?groupNo="+db.getString(rs,"groupID")+"\">"+db.getString(rs,"groupDesc")+"</a><br>");
                 numGroups++;
              }
           rs.close();
      }catch (Exception e){ e.printStackTrace(System.out); }
   return stringBuffer.toString();
   }

////----------------------------------------------------------------------------

   public java.util.Vector membersInGroups(String grpNo){

      groupMemberVector = new java.util.Vector();
      try{
        DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
        java.sql.ResultSet rs;
        String sql = new String("select * from groupMembers_tbl where groupID = '"+grpNo+"'");
        rs = db.GetSQL(sql);
        while (rs.next()){
          groupMemberVector.add(db.getString(rs,"provider_No"));
        }
        rs.close();
      }catch (java.sql.SQLException e){ e.printStackTrace(System.out); }

      return groupMemberVector;
   }


   public void printAllProvidersWithMembers(Locale locale,String grpNo,JspWriter out){

         java.util.Vector vector = membersInGroups(grpNo);

         try{
              DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
              java.sql.ResultSet rs;
              String sql = new String("select * from provider order by last_name");
              rs = db.GetSQL(sql);
              while (rs.next()){
                  out.print("   <tr>");
                  out.print("      <td>");
                 if ( vector.contains(db.getString(rs,"provider_no")) ){
                       out.print("<input type=\"checkbox\" name=providers value="+db.getString(rs,"provider_no")+" checked >");
                 }else{
                       out.print("<input type=\"checkbox\" name=providers value="+db.getString(rs,"provider_no")+">");
                 }
                  out.print("      </td>");
                  out.print("      <td>");
                  out.print(db.getString(rs,"last_name"));
                  out.print("      </td>");
                  out.print("      <td>");
                  out.print(db.getString(rs,"first_name"));
                  out.print("      </td>");
                  out.print("      <td>");

                  String strProviderType = new String();
                  ResourceBundle oscarR = ResourceBundle.getBundle("oscarResources",locale);
                  
                  if (db.getString(rs,"provider_type").equals("doctor")) {
                  	strProviderType = oscarR.getString("admin.provider.formType.optionDoctor");
                  }else if (db.getString(rs,"provider_type").equals("receptionist")) {
                  	strProviderType = oscarR.getString("admin.provider.formType.optionReceptionist");
                  }else if (db.getString(rs,"provider_type").equals("nurse")) {
                  	strProviderType = oscarR.getString("admin.provider.formType.optionNurse");
                  }else if (db.getString(rs,"provider_type").equals("resident")) {	
                  	strProviderType = oscarR.getString("admin.provider.formType.optionResident");
                  }else if (db.getString(rs,"provider_type").equals("admin")) {
                  	strProviderType = oscarR.getString("admin.provider.formType.optionAdmin");
                  }else{
                  	strProviderType = db.getString(rs,"provider_type");
                  }

                  out.print(strProviderType);
                  
                  out.print("      </td>");


                  out.print("   </tr>");
              }


              rs.close();
          }catch (Exception e){ e.printStackTrace(System.out); }

   }

   public String printAllBelowGroups(String grpNo){
      StringBuffer stringBuffer = new StringBuffer();
      int untilZero = Integer.parseInt(grpNo);

         try{
           DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
           java.sql.ResultSet rs;

           while (untilZero != 0){
              String sql = new String("select groupID, parentID, groupDesc from groups_tbl where groupID = '"+Integer.toString(untilZero)+"'");
              rs = db.GetSQL(sql);
                 if (rs.next()){

                  untilZero = Integer.parseInt(db.getString(rs,"parentID"));
                  stringBuffer.insert(0," <a href=\"MessengerAdmin.jsp?groupNo="+db.getString(rs,"groupID")+"\"> > "+db.getString(rs,"groupDesc")+"</a>");
                 }
                 else{
                  untilZero = 0;
                 }
              rs.close();
           }
         }catch (Exception e){ e.printStackTrace(System.out); }
         stringBuffer.insert(0,"<a href=\"MessengerAdmin.jsp?groupNo=0\">Root</a>");
         return stringBuffer.toString();
   }


}
