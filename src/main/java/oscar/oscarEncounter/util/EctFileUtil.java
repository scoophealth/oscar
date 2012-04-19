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


package oscar.oscarEncounter.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.log4j.Logger;
import org.oscarehr.util.MiscUtils;

public class EctFileUtil {
    private static Logger logger=MiscUtils.getLogger(); 
    
    public String[] loadData(String fileName,String path){
        int s = 50;
        String[] c;
        c = new String[s];
        for (int i=0; i < s; i++) {
            c[i] = "_";
        }
        try{
            String filepath =  path + "/"+fileName;
            InputStream is = getClass().getResourceAsStream(filepath);
            //InputStreamReader inputStreamReader = new InputStreamReader(is);
            BufferedReader bufReader = new BufferedReader(new InputStreamReader(is));
            for (int i=0; i<s ;i++) {
                c[i]=bufReader.readLine();
            }
        }catch (IOException ee){
            logger.error("houston, we have a problem: ", ee);
            return c;
        }
        
        return c;
    }
    
    //project home is ignored because it now uses the getResourceAsStream method.
    //Before it used the get absolutePath method. Which would change depending on the name of the web app.
    public String[] loadData(String fileName, String projecthome, String path){
        return loadData(fileName,"/oscar/"+path);
    }
    
}
