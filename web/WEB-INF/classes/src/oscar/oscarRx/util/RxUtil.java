  
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
package oscar.oscarRx.util;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class RxUtil {
    private static String defaultPattern = "yyyy/MM/dd";
    private static Locale locale = Locale.CANADA;
    
    public static Date StringToDate(String Expression) {
        return StringToDate(Expression, defaultPattern);
    }
    public static Date StringToDate(String Expression, String pattern) {
        try {
            SimpleDateFormat df = new SimpleDateFormat(pattern, locale);
            
            return df.parse(Expression);
        }
        catch (Exception e) {
            return null;
        }
    }
    
    public static String DateToString(Date Expression) {
        return DateToString(Expression, defaultPattern);
    }
    
    public static String DateToString(Date Expression, String pattern) {
        if(Expression!=null) {
            SimpleDateFormat df = new SimpleDateFormat(pattern, locale);
            
            return df.format(Expression);
        }
        else {
            return "";
        }
    }
    
    public static Date Today() {
        return (java.util.GregorianCalendar.getInstance().getTime());
    }
    
    
    public static int BoolToInt(boolean Expression) {
        if (Expression==true) {
            return 1;
        } else {
            return 0;
        }
    }
    
    public static boolean IntToBool(int Expression) {
        return (Expression!=0);
    }
    
    public static String FloatToString(float value) {
        Float f = new Float(value);
        
        java.text.NumberFormat fmt = java.text.NumberFormat.getNumberInstance();
        
        String s = fmt.format(f.doubleValue());
        
        return s;
    }
    
    public static float StringToFloat(String value) {
        return Float.parseFloat(value);
    }
    
    public static Object IIf(boolean Expression, Object TruePart, Object FalsePart) {
        if (Expression==true) {
            return TruePart;
        } else {
            return FalsePart;
        }
    }
    
    public static String joinArray(Object[] array) {
        String ret = "";
        int i;
        for (i=0; i<array.length; i++) {
            ret += "'" + String.valueOf(array[i]) + "'";
            if(i < array.length - 1) {
                ret += ", ";
            }
        }
        
        return ret;
    }
    
    public static String replace(String expression, String searchFor, String replaceWith) {
        if(expression!=null) {
            StringBuffer buf = new StringBuffer(expression);
            
            int pos = -1;
            
            while(true) {
                pos = buf.indexOf(searchFor, pos);
                
                if(pos>-1) {
                    buf.delete(pos, pos+searchFor.length());
                    buf.insert(pos, replaceWith);
                    
                    pos += replaceWith.length();
                }
                else {
                    break;
                }
            }
            
            return buf.toString();
        }
        else {
            return null;
        }
    }
    /**
     * Method for calculating creatinine clearance takes age, weight in kg and CREATININE values an returns Clcr 
     * age must be greater than zero.
     * weight must be greater than zero
     */
    public static int getClcr(int age, double weight, double sCr, boolean female) throws Exception{
      if  ( age < 0){
          throw new Exception("age must be greater than 0");
      }       
      if (weight < 0){
          throw new Exception("weight must be greater than 0");
      }
      if (sCr < 0){
          throw new Exception("sCr must be greater than 0");
      }
      
      double Clcr = (140 - age) * weight  /  ( sCr * 0.8 );
      if(female){
          Clcr = Clcr * 0.85;
      }
      
      return (int) Math.round(Clcr);
  }
}