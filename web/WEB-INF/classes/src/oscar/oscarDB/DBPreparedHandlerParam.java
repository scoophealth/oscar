package oscar.oscarDB;

import java.sql.Date;
import java.sql.Timestamp;


public class DBPreparedHandlerParam {
   private Date dateValue;
   private String stringValue;
   private int intValue;
   private String paramType;
   private Timestamp timestampValue;

   public static String PARAM_STRING = "String";
   public static String PARAM_DATE = "Date";
   public static String PARAM_INT = "Int";
   public static String PARAM_TIMESTAMP = "Timestamp";
   
   public DBPreparedHandlerParam(String stringValue){
	   this.intValue = 0;
	   this.stringValue= stringValue;
	   this.dateValue=null;
           this.timestampValue = null;
	   this.paramType=PARAM_STRING;
   }
   
   public DBPreparedHandlerParam(Date dateValue){
	   this.intValue = 0;
	   this.stringValue=null;
	   this.dateValue= dateValue;
           this.timestampValue = null;
           this.paramType=PARAM_DATE;
   }
   
    public DBPreparedHandlerParam(Timestamp dateValue){
	   this.intValue = 0;
	   this.stringValue=null;
	   this.dateValue= null;
           this.timestampValue = dateValue;
	   this.paramType=PARAM_TIMESTAMP;
   }
   

   public DBPreparedHandlerParam(int intValue){
	   this.intValue= intValue;
	   this.stringValue = "";
	   this.dateValue=null;
           this.timestampValue = null;
	   this.paramType=PARAM_INT;
   }

   public Date getDateValue() {
//          System.out.println("DBPreparedHandlerParam.getDateValue "+dateValue);
	  return dateValue;
   }

   
   public Timestamp getTimestampValue() {
//          System.out.println("DBPreparedHandlerParam.getTimestampValue "+timestampValue);
	  return this.timestampValue;
   }

   
   public int getIntValue() {
		  return intValue;
   }

//   public void setDateValue(Date dateValue) {
//	  this.dateValue = dateValue;
//   }

   public String getParamType() {
	  return paramType;
   }

//   public void setParamType(String paramType) {
//	  this.paramType = paramType;
//   }

   public String getStringValue() {
	  return stringValue;
   }

//   public void setStringValue(String stringValue) {
//	  this.stringValue = stringValue;
//   }
   
}
