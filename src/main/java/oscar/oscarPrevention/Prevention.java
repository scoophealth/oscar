/**
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
 * This software is published under the GPL GNU General Public License.
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 *
 * This software was written for the
 * Department of Family Medicine
 * McMaster University
 * Hamilton
 * Ontario, Canada
 */


package oscar.oscarPrevention;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.oscarehr.util.MiscUtils;

/**
 *
 * @author Jay Gallagher
 */
public class Prevention {
   private static Logger log = MiscUtils.getLogger();

   String sex;
   String name= null;  // Not really needed but handy for testing
   Hashtable preventionTypes = new Hashtable();
   Date DOB = null;

   Map<String, Object> warnings = new HashMap<String, Object>();

   ArrayList<String> messageList = new ArrayList<String>();
   ArrayList<String> reminder = new ArrayList<String>();

   int ageInMonths = -1;

   public Prevention() {
   }

   public Prevention(String nam,String se,Date birthdate) {
      name = nam;
      sex = se;
      DOB = birthdate;
   }

   public Prevention(String se,Date birthdate) {
      sex = se;
      DOB = birthdate;
   }

   public Prevention(String demographicNo) {

   }

   public void log(String logMessage){
      log.debug(name+" :"+logMessage);
   }

   public void setSex(String s){ sex = s; }
   public String getSex(){ return sex; }
   public java.lang.String getName() { return name; }
   public void setName(java.lang.String name) { this.name = name; }

   public void addWarning(String warn){
      messageList.add(warn);
   }

   public void addWarning(String prevName, String warn) {
        addWarning(warn);
        warnings.put(prevName, warn);
   }

   public ArrayList<String> getWarnings(){
      return messageList;
   }

   @SuppressWarnings("rawtypes")
   public Map getWarningMsgs() {
       return warnings;
   }

   public void addReminder(String warn){
      reminder.add(warn);
   }
   public ArrayList<String> getReminder(){
      return reminder;
   }

   public boolean isMale(){
      boolean retval = false;
      if (sex != null && sex.equals("M")){
         retval = true;
      }
      return retval;
   }

   public boolean isFemale(){
      boolean retval = false;
      if (sex != null && sex.equals("F")){
         retval = true;
      }
      return retval;
   }

   public void addPreventionItem(PreventionItem pItem){
      if (preventionTypes.containsKey(pItem.name)){
         Vector v = (Vector) preventionTypes.get(pItem.name);
         v.add(pItem);
      }else{
         Vector v = new Vector();
         v.add(pItem);
         preventionTypes.put(pItem.name,v);
      }
   }

   public int getAgeInMonths(Date DOB){
	   if(DOB!=null)
		   return getNumMonths(DOB,Calendar.getInstance().getTime());
	   else
		   return 0;
   }

   public int getAgeInMonths(){
      if (ageInMonths == -1){
      ageInMonths = getAgeInMonths(DOB);
      }
      return ageInMonths;
   }

   public int getAgeInYears(){
	   if(DOB !=null)
		   return getNumYears(DOB,Calendar.getInstance().getTime());
	   else
		   return 0;
   }

   public boolean isTodayinDateRange(String startDate,String endDate){
      boolean inRange = false;
      Calendar calendar = Calendar.getInstance();
      Date today = calendar.getTime();
      try {
         DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
         Date startd = formatter.parse(startDate);
         Date endd = formatter.parse(endDate);

         if (today.after(startd) && today.before(endd)){
            inRange = true;
         }
      } catch (ParseException e) {
      }
      return inRange;
   }

   public boolean isLastPreventionWithinRange(String preventionType, String startDate, String endDate){
      boolean withinRange = false;
      Date lastdate = getLastPreventionDate(preventionType);
      try {
         DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
         Date startd = formatter.parse(startDate);
         Date endd = formatter.parse(endDate);

         if ( lastdate != null && lastdate.after(startd) && lastdate.before(endd)){
            withinRange = true;
         }
      } catch (ParseException e) {
         MiscUtils.getLogger().error("Error", e);
      } catch (Exception ex){MiscUtils.getLogger().error("Error", ex);
      }
      return withinRange;
   }

////
   public Date getLastPreventionDate(String preventionType){
      //Still needs to be implemented
      Date lastDate = null;
      Vector vec = (Vector) preventionTypes.get(preventionType);
      if (vec != null){
       PreventionItem p = (PreventionItem)  vec.get(vec.size()-1);  // Get date from return object
       lastDate = p.getDatePreformed();
       log.debug("getting last date preformed of a "+preventionType+" :"+lastDate.toString());
      }

      return lastDate; //
   }

   public boolean isNextDateSet(String preventionType){
      boolean isSet = true;
      Date nextDate = getNextPreventionDate(preventionType);
      log.debug("IS SET WHAT DOES IT HAVE "+nextDate);
      if ( nextDate == null ){
         isSet = false;
      }
      return isSet;
   }
   
   public boolean isNotNextDateSet(String preventionType){
	   return !isNextDateSet(preventionType);
   }

   public boolean isPassedNextDate(String preventionType){
      boolean isPassed = true;
      Date nextDate = getNextPreventionDate(preventionType);
      log.debug(nextDate);
      if (nextDate != null){
         Calendar cal = Calendar.getInstance();
         if (!cal.getTime().after(nextDate)){
            isPassed = false;
         }
      }
      return isPassed;
   }
   
   public boolean isNotPassedNextDate(String preventionType){
	   return !isPassedNextDate(preventionType);
   }

   public Date getNextPreventionDate(String preventionType){
      Date nextDate = null;
      Vector vec = (Vector) preventionTypes.get(preventionType);
      if (vec != null){
       PreventionItem p = (PreventionItem)  vec.get(vec.size()-1);  // Get date from return object
       nextDate = p.getNextDate();
       log.debug("getting next date preformed of a "+preventionType+" :"+nextDate);
      }
      return nextDate; //
   }

   public boolean isPreventionNever(String preventionType){
      boolean ispreventionnever = false;
      Vector vec = (Vector) preventionTypes.get(preventionType);
      if (vec != null){
       PreventionItem p = (PreventionItem)  vec.get(vec.size()-1);  // Get date from return object
       ispreventionnever = p.getNeverVal();
       log.debug("getting never of a "+preventionType+" :"+ispreventionnever);
      }
      return ispreventionnever; //
   }
   
   public boolean isNotPreventionNever(String preventionType){
	   return !isNotPreventionNever(preventionType);
   }


   public boolean isInelligible(String preventionType) {
       boolean isInelligible = false;
       Vector vec = getPreventionData(preventionType);
       PreventionItem p;
       for( int idx = 0; idx < vec.size(); ++idx ) {
           p = (PreventionItem)vec.get(idx);
           if( p.isInelligible() ) {
               isInelligible = true;
               break;
           }
       }

       return isInelligible;
   }

   public boolean isNotInelligible(String preventionType){
	   return !isInelligible(preventionType);
   }



   public int getHowManyMonthsSinceLast(String preventionType){
      int retval = -1;
      try{
         retval = getNumMonths(getLastPreventionDate(preventionType),Calendar.getInstance().getTime());
      }catch(Exception e){
         log.debug("Probably no record of this prevention");
         log.debug(e.getMessage(),e);
         retval = -1;
      }
      return retval;
   }
   public int getHowManyDaysSinceLast(String preventionType){
      return getNumDays(getLastPreventionDate(preventionType),Calendar.getInstance().getTime());
   }
   public int getNumberOfPreventionType(String preventionType){
      int retval = 0;
      Vector vec = (Vector) preventionTypes.get(preventionType);
      if (vec != null){
         retval = vec.size();
      }
      return retval;
   }

   public Vector getPreventionData(String preventionType){
       Vector a =  (Vector) preventionTypes.get(preventionType);
       if ( a == null ){
           a = new Vector();
       }
       return a;
   }

   public int getAgeInMonthsLastPreventionTypeGiven(String preventionType){
      return getNumMonths(DOB,getLastPreventionDate(preventionType));
   }

   private String getStrDate(Date d){
       if (d == null){
           return null;
       }
       return d.toString();
   }

   private int getNumMonths(Date dStart, Date dEnd) {
	    if(dStart==null || dEnd==null)
	    	return -1;
        int i = 0;
        log.debug("Getting the number of months between "+getStrDate(dStart)+ " and "+getStrDate(dEnd) );
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dStart);
        while (calendar.getTime().before(dEnd) || calendar.getTime().equals(dEnd)) {
            calendar.add(Calendar.MONTH, 1);
            i++;
        }
        i--;
        if (i < 0) { i = 0; }
        return i;
   }

   private int getNumMonths(Calendar dStart,Calendar dEnd) {
        return getNumMonths(dStart.getTime(),dEnd.getTime());
    }

   private int getNumYears(Date dStart, Date dEnd) {
	   if(dStart==null || dEnd==null) return -1;
        int i = 0;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dStart);
        while (calendar.getTime().before(dEnd) || calendar.getTime().equals(dEnd)) {
            calendar.add(Calendar.MONTH, 12);
            i++;
        }
        i--;
        if (i < 0) { i = 0; }
        return i;
   }

   private int getNumDays(Date dStart, Date dEnd) {
        long diffDays = -1;
        if(dStart == null || dEnd==null) return -1;
        try{
        long timeDiff = dStart.getTime() - dEnd.getTime();
        diffDays = timeDiff/(24*60*60*1000);
        }catch(Exception e){}
        return new Long(diffDays).intValue();
   }

   public static void main(String[] args){
      Calendar date1 = new GregorianCalendar(1980, Calendar.JANUARY, 31);
      Date d1 = date1.getTime();
      Calendar date2= new GregorianCalendar(1980, Calendar.MARCH, 1);
      Date d2 = date2.getTime();
      Prevention p = new Prevention();
      log.debug(p.getNumMonths(date1,date2));

   }

   /**
    * Getter for property DOB.
    * @return Value of property DOB.
    */
   public java.util.Date getDOB() {
      return DOB;
   }

   /**
    * Setter for property DOB.
    * @param DOB New value of property DOB.
    */
   public void setDOB(java.util.Date DOB) {
      this.DOB = DOB;
   }

}
