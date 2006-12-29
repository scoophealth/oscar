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
 * McMaster University 
 * Hamilton 
 * Ontario, Canada 
 */
package oscar.util;

public class StringBufferUtils {
    static public int indexOfIgnoreCase(StringBuffer strbuf, String target, int start) {
        String searchStr = strbuf.toString().toLowerCase();
        String lowerTarget = target.toLowerCase();
        return searchStr.indexOf(lowerTarget, start);
    }
    /*
    static public int indexOfIgnoreCase(StringBuffer strbuf, String target, int start) {
        //resembles the indexOfIgnoreCase member function of the String class
        int pointer = 0;
        for (int i=start; i<strbuf.length(); i++) {
            if (!sameIgnoreCase(strbuf.charAt(i), target.charAt(0))) {
                if (i == strbuf.length()-1) return -1;
                continue;
            }
            pointer = i;
            i = strbuf.length();
        }
        for (int i=0; i<target.length(); i++) {
            if ((pointer+i < strbuf.length()) && (!sameIgnoreCase(target.charAt(i), strbuf.charAt(pointer+i)))) {
                pointer++;
                return indexOfIgnoreCase(strbuf, target, pointer);
            }
            else if (i == target.length()-1) {
                return pointer;
            }
        }
        return -1;  //never reached, but required by the compiler
    }
    
    //static public int equalsIgnoreCase(StringBuffer )
    
    static private boolean sameIgnoreCase(char char1, char char2) {
        //used as comparrison by the indexOfIgnoreCase method
        int char1a = char1;
        int char1b;
        if ((char1a <= 90) && (char1a >= 65)) char1b = char1a + 32;
        else if ((char1a >= 97) && (char1a <= 122)) char1b = char1a - 32;
        else char1b = char1a;
        if (((char) char1a == char2) || ((char) char1b == char2)) {
            return(true);
        }
        return(false);
    }*/
}
