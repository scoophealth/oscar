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


package oscar.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.oscarehr.util.MiscUtils;

public class ReadLocalFile {
    public static String getStringFromFile(String fileName) {
        StringBuilder fileStr = new StringBuilder();
        String lineStr = new String();
        try {
            BufferedReader in = new BufferedReader(new FileReader(fileName));
            while ((lineStr = in.readLine()) != null) {
                fileStr.append("\n" + lineStr);
            }
            in.close();
        } catch (IOException e) {
        }
        return(fileStr.toString());
    }
    public static void writeStreamFromFile(String fileName, javax.servlet.jsp.JspWriter outWriter) {
        String lineStr = "";
        try {
            BufferedReader in = new BufferedReader(new FileReader(fileName));
            while ((lineStr = in.readLine()) != null) {
                outWriter.write(lineStr + "\n");
                outWriter.flush();
            }
        } catch (IOException e) {
            MiscUtils.getLogger().error("Error", e);
        }
        //return outWriter;
    }
}
