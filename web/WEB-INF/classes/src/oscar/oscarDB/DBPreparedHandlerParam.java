package oscar.oscarDB;

import java.sql.Date;


public class DBPreparedHandlerParam {
   private Date dateValue;
   private String stringValue;
   private String paramType;

   public static String PARAM_STRING = "String";
   public static String PARAM_DATE = "Date";

   public DBPreparedHandlerParam(String stringValue){
	   this.stringValue= stringValue;
	   this.dateValue=null;
	   this.paramType=PARAM_STRING;
   }
   
   public DBPreparedHandlerParam(Date dateValue){
	   this.stringValue=null;
	   this.dateValue= dateValue;
	   String xxx=dateValue.toString();
	   this.paramType=PARAM_DATE;
   }

   public Date getDateValue() {
	  return dateValue;
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
