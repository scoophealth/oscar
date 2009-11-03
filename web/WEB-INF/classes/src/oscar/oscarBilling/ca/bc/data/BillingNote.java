/*
 * BillingNote.java
 *
 * Created on August 17, 2004, 1:30 PM
 */

package oscar.oscarBilling.ca.bc.data;


import java.sql.ResultSet;
import java.sql.SQLException;

import oscar.Misc;
import oscar.oscarDB.DBHandler;
import oscar.util.UtilMisc;

/**
 *+------------------+------------+------+-----+---------+----------------+
  | Field            | Type       | Null | Key | Default | Extra          |
  +------------------+------------+------+-----+---------+----------------+
  | billingnote_no   | int(10)    |      | PRI | NULL    | auto_increment |
  | billingmaster_no | int(10)    |      | MUL | 0       |                |
  | createdate       | datetime   | YES  | MUL | NULL    |                |
  | provider_no      | varchar(6) |      | MUL |         |                |
  | note             | text       | YES  |     | NULL    |                |
  | note_type        | int(2)     | YES  |     | NULL    |                |
  +------------------+------------+------+-----+---------+----------------+
 * @author  root
 */
public class BillingNote {

   /** Creates a new instance of BillingNote */
   public BillingNote() {
   }

   //
   public boolean hasNote(String billingmaster_no){
      boolean hasNote = false;
      String notesql = "select * from billingnote where billingmaster_no = '"+billingmaster_no+"' and note_type = '2'";
      try{
         DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
         ResultSet rs = db.GetSQL(notesql);
         if(rs.next()){
            hasNote = true;
         }
         rs.close();
      }catch (Exception e){
         e.printStackTrace();
      }
      return hasNote;
   }



public void addNote(String billingmaster_no,String provider_no,String note) throws SQLException{

      note = oscar.Misc.removeNewLine(note);
      String  notesql = "insert into billingnote (billingmaster_no,provider_no,createdate,note,note_type) values ( " +
                        "'"+billingmaster_no+"'," +
                        "'"+provider_no+"'," +
                        "now()," +
                        "'"+UtilMisc.mysqlEscape(note)+"'," +
                        "'2')";

      DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
                db.RunSQL(notesql);
   }

public void addNoteFromBillingNo(String billingNo, String provider,String note) throws SQLException{
   note = oscar.Misc.removeNewLine(note);
   String sql = "select billingmaster_no from billingmaster where billing_no = '"+billingNo+"' ";
      try{
         DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
         ResultSet rs = db.GetSQL(sql);
         while(rs.next()){
            String billingMasterNo =  rs.getString("billingmaster_no");
            addNote(billingMasterNo,provider,note);
         }
         rs.close();
      }catch (Exception e){
         e.printStackTrace();
      }

}

   /**
    *
    * @param billingmaster_no billingmaster_no from billingmaster table to get the full note class
    * @return Returns a Note Class
    */
   public Note getFullNote(String billingmaster_no){
      Note n = new Note();
      String notesql = "select * from billingnote where billingmaster_no = '"+billingmaster_no+"' and note_type = '2' order by createdate desc limit 1";
      try{
      DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
      ResultSet rs = db.GetSQL(notesql);
      if(rs.next()){
         n.setBillingnote_no(rs.getString("billingnote_no"));
         n.setBillingmaster_no(rs.getString("billingmaster_no"));
         n.setCreatedate(rs.getString("createdate"));
         n.setProviderNo(rs.getString("provider_no"));
         n.setNote(rs.getString("note"));
      }
      rs.close();
      }catch (Exception e){
         e.printStackTrace();
      }
      return n;
   }

   //TODO make sure this is the latest note
   public String getNote(String billingmaster_no){
      String retStr = "";
      String notesql = "select note from billingnote where billingmaster_no = '"+billingmaster_no+"' and note_type = '2' order by createdate desc limit 1 ";
      try{
         DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
         ResultSet rs = db.GetSQL(notesql);
         if(rs.next()){
            retStr = rs.getString("note");
         }
         rs.close();
         }catch (Exception e){
            e.printStackTrace();
         }
      return retStr;
   }



   /*
   First - REC-CODE-IN (3) must be 'N01'
   Second - DATA-CENTRE-NUM (5)
   Third - DATA-CENTRE-SEQNUM (7)
   Fourth - PAYEE-NUM (5)
   Fifth - PRACTITIONER-NUM (5)
   Sixth - NOTE-DATA-TYPE (1)
   Seventh - NOTE-DATA-LINE (400)
   */
   public static String getN01(String dataCenterNum,String dataCenterSeqNum,String payeeNum,String practitionerNum,String noteType,String note){
      Misc misc = new Misc();
      String s = "N01" + misc.forwardZero(dataCenterNum,5)
                       + misc.forwardZero(dataCenterSeqNum, 7)
                       + misc.forwardZero(payeeNum, 5)
                       + misc.forwardZero(practitionerNum, 5)
                       + misc.forwardSpace(noteType,1)
                       + misc.forwardSpace(note,400);
      return s;
   }

   class Note{
      String billingnote_no = null;
      String billingmaster_no = null;
      String createdate  = null;
      String provider_no = null;
      String note = null ;

    /**
     * Getter for property billingnote_no.
     * @return Value of property billingnote_no.
     */
    public java.lang.String getBillingnote_no() {
       return billingnote_no;
    }

    /**
     * Setter for property billingnote_no.
     * @param billingnote_no New value of property billingnote_no.
     */
    public void setBillingnote_no(java.lang.String billingnote_no) {
       this.billingnote_no = billingnote_no;
    }

    /**
     * Getter for property billingmaster_no.
     * @return Value of property billingmaster_no.
     */
    public java.lang.String getBillingmaster_no() {
       return billingmaster_no;
    }

    /**
     * Setter for property billingmaster_no.
     * @param billingmaster_no New value of property billingmaster_no.
     */
    public void setBillingmaster_no(java.lang.String billingmaster_no) {
       this.billingmaster_no = billingmaster_no;
    }

    /**
     * Getter for property createdate.
     * @return Value of property createdate.
     */
    public java.lang.String getCreatedate() {
       return createdate;
    }

    /**
     * Setter for property createdate.
     * @param createdate New value of property createdate.
     */
    public void setCreatedate(java.lang.String createdate) {
       this.createdate = createdate;
    }

    /**
     * Getter for property provider_no.
     * @return Value of property provider_no.
     */
    public java.lang.String getProviderNo() {
       return provider_no;
    }

    /**
     * Setter for property provider_no.
     * @param provider_no New value of property provider_no.
     */
    public void setProviderNo(java.lang.String provider_no) {
       this.provider_no = provider_no;
    }

    /**
     * Getter for property note.
     * @return Value of property note.
     */
    public java.lang.String getNote() {
       return note;
    }

    /**
     * Setter for property note.
     * @param note New value of property note.
     */
    public void setNote(java.lang.String note) {
       this.note = note;
    }

   }


}

